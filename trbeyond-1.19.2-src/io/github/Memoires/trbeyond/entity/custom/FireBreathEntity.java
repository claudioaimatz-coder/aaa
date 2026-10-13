package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.util.damage.TensuraDamageSource;
import io.github.Memoires.trbeyond.procedures.FireBreathWhileProjectileFlyingTickProcedure;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages.SpawnEntity;

@OnlyIn(
   value = Dist.CLIENT,
   _interface = ItemSupplier.class
)
public class FireBreathEntity extends AbstractArrow implements ItemSupplier {
   public FireBreathEntity(SpawnEntity packet, Level world) {
      super((EntityType)BeyondEntities.FIRE_BREATH.get(), world);
   }

   public FireBreathEntity(EntityType<? extends FireBreathEntity> type, Level world) {
      super(type, world);
   }

   public FireBreathEntity(EntityType<? extends FireBreathEntity> type, double x, double y, double z, Level world) {
      super(type, x, y, z, world);
   }

   public FireBreathEntity(EntityType<? extends FireBreathEntity> type, LivingEntity entity, Level world) {
      super(type, entity, world);
   }

   public Packet<?> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   @OnlyIn(Dist.CLIENT)
   public ItemStack getItem() {
      return new ItemStack(Blocks.AIR);
   }

   protected ItemStack getPickupItem() {
      return ItemStack.EMPTY;
   }

   protected void doPostHurtEffects(LivingEntity entity) {
      super.doPostHurtEffects(entity);
      entity.setArrowCount(entity.getArrowCount() - 1);
   }

   public void tick() {
      super.tick();
      FireBreathWhileProjectileFlyingTickProcedure.execute(this.getLevel(), this.getX(), this.getY(), this.getZ(), this.getOwner(), this);
      if (this.inGround) {
         this.discard();
      }
   }

   protected void onHitEntity(EntityHitResult pResult) {
      float f = (float)this.getDeltaMovement().length();
      int i = Mth.ceil(Mth.clamp((double)f * this.getBaseDamage(), 0.0, 2.147483647E9));
      if (this.isCritArrow()) {
         long j = (long)this.random.nextInt(i / 2 + 2);
         i = (int)Math.min(j + (long)i, 2147483647L);
      }

      if (pResult.getEntity() instanceof LivingEntity living) {
         living.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.ANTI_SKILL.get(), 200, 2));
         DamageSource source = new TensuraDamageSource("tensura.black_flame").setIgnoreResistance(100.0F).setIsFire().bypassEnchantments().setMagic();
         living.hurt(source, (float)i);
      }
   }

   public static FireBreathEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
      FireBreathEntity entityarrow = new FireBreathEntity((EntityType<? extends FireBreathEntity>)BeyondEntities.FIRE_BREATH.get(), entity, world);
      entityarrow.shoot(entity.getViewVector(1.0F).x, entity.getViewVector(1.0F).y, entity.getViewVector(1.0F).z, power * 2.0F, 0.0F);
      entityarrow.setSilent(true);
      entityarrow.setCritArrow(false);
      entityarrow.setBaseDamage(damage);
      entityarrow.setKnockback(knockback);
      world.addFreshEntity(entityarrow);
      return entityarrow;
   }

   public static FireBreathEntity shoot(LivingEntity entity, LivingEntity target) {
      FireBreathEntity entityarrow = new FireBreathEntity((EntityType<? extends FireBreathEntity>)BeyondEntities.FIRE_BREATH.get(), entity, entity.level);
      double dx = target.getX() - entity.getX();
      double dy = target.getY() + (double)target.getEyeHeight() - 1.1;
      double dz = target.getZ() - entity.getZ();
      entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 2.0F, 12.0F);
      entityarrow.setSilent(true);
      entityarrow.setBaseDamage(1.0);
      entityarrow.setKnockback(1);
      entityarrow.setCritArrow(false);
      entity.level.addFreshEntity(entityarrow);
      return entityarrow;
   }
}
