package io.github.Memoires.trbeyond.block.client.geo;

import io.github.Memoires.trbeyond.block.SlimeCeilingBlock;
import io.github.Memoires.trbeyond.block.entity.SlimeCeilingBlockEntity;
import java.util.Arrays;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SlimeCeilingModel extends AnimatedGeoModel<SlimeCeilingBlockEntity> {
   protected static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
      new ResourceLocation("trbeyond", "textures/block/slime_ceiling/slime_ceiling_1.png"),
      new ResourceLocation("trbeyond", "textures/block/slime_ceiling/slime_ceiling_2.png"),
      new ResourceLocation("trbeyond", "textures/block/slime_ceiling/slime_ceiling_3.png"),
      new ResourceLocation("trbeyond", "textures/block/slime_ceiling/slime_ceiling_4.png"),
      new ResourceLocation("trbeyond", "textures/block/slime_ceiling/slime_ceiling_5.png"),
      new ResourceLocation("trbeyond", "textures/block/slime_ceiling/slime_ceiling_6.png"),
      new ResourceLocation("trbeyond", "textures/block/slime_ceiling/slime_ceiling_7.png")
   };

   public ResourceLocation getAnimationResource(SlimeCeilingBlockEntity animatable) {
      return new ResourceLocation("trbeyond", "animations/block/slime_ceiling.animation.json");
   }

   public ResourceLocation getModelResource(SlimeCeilingBlockEntity animatable) {
      return new ResourceLocation("trbeyond", "geo/block/slime_ceiling.geo.json");
   }

   public ResourceLocation getTextureResource(SlimeCeilingBlockEntity entity) {
      return Arrays.stream(TEXTURES).toList().get(entity.tickCount / 3 % TEXTURES.length);
   }

   public void setCustomAnimations(SlimeCeilingBlockEntity slimeCeiling, int instanceId, AnimationEvent customPredicate) {
      super.setCustomAnimations(slimeCeiling, instanceId, customPredicate);
      switch (slimeCeiling.getBlockState().getValue(SlimeCeilingBlock.TYPE)) {
         case 1:
            this.getAnimationProcessor().getBone("Slime_Ceiling1").setHidden(false);
            this.getAnimationProcessor().getBone("Slime_Ceiling2").setHidden(true);
            this.getAnimationProcessor().getBone("Slime_Ceiling3").setHidden(true);
            break;
         case 2:
            this.getAnimationProcessor().getBone("Slime_Ceiling2").setHidden(false);
            this.getAnimationProcessor().getBone("Slime_Ceiling1").setHidden(true);
            this.getAnimationProcessor().getBone("Slime_Ceiling3").setHidden(true);
            break;
         case 3:
            this.getAnimationProcessor().getBone("Slime_Ceiling3").setHidden(false);
            this.getAnimationProcessor().getBone("Slime_Ceiling2").setHidden(true);
            this.getAnimationProcessor().getBone("Slime_Ceiling1").setHidden(true);
      }
   }
}
