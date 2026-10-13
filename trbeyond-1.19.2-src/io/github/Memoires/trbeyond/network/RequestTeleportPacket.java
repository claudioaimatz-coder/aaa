package io.github.Memoires.trbeyond.network;

import io.github.Memoires.trbeyond.items.effects.EffectItem;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class RequestTeleportPacket {
   public static void encode(RequestTeleportPacket msg, FriendlyByteBuf buf) {
   }

   public static RequestTeleportPacket decode(FriendlyByteBuf buf) {
      return new RequestTeleportPacket();
   }

   public static void handle(RequestTeleportPacket msg, Supplier<Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer player = ctx.get().getSender();
         if (player != null) {
            EffectItem.offsetPlayer(player);
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
