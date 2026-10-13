package io.github.Memoires.trbeyond.network;

import io.github.Memoires.trbeyond.capability.boss.BeyondBossCapability;
import io.github.Memoires.trbeyond.capability.dungeon.DungeonCapability;
import io.github.Memoires.trbeyond.capability.player.BeyondPlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

class ClientAccess {
   static final Minecraft minecraft = Minecraft.getInstance();

   static void updateDungeonCapability(int entityId, CompoundTag tag) {
      if (minecraft.level != null) {
         if (minecraft.level.getEntity(entityId) instanceof LivingEntity living) {
            DungeonCapability.getFrom(living).ifPresent(data -> data.deserializeNBT(tag));
         }
      }
   }

   static void updatePlayerCapability(int entityId, CompoundTag tag) {
      if (minecraft.level != null && minecraft.level.getEntity(entityId) instanceof Player player) {
         BeyondPlayerCapability.getFrom(player).ifPresent(data -> data.deserializeNBT(tag));
         player.refreshDimensions();
      }
   }

   static void updateBossCapability(int entityId, CompoundTag tag) {
      if (minecraft.level != null && minecraft.level.getEntity(entityId) instanceof Player player) {
         BeyondBossCapability.getFrom(player).ifPresent(data -> data.deserializeNBT(tag));
         player.refreshDimensions();
      }
   }
}
