package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.registry.items.TensuraSmithingSchematicItems;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.LookAtTradingPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class KaijinEntity extends AbstractVillager implements IAnimatable {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   private final KaijinMerchant merchant = new KaijinMerchant();

   public KaijinEntity(EntityType<? extends AbstractVillager> entityType, Level level) {
      super(entityType, level);
   }

   public boolean canBeAffected(MobEffectInstance pEffectInstance) {
      return false;
   }

   @NotNull
   public InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
      if (this.isAlive() && !this.isTrading() && !this.isSleeping() && !player.isSecondaryUseActive()) {
         List<? extends String> lockedNPCs = (List<? extends String>)ServerConfig.INSTANCE.npcShopConfigs.get();
         String key = ForgeRegistries.ENTITY_TYPES.getKey((EntityType)BeyondEntities.KAIJIN.get()).toString();
         if (!lockedNPCs.contains(key)) {
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
      this.openTradingScreen(player, Component.translatable("entity.trbeyond.kaijin"), 1);
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
      return Mob.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 48.0).add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.KNOCKBACK_RESISTANCE, 1.0).build();
   }

   public void registerControllers(AnimationData animationData) {
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   public boolean hurt(DamageSource source, float p_35511_) {
      return !source.isBypassInvul() ? false : super.hurt(source, p_35511_);
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
      if (player != null && this.isTensuraSchematic(result.getItem())) {
         System.out.println("[DEBUG] Player traded for a Schematic");
         if (player instanceof ServerPlayer serverPlayer) {
            MinecraftServer server = serverPlayer.getServer();
            if (server != null) {
               ResourceLocation advId = new ResourceLocation("trbeyond", "craftsman");
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

   private boolean isTensuraSchematic(Item item) {
      return item == TensuraSmithingSchematicItems.BASIC_BOWS.get()
         || item == TensuraSmithingSchematicItems.SPIDER_BOWS.get()
         || item == TensuraSmithingSchematicItems.JAPANESE_SWORD.get()
         || item == TensuraSmithingSchematicItems.HUNTING_KNIFE.get()
         || item == TensuraSmithingSchematicItems.SHORT_SWORD.get()
         || item == TensuraSmithingSchematicItems.LONG_SWORD.get()
         || item == TensuraSmithingSchematicItems.GREAT_SWORD.get()
         || item == TensuraSmithingSchematicItems.SPEAR.get()
         || item == TensuraSmithingSchematicItems.KUNAI.get()
         || item == TensuraSmithingSchematicItems.SHIELD.get()
         || item == TensuraSmithingSchematicItems.WINGED_SHOES.get()
         || item == TensuraSmithingSchematicItems.LEATHER_GEAR.get()
         || item == TensuraSmithingSchematicItems.MONSTER_LEATHER_GEAR.get()
         || item == TensuraSmithingSchematicItems.GOLD_GEAR.get()
         || item == TensuraSmithingSchematicItems.IRON_GEAR.get()
         || item == TensuraSmithingSchematicItems.SILVER_GEAR.get()
         || item == TensuraSmithingSchematicItems.ANT_CARAPACE_GEAR.get()
         || item == TensuraSmithingSchematicItems.SERPENT_SCALEMAIL_GEAR.get()
         || item == TensuraSmithingSchematicItems.DIAMOND_GEAR.get()
         || item == TensuraSmithingSchematicItems.KNIGHT_SPIDER_CARAPACE_GEAR.get()
         || item == TensuraSmithingSchematicItems.LOW_MAGISTEEL_GEAR.get()
         || item == TensuraSmithingSchematicItems.ARMOURSAURUS_SCALEMAIL_GEAR.get()
         || item == TensuraSmithingSchematicItems.HIGH_MAGISTEEL_GEAR.get()
         || item == TensuraSmithingSchematicItems.CHARYBDIS_SCALEMAIL_GEAR.get()
         || item == TensuraSmithingSchematicItems.MITHRIL_GEAR.get()
         || item == TensuraSmithingSchematicItems.ORICHALCUM_GEAR.get()
         || item == TensuraSmithingSchematicItems.PURE_MAGISTEEL_GEAR.get()
         || item == TensuraSmithingSchematicItems.ADAMANTITE_GEAR.get()
         || item == TensuraSmithingSchematicItems.HIHIIROKANE_GEAR.get()
         || item == TensuraSmithingSchematicItems.ANTI_MAGIC_MASK.get()
         || item == TensuraSmithingSchematicItems.DARK_SET.get()
         || item == TensuraSmithingSchematicItems.PIERROT_MASK.get()
         || item == TensuraSmithingSchematicItems.SPATIAL_BLADE.get()
         || item == TensuraSmithingSchematicItems.WEB_GUN.get();
   }
}
