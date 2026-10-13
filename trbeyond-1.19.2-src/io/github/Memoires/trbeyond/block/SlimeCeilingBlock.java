package io.github.Memoires.trbeyond.block;

import io.github.Memoires.trbeyond.block.entity.SlimeCeilingBlockEntity;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlocks;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SlimeCeilingBlock extends BaseEntityBlock {
   public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 3);
   public static final IntegerProperty TYPE = IntegerProperty.create("type", 1, 3);

   public SlimeCeilingBlock() {
      super(Properties.of(Material.BARRIER).noLootTable().strength(100.0F));
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
      return Shapes.empty();
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{STAGE, TYPE});
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      BlockState state = (BlockState)this.defaultBlockState().setValue(STAGE, 0);
      int type = context.getLevel().random.nextInt(1, 4);
      return (BlockState)state.setValue(TYPE, type);
   }

   public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
      if ((Integer)pState.getValue(STAGE) == 0) {
         activate(pState, pLevel, pPos);
      }
   }

   public static void activate(BlockState pState, Level pLevel, BlockPos pPos) {
      pLevel.setBlockAndUpdate(pPos, (BlockState)pState.setValue(STAGE, 1));
      pLevel.playSound(null, pPos, SoundEvents.DEEPSLATE_BREAK, SoundSource.BLOCKS, 1.0F, 0.8F + pLevel.random.nextFloat() * 0.4F);
      pLevel.playSound(null, pPos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 0.8F + pLevel.random.nextFloat() * 0.4F);

      for (Direction direction : Direction.values()) {
         pLevel.scheduleTick(pPos.relative(direction), (Block)BeyondBlocks.SLIME_CEILING.get(), 3);
      }
   }

   public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
      if ((Integer)pState.getValue(STAGE) != 0) {
         BlockPos blockpos = pPos.above();
         if (pLevel.getBlockState(blockpos).isAir() && !pLevel.getBlockState(blockpos).isSolidRender(pLevel, blockpos)) {
            if (pRandom.nextInt(100) == 0) {
               double d0 = (double)pPos.getX() + pRandom.nextDouble();
               double d1 = (double)pPos.getY() - 4.0;
               double d2 = (double)pPos.getZ() + pRandom.nextDouble();
               pLevel.playLocalSound(
                  d0, d1, d2, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.5F + pRandom.nextFloat() * 0.2F, 0.9F + pRandom.nextFloat() * 0.15F, false
               );
            }

            if (pRandom.nextInt(200) == 0) {
               pLevel.playLocalSound(
                  (double)pPos.getX(),
                  (double)(pPos.getY() - 4),
                  (double)pPos.getZ(),
                  SoundEvents.LAVA_AMBIENT,
                  SoundSource.BLOCKS,
                  0.5F + pRandom.nextFloat() * 0.2F,
                  0.9F + pRandom.nextFloat() * 0.15F,
                  false
               );
            }
         }
      }
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
      return new SlimeCeilingBlockEntity(pPos, pState);
   }

   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> type) {
      return createTickerHelper(type, (BlockEntityType)BeyondBlockEntities.SLIME_CEILING.get(), SlimeCeilingBlockEntity::tick);
   }
}
