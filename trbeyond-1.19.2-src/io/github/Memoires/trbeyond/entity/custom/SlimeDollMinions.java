package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
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

public class SlimeDollMinions extends Monster implements IAnimatable {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   private static final EntityDataAccessor<Integer> DATA_ATTACK_ID = SynchedEntityData.defineId(SlimeDollMinions.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> DATA_MODE = SynchedEntityData.defineId(SlimeDollMinions.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> DATA_ATTACK_SEQ = SynchedEntityData.defineId(SlimeDollBossEntity.class, EntityDataSerializers.INT);
   private int lastSeenAttackSeq = -1;
   private static final double MAX_HP = 50.0;
   private static final double BASE_DAMAGE = 25.0;
   private static final int ATK_NONE = 0;
   private static final int ATK_WALK_BOULDER = 2;
   private static final int ATK_WALK_BOULDER_2 = 3;
   private static final int ATK_CRUSH = 4;
   private static final int ATK_THRUST = 14;
   private static final int ATK_DOUBLE_THRUST = 15;
   private static final int ATK_JUMP_ANIM = 16;
   private static final int ATK_BOULDER_ANIM = 22;
   private static final int ATK_BOULDER_TO_ARMS = 27;
   private static final int ATK_MELT_BOULDER = 35;
   private static final int ATK_MELTED_JUMP = 11;
   private static final int ATK_MELTED_GRAB = 12;
   private static final int ATK_ENCASE_IDLE = 13;
   private static final int LEN_CRUSH = 20;
   private static final int LEN_BOULDER_MELT = 30;
   private static final int LEN_WALKATTACK_BOULDER = 18;
   private static final int LEN_WALKATTACK_BOULDER2 = 18;
   private static final int LEN_MELTED_JUMP_ATTACK = 40;
   private static final int LEN_ENCASEPLAYERIDLE = 20;
   private static final int LEN_BOULDER = 45;
   private static final int LEN_MELTEDGRAB = 35;
   private static final int LEN_BOULDER_TO_ARMS = 40;
   private static final int MODE_NORMAL = 0;
   private static final int MODE_BOULDER = 1;
   private static final int MODE_MELT = 5;
   private int attackTicks = 0;
   private int lastAnimAttackId = -1;
   private int queuedFollowup = 0;
   private boolean fightStarted = false;
   private int modeTicks = 0;

   public SlimeDollMinions(EntityType<? extends Monster> type, Level level) {
      super(type, level);
      this.xpReward = 20;
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 50.0)
         .add(Attributes.ARMOR, 0.0)
         .add(Attributes.ATTACK_DAMAGE, 25.0)
         .add(Attributes.MOVEMENT_SPEED, 0.3)
         .add(Attributes.FOLLOW_RANGE, 32.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(DATA_ATTACK_ID, 0);
      this.entityData.define(DATA_MODE, 0);
      this.entityData.define(DATA_ATTACK_SEQ, 0);
   }

   public int getAttackId() {
      return (Integer)this.entityData.get(DATA_ATTACK_ID);
   }

   public void setAttackId(int id) {
      if (id == 0 || id != this.getAttackId()) {
         this.entityData.set(DATA_ATTACK_ID, id);
         int seq = (Integer)this.entityData.get(DATA_ATTACK_SEQ);
         this.entityData.set(DATA_ATTACK_SEQ, seq + 1);
         this.lastAnimAttackId = -1;
         if (id != 0) {
            this.attackTicks = 0;
         }
      }
   }

   public int getMode() {
      return (Integer)this.entityData.get(DATA_MODE);
   }

   public void setMode(int mode) {
      this.entityData.set(DATA_MODE, mode);
   }

   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      tag.putInt("BossAttackId", this.getAttackId());
      tag.putInt("BossAttackTicks", this.attackTicks);
      tag.putInt("BossQueuedFollowup", this.queuedFollowup);
      tag.putInt("BossAnimMode", this.getMode());
      tag.putInt("BossModeTicks", this.modeTicks);
      tag.putBoolean("BossFightStarted", this.fightStarted);
   }

   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      this.entityData.set(DATA_ATTACK_ID, tag.getInt("BossAttackId"));
      this.attackTicks = tag.getInt("BossAttackTicks");
      this.queuedFollowup = tag.getInt("BossQueuedFollowup");
      int loadedMode = tag.contains("BossAnimMode") ? tag.getInt("BossAnimMode") : 0;
      this.entityData.set(DATA_MODE, loadedMode);
      this.setMode(loadedMode);
      this.modeTicks = tag.contains("BossModeTicks") ? tag.getInt("BossModeTicks") : 0;
      this.fightStarted = tag.contains("BossFightStarted") && tag.getBoolean("BossFightStarted");
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new SlimeDollMinions.BossAttackGoal(this, 1.1, true));
      this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8));
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 30.0F));
      this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, false));
   }

   private boolean isPhysical(DamageSource src) {
      if (src.isExplosion() || src.isMagic() || src == DamageSource.OUT_OF_WORLD) {
         return false;
      } else {
         return src == DamageSource.WITHER
            ? false
            : src == DamageSource.GENERIC
               || src == DamageSource.FALL
               || src == DamageSource.FALLING_BLOCK
               || src == DamageSource.ANVIL
               || src == DamageSource.CACTUS
               || src == DamageSource.SWEET_BERRY_BUSH
               || src.isProjectile();
      }
   }

   public void aiStep() {
      super.aiStep();
      if (!this.level.isClientSide) {
         LivingEntity t = this.getTarget();
         if (!this.fightStarted && t != null) {
            this.fightStarted = true;
            this.setMode(1);
            this.setAttackId(22);
         }

         if (this.getAttackId() != 0) {
            this.processAttackTick();
            return;
         }

         if (t != null && this.onGround && this.getRandom().nextFloat() < 0.02F) {
            int chosen = this.chooseWeightedAttack(t.distanceToSqr(this));
            if (chosen != 0) {
               this.setAttackId(chosen);
            }
         }
      }
   }

   private void processAttackTick() {
      this.attackTicks++;
      LivingEntity target = this.getTarget();
      if (target != null) {
         this.getLookControl().setLookAt(target, 30.0F, 30.0F);
      }

      if (!this.attackAllowedInMode(this.getAttackId(), this.getMode())) {
         this.endAttackOrQueue();
      } else {
         switch (this.getAttackId()) {
            case 2:
               if (this.attackTicks == 10 && target != null && this.isInMeleeRange(target)) {
                  this.hitTarget(target, 1.0F, 0.2);
               }

               if (this.attackTicks >= 18) {
                  this.endAttackOrQueue();
               }
               break;
            case 3:
               if (this.attackTicks == 12 && target != null && this.isInMeleeRange(target)) {
                  this.hitTarget(target, 1.0F, 0.2);
               }

               if (this.attackTicks >= 18) {
                  this.endAttackOrQueue();
               }
               break;
            case 4:
               this.getNavigation().stop();
               if (this.attackTicks == 15 && target != null && this.isInMeleeRange(target)) {
                  this.hitTarget(target, 2.0F, 0.6);
                  target.push(this.getLookAngle().x * 2.5, 0.1, this.getLookAngle().z * 2.5);
               }

               if (this.attackTicks >= 20) {
                  this.endAttackOrQueue();
               }
               break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 23:
            case 24:
            case 25:
            case 26:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            default:
               this.endAttackOrQueue();
               break;
            case 11:
               if (this.attackTicks == 12 && target != null) {
                  this.leapToward(target, 1.2, 0.6);
               } else if (this.attackTicks == 23 && target != null) {
                  this.hitTarget(target, 1.0F, 0.2);
               }

               if (this.attackTicks >= 40) {
                  this.setMode(1);
                  this.endAttackOrQueue();
               }
               break;
            case 12:
               if (this.attackTicks > 10 && target != null && this.isInMeleeRange(target)) {
                  Vec3 front = this.getFrontPosition(1.0, 0.0);
                  target.teleportTo(front.x, front.y, front.z);
                  this.hitTarget(target, 0.5F, 0.0);
               }

               if (this.attackTicks >= 35) {
                  this.queuedFollowup = 13;
                  this.endAttackOrQueue();
               }
               break;
            case 13:
               if (target instanceof Player p) {
                  this.getNavigation().stop();
                  if (this.attackTicks < 20 && this.isInMeleeRange(target)) {
                     p.teleportTo(this.getX(), this.getY(), this.getZ());
                     p.setYRot(this.getYRot() + 180.0F);
                     this.setDeltaMovement(0.0, 0.0, 0.0);
                     this.hitTarget(target, 0.8F, 0.0);
                  }
               }

               if (this.attackTicks >= 20) {
                  this.setMode(1);
                  this.endAttackOrQueue();
               }
               break;
            case 22:
               if (this.attackTicks >= 45) {
                  this.endAttackOrQueue();
               }
               break;
            case 27:
               this.getNavigation().stop();
               if (this.attackTicks >= 40) {
                  this.setMode(0);
                  this.endAttackOrQueue();
               }
               break;
            case 35:
               this.getNavigation().stop();
               if (this.attackTicks >= 30) {
                  if (this.queuedFollowup > 0) {
                     int next = this.queuedFollowup;
                     this.queuedFollowup = 0;
                     this.setAttackId(next);
                  } else {
                     this.endAttackOrQueue();
                  }
               }
         }
      }
   }

   private void endAttackOrQueue() {
      this.entityData.set(DATA_ATTACK_ID, 0);
      if (this.queuedFollowup > 0) {
         int next = this.queuedFollowup;
         this.queuedFollowup = 0;
         this.setAttackId(next);
      } else {
         this.attackTicks = 0;
         this.queuedFollowup = 0;
         this.lastAnimAttackId = -1;
      }
   }

   private boolean attackAllowedInMode(int attackId, int mode) {
      return switch (attackId) {
         case 0 -> true;
         default -> true;
         case 2, 3, 4, 11, 12, 13, 22, 27, 35 -> mode == 1 || mode == 5;
      };
   }

   public int chooseWeightedAttack(double dist2) {
      double reachPref = 16.0;
      int mode = this.getMode();

      for (int attempt = 0; attempt < 6; attempt++) {
         float r = this.random.nextFloat();
         int candidate = 0;
         switch (mode) {
            case 0:
               candidate = 22;
               this.setMode(1);
               break;
            case 1:
               if ((double)r < 0.4) {
                  candidate = 3;
               } else if ((double)r < 0.6) {
                  candidate = 4;
               } else if ((double)r < 0.9) {
                  candidate = 2;
               } else {
                  candidate = 3;
               }
               break;
            case 5:
               if (dist2 > reachPref * 4.0) {
                  candidate = 11;
               } else if ((double)r < 0.5) {
                  candidate = 12;
               } else {
                  candidate = 11;
               }
         }

         if (this.attackAllowedInMode(candidate, mode)) {
            return candidate;
         }
      }

      return 0;
   }

   private boolean isInMeleeRange(LivingEntity target) {
      double reach = (double)(this.getBbWidth() * this.getBbWidth() * 10.0F + target.getBbWidth());
      return this.distanceToSqr(target) <= reach;
   }

   private void hitTarget(LivingEntity target, float damageMultiplier, double knockUp) {
      if (target != null) {
         float base = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
         target.hurt(TensuraDamageSources.CORROSION, base * damageMultiplier);
         if (knockUp > 0.0) {
            target.push(0.0, knockUp, 0.0);
         }
      }
   }

   private Vec3 getFrontPosition(double distance, double yOffset) {
      float yawRad = this.getYRot() * (float) (Math.PI / 180.0);
      double x = this.getX() - Math.sin((double)yawRad) * distance;
      double z = this.getZ() + Math.cos((double)yawRad) * distance;
      double y = this.getY() + yOffset;
      return new Vec3(x, y, z);
   }

   private void leapToward(LivingEntity target, double forward, double up) {
      Vec3 cur = this.getDeltaMovement();
      Vec3 dir = new Vec3(target.getX() - this.getX(), 0.0, target.getZ() - this.getZ()).normalize();
      double nx = cur.x * 0.4 + dir.x * forward;
      double nz = cur.z * 0.4 + dir.z * forward;
      double ny = Math.max(cur.y, up);
      this.setDeltaMovement(nx, ny, nz);
      this.getJumpControl().jump();
   }

   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController(this, "movement", 0.0F, this::movementPredicate));
      data.addAnimationController(new AnimationController(this, "attacks", 0.0F, this::attacksPredicate));
   }

   private <E extends IAnimatable> PlayState movementPredicate(AnimationEvent<E> event) {
      if (this.getAttackId() != 0) {
         return PlayState.STOP;
      } else {
         String walk = "";
         String idle = "";
         switch (this.getMode()) {
            case 0:
               walk = "animation.model.walk";
               idle = "animation.model.idle";
               break;
            case 1:
               walk = "animation.model.walk_boulder";
               idle = "animation.model.idleball";
               break;
            case 5:
               walk = "animation.model.meltwalk";
               idle = "animation.model.meltwalk";
         }

         if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(walk, EDefaultLoopTypes.LOOP));
         } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(idle, EDefaultLoopTypes.LOOP));
         }

         return PlayState.CONTINUE;
      }
   }

   private <E extends IAnimatable> PlayState attacksPredicate(AnimationEvent<E> event) {
      int id = this.getAttackId();
      if (id == 0) {
         if (event.getController().getAnimationState() == AnimationState.Stopped) {
            this.lastAnimAttackId = -1;
            return PlayState.STOP;
         } else {
            return PlayState.CONTINUE;
         }
      } else if (!this.attackAllowedInMode(id, this.getMode())) {
         this.endAttackOrQueue();
         this.lastAnimAttackId = -1;
         return PlayState.STOP;
      } else {
         int seq = (Integer)this.entityData.get(DATA_ATTACK_SEQ);
         if (seq != this.lastAnimAttackId) {
            event.getController().markNeedsReload();
            switch (id) {
               case 2:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.walkattack_boulder", EDefaultLoopTypes.PLAY_ONCE));
                  break;
               case 3:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.walkattack_boulder2", EDefaultLoopTypes.PLAY_ONCE));
                  break;
               case 4:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.crush", EDefaultLoopTypes.PLAY_ONCE));
               case 5:
               case 6:
               case 7:
               case 8:
               case 9:
               case 10:
               case 17:
               case 18:
               case 19:
               case 20:
               case 21:
               case 23:
               case 24:
               case 25:
               case 26:
               case 28:
               case 29:
               case 30:
               case 31:
               case 32:
               case 33:
               case 34:
               default:
                  break;
               case 11:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.melted_jump_attack", EDefaultLoopTypes.PLAY_ONCE));
                  break;
               case 12:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.meltedgrab", EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
                  break;
               case 13:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.encaseplayeridle", EDefaultLoopTypes.PLAY_ONCE));
                  break;
               case 14:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.thrust", EDefaultLoopTypes.PLAY_ONCE));
                  break;
               case 15:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.double_thrust", EDefaultLoopTypes.PLAY_ONCE));
                  break;
               case 16:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.jump", EDefaultLoopTypes.PLAY_ONCE));
                  break;
               case 22:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.boulder", EDefaultLoopTypes.PLAY_ONCE));
                  break;
               case 27:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.boulder_to_arms", EDefaultLoopTypes.PLAY_ONCE));
                  break;
               case 35:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.melt_boulder", EDefaultLoopTypes.PLAY_ONCE));
            }

            this.lastAnimAttackId = seq;
         }

         return PlayState.CONTINUE;
      }
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag
   ) {
      return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
   }

   private static class BossAttackGoal extends MeleeAttackGoal {
      private final SlimeDollMinions boss;

      BossAttackGoal(SlimeDollMinions mob, double speedModifier, boolean longMemory) {
         super(mob, speedModifier, longMemory);
         this.boss = mob;
      }

      protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
         if (this.boss.getAttackId() == 0 && enemy.isAlive()) {
            if (this.isTimeToAttack()) {
               int chosen = this.boss.chooseWeightedAttack(distToEnemySqr);
               if (chosen != 0) {
                  boolean melee = chosen == 2 || chosen == 3 || chosen == 4 || chosen == 13;
                  if (!melee || !(distToEnemySqr > this.getAttackReachSqr(enemy))) {
                     this.resetAttackCooldown();
                     this.boss.setAttackId(chosen);
                  }
               }
            }
         }
      }

      public void tick() {
         if (this.boss.getAttackId() == 0) {
            super.tick();
         } else {
            LivingEntity t = this.boss.getTarget();
            if (t != null) {
               this.boss.getLookControl().setLookAt(t, 30.0F, 30.0F);
            }
         }
      }

      protected double getAttackReachSqr(LivingEntity target) {
         return (double)(this.mob.getBbWidth() * this.mob.getBbWidth() * 3.0F + target.getBbWidth());
      }
   }
}
