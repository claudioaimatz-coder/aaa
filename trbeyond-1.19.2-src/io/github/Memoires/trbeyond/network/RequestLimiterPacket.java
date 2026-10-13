package io.github.Memoires.trbeyond.network;

import io.github.Memoires.trbeyond.entity.custom.ILimiter;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;

public class RequestLimiterPacket {
   private final UUID bossId;
   private final ILimiter.Limitation action;

   public RequestLimiterPacket(FriendlyByteBuf buf) {
      this.bossId = buf.readUUID();
      this.action = (ILimiter.Limitation)buf.readEnum(ILimiter.Limitation.class);
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeUUID(this.bossId);
      buf.writeEnum(this.action);
   }

   public void handle(Supplier<Context> ctx) {
      ctx.get()
         .enqueueWork(
            () -> {
               Player player = ctx.get().getSender();
               if (player != null
                  && player.getLevel() instanceof ServerLevel serverLevel
                  && serverLevel.getEntity(this.bossId) instanceof ILimiter boss
                  && boss instanceof Mob mob) {
                  boss.setLimitation(this.action.getId());
                  mob.setTarget(player);
                  mob.setLastHurtByPlayer(player);
               }
            }
         );
      ctx.get().setPacketHandled(true);
   }

   public RequestLimiterPacket(UUID bossId, ILimiter.Limitation action) {
      this.bossId = bossId;
      this.action = action;
   }
}
