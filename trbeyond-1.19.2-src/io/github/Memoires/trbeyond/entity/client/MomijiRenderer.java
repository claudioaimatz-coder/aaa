package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.MomijiEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MomijiRenderer extends GeoEntityRenderer<MomijiEntity> {
   public MomijiRenderer(Context renderManager) {
      super(renderManager, new MomijiModel());
      this.shadowRadius = 0.5F;
   }

   public ResourceLocation getTextureLocation(MomijiEntity instance) {
      return getLocation(instance);
   }

   public static ResourceLocation getLocation(MomijiEntity entity) {
      return new ResourceLocation("trbeyond", "textures/entity/momiji.png");
   }
}
