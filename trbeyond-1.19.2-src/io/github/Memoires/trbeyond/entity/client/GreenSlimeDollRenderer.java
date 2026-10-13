package io.github.Memoires.trbeyond.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.entity.custom.slime.GreenSlimeDollEntity;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GreenSlimeDollRenderer extends GeoEntityRenderer<GreenSlimeDollEntity> {
   public GreenSlimeDollRenderer(Context renderManager) {
      super(renderManager, new GreenSlimeDollModel());
      this.shadowRadius = 0.3F;
   }

   public ResourceLocation getTextureLocation(GreenSlimeDollEntity animatable) {
      return new ResourceLocation("trbeyond", "textures/entity/green_slime_doll.png");
   }

   public RenderType getRenderType(
      GreenSlimeDollEntity slime,
      float partialTicks,
      PoseStack stack,
      MultiBufferSource renderTypeBuffer,
      VertexConsumer vertexBuilder,
      int packedLightIn,
      ResourceLocation textureLocation
   ) {
      if (slime.getType() == BeyondEntities.GREEN_SLIME_DOLL_MINION.get()) {
         stack.scale(0.5F, 0.5F, 0.5F);
      }

      return RenderType.entityTranslucent(this.getTextureLocation(slime));
   }
}
