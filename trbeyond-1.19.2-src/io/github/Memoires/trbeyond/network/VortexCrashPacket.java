package io.github.Memoires.trbeyond.network;

import io.github.Memoires.trbeyond.handlers.VortexCrashHandler;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class VortexCrashPacket {
   public VortexCrashPacket() {
   }

   public VortexCrashPacket(FriendlyByteBuf buf) {
   }

   public void toBytes(FriendlyByteBuf buf) {
   }

   public void handle(Supplier<Context> ctx) {
      ctx.get().enqueueWork(() -> {
         System.out.println("Server: VortexCrashPacket received.");
         ServerPlayer player = ctx.get().getSender();
         if (player != null) {
            System.out.println("Server: Valid player found - " + player.getName().getString());
            VortexCrashHandler.useSkillServerSide(player);
         }
      });
      ctx.get().setPacketHandled(true);
   }

   public static void sendToServer() {
      BeyondNetwork.INSTANCE.sendToServer(new VortexCrashPacket());
   }
}
