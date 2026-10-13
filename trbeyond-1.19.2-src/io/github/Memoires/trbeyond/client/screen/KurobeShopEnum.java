package io.github.Memoires.trbeyond.client.screen;

import io.github.Memoires.trbeyond.config.ServerConfig;

public class KurobeShopEnum {
   static String highValueCoin = (String)ServerConfig.INSTANCE.npcConfig.highValueItem.get();
   static String medValueCoin = (String)ServerConfig.INSTANCE.npcConfig.medValueItem.get();
   static String lowValueCoin = (String)ServerConfig.INSTANCE.npcConfig.lowValueItem.get();

   public static String getItem(int page, int slot) {
      for (KurobeShopEnum.KurobeShop item : KurobeShopEnum.KurobeShop.values()) {
         if (item.page == page && item.slot == slot) {
            return item.itemName;
         }
      }

      return null;
   }

   public static int getCost(String itemName) {
      for (KurobeShopEnum.KurobeShop item : KurobeShopEnum.KurobeShop.values()) {
         if (item.itemName.equals(itemName)) {
            if (ServerConfig.INSTANCE == null) {
               return item.cost;
            }

            return (Integer)ServerConfig.INSTANCE.npcConfig.item_prices.get(item).get();
         }
      }

      return 1;
   }

   public static String getCoinType(String itemName) {
      for (KurobeShopEnum.KurobeShop item : KurobeShopEnum.KurobeShop.values()) {
         if (item.itemName.equals(itemName)) {
            return switch (item.coinType) {
               case 0 -> lowValueCoin;
               case 1 -> medValueCoin;
               case 2 -> highValueCoin;
               default -> lowValueCoin;
            };
         }
      }

      return "stellar_gold_coin";
   }

   public static boolean hasPage(int page) {
      for (KurobeShopEnum.KurobeShop item : KurobeShopEnum.KurobeShop.values()) {
         if (item.page == page) {
            return true;
         }
      }

      return false;
   }

   public static int getLargestPage() {
      int largest = 1;

      for (KurobeShopEnum.KurobeShop item : KurobeShopEnum.KurobeShop.values()) {
         if (item.page > largest) {
            largest = item.page;
         }
      }

      return largest;
   }

   public static int getItemCount() {
      return KurobeShopEnum.KurobeShop.values().length;
   }

   public static int getItemCount(int page) {
      int count = 0;

      for (KurobeShopEnum.KurobeShop item : KurobeShopEnum.KurobeShop.values()) {
         if (item.page == page) {
            count++;
         }
      }

      return count;
   }

   public static enum KurobeShop {
      GUREN("guren_unique", 0, 1, 14, 2),
      VORTEX_SPEAR("vortex_spear_unique", 0, 2, 10, 2),
      MEAT_CLEAVER("meat_cleaver_unique", 0, 3, 8, 2),
      GORIKIMARU("gorikimaru_unique", 0, 4, 15, 2),
      DIABLO_CLAW("diablo_claw_legend", 0, 5, 15, 2),
      SHION_SHIRT("shion_chestplate_unique", 1, 1, 1, 2),
      SHION_PANTS("shion_leggings_unique", 1, 2, 1, 2),
      SHION_BOOTS("shion_boots_special", 1, 3, 5, 1),
      BENIMARU_SHIRT("benimaru_chestplate_unique", 1, 4, 1, 2),
      BENIMARU_PANTS("benimaru_leggings_unique", 1, 5, 1, 2),
      BENIMARU_BOOTS("benimaru_boots_special", 1, 6, 5, 1),
      HAKURO_SHIRT("hakuro_chestplate_unique", 1, 7, 1, 2),
      HAKURO_PANTS("hakuro_leggings_unique", 1, 8, 1, 2),
      HAKURO_BOOTS("hakuro_boots_special", 1, 9, 5, 1),
      DIALBO_SHIRT("diablo_chestplate_legend", 2, 1, 1, 2),
      DIALBO_PANTS("diablo_leggings_legend", 2, 2, 1, 2),
      DIALBO_BOOTS("diablo_boots_special", 2, 3, 5, 1),
      GELD_HELMET("geld_helmet_unique", 2, 4, 1, 2),
      GELD_CHESTPLATE("geld_chestplate_unique", 2, 5, 1, 2),
      GELD_LEGGINGS("geld_leggings_unique", 2, 6, 1, 2),
      GELD_BOOTS("geld_boots_unique", 2, 7, 1, 2),
      GELD_SHIELD("scale_shield_unique", 2, 8, 3, 2),
      GABIRU_SHIRT("gabiru_chestplate_unique", 3, 1, 1, 2),
      GABIRU_PANTS("gabiru_leggings_unique", 3, 2, 1, 2),
      SHIBKOBO("shibkobo_unique", 3, 4, 5, 2),
      HAKURO("hakuro_unique", 3, 5, 5, 2),
      NINJATO("ninjato_unique", 3, 6, 5, 2),
      SOUEI_SHIRT("souei_chestplate_unique", 3, 7, 1, 2),
      SOUEI_PANTS("souei_leggings_unique", 3, 8, 1, 2),
      SOUEI_BOOTS("souei_boots_special", 3, 9, 5, 1);

      public final String itemName;
      private final int page;
      private final int slot;
      public final int cost;
      private final int coinType;

      private KurobeShop(String itemName, int page, int slot, int cost, int coinType) {
         this.itemName = itemName;
         this.page = page;
         this.slot = slot;
         this.cost = cost;
         this.coinType = coinType;
      }
   }
}
