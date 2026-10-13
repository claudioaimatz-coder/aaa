package io.github.Memoires.trbeyond.config;

import java.util.Collections;
import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;

public class ServerConfig {
   public static final ServerConfig INSTANCE;
   public static final ForgeConfigSpec SPEC;
   public final DungeonConfig dungeonConfig;
   public final NpcConfig npcConfig;
   public final BossConfig bossConfig;
   public final BeyondSpawnConfig spawnConfig;
   public final FloorSpawnConfig floorSpawnConfig;
   public final ConfigValue<List<? extends String>> disableSkillsInDungeon;
   public final ConfigValue<List<? extends String>> npcShopConfigs;
   public static BooleanValue KORUOBEE_SKILL_ENABLED;
   public static IntValue ENCHANT_S_PRICE_GOLD;
   public static IntValue ENCHANT_A_PRICE_SILVER;
   public static IntValue ENCHANT_B_PRICE_SILVER;
   public static IntValue ENCHANT_C_TO_F_PRICE_SILVER;
   public static IntValue ENGRAVE_S_PRICE_STELLAR;
   public static IntValue ENGRAVE_A_PRICE_GOLD;
   public static IntValue ENGRAVE_B_PRICE_GOLD;
   public static IntValue ENGRAVE_F_PRICE_GOLD;
   public final ConfigValue<List<? extends String>> diabloClawBlacklist;

   public ServerConfig(Builder builder) {
      builder.push("disableSkillsInDungeon");
      this.disableSkillsInDungeon = builder.comment("List of skill IDs or categories to disable during dungeon runs.")
         .defineList("disableSkillsInDungeon", Collections.emptyList(), obj -> true);
      builder.pop();
      builder.push("npcShopConfigs");
      this.npcShopConfigs = builder.comment("Set what NPCs are allowed to sell in the dungeon lobby/shop.")
         .comment("Valid NPCs: trbeyond:dryad, trbeyond:kurobe, trbeyond:kaijin, trbeyond:koby, trbeyond:guild_lady, trbeyond:mjollmile, trbeyond:kaoru")
         .defineList("npcShopConfigs", Collections.emptyList(), obj -> true);
      builder.pop();
      builder.push("dungeon");
      this.dungeonConfig = new DungeonConfig(builder);
      builder.pop();
      builder.push("npc");
      this.npcConfig = new NpcConfig(builder);
      builder.pop();
      builder.push("Spawn");
      this.spawnConfig = new BeyondSpawnConfig(builder);
      builder.pop();
      builder.push("boss");
      this.bossConfig = new BossConfig(builder);
      builder.pop();
      builder.push("floorsSpawns");
      this.floorSpawnConfig = new FloorSpawnConfig(builder);
      builder.pop();
      builder.push("enchant_and_engrave_prices");
      KORUOBEE_SKILL_ENABLED = builder.comment(
            "If true, players with Koruobee skill can open both Enchant + Engrave GUIs. Otherwise only Shop GUI is available."
         )
         .define("koruobeeSkillEnabled", true);
      builder.comment("Enchant prices");
      ENCHANT_S_PRICE_GOLD = builder.defineInRange("enchant_S_price_gold", 1, 0, 1000);
      ENCHANT_A_PRICE_SILVER = builder.defineInRange("enchant_A_price_silver", 25, 0, 1000);
      ENCHANT_B_PRICE_SILVER = builder.defineInRange("enchant_B_price_silver", 10, 0, 1000);
      ENCHANT_C_TO_F_PRICE_SILVER = builder.defineInRange("enchant_CtoF_price_silver", 5, 0, 1000);
      builder.comment("Engrave prices");
      ENGRAVE_S_PRICE_STELLAR = builder.defineInRange("engrave_S_price_stellar", 1, 0, 1000);
      ENGRAVE_A_PRICE_GOLD = builder.defineInRange("engrave_A_price_gold", 25, 0, 1000);
      ENGRAVE_B_PRICE_GOLD = builder.defineInRange("engrave_B_price_gold", 10, 0, 1000);
      ENGRAVE_F_PRICE_GOLD = builder.defineInRange("engrave_F_price_gold", 1, 0, 1000);
      builder.push("Diablo_Claw_Deflect_Blacklist");
      this.diabloClawBlacklist = builder.comment("List of projectiles that cannot be deflected using the diablo claw")
         .defineList("Diablo_Claw_Deflect_Blacklist", Collections.emptyList(), bool -> true);
      builder.pop();
   }

   static {
      Pair<ServerConfig, ForgeConfigSpec> pair = new Builder().configure(ServerConfig::new);
      INSTANCE = (ServerConfig)pair.getKey();
      SPEC = (ForgeConfigSpec)pair.getValue();
   }
}
