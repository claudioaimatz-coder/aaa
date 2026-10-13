package io.github.Memoires.trbeyond.util;

import io.github.Memoires.trbeyond.network.BeyondNetwork;
import io.github.Memoires.trbeyond.network.ConfigSyncPacket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.PacketDistributor;

@EventBusSubscriber(
   bus = Bus.FORGE
)
public class ConfigHandler {
   private static final Map<UUID, Integer> pendingSyncs = new HashMap<>();

   @SubscribeEvent
   public static void onPlayerJoin(PlayerLoggedInEvent event) {
      if (event.getEntity() instanceof ServerPlayer player) {
         pendingSyncs.put(player.getUUID(), 20);
      }
   }

   @SubscribeEvent
   public static void onServerTick(ServerTickEvent event) {
      if (event.phase == Phase.END && event.side == LogicalSide.SERVER) {
         Iterator<Entry<UUID, Integer>> it = pendingSyncs.entrySet().iterator();

         while (it.hasNext()) {
            Entry<UUID, Integer> entry = it.next();
            UUID uuid = entry.getKey();
            int ticksLeft = entry.getValue() - 1;
            if (ticksLeft <= 0) {
               ServerPlayer player = event.getServer().getPlayerList().getPlayer(uuid);
               if (player != null) {
                  BeyondNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), ConfigSyncPacket.fromConfig());
                  System.out.println("[Server] Sent ConfigSyncPacket to " + player.getName().getString());
               }

               it.remove();
            } else {
               entry.setValue(ticksLeft);
            }
         }
      }
   }
}
