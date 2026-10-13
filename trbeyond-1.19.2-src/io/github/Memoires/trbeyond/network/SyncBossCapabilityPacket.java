package io.github.Memoires.trbeyond.network;

import io.github.Memoires.trbeyond.capability.boss.IBeyondBossCapability;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class SyncBossCapabilityPacket {
   private final CompoundTag tag;
   private final int entityId;

   public SyncBossCapabilityPacket(FriendlyByteBuf buf) {
      this.tag = buf.readAnySizeNbt();
      this.entityId = buf.readInt();
   }

   public SyncBossCapabilityPacket(IBeyondBossCapability data, int entityId) {
      this.tag = (CompoundTag)data.serializeNBT();
      this.entityId = entityId;
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeNbt(this.tag);
      buf.writeInt(this.entityId);
   }

   public void handle(Supplier<Context> ctx) {
      ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientAccess.updateBossCapability(this.entityId, this.tag)));
      ctx.get().setPacketHandled(true);
   }
}
