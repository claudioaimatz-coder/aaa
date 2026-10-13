package io.github.Memoires.trbeyond.entity.misc;

import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlocks;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Entity.MovementEmission;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.block.Block;

public class PrimedDungeonChestEntity extends Entity {
   private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(PrimedDungeonChestEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<ItemStack> SOURCE_ITEM = SynchedEntityData.defineId(PrimedDungeonChestEntity.class, EntityDataSerializers.ITEM_STACK);
   private static final EntityDataAccessor<Direction> SPAWN_DIRECTION = SynchedEntityData.defineId(
      PrimedDungeonChestEntity.class, EntityDataSerializers.DIRECTION
   );

   public PrimedDungeonChestEntity(EntityType<? extends PrimedDungeonChestEntity> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
      this.blocksBuilding = true;
   }

   public PrimedDungeonChestEntity(Level pLevel, double pX, double pY, double pZ) {
      this((EntityType<? extends PrimedDungeonChestEntity>)BeyondEntities.PRIMED_CHEST.get(), pLevel);
      this.setPos(pX, pY, pZ);
      this.unstableJump();
      this.setFuse(50);
      this.xo = pX;
      this.yo = pY;
      this.zo = pZ;
   }

   private void unstableJump() {
      double d0 = this.level.random.nextDouble() * Math.PI * 2.0;
      this.setDeltaMovement(-Math.sin(d0) * 0.1, 0.3F, -Math.cos(d0) * 0.1);
   }

   public void tick() {
      if (!this.isNoGravity()) {
         this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
      }

      this.move(MoverType.SELF, this.getDeltaMovement());
      this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
      if (this.isOnGround()) {
         this.unstableJump();
      }

      int i = this.getFuse() - 1;
      this.setFuse(i);
      if (i <= 0) {
         this.discard();
         if (!this.level.isClientSide) {
            this.explode();
         }
      } else {
         this.updateInWaterStateAndDoFluidPushing();
         if (i % 10 != 0) {
            return;
         }

         TensuraParticleHelper.addServerParticlesAroundSelf(this, ParticleTypes.SMOKE);
         TensuraParticleHelper.addServerParticlesAroundSelf(this, ParticleTypes.LARGE_SMOKE);
      }
   }

   protected void explode() {
      this.level
         .explode(
            this,
            this.getX(),
            this.getY(0.0625),
            this.getZ(),
            12.0F,
            this.level.dimension() == BeyondDungeon.DUNGEON_LEVEL_KEY ? BlockInteraction.NONE : BlockInteraction.DESTROY
         );
      TensuraParticleHelper.addServerParticlesAroundSelf(this, ParticleTypes.EXPLOSION_EMITTER, 4.0);
      TensuraParticleHelper.addServerParticlesAroundSelf(this, ParticleTypes.SMOKE, 4.0);
      TensuraParticleHelper.addServerParticlesAroundSelf(this, ParticleTypes.FLASH, 4.0);
      TensuraParticleHelper.addServerParticlesAroundSelf(this, ParticleTypes.FLASH, 3.0);
      TensuraParticleHelper.addServerParticlesAroundSelf(this, (ParticleOptions)TensuraParticles.SOLAR_FLASH.get(), 3.0);
   }

   protected void defineSynchedData() {
      this.entityData.define(DATA_FUSE_ID, 80);
      this.entityData.define(SPAWN_DIRECTION, Direction.EAST);
      this.entityData.define(SOURCE_ITEM, ((Block)BeyondBlocks.GOLD_CHEST.get()).asItem().getDefaultInstance());
   }

   protected void addAdditionalSaveData(CompoundTag pCompound) {
      pCompound.putShort("Fuse", (short)this.getFuse());
      pCompound.putInt("Direction", this.getDirection().get3DDataValue());
      if (pCompound.contains("Source Item", 10)) {
         this.setSourceItem(ItemStack.of(pCompound.getCompound("Source Item")));
      }
   }

   protected void readAdditionalSaveData(CompoundTag pCompound) {
      this.setFuse(pCompound.getShort("Fuse"));
      this.setSpawnDirection(Direction.from3DDataValue(pCompound.getInt("Direction")));
      pCompound.put("Source Item", this.getSourceItem().save(new CompoundTag()));
   }

   protected float getEyeHeight(Pose pPose, EntityDimensions pSize) {
      return 0.15F;
   }

   public void setFuse(int pLife) {
      this.entityData.set(DATA_FUSE_ID, pLife);
   }

   public int getFuse() {
      return (Integer)this.entityData.get(DATA_FUSE_ID);
   }

   public void setSpawnDirection(Direction pLife) {
      this.entityData.set(SPAWN_DIRECTION, pLife);
   }

   public Direction getSpawnDirection() {
      return (Direction)this.entityData.get(SPAWN_DIRECTION);
   }

   public ItemStack getSourceItem() {
      return (ItemStack)this.entityData.get(SOURCE_ITEM);
   }

   public void setSourceItem(ItemStack pStack) {
      this.entityData.set(SOURCE_ITEM, pStack);
   }

   public Packet<?> getAddEntityPacket() {
      return new ClientboundAddEntityPacket(this);
   }

   protected MovementEmission getMovementEmission() {
      return MovementEmission.NONE;
   }

   public boolean isPickable() {
      return !this.isRemoved();
   }
}
