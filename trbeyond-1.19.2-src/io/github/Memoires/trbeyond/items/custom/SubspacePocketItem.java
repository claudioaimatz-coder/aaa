package io.github.Memoires.trbeyond.items.custom;

import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.skill.extra.ShadowMotionSkill;
import com.github.manasmods.tensura.api.entity.subclass.IElementalSpirit;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.data.TensuraTags.EntityTypes;
import com.github.manasmods.tensura.item.custom.ShadowStorageItem;
import com.github.manasmods.tensura.util.TensuraRarity;
import io.github.Memoires.trbeyond.client.screen.SubspaceContainerMenu;
import io.github.Memoires.trbeyond.container.SubspacePocketContainer;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.registry.items.BeyondItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class SubspacePocketItem extends Item {
   public int storageCapacity;
   public int maxStackSize;
   public int subordinateCapacity;

   public SubspacePocketItem(int storageCapacity, int maxStackSize, int subordinateCapacity) {
      super(new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).rarity(TensuraRarity.UNIQUE).stacksTo(1));
      this.storageCapacity = storageCapacity;
      this.maxStackSize = maxStackSize;
      this.subordinateCapacity = subordinateCapacity;
   }

   public boolean canFitInsideContainerItems() {
      return false;
   }

   @NotNull
   public InteractionResultHolder<ItemStack> use(Level level, Player pPlayer, InteractionHand hand) {
      ItemStack stack = pPlayer.getItemInHand(hand);
      if (!level.isClientSide && stack.getItem() instanceof SubspacePocketItem && !pPlayer.isCrouching()) {
         NetworkHooks.openScreen(
            (ServerPlayer)pPlayer,
            new SimpleMenuProvider((windowId, playerInventory, player) -> new SubspaceContainerMenu(windowId, playerInventory, player, stack), stack.getDisplayName()),
            buffer -> buffer.writeItem(stack)
         );
      }

      return InteractionResultHolder.pass(stack);
   }

   public boolean onDroppedByPlayer(ItemStack item, Player player) {
      if (player.hasContainerOpen() && player.containerMenu instanceof SubspaceContainerMenu subspaceContainerMenu && subspaceContainerMenu.isThisContainerItem(item)) {
         player.closeContainer();
      }

      return true;
   }

   @NotNull
   public InteractionResult useOn(UseOnContext pContext) {
      Player player = pContext.getPlayer();
      Level level = pContext.getLevel();
      if (level instanceof ServerLevel serverLevel && player != null && player.isCrouching() && level.mayInteract(player, pContext.getClickedPos())) {
         ItemStack stack = pContext.getItemInHand();
         SubspacePocketContainer container = new SubspacePocketContainer(this.storageCapacity, this.maxStackSize, this.subordinateCapacity);
         container.fromTag(stack.getOrCreateTag().getList("SpatialStorage", 10));
         int subordinateIndex = container.getFirstSubordinateItemIndex();
         if (subordinateIndex != -1) {
            ItemStack shadow = container.getSubordinateItem(subordinateIndex);
            if (shadow.getItem() instanceof ShadowStorageItem shadowStorageItem) {
               shadowStorageItem.checkExtraContent(player, serverLevel, shadow, pContext.getClickedPos().relative(pContext.getClickedFace()));
               this.releaseCreatureEffects(serverLevel, pContext.getClickedPos().relative(pContext.getClickedFace(), 2));
            }

            container.removeSubordinateItem(subordinateIndex);
            stack.getOrCreateTag().put("SpatialStorage", container.createTag());
            player.getInventory().setChanged();
         }
      }

      return InteractionResult.PASS;
   }

   @NotNull
   public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
      if (!pPlayer.getLevel().isClientSide() && pPlayer.isCrouching() && canStoreEntity(pInteractionTarget, pPlayer)) {
         SubspacePocketContainer container = new SubspacePocketContainer(this.storageCapacity, this.maxStackSize, this.subordinateCapacity);
         container.fromTag(pStack.getOrCreateTag().getList("SpatialStorage", 10));
         if (container.canStoreSubordinate()) {
            ItemStack shadow = this.createShadowItem(pInteractionTarget);
            container.storeSubordinate(shadow);
            pStack.getOrCreateTag().put("SpatialStorage", container.createTag());
            this.storeCreatureEffects(pInteractionTarget);
            pInteractionTarget.discard();
            pPlayer.getInventory().setChanged();
         }
      }

      return InteractionResult.CONSUME;
   }

   private ItemStack createShadowItem(LivingEntity entity) {
      ItemStack shadow = new ItemStack((ItemLike)BeyondItems.STORED_SUBORDINATE.get());
      shadow.setHoverName(Component.literal(entity.getName().getString()).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
      CompoundTag compound = shadow.getOrCreateTag();
      compound.put("ShadowData", entity.serializeNBT());
      compound.putDouble("ShadowEP", TensuraEPCapability.getEP(entity));
      compound.putDouble("ShadowHP", (double)entity.getHealth());
      compound.putDouble("ShadowSHP", TensuraEPCapability.getSpiritualHealth(entity));
      compound.putString("EntityType", String.valueOf(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType())));
      return shadow;
   }

   private static boolean canStoreEntity(LivingEntity target, LivingEntity user) {
      if (target instanceof Player) {
         return false;
      } else if (!SkillHelper.isSubordinate(user, target)) {
         return false;
      } else {
         if (target instanceof IElementalSpirit spirit && spirit.getSummoningTick() > 0) {
            return false;
         }

         return target.getType().is(EntityTypes.MONSTER) ? true : !ShadowMotionSkill.shouldConsumeAir(target);
      }
   }

   private void storeCreatureEffects(LivingEntity entity) {
      ServerLevel level = (ServerLevel)entity.getLevel();
      level.playSound(null, entity.getOnPos(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
      Vec3 boundingBoxCenter = entity.getBoundingBox().getCenter();
      level.sendParticles(ParticleTypes.ENCHANT, boundingBoxCenter.x(), boundingBoxCenter.y(), boundingBoxCenter.z(), 100, 1.5, 1.5, 1.5, 0.0);
   }

   private void releaseCreatureEffects(ServerLevel level, BlockPos blockPos) {
      level.playSound(null, blockPos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 1.0F);
      level.playSound(null, blockPos, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
      level.playSound(null, blockPos, SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM, SoundSource.PLAYERS, 1.0F, 1.0F);
      level.sendParticles(ParticleTypes.LARGE_SMOKE, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 100, 1.5, 1.5, 1.5, 0.0);
   }
}
