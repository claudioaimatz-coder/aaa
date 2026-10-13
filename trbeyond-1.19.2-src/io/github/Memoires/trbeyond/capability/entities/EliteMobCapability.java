package io.github.Memoires.trbeyond.capability.entities;

import io.github.Memoires.trbeyond.network.BeyondNetwork;
import io.github.Memoires.trbeyond.network.SyncEliteMobCapabilityPacket;
import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

public class EliteMobCapability implements IEliteMobCapability {
   private boolean isElite = false;

   @Override
   public boolean isElite() {
      return this.isElite;
   }

   @Override
   public void setElite(boolean elite) {
      this.isElite = elite;
   }

   public static Optional<IEliteMobCapability> getFrom(LivingEntity entity) {
      LazyOptional<IEliteMobCapability> capability = entity.getCapability(EliteMobProvider.ELITE_CAPABILITY);
      return capability.resolve();
   }

   public static void sync(LivingEntity entity) {
      if (!entity.level.isClientSide()) {
         getFrom(entity)
            .ifPresent(
               data -> BeyondNetwork.INSTANCE
                     .send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new SyncEliteMobCapabilityPacket(data, entity.getId()))
            );
      }
   }
}
