package io.github.Memoires.trbeyond.items.food;

import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties.Builder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public class TeaFoodItem extends Item {
   public TeaFoodItem() {
      super(new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).food(new Builder().nutrition(0).saturationMod(0.0F).build()));
   }

   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.DRINK;
   }

   public int getUseDuration(ItemStack stack) {
      return 32;
   }

   public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
      ItemStack result = super.finishUsingItem(stack, level, entity);
      if (!level.isClientSide) {
         List<MobEffect> vanillaNegativeEffects = List.of(
            MobEffects.MOVEMENT_SLOWDOWN,
            MobEffects.DIG_SLOWDOWN,
            MobEffects.HARM,
            MobEffects.CONFUSION,
            MobEffects.BLINDNESS,
            MobEffects.HUNGER,
            MobEffects.WEAKNESS,
            MobEffects.POISON,
            MobEffects.WITHER,
            MobEffects.UNLUCK
         );
         List<MobEffect> effectsToRemove = new ArrayList<>();

         for (Entry<MobEffect, MobEffectInstance> entry : entity.getActiveEffectsMap().entrySet()) {
            MobEffect effect = entry.getKey();
            MobEffectInstance instance = entry.getValue();
            if (vanillaNegativeEffects.contains(effect)) {
               int newAmplifier = instance.getAmplifier() - 1;
               if (newAmplifier < 0) {
                  effectsToRemove.add(effect);
               } else {
                  entity.removeEffect(effect);
                  entity.addEffect(
                     new MobEffectInstance(effect, instance.getDuration(), newAmplifier, instance.isAmbient(), instance.isVisible(), instance.showIcon())
                  );
               }
            }
         }

         for (MobEffect effect : effectsToRemove) {
            entity.removeEffect(effect);
         }
      }

      return result;
   }
}
