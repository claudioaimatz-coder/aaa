package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.GraveLizard;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class GraveLizardModel extends AnimatedGeoModel<GraveLizard> {
   public ResourceLocation getModelResource(GraveLizard entity) {
      return new ResourceLocation("trbeyond", "geo/grave_lizard.geo.json");
   }

   public ResourceLocation getTextureResource(GraveLizard entity) {
      return new ResourceLocation("trbeyond", "textures/entity/grave_lizard.png");
   }

   public ResourceLocation getAnimationResource(GraveLizard entity) {
      return new ResourceLocation("trbeyond", "animations/grave_lizard.animation.json");
   }

   public void setCustomAnimations(GraveLizard animatable, int instanceId, AnimationEvent animationEvent) {
      super.setCustomAnimations(animatable, instanceId, animationEvent);
      IBone head = this.getAnimationProcessor().getBone("Head");
      EntityModelData extraData = (EntityModelData)animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
      AnimationData manager = animatable.getFactory().getOrCreateAnimationData(instanceId);
      int unpausedMultiplier = Minecraft.getInstance().isPaused() && !manager.shouldPlayWhilePaused ? 0 : 1;
      head.setRotationX(head.getRotationX() + extraData.headPitch * (float) (Math.PI / 180.0) * (float)unpausedMultiplier);
      head.setRotationY(head.getRotationY() + extraData.netHeadYaw * (float) (Math.PI / 180.0) * (float)unpausedMultiplier);
   }
}
