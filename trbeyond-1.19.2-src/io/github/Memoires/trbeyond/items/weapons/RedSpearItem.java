package io.github.Memoires.trbeyond.items.weapons;

import com.github.manasmods.tensura.entity.magic.projectile.FireBallProjectile;
import com.github.manasmods.tensura.item.TensuraToolTiers;
import com.github.manasmods.tensura.item.templates.custom.TensuraLongSword;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class RedSpearItem extends TensuraLongSword {
   public RedSpearItem(RedSpearItem.Grade grade) {
      this(
         grade.getMaterial(),
         grade.getDamage(),
         -3.0F,
         2.0,
         0.0,
         0.0,
         100.0,
         new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).rarity(grade.getRarity())
      );
   }

   public RedSpearItem(
      Tier pTier,
      int pAttackDamageModifier,
      float pAttackSpeedModifier,
      double attackRangeModifier,
      double critChance,
      double critDamageMultiplier,
      double sweepChance,
      Properties pProperties
   ) {
      super(pTier, pAttackDamageModifier, pAttackSpeedModifier, attackRangeModifier, critChance, critDamageMultiplier, sweepChance, pProperties);
   }

   @NotNull
   public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
      ItemStack itemstack = pPlayer.getItemInHand(pHand);
      pPlayer.startUsingItem(pHand);
      return InteractionResultHolder.consume(itemstack);
   }

   public void fireBolt(Player player) {
      FireBallProjectile fireBall = new FireBallProjectile(player.getLevel(), player);
      fireBall.setBurnTicks(10);
      fireBall.setSpeed(1.2F);
      fireBall.setDamage(40.0F);
      fireBall.setSpiritAttack(true);
      fireBall.setNoGravity(true);
      fireBall.setPosAndShoot(player);
      player.getLevel().addFreshEntity(fireBall);
      player.getLevel().playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
   }

   public UseAnim getUseAnimation(ItemStack pStack) {
      return UseAnim.SPEAR;
   }

   public void releaseUsing(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity entity, int pTimeLeft) {
      if (entity instanceof Player player) {
         int useTicks = this.getUseDuration(pStack) - pTimeLeft;
         if (useTicks >= 7) {
            CompoundTag tag = pStack.getOrCreateTag();
            double EP = tag.getDouble("DurabilityEP");
            int multiShot = pStack.getEnchantmentLevel(Enchantments.MULTISHOT);
            double cost = 150.0;
            if (EP < cost) {
               pLevel.playSound((Player)null, player, SoundEvents.PLAYER_ATTACK_WEAK, SoundSource.PLAYERS, 1.0F, 1.0F);
            } else {
               if (!pLevel.isClientSide) {
                  this.fireBolt(player);
                  pLevel.playSound((Player)null, player, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.0F);
                  if (!player.getAbilities().instabuild) {
                     tag.putDouble("DurabilityEP", EP - cost);
                  }
               }

               player.awardStat(Stats.ITEM_USED.get(this));
               player.swing(player.getUsedItemHand(), true);
            }
         }
      }
   }

   public int getUseDuration(ItemStack pStack) {
      return 72000;
   }

   public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
      boolean hurt = super.hurtEnemy(pStack, pTarget, pAttacker);
      if (!hurt) {
         return false;
      } else {
         pTarget.playSound(SoundEvents.PLAYER_HURT_ON_FIRE, 1.0F, 1.0F);
         if (pTarget.getTicksFrozen() < 350) {
            pTarget.setRemainingFireTicks(20);
         } else {
            pTarget.setRemainingFireTicks(pTarget.getRemainingFireTicks() + 20);
         }

         return true;
      }
   }

   public static enum Grade {
      UNIQUE(TensuraToolTiers.PURE_MAGISTEEL, 13, BeyondRarity.UNIQUE),
      LEGENDARY(TensuraToolTiers.ADAMANTITE, 5, BeyondRarity.LEGEND);

      private final Tier material;
      private final int damage;
      private final Rarity rarity;

      public Tier getMaterial() {
         return this.material;
      }

      public int getDamage() {
         return this.damage;
      }

      public Rarity getRarity() {
         return this.rarity;
      }

      private Grade(Tier material, int damage, Rarity rarity) {
         this.material = material;
         this.damage = damage;
         this.rarity = rarity;
      }
   }
}
