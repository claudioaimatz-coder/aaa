package io.github.Memoires.trbeyond.core;

import com.github.manasmods.tensura.effect.template.MobEffectHelper;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({MobEffectHelper.class})
public class MixinMobEffectHelper {
   @Inject(
      method = {"noTeleportation(Lnet/minecraft/world/entity/LivingEntity;)Z"},
      at = {@At("RETURN")},
      remap = false,
      cancellable = true
   )
   private static void noTeleportation(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
      if (!(Boolean)cir.getReturnValue()) {
         if (target.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            cir.setReturnValue(true);
         }
      }
   }
}
