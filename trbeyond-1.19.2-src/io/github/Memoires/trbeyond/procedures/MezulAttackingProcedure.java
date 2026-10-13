package io.github.Memoires.trbeyond.procedures;

import io.github.Memoires.trbeyond.TRBeyond;
import io.github.Memoires.trbeyond.entity.custom.MezulEntity;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;

public class MezulAttackingProcedure {
   public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         if (sourceentity instanceof Mob mob) {
            Entity target = mob.getTarget();
            if (target != null && target instanceof Player) {
               double rnd = (double)Mth.nextInt(RandomSource.create(), 1, 11);
               double waittick = 0.0;
               if ("empty".equals(((MezulEntity)sourceentity).animationprocedure)) {
                  if (rnd <= 4.0) {
                     if (!sourceentity.getPersistentData().getBoolean("dmg") && sourceentity.distanceToSqr(target) <= 36.0) {
                        ((MezulEntity)sourceentity).setAnimation("attack");
                        entity.level
                           .playSound(
                              null,
                              entity.getX(),
                              entity.getY(),
                              entity.getZ(),
                              (SoundEvent)BeyondSoundEvents.mezul_strike.get(),
                              SoundSource.HOSTILE,
                              1.0F,
                              1.0F
                           );
                        sourceentity.lookAt(Anchor.EYES, target.position());
                        sourceentity.getPersistentData().putBoolean("dmg", true);

                        for (int i = 0; i < 25; i++) {
                           TRBeyond.queueServerWork((int)waittick, () -> {
                              if (sourceentity != null && target != null) {
                                 sourceentity.lookAt(Anchor.EYES, target.position());
                              }
                           });
                           waittick++;
                        }

                        TRBeyond.queueServerWork(25, () -> {
                           if (target.isAlive() && sourceentity.distanceToSqr(target) <= 36.0) {
                              target.hurt(new DamageSource("Physical"), 500.0F);
                           }

                           TRBeyond.queueServerWork(40, () -> {
                              if (sourceentity instanceof MezulEntity) {
                                 sourceentity.getPersistentData().putBoolean("dmg", false);
                                 ((MezulEntity)sourceentity).setAnimation("empty");
                              }
                           });
                        });
                     }
                  } else if (rnd == 5.0 || rnd == 6.0) {
                     if (!sourceentity.getPersistentData().getBoolean("dmg") && sourceentity.distanceToSqr(target) <= 9.0) {
                        ((MezulEntity)sourceentity).setAnimation("back_kick");
                        entity.level
                           .playSound(
                              null,
                              entity.getX(),
                              entity.getY(),
                              entity.getZ(),
                              (SoundEvent)BeyondSoundEvents.mezul_kick.get(),
                              SoundSource.HOSTILE,
                              1.0F,
                              1.0F
                           );
                        sourceentity.getPersistentData().putBoolean("dmg", true);

                        for (int i = 0; i < 20; i++) {
                           TRBeyond.queueServerWork((int)waittick, () -> {
                              if (sourceentity != null && target != null) {
                                 sourceentity.lookAt(Anchor.EYES, target.position());
                              }
                           });
                           waittick++;
                        }

                        TRBeyond.queueServerWork(20, () -> {
                           if (target.isAlive() && sourceentity.distanceToSqr(target) <= 9.0) {
                              target.hurt(new DamageSource("Physical"), 500.0F);
                           }

                           TRBeyond.queueServerWork(40, () -> {
                              if (sourceentity instanceof MezulEntity) {
                                 sourceentity.getPersistentData().putBoolean("dmg", false);
                                 ((MezulEntity)sourceentity).setAnimation("empty");
                              }
                           });
                        });
                     }
                  } else if (rnd == 7.0) {
                     if (!sourceentity.getPersistentData().getBoolean("dmg") && sourceentity.distanceToSqr(target) <= 121.0) {
                        ((MezulEntity)sourceentity).setAnimation("thrust_attack");
                        sourceentity.lookAt(Anchor.EYES, target.position());
                        sourceentity.getPersistentData().putBoolean("dmg", true);

                        for (int i = 0; i < 45; i++) {
                           TRBeyond.queueServerWork((int)waittick, () -> {
                              if (sourceentity != null && target != null) {
                                 sourceentity.lookAt(Anchor.EYES, target.position());
                              }
                           });
                           waittick++;
                        }

                        TRBeyond.queueServerWork(
                           20,
                           () -> entity.level
                                 .playSound(
                                    null,
                                    entity.getX(),
                                    entity.getY(),
                                    entity.getZ(),
                                    (SoundEvent)BeyondSoundEvents.mezul_repeat_strike.get(),
                                    SoundSource.HOSTILE,
                                    1.0F,
                                    1.0F
                                 )
                        );
                        TRBeyond.queueServerWork(45, () -> {
                           if (sourceentity != null) {
                              sourceentity.getPersistentData().putBoolean("thrust", true);
                           }

                           TRBeyond.queueServerWork(40, () -> {
                              if (sourceentity != null) {
                                 sourceentity.getPersistentData().putBoolean("thrust", false);
                                 sourceentity.getPersistentData().putBoolean("dmg", false);
                                 if (sourceentity instanceof MezulEntity) {
                                    ((MezulEntity)sourceentity).setAnimation("empty");
                                 }
                              }
                           });
                        });
                     }
                  } else if (rnd == 8.0 || rnd == 10.0 || rnd == 11.0) {
                     if (!sourceentity.getPersistentData().getBoolean("dmg") && sourceentity.distanceToSqr(target) <= 121.0) {
                        ((MezulEntity)sourceentity).setAnimation("spear_spin_dash");
                        entity.level
                           .playSound(
                              null,
                              entity.getX(),
                              entity.getY(),
                              entity.getZ(),
                              (SoundEvent)BeyondSoundEvents.mezul_spin_strike.get(),
                              SoundSource.HOSTILE,
                              1.0F,
                              1.0F
                           );
                        sourceentity.lookAt(Anchor.EYES, target.position());
                        sourceentity.getPersistentData().putBoolean("dmg", true);

                        for (int i = 0; i < 22; i++) {
                           TRBeyond.queueServerWork((int)waittick, () -> {
                              if (sourceentity != null && target != null) {
                                 sourceentity.lookAt(Anchor.EYES, target.position());
                              }
                           });
                           waittick++;
                        }

                        TRBeyond.queueServerWork(20, () -> {
                           if (sourceentity != null) {
                              sourceentity.getPersistentData().putBoolean("blow", true);
                           }

                           TRBeyond.queueServerWork(2, () -> {
                              if (sourceentity != null) {
                                 sourceentity.getPersistentData().putBoolean("blow", false);
                              }

                              TRBeyond.queueServerWork(30, () -> {
                                 if (sourceentity instanceof MezulEntity) {
                                    sourceentity.getPersistentData().putBoolean("dmg", false);
                                    ((MezulEntity)sourceentity).setAnimation("empty");
                                 }
                              });
                           });
                        });
                     }
                  } else if (rnd == 9.0 && !sourceentity.getPersistentData().getBoolean("dmg") && sourceentity.distanceToSqr(target) <= 121.0) {
                     ((MezulEntity)sourceentity).setAnimation("spin_dash_loop");
                     entity.level
                        .playSound(
                           null,
                           entity.getX(),
                           entity.getY(),
                           entity.getZ(),
                           (SoundEvent)BeyondSoundEvents.mezul_spin.get(),
                           SoundSource.HOSTILE,
                           1.0F,
                           1.0F
                        );
                     sourceentity.lookAt(Anchor.EYES, target.position());
                     sourceentity.getPersistentData().putBoolean("dmg", true);
                     if (sourceentity instanceof LivingEntity _entity && !_entity.level.isClientSide()) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 3, false, false));
                     }

                     sourceentity.getPersistentData().putBoolean("dash", true);

                     for (int i = 0; i < 30; i++) {
                        TRBeyond.queueServerWork((int)waittick, () -> {
                           if (sourceentity != null && target != null) {
                              sourceentity.lookAt(Anchor.EYES, target.position());
                           }
                        });
                        waittick++;
                     }

                     TRBeyond.queueServerWork(35, () -> ((MezulEntity)sourceentity).setAnimation("spin_dash_loop"));
                     TRBeyond.queueServerWork(50, () -> {
                        ((MezulEntity)sourceentity).setAnimation("spin_dash_loop");
                        sourceentity.getPersistentData().putBoolean("dash", false);
                        TRBeyond.queueServerWork(40, () -> {
                           if (sourceentity instanceof MezulEntity) {
                              sourceentity.getPersistentData().putBoolean("dmg", false);
                              ((MezulEntity)sourceentity).setAnimation("empty");
                           }
                        });
                     });
                  }
               }
            }
         }
      }
   }
}
