package io.github.Memoires.trbeyond.network;

import io.github.Memoires.trbeyond.capability.entities.EliteMobCapability;
import io.github.Memoires.trbeyond.capability.entities.IEliteMobCapability;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent.Context;

public class SyncEliteMobCapabilityPacket {
   private final CompoundTag tag;
   private final int entityId;

   public SyncEliteMobCapabilityPacket(IEliteMobCapability data, int entityId) {
      this.tag = new CompoundTag();
      this.tag.putBoolean("isElite", data.isElite());
      this.entityId = entityId;
   }

   public SyncEliteMobCapabilityPacket(FriendlyByteBuf buf) {
      this.tag = buf.readNbt();
      this.entityId = buf.readInt();
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeNbt(this.tag);
      buf.writeInt(this.entityId);
   }

   public void handle(Supplier<Context> ctx) {
      ctx.get().enqueueWork(() -> {
         if (ctx.get().getSender().level.getEntity(this.entityId) instanceof LivingEntity livingEntity) {
            EliteMobCapability.getFrom(livingEntity).ifPresent(cap -> cap.setElite(this.tag.getBoolean("isElite")));
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
