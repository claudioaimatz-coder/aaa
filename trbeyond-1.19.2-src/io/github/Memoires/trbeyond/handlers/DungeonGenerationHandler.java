package io.github.Memoires.trbeyond.handlers;

import io.github.Memoires.trbeyond.Database;
import io.github.Memoires.trbeyond.block.DungeonAnchor;
import io.github.Memoires.trbeyond.block.DungeonUnlockerBlock;
import io.github.Memoires.trbeyond.block.entity.AnchorBlockEntity;
import io.github.Memoires.trbeyond.network.worldgen.DungeonSaveData;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlocks;
import io.github.Memoires.trbeyond.util.FunctionSpecifications;
import io.github.Memoires.trbeyond.util.MatrixTraversal;
import io.github.Memoires.trbeyond.util.MutableBlockPos;
import io.github.Memoires.trbeyond.world.BeyondGamerules;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules.IntegerValue;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.event.level.LevelEvent.Load;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.FORGE
)
public class DungeonGenerationHandler {
   @SubscribeEvent
   public static void loadDungeon(Load event) {
      if (!event.isCanceled()) {
         if (event.isCanceled()) {
            if (event.getLevel() instanceof ServerLevel serverLevel) {
               if (serverLevel.dimension() == BeyondDungeon.DUNGEON_LEVEL_KEY) {
                  MinecraftServer server = serverLevel.getServer();
                  DungeonSaveData saveData = DungeonSaveData.get(server.m_129783_());
                  if (!saveData.isLoaded()) {
                     ServerLevel level = server.m_129880_(BeyondDungeon.DUNGEON_LEVEL_KEY);
                     if (level != null) {
                        saveData.setLoaded(true);
                        BlockPos destinationPos = new BlockPos(
                           saveData.getStartGenPos().x(), saveData.getStartGenPos().y(), saveData.getStartGenPos().z()
                        );
                        BlockState state = serverLevel.getBlockState(destinationPos);
                        if (!state.hasProperty(DungeonAnchor.GENERATED)) {
                           serverLevel.setBlockAndUpdate(destinationPos, ((DungeonAnchor)BeyondBlocks.DUNGEON_ANCHOR.get()).defaultBlockState());
                           if (serverLevel.getBlockEntity(destinationPos) instanceof AnchorBlockEntity anchor) {
                              anchor.setCurrentFloor(0);
                           }

                           StructureTemplateManager manager = serverLevel.getStructureManager();
                           StructurePlaceSettings settings = new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(true);
                           BlockPos leftPos = destinationPos.below(20).below(5).west(15);
                           placeStructure(manager, serverLevel, leftPos, "dungeon/enter/enter_back_left", settings);
                           BlockPos rightPos = leftPos.north(12);
                           placeStructure(manager, serverLevel, rightPos, "dungeon/enter/enter_back_right", settings);
                           BlockPos leftEnterPos = leftPos.east(17).above(5);
                           placeStructure(manager, serverLevel, leftEnterPos, "dungeon/enter/enter_forward_left", settings);
                           BlockPos rightEnterPos = rightPos.east(17).above(5);
                           placeStructure(manager, serverLevel, rightEnterPos, "dungeon/enter/enter_forward_right", settings);
                           StructurePlaceSettings hallwaySettings = new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(true);
                           String direction = "west";
                           BlockPos firstHallwayPos = destinationPos.below(25).west(29).north(2);
                           placeStructure(manager, serverLevel, firstHallwayPos, "dungeon/hallway/thin_hallway", hallwaySettings);
                           MutableBlockPos previousHallwayPos = new MutableBlockPos(firstHallwayPos, hallwaySettings, direction);
                           generateMaze(previousHallwayPos, manager, serverLevel);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   static void placeBossRoom(StructureTemplateManager manager, ServerLevel destinationWorld, MutableBlockPos previousHallwayPos) {
      BlockPos basePos = previousHallwayPos.pos;
      Direction dir;
      if (Objects.equals(previousHallwayPos.direction, "west")) {
         dir = Direction.WEST;
         basePos = previousHallwayPos.pos.south(2).above(2);
      } else if (Objects.equals(previousHallwayPos.direction, "south")) {
         dir = Direction.SOUTH;
         basePos = previousHallwayPos.pos.east(2).above(2);
      } else if (Objects.equals(previousHallwayPos.direction, "north")) {
         dir = Direction.NORTH;
         basePos = previousHallwayPos.pos.west(2).above(2);
      } else if (Objects.equals(previousHallwayPos.direction, "east")) {
         dir = Direction.EAST;
         basePos = previousHallwayPos.pos.north(2).above(2);
      } else {
         dir = Direction.WEST;
         basePos = previousHallwayPos.pos.south(2).above(2);
      }

      StructurePlaceSettings settings = new StructurePlaceSettings()
         .setMirror(Mirror.NONE)
         .setRotation(BeyondDungeon.getRotFromFacing(Rotation.CLOCKWISE_90.rotate(dir)))
         .setIgnoreEntities(true);
      BlockPos rightPart = basePos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), 26).relative(dir, 15).below(2);
      placeStructure(manager, destinationWorld, rightPart, "dungeon/room_boss/10bossroom_1", settings);
      BlockPos placePos = basePos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), 10).relative(dir, 15).below(2);
      placeStructure(manager, destinationWorld, placePos, "dungeon/room_boss/10bossroom_2", settings);
      BlockPos leftPart = basePos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), -6).relative(dir, 15).below(2);
      placeStructure(manager, destinationWorld, leftPart, "dungeon/room_boss/10bossroom_3", settings);
      BlockPos leftPart2 = basePos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), -22).relative(dir, 15).below(2);
      placeStructure(manager, destinationWorld, leftPart2, "dungeon/room_boss/10bossroom_4", settings);
      BlockPos rightPart2 = basePos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), 26).relative(dir, 31).below(2);
      placeStructure(manager, destinationWorld, rightPart2, "dungeon/room_boss/10bossroom_5", settings);
      BlockPos placePos2 = basePos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), 10).relative(dir, 31).below(2);
      placeStructure(manager, destinationWorld, placePos2, "dungeon/room_boss/10bossroom_6", settings);
      BlockPos leftPart3 = basePos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), -6).relative(dir, 31).below(2);
      placeStructure(manager, destinationWorld, leftPart3, "dungeon/room_boss/10bossroom_7", settings);
      BlockPos leftPart4 = basePos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), -22).relative(dir, 31).below(2);
      placeStructure(manager, destinationWorld, leftPart4, "dungeon/room_boss/10bossroom_8", settings);
      BlockPos rightPart3 = basePos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), 26).relative(dir, 47).below(2);
      placeStructure(manager, destinationWorld, rightPart3, "dungeon/room_boss/10bossroom_9", settings);
      BlockPos placePos3 = basePos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), 10).relative(dir, 47).below(2);
      placeStructure(manager, destinationWorld, placePos3, "dungeon/room_boss/10bossroom_10", settings);
      BlockPos leftPart5 = basePos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), -6).relative(dir, 47).below(2);
      placeStructure(manager, destinationWorld, leftPart5, "dungeon/room_boss/10bossroom_11", settings);
      BlockPos leftPart6 = basePos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), -22).relative(dir, 47).below(2);
      placeStructure(manager, destinationWorld, leftPart6, "dungeon/room_boss/10bossroom_12", settings);
      BlockPos preLastPart = basePos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), 14).relative(dir, 52).below(2);
      placeStructure(manager, destinationWorld, preLastPart, "dungeon/room_boss/10bossroom_13", settings);
      BlockPos lastPart = basePos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), -1).relative(dir, 52).below(2);
      placeStructure(manager, destinationWorld, lastPart, "dungeon/room_boss/10bossroom_14", settings);
   }

   static void placeOnewayRoomWithoutDoors(StructureTemplateManager manager, ServerLevel destinationWorld, MutableBlockPos previousHallwayPos, String room_name) {
      BlockPos newHallwayPos = RoomblockPosByDirectionInverted(previousHallwayPos.direction, 10, previousHallwayPos.pos);
      placeStructure(manager, destinationWorld, newHallwayPos, room_name, previousHallwayPos.hallwaySettings);
      if (Objects.equals(previousHallwayPos.direction, "east")) {
         previousHallwayPos.pos = newHallwayPos.north(3);
      } else if (Objects.equals(previousHallwayPos.direction, "west")) {
         previousHallwayPos.pos = newHallwayPos.south(3);
      } else if (Objects.equals(previousHallwayPos.direction, "south")) {
         previousHallwayPos.pos = newHallwayPos.east(3);
      } else if (Objects.equals(previousHallwayPos.direction, "north")) {
         previousHallwayPos.pos = newHallwayPos.west(3);
      }

      placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, "dungeon/hallway/thin_hallway");
   }

   static void placeHallwayRandomSideRoom(StructureTemplateManager manager, ServerLevel destinationWorld, MutableBlockPos previousHallwayPos) {
      int prob = getRandomInRange(1, 9);
      if (prob == 9) {
         placeHallwayBothSideRoom(manager, destinationWorld, previousHallwayPos);
      } else {
         if (prob <= 4) {
            placeHallwayLeftSideRoom(manager, destinationWorld, previousHallwayPos);
         } else {
            placeHallwayRightSideRoom(manager, destinationWorld, previousHallwayPos);
         }
      }
   }

   static void placeHallwayBothSideRoom(StructureTemplateManager manager, ServerLevel destinationWorld, MutableBlockPos previousHallwayPos) {
      placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, "dungeon/hallway/2doorway-thin");
      String originalDirection = previousHallwayPos.direction;
      BlockPos originalPos = previousHallwayPos.pos;
      StructurePlaceSettings originalSettings = previousHallwayPos.hallwaySettings;
      if (Objects.equals(previousHallwayPos.direction, "west")) {
         String room_name = "dungeon/room/small_room_end";
         String direction = "south";
         previousHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
         int distance = 15;
         BlockPos prevPos = previousHallwayPos.pos;
         BlockPos pos = prevPos.south(distance).east(2);
         placeStructure(manager, destinationWorld, pos, room_name, previousHallwayPos.hallwaySettings);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, pos.east(5).north(10).above(2), Rotation.NONE, true);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
      } else if (Objects.equals(previousHallwayPos.direction, "east")) {
         String room_name = "dungeon/room/small_room_end";
         String direction = "north";
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
         int distance = 15;
         BlockPos prevPos = previousHallwayPos.pos;
         BlockPos var43 = prevPos.north(distance).west(2);
         placeStructure(manager, destinationWorld, var43, room_name, previousHallwayPos.hallwaySettings);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, var43.west(5).south(10).above(2), Rotation.CLOCKWISE_180, true);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
      } else if (Objects.equals(previousHallwayPos.direction, "south")) {
         String room_name = "dungeon/room/small_room_end";
         String direction = "east";
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
         int distance = 15;
         BlockPos prevPos = previousHallwayPos.pos;
         BlockPos var44 = prevPos.east(distance).north(2);
         placeStructure(manager, destinationWorld, var44, room_name, previousHallwayPos.hallwaySettings);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, var44.north(5).west(10).above(2), Rotation.COUNTERCLOCKWISE_90, true);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
      } else if (Objects.equals(previousHallwayPos.direction, "north")) {
         String room_name = "dungeon/room/small_room_end";
         String direction = "west";
         previousHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
         int distance = 15;
         BlockPos prevPos = previousHallwayPos.pos;
         BlockPos var45 = prevPos.west(distance).south(2);
         placeStructure(manager, destinationWorld, var45, room_name, previousHallwayPos.hallwaySettings);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, var45.south(5).east(10).above(2), Rotation.CLOCKWISE_90, true);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
      }

      previousHallwayPos.direction = originalDirection;
      previousHallwayPos.pos = originalPos;
      originalDirection = previousHallwayPos.direction;
      originalPos = previousHallwayPos.pos;
      originalSettings = previousHallwayPos.hallwaySettings;
      if (Objects.equals(previousHallwayPos.direction, "west")) {
         String room_name = "dungeon/room/small_room_end";
         String direction = "north";
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
         int distance = 11;
         BlockPos prevPos = previousHallwayPos.pos;
         BlockPos var46 = prevPos.north(distance).east(12);
         placeStructure(manager, destinationWorld, var46, room_name, previousHallwayPos.hallwaySettings);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, var46.west(5).south(10).above(2), Rotation.NONE, true);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
      } else if (Objects.equals(previousHallwayPos.direction, "east")) {
         String room_name = "dungeon/room/small_room_end";
         String direction = "south";
         previousHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
         int distance = 11;
         BlockPos prevPos = previousHallwayPos.pos;
         BlockPos var47 = prevPos.south(distance).west(12);
         placeStructure(manager, destinationWorld, var47, room_name, previousHallwayPos.hallwaySettings);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, var47.east(5).north(10).above(2), Rotation.CLOCKWISE_180, true);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
      } else if (Objects.equals(previousHallwayPos.direction, "south")) {
         String room_name = "dungeon/room/small_room_end";
         String direction = "west";
         previousHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
         int distance = 11;
         BlockPos prevPos = previousHallwayPos.pos;
         BlockPos var48 = prevPos.west(distance).north(12);
         placeStructure(manager, destinationWorld, var48, room_name, previousHallwayPos.hallwaySettings);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, var48.south(5).east(10).above(2), Rotation.COUNTERCLOCKWISE_90, true);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
      } else if (Objects.equals(previousHallwayPos.direction, "north")) {
         String room_name = "dungeon/room/small_room_end";
         String direction = "east";
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
         int distance = 11;
         BlockPos prevPos = previousHallwayPos.pos;
         BlockPos var49 = prevPos.east(distance).south(12);
         placeStructure(manager, destinationWorld, var49, room_name, previousHallwayPos.hallwaySettings);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, var49.north(5).west(10).above(2), Rotation.CLOCKWISE_90, true);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
      }

      previousHallwayPos.direction = originalDirection;
      previousHallwayPos.pos = originalPos;
      placeThinHallways(manager, destinationWorld, previousHallwayPos, 2, "dungeon/hallway/long_thin_hallway");
   }

   static void placeHallwayRightSideRoom(StructureTemplateManager manager, ServerLevel destinationWorld, MutableBlockPos previousHallwayPos) {
      placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, "dungeon/hallway/1doorwayright-thin");
      String originalDirection = previousHallwayPos.direction;
      BlockPos originalPos = previousHallwayPos.pos;
      StructurePlaceSettings originalSettings = previousHallwayPos.hallwaySettings;
      if (Objects.equals(previousHallwayPos.direction, "west")) {
         String room_name = "dungeon/room/small_room_end";
         String direction = "north";
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
         int distance = 11;
         BlockPos prevPos = previousHallwayPos.pos;
         BlockPos pos = prevPos.north(distance).east(12);
         placeStructure(manager, destinationWorld, pos, room_name, previousHallwayPos.hallwaySettings);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, pos.west(5).south(10).above(2), Rotation.NONE, true);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
      } else if (Objects.equals(previousHallwayPos.direction, "east")) {
         String room_name = "dungeon/room/small_room_end";
         String direction = "south";
         previousHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
         int distance = 11;
         BlockPos prevPos = previousHallwayPos.pos;
         BlockPos var24 = prevPos.south(distance).west(12);
         placeStructure(manager, destinationWorld, var24, room_name, previousHallwayPos.hallwaySettings);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, var24.east(5).north(10).above(2), Rotation.CLOCKWISE_180, true);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
      } else if (Objects.equals(previousHallwayPos.direction, "south")) {
         String room_name = "dungeon/room/small_room_end";
         String direction = "west";
         previousHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
         int distance = 11;
         BlockPos prevPos = previousHallwayPos.pos;
         BlockPos var25 = prevPos.west(distance).north(12);
         placeStructure(manager, destinationWorld, var25, room_name, previousHallwayPos.hallwaySettings);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, var25.south(5).east(10).above(2), Rotation.COUNTERCLOCKWISE_90, true);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
      } else if (Objects.equals(previousHallwayPos.direction, "north")) {
         String room_name = "dungeon/room/small_room_end";
         String direction = "east";
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
         int distance = 11;
         BlockPos prevPos = previousHallwayPos.pos;
         BlockPos var26 = prevPos.east(distance).south(12);
         placeStructure(manager, destinationWorld, var26, room_name, previousHallwayPos.hallwaySettings);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, var26.north(5).west(10).above(2), Rotation.CLOCKWISE_90, true);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
      }

      previousHallwayPos.direction = originalDirection;
      previousHallwayPos.pos = originalPos;
      placeThinHallways(manager, destinationWorld, previousHallwayPos, 2, "dungeon/hallway/long_thin_hallway");
   }

   static void placeHallwayLeftSideRoom(StructureTemplateManager manager, ServerLevel destinationWorld, MutableBlockPos previousHallwayPos) {
      placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, "dungeon/hallway/1doorwayleft-thin");
      String originalDirection = previousHallwayPos.direction;
      BlockPos originalPos = previousHallwayPos.pos;
      StructurePlaceSettings originalSettings = previousHallwayPos.hallwaySettings;
      if (Objects.equals(previousHallwayPos.direction, "west")) {
         String room_name = "dungeon/room/small_room_end";
         String direction = "south";
         previousHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
         int distance = 15;
         BlockPos prevPos = previousHallwayPos.pos;
         BlockPos pos = prevPos.south(distance).east(2);
         placeStructure(manager, destinationWorld, pos, room_name, previousHallwayPos.hallwaySettings);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, pos.east(5).north(10).above(2), Rotation.NONE, true);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
      } else if (Objects.equals(previousHallwayPos.direction, "east")) {
         String room_name = "dungeon/room/small_room_end";
         String direction = "north";
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
         int distance = 15;
         BlockPos prevPos = previousHallwayPos.pos;
         BlockPos var24 = prevPos.north(distance).west(2);
         placeStructure(manager, destinationWorld, var24, room_name, previousHallwayPos.hallwaySettings);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, var24.west(5).south(10).above(2), Rotation.CLOCKWISE_180, true);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
      } else if (Objects.equals(previousHallwayPos.direction, "south")) {
         String room_name = "dungeon/room/small_room_end";
         String direction = "east";
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
         int distance = 15;
         BlockPos prevPos = previousHallwayPos.pos;
         BlockPos var25 = prevPos.east(distance).north(2);
         placeStructure(manager, destinationWorld, var25, room_name, previousHallwayPos.hallwaySettings);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, var25.north(5).west(10).above(2), Rotation.COUNTERCLOCKWISE_90, true);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
      } else if (Objects.equals(previousHallwayPos.direction, "north")) {
         String room_name = "dungeon/room/small_room_end";
         String direction = "west";
         previousHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
         int distance = 15;
         BlockPos prevPos = previousHallwayPos.pos;
         BlockPos var26 = prevPos.west(distance).south(2);
         placeStructure(manager, destinationWorld, var26, room_name, previousHallwayPos.hallwaySettings);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, var26.south(5).east(10).above(2), Rotation.CLOCKWISE_90, true);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
      }

      previousHallwayPos.direction = originalDirection;
      previousHallwayPos.pos = originalPos;
   }

   static void generateMaze(MutableBlockPos previousHallwayPos, StructureTemplateManager manager, ServerLevel destinationWorld) {
      placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, "dungeon/hallway/long_thin_hallway");
      int floorCount = ((IntegerValue)destinationWorld.getGameRules().getRule(BeyondGamerules.DUNGEON_FLOOR_SIZE)).get();
      MatrixTraversal traversal = new MatrixTraversal(floorCount, 0.75, 0.6, previousHallwayPos.direction);
      traversal.generateMaze();

      for (FunctionSpecifications func : traversal.getFunctions()) {
         if (Objects.equals(func.getFunctionName(), "hallway")) {
            int prob = getRandomInRange(1, 6);
            if (prob == 6) {
               placeHallwayRandomSideRoom(manager, destinationWorld, previousHallwayPos);
            } else {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, "dungeon/hallway/long_thin_hallway");
            }
         } else if (Objects.equals(func.getFunctionName(), "deadend")) {
            placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, "dungeon/hallway/thin_hallway_end");
         } else if (Objects.equals(func.getFunctionName(), "corner_deadend")) {
            placeCornerHallway(previousHallwayPos, manager, destinationWorld, "dungeon/hallway/thin_hallway_end");
         } else if (Objects.equals(func.getFunctionName(), "stairs")) {
            placeStairs(previousHallwayPos, manager, destinationWorld);
         } else if (Objects.equals(func.getFunctionName(), "room")) {
            int prob = getRandomInRange(1, 3);
            if (prob == 3) {
               int prob2 = getRandomInRange(1, 10);
               if (prob2 == 10) {
                  placeOnewayRoomWithoutDoors(manager, destinationWorld, previousHallwayPos, "dungeon/treasure/gold_chest_loot");
               } else if (prob2 > 6) {
                  placeOnewayRoomWithoutDoors(manager, destinationWorld, previousHallwayPos, "dungeon/treasure/silver_chest_loot");
               } else {
                  placeOnewayRoomWithoutDoors(manager, destinationWorld, previousHallwayPos, "dungeon/treasure/bronze_chest_loot");
               }
            } else {
               placeOnewayRoom(manager, destinationWorld, previousHallwayPos, "dungeon/room/small_room_2");
            }
         } else if (Objects.equals(func.getFunctionName(), "corner")) {
            placeCornerHallway(previousHallwayPos, manager, destinationWorld, "dungeon/hallway/long_thin_hallway");
         } else if (Objects.equals(func.getFunctionName(), "three_way")) {
            place2wayHallway(previousHallwayPos, manager, destinationWorld, func.getSuccessDirection(), func.getNotSuccessCase(), func.getHallwayLen());
         } else {
            placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, "dungeon/hallway/long_thin_hallway");
         }
      }
   }

   static void presetMaze_1(MutableBlockPos previousHallwayPos, StructureTemplateManager manager, ServerLevel destinationWorld) {
      String thin_hallway = "dungeon/hallway/thin_hallway";
      String thin_hallway_end = "dungeon/hallway/thin_hallway_end";
      String room_name = "dungeon/room/small_room_2";
      int n = 2;
      placeThinHallways(manager, destinationWorld, previousHallwayPos, n, thin_hallway);
      placeCornerHallway(previousHallwayPos, manager, destinationWorld, thin_hallway);
      placeThinHallways(manager, destinationWorld, previousHallwayPos, n, thin_hallway);
      placeOnewayRoom(manager, destinationWorld, previousHallwayPos, room_name);
      place2wayHallway(previousHallwayPos, manager, destinationWorld, "left", "loopback", 1);
      placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, thin_hallway);
      placeOnewayRoom(manager, destinationWorld, previousHallwayPos, room_name);
      placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, thin_hallway);
      placeOnewayRoom(manager, destinationWorld, previousHallwayPos, room_name);
      place2wayHallway(previousHallwayPos, manager, destinationWorld, "left", "loopback", 1);
      placeThinHallways(manager, destinationWorld, previousHallwayPos, 3, thin_hallway);
      place2wayHallway(previousHallwayPos, manager, destinationWorld, "right", "loopback", 1);
      placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, thin_hallway);
      placeOnewayRoom(manager, destinationWorld, previousHallwayPos, room_name);
      place2wayHallway(previousHallwayPos, manager, destinationWorld, "right", "deadend", 6);
      placeThinHallways(manager, destinationWorld, previousHallwayPos, 2, thin_hallway);
      place2wayHallway(previousHallwayPos, manager, destinationWorld, "left", "deadend", 2);
      placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, thin_hallway);
      placeCornerHallway(previousHallwayPos, manager, destinationWorld, thin_hallway);
      placeThinHallways(manager, destinationWorld, previousHallwayPos, 5, thin_hallway);
      placeCornerHallway(previousHallwayPos, manager, destinationWorld, thin_hallway);
      placeStairs(previousHallwayPos, manager, destinationWorld);
   }

   static void placeStairswithoutGeneration(MutableBlockPos previousHallwayPos, StructureTemplateManager manager, ServerLevel destinationWorld, int numStairs) {
      if (Objects.equals(previousHallwayPos.direction, "east")) {
         BlockPos finalStairCasePos = previousHallwayPos.pos.east(9).below(8);
         placeStructure(manager, destinationWorld, finalStairCasePos, "dungeon/stairs/small_stairs", previousHallwayPos.hallwaySettings);
         previousHallwayPos.stairsplaced = true;
         MutableBlockPos newHallwayPos = new MutableBlockPos(
            previousHallwayPos.pos.north(8).west(0).below(8), previousHallwayPos.hallwaySettings, previousHallwayPos.direction
         );
         newHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
         newHallwayPos.direction = "west";
         newHallwayPos.floorcount = previousHallwayPos.floorcount + 1;
         newHallwayPos.stairsplaced = false;
         if (numStairs == 0) {
            for (int i = 0; i < 2; i++) {
               BlockPos newBlockPos = blockPosByDirection(newHallwayPos.direction, 14, newHallwayPos.pos);
               placeStructure(manager, destinationWorld, newBlockPos, "dungeon/hallway/thin_hallway", previousHallwayPos.hallwaySettings);
               newHallwayPos.pos = newBlockPos;
            }

            placeBossRoom(manager, destinationWorld, newHallwayPos);
            return;
         }

         placeStairswithoutGeneration(newHallwayPos, manager, destinationWorld, numStairs - 1);
      } else if (Objects.equals(previousHallwayPos.direction, "south")) {
         BlockPos finalStairCasePos = previousHallwayPos.pos.south(9).below(8);
         placeStructure(manager, destinationWorld, finalStairCasePos, "dungeon/stairs/small_stairs", previousHallwayPos.hallwaySettings);
         previousHallwayPos.stairsplaced = true;
         MutableBlockPos newHallwayPos = new MutableBlockPos(
            previousHallwayPos.pos.east(8).north(0).below(8), previousHallwayPos.hallwaySettings, previousHallwayPos.direction
         );
         newHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
         newHallwayPos.direction = "north";
         newHallwayPos.floorcount = previousHallwayPos.floorcount + 1;
         newHallwayPos.stairsplaced = false;
         if (numStairs == 0) {
            for (int i = 0; i < 2; i++) {
               BlockPos newBlockPos = blockPosByDirection(newHallwayPos.direction, 14, newHallwayPos.pos);
               placeStructure(manager, destinationWorld, newBlockPos, "dungeon/hallway/thin_hallway", previousHallwayPos.hallwaySettings);
               newHallwayPos.pos = newBlockPos;
            }

            placeBossRoom(manager, destinationWorld, newHallwayPos);
            return;
         }

         placeStairswithoutGeneration(newHallwayPos, manager, destinationWorld, numStairs - 1);
      } else if (Objects.equals(previousHallwayPos.direction, "west")) {
         BlockPos finalStairCasePos = previousHallwayPos.pos.west(9).below(8);
         placeStructure(manager, destinationWorld, finalStairCasePos, "dungeon/stairs/small_stairs", previousHallwayPos.hallwaySettings);
         previousHallwayPos.stairsplaced = true;
         MutableBlockPos newHallwayPos = new MutableBlockPos(
            previousHallwayPos.pos.south(8).east(0).below(8), previousHallwayPos.hallwaySettings, previousHallwayPos.direction
         );
         newHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
         newHallwayPos.direction = "east";
         newHallwayPos.floorcount = previousHallwayPos.floorcount + 1;
         newHallwayPos.stairsplaced = false;
         if (numStairs == 0) {
            for (int i = 0; i < 2; i++) {
               BlockPos newBlockPos = blockPosByDirection(newHallwayPos.direction, 14, newHallwayPos.pos);
               placeStructure(manager, destinationWorld, newBlockPos, "dungeon/hallway/thin_hallway", previousHallwayPos.hallwaySettings);
               newHallwayPos.pos = newBlockPos;
            }

            placeBossRoom(manager, destinationWorld, newHallwayPos);
            return;
         }

         placeStairswithoutGeneration(newHallwayPos, manager, destinationWorld, numStairs - 1);
      } else if (Objects.equals(previousHallwayPos.direction, "north")) {
         BlockPos finalStairCasePos = previousHallwayPos.pos.north(9).below(8);
         placeStructure(manager, destinationWorld, finalStairCasePos, "dungeon/stairs/small_stairs", previousHallwayPos.hallwaySettings);
         previousHallwayPos.stairsplaced = true;
         MutableBlockPos newHallwayPos = new MutableBlockPos(
            previousHallwayPos.pos.west(8).south(0).below(8), previousHallwayPos.hallwaySettings, previousHallwayPos.direction
         );
         newHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
         newHallwayPos.direction = "south";
         newHallwayPos.floorcount = previousHallwayPos.floorcount + 1;
         newHallwayPos.stairsplaced = false;
         if (numStairs == 0) {
            for (int i = 0; i < 2; i++) {
               BlockPos newBlockPos = blockPosByDirection(newHallwayPos.direction, 14, newHallwayPos.pos);
               placeStructure(manager, destinationWorld, newBlockPos, "dungeon/hallway/thin_hallway", previousHallwayPos.hallwaySettings);
               newHallwayPos.pos = newBlockPos;
            }

            placeBossRoom(manager, destinationWorld, newHallwayPos);
            return;
         }

         placeStairswithoutGeneration(newHallwayPos, manager, destinationWorld, numStairs - 1);
      }
   }

   static void placeStairs(MutableBlockPos previousHallwayPos, StructureTemplateManager manager, ServerLevel destinationWorld) {
      if (!previousHallwayPos.stairsplaced && previousHallwayPos.floorcount < 10) {
         if (Objects.equals(previousHallwayPos.direction, "east")) {
            BlockPos finalStairCasePos = previousHallwayPos.pos.east(9).below(8);
            placeStructure(manager, destinationWorld, finalStairCasePos, "dungeon/stairs/small_stairs", previousHallwayPos.hallwaySettings);
            previousHallwayPos.stairsplaced = true;
            MutableBlockPos newHallwayPos = new MutableBlockPos(
               previousHallwayPos.pos.north(8).west(4).below(8), previousHallwayPos.hallwaySettings, previousHallwayPos.direction
            );
            newHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
            newHallwayPos.direction = "west";
            newHallwayPos.floorcount = previousHallwayPos.floorcount + 1;
            newHallwayPos.stairsplaced = false;
            generateMaze(newHallwayPos, manager, destinationWorld);
         } else if (Objects.equals(previousHallwayPos.direction, "south")) {
            BlockPos finalStairCasePos = previousHallwayPos.pos.south(9).below(8);
            placeStructure(manager, destinationWorld, finalStairCasePos, "dungeon/stairs/small_stairs", previousHallwayPos.hallwaySettings);
            previousHallwayPos.stairsplaced = true;
            MutableBlockPos newHallwayPos = new MutableBlockPos(
               previousHallwayPos.pos.east(8).north(4).below(8), previousHallwayPos.hallwaySettings, previousHallwayPos.direction
            );
            newHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
            newHallwayPos.direction = "north";
            newHallwayPos.floorcount = previousHallwayPos.floorcount + 1;
            newHallwayPos.stairsplaced = false;
            generateMaze(newHallwayPos, manager, destinationWorld);
         } else if (Objects.equals(previousHallwayPos.direction, "west")) {
            BlockPos finalStairCasePos = previousHallwayPos.pos.west(9).below(8);
            placeStructure(manager, destinationWorld, finalStairCasePos, "dungeon/stairs/small_stairs", previousHallwayPos.hallwaySettings);
            previousHallwayPos.stairsplaced = true;
            MutableBlockPos newHallwayPos = new MutableBlockPos(
               previousHallwayPos.pos.south(8).east(4).below(8), previousHallwayPos.hallwaySettings, previousHallwayPos.direction
            );
            newHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
            newHallwayPos.direction = "east";
            newHallwayPos.floorcount = previousHallwayPos.floorcount + 1;
            newHallwayPos.stairsplaced = false;
            generateMaze(newHallwayPos, manager, destinationWorld);
         } else if (Objects.equals(previousHallwayPos.direction, "north")) {
            BlockPos finalStairCasePos = previousHallwayPos.pos.north(9).below(8);
            placeStructure(manager, destinationWorld, finalStairCasePos, "dungeon/stairs/small_stairs", previousHallwayPos.hallwaySettings);
            previousHallwayPos.stairsplaced = true;
            MutableBlockPos newHallwayPos = new MutableBlockPos(
               previousHallwayPos.pos.west(8).south(4).below(8), previousHallwayPos.hallwaySettings, previousHallwayPos.direction
            );
            newHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
            newHallwayPos.direction = "south";
            newHallwayPos.floorcount = previousHallwayPos.floorcount + 1;
            newHallwayPos.stairsplaced = false;
            generateMaze(newHallwayPos, manager, destinationWorld);
         }
      } else {
         if (previousHallwayPos.floorcount >= 10) {
            placeStairswithoutGeneration(previousHallwayPos, manager, destinationWorld, 3);
            return;
         }

         placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, "dungeon/hallway/thin_hallway_end");
      }
   }

   static void placeDeadendRoom(StructureTemplateManager manager, ServerLevel destinationWorld, MutableBlockPos previousHallwayPos) {
      String room_name = "dungeon/room/small_room_end";
      BlockPos newHallwayPos = RoomblockPosByDirectionInverted(previousHallwayPos.direction, 11, previousHallwayPos.pos);
      placeStructure(manager, destinationWorld, newHallwayPos, room_name, previousHallwayPos.hallwaySettings);
      if (Objects.equals(previousHallwayPos.direction, "east")) {
         previousHallwayPos.pos = newHallwayPos.north(3);
      } else if (Objects.equals(previousHallwayPos.direction, "west")) {
         previousHallwayPos.pos = newHallwayPos.south(3);
      } else if (Objects.equals(previousHallwayPos.direction, "south")) {
         previousHallwayPos.pos = newHallwayPos.east(3);
      } else if (Objects.equals(previousHallwayPos.direction, "north")) {
         previousHallwayPos.pos = newHallwayPos.west(3);
      }
   }

   public static BlockPos RoomblockPosByDirectionInverted(String direction, int distance, BlockPos prevPos) {
      BlockPos pos;
      if (Objects.equals(direction, "east")) {
         pos = prevPos.east(distance).south(3);
      } else if (Objects.equals(direction, "west")) {
         pos = prevPos.west(distance).north(3);
      } else if (Objects.equals(direction, "south")) {
         pos = prevPos.south(distance).west(3);
      } else {
         pos = prevPos.north(distance).east(3);
      }

      return pos;
   }

   static void placeOnewayRoom(StructureTemplateManager manager, ServerLevel destinationWorld, MutableBlockPos previousHallwayPos, String room_name) {
      MutableBlockPos.room_count++;
      BlockPos originalPos = previousHallwayPos.pos;
      BlockPos newHallwayPos = RoomblockPosByDirection(previousHallwayPos.direction, 0, previousHallwayPos.pos);
      if (Objects.equals(previousHallwayPos.direction, "east")) {
         placeStructure(manager, destinationWorld, newHallwayPos, room_name, previousHallwayPos.hallwaySettings.setRotation(Rotation.NONE));
         previousHallwayPos.pos = newHallwayPos.east(10).south(7);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
         if (!Objects.equals(room_name, "dungeon/room/small_room_end")) {
            placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, "dungeon/hallway/thin_hallway");
         }

         placeDungeonUnlockerBlockWithRotation(destinationWorld, originalPos.east(10).north(2).above(2), Rotation.CLOCKWISE_90, true);
      } else if (Objects.equals(previousHallwayPos.direction, "west")) {
         placeStructure(manager, destinationWorld, newHallwayPos, room_name, previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180));
         previousHallwayPos.pos = newHallwayPos.west(10).north(7);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
         if (!Objects.equals(room_name, "dungeon/room/small_room_end")) {
            placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, "dungeon/hallway/thin_hallway");
         }

         placeDungeonUnlockerBlockWithRotation(destinationWorld, originalPos.west(10).south(2).above(2), Rotation.COUNTERCLOCKWISE_90, true);
      } else if (Objects.equals(previousHallwayPos.direction, "south")) {
         placeStructure(manager, destinationWorld, newHallwayPos, room_name, previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90));
         previousHallwayPos.pos = newHallwayPos.south(10).west(7);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
         if (!Objects.equals(room_name, "dungeon/room/small_room_end")) {
            placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, "dungeon/hallway/thin_hallway");
         }

         placeDungeonUnlockerBlockWithRotation(destinationWorld, originalPos.south(10).east(2).above(2), Rotation.CLOCKWISE_180, true);
      } else if (Objects.equals(previousHallwayPos.direction, "north")) {
         placeStructure(manager, destinationWorld, newHallwayPos, room_name, previousHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90));
         previousHallwayPos.pos = newHallwayPos.north(10).east(7);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
         if (!Objects.equals(room_name, "dungeon/room/small_room_end")) {
            placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, "dungeon/hallway/thin_hallway");
         }

         placeDungeonUnlockerBlockWithRotation(destinationWorld, originalPos.north(10).west(2).above(2), Rotation.NONE, true);
      }
   }

   public static BlockPos RoomblockPosByDirection(String direction, int distance, BlockPos prevPos) {
      BlockPos pos;
      if (Objects.equals(direction, "east")) {
         pos = prevPos.east(distance).north(7);
      } else if (Objects.equals(direction, "west")) {
         pos = prevPos.west(distance).south(7);
      } else if (Objects.equals(direction, "south")) {
         pos = prevPos.south(distance).east(7);
      } else {
         pos = prevPos.north(distance).west(7);
      }

      return pos;
   }

   static void loopbackLeft(MutableBlockPos previousHallwayPos, StructureTemplateManager manager, ServerLevel destinationWorld) {
      int prob = getRandomInRange(1, 6);
      if (prob != 1 && prob != 2 && prob != 3 && prob != 4) {
         int probroomend = getRandomInRange(1, 6);
         placeCornerHallway(previousHallwayPos, manager, destinationWorld, "dungeon/hallway/thin_hallway");
         if (probroomend != 5 && probroomend != 6) {
            placeCornerHallway(previousHallwayPos, manager, destinationWorld, "dungeon/hallway/thin_hallway");
            placeDeadendRoom(manager, destinationWorld, previousHallwayPos);
         } else {
            placeCornerHallway(previousHallwayPos, manager, destinationWorld, "dungeon/hallway/thin_hallway_end");
         }
      } else {
         placeCornerHallway(previousHallwayPos, manager, destinationWorld, "dungeon/hallway/thin_hallway");
         placeCornerHallway(previousHallwayPos, manager, destinationWorld, "dungeon/hallway/thin_hallway");
         placeLongCornerHallway(previousHallwayPos, manager, destinationWorld, 19);
      }
   }

   static void loopbackRight(MutableBlockPos previousHallwayPos, StructureTemplateManager manager, ServerLevel destinationWorld) {
      int prob = getRandomInRange(1, 6);
      if (prob != 1 && prob != 2 && prob != 3 && prob != 4) {
         int probroomend = getRandomInRange(1, 6);
         placeCornerHallway(previousHallwayPos, manager, destinationWorld, "dungeon/hallway/thin_hallway");
         if (probroomend != 5 && probroomend != 6) {
            if (Objects.equals(previousHallwayPos.direction, "east")) {
               previousHallwayPos.pos = previousHallwayPos.pos.east(9).south(5);
               placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/thin_hallway_corner", previousHallwayPos.hallwaySettings);
               previousHallwayPos.hallwaySettings = previousHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
               previousHallwayPos.direction = "south";
               previousHallwayPos.pos = previousHallwayPos.pos.south(0).west(4);
            } else if (Objects.equals(previousHallwayPos.direction, "south")) {
               previousHallwayPos.pos = previousHallwayPos.pos.south(9).west(5);
               placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/thin_hallway_corner", previousHallwayPos.hallwaySettings);
               previousHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
               previousHallwayPos.direction = "west";
               previousHallwayPos.pos = previousHallwayPos.pos.west(0).north(4);
            } else if (Objects.equals(previousHallwayPos.direction, "west")) {
               previousHallwayPos.pos = previousHallwayPos.pos.west(9).north(5);
               placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/thin_hallway_corner", previousHallwayPos.hallwaySettings);
               previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
               previousHallwayPos.direction = "north";
               previousHallwayPos.pos = previousHallwayPos.pos.north(0).east(4);
            } else if (Objects.equals(previousHallwayPos.direction, "north")) {
               previousHallwayPos.pos = previousHallwayPos.pos.north(9).east(5);
               placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/thin_hallway_corner", previousHallwayPos.hallwaySettings);
               previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
               previousHallwayPos.direction = "east";
               previousHallwayPos.pos = previousHallwayPos.pos.east(0).south(4);
            }

            placeDeadendRoom(manager, destinationWorld, previousHallwayPos);
         } else {
            placeCornerHallway(previousHallwayPos, manager, destinationWorld, "dungeon/hallway/thin_hallway_end");
         }
      } else {
         int prob2 = getRandomInRange(1, 2);
         if (prob2 == 1) {
            placeCornerHallway(previousHallwayPos, manager, destinationWorld, "dungeon/hallway/thin_hallway");
            placeLongCornerHallway(previousHallwayPos, manager, destinationWorld, 19);
         } else {
            placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, "dungeon/hallway/thin_hallway");
            placeCornerHallway(previousHallwayPos, manager, destinationWorld, "dungeon/hallway/thin_hallway");
            placeCornerHallway(previousHallwayPos, manager, destinationWorld, "dungeon/hallway/thin_hallway");
            placeLongCornerHallway(previousHallwayPos, manager, destinationWorld, 19);
         }
      }
   }

   static void place2wayHallway(
      MutableBlockPos previousHallwayPos,
      StructureTemplateManager manager,
      ServerLevel destinationWorld,
      String success_direction_left_or_right,
      String not_success_loopback_or_deadend_or_stairs,
      int hallway_length
   ) {
      int left_success = 0;
      int not_success_case = 1;
      if (Objects.equals(not_success_loopback_or_deadend_or_stairs, "loopback")) {
         not_success_case = getRandomInRange(1, 3);
      } else if (Objects.equals(not_success_loopback_or_deadend_or_stairs, "deadend")) {
         not_success_case = getRandomInRange(4, 5);
      } else if (Objects.equals(not_success_loopback_or_deadend_or_stairs, "stairs")) {
         not_success_case = 6;
      }

      if (Objects.equals(success_direction_left_or_right, "left")) {
         left_success = 1;
      }

      if (Objects.equals(previousHallwayPos.direction, "east")) {
         previousHallwayPos.pos = previousHallwayPos.pos.east(9).south(5);
         placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/thin_hallway_t", previousHallwayPos.hallwaySettings);
         String original_direction = previousHallwayPos.direction;
         BlockPos original_pos = previousHallwayPos.pos;
         previousHallwayPos.direction = original_direction;
         if (left_success == 0) {
            previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
            previousHallwayPos.direction = "north";
            previousHallwayPos.pos = previousHallwayPos.pos.north(28);
            placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
            if (not_success_case == 1 || not_success_case == 2 || not_success_case == 3) {
               loopbackLeft(previousHallwayPos, manager, destinationWorld);
            } else if (not_success_case == 4 || not_success_case == 5) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, "dungeon/hallway/thin_hallway");
               BlockPos newHallwayPos = blockPosByDirection(previousHallwayPos.direction, 14, previousHallwayPos.pos);
               int probroomend = getRandomInRange(1, 6);
               if (probroomend != 5 && probroomend != 6) {
                  placeDeadendRoom(manager, destinationWorld, previousHallwayPos);
               } else {
                  placeStructure(manager, destinationWorld, newHallwayPos, "dungeon/hallway/thin_hallway_end", previousHallwayPos.hallwaySettings);
               }
            } else if (not_success_case == 6) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, "dungeon/hallway/thin_hallway");
               placeStairs(previousHallwayPos, manager, destinationWorld);
            }
         } else {
            previousHallwayPos.hallwaySettings = previousHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
            previousHallwayPos.direction = "south";
            previousHallwayPos.pos = previousHallwayPos.pos.south(14).west(4);
            placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
            if (not_success_case == 1 || not_success_case == 2 || not_success_case == 3) {
               loopbackRight(previousHallwayPos, manager, destinationWorld);
            } else if (not_success_case == 4 || not_success_case == 5) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, "dungeon/hallway/thin_hallway");
               BlockPos newHallwayPos = blockPosByDirection(previousHallwayPos.direction, 14, previousHallwayPos.pos);
               int probroomend = getRandomInRange(1, 6);
               if (probroomend != 5 && probroomend != 6) {
                  placeDeadendRoom(manager, destinationWorld, previousHallwayPos);
               } else {
                  placeStructure(manager, destinationWorld, newHallwayPos, "dungeon/hallway/thin_hallway_end", previousHallwayPos.hallwaySettings);
               }
            } else if (not_success_case == 6) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, "dungeon/hallway/thin_hallway");
               placeStairs(previousHallwayPos, manager, destinationWorld);
            }
         }

         previousHallwayPos.pos = original_pos;
         previousHallwayPos.direction = original_direction;
         if (left_success == 0) {
            previousHallwayPos.direction = original_direction;
            previousHallwayPos.hallwaySettings = previousHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
            previousHallwayPos.direction = "south";
            previousHallwayPos.pos = previousHallwayPos.pos.south(14).west(4);
            placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
         } else if (left_success == 1) {
            previousHallwayPos.direction = original_direction;
            previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
            previousHallwayPos.direction = "north";
            previousHallwayPos.pos = previousHallwayPos.pos.north(28);
            placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
         }
      } else if (Objects.equals(previousHallwayPos.direction, "south")) {
         previousHallwayPos.pos = previousHallwayPos.pos.south(9).west(5);
         placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/thin_hallway_t", previousHallwayPos.hallwaySettings);
         String original_directionx = previousHallwayPos.direction;
         BlockPos original_posx = previousHallwayPos.pos;
         previousHallwayPos.direction = original_directionx;
         if (left_success == 0) {
            previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
            previousHallwayPos.direction = "east";
            previousHallwayPos.pos = previousHallwayPos.pos.east(28);
            placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
            if (not_success_case == 1 || not_success_case == 2 || not_success_case == 3) {
               loopbackLeft(previousHallwayPos, manager, destinationWorld);
            } else if (not_success_case == 4 || not_success_case == 5) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, "dungeon/hallway/thin_hallway");
               BlockPos newHallwayPos = blockPosByDirection(previousHallwayPos.direction, 14, previousHallwayPos.pos);
               int probroomend = getRandomInRange(1, 6);
               if (probroomend != 5 && probroomend != 6) {
                  placeDeadendRoom(manager, destinationWorld, previousHallwayPos);
               } else {
                  placeStructure(manager, destinationWorld, newHallwayPos, "dungeon/hallway/thin_hallway_end", previousHallwayPos.hallwaySettings);
               }
            } else if (not_success_case == 6) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, "dungeon/hallway/thin_hallway");
               placeStairs(previousHallwayPos, manager, destinationWorld);
            }
         } else {
            previousHallwayPos.hallwaySettings = previousHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
            previousHallwayPos.direction = "west";
            previousHallwayPos.pos = previousHallwayPos.pos.west(14).north(4);
            placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
            if (not_success_case == 1 || not_success_case == 2 || not_success_case == 3) {
               loopbackRight(previousHallwayPos, manager, destinationWorld);
            } else if (not_success_case == 4 || not_success_case == 5) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, "dungeon/hallway/thin_hallway");
               BlockPos newHallwayPos = blockPosByDirection(previousHallwayPos.direction, 14, previousHallwayPos.pos);
               int probroomend = getRandomInRange(1, 6);
               if (probroomend != 5 && probroomend != 6) {
                  placeDeadendRoom(manager, destinationWorld, previousHallwayPos);
               } else {
                  placeStructure(manager, destinationWorld, newHallwayPos, "dungeon/hallway/thin_hallway_end", previousHallwayPos.hallwaySettings);
               }
            } else if (not_success_case == 6) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, "dungeon/hallway/thin_hallway");
               placeStairs(previousHallwayPos, manager, destinationWorld);
            }
         }

         previousHallwayPos.pos = original_posx;
         previousHallwayPos.direction = original_directionx;
         if (left_success == 0) {
            previousHallwayPos.direction = original_directionx;
            previousHallwayPos.hallwaySettings = previousHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
            previousHallwayPos.direction = "west";
            previousHallwayPos.pos = previousHallwayPos.pos.west(14).north(4);
            placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
         } else if (left_success == 1) {
            previousHallwayPos.direction = original_directionx;
            previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
            previousHallwayPos.direction = "east";
            previousHallwayPos.pos = previousHallwayPos.pos.east(28);
            placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
         }
      } else if (Objects.equals(previousHallwayPos.direction, "west")) {
         previousHallwayPos.pos = previousHallwayPos.pos.west(9).north(5);
         placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/thin_hallway_t", previousHallwayPos.hallwaySettings);
         String original_directionxx = previousHallwayPos.direction;
         BlockPos original_posxx = previousHallwayPos.pos;
         previousHallwayPos.pos = original_posxx;
         previousHallwayPos.direction = original_directionxx;
         if (left_success == 0) {
            previousHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
            previousHallwayPos.direction = "south";
            previousHallwayPos.pos = previousHallwayPos.pos.south(28);
            placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
            if (not_success_case == 1 || not_success_case == 2 || not_success_case == 3) {
               loopbackLeft(previousHallwayPos, manager, destinationWorld);
            } else if (not_success_case == 4 || not_success_case == 5) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, "dungeon/hallway/thin_hallway");
               BlockPos newHallwayPos = blockPosByDirection(previousHallwayPos.direction, 14, previousHallwayPos.pos);
               int probroomend = getRandomInRange(1, 6);
               if (probroomend != 5 && probroomend != 6) {
                  placeDeadendRoom(manager, destinationWorld, previousHallwayPos);
               } else {
                  placeStructure(manager, destinationWorld, newHallwayPos, "dungeon/hallway/thin_hallway_end", previousHallwayPos.hallwaySettings);
               }
            } else if (not_success_case == 6) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, "dungeon/hallway/thin_hallway");
               placeStairs(previousHallwayPos, manager, destinationWorld);
            }
         } else {
            previousHallwayPos.hallwaySettings = previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
            previousHallwayPos.direction = "north";
            previousHallwayPos.pos = previousHallwayPos.pos.north(14).east(4);
            placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
            if (not_success_case == 1 || not_success_case == 2 || not_success_case == 3) {
               loopbackRight(previousHallwayPos, manager, destinationWorld);
            } else if (not_success_case == 4 || not_success_case == 5) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, "dungeon/hallway/thin_hallway");
               BlockPos newHallwayPos = blockPosByDirection(previousHallwayPos.direction, 14, previousHallwayPos.pos);
               int probroomend = getRandomInRange(1, 6);
               if (probroomend != 5 && probroomend != 6) {
                  placeDeadendRoom(manager, destinationWorld, previousHallwayPos);
               } else {
                  placeStructure(manager, destinationWorld, newHallwayPos, "dungeon/hallway/thin_hallway_end", previousHallwayPos.hallwaySettings);
               }
            } else if (not_success_case == 6) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, "dungeon/hallway/thin_hallway");
               placeStairs(previousHallwayPos, manager, destinationWorld);
            }
         }

         previousHallwayPos.pos = original_posxx;
         previousHallwayPos.direction = original_directionxx;
         if (left_success == 0) {
            previousHallwayPos.direction = original_directionxx;
            previousHallwayPos.hallwaySettings = previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
            previousHallwayPos.direction = "north";
            previousHallwayPos.pos = previousHallwayPos.pos.north(14).east(4);
            placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
         } else if (left_success == 1) {
            previousHallwayPos.direction = original_directionxx;
            previousHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
            previousHallwayPos.direction = "south";
            previousHallwayPos.pos = previousHallwayPos.pos.south(28);
            placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
         }
      } else if (Objects.equals(previousHallwayPos.direction, "north")) {
         previousHallwayPos.pos = previousHallwayPos.pos.north(9).east(5);
         placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/thin_hallway_t", previousHallwayPos.hallwaySettings);
         String original_directionxxx = previousHallwayPos.direction;
         BlockPos original_posxxx = previousHallwayPos.pos;
         previousHallwayPos.pos = original_posxxx;
         previousHallwayPos.direction = original_directionxxx;
         if (left_success == 0) {
            previousHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
            previousHallwayPos.direction = "west";
            previousHallwayPos.pos = previousHallwayPos.pos.west(28);
            placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
            if (not_success_case == 1 || not_success_case == 2 || not_success_case == 3) {
               loopbackLeft(previousHallwayPos, manager, destinationWorld);
            } else if (not_success_case == 4 || not_success_case == 5) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, "dungeon/hallway/thin_hallway");
               BlockPos newHallwayPos = blockPosByDirection(previousHallwayPos.direction, 14, previousHallwayPos.pos);
               int probroomend = getRandomInRange(1, 6);
               if (probroomend != 5 && probroomend != 6) {
                  placeDeadendRoom(manager, destinationWorld, previousHallwayPos);
               } else {
                  placeStructure(manager, destinationWorld, newHallwayPos, "dungeon/hallway/thin_hallway_end", previousHallwayPos.hallwaySettings);
               }
            } else if (not_success_case == 6) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, "dungeon/hallway/thin_hallway");
               placeStairs(previousHallwayPos, manager, destinationWorld);
            }
         } else {
            previousHallwayPos.hallwaySettings = previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
            previousHallwayPos.direction = "east";
            previousHallwayPos.pos = previousHallwayPos.pos.east(14).south(4);
            placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
            if (not_success_case == 1 || not_success_case == 2 || not_success_case == 3) {
               loopbackRight(previousHallwayPos, manager, destinationWorld);
            } else if (not_success_case == 4 || not_success_case == 5) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, "dungeon/hallway/thin_hallway");
               BlockPos newHallwayPos = blockPosByDirection(previousHallwayPos.direction, 14, previousHallwayPos.pos);
               int probroomend = getRandomInRange(1, 6);
               if (probroomend != 5 && probroomend != 6) {
                  placeDeadendRoom(manager, destinationWorld, previousHallwayPos);
               } else {
                  placeStructure(manager, destinationWorld, newHallwayPos, "dungeon/hallway/thin_hallway_end", previousHallwayPos.hallwaySettings);
               }
            } else if (not_success_case == 6) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, "dungeon/hallway/thin_hallway");
               placeStairs(previousHallwayPos, manager, destinationWorld);
            }
         }

         previousHallwayPos.pos = original_posxxx;
         previousHallwayPos.direction = original_directionxxx;
         if (left_success == 0) {
            previousHallwayPos.direction = original_directionxxx;
            previousHallwayPos.hallwaySettings = previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
            previousHallwayPos.direction = "east";
            previousHallwayPos.pos = previousHallwayPos.pos.east(14).south(4);
            placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
         } else if (left_success == 1) {
            previousHallwayPos.direction = original_directionxxx;
            previousHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
            previousHallwayPos.direction = "west";
            previousHallwayPos.pos = previousHallwayPos.pos.west(28);
            placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
         }
      }
   }

   static void placeCornerHallway(MutableBlockPos previousHallwayPos, StructureTemplateManager manager, ServerLevel destinationWorld, String thin_hallway) {
      if (Objects.equals(previousHallwayPos.direction, "east")) {
         previousHallwayPos.pos = previousHallwayPos.pos.east(9).south(5);
         placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/thin_hallway_corner", previousHallwayPos.hallwaySettings);
         previousHallwayPos.hallwaySettings = previousHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
         previousHallwayPos.direction = "south";
         previousHallwayPos.pos = previousHallwayPos.pos.south(14).west(4);
         placeStructure(manager, destinationWorld, previousHallwayPos.pos, thin_hallway, previousHallwayPos.hallwaySettings);
      } else if (Objects.equals(previousHallwayPos.direction, "south")) {
         previousHallwayPos.pos = previousHallwayPos.pos.south(9).west(5);
         placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/thin_hallway_corner", previousHallwayPos.hallwaySettings);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
         previousHallwayPos.direction = "west";
         previousHallwayPos.pos = previousHallwayPos.pos.west(14).north(4);
         placeStructure(manager, destinationWorld, previousHallwayPos.pos, thin_hallway, previousHallwayPos.hallwaySettings);
      } else if (Objects.equals(previousHallwayPos.direction, "west")) {
         previousHallwayPos.pos = previousHallwayPos.pos.west(9).north(5);
         placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/thin_hallway_corner", previousHallwayPos.hallwaySettings);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
         previousHallwayPos.direction = "north";
         previousHallwayPos.pos = previousHallwayPos.pos.north(14).east(4);
         placeStructure(manager, destinationWorld, previousHallwayPos.pos, thin_hallway, previousHallwayPos.hallwaySettings);
      } else if (Objects.equals(previousHallwayPos.direction, "north")) {
         previousHallwayPos.pos = previousHallwayPos.pos.north(9).east(5);
         placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/thin_hallway_corner", previousHallwayPos.hallwaySettings);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
         previousHallwayPos.direction = "east";
         previousHallwayPos.pos = previousHallwayPos.pos.east(14).south(4);
         placeStructure(manager, destinationWorld, previousHallwayPos.pos, thin_hallway, previousHallwayPos.hallwaySettings);
      }
   }

   static void placeLongCornerHallway(MutableBlockPos previousHallwayPos, StructureTemplateManager manager, ServerLevel destinationWorld, int length) {
      if (Objects.equals(previousHallwayPos.direction, "east")) {
         previousHallwayPos.pos = previousHallwayPos.pos.east(9).south(5);
         placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/thin_hallway_corner", previousHallwayPos.hallwaySettings);
         previousHallwayPos.hallwaySettings = previousHallwayPos.hallwaySettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
         previousHallwayPos.direction = "south";
         if (length < 14) {
            length = 14;
         } else if (length > 28) {
            length = 28;
         }

         BlockPos newHallwayPos = previousHallwayPos.pos.south(14).west(4);
         placeStructure(manager, destinationWorld, newHallwayPos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
         previousHallwayPos.pos = newHallwayPos;
         newHallwayPos = blockPosByDirection(previousHallwayPos.direction, length - 14, previousHallwayPos.pos);
         placeStructure(manager, destinationWorld, newHallwayPos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
         previousHallwayPos.pos = newHallwayPos;
      } else if (Objects.equals(previousHallwayPos.direction, "south")) {
         previousHallwayPos.pos = previousHallwayPos.pos.south(9).west(5);
         placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/thin_hallway_corner", previousHallwayPos.hallwaySettings);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.NONE);
         previousHallwayPos.direction = "west";
         if (length < 14) {
            length = 14;
         } else if (length > 28) {
            length = 28;
         }

         BlockPos newHallwayPos = previousHallwayPos.pos.west(14).north(4);
         placeStructure(manager, destinationWorld, newHallwayPos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
         previousHallwayPos.pos = newHallwayPos;
         newHallwayPos = blockPosByDirection(previousHallwayPos.direction, length - 14, previousHallwayPos.pos);
         placeStructure(manager, destinationWorld, newHallwayPos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
         previousHallwayPos.pos = newHallwayPos;
      } else if (Objects.equals(previousHallwayPos.direction, "west")) {
         previousHallwayPos.pos = previousHallwayPos.pos.west(9).north(5);
         placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/thin_hallway_corner", previousHallwayPos.hallwaySettings);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_90);
         previousHallwayPos.direction = "north";
         if (length < 14) {
            length = 14;
         } else if (length > 28) {
            length = 28;
         }

         BlockPos newHallwayPos = previousHallwayPos.pos.north(14).east(4);
         placeStructure(manager, destinationWorld, newHallwayPos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
         previousHallwayPos.pos = newHallwayPos;
         newHallwayPos = blockPosByDirection(previousHallwayPos.direction, length - 14, previousHallwayPos.pos);
         placeStructure(manager, destinationWorld, newHallwayPos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
         previousHallwayPos.pos = newHallwayPos;
      } else if (Objects.equals(previousHallwayPos.direction, "north")) {
         previousHallwayPos.pos = previousHallwayPos.pos.north(9).east(5);
         placeStructure(manager, destinationWorld, previousHallwayPos.pos, "dungeon/hallway/thin_hallway_corner", previousHallwayPos.hallwaySettings);
         previousHallwayPos.hallwaySettings.setRotation(Rotation.CLOCKWISE_180);
         previousHallwayPos.direction = "east";
         if (length < 14) {
            length = 14;
         } else if (length > 28) {
            length = 28;
         }

         BlockPos newHallwayPos = previousHallwayPos.pos.east(14).south(4);
         placeStructure(manager, destinationWorld, newHallwayPos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
         previousHallwayPos.pos = newHallwayPos;
         newHallwayPos = blockPosByDirection(previousHallwayPos.direction, length - 14, previousHallwayPos.pos);
         placeStructure(manager, destinationWorld, newHallwayPos, "dungeon/hallway/long_thin_hallway", previousHallwayPos.hallwaySettings);
         previousHallwayPos.pos = newHallwayPos;
      }
   }

   static void placeThinHallways(StructureTemplateManager manager, ServerLevel destinationWorld, MutableBlockPos previousHallwayPos, int n, String thin_hallway) {
      for (int i = 0; i < n; i++) {
         BlockPos newHallwayPos = blockPosByDirection(previousHallwayPos.direction, 14, previousHallwayPos.pos);
         placeStructure(manager, destinationWorld, newHallwayPos, thin_hallway, previousHallwayPos.hallwaySettings);
         previousHallwayPos.pos = newHallwayPos;
      }
   }

   static void placeLongThinHallways(
      StructureTemplateManager manager, ServerLevel destinationWorld, MutableBlockPos previousHallwayPos, int length_min14_max28, String thin_hallway
   ) {
      if (length_min14_max28 < 14) {
         length_min14_max28 = 14;
      } else if (length_min14_max28 > 28) {
         length_min14_max28 = 28;
      }

      BlockPos newHallwayPos = blockPosByDirection(previousHallwayPos.direction, 14, previousHallwayPos.pos);
      placeStructure(manager, destinationWorld, newHallwayPos, thin_hallway, previousHallwayPos.hallwaySettings);
      previousHallwayPos.pos = newHallwayPos;
      newHallwayPos = blockPosByDirection(previousHallwayPos.direction, length_min14_max28 - 14, previousHallwayPos.pos);
      placeStructure(manager, destinationWorld, newHallwayPos, thin_hallway, previousHallwayPos.hallwaySettings);
      previousHallwayPos.pos = newHallwayPos;
   }

   public static BlockPos blockPosByDirection(String direction, int distance, BlockPos prevPos) {
      BlockPos pos;
      if (Objects.equals(direction, "east")) {
         pos = prevPos.east(distance);
      } else if (Objects.equals(direction, "west")) {
         pos = prevPos.west(distance);
      } else if (Objects.equals(direction, "south")) {
         pos = prevPos.south(distance);
      } else {
         pos = prevPos.north(distance);
      }

      return pos;
   }

   public static int getRandomInRange(int min, int max) {
      return (int)(Math.random() * (double)(max - min + 1)) + min;
   }

   private static void placeStructure(StructureTemplateManager manager, ServerLevel world, BlockPos pos, String structurePath, StructurePlaceSettings settings) {
      manager.get(Database.rl(structurePath)).ifPresent(template -> template.placeInWorld(world, pos, pos, settings, world.getRandom(), 3));
   }

   public static void placeDungeonUnlockerBlockWithRotation(ServerLevel destinationWorld, BlockPos pos, Rotation rotation, boolean canGenerate) {
      if (rotation == null) {
         throw new IllegalArgumentException("Rotation cannot be null.");
      } else {
         Direction baseDirection = Direction.NORTH;
         Direction facing = rotation.rotate(baseDirection);
         if (!facing.getAxis().isHorizontal()) {
            throw new IllegalArgumentException("DungeonUnlockerBlock can only face horizontal directions.");
         } else {
            BlockState blockState = (BlockState)((BlockState)((BlockState)((BlockState)((DungeonUnlockerBlock)BeyondBlocks.DUNGEON_UNLOCKER_BLOCK.get())
                        .defaultBlockState()
                        .setValue(DungeonUnlockerBlock.FACING, facing))
                     .setValue(DungeonUnlockerBlock.CAN_GENERATE, canGenerate))
                  .setValue(DungeonUnlockerBlock.ENABLED, true))
               .setValue(DungeonUnlockerBlock.ANIM_STAGE, 0);
            Block existingBlock = destinationWorld.getBlockState(pos).getBlock();
            if (existingBlock != Blocks.AIR) {
               ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(existingBlock);
               System.err
                  .println(
                     "Cannot place DungeonUnlockerBlock at "
                        + pos
                        + " as it is already occupied by "
                        + (registryName != null ? registryName.toString() : "Unknown Block")
                  );
            } else {
               boolean success = destinationWorld.setBlock(pos, blockState, 3);
               if (!success) {
                  ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(existingBlock);
                  System.err.println("Failed to place DungeonUnlockerBlock at " + pos + " with rotation " + rotation + " and canGenerate " + canGenerate);
               } else {
                  ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey((Block)BeyondBlocks.DUNGEON_UNLOCKER_BLOCK.get());
                  System.out
                     .println(
                        "Successfully placed DungeonUnlockerBlock ("
                           + (registryName != null ? registryName.toString() : "Unknown")
                           + ") at "
                           + pos
                           + " facing "
                           + facing
                           + " with canGenerate="
                           + canGenerate
                     );
               }
            }
         }
      }
   }
}
