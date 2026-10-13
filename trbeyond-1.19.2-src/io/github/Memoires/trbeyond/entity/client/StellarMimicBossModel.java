package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.StellarMimicBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StellarMimicBossModel extends AnimatedGeoModel<StellarMimicBossEntity> {
   public ResourceLocation getModelResource(StellarMimicBossEntity animatable) {
      return new ResourceLocation("trbeyond", "geo/stellar_mimic_boss.geo.json");
   }

   public ResourceLocation getTextureResource(StellarMimicBossEntity animatable) {
      return new ResourceLocation("trbeyond", "textures/entity/stellar_mimic_boss.png");
   }

   public ResourceLocation getAnimationResource(StellarMimicBossEntity animatable) {
      return new ResourceLocation("trbeyond", "animations/stellar_mimic_boss.animation.json");
   }
}
