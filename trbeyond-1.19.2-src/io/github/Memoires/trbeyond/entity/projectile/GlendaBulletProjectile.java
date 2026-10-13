package io.github.Memoires.trbeyond.entity.projectile;

import com.github.manasmods.tensura.entity.magic.TensuraProjectile;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GlendaBulletProjectile extends TensuraProjectile {
   @Nullable
   protected DamageSource damageSource;
   @Nullable
   protected Entity target;

   public GlendaBulletProjectile(EntityType<? extends GlendaBulletProjectile> entityType, Level level) {
      super(entityType, level);
      this.setSize(0.25F);
   }

   public GlendaBulletProjectile(Level pLevel, LivingEntity shooter) {
      super((EntityType<? extends Projectile>)BeyondEntities.GLENDA_BULLET.get(), pLevel);
      this.setOwner(shooter);
   }

   public GlendaBulletProjectile(Level pLevel, LivingEntity shooter, boolean right) {
      super((EntityType<? extends Projectile>)BeyondEntities.GLENDA_BULLET.get(), pLevel);
      this.setOwner(shooter);
      this.setSize(0.25F);
      float rot = shooter.yHeadRot + (float)(right ? 60 : -60);
      this.setPos(
         shooter.getX() - (double)shooter.getBbWidth() * 0.5 * (double)Mth.sin(rot * (float) (Math.PI / 180.0)),
         shooter.getEyeY() - 0.2F,
         shooter.getZ() + (double)shooter.getBbWidth() * 0.5 * (double)Mth.cos(rot * (float) (Math.PI / 180.0))
      );
   }

   @Override
   public boolean shouldDiscardInWater() {
      return false;
   }

   @Override
   public boolean shouldDiscardInLava() {
      return false;
   }

   @Override
   protected void dealDamage(Entity target) {
      if (!(this.damage <= 0.0F) && target != this.getOwner()) {
         DamageSource source = TensuraDamageSources.shot(this, this.getOwner());
         if (this.damageSource != null) {
            source = this.damageSource;
         }

         if (!target.hurt(DamageSourceHelper.addSkillAndCost(source, this.getMpCost(), this.getSkill()), this.getDamage())) {
            this.discard();
         }
      }
   }

   @Override
   public Optional<SoundEvent> hitSound() {
      return Optional.of(SoundEvents.ITEM_BREAK);
   }

   @Override
   public void hitParticles(double x, double y, double z) {
   }

   @Override
   public void flyingParticles() {
      Vec3 vec3 = this.position().subtract(this.getDeltaMovement().scale(2.0));
      this.level.addParticle(ParticleTypes.SMOKE, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
   }

   @Nullable
   public DamageSource getDamageSource() {
      return this.damageSource;
   }

   public void setDamageSource(@Nullable DamageSource damageSource) {
      this.damageSource = damageSource;
   }

   @Nullable
   public Entity getTarget() {
      return this.target;
   }

   public void setTarget(@Nullable Entity target) {
      this.target = target;
   }
}
