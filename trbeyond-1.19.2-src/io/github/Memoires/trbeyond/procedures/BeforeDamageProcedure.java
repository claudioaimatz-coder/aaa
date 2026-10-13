package io.github.Memoires.trbeyond.procedures;

import io.github.Memoires.trbeyond.TRBeyond;
import io.github.Memoires.trbeyond.entity.custom.GozulEntity;
import io.github.Memoires.trbeyond.network.worldgen.DungeonSaveData;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BeforeDamageProcedure {
   @SubscribeEvent
   public static void onEntityAttacked(LivingHurtEvent event) {
      if (event != null && event.getEntity() != null) {
         execute(event, event.getEntity().level, event.getEntity(), event.getSource().getEntity(), event.getSource());
      }
   }

   private static void execute(@Nullable Event event, LevelAccessor world, Entity entity, Entity sourceentity, DamageSource damageSource) {
      if (entity != null) {
         if (entity instanceof GozulEntity && sourceentity instanceof Player) {
            GozulEntity gozul = (GozulEntity)entity;
            if ("empty".equals(gozul.animationprocedure)) {
               gozul.setAnimation("defense_animation");
               entity.getPersistentData().putBoolean("block", true);
               TRBeyond.queueServerWork(20, () -> {
                  gozul.setAnimation("empty");
                  entity.getPersistentData().putBoolean("block", false);
               });
            }
         }

         if (damageSource == DamageSource.OUT_OF_WORLD && entity instanceof ServerPlayer player && player.getLevel().dimension() == BeyondDungeon.DUNGEON_LEVEL_KEY) {
            ServerLevel overworld = player.server.m_129880_(Level.OVERWORLD);
            ServerLevel dungeon = player.getCommandSenderWorld().getServer().m_129880_(BeyondDungeon.DUNGEON_LEVEL_KEY);
            if (overworld == null) {
               System.out.println("[TRBeyond] Overworld not loaded. Teleport failed.");
               return;
            }

            BlockPos lobbyPos = DungeonSaveData.getOverworld(overworld).getLobbyPos();
            if (lobbyPos == null || lobbyPos.equals(BlockPos.ZERO)) {
               System.out.println("[TRBeyond] Lobby position not set. Teleport canceled.");
               return;
            }

            System.out.println("[TRBeyond] Teleporting to lobby at: " + lobbyPos.toShortString());
            player.teleportTo(
               dungeon,
               (double)lobbyPos.getX() + 0.5,
               (double)lobbyPos.getY(),
               (double)lobbyPos.getZ() + 0.5,
               player.getYRot(),
               player.getXRot()
            );
         }
      }
   }
}
