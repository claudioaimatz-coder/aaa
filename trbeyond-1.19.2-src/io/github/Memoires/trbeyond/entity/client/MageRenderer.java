package io.github.Memoires.trbeyond.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.entity.custom.MageEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MageRenderer extends GeoEntityRenderer<MageEntity> {
   public MageRenderer(Context renderManager) {
      super(renderManager, new MageModel());
      this.shadowRadius = 0.5F;
   }

   public RenderType getRenderType(
      MageEntity entity,
      float partialTicks,
      PoseStack stack,
      MultiBufferSource renderTypeBuffer,
      VertexConsumer vertexBuilder,
      int packedLightIn,
      ResourceLocation textureLocation
   ) {
      stack.scale(1.0F, 1.0F, 1.0F);
      return RenderType.entityTranslucent(this.getTextureLocation(entity));
   }

   protected float getDeathMaxRotation(MageEntity entityLivingBaseIn) {
      return 0.0F;
   }
}
