package io.github.Memoires.trbeyond.items.armor;

import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import io.github.Memoires.trbeyond.items.BeyondArmourMaterials;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import java.util.function.Predicate;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantment;

public class MinosArmorItem extends BeyondArmorItem {
   public MinosArmorItem(ArmorMaterial material, EquipmentSlot equipmentSlot, Rarity rarity) {
      super(material, equipmentSlot, new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).rarity(rarity));
   }

   public MinosArmorItem(EquipmentSlot slot, MinosArmorItem.Grade grade) {
      this(grade.getMaterial(), slot, grade.getRarity());
   }

   public static void applyHolyCoat(ItemStack toStack, ItemStack fromStack, LivingEntity entity, EquipmentSlot toSlot) {
      Enchantment enchantment = (Enchantment)TensuraEnchantments.HOLY_COAT.get();
      Predicate<ItemStack> predicate = stackx -> stackx.getItem() instanceof MinosArmorItem;
      if (predicate.test(toStack) && toStack.getEnchantmentLevel(enchantment) < 2) {
         EngravingEnchantment.engrave(toStack, enchantment, 2);
      }

      if (!toSlot.getType().equals(Type.HAND)) {
         if (BeyondArmorItem.hasFullSet(entity, predicate, 4)) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
               if (!slot.getType().equals(Type.HAND)) {
                  ItemStack stack = entity.getItemBySlot(slot);
                  if (stack.getEnchantmentLevel(enchantment) < 3) {
                     EngravingEnchantment.engrave(stack, enchantment, 3);
                  }
               }
            }
         } else {
            if (fromStack.getItem() instanceof MinosArmorItem && fromStack.getEnchantmentLevel(enchantment) == 3) {
               EngravingEnchantment.engrave(fromStack, enchantment, 2);
            }

            for (EquipmentSlot slotx : EquipmentSlot.values()) {
               if (!slotx.getType().equals(Type.HAND)) {
                  ItemStack stack = entity.getItemBySlot(slotx);
                  if (stack.getEnchantmentLevel(enchantment) == 3) {
                     EngravingEnchantment.engrave(stack, enchantment, 2);
                  }
               }
            }
         }
      }
   }

   public static enum Grade {
      UNIQUE(BeyondArmourMaterials.MINOS_UNIQUE, BeyondRarity.UNIQUE),
      LEGEND(BeyondArmourMaterials.MINOS_LEGEND, BeyondRarity.LEGEND),
      GOD(BeyondArmourMaterials.MINOS_GOD, BeyondRarity.GOD);

      private final ArmorMaterial material;
      private final Rarity rarity;

      public ArmorMaterial getMaterial() {
         return this.material;
      }

      public Rarity getRarity() {
         return this.rarity;
      }

      private Grade(ArmorMaterial material, Rarity rarity) {
         this.material = material;
         this.rarity = rarity;
      }
   }
}
