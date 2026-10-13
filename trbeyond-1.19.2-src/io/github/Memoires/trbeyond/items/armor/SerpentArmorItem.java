package io.github.Memoires.trbeyond.items.armor;

import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import io.github.Memoires.trbeyond.items.BeyondArmourMaterials;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantment;

public class SerpentArmorItem extends BeyondArmorItem {
   public SerpentArmorItem(ArmorMaterial material, EquipmentSlot equipmentSlot, Rarity rarity) {
      super(material, equipmentSlot, new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).rarity(rarity));
   }

   public SerpentArmorItem(EquipmentSlot slot, SerpentArmorItem.Grade grade) {
      this(grade.getMaterial(), slot, grade.getRarity());
   }

   public boolean isFoil(ItemStack stack) {
      if (!stack.isEnchanted()) {
         return false;
      } else {
         Set<Enchantment> enchantments = stack.getAllEnchantments().keySet();
         return enchantments.size() != 1 || stack.getEnchantmentLevel((Enchantment)TensuraEnchantments.STURDY.get()) <= 0;
      }
   }

   public static void applySturdy(ItemStack toStack, ItemStack fromStack, LivingEntity entity, EquipmentSlot toSlot) {
      Enchantment enchantment = (Enchantment)TensuraEnchantments.STURDY.get();
      Predicate<ItemStack> predicate = stackx -> stackx.getItem() instanceof SerpentArmorItem;
      if (predicate.test(toStack) && toStack.getEnchantmentLevel(enchantment) < 1) {
         EngravingEnchantment.engrave(toStack, enchantment, 1);
      }

      if (!toSlot.getType().equals(Type.HAND)) {
         if (BeyondArmorItem.hasFullSet(entity, predicate, 4)) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
               if (!slot.getType().equals(Type.HAND)) {
                  ItemStack stack = entity.getItemBySlot(slot);
                  if (stack.getEnchantmentLevel(enchantment) < 2) {
                     EngravingEnchantment.engrave(stack, enchantment, 2);
                  }
               }
            }
         } else {
            if (fromStack.getItem() instanceof SerpentArmorItem && fromStack.getEnchantmentLevel(enchantment) == 2) {
               EngravingEnchantment.engrave(fromStack, enchantment, 1);
            }

            for (EquipmentSlot slotx : EquipmentSlot.values()) {
               if (!slotx.getType().equals(Type.HAND)) {
                  ItemStack stack = entity.getItemBySlot(slotx);
                  if (stack.getEnchantmentLevel(enchantment) == 2) {
                     EngravingEnchantment.engrave(stack, enchantment, 1);
                  }
               }
            }
         }
      }
   }

   public static enum Grade {
      UNIQUE(BeyondArmourMaterials.SERPENT_UNIQUE, BeyondRarity.UNIQUE),
      LEGEND(BeyondArmourMaterials.SERPENT_LEGEND, BeyondRarity.LEGEND),
      GOD(BeyondArmourMaterials.SERPENT_GOD, BeyondRarity.GOD);

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
