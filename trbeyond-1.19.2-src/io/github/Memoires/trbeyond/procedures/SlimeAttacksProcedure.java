package io.github.Memoires.trbeyond.procedures;

import io.github.Memoires.trbeyond.TRBeyond;
import io.github.Memoires.trbeyond.entity.custom.LivingArmorEntity;
import io.github.Memoires.trbeyond.entity.custom.MageEntity;
import io.github.Memoires.trbeyond.entity.custom.MilimSlimeScarletEntity;
import io.github.Memoires.trbeyond.entity.custom.SkullNightEntity;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class SlimeAttacksProcedure {
   public static void execute(LevelAccessor world, Entity target, Entity sourceentity) {
      if (target != null && sourceentity != null) {
         if (sourceentity instanceof MilimSlimeScarletEntity slime) {
            if (target instanceof LivingEntity livingTarget) {
               if (isValidTarget(slime, livingTarget)) {
                  if ("empty".equals(slime.animationprocedure)) {
                     int rnd = Mth.nextInt(RandomSource.create(), 1, 2);
                     performAttack(world, slime, livingTarget, rnd);
                  }
               }
            }
         }
      }
   }

   private static void performAttack(LevelAccessor world, MilimSlimeScarletEntity slime, LivingEntity target, int rnd) {
      if (!slime.getPersistentData().getBoolean("dmg")) {
         slime.getPersistentData().putBoolean("dmg", true);
         switch (rnd) {
            case 1:
               slime.setAnimation("animation.model.dash");
               slime.level
                  .playSound(
                     null,
                     slime.getX(),
                     slime.getY(),
                     slime.getZ(),
                     (SoundEvent)BeyondSoundEvents.milim_slime_dash.get(),
                     SoundSource.HOSTILE,
                     1.0F,
                     1.0F
                  );
               TRBeyond.queueServerWork(5, () -> {
                  if ("animation.model.dash".equals(slime.animationprocedure) && target.isAlive()) {
                     target.hurt(new EntityDamageSource("Physical", slime), 1000.0F);
                  }

                  resetAttack(slime, 10);
               });
               break;
            case 2:
               slime.setAnimation("animation.model.pop");
               TRBeyond.queueServerWork(5, () -> {
                  if ("animation.model.pop".equals(slime.animationprocedure) && target.isAlive()) {
                     target.hurt(new EntityDamageSource("Physical", slime), 1000.0F);
                  }

                  resetAttack(slime, 20);
               });
         }
      }
   }

   private static void resetAttack(MilimSlimeScarletEntity slime, int delay) {
      TRBeyond.queueServerWork(delay, () -> {
         slime.getPersistentData().putBoolean("dmg", false);
         slime.setAnimation("empty");
      });
   }

   private static boolean isValidTarget(MilimSlimeScarletEntity slime, LivingEntity entity) {
      if (entity == slime) {
         return false;
      } else if (entity instanceof MilimSlimeScarletEntity) {
         return false;
      } else if (entity instanceof LivingArmorEntity) {
         return false;
      } else {
         return entity instanceof SkullNightEntity ? false : !(entity instanceof MageEntity);
      }
   }
}
