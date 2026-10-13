package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.TempestSerpentBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TempestSerpentBossModel extends AnimatedGeoModel<TempestSerpentBossEntity> {
   public ResourceLocation getModelResource(TempestSerpentBossEntity object) {
      return new ResourceLocation("trbeyond", "geo/tempest_serpent_boss.geo.json");
   }

   public ResourceLocation getTextureResource(TempestSerpentBossEntity object) {
      return new ResourceLocation("trbeyond", "textures/entity/tempest_serpent_boss.png");
   }

   public ResourceLocation getAnimationResource(TempestSerpentBossEntity object) {
      return new ResourceLocation("trbeyond", "animations/tempest_serpent_boss.animation.json");
   }
}
