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

public class SpiderDaggerItem extends SimpleShortSwordItem {
   private final SpiderDaggerItem.Grade grade;

   public SpiderDaggerItem(SpiderDaggerItem.Grade grade) {
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
         long lastAttackTime = pStack.getOrCreateTag().getLong("LastPoisonTime");
         if (currentTime - lastAttackTime >= (long)this.grade.cooldown) {
            MobEffectInstance existingParalysis = pTarget.getEffect((MobEffect)TensuraMobEffects.FATAL_POISON.get());
            if (existingParalysis != null && existingParalysis.getAmplifier() >= this.grade.maxPoisonLevel - 1) {
               pStack.getOrCreateTag().putBoolean("MaxPoisonReached", true);
            } else {
               pStack.getOrCreateTag().putBoolean("MaxPoisonReached", false);
               pTarget.addEffect(
                  new MobEffectInstance(
                     (MobEffect)TensuraMobEffects.FATAL_POISON.get(),
                     this.grade.poisonDuration,
                     existingParalysis != null ? existingParalysis.getAmplifier() + 1 : 0
                  ),
                  pAttacker
               );
            }

            pStack.getOrCreateTag().putLong("LastPoisonTime", currentTime);
         }
      }

      return canAttack;
   }

   public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
      return pRepair.is((Item)TensuraMobDropItems.SPIDER_FANG.get());
   }

   public static enum Grade {
      SPECIAL(TensuraToolTiers.LOW_MAGISTEEL, 4, BeyondRarity.SPECIAL, 140, 2, 100),
      RARE(TensuraToolTiers.HIGH_MAGISTEEL, 7, BeyondRarity.RARE, 140, 2, 100),
      UNIQUE(TensuraToolTiers.PURE_MAGISTEEL, 5, BeyondRarity.UNIQUE, 140, 3, 100),
      LEGEND(TensuraToolTiers.ADAMANTITE, 11, BeyondRarity.LEGEND, 140, 6, 100);

      private final Tier material;
      private final int damage;
      private final Rarity rarity;
      private final int poisonDuration;
      private final int maxPoisonLevel;
      private final int cooldown;

      public Tier getMaterial() {
         return this.material;
      }

      public int getDamage() {
         return this.damage;
      }

      public Rarity getRarity() {
         return this.rarity;
      }

      public int getPoisonDuration() {
         return this.poisonDuration;
      }

      public int getMaxPoisonLevel() {
         return this.maxPoisonLevel;
      }

      public int getCooldown() {
         return this.cooldown;
      }

      private Grade(Tier material, int damage, Rarity rarity, int poisonDuration, int maxPoisonLevel, int cooldown) {
         this.material = material;
         this.damage = damage;
         this.rarity = rarity;
         this.poisonDuration = poisonDuration;
         this.maxPoisonLevel = maxPoisonLevel;
         this.cooldown = cooldown;
      }
   }
}
