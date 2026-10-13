package io.github.Memoires.trbeyond.entity.custom;

import io.github.Memoires.trbeyond.procedures.MageAnimationTickProcedure;
import io.github.Memoires.trbeyond.procedures.MageAttacksProcedure;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
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

public class MageEntity extends Monster implements IAnimatable {
   public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(MageEntity.class, EntityDataSerializers.BOOLEAN);
   public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(MageEntity.class, EntityDataSerializers.STRING);
   public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(MageEntity.class, EntityDataSerializers.STRING);
   private AnimationFactory factory = GeckoLibUtil.createFactory(this);
   private boolean swinging;
   private boolean lastloop;
   private long lastSwing;
   public String animationprocedure = "empty";

   public MageEntity(SpawnEntity packet, Level world) {
      this((EntityType<MageEntity>)BeyondEntities.MAGE.get(), world);
   }

   public MageEntity(EntityType<MageEntity> type, Level world) {
      super(type, world);
      this.xpReward = 0;
      this.setNoAi(false);
      this.setPersistenceRequired();
      this.moveControl = new FlyingMoveControl(this, 10, true);
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(SHOOT, false);
      this.entityData.define(ANIMATION, "undefined");
      this.entityData.define(TEXTURE, "goz");
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
      this.goalSelector.addGoal(1, new Goal() {
         {
            this.setFlags(EnumSet.of(Flag.MOVE));
         }

         public boolean canUse() {
            return MageEntity.this.getTarget() != null && !MageEntity.this.getMoveControl().hasWanted();
         }

         public boolean canContinueToUse() {
            return MageEntity.this.getMoveControl().hasWanted() && MageEntity.this.getTarget() != null && MageEntity.this.getTarget().isAlive();
         }

         public void start() {
            LivingEntity livingentity = MageEntity.this.getTarget();
            Vec3 vec3d = livingentity.getEyePosition(1.0F);
            MageEntity.this.moveControl.setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 1.0);
         }

         public void tick() {
            LivingEntity livingentity = MageEntity.this.getTarget();
            if (MageEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
               MageEntity.this.doHurtTarget(livingentity);
            } else {
               double d0 = MageEntity.this.distanceToSqr(livingentity);
               if (d0 < 16.0) {
                  Vec3 vec3d = livingentity.getEyePosition(1.0F);
                  MageEntity.this.moveControl.setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 1.0);
               }
            }
         }
      });
      this.goalSelector.addGoal(2, new AvoidEntityGoal(this, Player.class, 3.0F, 1.0, 1.2));
      this.targetSelector.addGoal(1, new MageEntity.MageAttack(this, 1.2, false));
      this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
      this.targetSelector
         .addGoal(
            3,
            new NearestAttackableTargetGoal(
               this,
               LivingEntity.class,
               false,
               target -> target.getClass() != LivingArmorEntity.class
                     && target.getClass() != MageEntity.class
                     && target.getClass() != SkullNightEntity.class
                     && target.getClass() != MilimSlimeScarletEntity.class
            )
         );
      this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0));
      this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(5, new FloatGoal(this));
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
      MageAnimationTickProcedure.execute(this.getLevel(), this);
      this.refreshDimensions();
   }

   public EntityDimensions getDimensions(Pose p_33597_) {
      return super.getDimensions(p_33597_).scale(1.0F);
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
      builder = builder.add(Attributes.MAX_HEALTH, 200.0);
      builder = builder.add(Attributes.ARMOR, 10.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 25.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 32.0);
      return builder.add(Attributes.FLYING_SPEED, 0.3);
   }

   public boolean doHurtTarget(Entity target) {
      return false;
   }

   private <E extends IAnimatable> PlayState movementPredicate(AnimationEvent<E> event) {
      if (this.animationprocedure.equals("empty")) {
         if (!this.isOnGround()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("fly", EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
         } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("fly", EDefaultLoopTypes.LOOP));
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

   class MageAttack extends MeleeAttackGoal {
      public MageAttack(MageEntity mage, double speed, boolean chaseIfLost) {
         super(mage, speed, chaseIfLost);
      }

      public void tick() {
         super.tick();
         if (this.mob.getTarget() != null) {
            MageAttacksProcedure.execute(this.mob.getLevel(), this.mob.getTarget(), this.mob);
         }
      }

      protected double getAttackReachSqr(LivingEntity entity) {
         return 64.0;
      }
   }
}
