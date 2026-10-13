package io.github.Memoires.trbeyond.network.worldgen.portal.info;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

public class DeadEndStructureInfo extends BaseStructureInfo<DeadEndStructureInfo.DeadEndInfoBuilder> {
   protected DeadEndStructureInfo(@NotNull DeadEndStructureInfo.DeadEndInfoBuilder builder) {
      super(builder);
   }

   @NotNull
   public static DeadEndStructureInfo.DeadEndInfoBuilder builder() {
      return new DeadEndStructureInfo.DeadEndInfoBuilder();
   }

   @Override
   public BlockPos calculatePlacementPos(BlockPos target, Direction facing) {
      return super.calculatePlacementPos(target, facing);
   }

   @Override
   public List<DungeonStructureInfo.DungeonCursor> calculateNewCursors(BlockPos target, Direction facing) {
      return List.of();
   }

   public static class DeadEndInfoBuilder extends InfoBuilder<DeadEndStructureInfo.DeadEndInfoBuilder> {
      public DeadEndStructureInfo build() {
         return new DeadEndStructureInfo(this);
      }

      protected DeadEndStructureInfo.DeadEndInfoBuilder getSelf() {
         return this;
      }
   }
}
