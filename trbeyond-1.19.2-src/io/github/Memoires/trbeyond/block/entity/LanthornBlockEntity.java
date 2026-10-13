package io.github.Memoires.trbeyond.block.entity;

import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LanthornBlockEntity extends BlockEntity {
   private long placedTime = -1L;

   public LanthornBlockEntity(BlockPos pos, BlockState state) {
      super((BlockEntityType)BeyondBlockEntities.LANTHORN_BLOCK.get(), pos, state);
   }

   public static void tick(Level level, BlockPos pos, BlockState state, LanthornBlockEntity blockEntity) {
      if (!level.isClientSide) {
         if (blockEntity.placedTime < 0L) {
            blockEntity.placedTime = level.getGameTime();
         } else if (level.getGameTime() >= blockEntity.placedTime + 400L) {
            level.removeBlock(pos, false);
         }
      }
   }

   public void load(CompoundTag tag) {
      super.load(tag);
      if (tag.contains("PlacedTime")) {
         this.placedTime = tag.getLong("PlacedTime");
      }
   }

   protected void saveAdditional(CompoundTag tag) {
      super.saveAdditional(tag);
      tag.putLong("PlacedTime", this.placedTime);
   }
}
