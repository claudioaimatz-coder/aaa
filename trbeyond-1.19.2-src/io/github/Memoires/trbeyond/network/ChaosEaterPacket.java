package io.github.Memoires.trbeyond.network;

import io.github.Memoires.trbeyond.entity.projectile.ChaosEaterProjectile;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;

public class ChaosEaterPacket {
   public ChaosEaterPacket() {
   }

   public ChaosEaterPacket(FriendlyByteBuf buf) {
   }

   public void toBytes(FriendlyByteBuf buf) {
   }

   public void handle(Supplier<Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer player = ctx.get().getSender();
         Level level = player.level;
         ChaosEaterProjectile chaosEater = new ChaosEaterProjectile(level, player.position().add(0.0, 2.0, 0.0));
         chaosEater.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 4.0F, 1.0F);
         chaosEater.setOwner(player);
         chaosEater.setYRot(player.getYRot());
         level.addFreshEntity(chaosEater);
      });
      ctx.get().setPacketHandled(true);
   }
}
