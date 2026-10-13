package io.github.Memoires.trbeyond.block.client;

import com.github.manasmods.tensura.block.state.properties.SmithingBenchPart;
import io.github.Memoires.trbeyond.block.DumbbellRackBlock;
import io.github.Memoires.trbeyond.block.entity.DumbbellRackBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DumbbellRackBlockEntityModel extends AnimatedGeoModel<DumbbellRackBlockEntity> {
   private static final ResourceLocation modelResource = new ResourceLocation("trbeyond", "geo/dumbbell_rack.geo.json");
   private static final ResourceLocation textureResource = new ResourceLocation("trbeyond", "textures/block/dumbbell_rack.png");
   private static final ResourceLocation animationResource = new ResourceLocation("trbeyond", "animations/dumbbell_rack.animation.json");

   public ResourceLocation getAnimationResource(DumbbellRackBlockEntity animatable) {
      return animationResource;
   }

   public ResourceLocation getModelResource(DumbbellRackBlockEntity object) {
      return object.getBlockState().getValue(DumbbellRackBlock.PART) == SmithingBenchPart.BENCH
         ? new ResourceLocation("trbeyond", "geo/empty.geo.json")
         : modelResource;
   }

   public ResourceLocation getTextureResource(DumbbellRackBlockEntity object) {
      return textureResource;
   }
}
