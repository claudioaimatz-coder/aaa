package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.OgreSubEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OgreSubModel extends AnimatedGeoModel<OgreSubEntity> {
   public ResourceLocation getModelResource(OgreSubEntity object) {
      return new ResourceLocation("trbeyond", "geo/ogrelord.geo.json");
   }

   public ResourceLocation getTextureResource(OgreSubEntity object) {
      return new ResourceLocation("trbeyond", "textures/entity/ogrelord.png");
   }

   public ResourceLocation getAnimationResource(OgreSubEntity object) {
      return new ResourceLocation("trbeyond", "animations/ogrelord.animation.json");
   }
}
