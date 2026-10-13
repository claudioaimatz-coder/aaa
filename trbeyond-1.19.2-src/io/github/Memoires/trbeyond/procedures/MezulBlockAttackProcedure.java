package io.github.Memoires.trbeyond.procedures;

import io.github.Memoires.trbeyond.TRBeyond;
import io.github.Memoires.trbeyond.entity.custom.GozulEntity;
import io.github.Memoires.trbeyond.entity.custom.MezulEntity;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MezulBlockAttackProcedure {
   @SubscribeEvent
   public static void onEntityAttacked(LivingHurtEvent event) {
      if (event != null && event.getEntity() != null) {
         execute(event, event.getEntity().level, event.getEntity(), event.getSource().getEntity());
      }
   }

   public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
      execute(null, world, entity, sourceentity);
   }

   private static void execute(@Nullable Event event, LevelAccessor world, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         if (entity instanceof MezulEntity && sourceentity instanceof Player && "empty".equals(((MezulEntity)entity).animationprocedure)) {
            if (entity instanceof GozulEntity) {
               ((GozulEntity)entity).setAnimation("block");
            }

            entity.getPersistentData().putBoolean("block", true);
            TRBeyond.queueServerWork(20, () -> {
               if (entity instanceof GozulEntity) {
                  ((GozulEntity)entity).setAnimation("empty");
               }

               entity.getPersistentData().putBoolean("block", false);
            });
         }
      }
   }
}
