package io.github.Memoires.trbeyond.util;

import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.entity.DirewolfEntity;
import com.github.manasmods.tensura.entity.SlimeEntity;
import com.github.manasmods.tensura.entity.SupermassiveSlimeEntity;
import com.github.manasmods.tensura.entity.variant.DirewolfVariant;
import io.github.Memoires.trbeyond.config.FloorSpawnConfig;
import io.github.Memoires.trbeyond.config.ServerConfig;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.util.random.WeightedEntry.Wrapper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

public class BeyondSpawnUtils {
   public static void spawnMobsAt(ServerLevel world, BlockPos pos, boolean hallwayEntity, boolean respawn, boolean treasure) {
      int floorCount = GetFloorFromBlockPos(pos);
      FloorSpawnConfig.SpawnParams type = ChooseEntity(floorCount, treasure, world.getRandom());
      if (type != null) {
         int amount = getRandomInRange(type.minCount(), type.maxCount());
         spawnMob(pos, hallwayEntity, floorCount, respawn, type.entityType(), amount, world, treasure);
      }
   }

   public static void spawnMob(
      BlockPos pos, boolean hallwayEntity, int floorCount, boolean respawn, EntityType<?> type, int amount, ServerLevel world, boolean treasureRoom
   ) {
      for (int i = 0; i < amount; i++) {
         Entity entity = type.create(world);
         if (entity != null) {
            if (entity instanceof SlimeEntity) {
               SlimeEntity slime = (SlimeEntity)entity;
               if (!(entity instanceof SupermassiveSlimeEntity)) {
                  int random_size = getRandomInRange(1, 4);
                  slime.setSize(random_size, true);
               }
            }

            if (entity instanceof Mob) {
               Mob mob = (Mob)entity;
               mob.setPersistenceRequired();
               if (entity instanceof NeutralMob) {
                  mob.targetSelector.removeAllGoals();
                  List<WrappedGoal> goals = mob.goalSelector.getAvailableGoals().stream().toList();
                  boolean hasAttackGoal = false;

                  for (WrappedGoal goal : goals) {
                     Goal goalType = goal.getGoal();
                     if (goalType instanceof MeleeAttackGoal || goalType instanceof RangedAttackGoal) {
                        hasAttackGoal = true;
                     }
                  }

                  if (!hasAttackGoal) {
                     mob.goalSelector.addGoal(1, new MeleeAttackGoal((PathfinderMob)mob, 2.0, true));
                  }

                  mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal(mob, Player.class, true));
               }
            }

            if (10 < floorCount && floorCount < 20 && entity instanceof DirewolfEntity) {
               DirewolfEntity direwolfEntity = (DirewolfEntity)entity;
               float rand = world.getRandom().nextFloat();
               if ((double)rand < 0.1) {
                  direwolfEntity.setVariant(DirewolfVariant.MYSTIC_WIND_WOLF);
               } else if ((double)rand < 0.4) {
                  direwolfEntity.setVariant(DirewolfVariant.GREEN_FANG);
               }
            }

            entity.getPersistentData().putBoolean("hallwayEntity", hallwayEntity);
            entity.getPersistentData().putInt("floorCount", floorCount);
            entity.getPersistentData().putBoolean("respawn", respawn);
            entity.getPersistentData().putInt("initialPosX", pos.getX());
            entity.getPersistentData().putInt("initialPosY", pos.getY());
            entity.getPersistentData().putInt("initialPosZ", pos.getZ());
            entity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
            world.addFreshEntity(entity);
            if (entity instanceof LivingEntity livingEntity) {
               IncreaseEntityEPBasedOnFloor(livingEntity, floorCount);
            }
         }
      }
   }

   public static FloorSpawnConfig.SpawnParams ChooseEntity(int floor, boolean treasure, RandomSource random) {
      WeightedRandomList<Wrapper<FloorSpawnConfig.SpawnParams>> mobs;
      if (treasure) {
         mobs = ServerConfig.INSTANCE.floorSpawnConfig.getTreasureFloorRandomEntryList(floor);
      } else {
         mobs = ServerConfig.INSTANCE.floorSpawnConfig.getFloorRandomEntryList(floor);
      }

      return !mobs.isEmpty()
         ? mobs.getRandom(random).<FloorSpawnConfig.SpawnParams>map(Wrapper::getData).orElse(null)
         : new FloorSpawnConfig.SpawnParams(EntityType.BAT, 0, 1);
   }

   private static int getRandomInRange(int min, int max) {
      return min + (int)(Math.random() * (double)(max - min + 1));
   }

   public static int GetFloorFromBlockPos(BlockPos pos) {
      int y = pos.getY();
      int floorMajorComponent = 0;
      int floorMinorComponent = 0;
      floorMajorComponent = Math.max(floorMajorComponent, 0);
      floorMajorComponent = Math.min(floorMajorComponent, 9);
      switch (floorMajorComponent) {
         case 0:
            if (y > 190) {
               return 95;
            } else {
               floorMinorComponent = (int)((143.0F - (float)y) / 16.0F);
               if (floorMinorComponent > 10) {
                  floorMinorComponent = (int)((93.0F - (float)y) / 12.0F);
                  floorMinorComponent = Math.max(floorMinorComponent, 11);
               }

               floorMinorComponent = Math.max(floorMinorComponent, 1);
               floorMinorComponent = Math.min(floorMinorComponent, 20);
            }
         default:
            return floorMinorComponent + floorMajorComponent * 10;
      }
   }

   public static void IncreaseEntityEPBasedOnFloor(LivingEntity livingEntity, int floorCount) {
      double newEP = TensuraEPCapability.getEP(livingEntity) * (1.0 + (double)floorCount * 0.05);
      TensuraEPCapability.setLivingEP(livingEntity, newEP);
      TensuraEPCapability.healSpiritualHealth(livingEntity, Double.MAX_VALUE);
      TensuraEPCapability.setCurrentLivingEP(livingEntity, newEP);
   }
}
