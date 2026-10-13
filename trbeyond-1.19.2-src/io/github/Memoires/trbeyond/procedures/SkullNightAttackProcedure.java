package io.github.Memoires.trbeyond.procedures;

import io.github.Memoires.trbeyond.TRBeyond;
import io.github.Memoires.trbeyond.entity.custom.SkullNightEntity;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

public class SkullNightAttackProcedure {
   public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         if (sourceentity instanceof SkullNightEntity skullNight) {
            Entity target = skullNight.getTarget();
            if (target != null && target != sourceentity) {
               if ("empty".equals(skullNight.animationprocedure)) {
                  int rnd = Mth.nextInt(RandomSource.create(), 1, 2);
                  double waittick = 0.0;
                  if (!sourceentity.getPersistentData().getBoolean("dmg")) {
                     sourceentity.getPersistentData().putBoolean("dmg", true);
                     if (rnd == 1) {
                        skullNight.setAnimation("animation.skullnight.slash");
                        sourceentity.lookAt(Anchor.EYES, target.position());

                        for (int i = 0; i < 15; i++) {
                           TRBeyond.queueServerWork((int)waittick, () -> sourceentity.lookAt(Anchor.EYES, target.position()));
                           waittick++;
                        }

                        TRBeyond.queueServerWork(
                           5,
                           () -> entity.level
                                 .playSound(
                                    null,
                                    target.getX(),
                                    target.getY(),
                                    target.getZ(),
                                    (SoundEvent)BeyondSoundEvents.golden_skeleton_swing.get(),
                                    SoundSource.HOSTILE,
                                    1.0F,
                                    1.0F
                                 )
                        );
                        TRBeyond.queueServerWork(10, () -> {
                           if (target.distanceToSqr(sourceentity) <= 16.0) {
                              target.hurt(new EntityDamageSource("Physical", entity), 50.0F);
                           }

                           resetAttack(skullNight, sourceentity, 20);
                        });
                     } else {
                        skullNight.setAnimation("animation.skullnight.slash2");

                        for (int i = 0; i < 23; i++) {
                           TRBeyond.queueServerWork((int)waittick, () -> sourceentity.lookAt(Anchor.EYES, target.position()));
                           waittick++;
                        }

                        TRBeyond.queueServerWork(
                           15,
                           () -> entity.level
                                 .playSound(
                                    null,
                                    target.getX(),
                                    target.getY(),
                                    target.getZ(),
                                    (SoundEvent)BeyondSoundEvents.golden_skeleton_swing.get(),
                                    SoundSource.HOSTILE,
                                    1.0F,
                                    1.0F
                                 )
                        );
                        TRBeyond.queueServerWork(23, () -> {
                           if (target.distanceToSqr(sourceentity) <= 16.0) {
                              target.hurt(new EntityDamageSource("Physical", entity), 100.0F);
                           }

                           resetAttack(skullNight, sourceentity, 40);
                        });
                     }
                  }
               }
            }
         }
      }
   }

   private static void resetAttack(SkullNightEntity skullNight, Entity sourceentity, int delay) {
      TRBeyond.queueServerWork(delay, () -> {
         sourceentity.getPersistentData().putBoolean("dmg", false);
         skullNight.setAnimation("empty");
      });
   }
}
