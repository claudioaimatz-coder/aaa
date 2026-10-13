package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.entity.magic.breath.ParalysingBreathProjectile;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class KingCrawlerEntity extends PathfinderMob implements IAnimatable {
   private static final EntityDataAccessor<Integer> ATTACK_STATE = SynchedEntityData.defineId(KingCrawlerEntity.class, EntityDataSerializers.INT);
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   private int animTick = 0;
   private final HitboxPartEntity[] subEntities = new HitboxPartEntity[]{
      new HitboxPartEntity(this, 2.0F, 1.5F, new Vec3(1.5, 2.75, 0.0)),
      new HitboxPartEntity(this, 2.0F, 1.5F, new Vec3(-2.0, 0.0, 0.0)),
      new HitboxPartEntity(this, 2.0F, 1.5F, new Vec3(-4.0, 0.0, 0.0)),
      new HitboxPartEntity(this, 2.0F, 1.5F, new Vec3(-6.0, 0.0, 0.0)),
      new HitboxPartEntity(this, 1.0F, 3.0F, new Vec3(-7.5, 0.5, 0.0))
   };

   public KingCrawlerEntity(EntityType<? extends KingCrawlerEntity> type, Level level) {
      super(type, level);

      for (int i = 0; i < this.subEntities.length; i++) {
         this.subEntities[i].setId(this.getId() + i + 1);
      }
   }

   public PartEntity<?>[] getParts() {
      return this.subEntities;
   }

   public boolean isMultipartEntity() {
      return true;
   }

   public void setId(int pId) {
      super.setId(pId);

      for (int i = 0; i < this.subEntities.length; i++) {
         this.subEntities[i].setId(pId + i + 1);
      }
   }

   public void updatePartPositions() {
      if (this.subEntities != null) {
         for (HitboxPartEntity part : this.subEntities) {
            part.updatePosition();
         }
      }
   }

   public static AttributeSupplier setAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 175.0)
         .add(Attributes.MOVEMENT_SPEED, 0.22)
         .add(Attributes.ATTACK_DAMAGE, 25.0)
         .add(Attributes.ARMOR, 6.0)
         .add(Attributes.FOLLOW_RANGE, 96.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
         .build();
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(ATTACK_STATE, 0);
   }

   public int getAttackState() {
      return (Integer)this.entityData.get(ATTACK_STATE);
   }

   public void setAttackState(int state) {
      this.entityData.set(ATTACK_STATE, state);
      this.animTick = 0;
      if (this.level.isClientSide) {
         AnimationController<?> controller = (AnimationController<?>)this.factory
            .getOrCreateAnimationData(this.getId())
            .getAnimationControllers()
            .get("controller");
         if (controller != null) {
            controller.markNeedsReload();
         }
      }
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("AttackState", this.getAttackState());
   }

   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setAttackState(compound.getInt("AttackState"));
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new KingCrawlerEntity.CustomAttackGoal(this));
      this.goalSelector.addGoal(1, new RandomStrollGoal(this, 0.8));
      this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(4, new FloatGoal(this));
      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, false));
   }

   public boolean doHurtTarget(Entity target) {
      if (this.level.isClientSide) {
         return false;
      } else if (this.getAttackState() == 0 && target instanceof LivingEntity livingTarget) {
         int choice = this.random.nextInt(3) + 1;
         this.setAttackState(choice);
         this.lookAt(Anchor.EYES, livingTarget.position());
         this.getNavigation().stop();
         return true;
      } else {
         return false;
      }
   }

   public void tick() {
      super.tick();
      this.updatePartPositions();
      if (this.getAttackState() != 0) {
         this.animTick++;
         LivingEntity target = this.getTarget();
         switch (this.getAttackState()) {
            case 1:
               if (!this.level.isClientSide && this.animTick == 15 && target != null && this.distanceToSqr(target) < 25.0) {
                  target.hurt(DamageSource.mobAttack(this), 12.0F);
               }

               if (this.animTick > 40) {
                  this.setAttackState(0);
               }
               break;
            case 2:
               if (!this.level.isClientSide && this.animTick == 24) {
                  for (LivingEntity e : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8.0), ex -> ex != this)) {
                     e.hurt(DamageSource.mobAttack(this), 10.0F);
                     e.knockback(0.8F, this.getX() - e.getX(), this.getZ() - e.getZ());
                  }
               }

               if (this.animTick > 33) {
                  this.setAttackState(0);
               }
               break;
            case 3:
               if (!this.level.isClientSide) {
                  if (this.animTick == 20 && target != null) {
                     ParalysingBreathProjectile spit = new ParalysingBreathProjectile(
                        (EntityType<? extends ParalysingBreathProjectile>)TensuraEntityTypes.PARALYSING_BREATH.get(), this.level
                     );
                     spit.setPos(this.getX(), this.getEyeY() - 0.1, this.getZ());
                     spit.setOwner(this);
                     double dx = target.getX() - this.getX();
                     double dy = target.getEyeY() - spit.getY();
                     double dz = target.getZ() - this.getZ();
                     spit.shoot(dx, dy, dz, 1.2F, 0.0F);
                     this.level.addFreshEntity(spit);
                  }
               } else if (this.animTick == 20 && target != null) {
                  ParticleOptions particle = (ParticleOptions)TensuraParticles.PARALYSING_BUBBLE.get();

                  for (int i = 0; i < 20; i++) {
                     double t = (double)i / 20.0;
                     double dx = this.getX() + (target.getX() - this.getX()) * t;
                     double dy = this.getEyeY() + (target.getEyeY() - this.getEyeY()) * t;
                     double dz = this.getZ() + (target.getZ() - this.getZ()) * t;
                     this.level.addParticle(particle, dx, dy, dz, 0.0, 0.0, 0.0);
                  }
               }

               if (this.animTick > 30) {
                  this.setAttackState(0);
               }
         }
      }
   }

   protected void customServerAiStep() {
      super.customServerAiStep();
      this.yBodyRot = this.getYRot();
   }

   public int getMaxHeadYRot() {
      return 45;
   }

   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      switch (this.getAttackState()) {
         case 1:
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.attack", EDefaultLoopTypes.PLAY_ONCE));
            break;
         case 2:
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.tendril", EDefaultLoopTypes.PLAY_ONCE));
            break;
         case 3:
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.spit", EDefaultLoopTypes.PLAY_ONCE));
            break;
         default:
            if (event.isMoving()) {
               event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.walk", EDefaultLoopTypes.LOOP));
            } else {
               event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.idle2", EDefaultLoopTypes.LOOP));
            }
      }

      return PlayState.CONTINUE;
   }

   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController(this, "controller", 0.0F, this::predicate));
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   @NotNull
   protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
      return SoundEvents.SPIDER_HURT;
   }

   @NotNull
   protected SoundEvent getDeathSound() {
      return SoundEvents.SPIDER_DEATH;
   }

   protected float getSoundVolume() {
      return 0.8F;
   }

   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag
   ) {
      SpawnGroupData groupData = super.finalizeSpawn(world, difficulty, reason, data, tag);
      AttributeInstance spiritualHP = this.getAttribute((Attribute)TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get());
      if (spiritualHP != null) {
         double sHP = (double)this.getMaxHealth() * 2.0;
         spiritualHP.setBaseValue(sHP);
         TensuraEPCapability.setSpiritualHealth(this, sHP);
      }

      return groupData;
   }

   public static class CustomAttackGoal extends MeleeAttackGoal {
      private final KingCrawlerEntity crawler;

      public CustomAttackGoal(KingCrawlerEntity mob) {
         super(mob, 1.2, true);
         this.crawler = mob;
      }

      protected void checkAndPerformAttack(LivingEntity target, double dist) {
         if (dist <= this.getAttackReachSqr(target) && this.getTicksUntilNextAttack() <= 0) {
            this.resetAttackCooldown();
            if (this.crawler.doHurtTarget(target)) {
               this.crawler.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }
         }
      }

      protected double getAttackReachSqr(LivingEntity target) {
         return 100.0;
      }
   }
}
