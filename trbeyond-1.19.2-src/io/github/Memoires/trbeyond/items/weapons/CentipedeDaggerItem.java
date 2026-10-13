package io.github.Memoires.trbeyond.items.weapons;

import com.github.manasmods.tensura.item.TensuraToolTiers;
import com.github.manasmods.tensura.item.templates.custom.SimpleShortSwordItem;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.items.TensuraMobDropItems;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item.Properties;

public class CentipedeDaggerItem extends SimpleShortSwordItem {
   private final CentipedeDaggerItem.Grade grade;

   public CentipedeDaggerItem(CentipedeDaggerItem.Grade grade) {
      super(
         grade.getMaterial(),
         grade.getDamage(),
         -2.0F,
         -0.75,
         20.0,
         0.5,
         new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).defaultDurability(150).rarity(grade.getRarity())
      );
      this.grade = grade;
   }

   public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
      boolean canAttack = super.hurtEnemy(pStack, pTarget, pAttacker);
      if (canAttack) {
         long currentTime = pAttacker.level.getGameTime();
         long lastAttackTime = pStack.getOrCreateTag().getLong("LastParalysisTime");
         int cooldown = pStack.getOrCreateTag().getBoolean("MaxParalysisReached") ? this.grade.maxParalysisCooldown : this.grade.cooldown;
         if (currentTime - lastAttackTime >= (long)cooldown) {
            MobEffectInstance existingParalysis = pTarget.getEffect((MobEffect)TensuraMobEffects.PARALYSIS.get());
            if (existingParalysis != null && existingParalysis.getAmplifier() >= this.grade.maxParalysisLevel - 1) {
               pStack.getOrCreateTag().putBoolean("MaxParalysisReached", true);
            } else {
               pStack.getOrCreateTag().putBoolean("MaxParalysisReached", false);
               pTarget.addEffect(
                  new MobEffectInstance(
                     (MobEffect)TensuraMobEffects.PARALYSIS.get(),
                     this.grade.paralysisDuration,
                     existingParalysis != null ? existingParalysis.getAmplifier() + 1 : 0
                  ),
                  pAttacker
               );
            }

            pStack.getOrCreateTag().putLong("LastParalysisTime", currentTime);
         }
      }

      return canAttack;
   }

   public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
      return pRepair.is((Item)TensuraMobDropItems.CENTIPEDE_STINGER.get());
   }

   public static enum Grade {
      SPECIAL(TensuraToolTiers.LOW_MAGISTEEL, 4, BeyondRarity.SPECIAL, 140, 2, 100, 600),
      RARE(TensuraToolTiers.HIGH_MAGISTEEL, 7, BeyondRarity.RARE, 140, 2, 100, 600),
      UNIQUE(TensuraToolTiers.PURE_MAGISTEEL, 5, BeyondRarity.UNIQUE, 140, 3, 100, 600),
      LEGEND(TensuraToolTiers.ADAMANTITE, 11, BeyondRarity.LEGEND, 140, 6, 100, 600);

      private final Tier material;
      private final int damage;
      private final Rarity rarity;
      private final int paralysisDuration;
      private final int maxParalysisLevel;
      private final int cooldown;
      private final int maxParalysisCooldown;

      public Tier getMaterial() {
         return this.material;
      }

      public int getDamage() {
         return this.damage;
      }

      public Rarity getRarity() {
         return this.rarity;
      }

      public int getParalysisDuration() {
         return this.paralysisDuration;
      }

      public int getMaxParalysisLevel() {
         return this.maxParalysisLevel;
      }

      public int getCooldown() {
         return this.cooldown;
      }

      public int getMaxParalysisCooldown() {
         return this.maxParalysisCooldown;
      }

      private Grade(Tier material, int damage, Rarity rarity, int paralysisDuration, int maxParalysisLevel, int cooldown, int maxParalysisCooldown) {
         this.material = material;
         this.damage = damage;
         this.rarity = rarity;
         this.paralysisDuration = paralysisDuration;
         this.maxParalysisLevel = maxParalysisLevel;
         this.cooldown = cooldown;
         this.maxParalysisCooldown = maxParalysisCooldown;
      }
   }
}
