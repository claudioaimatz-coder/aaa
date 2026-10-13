package io.github.Memoires.trbeyond.core;

import io.github.Memoires.trbeyond.ability.battlewill.utility.SkyWalkingArt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Entity.class})
public class MixinEntitySkyWalking {
   @Shadow
   private Vec3 deltaMovement;

   @Inject(
      method = {"isOnGround"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void injectSkyWalkingGroundCheck(CallbackInfoReturnable<Boolean> cir) {
      Entity entity = (Entity)this;
      if (entity instanceof Player player && SkyWalkingArt.isSkyWalkingActive(player.getUUID())) {
         double skyWalkHeight = SkyWalkingArt.getSkyWalkingHeight(player.getUUID());
         double playerY = player.getY();
         if (Math.abs(playerY - skyWalkHeight) <= 0.2) {
            cir.setReturnValue(true);
         }
      }
   }

   @Redirect(
      method = {"move"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
      )
   )
   private void redirectSetDeltaMovement(Entity entity, Vec3 movement) {
      if (entity instanceof Player player && SkyWalkingArt.isSkyWalkingActive(player.getUUID())) {
         double skyWalkHeight = SkyWalkingArt.getSkyWalkingHeight(player.getUUID());
         double playerY = player.getY();
         if (movement.y < 0.0 && playerY <= skyWalkHeight + 0.1) {
            movement = new Vec3(movement.x, 0.0, movement.z);
         }
      }

      entity.setDeltaMovement(movement);
   }
}
