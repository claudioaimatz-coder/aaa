package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.OgreLordEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OgreLordModel extends AnimatedGeoModel<OgreLordEntity> {
   public ResourceLocation getModelResource(OgreLordEntity object) {
      return new ResourceLocation("trbeyond", "geo/ogrelord.geo.json");
   }

   public ResourceLocation getTextureResource(OgreLordEntity object) {
      return new ResourceLocation("trbeyond", "textures/entity/ogrelord.png");
   }

   public ResourceLocation getAnimationResource(OgreLordEntity object) {
      return new ResourceLocation("trbeyond", "animations/ogrelord.animation.json");
   }
}
