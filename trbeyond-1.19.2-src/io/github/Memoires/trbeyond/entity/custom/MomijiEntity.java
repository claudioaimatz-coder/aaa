package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.entity.magic.projectile.WindBladeProjectile;
import com.github.manasmods.tensura.entity.magic.projectile.WindSphereProjectile;
import com.github.manasmods.tensura.entity.template.HumanoidNPCEntity;
import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class MomijiEntity extends HumanoidNPCEntity implements IAnimatable {
   public static final AnimationBuilder IDLE = new AnimationBuilder().addAnimation("animation.momiji.idle", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder RUN = new AnimationBuilder().addAnimation("animation.momiji.run", EDefaultLoopTypes.LOOP);
   public static final AnimationBuilder SLEEP = new AnimationBuilder().addAnimation("animation.momiji.sleep", EDefaultLoopTypes.LOOP);
   public static final AnimationBuilder WALK = new AnimationBuilder().addAnimation("animation.momiji.walk", EDefaultLoopTypes.LOOP);
   public static final AnimationBuilder JUMP = new AnimationBuilder().addAnimation("animation.momiji.jump", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder FAN_ATTACK_NORMAL = new AnimationBuilder()
      .addAnimation("animation.momiji.fan_attack_normal", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder MELEE = new AnimationBuilder().addAnimation("animation.momiji.melee_attack", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder LARGE_ATTACK = new AnimationBuilder().addAnimation("animation.momiji.big_fan_attack", EDefaultLoopTypes.PLAY_ONCE);
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   public static final EntityDataAccessor<Integer> MISC_ANIMATION = SynchedEntityData.defineId(MomijiEntity.class, EntityDataSerializers.INT);
   public int miscAnimationTicks = 0;
   private int rangedCooldown = 0;
   private int largeAttackCooldown = 0;

   public MomijiEntity(EntityType<? extends MomijiEntity> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(MISC_ANIMATION, 0);
   }

   @Override
   public void tick() {
      super.tick();
      this.miscAnimationHandler();
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("MiscAnimation", this.getMiscAnimation());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.getEntityData().set(MISC_ANIMATION, compound.getInt("MiscAnimation"));
   }

   public static AttributeSupplier setAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 8000.0)
         .add(Attributes.ARMOR, 400.0)
         .add(Attributes.ATTACK_DAMAGE, 500.0)
         .add(Attributes.MOVEMENT_SPEED, 0.25)
         .add(Attributes.FOLLOW_RANGE, 64.0)
         .build();
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.7));
      this.goalSelector.addGoal(1, new MomijiEntity.MomijiAttackGoal(this));
      this.goalSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
   }

   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController(this, "controller", 0.0F, this::predicate));
      data.addAnimationController(new AnimationController(this, "attackcontroller", 0.0F, this::miscPredicate));
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      if (this.getMiscAnimation() == 0) {
         if (this.isSleeping()) {
            event.getController().setAnimation(SLEEP);
         } else if (event.isMoving()) {
            if (this.isAngry()) {
               event.getController().setAnimation(RUN);
            } else {
               event.getController().setAnimation(WALK);
            }
         } else if (!event.isMoving()
            && event.getController().getCurrentAnimation() != null
            && event.getController().getCurrentAnimation().loop == EDefaultLoopTypes.LOOP) {
            if (this.random.nextInt(1000) != 1 || this.isAngry()) {
               event.getController().clearAnimationCache();
               return PlayState.STOP;
            }

            event.getController().setAnimation(IDLE);
         }
      }

      return PlayState.CONTINUE;
   }

   protected void miscAnimationHandler() {
      if (this.getMiscAnimation() != 0) {
         this.miscAnimationTicks++;
         if (!this.isAlive()) {
            return;
         }

         if (!this.getLevel().isClientSide()) {
            if (this.getMiscAnimation() == 1 && (this.miscAnimationTicks == 18 || this.miscAnimationTicks == 26)) {
               LivingEntity target = this.getTarget();
               if (target != null) {
                  this.lookAt(Anchor.EYES, target.getEyePosition());
                  if (this.distanceToSqr(target) <= 64.0 && this.hasLineOfSight(target)) {
                     if (target.invulnerableTime <= 12 && target.getLastHurtByMob() == this) {
                        target.invulnerableTime = 0;
                     }

                     this.doHurtTarget(target);
                  }
               }
            }

            if (this.getMiscAnimation() == 2 && (this.miscAnimationTicks == 20 || this.miscAnimationTicks == 27)) {
               LivingEntity target = this.getTarget();
               if (target != null) {
                  this.lookAt(Anchor.EYES, target.getEyePosition());
                  windAttack(this);
               }
            }

            if (this.getMiscAnimation() == 3 && this.miscAnimationTicks == 12) {
               LivingEntity target = this.getTarget();
               if (target != null) {
                  this.lookAt(Anchor.EYES, target.getEyePosition());
                  this.jumpAway();
               }
            }

            if (this.getMiscAnimation() == 3 && this.miscAnimationTicks == 116) {
               LivingEntity target = this.getTarget();
               if (target != null) {
                  this.lookAt(Anchor.EYES, target.getEyePosition());
                  largeAttack(this);
               }
            }
         }

         if (this.miscAnimationTicks > this.getAnimationTick(this.getMiscAnimation())) {
            this.setMiscAnimation(0);
            this.miscAnimationTicks = 0;
         }
      }
   }

   private int getAnimationTick(int miscAnimation) {
      return switch (miscAnimation) {
         case 1 -> 40;
         case 2 -> 40;
         case 3 -> 140;
         default -> 100;
      };
   }

   @Override
   public boolean canSleep() {
      return !this.isNoAi();
   }

   @Override
   protected void sleepHandler() {
      if (!this.getLevel().isClientSide()) {
         if (!this.isSleeping() || this.getTarget() == null && !this.isInFluidType() && (++this.sleepingTime <= this.maxSleepTime || !this.getLevel().isDay())) {
            if (this.getLevel().isNight() && this.random.nextInt(100) == 0) {
               if (this.random.nextBoolean()) {
                  this.setSleeping(true);
               } else {
                  this.sleepingTime = 0;
                  this.maxSleepTime = 100 + this.random.nextInt(600);
               }
            }
         } else {
            this.setSleeping(false);
         }
      }
   }

   private <E extends IAnimatable> PlayState miscPredicate(AnimationEvent<E> event) {
      if (event.getController().getAnimationState().equals(AnimationState.Stopped)) {
         event.getController().markNeedsReload();
         switch (this.getMiscAnimation()) {
            case 1:
               event.getController().setAnimation(MELEE);
               break;
            case 2:
               event.getController().setAnimation(FAN_ATTACK_NORMAL);
               break;
            case 3:
               event.getController().setAnimation(LARGE_ATTACK);
         }
      }

      return PlayState.CONTINUE;
   }

   public int getMiscAnimation() {
      return (Integer)this.getEntityData().get(MISC_ANIMATION);
   }

   @Override
   public void setMiscAnimation(int animation) {
      if (this.getMiscAnimation() == 0 || animation == 0) {
         this.getEntityData().set(MISC_ANIMATION, animation);
      }
   }

   public static void windAttack(LivingEntity entity) {
      WindBladeProjectile blade = new WindBladeProjectile(entity.getLevel(), entity);
      float angle = (float) (Math.PI / 180.0) * entity.yBodyRot;
      double xOffset = (double)Mth.sin((float)(Math.PI + (double)angle));
      double zOffset = (double)Mth.cos(angle);
      blade.moveTo(entity.getX() + xOffset, entity.getEyeY() - 0.5, entity.getZ() + zOffset, entity.getYRot(), entity.getXRot());
      blade.setDamage((float)entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.5F);
      blade.setSpeed(5.0F);
      blade.setNoGravity(true);
      blade.shootFromRot(entity.getLookAngle());
      entity.level.addFreshEntity(blade);
      entity.playSound(SoundEvents.TRIDENT_THROW);
   }

   public static void largeAttack(LivingEntity entity) {
      WindSphereProjectile sphere = new WindSphereProjectile(entity.getLevel(), entity);
      float angle = (float) (Math.PI / 180.0) * entity.yBodyRot;
      double xOffset = (double)Mth.sin((float)(Math.PI + (double)angle));
      double zOffset = (double)Mth.cos(angle);
      sphere.moveTo(entity.getX() + xOffset, entity.getEyeY() - 0.5, entity.getZ() + zOffset, entity.getYRot(), entity.getXRot());
      sphere.setDamage((float)entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * 2.0F);
      sphere.setSpeed(3.0F);
      sphere.setNoGravity(true);
      sphere.shootFromRot(entity.getLookAngle());
      entity.level.addFreshEntity(sphere);
      entity.playSound(SoundEvents.TRIDENT_THROW);
   }

   public void jumpAway() {
      Vec3 jump = this.position().subtract(this.getTarget().position()).normalize().multiply(0.8, 0.0, 0.8).add(0.0, 0.6, 0.0);
      this.setDeltaMovement(jump);
      this.setJumping(true);
      this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP);
   }

   static class MomijiAttackGoal extends Goal {
      private final MomijiEntity momiji;

      public MomijiAttackGoal(MomijiEntity entity) {
         this.setFlags(EnumSet.of(Flag.MOVE));
         this.momiji = entity;
      }

      public boolean canUse() {
         LivingEntity target = this.momiji.getTarget();
         if (target instanceof Player player && player.isCreative()) {
            this.momiji.forgetCurrentTargetAndRefreshUniversalAnger();
            return false;
         }

         return target != null && target.isAlive();
      }

      public boolean canContinueToUse() {
         return this.canUse();
      }

      public void start() {
         this.momiji.setAggressive(true);
      }

      public void tick() {
         this.momiji.rangedCooldown--;
         this.momiji.largeAttackCooldown--;
         if (this.momiji.getMiscAnimation() == 0) {
            LivingEntity target = this.momiji.getTarget();
            if (target == null) {
               return;
            }

            double dist = this.momiji.distanceToSqr(target);
            double cooldownMultiplier = (double)(1.0F - (this.momiji.getMaxHealth() - this.momiji.getHealth()) / this.momiji.getMaxHealth() / 2.0F);
            if (dist > 100.0 && this.momiji.rangedCooldown <= 0 && this.momiji.hasLineOfSight(target)) {
               this.momiji.rangedCooldown = (int)(200.0 * cooldownMultiplier) + this.momiji.getAnimationTick(2);
               this.momiji.getNavigation().stop();
               this.momiji.setMiscAnimation(2);
            } else if (dist > 64.0 && this.momiji.largeAttackCooldown <= 0 && this.momiji.hasLineOfSight(target)) {
               this.momiji.largeAttackCooldown = (int)(400.0 * cooldownMultiplier) + this.momiji.getAnimationTick(3);
               this.momiji.getNavigation().stop();
               this.momiji.setMiscAnimation(3);
            } else if (dist < 25.0) {
               this.momiji.getNavigation().moveTo(target, this.momiji.getAttributes().getValue(Attributes.MOVEMENT_SPEED) * 3.0);
               this.momiji.setMiscAnimation(1);
            } else {
               Path path = this.momiji.getNavigation().getPath();
               if (path == null || 9.0 < path.getTarget().distToCenterSqr(target.position())) {
                  this.momiji.getNavigation().moveTo(target, this.momiji.getAttributes().getValue(Attributes.MOVEMENT_SPEED) * 9.0);
               }
            }
         }
      }
   }
}
