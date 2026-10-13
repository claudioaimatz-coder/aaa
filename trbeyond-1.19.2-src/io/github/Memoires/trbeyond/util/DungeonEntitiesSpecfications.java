package io.github.Memoires.trbeyond.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class DungeonEntitiesSpecfications {
   ServerLevel world;
   BlockPos pos;
   int floorCount;
   boolean treasureRoom;
   boolean hallwayEntity;

   public DungeonEntitiesSpecfications(ServerLevel w, BlockPos p, int fc, boolean tR, boolean he) {
      this.world = w;
      this.pos = p;
      this.floorCount = fc;
      this.treasureRoom = tR;
      this.hallwayEntity = he;
   }

   public ServerLevel getWorld() {
      return this.world;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public int getFloorCount() {
      return this.floorCount;
   }

   public boolean isTreasureRoom() {
      return this.treasureRoom;
   }

   public boolean isHallwayEntity() {
      return this.hallwayEntity;
   }
}
