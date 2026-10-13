package io.github.Memoires.trbeyond.items.armor;

import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.Memoires.trbeyond.items.BeyondArmourMaterials;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantment;

public class DiabloArmorItem extends BeyondArmorItem {
   public DiabloArmorItem(ArmorMaterial material, EquipmentSlot equipmentSlot, Rarity rarity) {
      super(material, equipmentSlot, new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).rarity(rarity));
   }

   public DiabloArmorItem(EquipmentSlot slot, DiabloArmorItem.Grade grade) {
      this(grade.getMaterial(), slot, grade.getRarity());
   }

   public DiabloArmorItem(EquipmentSlot slot, DiabloArmorItem.Grade grade, Properties properties) {
      super(grade.getMaterial(), slot, properties.rarity(grade.getRarity()));
   }

   public static void applyEnchants(ItemStack toStack, ItemStack fromStack, EquipmentSlot toSlot) {
      Enchantment darknessInfusion = (Enchantment)BeyondEnchantments.DARKNESS_INFUSION.get();
      if (toSlot.getType().equals(Type.ARMOR)
         && toStack.getItem() instanceof DiabloArmorItem diabloArmorItem
         && diabloArmorItem.getRarity(fromStack).equals(DiabloArmorItem.Grade.GOD.getRarity())
         && toStack.getEnchantmentLevel(darknessInfusion) < 1) {
         EngravingEnchantment.engrave(toStack, darknessInfusion, 1);
      }
   }

   public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
      return (Multimap<Attribute, AttributeModifier>)(slot == EquipmentSlot.FEET ? ImmutableMultimap.of() : super.getDefaultAttributeModifiers(slot));
   }

   public boolean isDamageable(ItemStack stack) {
      return !this.slot.equals(EquipmentSlot.FEET);
   }

   public static enum Grade {
      LEGEND(BeyondArmourMaterials.DIABLO_LEGEND, BeyondRarity.LEGEND),
      GOD(BeyondArmourMaterials.DIABLO_GOD, BeyondRarity.GOD),
      SPECIAL(BeyondArmourMaterials.DIABLO_SPECIAL, BeyondRarity.SPECIAL);

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
