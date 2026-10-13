package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.block.ShrineBlock;
import io.github.Memoires.trbeyond.entity.custom.ShrineEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ShrineEntityModel extends AnimatedGeoModel<ShrineEntity> {
   public ResourceLocation getModelResource(ShrineEntity animatable) {
      return animatable.getTier() == ShrineBlock.DungeonShrineTier.STELLAR
         ? new ResourceLocation("trbeyond", "geo/stellar_shrine.geo.json")
         : new ResourceLocation("trbeyond", "geo/gold_shrine.geo.json");
   }

   public ResourceLocation getTextureResource(ShrineEntity animatable) {
      return animatable.getTier() == ShrineBlock.DungeonShrineTier.STELLAR
         ? new ResourceLocation("trbeyond", "textures/block/stellar_shrine.png")
         : new ResourceLocation("trbeyond", "textures/block/gold_shrine.png");
   }

   public ResourceLocation getAnimationResource(ShrineEntity animatable) {
      return animatable.getTier() == ShrineBlock.DungeonShrineTier.STELLAR
         ? new ResourceLocation("trbeyond", "animations/stellar_shrine.animation.json")
         : new ResourceLocation("trbeyond", "animations/gold_shrine.animation.json");
   }
}
