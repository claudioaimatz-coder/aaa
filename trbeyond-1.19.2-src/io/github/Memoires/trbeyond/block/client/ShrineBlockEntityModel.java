package io.github.Memoires.trbeyond.block.client;

import io.github.Memoires.trbeyond.block.GoldShrineBlock;
import io.github.Memoires.trbeyond.block.SilverShrineBlock;
import io.github.Memoires.trbeyond.block.StellarShrineBlock;
import io.github.Memoires.trbeyond.block.entity.ShrineBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ShrineBlockEntityModel extends AnimatedGeoModel<ShrineBlockEntity> {
   public ResourceLocation getAnimationResource(ShrineBlockEntity animatable) {
      if (animatable.getBlockState().getBlock() instanceof StellarShrineBlock) {
         return new ResourceLocation("trbeyond", "animations/stellar_shrine.animation.json");
      } else if (animatable.getBlockState().getBlock() instanceof GoldShrineBlock) {
         return new ResourceLocation("trbeyond", "animations/gold_shrine.animation.json");
      } else {
         return animatable.getBlockState().getBlock() instanceof SilverShrineBlock
            ? new ResourceLocation("trbeyond", "animations/silver_shrine.animation.json")
            : new ResourceLocation("trbeyond", "animations/bronze_shrine.animation.json");
      }
   }

   public ResourceLocation getModelResource(ShrineBlockEntity object) {
      if (object.getBlockState().getBlock() instanceof StellarShrineBlock) {
         return new ResourceLocation("trbeyond", "geo/stellar_shrine.geo.json");
      } else if (object.getBlockState().getBlock() instanceof GoldShrineBlock) {
         return new ResourceLocation("trbeyond", "geo/gold_shrine.geo.json");
      } else {
         return object.getBlockState().getBlock() instanceof SilverShrineBlock
            ? new ResourceLocation("trbeyond", "geo/silver_shrine.geo.json")
            : new ResourceLocation("trbeyond", "geo/bronze_shrine.geo.json");
      }
   }

   public ResourceLocation getTextureResource(ShrineBlockEntity object) {
      if (object.getBlockState().getBlock() instanceof StellarShrineBlock) {
         return new ResourceLocation("trbeyond", "textures/block/stellar_shrine.png");
      } else if (object.getBlockState().getBlock() instanceof GoldShrineBlock) {
         return new ResourceLocation("trbeyond", "textures/block/gold_shrine.png");
      } else {
         return object.getBlockState().getBlock() instanceof SilverShrineBlock
            ? new ResourceLocation("trbeyond", "textures/block/silver_shrine.png")
            : new ResourceLocation("trbeyond", "textures/block/bronze_shrine.png");
      }
   }
}
