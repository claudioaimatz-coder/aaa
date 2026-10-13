package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.slime.YellowSlimeDollEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class YellowSlimeDollModel extends AnimatedGeoModel<YellowSlimeDollEntity> {
   public ResourceLocation getModelResource(YellowSlimeDollEntity YellowSlimeDollEntity) {
      return new ResourceLocation("trbeyond", "geo/yellow_slime_doll.geo.json");
   }

   public ResourceLocation getTextureResource(YellowSlimeDollEntity YellowSlimeDollEntity) {
      return new ResourceLocation("trbeyond", "textures/entity/yellow_slime_doll.png");
   }

   public ResourceLocation getAnimationResource(YellowSlimeDollEntity YellowSlimeDollEntity) {
      return new ResourceLocation("trbeyond", "animations/yellow_slime_doll.animation.json");
   }
}
