package io.github.Memoires.trbeyond.util;

import io.github.Memoires.trbeyond.block.DungeonUnlockerBlock;
import io.github.Memoires.trbeyond.network.worldgen.portal.BeyondTeleporterUpdated;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class DoorRefreshManager {
   private static final int REFRESH_INTERVAL = 240;
   private static final Map<ServerLevel, Map<BlockPos, DoorRefreshManager.DoorInfo>> doorsByWorld = new ConcurrentHashMap<>();

   public static void registerDoor(ServerLevel world, BlockPos doorPos, Rotation rotation) {
      doorsByWorld.computeIfAbsent(world, lvl -> new ConcurrentHashMap<>()).put(doorPos, new DoorRefreshManager.DoorInfo(240, rotation));
   }

   public static void registerDoor(ServerLevel world, BlockPos doorPos) {
      registerDoor(world, doorPos, Rotation.NONE);
   }

   @SubscribeEvent
   public static void onLevelTick(LevelTickEvent evt) {
      if (evt.phase == Phase.END) {
         if (evt.level instanceof ServerLevel serverLevel) {
            Map<BlockPos, DoorRefreshManager.DoorInfo> timers = doorsByWorld.get(serverLevel);
            if (timers != null && !timers.isEmpty()) {
               for (Entry<BlockPos, DoorRefreshManager.DoorInfo> entry : timers.entrySet()) {
                  BlockPos pos = entry.getKey();
                  DoorRefreshManager.DoorInfo info = entry.getValue();
                  info.ticksRemaining--;
                  if (info.ticksRemaining <= 0) {
                     refreshDoor(serverLevel, pos, info.rotation, (Integer)serverLevel.getBlockState(pos).getValue(DungeonUnlockerBlock.TYPE));
                     info.ticksRemaining = 240;
                  }
               }
            }
         }
      }
   }

   private static void refreshDoor(ServerLevel world, BlockPos pos, Rotation rotation, int Type) {
      BlockState state = world.getBlockState(pos);
      if (state.hasProperty(DungeonUnlockerBlock.FLOOR)) {
         int floor = (Integer)state.getValue(DungeonUnlockerBlock.FLOOR);
         world.removeBlock(pos, false);
         world.removeBlock(pos.above(1), false);
         world.removeBlock(pos.above(2), false);
         BeyondTeleporterUpdated.placeDungeonUnlockerBlockWithRotation(world, pos, rotation, true, Type, floor);
      }
   }

   private static final class DoorInfo {
      int ticksRemaining;
      Rotation rotation;

      DoorInfo(int ticksRemaining, Rotation rotation) {
         this.ticksRemaining = ticksRemaining;
         this.rotation = rotation;
      }
   }
}
