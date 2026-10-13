package io.github.Memoires.trbeyond.handlers;

import io.github.Memoires.trbeyond.registry.effects.BeyondEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.FORGE
)
public class StatusEffectHandler {
   @SubscribeEvent
   public static void onAttackEntity(LivingAttackEvent event) {
      LivingEntity entity = event.getEntity();
      if (!entity.hasEffect((MobEffect)BeyondEffects.NO_DODGE.get())) {
         if (entity.hasEffect((MobEffect)BeyondEffects.DODGE.get()) && entity.getRandom().nextFloat() <= 0.5F) {
            event.setCanceled(true);
            entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_WEAK, SoundSource.PLAYERS, 1.0F, 1.0F);
         }
      }
   }
}
