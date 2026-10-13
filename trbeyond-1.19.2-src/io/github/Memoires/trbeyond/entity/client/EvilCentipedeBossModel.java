package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.EvilCentipedeBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EvilCentipedeBossModel extends AnimatedGeoModel<EvilCentipedeBossEntity> {
   public ResourceLocation getModelResource(EvilCentipedeBossEntity object) {
      return new ResourceLocation("trbeyond", "geo/evil_centipede_boss.geo.json");
   }

   public ResourceLocation getTextureResource(EvilCentipedeBossEntity object) {
      return new ResourceLocation("trbeyond", "textures/entity/evil_centipede_boss.png");
   }

   public ResourceLocation getAnimationResource(EvilCentipedeBossEntity object) {
      return new ResourceLocation("trbeyond", "animations/evil_centipede_boss.animation.json");
   }
}
