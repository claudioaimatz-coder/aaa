package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.capability.SkillStorage;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.skill.resist.ResistSkill;
import com.github.manasmods.tensura.ability.skill.resist.ResistSkill.ResistType;
import com.github.manasmods.tensura.api.entity.subclass.IGiantMob;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.entity.magic.barrier.BarrierEntity;
import com.github.manasmods.tensura.entity.magic.barrier.FlareCircleEntity;
import com.github.manasmods.tensura.network.TensuraNetwork;
import com.github.manasmods.tensura.network.play2client.RequestFxSpawningPacket;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import com.github.manasmods.tensura.registry.items.TensuraMaterialItems;
import com.github.manasmods.tensura.registry.magic.SpiritualMagics;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import com.github.manasmods.tensura.registry.skill.ResistanceSkills;
import io.github.Memoires.trbeyond.capability.boss.BeyondBossCapability;
import io.github.Memoires.trbeyond.capability.entities.EliteMobCapability;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.network.PlayerVariablesHandler;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import io.github.Memoires.trbeyond.registry.items.BeyondArmorItems;
import io.github.Memoires.trbeyond.registry.items.BeyondToolItems;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import io.github.Memoires.trbeyond.world.BeyondGamerules;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.SpawnPlacements.Type;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.PacketDistributor;
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

public class OgreLordEntity extends Monster implements IAnimatable, IGiantMob {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   private final ServerBossEvent bossEvent;
   protected static final EntityDataAccessor<BlockPos> FLARE_POS = SynchedEntityData.defineId(OgreLordEntity.class, EntityDataSerializers.BLOCK_POS);
   private static final EntityDataAccessor<Integer> MAGIC_ID = SynchedEntityData.defineId(OgreLordEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> MISC_ANIMATION = SynchedEntityData.defineId(OgreLordEntity.class, EntityDataSerializers.INT);
   public int miscAnimationTicks = 0;

   public OgreLordEntity(EntityType<? extends OgreLordEntity> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
      this.setHealth(this.getMaxHealth());
      this.xpReward = 100;
      this.bossEvent = (ServerBossEvent)new ServerBossEvent(Component.literal("Ogre Lord"), BossBarColor.RED, BossBarOverlay.PROGRESS)
         .setDarkenScreen(true)
         .setPlayBossMusic(true);
      this.bossEvent.setOverlay(BossBarOverlay.NOTCHED_6);
   }

   public Component getDisplayName() {
      return PlayerTeam.formatNameForTeam(this.getTeam(), Component.translatable("entity.trbeyond.ogreboss"))
         .withStyle(p_185975_ -> p_185975_.withHoverEvent(this.createHoverEvent()).withInsertion(this.getStringUUID()));
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(MISC_ANIMATION, 0);
      this.entityData.define(FLARE_POS, BlockPos.ZERO);
      this.entityData.define(MAGIC_ID, 0);
   }

   public void addAdditionalSaveData(@NotNull CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("MiscAnimation", this.getMiscAnimation());
      compound.putDouble("FlareX", (double)this.getFlarePos().getX());
      compound.putDouble("FlareY", (double)this.getFlarePos().getY());
      compound.putDouble("FlareZ", (double)this.getFlarePos().getZ());
      compound.putInt("MagicID", this.getMagicID());
   }

   public void readAdditionalSaveData(@NotNull CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.entityData.set(MISC_ANIMATION, compound.getInt("MiscAnimation"));
      this.setFlarePos(compound.getDouble("FlareX"), compound.getDouble("FlareY"), compound.getDouble("FlareZ"));
      this.setMagicID(compound.getInt("MagicID"));
   }

   public BlockPos getFlarePos() {
      return (BlockPos)this.entityData.get(FLARE_POS);
   }

   public void setFlarePos(double x, double y, double z) {
      this.entityData.set(FLARE_POS, new BlockPos(x, y, z));
   }

   public int getMagicID() {
      return (Integer)this.entityData.get(MAGIC_ID);
   }

   public void setMagicID(int id) {
      this.entityData.set(MAGIC_ID, id);
   }

   public boolean isInvulnerableTo(@NotNull DamageSource source) {
      return source == DamageSource.IN_FIRE
         || source == DamageSource.IN_WALL
         || source == DamageSource.FALLING_BLOCK
         || super.isInvulnerableTo(source)
         || source.getEntity() instanceof OgreLordEntity
         || source.getEntity() instanceof OgreSubEntity;
   }

   public boolean causeFallDamage(float pFallDistance, float pMultiplier, @NotNull DamageSource pSource) {
      return false;
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

   public static AttributeSupplier setAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.JUMP_STRENGTH, 1.5)
         .add(Attributes.MAX_HEALTH, 250.0)
         .add(Attributes.ATTACK_DAMAGE, 100.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
         .add(Attributes.ARMOR, 30.0)
         .add(Attributes.ARMOR_TOUGHNESS, 25.0)
         .add(Attributes.ATTACK_SPEED, 5.0)
         .add(Attributes.ATTACK_KNOCKBACK, 5.0)
         .add(Attributes.FOLLOW_RANGE, 40.0)
         .add(Attributes.MOVEMENT_SPEED, 0.4F)
         .add((Attribute)TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get(), 500.0)
         .build();
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(2, new OgreLordEntity.OgreLordAttackGoal(this, 1.0, true));
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 40.0F));
      this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.8));
      this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
      this.targetSelector
         .addGoal(
            2,
            new NearestAttackableTargetGoal(
               this, LivingEntity.class, false, target -> target.getClass() != this.getClass() && target.getClass() != OgreSubEntity.class
            )
         );
   }

   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      if (!event.isMoving()) {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ogrelord.idle", EDefaultLoopTypes.LOOP));
      } else {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ogrelord.walk", EDefaultLoopTypes.LOOP));
      }

      return PlayState.CONTINUE;
   }

   private <E extends IAnimatable> PlayState miscPredicate(AnimationEvent<E> event) {
      if (event.getController().getAnimationState().equals(AnimationState.Stopped)) {
         System.out.println("-------------------- ATTACK ANIMATION INSIDE -------------------");
         event.getController().markNeedsReload();
         System.out.println(this.getMiscAnimation());
         if (this.getMiscAnimation() == 1) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ogrelord.double_swing", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 2) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ogrelord.meteor_smash", EDefaultLoopTypes.PLAY_ONCE));
         } else if (this.getMiscAnimation() == 3) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ogrelord.ogre_flame", EDefaultLoopTypes.PLAY_ONCE));
         }
      }

      return PlayState.CONTINUE;
   }

   public void registerControllers(AnimationData animationData) {
      animationData.addAnimationController(new AnimationController(this, "movement", 5.0F, this::predicate));
      animationData.addAnimationController(new AnimationController(this, "attacks", 5.0F, this::miscPredicate));
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
            int killedOgre = cap.killedOgre();
            cap.setKilledOgre(killedOgre + 1);
         });
         BeyondBossCapability.sync(player);
         player.getCapability(PlayerVariablesHandler.PLAYER_VARIABLES_CAPABILITY, null)
            .ifPresent(
               capability -> {
                  if (!capability.KilledOgre) {
                     int coinsRemaining = (Integer)ServerConfig.INSTANCE.bossConfig.ogreLordGoldDrop.get();
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

                     capability.KilledOgre = true;
                     capability.syncPlayerVariables(player);
                  }

                  ItemStack[] uniqueDrops = new ItemStack[]{
                     new ItemStack((ItemLike)BeyondArmorItems.OGRE_CHESTPLATE_RARE.get()),
                     new ItemStack((ItemLike)BeyondArmorItems.OGRE_LEGGINGS_RARE.get()),
                     new ItemStack((ItemLike)BeyondArmorItems.OGRE_BOOTS_RARE.get()),
                     new ItemStack((ItemLike)BeyondToolItems.OGRE_AXE_RARE.get())
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
                  if (!capability.KilledOgre) {
                     int coinsRemaining = 10;
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

                     capability.KilledOgre = true;
                     capability.syncPlayerVariables(player);
                  }

                  ItemStack[] uniqueDrops = new ItemStack[]{
                     new ItemStack((ItemLike)BeyondArmorItems.OGRE_CHESTPLATE_RARE.get()),
                     new ItemStack((ItemLike)BeyondArmorItems.OGRE_LEGGINGS_RARE.get()),
                     new ItemStack((ItemLike)BeyondArmorItems.OGRE_BOOTS_RARE.get()),
                     new ItemStack((ItemLike)BeyondToolItems.OGRE_AXE_RARE.get())
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
      this.playSound(SoundEvents.IRON_GOLEM_STEP, 0.25F, 0.5F);
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

   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag
   ) {
      this.summonSubsRandomPos();
      SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
      this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.ogreLordJumpStrength.get());
      this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.ogreLordMaxHealth.get());
      this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.ogreLordAttackDamage.get());
      this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.ogreLordKnockbackResistance.get());
      this.getAttribute(Attributes.ARMOR).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.ogreLordArmor.get());
      this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.ogreLordArmorToughness.get());
      this.getAttribute(Attributes.ATTACK_SPEED).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.ogreLordAttackSpeed.get());
      this.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.ogreLordAttackKnockback.get());
      this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.ogreLordFollowRange.get());
      this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.ogreLordMovementSpeed.get());
      this.getAttribute((Attribute)TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get())
         .setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.ogreLordMaxSpiritualHealth.get());
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

   protected void summonSubsRandomPos() {
      if (this.getLevel() instanceof ServerLevel serverLevel) {
         int i = Mth.floor(this.getX());
         int j = Mth.floor(this.getY());
         int k = Mth.floor(this.getZ());

         for (int subs = 0; subs < 5; subs++) {
            OgreSubEntity sub = new OgreSubEntity((EntityType<? extends OgreSubEntity>)BeyondEntities.OGRE_SUB.get(), serverLevel);

            for (int l = 0; l < 50; l++) {
               int i1 = i + Mth.nextInt(this.random, 0, 7) * Mth.nextInt(this.random, -1, 1);
               int j1 = j + Mth.nextInt(this.random, 0, 7) * Mth.nextInt(this.random, -1, 1);
               int k1 = k + Mth.nextInt(this.random, 0, 7) * Mth.nextInt(this.random, -1, 1);
               BlockPos blockpos = new BlockPos(i1, j1, k1);
               EntityType<?> entitytype = (EntityType<?>)BeyondEntities.OGRE_SUB.get();
               Type type = SpawnPlacements.getPlacementType(entitytype);
               if (NaturalSpawner.isSpawnPositionOk(type, serverLevel, blockpos, entitytype)
                  && SpawnPlacements.checkSpawnRules(entitytype, serverLevel, MobSpawnType.REINFORCEMENT, blockpos, serverLevel.random)) {
                  sub.setPos((double)i1, (double)j1, (double)k1);
                  if (serverLevel.isUnobstructed(sub) && serverLevel.noCollision(sub)) {
                     sub.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(sub.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     sub.setTarget(this.getTarget());
                     serverLevel.addFreshEntityWithPassengers(sub);
                     SkillUtils.learnSkill(sub, (ManasSkill)ResistanceSkills.FLAME_ATTACK_RESISTANCE.get());
                     ManasSkillInstance instance = SkillUtils.getSkillOrNull(sub, (ManasSkill)ResistanceSkills.FLAME_ATTACK_RESISTANCE.get());
                     if (instance != null && instance.canBeToggled(sub)) {
                        instance.setToggled(true);
                     }
                     break;
                  }
               }
            }
         }
      }
   }

   private void createHelixParticles(Level pLevel, LivingEntity pLivingEntity, int stateTimer) {
      double baseRadius = 1.5;
      double heightIncrement = 0.1;
      int maxParticlesCount = Math.min(100, stateTimer / 2);

      for (int i = 0; i < maxParticlesCount; i++) {
         double angle = (double)(i * 2) * Math.PI / 10.0 + (double)stateTimer * 0.2;
         double x = baseRadius * Math.cos(angle);
         double y = (double)i * heightIncrement + 1.0;
         double z = baseRadius * Math.sin(angle);
         x += this.randomOffset();
         y += this.randomOffset();
         z += this.randomOffset();
         double particleX = pLivingEntity.getX() + x;
         double particleY = pLivingEntity.getY() + y;
         double particleZ = pLivingEntity.getZ() + z;
         TensuraParticleHelper.spawnServerParticles(
            pLevel, (ParticleOptions)TensuraParticles.RED_FIRE.get(), particleX, particleY, particleZ, 1, 0.0, 0.25, 0.0, 0.1, false
         );
      }
   }

   private void createFirePillar(Level pLevel, int FIRE_PILLAR_DURATION, int firePillarTimer, LivingEntity pLivingEntity) {
      double radius = 6.5;
      double height = 5.0;
      int particlesPerRevolution = 4;
      int totalParticles = particlesPerRevolution * FIRE_PILLAR_DURATION;
      Vec3 targetPosition = pLivingEntity.getPosition(0.0F);

      for (int i = 0; i < totalParticles; i++) {
         double t = (double)i / (double)particlesPerRevolution;
         double angle = (Math.PI * 2) * t + (double)firePillarTimer * (Math.PI / (double)FIRE_PILLAR_DURATION);
         double x = radius * Math.cos(angle);
         double y = height * t;
         double z = radius * Math.sin(angle);
         x += this.randomOffset();
         y += this.randomOffset();
         z += this.randomOffset();
         double particleX = targetPosition.x + x;
         double particleY = targetPosition.y + y;
         double particleZ = targetPosition.z + z;
         TensuraParticleHelper.spawnServerParticles(
            pLevel, (ParticleOptions)TensuraParticles.RED_FIRE.get(), particleX, particleY, particleZ, 1, 0.0, 0.25, 0.0, 0.1, false
         );
      }
   }

   private void createLineParticles(Level pLevel, LivingEntity pLivingEntity) {
      Vec3 lookVec = this.getLookAngle();
      Vec3 startPos = this.getEyePosition(1.0F);
      HitResult hitResult = pLevel.clip(
         new ClipContext(startPos, Objects.requireNonNull(this.getTarget()).getPosition(0.1F), Block.COLLIDER, Fluid.NONE, pLivingEntity)
      );
      int particleCount = 20;
      double swerveAmplitude = 0.8;
      double swerveFrequency = 2.0;

      for (int i = 0; i < particleCount; i++) {
         double t = (double)i / (double)(particleCount - 1);
         Vec3 swerveVec = this.calculateSwerve(lookVec, t, swerveAmplitude, swerveFrequency);
         Vec3 pos = startPos.add(lookVec.scale(hitResult.getLocation().distanceTo(startPos) * t)).add(swerveVec);
         double speedMultiplier = 0.5 + (1.0 - t) * 0.1;

         for (double j = 0.0; j < 5.0; j++) {
            TensuraParticleHelper.spawnServerParticles(
               pLevel,
               (ParticleOptions)TensuraParticles.RED_FIRE.get(),
               pos.x,
               pos.y - 0.3,
               pos.z,
               1,
               lookVec.x * speedMultiplier,
               lookVec.y * speedMultiplier,
               lookVec.z * speedMultiplier,
               0.1,
               true
            );
         }
      }
   }

   private Vec3 calculateSwerve(Vec3 lookVec, double t, double amplitude, double frequency) {
      double swerveX = Math.sin(t * Math.PI * frequency) * amplitude;
      double swerveY = Math.cos(t * Math.PI * frequency) * amplitude;
      return lookVec.cross(new Vec3(0.0, 1.0, 0.0))
         .normalize()
         .scale(swerveX)
         .add(lookVec.cross(new Vec3(1.0, 0.0, 0.0)).normalize().scale(swerveY))
         .add(new Vec3((this.random.nextDouble() - 0.5) * 0.1, (this.random.nextDouble() - 0.5) * 0.1, (this.random.nextDouble() - 0.5) * 0.1));
   }

   private double randomOffset() {
      return (this.random.nextDouble() - 0.5) * 0.2;
   }

   public boolean shouldAttack(LivingEntity target) {
      return target instanceof Player && ((Player)target).isCreative();
   }

   public void tick() {
      super.tick();
      if (this.getMiscAnimation() != 0) {
         this.miscAnimationTicks++;
         if (!this.isAlive()) {
            return;
         }

         if (this.getMiscAnimation() == 1) {
            if (this.miscAnimationTicks == 22) {
               LivingEntity target = this.getTarget();
               this.level
                  .playSound(
                     null,
                     this.getX(),
                     this.getY(),
                     this.getZ(),
                     (SoundEvent)BeyondSoundEvents.ogre_lord_double_strike.get(),
                     SoundSource.NEUTRAL,
                     1.0F,
                     1.0F
                  );
               if (target != null && (double)target.distanceTo(this) <= 5.0) {
                  this.lookAt(Anchor.EYES, target.position());
                  this.doHurtTarget(target);
               }
            }

            if (this.miscAnimationTicks == 47) {
               LivingEntity target = this.getTarget();
               if (target != null && (double)target.distanceTo(this) <= 5.0) {
                  this.lookAt(Anchor.EYES, target.position());
                  this.doHurtTarget(target);
               }
            }
         }

         if (this.getMiscAnimation() == 2) {
            LivingEntity target = this.getTarget();
            if (target != null) {
               this.lookAt(Anchor.EYES, target.position());
               if (this.miscAnimationTicks > 24 && this.miscAnimationTicks < 40 && this.isOnGround()) {
                  double x = target.getX() - this.getX();
                  double z = target.getZ() - this.getZ();
                  double xz = Math.sqrt(x * x + z * z);
                  this.setDeltaMovement(x / xz * 1.3, 1.0, z / xz * 1.3);
               }

               if (this.miscAnimationTicks % 2 == 0 && this.miscAnimationTicks < 55 && this.miscAnimationTicks > 25) {
                  this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 1.1, 1.0));
               }

               if (this.miscAnimationTicks == 50) {
                  this.setDeltaMovement(this.getDeltaMovement().multiply(0.0, 0.0, 0.0));
                  this.setNoGravity(true);
               }

               if (this.miscAnimationTicks == 60) {
                  this.setNoGravity(false);
                  this.setDeltaMovement(0.0, -8.0, 0.0);
                  this.level
                     .playSound(
                        null,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        (SoundEvent)BeyondSoundEvents.ogre_lord_meteorite_strike.get(),
                        SoundSource.HOSTILE,
                        1.0F,
                        1.0F
                     );
               }

               if (this.miscAnimationTicks == 65) {
                  if (this.isOnGround()) {
                     AABB aabb = new AABB(this.blockPosition()).inflate(5.0);

                     for (LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, aabb)) {
                        if (entity != this) {
                           entity.hurt(DamageSource.mobAttack(this), (float)(this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 4.0));
                        }
                     }
                  }

                  TensuraParticleHelper.spawnGroundSlamParticle(this, 75, 3.0F);
               }
            }
         }

         if (this.getMiscAnimation() == 3) {
            if (this.miscAnimationTicks <= 70 && this.miscAnimationTicks >= 20) {
               LivingEntity target = this.getTarget();
               this.createHelixParticles(this.level, this, this.miscAnimationTicks);
               if (target != null) {
                  this.lookAt(Anchor.EYES, target.position());
               }
            } else if (this.miscAnimationTicks == 72) {
               LivingEntity target = this.getTarget();
               if (target != null) {
                  Vec3 pos = target.position();
                  this.createLineParticles(this.level, target);
                  this.setFlarePos(pos.x(), pos.y(), pos.z());
                  this.setMagicID(0);
                  this.level
                     .playSound(
                        null,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        (SoundEvent)BeyondSoundEvents.ogre_lord_ogre_flame.get(),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
               }
            } else if (this.miscAnimationTicks >= 90) {
               LivingEntity target = this.getTarget();
               if (target != null) {
                  this.createFirePillar(this.level, 40, 40, target);
                  this.flareCircle(this.miscAnimationTicks);
               }
            }
         }

         if (this.miscAnimationTicks >= this.getAnimationTick(this.getMiscAnimation())) {
            this.setMiscAnimation(0);
            this.miscAnimationTicks = 0;
         }
      }
   }

   private int getAnimationTick(int miscAnimation) {
      return switch (miscAnimation) {
         case 1 -> 60;
         case 2 -> 80;
         case 3 -> 110;
         default -> 20;
      };
   }

   public boolean doHurtTarget(@NotNull Entity pEntity) {
      boolean flag = super.doHurtTarget(pEntity);
      if (flag && this.getMiscAnimation() == 0) {
         this.setMiscAnimation(1);
      }

      return flag;
   }

   private void flareCircle(int heldTicks) {
      BlockPos pos = this.getFlarePos();
      if (heldTicks >= 91) {
         int flareID = this.getMagicID();
         if (flareID == 0) {
            FlareCircleEntity barrier = new FlareCircleEntity((EntityType<? extends FlareCircleEntity>)TensuraEntityTypes.FLARE_CIRCLE.get(), this.level);
            barrier.setOwner(this);
            barrier.setDamage((float)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
            barrier.setRadius(4.0F);
            barrier.setHeight(5.0F);
            barrier.setLife(30);
            barrier.setHealth(200.0F);
            barrier.setPos(new Vec3((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()));
            barrier.setMpCost(2000.0);
            barrier.setSkill((ManasSkillInstance)SkillAPI.getSkillsFrom(this).getSkill((ManasSkill)SpiritualMagics.FLARE_CIRCLE.get()).orElse(null));
            this.getLevel().addFreshEntity(barrier);
            this.setMagicID(barrier.getId());
         } else if (this.getLevel().getEntity(flareID) instanceof BarrierEntity barrier) {
            barrier.increaseLife(5);
         } else {
            this.setMagicID(0);
         }

         this.playSound(SoundEvents.BLAZE_SHOOT, 10.0F, 0.95F + this.random.nextFloat() * 0.1F);
      } else {
         TensuraNetwork.INSTANCE
            .send(
               PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this),
               new RequestFxSpawningPacket(new ResourceLocation("tensura:flare_circle_circle"), pos, 0.0, 0.0, 0.0, 0, true)
            );
      }
   }

   class OgreLordAttackGoal extends MeleeAttackGoal {
      private final OgreLordEntity ogre;

      public OgreLordAttackGoal(OgreLordEntity ogre, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
         super(ogre, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
         this.ogre = ogre;
      }

      public void tick() {
         if (this.ogre.getMiscAnimation() == 0) {
            super.tick();
         }
      }

      protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy, double pDistToEnemySqr) {
         double reach = this.getAttackReachSqr(pEnemy);
         if (this.ogre.getMiscAnimation() == 0 && pEnemy.isAlive()) {
            int randomAttack = this.randomAttack(pDistToEnemySqr);
            double var10000;
            double var10001;
            switch (randomAttack) {
               case 2:
                  var10000 = 100.0;
                  var10001 = 25.0;
                  break;
               case 3:
                  this.ogre.getNavigation().stop();
                  var10000 = 400.0;
                  var10001 = 25.0;
                  break;
               default:
                  var10000 = reach;
                  var10001 = 1.0;
            }

            if (pDistToEnemySqr <= var10000 && pDistToEnemySqr >= var10001 && this.isTimeToAttack()) {
               this.resetAttackCooldown();
               this.ogre.setMiscAnimation(randomAttack);
            }
         }
      }

      protected int randomAttack(double distance) {
         if (distance > 25.0 && distance < 400.0 && this.ogre.getRandom().nextInt(10) == 0) {
            return 3;
         } else {
            return distance > 25.0 && distance < 100.0 && this.ogre.getRandom().nextInt(5) == 0 ? 2 : 1;
         }
      }

      protected double getAttackReachSqr(LivingEntity pAttackTarget) {
         return (double)(this.mob.getBbWidth() * this.mob.getBbWidth() * 3.0F + pAttackTarget.getBbWidth());
      }
   }
}
