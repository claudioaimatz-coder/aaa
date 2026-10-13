package io.github.Memoires.trbeyond.core;

import com.github.manasmods.tensura.handler.WeaponEPHandler;
import io.github.Memoires.trbeyond.entity.custom.BossBlackSpiderEntity;
import io.github.Memoires.trbeyond.entity.custom.EvilCentipedeBossEntity;
import io.github.Memoires.trbeyond.entity.custom.GozulEntity;
import io.github.Memoires.trbeyond.entity.custom.MezulEntity;
import io.github.Memoires.trbeyond.entity.custom.OgreLordEntity;
import io.github.Memoires.trbeyond.entity.custom.TempestSerpentBossEntity;
import io.github.Memoires.trbeyond.network.PlayerVariablesHandler;
import io.github.Memoires.trbeyond.world.BeyondGamerules;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WeaponEPHandler.class})
public class MixinWeaponEPHandler {
   @Inject(
      method = {"entityGetEP"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   private static void entityGetEP(LivingEntity living, LivingEntity target, double totalEP, CallbackInfo ci) {
      if (BeyondGamerules.getBoolean(living.level, BeyondGamerules.BOSS_EP_ONE_TIME_DROP)) {
         if (living instanceof Player player) {
            player.getCapability(PlayerVariablesHandler.PLAYER_VARIABLES_CAPABILITY, null)
               .ifPresent(
                  capability -> {
                     if (target instanceof BossBlackSpiderEntity && capability.KilledSpider
                        || target instanceof EvilCentipedeBossEntity && capability.KilledCentipede
                        || target instanceof OgreLordEntity && capability.KilledOgre
                        || target instanceof TempestSerpentBossEntity && capability.KilledSerpent
                        || target instanceof GozulEntity && capability.KilledGozul
                        || target instanceof MezulEntity && capability.KilledMezul) {
                        ci.cancel();
                     }
                  }
               );
         }
      }
   }
}
