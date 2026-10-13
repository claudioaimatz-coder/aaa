package io.github.Memoires.trbeyond.entity.projectile;

import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.entity.magic.barrier.BarrierEntity;
import com.github.manasmods.tensura.network.TensuraNetwork;
import com.github.manasmods.tensura.network.play2client.RequestFxSpawningPacket;
import com.github.manasmods.tensura.race.RaceHelper;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.Memoires.trbeyond.entity.custom.OgreLordEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

public class OgreFlameProjectile extends BarrierEntity {
   private LivingEntity target;

   public OgreFlameProjectile(Level level, LivingEntity entity, LivingEntity target) {
      this((EntityType<? extends OgreFlameProjectile>)TensuraEntityTypes.FLARE_CIRCLE.get(), level);
      this.setOwner(entity);
      this.setTarget(target);
   }

   public OgreFlameProjectile(EntityType<? extends OgreFlameProjectile> entityType, Level level) {
      super(entityType, level);
   }

   @Override
   public boolean canWalkThrough() {
      return this.getOwner() instanceof OgreLordEntity;
   }

   @Override
   public boolean blockBuilding() {
      return false;
   }

   @Override
   public boolean hurt(DamageSource pSource, float pAmount) {
      return false;
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.level.isClientSide()) {
         if (this.getMpCost() != 0.0) {
            TensuraNetwork.INSTANCE
               .send(
                  PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this),
                  new RequestFxSpawningPacket(new ResourceLocation("tensura:flare_circle_circle"), this.getId(), 0.0, 0.5, 0.0, true)
               );
         }

         TensuraNetwork.INSTANCE
            .send(
               PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this),
               new RequestFxSpawningPacket(new ResourceLocation("tensura:flare_circle_flare"), this.getId(), 0.0, 5.5, 0.0, true)
            );
      }
   }

   @Override
   public void applyEffect(LivingEntity entity) {
      if (!this.isAlly(entity)) {
         DamageSource source = this.getOwner() == null ? DamageSource.ON_FIRE : TensuraDamageSources.burn(this.getOwner());
         if (entity.hurt(source, this.getDamage())) {
            entity.setSecondsOnFire(8);
         }

         if (this.shouldConsumeAir(entity)) {
            entity.setAirSupply(entity.getAirSupply() - 30);
            if (entity.getAirSupply() == -20) {
               entity.setAirSupply(0);
               entity.hurt(TensuraDamageSources.SUFFOCATE, 2.0F);
            }
         }
      }
   }

   private boolean isAlly(LivingEntity target) {
      Entity owner = this.getOwner();
      if (target == owner) {
         return true;
      } else {
         if (owner instanceof LivingEntity entity) {
            if (target.isAlliedTo(entity)) {
               return true;
            }

            if (owner instanceof OgreLordEntity ogreLord) {
               return !ogreLord.shouldAttack(target);
            }
         }

         return false;
      }
   }

   private boolean shouldConsumeAir(LivingEntity entity) {
      if (TensuraEPCapability.isMajin(entity)) {
         return false;
      } else {
         if (entity instanceof Player player) {
            if (player.isCreative()) {
               return false;
            }

            if (RaceHelper.isSpiritualLifeForm(player)) {
               return false;
            }
         }

         return !entity.isSpectator();
      }
   }

   public void setTarget(LivingEntity target) {
      this.target = target;
   }

   public LivingEntity getTarget() {
      return this.target;
   }
}
