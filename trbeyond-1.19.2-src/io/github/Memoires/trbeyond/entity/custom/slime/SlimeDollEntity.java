package io.github.Memoires.trbeyond.entity.custom.slime;

import com.github.manasmods.tensura.entity.template.TensuraTamableEntity;
import io.github.Memoires.trbeyond.items.armor.SerpentArmorItem;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class SlimeDollEntity extends TensuraTamableEntity {
   private static final EntityDataAccessor<Integer> MISC_ANIMATION = SynchedEntityData.defineId(SlimeDollEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> MELTED = SynchedEntityData.defineId(SlimeDollEntity.class, EntityDataSerializers.BOOLEAN);
   public int miscAnimationTicks = 0;

   public SlimeDollEntity(EntityType<? extends SlimeDollEntity> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(MISC_ANIMATION, 0);
      this.entityData.define(MELTED, Boolean.FALSE);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag pCompound) {
      super.addAdditionalSaveData(pCompound);
      pCompound.putInt("MiscAnimation", this.getMiscAnimation());
      pCompound.putBoolean("Melted", this.isMelted());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag pCompound) {
      super.readAdditionalSaveData(pCompound);
      this.entityData.set(MISC_ANIMATION, pCompound.getInt("MiscAnimation"));
      this.entityData.set(MELTED, pCompound.getBoolean("Melted"));
   }

   public int getMiscAnimation() {
      return (Integer)this.entityData.get(MISC_ANIMATION);
   }

   public void setMiscAnimation(int animation) {
      this.entityData.set(MISC_ANIMATION, animation);
   }

   public boolean isMelted() {
      return (Boolean)this.entityData.get(MELTED);
   }

   public void setMelted(boolean melted) {
      this.entityData.set(MELTED, melted);
   }

   @Override
   public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
      if (MELTED.equals(pKey)) {
         this.refreshDimensions();
      }

      super.onSyncedDataUpdated(pKey);
   }

   public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
      return false;
   }

   @Override
   public EntityDimensions getDimensions(Pose pPose) {
      EntityDimensions entitydimensions = super.getDimensions(pPose);
      return this.isMelted() ? entitydimensions.scale(1.0F, 0.3F) : entitydimensions;
   }

   public boolean hurt(DamageSource pSource, float pAmount) {
      boolean hurt = super.hurt(pSource, pAmount);
      if (hurt && pSource.getDirectEntity() instanceof LivingEntity living) {
         for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack slotStack = living.getItemBySlot(slot);
            if (!(slotStack.getItem() instanceof SerpentArmorItem)) {
               slotStack.hurtAndBreak(5, living, player -> player.broadcastBreakEvent(slot));
            }
         }
      }

      return hurt;
   }

   @Nullable
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag
   ) {
      this.populateDefaultEquipmentSlots(this.random, pDifficulty);
      return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
   }
}
