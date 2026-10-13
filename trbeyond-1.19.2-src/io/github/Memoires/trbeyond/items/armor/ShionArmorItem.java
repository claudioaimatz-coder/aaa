package io.github.Memoires.trbeyond.items.armor;

import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.Memoires.trbeyond.items.BeyondArmourMaterials;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import java.util.function.Predicate;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantment;

public class ShionArmorItem extends BeyondArmorItem {
   public ShionArmorItem(ArmorMaterial material, EquipmentSlot equipmentSlot, Rarity rarity) {
      super(material, equipmentSlot, new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).rarity(rarity));
   }

   public ShionArmorItem(EquipmentSlot slot, ShionArmorItem.Grade grade) {
      this(grade.getMaterial(), slot, grade.getRarity());
   }

   public static void applyBattleInfusion(ItemStack toStack, ItemStack fromStack, LivingEntity entity, EquipmentSlot toSlot) {
      if (!toSlot.getType().equals(Type.HAND)) {
         Enchantment enchantment = (Enchantment)BeyondEnchantments.BATTLE_INFUSION.get();
         Enchantment tsukumogami = (Enchantment)TensuraEnchantments.TSUKUMOGAMI.get();
         Predicate<ItemStack> isGodGrade = stackx -> stackx.getItem() instanceof ShionArmorItem armorItem
               ? armorItem.getMaterial() == BeyondArmourMaterials.SHION_GOD
               : false;
         if (BeyondArmorItem.hasFullSet(entity, isGodGrade, 4)) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
               if (!slot.getType().equals(Type.HAND)) {
                  ItemStack stack = entity.getItemBySlot(slot);
                  if (isGodGrade.test(stack) && stack.getEnchantmentLevel(enchantment) < 3) {
                     EngravingEnchantment.engrave(stack, enchantment, 1);
                  }
               }
            }
         }

         if (toStack.getItem() instanceof ShionArmorItem item) {
            if (toStack.getEnchantmentLevel(tsukumogami) < 1) {
               EngravingEnchantment.engrave(toStack, tsukumogami, 1);
            }

            if (item.getMaterial().equals(BeyondArmourMaterials.SHION_GOD) && toStack.getEnchantmentLevel(enchantment) < 2) {
               EngravingEnchantment.engrave(toStack, enchantment, 1);
            }
         }
      }
   }

   public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
      return (Multimap<Attribute, AttributeModifier>)(slot == EquipmentSlot.FEET ? ImmutableMultimap.of() : super.getDefaultAttributeModifiers(slot));
   }

   public boolean isDamageable(ItemStack stack) {
      return !this.slot.equals(EquipmentSlot.FEET);
   }

   public static enum Grade {
      UNIQUE(BeyondArmourMaterials.SHION_UNIQUE, BeyondRarity.UNIQUE),
      LEGEND(BeyondArmourMaterials.SHION_LEGEND, BeyondRarity.LEGEND),
      GOD(BeyondArmourMaterials.SHION_GOD, BeyondRarity.GOD),
      SPECIAL(BeyondArmourMaterials.SHION_SPECIAL, BeyondRarity.SPECIAL);

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
