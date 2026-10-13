package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.MilimSlimeScarletEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MilimSlimeScarletModel extends AnimatedGeoModel<MilimSlimeScarletEntity> {
   public ResourceLocation getAnimationResource(MilimSlimeScarletEntity entity) {
      return new ResourceLocation("trbeyond", "animations/milim_slimescarlet.animation.json");
   }

   public ResourceLocation getModelResource(MilimSlimeScarletEntity entity) {
      return new ResourceLocation("trbeyond", "geo/milim_slimescarlet.geo.json");
   }

   public ResourceLocation getTextureResource(MilimSlimeScarletEntity entity) {
      return new ResourceLocation("trbeyond", "textures/entities/" + entity.getTexture() + ".png");
   }
}
