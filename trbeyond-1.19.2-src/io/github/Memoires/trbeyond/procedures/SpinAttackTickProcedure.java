package io.github.Memoires.trbeyond.procedures;

import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

public class SpinAttackTickProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (entity instanceof LivingEntity living) {
            if (entity.getPersistentData().getBoolean("dmg") && entity.getPersistentData().getBoolean("spin")) {
               Entity target = ((Monster)living).getTarget();
               if (target == null || target == entity) {
                  return;
               }

               if (target.distanceToSqr(entity) <= 16.0) {
                  target.setDeltaMovement(new Vec3(entity.getLookAngle().x * 1.0, 0.2, entity.getLookAngle().z * 1.0));
                  target.hurt(new EntityDamageSource("Physical", living), 125.0F);
               }
            }
         }
      }
   }
}
