package io.github.Memoires.trbeyond.items.armor;

import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import io.github.Memoires.trbeyond.items.BeyondArmourMaterials;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantment;

public class GeldArmorItem extends BeyondArmorItem {
   public GeldArmorItem(ArmorMaterial material, EquipmentSlot equipmentSlot, Rarity rarity) {
      super(material, equipmentSlot, new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).rarity(rarity));
   }

   public GeldArmorItem(EquipmentSlot slot, GeldArmorItem.Grade grade) {
      this(grade.getMaterial(), slot, grade.getRarity());
   }

   public static void applyEnchants(ItemStack toStack, ItemStack fromStack, EquipmentSlot toSlot) {
      Enchantment tsukumogami = (Enchantment)TensuraEnchantments.TSUKUMOGAMI.get();
      Enchantment earthInfusion = (Enchantment)BeyondEnchantments.EARTH_INFUSION.get();
      if (toSlot.getType().equals(Type.ARMOR) && toStack.getItem() instanceof GeldArmorItem geldArmorItem) {
         if (toStack.getEnchantmentLevel(tsukumogami) < 1) {
            EngravingEnchantment.engrave(toStack, tsukumogami, 1);
         }

         if (geldArmorItem.getRarity(fromStack).equals(GeldArmorItem.Grade.GOD.getRarity()) && toStack.getEnchantmentLevel(earthInfusion) < 1) {
            EngravingEnchantment.engrave(toStack, earthInfusion, 1);
         }
      }
   }

   public static enum Grade {
      UNIQUE(BeyondArmourMaterials.GELD_UNIQUE, BeyondRarity.UNIQUE),
      LEGEND(BeyondArmourMaterials.GELD_LEGEND, BeyondRarity.LEGEND),
      GOD(BeyondArmourMaterials.GELD_GOD, BeyondRarity.GOD);

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
