package io.github.Memoires.trbeyond.items.weapons;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.capability.SkillStorage;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.item.TensuraToolTiers;
import com.github.manasmods.tensura.item.templates.custom.TwoHandedLongSword;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import com.github.manasmods.tensura.registry.skill.ExtraSkills;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.Memoires.trbeyond.capability.player.BeyondPlayerCapability;
import io.github.Memoires.trbeyond.client.collision.OBB;
import io.github.Memoires.trbeyond.client.collision.TargetFinder;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.items.effects.AnimatedEffectWeapon;
import io.github.Memoires.trbeyond.registry.battlewill.BeyondUtilityArts;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GurenItem extends TwoHandedLongSword implements AnimatedEffectWeapon {
   public GurenItem(
      Tier pTier,
      int pAttackDamageModifier,
      float pAttackSpeedModifier,
      double attackRangeModifier,
      double critChance,
      double critDamageMultiplier,
      int pAttackDamageModifier1,
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
         pAttackDamageModifier1,
         sweepChance,
         oneHandedSweepChance,
         pProperties
      );
   }

   public GurenItem(GurenItem.Grade grade) {
      this(
         grade.material,
         grade.getDamage(),
         -2.5F,
         2.0,
         20.0,
         0.5,
         grade.getDamage() - 3,
         100.0,
         100.0,
         new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).defaultDurability(150).rarity(grade.getRarity())
      );
   }

   @Override
   public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
      pTooltipComponents.add(Component.translatable("tooltip.tensura.long_sword.tooltip"));
   }

   public static void applyEnchants(ItemStack toStack, ItemStack fromStack, EquipmentSlot toSlot) {
      Enchantment tsukumogami = (Enchantment)TensuraEnchantments.TSUKUMOGAMI.get();
      Enchantment flameInfusion = (Enchantment)BeyondEnchantments.FLAME_INFUSION.get();
      if (toSlot.getType().equals(Type.HAND) && toStack.getItem() instanceof GurenItem gurenItem) {
         if (toStack.getEnchantmentLevel(tsukumogami) < 1) {
            EngravingEnchantment.engrave(toStack, tsukumogami, 1);
         }

         if (gurenItem.getRarity(fromStack).equals(GurenItem.Grade.GOD.getRarity()) && toStack.getEnchantmentLevel(flameInfusion) < 1) {
            EngravingEnchantment.engrave(toStack, flameInfusion, 1);
         }
      }
   }

   public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
      if (SkillAPI.getSkillsFrom(pEntity).getSkill((ManasSkill)ExtraSkills.BLACK_FLAME.get()).isPresent()
         && ((ManasSkillInstance)SkillAPI.getSkillsFrom(pEntity).getSkill((ManasSkill)ExtraSkills.BLACK_FLAME.get()).get()).isToggled()) {
         pStack.getItem().getShareTag(pStack).putFloat("black_flame", 1.0F);
      } else {
         pStack.getItem().getShareTag(pStack).putFloat("black_flame", 0.0F);
      }
   }

   public InteractionResultHolder<ItemStack> use(Level pLevel, final Player pPlayer, InteractionHand pHand) {
      if ((
            SkillAPI.getSkillsFrom(pPlayer).getSkill((ManasSkill)BeyondUtilityArts.SKY_WALK.get()).isPresent()
                  && ((ManasSkillInstance)SkillAPI.getSkillsFrom(pPlayer).getSkill((ManasSkill)BeyondUtilityArts.SKY_WALK.get()).get()).isToggled()
               || pPlayer.isOnGround()
         )
         && SkillAPI.getSkillsFrom(pPlayer).getSkill((ManasSkill)ExtraSkills.BLACK_FLAME.get()).isPresent()
         && ((ManasSkillInstance)SkillAPI.getSkillsFrom(pPlayer).getSkill((ManasSkill)ExtraSkills.BLACK_FLAME.get()).get()).isToggled()
         && ((ManasSkillInstance)SkillAPI.getSkillsFrom(pPlayer).getSkill((ManasSkill)ExtraSkills.BLACK_FLAME.get()).get()).isMastered(pPlayer)
         && !SkillHelper.outOfMagicule(pPlayer, 20000.0)) {
         pPlayer.getCooldowns().addCooldown(this, 300);
         final Vec3 oldPos = pPlayer.getEyePosition();
         if (pLevel.isClientSide) {
            this.playAnimationClientSync(pPlayer);
         }

         final float yRot = pPlayer.getYRot();
         final float xRot = pPlayer.getXRot();
         pPlayer.level.playSound(null, pPlayer.blockPosition(), (SoundEvent)BeyondSoundEvents.BLACK_FLAME_SLASH.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
         MinecraftForge.EVENT_BUS.register(new Object() {
            int ticks = 0;
            final Vec3 finalOldPos = oldPos;
            final float finalYRot = yRot;
            final float finalXRot = xRot;

            @SubscribeEvent
            public void onServerTick(ServerTickEvent event) {
               if (event.phase == Phase.END) {
                  this.ticks++;
                  if (this.ticks <= 59) {
                     pPlayer.setYHeadRot(this.finalYRot);
                     pPlayer.setXRot(this.finalXRot);
                     pPlayer.setYRot(this.finalYRot);
                  }

                  TensuraParticleHelper.addServerParticlesAroundSelf(pPlayer, (ParticleOptions)TensuraParticles.BLACK_FIRE.get());
                  if (this.ticks == 50) {
                     TargetFinder.TargetResult result = GurenItem.this.findAttackTargetResultSlash(pPlayer, 11.0, this.finalOldPos);
                     List<Entity> entities = result.entities;
                     entities.forEach(entity -> {
                        entity.invulnerableTime = 0;
                        SkillStorage skills = SkillAPI.getSkillsFrom(pPlayer);
                        Optional<ManasSkillInstance> flameManipOpt = skills.getSkill((ManasSkill)ExtraSkills.FLAME_MANIPULATION.get());
                        Optional<ManasSkillInstance> flameDomOpt = skills.getSkill((ManasSkill)ExtraSkills.FLAME_DOMINATION.get());
                        boolean hasFlameManip = flameManipOpt.isPresent() && flameManipOpt.get().isToggled();
                        boolean hasFlameDom = flameDomOpt.isPresent() && flameDomOpt.get().isToggled();
                        boolean fireInfused = GurenItem.this.hasFireInfusion(pPlayer);
                        boolean flameInfused = GurenItem.this.hasFlameInfusion(pPlayer);
                        float damage = 250.0F;
                        if (hasFlameManip && !hasFlameDom && !fireInfused && !flameInfused) {
                           damage = 500.0F;
                        } else if (hasFlameDom && !fireInfused && !flameInfused) {
                           damage = 1000.0F;
                        } else if (hasFlameManip && !hasFlameDom && fireInfused && !flameInfused) {
                           damage = 550.0F;
                        } else if (hasFlameDom && fireInfused && !flameInfused) {
                           damage = 1050.0F;
                        } else if (hasFlameManip && !hasFlameDom && !fireInfused && flameInfused) {
                           damage = 600.0F;
                        } else if (hasFlameDom && !fireInfused && flameInfused) {
                           damage = 1100.0F;
                        } else if (hasFlameManip && !hasFlameDom && fireInfused && flameInfused) {
                           damage = 650.0F;
                        } else if (hasFlameDom && fireInfused && flameInfused) {
                           damage = 1150.0F;
                        } else if (!hasFlameManip && !hasFlameDom && !fireInfused && !flameInfused) {
                           damage = 250.0F;
                        } else if (!hasFlameManip && hasFlameDom && !fireInfused && !flameInfused) {
                           damage = 1000.0F;
                        } else if (!hasFlameManip && !hasFlameDom && fireInfused && !flameInfused) {
                           damage = 300.0F;
                        } else if (!hasFlameManip && hasFlameDom && fireInfused && !flameInfused) {
                           damage = 1050.0F;
                        } else if (!hasFlameManip && !hasFlameDom && !fireInfused && flameInfused) {
                           damage = 350.0F;
                        } else if (!hasFlameManip && hasFlameDom && !fireInfused && flameInfused) {
                           damage = 1100.0F;
                        } else if (!hasFlameManip && !hasFlameDom && fireInfused && flameInfused) {
                           damage = 400.0F;
                        } else if (!hasFlameManip && hasFlameDom && fireInfused && flameInfused) {
                           damage = 1150.0F;
                        }

                        if (damage > 0.0F) {
                           entity.hurt(TensuraDamageSources.blackFlame(pPlayer), damage);
                        }

                        if (entity instanceof LivingEntity living) {
                           living.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.PARALYSIS.get(), 140, 10));
                           living.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.BURDEN.get(), 100, 10));
                        }
                     });
                     BeyondPlayerCapability.getFrom(pPlayer).ifPresent(cap -> cap.setAction(true));
                     BeyondPlayerCapability.sync(pPlayer);
                  }

                  if (this.ticks >= 80 && this.ticks <= 120) {
                     TargetFinder.TargetResult resultVortex = GurenItem.this.findAttackTargetResultVortex(pPlayer, 7.0, this.finalOldPos);
                     List<Entity> entitiesVortex = resultVortex.entities;
                     Vec3 vortexCenter = resultVortex.obb.center;
                     GurenItem.this.pullEntitiesToPoint(entitiesVortex, vortexCenter, 0.5F);
                     TargetFinder.TargetResult resultExplosion = GurenItem.this.findAttackTargetResultVortex(pPlayer, 4.0, this.finalOldPos);
                     List<Entity> entitiesExplosion = resultExplosion.entities;
                     entitiesExplosion.forEach(entity -> {
                        entity.invulnerableTime = 0;
                        SkillStorage skills = SkillAPI.getSkillsFrom(pPlayer);
                        Optional<ManasSkillInstance> flameManipOpt = skills.getSkill((ManasSkill)ExtraSkills.FLAME_MANIPULATION.get());
                        Optional<ManasSkillInstance> flameDomOpt = skills.getSkill((ManasSkill)ExtraSkills.FLAME_DOMINATION.get());
                        boolean hasFlameManip = flameManipOpt.isPresent() && flameManipOpt.get().isToggled();
                        boolean hasFlameDom = flameDomOpt.isPresent() && flameDomOpt.get().isToggled();
                        boolean fireInfused = GurenItem.this.hasFireInfusion(pPlayer);
                        boolean flameInfused = GurenItem.this.hasFlameInfusion(pPlayer);
                        float damage = 500.0F;
                        if (hasFlameManip && !hasFlameDom && !fireInfused && !flameInfused) {
                           damage = 1000.0F;
                        } else if (hasFlameDom && !fireInfused && !flameInfused) {
                           damage = 2000.0F;
                        } else if (hasFlameManip && !hasFlameDom && fireInfused && !flameInfused) {
                           damage = 1100.0F;
                        } else if (hasFlameDom && fireInfused && !flameInfused) {
                           damage = 2100.0F;
                        } else if (hasFlameManip && !hasFlameDom && !fireInfused && flameInfused) {
                           damage = 1200.0F;
                        } else if (hasFlameDom && !fireInfused && flameInfused) {
                           damage = 2200.0F;
                        } else if (hasFlameManip && !hasFlameDom && fireInfused && flameInfused) {
                           damage = 1300.0F;
                        } else if (hasFlameDom && fireInfused && flameInfused) {
                           damage = 2300.0F;
                        } else if (!hasFlameManip && !hasFlameDom && !fireInfused && !flameInfused) {
                           damage = 500.0F;
                        } else if (!hasFlameManip && hasFlameDom && !fireInfused && !flameInfused) {
                           damage = 2000.0F;
                        } else if (!hasFlameManip && !hasFlameDom && fireInfused && !flameInfused) {
                           damage = 600.0F;
                        } else if (!hasFlameManip && hasFlameDom && fireInfused && !flameInfused) {
                           damage = 2100.0F;
                        } else if (!hasFlameManip && !hasFlameDom && !fireInfused && flameInfused) {
                           damage = 700.0F;
                        } else if (!hasFlameManip && hasFlameDom && !fireInfused && flameInfused) {
                           damage = 2200.0F;
                        } else if (!hasFlameManip && !hasFlameDom && flameInfused) {
                           damage = 700.0F;
                        } else if (!hasFlameManip && hasFlameDom && flameInfused) {
                           damage = 2200.0F;
                        }

                        if (damage > 0.0F) {
                           entity.hurt(TensuraDamageSources.blackFlame(pPlayer).bypassArmor(), damage);
                        }

                        if (entity instanceof LivingEntity living) {
                           living.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.PARALYSIS.get(), 120, 10));
                           living.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.BURDEN.get(), 100, 10));
                        }
                     });
                  }

                  if (this.ticks >= 150) {
                     BeyondPlayerCapability.getFrom(pPlayer).ifPresent(cap -> cap.setAction(false));
                     BeyondPlayerCapability.sync(pPlayer);
                     MinecraftForge.EVENT_BUS.unregister(this);
                  }
               }
            }
         });
      }

      System.out.println(pPlayer.getUseItem());
      return InteractionResultHolder.consume(pPlayer.getItemInHand(pHand));
   }

   public boolean hasFireInfusion(Player player) {
      ItemStack heldItem = player.getMainHandItem();
      int level = EnchantmentHelper.getItemEnchantmentLevel((Enchantment)BeyondEnchantments.FIRE_INFUSION.get(), heldItem);
      return level > 0;
   }

   public boolean hasFlameInfusion(Player player) {
      ItemStack heldItem = player.getMainHandItem();
      int level = EnchantmentHelper.getItemEnchantmentLevel((Enchantment)BeyondEnchantments.FLAME_INFUSION.get(), heldItem);
      return level > 0;
   }

   @Override
   public String getAnimationName() {
      return "benimaru_blade_attack";
   }

   @Override
   public AnimatedEffectWeapon.Effect getEffect() {
      return AnimatedEffectWeapon.Effect.GUREN;
   }

   public TargetFinder.TargetResult findAttackTargetResultSlash(Player player, double attackRange, Vec3 oldpos) {
      Vec3 origin = getInitialTracingPoint(player, oldpos);
      List<Entity> entities = TargetFinder.getInitialTargets(player, attackRange);
      Vec3 size = new Vec3(attackRange / 11.0 + 2.0, attackRange / 11.0 + 2.0, attackRange + 3.0);
      OBB obb = new OBB(origin, size, 0.0F, player.getYRot());
      obb.updateVertex();
      TargetFinder.CollisionFilter collisionFilter = new TargetFinder.CollisionFilter(obb);
      entities = collisionFilter.filter(entities);
      TargetFinder.RadialFilter radialFilter = new TargetFinder.RadialFilter(origin, obb.axisZ, attackRange, (double)player.getYRot());
      entities = radialFilter.filter(entities);
      return new TargetFinder.TargetResult(entities, obb);
   }

   public TargetFinder.TargetResult findAttackTargetResultVortex(Player player, double attackRange, Vec3 oldpos) {
      Vec3 lookVec = player.getLookAngle();
      Vec3 flatLookVec = new Vec3(lookVec.x, 0.0, lookVec.z).normalize();
      Vec3 behindPos = player.position().add(flatLookVec.scale(-4.0));
      AABB searchBox = new AABB(behindPos, behindPos).inflate(attackRange);
      Vec3 size = new Vec3(attackRange, attackRange, attackRange);
      OBB obb = new OBB(behindPos, size, 0.0F, player.getYRot());
      obb.updateVertex();
      List<LivingEntity> entities = player.level.getEntitiesOfClass(LivingEntity.class, searchBox, e -> e != player);
      return new TargetFinder.TargetResult(new ArrayList<>(entities), obb);
   }

   public void pullEntitiesToPoint(List<Entity> entities, Vec3 destination, float strength) {
      for (Entity entity : entities) {
         if (entity instanceof LivingEntity && !entity.isNoGravity()) {
            Vec3 currentPos = entity.position();
            Vec3 direction = destination.subtract(currentPos).normalize();
            double distance = currentPos.distanceTo(destination);
            double pullStrength = (double)strength * (1.0 - distance / 10.0);
            entity.setDeltaMovement(entity.getDeltaMovement().add(direction.scale(pullStrength)).scale(0.9));
            entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, 0.1, 0.0));
         }
      }
   }

   public static Vec3 getInitialTracingPoint(Player player, Vec3 oldpos) {
      double shoulderHeight = (double)player.getBbHeight() * 0.15 * (double)player.getScale();
      return oldpos.subtract(0.0, shoulderHeight, 0.0);
   }

   public boolean isFoil(ItemStack pStack) {
      return false;
   }

   public static void setTpState(Player player, boolean isAfterTp) {
      player.getPersistentData().putBoolean("after_tp", isAfterTp);
   }

   public static boolean isAfterTP(Player player) {
      return player.getPersistentData().getBoolean("after_tp");
   }

   public static enum Grade {
      UNIQUE(TensuraToolTiers.PURE_MAGISTEEL, 11, BeyondRarity.UNIQUE),
      LEGEND(TensuraToolTiers.ADAMANTITE, 12, BeyondRarity.LEGEND),
      GOD(TensuraToolTiers.HIHIIROKANE, 13, BeyondRarity.GOD);

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
