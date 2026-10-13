package io.github.Memoires.trbeyond.procedures;

import io.github.Memoires.trbeyond.TRBeyond;
import io.github.Memoires.trbeyond.entity.custom.MilimSlimeScarletEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SlimeTickProcedure {
   public static void execute(Entity entity, double x, double y, double z, LevelAccessor world) {
      if (entity != null) {
         if (24.0 == entity.getPersistentData().getDouble("anim")) {
            entity.getPersistentData().putDouble("anim", 0.0);
         } else {
            entity.getPersistentData().putDouble("anim", entity.getPersistentData().getDouble("anim") + 1.0);
         }

         if (0.0 == entity.getPersistentData().getDouble("anim") && entity instanceof MilimSlimeScarletEntity animatable) {
            animatable.setTexture("slime_1");
         }

         if (4.0 == entity.getPersistentData().getDouble("anim") && entity instanceof MilimSlimeScarletEntity animatable) {
            animatable.setTexture("slime_2");
         }

         if (8.0 == entity.getPersistentData().getDouble("anim") && entity instanceof MilimSlimeScarletEntity animatable) {
            animatable.setTexture("slime_3");
         }

         if (12.0 == entity.getPersistentData().getDouble("anim") && entity instanceof MilimSlimeScarletEntity animatable) {
            animatable.setTexture("slime_4");
         }

         if (16.0 == entity.getPersistentData().getDouble("anim") && entity instanceof MilimSlimeScarletEntity animatable) {
            animatable.setTexture("slime_5");
         }

         if (20.0 == entity.getPersistentData().getDouble("anim") && entity instanceof MilimSlimeScarletEntity animatable) {
            animatable.setTexture("slime_6");
         }

         if (!entity.getPersistentData().getBoolean("intro")
            && !world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 7.0, 7.0, 7.0), e -> true).isEmpty()) {
            entity.getPersistentData().putBoolean("intro", true);
            if (entity instanceof MilimSlimeScarletEntity) {
               ((MilimSlimeScarletEntity)entity).setAnimation("animation.model.charged_attack");
            }

            if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide()) {
               _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 5, false, false));
            }

            TRBeyond.queueServerWork(60, () -> {
               if (entity instanceof MilimSlimeScarletEntity) {
                  ((MilimSlimeScarletEntity)entity).setAnimation("empty");
               }
            });
         }
      }
   }
}
