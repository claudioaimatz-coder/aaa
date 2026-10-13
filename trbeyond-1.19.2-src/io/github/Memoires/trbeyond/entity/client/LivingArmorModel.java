package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.LivingArmorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class LivingArmorModel extends AnimatedGeoModel<LivingArmorEntity> {
   public ResourceLocation getAnimationResource(LivingArmorEntity entity) {
      return new ResourceLocation("trbeyond", "animations/living_armor.animation.json");
   }

   public ResourceLocation getModelResource(LivingArmorEntity entity) {
      return new ResourceLocation("trbeyond", "geo/living_armor.geo.json");
   }

   public ResourceLocation getTextureResource(LivingArmorEntity entity) {
      return new ResourceLocation("trbeyond", "textures/entities/" + entity.getTexture() + ".png");
   }

   public void setCustomAnimations(LivingArmorEntity animatable, int instanceId, AnimationEvent animationEvent) {
      super.setCustomAnimations(animatable, instanceId, animationEvent);
      IBone head = this.getAnimationProcessor().getBone("Head");
      EntityModelData extraData = (EntityModelData)animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
      AnimationData manager = animatable.getFactory().getOrCreateAnimationData(instanceId);
      int unpausedMultiplier = Minecraft.getInstance().isPaused() && !manager.shouldPlayWhilePaused ? 0 : 1;
      head.setRotationX(head.getRotationX() + extraData.headPitch * (float) (Math.PI / 180.0) * (float)unpausedMultiplier);
      head.setRotationY(head.getRotationY() + extraData.netHeadYaw * (float) (Math.PI / 180.0) * (float)unpausedMultiplier);
   }
}
