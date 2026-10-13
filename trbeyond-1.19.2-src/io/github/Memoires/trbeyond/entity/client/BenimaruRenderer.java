package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.BenimaruEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BenimaruRenderer extends GeoEntityRenderer<BenimaruEntity> {
   public BenimaruRenderer(Context renderManager) {
      super(renderManager, new BenimaruiModel());
      this.shadowRadius = 0.5F;
   }

   public ResourceLocation getTextureLocation(BenimaruEntity instance) {
      return getLocation(instance);
   }

   public static ResourceLocation getLocation(BenimaruEntity entity) {
      return new ResourceLocation("trbeyond", "textures/entity/benimaru.png");
   }
}
