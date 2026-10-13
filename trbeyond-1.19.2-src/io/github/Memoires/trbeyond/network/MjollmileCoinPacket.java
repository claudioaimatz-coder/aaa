package io.github.Memoires.trbeyond.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.registries.ForgeRegistries;

public class MjollmileCoinPacket {
   int changeType;

   public MjollmileCoinPacket(int changeType) {
      this.changeType = changeType;
   }

   public MjollmileCoinPacket(FriendlyByteBuf buf) {
      this.changeType = buf.readInt();
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeInt(this.changeType);
   }

   public void handle(Supplier<Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer serverPlayer = ctx.get().getSender();
         if (serverPlayer != null) {
            Item toCoin = switch (this.changeType) {
               case 1 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "bronze_coin"));
               case 2 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "silver_coin"));
               case 3 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "gold_coin"));
               case 4 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "silver_coin"));
               case 5 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "gold_coin"));
               case 6 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "stellar_gold_coin"));
               default -> null;
            };

            Item fromCoin = switch (this.changeType) {
               case 1 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "silver_coin"));
               case 2 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "gold_coin"));
               case 3 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "stellar_gold_coin"));
               case 4 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "bronze_coin"));
               case 5 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "silver_coin"));
               case 6 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "gold_coin"));
               default -> null;
            };

            int fromCount = switch (this.changeType) {
               case 1, 2, 3 -> 1;
               case 4, 5, 6 -> 100;
               default -> 0;
            };

            int toCount = switch (this.changeType) {
               case 1, 2, 3 -> 100;
               case 4, 5, 6 -> 1;
               default -> 0;
            };
            if (serverPlayer.getInventory().contains(new ItemStack(fromCoin)) && serverPlayer.getInventory().countItem(fromCoin) >= fromCount) {
               int coinsToRemove = fromCount;

               for (int i = 0; i < serverPlayer.getInventory().getContainerSize(); i++) {
                  ItemStack stack = serverPlayer.getInventory().getItem(i);
                  if (stack.getItem().equals(fromCoin)) {
                     int count = Math.min(coinsToRemove, stack.getCount());
                     stack.shrink(count);
                     coinsToRemove -= count;
                     if (coinsToRemove <= 0) {
                        break;
                     }
                  }
               }

               int coinsToAdd = toCount;

               for (int ix = 0; ix < serverPlayer.getInventory().getContainerSize(); ix++) {
                  ItemStack stack = serverPlayer.getInventory().getItem(ix);
                  if (stack.getItem().equals(toCoin)) {
                     int freeSpace = toCoin.getMaxStackSize() - stack.getCount();
                     if (freeSpace > 0) {
                        int countToAdd = Math.min(coinsToAdd, freeSpace);
                        stack.grow(countToAdd);
                        coinsToAdd -= countToAdd;
                        if (coinsToAdd <= 0) {
                           break;
                        }
                     }
                  }
               }

               while (coinsToAdd > 0) {
                  ItemStack newStack = new ItemStack(toCoin, Math.min(coinsToAdd, toCoin.getMaxStackSize()));
                  int freeSlot = serverPlayer.getInventory().getFreeSlot();
                  if (freeSlot != -1) {
                     coinsToAdd -= newStack.getCount();
                     serverPlayer.getInventory().setItem(freeSlot, newStack);
                  } else {
                     coinsToAdd -= newStack.getCount();
                     serverPlayer.drop(newStack, false);
                  }
               }
            }

            System.out.println("Handling MjollmileCoinPacket with changeType: " + this.changeType);
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
