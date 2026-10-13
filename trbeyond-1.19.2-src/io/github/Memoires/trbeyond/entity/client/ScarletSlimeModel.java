package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.ScarletSlimeEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ScarletSlimeModel extends AnimatedGeoModel<ScarletSlimeEntity> {
   public ResourceLocation getModelResource(ScarletSlimeEntity object) {
      return new ResourceLocation("trbeyond", "geo/scarlet_slime.geo.json");
   }

   public ResourceLocation getTextureResource(ScarletSlimeEntity object) {
      return new ResourceLocation("trbeyond", "textures/entity/scarlet_slime.png");
   }

   public ResourceLocation getAnimationResource(ScarletSlimeEntity animatable) {
      return new ResourceLocation("trbeyond", "animations/scarlet_slime.animation.json");
   }
}
