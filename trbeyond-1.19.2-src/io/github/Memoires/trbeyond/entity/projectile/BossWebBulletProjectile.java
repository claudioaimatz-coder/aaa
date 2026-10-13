package io.github.Memoires.trbeyond.entity.projectile;

import com.github.manasmods.tensura.entity.projectile.WebBulletProjectile;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.Memoires.trbeyond.entity.custom.BossBlackSpiderEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class BossWebBulletProjectile extends WebBulletProjectile {
   public BossWebBulletProjectile(EntityType<? extends WebBulletProjectile> type, Level level) {
      super(type, level);
   }

   public BossWebBulletProjectile(Level pLevel, double pX, double pY, double pZ) {
      super(pLevel, pX, pY, pZ);
   }

   public BossWebBulletProjectile(Level worldIn, LivingEntity shooter, boolean right, ItemStack sourceItem, ItemStack ammo) {
      super(worldIn, shooter, right, sourceItem, ammo);
   }

   public BossWebBulletProjectile(Level worldIn, LivingEntity shooter, boolean right, ItemStack ammo) {
      super(worldIn, shooter, right, ammo);
   }

   @Override
   public void tick() {
      super.tick();
      if (this.isSlinger()) {
         if (this.getOwner() instanceof BossBlackSpiderEntity owner) {
            Entity vehicle = this.getVehicle();
            if (vehicle == null) {
               return;
            }

            double f = (double)vehicle.distanceTo(owner);
            if (owner.getMiscAnimation() == -2) {
               double d0 = (owner.getX() - vehicle.getX()) / f;
               double d1 = (owner.getY() - vehicle.getY()) / f;
               double d2 = (owner.getZ() - vehicle.getZ()) / f;
               vehicle.setDeltaMovement(
                  vehicle.getDeltaMovement().add(Math.copySign(d0 * d0 * 0.2, d0), Math.copySign(d1 * d1 * 0.2, d1), Math.copySign(d2 * d2 * 0.2, d2))
               );
               vehicle.hurtMarked = true;
               if (f < 3.0) {
                  this.discard();
                  owner.setMiscAnimation(1);
                  owner.doHurtTarget(vehicle);
               }
            }
         }
      }
   }

   @Override
   protected void onHitEntity(EntityHitResult pResult) {
      Entity entity = pResult.getEntity();
      if (entity != this.getOwner()) {
         if (this.isSlinger() && entity instanceof LivingEntity pTarget) {
            if (this.getOwner() instanceof BossBlackSpiderEntity spider) {
               spider.setMiscAnimation(-2);
            }

            int duration = 600;
            pTarget.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.WEBBED.get(), duration, 0, false, false, true), this);
            pTarget.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.SILENCE.get(), duration, 0, false, false, true), this);
         }

         super.onHitEntity(pResult);
      }
   }

   @Override
   protected void onHitBlock(BlockHitResult pResult) {
      super.onHitBlock(pResult);
      if (!this.level.isClientSide && this.isSlinger()) {
         this.remove(RemovalReason.DISCARDED);
      }
   }
}
