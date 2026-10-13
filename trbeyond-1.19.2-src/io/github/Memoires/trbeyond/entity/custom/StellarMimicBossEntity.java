package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.registry.items.TensuraMaterialItems;
import io.github.Memoires.trbeyond.network.BeyondNetwork;
import io.github.Memoires.trbeyond.network.SyncAnimationPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
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

public class StellarMimicBossEntity extends Monster implements IAnimatable {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   private static final EntityDataAccessor<Integer> ATTACK_ID = SynchedEntityData.defineId(StellarMimicBossEntity.class, EntityDataSerializers.INT);
   private int attackTicks = 0;
   private int queuedFollowup = 0;
   private int lastAnimAttackId = -1;
   private static final int ATK_NONE = 0;
   private static final int ATK_BITE = 1;
   private static final int ATK_HEAVY = 2;
   private static final int ATK_LICK = 3;
   private static final int ATK_LICK2 = 4;
   private static final int ATK_LICK_GRAB = 5;
   private static final int ATK_HOP_SLAM = 6;
   private static final int ATK_EAT = 7;
   private static final int ATK_HOP_ONLY = 8;
   private static final int LEN_BITE = 30;
   private static final int LEN_HEAVY = 80;
   private static final int LEN_LICK = 40;
   private static final int LEN_LICK2 = 40;
   private static final int LEN_LICK_GRAB = 50;
   private static final int LEN_HOP_SLAM = 70;
   private static final int LEN_EAT = 155;
   private static final int LEN_HOP_ONLY = 16;
   private int hopCooldown = 0;
   private static final int HOP_CD_MIN = 1;
   private static final int HOP_CD_MAX = 2;
   private static final double HOP_Y = 0.5;
   private static final double HOP_FWD = 0.7;
   private int storedSilver = 0;
   private int storedGold = 0;
   private int storedStellar = 0;
   private static final int MAX_COIN_HP = 10000;
   private final ServerBossEvent bossEvent;

   public StellarMimicBossEntity(EntityType<? extends Monster> type, Level level) {
      super(type, level);
      this.xpReward = 20;
      this.bossEvent = new ServerBossEvent(Component.translatable("entity.trbeyond.stellar_mimic_boss"), BossBarColor.PURPLE, BossBarOverlay.NOTCHED_10);
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 500.0)
         .add(Attributes.ARMOR, 0.0)
         .add(Attributes.ATTACK_DAMAGE, 50.0)
         .add(Attributes.MOVEMENT_SPEED, 0.3)
         .add(Attributes.FOLLOW_RANGE, 32.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(ATTACK_ID, 0);
   }

   public void setHealth(float health) {
      super.setHealth(health);
      if (this.bossEvent != null) {
         this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
      }
   }

   public void startSeenByPlayer(ServerPlayer player) {
      super.startSeenByPlayer(player);
      this.bossEvent.addPlayer(player);
   }

   public void stopSeenByPlayer(ServerPlayer player) {
      super.stopSeenByPlayer(player);
      this.bossEvent.removePlayer(player);
   }

   public void customServerAiStep() {
      super.customServerAiStep();
      this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
   }

   public int getAttackId() {
      return (Integer)this.entityData.get(ATTACK_ID);
   }

   public void setAttackId(int id) {
      if (id == 0 || id != this.getAttackId()) {
         this.entityData.set(ATTACK_ID, id);
         if (id != 0) {
            this.attackTicks = 0;
            this.lastAnimAttackId = -1;
         }
      }
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new StellarMimicBossEntity.MimicAttackGoal(this, 1.1, true));
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 35.0F));
      this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false));
   }

   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      tag.putInt("MimicAttackId", this.getAttackId());
      tag.putInt("MimicAttackTicks", this.attackTicks);
      tag.putInt("MimicQueuedFollowup", this.queuedFollowup);
      tag.putInt("HopCooldown", this.hopCooldown);
      tag.putInt("StoredSilver", this.storedSilver);
      tag.putInt("StoredGold", this.storedGold);
      tag.putInt("StoredStellar", this.storedStellar);
   }

   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      this.entityData.set(ATTACK_ID, tag.getInt("MimicAttackId"));
      this.attackTicks = tag.getInt("MimicAttackTicks");
      this.queuedFollowup = tag.getInt("MimicQueuedFollowup");
      this.hopCooldown = tag.getInt("HopCooldown");
      this.storedSilver = tag.getInt("StoredSilver");
      this.storedGold = tag.getInt("StoredGold");
      this.storedStellar = tag.getInt("StoredStellar");
   }

   public void aiStep() {
      super.aiStep();
      if (this.getAttackId() != 0) {
         this.attackTicks++;
         LivingEntity target = this.getTarget();
         if (target != null) {
            this.getLookControl().setLookAt(target, 30.0F, 30.0F);
            double dx = target.getX() - this.getX();
            double dz = target.getZ() - this.getZ();
            float yaw = (float)(Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
            this.setYRot(yaw);
            this.yBodyRot = yaw;
            this.yHeadRot = yaw;
         }

         switch (this.getAttackId()) {
            case 1:
               if (this.attackTicks == 15 && target != null && this.isInMeleeRange(target)) {
                  this.hitTarget(target, 1.0F, 0.1);
               }

               if (this.attackTicks >= 30) {
                  this.endAttackOrQueue();
               }
               break;
            case 2:
               if (this.attackTicks == 65 && target != null && this.isInMeleeRange(target)) {
                  this.hitTarget(target, 2.0F, 0.1);
               }

               if (this.attackTicks >= 80) {
                  this.endAttackOrQueue();
               }
               break;
            case 3:
               if (this.attackTicks == 25 && target != null && this.isInMeleeRange(target)) {
                  this.hitTarget(target, 1.0F, 0.0);
                  float yaw = this.getYRot() * (float) (Math.PI / 180.0);
                  double leftX = (double)(-Mth.cos(yaw));
                  double leftZ = (double)Mth.sin(yaw);
                  target.push(leftX * 0.8, 0.2, leftZ * 0.8);
               }

               if (this.attackTicks >= 40) {
                  this.endAttackOrQueue();
               }
               break;
            case 4:
               if (this.attackTicks == 25 && target != null && this.isInMeleeRange(target)) {
                  this.hitTarget(target, 1.0F, 0.0);
                  float yaw = this.getYRot() * (float) (Math.PI / 180.0);
                  double leftX = (double)Mth.cos(yaw);
                  double leftZ = (double)(-Mth.sin(yaw));
                  target.push(leftX * 0.8, 0.2, leftZ * 0.8);
               }

               if (this.attackTicks >= 40) {
                  this.endAttackOrQueue();
               }
               break;
            case 5:
               if (this.attackTicks == 40 && target != null && this.distanceToSqr(target) < 25.0) {
                  this.hitTarget(target, 0.5F, 0.0);
                  Vec3 frontx = this.getFrontPosition(1.2, 0.0);
                  target.teleportTo(frontx.x, frontx.y, frontx.z);
                  target.setYRot(this.getYRot() + 180.0F);
               }

               if (this.attackTicks >= 50) {
                  if (this.queuedFollowup != 0) {
                     int next = this.queuedFollowup;
                     this.queuedFollowup = 0;
                     if (next == 7 && target != null && this.distanceToSqr(target) < 7.0) {
                        this.entityData.set(ATTACK_ID, 7);
                        this.attackTicks = 0;
                        return;
                     }
                  }

                  this.endAttackOrQueue();
               }
               break;
            case 6:
               if (this.attackTicks < 20) {
                  if (this.onGround) {
                     Vec3 v = this.getDeltaMovement();
                     this.setDeltaMovement(0.0, Math.min(v.y, 0.0), 0.0);
                  } else {
                     Vec3 v = this.getDeltaMovement();
                     this.setDeltaMovement(v.x * 0.5, v.y, v.z * 0.5);
                  }
               } else if (this.attackTicks == 30) {
                  if (!this.onGround) {
                     this.attackTicks--;
                     return;
                  }

                  this.setDeltaMovement(0.0, 1.0, 0.0);
                  this.hasImpulse = true;
               } else if (this.attackTicks > 20 && this.attackTicks < 40 && target != null) {
                  Vec3 dir = new Vec3(target.getX() - this.getX(), 0.0, target.getZ() - this.getZ()).normalize();
                  this.setDeltaMovement(dir.x * 0.6, this.getDeltaMovement().y, dir.z * 0.6);
               } else if (this.attackTicks == 50 && target != null) {
                  if (this.distanceToSqr(target) < 64.0) {
                     this.hitTarget(target, 4.0F, 0.2);
                     target.push(this.getLookAngle().x * 2.0, 0.1, this.getLookAngle().z * 2.0);
                  }
               } else if (this.attackTicks > 50 && this.onGround) {
                  this.setDeltaMovement(Vec3.ZERO);
               }

               if (this.attackTicks >= 70) {
                  this.endAttackOrQueue();
               }
               break;
            case 7:
               if (this.attackTicks >= 65 && target instanceof Player player) {
                  if ((this.attackTicks - 65) % 3 == 0) {
                     this.drainOneCoin(player);
                     this.hitTarget(target, 1.0F, 0.0);
                  }

                  Vec3 front = this.getFrontPosition(1.0, 0.0);
                  player.teleportTo(front.x, front.y, front.z);
                  player.setYRot(this.getYRot() + 180.0F);
                  if (this.attackTicks == 65) {
                     BeyondNetwork.toAll(new SyncAnimationPacket(player.getId(), "eat_player_animation", true));
                  }

                  if (this.attackTicks >= 155) {
                     BeyondNetwork.toAll(new SyncAnimationPacket(player.getId()));
                  }
               }

               if (this.attackTicks >= 155) {
                  this.endAttackOrQueue();
               }
               break;
            case 8:
               this.attackTicks = 16;
               this.endAttackOrQueue();
         }
      } else {
         if (this.getAttackId() == 0 && this.onGround) {
            if (this.hopCooldown > 0) {
               this.hopCooldown--;
            } else {
               LivingEntity t = this.getTarget();
               if (t != null) {
                  this.getLookControl().setLookAt(t, 30.0F, 30.0F);
                  this.leapToward(t, 0.7, 0.5);
               } else if (this.getNavigation().isInProgress()) {
                  Vec3 f = Vec3.directionFromRotation(0.0F, this.getYRot());
                  this.setDeltaMovement(
                     this.getDeltaMovement().x * 0.4 + f.x * 0.7,
                     Math.max(this.getDeltaMovement().y, 0.5),
                     this.getDeltaMovement().z * 0.4 + f.z * 0.7
                  );
                  this.getJumpControl().jump();
               }

               this.resetHopCooldown();
            }
         } else if (this.getAttackId() == 0 && !this.onGround) {
            LivingEntity t = this.getTarget();
            if (t != null) {
               double dx = t.getX() - this.getX();
               double dz = t.getZ() - this.getZ();
               float yaw = (float)(Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
               this.setYRot(yaw);
               this.yBodyRot = yaw;
               this.yHeadRot = yaw;
            }
         }
      }
   }

   public boolean hurt(DamageSource pSource, float pAmount) {
      return pSource.isFall() ? false : super.hurt(pSource, pAmount);
   }

   private void endAttackOrQueue() {
      this.setAttackId(0);
      this.attackTicks = 0;
      this.queuedFollowup = 0;
   }

   private boolean isInMeleeRange(LivingEntity target) {
      double extraRange = 4.0;
      double reach = (double)(this.getBbWidth() * this.getBbWidth() * 10.0F + target.getBbWidth());
      return this.distanceToSqr(target) <= reach + extraRange;
   }

   private void hitTarget(LivingEntity target, float damageMultiplier, double knockUp) {
      float base = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
      target.hurt(DamageSource.mobAttack(this), base * damageMultiplier);
      if (knockUp > 0.0) {
         target.push(0.0, knockUp, 0.0);
      }
   }

   private Vec3 getFrontPosition(double distance, double yOffset) {
      float yawRad = this.getYRot() * (float) (Math.PI / 180.0);
      double x = this.getX() - (double)Mth.sin(yawRad) * distance;
      double z = this.getZ() + (double)Mth.cos(yawRad) * distance;
      double y = this.getY() + yOffset;
      return new Vec3(x, y, z);
   }

   private boolean drainOneCoin(Player player) {
      Inventory inv = player.getInventory();

      for (int i = 0; i < inv.getContainerSize(); i++) {
         ItemStack stack = inv.getItem(i);
         if (!stack.isEmpty()) {
            double bonus = 0.0;
            if (stack.is((Item)TensuraMaterialItems.SILVER_COIN.get())) {
               bonus = 1.0;
               this.storedSilver++;
            } else if (stack.is((Item)TensuraMaterialItems.GOLD_COIN.get())) {
               bonus = 100.0;
               this.storedGold++;
            } else if (stack.is((Item)TensuraMaterialItems.STELLAR_GOLD_COIN.get())) {
               bonus = 10000.0;
               this.storedStellar++;
            }

            if (bonus > 0.0) {
               stack.shrink(1);
               AttributeInstance attr = this.getAttribute(Attributes.MAX_HEALTH);
               double newMax = attr.getBaseValue() + bonus;
               attr.setBaseValue(newMax);
               this.setHealth((float)Math.min((double)this.getHealth() + bonus, newMax));
               return true;
            }
         }
      }

      return false;
   }

   protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
      super.dropCustomDeathLoot(source, looting, recentlyHit);
      if (this.storedSilver > 0) {
         this.spawnAtLocation(new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get(), this.storedSilver));
      }

      if (this.storedGold > 0) {
         this.spawnAtLocation(new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get(), this.storedGold));
      }

      if (this.storedStellar > 0) {
         this.spawnAtLocation(new ItemStack((ItemLike)TensuraMaterialItems.STELLAR_GOLD_COIN.get(), this.storedStellar));
      }

      if (this.random.nextFloat() < 0.01F) {
         this.spawnAtLocation(new ItemStack((ItemLike)TensuraMaterialItems.STELLAR_GOLD_COIN.get(), 1));
      }
   }

   private void leapToward(LivingEntity target, double forward, double up) {
      Vec3 cur = this.getDeltaMovement();
      Vec3 dir = new Vec3(target.getX() - this.getX(), 0.0, target.getZ() - this.getZ()).normalize();
      double nx = cur.x * 0.4 + dir.x * forward;
      double nz = cur.z * 0.4 + dir.z * forward;
      double ny = Math.max(cur.y, up);
      this.setDeltaMovement(nx, ny, nz);
      this.getJumpControl().jump();
      double dx = target.getX() - this.getX();
      double dz = target.getZ() - this.getZ();
      float yaw = (float)(Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
      this.setYRot(yaw);
      this.yBodyRot = yaw;
      this.yHeadRot = yaw;
   }

   private void resetHopCooldown() {
      this.hopCooldown = Mth.nextInt(this.getRandom(), 1, 2);
   }

   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController(this, "movement", 1.0F, this::movementPredicate));
      data.addAnimationController(new AnimationController(this, "attacks", 1.0F, this::attacksPredicate));
   }

   private <E extends IAnimatable> PlayState movementPredicate(AnimationEvent<E> event) {
      if (this.getAttackId() != 0) {
         return PlayState.STOP;
      } else {
         if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("hop", EDefaultLoopTypes.LOOP));
         } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", EDefaultLoopTypes.LOOP));
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
      } else {
         if (id != this.lastAnimAttackId) {
            event.getController().markNeedsReload();
            event.getController().clearAnimationCache();
            switch (id) {
               case 1:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("bite", EDefaultLoopTypes.PLAY_ONCE));
                  break;
               case 2:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("heavy_bite", EDefaultLoopTypes.PLAY_ONCE));
                  break;
               case 3:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("lick", EDefaultLoopTypes.PLAY_ONCE));
                  break;
               case 4:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("lick2", EDefaultLoopTypes.PLAY_ONCE));
                  break;
               case 5:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("lick_grab", EDefaultLoopTypes.PLAY_ONCE));
                  break;
               case 6:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("hop2", EDefaultLoopTypes.PLAY_ONCE));
                  break;
               case 7:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("eat", EDefaultLoopTypes.PLAY_ONCE));
                  break;
               case 8:
                  event.getController().setAnimation(new AnimationBuilder().addAnimation("hop", EDefaultLoopTypes.PLAY_ONCE));
            }

            this.lastAnimAttackId = id;
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

   private static class MimicAttackGoal extends MeleeAttackGoal {
      private final StellarMimicBossEntity mimic;

      MimicAttackGoal(StellarMimicBossEntity mob, double speedModifier, boolean longMemory) {
         super(mob, speedModifier, longMemory);
         this.mimic = mob;
      }

      public void tick() {
         if (this.mimic.getAttackId() == 0) {
            super.tick();
         } else {
            LivingEntity t = this.mimic.getTarget();
            if (t != null) {
               this.mimic.getLookControl().setLookAt(t, 30.0F, 30.0F);
            }
         }
      }

      protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
         if (this.mimic.getAttackId() == 0 && enemy.isAlive()) {
            double reach = this.getAttackReachSqr(enemy);
            int chosen = this.chooseWeightedAttack(this.mob.getRandom(), distToEnemySqr, reach);
            if (chosen != 0) {
               boolean melee = chosen == 1 || chosen == 2 || chosen == 3 || chosen == 4 || chosen == 5 || chosen == 7;
               if (!melee || !(distToEnemySqr > reach)) {
                  if (this.isTimeToAttack()) {
                     this.resetAttackCooldown();
                     this.mimic.setAttackId(chosen);
                  }
               }
            }
         }
      }

      private int chooseWeightedAttack(RandomSource rng, double dist2, double reach) {
         double FAR2 = reach * 4.0;
         double MID2 = reach * 2.0;
         if (dist2 > FAR2) {
            return rng.nextFloat() < 0.65F ? 8 : 6;
         } else if (dist2 > MID2) {
            float r = rng.nextFloat();
            if (r < 0.2F) {
               return 1;
            } else if (r < 0.35F) {
               return 2;
            } else if (r < 0.45F) {
               return 3;
            } else if (r < 0.55F) {
               return 4;
            } else {
               return r < 0.85F ? 8 : 6;
            }
         } else {
            float r = rng.nextFloat();
            if (r < 0.3F) {
               return 1;
            } else if (r < 0.52F) {
               return 2;
            } else if (r < 0.66F) {
               return 3;
            } else if (r < 0.78F) {
               return 4;
            } else if (r < 0.815F) {
               this.mimic.queuedFollowup = 7;
               return 5;
            } else if (r < 0.995F) {
               return 6;
            } else {
               this.mimic.queuedFollowup = 0;
               return 5;
            }
         }
      }

      protected double getAttackReachSqr(LivingEntity target) {
         return (double)(this.mob.getBbWidth() * this.mob.getBbWidth() * 3.0F + target.getBbWidth());
      }
   }
}
