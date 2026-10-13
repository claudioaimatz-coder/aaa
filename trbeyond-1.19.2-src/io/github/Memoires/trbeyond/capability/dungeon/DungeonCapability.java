package io.github.Memoires.trbeyond.capability.dungeon;

import io.github.Memoires.trbeyond.block.ShrineBlock;
import io.github.Memoires.trbeyond.handlers.CapabilityHandler;
import io.github.Memoires.trbeyond.network.BeyondNetwork;
import io.github.Memoires.trbeyond.network.SyncDungeonCapabilityPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.FORGE
)
public class DungeonCapability implements IDungeonCapability {
   private static final Logger log = LogManager.getLogger(DungeonCapability.class);
   public static final Capability<IDungeonCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<IDungeonCapability>() {
   });
   private static final ResourceLocation IDENTIFIER = new ResourceLocation("trbeyond", "dungeon");
   private boolean revivalBracelet;
   private boolean takenVoucher;
   private double epLimit;
   private BlockPos overworldCoords;
   public int bronzeChallengeCompletionCount = 0;
   public int silverChallengeCompletionCount = 0;
   public int goldChallengeCompletionCount = 0;
   public int stellarChallengeCompletionCount = 0;

   @SubscribeEvent
   public static void attach(AttachCapabilitiesEvent<Entity> e) {
      e.addCapability(IDENTIFIER, new DungeonCapabilityProvider());
   }

   public static LazyOptional<IDungeonCapability> getFrom(LivingEntity entity) {
      return entity.getCapability(CAPABILITY);
   }

   public static void sync(LivingEntity entity) {
      if (!entity.level.isClientSide()) {
         getFrom(entity)
            .ifPresent(
               data -> BeyondNetwork.INSTANCE
                     .send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new SyncDungeonCapabilityPacket(data, entity.getId()))
            );
      }
   }

   public DungeonCapability() {
      this.revivalBracelet = false;
      this.takenVoucher = false;
      this.epLimit = 0.0;
      this.overworldCoords = new BlockPos(0, 0, 0);
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      tag.putBoolean("revivalBracelet", this.revivalBracelet);
      tag.putBoolean("takenVoucher", this.takenVoucher);
      tag.putDouble("epLimit", this.epLimit);
      tag.putLong("overworldPos", this.overworldCoords.asLong());
      tag.putInt("bronze_challenges_completed", this.bronzeChallengeCompletionCount);
      tag.putInt("silver_challenges_completed", this.silverChallengeCompletionCount);
      tag.putInt("gold_challenges_completed", this.goldChallengeCompletionCount);
      tag.putInt("stellar_challenges_completed", this.stellarChallengeCompletionCount);
      return tag;
   }

   public void deserializeNBT(CompoundTag tag) {
      this.revivalBracelet = tag.getBoolean("revivalBracelet");
      this.takenVoucher = tag.getBoolean("takenVoucher");
      this.epLimit = tag.getDouble("epLimit");
      this.overworldCoords = BlockPos.of(tag.getLong("overworldPos"));
      this.bronzeChallengeCompletionCount = tag.getInt("bronze_challenges_completed");
      this.silverChallengeCompletionCount = tag.getInt("silver_challenges_completed");
      this.goldChallengeCompletionCount = tag.getInt("gold_challenges_completed");
      this.stellarChallengeCompletionCount = tag.getInt("stellar_challenges_completed");
   }

   public static boolean hasRevivalBracelet(LivingEntity entity) {
      IDungeonCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
      return capability == null ? false : capability.isRevivalBracelet();
   }

   public static void setRevivalBracelet(LivingEntity pLivingEntity, boolean value) {
      getFrom(pLivingEntity).ifPresent(cap -> cap.setRevivalBracelet(value));
      sync(pLivingEntity);
   }

   public static boolean hasTakenVoucher(LivingEntity entity) {
      IDungeonCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
      return capability == null ? false : capability.isTakenVoucher();
   }

   public static void setTakenVoucher(LivingEntity pLivingEntity, boolean value) {
      getFrom(pLivingEntity).ifPresent(cap -> cap.setTakenVoucher(value));
      sync(pLivingEntity);
   }

   public static double getEpLimit(LivingEntity entity) {
      IDungeonCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
      return capability == null ? 0.0 : capability.getEpLimit();
   }

   public static void setEpLimit(LivingEntity pLivingEntity, double value) {
      getFrom(pLivingEntity).ifPresent(cap -> cap.setEpLimit(value));
      sync(pLivingEntity);
   }

   public static BlockPos getOverworldCoords(LivingEntity entity) {
      IDungeonCapability capability = CapabilityHandler.getCapability(entity, CAPABILITY);
      return capability == null ? new BlockPos(0, 0, 0) : capability.getOverworldCoords();
   }

   public static void setOverworldCoords(LivingEntity pLivingEntity, BlockPos coords) {
      getFrom(pLivingEntity).ifPresent(cap -> cap.setOverworldCoords(coords));
      sync(pLivingEntity);
   }

   @Override
   public int getChallengeCompletionCount(ShrineBlock.DungeonShrineTier tier) {
      return switch (tier) {
         case BRONZE -> this.bronzeChallengeCompletionCount;
         case SILVER -> this.silverChallengeCompletionCount;
         case GOLD -> this.goldChallengeCompletionCount;
         case STELLAR -> this.stellarChallengeCompletionCount;
      };
   }

   @Override
   public int getTotalChallengeCompletionCount() {
      return this.bronzeChallengeCompletionCount
         + this.silverChallengeCompletionCount
         + this.goldChallengeCompletionCount
         + this.stellarChallengeCompletionCount;
   }

   @Override
   public void setChallengeCompletionCount(ShrineBlock.DungeonShrineTier tier, Integer count) {
      switch (tier) {
         case BRONZE:
            this.bronzeChallengeCompletionCount = count;
            break;
         case SILVER:
            this.silverChallengeCompletionCount = count;
            break;
         case GOLD:
            this.goldChallengeCompletionCount = count;
            break;
         case STELLAR:
            this.stellarChallengeCompletionCount = count;
      }
   }

   @Override
   public boolean isRevivalBracelet() {
      return this.revivalBracelet;
   }

   @Override
   public boolean isTakenVoucher() {
      return this.takenVoucher;
   }

   @Override
   public void setRevivalBracelet(boolean revivalBracelet) {
      this.revivalBracelet = revivalBracelet;
   }

   @Override
   public void setTakenVoucher(boolean takenVoucher) {
      this.takenVoucher = takenVoucher;
   }

   @Override
   public double getEpLimit() {
      return this.epLimit;
   }

   @Override
   public void setEpLimit(double epLimit) {
      this.epLimit = epLimit;
   }

   @Override
   public BlockPos getOverworldCoords() {
      return this.overworldCoords;
   }

   @Override
   public void setOverworldCoords(BlockPos overworldCoords) {
      this.overworldCoords = overworldCoords;
   }
}
