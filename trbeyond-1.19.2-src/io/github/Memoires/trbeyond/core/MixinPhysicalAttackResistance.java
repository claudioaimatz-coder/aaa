package io.github.Memoires.trbeyond.core;

import com.github.manasmods.tensura.ability.skill.resist.PhysicalAttackResistance;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.Memoires.trbeyond.items.weapons.GorikimaruItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PhysicalAttackResistance.class})
public class MixinPhysicalAttackResistance {
   @Inject(
      method = {"canBypassPhysical"},
      at = {@At("RETURN")},
      remap = false,
      cancellable = true
   )
   private static void canBypassPhysical(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
      if (!(Boolean)cir.getReturnValue()) {
         if (damageSource.getEntity() instanceof LivingEntity attacker) {
            if (!(attacker.getMainHandItem().getItem() instanceof GorikimaruItem)) {
               return;
            }

            if (!attacker.hasEffect((MobEffect)TensuraMobEffects.OGRE_GUILLOTINE.get())) {
               return;
            }

            cir.setReturnValue(true);
         }
      }
   }
}
