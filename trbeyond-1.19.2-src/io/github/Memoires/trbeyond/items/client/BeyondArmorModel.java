package io.github.Memoires.trbeyond.items.client;

import io.github.Memoires.trbeyond.items.armor.BeyondArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BeyondArmorModel extends AnimatedGeoModel<BeyondArmorItem> {
   public ResourceLocation getModelResource(BeyondArmorItem object) {
      return new ResourceLocation("trbeyond", "geo/armor/" + object.getIdForRenderer() + ".geo.json");
   }

   public ResourceLocation getTextureResource(BeyondArmorItem object) {
      return new ResourceLocation("trbeyond", "textures/models/armor/" + object.getIdForRenderer() + ".png");
   }

   public ResourceLocation getAnimationResource(BeyondArmorItem item) {
      return new ResourceLocation("tensura", "animations/armor.animation.json");
   }
}
