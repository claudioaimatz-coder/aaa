package io.github.Memoires.trbeyond.items.food;

import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties.Builder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;

public class GyozaFoodItem extends Item {
   public GyozaFoodItem() {
      super(
         new Properties()
            .tab(BeyondItemGroup.BEYOND_ITEMS)
            .food(new Builder().nutrition(20).saturationMod(1.0F).effect(() -> new MobEffectInstance(MobEffects.SATURATION, 2400, 0), 1.0F).alwaysEat().build())
      );
   }

   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.EAT;
   }

   public int getUseDuration(ItemStack stack) {
      return 32;
   }
}
