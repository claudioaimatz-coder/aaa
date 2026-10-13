package io.github.Memoires.trbeyond.handlers;

import io.github.Memoires.trbeyond.capability.boss.BeyondBossCapability;
import io.github.Memoires.trbeyond.capability.boss.IBeyondBossCapability;
import io.github.Memoires.trbeyond.capability.player.BeyondPlayerCapability;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.FORGE
)
public class BossCapabilityHandler {
   @SubscribeEvent
   public static void registerCapabilities(RegisterCapabilitiesEvent e) {
      e.register(IBeyondBossCapability.class);
   }

   @SubscribeEvent
   static void onPlayerLogin(PlayerLoggedInEvent e) {
      Player player = e.getEntity();
      BeyondBossCapability.sync(player);
   }

   @SubscribeEvent
   static void onPlayerTrack(StartTracking e) {
      if (e.getTarget() instanceof Player player) {
         BeyondBossCapability.sync(player);
         BeyondBossCapability.sync(e.getEntity());
      }
   }

   @SubscribeEvent
   static void onPlayerClone(Clone e) {
      e.getOriginal().reviveCaps();
      Player newPlayer = e.getEntity();
      BeyondBossCapability.getFrom(e.getOriginal())
         .ifPresent(oldData -> BeyondBossCapability.getFrom(newPlayer).ifPresent(data -> data.deserializeNBT((CompoundTag)oldData.serializeNBT())));
      if (!e.isWasDeath()) {
         newPlayer.refreshDimensions();
      }

      e.getOriginal().invalidateCaps();
   }

   @SubscribeEvent
   static void onPlayerRespawn(PlayerRespawnEvent e) {
      Player player = e.getEntity();
      BeyondBossCapability.sync(player);
      BeyondPlayerCapability.getFrom(e.getEntity()).ifPresent(cap -> {
         cap.setReturned(true);
         cap.setUsedTicket(false);
      });
      BeyondPlayerCapability.sync(e.getEntity());
   }

   @SubscribeEvent
   static void onPlayerChangeDimension(PlayerChangedDimensionEvent e) {
      BeyondBossCapability.sync(e.getEntity());
      BeyondPlayerCapability.getFrom(e.getEntity()).ifPresent(cap -> {
         cap.setReturned(true);
         cap.setUsedTicket(false);
      });
      BeyondPlayerCapability.sync(e.getEntity());
   }

   @Nullable
   public static <T> T getCapability(Entity entity, Capability<T> capability) {
      return (T)(entity.getCapability(capability).isPresent()
         ? entity.getCapability(capability).orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty"))
         : null);
   }
}
