package io.github.Memoires.trbeyond.block.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.Memoires.trbeyond.block.DungeonChestBlock;
import io.github.Memoires.trbeyond.block.entity.DungeonChestBlockEntity;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoubleBlockCombiner.NeighborCombineResult;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.Vec3;

public abstract class DungeonChestRenderer<T extends BlockEntity & LidBlockEntity> implements BlockEntityRenderer<T> {
   private final ResourceLocation BLANK = new ResourceLocation("tensura", "textures/blank_texture.png");
   protected ModelPart lid;
   protected ModelPart bottom;
   protected ModelPart lock;

   public boolean shouldRenderOffScreen(T pBlockEntity) {
      return true;
   }

   public void render(T tileEntityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
      DungeonChestBlockEntity tileEntity = (DungeonChestBlockEntity)tileEntityIn;
      Level level = tileEntity.getLevel();
      BlockState blockState = tileEntity.getBlockState();
      if (blockState.getBlock() instanceof DungeonChestBlock abstractChestBlock) {
         poseStack.pushPose();
         Direction direction = (Direction)blockState.getValue(DungeonChestBlock.FACING);
         float f = direction.toYRot();
         switch (direction) {
            case NORTH:
               poseStack.translate(0.5 - this.getTranslateOffset(blockState), 0.0, 0.5);
               break;
            case SOUTH:
               poseStack.translate(-0.5 + this.getTranslateOffset(blockState), 0.0, -0.5);
               break;
            case WEST:
               poseStack.translate(0.5, 0.0, -0.5 + this.getTranslateOffset(blockState));
               break;
            case EAST:
               poseStack.translate(-0.5, 0.0, 0.5 - this.getTranslateOffset(blockState));
         }

         poseStack.translate(0.5, -0.5, 0.5);
         poseStack.mulPose(Vector3f.YN.rotationDegrees(f));
         poseStack.mulPose(Vector3f.ZN.rotationDegrees(180.0F));
         poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
         poseStack.translate(-0.5, -0.5, -0.5);
         NeighborCombineResult<? extends DungeonChestBlockEntity> neighborCombineResult = abstractChestBlock.combine(
            blockState, level, tileEntityIn.getBlockPos(), true
         );
         float openness = ((Float2FloatFunction)neighborCombineResult.apply(DungeonChestBlock.opennessCombiner(tileEntity))).get(partialTicks);
         openness = 1.0F - openness;
         openness = 1.0F - openness * openness * openness;
         int brightness = ((Int2IntFunction)neighborCombineResult.apply(new BrightnessCombiner())).applyAsInt(combinedLightIn);
         ResourceLocation location = blockState.getValue(DungeonChestBlock.TYPE) != ChestType.LEFT ? this.BLANK : this.getTexture();
         RenderType cameo = RenderType.entityCutout(location);
         this.render(poseStack, bufferSource.getBuffer(cameo), this.lid, this.lock, this.bottom, openness, brightness, combinedOverlayIn);
         poseStack.popPose();
      }
   }

   private double getTranslateOffset(BlockState blockState) {
      return blockState.getValue(DungeonChestBlock.TYPE) != ChestType.LEFT ? 1.0 : 0.0;
   }

   protected abstract ResourceLocation getTexture();

   private void render(
      PoseStack poseStack,
      VertexConsumer vertexConsumer,
      ModelPart lid,
      ModelPart lock,
      ModelPart bottom,
      float openness,
      int brightness,
      int combinedOverlayIn
   ) {
      lid.xRot = -(openness * (float) (Math.PI / 2));
      lock.xRot = lid.xRot;
      lid.render(poseStack, vertexConsumer, brightness, combinedOverlayIn);
      lock.render(poseStack, vertexConsumer, brightness, combinedOverlayIn);
      bottom.render(poseStack, vertexConsumer, brightness, combinedOverlayIn);
   }

   public boolean shouldRender(T tileEntity, Vec3 pCameraPos) {
      if (super.shouldRender(tileEntity, pCameraPos)) {
         return true;
      } else {
         BlockPos pos = tileEntity.getBlockPos().relative(DungeonChestBlock.getConnectedDirection(tileEntity.getBlockState()));
         return Vec3.atCenterOf(pos).closerThan(pCameraPos, (double)this.getViewDistance());
      }
   }
}
