package io.github.Memoires.trbeyond.network.worldgen.portal;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.data.TensuraTags.EntityTypes;
import com.github.manasmods.tensura.data.pack.EntityEPCount;
import com.github.manasmods.tensura.data.pack.TensuraData;
import com.github.manasmods.tensura.entity.BarghestEntity;
import com.github.manasmods.tensura.entity.BulldeerEntity;
import com.github.manasmods.tensura.entity.DirewolfEntity;
import com.github.manasmods.tensura.entity.DragonPeacockEntity;
import com.github.manasmods.tensura.entity.GiantAntEntity;
import com.github.manasmods.tensura.entity.GiantBatEntity;
import com.github.manasmods.tensura.entity.HornedRabbitEntity;
import com.github.manasmods.tensura.entity.HoundDogEntity;
import com.github.manasmods.tensura.entity.HoverLizardEntity;
import com.github.manasmods.tensura.entity.LeechLizardEntity;
import com.github.manasmods.tensura.entity.OneEyedOwlEntity;
import com.github.manasmods.tensura.entity.SlimeEntity;
import com.github.manasmods.tensura.entity.human.MarkLaurenEntity;
import com.github.manasmods.tensura.entity.human.ShinRyuseiEntity;
import com.github.manasmods.tensura.entity.human.ShinjiTanimuraEntity;
import com.github.manasmods.tensura.menu.RaceSelectionMenu;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import com.github.manasmods.tensura.registry.skill.ExtraSkills;
import com.github.manasmods.tensura.registry.skill.UniqueSkills;
import io.github.Memoires.trbeyond.Database;
import io.github.Memoires.trbeyond.block.DungeonAnchor;
import io.github.Memoires.trbeyond.block.DungeonUnlockerBlock;
import io.github.Memoires.trbeyond.block.entity.AnchorBlockEntity;
import io.github.Memoires.trbeyond.capability.entities.EliteMobCapability;
import io.github.Memoires.trbeyond.commands.ClearDungeonCommand;
import io.github.Memoires.trbeyond.entity.custom.BossBlackSpiderEntity;
import io.github.Memoires.trbeyond.entity.custom.EvilCentipedeBossEntity;
import io.github.Memoires.trbeyond.entity.custom.GozulEntity;
import io.github.Memoires.trbeyond.entity.custom.LivingArmorEntity;
import io.github.Memoires.trbeyond.entity.custom.MageEntity;
import io.github.Memoires.trbeyond.entity.custom.MezulEntity;
import io.github.Memoires.trbeyond.entity.custom.OgreLordEntity;
import io.github.Memoires.trbeyond.entity.custom.OgreSubEntity;
import io.github.Memoires.trbeyond.entity.custom.ScarletSlimeEntity;
import io.github.Memoires.trbeyond.entity.custom.SkullNightEntity;
import io.github.Memoires.trbeyond.entity.custom.TempestSerpentBossEntity;
import io.github.Memoires.trbeyond.network.worldgen.DungeonSaveData;
import io.github.Memoires.trbeyond.network.worldgen.portal.info.BossRoomPartStructureInfo;
import io.github.Memoires.trbeyond.network.worldgen.portal.info.DungeonStructureInfo;
import io.github.Memoires.trbeyond.network.worldgen.portal.info.ShopStructureInfo;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlocks;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import io.github.Memoires.trbeyond.util.BeyondSpawnUtils;
import io.github.Memoires.trbeyond.util.DungeonEntitiesSpecfications;
import io.github.Memoires.trbeyond.util.FunctionSpecifications;
import io.github.Memoires.trbeyond.util.MatrixTraversal;
import io.github.Memoires.trbeyond.util.MutableBlockPos;
import io.github.Memoires.trbeyond.world.BeyondGamerules;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class BeyondTeleporterUpdated {
   public static boolean STOPSPAWNS = false;
   public static int SPAWN_FREQUENCY = 0;
   public static int entitySpawnFrequency;
   public static boolean DontSpawnEntities = false;
   public static List<DungeonEntitiesSpecfications> entitySpecifications;
   public static int NUM_STRUCTURES = 30;
   public static double LOOPBACK_PROB = 0.75;
   public static double DEADEND_PROB = 0.6;
   public static final int MAX_FLOOR_AMOUNT = 20;
   public static boolean BOSS_RUSH = true;
   public static BlockPos Floor95ShopPos = new BlockPos(-17, 191, 0);
   public static BlockPos BackToLobbyDoor = new BlockPos(-28, 192, 15);
   public static BlockPos BackToLastKnownLocationDoor = new BlockPos(-17, 192, -2);
   public static BlockPos DontLetThisDoorOpen = null;
   public static BlockPos BossRushTeleporterDoor = null;
   private static final Map<String, RegistryObject<? extends Skill>> UNIQUE_SKILL_MAP = Map.ofEntries(
      Map.entry("OBSERVER", UniqueSkills.OBSERVER),
      Map.entry("REAPER", UniqueSkills.REAPER),
      Map.entry("SEER", UniqueSkills.SEER),
      Map.entry("FIGHTER", UniqueSkills.FIGHTER),
      Map.entry("SHADOW_STRIKER", UniqueSkills.SHADOW_STRIKER),
      Map.entry("SNIPER", UniqueSkills.SNIPER),
      Map.entry("SURVIVOR", UniqueSkills.SURVIVOR),
      Map.entry("TUNER", UniqueSkills.TUNER),
      Map.entry("GOURMAND", UniqueSkills.GOURMAND),
      Map.entry("ANTI_SKILL", UniqueSkills.ANTI_SKILL),
      Map.entry("CHOSEN_ONE", UniqueSkills.CHOSEN_ONE),
      Map.entry("COMMANDER", UniqueSkills.COMMANDER),
      Map.entry("COOK", UniqueSkills.COOK),
      Map.entry("ENGORGER", UniqueSkills.ENGORGER),
      Map.entry("GUARDIAN", UniqueSkills.GUARDIAN),
      Map.entry("MARTIAL_MASTER", UniqueSkills.MARTIAL_MASTER),
      Map.entry("MATHEMATICIAN", UniqueSkills.MATHEMATICIAN)
   );
   private static final Map<String, RegistryObject<? extends Skill>> EXTRA_SKILL_MAP = Map.of(
      "ALL_SEEING_EYE",
      ExtraSkills.ALL_SEEING_EYE,
      "STRENGTHEN_BODY",
      ExtraSkills.STRENGTHEN_BODY,
      "STEEL_STRENGTH",
      ExtraSkills.STEEL_STRENGTH,
      "BLACK_FLAME",
      ExtraSkills.BLACK_FLAME,
      "ULTRA_INSTINCT",
      ExtraSkills.ULTRA_INSTINCT,
      "HEAVENLY_EYE",
      ExtraSkills.HEAVENLY_EYE,
      "MAGIC_JAMMING",
      ExtraSkills.MAGIC_JAMMING,
      "ULTRA_REGENERATION",
      ExtraSkills.ULTRASPEED_REGENERATION
   );
   private static final String[] UNIQUE_SKILL = new String[]{
      "OBSERVER",
      "REAPER",
      "SEER",
      "FIGHTER",
      "SHADOW_STRIKER",
      "SNIPER",
      "SURVIVOR",
      "TUNER",
      "GOURMAND",
      "ANTI_SKILL",
      "CHOSEN_ONE",
      "COMMANDER",
      "COOK",
      "ENGORGER",
      "GUARDIAN",
      "MARTIAL_MASTER",
      "MATHEMATICIAN"
   };
   private static final String[] EXTRA_SKILL = new String[]{
      "ALL_SEEING_EYE", "STRENGTHEN_BODY", "STEEL_STRENGTH", "BLACK_FLAME", "ULTRA_INSTINCT", "HEAVENLY_EYE", "MAGIC_JAMMING", "ULTRA_REGENERATION"
   };

   public static boolean generateDungeon(ServerLevel currentWorld, ServerLevel destinationWorld) {
      STOPSPAWNS = false;
      entitySpawnFrequency = SPAWN_FREQUENCY;
      ClearDungeonCommand.dungeonCleared = false;
      if (ClearDungeonCommand.dungeonCleared && ClearDungeonCommand.createDungeon) {
         NUM_STRUCTURES = ClearDungeonCommand.NUM_STRUCTURES;
         LOOPBACK_PROB = ClearDungeonCommand.LOOPBACK_PROB;
         DEADEND_PROB = ClearDungeonCommand.DEADEND_PROB;
         ClearDungeonCommand.dungeonCleared = false;
         ClearDungeonCommand.createDungeon = false;
      }

      int[] coords = new int[]{0, 120, 0};
      int x = coords[0];
      int y = coords[1];
      int z = coords[2];
      BlockPos destinationPos = new BlockPos(x, y, z);
      destinationPos = destinationPos.above(20);
      BlockState state = destinationWorld.getBlockState(destinationPos);
      if (!state.hasProperty(DungeonAnchor.GENERATED)) {
         currentWorld.getServer()
            .getPlayerList()
            .getPlayers()
            .forEach(player -> player.sendSystemMessage(Component.translatable("trbeyond.message.generation").withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))));
         destinationWorld.setBlockAndUpdate(destinationPos, ((DungeonAnchor)BeyondBlocks.DUNGEON_ANCHOR.get()).defaultBlockState());
         if (destinationWorld.getBlockEntity(destinationPos) instanceof AnchorBlockEntity anchor) {
            anchor.setCurrentFloor(0);
         }

         StructureTemplateManager manager = destinationWorld.getStructureManager();
         StructurePlaceSettings settings = new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(false);
         BlockPos leftPos = destinationPos.below(20).below(5).west(15);
         placeStructure(manager, destinationWorld, leftPos, "dungeon/start/lobby_left", settings);
         BlockPos rightPos = leftPos.north(27);
         placeStructure(manager, destinationWorld, rightPos, "dungeon/start/lobby_right", settings);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, destinationPos.east(29).south(4).below(23), Rotation.CLOCKWISE_90, true, 4, 0);
         StructurePlaceSettings hallwaySettings = new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(false);
         DungeonSaveData.getOverworld(destinationWorld).setLobbyPos(destinationPos.below(24).north(15).east(9));
         BlockPos floor95Shop = destinationPos.above(50).west(29).north(2);
         placeStructure(manager, destinationWorld, floor95Shop, "dungeon/room/restaurant_1_best", hallwaySettings);
         placeStructure(manager, destinationWorld, floor95Shop.east(30), "dungeon/room/restaurant_2_best", hallwaySettings);
         Floor95ShopPos = floor95Shop.above(3).east(3).south(3);
         DungeonSaveData.getOverworld(destinationWorld).setFloor95ShopPos(floor95Shop.above(3).east(3).south(3));
         BackToLobbyDoor = floor95Shop.above(2).east(1).south(17);
         BackToLastKnownLocationDoor = floor95Shop.above(2).east(12).south(0);
         DungeonSaveData.getOverworld(destinationWorld).setBackToLobbyDoor(BackToLobbyDoor);
         DungeonSaveData.getOverworld(destinationWorld).setBackToLastKnownLocationDoor(BackToLastKnownLocationDoor);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, BackToLobbyDoor, Rotation.CLOCKWISE_90, true, 2, 0);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, BackToLastKnownLocationDoor, Rotation.NONE, true, 3, 0);
         hallwaySettings = new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(Rotation.COUNTERCLOCKWISE_90).setIgnoreEntities(false);
         String direction = "south";
         BlockPos firstHallwayPos = destinationPos.below(25).east(7).south(37);
         placeStructure(manager, destinationWorld, firstHallwayPos, "dungeon/floors/1-10/hallway/thin/0", hallwaySettings);
         DontSpawnEntities = true;
         MutableBlockPos previousHallwayPos = new MutableBlockPos(
            firstHallwayPos.south(1).east(2), new StructurePlaceSettings().setIgnoreEntities(true).setRotation(Rotation.CLOCKWISE_90), direction
         );
         placeAllBossRooms(manager, destinationWorld, previousHallwayPos);
         DontSpawnEntities = false;
         placeStairs(previousHallwayPos, manager, destinationWorld);
         if (!currentWorld.getGameRules().getBoolean(BeyondGamerules.MOBS_SPAWN_WITH_DUNGEON)) {
            DontSpawnEntities = true;
            STOPSPAWNS = true;
         }

         spawnAllEntities(destinationWorld);
         DungeonSaveData.getOverworld(currentWorld).setLoaded(true);
         return true;
      } else {
         return false;
      }
   }

   private static void placeAllBossRooms(StructureTemplateManager manager, ServerLevel destinationWorld, MutableBlockPos previousHallwayPos) {
      if (BOSS_RUSH) {
         RandomSource random = destinationWorld.getRandom();
         MutableBlockPos newPos = previousHallwayPos.copy();
         newPos.pos = previousHallwayPos.pos.above(50);
         DungeonSaveData.getOverworld(destinationWorld).setBossRushPos(newPos.pos.south(6).east(1).above(2));
         BlockPos floor95DoorPos = newPos.pos.south(3).east(3).above(2);
         BlockPos lobbyDoorPos = floor95DoorPos.west(5);
         newPos.floorcount = 30;
         DungeonStructureInfo startInfo = Database.F21_30.getThinHallwayEnd(random);
         newPos.pos = newPos.pos.south(startInfo.getSize().getX());
         newPos.direction = Direction.NORTH.getName();
         placeThinHallways(manager, destinationWorld, newPos, 1, startInfo);
         newPos.direction = Direction.SOUTH.getName();
         placeThinHallways(manager, destinationWorld, newPos, 2, Database.F21_30.getThinHallway(random));
         placeDesertBossRoom(manager, destinationWorld, newPos);
         newPos.floorcount = 40;
         placeThinHallways(manager, destinationWorld, newPos, 3, Database.F31_40.getThinHallway(random));
         placeSwampBossRoom(manager, destinationWorld, newPos);
         newPos.floorcount = 50;
         placeThinHallways(manager, destinationWorld, newPos, 3, Database.F41_50.getThinHallway(random));
         placePlainBossRoom(manager, destinationWorld, newPos);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, newPos.pos.above(1).north(), Rotation.NONE, true, 2, 0);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, floor95DoorPos, Rotation.NONE, true, 1, 0);
         placeDungeonUnlockerBlockWithRotation(destinationWorld, lobbyDoorPos, Rotation.NONE, true, 2, 0);
      }
   }

   private static void generateMaze(@NotNull MutableBlockPos previousHallwayPos, StructureTemplateManager manager, @NotNull ServerLevel destinationWorld) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      RandomSource random = destinationWorld.getRandom();
      placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, structureSet.getLongThinHallway(random));
      MatrixTraversal traversal = new MatrixTraversal(NUM_STRUCTURES, LOOPBACK_PROB, DEADEND_PROB, previousHallwayPos.direction);
      traversal.generateMaze();
      List<FunctionSpecifications> list = traversal.getFunctions();
      int hallwayCount = 0;
      boolean vanta = false;

      for (FunctionSpecifications func : list) {
         if (Objects.equals(func.getFunctionName(), "hallway")) {
            int prob = getRandomInRange(random, 1, 6);
            if (prob == 6) {
               placeHallwayRandomSideRoom(manager, destinationWorld, previousHallwayPos);
            } else {
               if (hallwayCount == 20) {
                  placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, structureSet.getLongThinHallway(random));
                  placefloor95hallway(manager, destinationWorld, previousHallwayPos);
               } else {
                  int prob2 = getRandomInRange(random, 1, 100);
                  if (prob2 == 1) {
                     placeThinHallways(manager, destinationWorld, previousHallwayPos, 5, structureSet.getLongThinHallway(random));
                     placeCornerHallway(previousHallwayPos, manager, destinationWorld, structureSet.getLongThinHallway(random));
                     vanta = true;
                  } else {
                     placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, structureSet.getLongThinHallway(random));
                  }
               }

               placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, structureSet.getLongThinHallway(random));
               hallwayCount++;
            }
         } else if (Objects.equals(func.getFunctionName(), "deadend")) {
            placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, structureSet.getThinHallwayEnd(random));
         } else if (Objects.equals(func.getFunctionName(), "corner_deadend")) {
            placeCornerHallway(previousHallwayPos, manager, destinationWorld, structureSet.getThinHallwayEnd(random));
         } else if (Objects.equals(func.getFunctionName(), "stairs")) {
            placeStairs(previousHallwayPos, manager, destinationWorld);
         } else if (Objects.equals(func.getFunctionName(), "room")) {
            int prob = getRandomInRange(random, 1, 3);
            if (prob == 3) {
               placeOnewayRoomWithoutDoors(manager, destinationWorld, previousHallwayPos);
            } else {
               placeOnewayRoom(manager, destinationWorld, previousHallwayPos);
            }
         } else if (Objects.equals(func.getFunctionName(), "corner")) {
            if (vanta) {
               vanta = false;
            } else {
               placeCornerHallway(previousHallwayPos, manager, destinationWorld, structureSet.getLongThinHallway(random));
            }
         } else if (Objects.equals(func.getFunctionName(), "three_way")) {
            if (vanta) {
               vanta = false;
            } else {
               place2wayHallway(previousHallwayPos, manager, destinationWorld, func.getSuccessDirection(), func.getNotSuccessCase(), func.getHallwayLen());
            }
         } else {
            placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, structureSet.getLongThinHallway(random));
         }
      }
   }

   private static void spawnAllEntities(ServerLevel serverLevel) {
      if (entitySpecifications != null && !entitySpecifications.isEmpty()) {
         for (DungeonEntitiesSpecfications spec : entitySpecifications) {
            ServerLevel world = spec.getWorld();
            BlockPos pos = spec.getPos();
            int floorCount = spec.getFloorCount();
            boolean treasureRoom = spec.isTreasureRoom();
            boolean hallwayEnt = spec.isHallwayEntity();
            if (treasureRoom) {
               BeyondSpawnUtils.spawnMobsAt(world, pos, hallwayEnt, true, treasureRoom);
            }
         }
      }
   }

   private static void placeThinHallways(
      StructureTemplateManager manager,
      @NotNull ServerLevel destinationWorld,
      @NotNull MutableBlockPos previousHallwayPos,
      int n,
      @NotNull DungeonStructureInfo info
   ) {
      Direction dir = Direction.byName(previousHallwayPos.direction);
      if (dir != null && !dir.getAxis().isVertical()) {
         for (int i = 0; i < n; i++) {
            BlockPos originPos = new BlockPos(previousHallwayPos.pos);
            BlockPos newHallwayPos = info.calculatePlacementPos(originPos, dir);
            BlockPos entityPos = info.calculateEntityPos(originPos, dir);
            if (info.getAddress().equals("dungeon/floors/1-10/hallway/trap/long_thin/special/slime")) {
               newHallwayPos = newHallwayPos.below();
            }

            placeStructure(
               manager, destinationWorld, newHallwayPos, info.getAddress(), new StructurePlaceSettings().setRotation(info.getRotationTo(dir)).setIgnoreEntities(true)
            );
            if (!DontSpawnEntities) {
               addFloorEntity(destinationWorld, entityPos, previousHallwayPos.floorcount, false, true);
            }

            List<DungeonStructureInfo.DungeonCursor> list = info.calculateNewCursors(originPos, dir);
            if (list.isEmpty()) {
               return;
            }

            DungeonStructureInfo.DungeonCursor cursor = list.get(0);
            previousHallwayPos.pos = cursor.pos();
            previousHallwayPos.direction = cursor.facing().getName();
         }
      }
   }

   private static void placeStairs(@NotNull MutableBlockPos previousHallwayPos, StructureTemplateManager manager, @NotNull ServerLevel destinationWorld) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      RandomSource random = destinationWorld.getRandom();
      DungeonStructureInfo info = structureSet.getStairs(random);
      Direction dir = Direction.byName(previousHallwayPos.direction);
      if (dir != null && !dir.getAxis().isVertical()) {
         BlockPos placementPos = info.calculatePlacementPos(previousHallwayPos.pos, dir);
         placeStructure(
            manager,
            destinationWorld,
            placementPos,
            structureSet.getStairs(random).getAddress(),
            new StructurePlaceSettings().setIgnoreEntities(true).setRotation(info.getRotationTo(dir))
         );
         DungeonStructureInfo.DungeonCursor cursor = info.calculateNewCursors(previousHallwayPos.pos, dir).get(0);
         previousHallwayPos.pos = cursor.pos();
         previousHallwayPos.direction = cursor.facing().getName();
         previousHallwayPos.floorcount++;
         MutableBlockPos newHallwayPos = previousHallwayPos.copy();
         if (newHallwayPos.floorcount <= 20) {
            if (newHallwayPos.floorcount % 10 != 0) {
               generateMaze(newHallwayPos, manager, destinationWorld);
            } else {
               placeThinHallways(manager, destinationWorld, newHallwayPos, 2, structureSet.getThinHallway(random));
               placefloor95hallway(manager, destinationWorld, newHallwayPos);
               placeBossRoom(manager, destinationWorld, newHallwayPos);
            }
         } else {
            newHallwayPos.floorcount--;
            placeDeadendRoom(manager, destinationWorld, newHallwayPos, false);
         }
      }
   }

   private static void placeDeadendRoom(
      StructureTemplateManager manager, @NotNull ServerLevel destinationWorld, @NotNull MutableBlockPos previousHallwayPos, boolean isTrial
   ) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      RandomSource random = destinationWorld.getRandom();
      DungeonStructureInfo info = structureSet.getDeadEndRoom(random, isTrial);
      Direction dir = Direction.byName(previousHallwayPos.direction);
      if (dir != null && !dir.getAxis().isVertical()) {
         BlockPos newHallwayPos = info.calculatePlacementPos(previousHallwayPos.pos, dir);
         StructurePlaceSettings settings = new StructurePlaceSettings().setIgnoreEntities(true).setRotation(info.getRotationTo(dir));
         placeStructure(manager, destinationWorld, newHallwayPos, info.getAddress(), settings);
      }
   }

   private static void placefloor95hallway(StructureTemplateManager manager, @NotNull ServerLevel destinationWorld, @NotNull MutableBlockPos previousHallwayPos) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      RandomSource random = destinationWorld.getRandom();
      DungeonStructureInfo info = structureSet.getThinHallway(random, false);
      if (info instanceof ShopStructureInfo shopInfo) {
         Direction dir = Direction.byName(previousHallwayPos.direction);
         if (dir != null && !dir.getAxis().isVertical()) {
            BlockPos newHallwayPos = info.calculatePlacementPos(previousHallwayPos.pos, dir);
            placeStructure(
               manager, destinationWorld, newHallwayPos, info.getAddress(), new StructurePlaceSettings().setIgnoreEntities(true).setRotation(info.getRotationTo(dir))
            );
            BlockPos unlockerPos = previousHallwayPos.pos
               .relative(dir, shopInfo.getShopOffset())
               .relative(dir.getCounterClockWise(), info.getSize().getZ() / 2)
               .above(2);
            if (structureSet == Database.F11_20) {
               unlockerPos = unlockerPos.relative(dir.getClockWise(), 1);
            }

            placeDungeonUnlockerBlockWithRotation(
               destinationWorld, unlockerPos, info.getRotationTo(dir), true, 1, calculateFloorForUnlocker(previousHallwayPos)
            );
            DungeonSaveData.getOverworld(destinationWorld).addFloor95Door(unlockerPos);
            DungeonStructureInfo.DungeonCursor cursor = info.calculateNewCursors(previousHallwayPos.pos, dir).get(0);
            previousHallwayPos.pos = cursor.pos();
            placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, structureSet.getThinHallway(random));
         }
      }
   }

   private static void placeBossRoom(StructureTemplateManager manager, @NotNull ServerLevel destinationWorld, @NotNull MutableBlockPos previousHallwayPos) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      RandomSource random = destinationWorld.getRandom();
      DungeonStructureInfo info = structureSet.getThinHallway(random);
      Direction dir = Direction.byName(previousHallwayPos.direction);
      if (dir != null && !dir.getAxis().isVertical()) {
         BlockPos basePos = new BlockPos(previousHallwayPos.pos);
         StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(info.getRotationTo(dir)).setIgnoreEntities(true);
         List<BossRoomPartStructureInfo> bossRoom = structureSet.getBossRoom();

         for (BossRoomPartStructureInfo bossRoomPart : bossRoom) {
            BlockPos placingPos = bossRoomPart.calculatePlacementPos(basePos, dir);
            placeStructure(manager, destinationWorld, placingPos, bossRoomPart.getAddress(), settings);
         }

         BossRoomPartStructureInfo mainPartInfo = bossRoom.get(0);
         mainPartInfo.getBossSpawner().spawnBoss(destinationWorld, basePos.relative(dir, 10).above(2), true, previousHallwayPos.floorcount, true);
         placeDungeonUnlockerBlockWithRotation(
            destinationWorld,
            basePos.relative(dir, -1).above(2),
            info.getRotationTo(dir).getRotated(Rotation.COUNTERCLOCKWISE_90),
            true,
            0,
            calculateFloorForUnlocker(previousHallwayPos)
         );
         BlockPos exitPosition = mainPartInfo.calculateExitPosition(basePos, dir);
         boolean mustGenFurther = previousHallwayPos.floorcount <= 20;
         placeDungeonUnlockerBlockWithRotation(
            destinationWorld,
            exitPosition.relative(dir.getOpposite()).above(2),
            info.getRotationTo(dir).getRotated(Rotation.COUNTERCLOCKWISE_90),
            true,
            mustGenFurther ? 0 : 3,
            calculateFloorForUnlocker(previousHallwayPos)
         );
         if (mustGenFurther) {
            previousHallwayPos.pos = new BlockPos(exitPosition);
            placeStairs(previousHallwayPos, manager, destinationWorld);
         }
      }
   }

   private static void placeHallwayRandomSideRoom(StructureTemplateManager manager, @NotNull ServerLevel destinationWorld, MutableBlockPos previousHallwayPos) {
      RandomSource random = destinationWorld.getRandom();
      int prob = getRandomInRange(random, 1, 9);
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

   private static void placeHallwayBothSideRoom(
      StructureTemplateManager manager, @NotNull ServerLevel destinationWorld, @NotNull MutableBlockPos previousHallwayPos
   ) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      RandomSource random = destinationWorld.getRandom();
      BlockPos originalPos = new BlockPos(previousHallwayPos.pos);
      DungeonStructureInfo info = structureSet.getBothSideRoom(random);
      Direction dir = Direction.byName(previousHallwayPos.direction);
      if (dir != null && !dir.getAxis().isVertical()) {
         StructurePlaceSettings hallway = new StructurePlaceSettings().setRotation(info.getRotationTo(dir)).setIgnoreEntities(true);
         BlockPos hallwayPos = info.calculatePlacementPos(originalPos, dir);
         placeStructure(manager, destinationWorld, hallwayPos, info.getAddress(), hallway);
         BlockPos junction = originalPos.relative(dir, info.getSize().getX() / 2);
         DungeonStructureInfo leftDeadEnd = structureSet.getDeadEndRoom(random, false);
         BlockPos leftPos = junction.relative(dir.getCounterClockWise(), info.getSize().getZ() / 2 + 1);
         BlockPos leftWithOffset = leftDeadEnd.calculatePlacementPos(leftPos, dir.getCounterClockWise());
         StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(leftDeadEnd.getRotationTo(dir.getCounterClockWise())).setIgnoreEntities(true);
         placeStructure(manager, destinationWorld, leftWithOffset, leftDeadEnd.getAddress(), settings);
         BlockPos entityPos = leftDeadEnd.calculateEntityPos(leftPos, dir.getCounterClockWise());
         addFloorEntity(destinationWorld, entityPos, previousHallwayPos.floorcount, true, false);
         BlockPos doorPos = leftPos.above(2).relative(dir.getClockWise());
         placeDungeonUnlockerBlockWithRotation(destinationWorld, doorPos, info.getRotationTo(dir), true, 0, calculateFloorForUnlocker(previousHallwayPos));
         DungeonStructureInfo rightDeadEnd = structureSet.getDeadEndRoom(random, false);
         BlockPos rightPos = junction.relative(dir.getClockWise(), info.getSize().getZ() / 2 + 1);
         BlockPos rightWithOffset = rightDeadEnd.calculatePlacementPos(rightPos, dir.getClockWise());
         settings = new StructurePlaceSettings().setRotation(rightDeadEnd.getRotationTo(dir.getClockWise())).setIgnoreEntities(true);
         placeStructure(manager, destinationWorld, rightWithOffset, rightDeadEnd.getAddress(), settings);
         entityPos = rightDeadEnd.calculateEntityPos(rightPos, dir.getClockWise());
         addFloorEntity(destinationWorld, entityPos, previousHallwayPos.floorcount, true, false);
         doorPos = rightPos.above(2).relative(dir.getCounterClockWise());
         placeDungeonUnlockerBlockWithRotation(destinationWorld, doorPos, info.getRotationTo(dir), true, 0, calculateFloorForUnlocker(previousHallwayPos));
         DungeonStructureInfo.DungeonCursor cursor = info.calculateNewCursors(originalPos, dir).get(0);
         previousHallwayPos.pos = cursor.pos();
         placeThinHallways(manager, destinationWorld, previousHallwayPos, 2, structureSet.getLongThinHallway(random));
      }
   }

   private static void placeHallwayLeftSideRoom(
      StructureTemplateManager manager, @NotNull ServerLevel destinationWorld, @NotNull MutableBlockPos previousHallwayPos
   ) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      RandomSource random = destinationWorld.getRandom();
      BlockPos originalPos = new BlockPos(previousHallwayPos.pos);
      DungeonStructureInfo info = structureSet.getLeftSideRoom(random);
      Direction dir = Direction.byName(previousHallwayPos.direction);
      if (dir != null && !dir.getAxis().isVertical()) {
         StructurePlaceSettings hallway = new StructurePlaceSettings().setRotation(info.getRotationTo(dir)).setIgnoreEntities(true);
         BlockPos hallwayPos = info.calculatePlacementPos(originalPos, dir);
         placeStructure(manager, destinationWorld, hallwayPos, info.getAddress(), hallway);
         BlockPos junction = originalPos.relative(dir, info.getSize().getX() / 2);
         DungeonStructureInfo leftDeadEnd = structureSet.getDeadEndRoom(random, false);
         BlockPos leftPos = junction.relative(dir.getCounterClockWise(), info.getSize().getZ() / 2 + 1);
         BlockPos leftWithOffset = leftDeadEnd.calculatePlacementPos(leftPos, dir.getCounterClockWise());
         StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(leftDeadEnd.getRotationTo(dir.getCounterClockWise())).setIgnoreEntities(true);
         placeStructure(manager, destinationWorld, leftWithOffset, leftDeadEnd.getAddress(), settings);
         BlockPos entityPos = leftDeadEnd.calculateEntityPos(leftPos, dir.getCounterClockWise());
         addFloorEntity(destinationWorld, entityPos, previousHallwayPos.floorcount, true, false);
         BlockPos doorPos = leftPos.above(2).relative(dir.getClockWise());
         placeDungeonUnlockerBlockWithRotation(destinationWorld, doorPos, info.getRotationTo(dir), true, 0, calculateFloorForUnlocker(previousHallwayPos));
         DungeonStructureInfo.DungeonCursor cursor = info.calculateNewCursors(originalPos, dir).get(0);
         previousHallwayPos.pos = cursor.pos();
         placeThinHallways(manager, destinationWorld, previousHallwayPos, 2, structureSet.getLongThinHallway(random));
      }
   }

   private static void placeHallwayRightSideRoom(
      StructureTemplateManager manager, @NotNull ServerLevel destinationWorld, @NotNull MutableBlockPos previousHallwayPos
   ) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      RandomSource random = destinationWorld.getRandom();
      BlockPos originalPos = new BlockPos(previousHallwayPos.pos);
      DungeonStructureInfo info = structureSet.getRightSideRoom(random);
      Direction dir = Direction.byName(previousHallwayPos.direction);
      if (dir != null && !dir.getAxis().isVertical()) {
         StructurePlaceSettings hallway = new StructurePlaceSettings().setRotation(info.getRotationTo(dir)).setIgnoreEntities(true);
         BlockPos hallwayPos = info.calculatePlacementPos(originalPos, dir);
         placeStructure(manager, destinationWorld, hallwayPos, info.getAddress(), hallway);
         BlockPos junction = originalPos.relative(dir, info.getSize().getX() / 2);
         DungeonStructureInfo rightDeadEnd = structureSet.getDeadEndRoom(random, false);
         BlockPos rightPos = junction.relative(dir.getClockWise(), info.getSize().getZ() / 2 + 1);
         BlockPos rightWithOffset = rightDeadEnd.calculatePlacementPos(rightPos, dir.getClockWise());
         StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(rightDeadEnd.getRotationTo(dir.getClockWise())).setIgnoreEntities(true);
         placeStructure(manager, destinationWorld, rightWithOffset, rightDeadEnd.getAddress(), settings);
         BlockPos entityPos = rightDeadEnd.calculateEntityPos(rightPos, dir.getClockWise());
         addFloorEntity(destinationWorld, entityPos, previousHallwayPos.floorcount, true, false);
         BlockPos doorPos = rightPos.above(2).relative(dir.getCounterClockWise());
         placeDungeonUnlockerBlockWithRotation(destinationWorld, doorPos, info.getRotationTo(dir), true, 0, calculateFloorForUnlocker(previousHallwayPos));
         DungeonStructureInfo.DungeonCursor cursor = info.calculateNewCursors(originalPos, dir).get(0);
         previousHallwayPos.pos = cursor.pos();
         placeThinHallways(manager, destinationWorld, previousHallwayPos, 2, structureSet.getLongThinHallway(random));
      }
   }

   private static void placeCornerHallway(
      @NotNull MutableBlockPos previousHallwayPos,
      StructureTemplateManager manager,
      @NotNull ServerLevel destinationWorld,
      @NotNull DungeonStructureInfo thin_hallway
   ) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      RandomSource random = destinationWorld.getRandom();
      DungeonStructureInfo info = structureSet.getCornerHallway(random);
      String CORNERHALLWAY = thin_hallway.getAddress().equals("dungeon/hallway/trap/long_thin_hallway_vanta")
         ? "dungeon/hallway/trap/thin_hallway_corner_vanta"
         : info.getAddress();
      Direction dir = Direction.byName(previousHallwayPos.direction);
      if (dir != null && !dir.getAxis().isVertical()) {
         BlockPos originalPos = new BlockPos(previousHallwayPos.pos);
         BlockPos placementPos = info.calculatePlacementPos(originalPos, dir);
         placeStructure(manager, destinationWorld, placementPos, CORNERHALLWAY, new StructurePlaceSettings().setRotation(info.getRotationTo(dir)).setIgnoreEntities(true));
         DungeonStructureInfo.DungeonCursor cursor = info.calculateNewCursors(originalPos, dir).get(0);
         previousHallwayPos.pos = cursor.pos();
         previousHallwayPos.direction = cursor.facing().getName();
         placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, thin_hallway);
      }
   }

   private static void placeOnewayRoomWithoutDoors(
      StructureTemplateManager manager, @NotNull ServerLevel destinationWorld, @NotNull MutableBlockPos previousHallwayPos
   ) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      RandomSource random = destinationWorld.getRandom();
      DungeonStructureInfo info = structureSet.getOneWayRoom(random);
      Direction dir = Direction.byName(previousHallwayPos.direction);
      if (dir != null && !dir.getAxis().isVertical()) {
         BlockPos newHallwayPos = info.calculatePlacementPos(previousHallwayPos.pos, dir);
         placeStructure(
            manager, destinationWorld, newHallwayPos, info.getAddress(), new StructurePlaceSettings().setIgnoreEntities(true).setRotation(info.getRotationTo(dir))
         );
         DungeonStructureInfo.DungeonCursor cursor = info.calculateNewCursors(previousHallwayPos.pos, dir).get(0);
         previousHallwayPos.pos = cursor.pos();
         placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, structureSet.getThinHallway(random));
      }
   }

   private static void placeOnewayRoom(StructureTemplateManager manager, @NotNull ServerLevel destinationWorld, @NotNull MutableBlockPos previousHallwayPos) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      RandomSource random = destinationWorld.getRandom();
      DungeonStructureInfo info = structureSet.getOneWayRoom(random);
      MutableBlockPos.room_count++;
      BlockPos originalPos = new BlockPos(previousHallwayPos.pos);
      Direction dir = Direction.byName(previousHallwayPos.direction);
      if (dir != null && !dir.getAxis().isVertical()) {
         BlockPos newHallwayPos = info.calculatePlacementPos(originalPos, dir);
         placeStructure(
            manager, destinationWorld, newHallwayPos, info.getAddress(), new StructurePlaceSettings().setRotation(info.getRotationTo(dir)).setIgnoreEntities(true)
         );
         previousHallwayPos.pos = info.calculateNewCursors(originalPos, dir).get(0).pos();
         placeDungeonUnlockerBlockWithRotation(
            destinationWorld,
            originalPos.above(2),
            info.getRotationTo(dir).getRotated(Rotation.CLOCKWISE_90),
            true,
            0,
            calculateFloorForUnlocker(previousHallwayPos)
         );
         BlockPos entityPos = info.calculateEntityPos(originalPos, dir);
         addFloorEntity(destinationWorld, entityPos, previousHallwayPos.floorcount, false, false);
         placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, structureSet.getThinHallway(random));
      }
   }

   private static void place2wayHallway(
      @NotNull MutableBlockPos previousHallwayPos,
      StructureTemplateManager manager,
      @NotNull ServerLevel destinationWorld,
      String success_direction_left_or_right,
      String not_success_loopback_or_deadend_or_stairs,
      int hallway_length
   ) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      RandomSource random = destinationWorld.getRandom();
      int left_success = 0;
      int not_success_case = 1;
      if (Objects.equals(not_success_loopback_or_deadend_or_stairs, "loopback")) {
         not_success_case = getRandomInRange(random, 1, 3);
      } else if (Objects.equals(not_success_loopback_or_deadend_or_stairs, "deadend")) {
         not_success_case = getRandomInRange(random, 4, 5);
      } else if (Objects.equals(not_success_loopback_or_deadend_or_stairs, "stairs")) {
         not_success_case = 6;
      }

      if (Objects.equals(success_direction_left_or_right, "left")) {
         left_success = 1;
      }

      Direction dir = Direction.byName(previousHallwayPos.direction);
      if (dir != null && !dir.getAxis().isVertical()) {
         BlockPos originPos = new BlockPos(previousHallwayPos.pos);
         DungeonStructureInfo junction = structureSet.getJunction(random);
         BlockPos placementPos = junction.calculatePlacementPos(originPos, dir);
         placeStructure(
            manager, destinationWorld, placementPos, junction.getAddress(), new StructurePlaceSettings().setRotation(junction.getRotationTo(dir)).setIgnoreEntities(true)
         );
         List<DungeonStructureInfo.DungeonCursor> cursorList = junction.calculateNewCursors(originPos, dir);
         if (left_success == 0) {
            DungeonStructureInfo.DungeonCursor cursor = cursorList.get(0);
            previousHallwayPos.direction = cursor.facing().getName();
            previousHallwayPos.pos = cursor.pos();
            DungeonStructureInfo hallway = structureSet.getLongThinHallway(random);
            placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, hallway);
            if (not_success_case == 1 || not_success_case == 2 || not_success_case == 3) {
               loopbackLeft(previousHallwayPos, manager, destinationWorld);
            } else if (not_success_case == 4 || not_success_case == 5) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, structureSet.getThinHallway(random));
               int probroomend = getRandomInRange(random, 1, 6);
               if (probroomend != 5 && probroomend != 6) {
                  previousHallwayPos.direction = dir.getCounterClockWise().getName();
                  placeDeadendRoom(manager, destinationWorld, previousHallwayPos, true);
               } else {
                  DungeonStructureInfo end = structureSet.getThinHallwayEnd(random);
                  Direction placeDir = Direction.byName(previousHallwayPos.direction);
                  if (placeDir == null || placeDir.getAxis().isVertical()) {
                     return;
                  }

                  placeStructure(
                     manager,
                     destinationWorld,
                     end.calculatePlacementPos(previousHallwayPos.pos, placeDir),
                     end.getAddress(),
                     new StructurePlaceSettings().setIgnoreEntities(true).setRotation(end.getRotationTo(placeDir))
                  );
               }
            } else if (not_success_case == 6) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, structureSet.getThinHallway(random));
               placeStairs(previousHallwayPos, manager, destinationWorld);
            }
         } else {
            DungeonStructureInfo.DungeonCursor cursor = cursorList.get(1);
            previousHallwayPos.direction = cursor.facing().getName();
            previousHallwayPos.pos = cursor.pos();
            DungeonStructureInfo hallway = structureSet.getLongThinHallway(random);
            placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, hallway);
            if (not_success_case == 1 || not_success_case == 2 || not_success_case == 3) {
               loopbackRight(previousHallwayPos, manager, destinationWorld);
            } else if (not_success_case == 4 || not_success_case == 5) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, structureSet.getThinHallway(random));
               int probroomend = getRandomInRange(random, 1, 6);
               if (probroomend != 5 && probroomend != 6) {
                  placeDeadendRoom(manager, destinationWorld, previousHallwayPos, true);
               } else {
                  placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, structureSet.getThinHallwayEnd(random));
               }
            } else if (not_success_case == 6) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, hallway_length, structureSet.getThinHallway(random));
               placeStairs(previousHallwayPos, manager, destinationWorld);
            }
         }

         DungeonStructureInfo.DungeonCursor cursorx;
         if (left_success == 0) {
            cursorx = cursorList.get(1);
         } else {
            cursorx = cursorList.get(0);
         }

         previousHallwayPos.pos = cursorx.pos();
         previousHallwayPos.direction = cursorx.facing().getName();
         placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, structureSet.getLongThinHallway(random));
      }
   }

   private static void loopbackLeft(@NotNull MutableBlockPos previousHallwayPos, StructureTemplateManager manager, @NotNull ServerLevel destinationWorld) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      RandomSource random = destinationWorld.getRandom();
      int prob = getRandomInRange(random, 1, 6);
      if (prob < 5) {
         placeCornerHallway(previousHallwayPos, manager, destinationWorld, structureSet.getThinHallway(random));
         placeCornerHallway(previousHallwayPos, manager, destinationWorld, structureSet.getThinHallway(random));
         placeCornerHallway(previousHallwayPos, manager, destinationWorld, structureSet.getThinHallwayEnd(random));
      } else {
         int probroomend = getRandomInRange(random, 1, 6);
         if (probroomend != 5 && probroomend != 6) {
            placeCornerHallway(previousHallwayPos, manager, destinationWorld, structureSet.getThinHallway(random));
            placeDeadendRoom(manager, destinationWorld, previousHallwayPos, false);
         } else {
            placeCornerHallway(previousHallwayPos, manager, destinationWorld, structureSet.getThinHallwayEnd(random));
         }
      }
   }

   private static void loopbackRight(@NotNull MutableBlockPos previousHallwayPos, StructureTemplateManager manager, @NotNull ServerLevel destinationWorld) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      RandomSource random = destinationWorld.getRandom();
      Direction dir = Direction.byName(previousHallwayPos.direction);
      if (dir != null && !dir.getAxis().isVertical()) {
         int prob = getRandomInRange(random, 1, 6);
         if (prob < 5) {
            int prob2 = getRandomInRange(random, 1, 2);
            if (prob2 == 2) {
               placeThinHallways(manager, destinationWorld, previousHallwayPos, 1, structureSet.getThinHallway(random));
               placeCornerHallway(previousHallwayPos, manager, destinationWorld, structureSet.getThinHallway(random));
            }

            placeCornerHallway(previousHallwayPos, manager, destinationWorld, structureSet.getThinHallway(random));
            placeCornerHallway(previousHallwayPos, manager, destinationWorld, structureSet.getThinHallwayEnd(random));
         } else {
            int probroomend = getRandomInRange(random, 1, 6);
            if (probroomend != 5 && probroomend != 6) {
               placeCornerHallway(previousHallwayPos, manager, destinationWorld, structureSet.getThinHallway(random));
               placeDeadendRoom(manager, destinationWorld, previousHallwayPos, false);
            } else {
               placeCornerHallway(previousHallwayPos, manager, destinationWorld, structureSet.getThinHallwayEnd(random));
            }
         }
      }
   }

   private static void placeLongCornerHallway(
      @NotNull MutableBlockPos previousHallwayPos, StructureTemplateManager manager, @NotNull ServerLevel destinationWorld, int length
   ) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      RandomSource random = destinationWorld.getRandom();
      DungeonStructureInfo corner = structureSet.getCornerHallway(random);
      Direction dir = Direction.byName(previousHallwayPos.direction);
      if (dir != null && !dir.getAxis().isVertical()) {
         BlockPos originalPos = new BlockPos(previousHallwayPos.pos);
         BlockPos placementPos = corner.calculatePlacementPos(originalPos, dir);
         placeStructure(
            manager, destinationWorld, placementPos, corner.getAddress(), new StructurePlaceSettings().setRotation(corner.getRotationTo(dir)).setIgnoreEntities(true)
         );
         DungeonStructureInfo.DungeonCursor cursor = corner.calculateNewCursors(originalPos, dir).get(0);
         previousHallwayPos.pos = cursor.pos();
         previousHallwayPos.direction = cursor.facing().getName();
         if (length < 1) {
            length = 1;
         } else if (length > 2) {
            length = 2;
         }

         DungeonStructureInfo info = structureSet.getThinHallway(random);
         placeThinHallways(manager, destinationWorld, previousHallwayPos, length, info);
      }
   }

   private static void placeDesertBossRoom(StructureTemplateManager manager, ServerLevel destinationWorld, @NotNull MutableBlockPos previousHallwayPos) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      Direction dir = Direction.byName(previousHallwayPos.direction);
      if (dir != null && !dir.getAxis().isVertical()) {
         BlockPos basePos = new BlockPos(previousHallwayPos.pos);
         List<BossRoomPartStructureInfo> bossRoom = structureSet.getBossRoom();

         for (BossRoomPartStructureInfo bossRoomPart : bossRoom) {
            StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(bossRoomPart.getRotationTo(dir)).setIgnoreEntities(true);
            BlockPos placingPos = bossRoomPart.calculatePlacementPos(basePos, dir);
            placeStructure(manager, destinationWorld, placingPos, bossRoomPart.getAddress(), settings);
         }

         BossRoomPartStructureInfo mainPartInfo = bossRoom.get(0);
         mainPartInfo.getBossSpawner().spawnBoss(destinationWorld, basePos.relative(dir, 10).above(2), true, previousHallwayPos.floorcount, true);
         previousHallwayPos.pos = mainPartInfo.calculateExitPosition(basePos, dir);
      }
   }

   private static void placeSwampBossRoom(StructureTemplateManager manager, ServerLevel destinationWorld, @NotNull MutableBlockPos previousHallwayPos) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      Direction dir = Direction.byName(previousHallwayPos.direction);
      if (dir != null && !dir.getAxis().isVertical()) {
         BlockPos basePos = new BlockPos(previousHallwayPos.pos);
         List<BossRoomPartStructureInfo> bossRoom = structureSet.getBossRoom();

         for (BossRoomPartStructureInfo bossRoomPart : bossRoom) {
            StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(bossRoomPart.getRotationTo(dir)).setIgnoreEntities(true);
            BlockPos placingPos = bossRoomPart.calculatePlacementPos(basePos, dir);
            placeStructure(manager, destinationWorld, placingPos, bossRoomPart.getAddress(), settings);
         }

         BossRoomPartStructureInfo mainPartInfo = bossRoom.get(0);
         mainPartInfo.getBossSpawner().spawnBoss(destinationWorld, basePos.relative(dir, 10).above(2), true, previousHallwayPos.floorcount, true);
         previousHallwayPos.pos = mainPartInfo.calculateExitPosition(basePos, dir);
      }
   }

   private static void placePlainBossRoom(StructureTemplateManager manager, ServerLevel destinationWorld, @NotNull MutableBlockPos previousHallwayPos) {
      DungeonStructureSet structureSet = Database.getByFloor(previousHallwayPos.floorcount);
      Direction dir = Direction.byName(previousHallwayPos.direction);
      if (dir != null && !dir.getAxis().isVertical()) {
         BlockPos basePos = new BlockPos(previousHallwayPos.pos);
         List<BossRoomPartStructureInfo> bossRoom = structureSet.getBossRoom();

         for (BossRoomPartStructureInfo bossRoomPart : bossRoom) {
            StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(bossRoomPart.getRotationTo(dir)).setIgnoreEntities(true);
            BlockPos placingPos = bossRoomPart.calculatePlacementPos(basePos, dir);
            placeStructure(manager, destinationWorld, placingPos, bossRoomPart.getAddress(), settings);
         }

         BossRoomPartStructureInfo mainPartInfo = bossRoom.get(0);
         mainPartInfo.getBossSpawner().spawnBoss(destinationWorld, basePos.relative(dir, 10).above(2), true, previousHallwayPos.floorcount, true);
         previousHallwayPos.pos = mainPartInfo.calculateExitPosition(basePos, dir);
      }
   }

   private static void addFloorEntity(ServerLevel world, BlockPos pos, int floorCount, boolean treasureRoom, boolean hallwayEntity) {
      if (!STOPSPAWNS) {
         if (entitySpawnFrequency > 0) {
            entitySpawnFrequency--;
         } else {
            entitySpawnFrequency = SPAWN_FREQUENCY;
            DungeonEntitiesSpecfications newEntity = new DungeonEntitiesSpecfications(world, pos, floorCount, treasureRoom, hallwayEntity);
            if (entitySpecifications == null) {
               entitySpecifications = new ArrayList<>();
            }

            entitySpecifications.add(newEntity);
         }
      }
   }

   private static void spawnLivingArmorAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      LivingArmorEntity livingArmor = (LivingArmorEntity)((EntityType)BeyondEntities.LIVING_ARMOR.get()).create(world);
      if (livingArmor != null) {
         if (hallwayEntity) {
            livingArmor.getPersistentData().putBoolean("hallwayEntity", true);
         }

         livingArmor.getPersistentData().putInt("floorCount", floorCount);
         livingArmor.getPersistentData().putBoolean("respawn", respawn);
         livingArmor.getPersistentData().putInt("initialPosX", pos.getX());
         livingArmor.getPersistentData().putInt("initialPosY", pos.getY());
         livingArmor.getPersistentData().putInt("initialPosZ", pos.getZ());
         livingArmor.setPersistenceRequired();
         livingArmor.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(livingArmor);
      }
   }

   private static void spawnScarletSlimeAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      ScarletSlimeEntity scarletSlime = (ScarletSlimeEntity)((EntityType)BeyondEntities.SCARLET_SLIME.get()).create(world);
      if (scarletSlime != null) {
         if (hallwayEntity) {
            scarletSlime.getPersistentData().putBoolean("hallwayEntity", true);
         }

         scarletSlime.getPersistentData().putInt("floorCount", floorCount);
         scarletSlime.getPersistentData().putBoolean("respawn", respawn);
         scarletSlime.getPersistentData().putInt("initialPosX", pos.getX());
         scarletSlime.getPersistentData().putInt("initialPosY", pos.getY());
         scarletSlime.getPersistentData().putInt("initialPosZ", pos.getZ());
         scarletSlime.setPersistenceRequired();
         scarletSlime.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(scarletSlime);
      }
   }

   private static void spawnMageAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      MageEntity mage = (MageEntity)((EntityType)BeyondEntities.MAGE.get()).create(world);
      if (mage != null) {
         if (hallwayEntity) {
            mage.getPersistentData().putBoolean("hallwayEntity", true);
         }

         mage.getPersistentData().putInt("floorCount", floorCount);
         mage.getPersistentData().putBoolean("respawn", respawn);
         mage.getPersistentData().putInt("initialPosX", pos.getX());
         mage.getPersistentData().putInt("initialPosY", pos.getY());
         mage.getPersistentData().putInt("initialPosZ", pos.getZ());
         mage.setPersistenceRequired();
         mage.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(mage);
      }
   }

   private static void spawnSkullNightAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      SkullNightEntity skullNight = (SkullNightEntity)((EntityType)BeyondEntities.SKULL_NIGHT.get()).create(world);
      if (skullNight != null) {
         if (hallwayEntity) {
            skullNight.getPersistentData().putBoolean("hallwayEntity", true);
         }

         skullNight.getPersistentData().putInt("floorCount", floorCount);
         skullNight.getPersistentData().putBoolean("respawn", respawn);
         skullNight.getPersistentData().putInt("initialPosX", pos.getX());
         skullNight.getPersistentData().putInt("initialPosY", pos.getY());
         skullNight.getPersistentData().putInt("initialPosZ", pos.getZ());
         skullNight.setPersistenceRequired();
         skullNight.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(skullNight);
      }
   }

   private static void spawnMarkLaurenAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean repsawn) {
      MarkLaurenEntity markLauren = (MarkLaurenEntity)((EntityType)TensuraEntityTypes.MARK_LAUREN.get()).create(world);
      if (markLauren != null) {
         if (hallwayEntity) {
            markLauren.getPersistentData().putBoolean("hallwayEntity", true);
         }

         markLauren.getPersistentData().putInt("floorCount", floorCount);
         markLauren.getPersistentData().putBoolean("respawn", repsawn);
         markLauren.getPersistentData().putInt("initialPosX", pos.getX());
         markLauren.getPersistentData().putInt("initialPosY", pos.getY());
         markLauren.getPersistentData().putInt("initialPosZ", pos.getZ());
         markLauren.setPersistenceRequired();
         markLauren.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(markLauren);
      }
   }

   private static void spawnShinjiTanimuraAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      ShinjiTanimuraEntity shinjiTanimura = (ShinjiTanimuraEntity)((EntityType)TensuraEntityTypes.SHINJI_TANIMURA.get()).create(world);
      if (shinjiTanimura != null) {
         if (hallwayEntity) {
            shinjiTanimura.getPersistentData().putBoolean("hallwayEntity", true);
         }

         shinjiTanimura.getPersistentData().putInt("floorCount", floorCount);
         shinjiTanimura.getPersistentData().putBoolean("respawn", respawn);
         shinjiTanimura.getPersistentData().putInt("initialPosX", pos.getX());
         shinjiTanimura.getPersistentData().putInt("initialPosY", pos.getY());
         shinjiTanimura.getPersistentData().putInt("initialPosZ", pos.getZ());
         shinjiTanimura.setPersistenceRequired();
         shinjiTanimura.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(shinjiTanimura);
      }
   }

   private static void spawnShinRyuseiAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      ShinRyuseiEntity shinRyusei = (ShinRyuseiEntity)((EntityType)TensuraEntityTypes.SHIN_RYUSEI.get()).create(world);
      if (shinRyusei != null) {
         if (hallwayEntity) {
            shinRyusei.getPersistentData().putBoolean("hallwayEntity", true);
         }

         shinRyusei.getPersistentData().putInt("floorCount", floorCount);
         shinRyusei.getPersistentData().putBoolean("respawn", respawn);
         shinRyusei.getPersistentData().putInt("initialPosX", pos.getX());
         shinRyusei.getPersistentData().putInt("initialPosY", pos.getY());
         shinRyusei.getPersistentData().putInt("initialPosZ", pos.getZ());
         shinRyusei.setPersistenceRequired();
         shinRyusei.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(shinRyusei);
      }
   }

   public static void spawnBulldeerAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      BulldeerEntity bulldeerEntity = (BulldeerEntity)((EntityType)TensuraEntityTypes.BULLDEER.get()).create(world);
      if (bulldeerEntity != null) {
         if (hallwayEntity) {
            bulldeerEntity.getPersistentData().putBoolean("hallwayEntity", true);
         }

         bulldeerEntity.getPersistentData().putInt("floorCount", floorCount);
         bulldeerEntity.getPersistentData().putBoolean("respawn", respawn);
         bulldeerEntity.getPersistentData().putInt("initialPosX", pos.getX());
         bulldeerEntity.getPersistentData().putInt("initialPosY", pos.getY());
         bulldeerEntity.getPersistentData().putInt("initialPosZ", pos.getZ());
         bulldeerEntity.setPersistenceRequired();
         bulldeerEntity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(bulldeerEntity);
      }
   }

   public static void spawnOneEyedOwlAt(@NotNull ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      int getrandomowls = getRandomInRange(world.getRandom(), 2, 5);

      for (int i = 0; i < getrandomowls; i++) {
         OneEyedOwlEntity oneEyedOwlEntity = (OneEyedOwlEntity)((EntityType)TensuraEntityTypes.ONE_EYED_OWL.get()).create(world);
         if (oneEyedOwlEntity != null) {
            if (hallwayEntity) {
               oneEyedOwlEntity.getPersistentData().putBoolean("hallwayEntity", true);
            }

            oneEyedOwlEntity.getPersistentData().putInt("floorCount", floorCount);
            oneEyedOwlEntity.getPersistentData().putBoolean("respawn", respawn);
            oneEyedOwlEntity.getPersistentData().putInt("initialPosX", pos.getX());
            oneEyedOwlEntity.getPersistentData().putInt("initialPosY", pos.getY());
            oneEyedOwlEntity.getPersistentData().putInt("initialPosZ", pos.getZ());
            oneEyedOwlEntity.setPersistenceRequired();
            oneEyedOwlEntity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
            world.addFreshEntity(oneEyedOwlEntity);
         }

         respawn = false;
      }
   }

   public static void spawnDragonPeacockAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      DragonPeacockEntity dragonPeacockEntity = (DragonPeacockEntity)((EntityType)TensuraEntityTypes.DRAGON_PEACOCK.get()).create(world);
      if (dragonPeacockEntity != null) {
         if (hallwayEntity) {
            dragonPeacockEntity.getPersistentData().putBoolean("hallwayEntity", true);
         }

         dragonPeacockEntity.getPersistentData().putInt("floorCount", floorCount);
         dragonPeacockEntity.getPersistentData().putBoolean("respawn", respawn);
         dragonPeacockEntity.getPersistentData().putInt("initialPosX", pos.getX());
         dragonPeacockEntity.getPersistentData().putInt("initialPosY", pos.getY());
         dragonPeacockEntity.getPersistentData().putInt("initialPosZ", pos.getZ());
         dragonPeacockEntity.setPersistenceRequired();
         dragonPeacockEntity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(dragonPeacockEntity);
      }
   }

   public static void spawnMultipleSmallSlimes(@NotNull ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      int count = getRandomInRange(world.getRandom(), 5, 7);

      for (int i = 0; i < count; i++) {
         double offsetX = (world.random.nextDouble() - 0.5) * 0.5;
         double offsetZ = (world.random.nextDouble() - 0.5) * 0.5;
         if (i > 0) {
            respawn = false;
         }

         spawnSmallSlimeAt(world, pos, offsetX, offsetZ, hallwayEntity, floorCount, respawn);
      }
   }

   public static void spawnSmallSlimeAt(ServerLevel world, BlockPos pos, double offsetX, double offsetZ, boolean hallwayEntity, int floorCount, boolean respawn) {
      SlimeEntity slime = (SlimeEntity)((EntityType)TensuraEntityTypes.SLIME.get()).create(world);
      if (slime != null) {
         int random_size = getRandomInRange(world.getRandom(), 1, 4);
         slime.setSize(random_size, true);
         if (hallwayEntity) {
            slime.getPersistentData().putBoolean("hallwayEntity", true);
         }

         slime.getPersistentData().putInt("floorCount", floorCount);
         slime.getPersistentData().putBoolean("respawn", respawn);
         slime.getPersistentData().putInt("initialPosX", pos.getX());
         slime.getPersistentData().putInt("initialPosY", pos.getY());
         slime.getPersistentData().putInt("initialPosZ", pos.getZ());
         slime.setPersistenceRequired();
         slime.setPos((double)pos.getX() + 0.5 + offsetX, (double)pos.getY(), (double)pos.getZ() + 0.5 + offsetZ);
         world.addFreshEntity(slime);
      }
   }

   public static void spawnHornedRabbitsAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      HornedRabbitEntity entity = (HornedRabbitEntity)((EntityType)TensuraEntityTypes.HORNED_RABBIT.get()).create(world);
      if (entity != null) {
         if (hallwayEntity) {
            entity.getPersistentData().putBoolean("hallwayEntity", true);
         }

         entity.getPersistentData().putInt("floorCount", floorCount);
         entity.getPersistentData().putBoolean("respawn", respawn);
         entity.getPersistentData().putInt("initialPosX", pos.getX());
         entity.getPersistentData().putInt("initialPosY", pos.getY());
         entity.getPersistentData().putInt("initialPosZ", pos.getZ());
         entity.setPersistenceRequired();
         entity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(entity);
      }
   }

   public static void spawnBarghastAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      BarghestEntity barghestEntity = (BarghestEntity)((EntityType)TensuraEntityTypes.BARGHEST.get()).create(world);
      if (barghestEntity != null) {
         if (hallwayEntity) {
            barghestEntity.getPersistentData().putBoolean("hallwayEntity", true);
         }

         barghestEntity.getPersistentData().putInt("floorCount", floorCount);
         barghestEntity.getPersistentData().putBoolean("respawn", respawn);
         barghestEntity.getPersistentData().putInt("initialPosX", pos.getX());
         barghestEntity.getPersistentData().putInt("initialPosY", pos.getY());
         barghestEntity.getPersistentData().putInt("initialPosZ", pos.getZ());
         barghestEntity.setPersistenceRequired();
         barghestEntity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(barghestEntity);
      }
   }

   public static void spawnHoundDogAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      HoundDogEntity entity = (HoundDogEntity)((EntityType)TensuraEntityTypes.HOUND_DOG.get()).create(world);
      if (entity != null) {
         if (hallwayEntity) {
            entity.getPersistentData().putBoolean("hallwayEntity", true);
         }

         entity.getPersistentData().putInt("floorCount", floorCount);
         entity.getPersistentData().putBoolean("respawn", respawn);
         entity.getPersistentData().putInt("initialPosX", pos.getX());
         entity.getPersistentData().putInt("initialPosY", pos.getY());
         entity.getPersistentData().putInt("initialPosZ", pos.getZ());
         entity.setPersistenceRequired();
         entity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(entity);
      }
   }

   public static void spawnHoverLizardAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      HoverLizardEntity entity = (HoverLizardEntity)((EntityType)TensuraEntityTypes.HOVER_LIZARD.get()).create(world);
      if (entity != null) {
         if (hallwayEntity) {
            entity.getPersistentData().putBoolean("hallwayEntity", true);
         }

         entity.getPersistentData().putInt("floorCount", floorCount);
         entity.getPersistentData().putBoolean("respawn", respawn);
         entity.getPersistentData().putInt("initialPosX", pos.getX());
         entity.getPersistentData().putInt("initialPosY", pos.getY());
         entity.getPersistentData().putInt("initialPosZ", pos.getZ());
         entity.setPersistenceRequired();
         entity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(entity);
      }
   }

   public static void spawnLeechLizardAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      LeechLizardEntity entity = (LeechLizardEntity)((EntityType)TensuraEntityTypes.LEECH_LIZARD.get()).create(world);
      if (entity != null) {
         if (hallwayEntity) {
            entity.getPersistentData().putBoolean("hallwayEntity", true);
         }

         entity.getPersistentData().putInt("floorCount", floorCount);
         entity.getPersistentData().putBoolean("respawn", respawn);
         entity.getPersistentData().putInt("initialPosX", pos.getX());
         entity.getPersistentData().putInt("initialPosY", pos.getY());
         entity.getPersistentData().putInt("initialPosZ", pos.getZ());
         entity.setPersistenceRequired();
         entity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(entity);
      }
   }

   public static void spawnDireworlfAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      DirewolfEntity entity = (DirewolfEntity)((EntityType)TensuraEntityTypes.DIREWOLF.get()).create(world);
      if (entity != null) {
         if (hallwayEntity) {
            entity.getPersistentData().putBoolean("hallwayEntity", true);
         }

         entity.getPersistentData().putInt("floorCount", floorCount);
         entity.getPersistentData().putBoolean("respawn", respawn);
         entity.getPersistentData().putInt("initialPosX", pos.getX());
         entity.getPersistentData().putInt("initialPosY", pos.getY());
         entity.getPersistentData().putInt("initialPosZ", pos.getZ());
         entity.setPersistenceRequired();
         entity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(entity);
      }
   }

   public static void spawnGiantBatAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      GiantBatEntity entity = (GiantBatEntity)((EntityType)TensuraEntityTypes.GIANT_BAT.get()).create(world);
      if (entity != null) {
         if (hallwayEntity) {
            entity.getPersistentData().putBoolean("hallwayEntity", true);
         }

         entity.getPersistentData().putInt("floorCount", floorCount);
         entity.getPersistentData().putBoolean("respawn", respawn);
         entity.getPersistentData().putInt("initialPosX", pos.getX());
         entity.getPersistentData().putInt("initialPosY", pos.getY());
         entity.getPersistentData().putInt("initialPosZ", pos.getZ());
         entity.setPersistenceRequired();
         entity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(entity);
      }
   }

   public static void spawnGiantAntAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      GiantAntEntity entity = (GiantAntEntity)((EntityType)TensuraEntityTypes.GIANT_ANT.get()).create(world);
      if (entity != null) {
         if (hallwayEntity) {
            entity.getPersistentData().putBoolean("hallwayEntity", true);
         }

         entity.getPersistentData().putInt("floorCount", floorCount);
         entity.getPersistentData().putBoolean("respawn", respawn);
         entity.getPersistentData().putInt("initialPosX", pos.getX());
         entity.getPersistentData().putInt("initialPosY", pos.getY());
         entity.getPersistentData().putInt("initialPosZ", pos.getZ());
         entity.setPersistenceRequired();
         entity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(entity);
      }
   }

   public static void spawnZombieAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      Zombie zombie = (Zombie)EntityType.ZOMBIE.create(world);
      if (zombie != null) {
         if (hallwayEntity) {
            zombie.getPersistentData().putBoolean("hallwayEntity", true);
         }

         zombie.getPersistentData().putInt("floorCount", floorCount);
         zombie.getPersistentData().putBoolean("respawn", respawn);
         zombie.getPersistentData().putInt("initialPosX", pos.getX());
         zombie.getPersistentData().putInt("initialPosY", pos.getY());
         zombie.getPersistentData().putInt("initialPosZ", pos.getZ());
         zombie.setPersistenceRequired();
         zombie.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(zombie);
      }
   }

   public static void spawnBlackSpiderAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      BossBlackSpiderEntity entity = (BossBlackSpiderEntity)((EntityType)BeyondEntities.BOSS_BLACK_SPIDER.get()).create(world);
      if (entity != null) {
         if (hallwayEntity) {
            entity.getPersistentData().putBoolean("hallwayEntity", true);
         }

         entity.getPersistentData().putInt("floorCount", floorCount);
         entity.getPersistentData().putBoolean("respawn", respawn);
         entity.getPersistentData().putBoolean("isBoss", true);
         entity.getPersistentData().putInt("initialPosX", pos.getX());
         entity.getPersistentData().putInt("initialPosY", pos.getY());
         entity.getPersistentData().putInt("initialPosZ", pos.getZ());
         entity.setPersistenceRequired();
         entity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(entity);
      }
   }

   public static void spawnEvilCentipede(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      EvilCentipedeBossEntity entity = (EvilCentipedeBossEntity)((EntityType)BeyondEntities.EVIL_CENTIPEDE_BOSS.get()).create(world);
      if (entity != null) {
         if (hallwayEntity) {
            entity.getPersistentData().putBoolean("hallwayEntity", true);
         }

         entity.getPersistentData().putInt("floorCount", floorCount);
         entity.getPersistentData().putBoolean("respawn", respawn);
         entity.getPersistentData().putBoolean("isBoss", true);
         entity.getPersistentData().putInt("initialPosX", pos.getX());
         entity.getPersistentData().putInt("initialPosY", pos.getY());
         entity.getPersistentData().putInt("initialPosZ", pos.getZ());
         entity.setPersistenceRequired();
         setBossElite(entity);
         entity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(entity);
      }
   }

   public static void spawnOgreLord(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      OgreLordEntity entity = (OgreLordEntity)((EntityType)BeyondEntities.OGRE_LORD.get()).create(world);
      if (entity != null) {
         if (hallwayEntity) {
            entity.getPersistentData().putBoolean("hallwayEntity", true);
         }

         entity.getPersistentData().putInt("floorCount", floorCount);
         entity.getPersistentData().putBoolean("respawn", respawn);
         entity.getPersistentData().putBoolean("isBoss", true);
         entity.getPersistentData().putInt("initialPosX", pos.getX());
         entity.getPersistentData().putInt("initialPosY", pos.getY());
         entity.getPersistentData().putInt("initialPosZ", pos.getZ());
         entity.setPersistenceRequired();
         setBossElite(entity);
         entity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(entity);

         for (int i = 0; i < 5; i++) {
            double angle = (Math.PI * 2) * (double)i / 5.0;
            double radius = 2.5;
            double offsetX = Math.cos(angle) * radius;
            double offsetZ = Math.sin(angle) * radius;
            OgreSubEntity sub = (OgreSubEntity)((EntityType)BeyondEntities.OGRE_SUB.get()).create(world);
            if (sub != null) {
               BlockPos subPos = pos.offset((int)offsetX, 0, (int)offsetZ);
               sub.setPos((double)subPos.getX() + 0.5, (double)subPos.getY(), (double)subPos.getZ() + 0.5);
               sub.setPersistenceRequired();
               world.addFreshEntity(sub);
            }
         }
      }
   }

   public static void spawnGozul(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      GozulEntity entity = (GozulEntity)((EntityType)BeyondEntities.GOZUL.get()).create(world);
      if (entity != null) {
         if (hallwayEntity) {
            entity.getPersistentData().putBoolean("hallwayEntity", true);
         }

         entity.getPersistentData().putInt("floorCount", floorCount);
         entity.getPersistentData().putBoolean("respawn", respawn);
         entity.getPersistentData().putBoolean("isBoss", true);
         entity.getPersistentData().putInt("initialPosX", pos.getX());
         entity.getPersistentData().putInt("initialPosY", pos.getY());
         entity.getPersistentData().putInt("initialPosZ", pos.getZ());
         entity.setPersistenceRequired();
         setBossElite(entity);
         entity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(entity);
      }
   }

   public static void spawnPlainBoss(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      if (world.getRandom().nextBoolean()) {
         spawnMezul(world, pos, hallwayEntity, floorCount, respawn);
      } else {
         spawnGozul(world, pos, hallwayEntity, floorCount, respawn);
      }
   }

   public static void spawnMezul(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      MezulEntity entity = (MezulEntity)((EntityType)BeyondEntities.MEZUL.get()).create(world);
      if (entity != null) {
         if (hallwayEntity) {
            entity.getPersistentData().putBoolean("hallwayEntity", true);
         }

         entity.getPersistentData().putInt("floorCount", floorCount);
         entity.getPersistentData().putBoolean("respawn", respawn);
         entity.getPersistentData().putBoolean("isBoss", true);
         entity.getPersistentData().putInt("initialPosX", pos.getX());
         entity.getPersistentData().putInt("initialPosY", pos.getY());
         entity.getPersistentData().putInt("initialPosZ", pos.getZ());
         entity.setPersistenceRequired();
         setBossElite(entity);
         entity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(entity);
      }
   }

   public static void spawnTempestSerpant(ServerLevel world, BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn) {
      TempestSerpentBossEntity entity = (TempestSerpentBossEntity)((EntityType)BeyondEntities.TEMPEST_SERPENT_BOSS.get()).create(world);
      if (entity != null) {
         if (hallwayEntity) {
            entity.getPersistentData().putBoolean("hallwayEntity", true);
         }

         entity.getPersistentData().putInt("floorCount", floorCount);
         entity.getPersistentData().putBoolean("respawn", respawn);
         entity.getPersistentData().putBoolean("isBoss", true);
         entity.getPersistentData().putInt("initialPosX", pos.getX());
         entity.getPersistentData().putInt("initialPosY", pos.getY());
         entity.getPersistentData().putInt("initialPosZ", pos.getZ());
         entity.setPersistenceRequired();
         setBossElite(entity);
         entity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(entity);
      }
   }

   public static void setBossElite(LivingEntity entity) {
      TensuraEPCapability.getFrom(entity).ifPresent(cap -> EliteMobCapability.getFrom(entity).ifPresent(cap1 -> {
            cap1.setElite(true);
            EliteMobCapability.sync(entity);
            applyRandomSkillLogic(entity);
            boolean epSet = false;

            for (EntityEPCount entityEP : TensuraData.getEntityEP()) {
               if (entityEP.getEntity().equals(EntityType.getKey(entity.getType()))) {
                  int minEP = entityEP.getMinEP();
                  int maxEP = Math.max(minEP + 1, entityEP.getMaxEP() + 1);
                  double randomEP = (double)entity.getRandom().nextInt(0, maxEP - minEP);
                  AttributeInstance magicule = entity.getAttribute((Attribute)TensuraAttributeRegistry.MAX_MAGICULE.get());
                  if (magicule != null) {
                     magicule.setBaseValue((double)minEP / 2.0);
                     randomEP += magicule.getValue();
                  }

                  AttributeInstance aura = entity.getAttribute((Attribute)TensuraAttributeRegistry.MAX_AURA.get());
                  if (aura != null) {
                     aura.setBaseValue((double)minEP / 2.0);
                     randomEP += aura.getValue();
                  }

                  cap.setEP(entity, randomEP);
                  epSet = true;

                  for (ResourceLocation skillID : entityEP.getSkills()) {
                     ManasSkill skill = (ManasSkill)SkillAPI.getSkillRegistry().getValue(skillID);
                     if (skill != null) {
                        TensuraSkillInstance instance = new TensuraSkillInstance(skill);
                        SkillAPI.getSkillsFrom(entity).learnSkill(instance);
                        if (instance.canBeToggled(entity)) {
                           instance.setToggled(true);
                        }
                     }
                  }

                  RaceSelectionMenu.grantLearningResistance(entity);
                  break;
               }
            }

            if (!epSet || cap.getEP() <= 0.0) {
               double energy = (double)(100.0F * entity.getMaxHealth());
               AttributeInstance magiculex = entity.getAttribute((Attribute)TensuraAttributeRegistry.MAX_MAGICULE.get());
               if (magiculex != null) {
                  energy += magiculex.getValue();
               }

               AttributeInstance aura = entity.getAttribute((Attribute)TensuraAttributeRegistry.MAX_AURA.get());
               if (aura != null) {
                  energy += aura.getValue();
               }

               cap.setEP(entity, energy);
            }

            if (entity.getType().is(EntityTypes.MONSTER)) {
               cap.setMajin(true);
            }

            applyEPToEntity(entity, cap.getEP());
            TensuraEPCapability.sync(entity);
         }));
   }

   private static void applyEPToEntity(LivingEntity entity, double amount) {
      if (entity != null && entity.isAlive()) {
         TensuraEPCapability.getFrom(entity).ifPresent(cap -> {
            cap.setEP(entity, amount * 10.0, true);
            cap.setCurrentEP(entity, cap.getEP());
            TensuraEPCapability.sync(entity);
         });
      }
   }

   private static void applyRandomSkillLogic(LivingEntity living) {
      TensuraEPCapability.getFrom(living).ifPresent(cap -> {
         assignRandomSkill(living, UNIQUE_SKILL_MAP, UNIQUE_SKILL);
         assignRandomSkill(living, EXTRA_SKILL_MAP, EXTRA_SKILL);
      });
   }

   private static void assignRandomSkill(LivingEntity living, @NotNull Map<String, RegistryObject<? extends Skill>> skillMap, @NotNull String[] skillArray) {
      String randomSkill = skillArray[new Random().nextInt(skillArray.length)];
      RegistryObject<? extends ManasSkill> skillRegistryObject = skillMap.get(randomSkill);
      if (skillRegistryObject != null) {
         ResourceLocation skillId = skillRegistryObject.getId();
         ManasSkill skill = (ManasSkill)SkillAPI.getSkillRegistry().getValue(skillId);
         if (skill != null) {
            TensuraSkillInstance instance = new TensuraSkillInstance(skill);
            SkillAPI.getSkillsFrom(living).learnSkill(instance);
            if (instance.canBeToggled(living)) {
               instance.setToggled(true);
            }

            instance.setMastery(skill.getMaxMastery());
            instance.markDirty();
            SkillAPI.getSkillsFrom(living).syncChanges();
            if (living instanceof LivingEntity) {
               instance.onSkillMastered(living);
            }

            living.sendSystemMessage(Component.translatable("tensura.skill.mastery", new Object[]{skill.getName()}).withStyle(ChatFormatting.GREEN));
         }
      }
   }

   public static boolean checkfloor95door(BlockPos pos, Level level) {
      return level != null && level.getServer() != null ? DungeonSaveData.get(level.getServer().m_129783_()).getFloor95Doors().contains(pos) : false;
   }

   public static int checkLobbyOrLastKnownLocationDoor(BlockPos pos, Level level) {
      if (level != null && level.getServer() != null) {
         BlockPos lastKnownPosDoor = DungeonSaveData.getOverworld(level).getBackToLastKnownLocationDoor();
         BlockPos BackToLobbyDoor = DungeonSaveData.getOverworld(level).getBackToLobbyDoor();
         if (pos.getX() == BackToLobbyDoor.getX() && pos.getY() == BackToLobbyDoor.getY() && pos.getZ() == BackToLobbyDoor.getZ()
            )
          {
            return -1;
         } else {
            return pos.getX() == lastKnownPosDoor.getX()
                  && pos.getY() == lastKnownPosDoor.getY()
                  && pos.getZ() == lastKnownPosDoor.getZ()
               ? 1
               : 0;
         }
      } else {
         return 0;
      }
   }

   public static boolean checkBossRushDoor(BlockPos pos, Level level) {
      if (level != null && level.getServer() != null) {
         BlockPos BossRushTeleporterDoor = DungeonSaveData.getOverworld(level.getServer().m_129783_()).getBossRushTeleporterDoor();
         return BossRushTeleporterDoor == null
            ? false
            : pos.getX() == BossRushTeleporterDoor.getX()
               && pos.getY() == BossRushTeleporterDoor.getY()
               && pos.getZ() == BossRushTeleporterDoor.getZ();
      } else {
         return false;
      }
   }

   public static boolean checkDontLetThisDoorOpen(BlockPos pos) {
      return DontLetThisDoorOpen == null
         ? false
         : pos.getX() == DontLetThisDoorOpen.getX()
            && pos.getY() == DontLetThisDoorOpen.getY()
            && pos.getZ() == DontLetThisDoorOpen.getZ();
   }

   public static int getRandomInRange(@NotNull RandomSource random, int min, int max) {
      return random.nextIntBetweenInclusive(min, max);
   }

   private static void placeStructure(
      @NotNull StructureTemplateManager manager, ServerLevel world, BlockPos pos, String structurePath, StructurePlaceSettings settings
   ) {
      manager.get(Database.rl(structurePath)).ifPresent(template -> template.placeInWorld(world, pos, pos, settings, world.getRandom(), 3));
   }

   public static void placeDungeonUnlockerBlockWithRotation(
      ServerLevel destinationWorld, BlockPos pos, Rotation rotation, boolean canGenerate, int type, int floor
   ) {
      if (rotation == null) {
         throw new IllegalArgumentException("Rotation cannot be null.");
      } else {
         Direction baseDir = Direction.NORTH;
         Direction facing = rotation.rotate(baseDir);
         if (!facing.getAxis().isHorizontal()) {
            throw new IllegalArgumentException("DungeonUnlockerBlock can only face horizontal directions.");
         } else {
            BlockState unlockerState = (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((DungeonUnlockerBlock)BeyondBlocks.DUNGEON_UNLOCKER_BLOCK
                              .get())
                           .defaultBlockState()
                           .setValue(DungeonUnlockerBlock.FACING, facing))
                        .setValue(DungeonUnlockerBlock.CAN_GENERATE, canGenerate))
                     .setValue(DungeonUnlockerBlock.ENABLED, true))
                  .setValue(DungeonUnlockerBlock.TYPE, type))
               .setValue(DungeonUnlockerBlock.FLOOR, floor);
            destinationWorld.setBlock(pos, unlockerState, 3);
         }
      }
   }

   private static int calculateFloorForUnlocker(@NotNull MutableBlockPos pos) {
      return (pos.floorcount - 1) / 10;
   }
}
