package io.github.Memoires.trbeyond.items.weapons;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.item.TensuraToolTiers;
import com.github.manasmods.tensura.item.templates.custom.TwoHandedLongSword;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import com.github.manasmods.tensura.registry.items.TensuraMaterialItems;
import com.github.manasmods.tensura.registry.skill.ExtraSkills;
import com.github.manasmods.tensura.registry.skill.ResistanceSkills;
import com.github.manasmods.tensura.registry.skill.UniqueSkills;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.Memoires.trbeyond.entity.projectile.ThrownMinosBardicheProjectile;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.items.armor.BeyondArmorItem;
import io.github.Memoires.trbeyond.items.armor.MinosArmorItem;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow.Pickup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MinosBardicheItem extends TwoHandedLongSword {
   private final MinosBardicheItem.Grade grade;

   public MinosBardicheItem(MinosBardicheItem.Grade grade) {
      super(
         grade.material,
         grade.getDamage(),
         -3.0F,
         2.0,
         0.0,
         0.0,
         grade.getDamage() - 3,
         100.0,
         100.0,
         new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).defaultDurability(150).rarity(grade.getRarity())
      );
      this.grade = grade;
   }

   @NotNull
   public UseAnim getUseAnimation(@NotNull ItemStack pStack) {
      return UseAnim.SPEAR;
   }

   public int getUseDuration(@NotNull ItemStack pStack) {
      return 36000;
   }

   public boolean isFoil(ItemStack pStack) {
      return false;
   }

   public void releaseUsing(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pEntityLiving, int pTimeLeft) {
      if (pEntityLiving instanceof Player player) {
         int i = this.getUseDuration(pStack) - pTimeLeft;
         if (i >= 10) {
            if (!pLevel.isClientSide) {
               pStack.hurtAndBreak(10, player, pPlayer -> pPlayer.broadcastBreakEvent(pEntityLiving.getUsedItemHand()));
               boolean left = pEntityLiving.getUsedItemHand() == InteractionHand.OFF_HAND && pEntityLiving.getMainArm() == HumanoidArm.RIGHT
                  || pEntityLiving.getUsedItemHand() == InteractionHand.MAIN_HAND && pEntityLiving.getMainArm() == HumanoidArm.LEFT;
               ThrownMinosBardicheProjectile minos = new ThrownMinosBardicheProjectile(pLevel, player, pStack, !left);
               minos.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
               float baseDamage = DamageSourceHelper.getMainWeaponDamage(player, null);
               Optional<ManasSkillInstance> optional = SkillAPI.getSkillsFrom(player).getSkill((ManasSkill)UniqueSkills.THROWER.get());
               if (optional.isPresent()) {
                  minos.setLoyaltyLevel(4);
                  baseDamage += (float)(optional.get().isMastered(player) ? 100 : 50);
                  int multiplier = 1;
                  if (SkillUtils.isSkillToggled(player, (ManasSkill)ExtraSkills.GRAVITY_DOMINATION.get())) {
                     multiplier = 3;
                  } else if (SkillUtils.isSkillToggled(player, (ManasSkill)ExtraSkills.GRAVITY_MANIPULATION.get())) {
                     multiplier = 2;
                  }

                  baseDamage *= (float)multiplier;
               } else {
                  if (SkillUtils.isSkillToggled(player, (ManasSkill)ExtraSkills.GRAVITY_DOMINATION.get())) {
                     baseDamage *= 2.0F;
                  }

                  if (SkillUtils.hasSkill(player, (ManasSkill)ExtraSkills.MOLECULAR_MANIPULATION.get())
                     || SkillUtils.hasSkill(player, (ManasSkill)ExtraSkills.GRAVITY_DOMINATION.get())) {
                     minos.setLoyaltyLevel(3);
                     minos.setReturnSecond(20);
                  }
               }

               minos.setBaseDamage((double)baseDamage);
               if (player.getAbilities().instabuild) {
                  minos.pickup = Pickup.CREATIVE_ONLY;
               }

               pLevel.addFreshEntity(minos);
               pLevel.playSound(null, minos, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.0F);
               if (!player.getAbilities().instabuild) {
                  player.getInventory().removeItem(pStack);
               }
            }

            player.awardStat(Stats.ITEM_USED.get(this));
         }
      }
   }

   @NotNull
   public InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pHand) {
      ItemStack itemstack = pPlayer.getItemInHand(pHand);
      if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 10) {
         return InteractionResultHolder.fail(itemstack);
      } else {
         pPlayer.startUsingItem(pHand);
         return InteractionResultHolder.consume(itemstack);
      }
   }

   public static void dealLightningDamage(Player attacker, LivingEntity target, ItemStack minos) {
      if (getWindCores(minos) >= 3) {
         if (!SkillUtils.isSkillToggled(target, (ManasSkill)ResistanceSkills.LIGHT_ATTACK_RESISTANCE.get())
            && !SkillUtils.isSkillToggled(target, (ManasSkill)ResistanceSkills.LIGHT_ATTACK_NULLIFICATION.get())) {
            target.invulnerableTime = 0;
            float amount = SkillHelper.noCritAttackDamage(attacker, target) * 1.5F;
            target.hurt(TensuraDamageSources.lightning(attacker), amount);
            target.invulnerableTime = 0;
            if ((double)attacker.getLevel().getRandom().nextFloat() <= 0.2) {
               target.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.PARALYSIS.get(), 60, 4));
            }
         }
      }
   }

   private static Stream<ItemStack> getContents(ItemStack pStack) {
      CompoundTag compoundtag = pStack.getTag();
      if (compoundtag == null) {
         return Stream.empty();
      } else {
         ListTag listtag = compoundtag.getList("Cores", 10);
         return listtag.stream().map(CompoundTag.class::cast).map(ItemStack::of);
      }
   }

   public static int getWindCores(ItemStack gear) {
      int coreNumber = 0;

      for (ItemStack cores : getContents(gear).toList()) {
         if (cores.is((Item)TensuraMaterialItems.ELEMENT_CORE_WIND.get())) {
            coreNumber++;
         }
      }

      return coreNumber;
   }

   public static void applyEnchants(ItemStack toStack, LivingEntity entity, EquipmentSlot toSlot) {
      if (toSlot.getType().equals(Type.HAND)
         && toStack.getItem() instanceof MinosBardicheItem bardiche
         && toStack.getEnchantmentLevel((Enchantment)TensuraEnchantments.SLOTTING.get()) < bardiche.getGrade().getSlottingLevel()) {
         EngravingEnchantment.engrave(toStack, (Enchantment)TensuraEnchantments.SLOTTING.get(), bardiche.getGrade().getSlottingLevel());
      }

      ItemStack hand = entity.getItemBySlot(EquipmentSlot.MAINHAND);
      if (hand.getItem() instanceof MinosBardicheItem) {
         Enchantment holyCoat = (Enchantment)TensuraEnchantments.HOLY_COAT.get();
         Predicate<ItemStack> predicate = stack -> stack.getItem() instanceof MinosArmorItem;
         if (BeyondArmorItem.hasFullSet(entity, predicate, 4)) {
            if (hand.getEnchantmentLevel(holyCoat) != 3) {
               EngravingEnchantment.engrave(hand, holyCoat, 3);
            }
         } else if (hand.getEnchantmentLevel(holyCoat) != 2) {
            EngravingEnchantment.engrave(hand, holyCoat, 2);
         }
      }
   }

   public MinosBardicheItem.Grade getGrade() {
      return this.grade;
   }

   public static enum Grade {
      UNIQUE(TensuraToolTiers.PURE_MAGISTEEL, 6, BeyondRarity.UNIQUE, 1),
      LEGEND(TensuraToolTiers.ADAMANTITE, 0, BeyondRarity.LEGEND, 2),
      GOD(TensuraToolTiers.HIHIIROKANE, -5, BeyondRarity.GOD, 3);

      private final Tier material;
      private final int damage;
      private final Rarity rarity;
      private final int slottingLevel;

      public Tier getMaterial() {
         return this.material;
      }

      public int getDamage() {
         return this.damage;
      }

      public Rarity getRarity() {
         return this.rarity;
      }

      public int getSlottingLevel() {
         return this.slottingLevel;
      }

      private Grade(Tier material, int damage, Rarity rarity, int slottingLevel) {
         this.material = material;
         this.damage = damage;
         this.rarity = rarity;
         this.slottingLevel = slottingLevel;
      }
   }
}
