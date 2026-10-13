package io.github.Memoires.trbeyond.client.collision;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TargetFinder {
   public static TargetFinder.TargetResult findAttackTargetResult(Player player, double attackRange) {
      Vec3 origin = getInitialTracingPoint(player);
      List<Entity> entities = getInitialTargets(player, attackRange);
      Vec3 size = new Vec3(attackRange / 6.0, attackRange / 6.0, attackRange * 1.0);
      OBB obb = new OBB(origin, size, 0.0F, player.getYRot());
      obb.updateVertex();
      TargetFinder.CollisionFilter collisionFilter = new TargetFinder.CollisionFilter(obb);
      entities = collisionFilter.filter(entities);
      TargetFinder.RadialFilter radialFilter = new TargetFinder.RadialFilter(origin, obb.axisZ, attackRange, (double)player.getYRot());
      entities = radialFilter.filter(entities);
      return new TargetFinder.TargetResult(entities, obb);
   }

   public static List<Entity> findAttackTargets(Player player, double attackRange) {
      return findAttackTargetResult(player, attackRange).entities;
   }

   public static Vec3 getInitialTracingPoint(Player player) {
      double shoulderHeight = (double)player.getBbHeight() * 0.15 * (double)player.getScale();
      return player.getEyePosition().subtract(0.0, shoulderHeight, 0.0);
   }

   public static List<Entity> getInitialTargets(Player player, double attackRange) {
      AABB box = player.getBoundingBox().inflate(attackRange + 1.0);
      return player.level
         .getEntities(player, box, entity -> !entity.isSpectator() && entity.isPickable())
         .stream()
         .filter(entity -> entity != player && entity.isAttackable())
         .collect(Collectors.toList());
   }

   public static class CollisionFilter implements TargetFinder.Filter {
      private OBB obb;

      public CollisionFilter(OBB obb) {
         this.obb = obb;
      }

      @Override
      public List<Entity> filter(List<Entity> entities) {
         return entities.stream()
            .filter(
               entity -> this.obb.intersects(entity.getBoundingBox().inflate((double)entity.getPickRadius()))
                     || this.obb.contains(entity.position().add(0.0, (double)(entity.getBbHeight() / 2.0F), 0.0))
            )
            .collect(Collectors.toList());
      }
   }

   public interface Filter {
      List<Entity> filter(List<Entity> var1);
   }

   public static class RadialFilter implements TargetFinder.Filter {
      private final Vec3 origin;
      private final Vec3 orientation;
      private final double attackRange;
      private final double attackAngle;

      public RadialFilter(Vec3 origin, Vec3 orientation, double attackRange, double attackAngle) {
         this.origin = origin;
         this.orientation = orientation;
         this.attackRange = attackRange;
         this.attackAngle = Math.max(Math.min(attackAngle, 360.0), 0.0);
      }

      @Override
      public List<Entity> filter(List<Entity> entities) {
         return entities.stream()
            .filter(
               entity -> {
                  double maxAngleDif = this.attackAngle / 2.0;
                  Vec3 distanceVector = CollisionHelper.distanceVector(this.origin, entity.getBoundingBox());
                  Vec3 positionVector = entity.position().add(0.0, (double)(entity.getBbHeight() / 2.0F), 0.0).subtract(this.origin);
                  return distanceVector.length() <= this.attackRange
                     && (
                        this.attackAngle == 0.0
                           || CollisionHelper.angleBetween(positionVector, this.orientation) <= maxAngleDif
                           || CollisionHelper.angleBetween(distanceVector, this.orientation) <= maxAngleDif
                     );
               }
            )
            .collect(Collectors.toList());
      }
   }

   public static class TargetResult {
      public List<Entity> entities;
      public OBB obb;

      public TargetResult(List<Entity> entities, OBB obb) {
         this.entities = entities;
         this.obb = obb;
      }
   }
}
