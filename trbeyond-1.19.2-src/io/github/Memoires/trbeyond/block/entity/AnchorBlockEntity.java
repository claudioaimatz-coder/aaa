package io.github.Memoires.trbeyond.block.entity;

import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AnchorBlockEntity extends BlockEntity {
   public static final int MAX_FLOOR = -9;
   private int currentFloor = 0;

   public AnchorBlockEntity(BlockPos pos, BlockState state) {
      super((BlockEntityType)BeyondBlockEntities.DUNGEON_ANCHOR.get(), pos, state);
   }

   public void increaseFloor() {
      this.currentFloor++;
   }

   public void decreaseFloor() {
      this.currentFloor--;
   }

   public int getCurrentFloor() {
      return this.currentFloor;
   }

   public void setCurrentFloor(int currentFloor) {
      this.currentFloor = currentFloor;
   }

   public void changeFloor(int value) {
      this.currentFloor += value;
   }

   protected void saveAdditional(CompoundTag tag) {
      super.saveAdditional(tag);
      tag.putInt("floor", this.currentFloor);
   }

   public void load(CompoundTag tag) {
      super.load(tag);
      this.currentFloor = tag.getInt("floor");
   }
}
