package io.github.Memoires.trbeyond.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.entity.custom.SlimeWallEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class SlimeWallRenderer extends GeoEntityRenderer<SlimeWallEntity> {
   public SlimeWallRenderer(Context renderManager) {
      super(renderManager, new SlimeWallModel());
      this.shadowRadius = 0.3F;
   }

   public ResourceLocation getTextureLocation(SlimeWallEntity animatable) {
      return new ResourceLocation("trbeyond", "textures/entity/slime_wall.png");
   }

   public RenderType getRenderType(
      SlimeWallEntity slime,
      float partialTicks,
      PoseStack stack,
      MultiBufferSource renderTypeBuffer,
      VertexConsumer vertexBuilder,
      int packedLightIn,
      ResourceLocation textureLocation
   ) {
      return RenderType.entityTranslucent(this.getTextureLocation(slime));
   }
}
