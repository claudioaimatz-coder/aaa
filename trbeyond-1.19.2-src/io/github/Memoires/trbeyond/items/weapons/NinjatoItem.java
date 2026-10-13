package io.github.Memoires.trbeyond.items.weapons;

import com.github.manasmods.manascore.attribute.ManasCoreAttributes;
import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.item.TensuraToolTiers;
import com.github.manasmods.tensura.item.templates.custom.TensuraLongSword;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NinjatoItem extends TensuraLongSword {
   private LivingEntity user;

   public NinjatoItem(
      Tier pTier,
      int pAttackDamageModifier,
      float pAttackSpeedModifier,
      double attackRangeModifier,
      double critChance,
      double critDamageMultiplier,
      Properties pProperties
   ) {
      super(pTier, pAttackDamageModifier, pAttackSpeedModifier, attackRangeModifier, critChance, critDamageMultiplier, pProperties);
   }

   public NinjatoItem(NinjatoItem.Grade grade) {
      this(
         grade.material,
         grade.getDamage(),
         -2.5F,
         0.0,
         20.0,
         0.5,
         new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).defaultDurability(150).rarity(grade.getRarity())
      );
   }

   public static void applyEnchants(ItemStack toStack, ItemStack fromStack, EquipmentSlot toSlot) {
      Enchantment tsukumogami = (Enchantment)TensuraEnchantments.TSUKUMOGAMI.get();
      if (toSlot.getType().equals(Type.HAND) && toStack.getItem() instanceof NinjatoItem ninjatoItem && toStack.getEnchantmentLevel(tsukumogami) < 1) {
         EngravingEnchantment.engrave(toStack, tsukumogami, 1);
      }
   }

   public static boolean isDualWieldingNinjatos(LivingEntity player) {
      ItemStack mainHand = player.getMainHandItem();
      ItemStack offHand = player.getOffhandItem();
      return mainHand.getItem() instanceof NinjatoItem && offHand.getItem() instanceof NinjatoItem;
   }

   public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
      pTooltipComponents.add(Component.literal("§9Buffed stats when used in both hands."));
      super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
   }

   public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
      Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create(super.getAttributeModifiers(EquipmentSlot.MAINHAND, stack));
      double attackSpeed = 0.0;
      double critChance = 0.0;
      if (this.user != null && isDualWieldingNinjatos(this.user)) {
         attackSpeed += 2.0;
         critChance += 40.0;
      }

      modifiers.put(
         Attributes.ATTACK_SPEED,
         new AttributeModifier(UUID.fromString("abed3f8e-3189-4fc6-b696-c41038247343"), "Weapon modifier", attackSpeed, Operation.ADDITION)
      );
      modifiers.put(
         (Attribute)ManasCoreAttributes.CRIT_CHANCE.get(),
         new AttributeModifier(UUID.fromString("abed3f8e-3189-4fc6-b696-c41038247343"), "Weapon modifier", critChance, Operation.ADDITION)
      );
      return (Multimap<Attribute, AttributeModifier>)(slot != EquipmentSlot.MAINHAND ? ImmutableMultimap.of() : modifiers);
   }

   public void setUser(LivingEntity user) {
      this.user = user;
   }

   public static enum Grade {
      UNIQUE(TensuraToolTiers.PURE_MAGISTEEL, 8, BeyondRarity.UNIQUE),
      LEGEND(TensuraToolTiers.ADAMANTITE, 8, BeyondRarity.LEGEND);

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
