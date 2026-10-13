package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
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

public class OgreSubEntity extends Monster implements IAnimatable {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   private static final EntityDataAccessor<Integer> MISC_ANIMATION = SynchedEntityData.defineId(OgreLordEntity.class, EntityDataSerializers.INT);
   public int miscAnimationTicks = 0;

   public OgreSubEntity(EntityType<? extends OgreSubEntity> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
      this.setHealth(this.getMaxHealth());
      this.xpReward = 100;
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(MISC_ANIMATION, 0);
   }

   public void addAdditionalSaveData(@NotNull CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("MiscAnimation", this.getMiscAnimation());
   }

   public void readAdditionalSaveData(@NotNull CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.entityData.set(MISC_ANIMATION, compound.getInt("MiscAnimation"));
   }

   public boolean isInvulnerableTo(@NotNull DamageSource source) {
      return source == DamageSource.IN_FIRE
         || source == DamageSource.IN_WALL
         || source == DamageSource.FALLING_BLOCK
         || super.isInvulnerableTo(source)
         || source.getEntity() instanceof OgreLordEntity
         || source.getEntity() instanceof OgreSubEntity;
   }

   public boolean causeFallDamage(float pFallDistance, float pMultiplier, @NotNull DamageSource pSource) {
      return false;
   }

   public void setCustomName(@Nullable Component pName) {
      super.setCustomName(pName);
   }

   public int getMiscAnimation() {
      return (Integer)this.entityData.get(MISC_ANIMATION);
   }

   public void setMiscAnimation(int animation) {
      if (this.getMiscAnimation() == 0 || animation == 0) {
         this.entityData.set(MISC_ANIMATION, animation);
      }
   }

   public static AttributeSupplier setAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.JUMP_STRENGTH, 1.5)
         .add(Attributes.MAX_HEALTH, 50.0)
         .add(Attributes.ATTACK_DAMAGE, 20.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
         .add(Attributes.ARMOR, 20.0)
         .add(Attributes.ARMOR_TOUGHNESS, 15.0)
         .add(Attributes.ATTACK_SPEED, 5.0)
         .add(Attributes.ATTACK_KNOCKBACK, 5.0)
         .add(Attributes.FOLLOW_RANGE, 40.0)
         .add(Attributes.MOVEMENT_SPEED, 0.3F)
         .build();
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(2, new OgreSubEntity.OgreSubAttackGoal(this, 1.0, true));
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 40.0F));
      this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.8));
      this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
      this.targetSelector
         .addGoal(
            2,
            new NearestAttackableTargetGoal(
               this, LivingEntity.class, false, target -> target.getClass() != OgreLordEntity.class && target.getClass() != OgreSubEntity.class
            )
         );
   }

   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      if (!event.isMoving()) {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ogrelord.idle", EDefaultLoopTypes.LOOP));
      } else {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ogrelord.walk", EDefaultLoopTypes.LOOP));
      }

      return PlayState.CONTINUE;
   }

   private <E extends IAnimatable> PlayState miscPredicate(AnimationEvent<E> event) {
      if (event.getController().getAnimationState().equals(AnimationState.Stopped)) {
         System.out.println("-------------------- ATTACK ANIMATION INSIDE -------------------");
         event.getController().markNeedsReload();
         System.out.println(this.getMiscAnimation());
         if (this.getMiscAnimation() == 1) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ogrelord.double_swing", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 2) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ogrelord.meteor_smash", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 3) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ogrelord.ogre_flame", EDefaultLoopTypes.PLAY_ONCE));
         }
      }

      return PlayState.CONTINUE;
   }

   public void registerControllers(AnimationData animationData) {
      animationData.addAnimationController(new AnimationController(this, "movement", 5.0F, this::predicate));
      animationData.addAnimationController(new AnimationController(this, "attacks", 5.0F, this::miscPredicate));
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockIn) {
      this.playSound(SoundEvents.IRON_GOLEM_STEP, 0.25F, 0.5F);
   }

   @NotNull
   protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
      return SoundEvents.GENERIC_HURT;
   }

   @NotNull
   protected SoundEvent getDeathSound() {
      return SoundEvents.GENERIC_DEATH;
   }

   protected float getSoundVolume() {
      return 0.8F;
   }

   public boolean shouldAttack(LivingEntity target) {
      return target instanceof Player && ((Player)target).isCreative();
   }

   public void tick() {
      super.tick();
      if (this.getMiscAnimation() != 0) {
         this.miscAnimationTicks++;
         if (!this.isAlive()) {
            return;
         }

         if (this.getMiscAnimation() == 1) {
            if (this.miscAnimationTicks == 22) {
               LivingEntity target = this.getTarget();
               this.level
                  .playSound(
                     null,
                     this.getX(),
                     this.getY(),
                     this.getZ(),
                     (SoundEvent)BeyondSoundEvents.ogre_lord_double_strike.get(),
                     SoundSource.NEUTRAL,
                     1.0F,
                     1.0F
                  );
               if (target != null && (double)target.distanceTo(this) <= 5.0) {
                  this.lookAt(Anchor.EYES, target.position());
                  float damage = (float)(this.getAttributeValue(Attributes.ATTACK_DAMAGE) + 30.0);
                  target.hurt(DamageSource.mobAttack(this), damage);
               }
            }

            if (this.miscAnimationTicks == 47) {
               LivingEntity target = this.getTarget();
               if (target != null && (double)target.distanceTo(this) <= 5.0) {
                  this.lookAt(Anchor.EYES, target.position());
                  float damage = (float)(this.getAttributeValue(Attributes.ATTACK_DAMAGE) + 30.0);
                  target.hurt(DamageSource.mobAttack(this), damage);
               }
            }
         }

         if (this.getMiscAnimation() == 2) {
            LivingEntity target = this.getTarget();
            if (target != null) {
               this.lookAt(Anchor.EYES, target.position());
               if (this.miscAnimationTicks > 24 && this.miscAnimationTicks < 35 && this.isOnGround()) {
                  double x = target.getX() - this.getX();
                  double z = target.getZ() - this.getZ();
                  double xz = Math.sqrt(x * x + z * z);
                  this.setDeltaMovement(x / xz * 1.3, 1.0, z / xz * 1.3);
               }

               if (this.miscAnimationTicks % 2 == 0 && this.miscAnimationTicks < 55 && this.miscAnimationTicks > 25) {
                  this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 1.1, 1.0));
               }

               if (this.miscAnimationTicks == 50) {
                  this.setDeltaMovement(this.getDeltaMovement().multiply(0.0, 0.0, 0.0));
                  this.setNoGravity(true);
               }

               if (this.miscAnimationTicks == 60) {
                  this.setNoGravity(false);
                  this.setDeltaMovement(0.0, -8.0, 0.0);
                  this.level
                     .playSound(
                        null,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        (SoundEvent)BeyondSoundEvents.ogre_lord_meteorite_strike.get(),
                        SoundSource.HOSTILE,
                        1.0F,
                        1.0F
                     );
               }

               if (this.miscAnimationTicks == 65) {
                  if (this.isOnGround()) {
                     AABB aabb = new AABB(this.blockPosition()).inflate(5.0);

                     for (LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, aabb)) {
                        if (entity != this) {
                           entity.hurt(DamageSource.mobAttack(this), (float)(this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 10.0));
                        }
                     }
                  }

                  TensuraParticleHelper.spawnGroundSlamParticle(this, 75, 3.0F);
               }
            }
         }

         if (this.miscAnimationTicks >= this.getAnimationTick(this.getMiscAnimation())) {
            this.setMiscAnimation(0);
            this.miscAnimationTicks = 0;
         }
      }
   }

   private int getAnimationTick(int miscAnimation) {
      return switch (miscAnimation) {
         case 1 -> 60;
         case 2 -> 80;
         default -> 20;
      };
   }

   public boolean doHurtTarget(@NotNull Entity pEntity) {
      boolean flag = super.doHurtTarget(pEntity);
      if (flag && this.getMiscAnimation() == 0) {
         this.setMiscAnimation(1);
      }

      return flag;
   }

   class OgreSubAttackGoal extends MeleeAttackGoal {
      private final OgreSubEntity ogre;

      public OgreSubAttackGoal(OgreSubEntity ogre, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
         super(ogre, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
         this.ogre = ogre;
      }

      public void tick() {
         if (this.ogre.getMiscAnimation() == 0) {
            super.tick();
         }
      }

      protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy, double pDistToEnemySqr) {
         double reach = this.getAttackReachSqr(pEnemy);
         if (this.ogre.getMiscAnimation() == 0 && pEnemy.isAlive()) {
            int randomAttack = this.randomAttack(pDistToEnemySqr);
            double var10000;
            double var10001;
            if (randomAttack == 2) {
               var10000 = 100.0;
               var10001 = 25.0;
            } else {
               var10000 = reach;
               var10001 = 1.0;
            }

            if (pDistToEnemySqr <= var10000 && pDistToEnemySqr >= var10001 && this.isTimeToAttack()) {
               this.resetAttackCooldown();
               this.ogre.setMiscAnimation(randomAttack);
            }
         }
      }

      protected int randomAttack(double distance) {
         return distance > 25.0 && distance < 100.0 && this.ogre.getRandom().nextInt(20) == 0 ? 2 : 1;
      }

      protected double getAttackReachSqr(LivingEntity pAttackTarget) {
         return (double)(this.mob.getBbWidth() * this.mob.getBbWidth() * 3.0F + pAttackTarget.getBbWidth());
      }
   }
}
