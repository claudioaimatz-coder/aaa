package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.registry.items.TensuraMaterialItems;
import com.github.manasmods.tensura.registry.items.TensuraSmithingSchematicItems;
import io.github.Memoires.trbeyond.registry.items.BeyondToolItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;

public class KaijinMerchant implements Merchant {
   private Player tradingPlayer;
   private MerchantOffers offers = new MerchantOffers();
   private final SimpleContainer inventory = new SimpleContainer(3);

   public KaijinMerchant() {
      this.initializeOffers();
   }

   public void initializeOffers() {
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.IRON_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.GOLD_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.DIAMOND_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.ANT_CARAPACE_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.ARMOURSAURUS_SCALEMAIL_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.KNIGHT_SPIDER_CARAPACE_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.SERPENT_SCALEMAIL_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.SILVER_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.LEATHER_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.MONSTER_LEATHER_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.DARK_SET.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.ANTI_MAGIC_MASK.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.LOW_MAGISTEEL_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.HIGH_MAGISTEEL_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.PURE_MAGISTEEL_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.MITHRIL_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.ORICHALCUM_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.ADAMANTITE_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.HIHIIROKANE_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.CHARYBDIS_SCALEMAIL_GEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.PIERROT_MASK.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.BASIC_BOWS.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.SPIDER_BOWS.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.GREAT_SWORD.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.HUNTING_KNIFE.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.SHIELD.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.SPEAR.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.JAPANESE_SWORD.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.KUNAI.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.LONG_SWORD.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.SHORT_SWORD.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.WINGED_SHOES.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.WEB_GUN.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 1),
               new ItemStack((ItemLike)TensuraSmithingSchematicItems.SPATIAL_BLADE.get(), 1),
               Integer.MAX_VALUE,
               0,
               0.0F
            )
         );
      this.offers
         .add(
            new MerchantOffer(
               new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), 10),
               new ItemStack((ItemLike)BeyondToolItems.GLENDA_PISTOL.get(), 1),
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
