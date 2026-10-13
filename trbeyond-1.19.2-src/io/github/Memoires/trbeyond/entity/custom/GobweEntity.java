package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.entity.template.HumanoidNPCEntity;
import com.github.manasmods.tensura.entity.template.HumanoidNPCEntity.NPCMeleeAttackGoal;
import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
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

public class GobweEntity extends HumanoidNPCEntity implements IAnimatable {
   private static final EntityDataAccessor<Integer> MISC_ANIMATION = SynchedEntityData.defineId(GobweEntity.class, EntityDataSerializers.INT);
   public static final AnimationBuilder idle1 = new AnimationBuilder().addAnimation("idle1", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder idle2 = new AnimationBuilder().addAnimation("idle2", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder idle3 = new AnimationBuilder().addAnimation("idle3", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder sleep = new AnimationBuilder().addAnimation("sleep", EDefaultLoopTypes.LOOP);
   public static final AnimationBuilder walk = new AnimationBuilder().addAnimation("walk", EDefaultLoopTypes.LOOP);
   public static final AnimationBuilder run = new AnimationBuilder().addAnimation("run", EDefaultLoopTypes.LOOP);
   public static final AnimationBuilder jump = new AnimationBuilder().addAnimation("jump", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder dagger_strike = new AnimationBuilder().addAnimation("dagger_strike", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder double_dagger_strike = new AnimationBuilder().addAnimation("double_dagger_strike", EDefaultLoopTypes.PLAY_ONCE);
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   public int miscAnimationTicks = 0;
   private static final double WALK_SPEED = 0.3;
   private static final double RUN_SPEED = 0.6;
   private boolean isRunning = false;
   private int movementStateTicks = 0;
   private static final EntityDataAccessor<Boolean> DAGGER_STRIKE = SynchedEntityData.defineId(GobweEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> DOUBLE_DAGGER_STRIKE = SynchedEntityData.defineId(GobweEntity.class, EntityDataSerializers.BOOLEAN);

   public GobweEntity(EntityType<? extends GobweEntity> type, Level level) {
      super(type, level);
   }

   @Override
   public void tick() {
      super.tick();
      this.miscAnimationHandler();
      this.updateMovementSpeed();
      this.updateMovementState();
      if (!this.level.isClientSide() && this.tickCount % 20 == 0) {
         Vec3 velocity = this.getDeltaMovement();
         double speed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
         if (speed > 0.01) {
            System.out
               .println(
                  "Entity moving - Speed: "
                     + speed
                     + ", OnGround: "
                     + this.onGround
                     + ", Target: "
                     + (this.getTarget() != null)
                     + ", IsRunning: "
                     + this.isRunning
               );
         }
      }
   }

   private void updateMovementState() {
      Vec3 velocity = this.getDeltaMovement();
      double speed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
      boolean hasPath = this.getNavigation().getPath() != null && !this.getNavigation().isDone();
      boolean shouldRun = this.getTarget() != null && this.isAggressive() || this.isAngry();
      boolean targetRunning = shouldRun && (speed > 0.02 || hasPath) && this.onGround;
      if (targetRunning != this.isRunning) {
         this.movementStateTicks++;
         if (this.movementStateTicks > 2) {
            this.isRunning = targetRunning;
            this.movementStateTicks = 0;
            System.out.println("Movement state changed - IsRunning: " + this.isRunning + ", Speed: " + speed + ", ShouldRun: " + shouldRun);
         }
      } else {
         this.movementStateTicks = 0;
      }
   }

   public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
      return false;
   }

   @Override
   public boolean doHurtTarget(Entity target) {
      boolean result = super.doHurtTarget(target);
      if (!this.level.isClientSide()) {
         System.out.println("Attempted damage to " + target + " - Success: " + result + " at tick: " + this.miscAnimationTicks);
      }

      return result;
   }

   public boolean forceHurtTarget(Entity target) {
      if (target instanceof LivingEntity livingTarget) {
         float damage = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
         DamageSource damageSource = DamageSource.mobAttack(this);
         int originalInvulnerableTime = livingTarget.invulnerableTime;
         livingTarget.invulnerableTime = 0;
         boolean result = livingTarget.hurt(damageSource, damage);
         if (!this.level.isClientSide() && result) {
            System.out.println("Force damage applied: " + damage + " to " + target);
         }

         return result;
      } else {
         return super.doHurtTarget(target);
      }
   }

   private void updateMovementSpeed() {
      if (this.getTarget() != null && this.isAggressive() && !this.isSleeping()) {
         if (this.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue() != 0.6) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.6);
         }
      } else if (this.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue() != 0.3) {
         this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3);
      }
   }

   public static AttributeSupplier setAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 200.0)
         .add(Attributes.FOLLOW_RANGE, 24.0)
         .add(Attributes.MOVEMENT_SPEED, 0.3)
         .add(Attributes.ARMOR_TOUGHNESS, 0.1F)
         .add(Attributes.ATTACK_KNOCKBACK, 0.5)
         .add(Attributes.ATTACK_DAMAGE, 2.0)
         .add(Attributes.ATTACK_SPEED, 4.0)
         .add(Attributes.ATTACK_KNOCKBACK)
         .build();
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new GobweEntity.GobweAttackGoal(this));
      this.goalSelector.addGoal(2, new GobweEntity.CustomMeleeGoal(2.0, true));
      this.goalSelector.addGoal(3, new GobweEntity.MoveToAttackerGoal(this));
      this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.7));
      this.goalSelector.addGoal(7, new HurtByTargetGoal(this, new Class[0]));
   }

   public int getMiscAnimation() {
      return (Integer)this.entityData.get(MISC_ANIMATION);
   }

   @Override
   public void setMiscAnimation(int tick) {
      this.entityData.set(MISC_ANIMATION, tick);
      if (tick == 0) {
         this.miscAnimationTicks = 0;
      }
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(MISC_ANIMATION, 0);
      this.entityData.define(DAGGER_STRIKE, false);
      this.entityData.define(DOUBLE_DAGGER_STRIKE, false);
   }

   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      if (this.getMiscAnimation() != 0) {
         return PlayState.STOP;
      } else {
         if (this.isSleeping()) {
            event.getController().setAnimation(sleep);
         } else {
            Vec3 velocity = this.getDeltaMovement();
            double speed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
            boolean hasPath = this.getNavigation().getPath() != null && !this.getNavigation().isDone();
            boolean isActuallyMoving = (speed > 0.02 || hasPath) && this.onGround;
            if (isActuallyMoving) {
               boolean shouldRunAnimation = this.getTarget() != null && this.isAggressive() || this.isAngry();
               if (shouldRunAnimation) {
                  event.getController().setAnimation(run);
                  System.out.println("Playing RUN animation - Speed: " + speed + ", HasTarget: " + (this.getTarget() != null));
               } else {
                  event.getController().setAnimation(walk);
                  System.out.println("Playing WALK animation - Speed: " + speed);
               }
            } else {
               boolean shouldPlayIdle = event.getController().getCurrentAnimation() == null
                  || event.getController().getAnimationState() == AnimationState.Stopped
                  || !event.getController().getCurrentAnimation().animationName.equals("walk")
                     && !event.getController().getCurrentAnimation().animationName.equals("run");
               if (!shouldPlayIdle) {
                  return PlayState.STOP;
               }

               int random = this.random.nextInt(300);
               if (random == 1 && !this.isAngry()) {
                  event.getController().setAnimation(idle1);
               } else if (random == 2 && !this.isAngry()) {
                  event.getController().setAnimation(idle2);
               } else {
                  if (random != 3 || this.isAngry()) {
                     return PlayState.STOP;
                  }

                  event.getController().setAnimation(idle3);
               }
            }
         }

         return PlayState.CONTINUE;
      }
   }

   protected void miscAnimationHandler() {
      if (this.getMiscAnimation() != 0) {
         this.miscAnimationTicks++;
         if (!this.level.isClientSide() && this.isAlive()) {
            LivingEntity target = this.getTarget();
            switch (this.getMiscAnimation()) {
               case 1:
                  if (this.miscAnimationTicks == 18) {
                     this.entityData.set(DAGGER_STRIKE, true);
                     if (target != null) {
                        this.doHurtTarget(target);
                     }
                  }

                  if (this.miscAnimationTicks == 19) {
                     this.entityData.set(DAGGER_STRIKE, false);
                  }
                  break;
               case 2:
                  if (this.miscAnimationTicks == 12) {
                     this.jumpAway();
                  }
                  break;
               case 3:
                  if (this.miscAnimationTicks == 12) {
                     this.entityData.set(DOUBLE_DAGGER_STRIKE, true);
                     if (target != null && target.isAlive()) {
                        this.doHurtTarget(target);
                        System.out.println("First dagger hit at tick: " + this.miscAnimationTicks);
                     }
                  }

                  if (this.miscAnimationTicks == 13) {
                     this.entityData.set(DOUBLE_DAGGER_STRIKE, false);
                  }

                  if (this.miscAnimationTicks == 18) {
                     this.entityData.set(DOUBLE_DAGGER_STRIKE, true);
                     if (target != null && target.isAlive()) {
                        this.forceHurtTarget(target);
                        System.out.println("Second dagger hit (forced) at tick: " + this.miscAnimationTicks);
                     }
                  }

                  if (this.miscAnimationTicks == 19) {
                     this.entityData.set(DOUBLE_DAGGER_STRIKE, false);
                  }
            }

            if (this.miscAnimationTicks >= this.getAnimationDuration(this.getMiscAnimation())) {
               this.setMiscAnimation(0);
               this.entityData.set(DAGGER_STRIKE, false);
               this.entityData.set(DOUBLE_DAGGER_STRIKE, false);
               this.miscAnimationTicks = 0;
            }
         }
      }
   }

   private int getAnimationDuration(int variant) {
      return switch (variant) {
         case 1, 2 -> 30;
         case 3 -> 35;
         default -> 0;
      };
   }

   public void jumpAway() {
      LivingEntity target = this.getTarget();
      if (target != null) {
         Vec3 jump = this.position().subtract(target.position()).normalize().multiply(0.8, 0.0, 0.8).add(0.0, 0.6, 0.0);
         this.setDeltaMovement(jump);
         this.setJumping(true);
      }
   }

   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController(this, "attackController", 0.0F, this::miscPredicate));
      data.addAnimationController(new AnimationController(this, "controller", 2.0F, this::predicate));
   }

   private <E extends IAnimatable> PlayState miscPredicate(AnimationEvent<E> event) {
      int anim = this.getMiscAnimation();
      if (anim != 0) {
         if (event.getController().getAnimationState() == AnimationState.Stopped) {
            event.getController().markNeedsReload();
         }

         switch (anim) {
            case 1:
               event.getController().setAnimation(dagger_strike);
               break;
            case 2:
               event.getController().setAnimation(jump);
               break;
            case 3:
               event.getController().setAnimation(double_dagger_strike);
         }

         return PlayState.CONTINUE;
      } else {
         return PlayState.STOP;
      }
   }

   public void performDaggerStrike(LivingEntity target) {
      if (target != null) {
         this.lookAt(Anchor.EYES, target.getEyePosition());
      }
   }

   public void performDoubleDaggerStrike(LivingEntity target) {
      if (target != null) {
         this.lookAt(Anchor.EYES, target.getEyePosition());
      }
   }

   public void daggerStrike(LivingEntity target) {
      if (this.getMiscAnimation() == 0) {
         this.setMiscAnimation(1);
         this.performDaggerStrike(target);
      }
   }

   public void doubleDaggerStrike(LivingEntity target) {
      if (this.getMiscAnimation() == 0) {
         this.setMiscAnimation(3);
         this.performDoubleDaggerStrike(target);
      }
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

   public AnimationFactory getFactory() {
      return this.factory;
   }

   class CustomMeleeGoal extends NPCMeleeAttackGoal {
      public CustomMeleeGoal(double speed, boolean useMemory) {
         super(GobweEntity.this, GobweEntity.this, speed, useMemory);
      }

      @Override
      public boolean canUse() {
         return false;
      }

      @Override
      protected double getAttackReachSqr(LivingEntity target) {
         double widthSum = (double)(GobweEntity.this.getBbWidth() + target.getBbWidth());
         return widthSum + 1.0;
      }
   }

   static class GobweAttackGoal extends Goal {
      private final GobweEntity gobwe;
      private int attackCooldown = 0;

      public GobweAttackGoal(GobweEntity entity) {
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
         this.gobwe = entity;
      }

      public boolean canUse() {
         LivingEntity target = this.gobwe.getTarget();
         if (target instanceof Player player && player.isCreative()) {
            this.gobwe.forgetCurrentTargetAndRefreshUniversalAnger();
            return false;
         }

         return target != null && target.isAlive();
      }

      public boolean canContinueToUse() {
         return this.canUse();
      }

      public void start() {
         this.gobwe.setAggressive(true);
         this.attackCooldown = 0;
      }

      public void tick() {
         LivingEntity target = this.gobwe.getTarget();
         if (target != null) {
            double distance = this.gobwe.distanceToSqr(target);
            double maxAttackDistance = 4.0;
            if (this.gobwe.getMiscAnimation() == 0 && distance <= maxAttackDistance && this.attackCooldown <= 0) {
               if (this.gobwe.hasLineOfSight(target)) {
                  this.gobwe.getNavigation().stop();
                  this.gobwe.lookAt(Anchor.EYES, target.getEyePosition());
                  int random = this.gobwe.random.nextInt(100);
                  if (random < 60) {
                     this.gobwe.daggerStrike(target);
                  } else if (random < 90) {
                     this.gobwe.doubleDaggerStrike(target);
                  } else {
                     this.gobwe.setMiscAnimation(2);
                     this.gobwe.jumpAway();
                  }

                  this.attackCooldown = 40;
               }
            } else if (distance > maxAttackDistance && this.gobwe.getMiscAnimation() == 0) {
               this.gobwe.getNavigation().moveTo(target, 1.2);
            }

            if (this.attackCooldown > 0) {
               this.attackCooldown--;
            }
         }
      }

      public void stop() {
         this.gobwe.setAggressive(false);
      }
   }

   public class MoveToAttackerGoal extends Goal {
      private final GobweEntity gobwe;
      private LivingEntity lastDamager;

      public MoveToAttackerGoal(GobweEntity gobwe) {
         this.gobwe = gobwe;
      }

      public boolean canUse() {
         if (this.gobwe.getMiscAnimation() != 0) {
            return false;
         } else {
            this.lastDamager = this.gobwe.getLastHurtByMob();
            return this.lastDamager != null && this.lastDamager.isAlive();
         }
      }

      public void start() {
         this.gobwe.getNavigation().moveTo(this.lastDamager, 1.0);
      }

      public void tick() {
         if (this.lastDamager != null && this.lastDamager.isAlive() && this.gobwe.getMiscAnimation() == 0) {
            this.gobwe.getNavigation().moveTo(this.lastDamager, 1.0);
         }
      }

      public void stop() {
         this.gobwe.getNavigation().stop();
      }
   }
}
