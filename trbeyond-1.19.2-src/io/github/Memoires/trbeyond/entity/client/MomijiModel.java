package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.MomijiEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class MomijiModel extends AnimatedGeoModel<MomijiEntity> {
   public ResourceLocation getModelResource(MomijiEntity object) {
      return new ResourceLocation("trbeyond", "geo/momiji.geo.json");
   }

   public ResourceLocation getTextureResource(MomijiEntity instance) {
      return MomijiRenderer.getLocation(instance);
   }

   public ResourceLocation getAnimationResource(MomijiEntity tiger) {
      return new ResourceLocation("trbeyond", "animations/momiji.animation.json");
   }

   public void setCustomAnimations(MomijiEntity entity, int instanceId, AnimationEvent customPredicate) {
      super.setCustomAnimations(entity, instanceId, customPredicate);
      EntityModelData extraData = (EntityModelData)customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
      IBone head = this.getAnimationProcessor().getBone("Head");
      if (head != null) {
         head.setRotationX(extraData.headPitch * (float) (Math.PI / 180.0));
         head.setRotationY(extraData.netHeadYaw * (float) (Math.PI / 180.0));
      }
   }
}
