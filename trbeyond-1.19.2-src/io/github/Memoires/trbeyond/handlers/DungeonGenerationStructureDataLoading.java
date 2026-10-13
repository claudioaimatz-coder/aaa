package io.github.Memoires.trbeyond.handlers;

import io.github.Memoires.trbeyond.Database;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.network.worldgen.portal.BeyondTeleporterUpdated;
import io.github.Memoires.trbeyond.network.worldgen.portal.info.BossRoomPartStructureInfo;
import io.github.Memoires.trbeyond.network.worldgen.portal.info.CornerStructureInfo;
import io.github.Memoires.trbeyond.network.worldgen.portal.info.DeadEndStructureInfo;
import io.github.Memoires.trbeyond.network.worldgen.portal.info.JunctionStructureInfo;
import io.github.Memoires.trbeyond.network.worldgen.portal.info.ShopStructureInfo;
import io.github.Memoires.trbeyond.network.worldgen.portal.info.StairsStructureInfo;
import io.github.Memoires.trbeyond.network.worldgen.portal.info.StraightStructureInfo;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.FORGE
)
public class DungeonGenerationStructureDataLoading {
   @SubscribeEvent
   public static void loadStructures(ServerAboutToStartEvent event) {
      Database.F1_10
         .addThinHallway(
            (float)((Integer)ServerConfig.INSTANCE.dungeonConfig.thinHallwayTrapChance.get()).intValue(),
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/thin/0")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(2))
                  .setEntityPos(BlockPos.ZERO.east(7).above(2))
                  .setSize(new BlockPos(15, 7, 5))
                  .build()
            )
         )
         .addThinHallway(
            (float)(100 - (Integer)ServerConfig.INSTANCE.dungeonConfig.thinHallwayTrapChance.get()),
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/thin/0")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(2))
                  .setEntityPos(BlockPos.ZERO.east(7).above(2))
                  .setSize(new BlockPos(15, 7, 5))
                  .build()
            )
         )
         .addThinHallway(
            100.0F,
            List.of(
               ShopStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/thin/shop/0")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(2))
                  .setSize(new BlockPos(15, 7, 5))
                  .setShopOffset(7)
                  .build()
            ),
            () -> false
         )
         .addLongThinHallway(
            (float)((Integer)ServerConfig.INSTANCE.dungeonConfig.longThinHallwayTrapChance.get()).intValue(),
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/special/illusionary")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/special/illusionary_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/special/illusionary_mimic")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/special/illusionary_paralysis")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/special/illusionary_tnt")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/special/moving_sealing")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/special/rotating_moving")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/00")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/01")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/02")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/03")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/04")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/05")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/06")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/07")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/10")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/11")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/12")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/13")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/long_thin/14")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build()
            )
         )
         .addLongThinHallway(
            3.0F,
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/long_thin/0")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build()
            )
         )
         .addLongThinHallway(
            (float)(97 - (Integer)ServerConfig.INSTANCE.dungeonConfig.longThinHallwayTrapChance.get()),
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/long_thin/1")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/long_thin/2")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(2))
                  .setSize(new BlockPos(19, 7, 5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(9))
                  .build()
            )
         )
         .addJunction(
            100.0F,
            List.of(
               JunctionStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/t/0")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(9).south(7))
                  .setSize(new BlockPos(10, 7, 15))
                  .build()
            )
         )
         .addCornerHallway(
            100.0F,
            List.of(
               CornerStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/corner/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(10, 7, 10))
                  .setEntranceOffset(BlockPos.ZERO.east(9).south(7))
                  .setEntityPos(BlockPos.ZERO.east(7).above(2))
                  .build()
            )
         )
         .addThinHallwayEnd(
            (float)((Integer)ServerConfig.INSTANCE.dungeonConfig.hallwayEndTrapChance.get()).intValue(),
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/thin_end/illusionary_bronze")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.south(2).east(14))
                  .setSize(new BlockPos(15, 7, 5))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/thin_end/illusionary_silver")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.south(2).east(14))
                  .setSize(new BlockPos(15, 7, 5))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/thin_end/illusionary_gold")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.south(2).east(14))
                  .setSize(new BlockPos(15, 7, 5))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/trap/thin_end/0")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.south(2).east(14))
                  .setSize(new BlockPos(15, 7, 5))
                  .build()
            )
         )
         .addThinHallwayEnd(
            (float)(100 - (Integer)ServerConfig.INSTANCE.dungeonConfig.hallwayEndTrapChance.get()),
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/thin_end/0")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.south(2).east(14))
                  .setSize(new BlockPos(15, 7, 5))
                  .build()
            )
         )
         .addDeadEndRoom(
            3.0F,
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trial/bronze/0")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.above(2).east(6))
                  .setSize(new BlockPos(15, 8, 15))
                  .build()
            ),
            () -> true
         )
         .addDeadEndRoom(
            2.0F,
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trial/gold/0")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.above(2).east(6))
                  .setSize(new BlockPos(15, 8, 15))
                  .build()
            ),
            () -> true
         )
         .addDeadEndRoom(
            2.0F,
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trial/silver/0")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.above(2).east(6))
                  .setSize(new BlockPos(15, 8, 15))
                  .build()
            ),
            () -> true
         )
         .addDeadEndRoom(
            1.0F,
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trial/stellar/0")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.above(2).east(6))
                  .setSize(new BlockPos(15, 8, 15))
                  .build()
            ),
            () -> true
         )
         .addDeadEndRoom(
            (float)((Integer)ServerConfig.INSTANCE.dungeonConfig.emptyEndRoomChance.get()).intValue(),
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/dead_end/0")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build()
            ),
            () -> false
         )
         .addDeadEndRoom(
            (float)((Integer)ServerConfig.INSTANCE.dungeonConfig.endRoomTrapChance.get()).intValue(),
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/1bronze_1gold_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/1bronze_chest_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/1bronze_right_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/1bronze_silver_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/1gold_1bronze_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/1gold_1silver_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/1gold_chest_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/1silver_1gold_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/1silver_chest_right_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/1silver_chest_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/2bronze_chest_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/2gold_chest_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/2silver_chest_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build()
            ),
            () -> false
         )
         .addDeadEndRoom(
            1.0F,
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/small_room_end_minhslime")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build()
            ),
            () -> false
         )
         .addDeadEndRoom(
            (float)(
               100
                  - (Integer)ServerConfig.INSTANCE.dungeonConfig.emptyEndRoomChance.get()
                  - (Integer)ServerConfig.INSTANCE.dungeonConfig.endRoomTrapChance.get()
                  - 1
            ),
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1bronze_1goldparalysis_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1bronze_1goldtnt_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1bronze_1silverparalysis_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1bronze_1silvertnt_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1bronzeparalysis_1bronzetnt")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1bronzeparalysis_1gold_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1bronzeparalysis_1goldtnt")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1bronzeparalysis_1silver_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1bronzeparalysis_1silvertnt")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1bronzetnt_1bronzeparalysis")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1bronzetnt_1goldparalysis")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1bronzetnt_1silver_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1bronzetnt_1silvertnt")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1gold_1bronzeparalysis_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1gold_1bronzetnt_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1gold_1silverparalysis_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1gold_1silvertnt_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1gold_1goldparalysis_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1gold_1goldtnt_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1goldparalysis_1bronze_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1goldparalysis_1bronzetnt")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1goldparalysis_1gold_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1goldparalysis_1silver_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1goldparalysis_1silvertnt")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1goldtnt_1bronze_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1goldtnt_1bronzeparalysis")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1goldtnt_1bronzetnt")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1goldtnt_1gold_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1goldtnt_1silver_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1goldtnt_1silverparalysis")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1silver_1bronzeparalysis_lot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1silver_1bronzetnt_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1silver_1goldparalysis_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1silver_1goldtnt_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1silver_1paralysis_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1silver_1silvertnt_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1silverparalysis_1bronze_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1silverparalysis_1gold")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1silverparalysis_1goldtnt")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1silverparalysis_1silver_loot")
                  .setFacing(Direction.WEST)
                  .setEntranceOffset(BlockPos.ZERO.east(11).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1silverparalysis_1silvertnt")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1silvertnt_1bronze_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1silvertnt_1bronzeparalysis")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1silvertnt_1bronzetnt")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1silvertnt_1gold_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1silvertnt_1goldparalysis")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/1silvertnt_1silver_loot")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/2bronzetnt")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/2goldtnt")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/2silvertnt")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/treasure/dead_end/empty_chest_room")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(4))
                  .setEntityPos(BlockPos.ZERO.above(2).east(4))
                  .setSize(new BlockPos(11, 8, 11))
                  .build()
            ),
            () -> false
         )
         .addOneWayRoom(
            1.0F,
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/small_rotating_floors")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .setSize(new BlockPos(11, 8, 11))
                  .build()
            )
         )
         .addOneWayRoom(
            1.0F,
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/trap/small_illusionary_wall")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .setSize(new BlockPos(11, 8, 11))
                  .build()
            )
         )
         .addOneWayRoom(
            98.0F,
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/one_way/0")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/one_way/1")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/one_way/2")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .setSize(new BlockPos(11, 8, 11))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/room/one_way/3")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(5))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .setSize(new BlockPos(11, 8, 11))
                  .build()
            )
         )
         .addLeftSideRoom(
            100.0F,
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/side/left/0")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(2))
                  .setEntityPos(BlockPos.ZERO.east(8).above(2))
                  .setSize(new BlockPos(15, 7, 5))
                  .build()
            )
         )
         .addRightSideRoom(
            100.0F,
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/side/right/0")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(2))
                  .setEntityPos(BlockPos.ZERO.east(8).above(2))
                  .setSize(new BlockPos(15, 7, 5))
                  .build()
            )
         )
         .addBothSideRoom(
            100.0F,
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/hallway/side/both/0")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(2))
                  .setEntityPos(BlockPos.ZERO.east(8).above(2))
                  .setSize(new BlockPos(15, 7, 5))
                  .build()
            )
         )
         .addStairs(
            100.0F,
            List.of(
               StairsStructureInfo.builder()
                  .setAddress("dungeon/floors/1-10/stairs/0")
                  .setFacing(Direction.EAST)
                  .setEntranceOffset(BlockPos.ZERO.east(11).south(2).above(16))
                  .setSize(new BlockPos(12, 23, 9))
                  .build()
            )
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/1-10/room/boss/0")
               .setFacing(Direction.EAST)
               .setProvider(BeyondTeleporterUpdated::spawnBlackSpiderAt)
               .setExitPosition(BlockPos.ZERO.east(53))
               .setEntranceOffset(BlockPos.ZERO.south(26).east(15))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/1-10/room/boss/1")
               .setFacing(Direction.EAST)
               .setEntranceOffset(BlockPos.ZERO.south(10).east(15))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/1-10/room/boss/2")
               .setFacing(Direction.EAST)
               .setEntranceOffset(BlockPos.ZERO.south(-6).east(15))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/1-10/room/boss/3")
               .setFacing(Direction.EAST)
               .setEntranceOffset(BlockPos.ZERO.south(-22).east(15))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/1-10/room/boss/4")
               .setFacing(Direction.EAST)
               .setEntranceOffset(BlockPos.ZERO.south(26).east(31))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/1-10/room/boss/5")
               .setFacing(Direction.EAST)
               .setEntranceOffset(BlockPos.ZERO.south(10).east(31))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/1-10/room/boss/6")
               .setFacing(Direction.EAST)
               .setEntranceOffset(BlockPos.ZERO.south(-6).east(31))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/1-10/room/boss/7")
               .setFacing(Direction.EAST)
               .setEntranceOffset(BlockPos.ZERO.south(-22).east(31))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/1-10/room/boss/8")
               .setFacing(Direction.EAST)
               .setEntranceOffset(BlockPos.ZERO.south(26).east(47))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/1-10/room/boss/9")
               .setFacing(Direction.EAST)
               .setEntranceOffset(BlockPos.ZERO.south(10).east(47))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/1-10/room/boss/10")
               .setFacing(Direction.EAST)
               .setEntranceOffset(BlockPos.ZERO.south(-6).east(47))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/1-10/room/boss/11")
               .setFacing(Direction.EAST)
               .setEntranceOffset(BlockPos.ZERO.south(-22).east(47))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/1-10/room/boss/12")
               .setFacing(Direction.EAST)
               .setEntranceOffset(BlockPos.ZERO.south(14).east(52))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/1-10/room/boss/13")
               .setFacing(Direction.EAST)
               .setEntranceOffset(BlockPos.ZERO.south(-1).east(52))
               .build()
         )
         .end();
      Database.F11_20
         .addThinHallway(
            (float)(100 - (Integer)ServerConfig.INSTANCE.dungeonConfig.thinHallwayTrapChance.get()),
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/thin/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/thin/1")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/thin/2")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/thin/3")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/thin/4")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/thin/5")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/thin/6")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/thin/7")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build()
            )
         )
         .addThinHallway(
            (float)((Integer)ServerConfig.INSTANCE.dungeonConfig.thinHallwayTrapChance.get()).intValue(),
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/1paralyze_1silver")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/1paralyze_silver_1tnt_bronze")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/1tnt_bronze_1normal_bronze")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/1tnt_bronze_1normal_silver")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/1tnt_gold_1normal_gold")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/1tnt_gold_1normal_silver")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/2paralyze")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/steel_thread")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/steel_thread_1")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/steel_thread_2")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/steel_thread_3")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/steel_thread_bronze_chest")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/steel_thread_jump_floor")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/steel_thread_silver_chest")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/steel_thread_tnt_gold")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/tnt_2chests")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/tnt_2chests_1")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/tnt_2chests_2")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/tnt_2chests_3")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/trap/thin/tnt_3chests")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build()
            )
         )
         .addThinHallway(
            100.0F,
            List.of(
               ShopStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/thin/shop/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(19, 9, 8))
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(4).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(4))
                  .setShopOffset(11)
                  .build()
            ),
            () -> false
         )
         .addLongThinHallway(
            100.0F,
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/long_thin/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(18, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(17).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(5).above(4))
                  .build()
            )
         )
         .addJunction(
            100.0F,
            List.of(
               JunctionStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/t/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(11, 9, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(7).above())
                  .setSideOffset(4)
                  .build(),
               JunctionStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/t/1")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(11, 9, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(7).above())
                  .setSideOffset(4)
                  .build(),
               JunctionStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/t/2")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(11, 9, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(7).above())
                  .setSideOffset(4)
                  .build(),
               JunctionStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/t/3")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(11, 9, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(7).above())
                  .setSideOffset(4)
                  .build()
            )
         )
         .addCornerHallway(
            100.0F,
            List.of(
               CornerStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/corner/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(11, 9, 11))
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(7).above())
                  .setSideOffset(new BlockPos(4, 0, 3))
                  .build(),
               CornerStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/corner/1")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(11, 9, 11))
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(7).above())
                  .setSideOffset(new BlockPos(4, 0, 3))
                  .build(),
               CornerStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/corner/2")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(11, 9, 11))
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(7).above())
                  .setSideOffset(new BlockPos(4, 0, 3))
                  .build(),
               CornerStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/corner/3")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(11, 9, 11))
                  .setEntranceOffset(BlockPos.ZERO.east(10).south(7).above())
                  .setSideOffset(new BlockPos(4, 0, 3))
                  .build()
            )
         )
         .addThinHallwayEnd(
            100.0F,
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/thin_end/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .setEntityPos(BlockPos.ZERO.east(6).above(3))
                  .build()
            )
         )
         .addDeadEndRoom(
            (float)(94 - (Integer)ServerConfig.INSTANCE.dungeonConfig.endRoomTrapChance.get()),
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(12, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(11).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/1")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(12, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(11).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/2")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(12, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(11).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/3")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(12, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(11).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/4")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(12, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(11).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/5")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build()
            ),
            () -> false
         )
         .addDeadEndRoom(
            (float)((Integer)ServerConfig.INSTANCE.dungeonConfig.endRoomTrapChance.get()).intValue(),
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/trap/invisible_silver_tnt")
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/trap/mimic_gold")
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/trap/mimic_silver")
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/trap/paralyze_bronze")
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/trap/paralyze_gold")
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/trap/paralyze_silver")
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/trap/steel_thread_bronze_chest")
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/trap/steel_thread_broze_tnt")
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/trap/steel_thread_gold_chest")
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/trap/steel_thread_invisible_gold_chest")
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/trap/tnt_bronze")
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/trap/tnt_silver")
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build()
            ),
            () -> false
         )
         .addDeadEndRoom(
            3.0F,
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/treasure/bronze")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build()
            ),
            () -> false
         )
         .addDeadEndRoom(
            1.0F,
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/treasure/gold")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build()
            ),
            () -> false
         )
         .addDeadEndRoom(
            2.0F,
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/treasure/silver")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build()
            ),
            () -> false
         )
         .addDeadEndRoom(
            3.0F,
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/trial/silver/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build()
            )
         )
         .addDeadEndRoom(
            2.0F,
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/trial/gold/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build()
            )
         )
         .addDeadEndRoom(
            4.0F,
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/trial/bronze/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build()
            )
         )
         .addDeadEndRoom(
            1.0F,
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/dead_end/trial/stellar/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build()
            )
         )
         .addOneWayRoom(
            97.0F,
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/one_way/0")
                  .setSize(new BlockPos(12, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(11).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/one_way/1")
                  .setSize(new BlockPos(12, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(11).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/one_way/2")
                  .setSize(new BlockPos(12, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(11).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build()
            )
         )
         .addOneWayRoom(
            3.0F,
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/trap/steel_thread")
                  .setSize(new BlockPos(12, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(11).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/trap/steel_thread_1")
                  .setSize(new BlockPos(12, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(11).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/room/trap/rotation_floors")
                  .setSize(new BlockPos(12, 8, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(11).south(7))
                  .setEntityPos(BlockPos.ZERO.east(5).above(2))
                  .build()
            )
         )
         .addLeftSideRoom(
            100.0F,
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/side/left/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .build()
            )
         )
         .addRightSideRoom(
            95.0F,
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/side/right/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/side/right/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .build()
            )
         )
         .addRightSideRoom(
            5.0F,
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/side/right/trap/steel_thread")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .build(),
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/side/right/trap/steel_thread_1")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .build()
            )
         )
         .addBothSideRoom(
            100.0F,
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/hallway/side/both/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 9, 7))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above())
                  .build()
            )
         )
         .addStairs(
            100.0F,
            List.of(
               StairsStructureInfo.builder()
                  .setAddress("dungeon/floors/11-20/stairs/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(15, 20, 15))
                  .setEntranceOffset(BlockPos.ZERO.east(14).south(3).above(12))
                  .build()
            )
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/11-20/room/boss/0")
               .setFacing(Direction.EAST)
               .setSize(new BlockPos(24, 12, 25))
               .setEntranceOffset(BlockPos.ZERO.east(23).south(26))
               .setProvider(BeyondTeleporterUpdated::spawnEvilCentipede)
               .setExitPosition(BlockPos.ZERO.east(51).south(2))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/11-20/room/boss/1")
               .setFacing(Direction.EAST)
               .setSize(new BlockPos(24, 12, 27))
               .setEntranceOffset(BlockPos.ZERO.east(23).south(2))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/11-20/room/boss/2")
               .setFacing(Direction.EAST)
               .setSize(new BlockPos(27, 12, 27))
               .setEntranceOffset(BlockPos.ZERO.east(50).south(2))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/11-20/room/boss/3")
               .setFacing(Direction.EAST)
               .setSize(new BlockPos(27, 12, 25))
               .setEntranceOffset(BlockPos.ZERO.east(50).south(27))
               .build()
         )
         .end();
      Database.F21_30
         .addThinHallway(
            1.0F,
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/21-30/hallway/thin/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(19, 9, 12))
                  .setEntranceOffset(BlockPos.ZERO.east(18).south(5))
                  .build()
            )
         )
         .addThinHallwayEnd(
            1.0F,
            List.of(
               DeadEndStructureInfo.builder()
                  .setAddress("dungeon/floors/21-30/hallway/thin_end/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(21, 9, 12))
                  .setEntranceOffset(BlockPos.ZERO.east(20).south(6))
                  .build()
            )
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/21-30/room/boss/0")
               .setFacing(Direction.EAST)
               .setSize(new BlockPos(26, 12, 28))
               .setEntranceOffset(BlockPos.ZERO.east(25).south(26))
               .setProvider(BeyondTeleporterUpdated::spawnOgreLord)
               .setExitPosition(BlockPos.ZERO.east(53))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/21-30/room/boss/1")
               .setFacing(Direction.EAST)
               .setSize(new BlockPos(26, 12, 26))
               .setEntranceOffset(BlockPos.ZERO.east(25).north(2))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/21-30/room/boss/2")
               .setFacing(Direction.EAST)
               .setSize(new BlockPos(27, 12, 26))
               .setEntranceOffset(BlockPos.ZERO.east(52).north(2))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/21-30/room/boss/3")
               .setFacing(Direction.EAST)
               .setSize(new BlockPos(27, 12, 28))
               .setEntranceOffset(BlockPos.ZERO.east(52).south(26))
               .build()
         )
         .end();
      Database.F31_40
         .addThinHallway(
            1.0F,
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/31-40/hallway/thin/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(14, 10, 12))
                  .setEntranceOffset(BlockPos.ZERO.east(13).south(5).above())
                  .build()
            )
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/31-40/room/boss/0")
               .setFacing(Direction.EAST)
               .setSize(new BlockPos(27, 12, 27))
               .setEntranceOffset(BlockPos.ZERO.east(26).south(26).above())
               .setProvider(BeyondTeleporterUpdated::spawnTempestSerpant)
               .setExitPosition(BlockPos.ZERO.east(52).above())
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/31-40/room/boss/1")
               .setFacing(Direction.EAST)
               .setSize(new BlockPos(27, 12, 27))
               .setEntranceOffset(BlockPos.ZERO.east(26).north().above())
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/31-40/room/boss/2")
               .setFacing(Direction.EAST)
               .setSize(new BlockPos(25, 12, 27))
               .setEntranceOffset(BlockPos.ZERO.east(51).north().above())
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/31-40/room/boss/3")
               .setFacing(Direction.EAST)
               .setSize(new BlockPos(25, 12, 27))
               .setEntranceOffset(BlockPos.ZERO.east(51).south(26).above())
               .build()
         )
         .end();
      Database.F41_50
         .addThinHallway(
            1.0F,
            List.of(
               StraightStructureInfo.builder()
                  .setAddress("dungeon/floors/31-40/hallway/thin/0")
                  .setFacing(Direction.EAST)
                  .setSize(new BlockPos(14, 10, 12))
                  .setEntranceOffset(BlockPos.ZERO.east(13).south(5).above(2))
                  .build()
            )
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/41-50/room/boss/0")
               .setFacing(Direction.EAST)
               .setSize(new BlockPos(27, 12, 27))
               .setEntranceOffset(BlockPos.ZERO.east(26).south(26).above())
               .setProvider(BeyondTeleporterUpdated::spawnPlainBoss)
               .setExitPosition(BlockPos.ZERO.east(52))
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/41-50/room/boss/1")
               .setFacing(Direction.EAST)
               .setSize(new BlockPos(25, 12, 27))
               .setEntranceOffset(BlockPos.ZERO.east(51).south(26).above())
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/41-50/room/boss/2")
               .setFacing(Direction.EAST)
               .setSize(new BlockPos(27, 12, 26))
               .setEntranceOffset(BlockPos.ZERO.east(26).north().above())
               .build()
         )
         .addBossRoomPart(
            BossRoomPartStructureInfo.builder()
               .setAddress("dungeon/floors/41-50/room/boss/3")
               .setFacing(Direction.EAST)
               .setSize(new BlockPos(25, 12, 26))
               .setEntranceOffset(BlockPos.ZERO.east(51).north().above())
               .build()
         )
         .end();
   }
}
