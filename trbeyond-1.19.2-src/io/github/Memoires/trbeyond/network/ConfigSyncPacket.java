package io.github.Memoires.trbeyond.network;

import io.github.Memoires.trbeyond.config.ServerConfig;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public class ConfigSyncPacket {
   public final boolean koruobee;
   public final int[] prices;

   public ConfigSyncPacket(boolean koruobee, int[] prices) {
      this.koruobee = koruobee;
      this.prices = prices;
   }

   public static void encode(ConfigSyncPacket msg, FriendlyByteBuf buf) {
      buf.writeBoolean(msg.koruobee);

      for (int v : msg.prices) {
         buf.writeInt(v);
      }
   }

   public static ConfigSyncPacket decode(FriendlyByteBuf buf) {
      boolean k = buf.readBoolean();
      int[] arr = new int[8];

      for (int i = 0; i < 8; i++) {
         arr[i] = buf.readInt();
      }

      return new ConfigSyncPacket(k, arr);
   }

   public static void handle(ConfigSyncPacket msg, Supplier<Context> ctxSup) {
      Context ctx = ctxSup.get();
      ctx.enqueueWork(() -> {
         ClientConfigData.KORUOBEE_SKILL_ENABLED = msg.koruobee;
         ClientConfigData.ENCHANT_S_PRICE_GOLD = msg.prices[0];
         ClientConfigData.ENCHANT_A_PRICE_SILVER = msg.prices[1];
         ClientConfigData.ENCHANT_B_PRICE_SILVER = msg.prices[2];
         ClientConfigData.ENCHANT_C_TO_F_PRICE_SILVER = msg.prices[3];
         ClientConfigData.ENGRAVE_S_PRICE_STELLAR = msg.prices[4];
         ClientConfigData.ENGRAVE_A_PRICE_GOLD = msg.prices[5];
         ClientConfigData.ENGRAVE_B_PRICE_GOLD = msg.prices[6];
         ClientConfigData.ENGRAVE_F_PRICE_GOLD = msg.prices[7];
      });
      ctx.setPacketHandled(true);
   }

   public static ConfigSyncPacket fromConfig() {
      return new ConfigSyncPacket(
         (Boolean)ServerConfig.KORUOBEE_SKILL_ENABLED.get(),
         new int[]{
            (Integer)ServerConfig.ENCHANT_S_PRICE_GOLD.get(),
            (Integer)ServerConfig.ENCHANT_A_PRICE_SILVER.get(),
            (Integer)ServerConfig.ENCHANT_B_PRICE_SILVER.get(),
            (Integer)ServerConfig.ENCHANT_C_TO_F_PRICE_SILVER.get(),
            (Integer)ServerConfig.ENGRAVE_S_PRICE_STELLAR.get(),
            (Integer)ServerConfig.ENGRAVE_A_PRICE_GOLD.get(),
            (Integer)ServerConfig.ENGRAVE_B_PRICE_GOLD.get(),
            (Integer)ServerConfig.ENGRAVE_F_PRICE_GOLD.get()
         }
      );
   }
}
