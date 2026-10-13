package io.github.Memoires.trbeyond.core;

import com.github.manasmods.tensura.capability.effects.TensuraEffectsCapability;
import com.github.manasmods.tensura.entity.client.layer.ChargedLayer.HumanoidArm;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.Memoires.trbeyond.registry.items.BeyondToolItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({HumanoidArm.class})
public abstract class MixinChargedLayer {
   @Inject(
      method = {"getChargingTexture"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   private void injectGuillotineCondition(LivingEntity entity, CallbackInfoReturnable<ResourceLocation> cir) {
      if (TensuraEffectsCapability.hasSyncedEffect(entity, (MobEffect)TensuraMobEffects.OGRE_GUILLOTINE.get())) {
         ItemStack mainHand = entity.getMainHandItem();
         ItemStack offHand = entity.getOffhandItem();
         boolean hasGorikimaru = mainHand.is((Item)BeyondToolItems.GORIKIMARU_UNIQUE.get())
            || offHand.is((Item)BeyondToolItems.GORIKIMARU_UNIQUE.get())
            || mainHand.is((Item)BeyondToolItems.GORIKIMARU_LEGEND.get())
            || offHand.is((Item)BeyondToolItems.GORIKIMARU_LEGEND.get())
            || mainHand.is((Item)BeyondToolItems.GORIKIMARU_GOD.get())
            || offHand.is((Item)BeyondToolItems.GORIKIMARU_GOD.get());
         if (!hasGorikimaru) {
            cir.setReturnValue(new ResourceLocation("tensura", "textures/models/layer/guillotine.png"));
         } else {
            cir.setReturnValue(new ResourceLocation("trbeyond", "textures/models/layer/void.png"));
         }
      }
   }
}
