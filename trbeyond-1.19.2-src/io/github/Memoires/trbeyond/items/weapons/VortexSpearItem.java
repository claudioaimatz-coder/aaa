package io.github.Memoires.trbeyond.items.weapons;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillClientUtils;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.item.TensuraToolTiers;
import com.github.manasmods.tensura.item.templates.custom.SimpleSpearItem;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.items.effects.AnimatedEffectWeapon;
import io.github.Memoires.trbeyond.network.VortexCrashPacket;
import io.github.Memoires.trbeyond.registry.battlewill.BeyondUtilityArts;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import io.github.Memoires.trbeyond.util.BeyondRarity;
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

public class VortexSpearItem extends SimpleSpearItem implements AnimatedEffectWeapon {
   public VortexSpearItem(
      Tier pTier,
      int pAttackDamageModifier,
      float pAttackSpeedModifier,
      double critChance,
      double critDamageMultiplier,
      int oneHandedAttackDamageModifier,
      float oneHandedAttackSpeedModifier,
      Properties pProperties
   ) {
      super(pTier, pAttackDamageModifier, -2.5F, critChance, critDamageMultiplier, oneHandedAttackDamageModifier, oneHandedAttackSpeedModifier, pProperties);
   }

   public VortexSpearItem(VortexSpearItem.Grade grade) {
      this(
         grade.getMaterial(),
         grade.getDamage(),
         -3.2F,
         0.0,
         0.0,
         100,
         0.0F,
         new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).defaultDurability(150).rarity(grade.getRarity())
      );
   }

   public void startAnimation(Level level, Player player, ItemStack stack, ManasSkillInstance instance) {
      if (SkillClientUtils.isSkillHeldClient(player, instance.getSkill())) {
         if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
            int power = instance.isMastered(player) ? 10 : 5;
            double cost = 200.0 * (double)power;
            if (!SkillHelper.outOfAura(player, cost)) {
               if (stack.getOrCreateTag().getInt("int") > 1) {
                  return;
               }

               player.getCooldowns().addCooldown(stack.getItem(), 300);
               stack.getOrCreateTag().putInt("int", 1);
               if (level.isClientSide) {
                  this.playAnimationClientSync(player);
               }
            }
         }
      }
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
      if (pPlayer.getCooldowns().isOnCooldown(this)) {
         return InteractionResultHolder.pass(pPlayer.getItemInHand(pHand));
      } else if (pPlayer.isShiftKeyDown()) {
         pPlayer.startUsingItem(pHand);
         return InteractionResultHolder.success(pPlayer.getItemInHand(pHand));
      } else {
         if ((
               SkillAPI.getSkillsFrom(pPlayer).getSkill((ManasSkill)BeyondUtilityArts.SKY_WALK.get()).isPresent()
                     && ((ManasSkillInstance)SkillAPI.getSkillsFrom(pPlayer).getSkill((ManasSkill)BeyondUtilityArts.SKY_WALK.get()).get()).isToggled()
                  || pPlayer.isOnGround()
            )
            && (!SkillHelper.outOfMagicule(pPlayer, 15000.0) || pPlayer.isCreative())) {
            pPlayer.getCooldowns().addCooldown(this, 300);
            if (pLevel.isClientSide) {
               this.playAnimationClientSync(pPlayer);
            }

            VortexCrashPacket.sendToServer();
         }

         return InteractionResultHolder.consume(pPlayer.getItemInHand(pHand));
      }
   }

   public static void applyEnchants(ItemStack toStack, ItemStack fromStack, EquipmentSlot toSlot) {
      Enchantment tsukumogami = (Enchantment)TensuraEnchantments.TSUKUMOGAMI.get();
      Enchantment waterInfusion = (Enchantment)BeyondEnchantments.WATER_INFUSION.get();
      if (toSlot.getType().equals(Type.HAND) && toStack.getItem() instanceof VortexSpearItem spearItem) {
         if (toStack.getEnchantmentLevel(tsukumogami) < 1) {
            EngravingEnchantment.engrave(toStack, tsukumogami, 1);
         }

         if (spearItem.getRarity(fromStack).equals(VortexSpearItem.Grade.GOD.getRarity()) && toStack.getEnchantmentLevel(waterInfusion) < 1) {
            EngravingEnchantment.engrave(toStack, waterInfusion, 1);
         }
      }
   }

   @Override
   public String getAnimationName() {
      return "gabiru_attack";
   }

   @Override
   public AnimatedEffectWeapon.Effect getEffect() {
      return AnimatedEffectWeapon.Effect.GABIRU;
   }

   public static enum Grade {
      UNIQUE(TensuraToolTiers.PURE_MAGISTEEL, 6, BeyondRarity.UNIQUE),
      LEGEND(TensuraToolTiers.ADAMANTITE, 12, BeyondRarity.LEGEND),
      GOD(TensuraToolTiers.HIHIIROKANE, 8, BeyondRarity.GOD);

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
