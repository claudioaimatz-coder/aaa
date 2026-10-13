package io.github.Memoires.trbeyond;

import com.mojang.logging.LogUtils;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import io.github.Memoires.trbeyond.ability.battlewill.utility.TRBeyondSkills;
import io.github.Memoires.trbeyond.commands.ClearDungeonCommand;
import io.github.Memoires.trbeyond.commands.DungeonStatsCommand;
import io.github.Memoires.trbeyond.commands.SummonEliteCommand;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.data.BeyondBlockStateProvider;
import io.github.Memoires.trbeyond.data.BeyondBlockTagProvider;
import io.github.Memoires.trbeyond.data.BeyondEntityEPProvider;
import io.github.Memoires.trbeyond.data.BeyondEntityTypeTagProvider;
import io.github.Memoires.trbeyond.data.BeyondGearEPProvider;
import io.github.Memoires.trbeyond.data.BeyondItemModelProvider;
import io.github.Memoires.trbeyond.data.BeyondLanguageProvider;
import io.github.Memoires.trbeyond.handlers.CapabilityHandler;
import io.github.Memoires.trbeyond.network.BeyondNetwork;
import io.github.Memoires.trbeyond.registry.BeyondRegistry;
import io.github.Memoires.trbeyond.util.BeyondItemProperties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

@Mod("trbeyond")
public class TRBeyond {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final String MODID = "trbeyond";
   private static final String PROTOCOL_VERSION = "1";
   public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(
      new ResourceLocation("trbeyond", "trbeyond"), () -> "1", "1"::equals, "1"::equals
   );
   private static int messageID = 0;
   private static final Collection<SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

   public TRBeyond() {
      IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
      IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
      TRBeyondSkills.register(modEventBus);
      BeyondRegistry.register(modEventBus);
      BeyondNetwork.register();
      modEventBus.addListener(CapabilityHandler::registerCapabilities);
      modEventBus.addListener(BeyondItemProperties::addItemProperties);
      modEventBus.addListener(this::commonSetup);
      modEventBus.addListener(this::generateData);
      MinecraftForge.EVENT_BUS.register(this);
      MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
      GeckoLib.initialize();
      ModLoadingContext.get().registerConfig(Type.SERVER, ServerConfig.SPEC, "tensura-dungeon+-server-config.toml");
   }

   private void onRegisterCommands(RegisterCommandsEvent event) {
      ClearDungeonCommand.register(event.getDispatcher());
      DungeonStatsCommand.register(event.getDispatcher());
      SummonEliteCommand.register(event.getDispatcher());
   }

   private void commonSetup(FMLCommonSetupEvent event) {
      LOGGER.info("HELLO FROM COMMON SETUP");
      LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
   }

   private void generateData(GatherDataEvent event) {
      DataGenerator gen = event.getGenerator();
      ExistingFileHelper efh = event.getExistingFileHelper();
      gen.addProvider(event.includeClient(), new BeyondEntityEPProvider(event));
      gen.addProvider(event.includeClient(), new BeyondEntityTypeTagProvider(event));
      gen.addProvider(event.includeClient(), new BeyondBlockTagProvider(event));
      gen.addProvider(event.includeServer(), new BeyondBlockStateProvider(gen, efh));
      gen.addProvider(event.includeClient(), new BeyondLanguageProvider(event));
      gen.addProvider(event.includeClient(), new BeyondItemModelProvider(event));
      gen.addProvider(event.includeClient(), new BeyondGearEPProvider(event));
   }

   public static <T> void addNetworkMessage(
      Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<Context>> messageConsumer
   ) {
      PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
      messageID++;
   }

   public static void queueServerWork(int tick, Runnable action) {
      workQueue.add(new SimpleEntry<>(action, tick));
   }

   @SubscribeEvent
   public void tick(ServerTickEvent event) {
      if (event.phase == Phase.END) {
         List<SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
         workQueue.forEach(work -> {
            work.setValue(work.getValue() - 1);
            if (work.getValue() == 0) {
               actions.add((SimpleEntry<Runnable, Integer>)work);
            }
         });
         actions.forEach(e -> e.getKey().run());
         workQueue.removeAll(actions);
      }
   }

   @SubscribeEvent
   public void onServerStarting(ServerStartingEvent event) {
      LOGGER.info("HELLO FROM SERVER STARTING");
   }

   public static Logger getLogger() {
      return LOGGER;
   }

   @EventBusSubscriber(
      modid = "trbeyond",
      bus = Bus.MOD
   )
   public static class playerAnimationsHandler {
      @SubscribeEvent
      public static void registerPlayerAnimationStuff(FMLClientSetupEvent event) {
         PlayerAnimationFactory.ANIMATION_DATA_FACTORY
            .registerFactory(new ResourceLocation("trbeyond", "animation"), 42, TRBeyond.playerAnimationsHandler::registerPlayerAnimation);
      }

      private static IAnimation registerPlayerAnimation(AbstractClientPlayer player) {
         return new ModifierLayer();
      }
   }
}
