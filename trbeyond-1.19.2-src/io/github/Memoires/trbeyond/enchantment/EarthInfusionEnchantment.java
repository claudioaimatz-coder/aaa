package io.github.Memoires.trbeyond.enchantment;

import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EarthInfusionEnchantment extends InfusionEnchantment {
   public EarthInfusionEnchantment() {
      super(EnchantmentCategory.BREAKABLE);
   }

   @Override
   public ChatFormatting getNameFormatting() {
      return ChatFormatting.GREEN;
   }

   @Override
   public float getDamageProtection(int pLevel, DamageSource source, LivingEntity wearer, EquipmentSlot slot, float damage) {
      if (slot.getType().equals(Type.HAND)) {
         return 0.0F;
      } else if (!this.canActivate(wearer.getItemBySlot(slot))) {
         return 0.0F;
      } else {
         float protection = 0.0F;
         if (DamageSourceHelper.isSpatialDamage(source)) {
            protection = 0.1F * (float)pLevel * damage;
         } else if (DamageSourceHelper.isEarthDamage(source)) {
            protection = 0.1F * (float)pLevel * damage;
         } else if (DamageSourceHelper.isFireDamage(source)) {
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
            return DamageSourceHelper.isEarthDamage(source) ? 0.2F * (float)pLevel * damage : 0.0F;
         }
      }
   }
}
