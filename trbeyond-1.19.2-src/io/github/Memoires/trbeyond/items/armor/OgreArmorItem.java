package io.github.Memoires.trbeyond.items.armor;

import com.github.manasmods.manascore.api.attribute.AttributeModifierHelper;
import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import io.github.Memoires.trbeyond.items.BeyondArmourMaterials;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.items.weapons.OgreAxeItem;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import io.github.Memoires.trbeyond.registry.items.BeyondToolItems;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import java.util.UUID;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantment;

public class OgreArmorItem extends BeyondArmorItem {
   public static final UUID ATTACK_UUID = UUID.fromString("03d8a37a-61c6-4ed8-a4bf-0afeb6aa3383");

   public OgreArmorItem(ArmorMaterial material, EquipmentSlot equipmentSlot, Rarity rarity) {
      super(material, equipmentSlot, new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).rarity(rarity));
   }

   public OgreArmorItem(EquipmentSlot slot, OgreArmorItem.Grade grade) {
      this(grade.getMaterial(), slot, grade.getRarity());
   }

   public static void applyEffects(ItemStack toStack, ItemStack fromStack, LivingEntity entity, EquipmentSlot toSlot) {
      if ((toStack.is((Item)BeyondToolItems.OGRE_AXE_GOD.get()) || toStack.getItem() instanceof OgreArmorItem && toStack.getRarity() == BeyondRarity.GOD)
         && toStack.getEnchantmentLevel((Enchantment)BeyondEnchantments.FIRE_INFUSION.get()) < 1) {
         EngravingEnchantment.engrave(toStack, (Enchantment)BeyondEnchantments.FIRE_INFUSION.get(), 1);
      }

      if (BeyondArmorItem.hasFullSet(entity, stackx -> stackx.getItem() instanceof OgreArmorItem || stackx.getItem() instanceof OgreAxeItem, 4, true)) {
         AttributeModifierHelper.setModifier(entity, Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_UUID, "Ogre Boost", 15.0, Operation.ADDITION));
      } else {
         AttributeModifierHelper.removeModifier(entity, Attributes.ATTACK_DAMAGE, ATTACK_UUID);
      }

      if (!toSlot.getType().equals(Type.HAND)) {
         Enchantment enchantment = (Enchantment)TensuraEnchantments.MAGIC_INTERFERENCE.get();
         if (BeyondArmorItem.hasFullSet(entity, stackx -> stackx.getItem() instanceof OgreArmorItem, 3)) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
               if (!slot.getType().equals(Type.HAND)) {
                  ItemStack stack = entity.getItemBySlot(slot);
                  if (stack.getEnchantmentLevel(enchantment) < 1) {
                     EngravingEnchantment.engrave(stack, enchantment, 1);
                  }
               }
            }
         } else {
            if (fromStack.getItem() instanceof OgreArmorItem && fromStack.getEnchantmentLevel(enchantment) == 1) {
               EngravingEnchantment.engrave(fromStack, enchantment, 0);
            }

            for (EquipmentSlot slotx : EquipmentSlot.values()) {
               if (!slotx.getType().equals(Type.HAND)) {
                  ItemStack stack = entity.getItemBySlot(slotx);
                  if (stack.getEnchantmentLevel(enchantment) == 1 && stack.getItem() instanceof OgreArmorItem) {
                     EngravingEnchantment.engrave(stack, enchantment, 0);
                  }
               }
            }
         }
      }
   }

   public static enum Grade {
      RARE(BeyondArmourMaterials.OGRE_RARE, BeyondRarity.RARE),
      UNIQUE(BeyondArmourMaterials.OGRE_UNIQUE, BeyondRarity.UNIQUE),
      LEGEND(BeyondArmourMaterials.OGRE_LEGEND, BeyondRarity.LEGEND),
      GOD(BeyondArmourMaterials.OGRE_GOD, BeyondRarity.GOD);

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
