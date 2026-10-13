package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.ShionEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ShionModel extends AnimatedGeoModel<ShionEntity> {
   public ResourceLocation getModelResource(ShionEntity object) {
      return new ResourceLocation("trbeyond", "geo/shion.geo.json");
   }

   public ResourceLocation getTextureResource(ShionEntity instance) {
      return ShionRenderer.getLocation(instance);
   }

   public ResourceLocation getAnimationResource(ShionEntity tiger) {
      return new ResourceLocation("trbeyond", "animations/shion.animation.json");
   }

   public void setCustomAnimations(ShionEntity entity, int instanceId, AnimationEvent customPredicate) {
      super.setCustomAnimations(entity, instanceId, customPredicate);
      IBone swordSheathe = this.getAnimationProcessor().getBone("sword_sheathed");
      IBone swordHand = this.getAnimationProcessor().getBone("sword");
      Boolean swordDrawn = (Boolean)entity.getEntityData().get(ShionEntity.SWORD_DRAWN);
      swordSheathe.setScaleX(swordDrawn ? 0.0F : 1.0F);
      swordSheathe.setScaleY(swordDrawn ? 0.0F : 1.0F);
      swordSheathe.setScaleZ(swordDrawn ? 0.0F : 1.0F);
      swordHand.setScaleX(swordDrawn ? 1.0F : 0.0F);
      swordHand.setScaleY(swordDrawn ? 1.0F : 0.0F);
      swordHand.setScaleZ(swordDrawn ? 1.0F : 0.0F);
      EntityModelData extraData = (EntityModelData)customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
      IBone head = this.getAnimationProcessor().getBone("Head");
      if (head != null) {
         head.setRotationX(extraData.headPitch * (float) (Math.PI / 180.0));
         head.setRotationY(extraData.netHeadYaw * (float) (Math.PI / 180.0));
      }
   }
}
