package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.KillerDryadEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class KillerDryadModel extends AnimatedGeoModel<KillerDryadEntity> {
   public ResourceLocation getModelResource(KillerDryadEntity killerDryadEntity) {
      return new ResourceLocation("trbeyond", "geo/dryad.geo.json");
   }

   public ResourceLocation getTextureResource(KillerDryadEntity killerDryadEntity) {
      return new ResourceLocation("trbeyond", "textures/entity/killerdryad.png");
   }

   public ResourceLocation getAnimationResource(KillerDryadEntity killerDryadEntity) {
      return new ResourceLocation("trbeyond", "animations/killerdryad.animation.json");
   }
}
