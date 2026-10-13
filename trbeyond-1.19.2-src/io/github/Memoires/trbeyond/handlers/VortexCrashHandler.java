package io.github.Memoires.trbeyond.handlers;

import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.Memoires.trbeyond.client.collision.OBB;
import io.github.Memoires.trbeyond.client.collision.TargetFinder;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import java.util.List;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class VortexCrashHandler {
   public static void useSkillServerSide(ServerPlayer player) {
      ItemStack stack = player.getMainHandItem();
      if (!stack.isEmpty()) {
         if (!SkillHelper.outOfAura(player, 0.0)) {
            player.level.playSound(null, player.blockPosition(), (SoundEvent)BeyondSoundEvents.VORTEX_CRASH.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            VortexCrashHandler.VortexCrashTickHandler handler = new VortexCrashHandler.VortexCrashTickHandler(player, stack);
            MinecraftForge.EVENT_BUS.register(handler);
         }
      }
   }

   public static class VortexCrashTickHandler {
      private final ServerPlayer player;
      private final ItemStack stack;
      private int ticks = 0;

      public VortexCrashTickHandler(ServerPlayer player, ItemStack stack) {
         this.player = player;
         this.stack = stack;
      }

      @SubscribeEvent
      public void onServerTick(ServerTickEvent event) {
         if (event.phase == Phase.END) {
            if (this.player.isRemoved()) {
               MinecraftForge.EVENT_BUS.unregister(this);
            } else {
               this.ticks++;
               if (this.player.level.getBlockState(this.player.getOnPos().below()).getMaterial() == Material.AIR) {
                  Vec3 deltaMovement = new Vec3(this.player.getDeltaMovement().x, 0.0, this.player.getDeltaMovement().z);
                  this.player.setDeltaMovement(deltaMovement);
               }

               TensuraParticleHelper.addServerParticlesAroundSelf(this.player, (ParticleOptions)TensuraParticles.WATER_EFFECT.get());
               if (this.ticks == 49 || this.ticks == 52 || this.ticks == 55 || this.ticks == 61) {
                  TargetFinder.TargetResult result = findAttackTargetResult(this.player, 22.0);

                  for (Entity entity : result.entities) {
                     DamageSource source = DamageSource.mobAttack(this.player).bypassArmor();
                     entity.invulnerableTime = 0;
                     entity.hurt(source, 50.0F);
                     entity.invulnerableTime = 0;
                     entity.hurt(TensuraDamageSources.elementalAttack("tensura.water_attack", this.player, true), 250.0F);
                     entity.invulnerableTime = 0;
                     entity.hurt(TensuraDamageSources.elementalAttack("tensura.water_attack", this.player, true).bypassArmor(), 250.0F);
                  }
               }

               if (this.ticks >= 65) {
                  MinecraftForge.EVENT_BUS.unregister(this);
               }
            }
         }
      }

      public static TargetFinder.TargetResult findAttackTargetResult(ServerPlayer player, double attackRange) {
         Vec3 origin = TargetFinder.getInitialTracingPoint(player);
         List<Entity> entities = TargetFinder.getInitialTargets(player, attackRange);
         Vec3 size = new Vec3(attackRange / 6.0, attackRange / 6.0, attackRange);
         OBB obb = new OBB(origin, size, 0.0F, player.getYRot());
         obb.updateVertex();
         TargetFinder.CollisionFilter collisionFilter = new TargetFinder.CollisionFilter(obb);
         entities = collisionFilter.filter(entities);
         TargetFinder.RadialFilter radialFilter = new TargetFinder.RadialFilter(origin, obb.axisZ, attackRange, (double)player.getYRot());
         entities = radialFilter.filter(entities);
         return new TargetFinder.TargetResult(entities, obb);
      }
   }
}
