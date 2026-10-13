package io.github.Memoires.trbeyond.enchantment;

import com.github.manasmods.manascore.api.attribute.AttributeModifierHelper;
import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.enchantment.IInherentEngrave;
import java.util.UUID;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;

public class InfusionEnchantment extends EngravingEnchantment implements IInherentEngrave {
   public InfusionEnchantment(EnchantmentCategory pCategory) {
      super(Rarity.VERY_RARE, pCategory, EquipmentSlot.values());
   }

   public int getMaxLevel() {
      return 1;
   }

   public static boolean canGainInfusion(ItemStack stack) {
      return stack.getTag() == null ? false : stack.getTag().getDouble("EP") >= 1000000.0;
   }

   public boolean canActivate(ItemStack stack) {
      return canGainInfusion(stack);
   }

   public static boolean hasFullSetEffect(LivingEntity entity, Enchantment enchantment) {
      return hasFullSetEffect(entity, enchantment, 2);
   }

   public static boolean hasFullSetEffect(LivingEntity entity, Enchantment enchantment, int number) {
      int i = 0;

      for (EquipmentSlot slot : EquipmentSlot.values()) {
         if (!slot.getType().equals(Type.HAND)) {
            if (entity.getItemBySlot(slot).getEnchantmentLevel(enchantment) >= 1) {
               i++;
            }

            if (i >= number) {
               return true;
            }
         }
      }

      return false;
   }

   @Override
   public boolean shouldHasFoil(ItemStack stack) {
      return false;
   }

   public boolean canDuplicate() {
      return false;
   }

   public static void addInfusionAttribute(LivingEquipmentChangeEvent event, Enchantment enchantment, Attribute attribute, UUID uuid, float amount) {
      LivingEntity entity = event.getEntity();
      int oldLevel = event.getTo().getEnchantmentLevel(enchantment);
      int newLevel = event.getFrom().getEnchantmentLevel(enchantment);
      if (newLevel != oldLevel) {
         int level = getEnchantmentLevel(enchantment, entity);
         AttributeModifierHelper.removeModifier(entity, attribute, uuid);
         if (level > 0) {
            AttributeModifierHelper.setModifier(
               entity, attribute, new AttributeModifier(uuid, "Infusion Attributes", (double)(amount * (float)level), Operation.ADDITION)
            );
         }
      }
   }

   public static int getEnchantmentLevel(Enchantment enchantment, LivingEntity entity) {
      int i = 0;

      for (EquipmentSlot slot : EquipmentSlot.values()) {
         if (!slot.getType().equals(Type.HAND)) {
            i += entity.getItemBySlot(slot).getEnchantmentLevel(enchantment);
         }
      }

      return i;
   }
}
