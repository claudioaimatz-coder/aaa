package io.github.Memoires.trbeyond.block;

import io.github.Memoires.trbeyond.block.entity.DungeonGateBlockEntity;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class DungeonGateBlock extends Block implements EntityBlock {
   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
   private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
   private static final VoxelShape COLLIDE = Block.box(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

   public DungeonGateBlock(Properties properties) {
      super(properties);
   }

   @NotNull
   public VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
      return COLLIDE;
   }

   @NotNull
   public VoxelShape getVisualShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
      return SHAPE;
   }

   @NotNull
   public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
      return SHAPE;
   }

   public BlockState getStateForPlacement(BlockPlaceContext pContext) {
      return (BlockState)this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
   }

   @NotNull
   public BlockState rotate(BlockState pState, Rotation pRotation) {
      return (BlockState)pState.setValue(FACING, pRotation.rotate((Direction)pState.getValue(FACING)));
   }

   @NotNull
   public BlockState mirror(BlockState pState, Mirror pMirror) {
      return pState.rotate(pMirror.getRotation((Direction)pState.getValue(FACING)));
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING});
   }

   public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
      return new DungeonGateBlockEntity(pPos, pState);
   }

   @NotNull
   public RenderShape getRenderShape(@NotNull BlockState state) {
      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
   }

   public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState pOldState, boolean pIsMoving) {
      this.set(
         state, level, pos, (BlockState)((Block)BeyondBlocks.DUNGEON_GATE_TELEPORTER.get()).defaultBlockState().setValue(FACING, (Direction)state.getValue(FACING))
      );
      super.onPlace(state, level, pos, pOldState, pIsMoving);
   }

   public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
      this.set(pState, pLevel, pPos, Blocks.AIR.defaultBlockState());
      super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
   }

   public void set(BlockState pState, Level pLevel, BlockPos pPos, BlockState set) {
      BlockPos pos = pPos;

      for (int i = 0; i < 13; i++) {
         if (i > 0) {
            pos = pos.above();
            pLevel.setBlockAndUpdate(pos, set);
         }

         this.setLeft(pState, pLevel, pos, i, set);
         this.setRight(pState, pLevel, pos, i, set);
      }
   }

   public void setRight(BlockState pState, Level pLevel, BlockPos pPos, int times, BlockState set) {
      BlockPos pos = pPos.relative(((Direction)pState.getValue(FACING)).getCounterClockWise());
      pLevel.setBlockAndUpdate(pos, set);

      for (int i = 0; i < 4; i++) {
         pos = pos.relative(((Direction)pState.getValue(FACING)).getCounterClockWise());
         pLevel.setBlockAndUpdate(pos, set);
         if (times == 16) {
            i += 3;
         } else if (times == 15) {
            i += 2;
         } else if (times >= 13) {
            i++;
         }
      }
   }

   public void setLeft(BlockState pState, Level pLevel, BlockPos pPos, int times, BlockState set) {
      BlockPos pos = pPos.relative(((Direction)pState.getValue(FACING)).getClockWise());
      pLevel.setBlockAndUpdate(pos, set);

      for (int i = 0; i < 4; i++) {
         pos = pos.relative(((Direction)pState.getValue(FACING)).getClockWise());
         pLevel.setBlockAndUpdate(pos, set);
         if (times == 16) {
            i += 3;
         } else if (times == 15) {
            i += 2;
         } else if (times >= 13) {
            i++;
         }
      }
   }
}
