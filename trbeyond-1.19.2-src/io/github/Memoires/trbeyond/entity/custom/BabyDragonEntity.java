package io.github.Memoires.trbeyond.entity.custom;

import io.github.Memoires.trbeyond.TRBeyond;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import java.util.Comparator;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages.SpawnEntity;
import net.minecraftforge.registries.ForgeRegistries;
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

public class BabyDragonEntity extends Monster implements RangedAttackMob, IAnimatable {
   public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(BabyDragonEntity.class, EntityDataSerializers.BOOLEAN);
   public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(BabyDragonEntity.class, EntityDataSerializers.STRING);
   public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(BabyDragonEntity.class, EntityDataSerializers.STRING);
   private AnimationFactory factory = GeckoLibUtil.createFactory(this);
   private boolean swinging;
   private boolean lastloop;
   private long lastSwing;
   public String animationprocedure = "empty";

   public BabyDragonEntity(SpawnEntity packet, Level world) {
      this((EntityType<BabyDragonEntity>)BeyondEntities.BABY_DRAGON.get(), world);
   }

   public BabyDragonEntity(EntityType<BabyDragonEntity> type, Level world) {
      super(type, world);
      this.xpReward = 100;
      this.setNoAi(false);
      this.setPersistenceRequired();
      this.moveControl = new FlyingMoveControl(this, 10, true);
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(SHOOT, false);
      this.entityData.define(ANIMATION, "undefined");
      this.entityData.define(TEXTURE, "babydragon");
   }

   public void setTexture(String texture) {
      this.entityData.set(TEXTURE, texture);
   }

   public String getTexture() {
      return (String)this.entityData.get(TEXTURE);
   }

   public Packet<?> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   protected PathNavigation createNavigation(Level world) {
      return new FlyingPathNavigation(this, world);
   }

   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0, 20) {
         protected Vec3 getPosition() {
            RandomSource random = BabyDragonEntity.this.getRandom();
            double dir_x = BabyDragonEntity.this.getX() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double dir_y = BabyDragonEntity.this.getY() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double dir_z = BabyDragonEntity.this.getZ() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            return new Vec3(dir_x, dir_y, dir_z);
         }
      });
      this.targetSelector.addGoal(3, new BabyDragonEntity.DragonBiteAttack(this, 1.2, false));
      this.goalSelector.addGoal(4, new Goal() {
         {
            this.setFlags(EnumSet.of(Flag.MOVE));
         }

         public boolean canUse() {
            return BabyDragonEntity.this.getTarget() != null && !BabyDragonEntity.this.getMoveControl().hasWanted();
         }

         public boolean canContinueToUse() {
            return BabyDragonEntity.this.getMoveControl().hasWanted() && BabyDragonEntity.this.getTarget() != null && BabyDragonEntity.this.getTarget().isAlive();
         }

         public void start() {
            LivingEntity livingentity = BabyDragonEntity.this.getTarget();
            Vec3 vec3d = livingentity.getEyePosition(1.0F);
            BabyDragonEntity.this.moveControl.setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 1.0);
         }

         public void tick() {
            LivingEntity livingentity = BabyDragonEntity.this.getTarget();
            if (BabyDragonEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
               BabyDragonEntity.this.doHurtTarget(livingentity);
            } else {
               double d0 = BabyDragonEntity.this.distanceToSqr(livingentity);
               if (d0 < 16.0) {
                  Vec3 vec3d = livingentity.getEyePosition(1.0F);
                  BabyDragonEntity.this.moveControl.setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 1.0);
               }
            }
         }
      });
      this.goalSelector.addGoal(4, new FollowMobGoal(this, 1.0, 10.0F, 5.0F));
      this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(6, new FloatGoal(this));
      this.goalSelector.addGoal(1, new BabyDragonEntity.RangedAttackGoal(this, 1.25, 300, 32.0F) {
         @Override
         public boolean canContinueToUse() {
            return this.canUse();
         }
      });
   }

   public MobType getMobType() {
      return MobType.UNDEFINED;
   }

   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return false;
   }

   public SoundEvent getHurtSound(DamageSource ds) {
      return (SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.hurt"));
   }

   public SoundEvent getDeathSound() {
      return (SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
   }

   public boolean causeFallDamage(float l, float d, DamageSource source) {
      return false;
   }

   public void baseTick() {
      super.baseTick();
      this.refreshDimensions();
   }

   public EntityDimensions getDimensions(Pose p_33597_) {
      return super.getDimensions(p_33597_).scale(1.0F);
   }

   public void performRangedAttack(LivingEntity target, float flval) {
      this.setAnimation("animation.model.chaos_breath");
      Level world = this.getLevel();
      TRBeyond.queueServerWork(40, () -> {
         if (world instanceof ServerLevel _level) {
            FireBreathEntity.shoot(this, target);
            this.setAnimation("empty");
            TRBeyond.queueServerWork(5, () -> {
               if (world instanceof ServerLevel _levell) {
                  FireBreathEntity.shoot(this, target);
                  this.setAnimation("empty");
               }
            });
         }
      });
   }

   protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
   }

   public void setNoGravity(boolean ignored) {
      super.setNoGravity(true);
   }

   public void aiStep() {
      super.aiStep();
      this.setNoGravity(true);
   }

   public static void init() {
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
      builder = builder.add(Attributes.MAX_HEALTH, 100.0);
      builder = builder.add(Attributes.ARMOR, 3.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 3.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 32.0);
      builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
      return builder.add(Attributes.FLYING_SPEED, 0.3);
   }

   public boolean doHurtTarget(Entity target) {
      return false;
   }

   private <E extends IAnimatable> PlayState movementPredicate(AnimationEvent<E> event) {
      if (this.animationprocedure.equals("empty")) {
         if (!this.isOnGround()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.fly", EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
         } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.idle", EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
         }
      } else {
         return PlayState.STOP;
      }
   }

   private <E extends IAnimatable> PlayState procedurePredicate(AnimationEvent<E> event) {
      Level world = super.level;
      boolean loop = false;
      double x = this.getX();
      double y = this.getY();
      double z = this.getZ();
      if (!loop && this.lastloop) {
         this.lastloop = false;
         event.getController().setAnimation(new AnimationBuilder().addAnimation(this.animationprocedure, EDefaultLoopTypes.PLAY_ONCE));
         event.getController().clearAnimationCache();
         return PlayState.STOP;
      } else {
         if (!this.animationprocedure.equals("empty") && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            if (!loop) {
               event.getController().setAnimation(new AnimationBuilder().addAnimation(this.animationprocedure, EDefaultLoopTypes.PLAY_ONCE));
               if (event.getController().getAnimationState().equals(AnimationState.Stopped)) {
                  this.animationprocedure = "empty";
                  event.getController().markNeedsReload();
               }
            } else {
               event.getController().setAnimation(new AnimationBuilder().addAnimation(this.animationprocedure, EDefaultLoopTypes.LOOP));
               this.lastloop = true;
            }
         }

         return PlayState.CONTINUE;
      }
   }

   protected void tickDeath() {
      this.deathTime++;
      if (this.deathTime == 20) {
         this.remove(RemovalReason.KILLED);
         this.dropExperience();
      }
   }

   public String getSyncedAnimation() {
      return (String)this.entityData.get(ANIMATION);
   }

   public void setAnimation(String animation) {
      this.entityData.set(ANIMATION, animation);
   }

   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController(this, "movement", 4.0F, this::movementPredicate));
      data.addAnimationController(new AnimationController(this, "procedure", 4.0F, this::procedurePredicate));
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   class DragonBiteAttack extends MeleeAttackGoal {
      public DragonBiteAttack(BabyDragonEntity dragon, double speed, boolean chaseIfLost) {
         super(dragon, speed, chaseIfLost);
      }

      public void tick() {
         super.tick();
         if (this.mob.getTarget() != null) {
            Level world = this.mob.getLevel();
            Entity entity = this.mob.getTarget();
            Entity sourceentity = this.mob;
            if (entity == null || sourceentity == null) {
               return;
            }

            double rnd = 0.0;
            double waittick = 0.0;
            if ("empty".equals(((BabyDragonEntity)sourceentity).animationprocedure)
               && !world.getEntitiesOfClass(
                     Player.class,
                     AABB.ofSize(new Vec3(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()), 8.0, 8.0, 8.0),
                     e -> true
                  )
                  .isEmpty()
               && !sourceentity.getPersistentData().getBoolean("dmg")) {
               if (sourceentity instanceof BabyDragonEntity) {
                  ((BabyDragonEntity)sourceentity).setAnimation("animation.model.bite");
               }

               sourceentity.lookAt(Anchor.EYES, new Vec3(entity.getX(), entity.getY(), entity.getZ()));
               sourceentity.getPersistentData().putBoolean("dmg", true);

               for (int index0 = 0; index0 < 50; index0++) {
                  TRBeyond.queueServerWork(
                     (int)waittick, () -> sourceentity.lookAt(Anchor.EYES, new Vec3(entity.getX(), entity.getY(), entity.getZ()))
                  );
                  waittick++;
               }

               TRBeyond.queueServerWork(
                  40,
                  () -> {
                     if (!world.getEntitiesOfClass(
                              Player.class,
                              AABB.ofSize(new Vec3(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()), 6.0, 6.0, 6.0),
                              e -> true
                           )
                           .isEmpty()
                        && "animation.model.bite".equals(((BabyDragonEntity)sourceentity).animationprocedure)) {
                        world.getEntitiesOfClass(
                              Player.class,
                              AABB.ofSize(new Vec3(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()), 6.0, 6.0, 6.0),
                              e -> true
                           )
                           .stream()
                           .sorted((new Object() {
                              Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                 return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                              }
                           }).compareDistOf(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()))
                           .findFirst()
                           .orElse(null)
                           .hurt(new EntityDamageSource("Physical", entity), 10.0F);
                     }

                     TRBeyond.queueServerWork(30, () -> {
                        sourceentity.getPersistentData().putBoolean("dmg", false);
                        if (sourceentity instanceof BabyDragonEntity) {
                           ((BabyDragonEntity)sourceentity).setAnimation("empty");
                        }
                     });
                  }
               );
            }
         }
      }

      protected double getAttackReachSqr(LivingEntity entity) {
         return 64.0;
      }
   }

   public class RangedAttackGoal extends Goal {
      private final Mob mob;
      private final RangedAttackMob rangedAttackMob;
      @Nullable
      private LivingEntity target;
      private int attackTime = -1;
      private final double speedModifier;
      private int seeTime;
      private final int attackIntervalMin;
      private final int attackIntervalMax;
      private final float attackRadius;
      private final float attackRadiusSqr;

      public RangedAttackGoal(RangedAttackMob p_25768_, double p_25769_, int p_25770_, float p_25771_) {
         this(p_25768_, p_25769_, p_25770_, p_25770_, p_25771_);
      }

      public RangedAttackGoal(RangedAttackMob p_25773_, double p_25774_, int p_25775_, int p_25776_, float p_25777_) {
         if (!(p_25773_ instanceof LivingEntity)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
         } else {
            this.rangedAttackMob = p_25773_;
            this.mob = (Mob)p_25773_;
            this.speedModifier = p_25774_;
            this.attackIntervalMin = p_25775_;
            this.attackIntervalMax = p_25776_;
            this.attackRadius = p_25777_;
            this.attackRadiusSqr = p_25777_ * p_25777_;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
         }
      }

      public boolean canUse() {
         LivingEntity livingentity = this.mob.getTarget();
         if (livingentity != null && livingentity.isAlive()) {
            this.target = livingentity;
            return true;
         } else {
            return false;
         }
      }

      public boolean canContinueToUse() {
         return this.canUse() || this.target.isAlive() && !this.mob.getNavigation().isDone();
      }

      public void stop() {
         this.target = null;
         this.seeTime = 0;
         this.attackTime = -1;
         ((BabyDragonEntity)this.rangedAttackMob).entityData.set(BabyDragonEntity.SHOOT, false);
      }

      public boolean requiresUpdateEveryTick() {
         return true;
      }

      public void tick() {
         double d0 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
         boolean flag = this.mob.getSensing().hasLineOfSight(this.target);
         if (flag) {
            this.seeTime++;
         } else {
            this.seeTime = 0;
         }

         if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 5) {
            this.mob.getNavigation().stop();
         } else {
            this.mob.getNavigation().moveTo(this.target, this.speedModifier);
         }

         this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
         if (--this.attackTime == 0) {
            if (!flag) {
               ((BabyDragonEntity)this.rangedAttackMob).entityData.set(BabyDragonEntity.SHOOT, false);
               return;
            }

            ((BabyDragonEntity)this.rangedAttackMob).entityData.set(BabyDragonEntity.SHOOT, true);
            float f = (float)Math.sqrt(d0) / this.attackRadius;
            float f1 = Mth.clamp(f, 0.1F, 1.0F);
            this.rangedAttackMob.performRangedAttack(this.target, f1);
            this.attackTime = Mth.floor(f * (float)(this.attackIntervalMax - this.attackIntervalMin) + (float)this.attackIntervalMin);
         } else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(
               Mth.lerp(Math.sqrt(d0) / (double)this.attackRadius, (double)this.attackIntervalMin, (double)this.attackIntervalMax)
            );
         } else {
            ((BabyDragonEntity)this.rangedAttackMob).entityData.set(BabyDragonEntity.SHOOT, false);
         }
      }
   }
}
