package io.github.Memoires.trbeyond.network;

import io.github.Memoires.trbeyond.capability.dungeon.IDungeonCapability;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class SyncDungeonCapabilityPacket {
   private final CompoundTag tag;
   private final int entityId;

   public SyncDungeonCapabilityPacket(FriendlyByteBuf buf) {
      this.tag = buf.readAnySizeNbt();
      this.entityId = buf.readInt();
   }

   public SyncDungeonCapabilityPacket(IDungeonCapability data, int entityId) {
      this.tag = (CompoundTag)data.serializeNBT();
      this.entityId = entityId;
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeNbt(this.tag);
      buf.writeInt(this.entityId);
   }

   public void handle(Supplier<Context> ctx) {
      ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientAccess.updateDungeonCapability(this.entityId, this.tag)));
      ctx.get().setPacketHandled(true);
   }
}
