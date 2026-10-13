package io.github.Memoires.trbeyond.enchantment;

import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class FlameInfusionEnchantment extends InfusionEnchantment {
   public FlameInfusionEnchantment() {
      super(EnchantmentCategory.WEARABLE);
   }

   @Override
   public ChatFormatting getNameFormatting() {
      return ChatFormatting.DARK_RED;
   }

   @Override
   public float getDamageProtection(int pLevel, DamageSource source, LivingEntity wearer, EquipmentSlot slot, float damage) {
      if (slot.getType().equals(Type.HAND)) {
         return 0.0F;
      } else if (!this.canActivate(wearer.getItemBySlot(slot))) {
         return 0.0F;
      } else {
         float protection = 0.0F;
         if (DamageSourceHelper.isFireDamage(source)) {
            protection = 0.2F * (float)pLevel * damage;
         } else if (DamageSourceHelper.isEarthDamage(source)) {
            protection = 0.2F * (float)pLevel * damage;
         } else if (DamageSourceHelper.isWaterDamage(source)) {
            protection = -0.2F * (float)pLevel * damage;
         }

         return protection;
      }
   }

   @Override
   public float getDamageBonus(int pLevel, DamageSource source, LivingEntity attacker, LivingEntity target, EquipmentSlot slot, float damage) {
      if (slot.getType().equals(Type.ARMOR)) {
         return 0.0F;
      } else {
         boolean canActivate = this.canActivate(attacker.getItemBySlot(slot));
         if (!canActivate) {
            return 0.0F;
         } else {
            return DamageSourceHelper.isFireDamage(source) ? 0.4F * (float)pLevel * damage : 0.0F;
         }
      }
   }
}
