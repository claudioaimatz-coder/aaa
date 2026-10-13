package io.github.Memoires.trbeyond.util;

import io.github.Memoires.trbeyond.block.DungeonChestBlock;
import io.github.Memoires.trbeyond.block.ShrineBlock;
import io.github.Memoires.trbeyond.capability.player.BeyondPlayerCapability;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.world.BeyondGamerules;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RecurringEntitySpawner {
   private static final int TICKS_BETWEEN_SPAWNS = 100;
   private static final int DESPAWN_CHECK_TICKS = 200;
   private static final int DESPAWN_DISTANCE = 60;
   private static final int SPAWNS_NEAR_PLAYER_MAX = 30;
   private static final int SPAWN_CHECK_DISTANCE = 50;
   private static final int MIN_SPAWN_DISTANCE = 15;
   private static final int MAX_SPAWN_DISTANCE = 40;
   private static final int MAX_SPAWN_ATTEMPTS = 40;
   private static final Map<Integer, Integer> occupiedFloors = new HashMap<>(100);
   private static ServerLevel serverWorld;
   private static final List<RecurringEntitySpawner.randomSpawnInfo> playerInstances = new ArrayList<>();
   private static final Set<Integer> PROHIBITED_SPAWN_FLOORS = Set.of(1, 95);

   @SubscribeEvent
   public static void onServerTick(ServerTickEvent event) {
      if (event.phase == Phase.END) {
         serverWorld = event.getServer().m_129880_(BeyondDungeon.DUNGEON_LEVEL_KEY);
         if (serverWorld != null) {
            if (serverWorld.getGameTime() % 200L == 0L) {
               List<Entity> entities = StreamSupport.<Entity>stream(serverWorld.getEntities().getAll().spliterator(), false).toList();
               entities.forEach(
                  entity -> {
                     int sourceFloor = entity.getPersistentData().contains("floorCount") ? entity.getPersistentData().getInt("floorCount") : -1;
                     if (entity != null
                        && (!entity.getPersistentData().contains("respawn") || !entity.getPersistentData().getBoolean("respawn"))
                        && entity.getPersistentData().contains("hallwayEntity")
                        && entity.getPersistentData().getBoolean("hallwayEntity")
                        && (
                           serverWorld.getNearestPlayer(entity, 60.0) == null
                              || !occupiedFloors.containsKey(sourceFloor) && !occupiedFloors.containsKey(sourceFloor - 1)
                        )) {
                        entity.discard();
                     }
                  }
               );
            }

            if (serverWorld.getGameTime() % 100L == 0L) {
               int spawnRate = BeyondGamerules.getInt(serverWorld, BeyondGamerules.AMBIENT_MOB_SPAWN_RATE);
               if (Math.random() >= (double)spawnRate / 100.0) {
                  return;
               }

               playerInstances.clear();
               occupiedFloors.clear();
               serverWorld.getPlayers(serverPlayer -> serverPlayer.level.dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY))
                  .forEach(
                     player -> {
                        BlockPos playerPosx = player.getOnPos();
                        int currentFloor = BeyondSpawnUtils.GetFloorFromBlockPos(playerPosx);
                        if (!PROHIBITED_SPAWN_FLOORS.contains(currentFloor) && player.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) {
                           if (occupiedFloors.containsKey(currentFloor)) {
                              occupiedFloors.put(currentFloor, occupiedFloors.get(currentFloor) + 1);
                           } else {
                              occupiedFloors.put(currentFloor, 1);
                           }

                           boolean inShrineChallenge = BeyondPlayerCapability.getFrom(player)
                              .map(cap -> cap.getChallengeType() == null ? false : cap.getChallengeType().getCategory().equals("shrine"))
                              .orElse(false);
                           if (inShrineChallenge) {
                              return;
                           }

                           int nearbySpawns = player.getLevel()
                              .getEntitiesOfClass(
                                 LivingEntity.class,
                                 new AABB(playerPosx.atY(getFloorHeightFromFloor(BeyondSpawnUtils.GetFloorFromBlockPos(playerPosx)) - 2))
                                    .inflate(50.0, 6.0, 50.0)
                              )
                              .stream()
                              .filter(LivingEntity -> LivingEntity.getPersistentData().contains("hallwayEntity"))
                              .toList()
                              .size();
                           playerInstances.add(
                              new RecurringEntitySpawner.randomSpawnInfo(player, nearbySpawns, BeyondSpawnUtils.GetFloorFromBlockPos(playerPosx))
                           );
                        }
                     }
                  );

               for (RecurringEntitySpawner.randomSpawnInfo playerInstance : playerInstances) {
                  if (playerInstance.nearbySpawns <= 30) {
                     BlockPos playerPos = playerInstance.player().getOnPos();

                     for (int i = 0; i < 2; i++) {
                        BlockPos spawnPos = getRandomSpawnDestination(playerPos, serverWorld);
                        if (spawnPos != null) {
                           int floorCount = BeyondSpawnUtils.GetFloorFromBlockPos(playerPos);
                           if (floorCount != 1 && floorCount % 10 != 0 && floorCount != 95) {
                              boolean treasureRoom = false;

                              for (int dx = -5; dx <= 5; dx++) {
                                 for (int dz = -5; dz <= 5; dz++) {
                                    BlockPos checkPos = spawnPos.offset(dx, 0, dz);
                                    Block block1 = serverWorld.getBlockState(checkPos).getBlock();
                                    Block block2 = serverWorld.getBlockState(checkPos.offset(0, 1, 0)).getBlock();
                                    if (block1 instanceof ShrineBlock
                                       || block1 instanceof DungeonChestBlock
                                       || block2 instanceof ShrineBlock
                                       || block2 instanceof DungeonChestBlock) {
                                       treasureRoom = true;
                                    }
                                 }
                              }

                              boolean hallwayEnt = true;
                              boolean respawn = false;
                              BeyondSpawnUtils.spawnMobsAt(serverWorld, spawnPos, hallwayEnt, respawn, treasureRoom);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Nullable
   private static BlockPos getRandomSpawnDestination(BlockPos sourcePos, Level level) {
      int spawnFloor = BeyondSpawnUtils.GetFloorFromBlockPos(sourcePos);
      if ((double)Mth.nextFloat(level.getRandom(), 0.0F, 1.0F) < 0.3) {
         spawnFloor++;
      }

      int spawnHeight = getFloorHeightFromFloor(spawnFloor);
      BlockPos spawnPos = sourcePos.atY(spawnHeight);
      RandomSource random = level.getRandom();

      for (int i = 0; i < 40; i++) {
         float randomDirection = random.nextFloat() * (float) Math.PI * 2.0F;
         int randomDistance = random.nextIntBetweenInclusive(15, 40);
         int dx = (int)(Mth.cos(randomDirection) * (float)randomDistance);
         int dz = (int)(Mth.sin(randomDirection) * (float)randomDistance);
         BlockPos attemptPos = spawnPos.offset(dx, 0, dz);
         if (!level.getBlockState(attemptPos).isAir() && level.getBlockState(attemptPos.offset(0, 1, 0)).isAir()) {
            dx = 0;
            dz = 0;

            for (int x = -1; x <= 1; x++) {
               for (int z = -1; z <= 1; z++) {
                  BlockPos offsetPos = attemptPos.offset(dx, 1, dz);
                  if (level.getBlockState(offsetPos).isSuffocating(level, offsetPos)) {
                     dx = dx == 0 ? x * -1 : dx;
                     dz = dz == 0 ? z * -1 : dz;
                  }
               }
            }

            return attemptPos.offset(dx, 1, dz);
         }
      }

      return null;
   }

   private static int getFloorHeightFromFloor(int floorNumber) {
      if (floorNumber > 1 && floorNumber < 10) {
         return 131 - floorNumber * 16;
      } else {
         return floorNumber < 20 ? 87 - floorNumber * 12 : -1;
      }
   }

   private static int getRandomInRange(int min, int max) {
      return min + (int)(Math.random() * (double)(max - min + 1));
   }

   private static record randomSpawnInfo(Player player, int nearbySpawns, int floor) {
   }
}
