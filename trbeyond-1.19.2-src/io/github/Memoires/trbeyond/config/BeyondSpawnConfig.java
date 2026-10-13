package io.github.Memoires.trbeyond.config;

import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class BeyondSpawnConfig {
   public final IntValue bossRespawnTime;

   public BeyondSpawnConfig(Builder builder) {
      builder.push("bossRespawnTime");
      this.bossRespawnTime = builder.comment("Time in minutes for the bosses to respawn. Default: 10 min, Min: 1, Max: 60")
         .defineInRange("bossRespawnTime", 10, 1, 60);
      builder.pop();
   }

   private DoubleValue configureSpawnChance(Builder builder, String name, double defaultValue, String comment) {
      return builder.comment(comment).defineInRange(name, defaultValue, 0.0, 100.0);
   }
}
