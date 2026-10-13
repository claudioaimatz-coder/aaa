package io.github.Memoires.trbeyond.items.weapons;

import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.item.TensuraToolTiers;
import com.github.manasmods.tensura.item.templates.custom.TensuraLongSword;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import java.util.UUID;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

public class ShibkoboItem extends TensuraLongSword {
   public ShibkoboItem(ShibkoboItem.Grade grade) {
      this(
         grade.getMaterial(),
         grade.getDamage(),
         -3.0F,
         2.0,
         0.0,
         0.0,
         100.0,
         new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).rarity(grade.getRarity())
      );
   }

   public ShibkoboItem(
      Tier pTier,
      int pAttackDamageModifier,
      float pAttackSpeedModifier,
      double attackRangeModifier,
      double critChance,
      double critDamageMultiplier,
      double sweepChance,
      Properties pProperties
   ) {
      super(pTier, pAttackDamageModifier, pAttackSpeedModifier, attackRangeModifier, critChance, critDamageMultiplier, sweepChance, pProperties);
   }

   public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (stack.getItem().getShareTag(stack).getFloat("delay") >= 2.0F) {
         if (player.isShiftKeyDown()) {
            stack.getItem()
               .getShareTag(stack)
               .putFloat("attack_range", stack.getTag().getFloat("attack_range") <= 0.0F ? 0.0F : stack.getTag().getFloat("attack_range") - 1.0F);
         } else {
            stack.getItem()
               .getShareTag(stack)
               .putFloat("attack_range", stack.getTag().getFloat("attack_range") >= 5.0F ? 5.0F : stack.getTag().getFloat("attack_range") + 1.0F);
         }

         stack.getItem().getShareTag(stack).putFloat("delay", 0.0F);
         return InteractionResultHolder.consume(stack);
      } else {
         stack.getItem().getShareTag(stack).putFloat("delay", stack.getItem().getShareTag(stack).getFloat("delay") + 1.0F);
         return InteractionResultHolder.pass(stack);
      }
   }

   public static void applyEnchants(ItemStack toStack, ItemStack fromStack, EquipmentSlot toSlot) {
      Enchantment tsukumogami = (Enchantment)TensuraEnchantments.TSUKUMOGAMI.get();
      Enchantment windInfusion = (Enchantment)BeyondEnchantments.WIND_INFUSION.get();
      if (toSlot.getType().equals(Type.HAND) && toStack.getItem() instanceof ShibkoboItem shibkoboItem) {
         if (toStack.getEnchantmentLevel(tsukumogami) < 1) {
            EngravingEnchantment.engrave(toStack, tsukumogami, 1);
         }

         if (shibkoboItem.getRarity(fromStack).equals(MeatCleaverItem.Grade.GOD.getRarity()) && toStack.getEnchantmentLevel(windInfusion) < 1) {
            EngravingEnchantment.engrave(toStack, windInfusion, 1);
         }
      }
   }

   public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
      Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create(super.getAttributeModifiers(EquipmentSlot.MAINHAND, stack));
      double attackRange = 0.0;
      double damage = 0.0;
      AttributeModifier old_damage = (AttributeModifier)modifiers.get(Attributes.ATTACK_DAMAGE).toArray()[0];
      attackRange = (double)stack.getTag().getFloat("attack_range");
      damage = Math.floor(old_damage.getAmount() * -0.01 * (double)(stack.getTag().getFloat("attack_range") * 10.0F));
      modifiers.put(
         (Attribute)ForgeMod.ATTACK_RANGE.get(),
         new AttributeModifier(UUID.fromString("f33c609e-1fcc-45f2-b4a8-1f787f2774ce"), "Weapon modifier", attackRange, Operation.ADDITION)
      );
      modifiers.put(
         Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("f33c609e-1fcc-45f2-b4a8-1f787f2774ce"), "Weapon modifier", damage, Operation.ADDITION)
      );
      return (Multimap<Attribute, AttributeModifier>)(slot != EquipmentSlot.MAINHAND ? ImmutableMultimap.of() : modifiers);
   }

   public static enum Grade {
      UNIQUE(TensuraToolTiers.PURE_MAGISTEEL, 4, BeyondRarity.UNIQUE),
      LEGENDARY(TensuraToolTiers.ADAMANTITE, 3, BeyondRarity.LEGEND),
      GOD(TensuraToolTiers.HIHIIROKANE, 1, BeyondRarity.GOD);

      private final Tier material;
      private final int damage;
      private final Rarity rarity;

      public Tier getMaterial() {
         return this.material;
      }

      public int getDamage() {
         return this.damage;
      }

      public Rarity getRarity() {
         return this.rarity;
      }

      private Grade(Tier material, int damage, Rarity rarity) {
         this.material = material;
         this.damage = damage;
         this.rarity = rarity;
      }
   }
}
