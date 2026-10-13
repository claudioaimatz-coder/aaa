package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.slime.GreenSlimeDollEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GreenSlimeDollModel extends AnimatedGeoModel<GreenSlimeDollEntity> {
   public ResourceLocation getModelResource(GreenSlimeDollEntity RedSlimeDollEntity) {
      return new ResourceLocation("trbeyond", "geo/red_slime_doll.geo.json");
   }

   public ResourceLocation getTextureResource(GreenSlimeDollEntity RedSlimeDollEntity) {
      return new ResourceLocation("trbeyond", "textures/entity/green_slime_doll.png");
   }

   public ResourceLocation getAnimationResource(GreenSlimeDollEntity RedSlimeDollEntity) {
      return new ResourceLocation("trbeyond", "animations/red_slime_doll.animation.json");
   }
}
