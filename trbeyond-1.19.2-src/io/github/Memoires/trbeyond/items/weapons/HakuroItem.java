package io.github.Memoires.trbeyond.items.weapons;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.capability.SkillStorage;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.entity.AkashEntity;
import com.github.manasmods.tensura.entity.CharybdisEntity;
import com.github.manasmods.tensura.entity.ElementalColossusEntity;
import com.github.manasmods.tensura.entity.IfritEntity;
import com.github.manasmods.tensura.entity.OrcDisasterEntity;
import com.github.manasmods.tensura.entity.OrcLordEntity;
import com.github.manasmods.tensura.entity.SupermassiveSlimeEntity;
import com.github.manasmods.tensura.entity.SylphideEntity;
import com.github.manasmods.tensura.entity.UndineEntity;
import com.github.manasmods.tensura.entity.WarGnomeEntity;
import com.github.manasmods.tensura.entity.human.HinataSakaguchiEntity;
import com.github.manasmods.tensura.item.TensuraToolTiers;
import com.github.manasmods.tensura.item.templates.custom.TwoHandedLongSword;
import com.github.manasmods.tensura.registry.battlewill.MeleeArts;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.enchantment.TensuraEnchantments;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSource;
import io.github.Memoires.trbeyond.entity.custom.BossBlackSpiderEntity;
import io.github.Memoires.trbeyond.entity.custom.EvilCentipedeBossEntity;
import io.github.Memoires.trbeyond.entity.custom.GozulEntity;
import io.github.Memoires.trbeyond.entity.custom.MezulEntity;
import io.github.Memoires.trbeyond.entity.custom.OgreLordEntity;
import io.github.Memoires.trbeyond.entity.custom.TempestSerpentBossEntity;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.items.effects.AnimatedEffectWeapon;
import io.github.Memoires.trbeyond.registry.battlewill.BeyondUtilityArts;
import io.github.Memoires.trbeyond.registry.effects.BeyondEffects;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class HakuroItem extends TwoHandedLongSword implements AnimatedEffectWeapon {
   public HakuroItem(
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

   public HakuroItem(HakuroItem.Grade grade) {
      this(
         grade.material,
         grade.getDamage(),
         -2.0F,
         0.0,
         20.0,
         0.0,
         grade.getDamage() - 3,
         0.0,
         0.0,
         new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).defaultDurability(150).rarity(grade.getRarity())
      );
   }

   public static void applyEnchants(ItemStack toStack, EquipmentSlot toSlot) {
      Enchantment tsukumogami = (Enchantment)TensuraEnchantments.TSUKUMOGAMI.get();
      if (toSlot.getType().equals(Type.HAND) && toStack.getItem() instanceof HakuroItem && toStack.getEnchantmentLevel(tsukumogami) < 1) {
         EngravingEnchantment.engrave(toStack, tsukumogami, 1);
      }
   }

   @NotNull
   public InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pHand) {
      if (pPlayer.getCooldowns().isOnCooldown(this)) {
         return InteractionResultHolder.pass(pPlayer.getItemInHand(pHand));
      } else {
         if (SkillAPI.getSkillsFrom(pPlayer).getSkill((ManasSkill)BeyondUtilityArts.SKY_WALK.get()).isPresent()
               && ((ManasSkillInstance)SkillAPI.getSkillsFrom(pPlayer).getSkill((ManasSkill)BeyondUtilityArts.SKY_WALK.get()).get()).isToggled()
            || pPlayer.isOnGround()) {
            if (pLevel.isClientSide) {
               this.playAnimationClientSync(pPlayer);
            }

            pPlayer.getCooldowns().addCooldown(this, 300);
            pPlayer.level.playSound(null, pPlayer.blockPosition(), (SoundEvent)BeyondSoundEvents.HAKURO_ATTACK.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            this.performHakuroAttack(pPlayer);
         }

         return InteractionResultHolder.consume(pPlayer.getItemInHand(pHand));
      }
   }

   @Override
   public String getAnimationName() {
      return "hakuro_sword_attack";
   }

   @Override
   public AnimatedEffectWeapon.Effect getEffect() {
      return AnimatedEffectWeapon.Effect.HAKURO_SWORD;
   }

   public void performHakuroAttack(Player player) {
      Level world = player.level;
      if (!world.isClientSide) {
         SkillHelper.checkThenAddEffectSource(player, player, (MobEffect)BeyondEffects.DODGE.get(), 120, 0, false, false, false, true);
         MinecraftForge.EVENT_BUS.register(new HakuroItem.HakuroAttackHandler(player, world));
      }
   }

   public static enum Grade {
      UNIQUE(TensuraToolTiers.PURE_MAGISTEEL, 6, BeyondRarity.UNIQUE),
      LEGEND(TensuraToolTiers.ADAMANTITE, 5, BeyondRarity.LEGEND);

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

   private static class HakuroAttackHandler {
      private final Player player;
      private final Level world;
      private int ticks = 0;
      private float baseDamage = 0.0F;

      public HakuroAttackHandler(Player player, Level world) {
         this.player = player;
         this.world = world;
      }

      @SubscribeEvent
      public void onServerTick(ServerTickEvent event) {
         if (event.phase == Phase.END) {
            this.ticks++;
            if (this.ticks == 30) {
               AABB hitbox = new AABB(this.player.blockPosition()).inflate(4.0, 4.0, 4.0);
               List<LivingEntity> initialTargets = this.world.getEntitiesOfClass(LivingEntity.class, hitbox, e -> e != this.player && e.isAlive());
               float attackStrength = this.player.getAttackStrengthScale(1.0F);
               this.baseDamage = (float)this.player.getAttributeValue(Attributes.ATTACK_DAMAGE);
               this.baseDamage = (this.baseDamage * 0.2F + this.baseDamage) * attackStrength;
               DamageSource battlewillDamage = DamageSourceHelper.turnTensura(DamageSource.playerAttack(this.player), this.player);

               for (LivingEntity target : initialTargets) {
                  if (target.isAlive()) {
                     SkillStorage skills = SkillAPI.getSkillsFrom(this.player);
                     Optional<ManasSkillInstance> guillotinePresent = skills.getSkill((ManasSkill)MeleeArts.OGRE_SWORD_GUILLOTINE.get());
                     boolean hasGuillotine = guillotinePresent.isPresent();
                     SkillHelper.checkThenAddEffectSource(target, this.player, (MobEffect)BeyondEffects.NO_DODGE.get(), 120, 1, false, false, false, true);
                     if (this.player.hasEffect((MobEffect)TensuraMobEffects.MAGIC_AURA.get())) {
                        target.hurt(battlewillDamage, this.baseDamage * 5.0F);
                     } else if (this.player.hasEffect((MobEffect)TensuraMobEffects.OGRE_GUILLOTINE.get())) {
                        if (hasGuillotine) {
                           ManasSkillInstance instance = guillotinePresent.get();
                           if (instance.isMastered(this.player)) {
                              target.hurt(battlewillDamage, this.baseDamage * 1.0F);
                           } else {
                              target.hurt(battlewillDamage, this.baseDamage * 0.75F);
                           }
                        }
                     } else {
                        target.hurt(battlewillDamage, this.baseDamage * 2.0F);
                     }
                  }
               }
            }

            if (this.ticks >= 60) {
               MinecraftForge.EVENT_BUS.unregister(this);
               if (!this.player.isAlive()) {
                  return;
               }

               AABB hitboxSecond = new AABB(this.player.blockPosition()).inflate(10.0, 10.0, 10.0);
               List<LivingEntity> targetsInHitboxSecond = this.world.getEntitiesOfClass(LivingEntity.class, hitboxSecond, e -> e != this.player && e.isAlive());
               if (!targetsInHitboxSecond.isEmpty()) {
                  LivingEntity closestTarget = this.getClosestTarget(this.player, targetsInHitboxSecond);
                  if (closestTarget != null) {
                     Vec3 pos = closestTarget.position();
                     Vec3 from = this.player.getEyePosition();
                     Vec3 to = closestTarget.getEyePosition();
                     BlockHitResult hitResult = this.world.clip(new ClipContext(from, to, Block.COLLIDER, Fluid.NONE, this.player));
                     if (hitResult.getType() != net.minecraft.world.phys.HitResult.Type.MISS) {
                        return;
                     }

                     this.player.teleportTo(pos.x, pos.y + 1.0, pos.z);
                     TensuraDamageSource var22 = DamageSourceHelper.turnTensura(DamageSource.playerAttack(this.player), this.player);
                     SkillStorage skills = SkillAPI.getSkillsFrom(this.player);
                     Optional<ManasSkillInstance> guillotinePresent = skills.getSkill((ManasSkill)MeleeArts.OGRE_SWORD_GUILLOTINE.get());
                     boolean hasGuillotine = guillotinePresent.isPresent();
                     float secondStrikeDamage = this.baseDamage * 5.0F;
                     if (this.player.hasEffect((MobEffect)TensuraMobEffects.MAGIC_AURA.get())) {
                        secondStrikeDamage = this.baseDamage * 7.5F;
                     } else if (this.player.hasEffect((MobEffect)TensuraMobEffects.OGRE_GUILLOTINE.get()) && hasGuillotine) {
                        ManasSkillInstance instance = guillotinePresent.get();
                        if (instance.isMastered(this.player)) {
                           secondStrikeDamage *= 2.0F;
                        } else {
                           secondStrikeDamage *= 1.5F;
                        }
                     }

                     closestTarget.hurt(var22, secondStrikeDamage);
                  } else {
                     System.out.println("No valid targets found for second strike.");
                  }
               }
            }
         }
      }

      private LivingEntity getClosestTarget(Player player, List<LivingEntity> targets) {
         List<LivingEntity> hostilePlayers = new ArrayList<>();
         List<LivingEntity> bosses = new ArrayList<>();
         List<LivingEntity> others = new ArrayList<>();

         for (LivingEntity target : targets) {
            if (target instanceof Player playerTarget && player.canHarmPlayer(playerTarget)) {
               hostilePlayers.add(target);
               continue;
            }

            if (this.isBoss(target)) {
               bosses.add(target);
            } else {
               others.add(target);
            }
         }

         LivingEntity closest = this.findClosestInList(player, hostilePlayers);
         if (closest != null) {
            return closest;
         } else {
            closest = this.findClosestInList(player, bosses);
            return closest != null ? closest : this.findClosestInList(player, others);
         }
      }

      private boolean isBoss(LivingEntity entity) {
         return entity instanceof EnderDragon
            || entity instanceof WitherBoss
            || entity instanceof SupermassiveSlimeEntity
            || entity instanceof IfritEntity
            || entity instanceof UndineEntity
            || entity instanceof WarGnomeEntity
            || entity instanceof SylphideEntity
            || entity instanceof AkashEntity
            || entity instanceof OrcLordEntity
            || entity instanceof OrcDisasterEntity
            || entity instanceof CharybdisEntity
            || entity instanceof HinataSakaguchiEntity
            || entity instanceof ElementalColossusEntity
            || entity instanceof BossBlackSpiderEntity
            || entity instanceof MezulEntity
            || entity instanceof GozulEntity
            || entity instanceof OgreLordEntity
            || entity instanceof EvilCentipedeBossEntity
            || entity instanceof TempestSerpentBossEntity;
      }

      private LivingEntity findClosestInList(Player player, List<LivingEntity> entities) {
         if (entities.isEmpty()) {
            return null;
         } else {
            LivingEntity closest = null;
            double minDistanceSqr = Double.MAX_VALUE;
            Vec3 playerPos = player.position();

            for (LivingEntity entity : entities) {
               double distanceSqr = entity.position().distanceToSqr(playerPos);
               if (distanceSqr < minDistanceSqr) {
                  minDistanceSqr = distanceSqr;
                  closest = entity;
               }
            }

            return closest;
         }
      }
   }
}
