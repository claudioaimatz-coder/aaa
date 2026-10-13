package io.github.Memoires.trbeyond.entity.client.misc;

import io.github.Memoires.trbeyond.entity.custom.TrainingDummyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TrainingDummyModel extends AnimatedGeoModel<TrainingDummyEntity> {
   public ResourceLocation getModelResource(TrainingDummyEntity trainingDummy) {
      return new ResourceLocation("trbeyond", "geo/training_dummy.geo.json");
   }

   public ResourceLocation getTextureResource(TrainingDummyEntity trainingDummy) {
      return new ResourceLocation("trbeyond", "textures/entity/training_dummy.png");
   }

   public ResourceLocation getAnimationResource(TrainingDummyEntity trainingDummy) {
      return new ResourceLocation("trbeyond", "animations/training_dummy.animation.json");
   }
}
