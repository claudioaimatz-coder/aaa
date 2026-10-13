package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.capability.SkillStorage;
import com.github.manasmods.tensura.ability.skill.resist.ResistSkill;
import com.github.manasmods.tensura.ability.skill.resist.ResistSkill.ResistType;
import com.github.manasmods.tensura.api.entity.subclass.IGiantMob;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.items.TensuraMaterialItems;
import io.github.Memoires.trbeyond.capability.boss.BeyondBossCapability;
import io.github.Memoires.trbeyond.capability.entities.EliteMobCapability;
import io.github.Memoires.trbeyond.client.screen.BossLimiterScreen;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.network.PlayerVariablesHandler;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.procedures.MezulAttackDamagingProcedure;
import io.github.Memoires.trbeyond.procedures.MezulAttackingProcedure;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import io.github.Memoires.trbeyond.registry.items.BeyondArmorItems;
import io.github.Memoires.trbeyond.registry.items.BeyondToolItems;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import io.github.Memoires.trbeyond.world.BeyondGamerules;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages.SpawnEntity;
import net.minecraftforge.registries.ForgeRegistries;
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

public class MezulEntity extends TensuraTamableEntity implements IAnimatable, ILimiter, IGiantMob {
   public static final EntityDataAccessor<Integer> LIMITATION = SynchedEntityData.defineId(GozulEntity.class, EntityDataSerializers.INT);
   public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(MezulEntity.class, EntityDataSerializers.STRING);
   public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(MezulEntity.class, EntityDataSerializers.STRING);
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   private boolean swinging;
   private boolean lastloop;
   private long lastSwing;
   public String animationprocedure = "empty";
   private final ServerBossEvent bossEvent;

   public MezulEntity(SpawnEntity packet, Level world) {
      this((EntityType<MezulEntity>)BeyondEntities.MEZUL.get(), world);
   }

   public MezulEntity(EntityType<MezulEntity> type, Level world) {
      super(type, world);
      this.maxUpStep = 2.0F;
      this.xpReward = 0;
      this.setNoAi(false);
      this.setPersistenceRequired();
      this.bossEvent = (ServerBossEvent)new ServerBossEvent(this.getDisplayName(), BossBarColor.RED, BossBarOverlay.NOTCHED_20).setPlayBossMusic(true);
   }

   public Component getDisplayName() {
      return PlayerTeam.formatNameForTeam(this.getTeam(), Component.translatable("entity.trbeyond.mezul"))
         .withStyle(p_185975_ -> p_185975_.withHoverEvent(this.createHoverEvent()).withInsertion(this.getStringUUID()));
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(ANIMATION, "undefined");
      this.entityData.define(TEXTURE, "mezul_retexture1");
   }

   @Override
   public void addAdditionalSaveData(CompoundTag pCompound) {
      super.addAdditionalSaveData(pCompound);
      pCompound.putInt("Limitation", this.getLimitation());
      pCompound.putString("Animation", this.getSyncedAnimation());
      pCompound.putString("Texture", this.getTexture());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag pCompound) {
      super.readAdditionalSaveData(pCompound);
      if (this.hasCustomName()) {
         this.bossEvent.setName(this.getDisplayName());
      }

      this.entityData.set(LIMITATION, pCompound.getInt("Limitation"));
      this.entityData.set(ANIMATION, pCompound.getString("Animation"));
      this.entityData.set(TEXTURE, pCompound.getString("Texture"));
   }

   public void setCustomName(@Nullable Component pName) {
      super.setCustomName(pName);
      this.bossEvent.setName(this.getDisplayName());
   }

   public void startSeenByPlayer(ServerPlayer pPlayer) {
      super.startSeenByPlayer(pPlayer);
      this.bossEvent.addPlayer(pPlayer);
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
   public void setLimitation(int id) {
      this.entityData.set(LIMITATION, id);
   }

   @Override
   public int getLimitation() {
      return (Integer)this.entityData.get(LIMITATION);
   }

   public void setTexture(String texture) {
      this.entityData.set(TEXTURE, texture);
   }

   public String getTexture() {
      return (String)this.entityData.get(TEXTURE);
   }

   public Packet<?> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   public boolean isInvulnerableTo(DamageSource source) {
      return this.getTarget() == null ? true : super.isInvulnerableTo(source);
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

   protected void registerGoals() {
      super.registerGoals();
      this.targetSelector.addGoal(1, new MezulEntity.MezulAttack(this, 1.2, false));
      this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0));
      this.targetSelector.addGoal(4, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
      this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(this, LivingEntity.class, 0, true, false, e -> true));
      this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(this, Player.class, false, target -> this.shouldAttack(this, target)));
      this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(6, new FloatGoal(this));
   }

   @Override
   public void tick() {
      super.tick();
      if (this.tickCount % 20 == 0) {
         this.applyLimitation(this);
      }
   }

   public MobType getMobType() {
      return MobType.UNDEFINED;
   }

   @Override
   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return false;
   }

   public SoundEvent getHurtSound(DamageSource ds) {
      return (SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.hurt"));
   }

   protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockIn) {
      this.playSound((SoundEvent)BeyondSoundEvents.mezul_step_loop.get(), 0.25F, 0.5F);
   }

   public SoundEvent getDeathSound() {
      return (SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
   }

   public void baseTick() {
      super.baseTick();
      MezulAttackDamagingProcedure.execute(this.level, this);
      this.refreshDimensions();
   }

   @Override
   public EntityDimensions getDimensions(Pose p_33597_) {
      return super.getDimensions(p_33597_).scale(1.0F);
   }

   public static void init() {
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
      builder = builder.add(Attributes.MAX_HEALTH, 2000.0);
      builder = builder.add(Attributes.ARMOR, 200.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 500.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 32.0);
      builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
      builder = builder.add(Attributes.ATTACK_KNOCKBACK, 1.0);
      return builder.add((Attribute)TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get(), 4000.0);
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      if (this.isAlive() && this.getTarget() == null) {
         if (TensuraEPCapability.getEP(player) < TensuraEPCapability.getEP(this)) {
            return InteractionResult.PASS;
         } else if (this.isAggressive()) {
            return InteractionResult.PASS;
         } else {
            if (player.level.isClientSide) {
               this.openBossLimiterScreen(this.getUUID());
            }

            return InteractionResult.sidedSuccess(player.level.isClientSide);
         }
      } else {
         return super.mobInteract(player, hand);
      }
   }

   @OnlyIn(Dist.CLIENT)
   private void openBossLimiterScreen(UUID mobUUID) {
      Minecraft.getInstance().setScreen(new BossLimiterScreen(mobUUID));
   }

   public boolean doHurtTarget(Entity target) {
      return false;
   }

   public void die(DamageSource source) {
      super.die(source);
      if (source.getEntity() instanceof Player player) {
         BeyondBossCapability.getFrom(player).ifPresent(cap -> {
            int killedMezul = cap.killedMezul();
            cap.setKilledMezul(killedMezul + 1);
         });
         BeyondBossCapability.sync(player);
         player.getCapability(PlayerVariablesHandler.PLAYER_VARIABLES_CAPABILITY, null)
            .ifPresent(
               capability -> {
                  if (!capability.KilledMezul) {
                     int coinsRemaining = (Integer)ServerConfig.INSTANCE.bossConfig.mezulGoldDrop.get();
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

                     capability.KilledMezul = true;
                     capability.syncPlayerVariables(player);
                  }

                  ItemStack[] uniqueDrops = new ItemStack[]{
                     new ItemStack((ItemLike)BeyondArmorItems.MINOS_HELMET_UNIQUE.get()),
                     new ItemStack((ItemLike)BeyondArmorItems.MINOS_CHESTPLATE_UNIQUE.get()),
                     new ItemStack((ItemLike)BeyondArmorItems.MINOS_LEGGINGS_UNIQUE.get()),
                     new ItemStack((ItemLike)BeyondArmorItems.MINOS_BOOTS_UNIQUE.get()),
                     new ItemStack((ItemLike)BeyondToolItems.MINOS_TRIDENT_UNIQUE.get())
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
      } else if (source.getEntity() instanceof LivingEntity attacker && attacker instanceof TamableAnimal tame && tame.getOwner() instanceof Player player) {
         player.getCapability(PlayerVariablesHandler.PLAYER_VARIABLES_CAPABILITY, null)
            .ifPresent(
               capability -> {
                  if (!capability.KilledMezul) {
                     int coinsRemaining = 100;
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

                     capability.KilledMezul = true;
                     capability.syncPlayerVariables(player);
                  }

                  ItemStack[] uniqueDrops = new ItemStack[]{
                     new ItemStack((ItemLike)BeyondArmorItems.MINOS_HELMET_UNIQUE.get()),
                     new ItemStack((ItemLike)BeyondArmorItems.MINOS_CHESTPLATE_UNIQUE.get()),
                     new ItemStack((ItemLike)BeyondArmorItems.MINOS_LEGGINGS_UNIQUE.get()),
                     new ItemStack((ItemLike)BeyondArmorItems.MINOS_BOOTS_UNIQUE.get()),
                     new ItemStack((ItemLike)BeyondToolItems.MINOS_TRIDENT_UNIQUE.get())
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

   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag
   ) {
      SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
      this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.mezulMovementSpeed.get());
      this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.mezulMaxHealth.get());
      this.getAttribute(Attributes.ARMOR).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.mezulArmor.get());
      this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.mezulAttackDamage.get());
      this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.mezulFollowRange.get());
      this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.mezulKnockbackResistance.get());
      this.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.mezulAttackKnockback.get());
      this.getAttribute((Attribute)TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get())
         .setBaseValue((Double)ServerConfig.INSTANCE.bossConfig.mezulMaxSpiritualHealth.get());
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

   private <E extends IAnimatable> PlayState movementPredicate(AnimationEvent<E> event) {
      if (this.animationprocedure.equals("empty")) {
         if (!event.isMoving() && event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
         } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("run", EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
         }
      } else {
         return PlayState.STOP;
      }
   }

   private <E extends IAnimatable> PlayState procedurePredicate(AnimationEvent<E> event) {
      Level world = super.level;
      boolean loop = false;
      double x = this.getX();
      double y = this.getY();
      double z = this.getZ();
      if (!loop && this.lastloop) {
         this.lastloop = false;
         event.getController().setAnimation(new AnimationBuilder().addAnimation(this.animationprocedure, EDefaultLoopTypes.PLAY_ONCE));
         event.getController().clearAnimationCache();
         return PlayState.STOP;
      } else {
         if (!this.animationprocedure.equals("empty") && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            if (!loop) {
               event.getController().setAnimation(new AnimationBuilder().addAnimation(this.animationprocedure, EDefaultLoopTypes.PLAY_ONCE));
               if (event.getController().getAnimationState().equals(AnimationState.Stopped)) {
                  this.animationprocedure = "empty";
                  event.getController().markNeedsReload();
               }
            } else {
               event.getController().setAnimation(new AnimationBuilder().addAnimation(this.animationprocedure, EDefaultLoopTypes.LOOP));
               this.lastloop = true;
            }
         }

         return PlayState.CONTINUE;
      }
   }

   protected void tickDeath() {
      this.deathTime++;
      if (this.deathTime == 20) {
         this.remove(RemovalReason.KILLED);
         this.dropExperience();
      }
   }

   public String getSyncedAnimation() {
      return (String)this.entityData.get(ANIMATION);
   }

   public void setAnimation(String animation) {
      this.entityData.set(ANIMATION, animation);
   }

   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController(this, "movement", 4.0F, this::movementPredicate));
      data.addAnimationController(new AnimationController(this, "procedure", 4.0F, this::procedurePredicate));
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   class MezulAttack extends MeleeAttackGoal {
      public MezulAttack(MezulEntity mezul, double speed, boolean chaseIfLost) {
         super(mezul, speed, chaseIfLost);
      }

      public void tick() {
         super.tick();
         if (this.mob.getTarget() != null) {
            MezulAttackingProcedure.execute(this.mob.getLevel(), this.mob.getTarget(), this.mob);
         }
      }

      protected double getAttackReachSqr(LivingEntity entity) {
         return 64.0;
      }
   }
}
