package io.github.Memoires.trbeyond.core;

import com.github.manasmods.tensura.effect.skill.AllyBoostEffect;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AllyBoostEffect.class})
public class MixinAllyBoost {
   @Inject(
      method = {"onKillEntity"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   private void injectDungeonCharmCheck(LivingEntity source, LivingDeathEvent e, CallbackInfo ci) {
      LivingEntity target = e.getEntity();
      Level level = target.getLevel();
      if (level.dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
         ci.cancel();
      }
   }
}
