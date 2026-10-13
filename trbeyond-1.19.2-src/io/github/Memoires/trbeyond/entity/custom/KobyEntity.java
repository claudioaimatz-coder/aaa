package io.github.Memoires.trbeyond.entity.custom;

import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.LookAtTradingPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class KobyEntity extends AbstractVillager implements IAnimatable {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   private final KobyMerchant merchant = new KobyMerchant();

   public KobyEntity(EntityType<? extends AbstractVillager> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
   }

   public boolean canBeAffected(MobEffectInstance pEffectInstance) {
      return false;
   }

   @NotNull
   public InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
      if (this.isAlive() && !this.isTrading() && !this.isSleeping() && !player.isSecondaryUseActive() && hand == InteractionHand.MAIN_HAND) {
         if (!this.level.isClientSide) {
            List<? extends String> lockedNPCs = (List<? extends String>)ServerConfig.INSTANCE.npcShopConfigs.get();
            String key = ForgeRegistries.ENTITY_TYPES.getKey((EntityType)BeyondEntities.KOBY.get()).toString();
            if (!lockedNPCs.contains(key)) {
               this.merchant.setTradingPlayer(player);
               if (this.merchant.getOffers().isEmpty()) {
                  this.setUnhappy();
                  this.merchant.setTradingPlayer(null);
               } else {
                  this.startTrading(player);
               }
            } else {
               this.setUnhappy();
            }
         }

         return InteractionResult.sidedSuccess(this.level.isClientSide);
      } else {
         return InteractionResult.sidedSuccess(this.level.isClientSide);
      }
   }

   private void setUnhappy() {
      this.setUnhappyCounter(40);
      if (!this.level.isClientSide()) {
         this.playSound(SoundEvents.VILLAGER_NO, this.getSoundVolume(), this.getVoicePitch());
      }
   }

   private void startTrading(Player player) {
      this.setTradingPlayer(player);
      this.merchant.setTradingPlayer(player);
      this.openTradingScreen(player, this.getDisplayName(), 1);
   }

   public void notifyTrade(MerchantOffer offer) {
      this.merchant.notifyTrade(offer);
   }

   public void stopTrading() {
      super.stopTrading();
      this.setTradingPlayer(null);
   }

   public void onAddedToWorld() {
      super.onAddedToWorld();
   }

   public MerchantOffers getOffers() {
      return this.merchant.getOffers();
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(2, new LookAtTradingPlayerGoal(this));
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 5.0F));
      this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
   }

   public void setTradingPlayer(@Nullable Player p_35508_) {
      boolean flag = this.getTradingPlayer() != null && p_35508_ == null;
      super.setTradingPlayer(p_35508_);
      if (flag) {
         this.stopTrading();
      }
   }

   public void thunderHit(ServerLevel level, LightningBolt bolt) {
   }

   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      return PlayState.CONTINUE;
   }

   private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
      if (this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
         event.getController().markNeedsReload();
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.swipe", EDefaultLoopTypes.PLAY_ONCE));
         this.swinging = false;
      }

      return PlayState.CONTINUE;
   }

   public static AttributeSupplier setAttributes() {
      return Villager.createLivingAttributes()
         .add(Attributes.MAX_HEALTH, 9999.0)
         .add(Attributes.ARMOR, 100.0)
         .add(Attributes.ARMOR_TOUGHNESS, 100.0)
         .add(Attributes.ATTACK_DAMAGE, 999.0)
         .add(Attributes.ATTACK_SPEED, 999.0)
         .add(Attributes.ATTACK_KNOCKBACK, 999.0)
         .add(Attributes.FOLLOW_RANGE, 48.0)
         .add(Attributes.MOVEMENT_SPEED, 0.5)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
         .build();
   }

   protected void rewardTradeXp(MerchantOffer pOffer) {
   }

   protected void updateTrades() {
   }

   @org.jetbrains.annotations.Nullable
   public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
      return null;
   }

   public void registerControllers(AnimationData animationData) {
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   public boolean isInvulnerableTo(DamageSource damageSource) {
      return !damageSource.equals(DamageSource.OUT_OF_WORLD);
   }

   public boolean isPushable() {
      return false;
   }

   public void checkDespawn() {
   }

   protected SoundEvent getAmbientSound() {
      return null;
   }

   protected SoundEvent getHurtSound(DamageSource damageSource) {
      return null;
   }

   protected SoundEvent getDeathSound() {
      return null;
   }

   protected SoundEvent getTradeUpdatedSound(boolean success) {
      return null;
   }

   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      this.merchant.saveToNBT(tag);
   }

   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      this.merchant.loadFromNBT(tag);
   }

   public void tick() {
      super.tick();
      if (this.tickCount % 1200 == 0) {
         this.merchant.checkAndRestock();
      }
   }
}
