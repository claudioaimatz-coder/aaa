package io.github.Memoires.trbeyond.network.worldgen;

import io.github.Memoires.trbeyond.block.ShrineBlock;
import io.github.Memoires.trbeyond.config.ServerConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class DungeonSaveData extends SavedData {
   private Vec3 startGenPos = new Vec3(0.0, 140.0, 0.0);
   private boolean loaded = false;
   private final Map<String, Map<UUID, Integer>> bossCooldown = new HashMap<>();
   private final List<BlockPos> bossChests = new ArrayList<>();
   private List<BlockPos> floor95Doors = new ArrayList<>();
   private BlockPos floor95ShopPos;
   private BlockPos BackToLobbyDoor;
   private BlockPos BackToLastKnownLocationDoor;
   private BlockPos bossRushDoor;
   private BlockPos bossRushPos;
   private BlockPos LobbyPos;
   private Map<UUID, Map<BlockPos, Long>> chestCooldown;
   private Map<UUID, Map<BlockPos, Long>> shrineCooldown;
   private List<UUID> tpToDungeonPlayers;

   public static DungeonSaveData get(ServerLevel overworld) {
      return (DungeonSaveData)overworld.getDataStorage().computeIfAbsent(DungeonSaveData::new, DungeonSaveData::new, "tensura_dungeon");
   }

   public static DungeonSaveData getOverworld(Level level) {
      return level instanceof ServerLevel serverLevel ? get(serverLevel.getServer().m_129783_()) : null;
   }

   private DungeonSaveData() {
   }

   private DungeonSaveData(CompoundTag tag) {
      this();
      this.loaded = tag.getBoolean("loaded");
      this.startGenPos = new Vec3(tag.getDouble("entranceX"), tag.getDouble("entranceY"), tag.getDouble("entranceZ"));
      if (tag.contains("cooldownList")) {
         CompoundTag cooldownList = (CompoundTag)tag.get("cooldownList");
         this.bossCooldown.clear();

         for (String boss : cooldownList.getAllKeys()) {
            CompoundTag cooldownTag = (CompoundTag)cooldownList.get(boss);
            HashMap<UUID, Integer> list = new HashMap<>();

            for (String cooldown : cooldownTag.getAllKeys()) {
               list.put(UUID.fromString(cooldown), cooldownTag.getInt(cooldown));
            }

            this.bossCooldown.put(boss, list);
         }
      }

      if (tag.contains("bossChests")) {
         CompoundTag bossChestsTag = (CompoundTag)tag.get("bossChests");
         this.bossChests.clear();

         for (String pos : bossChestsTag.getAllKeys()) {
            BlockPos blockPos = BlockPos.of(bossChestsTag.getLong(pos));
            this.bossChests.add(blockPos);
         }
      }

      if (tag.contains("floor95Doors")) {
         CompoundTag floor95DoorsTag = (CompoundTag)tag.get("floor95Doors");
         this.floor95Doors.clear();

         for (String pos : floor95DoorsTag.getAllKeys()) {
            BlockPos blockPos = BlockPos.of(floor95DoorsTag.getLong(pos));
            this.floor95Doors.add(blockPos);
         }
      }

      if (tag.contains("floor95ShopPos")) {
         this.floor95ShopPos = BlockPos.of(tag.getLong("floor95ShopPos"));
      }

      if (tag.contains("BackToLobbyDoor")) {
         this.BackToLobbyDoor = BlockPos.of(tag.getLong("BackToLobbyDoor"));
      }

      if (tag.contains("BackToLastKnownLocationDoor")) {
         this.BackToLastKnownLocationDoor = BlockPos.of(tag.getLong("BackToLastKnownLocationDoor"));
      }

      if (tag.contains("bossRushDoor")) {
         this.bossRushDoor = BlockPos.of(tag.getLong("bossRushDoor"));
      }

      if (tag.contains("bossRushPos")) {
         this.bossRushPos = BlockPos.of(tag.getLong("bossRushPos"));
      }

      if (tag.contains("LobbyPos")) {
         this.LobbyPos = BlockPos.of(tag.getLong("LobbyPos"));
      }

      if (tag.contains("chestCooldown")) {
         CompoundTag chestCooldownTag = (CompoundTag)tag.get("chestCooldown");
         this.chestCooldown = new HashMap<>();

         for (String key : chestCooldownTag.getAllKeys()) {
            if (!key.endsWith("_time")) {
               try {
                  UUID playerUUID = UUID.fromString(key);
                  CompoundTag posTag = chestCooldownTag.getCompound(key);
                  CompoundTag timeTag = chestCooldownTag.contains(key + "_time") ? chestCooldownTag.getCompound(key + "_time") : null;
                  Map<BlockPos, Long> map = new HashMap<>();

                  for (String posKey : posTag.getAllKeys()) {
                     BlockPos blockPos = BlockPos.of(posTag.getLong(posKey));
                     long time = timeTag != null && timeTag.contains(posKey) ? timeTag.getLong(posKey) : 0L;
                     map.put(blockPos, time);
                  }

                  this.chestCooldown.put(playerUUID, map);
               } catch (IllegalArgumentException var14) {
                  System.err.println("Skipping invalid chestCooldown key: " + key);
               }
            }
         }
      }

      if (tag.contains("shrineCooldown")) {
         CompoundTag shrineCooldownTag = (CompoundTag)tag.get("shrineCooldown");
         this.shrineCooldown = new HashMap<>();

         for (String player : shrineCooldownTag.getAllKeys()) {
            CompoundTag posTag = (CompoundTag)shrineCooldownTag.get(player);
            CompoundTag timeTag = (CompoundTag)shrineCooldownTag.get(player + "_time");
            Map<BlockPos, Long> map = new HashMap<>();

            for (String pos : posTag.getAllKeys()) {
               BlockPos blockPos = BlockPos.of(posTag.getLong(pos));
               map.put(blockPos, timeTag.getLong(pos));
            }

            this.shrineCooldown.put(UUID.fromString(player), map);
         }
      }

      this.tpToDungeonPlayers = new ArrayList<>();
      if (tag.contains("tpToDungeonPlayers")) {
         CompoundTag tpToDungeonPlayersTag = (CompoundTag)tag.get("tpToDungeonPlayers");
         this.tpToDungeonPlayers.clear();

         for (String uuid : tpToDungeonPlayersTag.getAllKeys()) {
            this.tpToDungeonPlayers.add(UUID.fromString(uuid));
         }
      } else {
         this.tpToDungeonPlayers = new ArrayList<>();
      }
   }

   @NotNull
   public CompoundTag save(CompoundTag tag) {
      tag.putBoolean("loaded", this.loaded);
      tag.putDouble("entranceX", this.startGenPos.x);
      tag.putDouble("entranceY", this.startGenPos.y);
      tag.putDouble("entranceZ", this.startGenPos.z);
      CompoundTag bossTag = new CompoundTag();

      for (String boss : this.bossCooldown.keySet()) {
         Map<UUID, Integer> cooldown = this.bossCooldown.get(boss);
         CompoundTag cooldownTag = new CompoundTag();

         for (UUID uuid : cooldown.keySet()) {
            cooldownTag.putInt(uuid.toString(), cooldown.get(uuid));
         }

         bossTag.put(boss, cooldownTag);
      }

      tag.put("cooldownList", bossTag);
      CompoundTag bossChestsTag = new CompoundTag();

      for (BlockPos pos : this.bossChests) {
         bossChestsTag.putLong(pos.toString(), pos.asLong());
      }

      tag.put("bossChests", bossChestsTag);
      CompoundTag floor95DoorsTag = new CompoundTag();

      for (BlockPos pos : this.floor95Doors) {
         floor95DoorsTag.putLong(pos.toString(), pos.asLong());
      }

      tag.put("floor95Doors", floor95DoorsTag);
      if (this.floor95ShopPos != null) {
         tag.putLong("floor95ShopPos", this.floor95ShopPos.asLong());
      }

      if (this.BackToLobbyDoor != null) {
         tag.putLong("BackToLobbyDoor", this.BackToLobbyDoor.asLong());
      }

      if (this.BackToLastKnownLocationDoor != null) {
         tag.putLong("BackToLastKnownLocationDoor", this.BackToLastKnownLocationDoor.asLong());
      }

      if (this.bossRushDoor != null) {
         tag.putLong("bossRushDoor", this.bossRushDoor.asLong());
      }

      if (this.bossRushPos != null) {
         tag.putLong("bossRushPos", this.bossRushPos.asLong());
      }

      if (this.LobbyPos != null) {
         tag.putLong("LobbyPos", this.LobbyPos.asLong());
      }

      if (this.chestCooldown != null) {
         CompoundTag chestCooldownTag = new CompoundTag();

         for (UUID player : this.chestCooldown.keySet()) {
            CompoundTag posTag = new CompoundTag();
            CompoundTag timeTag = new CompoundTag();
            Map<BlockPos, Long> map = this.chestCooldown.get(player);

            for (BlockPos pos : map.keySet()) {
               posTag.putLong(pos.toString(), pos.asLong());
               timeTag.putLong(pos.toString(), map.get(pos));
            }

            chestCooldownTag.put(player.toString(), posTag);
            chestCooldownTag.put(player.toString() + "_time", timeTag);
         }

         tag.put("chestCooldown", chestCooldownTag);
      }

      if (this.shrineCooldown != null) {
         CompoundTag shrineCooldownTag = new CompoundTag();

         for (UUID player : this.shrineCooldown.keySet()) {
            CompoundTag posTag = new CompoundTag();
            CompoundTag timeTag = new CompoundTag();
            Map<BlockPos, Long> map = this.shrineCooldown.get(player);

            for (BlockPos pos : map.keySet()) {
               posTag.putLong(pos.toString(), pos.asLong());
               timeTag.putLong(pos.toString(), map.get(pos));
            }

            shrineCooldownTag.put(player.toString(), posTag);
            shrineCooldownTag.put(player.toString() + "_time", timeTag);
         }

         tag.put("shrineCooldown", shrineCooldownTag);
      }

      CompoundTag tpToDungeonPlayersTag = new CompoundTag();
      if (this.tpToDungeonPlayers != null) {
         for (UUID uuid : this.tpToDungeonPlayers) {
            tpToDungeonPlayersTag.putString(uuid.toString(), uuid.toString());
         }
      }

      tag.put("tpToDungeonPlayers", tpToDungeonPlayersTag);
      return tag;
   }

   public void setLoaded(boolean loaded) {
      this.loaded = loaded;
      this.setDirty();
   }

   public void setStartGenPos(Vec3 startGenPos) {
      this.startGenPos = startGenPos;
      this.setDirty();
   }

   public Vec3 getStartGenPos() {
      return this.startGenPos;
   }

   public Map<String, Map<UUID, Integer>> getBossCooldownList() {
      return this.bossCooldown;
   }

   public int getBossCooldown(LivingEntity boss, Player player) {
      return this.bossCooldown.get(boss.getType().getDescriptionId()).get(player.getUUID());
   }

   public boolean isOnBossCooldown(LivingEntity boss, Player player) {
      Map<String, Map<UUID, Integer>> map = this.getBossCooldownList();
      if (!map.containsKey(boss.getType().getDescriptionId())) {
         return false;
      } else {
         return !map.get(boss.getType().getDescriptionId()).containsKey(player.getUUID())
            ? false
            : this.bossCooldown.get(boss.getType().getDescriptionId()).get(player.getUUID()) > 0;
      }
   }

   public void setBossCooldown(LivingEntity boss, Player player, int cooldown) {
      Map<String, Map<UUID, Integer>> map = this.getBossCooldownList();
      if (map.containsKey(boss.getType().getDescriptionId())) {
         map.get(boss.getType().getDescriptionId()).put(player.getUUID(), cooldown);
      } else {
         HashMap<UUID, Integer> playerMap = new HashMap<>();
         playerMap.put(player.getUUID(), cooldown);
         map.put(boss.getType().getDescriptionId(), playerMap);
      }
   }

   public void tickBossCooldown() {
      Map<String, Map<UUID, Integer>> bossCooldownList = this.getBossCooldownList();

      for (Entry<String, Map<UUID, Integer>> entry : bossCooldownList.entrySet()) {
         for (Entry<UUID, Integer> cooldown : entry.getValue().entrySet()) {
            if (cooldown.getValue() > 0) {
               cooldown.setValue(cooldown.getValue() - 1);
            }
         }
      }
   }

   public void addBossChest(BlockPos pos) {
      this.bossChests.add(pos);
      this.setDirty();
   }

   public void removeBossChest(BlockPos pos) {
      this.bossChests.remove(pos);
      this.setDirty();
   }

   public List<BlockPos> getBossChests() {
      return this.bossChests;
   }

   public void clearBossChests() {
      this.bossChests.clear();
      this.setDirty();
   }

   public boolean hasBossChest() {
      return !this.bossChests.isEmpty();
   }

   public void addFloor95Door(BlockPos pos) {
      this.floor95Doors.add(pos);
      this.setDirty();
   }

   public void removeFloor95Door(BlockPos pos) {
      this.floor95Doors.remove(pos);
      this.setDirty();
   }

   public List<BlockPos> getFloor95Doors() {
      return this.floor95Doors;
   }

   public void clearFloor95Doors() {
      this.floor95Doors.clear();
      this.setDirty();
   }

   public boolean hasFloor95Door() {
      return !this.floor95Doors.isEmpty();
   }

   public void setFloor95ShopPos(BlockPos pos) {
      this.floor95ShopPos = pos;
      this.setDirty();
   }

   public BlockPos getFloor95ShopPos() {
      return this.floor95ShopPos == null ? new BlockPos(-17, 191, 0) : this.floor95ShopPos;
   }

   public void setBackToLobbyDoor(BlockPos pos) {
      this.BackToLobbyDoor = pos;
      this.setDirty();
   }

   public BlockPos getBackToLobbyDoor() {
      return this.BackToLobbyDoor == null ? new BlockPos(-29, 192, 0) : this.BackToLobbyDoor;
   }

   public void setBackToLastKnownLocationDoor(BlockPos pos) {
      this.BackToLastKnownLocationDoor = pos;
      this.setDirty();
   }

   public BlockPos getBackToLastKnownLocationDoor() {
      return this.BackToLastKnownLocationDoor == null ? new BlockPos(-17, 192, -2) : this.BackToLastKnownLocationDoor;
   }

   public void setBossRushTeleporterDoor(BlockPos pos) {
      this.bossRushDoor = pos;
      this.setDirty();
   }

   public BlockPos getBossRushTeleporterDoor() {
      return this.bossRushDoor == null ? new BlockPos(0, 0, 0) : this.bossRushDoor;
   }

   public void setBossRushPos(BlockPos pos) {
      this.bossRushPos = pos;
      this.setDirty();
   }

   public BlockPos getBossRushPos() {
      return this.bossRushPos == null ? new BlockPos(0, 0, 0) : this.bossRushPos;
   }

   public void setLobbyPos(BlockPos pos) {
      this.LobbyPos = pos;
      this.setDirty();
   }

   public BlockPos getLobbyPos() {
      return this.LobbyPos == null ? new BlockPos(-10, 116, 4) : this.LobbyPos;
   }

   public void setChestCooldown(Player player, BlockPos chest, long time) {
      if (this.chestCooldown == null) {
         this.chestCooldown = new HashMap<>();
      }

      if (!this.chestCooldown.containsKey(player.getUUID())) {
         this.chestCooldown.put(player.getUUID(), new HashMap<>());
      }

      this.chestCooldown.get(player.getUUID()).put(chest, time);
      this.setDirty();
   }

   public Map<BlockPos, Long> getChestCooldown(Player player) {
      return this.chestCooldown == null ? null : this.chestCooldown.get(player.getUUID());
   }

   public void removeChestCooldown(Player player, BlockPos chest) {
      if (this.chestCooldown != null) {
         if (this.chestCooldown.containsKey(player.getUUID())) {
            this.chestCooldown.get(player.getUUID()).remove(chest);
            this.setDirty();
         }
      }
   }

   public void clearChestCooldown(Player player) {
      if (this.chestCooldown != null) {
         this.chestCooldown.remove(player.getUUID());
         this.setDirty();
      }
   }

   public void clearAllChestCooldown() {
      if (this.chestCooldown != null) {
         this.chestCooldown.clear();
         this.setDirty();
      }
   }

   public boolean canOpenChest(Player player, BlockPos chest, long packedTime) {
      if (!(Boolean)ServerConfig.INSTANCE.dungeonConfig.chestLock.get()) {
         return true;
      } else if (this.chestCooldown == null) {
         return true;
      } else if (!this.chestCooldown.containsKey(player.getUUID())) {
         return true;
      } else if (!this.chestCooldown.get(player.getUUID()).containsKey(chest)) {
         return true;
      } else {
         long savedTimeData = this.chestCooldown.get(player.getUUID()).get(chest);
         long cooldown = (Long)ServerConfig.INSTANCE.dungeonConfig.chestCooldown.get();
         return packedTime > savedTimeData + cooldown;
      }
   }

   public void setShrineCooldown(UUID player, BlockPos shrine, long time) {
      if (this.shrineCooldown == null) {
         this.shrineCooldown = new HashMap<>();
      }

      if (!this.shrineCooldown.containsKey(player)) {
         this.shrineCooldown.put(player, new HashMap<>());
      }

      this.shrineCooldown.get(player).put(shrine, time);
      this.setDirty();
   }

   public Map<BlockPos, Long> getShrineCooldown(Player player) {
      return this.shrineCooldown == null ? null : this.shrineCooldown.get(player.getUUID());
   }

   public void removeShrineCooldown(Player player, BlockPos shrine) {
      if (this.shrineCooldown != null) {
         if (this.shrineCooldown.containsKey(player.getUUID())) {
            this.shrineCooldown.get(player.getUUID()).remove(shrine);
            this.setDirty();
         }
      }
   }

   public void clearShrineCooldown(Player player) {
      if (this.shrineCooldown != null) {
         this.shrineCooldown.remove(player.getUUID());
         this.setDirty();
      }
   }

   public void clearAllShrineCooldown() {
      if (this.shrineCooldown != null) {
         this.shrineCooldown.clear();
         this.setDirty();
      }
   }

   public boolean canUseShrine(Player player, BlockPos shrine, long packedTime, Level level) {
      if (this.shrineCooldown == null) {
         return true;
      } else if (!this.shrineCooldown.containsKey(player.getUUID())) {
         return true;
      } else if (!this.shrineCooldown.get(player.getUUID()).containsKey(shrine)) {
         return true;
      } else {
         long savedTimeData = this.shrineCooldown.get(player.getUUID()).get(shrine);
         if (level.getBlockState(shrine).getBlock() instanceof ShrineBlock shrineBlock && shrineBlock.tier == ShrineBlock.DungeonShrineTier.STELLAR) {
            long cooldownLength = (long)((Integer)ServerConfig.INSTANCE.dungeonConfig.stellarShrineCooldown.get()).intValue();
            return packedTime > savedTimeData + cooldownLength;
         }

         long cooldownLength = (long)((Integer)ServerConfig.INSTANCE.dungeonConfig.shrineCooldown.get()).intValue();
         return packedTime > savedTimeData + cooldownLength;
      }
   }

   public List<UUID> getTpToDungeonPlayers() {
      return (List<UUID>)(this.tpToDungeonPlayers == null ? new ArrayList<>() : this.tpToDungeonPlayers);
   }

   public void addTpToDungeonPlayer(UUID uuid) {
      if (this.tpToDungeonPlayers == null) {
         this.tpToDungeonPlayers = new ArrayList<>();
      }

      if (!this.tpToDungeonPlayers.contains(uuid)) {
         this.tpToDungeonPlayers.add(uuid);
         this.setDirty();
      }
   }

   public void removeTpToDungeonPlayer(UUID uuid) {
      if (this.tpToDungeonPlayers != null) {
         this.tpToDungeonPlayers.remove(uuid);
      }

      this.setDirty();
   }

   public boolean isLoaded() {
      return this.loaded;
   }
}
