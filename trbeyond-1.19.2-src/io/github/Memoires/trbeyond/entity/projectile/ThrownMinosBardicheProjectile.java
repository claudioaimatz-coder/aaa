package io.github.Memoires.trbeyond.entity.projectile;

import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.registry.items.TensuraToolItems;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import io.github.Memoires.trbeyond.items.weapons.MinosBardicheItem;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import io.github.Memoires.trbeyond.util.BeyondDamageSources;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractArrow.Pickup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownMinosBardicheProjectile extends AbstractArrow {
   protected static final EntityDataAccessor<Integer> RETURN_SECOND = SynchedEntityData.defineId(
      ThrownMinosBardicheProjectile.class, EntityDataSerializers.INT
   );
   protected static final EntityDataAccessor<Integer> LOYALTY_LEVEL = SynchedEntityData.defineId(
      ThrownMinosBardicheProjectile.class, EntityDataSerializers.INT
   );
   private static final EntityDataAccessor<ItemStack> SOURCE_ITEM = SynchedEntityData.defineId(
      ThrownMinosBardicheProjectile.class, EntityDataSerializers.ITEM_STACK
   );
   private static final EntityDataAccessor<Boolean> SPINNING = SynchedEntityData.defineId(ThrownMinosBardicheProjectile.class, EntityDataSerializers.BOOLEAN);
   private boolean finishPiercing;
   private int piercingEntity = 0;
   public int clientSideReturnKunaiTickCount;

   public ThrownMinosBardicheProjectile(EntityType<? extends ThrownMinosBardicheProjectile> type, Level level) {
      super(type, level);
   }

   public ThrownMinosBardicheProjectile(Level worldIn, LivingEntity shooter, ItemStack pStack, boolean right) {
      super((EntityType)BeyondEntities.MINOS_BARDICHE.get(), shooter, worldIn);
      this.setSourceItem(pStack.copy());
      float rot = shooter.yHeadRot + (float)(right ? 60 : -60);
      this.setPos(
         shooter.getX() - (double)shooter.getBbWidth() * 0.5 * (double)Mth.sin(rot * (float) (Math.PI / 180.0)),
         shooter.getEyeY() - 0.2F,
         shooter.getZ() + (double)shooter.getBbWidth() * 0.5 * (double)Mth.cos(rot * (float) (Math.PI / 180.0))
      );
   }

   public ThrownMinosBardicheProjectile(Level pLevel, double pX, double pY, double pZ) {
      super((EntityType)BeyondEntities.MINOS_BARDICHE.get(), pX, pY, pZ, pLevel);
   }

   public ItemStack getPickupItem() {
      return this.getSourceItem();
   }

   @Nullable
   protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
      return this.finishPiercing ? null : super.findHitEntity(pStartVec, pEndVec);
   }

   private boolean isAcceptableReturnOwner() {
      Entity entity = this.getOwner();
      if (entity == null) {
         return false;
      } else {
         return !entity.isAlive() ? false : !(entity instanceof ServerPlayer) || !entity.isSpectator();
      }
   }

   public void tick() {
      if (this.inGroundTime > 0) {
         this.setShouldSpin(false);
      }

      if (this.inGroundTime > 4 || this.getReturnSecond() > 0 && this.tickCount >= this.getReturnSecond()) {
         this.finishPiercing = true;
      }

      Entity owner = this.getOwner();
      int i = this.getLoyaltyLevel();
      if (i > 0 && (this.finishPiercing || this.isNoPhysics()) && owner != null && this.isAlive()) {
         if (!this.isAcceptableReturnOwner()) {
            if (!this.level.isClientSide && this.pickup == Pickup.ALLOWED) {
               this.spawnAtLocation(this.getPickupItem(), 0.1F);
            }

            this.discard();
         } else {
            this.setNoPhysics(true);
            Vec3 vec3 = owner.getEyePosition().subtract(this.position());
            this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015 * (double)i, this.getZ());
            if (this.level.isClientSide) {
               this.yOld = this.getY();
            }

            double d0 = 0.05 * (double)i;
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec3.normalize().scale(d0)));
            if (this.clientSideReturnKunaiTickCount == 0) {
               this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
            }

            this.clientSideReturnKunaiTickCount++;
            this.setShouldSpin(true);
         }
      }

      super.tick();
      if (!this.level.isClientSide()
         && (this.shouldSpin() && this.tickCount % 2 == 0 || this.tickCount % 10 == 0)
         && MinosBardicheItem.getWindCores(this.getSourceItem()) >= 3) {
         TensuraParticleHelper.addServerParticlesAroundSelf(this, (ParticleOptions)TensuraParticles.LIGHTNING_SPARK.get(), 2.0);
      }
   }

   protected void onHitEntity(EntityHitResult pResult) {
      Entity entity = pResult.getEntity();
      float projectileDamage = (float)this.getBaseDamage();
      if (entity instanceof LivingEntity livingentity) {
         projectileDamage += EnchantmentHelper.getDamageBonus(this.getSourceItem(), livingentity.getMobType());
      }

      Entity ownerEntity = this.getOwner();
      DamageSource damagesource = BeyondDamageSources.minosBardiche(this, Objects.requireNonNullElse(ownerEntity, this));
      if (entity.hurt(damagesource, projectileDamage)) {
         if (entity.getType() == EntityType.ENDERMAN) {
            return;
         }

         if (entity instanceof LivingEntity target) {
            if (ownerEntity instanceof LivingEntity owner) {
               ItemStack sourceStack = this.getSourceItem();
               if (owner instanceof Player player) {
                  MinosBardicheItem.dealLightningDamage(player, target, sourceStack);
                  sourceStack.hurtEnemy(target, player);
               }

               EnchantmentHelper.doPostHurtEffects(target, ownerEntity);
               EnchantmentHelper.doPostDamageEffects(owner, target);
               EngravingEnchantment.doAdditionalAttack(sourceStack, owner, entity, projectileDamage);
               pResult.getEntity().setSecondsOnFire(3 * sourceStack.getEnchantmentLevel(Enchantments.FIRE_ASPECT));
            }

            this.doPostHurtEffects(target);
         }
      }

      this.piercingEntity++;
      if (this.piercingEntity >= 1 + this.getSourceItem().getEnchantmentLevel(Enchantments.PIERCING)) {
         this.finishPiercing = true;
         this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
         float f1 = 1.0F;
         this.playSound(SoundEvents.TRIDENT_HIT, f1, 1.0F);
      }
   }

   protected boolean tryPickup(Player pPlayer) {
      return super.tryPickup(pPlayer) || this.isNoPhysics() && this.ownedBy(pPlayer) && pPlayer.getInventory().add(this.getPickupItem());
   }

   protected SoundEvent getDefaultHitGroundSoundEvent() {
      return SoundEvents.TRIDENT_HIT_GROUND;
   }

   public void playerTouch(Player pEntity) {
      if (this.ownedBy(pEntity) || this.getOwner() == null) {
         super.playerTouch(pEntity);
      }
   }

   public ItemStack getSourceItem() {
      return (ItemStack)this.entityData.get(SOURCE_ITEM);
   }

   public void setSourceItem(ItemStack pStack) {
      this.entityData.set(SOURCE_ITEM, pStack);
      this.setLoyaltyLevel(EnchantmentHelper.getLoyalty(pStack));
   }

   public int getLoyaltyLevel() {
      return (Integer)this.entityData.get(LOYALTY_LEVEL);
   }

   public void setLoyaltyLevel(int level) {
      this.entityData.set(LOYALTY_LEVEL, level);
   }

   public int getReturnSecond() {
      return (Integer)this.entityData.get(RETURN_SECOND);
   }

   public void setReturnSecond(int second) {
      this.entityData.set(RETURN_SECOND, second);
   }

   public boolean shouldSpin() {
      return (Boolean)this.entityData.get(SPINNING);
   }

   public void setShouldSpin(boolean onGround) {
      this.entityData.set(SPINNING, onGround);
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(RETURN_SECOND, 0);
      this.entityData.define(LOYALTY_LEVEL, 0);
      this.entityData.define(SOURCE_ITEM, ((Item)TensuraToolItems.DIAMOND_SPEAR.get()).getDefaultInstance());
      this.entityData.define(SPINNING, Boolean.TRUE);
   }

   public void readAdditionalSaveData(CompoundTag pCompound) {
      super.readAdditionalSaveData(pCompound);
      if (pCompound.contains("Source Item", 10)) {
         this.setSourceItem(ItemStack.of(pCompound.getCompound("Source Item")));
      }

      this.setLoyaltyLevel(pCompound.getInt("Loyalty"));
      this.setReturnSecond(pCompound.getInt("ReturnSecond"));
      this.setShouldSpin(pCompound.getBoolean("ShouldSpin"));
      this.finishPiercing = pCompound.getBoolean("DealtDamage");
   }

   public void addAdditionalSaveData(CompoundTag pCompound) {
      super.addAdditionalSaveData(pCompound);
      pCompound.putBoolean("DealtDamage", this.finishPiercing);
      pCompound.put("Source Item", this.getSourceItem().save(new CompoundTag()));
      pCompound.putInt("Loyalty", this.getLoyaltyLevel());
      pCompound.putInt("ReturnSecond", this.getReturnSecond());
      pCompound.putBoolean("ShouldSpin", this.shouldSpin());
   }

   public void tickDespawn() {
      int i = this.getLoyaltyLevel();
      if (this.pickup != Pickup.ALLOWED || i <= 0) {
         super.tickDespawn();
      }
   }

   protected float getWaterInertia() {
      return 0.79F + (float)this.getSourceItem().getEnchantmentLevel(Enchantments.IMPALING) * 0.04F;
   }
}
