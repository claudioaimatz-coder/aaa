package io.github.Memoires.trbeyond.block;

import com.github.manasmods.tensura.block.SimpleBlock;
import com.github.manasmods.tensura.block.SimpleBlock.BlockProperties;
import com.github.manasmods.tensura.block.state.properties.SmithingBenchPart;
import com.github.manasmods.tensura.block.state.properties.TensuraBlockStateProperties;
import io.github.Memoires.trbeyond.block.entity.DumbbellRackBlockEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DumbbellRackBlock extends SimpleBlock implements SimpleWaterloggedBlock, EntityBlock {
   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   public static final EnumProperty<SmithingBenchPart> PART = TensuraBlockStateProperties.SMITHING_BENCH_PART;
   public static final MutableComponent TITLE = Component.translatable("block.tensura.smithing_bench");

   public DumbbellRackBlock(Material material, BlockProperties properties) {
      super(material, properties);
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH))
               .setValue(PART, SmithingBenchPart.BENCH))
            .setValue(WATERLOGGED, Boolean.FALSE)
      );
   }

   public BlockState updateShape(
      BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos
   ) {
      if ((Boolean)pState.getValue(WATERLOGGED)) {
         pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
      }

      return super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
   }

   public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
      super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
      if (!pLevel.isClientSide()) {
         BlockPos blockpos = this.getOtherPartPosition(pPos, (Direction)pState.getValue(FACING), (SmithingBenchPart)pState.getValue(PART));
         pLevel.setBlock(
            blockpos,
            (BlockState)((BlockState)pState.setValue(PART, SmithingBenchPart.ANVIL)).setValue(WATERLOGGED, this.isWaterAtPosition(pLevel, blockpos)),
            3
         );
         pLevel.blockUpdated(pPos, Blocks.AIR);
         pState.updateNeighbourShapes(pLevel, pPos, 3);
      }
   }

   public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
      return new DumbbellRackBlockEntity(pPos, pState);
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
      return Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
   }

   public static Direction getConnectedDirection(BlockState pState) {
      Direction direction = (Direction)pState.getValue(FACING);
      return pState.getValue(PART) == SmithingBenchPart.BENCH ? direction.getOpposite() : direction;
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext pContext) {
      BlockPos blockpos = pContext.getClickedPos();
      BlockPos other = blockpos.relative(pContext.getHorizontalDirection().getCounterClockWise());
      Level level = pContext.getLevel();
      return level.getWorldBorder().isWithinBounds(other) && level.getBlockState(other).canBeReplaced(pContext)
         ? (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite()))
            .setValue(WATERLOGGED, this.isWaterAtPosition(level, blockpos))
         : null;
   }

   public BlockState rotate(BlockState pState, Rotation pRotation) {
      return (BlockState)pState.setValue(FACING, pRotation.rotate((Direction)pState.getValue(FACING)));
   }

   public BlockState mirror(BlockState pState, Mirror pMirror) {
      return pState.rotate(pMirror.getRotation((Direction)pState.getValue(FACING)));
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
      pBuilder.add(new Property[]{FACING, PART, WATERLOGGED});
   }

   public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
      super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
      if (!pLevel.isClientSide()) {
         Direction direction = (Direction)pState.getValue(FACING);
         BlockPos blockpos = this.getOtherPartPosition(pPos, direction, (SmithingBenchPart)pState.getValue(PART));
         pLevel.setBlockAndUpdate(blockpos, Blocks.AIR.defaultBlockState());
      }
   }

   private BlockPos getOtherPartPosition(BlockPos sourcePos, Direction direction, SmithingBenchPart part) {
      return part == SmithingBenchPart.BENCH ? sourcePos.relative(direction.getClockWise()) : sourcePos.relative(direction.getCounterClockWise());
   }

   private boolean isWaterAtPosition(Level level, BlockPos blockPos) {
      return level.getFluidState(blockPos).is(Fluids.WATER);
   }

   public PushReaction getPistonPushReaction(BlockState pState) {
      return PushReaction.BLOCK;
   }

   public FluidState getFluidState(BlockState pState) {
      return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
   }

   public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
      if (!pLevel.isClientSide()) {
         pPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 0, false, false));
      }

      return InteractionResult.sidedSuccess(pLevel.isClientSide());
   }
}
