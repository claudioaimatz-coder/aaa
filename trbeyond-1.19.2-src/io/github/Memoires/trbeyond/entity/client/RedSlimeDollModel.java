package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.slime.RedSlimeDollEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RedSlimeDollModel extends AnimatedGeoModel<RedSlimeDollEntity> {
   public ResourceLocation getModelResource(RedSlimeDollEntity RedSlimeDollEntity) {
      return new ResourceLocation("trbeyond", "geo/red_slime_doll.geo.json");
   }

   public ResourceLocation getTextureResource(RedSlimeDollEntity RedSlimeDollEntity) {
      return new ResourceLocation("trbeyond", "textures/entity/red_slime_doll.png");
   }

   public ResourceLocation getAnimationResource(RedSlimeDollEntity RedSlimeDollEntity) {
      return new ResourceLocation("trbeyond", "animations/red_slime_doll.animation.json");
   }
}
