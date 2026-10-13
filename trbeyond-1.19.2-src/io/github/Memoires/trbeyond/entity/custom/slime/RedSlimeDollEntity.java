package io.github.Memoires.trbeyond.entity.custom.slime;

import com.github.manasmods.tensura.api.entity.ai.WanderingFollowOwnerGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraHurtByTargetGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraOwnerHurtByTargetGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraOwnerHurtTargetGoal;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
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

public class RedSlimeDollEntity extends SlimeDollEntity implements IAnimatable {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

   public RedSlimeDollEntity(EntityType<? extends RedSlimeDollEntity> pEntityType, Level pLevel) {
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
      this.goalSelector.addGoal(3, new RedSlimeDollEntity.RedSlimeDollAttackGoal());
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
      if (!this.getLevel().isClientSide()) {
         if (this.getMiscAnimation() != 0) {
            this.miscAnimationTicks++;
            if (!this.isAlive()) {
               return;
            }

            LivingEntity target = this.getTarget();
            if (target != null) {
               if (this.getMiscAnimation() == 3) {
                  if (this.miscAnimationTicks == 14 && (double)target.distanceTo(this) < this.getAttributeValue((Attribute)ForgeMod.ATTACK_RANGE.get())) {
                     this.doHurtTarget(target);
                  }
               } else if (this.getMiscAnimation() == 4
                  && this.miscAnimationTicks == 13
                  && (double)target.distanceTo(this) < this.getAttributeValue((Attribute)ForgeMod.ATTACK_RANGE.get())) {
                  this.doHurtTarget(target);
                  target.invulnerableTime = 0;
                  float damage = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 2.0F;
                  target.hurt(DamageSource.mobAttack(this).bypassArmor(), damage);
               }
            }

            if (this.miscAnimationTicks >= this.getAnimationTick(this.getMiscAnimation())) {
               this.setMiscAnimation(0);
               this.miscAnimationTicks = 0;
            }
         }
      }
   }

   private int getAnimationTick(int miscAnimation) {
      if (miscAnimation == 1) {
         return 35;
      } else {
         return miscAnimation == 2 ? 50 : 25;
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
         if (this.isAngry()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_red.walk_spear", EDefaultLoopTypes.LOOP));
         } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_red.walk", EDefaultLoopTypes.LOOP));
         }

         return PlayState.CONTINUE;
      }
   }

   private <E extends IAnimatable> PlayState miscPredicate(AnimationEvent<E> event) {
      if (event.getController().getAnimationState().equals(AnimationState.Stopped)) {
         event.getController().markNeedsReload();
         if (this.getMiscAnimation() == 1) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_red.spawn", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 2) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_red.spear", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 3) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_red.slash", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 4) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_red.pierce", EDefaultLoopTypes.PLAY_ONCE));
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

   class RedSlimeDollAttackGoal extends MeleeAttackGoal {
      private final RedSlimeDollEntity doll = RedSlimeDollEntity.this;

      public RedSlimeDollAttackGoal() {
         super(RedSlimeDollEntity.this, 2.0, true);
      }

      public boolean canUse() {
         return this.doll.getMiscAnimation() == 1 ? false : super.canUse();
      }

      protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
         if (this.doll.getMiscAnimation() == 0) {
            double d0 = this.getAttackReachSqr(pEnemy);
            if (pDistToEnemySqr <= d0 && this.getTicksUntilNextAttack() <= 0) {
               this.resetAttackCooldown();
               if (this.doll.getRandom().nextBoolean()) {
                  this.doll.setMiscAnimation(3);
               } else {
                  this.doll.setMiscAnimation(4);
               }
            }
         }
      }

      public void start() {
         super.start();
         this.doll.setMiscAnimation(2);
      }

      public void stop() {
         super.stop();
         if (this.doll.isMelted()) {
            this.doll.setMiscAnimation(1);
         }
      }

      protected double getAttackReachSqr(LivingEntity pAttackTarget) {
         return super.getAttackReachSqr(pAttackTarget) + 9.0;
      }
   }
}
