package io.github.Memoires.trbeyond.procedures;

import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.Memoires.trbeyond.entity.custom.LivingArmorEntity;
import io.github.Memoires.trbeyond.entity.custom.MageEntity;
import io.github.Memoires.trbeyond.entity.custom.SkullNightEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TornadoTickProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (entity instanceof Monster living) {
            if (entity.getPersistentData().getBoolean("dmg") && entity.getPersistentData().getBoolean("tornado")) {
               for (Entity e : world.getEntitiesOfClass(
                  Entity.class,
                  AABB.ofSize(entity.position(), 18.0, 18.0, 18.0),
                  ex -> ex instanceof LivingEntity
                        && ex != entity
                        && !(ex instanceof LivingArmorEntity)
                        && !(ex instanceof MageEntity)
                        && !(ex instanceof SkullNightEntity)
               )) {
                  Vec3 direction = e.position().subtract(entity.position()).normalize();
                  Vec3 knockback = new Vec3(direction.x * 1.2, 0.5, direction.z * 1.2);
                  e.setDeltaMovement(knockback);
                  e.hurt(TensuraDamageSources.indirectElementalAttack("tensura.wind_breath", entity, entity, false), 100.0F);
               }
            }

            if (entity.getPersistentData().getBoolean("dmg") && entity.getPersistentData().getBoolean("ray")) {
               for (Entity e : world.getEntitiesOfClass(
                  Entity.class,
                  AABB.ofSize(entity.position(), 24.0, 24.0, 24.0),
                  ex -> ex instanceof LivingEntity
                        && ex != entity
                        && !(ex instanceof LivingArmorEntity)
                        && !(ex instanceof MageEntity)
                        && !(ex instanceof SkullNightEntity)
               )) {
                  e.hurt(TensuraDamageSources.holyDamage(entity), 500.0F);
               }
            }
         }
      }
   }
}
