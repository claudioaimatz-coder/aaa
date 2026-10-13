package io.github.Memoires.trbeyond.network.worldgen.portal.info;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

public interface DungeonStructureInfo {
   Direction getFacing();

   String getAddress();

   BlockPos getSize();

   BlockPos getEntranceOffset();

   default BlockPos calculatePlacementPos(BlockPos target, Direction facing) {
      return this.calculatePlacementPos(target, facing, Mirror.NONE);
   }

   default BlockPos calculatePlacementPos(@NotNull BlockPos target, Direction facing, Mirror mirror) {
      Rotation rot = this.getRotationTo(facing);
      BlockPos movedEntrance = StructureTemplate.transform(this.getEntranceOffset(), mirror, rot, BlockPos.ZERO);
      return target.subtract(movedEntrance);
   }

   List<DungeonStructureInfo.DungeonCursor> calculateNewCursors(BlockPos var1, Direction var2);

   BlockPos calculateEntityPos(BlockPos var1, Direction var2);

   default Rotation getRotationTo(@NotNull Direction target) {
      int diff = (this.getFacing().get2DDataValue() - target.get2DDataValue() + 4) % 4;

      return switch (diff) {
         case 0 -> Rotation.CLOCKWISE_180;
         case 1 -> Rotation.CLOCKWISE_90;
         case 2 -> Rotation.NONE;
         case 3 -> Rotation.COUNTERCLOCKWISE_90;
         default -> throw new IllegalStateException("Impossible rotation: " + diff);
      };
   }

   public static record DungeonCursor(BlockPos pos, Direction facing) {
   }
}
