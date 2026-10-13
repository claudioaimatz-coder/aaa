package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.entity.template.HumanoidNPCEntity;
import com.mojang.logging.LogUtils;
import java.util.EnumSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class BenimaruEntity extends HumanoidNPCEntity implements IAnimatable {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   public static final EntityDataAccessor<Integer> MISC_ANIMATION = SynchedEntityData.defineId(BenimaruEntity.class, EntityDataSerializers.INT);
   public int miscAnimationTicks = 0;
   protected ServerBossEvent bossEvent;

   public BenimaruEntity(EntityType<? extends BenimaruEntity> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(MISC_ANIMATION, 0);
   }

   @Override
   public void tick() {
      super.tick();
      this.miscAnimationHandler();
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("MiscAnimation", this.getMiscAnimation());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.getEntityData().set(MISC_ANIMATION, compound.getInt("MiscAnimation"));
   }

   public static AttributeSupplier setAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 8000.0)
         .add(Attributes.ARMOR, 400.0)
         .add(Attributes.ATTACK_DAMAGE, 500.0)
         .add(Attributes.MOVEMENT_SPEED, 0.25)
         .add(Attributes.FOLLOW_RANGE, 64.0)
         .build();
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
   }

   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController(this, "controller", 0.0F, this::predicate));
      data.addAnimationController(new AnimationController(this, "attackcontroller", 0.0F, this::miscPredicate));
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      return PlayState.CONTINUE;
   }

   protected void miscAnimationHandler() {
      if (this.getMiscAnimation() != 0) {
         this.miscAnimationTicks++;
         if (!this.isAlive()) {
            return;
         }

         if (!this.getLevel().isClientSide()) {
         }

         if (this.miscAnimationTicks > this.getAnimationTick(this.getMiscAnimation())) {
            this.setMiscAnimation(0);
            this.miscAnimationTicks = 0;
         }
      }
   }

   private int getAnimationTick(int miscAnimation) {
      return switch (miscAnimation) {
         case 1 -> 30;
         default -> 100;
      };
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

   private <E extends IAnimatable> PlayState miscPredicate(AnimationEvent<E> event) {
      return PlayState.CONTINUE;
   }

   public int getMiscAnimation() {
      return (Integer)this.getEntityData().get(MISC_ANIMATION);
   }

   @Override
   public void setMiscAnimation(int animation) {
   }

   static class BenimaruAttackGoal extends Goal {
      private static final Logger logger = LogUtils.getLogger();
      private final BenimaruEntity benimaru;

      public BenimaruAttackGoal(BenimaruEntity entity) {
         this.setFlags(EnumSet.of(Flag.MOVE));
         this.benimaru = entity;
      }

      public boolean canUse() {
         LivingEntity target = this.benimaru.getTarget();
         if (target instanceof Player player && player.isCreative()) {
            this.benimaru.forgetCurrentTargetAndRefreshUniversalAnger();
            return false;
         }

         return target != null && target.isAlive();
      }

      public boolean canContinueToUse() {
         return this.canUse();
      }

      public void start() {
         this.benimaru.setAggressive(true);
      }

      public void tick() {
      }
   }
}
