package io.github.Memoires.trbeyond.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.block.ShrineBlock;
import io.github.Memoires.trbeyond.entity.custom.ShrineEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ShrineEntityRenderer extends GeoEntityRenderer<ShrineEntity> {
   public ShrineEntityRenderer(Context renderManager) {
      super(renderManager, new ShrineEntityModel());
      this.shadowRadius = 0.3F;
   }

   public ResourceLocation getTextureLocation(ShrineEntity animatable) {
      return animatable.getTier() == ShrineBlock.DungeonShrineTier.STELLAR
         ? new ResourceLocation("trbeyond", "textures/block/stellar_shrine.png")
         : new ResourceLocation("trbeyond", "textures/block/gold_shrine.png");
   }

   public RenderType getRenderType(
      ShrineEntity animatable,
      float partialTick,
      PoseStack poseStack,
      @Nullable MultiBufferSource bufferSource,
      @Nullable VertexConsumer buffer,
      int packedLight,
      ResourceLocation texture
   ) {
      return super.getRenderType(animatable, partialTick, poseStack, bufferSource, buffer, packedLight, texture);
   }
}
