package io.github.Memoires.trbeyond.ability.battlewill.utility;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.battlewill.Battewill;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.skill.ExtraSkills;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

public class SkyWalkingArt extends Battewill {
   private static final Set<UUID> TOGGLED_PLAYERS = new HashSet<>();
   private static final Map<UUID, Double> SKY_WALKING_HEIGHT = new HashMap<>();
   private static final Map<UUID, Boolean> LAST_SNEAK_STATE = new HashMap<>();

   @Override
   public double learningCost() {
      return 150.0;
   }

   @Override
   public boolean meetEPRequirement(Player entity, double newEP) {
      return SkillUtils.isSkillMastered(entity, (ManasSkill)ExtraSkills.WIND_DOMINATION.get());
   }

   @Nullable
   @Override
   public ResourceLocation getSkillIcon() {
      return new ResourceLocation("trbeyond", "textures/battlewill/sky_walk.png");
   }

   @Override
   public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) {
      return 100.0;
   }

   @Override
   public double auraCost(LivingEntity entity, ManasSkillInstance instance) {
      return 5.0;
   }

   public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
      if (entity.hasEffect((MobEffect)TensuraMobEffects.MAGIC_INTERFERENCE.get())) {
         if (entity instanceof Player player) {
            player.displayClientMessage(Component.translatable("tensura.skill.magic_interference").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
         }
      } else {
         UUID entityId = entity.getUUID();
         if (TOGGLED_PLAYERS.contains(entityId)) {
            this.disableSkyWalking(entityId, entity);
            if (entity instanceof Player player) {
               player.displayClientMessage(Component.literal("Sky Walking: OFF").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
            }
         } else {
            this.enableSkyWalking(entityId, entity);
            if (entity instanceof Player player) {
               player.displayClientMessage(Component.literal("Sky Walking: ON").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)), true);
            }
         }
      }
   }

   private void enableSkyWalking(UUID entityId, LivingEntity entity) {
      TOGGLED_PLAYERS.add(entityId);
      SKY_WALKING_HEIGHT.put(entityId, entity.getY());
      if (entity instanceof Player player) {
         LAST_SNEAK_STATE.put(entityId, player.isShiftKeyDown());
      }
   }

   private void disableSkyWalking(UUID entityId, LivingEntity entity) {
      TOGGLED_PLAYERS.remove(entityId);
      SKY_WALKING_HEIGHT.remove(entityId);
      LAST_SNEAK_STATE.remove(entityId);
   }

   @SubscribeEvent
   public static void onServerTick(ServerTickEvent event) {
      if (event.phase == Phase.END) {
         Iterator<UUID> toggledIterator = TOGGLED_PLAYERS.iterator();

         while (toggledIterator.hasNext()) {
            UUID playerId = toggledIterator.next();
            Player player = null;

            for (ServerLevel level : event.getServer().m_129785_()) {
               player = level.getPlayerByUUID(playerId);
               if (player != null) {
                  break;
               }
            }

            if (player == null) {
               SKY_WALKING_HEIGHT.remove(playerId);
               LAST_SNEAK_STATE.remove(playerId);
               toggledIterator.remove();
            } else {
               boolean currentlySneak = player.isShiftKeyDown();
               Boolean lastSneak = LAST_SNEAK_STATE.get(playerId);
               if (currentlySneak && (lastSneak == null || !lastSneak)) {
                  Double currentHeight = SKY_WALKING_HEIGHT.get(playerId);
                  if (currentHeight != null) {
                     SKY_WALKING_HEIGHT.put(playerId, currentHeight - 2.0);
                     player.displayClientMessage(Component.literal("Platform lowered!").setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)), true);
                  }
               }

               LAST_SNEAK_STATE.put(playerId, currentlySneak);
            }
         }
      }
   }

   public static boolean isSkyWalkingActive(UUID playerId) {
      return TOGGLED_PLAYERS.contains(playerId);
   }

   public static double getSkyWalkingHeight(UUID playerId) {
      return SKY_WALKING_HEIGHT.getOrDefault(playerId, 0.0);
   }

   public static void updateSkyWalkingHeight(UUID playerId, double newHeight) {
      if (TOGGLED_PLAYERS.contains(playerId)) {
         SKY_WALKING_HEIGHT.put(playerId, newHeight);
      }
   }

   static {
      MinecraftForge.EVENT_BUS.register(SkyWalkingArt.class);
   }
}
