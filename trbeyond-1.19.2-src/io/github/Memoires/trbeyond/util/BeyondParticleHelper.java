package io.github.Memoires.trbeyond.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class BeyondParticleHelper {
   public static void spawnParticle(
      Level level, ParticleOptions particle, double xpos, double ypos, double zpos, double xvelocity, double yvelocity, double zvelocity
   ) {
      if (level.isClientSide) {
         level.addParticle(particle, xpos, ypos, zpos, xvelocity, yvelocity, zvelocity);
      }

      if (level instanceof ServerLevel serverLevel) {
         serverLevel.sendParticles(particle, xpos, ypos, zpos, 1, xvelocity, yvelocity, zvelocity, 0.1);
      }
   }
}
