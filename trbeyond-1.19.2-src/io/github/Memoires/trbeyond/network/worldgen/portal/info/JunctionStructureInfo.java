package io.github.Memoires.trbeyond.network.worldgen.portal.info;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

public class JunctionStructureInfo extends BaseStructureInfo<JunctionStructureInfo.JunctionInfoBuilder> {
   private final int sideOffset;

   protected JunctionStructureInfo(@NotNull JunctionStructureInfo.JunctionInfoBuilder builder) {
      super(builder);
      this.sideOffset = builder.sideOffset;
   }

   @NotNull
   public static JunctionStructureInfo.JunctionInfoBuilder builder() {
      return new JunctionStructureInfo.JunctionInfoBuilder();
   }

   @Override
   public BlockPos calculatePlacementPos(BlockPos target, Direction facing) {
      return super.calculatePlacementPos(target, facing);
   }

   @Override
   public List<DungeonStructureInfo.DungeonCursor> calculateNewCursors(@NotNull BlockPos target, Direction facing) {
      return List.of(
         new DungeonStructureInfo.DungeonCursor(
            target.relative(facing, this.getSize().getX() - this.sideOffset).relative(facing.getCounterClockWise(), this.getSize().getZ() / 2 + 1),
            facing.getCounterClockWise()
         ),
         new DungeonStructureInfo.DungeonCursor(
            target.relative(facing, this.getSize().getX() - this.sideOffset).relative(facing.getClockWise(), this.getSize().getZ() / 2 + 1),
            facing.getClockWise()
         )
      );
   }

   public static class JunctionInfoBuilder extends InfoBuilder<JunctionStructureInfo.JunctionInfoBuilder> {
      private int sideOffset = 3;

      public JunctionStructureInfo build() {
         return new JunctionStructureInfo(this);
      }

      public JunctionStructureInfo.JunctionInfoBuilder setSideOffset(int sideOffset) {
         this.sideOffset = sideOffset;
         return this.getSelf();
      }

      protected JunctionStructureInfo.JunctionInfoBuilder getSelf() {
         return this;
      }
   }
}
