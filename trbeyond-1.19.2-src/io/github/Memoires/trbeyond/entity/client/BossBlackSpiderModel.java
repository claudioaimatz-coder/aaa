package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.BossBlackSpiderEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class BossBlackSpiderModel extends AnimatedGeoModel<BossBlackSpiderEntity> {
   public ResourceLocation getAnimationResource(BossBlackSpiderEntity animatable) {
      return new ResourceLocation("trbeyond", "animations/boss_black_spider.animation.json");
   }

   public ResourceLocation getModelResource(BossBlackSpiderEntity object) {
      return new ResourceLocation("trbeyond", "geo/boss_black_spider.geo.json");
   }

   public ResourceLocation getTextureResource(BossBlackSpiderEntity object) {
      return BossBlackSpiderRenderer.getSpiderTexture(object);
   }

   public void setCustomAnimations(BossBlackSpiderEntity spider, int instanceId, AnimationEvent customPredicate) {
      super.setCustomAnimations(spider, instanceId, customPredicate);
      this.getAnimationProcessor().getBone("Saddle").setHidden(true);
      EntityModelData extraData = (EntityModelData)customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
      IBone head = this.getAnimationProcessor().getBone("Head");
      if (head != null) {
         head.setRotationX(extraData.headPitch * (float) (Math.PI / 180.0));
         head.setRotationY(extraData.netHeadYaw * (float) (Math.PI / 180.0));
      }
   }
}
