package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.KurobeEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class KurobeModel extends AnimatedGeoModel<KurobeEntity> {
   public ResourceLocation getModelResource(KurobeEntity entity) {
      return new ResourceLocation("trbeyond", "geo/kurobe.geo.json");
   }

   public ResourceLocation getTextureResource(KurobeEntity entity) {
      return new ResourceLocation("trbeyond", "textures/entity/kurobe.png");
   }

   public ResourceLocation getAnimationResource(KurobeEntity entity) {
      return new ResourceLocation("trbeyond", "animations/kurobe.animation.json");
   }

   public void setCustomAnimations(KurobeEntity entity, int instanceId, AnimationEvent customPredicate) {
      super.setCustomAnimations(entity, instanceId, customPredicate);
      EntityModelData extraData = (EntityModelData)customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
      IBone head = this.getAnimationProcessor().getBone("Head");
      if (head != null) {
         head.setRotationX(extraData.headPitch * (float) (Math.PI / 180.0));
         head.setRotationY(extraData.netHeadYaw * (float) (Math.PI / 180.0));
      }
   }
}
