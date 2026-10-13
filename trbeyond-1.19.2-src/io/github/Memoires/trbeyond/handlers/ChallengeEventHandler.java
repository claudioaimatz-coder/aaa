package io.github.Memoires.trbeyond.handlers;

import com.github.manasmods.manascore.api.skills.event.SkillToggleEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.skill.resist.ResistSkill;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.Memoires.trbeyond.TRBeyond;
import io.github.Memoires.trbeyond.block.ShrineBlock;
import io.github.Memoires.trbeyond.capability.entities.EliteMobCapability;
import io.github.Memoires.trbeyond.capability.entities.IEliteMobCapability;
import io.github.Memoires.trbeyond.capability.player.BeyondPlayerCapability;
import io.github.Memoires.trbeyond.capability.player.IBeyondPlayerCapability;
import io.github.Memoires.trbeyond.data.BeyondTags;
import io.github.Memoires.trbeyond.entity.custom.BossBlackSpiderEntity;
import io.github.Memoires.trbeyond.entity.custom.MinhSlimeEntity;
import io.github.Memoires.trbeyond.entity.custom.ShrineEntity;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.util.ChallengeType;
import io.github.Memoires.trbeyond.util.ResistHelper;
import io.github.Memoires.trbeyond.world.BeyondGamerules;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent.Added;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(
   modid = "trbeyond"
)
public class ChallengeEventHandler {
   public static List<ChallengeType> challengeTypes = List.of(
      new ChallengeType("kill_slimes", "kill", "tensura:slime", 10, false, "tensura:silver_coin", 3, 60, true),
      new ChallengeType("kill_leech_lizard", "kill", "tensura:leech_lizard", 10, false, "tensura:silver_coin", 3, 60, true),
      new ChallengeType("collect_stone", "collect", "minecraft:cobblestone", 16, false, "tensura:gold_coin", 1, 0, false),
      new ChallengeType("explore_illusion", "explore", "trbeyond:illusionary_block", 2, false, "tensura:gold_coin", 1, 0, false),
      new ChallengeType("kill_elite_horned_rabbit", "kill", "tensura:horned_rabbit", 1, false, "tensura:gold_coin", 1, 0, false),
      new ChallengeType("dont_take_damage", "misc", "", 1, false, "tensura:silver_coin", 3, 60, true),
      new ChallengeType("kill_floor_boss", "kill", "trbeyond:boss_black_spider", 1, false, "tensura:gold_coin", 3, 960, true),
      new ChallengeType("kill_minh_slime", "kill", "trbeyond:minh_slime", 1, true, "tensura:gold_coin", 1, 60, false),
      new ChallengeType("kill_hound_dog", "kill", "tensura:hound_dog", 10, false, "tensura:silver_coin", 1, 60, true),
      new ChallengeType("kill_projectile", "special_kill", "", 50, false, "tensura:silver_coin", 5, 300, true),
      new ChallengeType("kill_stunned", "special_kill", "", 5, false, "tensura:silver_coin", 1, 300, false),
      new ChallengeType("kill_no_weapon", "special_kill", "", 50, false, "tensura:silver_coin", 1, 960, true),
      new ChallengeType("kill_floor_boss_no_heal", "kill", "trbeyond:boss_black_spider", 1, false, "tensura:gold_coin", 1, 960, false),
      new ChallengeType("kill_floor_boss_no_resist", "kill", "trbeyond:boss_black_spider", 1, false, "tensura:gold_coin", 1, 960, false),
      new ChallengeType("locate_majin", "locate", "", 1, false, "tensura:silver_coin", 3, 300, true),
      new ChallengeType("protect_or_kill_majin", "protect", "", 1, false, "tensura:silver_coin", 3, 0, false),
      new ChallengeType("kill_otherworlders", "group_kill", "", 3, false, "tensura:silver_coin", 10, 300, false),
      new ChallengeType("kill_dominators", "group_kill", "", 3, false, "tensura:silver_coin", 10, 300, false),
      new ChallengeType("one_shot_a_hundred", "group_kill", "", 100, false, "tensura:silver_coin", 3, 180, true),
      new ChallengeType("tame_kill_boss", "tame_kill", "trbeyond:boss_black_spider", 1, false, "tensura:gold_coin", 3, 960, true)
   );

   private static boolean challengesDisabled(Level level) {
      return BeyondGamerules.getBoolean(level, BeyondGamerules.DISABLE_CHALLENGES);
   }

   public static boolean shrineChallengesDisabled(Level level, Player player) {
      if (BeyondGamerules.getBoolean(level, BeyondGamerules.DISABLE_SHRINES)) {
         player.sendSystemMessage(
            Component.translatable("trbeyond.challenge.shrine_disabled").withStyle(style -> style.withColor(TextColor.fromRgb(16711680)).withBold(true))
         );
         return true;
      } else {
         return false;
      }
   }

   public static void giveChallengeType(Player player, String challengeType) {
      LazyOptional<IBeyondPlayerCapability> capabilityLazy = BeyondPlayerCapability.getFrom(player);
      if (challengeType != null) {
         if (!challengesDisabled(player.getLevel())) {
            capabilityLazy.ifPresent(
               capability -> {
                  ChallengeType type = challengeTypes.stream().filter(t -> t.getName().equals(challengeType)).findFirst().orElse(null);
                  if (type != null) {
                     capability.setChallengeType(ChallengeType.getChallengeType(type));
                     BeyondPlayerCapability.sync(player);
                     player.sendSystemMessage(
                        Component.translatable("trbeyond.challenge.give_challenge", new Object[]{Component.translatable("trbeyond.challenge." + challengeType)})
                           .withStyle(style -> style.withColor(TextColor.fromRgb(65280)).withBold(true))
                     );
                  }
               }
            );
         }
      }
   }

   public static boolean giveChallengeType(Player player, ChallengeType challengeType, boolean ignoreGlobalDisable) {
      AtomicBoolean success = new AtomicBoolean(false);
      if (challengeType == null) {
         return false;
      } else if (!ignoreGlobalDisable && challengesDisabled(player.getLevel())) {
         return false;
      } else if (player.getPersistentData().getInt("floor") != 1 && player.getPersistentData().getInt("floor") != 95) {
         getPlayerCapability(player)
            .ifPresent(
               capability -> {
                  if (capability.getChallengeType() == null) {
                     if (challengeType.getName().contains("no_resist") && !ResistHelper.checkResistSkills(player)) {
                        player.sendSystemMessage(
                           Component.translatable(
                                 "trbeyond.challenge.fail_challenge", new Object[]{Component.translatable("trbeyond.challenge." + challengeType.getName())}
                              )
                              .withStyle(style -> style.withColor(TextColor.fromRgb(16711680)).withBold(true))
                        );
                        player.sendSystemMessage(
                           Component.literal("Reason: You already have resists enabled").withStyle(style -> style.withColor(TextColor.fromRgb(16711680)))
                        );
                        return;
                     }

                     capability.setChallengeType(ChallengeType.getChallengeType(challengeType));
                     BeyondPlayerCapability.sync(player);
                     player.sendSystemMessage(
                        Component.translatable(
                              "trbeyond.challenge.give_challenge", new Object[]{Component.translatable("trbeyond.challenge." + challengeType.getName())}
                           )
                           .withStyle(style -> style.withColor(TextColor.fromRgb(65280)).withBold(true))
                     );
                     if (challengeType.getName().equals("kill_elite")) {
                        player.sendSystemMessage(
                           Component.translatable(
                                 "trbeyond.challenge.target_entity",
                                 new Object[]{
                                    Component.translatable(
                                       String.format(
                                          "entity.%s.%s",
                                          challengeType.getObjective().substring(0, challengeType.getObjective().indexOf(":")),
                                          challengeType.getObjective().substring(challengeType.getObjective().indexOf(":") + 1)
                                       )
                                    )
                                 }
                              )
                              .withStyle(style -> style.withColor(TextColor.fromRgb(65280)).withBold(true))
                        );
                     }

                     success.set(true);
                  }
               }
            );
         return success.get();
      } else {
         return false;
      }
   }

   public static boolean giveChallengeType(Player player, ChallengeType challengeType, Component customName) {
      AtomicBoolean success = new AtomicBoolean(false);
      if (challengeType == null) {
         return false;
      } else if (challengesDisabled(player.getLevel())) {
         return false;
      } else if (player.getPersistentData().getInt("floor") == 1) {
         return false;
      } else {
         getPlayerCapability(player)
            .ifPresent(
               capability -> {
                  if (capability.getChallengeType() == null) {
                     if (challengeType.getName().contains("no_resist") && !ResistHelper.checkResistSkills(player)) {
                        player.sendSystemMessage(
                           Component.translatable("trbeyond.challenge.fail_challenge", new Object[]{challengeType.getName()})
                              .withStyle(style -> style.withColor(TextColor.fromRgb(16711680)).withBold(true))
                        );
                        player.sendSystemMessage(
                           Component.literal("Reason: You already have resists enabled").withStyle(style -> style.withColor(TextColor.fromRgb(16711680)))
                        );
                        return;
                     }

                     capability.setChallengeType(ChallengeType.getChallengeType(challengeType));
                     BeyondPlayerCapability.sync(player);
                     player.sendSystemMessage(
                        Component.translatable("trbeyond.challenge.give_challenge", new Object[]{customName})
                           .withStyle(style -> style.withColor(TextColor.fromRgb(200)).withBold(true))
                     );
                     success.set(true);
                  }
               }
            );
         return success.get();
      }
   }

   public static Component getItemDisplayName(String itemLocation) {
      ResourceLocation loc = new ResourceLocation(itemLocation);
      Item item = (Item)ForgeRegistries.ITEMS.getValue(loc);
      return (Component)(item == null ? Component.literal(itemLocation) : new ItemStack(item).getHoverName());
   }

   public static void failChallenge(Player player, ChallengeType challengeType) {
      if (challengeType != null) {
         getPlayerCapability(player)
            .ifPresent(
               capability -> {
                  String challengeKey = "trbeyond.challenge." + challengeType.getName();
                  Component challengeName = Component.translatable(challengeKey);
                  player.sendSystemMessage(
                     Component.translatable("trbeyond.challenge.fail_challenge", new Object[]{challengeName})
                        .withStyle(style -> style.withColor(TextColor.fromRgb(16711680)).withBold(true))
                  );
                  capability.setChallengeType(null);
                  BeyondPlayerCapability.sync(player);
               }
            );
      }
   }

   public static Optional<IBeyondPlayerCapability> getPlayerCapability(Player player) {
      LazyOptional<IBeyondPlayerCapability> lazyOptional = BeyondPlayerCapability.getFrom(player);
      return lazyOptional.isPresent() ? lazyOptional.map(Optional::of).orElseGet(Optional::empty) : Optional.empty();
   }

   @SubscribeEvent
   public static void onEntitySpawn(EntityJoinLevelEvent event) {
      if (!event.getLevel().isClientSide()) {
         if (event.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            if (event.getEntity() instanceof LivingEntity) {
               List<ChallengeType> killChallengeTypes = challengeTypes.stream().filter(type -> type.getCategory().equals("kill") && type.isOnSpawn()).toList();
               boolean matchedKillChallenge = killChallengeTypes.stream()
                  .anyMatch(
                     type -> event.getEntity().getType().toString().contains(type.getObjective().split(":")[0])
                           && event.getEntity().getType().toString().contains(type.getObjective().split(":")[1])
                  );
               if (matchedKillChallenge) {
                  event.getLevel()
                     .getEntitiesOfClass(Player.class, event.getEntity().getBoundingBox().inflate(20.0))
                     .forEach(
                        player -> getPlayerCapability(player)
                              .ifPresent(
                                 capability -> {
                                    if (capability.getChallengeType() == null) {
                                       ChallengeType challengeType = killChallengeTypes.stream()
                                          .filter(
                                             type -> event.getEntity().getType().toString().contains(type.getObjective().split(":")[0])
                                                   && event.getEntity().getType().toString().contains(type.getObjective().split(":")[1])
                                          )
                                          .findFirst()
                                          .orElse(null);
                                       giveChallengeType(player, challengeType, false);
                                    }
                                 }
                              )
                     );
               }

               List<String> majinCandidates = List.of("tensura:orc", "tensura:lizardman", "tensura:goblin");
               if (event.getEntity() instanceof LivingEntity majinEntity) {
                  String typeId = majinEntity.getType().toString();
                  boolean isMajinCandidate = majinCandidates.stream().anyMatch(typeId::contains);
                  if (isMajinCandidate && majinEntity.hasCustomName()) {
                     event.getLevel()
                        .getEntitiesOfClass(Player.class, majinEntity.getBoundingBox().inflate(40.0))
                        .forEach(
                           player -> getPlayerCapability(player)
                                 .ifPresent(
                                    cap -> {
                                       if (cap.getChallengeType() == null) {
                                          ChallengeType locateMajin = ChallengeType.getChallengeType(
                                             "locate_majin_name", "locate", majinEntity.getStringUUID(), 1, false, "tensura:silver_coin", 3, 300, false
                                          );
                                          if (giveChallengeType(
                                             player,
                                             locateMajin,
                                             Component.translatable("trbeyond.challenge.locate_majin_name", new Object[]{majinEntity.getName()})
                                          )) {
                                             player.sendSystemMessage(
                                                Component.translatable("trbeyond.challenge.locate_majin.hint", new Object[]{majinEntity.getName()})
                                                   .withStyle(s -> s.withColor(TextColor.fromRgb(65535)).withBold(true))
                                             );
                                          }
                                       }
                                    }
                                 )
                        );
                  }
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onEntityDeath(LivingDeathEvent event) {
      if (!event.getEntity().getLevel().isClientSide) {
         if (event.getEntity().getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            LivingEntity entity = event.getEntity();
            if (entity.getType().is(BeyondTags.DUNGEON_BOSS) && !(event.getSource().getEntity() instanceof Player)) {
               entity.getLevel()
                  .getEntitiesOfClass(Player.class, entity.getBoundingBox().inflate(50.0))
                  .forEach(
                     playerx -> getPlayerCapability(playerx)
                           .ifPresent(
                              capability -> {
                                 if (capability.getChallengeType() != null) {
                                    ChallengeType challengeType = capability.getChallengeType();
                                    if (challengeType.getName().contains("kill_floor_boss")
                                       && (challengeType.getName().contains("no_heal") || challengeType.getName().contains("no_resist"))) {
                                       failChallenge(playerx, challengeType);
                                       playerx.sendSystemMessage(
                                          Component.literal("Reason: The floor boss wasn't killed by you")
                                             .withStyle(style -> style.withColor(TextColor.fromRgb(16711680)))
                                       );
                                    }
                                 }
                              }
                           )
                  );
            }

            if ((
                  entity.getType().toString().contains("mark_lauren")
                     || entity.getType().toString().contains("shinji_tanimura")
                     || entity.getType().toString().contains("shin_ryusei")
               )
               && !(event.getSource().getEntity() instanceof Player)) {
               entity.getLevel()
                  .getEntitiesOfClass(Player.class, entity.getBoundingBox().inflate(50.0))
                  .forEach(
                     playerx -> getPlayerCapability(playerx)
                           .ifPresent(
                              capability -> {
                                 ChallengeType challengeType = capability.getChallengeType();
                                 if (challengeType != null && challengeType.getName().equals("kill_otherworlders")) {
                                    failChallenge(playerx, challengeType);
                                    playerx.sendSystemMessage(
                                       Component.literal("Reason: The otherworlder wasn't killed by you")
                                          .withStyle(style -> style.withColor(TextColor.fromRgb(16711680)))
                                    );
                                 }
                              }
                           )
                  );
            }

            if ((
                  entity.getType().toString().contains("living_armor")
                     || entity.getType().toString().contains("milim_slime_scarlet")
                     || entity.getType().toString().contains("mage")
                     || entity.getType().toString().contains("skull_night")
               )
               && !(event.getSource().getEntity() instanceof Player)) {
               entity.getLevel()
                  .getEntitiesOfClass(Player.class, entity.getBoundingBox().inflate(50.0))
                  .forEach(
                     playerx -> getPlayerCapability(playerx)
                           .ifPresent(
                              capability -> {
                                 ChallengeType challengeType = capability.getChallengeType();
                                 if (challengeType != null && challengeType.getName().equals("kill_dominators")) {
                                    failChallenge(playerx, challengeType);
                                    playerx.sendSystemMessage(
                                       Component.literal("Reason: The dominator wasn't killed by you")
                                          .withStyle(style -> style.withColor(TextColor.fromRgb(16711680)))
                                    );
                                 }
                              }
                           )
                  );
            }

            if (entity.getPersistentData().contains("challengeMob") && entity.getPersistentData().contains("shrinePos")) {
               BlockPos shrinePos = BlockPos.of(entity.getPersistentData().getLong("shrinePos"));
               if (entity.tickCount < 2400 && entity.getLevel().getBlockState(shrinePos).getBlock() instanceof ShrineBlock shrineBlock) {
                  shrineBlock.mobKilled();
               }
            }

            if (event.getSource().getEntity() instanceof Player player) {
               getPlayerCapability(player)
                  .ifPresent(
                     capability -> {
                        if (capability.getChallengeType() != null) {
                           ChallengeType challengeType = capability.getChallengeType();
                           String targetUuid = challengeType.getCategory();
                           switch (targetUuid) {
                              case "kill":
                                 String entityType = event.getEntity().getType().toString();
                                 if (entityType.contains(challengeType.getObjective().split(":")[0])
                                    && entityType.contains(challengeType.getObjective().split(":")[1])) {
                                    if (challengeType.getName().contains("elite")
                                       && !EliteMobCapability.getFrom(event.getEntity()).map(IEliteMobCapability::isElite).orElse(false)) {
                                       return;
                                    }

                                    challengeType.setCountDone(challengeType.getCountDone() + 1);
                                 }
                                 break;
                              case "special_kill":
                                 if (challengeType.getName().equals("kill_projectile") && event.getSource().getDirectEntity() instanceof Projectile) {
                                    challengeType.setCountDone(challengeType.getCountDone() + 1);
                                 } else if (challengeType.getName().equals("kill_stunned") && player.hasEffect((MobEffect)TensuraMobEffects.PARALYSIS.get())) {
                                    challengeType.setCountDone(challengeType.getCountDone() + 1);
                                 } else if (challengeType.getName().equals("kill_no_weapon") && player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty()) {
                                    challengeType.setCountDone(challengeType.getCountDone() + 1);
                                 }
                                 break;
                              case "group_kill":
                                 String typeStr = entity.getType().toString();
                                 if ((typeStr.contains("mark_lauren") || typeStr.contains("shinji_tanimura") || typeStr.contains("shin_ryusei"))
                                    && challengeType.getName().equals("kill_otherworlders")) {
                                    challengeType.setCountDone(challengeType.getCountDone() + 1);
                                 }

                                 if ((
                                       typeStr.contains("living_armor")
                                          || typeStr.contains("milim_slime_scarlet")
                                          || typeStr.contains("mage")
                                          || typeStr.contains("skull_night")
                                    )
                                    && challengeType.getName().equals("kill_dominators")) {
                                    challengeType.setCountDone(challengeType.getCountDone() + 1);
                                 }
                                 break;
                              case "tame_kill":
                                 failChallenge(player, capability.getChallengeType());
                           }

                           targetUuid = challengeType.getObjective();
                           if (targetUuid != null && !targetUuid.isEmpty() && event.getEntity().getUUID().toString().equals(targetUuid)) {
                              if (challengeType.getName().startsWith("protect_or_kill_majin_")) {
                                 challengeType.setCountDone(1);
                                 player.getPersistentData().remove("protect_challenge_time");
                              } else if (challengeType.getName().startsWith("locate_majin_") && challengeType.getCountDone() < challengeType.getCount()) {
                                 player.getPersistentData().remove("protect_challenge_time");
                                 failChallenge(player, challengeType);
                              }
                           }

                           BeyondPlayerCapability.sync(player);
                        }
                     }
                  );
            } else if (event.getSource().getEntity() instanceof LivingEntity attacker && attacker instanceof TamableAnimal tame) {
               if (tame.getOwner() instanceof Player player) {
                  getPlayerCapability(player)
                     .ifPresent(
                        capability -> {
                           if (capability.getChallengeType() != null) {
                              ChallengeType challengeType = capability.getChallengeType();
                              String targetUuid = challengeType.getCategory();
                              switch (targetUuid) {
                                 case "kill":
                                    String entityType = event.getEntity().getType().toString();
                                    if (entityType.contains(challengeType.getObjective().split(":")[0])
                                       && entityType.contains(challengeType.getObjective().split(":")[1])) {
                                       if (challengeType.getName().contains("elite")
                                          && !EliteMobCapability.getFrom(event.getEntity()).map(IEliteMobCapability::isElite).orElse(false)) {
                                          return;
                                       }

                                       challengeType.setCountDone(challengeType.getCountDone() + 1);
                                    }
                                    break;
                                 case "special_kill":
                                    if (challengeType.getName().equals("kill_projectile") && event.getSource().getDirectEntity() instanceof Projectile) {
                                       challengeType.setCountDone(challengeType.getCountDone() + 1);
                                    } else if (challengeType.getName().equals("kill_stunned") && player.hasEffect((MobEffect)TensuraMobEffects.PARALYSIS.get())) {
                                       challengeType.setCountDone(challengeType.getCountDone() + 1);
                                    } else if (challengeType.getName().equals("kill_no_weapon") && player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty()
                                       )
                                     {
                                       challengeType.setCountDone(challengeType.getCountDone() + 1);
                                    }
                                    break;
                                 case "group_kill":
                                    String typeStr = entity.getType().toString();
                                    if ((typeStr.contains("mark_lauren") || typeStr.contains("shinji_tanimura") || typeStr.contains("shin_ryusei"))
                                       && challengeType.getName().equals("kill_otherworlders")) {
                                       challengeType.setCountDone(challengeType.getCountDone() + 1);
                                    }

                                    if ((
                                          typeStr.contains("living_armor")
                                             || typeStr.contains("milim_slime_scarlet")
                                             || typeStr.contains("mage")
                                             || typeStr.contains("skull_night")
                                       )
                                       && challengeType.getName().equals("kill_dominators")) {
                                       challengeType.setCountDone(challengeType.getCountDone() + 1);
                                    }
                              }

                              targetUuid = challengeType.getObjective();
                              if (targetUuid != null && !targetUuid.isEmpty() && event.getEntity().getUUID().toString().equals(targetUuid)) {
                                 if (challengeType.getName().startsWith("protect_or_kill_majin_")) {
                                    challengeType.setCountDone(1);
                                    player.getPersistentData().remove("protect_challenge_time");
                                 } else if (challengeType.getName().startsWith("locate_majin_") && challengeType.getCountDone() < challengeType.getCount()) {
                                    player.getPersistentData().remove("protect_challenge_time");
                                    failChallenge(player, challengeType);
                                 }
                              }

                              BeyondPlayerCapability.sync(player);
                           }
                        }
                     );
               }

               if (entity instanceof BossBlackSpiderEntity && tame.getOwner() instanceof Player player) {
                  getPlayerCapability(player).ifPresent(capability -> {
                     if (capability.getChallengeType() != null) {
                        ChallengeType challengeType = capability.getChallengeType();
                        if (challengeType.getCategory().equals("tame_kill")) {
                           challengeType.setCountDone(challengeType.getCountDone() + 1);
                        }

                        BeyondPlayerCapability.sync(player);
                     }
                  });
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onEntityTick(PlayerTickEvent event) {
      if (!event.player.level.isClientSide()) {
         if (event.player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            getPlayerCapability(event.player).ifPresent(capability -> {
               if (capability.getChallengeType() != null) {
                  ChallengeType challengeType = capability.getChallengeType();
                  String var3 = challengeType.getCategory();
                  switch (var3) {
                     case "collect":
                        event.player.getInventory().items.forEach(itemStack -> {
                           if (itemStack.getItem().toString().equals(challengeType.getObjective()) && itemStack.getCount() >= challengeType.getCount()) {
                              challengeType.setCountDone(challengeType.getCount());
                              BeyondPlayerCapability.sync(event.player);
                           }
                        });
                        break;
                     case "locate":
                        try {
                           UUID uuid = UUID.fromString(challengeType.getObjective());
                           if (((ServerLevel)event.player.level).getEntity(uuid) instanceof LivingEntity living) {
                              if (challengeType.getCountDone() < challengeType.getCount()) {
                                 if (living.isDeadOrDying()) {
                                    failChallenge(event.player, challengeType);
                                 } else if (event.player.distanceTo(living) < 5.0F) {
                                    challengeType.setCountDone(1);
                                    BeyondPlayerCapability.sync(event.player);
                                 }
                              }
                           } else if (challengeType.getCountDone() < challengeType.getCount()) {
                              failChallenge(event.player, challengeType);
                           }
                        } catch (IllegalArgumentException var10) {
                        }
                        break;
                     case "protect":
                        try {
                           UUID uuid = UUID.fromString(challengeType.getObjective());
                           if (((ServerLevel)event.player.level).getEntity(uuid) instanceof LivingEntity living) {
                              if (challengeType.getCountDone() >= challengeType.getCount()) {
                                 return;
                              }

                              if (living.isDeadOrDying()) {
                                 event.player.getPersistentData().remove("protect_challenge_time");
                                 failChallenge(event.player, challengeType);
                              } else if (event.player.tickCount % 20 == 0) {
                                 int timer = event.player.getPersistentData().getInt("protect_challenge_time");
                                 event.player.getPersistentData().putInt("protect_challenge_time", ++timer);
                                 if (timer >= 30) {
                                    challengeType.setCountDone(1);
                                    event.player.getPersistentData().remove("protect_challenge_time");
                                    BeyondPlayerCapability.sync(event.player);
                                 }
                              }
                           } else if (challengeType.getCountDone() < challengeType.getCount()) {
                              event.player.getPersistentData().remove("protect_challenge_time");
                              failChallenge(event.player, challengeType);
                           }
                        } catch (IllegalArgumentException var9) {
                        }
                  }
               }
            });
         }
      }
   }

   @SubscribeEvent
   public static void ChallengeRewardTick(PlayerTickEvent event) {
      if (!event.player.level.isClientSide()) {
         if (event.player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            getPlayerCapability(event.player)
               .ifPresent(
                  capability -> {
                     if (capability.getChallengeType() != null) {
                        ChallengeType challengeType = capability.getChallengeType();
                        if (challengeType.getCountDone() >= challengeType.getCount()) {
                           ItemStack reward = new ItemStack(
                              (ItemLike)ForgeRegistries.ITEMS.getValue(new ResourceLocation(challengeType.getReward())), challengeType.getRewardAmount()
                           );
                           if (!event.player.getInventory().add(reward)) {
                              event.player
                                 .level
                                 .addFreshEntity(
                                    new ItemEntity(event.player.level, event.player.getX(), event.player.getY(), event.player.getZ(), reward)
                                 );
                           }

                           capability.setChallengeType(null);
                           BeyondPlayerCapability.sync(event.player);
                           event.player
                              .sendSystemMessage(
                                 Component.translatable(
                                       "trbeyond.challenge.complete_challenge",
                                       new Object[]{Component.translatable("trbeyond.challenge." + challengeType.getName())}
                                    )
                                    .withStyle(style -> style.withColor(TextColor.fromRgb(65280)).withBold(true))
                              );
                           if (challengeType.getReward() != "minecraft:air") {
                              event.player
                                 .sendSystemMessage(
                                    Component.translatable(
                                          "trbeyond.challenge.reward",
                                          new Object[]{getItemDisplayName(challengeType.getReward()), challengeType.getRewardAmount()}
                                       )
                                       .withStyle(style -> style.withColor(TextColor.fromRgb(65280)).withBold(true))
                                 );
                           }

                           if (event.player.getPersistentData().contains("challenge_time")) {
                              event.player.getPersistentData().remove("challenge_time");
                           }

                           if (challengeType.getName().startsWith("locate_majin_")) {
                              Entity entity = SkillHelper.getEntityFromUUID((ServerLevel)event.player.level, UUID.fromString(challengeType.getObjective()));
                              if (entity == null) {
                                 TRBeyond.getLogger().warn("Failed to find Majin entity with UUID: {} for challenge completion.", challengeType.getObjective());
                                 return;
                              }

                              ChallengeType next = ChallengeType.getChallengeType(
                                 "protect_or_kill_majin_name", "protect", challengeType.getObjective(), 1, false, "tensura:silver_coin", 3, 0, false
                              );
                              giveChallengeType(
                                 event.player, next, Component.translatable("trbeyond.challenge.protect_or_kill_majin_name", new Object[]{entity.getName()})
                              );
                           }
                        }
                     }
                  }
               );
         }
      }
   }

   @SubscribeEvent
   public static void nearEliteChallenge(PlayerTickEvent event) {
      if (!event.player.level.isClientSide() && !challengesDisabled(event.player.level)) {
         if (event.player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            Player player = event.player;
            if (player.tickCount % 100 == 0) {
               boolean[] hasElite = new boolean[]{false};
               List<ChallengeType> killChallengeTypes = challengeTypes.stream().filter(type -> type.getCategory().equals("kill")).toList();
               player.level
                  .getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(20.0, 6.0, 20.0))
                  .forEach(
                     entity -> {
                        if (entity != null && !entity.isDeadOrDying()) {
                           if (EliteMobCapability.getFrom(entity).map(IEliteMobCapability::isElite).orElse(false)) {
                              getPlayerCapability(player)
                                 .ifPresent(
                                    capability -> {
                                       if (capability.getChallengeType() == null) {
                                          if (killChallengeTypes.stream()
                                             .anyMatch(
                                                type -> type.getName().contains("elite")
                                                      && type.getObjective()
                                                         .contains(
                                                            entity.getType()
                                                               .toString()
                                                               .substring(entity.getType().toString().indexOf(46) + 1)
                                                               .substring(
                                                                  entity.getType()
                                                                        .toString()
                                                                        .substring(entity.getType().toString().indexOf(46) + 1)
                                                                        .indexOf(46)
                                                                     + 1
                                                               )
                                                         )
                                                      && type.getObjective()
                                                         .contains(
                                                            entity.getType()
                                                               .toString()
                                                               .substring(entity.getType().toString().indexOf(46) + 1)
                                                               .substring(
                                                                  0,
                                                                  entity.getType()
                                                                     .toString()
                                                                     .substring(entity.getType().toString().indexOf(46) + 1)
                                                                     .indexOf(46)
                                                               )
                                                         )
                                             )) {
                                             ChallengeType existingChallenge = killChallengeTypes.stream()
                                                .filter(
                                                   type -> type.getName().contains("elite")
                                                         && type.getObjective()
                                                            .contains(entity.getType().toString().substring(entity.getType().toString().indexOf(46) + 1))
                                                         && type.getObjective().contains(entity.getType().toString().split("\\.")[2])
                                                )
                                                .findFirst()
                                                .orElse(null);
                                             giveChallengeType(player, existingChallenge, false);
                                             return;
                                          }

                                          String entityName = entity.getType()
                                             .toString()
                                             .substring(entity.getType().toString().indexOf(46) + 1)
                                             .replace(".", ":");
                                          ChallengeType challengeType = ChallengeType.getChallengeType(
                                             "kill_elite", "kill", entityName, 1, false, "tensura:gold_coin", 1, 0, false
                                          );
                                          giveChallengeType(player, challengeType, false);
                                       }

                                       hasElite[0] = true;
                                    }
                                 );
                           }
                        }
                     }
                  );
               if (!hasElite[0]) {
                  getPlayerCapability(player).ifPresent(capability -> {
                     if (capability.getChallengeType() != null) {
                        ChallengeType challengeType = capability.getChallengeType();
                        if (challengeType.getName().contains("elite")) {
                           failChallenge(player, challengeType);
                        }
                     }
                  });
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void nearFloorBossChallenge(PlayerTickEvent event) {
      if (event.phase == Phase.START && !event.player.level.isClientSide() && !challengesDisabled(event.player.level)) {
         if (event.player.tickCount % 100 == 0) {
            double radius = 50.0;
            List<LivingEntity> bosses = event.player.level.getEntitiesOfClass(LivingEntity.class, event.player.getBoundingBox().inflate(radius, 0.0, radius), entity -> {
               ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
               return entityId != null && entityId.toString().equals("trbeyond:boss_black_spider");
            });
            boolean foundBoss = !bosses.isEmpty();
            if (foundBoss) {
               getPlayerCapability(event.player)
                  .ifPresent(
                     capability -> {
                        if (capability.getChallengeType() == null) {
                           List<ChallengeType> bossChallengeTypes = challengeTypes.stream()
                              .filter(type -> type.getObjective().equals("trbeyond:boss_black_spider"))
                              .toList();
                           if (!bossChallengeTypes.isEmpty()) {
                              int dayLength = 86400;
                              if (getRandomInRange(1, 100) <= 25) {
                                 String[] specialChallenges = new String[]{
                                    "kill_floor_boss_no_heal", "kill_floor_boss_no_resist", "kill_floor_boss_no_heal_no_resist", "tame_kill_boss"
                                 };

                                 for (int i = specialChallenges.length - 1; i > 0; i--) {
                                    int j = getRandomInRange(0, i);
                                    String temp = specialChallenges[i];
                                    specialChallenges[i] = specialChallenges[j];
                                    specialChallenges[j] = temp;
                                 }

                                 for (String challengeKey : specialChallenges) {
                                    String cooldownKey = challengeKey + "_cooldown";
                                    if (!event.player.getPersistentData().contains(cooldownKey)) {
                                       ChallengeType specialChallenge;
                                       if (!challengeKey.equals("kill_floor_boss_no_heal_no_resist")) {
                                          specialChallenge = bossChallengeTypes.stream()
                                             .filter(type -> type.getName().equals(challengeKey))
                                             .findFirst()
                                             .orElse(null);
                                       } else {
                                          specialChallenge = new ChallengeType(
                                             "kill_floor_boss_no_heal_no_resist",
                                             "kill",
                                             "trbeyond:boss_black_spider",
                                             1,
                                             false,
                                             "tensura:gold_coin",
                                             5,
                                             960,
                                             false
                                          );
                                       }

                                       if (specialChallenge != null) {
                                          giveChallengeType(event.player, specialChallenge, false);
                                          event.player.getPersistentData().putInt(cooldownKey, dayLength);
                                          if (challengeKey.equals("kill_floor_boss_no_heal_no_resist")) {
                                             event.player
                                                .sendSystemMessage(
                                                   Component.literal("Special Challenge: Defeat the floor boss without healing or resistance!")
                                                      .withStyle(style -> style.withColor(TextColor.fromRgb(16755200)).withBold(true))
                                                );
                                          }

                                          return;
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  );
            }
         }
      }
   }

   @SubscribeEvent
   public static void randomChallenge(PlayerTickEvent event) {
      if (!event.player.level.isClientSide()
         && !challengesDisabled(event.player.level)
         && event.player.level.dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
         if (event.player.tickCount % 1200 == 0) {
            Player player = event.player;
            getPlayerCapability(player)
               .ifPresent(
                  capability -> {
                     if (capability.getChallengeType() == null) {
                        if (player.getPersistentData().contains("challenge_cooldown")) {
                           int cooldown = player.getPersistentData().getInt("challenge_cooldown");
                           if (cooldown > 0) {
                              player.getPersistentData().putInt("challenge_cooldown", cooldown - 1);
                              return;
                           }

                           player.getPersistentData().remove("challenge_cooldown");
                        }

                        if (player.getPersistentData().contains("challenge_chance")) {
                           int chance = player.getPersistentData().getInt("challenge_chance");
                           if (chance < 100) {
                              if (chance < 20) {
                                 chance++;
                              } else {
                                 chance += 2;
                              }

                              player.getPersistentData().putInt("challenge_chance", chance);
                           }
                        } else {
                           player.getPersistentData().putInt("challenge_chance", 1);
                        }

                        int chance = player.getPersistentData().getInt("challenge_chance");
                        if (chance >= 100 || getRandomInRange(1, 100) <= chance) {
                           player.getPersistentData().remove("challenge_chance");
                           if (capability.getChallengeType() == null) {
                              List<ChallengeType> randomChallenges = challengeTypes.stream().filter(ChallengeType::isRandom).toList();
                              ChallengeType selected = randomChallenges.get((int)(Math.random() * (double)randomChallenges.size()));
                              if ("locate_majin".equals(selected.getName())) {
                                 ServerLevel level = (ServerLevel)player.level;
                                 List<String> majinEntities = List.of("tensura:orc", "tensura:lizardman", "tensura:goblin");
                                 List<String> goblinNames = List.of(
                                    "Gobrin", "Gobuya", "Gobsha", "Gobnari", "Gobbiko", "Gobmori", "Gobzai", "Gobrinchi", "Gobufu", "Gobtoki"
                                 );
                                 List<String> orcNames = List.of(
                                    "Geldar", "Brakto", "Dorgel", "Makrid", "Grudon", "Vorgul", "Kelbar", "Rendor", "Beldrak", "Orgram"
                                 );
                                 List<String> dragonewtNames = List.of(
                                    "Saryon", "Vaskir", "Drakoru", "Zarenth", "Kurasyl", "Thalvik", "Ryugar", "Myzoth", "Dravak", "Ozarien"
                                 );
                                 List<String> tensuraNames = List.of(
                                    "Miyaka", "Renjou", "Teshka", "Okamaru", "Zegrah", "Hinoro", "Kaelvi", "Nadeka", "Retsu", "Valmere"
                                 );
                                 String entityId = majinEntities.get(getRandomInRange(0, majinEntities.size() - 1));
                                 List<String> namePool = new ArrayList<>();
                                 if (entityId.contains("goblin")) {
                                    namePool.addAll(goblinNames);
                                 } else if (entityId.contains("orc")) {
                                    namePool.addAll(orcNames);
                                 } else {
                                    namePool.addAll(dragonewtNames);
                                 }

                                 namePool.addAll(tensuraNames);
                                 String name = namePool.get(getRandomInRange(0, namePool.size() - 1));
                                 EntityType<?> type = (EntityType<?>)ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(entityId));
                                 if (type != null && type.create(level) instanceof LivingEntity majin) {
                                    double spawnX = player.getX();
                                    double spawnZ = player.getZ();
                                    double spawnY = player.getY();
                                    boolean foundPos = false;

                                    for (int attempt = 0; attempt < 20 && !foundPos; attempt++) {
                                       double angle = Math.random() * Math.PI * 2.0;
                                       double distance = 5.0 + Math.random() * 25.0;
                                       double candidateX = player.getX() + Math.cos(angle) * distance;
                                       double candidateZ = player.getZ() + Math.sin(angle) * distance;
                                       BlockPos candidatePos = new BlockPos((int)Math.floor(candidateX), (int)Math.floor(spawnY), (int)Math.floor(candidateZ));
                                       if (level.isEmptyBlock(candidatePos) && !level.isEmptyBlock(candidatePos.below())) {
                                          majin.moveTo(candidateX + 0.5, spawnY, candidateZ + 0.5, majin.getYRot(), majin.getXRot());
                                          if (level.noCollision(majin)) {
                                             spawnX = candidateX;
                                             spawnZ = candidateZ;
                                             foundPos = true;
                                          }
                                       }
                                    }

                                    majin.moveTo(spawnX + 0.5, spawnY, spawnZ + 0.5, majin.getYRot(), majin.getXRot());
                                    majin.setCustomName(Component.literal(name));
                                    majin.setCustomNameVisible(true);
                                    if (majin instanceof Mob mob) {
                                       mob.setPersistenceRequired();
                                    }

                                    level.addFreshEntity(majin);
                                    ChallengeType dynamic = ChallengeType.getChallengeType(
                                       "locate_majin_name" + majin.getName(), "locate", majin.getStringUUID(), 1, false, "tensura:silver_coin", 3, 300, false
                                    );
                                    if (giveChallengeType(
                                       player, dynamic, Component.translatable("trbeyond.challenge.locate_majin_name", new Object[]{majin.getName()})
                                    )) {
                                       player.sendSystemMessage(
                                          Component.translatable("trbeyond.challenge.locate_majin.hint", new Object[]{majin.getName()})
                                             .withStyle(s -> s.withColor(TextColor.fromRgb(65535)).withBold(true))
                                       );
                                    }
                                 }
                              } else {
                                 if (selected.getName().contains("floor_boss")
                                    && (
                                       !player.getPersistentData().contains("floor")
                                          || player.getPersistentData().getInt("floor") < 2
                                          || player.getPersistentData().getInt("floor") > 5
                                    )) {
                                    return;
                                 }

                                 giveChallengeType(player, selected, false);
                              }

                              player.getPersistentData().putInt("challenge_cooldown", 15);
                           }
                        }
                     }
                  }
               );
         }
      }
   }

   @SubscribeEvent
   public static void ChallengeTimer(PlayerTickEvent event) {
      if (event.phase != Phase.START && !event.player.level.isClientSide()) {
         Player player = event.player;
         if (event.player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            getPlayerCapability(player).ifPresent(capability -> {
               if (capability.getChallengeType() != null) {
                  ChallengeType challengeType = capability.getChallengeType();
                  if (challengeType.getTimeLimit() > 0 && player.tickCount % 20 == 0) {
                     if (player.getPersistentData().contains("challenge_time")) {
                        int timer = player.getPersistentData().getInt("challenge_time");
                        player.getPersistentData().putInt("challenge_time", ++timer);
                        if (timer > challengeType.getTimeLimit()) {
                           if (challengeType.getName().equals("dont_take_damage")) {
                              challengeType.setCountDone(challengeType.getCount());
                              return;
                           }

                           player.getPersistentData().remove("challenge_time");
                           failChallenge(player, challengeType);
                        }
                     } else {
                        player.getPersistentData().putInt("challenge_time", 1);
                     }
                  }
               }
            });
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerDamage(LivingHurtEvent event) {
      if (event.getEntity() instanceof Player player) {
         if (player.level.isClientSide() || challengesDisabled(player.level)) {
            return;
         }

         if (!player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            return;
         }

         getPlayerCapability(player).ifPresent(capability -> {
            if (capability.getChallengeType() != null) {
               ChallengeType challengeType = capability.getChallengeType();
               if (challengeType.getName().equals("dont_take_damage")) {
                  failChallenge(player, challengeType);
               }
            }
         });
      }

      if (event.getSource().getEntity() instanceof Player player) {
         if (player.level.isClientSide() || challengesDisabled(player.level)) {
            return;
         }

         if (!player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            return;
         }

         getPlayerCapability(player).ifPresent(capability -> {
            if (capability.getChallengeType() != null) {
               ChallengeType challengeType = capability.getChallengeType();
               if (challengeType.getName().equals("one_shot_a_hundred")) {
                  LivingEntity target = event.getEntity();
                  if (event.getAmount() >= target.getMaxHealth()) {
                     challengeType.setCountDone(challengeType.getCountDone() + 1);
                  }
               }
            }
         });
      }
   }

   @SubscribeEvent
   public static void syncChallenge(PlayerTickEvent event) {
      if (event.phase != Phase.START && !event.player.level.isClientSide()) {
         Player player = event.player;
         if (player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            getPlayerCapability(player)
               .ifPresent(
                  capability -> {
                     BeyondPlayerCapability.sync(player);
                     if (capability.getChallengeType() != null
                        && capability.getChallengeType().getTimeLimit() > 0
                        && player.getPersistentData().contains("challenge_time")) {
                        capability.getChallengeType().setTime(player.getPersistentData().getInt("challenge_time"));
                     }
                  }
               );
         }
      }
   }

   @SubscribeEvent
   public static void onEffectApplied(Added event) {
      if (event.getEntity() instanceof Player player) {
         if (player.level.isClientSide()) {
            return;
         }

         if (!player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            return;
         }

         if (event.getEffectInstance().getEffect() == TensuraMobEffects.PARALYSIS.get()) {
            getPlayerCapability(player)
               .ifPresent(
                  capability -> {
                     if (capability.getChallengeType() == null && getRandomInRange(1, 100) <= 1) {
                        challengeTypes.stream()
                           .filter(type -> type.getName().equals("kill_stunned"))
                           .findFirst()
                           .ifPresent(stunChallenge -> giveChallengeType(player, stunChallenge, false));
                     }
                  }
               );
         } else if (event.getEffectInstance().getEffect() == TensuraMobEffects.SELF_REGENERATION.get()
            || event.getEffectInstance().getEffect() == TensuraMobEffects.INSTANT_REGENERATION.get()) {
            getPlayerCapability(player).ifPresent(capability -> {
               if (capability.getChallengeType() != null) {
                  ChallengeType challengeType = capability.getChallengeType();
                  if (challengeType.getName().equals("kill_floor_boss_no_heal") || challengeType.getName().equals("kill_floor_boss_no_heal_no_resist")) {
                     failChallenge(player, challengeType);
                  }
               }
            });
         }
      }
   }

   @SubscribeEvent
   public static void onSkillToggleEvent(SkillToggleEvent event) {
      Player player = event.getEntity();
      if (!challengesDisabled(player.level)) {
         if (event.getSkillInstance().getSkill() instanceof ResistSkill && event.isToggleOn()) {
            getPlayerCapability(player)
               .ifPresent(
                  capability -> {
                     if (capability.getChallengeType() != null) {
                        ChallengeType challengeType = capability.getChallengeType();
                        if (challengeType.getName().equals("kill_floor_boss_no_resist") || challengeType.getName().equals("kill_floor_boss_no_heal_no_resist")) {
                           capability.setChallengeType(null);
                           BeyondPlayerCapability.sync(player);
                           player.sendSystemMessage(
                              Component.translatable("trbeyond.challenge.fail_challenge", new Object[]{challengeType.getName()})
                                 .withStyle(style -> style.withColor(TextColor.fromRgb(16711680)).withBold(true))
                           );
                        }
                     }
                  }
               );
         }
      }
   }

   @SubscribeEvent
   public static void onShrineEntityDeath(LivingDeathEvent event) {
      if (event.getEntity() instanceof ShrineEntity shrine) {
         BlockPos pos = BlockPos.of(shrine.getPersistentData().getLong("blockPos"));
         if (shrine.getLevel().getBlockState(pos).getBlock() instanceof ShrineBlock shrineBlock) {
            if (shrine.getPersistentData().getString("challenge").equals("Protect the Shrine")) {
               shrineBlock.failChallenge(shrine.getLevel(), pos, shrine.getLevel().getPlayerByUUID(UUID.fromString(shrine.getPersistentData().getString("player"))));
            } else if (shrine.getPersistentData().getString("challengeMob").equals("Destroy the Shrine")) {
               shrineBlock.completeChallenge(
                  shrine.getLevel().getBlockState(pos),
                  shrine.getLevel(),
                  pos,
                  shrine.getLevel().getPlayerByUUID(UUID.fromString(shrine.getPersistentData().getString("player"))),
                  true,
                  true
               );
            }
         }
      } else if (event.getEntity().getPersistentData().getString("challengeMob").equals("Defeat the Boss")) {
         BlockPos pos = BlockPos.of(event.getEntity().getPersistentData().getLong("blockPos"));
         if (event.getEntity().getLevel().getBlockState(pos).getBlock() instanceof ShrineBlock shrineBlockx) {
            shrineBlockx.completeChallenge(
               event.getEntity().getLevel().getBlockState(pos),
               event.getEntity().getLevel(),
               pos,
               event.getEntity().getLevel().getPlayerByUUID(UUID.fromString(event.getEntity().getPersistentData().getString("player"))),
               true,
               true
            );
         }
      }
   }

   @SubscribeEvent
   public static void onShrineEntityDamage(LivingHurtEvent event) {
      if (event.getEntity() instanceof ShrineEntity shrine && shrine.getPersistentData().getString("challenge").equals("Protect the Shrine")) {
         if (event.getSource().getEntity() instanceof Player) {
            event.setCanceled(true);
         } else if (event.getSource().getEntity() instanceof LivingEntity living) {
            if (!living.getPersistentData().getString("challengeMob").equals("Protect the Shrine")) {
               event.setCanceled(true);
            }
         } else {
            event.setCanceled(true);
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerTick(PlayerTickEvent event) {
      if (event.phase == Phase.END && !event.player.level.isClientSide()) {
         Player player = event.player;
         if (player.tickCount % 200 == 0) {
            getPlayerCapability(player).ifPresent(capability -> {
               if (capability.getChallengeType() != null) {
                  ChallengeType challengeType = capability.getChallengeType();
                  if (challengeType.getCategory().contains("shrine") && getRandomInRange(1, 100) <= 5 && player.getPersistentData().contains("shrineTier")) {
                     String shrineTier = player.getPersistentData().getString("shrineTier");
                     if (shrineTier.equals("stellar") && player.getPersistentData().getString("shrineChallenge").equals("waves")) {
                        int effectChance = getRandomInRange(1, 100);
                        if (effectChance <= 33) {
                           player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, getRandomInRange(100, 200), 0, false, false));
                        } else if (effectChance <= 66) {
                           player.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.PARALYSIS.get(), getRandomInRange(100, 200), 1, false, false));
                        } else {
                           player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, getRandomInRange(100, 200), 0, false, false));
                        }
                     }

                     if ((shrineTier.equals("gold") || shrineTier.equals("stellar")) && getRandomInRange(1, 3) <= 1) {
                        double angle = Math.random() * Math.PI * 2.0;
                        double distance = 5.0 + Math.random() * 10.0;
                        double knockbackX = Math.cos(angle) * distance;
                        double knockbackZ = Math.sin(angle) * distance;
                        player.push(knockbackX, 0.5, knockbackZ);
                        player.hurt(DamageSource.GENERIC, (float)getRandomInRange(1, 3));
                     }
                  }
               }
            });
         }
      }
   }

   @SubscribeEvent
   public static void giveOtherWorlderChallengeInRange(PlayerTickEvent event) {
      if (event.phase == Phase.END && !event.player.level.isClientSide() && !challengesDisabled(event.player.level)) {
         if (event.player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            if (event.player.tickCount % 200 == 0) {
               getPlayerCapability(event.player)
                  .ifPresent(
                     capability -> {
                        ChallengeType challengeType = capability.getChallengeType();
                        if (challengeType == null) {
                           long otherworlderCount = event.player
                              .level
                              .getEntitiesOfClass(LivingEntity.class, event.player.getBoundingBox().inflate(50.0, 0.0, 50.0))
                              .stream()
                              .filter(e -> e.getType().toString().matches(".*(mark_lauren|shinji_tanimura|shin_ryusei).*"))
                              .count();
                           if (otherworlderCount > 0L) {
                              giveChallengeType(event.player, "kill_otherworlders");
                           }
                        }
                     }
                  );
            }
         }
      }
   }

   @SubscribeEvent
   public static void checkForOtherWorlders(PlayerTickEvent event) {
      if (event.phase == Phase.END && !event.player.level.isClientSide() && !challengesDisabled(event.player.level)) {
         if (event.player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            if (event.player.tickCount % 200 == 0) {
               getPlayerCapability(event.player)
                  .ifPresent(
                     capability -> {
                        ChallengeType challengeType = capability.getChallengeType();
                        if (challengeType != null && challengeType.getName().equals("kill_otherworlders")) {
                           long otherworlderCount = event.player
                              .level
                              .getEntitiesOfClass(LivingEntity.class, event.player.getBoundingBox().inflate(50.0, 0.0, 50.0))
                              .stream()
                              .filter(e -> e.getType().toString().matches(".*(mark_lauren|shinji_tanimura|shin_ryusei).*"))
                              .count();
                           if (otherworlderCount == 0L) {
                              failChallenge(event.player, challengeType);
                              event.player
                                 .sendSystemMessage(
                                    Component.literal("Reason: You left the otherworlders behind!")
                                       .withStyle(style -> style.withColor(TextColor.fromRgb(16711680)))
                                 );
                           }
                        }
                     }
                  );
            }
         }
      }
   }

   @SubscribeEvent
   public static void giveDominatorsChallengeInRange(PlayerTickEvent event) {
      if (event.phase == Phase.END && !event.player.level.isClientSide() && !challengesDisabled(event.player.level)) {
         if (event.player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            if (event.player.tickCount % 200 == 0) {
               getPlayerCapability(event.player)
                  .ifPresent(
                     capability -> {
                        ChallengeType challengeType = capability.getChallengeType();
                        if (challengeType == null) {
                           long dominatorCount = event.player
                              .level
                              .getEntitiesOfClass(LivingEntity.class, event.player.getBoundingBox().inflate(50.0, 0.0, 50.0))
                              .stream()
                              .filter(e -> e.getType().toString().matches(".*(living_armor|milim_slime_scarlet|mage|skull_night).*"))
                              .count();
                           if (dominatorCount > 0L) {
                              giveChallengeType(event.player, "kill_dominators");
                           }
                        }
                     }
                  );
            }
         }
      }
   }

   @SubscribeEvent
   public static void checkForDominators(PlayerTickEvent event) {
      if (event.phase == Phase.END && !event.player.level.isClientSide() && !challengesDisabled(event.player.level)) {
         if (event.player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            if (event.player.tickCount % 200 == 0) {
               getPlayerCapability(event.player)
                  .ifPresent(
                     capability -> {
                        ChallengeType challengeType = capability.getChallengeType();
                        if (challengeType != null && challengeType.getName().equals("kill_dominators")) {
                           long dominatorCount = event.player
                              .level
                              .getEntitiesOfClass(LivingEntity.class, event.player.getBoundingBox().inflate(50.0, 0.0, 50.0))
                              .stream()
                              .filter(e -> e.getType().toString().matches(".*(living_armor|milim_slime_scarlet|mage|skull_night).*"))
                              .count();
                           if (dominatorCount == 0L) {
                              failChallenge(event.player, challengeType);
                              event.player
                                 .sendSystemMessage(
                                    Component.literal("Reason: You left the dominators behind!")
                                       .withStyle(style -> style.withColor(TextColor.fromRgb(16711680)))
                                 );
                           }
                        }
                     }
                  );
            }
         }
      }
   }

   @SubscribeEvent
   public static void minhSlimeChallenge(EntityLeaveLevelEvent event) {
      if (event.getLevel() instanceof ServerLevel serverLevel) {
         if (serverLevel.dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            if (event.getEntity() instanceof MinhSlimeEntity minhSlime) {
               if (minhSlime.getRemovalReason() == RemovalReason.DISCARDED) {
                  for (ServerPlayer player : serverLevel.players()) {
                     getPlayerCapability(player)
                        .ifPresent(
                           capability -> {
                              if (capability.getChallengeType() != null && "kill_minh_slime".equals(capability.getChallengeType().getName())) {
                                 failChallenge(player, capability.getChallengeType());
                                 player.sendSystemMessage(
                                    Component.literal("Reason: You let the Minh Slime escape!")
                                       .withStyle(style -> style.withColor(TextColor.fromRgb(16711680)).withBold(true))
                                 );
                              }
                           }
                        );
                  }
               }
            }
         }
      }
   }

   private static BlockPos findSafeSpawnPos(Level level, BlockPos center, int radius) {
      int y = center.getY();

      for (int attempts = 0; attempts < 20; attempts++) {
         int x = center.getX() + getRandomInRange(-radius, radius);
         int z = center.getZ() + getRandomInRange(-radius, radius);
         BlockPos pos = new BlockPos(x, y, z);
         boolean clear = level.isEmptyBlock(pos) && level.isEmptyBlock(pos.above());
         boolean solidBlock = level.getBlockState(pos.below()).isSolidRender(level, pos.below());
         if (clear && solidBlock) {
            return pos;
         }
      }

      return null;
   }

   public static int getRandomInRange(int min, int max) {
      return (int)(Math.random() * (double)(max - min + 1)) + min;
   }
}
