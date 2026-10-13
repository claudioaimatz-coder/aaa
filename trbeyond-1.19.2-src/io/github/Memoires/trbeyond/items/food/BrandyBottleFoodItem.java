package io.github.Memoires.trbeyond.items.food;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.capability.SkillStorage;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties.Builder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public class BrandyBottleFoodItem extends Item {
   public BrandyBottleFoodItem() {
      super(
         new Properties()
            .tab(BeyondItemGroup.BEYOND_ITEMS)
            .food(new Builder().nutrition(20).saturationMod(1.0F).effect(() -> new MobEffectInstance(MobEffects.SATURATION, 2400, 0), 1.0F).alwaysEat().build())
            .durability(10)
      );
   }

   public boolean isDamageable(ItemStack stack) {
      return true;
   }

   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.DRINK;
   }

   public int getUseDuration(ItemStack stack) {
      return 32;
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      player.startUsingItem(hand);
      return InteractionResultHolder.consume(stack);
   }

   public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
      if (entity instanceof Player player) {
         player.getFoodData().eat(this.getFoodProperties().getNutrition(), this.getFoodProperties().getSaturationModifier());
         if (!level.isClientSide) {
            boolean hasPoisonProtection = false;
            SkillStorage storage = SkillAPI.getSkillsFrom(player);

            for (ManasSkillInstance instance : storage.getLearnedSkills()) {
               MutableComponent name = instance.getSkill().getName();
               if (name.getContents() instanceof TranslatableContents contents) {
                  String key = contents.getKey();
                  if ((key.equals("tensura.skill.poison_nullification") || key.equals("tensura.skill.poison_resistance"))
                     && instance.getMastery() >= 0
                     && instance.isToggled()) {
                     hasPoisonProtection = true;
                     break;
                  }
               }
            }

            if (!hasPoisonProtection) {
               MobEffectInstance currentSlowness = player.getEffect(MobEffects.MOVEMENT_SLOWDOWN);
               this.stackEffect(player, currentSlowness, MobEffects.MOVEMENT_SLOWDOWN, 600);
               MobEffectInstance currentInsanity = player.getEffect((MobEffect)TensuraMobEffects.INSANITY.get());
               this.stackEffect(player, currentInsanity, (MobEffect)TensuraMobEffects.INSANITY.get(), 600);
            }

            MobEffectInstance currentAbsorption = player.getEffect(MobEffects.ABSORPTION);
            this.stackEffect(player, currentAbsorption, MobEffects.ABSORPTION, 1200);
         }

         stack.hurt(1, player.getRandom(), player instanceof ServerPlayer ? (ServerPlayer)player : null);
         if (stack.getDamageValue() >= stack.getMaxDamage()) {
            stack.shrink(1);
         }
      }

      return stack;
   }

   int stackEffect(Player p, MobEffectInstance current, MobEffect effect, int durationTicks) {
      int amplifier = current != null ? current.getAmplifier() + 1 : 0;
      amplifier = Math.min(amplifier, 100);
      p.addEffect(new MobEffectInstance(effect, durationTicks, amplifier));
      return amplifier;
   }
}
