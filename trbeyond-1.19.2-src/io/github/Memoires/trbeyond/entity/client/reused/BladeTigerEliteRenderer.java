package io.github.Memoires.trbeyond.entity.client.reused;

import com.github.manasmods.tensura.entity.BladeTigerEntity;
import com.github.manasmods.tensura.entity.client.BladeTigerModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BladeTigerEliteRenderer extends GeoEntityRenderer<BladeTigerEntity> {
   public BladeTigerEliteRenderer(Context renderManager) {
      super(renderManager, new BladeTigerModel());
      this.shadowRadius = 1.0F;
   }

   public ResourceLocation getTextureLocation(BladeTigerEntity instance) {
      return instance.isWhite()
         ? new ResourceLocation("tensura", "textures/entity/blade_tiger/blade_tiger_white.png")
         : new ResourceLocation("tensura", "textures/entity/blade_tiger/blade_tiger.png");
   }

   public RenderType getRenderType(
      BladeTigerEntity tiger,
      float partialTicks,
      PoseStack stack,
      MultiBufferSource renderTypeBuffer,
      VertexConsumer vertexBuilder,
      int packedLightIn,
      ResourceLocation textureLocation
   ) {
      if (tiger.isBaby()) {
         stack.scale(0.5F, 0.5F, 0.5F);
      }

      return RenderType.entityTranslucent(this.getTextureLocation(tiger));
   }
}
