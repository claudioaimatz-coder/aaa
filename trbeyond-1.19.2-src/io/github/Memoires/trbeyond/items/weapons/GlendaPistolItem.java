package io.github.Memoires.trbeyond.items.weapons;

import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.Memoires.trbeyond.entity.projectile.GlendaBulletProjectile;
import io.github.Memoires.trbeyond.registry.items.BeyondItems;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import java.util.Random;
import java.util.Set;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class GlendaPistolItem extends CrossbowItem {
   private final Random random = new Random();
   private static final Set<Enchantment> AcceptableEnchantments = Set.of(Enchantments.POWER_ARROWS, Enchantments.UNBREAKING, Enchantments.MENDING);

   public GlendaPistolItem(Properties pProperties) {
      super(pProperties);
   }

   public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
      return AcceptableEnchantments.contains(enchantment);
   }

   @NotNull
   public UseAnim getUseAnimation(@NotNull ItemStack pStack) {
      return UseAnim.CROSSBOW;
   }

   public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
      super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
      if (!pStack.hasTag() || !pStack.getTag().getBoolean("Charged")) {
         pStack.getOrCreateTag().putBoolean("Charged", true);
      }
   }

   public int getUseDuration(ItemStack pStack) {
      return 1;
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (player.getCooldowns().isOnCooldown(this)) {
         InteractionResultHolder.fail(stack);
      }

      boolean shot = this.attemptShoot(stack, level, player);
      player.startUsingItem(hand);
      if (shot) {
         player.getCooldowns().addCooldown(this, 4);
         return InteractionResultHolder.consume(stack);
      } else {
         return InteractionResultHolder.fail(stack);
      }
   }

   public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pCount) {
   }

   public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
   }

   private boolean attemptShoot(ItemStack pStack, Level level, LivingEntity entity) {
      boolean canShoot = false;
      ItemStack ammoStack = null;
      Player playerEntity = null;
      if (entity instanceof Player player) {
         if (player.getAbilities().instabuild) {
            canShoot = true;
         } else if (player.getInventory().hasAnyOf(Set.of((Item)BeyondItems.MAGISTEEL_BULLET.get()))) {
            canShoot = true;
            playerEntity = player;
            ammoStack = player.getInventory().getItem(player.getInventory().findSlotMatchingItem(new ItemStack((ItemLike)BeyondItems.MAGISTEEL_BULLET.get())));
         }
      } else {
         canShoot = true;
      }

      if (canShoot) {
         InteractionHand hand = entity.getUsedItemHand();
         SoundSource soundSource = playerEntity != null ? SoundSource.PLAYERS : SoundSource.HOSTILE;
         boolean left = hand == InteractionHand.OFF_HAND && entity.getMainArm() == HumanoidArm.RIGHT
            || hand == InteractionHand.MAIN_HAND && entity.getMainArm() == HumanoidArm.LEFT;
         level.playSound(
            (Player)null,
            entity.getX(),
            entity.getY(),
            entity.getZ(),
            SoundEvents.BLAZE_SHOOT,
            soundSource,
            0.5F,
            0.4F + this.random.nextFloat() * 0.4F + 0.8F
         );
         GlendaBulletProjectile bullet = new GlendaBulletProjectile(level, entity, !left);
         bullet.setDamage(30.0F + 5.0F * (float)pStack.getEnchantmentLevel(Enchantments.POWER_ARROWS));
         bullet.setDamageSource(TensuraDamageSources.shot(bullet, entity).setProjectile());
         bullet.setSpeed(5.0F);
         bullet.shootFromRot(entity.getLookAngle());
         level.addFreshEntity(bullet);
         level.playSound(
            (Player)null,
            entity.getX(),
            entity.getY(),
            entity.getZ(),
            (SoundEvent)BeyondSoundEvents.GUN_SHOT.get(),
            soundSource,
            1.0F,
            1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F
         );
         if (playerEntity != null) {
            playerEntity.awardStat(Stats.ITEM_USED.get(this));
            pStack.hurtAndBreak(1, playerEntity, playerx -> playerx.broadcastBreakEvent(hand));
         }

         if (ammoStack != null) {
            ammoStack.shrink(1);
            if (ammoStack.isEmpty()) {
               playerEntity.getInventory().removeItem(ammoStack);
            }
         }
      }

      return canShoot;
   }
}
