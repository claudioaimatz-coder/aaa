package io.github.Memoires.trbeyond.procedures;

import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.Memoires.trbeyond.TRBeyond;
import io.github.Memoires.trbeyond.entity.custom.GozulEntity;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LevelAccessor;

public class TargetProcedure {
   public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
      if (sourceentity instanceof LivingEntity living && sourceentity instanceof GozulEntity gozul) {
         double rnd = (double)Mth.nextInt(RandomSource.create(), 1, 5);
         double waittick = 0.0;
         if ("empty".equals(gozul.animationprocedure) && !sourceentity.getPersistentData().getBoolean("axe_charging")) {
            LivingEntity target = ((Monster)living).getTarget();
            if (target == null || target == sourceentity) {
               return;
            }

            switch ((int)rnd) {
               case 1:
                  if (!sourceentity.getPersistentData().getBoolean("dmg") && sourceentity.distanceToSqr(target) <= 36.0) {
                     gozul.setAnimation("jumping_axe_kick_animation");
                     entity.level
                        .playSound(
                           null,
                           entity.getX(),
                           entity.getY(),
                           entity.getZ(),
                           (SoundEvent)BeyondSoundEvents.gozul_jump_attack.get(),
                           SoundSource.HOSTILE,
                           1.0F,
                           1.0F
                        );
                     sourceentity.lookAt(Anchor.EYES, target.position());
                     sourceentity.getPersistentData().putBoolean("dmg", true);

                     for (int i = 0; i < 25; i++) {
                        TRBeyond.queueServerWork((int)waittick, () -> sourceentity.lookAt(Anchor.EYES, target.position()));
                        waittick++;
                     }

                     TRBeyond.queueServerWork(25, () -> {
                        if (target.isAlive() && sourceentity.distanceToSqr(target) <= 36.0) {
                           float dmg = entity.getPersistentData().getBoolean("axe_charged") ? 1000.0F : 500.0F;
                           target.hurt(new EntityDamageSource("Physical", entity), dmg);
                        }

                        TRBeyond.queueServerWork(40, () -> {
                           sourceentity.getPersistentData().putBoolean("dmg", false);
                           gozul.setAnimation("empty");
                        });
                     });
                  }
                  break;
               case 2:
                  if (!sourceentity.getPersistentData().getBoolean("dmg") && sourceentity.distanceToSqr(target) <= 36.0) {
                     gozul.setAnimation("axe_attack_animation");
                     entity.level
                        .playSound(
                           null,
                           entity.getX(),
                           entity.getY(),
                           entity.getZ(),
                           (SoundEvent)BeyondSoundEvents.gozul_regular_swing.get(),
                           SoundSource.HOSTILE,
                           1.0F,
                           1.0F
                        );
                     sourceentity.lookAt(Anchor.EYES, target.position());
                     sourceentity.getPersistentData().putBoolean("dmg", true);

                     for (int i = 0; i < 17; i++) {
                        TRBeyond.queueServerWork((int)waittick, () -> sourceentity.lookAt(Anchor.EYES, target.position()));
                        waittick++;
                     }

                     TRBeyond.queueServerWork(17, () -> {
                        if (target.isAlive() && sourceentity.distanceToSqr(target) <= 36.0) {
                           float dmg = entity.getPersistentData().getBoolean("axe_charged") ? 500.0F : 250.0F;
                           target.hurt(new EntityDamageSource("Physical", entity), dmg);
                        }

                        TRBeyond.queueServerWork(40, () -> {
                           sourceentity.getPersistentData().putBoolean("dmg", false);
                           gozul.setAnimation("empty");
                        });
                     });
                  }
                  break;
               case 3:
                  if (!sourceentity.getPersistentData().getBoolean("dmg") && sourceentity.distanceToSqr(target) <= 121.0) {
                     gozul.setAnimation("electric_horns_animation");
                     entity.level
                        .playSound(
                           null,
                           entity.getX(),
                           entity.getY(),
                           entity.getZ(),
                           (SoundEvent)BeyondSoundEvents.gozul_electric_horn.get(),
                           SoundSource.HOSTILE,
                           1.0F,
                           1.0F
                        );
                     if (!sourceentity.level.isClientSide()) {
                        living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 99, false, false));
                     }

                     sourceentity.lookAt(Anchor.EYES, target.position());
                     sourceentity.getPersistentData().putBoolean("dmg", true);

                     for (int i = 0; i < 45; i++) {
                        TRBeyond.queueServerWork((int)waittick, () -> sourceentity.lookAt(Anchor.EYES, target.position()));
                        waittick++;
                     }

                     TRBeyond.queueServerWork(35, () -> {
                        TRBeyond.queueServerWork(20, () -> {
                           TRBeyond.queueServerWork(35, () -> {
                              sourceentity.getPersistentData().putBoolean("dmg", false);
                              gozul.setAnimation("empty");
                           });
                           sourceentity.getPersistentData().putBoolean("horn", false);
                        });
                        sourceentity.getPersistentData().putBoolean("horn", true);
                     });
                  }
                  break;
               case 4:
                  if (!sourceentity.getPersistentData().getBoolean("dmg") && sourceentity.distanceToSqr(target) <= 121.0) {
                     gozul.setAnimation("horn_blow_animation");
                     entity.level
                        .playSound(
                           null,
                           entity.getX(),
                           entity.getY(),
                           entity.getZ(),
                           (SoundEvent)BeyondSoundEvents.gozul_electric_horn.get(),
                           SoundSource.HOSTILE,
                           1.0F,
                           1.0F
                        );
                     sourceentity.lookAt(Anchor.EYES, target.position());
                     if (!sourceentity.level.isClientSide()) {
                        living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 99, false, false));
                     }

                     sourceentity.getPersistentData().putBoolean("dmg", true);

                     for (int i = 0; i < 45; i++) {
                        TRBeyond.queueServerWork((int)waittick, () -> sourceentity.lookAt(Anchor.EYES, target.position()));
                        waittick++;
                     }

                     TRBeyond.queueServerWork(35, () -> {
                        if (!sourceentity.level.isClientSide()) {
                           living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 3, false, false));
                        }

                        sourceentity.getPersistentData().putBoolean("blow", true);
                        TRBeyond.queueServerWork(20, () -> {
                           sourceentity.getPersistentData().putBoolean("blow", false);
                           TRBeyond.queueServerWork(30, () -> {
                              sourceentity.getPersistentData().putBoolean("dmg", false);
                              gozul.setAnimation("empty");
                           });
                        });
                     });
                  }
                  break;
               case 5:
                  if (!sourceentity.getPersistentData().getBoolean("dmg") && sourceentity.distanceToSqr(target) <= 100.0) {
                     gozul.setAnimation("axe_throw_animation");
                     entity.level
                        .playSound(
                           null,
                           entity.getX(),
                           entity.getY(),
                           entity.getZ(),
                           (SoundEvent)BeyondSoundEvents.gozul_throw.get(),
                           SoundSource.HOSTILE,
                           1.0F,
                           1.0F
                        );
                     sourceentity.getPersistentData().putBoolean("dmg", true);

                     for (int i = 0; i < 25; i++) {
                        TRBeyond.queueServerWork((int)waittick, () -> sourceentity.lookAt(Anchor.EYES, target.position()));
                        waittick++;
                     }

                     TRBeyond.queueServerWork(25, () -> {
                        if (target.isAlive() && sourceentity.distanceToSqr(target) <= 100.0) {
                           target.hurt(new EntityDamageSource("Physical", entity), 750.0F);
                           if (entity.getPersistentData().getBoolean("axe_charged")) {
                              DamageSource lightning = TensuraDamageSources.indirectElementalAttack("tensura.lightning", entity, entity, false);
                              target.hurt(lightning, 750.0F);
                           }
                        }

                        TRBeyond.queueServerWork(40, () -> {
                           sourceentity.getPersistentData().putBoolean("dmg", false);
                           gozul.setAnimation("empty");
                        });
                     });
                  }
            }
         }

         return;
      }
   }
}
