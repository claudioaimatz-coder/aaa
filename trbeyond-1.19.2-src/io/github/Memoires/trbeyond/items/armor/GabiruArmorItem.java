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

public class GabiruArmorItem extends BeyondArmorItem {
   public GabiruArmorItem(ArmorMaterial material, EquipmentSlot equipmentSlot, Rarity rarity) {
      super(material, equipmentSlot, new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).rarity(rarity));
   }

   public GabiruArmorItem(EquipmentSlot slot, GabiruArmorItem.Grade grade) {
      this(grade.getMaterial(), slot, grade.getRarity());
   }

   public static void applyEnchants(ItemStack toStack, ItemStack fromStack, EquipmentSlot toSlot) {
      Enchantment waterInfusion = (Enchantment)BeyondEnchantments.WATER_INFUSION.get();
      Enchantment tsukumogami = (Enchantment)TensuraEnchantments.TSUKUMOGAMI.get();
      if (toSlot.getType().equals(Type.ARMOR) && toStack.getItem() instanceof GabiruArmorItem gabiruArmorItem) {
         if (toStack.getEnchantmentLevel(tsukumogami) < 1) {
            EngravingEnchantment.engrave(toStack, tsukumogami, 1);
         }

         if (gabiruArmorItem.getRarity(fromStack).equals(GabiruArmorItem.Grade.GOD.getRarity()) && toStack.getEnchantmentLevel(waterInfusion) < 1) {
            EngravingEnchantment.engrave(toStack, waterInfusion, 1);
         }
      }
   }

   public static enum Grade {
      UNIQUE(BeyondArmourMaterials.GABIRU_UNIQUE, BeyondRarity.UNIQUE),
      LEGEND(BeyondArmourMaterials.GABIRU_LEGEND, BeyondRarity.LEGEND),
      GOD(BeyondArmourMaterials.GABIRU_GOD, BeyondRarity.GOD);

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
