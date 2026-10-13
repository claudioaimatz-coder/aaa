package io.github.Memoires.trbeyond.procedures;

import io.github.Memoires.trbeyond.entity.custom.LivingArmorEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

public class LivingArmorOnEntityTickUpdateProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (12.0 == entity.getPersistentData().getDouble("anim")) {
            entity.getPersistentData().putDouble("anim", 0.0);
         } else {
            entity.getPersistentData().putDouble("anim", entity.getPersistentData().getDouble("anim") + 1.0);
         }

         if (0.0 == entity.getPersistentData().getDouble("anim") && entity instanceof LivingArmorEntity animatable) {
            animatable.setTexture("living_armor_1");
         }

         if (3.0 == entity.getPersistentData().getDouble("anim") && entity instanceof LivingArmorEntity animatable) {
            animatable.setTexture("living_armor_2");
         }

         if (6.0 == entity.getPersistentData().getDouble("anim") && entity instanceof LivingArmorEntity animatable) {
            animatable.setTexture("living_armor_3");
         }

         if (9.0 == entity.getPersistentData().getDouble("anim") && entity instanceof LivingArmorEntity animatable) {
            animatable.setTexture("living_armor_4");
         }

         SpinAttackTickProcedure.execute(world, entity);
      }
   }
}
