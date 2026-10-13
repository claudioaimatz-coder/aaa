package io.github.Memoires.trbeyond.block.client;

import io.github.Memoires.trbeyond.block.DungeonChestBlock;
import io.github.Memoires.trbeyond.block.entity.StellarChestBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.ChestType;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StellarChestBlockEntityModel extends AnimatedGeoModel<StellarChestBlockEntity> {
   private static final ResourceLocation modelResource = new ResourceLocation("trbeyond", "geo/stellar_chest.geo.json");
   private static final ResourceLocation textureResource = new ResourceLocation("trbeyond", "textures/block/stellar_chest.png");
   private static final ResourceLocation animationResource = new ResourceLocation("trbeyond", "animations/stellar_chest.animation.json");

   public ResourceLocation getAnimationResource(StellarChestBlockEntity animatable) {
      return animationResource;
   }

   public ResourceLocation getModelResource(StellarChestBlockEntity object) {
      return object.getBlockState().getValue(DungeonChestBlock.TYPE) == ChestType.RIGHT ? new ResourceLocation("trbeyond", "geo/empty.geo.json") : modelResource;
   }

   public ResourceLocation getTextureResource(StellarChestBlockEntity object) {
      return textureResource;
   }
}
