package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class SlimeDollHeadEntity extends Projectile implements IAnimatable {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

   public SlimeDollHeadEntity(EntityType<SlimeDollHeadEntity> type, Level level) {
      super(type, level);
   }

   public SlimeDollHeadEntity(Level level, double x, double y, double z) {
      this((EntityType<SlimeDollHeadEntity>)BeyondEntities.SLIME_DOLL_HEAD.get(), level);
      this.setPos(x, y, z);
   }

   protected void onHitEntity(EntityHitResult result) {
      if (!this.level.isClientSide) {
         result.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 5.0F);
         this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
      }
   }

   protected void onHitBlock(BlockHitResult result) {
      Vec3 motion = this.getDeltaMovement();
      switch (result.getDirection()) {
         case UP:
         case DOWN:
            motion = new Vec3(motion.x, -motion.y * 0.9, motion.z);
            break;
         case NORTH:
         case SOUTH:
            motion = new Vec3(motion.x, motion.y, -motion.z * 0.7);
            break;
         case EAST:
         case WEST:
            motion = new Vec3(-motion.x * 0.7, motion.y, motion.z);
      }

      motion = motion.scale(0.9);
      Vec3 hitLoc = result.getLocation();
      this.setPos(hitLoc.x + motion.x * 0.001, hitLoc.y + motion.y * 0.001, hitLoc.z + motion.z * 0.001);
      this.setDeltaMovement(motion);
   }

   public void tick() {
      super.tick();
      HitResult hit = ProjectileUtil.getHitResult(this, x$0 -> this.canHitEntity(x$0));
      if (hit.getType() != Type.MISS) {
         this.onHit(hit);
      }

      Vec3 motion = this.getDeltaMovement();
      if (!this.isNoGravity()) {
         motion = motion.add(0.0, -0.04, 0.0);
      }

      this.setDeltaMovement(motion);
      this.move(MoverType.SELF, motion);
      ProjectileUtil.rotateTowardsMovement(this, 0.2F);
      if (!this.level.isClientSide && this.tickCount >= 40 && this.tickCount <= 80) {
         this.level.explode(this, this.getX(), this.getY(), this.getZ(), 4.0F, BlockInteraction.NONE);
         double radius = 4.0;
         this.level.getEntities(this, this.getBoundingBox().inflate(radius)).forEach(entity -> {
            if (entity != this.getOwner() && entity.isAlive()) {
               entity.hurt(TensuraDamageSources.CORROSION, 500.0F);
            }
         });
         this.discard();
      }
   }

   protected void defineSynchedData() {
   }

   protected void addAdditionalSaveData(CompoundTag tag) {
   }

   protected void readAdditionalSaveData(CompoundTag tag) {
   }

   public Packet<?> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController(this, "controller", 0.0F, this::predicate));
   }

   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      return PlayState.CONTINUE;
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }
}
