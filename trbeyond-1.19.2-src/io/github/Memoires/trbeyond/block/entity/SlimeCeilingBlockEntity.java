package io.github.Memoires.trbeyond.block.entity;

import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.Memoires.trbeyond.block.SlimeCeilingBlock;
import io.github.Memoires.trbeyond.items.armor.SerpentArmorItem;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class SlimeCeilingBlockEntity extends BlockEntity implements IAnimatable {
   public int animationTick;
   public int tickCount;
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

   public SlimeCeilingBlockEntity(BlockPos pos, BlockState state) {
      super((BlockEntityType)BeyondBlockEntities.SLIME_CEILING.get(), pos, state);
   }

   protected void saveAdditional(CompoundTag pTag) {
      super.saveAdditional(pTag);
      pTag.putInt("animationTick", this.animationTick);
   }

   public void load(CompoundTag pTag) {
      super.load(pTag);
      this.animationTick = pTag.getInt("animationTick");
   }

   public ClientboundBlockEntityDataPacket getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   public CompoundTag getUpdateTag() {
      CompoundTag tag = super.getUpdateTag();
      this.saveAdditional(tag);
      return tag;
   }

   public static void tick(Level level, BlockPos pos, BlockState state, SlimeCeilingBlockEntity pEntity) {
      pEntity.tickCount++;
      if (!level.isClientSide()) {
         if ((Integer)state.getValue(SlimeCeilingBlock.STAGE) == 0) {
            pEntity.animationTick++;
            if (pEntity.animationTick % 20 == 0) {
               AABB aabb = new AABB(pos).inflate(0.5, 0.0, 0.5).expandTowards(0.0, -6.0, 0.0);
               if (!level.getEntitiesOfClass(LivingEntity.class, aabb, entityx -> (!(entityx instanceof Player player) || !player.isCreative()) && !entityx.isSpectator())
                  .isEmpty()) {
                  pEntity.setAnimationTick(0);
                  SlimeCeilingBlock.activate(state, level, pos);
               }
            }
         } else if ((Integer)state.getValue(SlimeCeilingBlock.STAGE) == 1) {
            pEntity.animationTick++;
            if (pEntity.getAnimationTick() >= 29) {
               pEntity.setAnimationTick(0);
               level.setBlockAndUpdate(pos, (BlockState)state.setValue(SlimeCeilingBlock.STAGE, 2));
            }
         } else if ((Integer)state.getValue(SlimeCeilingBlock.STAGE) == 2) {
            pEntity.animationTick++;
            if (pEntity.animationTick % 20 == 0) {
               AABB aabb = new AABB(pos).inflate(0.5, 0.0, 0.5).expandTowards(0.0, -7.0, 0.0);

               for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, aabb)) {
                  entity.hurt(TensuraDamageSources.CORROSION, 25.0F);

                  for (EquipmentSlot slot : EquipmentSlot.values()) {
                     ItemStack slotStack = entity.getItemBySlot(slot);
                     if (!(slotStack.getItem() instanceof SerpentArmorItem)) {
                        slotStack.hurtAndBreak(5, entity, player -> player.broadcastBreakEvent(slot));
                     }
                  }

                  level.playSound(null, entity.blockPosition(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.3F, 0.6F + level.random.nextFloat() * 0.4F);
               }
            }

            if (pEntity.getAnimationTick() >= 1200) {
               pEntity.setAnimationTick(0);
               level.setBlockAndUpdate(pos, (BlockState)state.setValue(SlimeCeilingBlock.STAGE, 3));
            }
         } else if ((Integer)state.getValue(SlimeCeilingBlock.STAGE) == 3) {
            pEntity.animationTick++;
            if (pEntity.getAnimationTick() >= 59) {
               pEntity.setAnimationTick(0);
               level.setBlockAndUpdate(pos, (BlockState)state.setValue(SlimeCeilingBlock.STAGE, 0));
            }
         }
      }
   }

   private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      AnimationController<E> controller = event.getController();
      controller.transitionLengthTicks = 0.0;
      if ((Integer)((BlockEntity)event.getAnimatable()).getBlockState().getValue(SlimeCeilingBlock.STAGE) == 1) {
         String start = "animation.slime_ceiling.start_" + this.getBlockState().getValue(SlimeCeilingBlock.TYPE);
         controller.setAnimation(new AnimationBuilder().addAnimation(start, EDefaultLoopTypes.LOOP));
         controller.setAnimationSpeed(2.0);
      } else if ((Integer)((BlockEntity)event.getAnimatable()).getBlockState().getValue(SlimeCeilingBlock.STAGE) == 2) {
         String loop = "animation.slime_ceiling.loop_" + this.getBlockState().getValue(SlimeCeilingBlock.TYPE);
         controller.setAnimation(new AnimationBuilder().addAnimation(loop, EDefaultLoopTypes.LOOP));
      } else if ((Integer)((BlockEntity)event.getAnimatable()).getBlockState().getValue(SlimeCeilingBlock.STAGE) == 3) {
         String end = "animation.slime_ceiling.end_" + this.getBlockState().getValue(SlimeCeilingBlock.TYPE);
         controller.setAnimation(new AnimationBuilder().addAnimation(end, EDefaultLoopTypes.LOOP));
         controller.setAnimationSpeed(1.0);
      } else {
         controller.setAnimation(new AnimationBuilder().addAnimation("animation.slime_ceiling.hidden", EDefaultLoopTypes.LOOP));
         controller.setAnimationSpeed(1.0);
      }

      return PlayState.CONTINUE;
   }

   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController(this, "controller", 0.0F, this::predicate));
   }

   public void setAnimationTick(int animationTick) {
      this.animationTick = animationTick;
   }

   public int getAnimationTick() {
      return this.animationTick;
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }
}
