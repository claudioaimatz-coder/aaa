package io.github.Memoires.trbeyond.block.entity;

import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.entity.custom.GozulEntity;
import io.github.Memoires.trbeyond.entity.custom.MezulEntity;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;

public class RespawnBossBlockEntity extends BlockEntity {
   private static final double RADIUS = 40.0;
   private static final int CHECK_INTERVAL = (Integer)ServerConfig.INSTANCE.spawnConfig.bossRespawnTime.get();
   private int tickCounter = 0;
   private EntityType<? extends LivingEntity> selectedBoss;

   public RespawnBossBlockEntity(BlockPos pos, BlockState state) {
      super((BlockEntityType)BeyondBlockEntities.BOSS_SPAWNER.get(), pos, state);
   }

   public void load(CompoundTag tag) {
      super.load(tag);
      if (tag.contains("BossType", 8)) {
         String bossIdString = tag.getString("BossType");
         ResourceLocation id = new ResourceLocation(bossIdString);
         EntityType<?> rawType = (EntityType<?>)ForgeRegistries.ENTITY_TYPES.getValue(id);
         if (rawType != null) {
            this.selectedBoss = (EntityType<? extends LivingEntity>)rawType;
         }
      }

      if (this.selectedBoss == null) {
         this.selectedBoss = (EntityType<? extends LivingEntity>)BeyondEntities.MEZUL.get();
      }
   }

   protected void saveAdditional(CompoundTag tag) {
      super.saveAdditional(tag);
      if (this.selectedBoss != null) {
         ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(this.selectedBoss);
         if (id != null) {
            tag.putString("BossType", id.toString());
         }
      }
   }

   public static void tick(Level level, BlockPos pos, BlockState state, RespawnBossBlockEntity blockEntity) {
      if (!level.isClientSide) {
         blockEntity.tickCounter++;
         if (blockEntity.tickCounter >= CHECK_INTERVAL * 60 * 20) {
            blockEntity.tickCounter = 0;
            if (level instanceof ServerLevel serverLevel) {
               blockEntity.checkAndSpawnBoss(serverLevel, pos);
            }
         }
      }
   }

   public void setBoss(EntityType<? extends LivingEntity> newBoss) {
      this.selectedBoss = newBoss;
      this.setChanged();
   }

   private void checkAndSpawnBoss(ServerLevel world, BlockPos pos) {
      double x = (double)pos.getX() + 0.5;
      double y = (double)pos.getY() + 0.5;
      double z = (double)pos.getZ() + 0.5;
      AABB scanArea = new AABB(x - 40.0, y - 40.0, z - 40.0, x + 40.0, y + 40.0, z + 40.0);
      boolean bossFound = false;
      if (this.selectedBoss != null) {
         if (this.selectedBoss != BeyondEntities.MEZUL.get() && this.selectedBoss != BeyondEntities.GOZUL.get()) {
            bossFound = world.getEntitiesOfClass(LivingEntity.class, scanArea).stream().anyMatch(entity -> entity.getType() == this.selectedBoss);
         } else {
            bossFound = world.getEntitiesOfClass(LivingEntity.class, scanArea)
               .stream()
               .anyMatch(entity -> entity instanceof GozulEntity || entity instanceof MezulEntity);
         }
      }

      if (!bossFound) {
         this.spawnBoss(world, x, y + 2.0, z);
      }
   }

   private void spawnBoss(ServerLevel world, double x, double y, double z) {
      if (this.selectedBoss != null) {
         LivingEntity boss = (LivingEntity)this.selectedBoss.create(world);
         if (boss != null) {
            boss.moveTo(x, y, z, world.random.nextFloat() * 360.0F, 0.0F);
            if (boss instanceof GozulEntity || boss instanceof MezulEntity) {
               Random random = new Random();
               int roll = random.nextInt(2);
               if (roll == 0) {
                  LivingEntity gozul = new GozulEntity((EntityType<GozulEntity>)BeyondEntities.GOZUL.get(), world);
                  gozul.moveTo(x, y, z, world.random.nextFloat() * 360.0F, 0.0F);
                  if (gozul instanceof Mob mob) {
                     mob.finalizeSpawn(world, world.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.EVENT, null, null);
                  }

                  world.addFreshEntity(gozul);
                  return;
               }

               LivingEntity mezul = new MezulEntity((EntityType<MezulEntity>)BeyondEntities.MEZUL.get(), world);
               mezul.moveTo(x, y, z, world.random.nextFloat() * 360.0F, 0.0F);
               if (mezul instanceof Mob mob) {
                  mob.finalizeSpawn(world, world.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.EVENT, null, null);
               }

               world.addFreshEntity(mezul);
               return;
            }

            if (boss instanceof Mob mob) {
               mob.finalizeSpawn(world, world.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.EVENT, null, null);
            }

            world.addFreshEntity(boss);
         }
      }
   }
}
