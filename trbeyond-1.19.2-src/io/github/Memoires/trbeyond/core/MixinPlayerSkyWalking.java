package io.github.Memoires.trbeyond.core;

import io.github.Memoires.trbeyond.ability.battlewill.utility.SkyWalkingArt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Player.class})
public class MixinPlayerSkyWalking {
   @Inject(
      method = {"tick"},
      at = {@At("TAIL")}
   )
   private void injectSkyWalkingTick(CallbackInfo ci) {
      Player player = (Player)this;
      if (SkyWalkingArt.isSkyWalkingActive(player.getUUID())) {
         double skyWalkHeight = SkyWalkingArt.getSkyWalkingHeight(player.getUUID());
         double playerY = player.getY();
         Vec3 movement = player.getDeltaMovement();
         if (playerY < skyWalkHeight - 0.05 && movement.y <= 0.0) {
            player.setPos(player.getX(), skyWalkHeight, player.getZ());
            player.setDeltaMovement(new Vec3(movement.x, 0.0, movement.z));
            player.setOnGround(true);
         } else if (playerY > skyWalkHeight + 1.0 && movement.y > 0.0) {
            SkyWalkingArt.updateSkyWalkingHeight(player.getUUID(), playerY);
         } else if (Math.abs(playerY - skyWalkHeight) <= 0.1 && movement.y <= 0.0) {
            player.setOnGround(true);
            if (movement.y < 0.0) {
               player.setDeltaMovement(new Vec3(movement.x, 0.0, movement.z));
            }
         }
      }
   }

   @Inject(
      method = {"aiStep"},
      at = {@At("HEAD")}
   )
   private void injectSkyWalkingAiStep(CallbackInfo ci) {
      Player player = (Player)this;
      if (SkyWalkingArt.isSkyWalkingActive(player.getUUID())) {
         double skyWalkHeight = SkyWalkingArt.getSkyWalkingHeight(player.getUUID());
         double playerY = player.getY();
         if (Math.abs(playerY - skyWalkHeight) <= 0.2) {
            player.setOnGround(true);
         }
      }
   }
}
