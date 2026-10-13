package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.api.entity.ai.WanderingFollowOwnerGoal;
import com.github.manasmods.tensura.entity.SlimeEntity;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraHurtByTargetGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraOwnerHurtByTargetGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraOwnerHurtTargetGoal;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.Memoires.trbeyond.items.armor.SerpentArmorItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class SlimeWallEntity extends TensuraTamableEntity implements IAnimatable {
   private boolean trappingTarget = false;
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

   public SlimeWallEntity(EntityType<? extends SlimeWallEntity> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
      this.xpReward = 1000;
      this.maxUpStep = 2.0F;
   }

   public static AttributeSupplier setAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.ATTACK_DAMAGE, 10.0)
         .add(Attributes.MAX_HEALTH, 100.0)
         .add(Attributes.MOVEMENT_SPEED, 0.35F)
         .add(Attributes.FOLLOW_RANGE, 32.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
         .build();
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(2, new SlimeWallEntity.WallMeleeAttackGoal(this, 1.0, false));
      this.targetSelector.addGoal(2, new TensuraHurtByTargetGoal(this).setAlertOthers(new Class[0]));
      this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, Player.class, false));
      this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
      this.goalSelector.addGoal(1, new WanderingFollowOwnerGoal(this, 0.2, 20.0F, 5.0F, false));
      this.targetSelector.addGoal(1, new TensuraOwnerHurtByTargetGoal(this));
      this.targetSelector.addGoal(2, new TensuraOwnerHurtTargetGoal(this));
   }

   public int getMaxHeadXRot() {
      return 180;
   }

   public int getMaxHeadYRot() {
      return 180;
   }

   @Override
   public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
      return false;
   }

   @Override
   public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
      return null;
   }

   public boolean isInvulnerableTo(DamageSource source) {
      return source == DamageSource.CACTUS || source == DamageSource.HOT_FLOOR || source == DamageSource.SWEET_BERRY_BUSH || super.isInvulnerableTo(source);
   }

   public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
      return false;
   }

   public boolean isPushable() {
      return this.isAlive();
   }

   public boolean rideableUnderWater() {
      return true;
   }

   public boolean shouldRiderSit() {
      return false;
   }

   public static boolean isSlime(Entity entity) {
      return entity instanceof SlimeEntity || entity instanceof SlimeWallEntity || entity instanceof Slime;
   }

   public void playerTouch(Player pEntity) {
      if (!this.isOwnedBy(pEntity)) {
         if (this.isEffectiveAi()) {
            this.trapping(pEntity);
            this.damageCollidedEntity(pEntity);
            if (this.getControllingPassenger() instanceof Player && !pEntity.equals(this.getControllingPassenger())) {
               pEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 30, 1, false, false, false), this);
               pEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 30, 1, false, false, false), this);
               pEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 30, 0, false, false, false), this);
            }

            if (!pEntity.isCreative() && !pEntity.isSpectator() && (double)this.distanceTo(pEntity) < 3.5) {
               pEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2, false, false, false), this);
               if (pEntity.equals(this.getTarget())) {
                  this.trappingTarget = true;
               }
            }
         }
      }
   }

   public void push(Entity pEntity) {
      super.push(pEntity);
      if (this.isEffectiveAi()) {
         if (!isSlime(pEntity) && pEntity.getDeltaMovement().y() < 0.0) {
            pEntity.resetFallDistance();
            pEntity.setDeltaMovement(pEntity.getDeltaMovement().multiply(1.0, 0.5, 1.0));
         }

         if (!isSlime(pEntity) || this.getTarget() == pEntity) {
            if (!this.isAlliedTo(pEntity)) {
               if (pEntity instanceof LivingEntity living && this.isOwnedBy(living)) {
                  return;
               }

               this.trapping(pEntity);
               this.damageCollidedEntity(pEntity);
            }
         }
      }
   }

   protected void trapping(Entity entity) {
      if (!this.trappingTarget) {
         this.trappingTarget = true;
         if (!(this.distanceTo(entity) > 8.0F)) {
            entity.startRiding(this, true);
         }
      }
   }

   protected void damageCollidedEntity(Entity entity) {
      float damage = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
      this.dealDamage(entity, damage, entity == this.getTarget());
      if (entity instanceof LivingEntity living) {
         for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack slotStack = living.getItemBySlot(slot);
            if (!(slotStack.getItem() instanceof SerpentArmorItem)) {
               slotStack.hurtAndBreak(3, living, player -> player.broadcastBreakEvent(slot));
            }
         }
      }
   }

   protected boolean dealDamage(Entity entity, float damage, boolean aggro) {
      if (damage < 0.0F) {
         return false;
      } else if (!this.isAlive()) {
         return false;
      } else {
         DamageSource damageSource = TensuraDamageSources.corrosion(this);
         if (!aggro) {
            damageSource = damageSource.setNoAggro();
         }

         if (entity.getVehicle() == this) {
            damage *= 2.0F;
         }

         if (this.distanceTo(entity) < this.getBbWidth() / 2.0F + 1.0F && entity.hurt(damageSource, damage)) {
            this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.doEnchantDamageEffects(this, entity);
            return true;
         } else {
            return false;
         }
      }
   }

   public void positionRider(Entity passenger) {
      if (this.hasPassenger(passenger)) {
         passenger.resetFallDistance();
         float radius = -0.25F;
         float angle = (float) (Math.PI / 180.0) * this.yBodyRot;
         double extraX = (double)(radius * Mth.sin((float)(Math.PI + (double)angle)));
         double extraZ = (double)(radius * Mth.cos(angle));
         double yOffset = this.getY() + 1.5 + this.getPassengersRidingOffset() + passenger.getMyRidingOffset();
         if (!(passenger instanceof LivingEntity living) || !this.isOwnedBy(living)) {
            yOffset -= 3.0;
         }

         passenger.setPos(this.getX() + extraX, yOffset, this.getZ() + extraZ);
      }
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.level.isClientSide) {
         if (this.isAlive()) {
            this.targetingMovementHelper();
            if (this.trappingTarget && (this.getTarget() == null || this.getTarget() != null && this.distanceToSqr(this.getTarget()) > 3.0)) {
               this.trappingTarget = false;
            }
         }
      }
   }

   protected SoundEvent getAmbientSound() {
      return SoundEvents.SLIME_SQUISH;
   }

   protected SoundEvent getHurtSound(DamageSource pDamageSource) {
      return SoundEvents.SLIME_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.SLIME_DEATH;
   }

   public SoundSource getSoundSource() {
      return SoundSource.HOSTILE;
   }

   public void registerControllers(AnimationData animationData) {
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   public class WallMeleeAttackGoal extends MeleeAttackGoal {
      public WallMeleeAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
         super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
      }

      protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
      }
   }
}
