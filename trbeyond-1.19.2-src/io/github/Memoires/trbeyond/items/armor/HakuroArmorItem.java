package io.github.Memoires.trbeyond.items.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.Memoires.trbeyond.items.BeyondArmourMaterials;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;

public class HakuroArmorItem extends BeyondArmorItem {
   public HakuroArmorItem(ArmorMaterial material, EquipmentSlot equipmentSlot, Rarity rarity) {
      super(material, equipmentSlot, new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).rarity(rarity));
   }

   public HakuroArmorItem(EquipmentSlot slot, HakuroArmorItem.Grade grade) {
      this(grade.getMaterial(), slot, grade.getRarity());
   }

   public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
      return (Multimap<Attribute, AttributeModifier>)(slot == EquipmentSlot.FEET ? ImmutableMultimap.of() : super.getDefaultAttributeModifiers(slot));
   }

   public boolean isDamageable(ItemStack stack) {
      return !this.slot.equals(EquipmentSlot.FEET);
   }

   public static enum Grade {
      UNIQUE(BeyondArmourMaterials.HAKURO_UNIQUE, BeyondRarity.UNIQUE),
      LEGEND(BeyondArmourMaterials.HAKURO_LEGEND, BeyondRarity.LEGEND),
      SPECIAL(BeyondArmourMaterials.HAKURO_SPECIAL, BeyondRarity.SPECIAL);

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
