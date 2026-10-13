package io.github.Memoires.trbeyond.items.custom;

import io.github.Memoires.trbeyond.entity.custom.FireBreathEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow.Pickup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public class FireBreathItem extends Item {
   public FireBreathItem() {
      super(new Properties().tab(null).durability(100));
   }

   public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
      entity.startUsingItem(hand);
      return new InteractionResultHolder(InteractionResult.SUCCESS, entity.getItemInHand(hand));
   }

   public UseAnim getUseAnimation(ItemStack itemstack) {
      return UseAnim.NONE;
   }

   public int getUseDuration(ItemStack itemstack) {
      return 72000;
   }

   public void releaseUsing(ItemStack itemstack, Level world, LivingEntity entityLiving, int timeLeft) {
      if (!world.isClientSide() && entityLiving instanceof ServerPlayer entity) {
         double x = entity.getX();
         double y = entity.getY();
         double z = entity.getZ();
         FireBreathEntity entityarrow = FireBreathEntity.shoot(world, entity, world.getRandom(), 1.0F, 1.0, 1);
         itemstack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(entity.getUsedItemHand()));
         entityarrow.pickup = Pickup.DISALLOWED;
      }
   }
}
