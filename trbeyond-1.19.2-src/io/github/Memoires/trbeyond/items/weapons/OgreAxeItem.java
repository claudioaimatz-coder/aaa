package io.github.Memoires.trbeyond.items.weapons;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.item.TensuraToolTiers;
import com.github.manasmods.tensura.item.templates.custom.TwoHandedSword;
import com.github.manasmods.tensura.registry.battlewill.ProjectileArts;
import com.github.manasmods.tensura.registry.skill.ExtraSkills;
import com.github.manasmods.tensura.util.damage.TensuraDamageSource;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.registry.items.BeyondToolItems;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OgreAxeItem extends TwoHandedSword {
   public OgreAxeItem(
      Tier pTier,
      int pAttackDamageModifier,
      float pAttackSpeedModifier,
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
         critChance,
         critDamageMultiplier,
         pAttackDamageModifier,
         sweepChance,
         oneHandedSweepChance,
         pProperties
      );
   }

   public OgreAxeItem(OgreAxeItem.Grade grade) {
      this(
         grade.getMaterial(),
         grade.getDamage(),
         -3.6F,
         20.0,
         0.0,
         100.0,
         50.0,
         new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).defaultDurability(150).rarity(grade.getRarity())
      );
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

   public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
      pPlayer.startUsingItem(pHand);
      return InteractionResultHolder.consume(pPlayer.getItemInHand(pHand));
   }

   public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
      return ToolActions.SHIELD_BLOCK.equals(toolAction) ? true : super.canPerformAction(stack, toolAction);
   }

   public static boolean canBoostDamage(DamageSource source) {
      if (!(source.getEntity() instanceof LivingEntity entity)) {
         return false;
      } else if (entity.getMainHandItem().is((Item)BeyondToolItems.OGRE_AXE_GOD.get())) {
         return false;
      } else if (!(source instanceof TensuraDamageSource damageSource)) {
         return false;
      } else if (damageSource.getSkill() == null) {
         return false;
      } else {
         ManasSkillInstance instance = damageSource.getSkill();
         return instance.getSkill() == ProjectileArts.OGRE_FLAME.get() ? true : instance.getSkill() == ExtraSkills.BLACK_FLAME.get() && instance.getMode() >= 3;
      }
   }

   public static enum Grade {
      RARE(TensuraToolTiers.LOW_MAGISTEEL, 17, BeyondRarity.RARE),
      UNIQUE(TensuraToolTiers.HIGH_MAGISTEEL, 23, BeyondRarity.UNIQUE),
      LEGEND(TensuraToolTiers.ADAMANTITE, 10, BeyondRarity.LEGEND),
      GOD(TensuraToolTiers.HIHIIROKANE, 10, BeyondRarity.GOD);

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
