package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.misc.MimicChestEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MimicChestModel extends AnimatedGeoModel<MimicChestEntity> {
   public ResourceLocation getModelResource(MimicChestEntity animatable) {
      return animatable.isGolden()
         ? new ResourceLocation("trbeyond", "geo/mimic/gold_mimic.geo.json")
         : new ResourceLocation("trbeyond", "geo/mimic/silver_mimic.geo.json");
   }

   public ResourceLocation getTextureResource(MimicChestEntity animatable) {
      return animatable.isGolden()
         ? new ResourceLocation("trbeyond", "textures/entity/gold_mimic.png")
         : new ResourceLocation("trbeyond", "textures/entity/silver_mimic.png");
   }

   public ResourceLocation getAnimationResource(MimicChestEntity animatable) {
      return animatable.isGolden()
         ? new ResourceLocation("trbeyond", "animations/gold_mimic.animation.json")
         : new ResourceLocation("trbeyond", "animations/silver_mimic.animation.json");
   }
}
