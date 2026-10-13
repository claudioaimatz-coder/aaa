package io.github.Memoires.trbeyond.entity.misc;

import com.github.manasmods.tensura.api.entity.ai.WanderingFollowOwnerGoal;
import com.github.manasmods.tensura.api.entity.controller.JumpingEntityMoveControl;
import com.github.manasmods.tensura.api.entity.navigator.StraightFlightNavigator;
import com.github.manasmods.tensura.api.entity.subclass.IJumpingEntity;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraHurtByTargetGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraOwnerHurtByTargetGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraOwnerHurtTargetGoal;
import com.github.manasmods.tensura.item.food.HealingPotionItem;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.mojang.math.Vector3f;
import io.github.Memoires.trbeyond.block.DungeonChestBlock;
import io.github.Memoires.trbeyond.util.BeyondDamageSources;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class MimicChestEntity extends TensuraTamableEntity implements IJumpingEntity, IAnimatable {
   private int jumpTicks;
   private int jumpDuration;
   private boolean wasOnGround;
   private int jumpDelayTicks;
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   private final boolean golden;

   public MimicChestEntity(EntityType<? extends MimicChestEntity> pEntityType, Level pLevel, boolean golden) {
      super(pEntityType, pLevel);
      this.xpReward = 10;
      this.lookControl = new MimicChestEntity.MimicLookControl(this);
      this.moveControl = new JumpingEntityMoveControl(this);
      this.navigation = new StraightFlightNavigator(this, pLevel);
      this.golden = golden;
   }

   public static AttributeSupplier setAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.ATTACK_DAMAGE, 25.0)
         .add(Attributes.MAX_HEALTH, 100.0)
         .add(Attributes.MOVEMENT_SPEED, 0.4F)
         .add(Attributes.FOLLOW_RANGE, 32.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
         .build();
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level));
      this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
      this.goalSelector.addGoal(2, new WanderingFollowOwnerGoal(this, 2.0, 20.0F, 5.0F, false));
      this.goalSelector.addGoal(4, new BreedGoal(this, 2.0));
      this.targetSelector.addGoal(1, new TensuraOwnerHurtByTargetGoal(this));
      this.targetSelector.addGoal(2, new TensuraOwnerHurtTargetGoal(this));
      this.targetSelector.addGoal(3, new TensuraHurtByTargetGoal(this).setAlertOthers(new Class[0]));
      this.targetSelector.addGoal(3, new MimicChestEntity.MimicAttackGoal(this, 2.0, true, 2.0F));
      this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::isAngryAt));
   }

   public Vec3 getDeltaMovement() {
      return this.isAngry() ? super.getDeltaMovement() : Vec3.ZERO;
   }

   public void setDeltaMovement(Vec3 pMotion) {
      if (this.isAngry()) {
         super.setDeltaMovement(pMotion);
      }
   }

   public int getMaxHeadXRot() {
      return this.isAngry() ? super.getMaxHeadXRot() : 180;
   }

   public int getMaxHeadYRot() {
      return this.isAngry() ? super.getMaxHeadYRot() : 180;
   }

   protected float getJumpPower() {
      if (this.horizontalCollision) {
         return 0.6F;
      } else if (this.moveControl.hasWanted() && this.moveControl.getWantedY() > this.getY() + 0.5) {
         return 0.6F;
      } else {
         Path path = this.navigation.getPath();
         if (path != null && !path.isDone()) {
            Vec3 vec3 = path.getNextEntityPos(this);
            if (vec3.y > this.getY() + 0.5) {
               return 0.6F;
            }
         }

         return this.moveControl.getSpeedModifier() <= 0.6 ? 0.3F : 0.4F;
      }
   }

   protected void jumpFromGround() {
      if (!this.isOrderedToSit()) {
         super.jumpFromGround();
         double d0 = this.moveControl.getSpeedModifier();
         if (d0 > 0.0) {
            double d1 = this.getDeltaMovement().horizontalDistanceSqr();
            if (d1 < 0.01) {
               this.moveRelative(0.1F, new Vec3(0.0, 0.0, 1.0));
            }
         }

         if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)1);
         }
      }
   }

   public float getJumpCompletion(float pPartialTick) {
      return this.jumpDuration == 0 ? 0.0F : ((float)this.jumpTicks + pPartialTick) / (float)this.jumpDuration;
   }

   public void setJumping(boolean pJumping) {
      super.setJumping(pJumping);
      if (pJumping) {
         this.playSound(this.getJumpSound(), this.getJumpSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
      }
   }

   public void startJumping() {
      this.setJumping(true);
      this.jumpDuration = 10;
      this.jumpTicks = 0;
   }

   public void customServerAiStep() {
      if (this.getJumpDelay() > 0) {
         this.jumpDelayTicks--;
      }

      if (this.onGround) {
         if (!this.wasOnGround) {
            this.setJumping(false);
            if (this.moveControl.getSpeedModifier() < 2.2) {
               this.jumpDelayTicks = 10;
            } else {
               this.jumpDelayTicks = 1;
            }
         }

         if (this.getJumpDelay() == 0) {
            if (this.setWantedTarget(this, 25.0)) {
               LivingEntity livingentity = this.getTarget();

               assert livingentity != null;

               this.facePoint(livingentity.getX(), livingentity.getZ());
               this.startJumping();
            } else if (this.setWantedOwner(this, 25.0)) {
               LivingEntity owner = this.getOwner();

               assert owner != null;

               this.facePoint(owner.getX(), owner.getZ());
               this.startJumping();
            }
         }
      }

      this.wasOnGround = this.onGround;
   }

   public boolean canSpawnSprintParticle() {
      return false;
   }

   private void facePoint(double pX, double pZ) {
      this.setYRot((float)(Mth.atan2(pZ - this.getZ(), pX - this.getX()) * 180.0F / (float)Math.PI) - 90.0F);
   }

   @Override
   public void aiStep() {
      super.aiStep();
      if (this.jumpTicks != this.jumpDuration) {
         this.jumpTicks++;
      } else if (this.jumpDuration != 0) {
         this.jumpTicks = 0;
         this.jumpDuration = 0;
         this.setJumping(false);
      }
   }

   public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
      if (this.isAngry()) {
         super.lerpTo(pX, pY, pZ, pYaw, pPitch, pPosRotationIncrements, pTeleport);
      } else {
         this.lerpSteps = 0;
         this.setPos(pX, pY, pZ);
         this.setRot(pYaw, pPitch);
      }
   }

   @Override
   public int getJumpDelay() {
      return this.jumpDelayTicks;
   }

   @Override
   public void setJumpAnimation(boolean jump) {
   }

   @Override
   protected boolean shouldDespawnInPeaceful() {
      return false;
   }

   @Override
   public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
      return false;
   }

   public boolean canBeCollidedWith() {
      return this.isAngry() ? super.canBeCollidedWith() : this.isAlive();
   }

   public boolean isInvulnerableTo(DamageSource pSource) {
      if (super.isInvulnerableTo(pSource)) {
         return true;
      } else {
         return pSource.equals(BeyondDamageSources.PARALYSIS) ? true : pSource.isExplosion();
      }
   }

   public boolean canBeAffected(MobEffectInstance pEffectInstance) {
      return pEffectInstance.getEffect().equals(TensuraMobEffects.PARALYSIS.get()) ? false : super.canBeAffected(pEffectInstance);
   }

   public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
      super.recreateFromPacket(pPacket);
      this.yBodyRot = 0.0F;
      this.yBodyRotO = 0.0F;
   }

   @Override
   public SoundEvent getJumpSound() {
      return SoundEvents.CHEST_LOCKED;
   }

   @Override
   public float getJumpSoundVolume() {
      return this.getSoundVolume();
   }

   protected SoundEvent getAmbientSound() {
      return null;
   }

   protected SoundEvent getHurtSound(DamageSource pDamageSource) {
      return SoundEvents.CHEST_OPEN;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.CHEST_CLOSE;
   }

   @Override
   public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
      return null;
   }

   public void handleEntityEvent(byte pId) {
      if (pId == 1) {
         this.spawnSprintParticle();
         this.jumpDuration = 10;
         this.jumpTicks = 0;
      } else {
         super.handleEntityEvent(pId);
      }
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      if (itemstack.getItem() instanceof HealingPotionItem) {
         return super.mobInteract(player, hand);
      } else if (this.level.isClientSide) {
         boolean flag = this.isOwnedBy(player) || this.isTame();
         return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
      } else if (this.isTame()) {
         if (!super.mobInteract(player, hand).consumesAction() && this.isOwnedBy(player) && !this.isBaby()) {
            this.commanding(player);
            return InteractionResult.SUCCESS;
         } else {
            return InteractionResult.PASS;
         }
      } else if (player.isCreative()) {
         return super.mobInteract(player, hand);
      } else {
         this.setTarget(player);

         for (Entity entity : this.level.getEntities(this, this.getBoundingBox().inflate(10.0))) {
            if (entity instanceof MimicChestEntity mimic) {
               mimic.setTarget(player);
            }
         }

         for (int x = -10; x < 10; x++) {
            for (int y = -10; y < 10; y++) {
               for (int z = -10; z < 10; z++) {
                  Block var8 = this.level.getBlockState(this.blockPosition().offset(x, y, z)).getBlock();
                  if (var8 instanceof DungeonChestBlock) {
                     DungeonChestBlock chest = (DungeonChestBlock)var8;
                     if (chest.getType() == DungeonChestBlock.DungeonChestType.MIMIC) {
                        DungeonChestBlock.spawnMimic(
                           this.level, this.blockPosition().offset(x, y, z), player, this.level.getBlockState(this.blockPosition().offset(x, y, z))
                        );
                     }
                  }
               }
            }
         }

         return InteractionResult.SUCCESS;
      }
   }

   public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
      return false;
   }

   @Nullable
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag
   ) {
      this.setYRot(0.0F);
      this.yHeadRot = this.getYRot();
      this.setOldPosAndRot();
      if (!this.getPersistentData().contains("respawn")) {
         this.getPersistentData().putBoolean("respawn", true);
      }

      this.getPersistentData().putDouble("initialPosX", this.getX());
      this.getPersistentData().putDouble("initialPosY", this.getY());
      this.getPersistentData().putDouble("initialPosZ", this.getZ());
      return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
   }

   public void registerControllers(AnimationData animationData) {
      animationData.addAnimationController(new AnimationController(this, "controller", 0.0F, this::predicate));
   }

   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      if (this.isAngry()) {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mimic.hop", EDefaultLoopTypes.LOOP));
      } else {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mimic.hidden", EDefaultLoopTypes.LOOP));
      }

      return PlayState.CONTINUE;
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   public boolean isGolden() {
      return this.golden;
   }

   public static class MimicAttackGoal extends MeleeAttackGoal {
      float range;

      public MimicAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, float additionalRange) {
         super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
         this.range = additionalRange;
      }

      protected double getAttackReachSqr(@NotNull LivingEntity pAttackTarget) {
         return (double)this.range + super.getAttackReachSqr(pAttackTarget);
      }
   }

   class MimicLookControl extends LookControl {
      public MimicLookControl(Mob pMob) {
         super(pMob);
      }

      protected void clampHeadRotationToBody() {
         if (MimicChestEntity.this.isAngry()) {
            super.clampHeadRotationToBody();
         }
      }

      protected Optional<Float> getYRotD() {
         if (MimicChestEntity.this.isAngry()) {
            return super.getYRotD();
         } else {
            Direction direction = Direction.DOWN;
            Vector3f vector3f = (Vector3f)Util.make(() -> {
               Vec3i vec3ix = Direction.SOUTH.getNormal();
               return new Vector3f((float)vec3ix.getX(), (float)vec3ix.getY(), (float)vec3ix.getZ());
            });
            vector3f.transform(direction.getRotation());
            Vec3i vec3i = direction.getNormal();
            Vector3f vector3f1 = new Vector3f((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
            vector3f1.cross(vector3f);
            double d0 = this.wantedX - this.mob.getX();
            double d1 = this.wantedY - this.mob.getEyeY();
            double d2 = this.wantedZ - this.mob.getZ();
            Vector3f vector3f2 = new Vector3f((float)d0, (float)d1, (float)d2);
            float f = vector3f1.dot(vector3f2);
            float f1 = vector3f.dot(vector3f2);
            return !(Math.abs(f) > 1.0E-5F) && !(Math.abs(f1) > 1.0E-5F)
               ? Optional.empty()
               : Optional.of((float)(Mth.atan2((double)(-f), (double)f1) * 180.0F / (float)Math.PI));
         }
      }

      protected Optional<Float> getXRotD() {
         return MimicChestEntity.this.isAngry() ? super.getXRotD() : Optional.of(0.0F);
      }
   }
}
