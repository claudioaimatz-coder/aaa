package io.github.Memoires.trbeyond.block.client;

import io.github.Memoires.trbeyond.block.entity.DungeonGateBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DungeonGateBlockEntityModel extends AnimatedGeoModel<DungeonGateBlockEntity> {
   private static final ResourceLocation modelResource = new ResourceLocation("trbeyond", "geo/outside_gate.geo.json");
   private static final ResourceLocation textureResource = new ResourceLocation("trbeyond", "textures/block/outside_gate.png");
   private static final ResourceLocation animationResource = new ResourceLocation("trbeyond", "animations/outside_gate.animation.json");

   public ResourceLocation getAnimationResource(DungeonGateBlockEntity animatable) {
      return animationResource;
   }

   public ResourceLocation getModelResource(DungeonGateBlockEntity object) {
      return modelResource;
   }

   public ResourceLocation getTextureResource(DungeonGateBlockEntity object) {
      return textureResource;
   }
}
