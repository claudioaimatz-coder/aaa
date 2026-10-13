package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.KingCrawlerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class KingCrawlerModel extends AnimatedGeoModel<KingCrawlerEntity> {
   public ResourceLocation getModelResource(KingCrawlerEntity kingCrawlerEntity) {
      return new ResourceLocation("trbeyond", "geo/centipede.geo.json");
   }

   public ResourceLocation getTextureResource(KingCrawlerEntity kingCrawlerEntity) {
      return new ResourceLocation("trbeyond", "textures/entity/centipede.png");
   }

   public ResourceLocation getAnimationResource(KingCrawlerEntity kingCrawlerEntity) {
      return new ResourceLocation("trbeyond", "animations/centipede.animation.json");
   }
}
