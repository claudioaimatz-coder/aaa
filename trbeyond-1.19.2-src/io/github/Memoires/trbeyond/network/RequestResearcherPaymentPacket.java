package io.github.Memoires.trbeyond.network;

import com.github.manasmods.tensura.client.TensuraGUIHelper;
import com.github.manasmods.tensura.menu.ResearcherEnchantmentMenu;
import com.github.manasmods.tensura.registry.items.TensuraMaterialItems;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;

public class RequestResearcherPaymentPacket {
   private final int stellar;
   private final int gold;
   private final int silver;

   public RequestResearcherPaymentPacket() {
      this(0, 0, 0);
   }

   public RequestResearcherPaymentPacket(int stellar, int gold, int silver) {
      this.stellar = stellar;
      this.gold = gold;
      this.silver = silver;
   }

   public static void encode(RequestResearcherPaymentPacket msg, FriendlyByteBuf buf) {
      buf.writeInt(msg.stellar);
      buf.writeInt(msg.gold);
      buf.writeInt(msg.silver);
   }

   public static RequestResearcherPaymentPacket decode(FriendlyByteBuf buf) {
      return new RequestResearcherPaymentPacket(buf.readInt(), buf.readInt(), buf.readInt());
   }

   public static void handle(RequestResearcherPaymentPacket msg, Supplier<Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer player = ctx.get().getSender();
         if (player != null) {
            Inventory inv = player.getInventory();
            boolean success = true;
            if (msg.stellar > 0) {
               if (hasEnoughCoins(inv, (Item)TensuraMaterialItems.STELLAR_GOLD_COIN.get(), msg.stellar)) {
                  success &= removeCoins(inv, (Item)TensuraMaterialItems.STELLAR_GOLD_COIN.get(), msg.stellar);
               } else {
                  success = false;
               }
            } else if (msg.gold > 0) {
               if (hasEnoughCoins(inv, (Item)TensuraMaterialItems.GOLD_COIN.get(), msg.gold)) {
                  success &= removeCoins(inv, (Item)TensuraMaterialItems.GOLD_COIN.get(), msg.gold);
               } else {
                  success = false;
               }
            } else if (msg.silver > 0) {
               if (hasEnoughCoins(inv, (Item)TensuraMaterialItems.SILVER_COIN.get(), msg.silver)) {
                  success &= removeCoins(inv, (Item)TensuraMaterialItems.SILVER_COIN.get(), msg.silver);
               } else {
                  success = false;
               }
            }

            if (!success) {
               player.displayClientMessage(Component.literal("Not enough coins!"), true);
            } else {
               if (player.containerMenu instanceof ResearcherEnchantmentMenu menu) {
                  menu.handleFinalizeEnchantment();
               }

               TensuraGUIHelper.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F);
            }
         }
      });
      ctx.get().setPacketHandled(true);
   }

   private static boolean hasEnoughCoins(Inventory inv, Item itemId, int amount) {
      int total = 0;

      for (int i = 0; i < inv.getContainerSize(); i++) {
         ItemStack stack = inv.getItem(i);
         if (!stack.isEmpty() && stack.getItem().equals(itemId)) {
            total += stack.getCount();
            if (total >= amount) {
               return true;
            }
         }
      }

      return false;
   }

   private static boolean removeCoins(Inventory inv, Item itemId, int amount) {
      int remaining = amount;

      for (int i = 0; i < inv.getContainerSize(); i++) {
         ItemStack stack = inv.getItem(i);
         if (!stack.isEmpty() && stack.getItem().equals(itemId)) {
            int count = stack.getCount();
            int taken = Math.min(count, remaining);
            stack.shrink(taken);
            remaining -= taken;
            if (remaining <= 0) {
               break;
            }
         }
      }

      return remaining <= 0;
   }
}
