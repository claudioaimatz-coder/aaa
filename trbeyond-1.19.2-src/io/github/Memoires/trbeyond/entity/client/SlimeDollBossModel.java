package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.SlimeDollBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SlimeDollBossModel extends AnimatedGeoModel<SlimeDollBossEntity> {
   public ResourceLocation getModelResource(SlimeDollBossEntity entity) {
      return new ResourceLocation("trbeyond", "geo/slime_boss.geo.json");
   }

   public ResourceLocation getTextureResource(SlimeDollBossEntity entity) {
      return new ResourceLocation("trbeyond", "textures/entity/slime_boss_1.png");
   }

   public ResourceLocation getAnimationResource(SlimeDollBossEntity entity) {
      return new ResourceLocation("trbeyond", "animations/slime_boss.animation.json");
   }
}
