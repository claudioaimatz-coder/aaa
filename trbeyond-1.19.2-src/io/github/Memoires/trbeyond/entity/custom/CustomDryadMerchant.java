package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.registry.items.TensuraMaterialItems;
import io.github.Memoires.trbeyond.registry.items.BeyondItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;

public class CustomDryadMerchant implements Merchant {
   private Player tradingPlayer;
   private MerchantOffers offers = new MerchantOffers();
   private final SimpleContainer inventory = new SimpleContainer(3);

   public CustomDryadMerchant() {
      this.initializeOffers();
   }

   public void initializeOffers() {
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get(), 2),
               new ItemStack((ItemLike)BeyondItems.REVIVAL_BRACELET.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get(), 30),
               new ItemStack((ItemLike)BeyondItems.RETURN_WHISTLE.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)BeyondItems.RECORDING_CRYSTAL.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 5),
               new ItemStack((ItemLike)BeyondItems.AUTO_RECORDING_CRYSTAL.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 3),
               new ItemStack((ItemLike)BeyondItems.SPATIAL_POUCH.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 20),
               new ItemStack((ItemLike)BeyondItems.SPATIAL_BAG.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.STELLAR_GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)BeyondItems.SPATIAL_BACKPACK.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
   }

   public void setTradingPlayer(Player player) {
      this.tradingPlayer = player;
   }

   public Player getTradingPlayer() {
      return this.tradingPlayer;
   }

   public MerchantOffers getOffers() {
      return this.offers;
   }

   public void overrideOffers(MerchantOffers p_45306_) {
      this.offers = p_45306_;
   }

   public void notifyTrade(MerchantOffer offer) {
   }

   public void notifyTradeUpdated(ItemStack p_45308_) {
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
}
