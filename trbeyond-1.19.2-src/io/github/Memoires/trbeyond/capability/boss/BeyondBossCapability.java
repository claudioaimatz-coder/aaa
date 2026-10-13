package io.github.Memoires.trbeyond.capability.boss;

import io.github.Memoires.trbeyond.network.BeyondNetwork;
import io.github.Memoires.trbeyond.network.SyncBossCapabilityPacket;
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
public class BeyondBossCapability implements IBeyondBossCapability {
   public static final Capability<IBeyondBossCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<IBeyondBossCapability>() {
   });
   private static final ResourceLocation ID = new ResourceLocation("trbeyond", "boss_cap");
   public int killedSpider = 0;
   public int killedCentipede = 0;
   public int killedSerpent = 0;
   public int killedOgre = 0;
   public int killedGozul = 0;
   public int killedMezul = 0;
   private long spiderLastKillTime = 0L;
   private long centipedeLastKillTime = 0L;
   private long serpentLastKillTime = 0L;
   private long ogreLastKillTime = 0L;
   private long gozulLastKillTime = 0L;
   private long mezulLastKillTime = 0L;

   @SubscribeEvent
   public static void attach(AttachCapabilitiesEvent<Entity> e) {
      if (e.getObject() instanceof Player) {
         e.addCapability(ID, new BeyondBossCapabilityProvider() {
         });
      }
   }

   public static LazyOptional<IBeyondBossCapability> getFrom(Player player) {
      return player.getCapability(CAPABILITY);
   }

   public static void sync(Player player) {
      if (player instanceof ServerPlayer serverPlayer) {
         getFrom(serverPlayer)
            .ifPresent(data -> BeyondNetwork.sendToAllTrackingAndSelf(new SyncBossCapabilityPacket(data, serverPlayer.getId()), serverPlayer));
      }
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      tag.putInt("killedSpider", this.killedSpider);
      tag.putInt("killedCentipede", this.killedCentipede);
      tag.putInt("killedSerpent", this.killedSerpent);
      tag.putInt("killedOgre", this.killedOgre);
      tag.putInt("killedGozul", this.killedGozul);
      tag.putInt("killedMezul", this.killedMezul);
      tag.putLong("spiderLastKillTime", this.spiderLastKillTime);
      tag.putLong("centipedeLastKillTime", this.centipedeLastKillTime);
      tag.putLong("serpentLastKillTime", this.serpentLastKillTime);
      tag.putLong("ogreLastKillTime", this.ogreLastKillTime);
      tag.putLong("gozulLastKillTime", this.gozulLastKillTime);
      tag.putLong("mezulLastKillTime", this.mezulLastKillTime);
      return tag;
   }

   public void deserializeNBT(CompoundTag nbt) {
      this.killedSpider = nbt.getInt("killedSpider");
      this.killedCentipede = nbt.getInt("killedCentipede");
      this.killedSerpent = nbt.getInt("killedSerpent");
      this.killedOgre = nbt.getInt("killedOgre");
      this.killedGozul = nbt.getInt("killedGozul");
      this.killedMezul = nbt.getInt("killedMezul");
      this.spiderLastKillTime = nbt.getLong("spiderLastKillTime");
      this.centipedeLastKillTime = nbt.getLong("centipedeLastKillTime");
      this.serpentLastKillTime = nbt.getLong("serpentLastKillTime");
      this.ogreLastKillTime = nbt.getLong("ogreLastKillTime");
      this.gozulLastKillTime = nbt.getLong("gozulLastKillTime");
      this.mezulLastKillTime = nbt.getLong("mezulLastKillTime");
   }

   @Override
   public int killedSpider() {
      return this.killedSpider;
   }

   @Override
   public int killedCentipede() {
      return this.killedCentipede;
   }

   @Override
   public int killedSerpent() {
      return this.killedSerpent;
   }

   @Override
   public int killedOgre() {
      return this.killedOgre;
   }

   @Override
   public int killedGozul() {
      return this.killedGozul;
   }

   @Override
   public int killedMezul() {
      return this.killedMezul;
   }

   @Override
   public void setKilledSpider(int killedSpider) {
      this.killedSpider = killedSpider;
      if (killedSpider > 0) {
         this.spiderLastKillTime = System.currentTimeMillis();
      }
   }

   @Override
   public void setKilledCentipede(int killedCentipede) {
      this.killedCentipede = killedCentipede;
      if (killedCentipede > 0) {
         this.centipedeLastKillTime = System.currentTimeMillis();
      }
   }

   @Override
   public void setKilledSerpent(int killedSerpent) {
      this.killedSerpent = killedSerpent;
      if (killedSerpent > 0) {
         this.serpentLastKillTime = System.currentTimeMillis();
      }
   }

   @Override
   public void setKilledOgre(int killedOgre) {
      this.killedOgre = killedOgre;
      if (killedOgre > 0) {
         this.ogreLastKillTime = System.currentTimeMillis();
      }
   }

   @Override
   public void setKilledGozul(int killedGozul) {
      this.killedGozul = killedGozul;
      if (killedGozul > 0) {
         this.gozulLastKillTime = System.currentTimeMillis();
      }
   }

   @Override
   public void setKilledMezul(int killedMezul) {
      this.killedMezul = killedMezul;
      if (killedMezul > 0) {
         this.mezulLastKillTime = System.currentTimeMillis();
      }
   }

   @Override
   public long getSpiderLastKillTime() {
      return this.spiderLastKillTime;
   }

   @Override
   public void setSpiderLastKillTime(long time) {
      this.spiderLastKillTime = time;
   }

   @Override
   public long getCentipedeLastKillTime() {
      return this.centipedeLastKillTime;
   }

   @Override
   public void setCentipedeLastKillTime(long time) {
      this.centipedeLastKillTime = time;
   }

   @Override
   public long getSerpentLastKillTime() {
      return this.serpentLastKillTime;
   }

   @Override
   public void setSerpentLastKillTime(long time) {
      this.serpentLastKillTime = time;
   }

   @Override
   public long getOgreLastKillTime() {
      return this.ogreLastKillTime;
   }

   @Override
   public void setOgreLastKillTime(long time) {
      this.ogreLastKillTime = time;
   }

   @Override
   public long getGozulLastKillTime() {
      return this.gozulLastKillTime;
   }

   @Override
   public void setGozulLastKillTime(long time) {
      this.gozulLastKillTime = time;
   }

   @Override
   public long getMezulLastKillTime() {
      return this.mezulLastKillTime;
   }

   @Override
   public void setMezulLastKillTime(long time) {
      this.mezulLastKillTime = time;
   }

   public int getKilledSpider() {
      return this.killedSpider;
   }

   public int getKilledCentipede() {
      return this.killedCentipede;
   }

   public int getKilledSerpent() {
      return this.killedSerpent;
   }

   public int getKilledOgre() {
      return this.killedOgre;
   }

   public int getKilledGozul() {
      return this.killedGozul;
   }

   public int getKilledMezul() {
      return this.killedMezul;
   }
}
