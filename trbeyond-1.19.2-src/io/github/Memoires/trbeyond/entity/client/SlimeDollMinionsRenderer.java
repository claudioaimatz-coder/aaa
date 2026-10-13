package io.github.Memoires.trbeyond.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.entity.custom.SlimeDollMinions;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class SlimeDollMinionsRenderer extends GeoEntityRenderer<SlimeDollMinions> {
   public SlimeDollMinionsRenderer(Context renderManager) {
      super(renderManager, new SlimeDollMinionsModel());
      this.shadowRadius = 0.5F;
   }

   public RenderType getRenderType(
      SlimeDollMinions entity,
      float partialTicks,
      PoseStack stack,
      MultiBufferSource buffer,
      VertexConsumer vertexBuilder,
      int packedLight,
      ResourceLocation texture
   ) {
      stack.scale(0.6F, 0.6F, 0.6F);
      return RenderType.entityTranslucent(this.getTextureLocation(entity));
   }

   protected float getDeathMaxRotation(SlimeDollMinions entityLivingBaseIn) {
      return 0.0F;
   }
}
