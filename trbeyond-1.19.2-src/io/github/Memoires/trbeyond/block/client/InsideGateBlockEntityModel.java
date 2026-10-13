package io.github.Memoires.trbeyond.block.client;

import io.github.Memoires.trbeyond.block.entity.InsideGateBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class InsideGateBlockEntityModel extends AnimatedGeoModel<InsideGateBlockEntity> {
   private static final ResourceLocation modelResource = new ResourceLocation("trbeyond", "geo/inside_gate.geo.json");
   private static final ResourceLocation textureResource = new ResourceLocation("trbeyond", "textures/block/inside_gate.png");
   private static final ResourceLocation animationResource = new ResourceLocation("trbeyond", "animations/inside_gate.animation.json");

   public ResourceLocation getAnimationResource(InsideGateBlockEntity animatable) {
      return animationResource;
   }

   public ResourceLocation getModelResource(InsideGateBlockEntity object) {
      return modelResource;
   }

   public ResourceLocation getTextureResource(InsideGateBlockEntity object) {
      return textureResource;
   }
}
