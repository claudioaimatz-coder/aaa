package io.github.Memoires.trbeyond.procedures;

import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import java.util.Comparator;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class FireBreathWhileProjectileFlyingTickProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity immediatesourceentity) {
      if (entity != null && immediatesourceentity != null) {
         if (14.0F > (immediatesourceentity != null ? entity.distanceTo(immediatesourceentity) : -1.0F)) {
            if (world instanceof ServerLevel _level) {
               _level.sendParticles(ParticleTypes.DRAGON_BREATH, x, y, z, 300, 0.1, 0.1, 0.1, 0.05);
            }

            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
               .stream()
               .sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
               .toList()) {
               if (entityiterator != immediatesourceentity && entityiterator != entity) {
                  entityiterator.hurt(TensuraDamageSources.blackFlame(entity), 500.0F);
               }
            }
         } else {
            immediatesourceentity.discard();
         }
      }
   }
}
