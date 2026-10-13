package io.github.Memoires.trbeyond.network.worldgen.portal.info;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

public class StairsStructureInfo extends BaseStructureInfo<StairsStructureInfo.StairsInfoBuilder> {
   protected StairsStructureInfo(@NotNull StairsStructureInfo.StairsInfoBuilder builder) {
      super(builder);
   }

   @NotNull
   public static StairsStructureInfo.StairsInfoBuilder builder() {
      return new StairsStructureInfo.StairsInfoBuilder();
   }

   @Override
   public BlockPos calculatePlacementPos(BlockPos target, Direction facing) {
      return super.calculatePlacementPos(target, facing);
   }

   @Override
   public List<DungeonStructureInfo.DungeonCursor> calculateNewCursors(@NotNull BlockPos target, @NotNull Direction facing) {
      return List.of(
         new DungeonStructureInfo.DungeonCursor(
            target.below(this.getEntranceOffset().getY()).relative(facing.getCounterClockWise(), this.getSize().getZ() / 2).relative(facing.getOpposite()),
            facing.getOpposite()
         )
      );
   }

   public static class StairsInfoBuilder extends InfoBuilder<StairsStructureInfo.StairsInfoBuilder> {
      public StairsStructureInfo build() {
         return new StairsStructureInfo(this);
      }

      protected StairsStructureInfo.StairsInfoBuilder getSelf() {
         return this;
      }
   }
}
