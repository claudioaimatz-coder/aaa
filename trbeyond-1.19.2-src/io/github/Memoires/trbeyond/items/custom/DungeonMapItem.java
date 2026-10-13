package io.github.Memoires.trbeyond.items.custom;

import com.github.manasmods.tensura.util.TensuraRarity;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DungeonMapItem extends Item {
   private static final Component failedMessage = Component.literal("Whatever this map points to is too far away...");

   public DungeonMapItem() {
      super(new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).rarity(TensuraRarity.UNIQUE).stacksTo(1));
   }

   public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
      pTooltipComponents.add(Component.translatable("trbeyond.tooltip.dungeon_map").withStyle(ChatFormatting.DARK_AQUA));
   }

   @NotNull
   public InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
         ServerLevel serverLevel = serverPlayer.getLevel();
         BlockPos foundPos = findStructure(serverLevel, serverPlayer.blockPosition());
         serverPlayer.sendSystemMessage(
            (Component)(foundPos == null
               ? failedMessage
               : Component.literal(String.format("This map shows a large structure located at: X: %s, Z: %s", foundPos.getX(), foundPos.getZ())))
         );
      }

      return InteractionResultHolder.success(stack);
   }

   public static BlockPos findStructure(ServerLevel level, BlockPos pos) {
      ResourceLocation tagLocation = new ResourceLocation("trbeyond", "colosseum");
      TagKey<Structure> structureTag = TagKey.create(BuiltinRegistries.STRUCTURES.key(), tagLocation);

      for (Holder<Structure> holder : level.registryAccess().registryOrThrow(BuiltinRegistries.STRUCTURES.key()).getTagOrEmpty(structureTag)) {
         if (holder.unwrapKey().isPresent()) {
            TagKey<Structure> key = TagKey.create(BuiltinRegistries.STRUCTURES.key(), ((ResourceKey)holder.unwrapKey().get()).location());
            BlockPos foundPos = level.findNearestMapStructure(key, pos, 10000, false);
            if (foundPos != null) {
               return foundPos;
            }
         }
      }

      return null;
   }

   public static Component getFailedMessage() {
      return failedMessage;
   }
}
