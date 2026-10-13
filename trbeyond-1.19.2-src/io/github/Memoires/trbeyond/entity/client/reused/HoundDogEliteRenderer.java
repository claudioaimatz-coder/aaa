package io.github.Memoires.trbeyond.entity.client.reused;

import com.github.manasmods.tensura.entity.HoundDogEntity;
import com.github.manasmods.tensura.entity.client.HoundDogModel;
import com.github.manasmods.tensura.entity.variant.HoundDogVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class HoundDogEliteRenderer extends GeoEntityRenderer<HoundDogEntity> {
   public HoundDogEliteRenderer(Context renderManager) {
      super(renderManager, new HoundDogModel());
      this.shadowRadius = 1.0F;
   }

   public ResourceLocation getTextureLocation(HoundDogEntity instance) {
      return instance.isSnakeControlled()
         ? new ResourceLocation("tensura", "textures/entity/hound_dog/hound_dog_snake_controlled.png")
         : HoundDogVariant.LOCATION_BY_VARIANT.get(instance.getVariant());
   }

   public RenderType getRenderType(
      HoundDogEntity dog,
      float partialTicks,
      PoseStack stack,
      MultiBufferSource renderTypeBuffer,
      VertexConsumer vertexBuilder,
      int packedLightIn,
      ResourceLocation textureLocation
   ) {
      return RenderType.entityTranslucent(this.getTextureLocation(dog));
   }
}
