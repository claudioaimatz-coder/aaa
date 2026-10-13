package io.github.Memoires.trbeyond.handlers;

import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import io.github.Memoires.trbeyond.capability.entities.EliteMobCapability;
import io.github.Memoires.trbeyond.world.BeyondGamerules;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.FORGE
)
public class EliteMonsterParticleHandler {
   @SubscribeEvent
   public static void onTick(LivingTickEvent event) {
      LivingEntity entity = event.getEntity();
      if (BeyondGamerules.getBoolean(entity.level, BeyondGamerules.UNIQUE_PARTICLES)) {
         EliteMobCapability.getFrom(entity)
            .ifPresent(
               cap -> {
                  if (cap.isElite()) {
                     TensuraParticleHelper.spawnServerParticles(
                        entity.level,
                        (ParticleOptions)TensuraParticles.PURPLE_LIGHTNING_SPARK.get(),
                        entity.getX(),
                        entity.getY() + 1.0,
                        entity.getZ(),
                        2,
                        0.08,
                        0.08,
                        0.08,
                        0.2,
                        true
                     );
                  }
               }
            );
      }
   }
}
