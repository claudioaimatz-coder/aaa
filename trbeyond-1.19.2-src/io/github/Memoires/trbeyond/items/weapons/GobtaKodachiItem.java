package io.github.Memoires.trbeyond.items.weapons;

import com.github.manasmods.tensura.entity.magic.lightning.BlackLightningBolt;
import com.github.manasmods.tensura.item.TensuraToolTiers;
import com.github.manasmods.tensura.item.templates.custom.SimpleShortSwordItem;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class GobtaKodachiItem extends SimpleShortSwordItem {
   private final GobtaKodachiItem.Grade grade;

   public GobtaKodachiItem(GobtaKodachiItem.Grade grade) {
      super(
         grade.getMaterial(),
         grade.getDamage(),
         -2.0F,
         0.2,
         0.5,
         100.0,
         new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).defaultDurability(150).rarity(grade.getRarity())
      );
      this.grade = grade;
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (!level.isClientSide && !player.getCooldowns().isOnCooldown(this)) {
         player.getCooldowns().addCooldown(this, 1200);
         Vec3 eyePos = player.getEyePosition(1.0F);
         Vec3 lookVec = player.getLookAngle();
         Vec3 endPos = eyePos.add(lookVec.scale(10.0));
         AABB box = player.getBoundingBox().expandTowards(lookVec.scale(10.0)).inflate(1.0);
         EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(
            level, player, eyePos, endPos, box, entity -> entity instanceof LivingEntity && entity != player && entity.isPickable()
         );
         BlackLightningBolt lightning = new BlackLightningBolt(level, player);
         lightning.moveTo(endPos.x, endPos.y, endPos.z);
         level.addFreshEntity(lightning);
         if (hitResult != null && hitResult.getEntity() instanceof LivingEntity target) {
            target.hurt(DamageSource.playerAttack(player), 20.0F);
            target.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.SEVERANCE_BLADE.get(), 200, 0, false, true));
         }

         level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 1.0F, 1.0F);
      }

      return InteractionResultHolder.success(stack);
   }

   public static enum Grade {
      RARE(TensuraToolTiers.HIGH_MAGISTEEL, 11, BeyondRarity.RARE),
      UNIQUE(TensuraToolTiers.PURE_MAGISTEEL, 3, BeyondRarity.UNIQUE);

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
