package io.github.Memoires.trbeyond.procedures;

import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

public class DamagesProcedure {
   private static final float DAMAGE_AMOUNT = 1000.0F;
   private static final double KNOCKBACK_STRENGTH = 5.0;
   private static final double DETECTION_RANGE = 11.0;

   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null && world != null) {
         if (entity instanceof LivingEntity living) {
            if (entity.getPersistentData().getBoolean("dmg")) {
               LivingEntity target = ((Monster)living).getTarget();
               if (target != null && target != entity) {
                  if (!(target.distanceToSqr(entity) > 121.0)) {
                     if (entity.getPersistentData().getBoolean("blow")) {
                        applyBlowAttack(entity, target);
                     }

                     if (entity.getPersistentData().getBoolean("horn")) {
                        applyHornAttack(entity, target);
                     }
                  }
               }
            }
         }
      }
   }

   private static void applyBlowAttack(Entity source, LivingEntity target) {
      Vec3 lookAngle = source.getLookAngle();
      target.setDeltaMovement(new Vec3(lookAngle.x * 5.0, 0.1, lookAngle.z * 5.0));
      DamageSource physical = DamageSourceHelper.turnTensura(new EntityDamageSource("Physical", source), source);
      target.hurt(physical, 1000.0F);
   }

   private static void applyHornAttack(Entity source, LivingEntity target) {
      DamageSource lightning = TensuraDamageSources.elementalAttack("tensura.lightning_attack", source, false);
      target.hurt(lightning, 1000.0F);
      DamageSource physical = DamageSourceHelper.turnTensura(new EntityDamageSource("Physical", source), source);
      target.hurt(physical, 1000.0F);
   }
}
