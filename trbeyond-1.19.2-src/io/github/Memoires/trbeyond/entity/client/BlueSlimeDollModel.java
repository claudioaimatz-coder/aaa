package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.slime.BlueSlimeDollEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BlueSlimeDollModel extends AnimatedGeoModel<BlueSlimeDollEntity> {
   public ResourceLocation getModelResource(BlueSlimeDollEntity BlueSlimeDollEntity) {
      return new ResourceLocation("trbeyond", "geo/blue_slime_doll.geo.json");
   }

   public ResourceLocation getTextureResource(BlueSlimeDollEntity BlueSlimeDollEntity) {
      return new ResourceLocation("trbeyond", "textures/entity/blue_slime_doll.png");
   }

   public ResourceLocation getAnimationResource(BlueSlimeDollEntity BlueSlimeDollEntity) {
      return new ResourceLocation("trbeyond", "animations/blue_slime_doll.animation.json");
   }
}
