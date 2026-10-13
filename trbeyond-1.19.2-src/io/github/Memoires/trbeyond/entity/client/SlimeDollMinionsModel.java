package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.SlimeDollMinions;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SlimeDollMinionsModel extends AnimatedGeoModel<SlimeDollMinions> {
   public ResourceLocation getModelResource(SlimeDollMinions entity) {
      return new ResourceLocation("trbeyond", "geo/slime_boss.geo.json");
   }

   public ResourceLocation getTextureResource(SlimeDollMinions entity) {
      return new ResourceLocation("trbeyond", "textures/entity/slime_boss_1.png");
   }

   public ResourceLocation getAnimationResource(SlimeDollMinions entity) {
      return new ResourceLocation("trbeyond", "animations/slime_boss.animation.json");
   }
}
