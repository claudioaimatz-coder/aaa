package io.github.Memoires.trbeyond.block;

import io.github.Memoires.trbeyond.block.entity.RespawnBossBlockEntity;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RespawnBossBlock extends BaseEntityBlock {
   public RespawnBossBlock(Properties properties) {
      super(properties);
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new RespawnBossBlockEntity(pos, state);
   }

   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
      return level.isClientSide ? null : createTickerHelper(type, (BlockEntityType)BeyondBlockEntities.BOSS_SPAWNER.get(), RespawnBossBlockEntity::tick);
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
      return Shapes.empty();
   }

   public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int id, int param) {
      super.triggerEvent(state, world, pos, id, param);
      BlockEntity blockEntity = world.getBlockEntity(pos);
      return blockEntity != null && blockEntity.triggerEvent(id, param);
   }

   public boolean hasAnalogOutputSignal(BlockState state) {
      return false;
   }

   public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
      return 0;
   }
}
