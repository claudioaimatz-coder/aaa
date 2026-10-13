package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.SlimeWallEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SlimeWallModel extends AnimatedGeoModel<SlimeWallEntity> {
   public ResourceLocation getModelResource(SlimeWallEntity slimeWallEntity) {
      return new ResourceLocation("trbeyond", "geo/slime_wall.geo.json");
   }

   public ResourceLocation getTextureResource(SlimeWallEntity slimeWallEntity) {
      return new ResourceLocation("trbeyond", "textures/entity/slime_wall.png");
   }

   public ResourceLocation getAnimationResource(SlimeWallEntity slimeWallEntity) {
      return new ResourceLocation("trbeyond", "animations/dryad.animation.json");
   }
}
