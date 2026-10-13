package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.ShionEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ShionRenderer extends GeoEntityRenderer<ShionEntity> {
   public ShionRenderer(Context renderManager) {
      super(renderManager, new ShionModel());
      this.shadowRadius = 0.5F;
   }

   public ResourceLocation getTextureLocation(ShionEntity instance) {
      return getLocation(instance);
   }

   public static ResourceLocation getLocation(ShionEntity entity) {
      return new ResourceLocation("trbeyond", "textures/entity/shion.png");
   }
}
