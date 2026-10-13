package io.github.Memoires.trbeyond.entity.custom.slime;

import com.github.manasmods.tensura.api.entity.ai.WanderingFollowOwnerGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraHurtByTargetGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraOwnerHurtByTargetGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraOwnerHurtTargetGoal;
import io.github.Memoires.trbeyond.items.armor.SerpentArmorItem;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
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
import net.minecraft.world.item.ItemStack;
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

public class YellowSlimeDollEntity extends SlimeDollEntity implements IAnimatable {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

   public YellowSlimeDollEntity(EntityType<? extends YellowSlimeDollEntity> pEntityType, Level pLevel) {
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
         .add((Attribute)ForgeMod.ATTACK_RANGE.get(), 2.0)
         .build();
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
      this.goalSelector.addGoal(3, new YellowSlimeDollEntity.YellowSlimeDollAttackGoal());
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

   public boolean rideableUnderWater() {
      return true;
   }

   public boolean shouldRiderSit() {
      return false;
   }

   public void positionRider(Entity passenger) {
      passenger.setPos(this.getX(), this.getY(), this.getZ());
   }

   public boolean doHurtTarget(Entity pEntity) {
      boolean flag = super.doHurtTarget(pEntity);
      if (flag && pEntity instanceof LivingEntity living) {
         for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack slotStack = living.getItemBySlot(slot);
            if (!(slotStack.getItem() instanceof SerpentArmorItem)) {
               slotStack.hurtAndBreak(5, living, player -> player.broadcastBreakEvent(slot));
            }
         }
      }

      return flag;
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
            } else if (this.getMiscAnimation() == 5 && (target == null || target.distanceTo(this) > 3.0F)) {
               this.setMiscAnimation(0);
               this.miscAnimationTicks = 0;
            }

            if (target != null) {
               if (this.getMiscAnimation() == 3) {
                  if (target.distanceTo(this) < 3.0F) {
                     if (this.isMelted()) {
                        if (this.miscAnimationTicks == 34) {
                           target.unRide();
                           target.startRiding(this, true);
                           this.setMelted(false);
                        }
                     } else if (this.miscAnimationTicks == 19) {
                        target.unRide();
                        target.startRiding(this, true);
                        this.setMelted(false);
                     }
                  }
               } else if (this.getMiscAnimation() == 4 && target.getVehicle() == this && this.miscAnimationTicks == 19) {
                  this.setMelted(false);
                  this.setMiscAnimation(5);
                  this.miscAnimationTicks = 0;
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
      if (miscAnimation == 5) {
         return 1200;
      } else if (miscAnimation == 4) {
         return 20;
      } else if (miscAnimation == 3) {
         return this.isMelted() ? 35 : 20;
      } else {
         return 30;
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
      if (this.getMiscAnimation() == 5) {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_yellow.encasing", EDefaultLoopTypes.LOOP));
         return PlayState.CONTINUE;
      } else if (this.getMiscAnimation() != 0) {
         return PlayState.STOP;
      } else {
         if (this.getFirstPassenger() != null) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_yellow.grabbing", EDefaultLoopTypes.LOOP));
         } else if (this.isMelted()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_yellow.melt_walk", EDefaultLoopTypes.LOOP));
         } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_yellow.walk", EDefaultLoopTypes.LOOP));
         }

         return PlayState.CONTINUE;
      }
   }

   private <E extends IAnimatable> PlayState miscPredicate(AnimationEvent<E> event) {
      if (event.getController().getAnimationState().equals(AnimationState.Stopped)) {
         event.getController().markNeedsReload();
         if (this.getMiscAnimation() == 1) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_yellow.spawn", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 2) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_yellow.melt", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 3) {
            if (this.isMelted()) {
               event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_yellow.melted_grab", EDefaultLoopTypes.PLAY_ONCE));
            } else {
               event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_yellow.grab", EDefaultLoopTypes.PLAY_ONCE));
            }
         } else if (this.getMiscAnimation() == 4) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime_yellow.encase_player", EDefaultLoopTypes.PLAY_ONCE));
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

   class YellowSlimeDollAttackGoal extends MeleeAttackGoal {
      private final YellowSlimeDollEntity doll = YellowSlimeDollEntity.this;

      public YellowSlimeDollAttackGoal() {
         super(YellowSlimeDollEntity.this, 2.0, true);
      }

      public boolean canUse() {
         return this.doll.getMiscAnimation() == 1 ? false : super.canUse();
      }

      protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
         if (this.doll.getMiscAnimation() == 0 || this.doll.getMiscAnimation() == 5) {
            double d0 = this.getAttackReachSqr(pEnemy);
            if (pDistToEnemySqr <= d0 && this.getTicksUntilNextAttack() <= 0) {
               this.resetAttackCooldown();
               this.mob.doHurtTarget(pEnemy);
               if (this.doll.getMiscAnimation() != 5) {
                  if (pEnemy.getVehicle() == this.mob) {
                     this.doll.setMiscAnimation(4);
                  } else {
                     this.doll.setMiscAnimation(3);
                  }
               }
            }
         }
      }

      public void start() {
         super.start();
         if (!this.doll.isMelted()) {
            this.doll.setMiscAnimation(2);
         }
      }

      public void stop() {
         super.stop();
         if (this.doll.isMelted()) {
            this.doll.setMiscAnimation(1);
         }
      }

      protected double getAttackReachSqr(LivingEntity pAttackTarget) {
         return super.getAttackReachSqr(pAttackTarget) + 4.0;
      }
   }
}
