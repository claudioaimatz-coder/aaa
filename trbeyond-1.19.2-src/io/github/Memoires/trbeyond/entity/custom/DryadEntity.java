package io.github.Memoires.trbeyond.entity.custom;

import io.github.Memoires.trbeyond.capability.dungeon.DungeonCapability;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import io.github.Memoires.trbeyond.registry.items.BeyondItems;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
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

public class DryadEntity extends AbstractVillager implements IAnimatable {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   private final CustomDryadMerchant merchant = new CustomDryadMerchant();

   public DryadEntity(EntityType<? extends AbstractVillager> entityType, Level level) {
      super(entityType, level);
   }

   public boolean canBeAffected(MobEffectInstance pEffectInstance) {
      return false;
   }

   @NotNull
   public InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
      if (this.isAlive() && !this.isTrading() && !this.isSleeping() && !player.isSecondaryUseActive()) {
         List<? extends String> lockedNPCs = (List<? extends String>)ServerConfig.INSTANCE.npcShopConfigs.get();
         String key = ForgeRegistries.ENTITY_TYPES.getKey((EntityType)BeyondEntities.DRYAD.get()).toString();
         if (!lockedNPCs.contains(key)) {
            if (!DungeonCapability.hasTakenVoucher(player)) {
               if (player.getLevel().isClientSide()) {
                  player.displayClientMessage(Component.translatable("trbeyond.message.dryad.voucher").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)), false);
                  player.displayClientMessage(Component.translatable("trbeyond.message.dryad.boss_rush").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)), false);
               }

               DungeonCapability.setTakenVoucher(player, true);
               player.addItem(new ItemStack((ItemLike)BeyondItems.DUNGEON_VOUCHER.get()));
               player.addItem(new ItemStack((ItemLike)BeyondItems.REVIVAL_BRACELET.get()));
               this.playSound(SoundEvents.VILLAGER_YES, this.getSoundVolume(), this.getVoicePitch());
               return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

            boolean empty = this.getOffers().isEmpty();
            if (hand == InteractionHand.MAIN_HAND && empty && !this.level.isClientSide) {
               this.setUnhappy();
            }

            if (!empty && !this.level.isClientSide && !this.merchant.getOffers().isEmpty()) {
               this.startTrading(player);
            }
         }

         return InteractionResult.sidedSuccess(this.level.isClientSide);
      } else {
         return super.mobInteract(player, hand);
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
      this.openTradingScreen(player, Component.translatable("entity.trbeyond.dryad"), 1);
   }

   public void stopTrading() {
      super.stopTrading();
      this.setTradingPlayer(null);
   }

   public void setTradingPlayer(@Nullable Player p_35508_) {
      boolean flag = this.getTradingPlayer() != null && p_35508_ == null;
      super.setTradingPlayer(p_35508_);
      if (flag) {
         this.stopTrading();
      }
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

   public void thunderHit(ServerLevel level, LightningBolt bolt) {
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

   public void registerControllers(AnimationData animationData) {
      animationData.addAnimationController(new AnimationController(this, "controller", 0.0F, this::predicate));
      animationData.addAnimationController(new AnimationController(this, "attackController", 0.0F, this::attackPredicate));
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   protected void rewardTradeXp(MerchantOffer p_35299_) {
   }

   protected void updateTrades() {
   }

   @org.jetbrains.annotations.Nullable
   public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
      return null;
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

   public boolean isInvulnerableTo(DamageSource damageSource) {
      return !damageSource.equals(DamageSource.OUT_OF_WORLD);
   }

   public boolean isPushable() {
      return false;
   }

   public void checkDespawn() {
   }

   public void notifyTrade(MerchantOffer offer) {
      ItemStack result = offer.getResult();
      Player player = this.getTradingPlayer();
      if (player != null && result.is((Item)BeyondItems.RECORDING_CRYSTAL.get())) {
         System.out.println("[DEBUG] Player traded for Recording Crystal");
         if (player instanceof ServerPlayer serverPlayer) {
            MinecraftServer server = serverPlayer.getServer();
            if (server != null) {
               ResourceLocation advId = new ResourceLocation("trbeyond", "big_spender");
               Advancement advancement = server.m_129889_().getAdvancement(advId);
               if (advancement != null) {
                  AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);

                  for (String criterion : progress.getRemainingCriteria()) {
                     serverPlayer.getAdvancements().award(advancement, criterion);
                  }
               }
            }
         }
      }
   }
}
