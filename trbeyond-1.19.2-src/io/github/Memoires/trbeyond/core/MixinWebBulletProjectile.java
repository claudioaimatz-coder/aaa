package io.github.Memoires.trbeyond.core;

import com.github.manasmods.tensura.entity.projectile.WebBulletProjectile;
import io.github.Memoires.trbeyond.entity.custom.BossBlackSpiderEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({WebBulletProjectile.class})
public abstract class MixinWebBulletProjectile {
   @Inject(
      method = {"canPull(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/LivingEntity;)Z"},
      at = {@At("RETURN")},
      cancellable = true,
      remap = false
   )
   private void getProjectile(Entity entity, LivingEntity owner, CallbackInfoReturnable<Boolean> cir) {
      if (owner instanceof BossBlackSpiderEntity) {
         cir.setReturnValue(true);
      }
   }
}
