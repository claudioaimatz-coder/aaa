package io.github.Memoires.trbeyond.enchantment;

import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;

public class DarknessInfusionEnchantment extends InfusionEnchantment {
   public DarknessInfusionEnchantment() {
      super(EnchantmentCategory.WEARABLE);
   }

   @Override
   public ChatFormatting getNameFormatting() {
      return ChatFormatting.BLACK;
   }

   @Override
   public float getDamageProtection(int pLevel, DamageSource source, LivingEntity wearer, EquipmentSlot slot, float damage) {
      if (slot.getType().equals(Type.HAND)) {
         return 0.0F;
      } else if (!this.canActivate(wearer.getItemBySlot(slot))) {
         return 0.0F;
      } else {
         float protection = 0.0F;
         if (this.isDark(wearer) && InfusionEnchantment.hasFullSetEffect(wearer, this)) {
            protection = 0.2F * (float)pLevel * damage;
         } else if (DamageSourceHelper.isDarkDamage(source)) {
            protection = 0.2F * (float)pLevel * damage;
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
            return DamageSourceHelper.isDarkDamage(source) ? 0.2F * (float)pLevel * damage : 0.0F;
         }
      }
   }

   public boolean isDark(LivingEntity entity) {
      if (entity != null && entity.level != null) {
         Level level = entity.level;
         BlockPos pos = entity.blockPosition();
         int actualLight = level.getMaxLocalRawBrightness(pos);
         return actualLight < 5;
      } else {
         return false;
      }
   }
}
