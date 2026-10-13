package io.github.Memoires.trbeyond.core;

import com.github.manasmods.tensura.ability.SkillUtils;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.Memoires.trbeyond.items.weapons.MinosBardicheItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   value = {Player.class},
   priority = 2000
)
public abstract class MixinPlayer {
   @Inject(
      method = {"attack"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
         shift = Shift.AFTER
      )}
   )
   public void attack(Entity entity, CallbackInfo ci, @Local(ordinal = 0) float damage) {
      Player attacker = (Player)this;
      if (!attacker.getLevel().isClientSide()) {
         if (!SkillUtils.noInteractiveMode(attacker)) {
            if (entity instanceof LivingEntity target) {
               MinosBardicheItem.dealLightningDamage(attacker, target, attacker.getMainHandItem());
            }
         }
      }
   }
}
