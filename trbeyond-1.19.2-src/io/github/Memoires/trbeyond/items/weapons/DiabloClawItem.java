package io.github.Memoires.trbeyond.items.weapons;

import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.item.TensuraToolTiers;
import com.github.manasmods.tensura.item.templates.custom.SimpleShortSwordItem;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class DiabloClawItem extends SimpleShortSwordItem {
   public static final int COOLDOWN = 40;
   public static final int MAX_USE_TIME = 40;
   public int impactOffset = 0;
   public boolean hasReflected = false;

   public DiabloClawItem(DiabloClawItem.Grade grade) {
      super(
         grade.getMaterial(),
         grade.getDamage(),
         -2.0F,
         0.0,
         20.0,
         0.75,
         new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).defaultDurability(150).rarity(grade.getRarity())
      );
   }

   public static void applyEnchants(ItemStack toStack, ItemStack fromStack, EquipmentSlot toSlot) {
      Enchantment tsukumogami = (Enchantment)TensuraEnchantments.TSUKUMOGAMI.get();
      Enchantment darkInfusion = (Enchantment)BeyondEnchantments.DARKNESS_INFUSION.get();
      if (toSlot.getType().equals(Type.HAND) && toStack.getItem() instanceof DiabloClawItem diabloClawItem) {
         if (toStack.getEnchantmentLevel(tsukumogami) < 1) {
            EngravingEnchantment.engrave(toStack, tsukumogami, 1);
         }

         if (diabloClawItem.getRarity(fromStack).equals(DiabloClawItem.Grade.GOD.getRarity()) && toStack.getEnchantmentLevel(darkInfusion) < 1) {
            EngravingEnchantment.engrave(toStack, darkInfusion, 1);
         }
      }
   }

   public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
      ItemStack itemstack = pPlayer.getItemInHand(pHand);
      pPlayer.startUsingItem(pHand);
      return InteractionResultHolder.consume(itemstack);
   }

   public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
      this.hasReflected = false;
   }

   public int getUseDuration(ItemStack pStack) {
      return 72000;
   }

   @Override
   public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
      return ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
   }

   public static enum Grade {
      LEGEND(TensuraToolTiers.ADAMANTITE, 12, BeyondRarity.LEGEND),
      GOD(TensuraToolTiers.HIHIIROKANE, 13, BeyondRarity.GOD);

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
