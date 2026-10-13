package io.github.Memoires.trbeyond.network.worldgen.portal.info;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

public abstract class BaseStructureInfo<T extends InfoBuilder<T>> implements DungeonStructureInfo {
   private final String address;
   private final Direction facing;
   private final BlockPos entranceOffset;
   private final BlockPos size;
   private final BlockPos entityPos;

   protected BaseStructureInfo(@NotNull T builder) {
      this.address = builder.address;
      this.facing = builder.facing;
      this.entranceOffset = builder.entranceOffset;
      this.size = builder.size;
      this.entityPos = builder.entityPos;
   }

   @Override
   public Direction getFacing() {
      return this.facing;
   }

   @Override
   public String getAddress() {
      return this.address;
   }

   @Override
   public BlockPos getSize() {
      return this.size;
   }

   @Override
   public BlockPos getEntranceOffset() {
      return this.entranceOffset;
   }

   @Override
   public BlockPos calculateEntityPos(@NotNull BlockPos target, @NotNull Direction facing) {
      return target.relative(facing.getClockWise(), this.entityPos.getZ())
         .relative(facing, this.entityPos.getX())
         .offset(0, this.entityPos.getY(), 0);
   }

   @Override
   public String toString() {
      return this.address + " {Face: " + this.facing + " Size: " + this.size.toShortString() + "}";
   }
}
