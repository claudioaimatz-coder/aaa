package io.github.Memoires.trbeyond.block.client.geo;

import io.github.Memoires.trbeyond.block.SlimeFloorBlock;
import io.github.Memoires.trbeyond.block.entity.SlimeFloorBlockEntity;
import java.util.Arrays;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SlimeFloorModel extends AnimatedGeoModel<SlimeFloorBlockEntity> {
   protected static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
      new ResourceLocation("trbeyond", "textures/block/slime_floor/slime_floor_1.png"),
      new ResourceLocation("trbeyond", "textures/block/slime_floor/slime_floor_2.png"),
      new ResourceLocation("trbeyond", "textures/block/slime_floor/slime_floor_3.png"),
      new ResourceLocation("trbeyond", "textures/block/slime_floor/slime_floor_4.png"),
      new ResourceLocation("trbeyond", "textures/block/slime_floor/slime_floor_5.png"),
      new ResourceLocation("trbeyond", "textures/block/slime_floor/slime_floor_6.png"),
      new ResourceLocation("trbeyond", "textures/block/slime_floor/slime_floor_7.png")
   };

   public ResourceLocation getAnimationResource(SlimeFloorBlockEntity animatable) {
      return new ResourceLocation("trbeyond", "animations/block/slime_floor.animation.json");
   }

   public ResourceLocation getModelResource(SlimeFloorBlockEntity animatable) {
      return new ResourceLocation("trbeyond", "geo/block/slime_floor.geo.json");
   }

   public ResourceLocation getTextureResource(SlimeFloorBlockEntity entity) {
      return Arrays.stream(TEXTURES).toList().get(entity.tickCount / 5 % TEXTURES.length);
   }

   public void setCustomAnimations(SlimeFloorBlockEntity slimeFloor, int instanceId, AnimationEvent customPredicate) {
      super.setCustomAnimations(slimeFloor, instanceId, customPredicate);
      switch (slimeFloor.getBlockState().getValue(SlimeFloorBlock.TYPE)) {
         case 1:
            this.getAnimationProcessor().getBone("Floor1").setHidden(false);
            this.getAnimationProcessor().getBone("Floor2").setHidden(true);
            this.getAnimationProcessor().getBone("Floor3").setHidden(true);
            break;
         case 2:
            this.getAnimationProcessor().getBone("Floor2").setHidden(false);
            this.getAnimationProcessor().getBone("Floor1").setHidden(true);
            this.getAnimationProcessor().getBone("Floor3").setHidden(true);
            break;
         case 3:
            this.getAnimationProcessor().getBone("Floor3").setHidden(false);
            this.getAnimationProcessor().getBone("Floor2").setHidden(true);
            this.getAnimationProcessor().getBone("Floor1").setHidden(true);
      }
   }
}
