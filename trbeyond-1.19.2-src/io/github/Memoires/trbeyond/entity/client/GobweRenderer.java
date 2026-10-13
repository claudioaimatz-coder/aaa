package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.GobweEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GobweRenderer extends GeoEntityRenderer<GobweEntity> {
   public GobweRenderer(Context renderManager) {
      super(renderManager, new GobweModel());
      this.shadowRadius = 0.5F;
   }

   public ResourceLocation getTextureLocation(GobweEntity instance) {
      return getLocation(instance);
   }

   public static ResourceLocation getLocation(GobweEntity entity) {
      return new ResourceLocation("trbeyond", "textures/entity/gobwe.png");
   }
}
