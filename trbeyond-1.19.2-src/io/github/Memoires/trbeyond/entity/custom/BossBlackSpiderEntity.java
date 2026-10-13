package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.capability.SkillStorage;
import com.github.manasmods.tensura.ability.skill.resist.ResistSkill;
import com.github.manasmods.tensura.ability.skill.resist.ResistSkill.ResistType;
import com.github.manasmods.tensura.api.entity.ai.LeapWithStrengthGoal;
import com.github.manasmods.tensura.api.entity.ai.WanderingFollowOwnerGoal;
import com.github.manasmods.tensura.api.entity.subclass.IGiantMob;
import com.github.manasmods.tensura.block.SpiderEggBlock;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.data.TensuraTags.Blocks;
import com.github.manasmods.tensura.entity.BlackSpiderEntity;
import com.github.manasmods.tensura.entity.template.ClimbingEntity;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraHurtByTargetGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraOwnerHurtByTargetGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraOwnerHurtTargetGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.WanderAroundPosGoal;
import com.github.manasmods.tensura.item.food.HealingPotionItem;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import com.github.manasmods.tensura.registry.items.TensuraMaterialItems;
import com.github.manasmods.tensura.registry.items.TensuraToolItems;
import com.github.manasmods.tensura.registry.sound.TensuraSoundEvents;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.Memoires.trbeyond.capability.entities.EliteMobCapability;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.entity.projectile.BossWebBulletProjectile;
import io.github.Memoires.trbeyond.network.PlayerVariablesHandler;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlocks;
import io.github.Memoires.trbeyond.registry.items.BeyondToolItems;
import io.github.Memoires.trbeyond.world.BeyondGamerules;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
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

public class BossBlackSpiderEntity extends ClimbingEntity implements IAnimatable, IGiantMob {
   private final ServerBossEvent bossEvent;
   private static final EntityDataAccessor<Integer> MISC_ANIMATION = SynchedEntityData.defineId(BossBlackSpiderEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> EGGS = SynchedEntityData.defineId(BossBlackSpiderEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> STRIPED = SynchedEntityData.defineId(BossBlackSpiderEntity.class, EntityDataSerializers.BOOLEAN);
   public int miscAnimationTicks = 0;
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   private final HitboxPartEntity[] subEntities;

   public BossBlackSpiderEntity(EntityType<? extends BossBlackSpiderEntity> type, Level level) {
      super(type, level);
      this.maxUpStep = 3.0F;
      this.xpReward = 250;
      this.moveControl = new BossBlackSpiderEntity.BossBlackSpiderMoveControl(this);
      this.lookControl = new BossBlackSpiderEntity.BossBlackSpiderLookControl(this);
      this.bossEvent = new ServerBossEvent(this.getDisplayName(), BossBarColor.WHITE, BossBarOverlay.NOTCHED_20);
      this.subEntities = new HitboxPartEntity[]{
         new HitboxPartEntity(this, 2.0F, 1.5F, new Vec3(2.0, 1.75, 0.0)),
         new HitboxPartEntity(this, 2.5F, 3.0F, new Vec3(-1.0, 0.0, 3.5)),
         new HitboxPartEntity(this, 2.5F, 3.0F, new Vec3(1.0, 0.0, 3.5)),
         new HitboxPartEntity(this, 2.5F, 3.0F, new Vec3(-1.0, 0.0, -3.5)),
         new HitboxPartEntity(this, 2.5F, 3.0F, new Vec3(1.0, 0.0, -3.5)),
         new HitboxPartEntity(this, 3.0F, 3.5F, new Vec3(-3.75, 1.5, 0.0))
      };

      for (int i = 0; i < this.subEntities.length; i++) {
         this.subEntities[i].setId(this.getId() + i + 1);
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

   public Component getDisplayName() {
      return PlayerTeam.formatNameForTeam(this.getTeam(), Component.translatable("entity.trbeyond.boss_black_spider"))
         .withStyle(p_185975_ -> p_185975_.withHoverEvent(this.createHoverEvent()).withInsertion(this.getStringUUID()));
   }

   public static AttributeSupplier setAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.ARMOR, 10.0)
         .add(Attributes.MAX_HEALTH, 250.0)
         .add(Attributes.FOLLOW_RANGE, 32.0)
         .add(Attributes.ATTACK_DAMAGE, 25.0)
         .add(Attributes.MOVEMENT_SPEED, 0.25)
         .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
         .add(Attributes.JUMP_STRENGTH, 1.5)
         .add((Attribute)ForgeMod.SWIM_SPEED.get(), 3.0)
         .build();
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
      this.goalSelector.addGoal(2, new BossBlackSpiderEntity.SpiderLayEggsGoal(this, 1.0));
      this.goalSelector.addGoal(3, new WanderingFollowOwnerGoal(this, 1.2, 20.0F, 5.0F, false));
      this.goalSelector.addGoal(4, new BossBlackSpiderEntity.BossBlackSpiderLeapGoal(this));
      this.goalSelector.addGoal(5, new WanderAroundPosGoal(this));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 10.0F));
      this.targetSelector.addGoal(1, new TensuraOwnerHurtByTargetGoal(this));
      this.targetSelector.addGoal(2, new TensuraOwnerHurtTargetGoal(this));
      this.targetSelector.addGoal(3, new TensuraHurtByTargetGoal(this).setAlertOthers(new Class[0]));
      this.targetSelector.addGoal(2, new BossBlackSpiderEntity.BossBlackSpiderAttackGoal(this, 1.2, true));
      this.targetSelector.addGoal(4, new NonTameRandomTargetGoal(this, Player.class, false, null));
      this.targetSelector.addGoal(4, new NonTameRandomTargetGoal(this, Villager.class, false, null));
      this.targetSelector.addGoal(5, new NonTameRandomTargetGoal(this, Animal.class, false, entity -> !(entity instanceof BossBlackSpiderEntity)));
      this.targetSelector.addGoal(5, new NonTameRandomTargetGoal(this, IronGolem.class, false, null));
      this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal(this, true));
   }

   @Override
   public boolean isAlliedTo(Entity entity) {
      return super.isAlliedTo(entity) ? true : entity instanceof BlackSpiderEntity;
   }

   public void setCustomName(@Nullable Component pName) {
      super.setCustomName(pName);
      this.bossEvent.setName(this.getDisplayName());
   }

   public void startSeenByPlayer(ServerPlayer pPlayer) {
      super.startSeenByPlayer(pPlayer);
      if (!this.isTame()) {
         this.bossEvent.addPlayer(pPlayer);
      }
   }

   public void stopSeenByPlayer(ServerPlayer pPlayer) {
      super.stopSeenByPlayer(pPlayer);
      this.bossEvent.removePlayer(pPlayer);
   }

   protected void customServerAiStep() {
      super.customServerAiStep();
      this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(MISC_ANIMATION, 0);
      this.entityData.define(EGGS, 0);
      this.entityData.define(STRIPED, Boolean.FALSE);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("MiscAnimation", this.getMiscAnimation());
      compound.putInt("Eggs", this.getEggs());
      compound.putBoolean("Striped", this.isStriped());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.entityData.set(MISC_ANIMATION, compound.getInt("MiscAnimation"));
      this.setEggs(compound.getInt("Eggs"));
      this.setStriped(compound.getBoolean("Striped"));
   }

   public int getMiscAnimation() {
      return (Integer)this.entityData.get(MISC_ANIMATION);
   }

   public void setMiscAnimation(int animation) {
      this.entityData.set(MISC_ANIMATION, animation);
   }

   public int getEggs() {
      return (Integer)this.entityData.get(EGGS);
   }

   public void setEggs(int egg) {
      this.entityData.set(EGGS, egg);
   }

   public boolean isStriped() {
      return (Boolean)this.entityData.get(STRIPED);
   }

   public void setStriped(boolean striped) {
      this.entityData.set(STRIPED, striped);
   }

   public boolean canFallInLove() {
      return super.canFallInLove() && this.getEggs() <= 0;
   }

   public boolean canMate(Animal pOtherAnimal) {
      return !this.isTame() ? false : super.canMate(pOtherAnimal);
   }

   public boolean canBeAffected(MobEffectInstance pEffectInstance) {
      if (pEffectInstance.getEffect().equals(TensuraMobEffects.WEBBED.get())) {
         return false;
      } else if (pEffectInstance.getEffect().equals(TensuraMobEffects.FATAL_POISON.get())) {
         return false;
      } else {
         return pEffectInstance.getEffect().equals(MobEffects.POISON) ? false : super.canBeAffected(pEffectInstance);
      }
   }

   public boolean doHurtTarget(Entity pEntity) {
      boolean flag = super.doHurtTarget(pEntity);
      if (flag && this.getMiscAnimation() == 0) {
         this.setMiscAnimation(1);
         if (pEntity instanceof LivingEntity target) {
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 4));
         }
      }

      return flag;
   }

   public int getMaxSpawnClusterSize() {
      return 1;
   }

   @Override
   public void checkDespawn() {
   }

   @Override
   public boolean cantBreakBlock(LivingEntity entity, boolean ignoreCollision) {
      Level level = entity.getLevel();
      if (level.dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
         return false;
      } else {
         return !ForgeEventFactory.getMobGriefingEvent(entity.level, entity) ? true : true;
      }
   }

   @Override
   public EntityDimensions getDimensions(Pose pPose) {
      EntityDimensions entitydimensions = super.getDimensions(pPose);
      return this.isBaby() ? entitydimensions.scale(0.4F, 0.4F) : entitydimensions;
   }

   public void push(Entity pEntity) {
      if (!(pEntity instanceof BossBlackSpiderEntity)) {
         super.push(pEntity);
      }
   }

   @Override
   public void tick() {
      super.tick();
      this.updatePartPositions();
      this.targetingMovementHelper();
      if (this.getMiscAnimation() != 0) {
         this.miscAnimationTicks++;
         if (!this.isAlive()) {
            return;
         }

         if (this.getMiscAnimation() == 3 && this.miscAnimationTicks == 25) {
            this.areaAttack();
            TensuraParticleHelper.spawnGroundSlamParticle(this, 5, 5.0F);
            TensuraParticleHelper.spawnGroundSlamParticle(this, 5, 4.0F);
            TensuraParticleHelper.spawnGroundSlamParticle(this, 5, 3.0F);
            TensuraParticleHelper.spawnGroundSlamParticle(this, 5, 2.0F);
            this.getLevel().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1.0F, 1.0F);
         } else if (this.getMiscAnimation() == 4 && this.miscAnimationTicks == 15) {
            LivingEntity target = this.getTarget();
            if (target != null) {
               this.performRangedAttack(target, this.isBaby() ? -0.3 : -1.5, this.isBaby() ? 1.0 : 5.0, false);
               this.getLevel().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LLAMA_SPIT, SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
         } else if (this.getMiscAnimation() == 6 && this.miscAnimationTicks == 15) {
            LivingEntity target = this.getTarget();
            if (target != null) {
               this.performRangedAttack(target, this.isBaby() ? -0.3 : -1.5, this.isBaby() ? 1.0 : 5.0, true);
               this.getLevel().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LLAMA_SPIT, SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
         } else if (this.getMiscAnimation() == -2) {
            this.getNavigation().stop();
         }

         if (this.miscAnimationTicks >= this.getAnimationTick(this.getMiscAnimation())) {
            this.setMiscAnimation(0);
            this.miscAnimationTicks = 0;
         }
      }
   }

   private int getAnimationTick(int miscAnimation) {
      return switch (miscAnimation) {
         case -2 -> 100;
         case -1, 4, 6 -> 25;
         default -> 5;
         case 2 -> 27;
         case 3, 5 -> 30;
      };
   }

   public void areaAttack() {
      AABB aabb = this.getBoundingBox().inflate(5.0);
      List<LivingEntity> livingEntityList = this.level.getEntitiesOfClass(LivingEntity.class, aabb, entity -> !this.isAlliedTo(entity) && !entity.equals(this));
      if (!livingEntityList.isEmpty()) {
         for (LivingEntity target : livingEntityList) {
            double damageMultiplier = 2.0;
            target.hurt(
               DamageSourceHelper.addSkillAndCost(DamageSource.mobAttack(this).setMagic(), 20.0), (float)(this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damageMultiplier)
            );
            target.getDeltaMovement().add(0.0, 0.5 * damageMultiplier, 0.0);
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 4));
         }
      }
   }

   private void performRangedAttack(@NotNull LivingEntity target, double xzOffset, double yOffset, boolean slinger) {
      BossWebBulletProjectile spit = new BossWebBulletProjectile(this.level, this, true, ((Item)TensuraToolItems.STICKY_STEEL_WEB_CARTRIDGE.get()).getDefaultInstance());
      spit.setSlinger(slinger);
      float angle = (float) (Math.PI / 180.0) * this.yBodyRot;
      double xOffset = xzOffset * (double)Mth.sin((float)(Math.PI + (double)angle));
      double zOffset = xzOffset * (double)Mth.cos(angle);
      spit.moveTo(this.getX() + xOffset, this.getY() + yOffset, this.getZ() + zOffset, this.getYRot(), this.getXRot());
      double d0 = target.getY() + (double)(target.getBbHeight() / 2.0F);
      double d1 = target.getX() - this.getX();
      double d2 = d0 - spit.getY();
      double d3 = target.getZ() - this.getZ();
      double f = Math.sqrt(d1 * d1 + d3 * d3) * 0.2F;
      spit.shoot(d1, d2 + f, d3, 2.0F, 0.0F);
      this.level.addFreshEntity(spit);
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      if (itemstack.getItem() instanceof HealingPotionItem) {
         return super.mobInteract(player, hand);
      } else {
         InteractionResult eating = this.handleEating(player, hand, itemstack);
         if (eating.consumesAction()) {
            return eating;
         } else if (!this.level.isClientSide) {
            if (this.isTame() && this.isOwnedBy(player)) {
               this.commanding(player);
               return InteractionResult.SUCCESS;
            } else {
               return super.mobInteract(player, hand);
            }
         } else {
            boolean flag = this.isOwnedBy(player) || this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
         }
      }
   }

   @Override
   public InteractionResult handleEating(Player pPlayer, InteractionHand hand, ItemStack itemstack) {
      if (this.isFood(itemstack)) {
         if (this.getHealth() < this.getMaxHealth()) {
            if (!pPlayer.isCreative()) {
               itemstack.shrink(1);
            }

            this.ate();
            this.getLevel().playSound(null, this, (SoundEvent)TensuraSoundEvents.EATING.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
         }

         int i = this.getAge();
         if (!this.level.isClientSide() && i == 0 && this.canFallInLove()) {
            this.usePlayerItem(pPlayer, hand, itemstack);
            this.setInLove(pPlayer);
            return InteractionResult.SUCCESS;
         }

         if (this.isBaby()) {
            this.usePlayerItem(pPlayer, hand, itemstack);
            this.ate();
            this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
         }
      }

      return InteractionResult.PASS;
   }

   public void ate() {
      super.ate();
      this.setMiscAnimation(1);
      this.heal(5.0F);
   }

   public boolean isFood(ItemStack pStack) {
      return !(pStack.getItem() instanceof HealingPotionItem) && pStack.isEdible();
   }

   public boolean rideableUnderWater() {
      return true;
   }

   public boolean isInvulnerableTo(DamageSource source) {
      return source == DamageSource.IN_WALL
         || source == DamageSource.CACTUS
         || source == DamageSource.SWEET_BERRY_BUSH
         || source == DamageSource.HOT_FLOOR
         || source == TensuraDamageSources.STEEL_THREAD
         || super.isInvulnerableTo(source);
   }

   public void makeStuckInBlock(BlockState pState, Vec3 pMotionMultiplier) {
      if (!pState.is(Blocks.WEB_BLOCKS)) {
         super.makeStuckInBlock(pState, pMotionMultiplier);
      }
   }

   protected float getBlockSpeedFactor() {
      BlockState blockstate = this.level.getBlockState(this.blockPosition());
      return blockstate.is(Blocks.WEB_BLOCKS) ? 1.0F : super.getBlockSpeedFactor();
   }

   protected float getBlockJumpFactor() {
      BlockState blockstate = this.level.getBlockState(this.blockPosition());
      return blockstate.is(Blocks.WEB_BLOCKS) ? 1.0F : super.getBlockJumpFactor();
   }

   @Override
   protected float getClimbSpeedMultiplier() {
      return this.isBaby() ? 1.5F : 3.0F;
   }

   public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
      return false;
   }

   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag
   ) {
      if (this.random.nextBoolean()) {
         this.setStriped(true);
      }

      this.getAttribute(Attributes.ARMOR).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.blackSpiderArmor.get());
      this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.blackSpiderMaxHealth.get());
      this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.blackSpiderFollowRange.get());
      this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.blackSpiderAttackDamage.get());
      this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.blackSpiderMovementSpeed.get());
      this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.blackSpiderKnockbackResistance.get());
      this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.blackSpiderJumpStrength.get());
      this.getAttribute((Attribute)ForgeMod.SWIM_SPEED.get()).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.blackSpiderSwimSpeed.get());
      this.setHealth(this.getMaxHealth());
      AttributeInstance spiritualHP = this.getAttribute((Attribute)TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get());
      AttributeInstance maxHP = this.getAttribute(Attributes.MAX_HEALTH);
      double sHP = maxHP.getValue() * 2.0;
      if (spiritualHP != null) {
         spiritualHP.setBaseValue(sHP);
         TensuraEPCapability.setSpiritualHealth(this, sHP);
      }

      if (this.level.getGameRules().getBoolean(BeyondGamerules.NIGHTMARE_MODE)) {
         SkillStorage storage = SkillAPI.getSkillsFrom(this);

         for (ManasSkill manasSkill : SkillAPI.getSkillRegistry()) {
            if (manasSkill instanceof ResistSkill) {
               ResistSkill resist = (ResistSkill)manasSkill;
               if (resist.getResistType() == ResistType.RESISTANCE) {
                  Optional<ManasSkillInstance> existing = storage.getSkill(manasSkill);
                  if (existing.isEmpty()) {
                     ManasSkillInstance instance = manasSkill.createDefaultInstance();
                     storage.learnSkill(instance);
                  }
               }
            }
         }

         for (ManasSkillInstance instance : storage.getLearnedSkills()) {
            ManasSkill skill = instance.getSkill();
            if (skill instanceof ResistSkill) {
               ResistSkill resist = (ResistSkill)skill;
               if (resist.getResistType() == ResistType.RESISTANCE) {
                  instance.setMastery(0);
                  instance.setToggled(true);
                  storage.updateSkill(instance);
               }
            }
         }
      }

      return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
   }

   public void die(DamageSource pDamageSource) {
      super.die(pDamageSource);
      if (!this.getLevel().isClientSide() && !this.isBaby()) {
         int additionalAmount = this.getRandom().nextInt(6, 9);
         int eggAmount = this.getEggs() + additionalAmount;
         if (eggAmount > 0) {
            for (int i = 0; i < eggAmount; i++) {
               BlackSpiderEntity spider = (BlackSpiderEntity)((EntityType)TensuraEntityTypes.BLACK_SPIDER.get()).create(this.getLevel());
               if (spider != null) {
                  spider.setAge(-24000);
                  spider.moveTo(this.getX() + 0.3, this.getY(), this.getZ() + 0.3);
                  spider.setDeltaMovement((double)(this.random.nextFloat() - 0.5F), (double)this.random.nextFloat(), (double)(this.random.nextFloat() - 0.5F));
                  spider.hurtMarked = true;
                  this.getLevel().addFreshEntity(spider);
               }
            }
         }
      }

      if (pDamageSource.getEntity() instanceof Player player) {
         player.getCapability(PlayerVariablesHandler.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            if (!capability.KilledSpider) {
               int coinsRemaining = (Integer)ServerConfig.INSTANCE.bossConfig.blackSpiderGoldDrop.get();
               Item coinItem = (Item)TensuraMaterialItems.GOLD_COIN.get();
               int maxStackSize = new ItemStack(coinItem).getMaxStackSize();

               for (int ix = 0; ix < 36 && coinsRemaining > 0; ix++) {
                  ItemStack slotItem = player.getInventory().getItem(ix);
                  if (!slotItem.isEmpty() && slotItem.is(coinItem) && slotItem.getCount() < maxStackSize) {
                     int spaceLeft = maxStackSize - slotItem.getCount();
                     int amountToAdd = Math.min(spaceLeft, coinsRemaining);
                     slotItem.grow(amountToAdd);
                     player.getInventory().setItem(ix, slotItem);
                     coinsRemaining -= amountToAdd;
                  }
               }

               if (coinsRemaining > 0) {
                  for (int ix = 0; ix < 36 && coinsRemaining > 0; ix++) {
                     ItemStack slotItem = player.getInventory().getItem(ix);
                     if (slotItem.isEmpty()) {
                        int amountToAdd = Math.min(maxStackSize, coinsRemaining);
                        ItemStack newCoinStack = new ItemStack(coinItem, amountToAdd);
                        player.getInventory().setItem(ix, newCoinStack);
                        coinsRemaining -= amountToAdd;
                     }
                  }
               }

               capability.KilledSpider = true;
               capability.syncPlayerVariables(player);
            }

            ItemStack[] uniqueDrops = new ItemStack[]{new ItemStack((ItemLike)BeyondToolItems.SPIDER_DAGGER_SPECIAL.get())};
            EliteMobCapability.getFrom(this).ifPresent(cap -> {
               if (this.random.nextFloat() < 0.05F || cap.isElite()) {
                  ItemStack drop = uniqueDrops[this.random.nextInt(uniqueDrops.length)];
                  if (!player.getInventory().add(drop)) {
                     this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), drop));
                  }
               }
            });
         });
      } else if (pDamageSource.getEntity() instanceof LivingEntity attacker && attacker instanceof TamableAnimal tame && tame.getOwner() instanceof Player player
         )
       {
         player.getCapability(PlayerVariablesHandler.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            if (!capability.KilledSpider) {
               int coinsRemaining = 3;
               Item coinItem = (Item)TensuraMaterialItems.GOLD_COIN.get();
               int maxStackSize = new ItemStack(coinItem).getMaxStackSize();

               for (int ix = 0; ix < 36 && coinsRemaining > 0; ix++) {
                  ItemStack slotItem = player.getInventory().getItem(ix);
                  if (!slotItem.isEmpty() && slotItem.is(coinItem) && slotItem.getCount() < maxStackSize) {
                     int spaceLeft = maxStackSize - slotItem.getCount();
                     int amountToAdd = Math.min(spaceLeft, coinsRemaining);
                     slotItem.grow(amountToAdd);
                     player.getInventory().setItem(ix, slotItem);
                     coinsRemaining -= amountToAdd;
                  }
               }

               if (coinsRemaining > 0) {
                  for (int ix = 0; ix < 36 && coinsRemaining > 0; ix++) {
                     ItemStack slotItem = player.getInventory().getItem(ix);
                     if (slotItem.isEmpty()) {
                        int amountToAdd = Math.min(maxStackSize, coinsRemaining);
                        ItemStack newCoinStack = new ItemStack(coinItem, amountToAdd);
                        player.getInventory().setItem(ix, newCoinStack);
                        coinsRemaining -= amountToAdd;
                     }
                  }
               }

               capability.KilledSpider = true;
               capability.syncPlayerVariables(player);
            }

            ItemStack[] uniqueDrops = new ItemStack[]{new ItemStack((ItemLike)BeyondToolItems.SPIDER_DAGGER_SPECIAL.get())};
            EliteMobCapability.getFrom(this).ifPresent(cap -> {
               if (this.random.nextFloat() < 0.05F || cap.isElite()) {
                  ItemStack drop = uniqueDrops[this.random.nextInt(uniqueDrops.length)];
                  if (!player.getInventory().add(drop)) {
                     this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), drop));
                  }
               }
            });
         });
      }
   }

   protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
   }

   protected SoundEvent getAmbientSound() {
      return SoundEvents.SPIDER_AMBIENT;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SoundEvents.SPIDER_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.SPIDER_DEATH;
   }

   @NotNull
   public SoundSource getSoundSource() {
      return SoundSource.HOSTILE;
   }

   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      if (this.getMiscAnimation() == -2) {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.black_spider.player_dragging", EDefaultLoopTypes.LOOP));
         return PlayState.CONTINUE;
      } else if (this.getMiscAnimation() != 0 && this.getMiscAnimation() != 1) {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.black_spider.still", EDefaultLoopTypes.LOOP));
         return PlayState.CONTINUE;
      } else {
         if (this.isInSittingPose()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.black_spider.stay", EDefaultLoopTypes.LOOP));
         } else if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.black_spider.walk", EDefaultLoopTypes.LOOP));
         } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.black_spider.idle", EDefaultLoopTypes.LOOP));
         }

         return PlayState.CONTINUE;
      }
   }

   private <E extends IAnimatable> PlayState miscPredicate(AnimationEvent<E> event) {
      if (event.getController().getAnimationState().equals(AnimationState.Stopped)) {
         event.getController().markNeedsReload();
         if (this.getMiscAnimation() == 1) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.black_spider.bite", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 2) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.black_spider.leap", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 3) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.black_spider.slam", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 4) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.black_spider.web_shoot", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 5) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.black_spider.lay_eggs", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 6) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.black_spider.web_grab", EDefaultLoopTypes.PLAY_ONCE));
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

   static class BossBlackSpiderAttackGoal extends MeleeAttackGoal {
      private final BossBlackSpiderEntity spider;

      public BossBlackSpiderAttackGoal(BossBlackSpiderEntity spider, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
         super(spider, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
         this.spider = spider;
      }

      public boolean canUse() {
         return !this.spider.isOrderedToSit() && super.canUse();
      }

      public boolean canContinueToUse() {
         return !this.spider.isOrderedToSit() && super.canContinueToUse();
      }

      public void tick() {
         if (this.spider.getMiscAnimation() == 0) {
            super.tick();
         }
      }

      protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
         double distance = this.getAttackReachSqr(pEnemy);
         if (this.spider.getMiscAnimation() == 0) {
            int randomAttack = this.randomAttack(distance);

            double attackRange = switch (randomAttack) {
               case 3 -> {
                  this.spider.getNavigation().stop();
                  yield 25.0;
               }
               case 4, 6 -> 400.0;
               default -> distance;
            };
            if (pDistToEnemySqr <= attackRange && this.isTimeToAttack()) {
               this.resetAttackCooldown();
               this.spider.setMiscAnimation(randomAttack);
               if (randomAttack == 1) {
                  this.spider.doHurtTarget(pEnemy);
               }
            }
         }
      }

      protected int randomAttack(double distance) {
         if (this.spider.random.nextInt(5) == 2 && this.spider.getControllingPassenger() == null && !this.spider.isBaby()) {
            return 3;
         } else {
            if (distance >= (this.spider.isBaby() ? 1.6 : 8.0)
               && (this.spider.getTarget() == null || !this.spider.getTarget().hasEffect((MobEffect)TensuraMobEffects.WEBBED.get()))) {
               if ((double)this.spider.random.nextFloat() <= 0.4) {
                  return 6;
               }

               if ((double)this.spider.random.nextFloat() <= 0.4) {
                  return 4;
               }
            }

            return 1;
         }
      }

      protected double getAttackReachSqr(LivingEntity pAttackTarget) {
         return (double)(this.mob.getBbWidth() * this.mob.getBbWidth() * 3.0F + pAttackTarget.getBbWidth());
      }
   }

   static class BossBlackSpiderLeapGoal extends LeapWithStrengthGoal {
      private final BossBlackSpiderEntity spider;

      public BossBlackSpiderLeapGoal(BossBlackSpiderEntity spider) {
         super(spider, 0.7F, 2.0F, 10.0, 20.0, 40);
         this.spider = spider;
      }

      @Override
      public boolean canUse() {
         return this.spider.getMiscAnimation() != 0 ? false : super.canUse();
      }

      @Override
      public void start() {
         super.start();
         this.spider.setMiscAnimation(2);
      }

      @Override
      public boolean canContinueToUse() {
         return this.spider.getMiscAnimation() != 0 ? false : !this.spider.isOnGround();
      }
   }

   public static class BossBlackSpiderLookControl extends LookControl {
      private final BossBlackSpiderEntity entity;

      public BossBlackSpiderLookControl(BossBlackSpiderEntity this$0) {
         super(this$0);
         this.entity = this$0;
      }

      public void tick() {
         if (!this.entity.isSleeping()) {
            if (this.entity.getMiscAnimation() != 3 && this.entity.getMiscAnimation() != 5) {
               super.tick();
            }
         }
      }
   }

   public static class BossBlackSpiderMoveControl extends MoveControl {
      private final BossBlackSpiderEntity entity;

      public BossBlackSpiderMoveControl(BossBlackSpiderEntity this$0) {
         super(this$0);
         this.entity = this$0;
      }

      public void tick() {
         if (!this.entity.isSleeping()) {
            if (this.entity.getMiscAnimation() != 3 && this.entity.getMiscAnimation() != -2 && this.entity.getMiscAnimation() != 5) {
               super.tick();
            }
         }
      }
   }

   static class SpiderLayEggsGoal extends MoveToBlockGoal {
      private final BossBlackSpiderEntity spider;
      private int coolDown = 200;

      SpiderLayEggsGoal(BossBlackSpiderEntity spider, double pSpeedModifier) {
         super(spider, pSpeedModifier, 20);
         this.spider = spider;
      }

      public boolean canUse() {
         if (super.canUse() && !this.spider.isOrderedToSit()) {
            if (this.spider.getMiscAnimation() == -2) {
               return false;
            } else {
               LivingEntity target = this.spider.getTarget();
               if (target == null) {
                  return true;
               } else {
                  return target.distanceTo(this.spider) < 3.0F ? false : target.hasEffect((MobEffect)TensuraMobEffects.WEBBED.get());
               }
            }
         } else {
            return false;
         }
      }

      public void start() {
         super.start();
         this.coolDown = 0;
      }

      public void tick() {
         this.coolDown--;
         if (this.coolDown <= 0) {
            super.tick();
            BlockPos blockpos = this.spider.blockPosition();
            Level level = this.spider.level;
            if (this.isReachedTarget()) {
               level.playSound(null, blockpos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);
               level.levelEvent(2001, blockpos, Block.getId(level.getBlockState(blockpos.below())));
               if (this.spider.getMiscAnimation() == 5) {
                  this.spider.navigation.stop();
                  if (this.spider.miscAnimationTicks == 15) {
                     level.setBlock(this.blockPos.above(), ((Block)BeyondBlocks.BOSS_SPIDER_EGG.get()).defaultBlockState(), 3);
                     level.scheduleTick(blockpos, (Block)BeyondBlocks.BOSS_SPIDER_EGG.get(), 600);
                     this.coolDown = 200;
                     this.spider.setMiscAnimation(0);
                  }
               } else {
                  this.spider.setMiscAnimation(5);
               }
            }
         }
      }

      protected BlockPos getMoveToTarget() {
         BlockPos newPos = new BlockPos((double)this.blockPos.getX() + 0.5, (double)this.blockPos.getY(), (double)this.blockPos.getZ() + 0.5);
         return newPos.above();
      }

      protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
         return pLevel.isEmptyBlock(pPos.above()) && SpiderEggBlock.canLayEgg(pLevel, pPos);
      }
   }
}
