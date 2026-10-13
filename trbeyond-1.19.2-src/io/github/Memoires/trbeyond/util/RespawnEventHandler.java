package io.github.Memoires.trbeyond.util;

import io.github.Memoires.trbeyond.commands.ClearDungeonCommand;
import io.github.Memoires.trbeyond.entity.misc.MimicChestEntity;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.network.worldgen.portal.BeyondTeleporterUpdated;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import io.github.Memoires.trbeyond.world.BeyondGamerules;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber
public class RespawnEventHandler {
   @SubscribeEvent
   public static void onLivingDeath(LivingDeathEvent event) {
      LivingEntity entity = event.getEntity();
      if (entity.level instanceof ServerLevel serverWorld
         && event.getEntity().level.dimension() == BeyondDungeon.DUNGEON_LEVEL_KEY
         && !event.getSource().equals(DamageSource.OUT_OF_WORLD)) {
         int x = entity.getPersistentData().getInt("initialPosX");
         int y = entity.getPersistentData().getInt("initialPosY");
         int z = entity.getPersistentData().getInt("initialPosZ");
         BlockPos deathPos = new BlockPos(x, y, z);
         EntityType<?> entityType = entity.getType();
         ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
         List<LivingEntity> nearbyEntities = serverWorld.getEntitiesOfClass(LivingEntity.class, new AABB(deathPos).inflate(10.0));
         if (nearbyEntities.size() >= 10) {
            return;
         }

         Player player = getPlayer(event);
         int timer = 120;
         if (!entity.getPersistentData().getBoolean("hallwayEntity")) {
            timer = 1200;
         }

         boolean isBoss = entity.getPersistentData().getBoolean("isBoss");
         if (isBoss) {
            timer = 36000;
         }

         TaskScheduler.scheduleTask(new TaskScheduler.ScheduledTask(timer, () -> {
            boolean hallwayEnt = entity.getPersistentData().getBoolean("hallwayEntity");
            boolean respawn = entity.getPersistentData().getBoolean("respawn");
            if (respawn && !ClearDungeonCommand.dungeonCleared) {
               if (hallwayEnt) {
                  int spawnRate = BeyondGamerules.getInt(serverWorld, BeyondGamerules.AMBIENT_MOB_SPAWN_RATE);
                  if (Math.random() >= (double)spawnRate / 100.0) {
                     return;
                  }

                  int floorCount = entity.getPersistentData().getInt("floorCount");
                  if (player != null) {
                     floorCount = (player.getPersistentData().getInt("floor") - 1) * 2;
                  }

                  boolean treasureRoom = entity.getPersistentData().getBoolean("treasureRoom");
                  int entityInt;
                  if (floorCount < 7) {
                     if (treasureRoom) {
                        entityInt = getRandomInRange(5, 9);
                     } else {
                        entityInt = getRandomInRange(1, 4);
                     }
                  } else if (treasureRoom) {
                     entityInt = getRandomInRange(11, 12);
                  } else {
                     entityInt = getRandomInRange(5, 10);
                  }

                  floorCount = entity.getPersistentData().getInt("floorCount");
                  if (isBoss) {
                     if (entityType.equals(BeyondEntities.BOSS_BLACK_SPIDER.get())) {
                        BeyondTeleporterUpdated.spawnBlackSpiderAt(serverWorld, deathPos, hallwayEnt, floorCount, respawn);
                     } else if (entityType.equals(BeyondEntities.MEZUL.get())) {
                        BeyondTeleporterUpdated.spawnGozul(serverWorld, deathPos, hallwayEnt, floorCount, respawn);
                     } else if (entityType.equals(BeyondEntities.GOZUL.get())) {
                        BeyondTeleporterUpdated.spawnMezul(serverWorld, deathPos, hallwayEnt, floorCount, respawn);
                     } else if (entityType.equals(BeyondEntities.EVIL_CENTIPEDE_BOSS.get())) {
                        BeyondTeleporterUpdated.spawnEvilCentipede(serverWorld, deathPos, hallwayEnt, floorCount, respawn);
                     } else if (entityType.equals(BeyondEntities.TEMPEST_SERPENT_BOSS.get())) {
                        BeyondTeleporterUpdated.spawnTempestSerpant(serverWorld, deathPos, hallwayEnt, floorCount, respawn);
                     } else if (entityType.equals(BeyondEntities.OGRE_LORD.get())) {
                        BeyondTeleporterUpdated.spawnOgreLord(serverWorld, deathPos, hallwayEnt, floorCount, respawn);
                     }
                  } else if (entityInt == 1) {
                     BeyondTeleporterUpdated.spawnBulldeerAt(serverWorld, deathPos, hallwayEnt, floorCount, respawn);
                  } else if (entityInt == 2) {
                     BeyondTeleporterUpdated.spawnOneEyedOwlAt(serverWorld, deathPos, hallwayEnt, floorCount, respawn);
                  } else if (entityInt == 3) {
                     BeyondTeleporterUpdated.spawnDragonPeacockAt(serverWorld, deathPos, hallwayEnt, floorCount, respawn);
                  } else if (entityInt == 4) {
                     BeyondTeleporterUpdated.spawnMultipleSmallSlimes(serverWorld, deathPos, hallwayEnt, floorCount, respawn);
                  } else if (entityInt == 5) {
                     BeyondTeleporterUpdated.spawnBarghastAt(serverWorld, deathPos, hallwayEnt, floorCount, respawn);
                  } else if (entityInt == 6) {
                     BeyondTeleporterUpdated.spawnHornedRabbitsAt(serverWorld, deathPos, hallwayEnt, floorCount, respawn);
                  } else if (entityInt == 7) {
                     BeyondTeleporterUpdated.spawnHoundDogAt(serverWorld, deathPos, hallwayEnt, floorCount, respawn);
                  } else if (entityInt == 8) {
                     BeyondTeleporterUpdated.spawnHoverLizardAt(serverWorld, deathPos, hallwayEnt, floorCount, false);
                  } else if (entityInt == 9) {
                     int randomCount = getRandomInRange(3, 4);

                     for (int i = 0; i < randomCount; i++) {
                        BeyondTeleporterUpdated.spawnLeechLizardAt(serverWorld, deathPos, hallwayEnt, floorCount, false);
                     }
                  } else if (entityInt == 10) {
                     BeyondTeleporterUpdated.spawnDireworlfAt(serverWorld, deathPos, hallwayEnt, floorCount, true);
                  } else if (entityInt == 11) {
                     int randomCount = getRandomInRange(3, 4);

                     for (int i = 0; i < randomCount; i++) {
                        BeyondTeleporterUpdated.spawnGiantBatAt(serverWorld, deathPos, hallwayEnt, floorCount, true);
                     }
                  } else if (entityInt == 12) {
                     int randomCount = getRandomInRange(1, 2);

                     for (int i = 0; i < randomCount; i++) {
                        BeyondTeleporterUpdated.spawnGiantAntAt(serverWorld, deathPos, hallwayEnt, floorCount, true);
                     }
                  }
               }

               if (entityType.equals(EntityType.SLIME)) {
                  BeyondTeleporterUpdated.spawnSmallSlimeAt(serverWorld, deathPos, 0.0, 0.0, false, 0, true);
               } else if (id != null && id.getPath().equals("bulldeer")) {
                  BeyondTeleporterUpdated.spawnBulldeerAt(serverWorld, deathPos, false, 0, true);
               } else if (id != null && id.getPath().equals("one_eyed_owl")) {
                  BeyondTeleporterUpdated.spawnOneEyedOwlAt(serverWorld, deathPos, false, 0, true);
               } else if (id != null && id.getPath().equals("dragon_peacock")) {
                  BeyondTeleporterUpdated.spawnDragonPeacockAt(serverWorld, deathPos, false, 0, true);
               } else if (id != null && id.getPath().equals("barghest")) {
                  BeyondTeleporterUpdated.spawnBarghastAt(serverWorld, deathPos, false, 0, true);
               } else if (id != null && id.getPath().equals("horned_rabbit")) {
                  BeyondTeleporterUpdated.spawnHornedRabbitsAt(serverWorld, deathPos, false, 0, true);
               } else if (id != null && id.getPath().equals("hound_dog")) {
                  BeyondTeleporterUpdated.spawnHoundDogAt(serverWorld, deathPos, false, 0, true);
               } else if (id != null && id.getPath().equals("hover_lizard")) {
                  BeyondTeleporterUpdated.spawnHoverLizardAt(serverWorld, deathPos, false, 0, true);
               } else if (id != null && id.getPath().equals("leech_lizard")) {
                  BeyondTeleporterUpdated.spawnLeechLizardAt(serverWorld, deathPos, false, 0, true);
               } else if (id != null && id.getPath().equals("direwolf")) {
                  BeyondTeleporterUpdated.spawnDireworlfAt(serverWorld, deathPos, false, 0, true);
               } else if (id != null && id.getPath().equals("giant_bat")) {
                  BeyondTeleporterUpdated.spawnGiantBatAt(serverWorld, deathPos, false, 0, true);
               } else if (id != null && id.getPath().equals("giant_ant")) {
                  BeyondTeleporterUpdated.spawnGiantAntAt(serverWorld, deathPos, false, 0, true);
               } else if (entityType.equals(BeyondEntities.GOLD_MIMIC_CHEST.get())) {
                  spawnMimic(serverWorld, deathPos, true, true);
               } else if (entityType.equals(BeyondEntities.SILVER_MIMIC_CHEST.get())) {
                  spawnMimic(serverWorld, deathPos, true, false);
               }
            }
         }));
         return;
      }
   }

   @Nullable
   private static Player getPlayer(LivingDeathEvent event) {
      Player player;
      if (event.getSource().getEntity() instanceof Player p) {
         player = p;
      } else {
         player = null;
      }

      return player;
   }

   private static int getRandomInRange(int min, int max) {
      return min + (int)(Math.random() * (double)(max - min + 1));
   }

   public static void spawnMimic(ServerLevel world, BlockPos pos, boolean respawn, boolean gold) {
      MimicChestEntity entity;
      if (gold) {
         entity = (MimicChestEntity)((EntityType)BeyondEntities.GOLD_MIMIC_CHEST.get()).create(world);
      } else {
         entity = (MimicChestEntity)((EntityType)BeyondEntities.SILVER_MIMIC_CHEST.get()).create(world);
      }

      if (entity != null) {
         entity.getPersistentData().putBoolean("respawn", respawn);
         entity.getPersistentData().putInt("initialPosX", pos.getX());
         entity.getPersistentData().putInt("initialPosY", pos.getY());
         entity.getPersistentData().putInt("initialPosZ", pos.getZ());
         entity.setPersistenceRequired();
         entity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         world.addFreshEntity(entity);
      }
   }

   public static void spawnBoss(ServerLevel world, LivingEntity entity) {
      EntityType<?> entityType = entity.getType();
      if (entityType.equals(BeyondEntities.GOZUL.get())) {
         entityType = (EntityType<?>)BeyondEntities.MEZUL.get();
      } else if (entityType.equals(BeyondEntities.MEZUL.get())) {
         entityType = (EntityType<?>)BeyondEntities.GOZUL.get();
      }

      BlockPos pos = entity.blockPosition().immutable();
      Entity newEntity = entityType.create(world);
      if (newEntity != null) {
         newEntity.getPersistentData().putBoolean("isBoss", true);
         if (newEntity instanceof Mob mob) {
            mob.setPersistenceRequired();
            mob.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
            world.addFreshEntity(mob);
         }
      }
   }
}
