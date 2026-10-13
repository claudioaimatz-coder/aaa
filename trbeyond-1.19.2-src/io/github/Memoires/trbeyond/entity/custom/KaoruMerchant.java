package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.registry.items.TensuraMaterialItems;
import io.github.Memoires.trbeyond.registry.items.BeyondFoodItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;

public class KaoruMerchant implements Merchant {
   private Player tradingPlayer;
   private MerchantOffers offers = new MerchantOffers();
   private final SimpleContainer inventory = new SimpleContainer(3);

   public KaoruMerchant() {
      this.initializeOffers();
   }

   public void initializeOffers() {
      this.offers.clear();
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 30),
               new ItemStack((ItemLike)BeyondFoodItems.MEAT.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get(), 1),
               new ItemStack((ItemLike)BeyondFoodItems.MEGALODON_SASHIMI.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 5),
               new ItemStack((ItemLike)BeyondFoodItems.BULK_MEAT_SKEWER.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 10),
               new ItemStack((ItemLike)BeyondFoodItems.PORK_RAMEN.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 3),
               new ItemStack((ItemLike)BeyondFoodItems.TAMAHOYAKI.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 8),
               new ItemStack((ItemLike)BeyondFoodItems.SUSHI_PLATE.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 10),
               new ItemStack((ItemLike)BeyondFoodItems.SHUNA_SOUP.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 3),
               new ItemStack((ItemLike)BeyondFoodItems.TEA.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get(), 1),
               new ItemStack((ItemLike)BeyondFoodItems.HONEY.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get(), 2),
               new ItemStack((ItemLike)BeyondFoodItems.BRANDY_BOTTLE.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get(), 20),
               new ItemStack((ItemLike)BeyondFoodItems.BRANDY_GLASS.get(), 1),
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
