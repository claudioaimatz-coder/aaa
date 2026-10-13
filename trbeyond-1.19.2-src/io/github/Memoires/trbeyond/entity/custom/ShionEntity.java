package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.ability.skill.unique.CookSkill;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.entity.magic.projectile.AuraBulletProjectile;
import com.github.manasmods.tensura.entity.magic.projectile.AuraSlashProjectile;
import com.github.manasmods.tensura.entity.magic.projectile.AuraBulletProjectile.AuraColor;
import com.github.manasmods.tensura.entity.template.HumanoidNPCEntity;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import com.mojang.logging.LogUtils;
import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
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

public class ShionEntity extends HumanoidNPCEntity implements IAnimatable {
   public static final AnimationBuilder IDLE_1 = new AnimationBuilder().addAnimation("animation.shion.idle_1", EDefaultLoopTypes.LOOP);
   public static final AnimationBuilder IDLE_2 = new AnimationBuilder().addAnimation("animation.shion.idle_2", EDefaultLoopTypes.LOOP);
   public static final AnimationBuilder IDLE_4 = new AnimationBuilder().addAnimation("animation.shion.idle_4", EDefaultLoopTypes.LOOP);
   public static final AnimationBuilder SLEEP = new AnimationBuilder().addAnimation("animation.shion.sleep", EDefaultLoopTypes.LOOP);
   public static final AnimationBuilder RUN = new AnimationBuilder().addAnimation("animation.shion.run", EDefaultLoopTypes.LOOP);
   public static final AnimationBuilder WALK = new AnimationBuilder().addAnimation("animation.shion.walk", EDefaultLoopTypes.LOOP);
   public static final AnimationBuilder JUMP_KICK = new AnimationBuilder().addAnimation("animation.shion.jump_kick", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder SWORD_REFLECTION = new AnimationBuilder().addAnimation("animation.shion.sword_reflection", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder SHEATH = new AnimationBuilder().addAnimation("animation.shion.sheath", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder UNSHEATH = new AnimationBuilder().addAnimation("animation.shion.unsheath", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder SPIN_PUNCH = new AnimationBuilder().addAnimation("animation.shion.spin_punch", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder MAGIC_BULLET_PUSH = new AnimationBuilder()
      .addAnimation("animation.shion.magic_bullet_push", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder OGRE_SWORD_CANNON = new AnimationBuilder()
      .addAnimation("animation.shion.ogre_sword_cannon", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder QUICK_PUNCHES = new AnimationBuilder().addAnimation("animation.shion.quick_punches", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder DASH_SWORD_STRIKE = new AnimationBuilder()
      .addAnimation("animation.shion.dash_sword_strike", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder DOUBLE_SWORD_STRIKE = new AnimationBuilder()
      .addAnimation("animation.shion.double_sword_strike", EDefaultLoopTypes.PLAY_ONCE);
   public static final AnimationBuilder JUMP_SWORD_SMASH = new AnimationBuilder().addAnimation("animation.shion.jump_sword_smash", EDefaultLoopTypes.PLAY_ONCE);
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   public static final EntityDataAccessor<Integer> MISC_ANIMATION = SynchedEntityData.defineId(ShionEntity.class, EntityDataSerializers.INT);
   public static final EntityDataAccessor<Integer> COOK_TICKS = SynchedEntityData.defineId(ShionEntity.class, EntityDataSerializers.INT);
   public static final EntityDataAccessor<Boolean> SWORD_DRAWN = SynchedEntityData.defineId(ShionEntity.class, EntityDataSerializers.BOOLEAN);
   public int miscAnimationTicks = 0;
   public int deflectCooldown = 0;
   public int cookCooldown = 0;
   public boolean swordDrawn = false;

   public ShionEntity(EntityType<? extends ShionEntity> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(MISC_ANIMATION, 0);
      this.entityData.define(COOK_TICKS, 0);
      this.entityData.define(SWORD_DRAWN, false);
   }

   @Override
   public void tick() {
      super.tick();
      this.miscAnimationHandler();
      this.deflectCooldown--;
      this.cookCooldown--;
      if (this.swordDrawn && this.deflectCooldown <= 0 && (this.getMiscAnimation() == 0 || this.getMiscAnimation() == 11)) {
         AABB projectileDetection = this.getBoundingBox().inflate(5.0);

         for (Projectile projectile : this.level.getEntitiesOfClass(Projectile.class, projectileDetection)) {
            if (projectile.getOwner() != this) {
               projectile.setNoGravity(true);
               projectile.setDeltaMovement(0.0, 0.0, 0.0);
               if (this.getMiscAnimation() == 0) {
                  this.setMiscAnimation(11);
               }
            }
         }
      }
   }

   @Override
   public void addAdditionalSaveData(@NotNull CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("MiscAnimation", this.getMiscAnimation());
      compound.putInt("CookTicks", this.cookCooldown);
      compound.putBoolean("SwordDrawn", this.swordDrawn);
   }

   @Override
   public void readAdditionalSaveData(@NotNull CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.getEntityData().set(MISC_ANIMATION, compound.getInt("MiscAnimation"));
      this.cookCooldown = compound.getInt("CookTicks");
      this.swordDrawn = compound.getBoolean("SwordDrawn");
   }

   public static AttributeSupplier setAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 28000.0)
         .add(Attributes.ARMOR, 1400.0)
         .add(Attributes.ATTACK_DAMAGE, 1750.0)
         .add(Attributes.MOVEMENT_SPEED, 0.25)
         .add(Attributes.FOLLOW_RANGE, 64.0)
         .build();
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.7));
      this.goalSelector.addGoal(1, new ShionEntity.ShionAttackGoal(this));
      this.goalSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
   }

   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController(this, "controller", 0.0F, this::predicate));
      data.addAnimationController(new AnimationController(this, "attackcontroller", 0.0F, this::miscPredicate));
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      if (this.getMiscAnimation() == 0) {
         if (this.isSleeping()) {
            event.getController().setAnimation(SLEEP);
         } else if (event.isMoving()) {
            if (this.isAngry() && this.swordDrawn) {
               event.getController().setAnimation(RUN);
            } else {
               event.getController().setAnimation(WALK);
            }
         } else if (!event.isMoving()
            && event.getController().getCurrentAnimation() != null
            && event.getController().getCurrentAnimation().loop == EDefaultLoopTypes.LOOP) {
            if (this.random.nextInt(3000) != 1 || this.isAngry()) {
               event.getController().clearAnimationCache();
               return PlayState.STOP;
            }

            switch (this.random.nextIntBetweenInclusive(1, 3)) {
               case 1:
                  event.getController().setAnimation(IDLE_1);
                  break;
               case 2:
                  event.getController().setAnimation(IDLE_2);
                  break;
               case 3:
                  event.getController().setAnimation(IDLE_4);
            }
         }
      }

      return PlayState.CONTINUE;
   }

   protected void miscAnimationHandler() {
      if (this.getMiscAnimation() != 0) {
         this.miscAnimationTicks++;
         if (!this.isAlive()) {
            return;
         }

         if (!this.getLevel().isClientSide()) {
            switch (this.getMiscAnimation()) {
               case 3:
                  if (this.miscAnimationTicks == 8) {
                     LivingEntity target = this.getTarget();
                     if (target != null) {
                        this.lookAt(Anchor.EYES, target.getEyePosition());
                        if (this.distanceToSqr(target) <= 64.0 && this.hasLineOfSight(target)) {
                           this.doHurtTarget(target, 0.2F, 10.0F);
                        }
                     }
                  }
                  break;
               case 4:
                  if (this.miscAnimationTicks >= 21 && this.miscAnimationTicks <= 69) {
                     LivingEntity target = this.getTarget();
                     if (target != null) {
                        this.lookAt(Anchor.EYES, target.getEyePosition());
                        if (this.distanceToSqr(target) <= 64.0 && this.hasLineOfSight(target) && target.getLastHurtByMob() == this) {
                           target.invulnerableTime = 0;
                           this.doHurtTarget(target, 0.1F, 0.25F);
                        }
                     }
                  }
                  break;
               case 5:
                  if (this.miscAnimationTicks == 8) {
                     LivingEntity target = this.getTarget();
                     if (target != null) {
                        Vec3 jump = target.position().subtract(this.position()).normalize().multiply(2.0, 0.0, 2.0).add(0.0, 0.2, 0.0);
                        this.setDeltaMovement(jump);
                        this.setJumping(true);
                     }
                  } else if (this.miscAnimationTicks == 15) {
                     LivingEntity target = this.getTarget();
                     if (target != null) {
                        this.lookAt(Anchor.EYES, target.getEyePosition());
                        if (this.distanceToSqr(target) <= 64.0 && this.hasLineOfSight(target)) {
                           this.doHurtTarget(target, 0.25F, 10.0F);
                        }
                     }
                  }
                  break;
               case 6:
                  if (this.miscAnimationTicks == 8) {
                     LivingEntity target = this.getTarget();
                     if (target != null) {
                        this.lookAt(Anchor.EYES, target.getEyePosition());
                        magicBullet(this);
                     }
                  }
                  break;
               case 7:
                  if (this.miscAnimationTicks != 20 && this.miscAnimationTicks != 28) {
                     break;
                  }

                  LivingEntity target = this.getTarget();
                  if (target != null) {
                     this.lookAt(Anchor.EYES, target.getEyePosition());
                     if (this.distanceToSqr(target) <= 64.0 && this.hasLineOfSight(target)) {
                        if (target.invulnerableTime <= 12 && target.getLastHurtByMob() == this) {
                           target.invulnerableTime = 0;
                        }

                        this.doHurtTarget(target, 1.0F, 1.0F);
                     }
                  }
                  break;
               case 8:
                  if (this.miscAnimationTicks == 28) {
                     LivingEntity target = this.getTarget();
                     if (target != null) {
                        Vec3 jump = target.position().subtract(this.position()).normalize().multiply(3.0, 0.0, 3.0).add(0.0, 0.3, 0.0);
                        this.setDeltaMovement(jump);
                        this.setJumping(true);
                     }
                  }

                  if (this.miscAnimationTicks >= 28 && this.miscAnimationTicks <= 40) {
                     LivingEntity target = this.getTarget();
                     if (target != null) {
                        this.lookAt(Anchor.EYES, target.getEyePosition());
                        if (this.distanceToSqr(target) <= 64.0 && this.hasLineOfSight(target)) {
                           this.doHurtTarget(target, 1.25F, 1.0F);
                        }
                     }
                  }
                  break;
               case 9:
                  if (this.miscAnimationTicks == 8) {
                     LivingEntity target = this.getTarget();
                     if (target != null) {
                        Vec3 jump = target.position().subtract(this.position()).normalize().multiply(2.0, 0.0, 2.0).add(0.0, 0.8, 0.0);
                        this.setDeltaMovement(jump);
                        this.setJumping(true);
                     }
                  } else if (this.miscAnimationTicks == 26) {
                     LivingEntity target = this.getTarget();
                     if (target != null) {
                        this.lookAt(Anchor.EYES, target.getEyePosition());
                        if (this.distanceToSqr(target) <= 64.0 && this.hasLineOfSight(target)) {
                           this.doHurtTarget(target, 1.5F, 3.0F);
                        }
                     }
                  }
                  break;
               case 10:
                  if (this.miscAnimationTicks == 45) {
                     LivingEntity target = this.getTarget();
                     if (target != null) {
                        this.lookAt(Anchor.EYES, target.getEyePosition());
                        ogreSwordCannon(this);
                     }
                  }
                  break;
               case 11:
                  if (this.miscAnimationTicks == 13) {
                     AABB projectileDetection = this.getBoundingBox().inflate(6.0);

                     for (Projectile projectile : this.level.getEntitiesOfClass(Projectile.class, projectileDetection)) {
                        if (projectile.getOwner() != this) {
                           if (projectile.getOwner() != null && projectile.getOwner().getLevel() == this.getLevel()) {
                              projectile.setDeltaMovement(projectile.getOwner().getPosition(0.0F).subtract(this.getPosition(0.0F)).normalize().scale(10.0));
                           } else {
                              projectile.setDeltaMovement(projectile.getPosition(0.0F).subtract(this.getPosition(0.0F)).normalize().scale(10.0));
                              projectile.setNoGravity(false);
                           }
                        }
                     }
                  }
            }
         }

         if (this.miscAnimationTicks > this.getAnimationTick(this.getMiscAnimation())) {
            switch (this.getMiscAnimation()) {
               case 1:
                  this.swordDrawn = false;
                  this.entityData.set(SWORD_DRAWN, false);
                  break;
               case 2:
                  this.swordDrawn = true;
                  this.entityData.set(SWORD_DRAWN, true);
                  break;
               case 11:
                  this.deflectCooldown = 200;
            }

            this.setMiscAnimation(0);
            this.miscAnimationTicks = 0;
         }
      }
   }

   private int getAnimationTick(int miscAnimation) {
      return switch (miscAnimation) {
         case 1 -> 20;
         case 2 -> 20;
         case 3 -> 30;
         case 4 -> 100;
         case 5 -> 33;
         case 6 -> 20;
         case 7 -> 60;
         case 8 -> 50;
         case 9 -> 60;
         case 10 -> 100;
         case 11 -> 30;
         default -> 100;
      };
   }

   @Override
   public boolean canSleep() {
      return !this.isNoAi() && !this.isAngry();
   }

   public boolean doHurtTarget(@NotNull Entity pEntity, float damageMultiplier, float knockbackMultiplier) {
      float damage = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damageMultiplier;
      float knockback = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK) * knockbackMultiplier;
      boolean flag = pEntity.hurt(DamageSource.mobAttack(this), damage);
      if (flag) {
         if (knockback > 0.0F && pEntity instanceof LivingEntity living) {
            living.knockback(
               (double)(knockback * 0.5F),
               (double)Mth.sin(this.getYRot() * (float) (Math.PI / 180.0)),
               (double)(-Mth.cos(this.getYRot() * (float) (Math.PI / 180.0)))
            );
            ItemStack itemStack = this.inventory.getItem(4);
            itemStack.getItem().hurtEnemy(itemStack, living, this);
         }

         this.doEnchantDamageEffects(this, pEntity);
         this.setLastHurtMob(pEntity);
      }

      return true;
   }

   @Override
   public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
      if (this.getHealth() < this.getMaxHealth() * 0.5F
         && !this.hasEffect((MobEffect)TensuraMobEffects.OGRE_BERSERKER.get())
         && !this.hasEffect((MobEffect)TensuraMobEffects.FRAGILITY.get())) {
         this.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.OGRE_BERSERKER.get(), 7200, 3, false, false, false));
         this.playSound(SoundEvents.RAVAGER_ROAR);
         TensuraParticleHelper.addServerParticlesAroundSelf(this, ParticleTypes.FLASH, 3.0);
         TensuraParticleHelper.addServerParticlesAroundSelf(this, ParticleTypes.FLASH, 2.0);
         TensuraParticleHelper.spawnServerParticles(
            this.getLevel(),
            (ParticleOptions)TensuraParticles.PURPLE_LIGHTNING_SPARK.get(),
            this.getX(),
            this.getY(),
            this.getZ(),
            55,
            0.08,
            0.08,
            0.08,
            0.5,
            true
         );
      }

      if (this.getHealth() < this.getMaxHealth() * 0.25F) {
         this.playSound(SoundEvents.GUARDIAN_DEATH);
      }

      return super.hurt(pSource, pAmount);
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
      if (event.getController().getAnimationState().equals(AnimationState.Stopped)) {
         event.getController().markNeedsReload();
         switch (this.getMiscAnimation()) {
            case 1:
               event.getController().setAnimation(SHEATH);
               break;
            case 2:
               event.getController().setAnimation(UNSHEATH);
               break;
            case 3:
               event.getController().setAnimation(SPIN_PUNCH);
               break;
            case 4:
               event.getController().setAnimation(QUICK_PUNCHES);
               break;
            case 5:
               event.getController().setAnimation(JUMP_KICK);
               break;
            case 6:
               event.getController().setAnimation(MAGIC_BULLET_PUSH);
               break;
            case 7:
               event.getController().setAnimation(DOUBLE_SWORD_STRIKE);
               break;
            case 8:
               event.getController().setAnimation(DASH_SWORD_STRIKE);
               break;
            case 9:
               event.getController().setAnimation(JUMP_SWORD_SMASH);
               break;
            case 10:
               event.getController().setAnimation(OGRE_SWORD_CANNON);
               break;
            case 11:
               event.getController().setAnimation(SWORD_REFLECTION);
         }
      }

      return PlayState.CONTINUE;
   }

   public int getMiscAnimation() {
      return (Integer)this.getEntityData().get(MISC_ANIMATION);
   }

   @Override
   public void setMiscAnimation(int animation) {
      if (this.getMiscAnimation() == 0 || animation == 0) {
         this.getEntityData().set(MISC_ANIMATION, animation);
      }
   }

   public void cookEffects(LivingEntity entity, double damage) {
      if (TensuraEPCapability.getEP(entity) < TensuraEPCapability.getEP(this) * 2.0) {
         AttributeInstance barrier = entity.getAttribute((Attribute)TensuraAttributeRegistry.BARRIER.get());
         if (barrier != null) {
            barrier.removeModifiers();
         }
      }

      if (this.cookCooldown < 0) {
         AttributeInstance health = entity.getAttribute(Attributes.MAX_HEALTH);
         if (health != null) {
            AttributeModifier chefModifier = health.getModifier(CookSkill.COOK);
            if (chefModifier != null) {
               damage -= chefModifier.getAmount();
            }

            AttributeModifier attributeModifier = new AttributeModifier(CookSkill.COOK, "Cook", damage * -1.0, Operation.ADDITION);
            health.removeModifier(attributeModifier);
            health.addPermanentModifier(attributeModifier);
            this.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE, 1.0F, 1.0F);
         }

         this.cookCooldown = 200;
      }
   }

   public static void magicBullet(LivingEntity entity) {
      AuraBulletProjectile bullet = new AuraBulletProjectile(entity.getLevel(), entity);
      float angle = (float) (Math.PI / 180.0) * entity.yBodyRot;
      double xOffset = (double)Mth.sin((float)(Math.PI + (double)angle));
      double zOffset = (double)Mth.cos(angle);
      bullet.moveTo(entity.getX() + xOffset, entity.getEyeY() - 0.5, entity.getZ() + zOffset, entity.getYRot(), entity.getXRot());
      bullet.setDamage((float)entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.5F);
      bullet.setSpeed(5.0F);
      bullet.setNoGravity(true);
      bullet.shootFromRot(entity.getLookAngle());
      bullet.setColor(AuraColor.PURPLE);
      bullet.setSize(2.0F);
      entity.level.addFreshEntity(bullet);
      entity.playSound(SoundEvents.TRIDENT_THROW);
   }

   public static void ogreSwordCannon(LivingEntity entity) {
      AuraSlashProjectile slash = new AuraSlashProjectile(entity.getLevel(), entity);
      slash.setSpeed(0.5F);
      slash.setDamage((float)entity.getAttributeValue(Attributes.ATTACK_DAMAGE));
      slash.setLife(100);
      slash.setSize(5.0F);
      slash.setNoGravity(true);
      slash.shootFromRot(entity.getLookAngle());
      entity.level.addFreshEntity(slash);
      entity.playSound(SoundEvents.WARDEN_ATTACK_IMPACT);
   }

   static class ShionAttackGoal extends Goal {
      private static final Logger logger = LogUtils.getLogger();
      private final ShionEntity shion;

      public ShionAttackGoal(ShionEntity entity) {
         this.setFlags(EnumSet.of(Flag.MOVE));
         this.shion = entity;
      }

      public boolean canUse() {
         LivingEntity target = this.shion.getTarget();
         if (target instanceof Player player && player.isCreative()) {
            this.shion.forgetCurrentTargetAndRefreshUniversalAnger();
            return false;
         }

         if (target != null && target.isAlive()) {
            return true;
         } else {
            if (this.shion.swordDrawn && this.shion.getMiscAnimation() == 0) {
               this.shion.setMiscAnimation(1);
            }

            return false;
         }
      }

      public boolean canContinueToUse() {
         return this.canUse();
      }

      public void start() {
         this.shion.setAggressive(true);
      }

      public void tick() {
         if (this.shion.getMiscAnimation() == 0) {
            LivingEntity target = this.shion.getTarget();
            if (target == null) {
               return;
            }

            double targetEP = TensuraEPCapability.getEP(target);
            double currentEP = TensuraEPCapability.getEP(this.shion);
            double targetDist = this.shion.distanceToSqr(target);
            if (targetEP > currentEP * 10.0 && !this.shion.swordDrawn) {
               this.shion.setMiscAnimation(2);
            }

            if (this.shion.swordDrawn) {
               if (targetDist > 100.0 && targetDist < 400.0 && this.shion.random.nextIntBetweenInclusive(1, 100) < 20) {
                  this.shion.setMiscAnimation(9);
               } else if (targetDist < 25.0) {
                  this.shion.getNavigation().moveTo(target, 1.0);
                  this.shion.setMiscAnimation(7);
               } else if (targetDist > 64.0 && targetDist < 225.0 && this.shion.random.nextBoolean()) {
                  this.shion.getNavigation().stop();
                  this.shion.setMiscAnimation(8);
               } else if (targetDist > 64.0 && targetDist < 225.0 && this.shion.random.nextInt(20) < 18) {
                  this.shion.getNavigation().stop();
                  this.shion.setMiscAnimation(9);
               } else if (targetDist > 100.0 && targetDist < 625.0) {
                  this.shion.getNavigation().stop();
                  this.shion.setMiscAnimation(10);
               } else {
                  Path path = this.shion.getNavigation().getPath();
                  if (path == null || 9.0 < path.getTarget().distToCenterSqr(target.position())) {
                     this.shion.getNavigation().moveTo(target, this.shion.getAttributes().getValue(Attributes.MOVEMENT_SPEED) * 9.0);
                  }
               }
            } else if (targetDist > 225.0 && this.shion.random.nextInt(20) < 2) {
               this.shion.getNavigation().stop();
               this.shion.setMiscAnimation(6);
            } else if (targetDist > 64.0 && targetDist < 144.0 && this.shion.random.nextBoolean()) {
               this.shion.getNavigation().stop();
               this.shion.setMiscAnimation(5);
            } else if (targetDist < 25.0 && this.shion.random.nextInt(20) < 15) {
               this.shion.getNavigation().stop();
               this.shion.setMiscAnimation(3);
            } else if (targetDist < 25.0) {
               this.shion.setMiscAnimation(4);
            } else {
               Path path = this.shion.getNavigation().getPath();
               if (path == null || 9.0 < path.getTarget().distToCenterSqr(target.position())) {
                  this.shion.getNavigation().moveTo(target, this.shion.getAttributes().getValue(Attributes.MOVEMENT_SPEED) * 9.0);
               }
            }
         }
      }
   }
}
