package io.github.Memoires.trbeyond.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.entity.custom.OgreSubEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class OgreSubRenderer extends GeoEntityRenderer<OgreSubEntity> {
   public OgreSubRenderer(Context renderManager) {
      super(renderManager, new OgreSubModel());
      this.shadowRadius = 0.8F;
   }

   @NotNull
   public ResourceLocation getTextureLocation(@NotNull OgreSubEntity ogresub) {
      return new ResourceLocation("trbeyond", "textures/entity/ogrelord.png");
   }

   public RenderType getRenderType(
      OgreSubEntity animatable,
      float partialTick,
      PoseStack stack,
      @Nullable MultiBufferSource renderTypeBuffer,
      @Nullable VertexConsumer vertexBuilder,
      int packedLightIn,
      ResourceLocation texture
   ) {
      stack.scale(0.7F, 0.7F, 0.7F);
      return super.getRenderType(animatable, partialTick, stack, renderTypeBuffer, vertexBuilder, packedLightIn, texture);
   }
}
