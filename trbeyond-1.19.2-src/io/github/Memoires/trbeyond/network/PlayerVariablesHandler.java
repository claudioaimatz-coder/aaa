package io.github.Memoires.trbeyond.network;

import io.github.Memoires.trbeyond.TRBeyond;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent.Context;

@EventBusSubscriber(
   bus = Bus.MOD
)
public class PlayerVariablesHandler {
   public static final Capability<PlayerVariablesHandler.PlayerVariables> PLAYER_VARIABLES_CAPABILITY = CapabilityManager.get(
      new CapabilityToken<PlayerVariablesHandler.PlayerVariables>() {
      }
   );

   @SubscribeEvent
   public static void init(FMLCommonSetupEvent event) {
      TRBeyond.addNetworkMessage(
         PlayerVariablesHandler.PlayerVariablesSyncMessage.class,
         PlayerVariablesHandler.PlayerVariablesSyncMessage::buffer,
         PlayerVariablesHandler.PlayerVariablesSyncMessage::new,
         PlayerVariablesHandler.PlayerVariablesSyncMessage::handler
      );
   }

   @SubscribeEvent
   public static void init(RegisterCapabilitiesEvent event) {
      event.register(PlayerVariablesHandler.PlayerVariables.class);
   }

   @EventBusSubscriber
   public static class EventBusVariableHandlers {
      @SubscribeEvent
      public static void onPlayerLoggedInSyncPlayerVariables(PlayerLoggedInEvent event) {
         if (!event.getEntity().level.isClientSide()) {
            ((PlayerVariablesHandler.PlayerVariables)event.getEntity()
                  .getCapability(PlayerVariablesHandler.PLAYER_VARIABLES_CAPABILITY, null)
                  .orElse(new PlayerVariablesHandler.PlayerVariables()))
               .syncPlayerVariables(event.getEntity());
         }
      }

      @SubscribeEvent
      public static void onPlayerRespawnedSyncPlayerVariables(PlayerRespawnEvent event) {
         if (!event.getEntity().level.isClientSide()) {
            ((PlayerVariablesHandler.PlayerVariables)event.getEntity()
                  .getCapability(PlayerVariablesHandler.PLAYER_VARIABLES_CAPABILITY, null)
                  .orElse(new PlayerVariablesHandler.PlayerVariables()))
               .syncPlayerVariables(event.getEntity());
         }
      }

      @SubscribeEvent
      public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerChangedDimensionEvent event) {
         if (!event.getEntity().level.isClientSide()) {
            ((PlayerVariablesHandler.PlayerVariables)event.getEntity()
                  .getCapability(PlayerVariablesHandler.PLAYER_VARIABLES_CAPABILITY, null)
                  .orElse(new PlayerVariablesHandler.PlayerVariables()))
               .syncPlayerVariables(event.getEntity());
         }
      }

      @SubscribeEvent
      public static void clonePlayer(Clone event) {
         event.getOriginal().revive();
         PlayerVariablesHandler.PlayerVariables original = (PlayerVariablesHandler.PlayerVariables)event.getOriginal()
            .getCapability(PlayerVariablesHandler.PLAYER_VARIABLES_CAPABILITY, null)
            .orElse(new PlayerVariablesHandler.PlayerVariables());
         PlayerVariablesHandler.PlayerVariables clone = (PlayerVariablesHandler.PlayerVariables)event.getEntity()
            .getCapability(PlayerVariablesHandler.PLAYER_VARIABLES_CAPABILITY, null)
            .orElse(new PlayerVariablesHandler.PlayerVariables());
         clone.KilledSpider = original.KilledSpider;
         clone.KilledCentipede = original.KilledCentipede;
         clone.KilledGozul = original.KilledGozul;
         clone.KilledMezul = original.KilledMezul;
         clone.KilledOgre = original.KilledOgre;
         clone.KilledSerpent = original.KilledSerpent;
         if (!event.isWasDeath()) {
         }
      }
   }

   public static class PlayerVariables {
      public boolean KilledSpider = false;
      public boolean KilledCentipede = false;
      public boolean KilledGozul = false;
      public boolean KilledMezul = false;
      public boolean KilledOgre = false;
      public boolean KilledSerpent = false;

      public void syncPlayerVariables(Entity entity) {
         if (entity instanceof ServerPlayer serverPlayer) {
            TRBeyond.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PlayerVariablesHandler.PlayerVariablesSyncMessage(this));
         }
      }

      public Tag writeNBT() {
         CompoundTag nbt = new CompoundTag();
         nbt.putBoolean("KilledSpider", this.KilledSpider);
         nbt.putBoolean("KilledCentipede", this.KilledCentipede);
         nbt.putBoolean("KilledGozul", this.KilledGozul);
         nbt.putBoolean("KilledMezul", this.KilledMezul);
         nbt.putBoolean("KilledOgre", this.KilledOgre);
         nbt.putBoolean("KilledSerpent", this.KilledSerpent);
         return nbt;
      }

      public void readNBT(Tag Tag) {
         CompoundTag nbt = (CompoundTag)Tag;
         this.KilledSpider = nbt.getBoolean("KilledSpider");
         this.KilledCentipede = nbt.getBoolean("KilledCentipede");
         this.KilledGozul = nbt.getBoolean("KilledGozul");
         this.KilledMezul = nbt.getBoolean("KilledMezul");
         this.KilledOgre = nbt.getBoolean("KilledOgre");
         this.KilledSerpent = nbt.getBoolean("KilledSerpent");
      }
   }

   @EventBusSubscriber
   private static class PlayerVariablesProvider implements ICapabilitySerializable<Tag> {
      private final PlayerVariablesHandler.PlayerVariables playerVariables = new PlayerVariablesHandler.PlayerVariables();
      private final LazyOptional<PlayerVariablesHandler.PlayerVariables> instance = LazyOptional.of(() -> this.playerVariables);

      @SubscribeEvent
      public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
         if (event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer)) {
            event.addCapability(new ResourceLocation("test", "player_variables"), new PlayerVariablesHandler.PlayerVariablesProvider());
         }
      }

      public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
         return cap == PlayerVariablesHandler.PLAYER_VARIABLES_CAPABILITY ? this.instance.cast() : LazyOptional.empty();
      }

      public Tag serializeNBT() {
         return this.playerVariables.writeNBT();
      }

      public void deserializeNBT(Tag nbt) {
         this.playerVariables.readNBT(nbt);
      }
   }

   public static class PlayerVariablesSyncMessage {
      public PlayerVariablesHandler.PlayerVariables data;

      public PlayerVariablesSyncMessage(FriendlyByteBuf buffer) {
         this.data = new PlayerVariablesHandler.PlayerVariables();
         this.data.readNBT(buffer.readNbt());
      }

      public PlayerVariablesSyncMessage(PlayerVariablesHandler.PlayerVariables data) {
         this.data = data;
      }

      public static void buffer(PlayerVariablesHandler.PlayerVariablesSyncMessage message, FriendlyByteBuf buffer) {
         buffer.writeNbt((CompoundTag)message.data.writeNBT());
      }

      public static void handler(PlayerVariablesHandler.PlayerVariablesSyncMessage message, Supplier<Context> contextSupplier) {
         Context context = contextSupplier.get();
         context.enqueueWork(
            () -> {
               if (!context.getDirection().getReceptionSide().isServer()) {
                  PlayerVariablesHandler.PlayerVariables variables = (PlayerVariablesHandler.PlayerVariables)Minecraft.getInstance()
                     .player
                     .getCapability(PlayerVariablesHandler.PLAYER_VARIABLES_CAPABILITY, null)
                     .orElse(new PlayerVariablesHandler.PlayerVariables());
                  variables.KilledSpider = message.data.KilledSpider;
                  variables.KilledCentipede = message.data.KilledCentipede;
                  variables.KilledGozul = message.data.KilledGozul;
                  variables.KilledMezul = message.data.KilledMezul;
                  variables.KilledOgre = message.data.KilledOgre;
                  variables.KilledSerpent = message.data.KilledSerpent;
               }
            }
         );
         context.setPacketHandled(true);
      }
   }
}
