package io.github.Memoires.trbeyond.procedures;

import io.github.Memoires.trbeyond.entity.custom.MageEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

public class MageAnimationTickProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (24.0 == entity.getPersistentData().getDouble("anim")) {
            entity.getPersistentData().putDouble("anim", 0.0);
         } else {
            entity.getPersistentData().putDouble("anim", entity.getPersistentData().getDouble("anim") + 1.0);
         }

         if (0.0 == entity.getPersistentData().getDouble("anim") && entity instanceof MageEntity animatable) {
            animatable.setTexture("mage_texture_1");
         }

         if (4.0 == entity.getPersistentData().getDouble("anim") && entity instanceof MageEntity animatable) {
            animatable.setTexture("mage_texture_2");
         }

         if (8.0 == entity.getPersistentData().getDouble("anim") && entity instanceof MageEntity animatable) {
            animatable.setTexture("mage_texture_3");
         }

         if (12.0 == entity.getPersistentData().getDouble("anim") && entity instanceof MageEntity animatable) {
            animatable.setTexture("mage_texture_4");
         }

         if (16.0 == entity.getPersistentData().getDouble("anim") && entity instanceof MageEntity animatable) {
            animatable.setTexture("mage_texture_5");
         }

         if (20.0 == entity.getPersistentData().getDouble("anim") && entity instanceof MageEntity animatable) {
            animatable.setTexture("mage_texture_6");
         }

         TornadoTickProcedure.execute(world, entity);
      }
   }
}
