package io.github.Memoires.trbeyond.network.worldgen.dimension;

import io.github.Memoires.trbeyond.Database;
import io.github.Memoires.trbeyond.block.DungeonUnlockerBlock;
import io.github.Memoires.trbeyond.block.entity.AnchorBlockEntity;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class BeyondDungeon {
   public static final ResourceKey<Level> DUNGEON_LEVEL_KEY = ResourceKey.create(Registry.DIMENSION_REGISTRY, Database.rl("dungeon"));
   public static final ResourceKey<DimensionType> DUNGEON_DIM_TYPE_KEY = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, Database.rl("dungeon"));

   public static void generateNextPart(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
      BlockPos anchorPos = new BlockPos(pos.getX() / 800 * 800, 140, pos.getZ() / 800 * 800);
      BlockEntity be = level.getBlockEntity(anchorPos);
      AnchorBlockEntity anchor = (AnchorBlockEntity)be;
      Direction dir = ((Direction)state.getValue(DungeonUnlockerBlock.FACING)).getOpposite();
      if (level instanceof ServerLevel serverLevel) {
         serverLevel.scheduleTick(pos, state.getBlock(), 1);
      }
   }

   private static void placeChainOfThinHallwaysWithRandomJunction(
      ServerLevel serverLevel, StructureTemplateManager manager, BlockPos startPos, Direction dir, boolean mirror, Level level
   ) {
      int count = 4 + level.getRandom().nextInt(2);
      BeyondDungeon.DungeonJigsawPart chainHallPart = new BeyondDungeon.DungeonJigsawPart(
         Rotation.CLOCKWISE_90, new BlockPos(15, 7, 5), new BlockPos(2, 2, 14), "hallway/thin_hallway", false
      );
      BeyondDungeon.DungeonJigsawPart cornerPart = new BeyondDungeon.DungeonJigsawPart(
         Rotation.CLOCKWISE_90, new BlockPos(10, 7, 10), new BlockPos(7, 2, 9), "hallway/thin_hallway_corner", true
      );
      BeyondDungeon.DungeonJigsawPart tPart = new BeyondDungeon.DungeonJigsawPart(
         Rotation.CLOCKWISE_90, new BlockPos(10, 7, 10), new BlockPos(7, 2, 9), "hallway/thin_hallway_t", true
      );
      BeyondDungeon.DungeonJigsawPart fourWayPart = new BeyondDungeon.DungeonJigsawPart(
         Rotation.CLOCKWISE_90, new BlockPos(15, 7, 15), new BlockPos(7, 2, 14), "hallway/thin_hallway_4way", true
      );
      BlockPos currentEnd = startPos;

      for (int i = 1; i < count; i++) {
         StructurePlaceSettings chainSettings = new StructurePlaceSettings()
            .setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE)
            .setRotation(getRotFromFacing(chainHallPart.rotation().rotate(dir)))
            .setIgnoreEntities(true);
         BlockPos chainPos = currentEnd.relative(
               chainHallPart.rotation().rotate(dir),
               chainHallPart.canBeMirrored() && mirror ? -chainHallPart.relativePos().getX() : chainHallPart.relativePos().getX()
            )
            .relative(dir, chainHallPart.relativePos().getZ())
            .below(chainHallPart.relativePos().getY());
         chainPos = chainPos.relative(dir.getOpposite(), 15).relative(dir.getCounterClockWise(), 2).above(2);
         if (i < count - 1) {
            manager.get(chainHallPart.getLocation())
               .ifPresent(template -> template.placeInWorld(serverLevel, chainPos, chainPos, chainSettings, level.getRandom(), 3));
            currentEnd = chainPos.relative(dir, 15);
         } else {
            int choice = level.getRandom().nextInt(3);

            BeyondDungeon.DungeonJigsawPart finalPart = switch (choice) {
               case 0 -> cornerPart;
               case 1 -> tPart;
               case 2 -> fourWayPart;
               default -> cornerPart;
            };
            StructurePlaceSettings finalSettings = new StructurePlaceSettings()
               .setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE)
               .setRotation(getRotFromFacing(finalPart.rotation().rotate(dir)))
               .setIgnoreEntities(true);
            BlockPos finalPos = chainPos.relative(
                  finalPart.rotation().rotate(dir),
                  finalPart.canBeMirrored() && mirror ? -finalPart.relativePos().getX() : finalPart.relativePos().getX()
               )
               .relative(dir, finalPart.relativePos().getZ())
               .below(finalPart.relativePos().getY());
            finalPos = finalPos.relative(dir.getOpposite(), 15).relative(dir.getCounterClockWise(), 2).above(2);
            manager.get(finalPart.getLocation())
               .ifPresent(template -> template.placeInWorld(serverLevel, finalPos, finalPos, finalSettings, level.getRandom(), 3));
         }
      }
   }

   private static void genBossFightRoom(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
      Direction dir = ((Direction)state.getValue(DungeonUnlockerBlock.FACING)).getOpposite();
      if (level instanceof ServerLevel serverLevel) {
         StructureTemplateManager manager = serverLevel.getStructureManager();
         StructurePlaceSettings settings = new StructurePlaceSettings()
            .setMirror(Mirror.NONE)
            .setRotation(getRotFromFacing(Rotation.CLOCKWISE_90.rotate(dir)))
            .setIgnoreEntities(true);
         BlockPos rightPart = pos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), 26).relative(dir, 15).below(2);
         manager.get(Database.rl("dungeon/room_boss/10bossroom_1"))
            .ifPresent(template -> template.placeInWorld(serverLevel, rightPart, rightPart, settings, serverLevel.getRandom(), 3));
         BlockPos placePos = pos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), 10).relative(dir, 15).below(2);
         manager.get(Database.rl("dungeon/room_boss/10bossroom_2"))
            .ifPresent(template -> template.placeInWorld(serverLevel, placePos, placePos, settings, serverLevel.getRandom(), 3));
         BlockPos leftPart = pos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), -6).relative(dir, 15).below(2);
         manager.get(Database.rl("dungeon/room_boss/10bossroom_3"))
            .ifPresent(template -> template.placeInWorld(serverLevel, leftPart, leftPart, settings, serverLevel.getRandom(), 3));
         BlockPos leftPart2 = pos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), -22).relative(dir, 15).below(2);
         manager.get(Database.rl("dungeon/room_boss/10bossroom_4"))
            .ifPresent(template -> template.placeInWorld(serverLevel, leftPart2, leftPart2, settings, serverLevel.getRandom(), 3));
         BlockPos rightPart2 = pos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), 26).relative(dir, 31).below(2);
         manager.get(Database.rl("dungeon/room_boss/10bossroom_5"))
            .ifPresent(template -> template.placeInWorld(serverLevel, rightPart2, rightPart2, settings, serverLevel.getRandom(), 3));
         BlockPos placePos2 = pos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), 10).relative(dir, 31).below(2);
         manager.get(Database.rl("dungeon/room_boss/10bossroom_6"))
            .ifPresent(template -> template.placeInWorld(serverLevel, placePos2, placePos2, settings, serverLevel.getRandom(), 3));
         BlockPos leftPart3 = pos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), -6).relative(dir, 31).below(2);
         manager.get(Database.rl("dungeon/room_boss/10bossroom_7"))
            .ifPresent(template -> template.placeInWorld(serverLevel, leftPart3, leftPart3, settings, serverLevel.getRandom(), 3));
         BlockPos leftPart4 = pos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), -22).relative(dir, 31).below(2);
         manager.get(Database.rl("dungeon/room_boss/10bossroom_8"))
            .ifPresent(template -> template.placeInWorld(serverLevel, leftPart4, leftPart4, settings, serverLevel.getRandom(), 3));
         BlockPos rightPart3 = pos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), 26).relative(dir, 47).below(2);
         manager.get(Database.rl("dungeon/room_boss/10bossroom_9"))
            .ifPresent(template -> template.placeInWorld(serverLevel, rightPart3, rightPart3, settings, serverLevel.getRandom(), 3));
         BlockPos placePos3 = pos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), 10).relative(dir, 47).below(2);
         manager.get(Database.rl("dungeon/room_boss/10bossroom_10"))
            .ifPresent(template -> template.placeInWorld(serverLevel, placePos3, placePos3, settings, serverLevel.getRandom(), 3));
         BlockPos leftPart5 = pos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), -6).relative(dir, 47).below(2);
         manager.get(Database.rl("dungeon/room_boss/10bossroom_11"))
            .ifPresent(template -> template.placeInWorld(serverLevel, leftPart5, leftPart5, settings, serverLevel.getRandom(), 3));
         BlockPos leftPart6 = pos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), -22).relative(dir, 47).below(2);
         manager.get(Database.rl("dungeon/room_boss/10bossroom_12"))
            .ifPresent(template -> template.placeInWorld(serverLevel, leftPart6, leftPart6, settings, serverLevel.getRandom(), 3));
         BlockPos preLastPart = pos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), 14).relative(dir, 52).below(2);
         manager.get(Database.rl("dungeon/room_boss/10bossroom_13"))
            .ifPresent(template -> template.placeInWorld(serverLevel, preLastPart, preLastPart, settings, serverLevel.getRandom(), 3));
         BlockPos lastPart = pos.offset(dir.getNormal()).relative(Rotation.CLOCKWISE_90.rotate(dir), -1).relative(dir, 52).below(2);
         manager.get(Database.rl("dungeon/room_boss/10bossroom_14"))
            .ifPresent(template -> template.placeInWorld(serverLevel, lastPart, lastPart, settings, serverLevel.getRandom(), 3));
      }
   }

   private static List<BeyondDungeon.DungeonJigsawPart> getPossibleParts(List<Direction> list, Direction dir) {
      List<BeyondDungeon.DungeonJigsawPart> parts = new ArrayList<>();
      if (list.contains(dir)) {
         parts.addAll(BeyondDungeon.DungeonJigsawType.getForward());
      }

      if (list.contains(dir.getClockWise()) || list.contains(dir.getCounterClockWise())) {
         parts.addAll(BeyondDungeon.DungeonJigsawType.getCornerList());
      }

      if (list.contains(dir.getClockWise()) && list.contains(dir.getCounterClockWise())) {
         parts.addAll(BeyondDungeon.DungeonJigsawType.getTwoWays());
         if (list.contains(dir)) {
            parts.addAll(BeyondDungeon.DungeonJigsawType.getThreeWays());
         }
      }

      if (list.contains(Direction.DOWN)) {
         parts.add(BeyondDungeon.DungeonJigsawType.getStairs());
      }

      if (list.contains(Direction.UP)) {
         parts.add(BeyondDungeon.DungeonJigsawType.getUp());
      }

      return parts;
   }

   private static List<Direction> getPossibleDirections(Map<Direction, Boolean> map) {
      return map.entrySet().stream().filter(Entry::getValue).map(Entry::getKey).toList();
   }

   private static Map<Direction, Boolean> checkAllDirectionsForGen(Level level, BlockPos genPos, Direction facing, BlockPos anchorPos) {
      Map<Direction, Boolean> map = new EnumMap<>(Direction.class);
      Direction.stream().forEach(dir -> map.put(dir, true));
      BlockPos startPos = genPos.relative(facing, 3);
      BlockPos limitPos = genPos.relative(facing, 15);
      int x = limitPos.getX() / 800 * 800;
      int z = limitPos.getZ() / 800 * 800;
      BlockPos finalLimitPos = new BlockPos(x, 140, z);
      Direction.stream().forEach(dir -> {
         if (dir == facing.getOpposite()) {
            map.put(dir, false);
         } else {
            if (dir == facing && !finalLimitPos.equals(anchorPos)) {
               map.put(dir, false);
               return;
            }

            for (int q = 0; q < 15; q++) {
               BlockPos checkPos = startPos.relative(dir, q);
               BlockState checkState = level.getBlockState(checkPos);
               if (!checkState.isAir()) {
                  map.put(dir, false);
               }
            }
         }
      });
      return map;
   }

   public static Rotation getRotFromFacing(Direction to) {
      if (to == Direction.NORTH) {
         return Rotation.NONE;
      } else if (to == Direction.SOUTH) {
         return Rotation.CLOCKWISE_180;
      } else {
         return to == Direction.WEST ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90;
      }
   }

   public static record DungeonJigsaw(BeyondDungeon.DungeonJigsawPart... parts) {
      public BeyondDungeon.DungeonJigsawPart getRandomPart(@NotNull Level level) {
         int q = level.getRandom().nextInt(this.parts().length);
         return this.parts[q];
      }
   }

   public static record DungeonJigsawPart(
      Rotation rotation,
      BlockPos size,
      BlockPos relativePos,
      String name,
      boolean canBeMirrored,
      List<BeyondDungeon.DungeonJigsawPart> additionalParts,
      int floorChanges
   ) {
      public DungeonJigsawPart(Rotation rotation, BlockPos size, BlockPos relativePos, String name) {
         this(rotation, size, relativePos, name, false, List.of(), 0);
      }

      public DungeonJigsawPart(Rotation rotation, BlockPos size, BlockPos relativePos, String name, boolean canBeMirrored) {
         this(rotation, size, relativePos, name, canBeMirrored, List.of(), 0);
      }

      public DungeonJigsawPart(Rotation rotation, BlockPos size, BlockPos relativePos, String name, int floorChanges) {
         this(rotation, size, relativePos, name, false, List.of(), floorChanges);
      }

      public DungeonJigsawPart(Rotation rotation, BlockPos size, BlockPos relativePos, String name, boolean canBeMirrored, int floorChanges) {
         this(rotation, size, relativePos, name, canBeMirrored, List.of(), floorChanges);
      }

      public ResourceLocation getLocation() {
         return Database.rl(BeyondDungeon.DungeonJigsawType.getPath(this.name));
      }

      public boolean hasAdditional() {
         return !this.additionalParts().isEmpty();
      }
   }

   public static enum DungeonJigsawType {
      HALLWAY(
         new BeyondDungeon.DungeonJigsaw(
            new BeyondDungeon.DungeonJigsawPart(Rotation.CLOCKWISE_90, new BlockPos(15, 7, 5), new BlockPos(2, 2, 14), "hallway/thin_hallway_end"),
            new BeyondDungeon.DungeonJigsawPart(Rotation.CLOCKWISE_90, new BlockPos(15, 7, 5), new BlockPos(2, 2, 14), "hallway/thin_hallway"),
            new BeyondDungeon.DungeonJigsawPart(Rotation.CLOCKWISE_90, new BlockPos(10, 7, 10), new BlockPos(7, 2, 9), "hallway/thin_hallway_corner", true),
            new BeyondDungeon.DungeonJigsawPart(Rotation.CLOCKWISE_90, new BlockPos(10, 7, 15), new BlockPos(7, 2, 9), "hallway/thin_hallway_t", true),
            new BeyondDungeon.DungeonJigsawPart(Rotation.CLOCKWISE_90, new BlockPos(15, 7, 15), new BlockPos(7, 2, 14), "hallway/thin_hallway_4way", true)
         )
      ),
      ROOM(
         new BeyondDungeon.DungeonJigsaw(
            new BeyondDungeon.DungeonJigsawPart(Rotation.CLOCKWISE_90, new BlockPos(11, 8, 11), new BlockPos(5, 2, 10), "room/small_room_2"),
            new BeyondDungeon.DungeonJigsawPart(Rotation.CLOCKWISE_90, new BlockPos(11, 8, 11), new BlockPos(5, 2, 10), "room/small_room_2way1"),
            new BeyondDungeon.DungeonJigsawPart(Rotation.CLOCKWISE_90, new BlockPos(11, 8, 11), new BlockPos(5, 2, 10), "room/small_room_3way1"),
            new BeyondDungeon.DungeonJigsawPart(Rotation.CLOCKWISE_90, new BlockPos(11, 8, 11), new BlockPos(5, 2, 10), "room/small_room_end"),
            new BeyondDungeon.DungeonJigsawPart(Rotation.CLOCKWISE_90, new BlockPos(11, 8, 11), new BlockPos(5, 2, 10), "room/small_room_turn1", true)
         )
      ),
      TRAPROOM(
         new BeyondDungeon.DungeonJigsaw(
            new BeyondDungeon.DungeonJigsawPart(Rotation.CLOCKWISE_90, new BlockPos(11, 8, 11), new BlockPos(5, 2, 10), "traprooms", false)
         )
      ),
      EMPTYROOM(
         new BeyondDungeon.DungeonJigsaw(
            new BeyondDungeon.DungeonJigsawPart(Rotation.CLOCKWISE_90, new BlockPos(11, 8, 11), new BlockPos(5, 2, 10), "emptyroom", false)
         )
      ),
      TREASURE(
         new BeyondDungeon.DungeonJigsaw(
            new BeyondDungeon.DungeonJigsawPart(Rotation.CLOCKWISE_90, new BlockPos(11, 8, 11), new BlockPos(5, 2, 10), "treasure/bronze_chest_loot", false),
            new BeyondDungeon.DungeonJigsawPart(Rotation.CLOCKWISE_90, new BlockPos(11, 8, 11), new BlockPos(5, 2, 10), "treasure/silver_chest_loot", false),
            new BeyondDungeon.DungeonJigsawPart(Rotation.CLOCKWISE_90, new BlockPos(11, 8, 11), new BlockPos(5, 2, 10), "treasure/gold_chest_loot", false)
         )
      ),
      STAIRS(
         new BeyondDungeon.DungeonJigsaw(
            new BeyondDungeon.DungeonJigsawPart(Rotation.CLOCKWISE_90, new BlockPos(10, 15, 9), new BlockPos(2, 10, 9), "stairs/small_stairs", true, -1)
         )
      );

      private final BeyondDungeon.DungeonJigsaw jigsaw;

      private DungeonJigsawType(BeyondDungeon.DungeonJigsaw jigsaw) {
         this.jigsaw = jigsaw;
      }

      public BeyondDungeon.DungeonJigsaw getJigsaw() {
         return this.jigsaw;
      }

      public static BeyondDungeon.DungeonJigsawType getRandom(@NotNull Level level) {
         RandomSource rand = level.getRandom();
         float roll = rand.nextFloat() * 100.0F;
         if (roll < 66.0F) {
            return HALLWAY;
         } else if (roll < 80.0F) {
            return TRAPROOM;
         } else if (roll < 94.0F) {
            return EMPTYROOM;
         } else {
            return roll < 99.0F ? TREASURE : STAIRS;
         }
      }

      @NotNull
      public static List<BeyondDungeon.DungeonJigsawPart> getDeadEndList() {
         List<BeyondDungeon.DungeonJigsawPart> parts = new ArrayList<>();
         parts.add(HALLWAY.getJigsaw().parts()[0]);
         parts.add(ROOM.getJigsaw().parts()[3]);
         return parts;
      }

      public static BeyondDungeon.DungeonJigsawPart getRandomEnd(Level level) {
         return getRandomPart(getDeadEndList(), level);
      }

      public static List<BeyondDungeon.DungeonJigsawPart> getCornerList() {
         List<BeyondDungeon.DungeonJigsawPart> parts = new ArrayList<>();
         parts.add(HALLWAY.getJigsaw().parts()[2]);
         parts.add(ROOM.getJigsaw().parts()[4]);
         return parts;
      }

      public static BeyondDungeon.DungeonJigsawPart getRandomCorner(Level level) {
         return getRandomPart(getCornerList(), level);
      }

      public static BeyondDungeon.DungeonJigsawPart getStairs() {
         return STAIRS.getJigsaw().parts()[0];
      }

      public static BeyondDungeon.DungeonJigsawPart getUp() {
         return ROOM.getJigsaw().parts()[0];
      }

      public static List<BeyondDungeon.DungeonJigsawPart> getForward() {
         List<BeyondDungeon.DungeonJigsawPart> list = new ArrayList<>();
         list.add(HALLWAY.getJigsaw().parts()[1]);
         list.add(ROOM.getJigsaw().parts()[0]);
         return list;
      }

      public static List<BeyondDungeon.DungeonJigsawPart> getTwoWays() {
         List<BeyondDungeon.DungeonJigsawPart> list = new ArrayList<>();
         list.add(HALLWAY.getJigsaw().parts()[3]);
         list.add(ROOM.getJigsaw().parts()[1]);
         return list;
      }

      public static List<BeyondDungeon.DungeonJigsawPart> getThreeWays() {
         List<BeyondDungeon.DungeonJigsawPart> list = new ArrayList<>();
         list.add(HALLWAY.getJigsaw().parts()[4]);
         list.add(ROOM.getJigsaw().parts()[2]);
         return list;
      }

      public static BeyondDungeon.DungeonJigsawPart getRandomPart(@NotNull List<BeyondDungeon.DungeonJigsawPart> parts, @NotNull Level level) {
         return parts.get(level.getRandom().nextInt(parts.size()));
      }

      @NotNull
      public static String getPath(String str) {
         return "dungeon/" + str;
      }
   }
}
