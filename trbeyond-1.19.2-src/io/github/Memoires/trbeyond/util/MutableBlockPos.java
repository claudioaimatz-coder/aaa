package io.github.Memoires.trbeyond.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

public class MutableBlockPos {
   public BlockPos pos;
   public StructurePlaceSettings hallwaySettings;
   public String direction;
   public boolean stairsplaced;
   public int floorcount;
   public static int room_count;

   public MutableBlockPos(BlockPos pos, StructurePlaceSettings hallwaySet, String dir) {
      this.pos = pos;
      this.hallwaySettings = hallwaySet;
      this.direction = dir;
      this.stairsplaced = false;
      this.floorcount = 1;
      room_count = 0;
   }

   public MutableBlockPos copy() {
      MutableBlockPos copy = new MutableBlockPos(new BlockPos(this.pos), this.hallwaySettings.copy(), this.direction);
      copy.stairsplaced = this.stairsplaced;
      copy.floorcount = this.floorcount;
      return copy;
   }
}
