package io.github.Memoires.trbeyond.procedures;

import io.github.Memoires.trbeyond.TRBeyond;
import io.github.Memoires.trbeyond.entity.custom.LivingArmorEntity;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class LivingAttackProcedure {
   public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         if (sourceentity instanceof LivingArmorEntity livingArmor) {
            int rnd = Mth.nextInt(RandomSource.create(), 1, 3);
            double waittick = 0.0;
            Entity target = livingArmor.getTarget();
            if (target instanceof LivingEntity) {
               LivingEntity livingTarget = (LivingEntity)target;
               if ("empty".equals(livingArmor.animationprocedure) && target != sourceentity && !sourceentity.getPersistentData().getBoolean("dmg")) {
                  sourceentity.getPersistentData().putBoolean("dmg", true);
                  if (rnd == 1) {
                     livingArmor.setAnimation("animation.model.horizontal_slash");
                     sourceentity.lookAt(Anchor.EYES, target.position());

                     for (int i = 0; i < 35; i++) {
                        TRBeyond.queueServerWork((int)waittick, () -> {
                           sourceentity.lookAt(Anchor.EYES, target.position());
                           livingArmor.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 25, 5, false, false));
                        });
                        waittick++;
                     }

                     TRBeyond.queueServerWork(
                        17,
                        () -> entity.level
                              .playSound(
                                 null,
                                 target.getX(),
                                 target.getY(),
                                 target.getZ(),
                                 (SoundEvent)BeyondSoundEvents.living_armor_strike.get(),
                                 SoundSource.HOSTILE,
                                 1.0F,
                                 1.0F
                              )
                     );
                     TRBeyond.queueServerWork(27, () -> {
                        if (target.isAlive() && target.distanceToSqr(sourceentity) <= 16.0) {
                           target.hurt(new EntityDamageSource("Physical", entity), 94.0F);
                        }

                        resetAttack(livingArmor, sourceentity, 30);
                     });
                  } else if (rnd == 2) {
                     livingArmor.setAnimation("animation.model.vertical_slash");

                     for (int i = 0; i < 40; i++) {
                        TRBeyond.queueServerWork((int)waittick, () -> {
                           sourceentity.lookAt(Anchor.EYES, target.position());
                           livingArmor.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 25, 5, false, false));
                        });
                        waittick++;
                     }

                     TRBeyond.queueServerWork(
                        27,
                        () -> entity.level
                              .playSound(
                                 null,
                                 target.getX(),
                                 target.getY(),
                                 target.getZ(),
                                 (SoundEvent)BeyondSoundEvents.living_armor_strike.get(),
                                 SoundSource.HOSTILE,
                                 1.0F,
                                 1.0F
                              )
                     );
                     TRBeyond.queueServerWork(35, () -> {
                        if (target.isAlive() && target.distanceToSqr(sourceentity) <= 16.0) {
                           target.hurt(new EntityDamageSource("Physical", entity), 188.0F);
                        }

                        resetAttack(livingArmor, sourceentity, 75);
                     });
                  } else {
                     livingArmor.setAnimation("animation.model.spin_attack");
                     sourceentity.lookAt(Anchor.EYES, target.position());

                     for (int i = 0; i < 90; i++) {
                        TRBeyond.queueServerWork((int)waittick, () -> {
                           sourceentity.lookAt(Anchor.EYES, target.position());
                           livingArmor.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 1, false, false));
                        });
                        waittick++;
                     }

                     TRBeyond.queueServerWork(
                        40,
                        () -> entity.level
                              .playSound(
                                 null,
                                 target.getX(),
                                 target.getY(),
                                 target.getZ(),
                                 (SoundEvent)BeyondSoundEvents.living_armor_spin.get(),
                                 SoundSource.HOSTILE,
                                 1.0F,
                                 1.0F
                              )
                     );
                     TRBeyond.queueServerWork(40, () -> {
                        if (!livingArmor.level.isClientSide()) {
                           livingArmor.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 3, false, false));
                        }

                        if (target.isAlive() && target.distanceToSqr(sourceentity) <= 64.0) {
                           target.hurt(new EntityDamageSource("Physical", entity), 60.0F);
                        }

                        sourceentity.getPersistentData().putBoolean("spin", true);
                        TRBeyond.queueServerWork(50, () -> {
                           sourceentity.getPersistentData().putBoolean("spin", false);
                           resetAttack(livingArmor, sourceentity, 0);
                        });
                     });
                  }
               }
            }
         }
      }
   }

   private static void resetAttack(LivingArmorEntity livingArmor, Entity sourceentity, int delay) {
      TRBeyond.queueServerWork(delay, () -> {
         sourceentity.getPersistentData().putBoolean("dmg", false);
         livingArmor.setAnimation("empty");
      });
   }
}
