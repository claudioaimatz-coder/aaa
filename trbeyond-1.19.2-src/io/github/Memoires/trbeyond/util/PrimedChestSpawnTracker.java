package io.github.Memoires.trbeyond.util;

import io.github.Memoires.trbeyond.entity.misc.PrimedDungeonChestEntity;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.entity.Entity;

public class PrimedChestSpawnTracker {
   private static final Map<UUID, Long> recentPrimedChestTimestamps = new ConcurrentHashMap<>();

   public static void track(PrimedDungeonChestEntity chest) {
      recentPrimedChestTimestamps.put(chest.getUUID(), System.currentTimeMillis());
      System.out.println("[DEBUG] Tracked new PrimedDungeonChestEntity: " + chest.getUUID());
   }

   public static boolean isPrimedChestNear(Entity entity, double radius) {
      long now = System.currentTimeMillis();
      recentPrimedChestTimestamps.entrySet().removeIf(entry -> now - entry.getValue() > 10000L);
      return !recentPrimedChestTimestamps.isEmpty();
   }
}
