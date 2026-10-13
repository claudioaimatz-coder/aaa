package io.github.Memoires.trbeyond.handlers;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.skill.ExtraSkills;
import com.github.manasmods.tensura.registry.skill.ResistanceSkills;
import io.github.Memoires.trbeyond.data.BeyondTags;
import io.github.Memoires.trbeyond.entity.custom.BossBlackSpiderEntity;
import io.github.Memoires.trbeyond.entity.custom.EvilCentipedeBossEntity;
import io.github.Memoires.trbeyond.entity.custom.GozulEntity;
import io.github.Memoires.trbeyond.entity.custom.MezulEntity;
import io.github.Memoires.trbeyond.entity.custom.OgreLordEntity;
import io.github.Memoires.trbeyond.entity.custom.TempestSerpentBossEntity;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.world.BeyondGamerules;
import java.util.Random;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(
   modid = "trbeyond"
)
public class BeyondBossHandler {
   @SubscribeEvent
   public static void applyPresenceSence(LivingTickEvent event) {
      if (event.getEntity().getType().getTags().toList().contains(BeyondTags.DUNGEON_BOSS)) {
         event.getEntity().addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.PRESENCE_SENSE.get(), 720000, 10, false, false, false));
      }
   }

   @SubscribeEvent
   public static void setEp(LivingSpawnEvent event) {
      LivingEntity entity = event.getEntity();
      Random random = new Random();
      if (entity instanceof EvilCentipedeBossEntity) {
         int min = 80000;
         int max = 90000;
         double ep = random.nextDouble((double)min, (double)max);
         TensuraEPCapability.setLivingEP(entity, ep);
         TensuraEPCapability.setCurrentLivingEP(entity, TensuraEPCapability.getEP(entity));
         TensuraEPCapability.sync(entity);
      } else if (entity instanceof GozulEntity || entity instanceof MezulEntity) {
         TensuraEPCapability.setLivingEP(entity, 130000.0);
         TensuraEPCapability.setCurrentLivingEP(entity, TensuraEPCapability.getEP(entity));
         TensuraEPCapability.sync(entity);
         SkillUtils.learnSkill(entity, (ManasSkill)ExtraSkills.ULTRASPEED_REGENERATION.get());
         ManasSkillInstance instance = SkillUtils.getSkillOrNull(entity, (ManasSkill)ExtraSkills.ULTRASPEED_REGENERATION.get());
         if (instance != null && instance.canBeToggled(entity)) {
            instance.setToggled(true);
         }
      } else if (entity instanceof TempestSerpentBossEntity) {
         int min = 90000;
         int max = 100000;
         double ep = random.nextDouble((double)min, (double)max);
         TensuraEPCapability.setLivingEP(entity, ep);
         TensuraEPCapability.setCurrentLivingEP(entity, TensuraEPCapability.getEP(entity));
         TensuraEPCapability.sync(entity);
      } else if (entity instanceof OgreLordEntity) {
         TensuraEPCapability.setLivingEP(entity, 50000.0);
         TensuraEPCapability.setCurrentLivingEP(entity, TensuraEPCapability.getEP(entity));
         TensuraEPCapability.sync(entity);
         SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.FLAME_ATTACK_RESISTANCE.get());
         ManasSkillInstance instance = SkillUtils.getSkillOrNull(entity, (ManasSkill)ResistanceSkills.FLAME_ATTACK_RESISTANCE.get());
         if (instance != null && instance.canBeToggled(entity)) {
            instance.setToggled(true);
         }
      }
   }

   @SubscribeEvent
   public static void onDamage(LivingDamageEvent event) {
      Entity attacker = event.getSource().getEntity();
      if (attacker != null
         && attacker.level.getGameRules().getBoolean(BeyondGamerules.NIGHTMARE_MODE)
         && attacker instanceof LivingEntity boss
         && (
            boss instanceof BossBlackSpiderEntity
               || boss instanceof EvilCentipedeBossEntity
               || boss instanceof TempestSerpentBossEntity
               || boss instanceof GozulEntity
               || boss instanceof MezulEntity
               || boss instanceof OgreLordEntity
         )) {
         Entity target = event.getEntity();
         if (target instanceof LivingEntity livingEntity) {
            SkillHelper.addEffectWithSource(livingEntity, boss, (MobEffect)TensuraMobEffects.ANTI_SKILL.get(), 10, 0);
         }
      }
   }

   public static boolean isBoss(LivingEntity entity) {
      return entity instanceof EvilCentipedeBossEntity
         || entity instanceof TempestSerpentBossEntity
         || entity instanceof BossBlackSpiderEntity
         || entity instanceof GozulEntity
         || entity instanceof MezulEntity
         || entity instanceof OgreLordEntity;
   }

   public static boolean isBossUnique(LivingEntity entity) {
      return entity instanceof EvilCentipedeBossEntity
         || entity instanceof TempestSerpentBossEntity
         || entity instanceof GozulEntity
         || entity instanceof MezulEntity
         || entity instanceof OgreLordEntity;
   }

   @SubscribeEvent
   public static void onPlayerTick(PlayerTickEvent event) {
      if (event.phase == Phase.END) {
         Player player = event.player;
         if (player == null) {
            return;
         }

         if (player.isCreative() || player.isSpectator()) {
            return;
         }

         if (player.level.getGameRules().getBoolean(BeyondGamerules.NIGHTMARE_MODE)) {
            for (Entity entity : player.level
               .getEntities(
                  player,
                  new AABB(
                     player.getX() - 30.0,
                     player.getY() - 30.0,
                     player.getZ() - 30.0,
                     player.getX() + 30.0,
                     player.getY() + 30.0,
                     player.getZ() + 30.0
                  )
               )) {
               if (entity instanceof LivingEntity) {
                  LivingEntity livingEntity = (LivingEntity)entity;
                  if (isBoss(livingEntity)) {
                     if (player.getAbilities().mayfly) {
                        player.getAbilities().mayfly = false;
                        player.getAbilities().flying = false;
                        player.onUpdateAbilities();
                     }

                     SkillHelper.checkThenAddEffectSource(player, livingEntity, (MobEffect)TensuraMobEffects.ANTI_SKILL.get(), 3, 0, true, true, true);
                  }
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onEntityBreaks(LivingDestroyBlockEvent event) {
      LivingEntity livingEntity = event.getEntity();
      if (livingEntity.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
         event.setCanceled(true);
      }
   }
}
