package io.github.Memoires.trbeyond.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
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

public class GraveLizard extends Monster implements IAnimatable {
   private static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(GraveLizard.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> ANIM_TICK = SynchedEntityData.defineId(GraveLizard.class, EntityDataSerializers.INT);
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   private static final int LEN_BITE_T = 20;
   private static final int LEN_STOMP_T = 40;
   private static final int LEN_SPIN_T = 60;
   private final HitboxPartEntity[] subEntities = new HitboxPartEntity[]{
      new HitboxPartEntity(this, 2.0F, 1.5F, new Vec3(3.0, 2.5, 0.0)), new HitboxPartEntity(this, 2.0F, 1.5F, new Vec3(-3.0, 0.5, 0.0))
   };

   public GraveLizard(EntityType<? extends GraveLizard> type, Level level) {
      super(type, level);

      for (int i = 0; i < this.subEntities.length; i++) {
         this.subEntities[i].setId(this.getId() + i + 1);
      }
   }

   public static Builder createAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 150.0)
         .add(Attributes.MOVEMENT_SPEED, 0.25)
         .add(Attributes.ATTACK_DAMAGE, 25.0)
         .add(Attributes.ARMOR, 4.0)
         .add(Attributes.FOLLOW_RANGE, 64.0);
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(ANIM_STATE, 0);
      this.entityData.define(ANIM_TICK, 0);
   }

   private int getAnimState() {
      return (Integer)this.entityData.get(ANIM_STATE);
   }

   private int getAnimTick() {
      return (Integer)this.entityData.get(ANIM_TICK);
   }

   private void setAnimState(int state) {
      this.entityData.set(ANIM_STATE, state);
      this.entityData.set(ANIM_TICK, 0);
      if (this.level.isClientSide) {
         AnimationController<?> c = (AnimationController<?>)this.factory.getOrCreateAnimationData(this.getId()).getAnimationControllers().get("controller");
         if (c != null) {
            c.markNeedsReload();
         }
      }
   }

   private void incAnimTick() {
      this.entityData.set(ANIM_TICK, this.getAnimTick() + 1);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(0, new GraveLizard.GraveLizardAttackGoal(this, 1.0, true));
      this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8));
      this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(0, new NearestAttackableTargetGoal(this, LivingEntity.class, false));
   }

   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag
   ) {
      return super.finalizeSpawn(world, difficulty, reason, data, tag);
   }

   public boolean doHurtTarget(Entity target) {
      if (this.getAnimState() == 0 && target instanceof LivingEntity) {
         int choice = this.random.nextInt(3) + 1;
         this.setAnimState(choice);
         this.getNavigation().stop();
         return false;
      } else {
         return false;
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

   public void tick() {
      super.tick();
      this.updatePartPositions();
      int state = this.getAnimState();
      if (state != 0) {
         this.getNavigation().stop();
         if (!this.level.isClientSide) {
            this.incAnimTick();
            int t = this.getAnimTick();
            LivingEntity target = this.getTarget();
            switch (state) {
               case 1:
                  if (t == 10 && target != null && (double)this.distanceTo(target) < 8.0) {
                     target.hurt(DamageSource.mobAttack(this), 12.0F);
                  }

                  if (t >= 20) {
                     this.setAnimState(0);
                  }
                  break;
               case 2:
                  if (t == 35) {
                     AABB area = this.getBoundingBox().inflate(10.0);

                     for (LivingEntity e : this.level.getEntitiesOfClass(LivingEntity.class, area, ex -> ex != this)) {
                        double dx = e.getX() - this.getX();
                        double dz = e.getZ() - this.getZ();
                        e.push(dx * 0.4, 0.2, dz * 0.4);
                        e.hurt(DamageSource.mobAttack(this), 10.0F);
                     }
                  }

                  if (t >= 60) {
                     this.setAnimState(0);
                  }
                  break;
               case 3:
                  if (t == 18) {
                     AABB area = this.getBoundingBox().inflate(12.0);

                     for (LivingEntity e : this.level.getEntitiesOfClass(LivingEntity.class, area, ex -> ex != this)) {
                        e.hurt(DamageSource.mobAttack(this), 16.0F);
                        e.knockback(1.5, this.getX() - e.getX(), this.getZ() - e.getZ());
                     }

                     for (int i = 0; i < 30; i++) {
                        this.level
                           .addParticle(
                              ParticleTypes.CLOUD,
                              this.getX() + (this.random.nextDouble() - 0.5) * 5.0,
                              this.getY(),
                              this.getZ() + (this.random.nextDouble() - 0.5) * 5.0,
                              0.0,
                              0.15,
                              0.0
                           );
                     }
                  }

                  if (t >= 40) {
                     this.setAnimState(0);
                  }
            }
         }
      }
   }

   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController(this, "controller", 8.0F, this::animationPredicate));
   }

   private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
      int state = this.getAnimState();
      AnimationController<?> c = event.getController();
      if (state != 0) {
         if (state == 1) {
            c.setAnimation(new AnimationBuilder().addAnimation("animation.model.bite", EDefaultLoopTypes.PLAY_ONCE));
         } else if (state == 2) {
            c.setAnimation(new AnimationBuilder().addAnimation("animation.model.spinattack", EDefaultLoopTypes.PLAY_ONCE));
         } else if (state == 3) {
            c.setAnimation(new AnimationBuilder().addAnimation("animation.model.stomp", EDefaultLoopTypes.PLAY_ONCE));
         }

         return PlayState.CONTINUE;
      } else {
         if (event.isMoving()) {
            c.setAnimation(new AnimationBuilder().addAnimation("animation.model.run", EDefaultLoopTypes.LOOP));
         } else {
            c.setAnimation(new AnimationBuilder().addAnimation("animation.model.idle", EDefaultLoopTypes.LOOP));
         }

         return PlayState.CONTINUE;
      }
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   static class GraveLizardAttackGoal extends MeleeAttackGoal {
      private final GraveLizard lizard;

      public GraveLizardAttackGoal(GraveLizard mob, double speed, boolean follow) {
         super(mob, speed, follow);
         this.lizard = mob;
      }

      public boolean canUse() {
         return this.lizard.getAnimState() == 0 && super.canUse();
      }

      public boolean canContinueToUse() {
         return this.lizard.getAnimState() == 0 && super.canContinueToUse();
      }

      protected void checkAndPerformAttack(LivingEntity target, double dist) {
         if (this.lizard.getAnimState() == 0) {
            if (dist <= this.getAttackReachSqr(target) && this.getTicksUntilNextAttack() <= 0) {
               this.resetAttackCooldown();
               this.lizard.doHurtTarget(target);
            }
         }
      }
   }
}
