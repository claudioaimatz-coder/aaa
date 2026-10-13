package io.github.Memoires.trbeyond.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

public class HitboxPartEntity<T extends LivingEntity> extends PartEntity<T> {
   public final T parentEntity;
   private final EntityDimensions size;
   private Vec3 offsets;

   public HitboxPartEntity(T parent, float width, float height, Vec3 offsets) {
      super(parent);
      this.size = EntityDimensions.scalable(width, height);
      this.offsets = offsets;
      this.refreshDimensions();
      this.parentEntity = parent;
   }

   public HitboxPartEntity(T parent, float width, float height, double forwardOffset, double verticalOffset, double sidewaysOffset) {
      this(parent, width, height, new Vec3(forwardOffset, sidewaysOffset, verticalOffset));
   }

   public void updatePosition() {
      this.xOld = this.getX();
      this.yOld = this.getY();
      this.zOld = this.getZ();
      float yRotRadians = this.parentEntity.getVisualRotationYInDegrees() * (float) Math.PI / 180.0F;
      float f1 = -Mth.sin(yRotRadians);
      float f2 = Mth.cos(yRotRadians);
      float s1 = -Mth.sin(yRotRadians + (float) (Math.PI / 2));
      float s2 = Mth.cos(yRotRadians + (float) (Math.PI / 2));
      Vec3 originPos = this.parentEntity.getPosition(0.0F);
      this.setPos(
         originPos.x + (double)f1 * this.offsets.x + (double)s1 * this.offsets.z,
         originPos.y + this.offsets.y,
         originPos.z + (double)f2 * this.offsets.x + (double)s2 * this.offsets.z
      );
   }

   public boolean isPickable() {
      return true;
   }

   public boolean hurt(DamageSource pSource, float pAmount) {
      return this.isInvulnerableTo(pSource) ? false : this.parentEntity.hurt(pSource, pAmount);
   }

   public boolean is(Entity pEntity) {
      return this == pEntity || this.parentEntity == pEntity;
   }

   public EntityDimensions getDimensions(Pose pPose) {
      return this.size;
   }

   public boolean shouldBeSaved() {
      return false;
   }

   protected void defineSynchedData() {
   }

   protected void readAdditionalSaveData(CompoundTag pCompound) {
   }

   protected void addAdditionalSaveData(CompoundTag pCompound) {
   }
}
