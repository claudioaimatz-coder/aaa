package io.github.Memoires.trbeyond.procedures;

import io.github.Memoires.trbeyond.TRBeyond;
import io.github.Memoires.trbeyond.entity.custom.MageEntity;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LevelAccessor;

public class MageAttacksProcedure {
   public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         if (sourceentity instanceof MageEntity mage) {
            if (sourceentity instanceof LivingEntity living) {
               LivingEntity target = ((Monster)living).getTarget();
               if (target != null && target != sourceentity) {
                  if ("empty".equals(mage.animationprocedure)) {
                     int rnd = Mth.nextInt(RandomSource.create(), 1, 3);
                     double waittick = 0.0;
                     if (!sourceentity.getPersistentData().getBoolean("dmg")) {
                        sourceentity.getPersistentData().putBoolean("dmg", true);
                        if (rnd == 1) {
                           mage.setAnimation("slash_attack");
                           sourceentity.lookAt(Anchor.EYES, target.position());

                           for (int i = 0; i < 30; i++) {
                              TRBeyond.queueServerWork((int)waittick, () -> sourceentity.lookAt(Anchor.EYES, target.position()));
                              waittick++;
                           }

                           TRBeyond.queueServerWork(22, () -> {
                              if ("slash_attack".equals(mage.animationprocedure) && target.isAlive() && target.distanceToSqr(sourceentity) <= 16.0) {
                                 target.hurt(new EntityDamageSource("Physical", entity), 25.0F);
                              }

                              resetAttack(mage, sourceentity, 30);
                           });
                        } else if (rnd == 2) {
                           mage.setAnimation("tornado_blade");

                           for (int i = 0; i < 50; i++) {
                              TRBeyond.queueServerWork((int)waittick, () -> sourceentity.lookAt(Anchor.EYES, target.position()));
                              waittick++;
                           }

                           TRBeyond.queueServerWork(
                              1,
                              () -> entity.level
                                    .playSound(
                                       null,
                                       target.getX(),
                                       target.getY(),
                                       target.getZ(),
                                       (SoundEvent)BeyondSoundEvents.ghost_mage_wind_cutter.get(),
                                       SoundSource.HOSTILE,
                                       1.0F,
                                       1.0F
                                    )
                           );
                           TRBeyond.queueServerWork(30, () -> {
                              sourceentity.getPersistentData().putBoolean("tornado", true);
                              TRBeyond.queueServerWork(20, () -> {
                                 sourceentity.getPersistentData().putBoolean("tornado", false);
                                 resetAttack(mage, sourceentity, 50);
                              });
                           });
                        } else {
                           mage.setAnimation("holy_ray");

                           for (int i = 0; i < 150; i++) {
                              TRBeyond.queueServerWork((int)waittick, () -> sourceentity.lookAt(Anchor.EYES, target.position()));
                              waittick++;
                           }

                           TRBeyond.queueServerWork(
                              45,
                              () -> entity.level
                                    .playSound(
                                       null,
                                       target.getX(),
                                       target.getY(),
                                       target.getZ(),
                                       (SoundEvent)BeyondSoundEvents.ghost_mage_holy_ray.get(),
                                       SoundSource.HOSTILE,
                                       1.0F,
                                       1.0F
                                    )
                           );
                           TRBeyond.queueServerWork(85, () -> {
                              sourceentity.getPersistentData().putBoolean("ray", true);
                              TRBeyond.queueServerWork(40, () -> {
                                 sourceentity.getPersistentData().putBoolean("ray", false);
                                 resetAttack(mage, sourceentity, 70);
                              });
                           });
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static void resetAttack(MageEntity mage, Entity sourceentity, int delay) {
      TRBeyond.queueServerWork(delay, () -> {
         sourceentity.getPersistentData().putBoolean("dmg", false);
         mage.setAnimation("empty");
      });
   }
}
