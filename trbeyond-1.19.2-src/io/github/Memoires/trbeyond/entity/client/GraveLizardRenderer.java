package io.github.Memoires.trbeyond.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.entity.custom.GraveLizard;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GraveLizardRenderer extends GeoEntityRenderer<GraveLizard> {
   public GraveLizardRenderer(Context ctx) {
      super(ctx, new GraveLizardModel());
      this.shadowRadius = 0.5F;
   }

   public RenderType getRenderType(
      GraveLizard entity,
      float partialTicks,
      PoseStack stack,
      MultiBufferSource renderTypeBuffer,
      VertexConsumer vertexBuilder,
      int packedLightIn,
      ResourceLocation textureLocation
   ) {
      stack.scale(0.4F, 0.4F, 0.4F);
      return RenderType.entityTranslucent(this.getTextureLocation(entity));
   }
}
