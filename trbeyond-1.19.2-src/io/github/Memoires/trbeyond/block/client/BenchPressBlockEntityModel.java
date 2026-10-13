package io.github.Memoires.trbeyond.block.client;

import com.github.manasmods.tensura.block.state.properties.SmithingBenchPart;
import io.github.Memoires.trbeyond.block.BenchPressBlock;
import io.github.Memoires.trbeyond.block.entity.BenchPressBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BenchPressBlockEntityModel extends AnimatedGeoModel<BenchPressBlockEntity> {
   private static final ResourceLocation modelResource = new ResourceLocation("trbeyond", "geo/bench_press.geo.json");
   private static final ResourceLocation textureResource = new ResourceLocation("trbeyond", "textures/block/bench_press.png");
   private static final ResourceLocation animationResource = new ResourceLocation("trbeyond", "animations/bench_press.animation.json");

   public ResourceLocation getAnimationResource(BenchPressBlockEntity animatable) {
      return animationResource;
   }

   public ResourceLocation getModelResource(BenchPressBlockEntity object) {
      return object.getBlockState().getValue(BenchPressBlock.PART) == SmithingBenchPart.BENCH
         ? new ResourceLocation("trbeyond", "geo/empty.geo.json")
         : modelResource;
   }

   public ResourceLocation getTextureResource(BenchPressBlockEntity object) {
      return textureResource;
   }
}
