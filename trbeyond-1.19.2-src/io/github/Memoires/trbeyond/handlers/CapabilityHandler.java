package io.github.Memoires.trbeyond.handlers;

import io.github.Memoires.trbeyond.capability.dungeon.DungeonCapability;
import io.github.Memoires.trbeyond.capability.dungeon.IDungeonCapability;
import io.github.Memoires.trbeyond.capability.entities.EliteMobCapability;
import io.github.Memoires.trbeyond.capability.entities.IEliteMobCapability;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
public class CapabilityHandler {
   public static void registerCapabilities(RegisterCapabilitiesEvent e) {
      e.register(IDungeonCapability.class);
      e.register(IEliteMobCapability.class);
   }

   @SubscribeEvent
   static void onPlayerLogin(PlayerLoggedInEvent e) {
      DungeonCapability.sync(e.getEntity());
   }

   @SubscribeEvent
   static void onPlayerTrack(StartTracking e) {
      if (e.getTarget() instanceof LivingEntity living) {
         DungeonCapability.sync(living);
         DungeonCapability.sync(e.getEntity());
         EliteMobCapability.sync(living);
      }
   }

   @SubscribeEvent
   static void onPlayerClone(Clone e) {
      e.getOriginal().reviveCaps();
      DungeonCapability.getFrom(e.getOriginal())
         .ifPresent(oldData -> DungeonCapability.getFrom(e.getEntity()).ifPresent(data -> data.deserializeNBT((CompoundTag)oldData.serializeNBT())));
      e.getOriginal().invalidateCaps();
   }

   @SubscribeEvent
   static void onPlayerRespawn(PlayerRespawnEvent e) {
      DungeonCapability.sync(e.getEntity());
   }

   @SubscribeEvent
   static void onPlayerChangeDimension(PlayerChangedDimensionEvent e) {
      DungeonCapability.sync(e.getEntity());
   }

   @Nullable
   public static <T> T getCapability(Entity entity, Capability<T> capability) {
      return (T)(entity.getCapability(capability).isPresent()
         ? entity.getCapability(capability).orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty"))
         : null);
   }
}
