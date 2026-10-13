package io.github.Memoires.trbeyond.config;

import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.LongValue;

public class DungeonConfig {
   public final BooleanValue generateColosseum;
   public final IntValue thinHallwayTrapChance;
   public final IntValue longThinHallwayTrapChance;
   public final IntValue hallwayEndTrapChance;
   public final IntValue emptyEndRoomChance;
   public final IntValue endRoomTrapChance;
   public final BooleanValue chestLock;
   public final LongValue chestCooldown;
   public final IntValue chestLootTime;
   public final IntValue shrineCooldown;
   public final IntValue stellarShrineCooldown;
   public final BooleanValue shrineCooldownPlaythroughs;
   public final IntValue shrineFailPenalty;
   public final IntValue playerLimit;
   public final LongValue enterCooldown;
   public final BooleanValue perPlayerLoot;
   public final ConfigValue<String> bronzeChestLoot;
   public final ConfigValue<String> silverChestLoot;
   public final ConfigValue<String> goldChestLoot;
   public final ConfigValue<String> bossChestLoot;
   public final IntValue maxDungeonTime;

   public DungeonConfig(Builder builder) {
      builder.push("generateColosseum");
      this.generateColosseum = builder.comment("Should the Colosseum be generated?").define("generateColosseum", true);
      builder.pop();
      builder.push("trapChances");
      this.thinHallwayTrapChance = builder.comment("Chance for traps to spawn in the thin hallway").defineInRange("thinHallwayTrapChance", 2, 0, 100);
      this.longThinHallwayTrapChance = builder.comment("Chance for traps to spawn in the long thin hallway")
         .defineInRange("longThinHallwayTrapChance", 15, 0, 100);
      this.hallwayEndTrapChance = builder.comment("Chance for traps to spawn at the end of the hallway").defineInRange("hallwayEndTrapChance", 10, 0, 100);
      this.emptyEndRoomChance = builder.comment("Chance for a dead end room to be empty").defineInRange("emptyEndRoomChance", 10, 0, 100);
      this.endRoomTrapChance = builder.comment("Chance for traps to spawn in a dead end room instead of treasure")
         .defineInRange("endRoomTrapChance", 65, 0, 100);
      builder.pop();
      builder.push("chestLock");
      this.chestLock = builder.comment("Should the chests be locked after the cooldown has finished?").define("chestLock", false);
      builder.pop();
      builder.push("chestCooldown");
      this.chestCooldown = builder.comment("Cooldown for chests in the dungeon in minutes.").defineInRange("chestCooldown", 30L, 0L, 1440L);
      builder.pop();
      builder.push("chestLootTime");
      this.chestLootTime = builder.comment("Time in minutes to wait before a chest will reset loot.").defineInRange("chestLootTime", 30, 0, 1440);
      builder.pop();
      builder.push("shrineCooldown");
      this.shrineCooldown = builder.comment("Cooldown for shrines in the dungeon in minutes.").defineInRange("shrineCooldown", 30, 0, 1440);
      builder.pop();
      builder.push("stellarShrineCooldown");
      this.stellarShrineCooldown = builder.comment("Cooldown for stellar shrines in the dungeon in minutes.")
         .defineInRange("stellarShrineCooldown", 90, 0, 1440);
      builder.pop();
      builder.push("shrineCooldownPlaythroughs");
      this.shrineCooldownPlaythroughs = builder.comment("Should players be able to play through shrine challenges without rewards if on cooldown?")
         .define("shrineCooldownPlaythroughs", false);
      builder.pop();
      builder.push("shrineFailPenalty");
      this.shrineFailPenalty = builder.comment(
            "Number of completions the player gets subtracted from their completion count when they fail a shrine challenge."
         )
         .defineInRange("shrineFailPenalty", 5, 0, 20);
      builder.pop();
      builder.push("playerLimit");
      this.playerLimit = builder.comment("Number of player to limit teleportation at. or 0 for no limit").defineInRange("playerLimit", 0, 0, 100);
      builder.pop();
      builder.push("enterCooldown");
      this.enterCooldown = builder.comment("Cooldown for entering the dungeon in seconds.").defineInRange("enterCooldown", 0L, 0L, Long.MAX_VALUE);
      builder.pop();
      builder.push("perPlayerLoot");
      this.perPlayerLoot = builder.comment("Should the loot of the chests be per player?").define("perPlayerLoot", true);
      builder.pop();
      builder.push("chestLoot");
      this.bronzeChestLoot = builder.comment("Loot table for bronze chests").define("bronzeChestLoot", "trbeyond:bronze_1");
      this.silverChestLoot = builder.comment("Loot table for silver chests").define("silverChestLoot", "trbeyond:silver_1");
      this.goldChestLoot = builder.comment("Loot table for gold chests").define("goldChestLoot", "trbeyond:gold_1");
      this.bossChestLoot = builder.comment("Loot table for boss chests").define("bossChestLoot", "trbeyond:gold_1");
      builder.pop();
      builder.push("maxDungeonTime");
      this.maxDungeonTime = builder.comment(
            "Time in minutes that they players are allowed to be in the Dungeon before being forcefully teleported outside. 0 = infinite time"
         )
         .defineInRange("maxDungeonTime", 0, 0, 100);
      builder.pop();
   }
}
