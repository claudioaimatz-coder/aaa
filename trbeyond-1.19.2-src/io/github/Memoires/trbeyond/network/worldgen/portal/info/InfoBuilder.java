package io.github.Memoires.trbeyond.network.worldgen.portal.info;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public abstract class InfoBuilder<T extends InfoBuilder<T>> {
   protected String address = "";
   protected Direction facing = Direction.EAST;
   protected BlockPos entranceOffset = BlockPos.ZERO;
   protected BlockPos size = BlockPos.ZERO;
   protected BlockPos entityPos = BlockPos.ZERO;

   public T setAddress(String address) {
      this.address = address;
      return this.getSelf();
   }

   public T setFacing(Direction facing) {
      this.facing = facing;
      return this.getSelf();
   }

   public T setEntranceOffset(BlockPos entranceOffset) {
      this.entranceOffset = entranceOffset;
      return this.getSelf();
   }

   public T setSize(BlockPos size) {
      this.size = size;
      return this.getSelf();
   }

   public T setEntityPos(BlockPos entityPos) {
      this.entityPos = entityPos;
      return this.getSelf();
   }

   protected abstract T getSelf();
}
