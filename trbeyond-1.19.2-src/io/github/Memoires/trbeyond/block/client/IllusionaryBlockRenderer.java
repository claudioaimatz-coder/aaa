package io.github.Memoires.trbeyond.block.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.Memoires.trbeyond.block.ThreadBlock;
import io.github.Memoires.trbeyond.block.entity.IllusionaryBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class IllusionaryBlockRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
   protected final BlockRenderDispatcher blockRenderer;
   private static final Class<? extends Block> STEEL_THREADS_BLOCK = ThreadBlock.class;

   public IllusionaryBlockRenderer(Context context) {
      this.blockRenderer = context.getBlockRenderDispatcher();
   }

   public void render(T blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
      Level level = blockEntity.getLevel();
      if (level != null) {
         BlockPos blockPos = blockEntity.getBlockPos();
         BlockPos copyPos = this.findCopyPos(level, blockPos, Direction.DOWN);
         if (copyPos == null) {
            copyPos = this.findCopyPos(level, blockPos, Direction.UP);
         }

         if (copyPos == null) {
            for (Direction direction : Direction.values()) {
               if (direction.getAxis() != Axis.Y) {
                  copyPos = this.findCopyPos(level, blockPos, direction);
                  if (copyPos != null) {
                     break;
                  }
               }
            }
         }

         if (copyPos != null) {
            BlockState copyState = level.getBlockState(copyPos);
            if (copyState.getBlock().getClass() != STEEL_THREADS_BLOCK) {
               poseStack.pushPose();
               this.blockRenderer.renderBatched(copyState, blockPos, level, poseStack, bufferSource.getBuffer(RenderType.translucent()), false, level.getRandom());
               poseStack.popPose();
            }
         }
      }
   }

   private BlockPos findCopyPos(Level level, BlockPos pos, Direction direction) {
      BlockPos illusionaryPos = pos.relative(direction);
      if (level.getBlockState(illusionaryPos).isAir()) {
         return null;
      } else {
         while (level.getBlockEntity(illusionaryPos) instanceof IllusionaryBlockEntity) {
            illusionaryPos = illusionaryPos.relative(direction);
         }

         BlockState targetState = level.getBlockState(illusionaryPos);
         return !targetState.isAir() && targetState.getBlock().getClass() != STEEL_THREADS_BLOCK ? illusionaryPos : null;
      }
   }
}
