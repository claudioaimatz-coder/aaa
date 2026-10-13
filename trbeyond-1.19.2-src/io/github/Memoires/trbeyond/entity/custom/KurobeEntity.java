package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.entity.template.HumanoidNPCEntity;
import io.github.Memoires.trbeyond.client.screen.KurobeMainMenu;
import io.github.Memoires.trbeyond.client.screen.KurobeMainMenuScreen;
import io.github.Memoires.trbeyond.client.screen.KurobeShopMenu;
import io.github.Memoires.trbeyond.client.screen.KurobeShopScreen;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.network.ClientConfigData;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import io.github.Memoires.trbeyond.registry.menu.BeyondMenus;
import java.util.List;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class KurobeEntity extends HumanoidNPCEntity implements IAnimatable {
   private AnimationFactory factory = GeckoLibUtil.createFactory(this);

   public KurobeEntity(EntityType<? extends HumanoidNPCEntity> p_35381_, Level p_35382_) {
      super(p_35381_, p_35382_);
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      if (this.isAlive() && !this.isSleeping() && !player.isSecondaryUseActive()) {
         if (!((List)ServerConfig.INSTANCE.npcShopConfigs.get())
               .contains(ForgeRegistries.ENTITY_TYPES.getKey((EntityType)BeyondEntities.KUROBE.get()).toString())
            && player.level.isClientSide) {
            this.openKurobeShopScreen(player);
         }

         return InteractionResult.sidedSuccess(player.level.isClientSide);
      } else {
         return super.mobInteract(player, hand);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void openKurobeShopScreen(Player player) {
      if (ClientConfigData.KORUOBEE_SKILL_ENABLED) {
         Minecraft.getInstance()
            .setScreen(
               new KurobeMainMenuScreen(
                  (KurobeMainMenu)((MenuType)BeyondMenus.KUROBE_MAIN_MENU.get()).create(1, player.getInventory()), player.getInventory(), player.getDisplayName()
               )
            );
      } else {
         Minecraft.getInstance()
            .setScreen(
               new KurobeShopScreen(
                  (KurobeShopMenu)((MenuType)BeyondMenus.KUROBE_MENU.get()).create(1, player.getInventory()), player.getInventory(), player.getDisplayName()
               )
            );
      }
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 5.0F));
      this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
   }

   public static AttributeSupplier setAttributes() {
      return Mob.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 48.0).add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.KNOCKBACK_RESISTANCE, 1.0).build();
   }

   public void registerControllers(AnimationData animationData) {
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

   public AnimationFactory getFactory() {
      return this.factory;
   }
}
