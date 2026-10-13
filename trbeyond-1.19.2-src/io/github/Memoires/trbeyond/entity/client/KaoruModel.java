package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.KaoruEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class KaoruModel extends AnimatedGeoModel<KaoruEntity> {
   public ResourceLocation getModelResource(KaoruEntity kaoruEntity) {
      return new ResourceLocation("trbeyond", "geo/kaoru.geo.json");
   }

   public ResourceLocation getTextureResource(KaoruEntity kaoruEntity) {
      return new ResourceLocation("trbeyond", "textures/entity/kaoru.png");
   }

   public ResourceLocation getAnimationResource(KaoruEntity kaoruEntity) {
      return new ResourceLocation("trbeyond", "animations/dryad.animation.json");
   }

   public void setCustomAnimations(KaoruEntity entity, int instanceId, AnimationEvent customPredicate) {
      super.setCustomAnimations(entity, instanceId, customPredicate);
      EntityModelData extraData = (EntityModelData)customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
      IBone head = this.getAnimationProcessor().getBone("Head");
      if (head != null) {
         head.setRotationX(extraData.headPitch * (float) (Math.PI / 180.0));
         head.setRotationY(extraData.netHeadYaw * (float) (Math.PI / 180.0));
      }
   }
}
