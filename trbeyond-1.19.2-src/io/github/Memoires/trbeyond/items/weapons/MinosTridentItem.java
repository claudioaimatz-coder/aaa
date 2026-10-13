package io.github.Memoires.trbeyond.items.weapons;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.item.TensuraToolTiers;
import com.github.manasmods.tensura.item.templates.custom.SimpleSpearItem;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.items.armor.BeyondArmorItem;
import io.github.Memoires.trbeyond.items.armor.MinosArmorItem;
import io.github.Memoires.trbeyond.items.effects.AnimatedEffectWeapon;
import io.github.Memoires.trbeyond.registry.battlewill.BeyondUtilityArts;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

public class MinosTridentItem extends SimpleSpearItem implements AnimatedEffectWeapon {
   protected static final UUID SPEED_UUID = UUID.fromString("b42381aa-787d-4574-8e82-a4f4039191ec");
   public MinosTridentItem.Grade grade;

   public MinosTridentItem(
      Tier pTier,
      int pAttackDamageModifier,
      double critChance,
      double critDamageMultiplier,
      int oneHandedAttackDamageModifier,
      float oneHandedAttackSpeedModifier,
      Properties pProperties
   ) {
      super(pTier, pAttackDamageModifier, -2.5F, critChance, critDamageMultiplier, oneHandedAttackDamageModifier, oneHandedAttackSpeedModifier, pProperties);
   }

   public MinosTridentItem(MinosTridentItem.Grade grade) {
      this(
         grade.getMaterial(),
         grade.getDamage(),
         0.0,
         0.0,
         100,
         0.0F,
         new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).defaultDurability(150).rarity(grade.getRarity())
      );
      this.grade = grade;
   }

   @Override
   public void releaseUsing(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pEntityLiving, int pTimeLeft) {
      if (pEntityLiving instanceof Player player) {
         int i = this.getUseDuration(pStack) - pTimeLeft;
         CompoundTag tag = pStack.getOrCreateTag();
         if (i >= 10 && tag.getInt("attackTicks") <= 0) {
            if (pLevel.isClientSide()) {
               this.playAnimationClientSync(player);
            }

            tag.putInt("attackTicks", 70);
         }
      }
   }

   public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
      boolean hurtEnemy = super.hurtEnemy(pStack, pTarget, pAttacker);
      if (hurtEnemy && pTarget.invulnerableTime < 40) {
         pTarget.invulnerableTime = 0;
         float damage = (float)pAttacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.1F;
         if (pAttacker instanceof Player player) {
            pTarget.hurt(DamageSource.playerAttack(player).bypassArmor(), damage);
         } else {
            pTarget.hurt(DamageSource.mobAttack(pAttacker).bypassArmor(), damage);
         }

         pTarget.invulnerableTime = 0;
      }

      return hurtEnemy;
   }

   public void inventoryTick(ItemStack pStack, Level level, Entity entity, int pSlotId, boolean pIsSelected) {
      if (SkillAPI.getSkillsFrom(entity).getSkill((ManasSkill)BeyondUtilityArts.SKY_WALK.get()).isPresent()
            && ((ManasSkillInstance)SkillAPI.getSkillsFrom(entity).getSkill((ManasSkill)BeyondUtilityArts.SKY_WALK.get()).get()).isToggled()
         || entity.isOnGround()) {
         CompoundTag tag = pStack.getOrCreateTag();
         int tick = tag.getInt("attackTicks");
         if (tick <= 0) {
            return;
         }

         if (!(entity instanceof LivingEntity owner)) {
            return;
         }

         entity.setYBodyRot(entity.getYHeadRot());
         if (level.isClientSide()) {
            return;
         }

         if (tick < 60) {
            level.playSound(null, entity, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.0F);
         }

         tag.putInt("attackTicks", tick - 1);
         if (tag.getInt("attackTicks") % 7 != 0) {
            return;
         }

         Vec3 lookAngle = entity.getLookAngle().multiply(1.0, 0.0, 1.0).normalize();
         Vec3 targetPos = entity.getEyePosition().add(lookAngle.scale(owner.getAttributeValue((Attribute)ForgeMod.ATTACK_RANGE.get())));
         Vec3 source = entity.getEyePosition().add(lookAngle);
         Vec3 offSetToTarget = targetPos.subtract(source);
         Vec3 normalizes = offSetToTarget.normalize();

         for (int particleIndex = 1; particleIndex < Mth.floor(offSetToTarget.length()); particleIndex++) {
            Vec3 pos = source.add(normalizes.scale((double)particleIndex));
            AABB aabb = new AABB(new BlockPos(pos.x, pos.y, pos.z)).inflate(1.0);
            List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, aabb, entityData -> !entityData.is(entity) && !entityData.isAlliedTo(entity));
            if (!list.isEmpty()) {
               if (owner instanceof Player) {
                  Player player = (Player)owner;

                  for (LivingEntity target : list) {
                     if (target.invulnerableTime < 40) {
                        target.invulnerableTime = 0;
                     }

                     player.attack(target);
                  }
               } else {
                  for (LivingEntity target : list) {
                     if (target.invulnerableTime < 40) {
                        target.invulnerableTime = 0;
                     }

                     target.hurt(DamageSource.mobAttack(owner), (float)owner.getAttributeValue(Attributes.ATTACK_DAMAGE));
                     this.hurtEnemy(pStack, target, owner);
                  }
               }
            }
         }
      }
   }

   @Override
   public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
      if (slot != EquipmentSlot.MAINHAND) {
         return ImmutableMultimap.of();
      } else {
         CompoundTag tag = stack.getOrCreateTag();
         Multimap<Attribute, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
         return (Multimap<Attribute, AttributeModifier>)(tag.getInt("attackTicks") <= 0
            ? map
            : ImmutableMultimap.builder()
               .putAll(map)
               .put(Attributes.MOVEMENT_SPEED, new AttributeModifier(SPEED_UUID, "Speed Modifier", -1.0, Operation.MULTIPLY_TOTAL))
               .build());
      }
   }

   public static void applyEnchants(LivingEntity entity) {
      Enchantment enchantment = (Enchantment)TensuraEnchantments.HOLY_COAT.get();
      Predicate<ItemStack> predicate = stack -> stack.getItem() instanceof MinosArmorItem;
      ItemStack handStack = entity.getItemBySlot(EquipmentSlot.MAINHAND);
      if (handStack.getItem() instanceof MinosTridentItem) {
         if (BeyondArmorItem.hasFullSet(entity, predicate, 4)) {
            if (handStack.getEnchantmentLevel(enchantment) != 3) {
               EngravingEnchantment.engrave(handStack, enchantment, 3);
            }
         } else if (handStack.getEnchantmentLevel(enchantment) != 2) {
            EngravingEnchantment.engrave(handStack, enchantment, 2);
         }
      }
   }

   @Override
   public String getAnimationName() {
      return "minos_trident_attack";
   }

   @Override
   public AnimatedEffectWeapon.Effect getEffect() {
      return switch (this.grade) {
         case UNIQUE -> AnimatedEffectWeapon.Effect.MINOS_TRIDENT_UNIQUE;
         case GOD -> AnimatedEffectWeapon.Effect.MINOS_TRIDENT_GOD;
         case LEGEND -> AnimatedEffectWeapon.Effect.MINOS_TRIDENT_LEGEND;
      };
   }

   public static enum Grade {
      UNIQUE(TensuraToolTiers.SILVER, 30, BeyondRarity.UNIQUE),
      LEGEND(TensuraToolTiers.SILVER, 43, BeyondRarity.LEGEND),
      GOD(TensuraToolTiers.SILVER, 67, BeyondRarity.GOD);

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
