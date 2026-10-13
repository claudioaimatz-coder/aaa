package io.github.Memoires.trbeyond.items.effects;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EffectModel extends AnimatedGeoModel<EffectItem> {
   private final AnimatedTexture texture;
   private final ResourceLocation model;
   private final ResourceLocation animation;

   public EffectModel(AnimatedEffectWeapon.Effect effect) {
      this.texture = new AnimatedTexture("trbeyond", effect.getTexture(), effect.getNumber_of_frames(), 100);
      this.model = new ResourceLocation("trbeyond", effect.getModel());
      this.animation = new ResourceLocation("trbeyond", effect.getAnimation());
   }

   public ResourceLocation getModelResource(EffectItem object) {
      return this.model;
   }

   public ResourceLocation getTextureResource(EffectItem object) {
      return this.texture.getTexture();
   }

   public ResourceLocation getAnimationResource(EffectItem animatable) {
      return this.animation;
   }
}
