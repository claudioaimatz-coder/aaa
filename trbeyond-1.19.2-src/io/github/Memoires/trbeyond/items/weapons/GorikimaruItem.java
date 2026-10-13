package io.github.Memoires.trbeyond.items.weapons;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.capability.SkillStorage;
import com.github.manasmods.tensura.ability.SkillClientUtils;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.item.TensuraToolTiers;
import com.github.manasmods.tensura.item.templates.custom.TwoHandedLongSword;
import com.github.manasmods.tensura.registry.battlewill.ProjectileArts;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.items.effects.AnimatedEffectWeapon;
import io.github.Memoires.trbeyond.network.OgreSwordCannonPacket;
import io.github.Memoires.trbeyond.registry.battlewill.BeyondUtilityArts;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GorikimaruItem extends TwoHandedLongSword implements AnimatedEffectWeapon {
   public GorikimaruItem(
      Tier pTier,
      int pAttackDamageModifier,
      float pAttackSpeedModifier,
      double pAttackRangeModifier,
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
         pAttackRangeModifier,
         critChance,
         critDamageMultiplier,
         pAttackDamageModifier,
         sweepChance,
         oneHandedSweepChance,
         pProperties
      );
   }

   public GorikimaruItem(GorikimaruItem.Grade grade) {
      this(
         grade.getMaterial(),
         grade.getDamage(),
         -3.2F,
         2.0,
         20.0,
         0.0,
         100.0,
         0.0,
         new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).defaultDurability(150).rarity(grade.getRarity())
      );
   }

   public static void applyEnchants(ItemStack toStack, ItemStack fromStack, EquipmentSlot toSlot) {
      Enchantment soulEater = (Enchantment)TensuraEnchantments.SOUL_EATER.get();
      Enchantment tsukumogami = (Enchantment)TensuraEnchantments.TSUKUMOGAMI.get();
      Enchantment battleInfusion = (Enchantment)BeyondEnchantments.BATTLE_INFUSION.get();
      if (toSlot.getType().equals(Type.HAND) && toStack.getItem() instanceof GorikimaruItem gorikimaruItem) {
         if (toStack.getEnchantmentLevel(soulEater) < 1) {
            EngravingEnchantment.engrave(toStack, soulEater, 1);
            EngravingEnchantment.engrave(toStack, tsukumogami, 1);
         }

         if (gorikimaruItem.getRarity(fromStack).equals(GorikimaruItem.Grade.GOD.getRarity()) && toStack.getEnchantmentLevel(battleInfusion) < 1) {
            EngravingEnchantment.engrave(toStack, battleInfusion, 1);
         }
      }
   }

   @Override
   public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
      pTooltipComponents.add(Component.translatable("tooltip.tensura.great_sword.tooltip"));
   }

   public UseAnim getUseAnimation(ItemStack pStack) {
      return UseAnim.BLOCK;
   }

   public int getUseDuration(ItemStack pStack) {
      return 72000;
   }

   public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
      if (pIsSelected && pEntity instanceof Player player) {
         this.handleOgreCannonSkill(pStack, pLevel, player);
      }

      this.updateOgreGuillotineState(pEntity);
   }

   private void handleOgreCannonSkill(ItemStack pStack, Level pLevel, Player player) {
      SkillStorage storage = SkillAPI.getSkillsFrom(player);
      if (this.canPlayerUseSkill(player, storage)) {
         storage.getSkill((ManasSkill)ProjectileArts.OGRE_SWORD_CANNON.get())
            .ifPresent(ogreCannon -> this.startAnimation(pLevel, player, player.getItemInHand(InteractionHand.MAIN_HAND), ogreCannon));
         this.decreaseOgreCannonCooldown(player);
      }
   }

   private boolean canPlayerUseSkill(Player player, SkillStorage storage) {
      return storage.getSkill((ManasSkill)BeyondUtilityArts.SKY_WALK.get()).map(skyWalk -> skyWalk.isToggled() || player.isOnGround()).orElse(player.isOnGround());
   }

   private void decreaseOgreCannonCooldown(Player player) {
      if (!player.level.isClientSide() && player.level.getServer() != null) {
         SkillAPI.getSkillsFrom(player).getSkill((ManasSkill)ProjectileArts.OGRE_SWORD_CANNON.get()).ifPresent(instance -> {
            boolean canCooldownDecrease = player.getLevel().getServer().m_129921_() % 20 == 0;
            if (canCooldownDecrease) {
               int currentCooldown = instance.getOrCreateTag().getInt("ogre_cannon_cooldown");
               if (currentCooldown > 0) {
                  instance.getOrCreateTag().putInt("ogre_cannon_cooldown", currentCooldown - 1);
               }
            }
         });
      }
   }

   private void updateOgreGuillotineState(Entity pEntity) {
      if (pEntity instanceof LivingEntity living) {
         float cannonState = living.hasEffect((MobEffect)TensuraMobEffects.OGRE_GUILLOTINE.get()) ? 1.0F : 0.0F;
         if (living.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof GorikimaruItem) {
            living.getItemInHand(InteractionHand.MAIN_HAND).getOrCreateTag().putFloat("ogre_cannon", cannonState);
         }
      }
   }

   public void startAnimation(Level level, Player player, ItemStack stack, ManasSkillInstance instance) {
      if (level.isClientSide) {
         if (SkillClientUtils.isSkillHeldClient(player, instance.getSkill())) {
            if (player.getCooldowns().isOnCooldown(stack.getItem())) {
               player.displayClientMessage(
                  Component.translatable(
                     "trbeyond.message.ogre_cannon_cooldown.on_cooldown", new Object[]{Math.round(player.getCooldowns().getCooldownPercent(stack.getItem(), 0.0F) * 20.0F)}
                  ),
                  true
               );
            } else if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
               if (SkillAPI.getSkillsFrom(player).getSkill((ManasSkill)BeyondUtilityArts.SKY_WALK.get()).isPresent()
                     && ((ManasSkillInstance)SkillAPI.getSkillsFrom(player).getSkill((ManasSkill)BeyondUtilityArts.SKY_WALK.get()).get()).isToggled()
                  || player.isOnGround()) {
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

                     OgreSwordCannonPacket.sendToServer();
                  }
               }
            }
         }
      }
   }

   public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
      return ToolActions.SHIELD_BLOCK.equals(toolAction) ? true : super.canPerformAction(stack, toolAction);
   }

   public boolean isFoil(ItemStack pStack) {
      return false;
   }

   @Override
   public String getAnimationName() {
      return "gorikimaru_attack";
   }

   @Override
   public AnimatedEffectWeapon.Effect getEffect() {
      return AnimatedEffectWeapon.Effect.GORIKIMARU;
   }

   public static enum Grade {
      UNIQUE(TensuraToolTiers.PURE_MAGISTEEL, 14, BeyondRarity.UNIQUE),
      LEGEND(TensuraToolTiers.ADAMANTITE, 17, BeyondRarity.LEGEND),
      GOD(TensuraToolTiers.HIHIIROKANE, 23, BeyondRarity.GOD);

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
