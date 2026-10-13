package io.github.Memoires.trbeyond.entity.custom;

import io.github.Memoires.trbeyond.procedures.SlimeAttacksProcedure;
import io.github.Memoires.trbeyond.procedures.SlimeTickProcedure;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import java.util.Random;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages.SpawnEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
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

public class MilimSlimeScarletEntity extends Monster implements IAnimatable {
   public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(MilimSlimeScarletEntity.class, EntityDataSerializers.BOOLEAN);
   public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(MilimSlimeScarletEntity.class, EntityDataSerializers.STRING);
   public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(MilimSlimeScarletEntity.class, EntityDataSerializers.STRING);
   private static final Random RANDOM = new Random();
   private AnimationFactory factory = GeckoLibUtil.createFactory(this);
   private boolean swinging;
   private boolean lastloop;
   private long lastSwing;
   public String animationprocedure = "empty";

   public MilimSlimeScarletEntity(SpawnEntity packet, Level world) {
      this((EntityType<MilimSlimeScarletEntity>)BeyondEntities.MILIM_SLIME_SCARLET.get(), world);
   }

   public MilimSlimeScarletEntity(EntityType<MilimSlimeScarletEntity> type, Level world) {
      super(type, world);
      this.xpReward = 0;
      this.setNoAi(false);
      this.setPersistenceRequired();
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

   protected void registerGoals() {
      super.registerGoals();
      this.targetSelector.addGoal(2, new MilimSlimeScarletEntity.SlimeAttack(this, 1.5, false));
      this.targetSelector.addGoal(3, new HurtByTargetGoal(this, new Class[0]));
      this.targetSelector
         .addGoal(
            1,
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
      this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0));
      this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(5, new FloatGoal(this));
   }

   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag
   ) {
      SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
      this.setAnimation("animation.model.charged_attack");
      return retval;
   }

   public boolean hurt(DamageSource source, float amount) {
      return RANDOM.nextFloat() <= 0.1F ? super.hurt(source, amount) : false;
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

   public void baseTick() {
      super.baseTick();
      SlimeTickProcedure.execute(this, this.getX(), this.getY(), this.getZ(), this.getLevel());
      this.refreshDimensions();
   }

   public EntityDimensions getDimensions(Pose p_33597_) {
      return super.getDimensions(p_33597_).scale(1.0F);
   }

   public static void init() {
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
      builder = builder.add(Attributes.MAX_HEALTH, 250.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 100.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 32.0);
      builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
      return builder.add(Attributes.ATTACK_KNOCKBACK, 1.0);
   }

   public boolean doHurtTarget(Entity target) {
      return false;
   }

   private <E extends IAnimatable> PlayState movementPredicate(AnimationEvent<E> event) {
      if (!this.animationprocedure.equals("empty")) {
         return PlayState.STOP;
      } else if ((event.isMoving() || !(event.getLimbSwingAmount() > -0.15F) || !(event.getLimbSwingAmount() < 0.15F)) && !this.isAggressive()) {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.walk", EDefaultLoopTypes.LOOP));
         return PlayState.CONTINUE;
      } else if (this.isAggressive() && event.isMoving()) {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.zigzag", EDefaultLoopTypes.LOOP));
         return PlayState.CONTINUE;
      } else {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.idle", EDefaultLoopTypes.LOOP));
         return PlayState.CONTINUE;
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

   class SlimeAttack extends MeleeAttackGoal {
      public SlimeAttack(MilimSlimeScarletEntity slime, double speed, boolean chaseIfLost) {
         super(slime, speed, chaseIfLost);
      }

      public void tick() {
         super.tick();
         if (this.mob.getTarget() != null) {
            SlimeAttacksProcedure.execute(this.mob.getLevel(), this.mob.getTarget(), this.mob);
         }
      }

      protected double getAttackReachSqr(LivingEntity entity) {
         return 64.0;
      }
   }
}
