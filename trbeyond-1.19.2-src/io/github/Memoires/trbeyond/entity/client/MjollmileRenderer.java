package io.github.Memoires.trbeyond.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.entity.custom.MjollmileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MjollmileRenderer extends GeoEntityRenderer<MjollmileEntity> {
   public MjollmileRenderer(Context renderManager) {
      super(renderManager, new MjollmileModel());
      this.shadowRadius = 0.3F;
   }

   public ResourceLocation getTextureLocation(MjollmileEntity animatable) {
      return new ResourceLocation("trbeyond", "textures/entity/mjollmile.png");
   }

   public RenderType getRenderType(
      MjollmileEntity animatable,
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
