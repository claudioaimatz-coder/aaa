package io.github.Memoires.trbeyond.capability.entities;

import io.github.Memoires.trbeyond.Database;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EliteMobProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
   public static final Capability<IEliteMobCapability> ELITE_CAPABILITY = CapabilityManager.get(new CapabilityToken<IEliteMobCapability>() {
   });
   private final IEliteMobCapability instance = new EliteMobCapability();
   private final LazyOptional<IEliteMobCapability> optionalInstance = LazyOptional.of(() -> this.instance);

   public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
      return cap == ELITE_CAPABILITY ? this.optionalInstance.cast() : LazyOptional.empty();
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      tag.putBoolean("isElite", this.instance.isElite());
      return tag;
   }

   public void deserializeNBT(CompoundTag tag) {
      this.instance.setElite(tag.getBoolean("isElite"));
   }

   @SubscribeEvent
   public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof LivingEntity) {
         event.addCapability(Database.rl("elite_mob"), new EliteMobProvider());
      }
   }
}
