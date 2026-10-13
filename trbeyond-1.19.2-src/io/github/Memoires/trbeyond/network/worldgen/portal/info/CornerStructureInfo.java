package io.github.Memoires.trbeyond.network.worldgen.portal.info;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import org.jetbrains.annotations.NotNull;

public class CornerStructureInfo extends BaseStructureInfo<CornerStructureInfo.CornerInfoBuilder> {
   private BlockPos sideOffset;

   protected CornerStructureInfo(@NotNull CornerStructureInfo.CornerInfoBuilder builder) {
      super(builder);
      this.sideOffset = builder.sideOffset;
   }

   @NotNull
   public static CornerStructureInfo.CornerInfoBuilder builder() {
      return new CornerStructureInfo.CornerInfoBuilder();
   }

   @Override
   public BlockPos calculatePlacementPos(@NotNull BlockPos target, Direction facing, Mirror mirror) {
      return super.calculatePlacementPos(target, facing, mirror);
   }

   @Override
   public List<DungeonStructureInfo.DungeonCursor> calculateNewCursors(@NotNull BlockPos target, @NotNull Direction facing) {
      return List.of(
         new DungeonStructureInfo.DungeonCursor(
            target.relative(facing, this.getSize().getX() - this.sideOffset.getX())
               .relative(facing.getClockWise(), this.getSize().getZ() - this.sideOffset.getZ()),
            facing.getClockWise()
         )
      );
   }

   public static class CornerInfoBuilder extends InfoBuilder<CornerStructureInfo.CornerInfoBuilder> {
      private BlockPos sideOffset = new BlockPos(3, 0, 2);

      public CornerStructureInfo build() {
         return new CornerStructureInfo(this);
      }

      public CornerStructureInfo.CornerInfoBuilder setSideOffset(BlockPos sideOffset) {
         this.sideOffset = sideOffset;
         return this.getSelf();
      }

      protected CornerStructureInfo.CornerInfoBuilder getSelf() {
         return this;
      }
   }
}
