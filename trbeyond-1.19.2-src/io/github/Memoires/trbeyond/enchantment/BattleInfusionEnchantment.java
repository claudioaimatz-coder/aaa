package io.github.Memoires.trbeyond.enchantment;

import com.github.manasmods.tensura.ability.battlewill.Battewill;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSource;
import net.minecraft.ChatFormatting;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class BattleInfusionEnchantment extends InfusionEnchantment {
   public BattleInfusionEnchantment() {
      super(EnchantmentCategory.BREAKABLE);
   }

   @Override
   public ChatFormatting getNameFormatting() {
      return ChatFormatting.GRAY;
   }

   @Override
   public float getDamageProtection(int pLevel, DamageSource source, LivingEntity wearer, EquipmentSlot slot, float damage) {
      if (slot.getType().equals(Type.HAND)) {
         return 0.0F;
      } else if (!this.canActivate(wearer.getItemBySlot(slot))) {
         return 0.0F;
      } else {
         float protection = 0.0F;
         if (DamageSourceHelper.isNaturalEffects(source)) {
            protection = 0.1F * (float)pLevel * damage;
         }

         return protection;
      }
   }

   @Override
   public float getDamageBonus(int level, DamageSource source, LivingEntity attacker, LivingEntity target, EquipmentSlot slot, float damage) {
      if (slot.getType().equals(Type.ARMOR)) {
         return 0.0F;
      } else {
         ItemStack stack = attacker.getItemBySlot(slot);
         if (!this.canActivate(stack)) {
            return 0.0F;
         } else {
            float bonus = 0.0F;
            if (slot.equals(EquipmentSlot.MAINHAND)
               && attacker.hasEffect((MobEffect)TensuraMobEffects.OGRE_GUILLOTINE.get())
               && DamageSourceHelper.isPhysicalAttack(source)) {
               bonus = damage;
            }

            if (source instanceof TensuraDamageSource damageSource
               && damageSource.getSkill() != null
               && damageSource.getSkill().getSkill() instanceof Battewill) {
               return bonus + damage * (float)level * 0.2F;
            }

            float newDamage = 10.0F * (float)level;
            if (stack.getItem() instanceof SwordItem sword) {
               newDamage = sword.getDamage() * (float)level;
            }

            if (stack.getItem() instanceof DiggerItem digger) {
               newDamage = digger.getAttackDamage() * (float)level;
            }

            return bonus + newDamage * 0.1F;
         }
      }
   }
}
