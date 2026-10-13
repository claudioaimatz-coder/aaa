package io.github.Memoires.trbeyond.handlers;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.ep.ITensuraEPCapability;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.data.TensuraTags.EntityTypes;
import com.github.manasmods.tensura.data.pack.EntityEPCount;
import com.github.manasmods.tensura.data.pack.TensuraData;
import com.github.manasmods.tensura.entity.human.CloneEntity;
import com.github.manasmods.tensura.handler.EntityEPHandler;
import com.github.manasmods.tensura.menu.RaceSelectionMenu;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.skill.ExtraSkills;
import com.github.manasmods.tensura.registry.skill.UniqueSkills;
import io.github.Memoires.trbeyond.capability.entities.EliteMobCapability;
import io.github.Memoires.trbeyond.data.BeyondTags;
import io.github.Memoires.trbeyond.entity.custom.DryadEntity;
import io.github.Memoires.trbeyond.entity.custom.GuildLadyEntity;
import io.github.Memoires.trbeyond.entity.custom.KaijinEntity;
import io.github.Memoires.trbeyond.entity.custom.KaoruEntity;
import io.github.Memoires.trbeyond.entity.custom.KobyEntity;
import io.github.Memoires.trbeyond.entity.custom.MjollmileEntity;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import io.github.Memoires.trbeyond.world.BeyondGamerules;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.FORGE
)
public class BeyondEntityEPHandler extends EntityEPHandler {
   private static final String[] UNIQUE_SKILL = new String[]{
      "OBSERVER",
      "REAPER",
      "SEER",
      "FIGHTER",
      "SHADOW_STRIKER",
      "SNIPER",
      "SURVIVOR",
      "TUNER",
      "GOURMAND",
      "ANTI_SKILL",
      "CHOSEN_ONE",
      "COMMANDER",
      "COOK",
      "ENGORGER",
      "GUARDIAN",
      "MARTIAL_MASTER",
      "MATHEMATICIAN"
   };
   private static final String[] EXTRA_SKILL = new String[]{
      "ALL_SEEING_EYE", "STRENGTHEN_BODY", "STEEL_STRENGTH", "BLACK_FLAME", "ULTRA_INSTINCT", "HEAVENLY_EYE", "MAGIC_JAMMING", "ULTRA_REGENERATION"
   };
   private static final Map<String, RegistryObject<? extends Skill>> UNIQUE_SKILL_MAP = new HashMap<>();
   private static final Map<String, RegistryObject<? extends Skill>> EXTRA_SKILL_MAP = new HashMap<>();

   @SubscribeEvent(
      priority = EventPriority.LOWEST
   )
   public static void onEntitySpawn(EntityJoinLevelEvent event) {
      if (!event.getLevel().isClientSide()) {
         Entity entity = event.getEntity();
         if (entity instanceof LivingEntity living) {
            if (living.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)
               || BeyondGamerules.getBoolean(entity.getLevel(), BeyondGamerules.UNIQUE_OVERWORLD)) {
               if (!(entity instanceof CloneEntity)) {
                  if (living instanceof TamableAnimal tame) {
                     LivingEntity owner = tame.getOwner();
                     if (owner != null) {
                        return;
                     }
                  }

                  if (!living.getType().is(BeyondTags.NOT_ELITE) && !(living instanceof Player)) {
                     if (!BeyondBossHandler.isBossUnique(living)) {
                        int chancePercent = living.getLevel().getGameRules().getInt(BeyondGamerules.UNIQUE_MONSTER_CHANCE);
                        float chance = (float)chancePercent / 1000.0F;
                        if (living.getRandom().nextFloat() > chance
                           && !living.getPersistentData().contains("spawnElite")
                           && !living.getPersistentData().contains("challengeElite")) {
                           return;
                        }
                     }

                     TensuraEPCapability.getFrom(living).ifPresent(cap -> EliteMobCapability.getFrom(living).ifPresent(cap1 -> {
                           if (!cap1.isElite()) {
                              cap1.setElite(true);
                              EliteMobCapability.sync(living);
                              applyRandomSkillLogic(living);
                              double energy = 0.0;
                              boolean epSet = false;

                              for (EntityEPCount entityEP : TensuraData.getEntityEP()) {
                                 if (entityEP.getEntity().equals(EntityType.getKey(living.getType()))) {
                                    int minEP = entityEP.getMinEP();
                                    int maxEP = Math.max(minEP + 1, entityEP.getMaxEP() + 1);
                                    double randomEP = (double)living.getRandom().nextInt(0, maxEP - minEP);
                                    AttributeInstance magicule = living.getAttribute((Attribute)TensuraAttributeRegistry.MAX_MAGICULE.get());
                                    if (magicule != null) {
                                       magicule.setBaseValue((double)minEP / 2.0);
                                       randomEP += magicule.getValue();
                                    }

                                    AttributeInstance aura = living.getAttribute((Attribute)TensuraAttributeRegistry.MAX_AURA.get());
                                    if (aura != null) {
                                       aura.setBaseValue((double)minEP / 2.0);
                                       randomEP += aura.getValue();
                                    }

                                    cap.setEP(living, randomEP);
                                    epSet = true;

                                    for (ResourceLocation skillID : entityEP.getSkills()) {
                                       ManasSkill skill = (ManasSkill)SkillAPI.getSkillRegistry().getValue(skillID);
                                       if (skill != null) {
                                          TensuraSkillInstance instance = new TensuraSkillInstance(skill);
                                          SkillAPI.getSkillsFrom(living).learnSkill(instance);
                                          if (instance.canBeToggled(living)) {
                                             instance.setToggled(true);
                                          }
                                       }
                                    }

                                    RaceSelectionMenu.grantLearningResistance(living);
                                    break;
                                 }
                              }

                              if (!epSet || cap.getEP() <= 0.0) {
                                 energy = (double)(100.0F * living.getMaxHealth());
                                 AttributeInstance magiculex = living.getAttribute((Attribute)TensuraAttributeRegistry.MAX_MAGICULE.get());
                                 if (magiculex != null) {
                                    energy += magiculex.getValue();
                                 }

                                 AttributeInstance aura = living.getAttribute((Attribute)TensuraAttributeRegistry.MAX_AURA.get());
                                 if (aura != null) {
                                    energy += aura.getValue();
                                 }

                                 cap.setEP(living, energy);
                              }

                              if (living.getType().is(EntityTypes.MONSTER)) {
                                 cap.setMajin(true);
                              }

                              applyEPToEntity(living, cap.getEP(), true);
                              TensuraEPCapability.sync(living);
                           }
                        }));
                  }
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onEntityTick(PlayerTickEvent event) {
      if (!event.player.getLevel().isClientSide()) {
         if (event.player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            if (event.player.tickCount % 600 == 0) {
               LivingEntity entity = event.player;
               if (entity.getLevel() instanceof ServerLevel serverLevel && entity.getRandom().nextFloat() < 0.005F) {
                  BlockPos spawnPos = findSafeSpawnPos(serverLevel, entity.blockPosition(), 50);
                  if (spawnPos != null) {
                     EntityType<?> minhSlimeType = (EntityType<?>)BeyondEntities.MINH_SLIME.get();
                     minhSlimeType.spawn(serverLevel, null, null, spawnPos, MobSpawnType.EVENT, true, false);
                  }
               }
            }
         }
      }
   }

   private static BlockPos findSafeSpawnPos(Level level, BlockPos center, int radius) {
      int y = center.getY();
      if (y > 115) {
         return null;
      } else {
         for (int attempts = 0; attempts < 20; attempts++) {
            int x = center.getX() + ChallengeEventHandler.getRandomInRange(-radius, radius);
            int z = center.getZ() + ChallengeEventHandler.getRandomInRange(-radius, radius);
            BlockPos pos = new BlockPos(x, y, z);
            boolean clear = level.isEmptyBlock(pos) && level.isEmptyBlock(pos.above());
            boolean solidBlock = level.getBlockState(pos.below()).isSolidRender(level, pos.below());
            if (clear && solidBlock) {
               return pos;
            }
         }

         return null;
      }
   }

   private static void applyRandomSkillLogic(LivingEntity living) {
      TensuraEPCapability.getFrom(living).ifPresent(cap -> {
         assignRandomSkill(living, UNIQUE_SKILL_MAP, UNIQUE_SKILL);
         assignRandomSkill(living, EXTRA_SKILL_MAP, EXTRA_SKILL);
      });
   }

   private static void assignRandomSkill(LivingEntity living, Map<String, RegistryObject<? extends Skill>> skillMap, String[] skillArray) {
      String randomSkill = skillArray[new Random().nextInt(skillArray.length)];
      RegistryObject<? extends ManasSkill> skillRegistryObject = skillMap.get(randomSkill);
      if (skillRegistryObject != null) {
         ResourceLocation skillId = skillRegistryObject.getId();
         ManasSkill skill = (ManasSkill)SkillAPI.getSkillRegistry().getValue(skillId);
         if (skill != null) {
            TensuraSkillInstance instance = new TensuraSkillInstance(skill);
            SkillAPI.getSkillsFrom(living).learnSkill(instance);
            if (instance.canBeToggled(living)) {
               instance.setToggled(true);
            }

            instance.setMastery(skill.getMaxMastery());
            instance.markDirty();
            SkillAPI.getSkillsFrom(living).syncChanges();
            if (living instanceof LivingEntity) {
               instance.onSkillMastered(living);
            }

            living.sendSystemMessage(Component.translatable("tensura.skill.mastery", new Object[]{skill.getName()}).withStyle(ChatFormatting.GREEN));
         }
      }
   }

   private static void applyEPToEntity(LivingEntity entity, double amount, boolean updateStats) {
      if (entity != null && entity.isAlive()) {
         LazyOptional<ITensuraEPCapability> capOptional = TensuraEPCapability.getFrom(entity);
         if (capOptional.isPresent()) {
            TensuraEPCapability cap = (TensuraEPCapability)capOptional.orElseThrow(() -> new IllegalStateException("Missing EP capability!"));
            cap.setEP(entity, amount * 10.0, updateStats);
            cap.setCurrentEP(entity, cap.getEP());
            TensuraEPCapability.sync(entity);
         } else {
            System.out.println("No EP Capability found for: " + entity.getType());
         }
      }
   }

   @SubscribeEvent
   public static void onEntityTick(LivingTickEvent event) {
      LivingEntity entity = event.getEntity();
      double y = entity.getY();
      Level level = entity.level;
      if (!level.isClientSide) {
         if (y >= 115.0 && y <= 130.0) {
            if (!level.dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
               return;
            }

            if (entity instanceof ServerPlayer) {
               return;
            }

            if (entity instanceof DryadEntity
               || entity instanceof KaijinEntity
               || entity instanceof KaoruEntity
               || entity instanceof KobyEntity
               || entity instanceof GuildLadyEntity
               || entity instanceof MjollmileEntity) {
               return;
            }

            if (entity instanceof TamableAnimal tame) {
               UUID permanentOwner = TensuraEPCapability.getPermanentOwner(tame);
               if (permanentOwner != null) {
                  return;
               }
            }

            entity.discard();
         }
      }
   }

   static {
      UNIQUE_SKILL_MAP.put("OBSERVER", UniqueSkills.OBSERVER);
      UNIQUE_SKILL_MAP.put("REAPER", UniqueSkills.REAPER);
      UNIQUE_SKILL_MAP.put("SEER", UniqueSkills.SEER);
      UNIQUE_SKILL_MAP.put("FIGHTER", UniqueSkills.FIGHTER);
      UNIQUE_SKILL_MAP.put("SHADOW_STRIKER", UniqueSkills.SHADOW_STRIKER);
      UNIQUE_SKILL_MAP.put("SNIPER", UniqueSkills.SNIPER);
      UNIQUE_SKILL_MAP.put("SURVIVOR", UniqueSkills.SURVIVOR);
      UNIQUE_SKILL_MAP.put("TUNER", UniqueSkills.TUNER);
      UNIQUE_SKILL_MAP.put("GOURMAND", UniqueSkills.GOURMAND);
      UNIQUE_SKILL_MAP.put("ANTI_SKILL", UniqueSkills.ANTI_SKILL);
      UNIQUE_SKILL_MAP.put("CHOSEN_ONE", UniqueSkills.CHOSEN_ONE);
      UNIQUE_SKILL_MAP.put("COMMANDER", UniqueSkills.COMMANDER);
      UNIQUE_SKILL_MAP.put("COOK", UniqueSkills.COOK);
      UNIQUE_SKILL_MAP.put("ENGORGER", UniqueSkills.ENGORGER);
      UNIQUE_SKILL_MAP.put("GUARDIAN", UniqueSkills.GUARDIAN);
      UNIQUE_SKILL_MAP.put("MARTIAL_MASTER", UniqueSkills.MARTIAL_MASTER);
      UNIQUE_SKILL_MAP.put("MATHEMATICIAN", UniqueSkills.MATHEMATICIAN);
      EXTRA_SKILL_MAP.put("ALL_SEEING_EYE", ExtraSkills.ALL_SEEING_EYE);
      EXTRA_SKILL_MAP.put("STRENGTHEN_BODY", ExtraSkills.STRENGTHEN_BODY);
      EXTRA_SKILL_MAP.put("STEEL_STRENGTH", ExtraSkills.STEEL_STRENGTH);
      EXTRA_SKILL_MAP.put("BLACK_FLAME", ExtraSkills.BLACK_FLAME);
      EXTRA_SKILL_MAP.put("ULTRA_INSTINCT", ExtraSkills.ULTRA_INSTINCT);
      EXTRA_SKILL_MAP.put("HEAVENLY_EYE", ExtraSkills.HEAVENLY_EYE);
      EXTRA_SKILL_MAP.put("MAGIC_JAMMING", ExtraSkills.MAGIC_JAMMING);
      EXTRA_SKILL_MAP.put("ULTRA_REGENERATION", ExtraSkills.ULTRASPEED_REGENERATION);
   }
}
