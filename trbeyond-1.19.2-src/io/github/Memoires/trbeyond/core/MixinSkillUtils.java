package io.github.Memoires.trbeyond.core;

import com.github.manasmods.tensura.ability.SkillUtils;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.Memoires.trbeyond.enchantment.InfusionEnchantment;
import io.github.Memoires.trbeyond.enchantment.SpatialInfusionEnchantment;
import io.github.Memoires.trbeyond.entity.custom.BossBlackSpiderEntity;
import io.github.Memoires.trbeyond.entity.custom.EvilCentipedeBossEntity;
import io.github.Memoires.trbeyond.entity.custom.GozulEntity;
import io.github.Memoires.trbeyond.entity.custom.MezulEntity;
import io.github.Memoires.trbeyond.entity.custom.OgreLordEntity;
import io.github.Memoires.trbeyond.entity.custom.TempestSerpentBossEntity;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import io.github.Memoires.trbeyond.world.BeyondGamerules;
import java.util.Map;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({SkillUtils.class})
public class MixinSkillUtils {
   @Unique
   private static final Enchantment trbeyond$SPATIAL = (Enchantment)BeyondEnchantments.SPATIAL_INFUSION.get();

   @ModifyReturnValue(
      method = {"canAutoSmelt"},
      at = {@At("RETURN")},
      remap = false
   )
   private static boolean canAutoSmelt(boolean original, Entity entity) {
      if (original) {
         return true;
      } else if (!(entity instanceof LivingEntity)) {
         return original;
      } else {
         if (InfusionEnchantment.hasFullSetEffect((LivingEntity)entity, (Enchantment)BeyondEnchantments.FLAME_INFUSION.get())) {
            original = true;
         }

         return original;
      }
   }

   @ModifyReturnValue(
      method = {"haveSeveranceAttack"},
      at = {@At("RETURN")},
      remap = false
   )
   private static boolean haveSeveranceAttack(boolean original, DamageSource damageSource, LivingEntity target) {
      if (trbeyond$hasSpatialInfusionInArmor(target)) {
         return false;
      } else {
         if (damageSource.getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();
            if (weapon.isEnchanted() && weapon.getEnchantmentLevel(trbeyond$SPATIAL) > 0) {
               return true;
            }
         }

         return original;
      }
   }

   @Unique
   private static boolean trbeyond$hasSpatialInfusionInArmor(LivingEntity entity) {
      for (EquipmentSlot slot : EquipmentSlot.values()) {
         if (slot.getType() == Type.ARMOR) {
            ItemStack armorItem = entity.getItemBySlot(slot);
            if (!armorItem.isEmpty()) {
               Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(armorItem);
               if (enchantments.keySet().stream().anyMatch(e -> e instanceof SpatialInfusionEnchantment)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   @ModifyReturnValue(
      method = {"reducingResistances"},
      at = {@At("RETURN")},
      remap = false
   )
   private static boolean reducingResistances(boolean original, LivingEntity entity) {
      return !entity.level.getGameRules().getBoolean(BeyondGamerules.NIGHTMARE_MODE)
         ? original
         : original
            || entity instanceof BossBlackSpiderEntity
            || entity instanceof EvilCentipedeBossEntity
            || entity instanceof TempestSerpentBossEntity
            || entity instanceof GozulEntity
            || entity instanceof MezulEntity
            || entity instanceof OgreLordEntity;
   }
}
