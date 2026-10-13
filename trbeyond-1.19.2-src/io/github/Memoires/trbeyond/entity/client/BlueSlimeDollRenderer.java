package io.github.Memoires.trbeyond.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.entity.custom.slime.BlueSlimeDollEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BlueSlimeDollRenderer extends GeoEntityRenderer<BlueSlimeDollEntity> {
   public BlueSlimeDollRenderer(Context renderManager) {
      super(renderManager, new BlueSlimeDollModel());
      this.shadowRadius = 0.3F;
   }

   protected float getDeathMaxRotation(BlueSlimeDollEntity animatable) {
      return 0.0F;
   }

   public ResourceLocation getTextureLocation(BlueSlimeDollEntity animatable) {
      return new ResourceLocation("trbeyond", "textures/entity/blue_slime_doll.png");
   }

   public RenderType getRenderType(
      BlueSlimeDollEntity slime,
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
