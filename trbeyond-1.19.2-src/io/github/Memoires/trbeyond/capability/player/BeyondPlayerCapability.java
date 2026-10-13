package io.github.Memoires.trbeyond.capability.player;

import io.github.Memoires.trbeyond.network.BeyondNetwork;
import io.github.Memoires.trbeyond.network.SyncPlayerCapabilityPacket;
import io.github.Memoires.trbeyond.util.ChallengeType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.FORGE
)
public class BeyondPlayerCapability implements IBeyondPlayerCapability {
   public static final Capability<IBeyondPlayerCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<IBeyondPlayerCapability>() {
   });
   private static final ResourceLocation ID = new ResourceLocation("trbeyond", "player_cap");
   public boolean isAction = false;
   public boolean usedTicket = false;
   public boolean usedCrystal = false;
   public boolean isReturned = true;
   public ChallengeType challengeType = null;
   public BlockPos overworldCoords;

   @SubscribeEvent
   public static void attach(AttachCapabilitiesEvent<Entity> e) {
      if (e.getObject() instanceof Player) {
         e.addCapability(ID, new BeyondPlayerCapabilityProvider() {
         });
      }
   }

   public static LazyOptional<IBeyondPlayerCapability> getFrom(Player player) {
      return player.getCapability(CAPABILITY);
   }

   public static void sync(Player player) {
      if (player instanceof ServerPlayer serverPlayer) {
         getFrom(serverPlayer)
            .ifPresent(data -> BeyondNetwork.sendToAllTrackingAndSelf(new SyncPlayerCapabilityPacket(data, serverPlayer.getId()), serverPlayer));
      }
   }

   @Override
   public boolean getAction() {
      return this.isAction;
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      tag.putBoolean("isAction", this.isAction);
      tag.putBoolean("usedTicket", this.usedTicket);
      if (this.challengeType != null) {
         CompoundTag challengeTag = new CompoundTag();
         challengeTag.putString("name", this.challengeType.getName());
         challengeTag.putString("category", this.challengeType.getCategory());
         challengeTag.putString("objective", this.challengeType.getObjective());
         challengeTag.putInt("count", this.challengeType.getCount());
         challengeTag.putBoolean("onSpawn", this.challengeType.isOnSpawn());
         challengeTag.putString("reward", this.challengeType.getReward());
         challengeTag.putInt("rewardAmount", this.challengeType.getRewardAmount());
         challengeTag.putInt("countDone", this.challengeType.getCountDone());
         challengeTag.putInt("timeLimit", this.challengeType.getTimeLimit());
         challengeTag.putBoolean("random", this.challengeType.isRandom());
         challengeTag.putInt("time", this.challengeType.getTime());
         challengeTag.putInt("waveDelay", this.challengeType.getWaveDelay());
         tag.put("challengeType", challengeTag);
      }

      if (this.overworldCoords != null) {
         tag.putLong("overworldCoords", this.overworldCoords.asLong());
      }

      return tag;
   }

   public void deserializeNBT(CompoundTag nbt) {
      this.isAction = nbt.getBoolean("isAction");
      this.usedTicket = nbt.getBoolean("usedTicket");
      this.challengeType = null;
      if (nbt.contains("challengeType")) {
         CompoundTag challengeTag = nbt.getCompound("challengeType");
         this.challengeType = new ChallengeType(
            challengeTag.getString("name"),
            challengeTag.getString("category"),
            challengeTag.getString("objective"),
            challengeTag.getInt("count"),
            challengeTag.getBoolean("onSpawn"),
            challengeTag.getString("reward"),
            challengeTag.getInt("rewardAmount"),
            challengeTag.getInt("timeLimit"),
            challengeTag.getBoolean("random"),
            challengeTag.getInt("waveDelay")
         );
         this.challengeType.setCountDone(challengeTag.getInt("countDone"));
         this.challengeType.setTime(challengeTag.getInt("time"));
      }

      if (nbt.contains("overworldCoords")) {
         this.overworldCoords = BlockPos.of(nbt.getLong("overworldCoords"));
      } else {
         this.overworldCoords = new BlockPos(0, 60, 0);
      }
   }

   @Override
   public boolean isAction() {
      return this.isAction;
   }

   @Override
   public void setAction(boolean action) {
      this.isAction = action;
   }

   @Override
   public boolean isUsedTicket() {
      return this.usedTicket;
   }

   @Override
   public void setUsedTicket(boolean used) {
      this.usedTicket = used;
   }

   @Override
   public boolean isReturned() {
      return this.isReturned;
   }

   @Override
   public void setReturned(boolean returned) {
      this.isReturned = returned;
   }

   @Override
   public void setOverworldCoords(BlockPos overworldCoords) {
      this.overworldCoords = overworldCoords;
   }

   @Override
   public BlockPos getOverworldCoords() {
      return this.overworldCoords != null ? this.overworldCoords : new BlockPos(0, 60, 0);
   }

   @Override
   public boolean isUsedCrystal() {
      return this.usedCrystal;
   }

   @Override
   public void setUsedCrystal(boolean used) {
      this.usedCrystal = used;
   }

   @Override
   public ChallengeType getChallengeType() {
      return this.challengeType;
   }

   @Override
   public void setChallengeType(ChallengeType challengeType) {
      this.challengeType = challengeType;
   }
}
