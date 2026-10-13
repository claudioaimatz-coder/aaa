package io.github.Memoires.trbeyond.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.entity.custom.slime.YellowSlimeDollEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class YellowSlimeDollRenderer extends GeoEntityRenderer<YellowSlimeDollEntity> {
   public YellowSlimeDollRenderer(Context renderManager) {
      super(renderManager, new YellowSlimeDollModel());
      this.shadowRadius = 0.3F;
   }

   protected float getDeathMaxRotation(YellowSlimeDollEntity animatable) {
      return 0.0F;
   }

   public ResourceLocation getTextureLocation(YellowSlimeDollEntity animatable) {
      return new ResourceLocation("trbeyond", "textures/entity/yellow_slime_doll.png");
   }

   public RenderType getRenderType(
      YellowSlimeDollEntity slime,
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
