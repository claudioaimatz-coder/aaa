package io.github.Memoires.trbeyond.entity.projectile;

import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSource;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.Memoires.trbeyond.registry.effects.BeyondEffects;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import java.util.Comparator;
import java.util.List;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public class ChaosEaterProjectile extends AbstractArrow {
   private int lifetime = 0;
   private static final float BASE_DAMAGE = 50.0F;
   private LivingEntity currentTarget;
   private float health = 200.0F;

   public ChaosEaterProjectile(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
      this.setNoGravity(true);
   }

   public ChaosEaterProjectile(Level pLevel, Vec3 pos) {
      super((EntityType)BeyondEntities.CHAOS_EATER.get(), pLevel);
      this.setPos(pos);
      this.setNoGravity(true);
   }

   protected ItemStack getPickupItem() {
      return ItemStack.EMPTY;
   }

   public void tick() {
      super.tick();
      if (!this.level.isClientSide) {
         try {
            this.calculateLifetime();
            AABB area = new AABB(this.position().subtract(20.0, 20.0, 20.0), this.position().add(20.0, 20.0, 20.0));
            List<LivingEntity> targets = this.level.getEntitiesOfClass(LivingEntity.class, area, entity -> true);
            targets.sort(Comparator.comparingDouble(entity -> entity.distanceToSqr(this.position())));
            LivingEntity currentTarget = targets.get(0);
            Vec3 direction;
            if (!currentTarget.isSpectator() && currentTarget.isAlive() && currentTarget != this.getOwner() && this.hasLineOfSight(currentTarget)) {
               direction = currentTarget.position().add(0.0, 1.0, 0.0).subtract(this.position()).normalize();
            } else {
               direction = this.getDeltaMovement().normalize();
            }

            Vec3 currentVelocity = this.getDeltaMovement();
            Vec3 newVelocity = currentVelocity.add(direction.scale(0.4)).normalize().scale(0.5);
            this.setDeltaMovement(newVelocity);
            this.hasImpulse = true;
            double dx = direction.x;
            double dy = direction.y;
            double dz = direction.z;
            float yaw = (float)(Mth.atan2(dx, dz) * (180.0 / Math.PI));
            float pitch = (float)(Mth.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * (180.0 / Math.PI));
            this.setYRot(yaw);
            this.setXRot(pitch);
            this.yRotO = yaw;
            this.xRotO = pitch;
         } catch (Exception var15) {
         }
      }
   }

   protected void onHitBlock(BlockHitResult pResult) {
      this.discard();
   }

   protected void onHitEntity(EntityHitResult pResult) {
      Entity entity = pResult.getEntity();
      if (entity != null && !this.level.isClientSide) {
         Entity owner = this.getOwner();
         if (owner != null) {
            entity.hurt(DamageSource.arrow(this, owner), 50.0F);
         } else {
            entity.hurt(DamageSource.arrow(this, this), 50.0F);
         }

         if (entity instanceof LivingEntity livingTarget && owner instanceof LivingEntity livingOwner) {
            livingTarget.hurt(TensuraDamageSources.corrosion(livingOwner), 50.0F);
            DamageSourceHelper.directSpiritualHurt(livingTarget, livingOwner, 50.0F, 0.0F);
            MobEffect rotEffect = (MobEffect)BeyondEffects.ROT.get();
            if (rotEffect != null) {
               SkillHelper.checkThenAddEffectSource(livingTarget, livingOwner, (MobEffect)BeyondEffects.ROT.get(), 1200, 0, false, false, true, true);
            }

            ItemStack mainHandItem = livingOwner.getMainHandItem();
            if (EnchantmentHelper.getItemEnchantmentLevel((Enchantment)BeyondEnchantments.LAND_INFUSION.get(), mainHandItem) > 0
               || EnchantmentHelper.getItemEnchantmentLevel((Enchantment)BeyondEnchantments.EARTH_INFUSION.get(), mainHandItem) > 0) {
               MobEffect burdenEffect = (MobEffect)TensuraMobEffects.BURDEN.get();
               if (burdenEffect != null) {
                  SkillHelper.checkThenAddEffectSource(livingTarget, livingOwner, (MobEffect)TensuraMobEffects.BURDEN.get(), 1200, 2, false, false, true, true);
               }
            }

            EnchantmentHelper.doPostHurtEffects(livingTarget, livingOwner);
            EnchantmentHelper.doPostDamageEffects(livingOwner, livingTarget);
         }
      }
   }

   public boolean hurt(DamageSource source, float amount) {
      if (DamageSourceHelper.isPhysicalAttack(source) && !canBypassPhysical(source)) {
         return false;
      } else if (this.isInvulnerableTo(source)) {
         return false;
      } else {
         this.damageProjectile(amount);
         return true;
      }
   }

   private void calculateLifetime() {
      if (++this.lifetime >= 120) {
         this.discard();
      }
   }

   private void damageProjectile(float amount) {
      this.health -= amount;
      if (this.health <= 0.0F) {
         this.discard();
      }
   }

   public boolean hasLineOfSight(Entity pEntity) {
      Vec3 vec3 = new Vec3(this.getX(), this.getEyeY(), this.getZ());
      Vec3 vec31 = new Vec3(pEntity.getX(), pEntity.getEyeY(), pEntity.getZ());
      return this.level.clip(new ClipContext(vec3, vec31, Block.COLLIDER, Fluid.NONE, this)).getType() == Type.MISS;
   }

   public boolean isPickable() {
      return true;
   }

   public boolean isAttackable() {
      return true;
   }

   private static boolean canBypassPhysical(DamageSource source) {
      if (source.getEntity() instanceof LivingEntity living) {
         if (living.hasEffect((MobEffect)TensuraMobEffects.HAKI_COAT.get())) {
            return true;
         } else if (living.hasEffect((MobEffect)TensuraMobEffects.MAGIC_AURA.get())) {
            return true;
         } else {
            MobEffectInstance sev = living.getEffect((MobEffect)TensuraMobEffects.SEVERANCE_BLADE.get());
            if (sev != null && sev.getAmplifier() >= 4) {
               return true;
            } else {
               if (source instanceof TensuraDamageSource tens && tens.getIgnoreResistance() == 1.0F) {
                  return true;
               }

               return SkillUtils.reducingResistances(living);
            }
         }
      } else {
         return false;
      }
   }
}
