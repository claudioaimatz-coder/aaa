package io.github.Memoires.trbeyond.entity.custom.slime;

import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.api.entity.ai.WanderingFollowOwnerGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraHurtByTargetGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraOwnerHurtByTargetGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraOwnerHurtTargetGoal;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;
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

public class BlueSlimeDollEntity extends SlimeDollEntity implements IAnimatable {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

   public BlueSlimeDollEntity(EntityType<? extends BlueSlimeDollEntity> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
      this.xpReward = 100;
      this.maxUpStep = 1.0F;
   }

   public static AttributeSupplier setAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.ARMOR, 0.0)
         .add(Attributes.ATTACK_DAMAGE, 25.0)
         .add(Attributes.MAX_HEALTH, 50.0)
         .add(Attributes.MOVEMENT_SPEED, 0.15F)
         .add(Attributes.FOLLOW_RANGE, 32.0)
         .add((Attribute)ForgeMod.SWIM_SPEED.get(), 2.0)
         .add((Attribute)ForgeMod.ATTACK_RANGE.get(), 4.0)
         .build();
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
      this.goalSelector.addGoal(3, new BlueSlimeDollEntity.BlueSlimeDollAttackGoal());
      this.goalSelector.addGoal(4, new WanderingFollowOwnerGoal(this, 1.5, 10.0F, 5.0F, false));
      this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.2));
      this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.targetSelector.addGoal(1, new TensuraOwnerHurtByTargetGoal(this));
      this.targetSelector.addGoal(2, new TensuraOwnerHurtTargetGoal(this));
      this.targetSelector.addGoal(3, new TensuraHurtByTargetGoal(this, SlimeDollEntity.class).setAlertOthers(new Class[0]));
      this.targetSelector
         .addGoal(5, new NonTameRandomTargetGoal(this, LivingEntity.class, false, entity -> !(entity instanceof SlimeDollEntity) && !entity.isAlliedTo(this)));
      this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal(this, true));
   }

   @Override
   public void tick() {
      super.tick();
      LivingEntity target = this.getTarget();
      if (!this.getLevel().isClientSide()) {
         if (this.getMiscAnimation() != 0) {
            this.miscAnimationTicks++;
            if (!this.isAlive()) {
               return;
            }

            if (this.getMiscAnimation() == 1 && this.miscAnimationTicks == 20) {
               this.setMelted(false);
            } else if (this.getMiscAnimation() == 2 && this.miscAnimationTicks == 20) {
               this.setMelted(true);
            }

            if (this.getMiscAnimation() != 3 || target == null) {
               if (this.getMiscAnimation() == 4 && this.miscAnimationTicks == 23) {
                  AABB aabb = this.getBoundingBox().inflate(this.getAttributeValue((Attribute)ForgeMod.ATTACK_RANGE.get()));
                  List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, aabb, entityx -> !this.isAlliedTo(entityx) && !entityx.equals(this));
                  this.setMelted(false);
                  if (!list.isEmpty()) {
                     for (LivingEntity entity : list) {
                        double damageMultiplier = 2.0;
                        entity.hurt(DamageSource.mobAttack(this), (float)(this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damageMultiplier));
                        entity.getDeltaMovement().add(0.0, 0.5 * damageMultiplier, 0.0);
                        SkillHelper.knockBack(this, entity, 1.5F);
                     }
                  }
               }
            } else if (this.miscAnimationTicks == 15 && (double)target.distanceTo(this) < this.getAttributeValue((Attribute)ForgeMod.ATTACK_RANGE.get())) {
               this.doHurtTarget(target);
            }

            if (this.miscAnimationTicks >= this.getAnimationTick(this.getMiscAnimation())) {
               this.setMiscAnimation(0);
               this.miscAnimationTicks = 0;
            }
         }
      }
   }

   private int getAnimationTick(int miscAnimation) {
      if (miscAnimation == 4) {
         return 40;
      } else {
         return miscAnimation == 3 ? 25 : 30;
      }
   }

   public void die(DamageSource source) {
      super.die(source);
      if (!this.isAlive()) {
         if (this.getPose() == Pose.DYING) {
            this.setMiscAnimation(2);
         }
      }
   }

   protected void tickDeath() {
      if (++this.deathTime >= 29) {
         this.remove(RemovalReason.KILLED);
      }
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag
   ) {
      this.setMiscAnimation(1);
      return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
   }

   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      if (this.getMiscAnimation() != 0) {
         return PlayState.STOP;
      } else {
         if (this.isMelted()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_blue.melt_walk", EDefaultLoopTypes.LOOP));
         } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_blue.walk", EDefaultLoopTypes.LOOP));
         }

         return PlayState.CONTINUE;
      }
   }

   private <E extends IAnimatable> PlayState miscPredicate(AnimationEvent<E> event) {
      if (event.getController().getAnimationState().equals(AnimationState.Stopped)) {
         event.getController().markNeedsReload();
         if (this.getMiscAnimation() == 1) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_blue.spawn", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 2) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_blue.melt", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 3) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_blue.attack", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 4) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_blue.melted_slam", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 5) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_blue.hammers", EDefaultLoopTypes.PLAY_ONCE));
         }
      }

      return PlayState.CONTINUE;
   }

   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController(this, "controller", 0.0F, this::predicate));
      data.addAnimationController(new AnimationController(this, "miscController", 0.0F, this::miscPredicate));
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   class BlueSlimeDollAttackGoal extends MeleeAttackGoal {
      private final BlueSlimeDollEntity doll = BlueSlimeDollEntity.this;

      public BlueSlimeDollAttackGoal() {
         super(BlueSlimeDollEntity.this, 2.0, true);
      }

      public boolean canUse() {
         return this.doll.getMiscAnimation() == 1 ? false : super.canUse();
      }

      public void start() {
         super.start();
         if (!this.doll.isMelted()) {
            this.doll.setMiscAnimation(5);
         }
      }

      protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
         if (this.doll.getMiscAnimation() == 0) {
            int attack = this.getAttack(pDistToEnemySqr);
            double d0 = attack == 2 ? 400.0 : this.getAttackReachSqr(pEnemy);
            if (pDistToEnemySqr <= d0 && this.getTicksUntilNextAttack() <= 0) {
               this.resetAttackCooldown();
               this.doll.setMiscAnimation(attack);
            }
         }
      }

      private int getAttack(double dist) {
         if (this.doll.isMelted()) {
            return 4;
         } else {
            return this.doll.getRandom().nextFloat() <= 0.3F && dist > 36.0 ? 2 : 3;
         }
      }

      protected double getAttackReachSqr(LivingEntity pAttackTarget) {
         return super.getAttackReachSqr(pAttackTarget) + 9.0;
      }
   }
}
