package io.github.Memoires.trbeyond.handlers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.server.ServerLifecycleHooks;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.FORGE
)
public class TickEventsHandler {
   @SubscribeEvent
   public static void onServerTick(ServerTickEvent event) {
      if (event.phase == Phase.END) {
         MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
         if (server != null) {
            for (ServerLevel level : server.m_129785_()) {
               TaskHandler.tick(level);
            }
         }
      }
   }
}
