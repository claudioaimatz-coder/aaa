package io.github.Memoires.trbeyond.procedures;

import io.github.Memoires.trbeyond.entity.custom.LivingArmorEntity;
import io.github.Memoires.trbeyond.entity.custom.MageEntity;
import io.github.Memoires.trbeyond.entity.custom.MilimSlimeScarletEntity;
import io.github.Memoires.trbeyond.entity.custom.SkullNightEntity;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;

public class DungeonDominatorsRightclickedOnBlockProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z) {
      y++;
      if (world instanceof ServerLevel _level) {
         Entity entityToSpawn = new MageEntity((EntityType<MageEntity>)BeyondEntities.MAGE.get(), _level);
         entityToSpawn.moveTo(x, y, z, 0.0F, 0.0F);
         entityToSpawn.setYBodyRot(0.0F);
         entityToSpawn.setYHeadRot(0.0F);
         entityToSpawn.setDeltaMovement(0.0, 0.0, 0.0);
         if (entityToSpawn instanceof Mob _mobToSpawn) {
            _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
         }

         world.addFreshEntity(entityToSpawn);
      }

      if (world instanceof ServerLevel _level) {
         Entity entityToSpawn = new LivingArmorEntity((EntityType<LivingArmorEntity>)BeyondEntities.LIVING_ARMOR.get(), _level);
         entityToSpawn.moveTo(x, y, z, 0.0F, 0.0F);
         entityToSpawn.setYBodyRot(0.0F);
         entityToSpawn.setYHeadRot(0.0F);
         entityToSpawn.setDeltaMovement(0.0, 0.0, 0.0);
         if (entityToSpawn instanceof Mob _mobToSpawn) {
            _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
         }

         world.addFreshEntity(entityToSpawn);
      }

      if (world instanceof ServerLevel _level) {
         Entity entityToSpawn = new MilimSlimeScarletEntity((EntityType<MilimSlimeScarletEntity>)BeyondEntities.MILIM_SLIME_SCARLET.get(), _level);
         entityToSpawn.moveTo(x, y, z, 0.0F, 0.0F);
         entityToSpawn.setYBodyRot(0.0F);
         entityToSpawn.setYHeadRot(0.0F);
         entityToSpawn.setDeltaMovement(0.0, 0.0, 0.0);
         if (entityToSpawn instanceof Mob _mobToSpawn) {
            _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
         }

         world.addFreshEntity(entityToSpawn);
      }

      if (world instanceof ServerLevel _level) {
         Entity entityToSpawn = new SkullNightEntity((EntityType<SkullNightEntity>)BeyondEntities.SKULL_NIGHT.get(), _level);
         entityToSpawn.moveTo(x, y, z, 0.0F, 0.0F);
         entityToSpawn.setYBodyRot(0.0F);
         entityToSpawn.setYHeadRot(0.0F);
         entityToSpawn.setDeltaMovement(0.0, 0.0, 0.0);
         if (entityToSpawn instanceof Mob _mobToSpawn) {
            _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
         }

         world.addFreshEntity(entityToSpawn);
      }
   }
}
