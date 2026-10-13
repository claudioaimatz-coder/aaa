package io.github.Memoires.trbeyond.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

public class BeyondNetwork {
   private static final String PROTOCOL_VERSION = ModList.get().getModFileById("trbeyond").versionString().replaceAll("\\.", "");
   public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
      new ResourceLocation("trbeyond", "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
   );

   public static void register() {
      int i = 0;
      INSTANCE.registerMessage(
         ++i, SyncDungeonCapabilityPacket.class, SyncDungeonCapabilityPacket::toBytes, SyncDungeonCapabilityPacket::new, SyncDungeonCapabilityPacket::handle
      );
      INSTANCE.registerMessage(
         ++i,
         SyncEliteMobCapabilityPacket.class,
         SyncEliteMobCapabilityPacket::toBytes,
         SyncEliteMobCapabilityPacket::new,
         SyncEliteMobCapabilityPacket::handle
      );
      INSTANCE.registerMessage(
         ++i, SyncPlayerCapabilityPacket.class, SyncPlayerCapabilityPacket::toBytes, SyncPlayerCapabilityPacket::new, SyncPlayerCapabilityPacket::handle
      );
      INSTANCE.registerMessage(
         ++i, SyncBossCapabilityPacket.class, SyncBossCapabilityPacket::toBytes, SyncBossCapabilityPacket::new, SyncBossCapabilityPacket::handle
      );
      INSTANCE.registerMessage(++i, SummonExamMobPacket.class, SummonExamMobPacket::toBytes, SummonExamMobPacket::new, SummonExamMobPacket::handle);
      INSTANCE.registerMessage(++i, RequestLimiterPacket.class, RequestLimiterPacket::toBytes, RequestLimiterPacket::new, RequestLimiterPacket::handle);
      INSTANCE.registerMessage(++i, KurobeShopPacket.class, KurobeShopPacket::toBytes, KurobeShopPacket::new, KurobeShopPacket::handle);
      INSTANCE.registerMessage(++i, MjollmileCoinPacket.class, MjollmileCoinPacket::toBytes, MjollmileCoinPacket::new, MjollmileCoinPacket::handle);
      INSTANCE.registerMessage(++i, SyncAnimationPacket.class, SyncAnimationPacket::toBytes, SyncAnimationPacket::new, SyncAnimationPacket::handle);
      INSTANCE.registerMessage(++i, ChaosEaterPacket.class, ChaosEaterPacket::toBytes, ChaosEaterPacket::new, ChaosEaterPacket::handle);
      INSTANCE.registerMessage(++i, RequestTeleportPacket.class, RequestTeleportPacket::encode, RequestTeleportPacket::decode, RequestTeleportPacket::handle);
      INSTANCE.registerMessage(++i, OgreSwordCannonPacket.class, (msg, buf) -> msg.toBytes(buf), OgreSwordCannonPacket::new, OgreSwordCannonPacket::handle);
      INSTANCE.registerMessage(++i, VortexCrashPacket.class, (msg, buf) -> msg.toBytes(buf), VortexCrashPacket::new, VortexCrashPacket::handle);
      INSTANCE.registerMessage(++i, KurobeGUIPacket.class, KurobeGUIPacket::encode, KurobeGUIPacket::decode, KurobeGUIPacket::handle);
      INSTANCE.registerMessage(
         ++i,
         RequestResearcherPaymentPacket.class,
         RequestResearcherPaymentPacket::encode,
         RequestResearcherPaymentPacket::decode,
         RequestResearcherPaymentPacket::handle
      );
      INSTANCE.registerMessage(++i, ConfigSyncPacket.class, ConfigSyncPacket::encode, ConfigSyncPacket::decode, ConfigSyncPacket::handle);
   }

   public static <T> void toServer(T message) {
      INSTANCE.sendToServer(message);
   }

   public static <T> void toAll(T message) {
      for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
         INSTANCE.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
      }
   }

   public static <MSG> void sendToAllTrackingAndSelf(MSG msg, LivingEntity tracked) {
      INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> tracked), msg);
   }
}
