package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.MjollmileEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class MjollmileModel extends AnimatedGeoModel<MjollmileEntity> {
   public ResourceLocation getModelResource(MjollmileEntity entity) {
      return new ResourceLocation("trbeyond", "geo/mjollmile.geo.json");
   }

   public ResourceLocation getTextureResource(MjollmileEntity entity) {
      return new ResourceLocation("trbeyond", "textures/entity/mjollmile.png");
   }

   public ResourceLocation getAnimationResource(MjollmileEntity entity) {
      return new ResourceLocation("trbeyond", "animations/mjollmile.animation.json");
   }

   public void setCustomAnimations(MjollmileEntity entity, int instanceId, AnimationEvent customPredicate) {
      super.setCustomAnimations(entity, instanceId, customPredicate);
      EntityModelData extraData = (EntityModelData)customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
      IBone head = this.getAnimationProcessor().getBone("Head");
      if (head != null) {
         head.setRotationX(extraData.headPitch * (float) (Math.PI / 180.0));
         head.setRotationY(extraData.netHeadYaw * (float) (Math.PI / 180.0));
      }
   }
}
