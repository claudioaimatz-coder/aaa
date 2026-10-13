package io.github.Memoires.trbeyond.capability.boss;

import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public abstract class BeyondBossCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
   private final IBeyondBossCapability defaultCapability = new BeyondBossCapability();
   private final LazyOptional<IBeyondBossCapability> cap = LazyOptional.of(() -> this.defaultCapability);

   @NotNull
   public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
      return cap == BeyondBossCapability.CAPABILITY ? this.cap.cast() : LazyOptional.empty();
   }

   public CompoundTag serializeNBT() {
      return (CompoundTag)this.defaultCapability.serializeNBT();
   }

   public void deserializeNBT(CompoundTag nbt) {
      this.defaultCapability.deserializeNBT(nbt);
   }
}
