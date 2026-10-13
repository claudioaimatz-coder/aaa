package io.github.Memoires.trbeyond.procedures;

import io.github.Memoires.trbeyond.TRBeyond;
import io.github.Memoires.trbeyond.entity.custom.GozulEntity;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class TextureAnimationsProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity instanceof GozulEntity gozul) {
         int anim = (int)entity.getPersistentData().getDouble("anim");
         switch (anim) {
            case 0:
               gozul.setTexture("gozul_texture_1");
               break;
            case 1:
               gozul.setTexture("gozul_texture_2");
               break;
            case 2:
               gozul.setTexture("gozul_texture_3");
         }

         if (anim >= 3) {
            entity.getPersistentData().putDouble("anim", 0.0);
         } else {
            entity.getPersistentData().putDouble("anim", (double)(anim + 1));
         }

         DamagesProcedure.execute(world, entity);
         if (entity instanceof LivingEntity living) {
            float health = living.getHealth();
            float maxHealth = living.getMaxHealth();
            if (health < maxHealth / 2.0F && !entity.getPersistentData().getBoolean("axe_charged") && "empty".equals(gozul.animationprocedure)) {
               entity.getPersistentData().putBoolean("axe_charged", true);
               entity.getPersistentData().putBoolean("axe_charging", true);
               gozul.setAnimation("electric_axe_animation");
               entity.level
                  .playSound(
                     null,
                     entity.getX(),
                     entity.getY(),
                     entity.getZ(),
                     (SoundEvent)BeyondSoundEvents.gozul_lightning_charge.get(),
                     SoundSource.HOSTILE,
                     1.0F,
                     1.0F
                  );
               TRBeyond.queueServerWork(30, () -> {
                  entity.getPersistentData().putBoolean("axe_charging", false);
                  RefreshNBTProcedure.execute(entity);
               });
            }
         }
      }
   }
}
