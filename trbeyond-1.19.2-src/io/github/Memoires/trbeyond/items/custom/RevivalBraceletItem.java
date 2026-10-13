package io.github.Memoires.trbeyond.items.custom;

import com.github.manasmods.tensura.util.TensuraRarity;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RevivalBraceletItem extends Item {
   public RevivalBraceletItem() {
      super(new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).rarity(TensuraRarity.UNIQUE).stacksTo(1));
   }

   public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
      pTooltipComponents.add(Component.translatable("trbeyond.tooltip.items_dungeon_only").withStyle(ChatFormatting.RED));
      pTooltipComponents.add(Component.translatable("trbeyond.tooltip.revival_bracelet").withStyle(ChatFormatting.DARK_AQUA));
   }
}
