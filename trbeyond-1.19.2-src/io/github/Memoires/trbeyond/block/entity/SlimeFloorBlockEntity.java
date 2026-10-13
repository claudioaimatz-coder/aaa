package io.github.Memoires.trbeyond.block.entity;

import io.github.Memoires.trbeyond.block.SlimeFloorBlock;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class SlimeFloorBlockEntity extends BlockEntity implements IAnimatable {
   public int animationTick;
   public int tickCount;
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

   public SlimeFloorBlockEntity(BlockPos pos, BlockState state) {
      super((BlockEntityType)BeyondBlockEntities.SLIME_FLOOR.get(), pos, state);
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

   public static void tick(Level level, BlockPos pos, BlockState state, SlimeFloorBlockEntity pEntity) {
      pEntity.tickCount++;
      if (!level.isClientSide()) {
         if ((Integer)state.getValue(SlimeFloorBlock.STAGE) == 1) {
            pEntity.animationTick++;
            if (pEntity.getAnimationTick() >= 34) {
               pEntity.setAnimationTick(0);
               level.setBlockAndUpdate(pos, (BlockState)state.setValue(SlimeFloorBlock.STAGE, 2));
            }
         } else if ((Integer)state.getValue(SlimeFloorBlock.STAGE) == 2) {
            pEntity.animationTick++;
            if (pEntity.getAnimationTick() >= 1200) {
               pEntity.setAnimationTick(0);
               level.setBlockAndUpdate(pos, (BlockState)state.setValue(SlimeFloorBlock.STAGE, 3));
            }
         } else if ((Integer)state.getValue(SlimeFloorBlock.STAGE) == 3) {
            pEntity.animationTick++;
            if (pEntity.getAnimationTick() >= 69) {
               pEntity.setAnimationTick(0);
               level.setBlockAndUpdate(pos, (BlockState)state.setValue(SlimeFloorBlock.STAGE, 0));
            }
         }
      }
   }

   private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      AnimationController<E> controller = event.getController();
      controller.transitionLengthTicks = 0.0;
      if ((Integer)((BlockEntity)event.getAnimatable()).getBlockState().getValue(SlimeFloorBlock.STAGE) == 1) {
         String open = "animation.slime_floor.open_" + this.getBlockState().getValue(SlimeFloorBlock.TYPE);
         controller.setAnimation(new AnimationBuilder().addAnimation(open, EDefaultLoopTypes.LOOP));
         controller.setAnimationSpeed(2.0);
      } else if ((Integer)((BlockEntity)event.getAnimatable()).getBlockState().getValue(SlimeFloorBlock.STAGE) == 2) {
         controller.setAnimation(new AnimationBuilder().addAnimation("animation.slime_floor.exposed", EDefaultLoopTypes.LOOP));
         controller.setAnimationSpeed(1.0);
      } else if ((Integer)((BlockEntity)event.getAnimatable()).getBlockState().getValue(SlimeFloorBlock.STAGE) == 3) {
         String open = "animation.slime_floor.close_" + this.getBlockState().getValue(SlimeFloorBlock.TYPE);
         controller.setAnimation(new AnimationBuilder().addAnimation(open, EDefaultLoopTypes.LOOP));
         controller.setAnimationSpeed(1.0);
      } else {
         controller.setAnimation(new AnimationBuilder().addAnimation("animation.slime_floor.hidden", EDefaultLoopTypes.LOOP));
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
