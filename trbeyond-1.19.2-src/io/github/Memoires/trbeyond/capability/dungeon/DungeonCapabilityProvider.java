package io.github.Memoires.trbeyond.capability.dungeon;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DungeonCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
   private final IDungeonCapability defaultCapability = new DungeonCapability();
   private final LazyOptional<IDungeonCapability> cap = LazyOptional.of(() -> this.defaultCapability);

   @NotNull
   public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
      return cap == DungeonCapability.CAPABILITY ? this.cap.cast() : LazyOptional.empty();
   }

   public CompoundTag serializeNBT() {
      return (CompoundTag)this.defaultCapability.serializeNBT();
   }

   public void deserializeNBT(CompoundTag nbt) {
      this.defaultCapability.deserializeNBT(nbt);
   }
}
