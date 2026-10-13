package io.github.Memoires.trbeyond.handlers;

import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.block.HipokuteGrass;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity;
import com.github.manasmods.tensura.event.NamingEvent;
import com.github.manasmods.tensura.event.SkillGriefEvent;
import com.github.manasmods.tensura.registry.items.TensuraToolItems;
import io.github.Memoires.trbeyond.capability.boss.BeyondBossCapability;
import io.github.Memoires.trbeyond.capability.dungeon.DungeonCapability;
import io.github.Memoires.trbeyond.capability.player.BeyondPlayerCapability;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.data.BeyondTags;
import io.github.Memoires.trbeyond.entity.custom.KillerDryadEntity;
import io.github.Memoires.trbeyond.network.worldgen.DungeonSaveData;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import io.github.Memoires.trbeyond.util.TimerUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.level.BlockEvent.BlockToolModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber
public class DungeonRulesHandler {
   public static final Map<UUID, BlockPos> playerPositions = new HashMap<>();
   public static final int CAMP_TIME = 12000;

   @SubscribeEvent
   public static void onWorldTick(LevelTickEvent event) {
      if (event.phase == Phase.END) {
         Level level = event.level;
         if (level.isClientSide()) {
            return;
         }

         if (level.dimension().equals(Level.OVERWORLD)) {
            DungeonSaveData saveData = DungeonSaveData.getOverworld(level);
            if (saveData == null) {
               return;
            }

            saveData.tickBossCooldown();
         }

         if (!level.dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            return;
         }

         for (Player player : level.players()) {
            if (!player.isCreative() && !player.isSpectator() && player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
               BlockPos currentPos = player.blockPosition();
               BlockPos lastPos = playerPositions.get(player.getUUID());
               TimerUtils.Timer timer = TimerUtils.getOrCreateTimer(player.getDisplayName().getString() + "_idle");
               if (player.getY() < 116.0) {
                  if (lastPos != null && isWithinArea(currentPos, lastPos)) {
                     switch (timer.getTicks()) {
                        case 6000:
                           player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                           player.displayClientMessage(Component.translatable("trbeyond.message.rule_violation.confirmed").withStyle(ChatFormatting.RED), true);
                           break;
                        case 10800:
                           player.displayClientMessage(Component.translatable("trbeyond.message.rule_violation.one_minute").withStyle(ChatFormatting.RED), true);
                           level.playSound(null, player.blockPosition(), SoundEvents.VILLAGER_NO, player.getSoundSource(), 1.0F, 1.0F);
                           KillerDryadEntity dryad = new KillerDryadEntity((EntityType<? extends TensuraTamableEntity>)BeyondEntities.KILLER_DRYAD.get(), level);
                           dryad.setPos(player.position());
                           dryad.tame(player);
                           dryad.teleportTowards(dryad, player, 5.0);
                           level.addFreshEntity(dryad);
                           break;
                        default:
                           if (timer.getTicks() >= 11600 && timer.getTicks() <= 12000) {
                              player.displayClientMessage(
                                 Component.translatable("trbeyond.message.rule_violation.seconds", new Object[]{(12000 - timer.getTicks()) / 20})
                                    .withStyle(ChatFormatting.RED),
                                 true
                              );
                           }
                     }
                  } else {
                     timer.start(12000, () -> {
                     });
                     playerPositions.put(player.getUUID(), currentPos);
                     player.removeEffect(MobEffects.CONFUSION);
                     timer.reset();
                  }
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void maxDungeonTime(LevelTickEvent event) {
      if (event.phase == Phase.END) {
         Level level = event.level;
         if (level.isClientSide()) {
            return;
         }

         if (level.dimension().equals(Level.OVERWORLD)) {
            DungeonSaveData saveData = DungeonSaveData.getOverworld(level);
            if (saveData == null) {
               return;
            }

            saveData.tickBossCooldown();
         }

         if (!level.dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            return;
         }

         if ((Integer)ServerConfig.INSTANCE.dungeonConfig.maxDungeonTime.get() == 0) {
            return;
         }

         for (Player player : new ArrayList(level.players())) {
            if (!player.isCreative() && !player.isSpectator() && player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
               TimerUtils.Timer timer = TimerUtils.getOrCreateTimer(player.getDisplayName().getString() + "_max");
               if (player.getY() < 116.0) {
                  if (!timer.active) {
                     timer.start((Integer)ServerConfig.INSTANCE.dungeonConfig.maxDungeonTime.get() * 20 * 60, () -> {
                     });
                  } else {
                     int maxTicks = (Integer)ServerConfig.INSTANCE.dungeonConfig.maxDungeonTime.get() * 20 * 60;
                     int ticks = timer.getTicks();
                     if (ticks == (int)((double)maxTicks * 0.8)) {
                        player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                        player.displayClientMessage(Component.translatable("trbeyond.message.dungeon_ejection").withStyle(ChatFormatting.RED), true);
                     } else if ((double)ticks == (double)maxTicks * 0.9) {
                        DungeonCapability.getFrom(player)
                           .ifPresent(
                              cap -> {
                                 BlockPos overworldCoords = cap.getOverworldCoords();
                                 level.getServer()
                                    .execute(
                                       () -> SkillHelper.moveAcrossDimensionTo(
                                             player,
                                             (double)overworldCoords.getX(),
                                             (double)overworldCoords.getY(),
                                             (double)overworldCoords.getZ(),
                                             player.getYRot(),
                                             player.getXRot(),
                                             level.getServer().m_129783_()
                                          )
                                    );
                              }
                           );
                     } else if (ticks >= maxTicks - 400 && ticks <= maxTicks) {
                        player.displayClientMessage(
                           Component.translatable("trbeyond.message.rule_violation.seconds", new Object[]{(maxTicks - ticks) / 20 - 6})
                              .withStyle(ChatFormatting.RED),
                           true
                        );
                     }
                  }
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerTick(PlayerTickEvent event) {
      if (event.phase == Phase.END) {
         if (!event.player.level.isClientSide) {
            event.player.getCapability(BeyondBossCapability.CAPABILITY).ifPresent(cap -> {
               long now = System.currentTimeMillis();
               if (cap.killedSpider() > 0 && now - cap.getSpiderLastKillTime() >= 3600000L) {
                  cap.setKilledSpider(0);
                  cap.setSpiderLastKillTime(0L);
               }

               if (cap.killedCentipede() > 0 && now - cap.getCentipedeLastKillTime() >= 3600000L) {
                  cap.setKilledCentipede(0);
                  cap.setCentipedeLastKillTime(0L);
               }

               if (cap.killedSerpent() > 0 && now - cap.getSerpentLastKillTime() >= 3600000L) {
                  cap.setKilledSerpent(0);
                  cap.setSerpentLastKillTime(0L);
               }

               if (cap.killedOgre() > 0 && now - cap.getOgreLastKillTime() >= 3600000L) {
                  cap.setKilledOgre(0);
                  cap.setOgreLastKillTime(0L);
               }

               if (cap.killedGozul() > 0 && now - cap.getGozulLastKillTime() >= 3600000L) {
                  cap.setKilledGozul(0);
                  cap.setGozulLastKillTime(0L);
               }

               if (cap.killedMezul() > 0 && now - cap.getMezulLastKillTime() >= 3600000L) {
                  cap.setKilledMezul(0);
                  cap.setMezulLastKillTime(0L);
               }
            });
            BeyondPlayerCapability.sync(event.player);
         }
      }
   }

   @SubscribeEvent
   public static void playerInBossRoom(LevelTickEvent event) {
      if (event.phase == Phase.END) {
         Level level = event.level;
         if (level.isClientSide()) {
            return;
         }

         if (level.dimension().equals(Level.OVERWORLD)) {
            DungeonSaveData saveData = DungeonSaveData.getOverworld(level);
            if (saveData == null) {
               return;
            }

            saveData.tickBossCooldown();
         }

         if (!level.dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            return;
         }

         for (Player player : level.players()) {
            if (!player.isCreative() && !player.isSpectator() && player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
               TimerUtils.Timer timer = TimerUtils.getOrCreateTimer(player.getDisplayName().getString() + "_boss");
               if (player.getY() <= 42.0) {
                  BeyondBossCapability.getFrom(player).ifPresent(cap -> {
                     if (cap.killedSpider() >= (Integer)ServerConfig.INSTANCE.bossConfig.bossKillCap.get()) {
                        player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                        player.displayClientMessage(Component.translatable("trbeyond.message.boss_rule_violation.confirmed").withStyle(ChatFormatting.RED), true);
                     }

                     if (cap.killedSpider() >= (Integer)ServerConfig.INSTANCE.bossConfig.bossKillCap.get() + 1) {
                        if (!timer.active) {
                           timer.start(20, () -> {
                           });
                        }

                        spawnDryad(player, level, timer);
                     }
                  });
               } else if (player.getY() >= 166.0 && player.getY() < 178.0) {
                  BeyondBossCapability.getFrom(player)
                     .ifPresent(
                        cap -> {
                           if (cap.killedCentipede() >= (Integer)ServerConfig.INSTANCE.bossConfig.bossKillCap.get()
                              || cap.killedOgre() >= (Integer)ServerConfig.INSTANCE.bossConfig.bossKillCap.get()
                              || cap.killedSerpent() >= (Integer)ServerConfig.INSTANCE.bossConfig.bossKillCap.get()
                              || cap.killedMezul() >= (Integer)ServerConfig.INSTANCE.bossConfig.bossKillCap.get()
                              || cap.killedGozul() >= (Integer)ServerConfig.INSTANCE.bossConfig.bossKillCap.get()) {
                              player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                              player.displayClientMessage(Component.translatable("trbeyond.message.boss_rule_violation.confirmed").withStyle(ChatFormatting.RED), true);
                           }

                           if (cap.killedCentipede() >= (Integer)ServerConfig.INSTANCE.bossConfig.bossKillCap.get() + 1
                              || cap.killedOgre() >= (Integer)ServerConfig.INSTANCE.bossConfig.bossKillCap.get() + 1
                              || cap.killedSerpent() >= (Integer)ServerConfig.INSTANCE.bossConfig.bossKillCap.get() + 1
                              || cap.killedMezul() >= (Integer)ServerConfig.INSTANCE.bossConfig.bossKillCap.get() + 1
                              || cap.killedGozul() >= (Integer)ServerConfig.INSTANCE.bossConfig.bossKillCap.get() + 1) {
                              if (!timer.active) {
                                 timer.start(200, () -> {
                                 });
                              }

                              spawnDryad(player, level, timer);
                           }
                        }
                     );
               } else {
                  timer.reset();
               }
            }
         }
      }
   }

   private static void spawnDryad(Player player, Level level, TimerUtils.Timer timer) {
      switch (timer.getTicks()) {
         case 40:
            KillerDryadEntity dryad = new KillerDryadEntity((EntityType<? extends TensuraTamableEntity>)BeyondEntities.KILLER_DRYAD.get(), level);
            dryad.setPos(player.position());
            dryad.tame(player);
            dryad.teleportTowards(dryad, player, 5.0);
            break;
         default:
            if (timer.getTicks() >= -380 && timer.getTicks() <= 20) {
            }
      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGH
   )
   public static void onSkillGrief(SkillGriefEvent event) {
      if (event.getSkillCaster() != null) {
         Level level = event.getSkillCaster().getLevel();
         if (level.dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            event.setCanceled(true);
         }
      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGH
   )
   public static void onNaming(NamingEvent event) {
      Player player = event.getNamer();
      LivingEntity named = event.getEntity();
      if (!(named instanceof Player)) {
         if (!player.isCreative() && !player.getMainHandItem().is((Item)TensuraToolItems.ORB_OF_DOMINATION.get())) {
            Level level = player.getLevel();
            if (level.dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
               player.displayClientMessage(Component.translatable("trbeyond.message.rule_violation.prohibited").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
               event.setCanceled(true);
            }
         }
      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGH
   )
   public static void onAnimalTame(AnimalTameEvent event) {
      Player player = event.getTamer();
      if (!player.isCreative() && !player.getMainHandItem().is((Item)TensuraToolItems.ORB_OF_DOMINATION.get())) {
         Level level = player.getLevel();
         if (level.dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            player.displayClientMessage(Component.translatable("trbeyond.message.rule_violation.prohibited").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
            event.setCanceled(true);
         }
      }
   }

   @SubscribeEvent
   public static void onBlockToolModification(BlockToolModificationEvent e) {
      Player player = e.getPlayer();
      if (player != null) {
         if (!player.isCreative()) {
            if (player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
               e.setCanceled(true);
            }
         }
      }
   }

   @SubscribeEvent
   public static void getBreakSpeed(BreakSpeed e) {
      Player player = e.getEntity();
      if (!player.isCreative()) {
         if (!(e.getState().getBlock() instanceof HipokuteGrass)) {
            if (player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
               if (!e.getState().is(BeyondTags.DUNGEON_PLACEABLE)) {
                  e.setCanceled(true);
                  e.setNewSpeed(0.0F);
               }
            }
         }
      }
   }

   public static boolean isWithinArea(BlockPos pos1, @NotNull BlockPos pos2) {
      return Math.abs(pos1.getX() - pos2.getX()) <= 10
         && Math.abs(pos1.getZ() - pos2.getZ()) <= 10
         && Math.abs(pos1.getY() - pos2.getY()) <= 10;
   }
}
