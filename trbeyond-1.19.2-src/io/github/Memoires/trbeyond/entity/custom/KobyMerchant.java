package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.registry.items.TensuraArmorItems;
import com.github.manasmods.tensura.registry.items.TensuraConsumableItems;
import com.github.manasmods.tensura.registry.items.TensuraMaterialItems;
import com.github.manasmods.tensura.registry.items.TensuraToolItems;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class KobyMerchant implements Merchant {
   private Player tradingPlayer;
   private final Map<UUID, MerchantOffers> playerOffers = new HashMap<>();
   private final Map<UUID, LocalDateTime> lastRestockTimes = new HashMap<>();
   private final Map<UUID, Boolean> needsRestockMap = new HashMap<>();

   public void initializeOffersForPlayer(UUID playerUUID) {
      int tradeCount = 100;
      MerchantOffers offers = new MerchantOffers();
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get(), 4),
            new ItemStack((ItemLike)TensuraConsumableItems.LOW_POTION.get(), 1),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get(), 35),
            new ItemStack((ItemLike)TensuraConsumableItems.HIGH_POTION.get(), 1),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 5),
            new ItemStack((ItemLike)TensuraConsumableItems.FULL_POTION.get(), 1),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraToolItems.SPIDER_BOW.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 30),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraToolItems.ANT_CROSSBOW.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 30),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraToolItems.GOBLIN_CLUB.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 30),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraToolItems.CENTIPEDE_DAGGER.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 30),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraToolItems.SPIDER_DAGGER.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 30),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraArmorItems.ANT_CARAPACE_HELMET.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 30),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraArmorItems.ANT_CARAPACE_CHESTPLATE.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 30),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraArmorItems.ANT_CARAPACE_LEGGINGS.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 30),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraArmorItems.ANT_CARAPACE_BOOTS.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 30),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraArmorItems.KNIGHT_SPIDER_CARAPACE_HELMET.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 30),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraArmorItems.KNIGHT_SPIDER_CARAPACE_CHESTPLATE.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 30),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraArmorItems.KNIGHT_SPIDER_CARAPACE_LEGGINGS.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 30),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraArmorItems.KNIGHT_SPIDER_CARAPACE_BOOTS.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 30),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraToolItems.BLADE_TIGER_SCYTHE.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 50),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraToolItems.KANABO.get(), 1), new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 50), tradeCount, 0, 0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraToolItems.BEAST_HORN_SPEAR.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 50),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraToolItems.UNICORN_HORN_SPEAR.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 50),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraArmorItems.SERPENT_SCALEMAIL_HELMET.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 50),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraArmorItems.SERPENT_SCALEMAIL_CHESTPLATE.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 50),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraArmorItems.SERPENT_SCALEMAIL_LEGGINGS.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 50),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraArmorItems.SERPENT_SCALEMAIL_BOOTS.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 50),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraArmorItems.ARMOURSAURUS_SCALEMAIL_HELMET.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 50),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraArmorItems.ARMOURSAURUS_SCALEMAIL_CHESTPLATE.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 50),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraArmorItems.ARMOURSAURUS_SCALEMAIL_LEGGINGS.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 50),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraArmorItems.ARMOURSAURUS_SCALEMAIL_BOOTS.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 50),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraToolItems.ICE_BLADE.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get(), 1),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraToolItems.VORTEX_SPEAR.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get(), 1),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraToolItems.TEMPEST_SCALE_SWORD.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get(), 1),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraToolItems.TEMPEST_SCALE_KNIFE.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get(), 1),
            tradeCount,
            0,
            0.0F
         )
      );
      offers.add(
         new MerchantOffer(
            new ItemStack((ItemLike)TensuraToolItems.TEMPEST_SCALE_SHIELD.get(), 1),
            new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get(), 1),
            tradeCount,
            0,
            0.0F
         )
      );
      this.playerOffers.put(playerUUID, offers);
      this.lastRestockTimes.put(playerUUID, LocalDateTime.now());
      this.needsRestockMap.put(playerUUID, false);
   }

   public void setTradingPlayer(Player player) {
      this.tradingPlayer = player;
      if (player != null) {
         UUID playerUUID = player.getUUID();
         if (!this.playerOffers.containsKey(playerUUID)) {
            this.initializeOffersForPlayer(playerUUID);
         }
      }
   }

   public Player getTradingPlayer() {
      return this.tradingPlayer;
   }

   @NotNull
   public MerchantOffers getOffers() {
      if (this.tradingPlayer == null) {
         return new MerchantOffers();
      } else {
         UUID playerUUID = this.tradingPlayer.getUUID();
         if (!this.playerOffers.containsKey(playerUUID)) {
            this.initializeOffersForPlayer(playerUUID);
         }

         return this.playerOffers.get(playerUUID);
      }
   }

   public void overrideOffers(@NotNull MerchantOffers pOffers) {
      if (this.tradingPlayer != null) {
         this.playerOffers.put(this.tradingPlayer.getUUID(), pOffers);
      }
   }

   public void notifyTrade(@NotNull MerchantOffer offer) {
      if (this.tradingPlayer != null) {
         UUID playerUUID = this.tradingPlayer.getUUID();
         offer.increaseUses();
         if (this.needsToRestock(playerUUID)) {
            this.needsRestockMap.put(playerUUID, true);
         }
      }
   }

   public void notifyTradeUpdated(@NotNull ItemStack pStack) {
   }

   public void overrideXp(int xp) {
   }

   public boolean showProgressBar() {
      return false;
   }

   public SoundEvent getNotifyTradeSound() {
      return null;
   }

   public int getVillagerXp() {
      return 0;
   }

   public boolean isClientSide() {
      return false;
   }

   public void checkDespawn() {
   }

   private boolean needsToRestock(UUID playerUUID) {
      if (!this.playerOffers.containsKey(playerUUID)) {
         return false;
      } else {
         for (MerchantOffer offer : this.playerOffers.get(playerUUID)) {
            if (offer.needsRestock()) {
               return true;
            }
         }

         return false;
      }
   }

   public void restock(UUID playerUUID) {
      if (this.canRestock(playerUUID)) {
         MerchantOffers offers = this.playerOffers.get(playerUUID);
         if (offers != null) {
            for (MerchantOffer offer : offers) {
               offer.resetUses();
            }

            this.lastRestockTimes.put(playerUUID, LocalDateTime.now());
            this.needsRestockMap.put(playerUUID, false);
         }
      }
   }

   private boolean has24HoursPassed(UUID playerUUID) {
      LocalDateTime lastRestock = this.lastRestockTimes.get(playerUUID);
      if (lastRestock == null) {
         return true;
      } else {
         LocalDateTime now = LocalDateTime.now();
         long hoursPassed = ChronoUnit.HOURS.between(lastRestock, now);
         return hoursPassed >= 24L;
      }
   }

   public boolean canRestock(UUID playerUUID) {
      Boolean needsRestock = this.needsRestockMap.getOrDefault(playerUUID, false);
      return needsRestock && this.has24HoursPassed(playerUUID);
   }

   public void checkAndRestock() {
      for (UUID playerUUID : this.playerOffers.keySet()) {
         if (this.canRestock(playerUUID)) {
            this.restock(playerUUID);
         }
      }
   }

   public void saveToNBT(CompoundTag tag) {
      CompoundTag offersTag = new CompoundTag();

      for (Entry<UUID, MerchantOffers> entry : this.playerOffers.entrySet()) {
         String uuidString = entry.getKey().toString();
         offersTag.put(uuidString, entry.getValue().createTag());
      }

      tag.put("PlayerOffers", offersTag);
      CompoundTag restockTimesTag = new CompoundTag();

      for (Entry<UUID, LocalDateTime> entry : this.lastRestockTimes.entrySet()) {
         String uuidString = entry.getKey().toString();
         restockTimesTag.putString(uuidString, entry.getValue().toString());
      }

      tag.put("RestockTimes", restockTimesTag);
      CompoundTag needsRestockTag = new CompoundTag();

      for (Entry<UUID, Boolean> entry : this.needsRestockMap.entrySet()) {
         String uuidString = entry.getKey().toString();
         needsRestockTag.putBoolean(uuidString, entry.getValue());
      }

      tag.put("NeedsRestock", needsRestockTag);
   }

   public void loadFromNBT(CompoundTag tag) {
      this.playerOffers.clear();
      if (tag.contains("PlayerOffers")) {
         CompoundTag offersTag = tag.getCompound("PlayerOffers");

         for (String key : offersTag.getAllKeys()) {
            UUID playerId = UUID.fromString(key);
            MerchantOffers offers = new MerchantOffers(offersTag.getCompound(key));
            this.playerOffers.put(playerId, offers);
         }
      }

      this.lastRestockTimes.clear();
      if (tag.contains("RestockTimes")) {
         CompoundTag timesTag = tag.getCompound("RestockTimes");

         for (String key : timesTag.getAllKeys()) {
            UUID playerId = UUID.fromString(key);
            LocalDateTime time = LocalDateTime.parse(timesTag.getString(key));
            this.lastRestockTimes.put(playerId, time);
         }
      }

      this.needsRestockMap.clear();
      if (tag.contains("NeedsRestock")) {
         CompoundTag restockTag = tag.getCompound("NeedsRestock");

         for (String key : restockTag.getAllKeys()) {
            UUID playerId = UUID.fromString(key);
            boolean needsRestock = restockTag.getBoolean(key);
            this.needsRestockMap.put(playerId, needsRestock);
         }
      }
   }
}
