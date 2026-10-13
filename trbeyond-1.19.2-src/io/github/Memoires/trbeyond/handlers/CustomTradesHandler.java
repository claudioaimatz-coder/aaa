package io.github.Memoires.trbeyond.handlers;

import io.github.Memoires.trbeyond.registry.items.BeyondItems;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(
   modid = "trbeyond"
)
public class CustomTradesHandler {
   private static HashMap<UUID, MerchantOffers> offersPerTrader = new HashMap<>();

   @SubscribeEvent
   public static void wandererSpawns(LivingSpawnEvent event) {
      if (event.getEntity() instanceof WanderingTrader trader && !offersPerTrader.containsKey(trader.getUUID())) {
         Random random = new Random();
         List<ItemStack> costItems = List.of(new ItemStack(Items.EMERALD, 3), new ItemStack(Items.DIAMOND, 3));
         ItemStack costItem = costItems.get(random.nextInt(0, costItems.size()));
         MerchantOffer offer = new MerchantOffer(costItem, new ItemStack((ItemLike)BeyondItems.DUNGEON_VOUCHER.get(), 1), 1, 0, 1.0F);
         if (!trader.getOffers().contains(offer)) {
            MerchantOffers offers = trader.getOffers();
            offers.add(offer);
            trader.overrideOffers(offers);
            offersPerTrader.put(trader.getUUID(), offers);
         }
      }
   }
}
