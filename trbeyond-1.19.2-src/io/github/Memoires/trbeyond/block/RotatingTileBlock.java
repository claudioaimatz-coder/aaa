package io.github.Memoires.trbeyond.block;

import io.github.Memoires.trbeyond.block.entity.IllusionaryBlockEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RotatingTileBlock extends BaseEntityBlock {
   public RotatingTileBlock() {
      super(Properties.of(Material.BARRIER).noLootTable().strength(100.0F));
   }

   public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity entity) {
      super.stepOn(pLevel, pPos, pState, entity);
      float speed = 32.0F;
      if (entity instanceof LivingEntity livingEntity) {
         float diff = entity.getYHeadRot() - speed;
         livingEntity.setNoActionTime(20);
         entity.setYBodyRot(diff);
         entity.setYHeadRot(diff);
         entity.hurtMarked = true;
      }

      entity.setYRot(entity.getYRot() - speed);
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
      return Shapes.empty();
   }

   public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
      return Shapes.block();
   }

   public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
      return true;
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
      return new IllusionaryBlockEntity(pPos, pState);
   }
}
