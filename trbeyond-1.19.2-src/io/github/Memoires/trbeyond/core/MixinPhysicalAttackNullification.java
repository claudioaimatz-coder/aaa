package io.github.Memoires.trbeyond.core;

import com.github.manasmods.tensura.ability.skill.resist.PhysicalAttackNullification;
import io.github.Memoires.trbeyond.enchantment.InfusionEnchantment;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PhysicalAttackNullification.class})
public class MixinPhysicalAttackNullification {
   @Inject(
      method = {"ogreBerserkerResist"},
      at = {@At(
         value = "RETURN",
         ordinal = 2
      )},
      remap = false,
      cancellable = true
   )
   private static void ogreBerserkerResist(DamageSource source, LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
      if (!(Boolean)cir.getReturnValue()) {
         if (InfusionEnchantment.hasFullSetEffect(target, (Enchantment)BeyondEnchantments.BATTLE_INFUSION.get())) {
            cir.setReturnValue(true);
         }
      }
   }
}
