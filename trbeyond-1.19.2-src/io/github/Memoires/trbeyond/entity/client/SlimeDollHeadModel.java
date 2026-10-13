package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.SlimeDollHeadEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SlimeDollHeadModel extends AnimatedGeoModel<SlimeDollHeadEntity> {
   public ResourceLocation getModelResource(SlimeDollHeadEntity entity) {
      return new ResourceLocation("trbeyond", "geo/slime_head.geo.json");
   }

   public ResourceLocation getTextureResource(SlimeDollHeadEntity entity) {
      return new ResourceLocation("trbeyond", "textures/entity/slime_boss_1.png");
   }

   public ResourceLocation getAnimationResource(SlimeDollHeadEntity entity) {
      return new ResourceLocation("trbeyond", "animations/slime_boss.animation.json");
   }
}
