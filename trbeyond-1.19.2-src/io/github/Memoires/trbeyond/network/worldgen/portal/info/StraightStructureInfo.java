package io.github.Memoires.trbeyond.network.worldgen.portal.info;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

public class StraightStructureInfo extends BaseStructureInfo<StraightStructureInfo.StraightInfoBuilder> {
   protected StraightStructureInfo(@NotNull StraightStructureInfo.StraightInfoBuilder builder) {
      super(builder);
   }

   @NotNull
   public static StraightStructureInfo.StraightInfoBuilder builder() {
      return new StraightStructureInfo.StraightInfoBuilder();
   }

   @Override
   public BlockPos calculatePlacementPos(@NotNull BlockPos target, Direction facing, Mirror mirror) {
      Rotation rot = this.getRotationTo(facing);
      BlockPos movedEntrance = StructureTemplate.transform(this.getEntranceOffset(), mirror, rot, BlockPos.ZERO);
      return target.subtract(movedEntrance);
   }

   @Override
   public List<DungeonStructureInfo.DungeonCursor> calculateNewCursors(@NotNull BlockPos target, Direction facing) {
      return List.of(new DungeonStructureInfo.DungeonCursor(target.relative(facing, this.getSize().getX()), facing));
   }

   public static class StraightInfoBuilder extends InfoBuilder<StraightStructureInfo.StraightInfoBuilder> {
      public StraightStructureInfo build() {
         return new StraightStructureInfo(this);
      }

      protected StraightStructureInfo.StraightInfoBuilder getSelf() {
         return this;
      }
   }
}
