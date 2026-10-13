package io.github.Memoires.trbeyond.network.worldgen.portal;

import io.github.Memoires.trbeyond.network.worldgen.portal.info.BossRoomPartStructureInfo;
import io.github.Memoires.trbeyond.network.worldgen.portal.info.DungeonStructureInfo;
import io.github.Memoires.trbeyond.util.DungeonWeightedList;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

public class DungeonStructureSet {
   private final DungeonWeightedList<DungeonStructureInfo> THIN_HALLWAY = DungeonWeightedList.newList();
   private final DungeonWeightedList<DungeonStructureInfo> LONG_THIN_HALLWAY = DungeonWeightedList.newList();
   private final DungeonWeightedList<DungeonStructureInfo> T_JUNCTION = DungeonWeightedList.newList();
   private final DungeonWeightedList<DungeonStructureInfo> CORNER_HALLWAY = DungeonWeightedList.newList();
   private final DungeonWeightedList<DungeonStructureInfo> THIN_HALLWAY_END = DungeonWeightedList.newList();
   private final DungeonWeightedList<DungeonStructureInfo> DEAD_END_ROOM = DungeonWeightedList.newList();
   private final DungeonWeightedList<DungeonStructureInfo> ONEWAY_ROOM = DungeonWeightedList.newList();
   private final DungeonWeightedList<DungeonStructureInfo> LEFT_SIDEROOM_HALLWAY = DungeonWeightedList.newList();
   private final DungeonWeightedList<DungeonStructureInfo> RIGHT_SIDEROOM_HALLWAY = DungeonWeightedList.newList();
   private final DungeonWeightedList<DungeonStructureInfo> BOTH_SIDEROOM_HALLWAY = DungeonWeightedList.newList();
   private final DungeonWeightedList<DungeonStructureInfo> STAIRS = DungeonWeightedList.newList();
   private final List<BossRoomPartStructureInfo> BOSS_ROOM = new ArrayList<>();

   private DungeonStructureSet() {
   }

   @NotNull
   public static DungeonStructureSet newSet() {
      return new DungeonStructureSet();
   }

   public DungeonStructureInfo getThinHallway(RandomSource random) {
      return this.getThinHallway(random, true);
   }

   public DungeonStructureInfo getThinHallway(RandomSource random, boolean testValue) {
      return this.THIN_HALLWAY.getRandom(random, testValue);
   }

   public DungeonStructureInfo getLongThinHallway(RandomSource random) {
      return this.getLongThinHallway(random, true);
   }

   public DungeonStructureInfo getLongThinHallway(RandomSource random, boolean testValue) {
      return this.LONG_THIN_HALLWAY.getRandom(random, testValue);
   }

   public DungeonStructureInfo getJunction(RandomSource random) {
      return this.getJunction(random, true);
   }

   public DungeonStructureInfo getJunction(RandomSource random, boolean testValue) {
      return this.T_JUNCTION.getRandom(random, testValue);
   }

   public DungeonStructureInfo getCornerHallway(RandomSource random) {
      return this.getCornerHallway(random, true);
   }

   public DungeonStructureInfo getCornerHallway(RandomSource random, boolean testValue) {
      return this.CORNER_HALLWAY.getRandom(random, testValue);
   }

   public DungeonStructureInfo getThinHallwayEnd(RandomSource random) {
      return this.getThinHallwayEnd(random, true);
   }

   public DungeonStructureInfo getThinHallwayEnd(RandomSource random, boolean testValue) {
      return this.THIN_HALLWAY_END.getRandom(random, testValue);
   }

   public DungeonStructureInfo getDeadEndRoom(RandomSource random) {
      return this.getDeadEndRoom(random, true);
   }

   public DungeonStructureInfo getDeadEndRoom(RandomSource random, boolean testValue) {
      return this.DEAD_END_ROOM.getRandom(random, testValue);
   }

   public DungeonStructureInfo getOneWayRoom(RandomSource random) {
      return this.getOneWayRoom(random, true);
   }

   public DungeonStructureInfo getOneWayRoom(RandomSource random, boolean testValue) {
      return this.ONEWAY_ROOM.getRandom(random, testValue);
   }

   public DungeonStructureInfo getLeftSideRoom(RandomSource random) {
      return this.getLeftSideRoom(random, true);
   }

   public DungeonStructureInfo getLeftSideRoom(RandomSource random, boolean testValue) {
      return this.LEFT_SIDEROOM_HALLWAY.getRandom(random, testValue);
   }

   public DungeonStructureInfo getRightSideRoom(RandomSource random) {
      return this.getRightSideRoom(random, true);
   }

   public DungeonStructureInfo getRightSideRoom(RandomSource random, boolean testValue) {
      return this.RIGHT_SIDEROOM_HALLWAY.getRandom(random, testValue);
   }

   public DungeonStructureInfo getBothSideRoom(RandomSource random) {
      return this.getBothSideRoom(random, true);
   }

   public DungeonStructureInfo getBothSideRoom(RandomSource random, boolean testValue) {
      return this.BOTH_SIDEROOM_HALLWAY.getRandom(random, testValue);
   }

   public DungeonStructureInfo getStairs(RandomSource random) {
      return this.getStairs(random, true);
   }

   public DungeonStructureInfo getStairs(RandomSource random, boolean testValue) {
      return this.STAIRS.getRandom(random, testValue);
   }

   public List<BossRoomPartStructureInfo> getBossRoom() {
      return this.BOSS_ROOM;
   }

   public DungeonStructureSet addThinHallway(float weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition) {
      return this.addThinHallway(() -> weight, addresses, condition);
   }

   public DungeonStructureSet addThinHallway(float weight, List<DungeonStructureInfo> addresses) {
      return this.addThinHallway(() -> weight, addresses);
   }

   public DungeonStructureSet addThinHallway(Supplier<Float> weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition) {
      this.THIN_HALLWAY.add(weight, addresses, condition);
      return this;
   }

   public DungeonStructureSet addThinHallway(Supplier<Float> weight, List<DungeonStructureInfo> addresses) {
      this.THIN_HALLWAY.add(weight, addresses);
      return this;
   }

   public DungeonStructureSet addLongThinHallway(float weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition) {
      return this.addLongThinHallway(() -> weight, addresses, condition);
   }

   public DungeonStructureSet addLongThinHallway(float weight, List<DungeonStructureInfo> addresses) {
      return this.addLongThinHallway(() -> weight, addresses);
   }

   public DungeonStructureSet addLongThinHallway(
      Supplier<Float> weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition
   ) {
      this.LONG_THIN_HALLWAY.add(weight, addresses, condition);
      return this;
   }

   public DungeonStructureSet addLongThinHallway(Supplier<Float> weight, List<DungeonStructureInfo> addresses) {
      this.LONG_THIN_HALLWAY.add(weight, addresses);
      return this;
   }

   public DungeonStructureSet addJunction(float weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition) {
      return this.addJunction(() -> weight, addresses, condition);
   }

   public DungeonStructureSet addJunction(float weight, List<DungeonStructureInfo> addresses) {
      return this.addJunction(() -> weight, addresses);
   }

   public DungeonStructureSet addJunction(Supplier<Float> weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition) {
      this.T_JUNCTION.add(weight, addresses, condition);
      return this;
   }

   public DungeonStructureSet addJunction(Supplier<Float> weight, List<DungeonStructureInfo> addresses) {
      this.T_JUNCTION.add(weight, addresses);
      return this;
   }

   public DungeonStructureSet addCornerHallway(float weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition) {
      return this.addCornerHallway(() -> weight, addresses, condition);
   }

   public DungeonStructureSet addCornerHallway(float weight, List<DungeonStructureInfo> addresses) {
      return this.addCornerHallway(() -> weight, addresses);
   }

   public DungeonStructureSet addCornerHallway(
      Supplier<Float> weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition
   ) {
      this.CORNER_HALLWAY.add(weight, addresses, condition);
      return this;
   }

   public DungeonStructureSet addCornerHallway(Supplier<Float> weight, List<DungeonStructureInfo> addresses) {
      this.CORNER_HALLWAY.add(weight, addresses);
      return this;
   }

   public DungeonStructureSet addThinHallwayEnd(float weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition) {
      return this.addThinHallwayEnd(() -> weight, addresses, condition);
   }

   public DungeonStructureSet addThinHallwayEnd(float weight, List<DungeonStructureInfo> addresses) {
      return this.addThinHallwayEnd(() -> weight, addresses);
   }

   public DungeonStructureSet addThinHallwayEnd(
      Supplier<Float> weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition
   ) {
      this.THIN_HALLWAY_END.add(weight, addresses, condition);
      return this;
   }

   public DungeonStructureSet addThinHallwayEnd(Supplier<Float> weight, List<DungeonStructureInfo> addresses) {
      this.THIN_HALLWAY_END.add(weight, addresses);
      return this;
   }

   public DungeonStructureSet addDeadEndRoom(float weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition) {
      return this.addDeadEndRoom(() -> weight, addresses, condition);
   }

   public DungeonStructureSet addDeadEndRoom(float weight, List<DungeonStructureInfo> addresses) {
      return this.addDeadEndRoom(() -> weight, addresses);
   }

   public DungeonStructureSet addDeadEndRoom(Supplier<Float> weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition) {
      this.DEAD_END_ROOM.add(weight, addresses, condition);
      return this;
   }

   public DungeonStructureSet addDeadEndRoom(Supplier<Float> weight, List<DungeonStructureInfo> addresses) {
      this.DEAD_END_ROOM.add(weight, addresses);
      return this;
   }

   public DungeonStructureSet addOneWayRoom(float weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition) {
      return this.addOneWayRoom(() -> weight, addresses, condition);
   }

   public DungeonStructureSet addOneWayRoom(float weight, List<DungeonStructureInfo> addresses) {
      return this.addOneWayRoom(() -> weight, addresses);
   }

   public DungeonStructureSet addOneWayRoom(Supplier<Float> weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition) {
      this.ONEWAY_ROOM.add(weight, addresses, condition);
      return this;
   }

   public DungeonStructureSet addOneWayRoom(Supplier<Float> weight, List<DungeonStructureInfo> addresses) {
      this.ONEWAY_ROOM.add(weight, addresses);
      return this;
   }

   public DungeonStructureSet addLeftSideRoom(float weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition) {
      return this.addLeftSideRoom(() -> weight, addresses, condition);
   }

   public DungeonStructureSet addLeftSideRoom(float weight, List<DungeonStructureInfo> addresses) {
      return this.addLeftSideRoom(() -> weight, addresses);
   }

   public DungeonStructureSet addLeftSideRoom(
      Supplier<Float> weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition
   ) {
      this.LEFT_SIDEROOM_HALLWAY.add(weight, addresses);
      return this;
   }

   public DungeonStructureSet addLeftSideRoom(Supplier<Float> weight, List<DungeonStructureInfo> addresses) {
      this.LEFT_SIDEROOM_HALLWAY.add(weight, addresses);
      return this;
   }

   public DungeonStructureSet addRightSideRoom(float weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition) {
      return this.addRightSideRoom(() -> weight, addresses, condition);
   }

   public DungeonStructureSet addRightSideRoom(float weight, List<DungeonStructureInfo> addresses) {
      return this.addRightSideRoom(() -> weight, addresses);
   }

   public DungeonStructureSet addRightSideRoom(
      Supplier<Float> weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition
   ) {
      this.RIGHT_SIDEROOM_HALLWAY.add(weight, addresses, condition);
      return this;
   }

   public DungeonStructureSet addRightSideRoom(Supplier<Float> weight, List<DungeonStructureInfo> addresses) {
      this.RIGHT_SIDEROOM_HALLWAY.add(weight, addresses);
      return this;
   }

   public DungeonStructureSet addBothSideRoom(float weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition) {
      return this.addBothSideRoom(() -> weight, addresses, condition);
   }

   public DungeonStructureSet addBothSideRoom(float weight, List<DungeonStructureInfo> addresses) {
      return this.addBothSideRoom(() -> weight, addresses);
   }

   public DungeonStructureSet addBothSideRoom(
      Supplier<Float> weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition
   ) {
      this.BOTH_SIDEROOM_HALLWAY.add(weight, addresses, condition);
      return this;
   }

   public DungeonStructureSet addBothSideRoom(Supplier<Float> weight, List<DungeonStructureInfo> addresses) {
      this.BOTH_SIDEROOM_HALLWAY.add(weight, addresses);
      return this;
   }

   public DungeonStructureSet addStairs(float weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition) {
      return this.addStairs(() -> weight, addresses, condition);
   }

   public DungeonStructureSet addStairs(float weight, List<DungeonStructureInfo> addresses) {
      return this.addStairs(() -> weight, addresses);
   }

   public DungeonStructureSet addStairs(Supplier<Float> weight, List<DungeonStructureInfo> addresses, DungeonWeightedList.WeightedEntryCondition condition) {
      this.STAIRS.add(weight, addresses, condition);
      return this;
   }

   public DungeonStructureSet addStairs(Supplier<Float> weight, List<DungeonStructureInfo> addresses) {
      this.STAIRS.add(weight, addresses);
      return this;
   }

   public DungeonStructureSet addBossRoomPart(@NotNull BossRoomPartStructureInfo part) {
      this.BOSS_ROOM.add(part);
      return this;
   }

   public DungeonStructureSet end() {
      this.THIN_HALLWAY.end();
      this.LONG_THIN_HALLWAY.end();
      this.T_JUNCTION.end();
      this.CORNER_HALLWAY.end();
      this.THIN_HALLWAY_END.end();
      this.DEAD_END_ROOM.end();
      this.ONEWAY_ROOM.end();
      this.LEFT_SIDEROOM_HALLWAY.end();
      this.RIGHT_SIDEROOM_HALLWAY.end();
      this.BOTH_SIDEROOM_HALLWAY.end();
      this.STAIRS.end();
      return this;
   }
}
