package io.github.Memoires.trbeyond.procedures;

import io.github.Memoires.trbeyond.network.worldgen.DungeonSaveData;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.server.ServerLifecycleHooks;

@EventBusSubscriber
public class onServerTickProcedure {
   @SubscribeEvent
   public static void onServerTick(ServerTickEvent event) {
      if (event.phase == Phase.END) {
         MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
         ServerLevel dungeonLevel = server.m_129880_(BeyondDungeon.DUNGEON_LEVEL_KEY);
         if (dungeonLevel != null) {
            dungeonLevel.getAllEntities().forEach(entity -> {
               if (dungeonLevel.dimensionTypeId() == BeyondDungeon.DUNGEON_DIM_TYPE_KEY) {
                  BlockPos position = DungeonSaveData.getOverworld(dungeonLevel).getLobbyPos();
                  if (entity instanceof TamableAnimal tameable && tameable.getOwner() != null && tameable.getBlockY() < tameable.getLevel().getMinBuildHeight()) {
                     tameable.resetFallDistance();
                     tameable.teleportTo((double)position.getX(), (double)position.getY(), (double)position.getZ());
                  }
               }
            });
         }
      }
   }
}
