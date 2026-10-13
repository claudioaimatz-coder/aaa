package io.github.Memoires.trbeyond.config;

import io.github.Memoires.trbeyond.client.screen.KurobeShopEnum;
import java.util.EnumMap;
import java.util.Map;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class NpcConfig {
   public final ConfigValue<String> highValueItem;
   public final ConfigValue<String> medValueItem;
   public final ConfigValue<String> lowValueItem;
   public final ConfigValue<String> lowestValueItem;
   public final ConfigValue<String> nameSpace;
   public final Map<KurobeShopEnum.KurobeShop, IntValue> item_prices = new EnumMap<>(KurobeShopEnum.KurobeShop.class);

   public NpcConfig(Builder builder) {
      builder.push("customCoinItems");
      this.highValueItem = builder.comment("name of item to use for NPC shops, highest value").define("highCoin", "stellar_gold_coin");
      this.medValueItem = builder.comment("name of item to use for NPC shops, medium value").define("medCoin", "gold_coin");
      this.lowValueItem = builder.comment("name of item to use for NPC shops, low value").define("lowCoin", "silver_coin");
      this.lowestValueItem = builder.comment("name of item to use for NPC shops, lowest value").define("lowestCoin", "bronze_coin");
      this.nameSpace = builder.comment("namespace for items, used to prevent conflicts with other mods").define("name_space", "tensura");
      builder.pop();
      builder.push("itemPrices");

      for (KurobeShopEnum.KurobeShop item : KurobeShopEnum.KurobeShop.values()) {
         this.item_prices.put(item, builder.comment("Price for " + item.name()).defineInRange(item.name().toLowerCase() + "_price", item.cost, 1, 64));
      }

      builder.pop();
   }
}
