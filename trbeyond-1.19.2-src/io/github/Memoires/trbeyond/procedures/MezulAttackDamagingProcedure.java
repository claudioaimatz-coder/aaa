package io.github.Memoires.trbeyond.procedures;

import io.github.Memoires.trbeyond.TRBeyond;
import java.util.Comparator;
import java.util.List;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class MezulAttackDamagingProcedure {
   private static final float BLOW_DAMAGE = 1000.0F;
   private static final float DASH_DAMAGE = 375.0F;
   private static final float THRUST_DAMAGE = 1000.0F;
   private static final float THRUST_FOLLOW_UP_DAMAGE = 1500.0F;
   private static final int ATTACK_RANGE = 6;
   private static final DamageSource PHYSICAL_DAMAGE = new DamageSource("Physical");

   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         List<Player> nearbyPlayers = getNearbyPlayers(world, entity);
         if (!nearbyPlayers.isEmpty() && entity.getPersistentData().getBoolean("dmg")) {
            Player target = getNearestPlayer(world, entity);
            if (target != null) {
               if (entity.getPersistentData().getBoolean("blow")) {
                  performBlowAttack(entity, target);
               }

               if (entity.getPersistentData().getBoolean("dash")) {
                  performDashAttack(entity, target);
               }

               if (entity.getPersistentData().getBoolean("thrust")) {
                  performThrustAttack(world, entity, target);
               }
            }
         }
      }
   }

   private static List<Player> getNearbyPlayers(LevelAccessor world, Entity entity) {
      return world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(entity.getX(), entity.getY(), entity.getZ()), 6.0, 6.0, 6.0), e -> true);
   }

   private static Player getNearestPlayer(LevelAccessor world, Entity entity) {
      return world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(entity.getX(), entity.getY(), entity.getZ()), 6.0, 6.0, 6.0), e -> true)
         .stream()
         .min(Comparator.comparingDouble(player -> player.distanceToSqr(entity.getX(), entity.getY(), entity.getZ())))
         .orElse(null);
   }

   private static void knockbackTarget(Entity entity, Entity target) {
      target.setDeltaMovement(new Vec3(entity.getLookAngle().x, 0.2, entity.getLookAngle().z));
   }

   private static void performBlowAttack(Entity entity, Entity target) {
      knockbackTarget(entity, target);
      target.hurt(PHYSICAL_DAMAGE, 1000.0F);
   }

   private static void performDashAttack(Entity entity, Entity target) {
      knockbackTarget(entity, target);
      target.hurt(PHYSICAL_DAMAGE, 375.0F);
   }

   private static void performThrustAttack(LevelAccessor world, Entity entity, Entity target) {
      target.hurt(PHYSICAL_DAMAGE, 1000.0F);
      TRBeyond.queueServerWork(3, () -> {
         List<Player> playersStillNearby = getNearbyPlayers(world, entity);
         if (!playersStillNearby.isEmpty()) {
            Player nearestPlayer = getNearestPlayer(world, entity);
            if (nearestPlayer != null) {
               for (int i = 0; i < 3; i++) {
                  nearestPlayer.hurt(PHYSICAL_DAMAGE, 1500.0F);
               }
            }
         }
      });
   }
}
