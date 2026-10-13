package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.entity.template.HumanoidNPCEntity;
import io.github.Memoires.trbeyond.client.screen.TestScreen;
import io.github.Memoires.trbeyond.registry.items.BeyondItems;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class GuildTesterEntity extends HumanoidNPCEntity implements IAnimatable {
   private AnimationFactory factory = GeckoLibUtil.createFactory(this);

   public GuildTesterEntity(EntityType<? extends HumanoidNPCEntity> p_35381_, Level p_35382_) {
      super(p_35381_, p_35382_);
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      if (this.isAlive() && !this.isSleeping() && !player.isSecondaryUseActive()) {
         if (player.getInventory().contains(((Item)BeyondItems.ID_CARD.get()).getDefaultInstance())) {
            if (player.level.isClientSide) {
               this.openTestScreen(player, this.getCustomName());
            }

            return InteractionResult.sidedSuccess(player.level.isClientSide);
         } else {
            return InteractionResult.PASS;
         }
      } else {
         return super.mobInteract(player, hand);
      }
   }

   @OnlyIn(Dist.CLIENT)
   private void openTestScreen(Player player, Component title) {
      Minecraft.getInstance().setScreen(new TestScreen(title));
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 5.0F));
      this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
   }

   public static AttributeSupplier setAttributes() {
      return Mob.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 48.0).add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.KNOCKBACK_RESISTANCE, 1.0).build();
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

   @Override
   public boolean hurt(DamageSource source, float p_35511_) {
      return !source.isBypassInvul() ? false : super.hurt(source, p_35511_);
   }

   public boolean isPushable() {
      return false;
   }

   @Override
   public void checkDespawn() {
   }

   public void registerControllers(AnimationData data) {
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }
}
