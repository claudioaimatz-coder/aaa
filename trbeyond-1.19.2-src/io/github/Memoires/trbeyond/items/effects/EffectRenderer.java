package io.github.Memoires.trbeyond.items.effects;

import io.github.Memoires.trbeyond.registry.effects.BeyondAnimatedEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class EffectRenderer extends GeoItemRenderer<EffectItem> {
   private ItemStack itemProvider;

   public EffectRenderer(AnimatedGeoModel modelProvider) {
      super(modelProvider);
   }

   public void setItem(AnimatedEffectWeapon.Effect effect) {
      this.itemProvider = switch (effect) {
         case NONE -> null;
         case MINOS_TRIDENT_UNIQUE, MINOS_TRIDENT_GOD, MINOS_TRIDENT_LEGEND -> new ItemStack((ItemLike)BeyondAnimatedEffects.MINOS_TRIDENT_EFFECT.get());
         case GUREN -> new ItemStack((ItemLike)BeyondAnimatedEffects.GUREN_EFFECT.get());
         case GORIKIMARU -> new ItemStack((ItemLike)BeyondAnimatedEffects.GORIKIMARU_EFFECT.get());
         case GABIRU -> new ItemStack((ItemLike)BeyondAnimatedEffects.GABIRU_EFFECT.get());
         case HAKURO_SWORD -> new ItemStack((ItemLike)BeyondAnimatedEffects.HARUKO_SWORD_EFFECT.get());
         case GELDS -> new ItemStack((ItemLike)BeyondAnimatedEffects.GELDS_EFFECT.get());
      };
   }

   public ItemStack getItemStack() {
      return this.itemProvider;
   }
}
