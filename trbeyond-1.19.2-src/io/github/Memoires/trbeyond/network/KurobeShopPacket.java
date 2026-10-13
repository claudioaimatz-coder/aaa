package io.github.Memoires.trbeyond.network;

import io.github.Memoires.trbeyond.client.screen.KurobeShopEnum;
import io.github.Memoires.trbeyond.config.ServerConfig;
import java.util.function.Supplier;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.registries.ForgeRegistries;

public class KurobeShopPacket {
   int page;
   int slot;

   public KurobeShopPacket(int page, int slot) {
      this.page = page;
      this.slot = slot;
   }

   public KurobeShopPacket(FriendlyByteBuf buf) {
      this.page = buf.readInt();
      this.slot = buf.readInt();
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeInt(this.page);
      buf.writeInt(this.slot);
   }

   public void handle(Supplier<Context> ctx) {
      ctx.get()
         .enqueueWork(
            () -> {
               ServerPlayer serverPlayer = ctx.get().getSender();
               if (serverPlayer != null) {
                  if (serverPlayer.getInventory()
                        .contains(
                           new ItemStack(
                              (ItemLike)ForgeRegistries.ITEMS
                                 .getValue(
                                    new ResourceLocation(
                                       (String)ServerConfig.INSTANCE.npcConfig.nameSpace.get(),
                                       KurobeShopEnum.getCoinType(KurobeShopEnum.getItem(this.page, this.slot))
                                    )
                                 )
                           )
                        )
                     && serverPlayer.getInventory()
                           .countItem(
                              (Item)ForgeRegistries.ITEMS
                                 .getValue(
                                    new ResourceLocation(
                                       (String)ServerConfig.INSTANCE.npcConfig.nameSpace.get(),
                                       KurobeShopEnum.getCoinType(KurobeShopEnum.getItem(this.page, this.slot))
                                    )
                                 )
                           )
                        >= KurobeShopEnum.getCost(KurobeShopEnum.getItem(this.page, this.slot))
                     && serverPlayer.getInventory().getFreeSlot() != -1) {
                     int coinsToRemove = KurobeShopEnum.getCost(KurobeShopEnum.getItem(this.page, this.slot));

                     for (int i = 0; i < serverPlayer.getInventory().getContainerSize(); i++) {
                        ItemStack stack = serverPlayer.getInventory().getItem(i);
                        if (stack.getItem()
                           .equals(
                              ForgeRegistries.ITEMS
                                 .getValue(
                                    new ResourceLocation(
                                       (String)ServerConfig.INSTANCE.npcConfig.nameSpace.get(),
                                       KurobeShopEnum.getCoinType(KurobeShopEnum.getItem(this.page, this.slot))
                                    )
                                 )
                           )) {
                           int count = Math.min(coinsToRemove, stack.getCount());
                           stack.shrink(count);
                           coinsToRemove -= count;
                           if (coinsToRemove <= 0) {
                              break;
                           }
                        }
                     }

                     ItemStack itemToGive = new ItemStack(
                        (ItemLike)ForgeRegistries.ITEMS.getValue(new ResourceLocation("trbeyond", KurobeShopEnum.getItem(this.page, this.slot)))
                     );
                     serverPlayer.getInventory().add(itemToGive);
                     MinecraftServer server = serverPlayer.getServer();
                     if (server != null) {
                        ResourceLocation advId = new ResourceLocation("trbeyond", "a_true_marvel");
                        Advancement advancement = server.m_129889_().getAdvancement(advId);
                        if (advancement != null) {
                           AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);

                           for (String criterion : progress.getRemainingCriteria()) {
                              serverPlayer.getAdvancements().award(advancement, criterion);
                           }
                        }
                     }
                  }
               }
            }
         );
      ctx.get().setPacketHandled(true);
   }
}
