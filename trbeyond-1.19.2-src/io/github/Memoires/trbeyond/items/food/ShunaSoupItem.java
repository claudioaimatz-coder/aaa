package io.github.Memoires.trbeyond.items.food;

import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import java.util.ArrayList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties.Builder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public class ShunaSoupItem extends Item {
   public ShunaSoupItem() {
      super(new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).food(new Builder().nutrition(20).saturationMod(1.0F).build()));
   }

   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.EAT;
   }

   public int getUseDuration(ItemStack stack) {
      return 32;
   }

   public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
      ItemStack result = super.finishUsingItem(stack, level, entity);
      if (!level.isClientSide) {
         System.out.println("[ShunaSoupItem] Consumed by: " + entity.getName().getString());

         for (MobEffectInstance effect : new ArrayList(entity.getActiveEffects())) {
            ResourceLocation effectId = Registry.MOB_EFFECT.getKey(effect.getEffect());
            System.out.println("[ShunaSoupItem] Checking effect: " + (effectId != null ? effectId.toString() : "null") + " " + effect.getEffect().getCategory());
            if (effectId != null && effectId.getNamespace().equals("tensura")) {
               System.out.println("[ShunaSoupItem] Skipping effect from 'tensura' namespace: " + effectId);
            } else if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
               System.out.println("[ShunaSoupItem] Removing harmful effect: " + (effectId != null ? effectId : effect.getDescriptionId()));
               entity.removeEffect(effect.getEffect());
            }
         }
      }

      return result;
   }
}
