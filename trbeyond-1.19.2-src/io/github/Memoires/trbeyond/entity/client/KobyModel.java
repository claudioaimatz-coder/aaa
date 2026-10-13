package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.KobyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class KobyModel extends AnimatedGeoModel<KobyEntity> {
   public ResourceLocation getModelResource(KobyEntity kobyEntity) {
      return new ResourceLocation("trbeyond", "geo/koby.geo.json");
   }

   public ResourceLocation getTextureResource(KobyEntity kobyEntity) {
      return new ResourceLocation("trbeyond", "textures/entity/koby.png");
   }

   public ResourceLocation getAnimationResource(KobyEntity kobyEntity) {
      return null;
   }

   public void setCustomAnimations(KobyEntity entity, int instanceId, AnimationEvent customPredicate) {
      super.setCustomAnimations(entity, instanceId, customPredicate);
      EntityModelData extraData = (EntityModelData)customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
      IBone head = this.getAnimationProcessor().getBone("Head");
      if (head != null) {
         head.setRotationX(extraData.headPitch * (float) (Math.PI / 180.0));
         head.setRotationY(extraData.netHeadYaw * (float) (Math.PI / 180.0));
      }
   }
}
