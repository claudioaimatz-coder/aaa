package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.GobweEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class GobweModel extends AnimatedGeoModel<GobweEntity> {
   public ResourceLocation getModelResource(GobweEntity object) {
      return new ResourceLocation("trbeyond", "geo/gobwe.geo.json");
   }

   public ResourceLocation getTextureResource(GobweEntity instance) {
      return GobweRenderer.getLocation(instance);
   }

   public ResourceLocation getAnimationResource(GobweEntity tiger) {
      return new ResourceLocation("trbeyond", "animations/gobwe.animation.json");
   }

   public void setCustomAnimations(GobweEntity entity, int instanceId, AnimationEvent customPredicate) {
      super.setCustomAnimations(entity, instanceId, customPredicate);
      EntityModelData extraData = (EntityModelData)customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
      IBone head = this.getAnimationProcessor().getBone("Head");
      if (head != null) {
         head.setRotationX(extraData.headPitch * (float) (Math.PI / 180.0));
         head.setRotationY(extraData.netHeadYaw * (float) (Math.PI / 180.0));
      }
   }
}
