package io.github.Memoires.trbeyond.procedures;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class OnWorldOpenedProcedure {
   @SubscribeEvent
   public static void onPlayerJoin(PlayerLoggedInEvent event) {
      if (event.getEntity() instanceof ServerPlayer player) {
         awardAdvancement(player, "trbeyond", "root");
      }
   }

   private static void awardAdvancement(ServerPlayer player, String namespace, String path) {
      MinecraftServer server = player.getServer();
      if (server != null) {
         ResourceLocation advId = new ResourceLocation(namespace, path);
         Advancement advancement = server.m_129889_().getAdvancement(advId);
         if (advancement != null) {
            AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
            if (!progress.isDone()) {
               for (String criterion : progress.getRemainingCriteria()) {
                  player.getAdvancements().award(advancement, criterion);
               }

               System.out.println("[DEBUG] Awarded " + path + " to: " + player.getName().getString());
            }
         }
      }
   }
}
