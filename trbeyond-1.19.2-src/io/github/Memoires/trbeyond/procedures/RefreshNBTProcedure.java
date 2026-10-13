package io.github.Memoires.trbeyond.procedures;

import io.github.Memoires.trbeyond.entity.custom.BabyDragonEntity;
import io.github.Memoires.trbeyond.entity.custom.GozulEntity;
import io.github.Memoires.trbeyond.entity.custom.LivingArmorEntity;
import io.github.Memoires.trbeyond.entity.custom.MageEntity;
import io.github.Memoires.trbeyond.entity.custom.MezulEntity;
import io.github.Memoires.trbeyond.entity.custom.MilimSlimeScarletEntity;
import io.github.Memoires.trbeyond.entity.custom.SkullNightEntity;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RefreshNBTProcedure {
   @SubscribeEvent
   public static void onEntitySpawned(EntityJoinLevelEvent event) {
      execute(event, event.getEntity());
   }

   public static void execute(Entity entity) {
      execute(null, entity);
   }

   private static void execute(@Nullable Event event, Entity entity) {
      if (entity != null) {
         if (entity instanceof GozulEntity) {
            entity.getPersistentData().putBoolean("dmg", false);
            entity.getPersistentData().putBoolean("block", false);
            entity.getPersistentData().putBoolean("horn", false);
            entity.getPersistentData().putBoolean("blow", false);
            if (entity instanceof GozulEntity) {
               ((GozulEntity)entity).setAnimation("empty");
            }
         }

         if (entity instanceof MezulEntity) {
            entity.getPersistentData().putBoolean("dmg", false);
            entity.getPersistentData().putBoolean("block", false);
            entity.getPersistentData().putBoolean("dash", false);
            entity.getPersistentData().putBoolean("blow", false);
            entity.getPersistentData().putBoolean("thrust", false);
            if (entity instanceof MezulEntity) {
               ((MezulEntity)entity).setAnimation("empty");
            }
         }

         if (entity instanceof LivingArmorEntity) {
            entity.getPersistentData().putBoolean("dmg", false);
            entity.getPersistentData().putBoolean("spin", false);
            if (entity instanceof LivingArmorEntity) {
               ((LivingArmorEntity)entity).setAnimation("empty");
            }
         }

         if (entity instanceof SkullNightEntity) {
            entity.getPersistentData().putBoolean("dmg", false);
            if (entity instanceof SkullNightEntity) {
               ((SkullNightEntity)entity).setAnimation("empty");
            }
         }

         if (entity instanceof MageEntity) {
            entity.getPersistentData().putBoolean("dmg", false);
            entity.getPersistentData().putBoolean("tornado", false);
            if (entity instanceof MageEntity) {
               ((MageEntity)entity).setAnimation("empty");
            }
         }

         if (entity instanceof MilimSlimeScarletEntity) {
            entity.getPersistentData().putBoolean("dmg", false);
            if (entity instanceof MilimSlimeScarletEntity) {
               ((MilimSlimeScarletEntity)entity).setAnimation("empty");
            }
         }

         if (entity instanceof BabyDragonEntity) {
            entity.getPersistentData().putBoolean("dmg", false);
            if (entity instanceof BabyDragonEntity) {
               ((BabyDragonEntity)entity).setAnimation("empty");
            }
         }
      }
   }
}
