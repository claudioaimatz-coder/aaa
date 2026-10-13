package io.github.Memoires.trbeyond.procedures;

import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.Memoires.trbeyond.capability.entities.EliteMobProvider;
import io.github.Memoires.trbeyond.capability.entities.IEliteMobCapability;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlocks;
import io.github.Memoires.trbeyond.util.BeyondDamageSources;
import io.github.Memoires.trbeyond.util.PrimedChestSpawnTracker;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class OnPlayerDeathProcedure {
   @SubscribeEvent
   public static void onPlayerKilled(LivingDeathEvent event) {
      if (event.getEntity() instanceof ServerPlayer player) {
         Entity var12 = event.getSource().getEntity();
         Level level = player.level;
         BlockPos feetPos = player.blockPosition();
         BlockPos headPos = feetPos.above();
         Block feetBlock = level.getBlockState(feetPos).getBlock();
         Block headBlock = level.getBlockState(headPos).getBlock();
         System.out.println("[DEBUG DAMAGE SOURCE] " + event.getSource());
         System.out.println("[DEBUG SOURCE ENTITY] " + var12);
         boolean diedFromSuffocation = event.getSource() == TensuraDamageSources.SUFFOCATE;
         if (diedFromSuffocation && (feetBlock == BeyondBlocks.SEALING_AIR.get() || headBlock == BeyondBlocks.SEALING_AIR.get())) {
            awardAdvancement(player, "trbeyond", "lifes_not_fair");
         }

         boolean diedFromParalysis = event.getSource() == BeyondDamageSources.PARALYSIS;
         if (diedFromParalysis && isAnyParalysisChestNearby(level, player.blockPosition(), 3)) {
            awardAdvancement(player, "trbeyond", "better_luck_next_time");
         }

         boolean diedFromExplosion = event.getSource().isExplosion();
         System.out
            .println("[DEBUG Explosion Check] Explosion=" + diedFromExplosion + " | NearChest=" + PrimedChestSpawnTracker.isPrimedChestNear(player, 20.0));
         if (diedFromExplosion && PrimedChestSpawnTracker.isPrimedChestNear(player, 20.0)) {
            awardAdvancement(player, "trbeyond", "a_scary_place");
         }

         if (var12 != null) {
            LazyOptional<IEliteMobCapability> cap = var12.getCapability(EliteMobProvider.ELITE_CAPABILITY);
            cap.ifPresent(eliteData -> {
               if (eliteData.isElite()) {
                  awardAdvancement(player, "trbeyond", "what_was_that");
               }
            });
         }
      }
   }

   private static void awardAdvancement(ServerPlayer player, String namespace, String path) {
      MinecraftServer server = player.getServer();
      if (server != null) {
         ResourceLocation advId = new ResourceLocation(namespace, path);
         Advancement advancement = server.m_129889_().getAdvancement(advId);
         if (advancement != null) {
            AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);

            for (String criterion : progress.getRemainingCriteria()) {
               player.getAdvancements().award(advancement, criterion);
            }

            System.out.println("[DEBUG] Advancement awarded: " + path);
         } else {
            System.out.println("[DEBUG] Advancement not found: " + path);
         }
      }
   }

   private static boolean isAnyParalysisChestNearby(Level level, BlockPos center, int radius) {
      Block[] chestBlocks = new Block[]{
         (Block)BeyondBlocks.GOLD_PARALYSIS_CHEST.get(), (Block)BeyondBlocks.SILVER_PARALYSIS_CHEST.get(), (Block)BeyondBlocks.BRONZE_PARALYSIS_CHEST.get()
      };

      for (int dx = -radius; dx <= radius; dx++) {
         for (int dy = -radius; dy <= radius; dy++) {
            for (int dz = -radius; dz <= radius; dz++) {
               BlockPos checkPos = center.offset(dx, dy, dz);
               Block foundBlock = level.getBlockState(checkPos).getBlock();

               for (Block target : chestBlocks) {
                  if (foundBlock == target) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }
}
