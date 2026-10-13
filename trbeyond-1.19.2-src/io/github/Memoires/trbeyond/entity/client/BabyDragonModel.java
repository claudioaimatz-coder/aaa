package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.BabyDragonEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class BabyDragonModel extends AnimatedGeoModel<BabyDragonEntity> {
   public ResourceLocation getAnimationResource(BabyDragonEntity entity) {
      return new ResourceLocation("trbeyond", "animations/baby_dragon.animation.json");
   }

   public ResourceLocation getModelResource(BabyDragonEntity entity) {
      return new ResourceLocation("trbeyond", "geo/baby_dragon.geo.json");
   }

   public ResourceLocation getTextureResource(BabyDragonEntity entity) {
      return new ResourceLocation("trbeyond", "textures/entities/" + entity.getTexture() + ".png");
   }

   public void setCustomAnimations(BabyDragonEntity animatable, int instanceId, AnimationEvent animationEvent) {
      super.setCustomAnimations(animatable, instanceId, animationEvent);
      IBone head = this.getAnimationProcessor().getBone("head");
      EntityModelData extraData = (EntityModelData)animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
      AnimationData manager = animatable.getFactory().getOrCreateAnimationData(instanceId);
      int unpausedMultiplier = Minecraft.getInstance().isPaused() && !manager.shouldPlayWhilePaused ? 0 : 1;
      head.setRotationX(head.getRotationX() + extraData.headPitch * (float) (Math.PI / 180.0) * (float)unpausedMultiplier);
      head.setRotationY(head.getRotationY() + extraData.netHeadYaw * (float) (Math.PI / 180.0) * (float)unpausedMultiplier);
   }
}
