package io.github.Memoires.trbeyond.items.food;

import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import net.minecraft.world.food.FoodProperties.Builder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;

public class HoneyFoodItem extends Item {
   public HoneyFoodItem() {
      super(new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).food(new Builder().nutrition(20).saturationMod(1.0F).build()));
   }

   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.EAT;
   }

   public int getUseDuration(ItemStack stack) {
      return 32;
   }
}
