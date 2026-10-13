package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.BenimaruEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class BenimaruiModel extends AnimatedGeoModel<BenimaruEntity> {
   public ResourceLocation getModelResource(BenimaruEntity object) {
      return new ResourceLocation("trbeyond", "geo/benimaru.geo.json");
   }

   public ResourceLocation getTextureResource(BenimaruEntity instance) {
      return BenimaruRenderer.getLocation(instance);
   }

   public ResourceLocation getAnimationResource(BenimaruEntity tiger) {
      return new ResourceLocation("trbeyond", "animations/benimaru.animation.json");
   }

   public void setCustomAnimations(BenimaruEntity entity, int instanceId, AnimationEvent customPredicate) {
      super.setCustomAnimations(entity, instanceId, customPredicate);
      EntityModelData extraData = (EntityModelData)customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
      IBone head = this.getAnimationProcessor().getBone("Head");
      if (head != null) {
         head.setRotationX(extraData.headPitch * (float) (Math.PI / 180.0));
         head.setRotationY(extraData.netHeadYaw * (float) (Math.PI / 180.0));
      }
   }
}
