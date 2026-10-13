package io.github.Memoires.trbeyond.items.weapons;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.item.TensuraToolTiers;
import com.github.manasmods.tensura.item.templates.custom.TwoHandedLongSword;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import com.github.manasmods.tensura.registry.skill.UniqueSkills;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.items.effects.AnimatedEffectWeapon;
import io.github.Memoires.trbeyond.registry.battlewill.BeyondUtilityArts;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MeatCleaverItem extends TwoHandedLongSword implements AnimatedEffectWeapon {
   public MeatCleaverItem(
      Tier pTier,
      int pAttackDamageModifier,
      float pAttackSpeedModifier,
      double attackRangeModifier,
      double critChance,
      double critDamageMultiplier,
      double sweepChance,
      double oneHandedSweepChance,
      Properties pProperties
   ) {
      super(
         pTier,
         pAttackDamageModifier,
         pAttackSpeedModifier,
         attackRangeModifier,
         critChance,
         critDamageMultiplier,
         sweepChance,
         oneHandedSweepChance,
         pProperties
      );
   }

   public MeatCleaverItem(MeatCleaverItem.Grade grade) {
      this(
         grade.getMaterial(),
         grade.getDamage(),
         -3.0F,
         2.0,
         0.0,
         0.0,
         100.0,
         100.0,
         new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).defaultDurability(500).rarity(grade.getRarity())
      );
   }

   public static void applyEnchants(ItemStack toStack, ItemStack fromStack, EquipmentSlot toSlot) {
      Enchantment tsukumogami = (Enchantment)TensuraEnchantments.TSUKUMOGAMI.get();
      Enchantment earthInfusion = (Enchantment)BeyondEnchantments.EARTH_INFUSION.get();
      Enchantment soulEater = (Enchantment)TensuraEnchantments.SOUL_EATER.get();
      if (toSlot.getType().equals(Type.HAND) && toStack.getItem() instanceof MeatCleaverItem meatCleaverItem) {
         if (toStack.getEnchantmentLevel(tsukumogami) < 1) {
            EngravingEnchantment.engrave(toStack, tsukumogami, 1);
         }

         if (meatCleaverItem.getRarity(fromStack).equals(MeatCleaverItem.Grade.GOD.getRarity()) && toStack.getEnchantmentLevel(earthInfusion) < 1) {
            EngravingEnchantment.engrave(toStack, earthInfusion, 1);
            EngravingEnchantment.engrave(toStack, soulEater, 1);
         }
      }
   }

   public int getUseDuration(@NotNull ItemStack pStack) {
      return 36000;
   }

   @NotNull
   public InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pHand) {
      ItemStack itemstack = pPlayer.getItemInHand(pHand);
      if (pPlayer.getCooldowns().isOnCooldown(this)) {
         return InteractionResultHolder.pass(itemstack);
      } else if ((
            !SkillAPI.getSkillsFrom(pPlayer).getSkill((ManasSkill)BeyondUtilityArts.SKY_WALK.get()).isPresent()
               || !((ManasSkillInstance)SkillAPI.getSkillsFrom(pPlayer).getSkill((ManasSkill)BeyondUtilityArts.SKY_WALK.get()).get()).isToggled()
         )
         && !pPlayer.isOnGround()) {
         return InteractionResultHolder.consume(itemstack);
      } else {
         if (SkillAPI.getSkillsFrom(pPlayer).getSkill((ManasSkill)UniqueSkills.STARVED.get()).isPresent()
            || SkillAPI.getSkillsFrom(pPlayer).getSkill((ManasSkill)UniqueSkills.GOURMET.get()).isPresent()
            || SkillAPI.getSkillRegistry().containsKey(new ResourceLocation("trmysticism", "beezlebub"))) {
            pPlayer.startUsingItem(pHand);
            pPlayer.getCooldowns().addCooldown(this, 300);
            if (pLevel.isClientSide) {
               this.playAnimationClientSync(pPlayer);
            }
         }

         return InteractionResultHolder.consume(itemstack);
      }
   }

   @Override
   public String getAnimationName() {
      return "gelds_attack";
   }

   @Override
   public AnimatedEffectWeapon.Effect getEffect() {
      return AnimatedEffectWeapon.Effect.GELDS;
   }

   public static enum Grade {
      UNIQUE(TensuraToolTiers.PURE_MAGISTEEL, 7, BeyondRarity.UNIQUE),
      LEGENDARY(TensuraToolTiers.ADAMANTITE, 6, BeyondRarity.LEGEND),
      GOD(TensuraToolTiers.HIHIIROKANE, 4, BeyondRarity.GOD);

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
