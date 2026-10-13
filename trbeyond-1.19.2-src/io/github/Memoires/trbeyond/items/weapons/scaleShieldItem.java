package io.github.Memoires.trbeyond.items.weapons;

import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.item.templates.SimpleShieldItem;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class scaleShieldItem extends SimpleShieldItem {
   public scaleShieldItem(Properties pProperties) {
      super(pProperties);
   }

   public scaleShieldItem(scaleShieldItem.Grade grade) {
      this(new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).defaultDurability(1500).rarity(grade.getRarity()));
   }

   public static void applyEnchants(ItemStack toStack, ItemStack fromStack, EquipmentSlot toSlot) {
      Enchantment tsukumogami = (Enchantment)TensuraEnchantments.TSUKUMOGAMI.get();
      Enchantment magicInterference = (Enchantment)TensuraEnchantments.MAGIC_INTERFERENCE.get();
      if (toSlot.getType().equals(Type.HAND) && toStack.getItem() instanceof scaleShieldItem && toStack.getEnchantmentLevel(magicInterference) < 1) {
         EngravingEnchantment.engrave(toStack, tsukumogami, 1);
         EngravingEnchantment.engrave(toStack, magicInterference, 1);
      }
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
      ItemStack stack = pPlayer.getItemInHand(pHand);
      pPlayer.startUsingItem(pHand);
      return InteractionResultHolder.consume(stack);
   }

   public static enum Grade {
      UNIQUE(BeyondRarity.UNIQUE),
      LEGENDARY(BeyondRarity.LEGEND),
      GOD(BeyondRarity.GOD);

      private final Rarity rarity;

      public Rarity getRarity() {
         return this.rarity;
      }

      private Grade(Rarity rarity) {
         this.rarity = rarity;
      }
   }
}
