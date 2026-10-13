package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.capability.SkillStorage;
import com.github.manasmods.tensura.ability.skill.resist.ResistSkill;
import com.github.manasmods.tensura.ability.skill.resist.ResistSkill.ResistType;
import com.github.manasmods.tensura.api.entity.subclass.IGiantMob;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.items.TensuraMaterialItems;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.Memoires.trbeyond.capability.boss.BeyondBossCapability;
import io.github.Memoires.trbeyond.capability.entities.EliteMobCapability;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.network.PlayerVariablesHandler;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.registry.items.BeyondArmorItems;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import io.github.Memoires.trbeyond.world.BeyondGamerules;
import java.util.Optional;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
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

public class TempestSerpentBossEntity extends Monster implements IAnimatable, IGiantMob {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   private final ServerBossEvent bossEvent;
   private static final EntityDataAccessor<Integer> MISC_ANIMATION = SynchedEntityData.defineId(
      TempestSerpentBossEntity.class, EntityDataSerializers.INT
   );
   public int miscAnimationTicks = 0;
   public LivingEntity constrictTarget;

   public TempestSerpentBossEntity(EntityType<? extends TempestSerpentBossEntity> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
      this.setHealth(this.getMaxHealth());
      this.xpReward = 100;
      this.bossEvent = (ServerBossEvent)new ServerBossEvent(
            Component.translatable("entity.trbeyond.tempest_serpent_boss"), BossBarColor.RED, BossBarOverlay.PROGRESS
         )
         .setDarkenScreen(true)
         .setPlayBossMusic(true);
      this.bossEvent.setOverlay(BossBarOverlay.NOTCHED_6);
   }

   public Component getDisplayName() {
      return PlayerTeam.formatNameForTeam(this.getTeam(), Component.translatable("entity.trbeyond.tempest_serpent_boss"))
         .withStyle(p_185975_ -> p_185975_.withHoverEvent(this.createHoverEvent()).withInsertion(this.getStringUUID()));
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(MISC_ANIMATION, 0);
   }

   public void addAdditionalSaveData(@NotNull CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("MiscAnimation", this.getMiscAnimation());
   }

   public void readAdditionalSaveData(@NotNull CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.entityData.set(MISC_ANIMATION, compound.getInt("MiscAnimation"));
   }

   public boolean isInvulnerableTo(@NotNull DamageSource source) {
      return source == TensuraDamageSources.FATAL_POISON
         || source == TensuraDamageSources.CORROSION
         || source == DamageSource.IN_FIRE
         || source == DamageSource.IN_WALL
         || source == DamageSource.FALLING_BLOCK
         || super.isInvulnerableTo(source);
   }

   public boolean causeFallDamage(float pFallDistance, float pMultiplier, @NotNull DamageSource pSource) {
      return false;
   }

   public void setCustomName(@Nullable Component pName) {
      super.setCustomName(pName);
      this.bossEvent.setName(this.getDisplayName());
   }

   public int getMiscAnimation() {
      return (Integer)this.entityData.get(MISC_ANIMATION);
   }

   public void setMiscAnimation(int animation) {
      if (this.getMiscAnimation() == 0 || animation == 0) {
         this.entityData.set(MISC_ANIMATION, animation);
      }
   }

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

   public static AttributeSupplier setAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.JUMP_STRENGTH, 1.5)
         .add(Attributes.MAX_HEALTH, 1000.0)
         .add(Attributes.ATTACK_DAMAGE, 200.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
         .add(Attributes.ARMOR, 100.0)
         .add(Attributes.ARMOR_TOUGHNESS, 25.0)
         .add(Attributes.ATTACK_SPEED, 5.0)
         .add(Attributes.ATTACK_KNOCKBACK, 5.0)
         .add(Attributes.FOLLOW_RANGE, 40.0)
         .add(Attributes.MOVEMENT_SPEED, 0.4F)
         .add((Attribute)TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get(), 2000.0)
         .build();
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(2, new TempestSerpentBossEntity.TempestSerpentBossAttackGoal(this, 1.5, true));
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 40.0F));
      this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.8));
      this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, LivingEntity.class, false));
   }

   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      if (!event.isMoving()) {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tempest_serpent.idle", EDefaultLoopTypes.LOOP));
      } else {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tempest_serpent.walk", EDefaultLoopTypes.LOOP));
      }

      return PlayState.CONTINUE;
   }

   private <E extends IAnimatable> PlayState miscPredicate(AnimationEvent<E> event) {
      if (event.getController().getAnimationState().equals(AnimationState.Stopped)) {
         event.getController().markNeedsReload();
         if (this.getMiscAnimation() == 1) {
            event.getController().setAnimation(new AnimationBuilder().playOnce("animation.tempest_serpent.bite"));
         } else if (this.getMiscAnimation() == 2) {
            event.getController().setAnimation(new AnimationBuilder().playOnce("animation.tempest_serpent.tail_swipe"));
         } else if (this.getMiscAnimation() == 3) {
            event.getController().setAnimation(new AnimationBuilder().playOnce("animation.tempest_serpent.constrict"));
         } else if (this.getMiscAnimation() == 4) {
            event.getController().setAnimation(new AnimationBuilder().playOnce("animation.tempest_serpent.poison_cloud"));
         }
      }

      return PlayState.CONTINUE;
   }

   public void registerControllers(AnimationData animationData) {
      animationData.addAnimationController(new AnimationController(this, "movement", 1.0F, this::predicate));
      animationData.addAnimationController(new AnimationController(this, "attacks", 0.0F, this::miscPredicate));
   }

   public void startSeenByPlayer(@NotNull ServerPlayer pPlayer) {
      super.startSeenByPlayer(pPlayer);
      this.bossEvent.addPlayer(pPlayer);
   }

   public void stopSeenByPlayer(@NotNull ServerPlayer pPlayer) {
      super.stopSeenByPlayer(pPlayer);
      this.bossEvent.removePlayer(pPlayer);
   }

   protected void customServerAiStep() {
      super.customServerAiStep();
      this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
   }

   public void die(@NotNull DamageSource cause) {
      super.die(cause);
      this.bossEvent.removeAllPlayers();
      if (cause.getEntity() instanceof Player player) {
         BeyondBossCapability.getFrom(player).ifPresent(cap -> {
            int killedSerpent = cap.killedSerpent();
            cap.setKilledSerpent(killedSerpent + 1);
         });
         BeyondBossCapability.sync(player);
         player.getCapability(PlayerVariablesHandler.PLAYER_VARIABLES_CAPABILITY, null)
            .ifPresent(
               capability -> {
                  if (!capability.KilledSerpent) {
                     int coinsRemaining = (Integer)ServerConfig.INSTANCE.bossConfig.tempestSerpentGoldDrop.get();
                     Item coinItem = (Item)TensuraMaterialItems.GOLD_COIN.get();
                     int maxStackSize = new ItemStack(coinItem).getMaxStackSize();

                     for (int i = 0; i < 36 && coinsRemaining > 0; i++) {
                        ItemStack slotItem = player.getInventory().getItem(i);
                        if (!slotItem.isEmpty() && slotItem.is(coinItem) && slotItem.getCount() < maxStackSize) {
                           int spaceLeft = maxStackSize - slotItem.getCount();
                           int amountToAdd = Math.min(spaceLeft, coinsRemaining);
                           slotItem.grow(amountToAdd);
                           player.getInventory().setItem(i, slotItem);
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

                     if (coinsRemaining > 0) {
                        while (coinsRemaining > 0) {
                           int dropAmount = Math.min(coinsRemaining, maxStackSize);
                           ItemStack dropStack = new ItemStack(coinItem, dropAmount);
                           this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), dropStack));
                           coinsRemaining -= dropAmount;
                        }
                     }

                     capability.KilledSerpent = true;
                     capability.syncPlayerVariables(player);
                  }

                  ItemStack[] uniqueDrops = new ItemStack[]{
                     new ItemStack((ItemLike)BeyondArmorItems.SERPENT_HELMET_UNIQUE.get()),
                     new ItemStack((ItemLike)BeyondArmorItems.SERPENT_CHESTPLATE_UNIQUE.get()),
                     new ItemStack((ItemLike)BeyondArmorItems.SERPENT_LEGGINGS_UNIQUE.get()),
                     new ItemStack((ItemLike)BeyondArmorItems.SERPENT_BOOTS_UNIQUE.get())
                  };
                  EliteMobCapability.getFrom(this).ifPresent(cap -> {
                     if (this.random.nextFloat() < 0.05F || cap.isElite()) {
                        ItemStack drop = uniqueDrops[this.random.nextInt(uniqueDrops.length)];
                        if (!player.getInventory().add(drop)) {
                           this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), drop));
                        }
                     }
                  });
               }
            );
      } else if (cause.getEntity() instanceof LivingEntity attacker && attacker instanceof TamableAnimal tame && tame.getOwner() instanceof Player player) {
         player.getCapability(PlayerVariablesHandler.PLAYER_VARIABLES_CAPABILITY, null)
            .ifPresent(
               capability -> {
                  if (!capability.KilledSerpent) {
                     int coinsRemaining = 20;
                     Item coinItem = (Item)TensuraMaterialItems.GOLD_COIN.get();
                     int maxStackSize = new ItemStack(coinItem).getMaxStackSize();

                     for (int i = 0; i < 36 && coinsRemaining > 0; i++) {
                        ItemStack slotItem = player.getInventory().getItem(i);
                        if (!slotItem.isEmpty() && slotItem.is(coinItem) && slotItem.getCount() < maxStackSize) {
                           int spaceLeft = maxStackSize - slotItem.getCount();
                           int amountToAdd = Math.min(spaceLeft, coinsRemaining);
                           slotItem.grow(amountToAdd);
                           player.getInventory().setItem(i, slotItem);
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

                     if (coinsRemaining > 0) {
                        while (coinsRemaining > 0) {
                           int dropAmount = Math.min(coinsRemaining, maxStackSize);
                           ItemStack dropStack = new ItemStack(coinItem, dropAmount);
                           this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), dropStack));
                           coinsRemaining -= dropAmount;
                        }
                     }

                     capability.KilledSerpent = true;
                     capability.syncPlayerVariables(player);
                  }

                  ItemStack[] uniqueDrops = new ItemStack[]{
                     new ItemStack((ItemLike)BeyondArmorItems.SERPENT_HELMET_UNIQUE.get()),
                     new ItemStack((ItemLike)BeyondArmorItems.SERPENT_CHESTPLATE_UNIQUE.get()),
                     new ItemStack((ItemLike)BeyondArmorItems.SERPENT_LEGGINGS_UNIQUE.get()),
                     new ItemStack((ItemLike)BeyondArmorItems.SERPENT_BOOTS_UNIQUE.get())
                  };
                  EliteMobCapability.getFrom(this).ifPresent(cap -> {
                     if (this.random.nextFloat() < 0.05F || cap.isElite()) {
                        ItemStack drop = uniqueDrops[this.random.nextInt(uniqueDrops.length)];
                        if (!player.getInventory().add(drop)) {
                           this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), drop));
                        }
                     }
                  });
               }
            );
      }
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockIn) {
      this.playSound((SoundEvent)BeyondSoundEvents.tempest_serpent_movement.get(), 0.25F, 0.5F);
   }

   @NotNull
   protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
      return SoundEvents.GENERIC_HURT;
   }

   @NotNull
   protected SoundEvent getDeathSound() {
      return SoundEvents.GENERIC_DEATH;
   }

   protected float getSoundVolume() {
      return 0.8F;
   }

   public boolean shouldAttack(LivingEntity target) {
      return target instanceof Player && ((Player)target).isCreative();
   }

   public boolean canBeCollidedWith() {
      return false;
   }

   public void tick() {
      super.tick();
      LivingEntity target = this.getTarget();
      if (this.getMiscAnimation() != 0) {
         this.miscAnimationTicks++;
         if (this.isAlive() && target != null && target.isAlive()
            || this.getMiscAnimation() == 3 && this.constrictTarget != null && this.constrictTarget.isAlive()) {
            if (this.getMiscAnimation() == 1) {
               if (this.miscAnimationTicks == 15 && target != null && (double)this.distanceTo(target) < 4.0) {
                  this.lookAt(Anchor.EYES, target.position());
                  this.level
                     .playSound(
                        null,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        (SoundEvent)BeyondSoundEvents.tempest_serpent_bite.get(),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
                  target.hurt(DamageSource.mobAttack(this), (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.5F);
               }
            } else if (this.getMiscAnimation() == 2) {
               if (target != null && this.miscAnimationTicks == 13) {
                  this.lookAt(Anchor.EYES, target.position());
                  this.level
                     .playSound(
                        null,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        (SoundEvent)BeyondSoundEvents.tempest_serpent_tail_whip.get(),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );

                  for (Entity entity : this.level.getEntities(this, this.getBoundingBox().inflate(3.0, 1.0, 3.0))) {
                     if (entity instanceof LivingEntity ent) {
                        entity.hurt(DamageSource.mobAttack(this), (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                        ent.knockback(1.0, entity.getX() - this.getX(), entity.getZ() - this.getZ());
                     }
                  }
               }
            } else if (this.getMiscAnimation() == 3) {
               if (this.miscAnimationTicks == 1) {
                  if ((double)this.distanceTo(target) < 6.0) {
                     this.constrictTarget = target;
                  }

                  this.level
                     .playSound(
                        null,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        (SoundEvent)BeyondSoundEvents.tempest_serpent_constrict.get(),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
               }

               if (this.constrictTarget != null && this.constrictTarget.isAlive()) {
                  if (this.miscAnimationTicks == 2 && this.constrictTarget != null && (double)this.distanceTo(this.constrictTarget) < 8.0) {
                     this.constrictTarget.teleportTo(this.getX(), this.getY(), this.getZ());
                  }

                  if (this.miscAnimationTicks % 6 == 0 && this.constrictTarget != null && (double)this.distanceTo(this.constrictTarget) < 6.0) {
                     this.constrictTarget.teleportTo(this.getX(), this.getY(), this.getZ());
                     this.constrictTarget.setDeltaMovement(0.0, this.constrictTarget.getDeltaMovement().y, 0.0);
                     if (this.miscAnimationTicks % 24 == 0) {
                        this.constrictTarget.hurt(DamageSource.mobAttack(this), (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                        this.constrictTarget.setAirSupply(0);
                     }
                  }
               }

               if (this.miscAnimationTicks >= 60 && (this.constrictTarget == null || !this.constrictTarget.isAlive())) {
                  this.miscAnimationTicks = 96;
               }
            } else if (this.getMiscAnimation() == 4) {
               if (this.miscAnimationTicks == 1) {
                  this.level
                     .playSound(
                        null,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        (SoundEvent)BeyondSoundEvents.tempest_serpent_poison_cloud.get(),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
               }

               if (this.miscAnimationTicks == 22) {
                  Vec3 endPos = target.position();
                  this.lookAt(Anchor.EYES, target.position());
                  Vec3 cloudPos = endPos.add(new Random().nextGaussian() * 0.5, new Random().nextGaussian() * 0.5 + 0.5, new Random().nextGaussian() * 0.5);
                  AreaEffectCloud cloud = new AreaEffectCloud(this.level, cloudPos.x, cloudPos.y, cloudPos.z);
                  cloud.setRadius(2.0F);
                  cloud.setWaitTime(10);
                  cloud.setDuration(200);
                  cloud.setRadiusPerTick(0.02F);
                  cloud.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.FATAL_POISON.get(), 100, 9));
                  cloud.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.CORROSION.get(), 100, 9));
                  cloud.setParticle((ParticleOptions)TensuraParticles.POISON_EFFECT.get());
                  this.level.addFreshEntity(cloud);
               }
            }
         }

         if (this.miscAnimationTicks >= this.getAnimationTick(this.getMiscAnimation())) {
            this.setMiscAnimation(0);
            this.miscAnimationTicks = 0;
         }

         if (this.miscAnimationTicks % 40 == 0) {
         }
      }
   }

   private int getAnimationTick(int miscAnimation) {
      return switch (miscAnimation) {
         case 1 -> 55;
         case 2 -> 56;
         case 3 -> 96;
         case 4 -> 120;
         default -> 12;
      };
   }

   public boolean doHurtTarget(@NotNull Entity pEntity) {
      boolean flag = super.doHurtTarget(pEntity);
      if (flag && this.getMiscAnimation() == 0) {
         this.setMiscAnimation(1);
      }

      return flag;
   }

   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag
   ) {
      SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
      this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.tempestSerpentJumpStrength.get());
      this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.tempestSerpentMaxHealth.get());
      this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.tempestSerpentAttackDamage.get());
      this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.tempestSerpentKnockbackResistance.get());
      this.getAttribute(Attributes.ARMOR).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.tempestSerpentArmor.get());
      this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.tempestSerpentArmorToughness.get());
      this.getAttribute(Attributes.ATTACK_SPEED).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.tempestSerpentAttackSpeed.get());
      this.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.tempestSerpentAttackKnockback.get());
      this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.tempestSerpentFollowRange.get());
      this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.tempestSerpentMovementSpeed.get());
      this.getAttribute((Attribute)TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get())
         .setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.tempestSerpentMaxSpiritualHealth.get());
      this.setHealth(this.getMaxHealth());
      AttributeInstance spiritualHP = this.getAttribute((Attribute)TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get());
      AttributeInstance maxHP = this.getAttribute(Attributes.MAX_HEALTH);
      double sHP = maxHP.getValue() * 2.0;
      if (spiritualHP != null) {
         spiritualHP.setBaseValue(sHP);
         TensuraEPCapability.setSpiritualHealth(this, sHP);
      }

      this.getPersistentData().putBoolean("isBoss", true);
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

      return data;
   }

   class TempestSerpentBossAttackGoal extends MeleeAttackGoal {
      private final TempestSerpentBossEntity boss;

      public TempestSerpentBossAttackGoal(TempestSerpentBossEntity boss, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
         super(boss, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
         this.boss = boss;
      }

      public void tick() {
         if (this.boss.getMiscAnimation() == 0) {
            super.tick();
         }
      }

      protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy, double pDistToEnemySqr) {
         double reach = super.getAttackReachSqr(pEnemy);
         if (this.boss.getMiscAnimation() == 0 && pEnemy.isAlive()) {
            int randomAttack = this.randomAttack(pDistToEnemySqr);
            if (randomAttack == 4) {
               reach = 25.0;
               this.boss.getNavigation().stop();
            }

            if (pDistToEnemySqr <= reach && this.isTimeToAttack()) {
               this.resetAttackCooldown();
               this.boss.setMiscAnimation(randomAttack);
            }
         }
      }

      protected int randomAttack(double distance) {
         return this.boss.random.nextInt(100) < 5 ? 4 : (this.boss.random.nextInt(100) < 2 ? 3 : (this.boss.random.nextInt(100) < 20 ? 2 : 1));
      }
   }
}
