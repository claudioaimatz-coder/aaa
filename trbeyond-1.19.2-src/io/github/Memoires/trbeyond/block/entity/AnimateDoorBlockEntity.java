package io.github.Memoires.trbeyond.block.entity;

import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class AnimateDoorBlockEntity extends BlockEntity {
   private static final AABB RENDER_AABB = new AABB(-1.0, -1.0, -1.0, 1.0, 5.0, 1.0);

   public AnimateDoorBlockEntity(BlockPos pos, BlockState state) {
      super((BlockEntityType)BeyondBlockEntities.ANIMATE_DOOR.get(), pos, state);
   }
}
