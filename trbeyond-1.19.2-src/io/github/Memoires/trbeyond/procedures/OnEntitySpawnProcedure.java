package io.github.Memoires.trbeyond.procedures;

import io.github.Memoires.trbeyond.entity.misc.PrimedDungeonChestEntity;
import io.github.Memoires.trbeyond.util.PrimedChestSpawnTracker;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class OnEntitySpawnProcedure {
   @SubscribeEvent
   public static void onEntitySpawn(EntityJoinLevelEvent event) {
      if (event.getEntity() instanceof PrimedDungeonChestEntity chest) {
         PrimedChestSpawnTracker.track(chest);
         System.out.println("[DEBUG] Tracked new PrimedDungeonChestEntity: " + chest.getUUID());
      }
   }
}
