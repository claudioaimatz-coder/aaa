package io.github.Memoires.trbeyond.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.entity.custom.SlimeDollBossEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class SlimeDollBossRenderer extends GeoEntityRenderer<SlimeDollBossEntity> {
   public SlimeDollBossRenderer(Context renderManager) {
      super(renderManager, new SlimeDollBossModel());
      this.shadowRadius = 0.5F;
   }

   public RenderType getRenderType(
      SlimeDollBossEntity entity,
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

   protected float getDeathMaxRotation(SlimeDollBossEntity entityLivingBaseIn) {
      return 0.0F;
   }
}
