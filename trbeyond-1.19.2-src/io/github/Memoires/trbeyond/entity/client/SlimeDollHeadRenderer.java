package io.github.Memoires.trbeyond.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.entity.custom.SlimeDollHeadEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class SlimeDollHeadRenderer extends GeoProjectilesRenderer<SlimeDollHeadEntity> {
   public SlimeDollHeadRenderer(Context renderManager) {
      super(renderManager, new SlimeDollHeadModel());
      this.shadowRadius = 0.5F;
   }

   public RenderType getRenderType(
      SlimeDollHeadEntity entity,
      float partialTicks,
      PoseStack stack,
      MultiBufferSource buffer,
      VertexConsumer vertexBuilder,
      int packedLight,
      ResourceLocation texture
   ) {
      stack.scale(1.0F, 1.0F, 1.0F);
      return RenderType.entityTranslucent(this.getTextureLocation(entity));
   }
}
