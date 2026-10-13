package io.github.Memoires.trbeyond.client;

import io.github.Memoires.trbeyond.block.client.BenchPressRenderer;
import io.github.Memoires.trbeyond.block.client.BronzeChestRenderer;
import io.github.Memoires.trbeyond.block.client.DumbbellRackRenderer;
import io.github.Memoires.trbeyond.block.client.DungeonDoorModel0;
import io.github.Memoires.trbeyond.block.client.DungeonDoorModel1;
import io.github.Memoires.trbeyond.block.client.DungeonGateRenderer;
import io.github.Memoires.trbeyond.block.client.GoldChestRenderer;
import io.github.Memoires.trbeyond.block.client.IllusionaryBlockRenderer;
import io.github.Memoires.trbeyond.block.client.InsideGateRenderer;
import io.github.Memoires.trbeyond.block.client.ShrineBlockRenderer;
import io.github.Memoires.trbeyond.block.client.SilverChestRenderer;
import io.github.Memoires.trbeyond.block.client.StellarChestRenderer;
import io.github.Memoires.trbeyond.block.client.geo.SlimeCeilingRenderer;
import io.github.Memoires.trbeyond.block.client.geo.SlimeFloorRenderer;
import io.github.Memoires.trbeyond.block.entity.DungeonDoorRenderer;
import io.github.Memoires.trbeyond.client.gui.ChallengeOverlay;
import io.github.Memoires.trbeyond.client.renderer.FontRenderer;
import io.github.Memoires.trbeyond.client.screen.CustomResearcherEnchantmentScreen;
import io.github.Memoires.trbeyond.client.screen.KurobeMainMenuScreen;
import io.github.Memoires.trbeyond.client.screen.KurobeShopScreen;
import io.github.Memoires.trbeyond.client.screen.MjollmileScreen;
import io.github.Memoires.trbeyond.client.screen.SubspaceContainerScreen;
import io.github.Memoires.trbeyond.entity.client.BabyDragonRenderer;
import io.github.Memoires.trbeyond.entity.client.BenimaruRenderer;
import io.github.Memoires.trbeyond.entity.client.BlueSlimeDollRenderer;
import io.github.Memoires.trbeyond.entity.client.BossBlackSpiderRenderer;
import io.github.Memoires.trbeyond.entity.client.DryadRenderer;
import io.github.Memoires.trbeyond.entity.client.EvilCentipedeBossRenderer;
import io.github.Memoires.trbeyond.entity.client.GlendaBulletModel;
import io.github.Memoires.trbeyond.entity.client.GlendaBulletRenderer;
import io.github.Memoires.trbeyond.entity.client.GobweRenderer;
import io.github.Memoires.trbeyond.entity.client.GozulRenderer;
import io.github.Memoires.trbeyond.entity.client.GraveLizardRenderer;
import io.github.Memoires.trbeyond.entity.client.GreenSlimeDollRenderer;
import io.github.Memoires.trbeyond.entity.client.GuildLadyRenderer;
import io.github.Memoires.trbeyond.entity.client.GuildTesterRenderer;
import io.github.Memoires.trbeyond.entity.client.KaijinRenderer;
import io.github.Memoires.trbeyond.entity.client.KaoruRenderer;
import io.github.Memoires.trbeyond.entity.client.KillerDryadRenderer;
import io.github.Memoires.trbeyond.entity.client.KingCrawlerRenderer;
import io.github.Memoires.trbeyond.entity.client.KobyRenderer;
import io.github.Memoires.trbeyond.entity.client.KurobeRenderer;
import io.github.Memoires.trbeyond.entity.client.LivingArmorRenderer;
import io.github.Memoires.trbeyond.entity.client.MageRenderer;
import io.github.Memoires.trbeyond.entity.client.MezulRenderer;
import io.github.Memoires.trbeyond.entity.client.MilimSlimeScarletRenderer;
import io.github.Memoires.trbeyond.entity.client.MimicChestRenderer;
import io.github.Memoires.trbeyond.entity.client.MjollmileRenderer;
import io.github.Memoires.trbeyond.entity.client.MomijiRenderer;
import io.github.Memoires.trbeyond.entity.client.OgreLordRenderer;
import io.github.Memoires.trbeyond.entity.client.OgreSubRenderer;
import io.github.Memoires.trbeyond.entity.client.RedSlimeDollRenderer;
import io.github.Memoires.trbeyond.entity.client.ScarletSlimeRenderer;
import io.github.Memoires.trbeyond.entity.client.ShionRenderer;
import io.github.Memoires.trbeyond.entity.client.SkullNightRenderer;
import io.github.Memoires.trbeyond.entity.client.SlimeDollBossRenderer;
import io.github.Memoires.trbeyond.entity.client.SlimeDollHeadRenderer;
import io.github.Memoires.trbeyond.entity.client.SlimeDollMinionsRenderer;
import io.github.Memoires.trbeyond.entity.client.SlimeWallRenderer;
import io.github.Memoires.trbeyond.entity.client.StellarMimicBossRenderer;
import io.github.Memoires.trbeyond.entity.client.TempestSerpentBossRenderer;
import io.github.Memoires.trbeyond.entity.client.YellowSlimeDollRenderer;
import io.github.Memoires.trbeyond.entity.client.misc.ChaosEaterRenderer;
import io.github.Memoires.trbeyond.entity.client.misc.PrimedDungeonChestRenderer;
import io.github.Memoires.trbeyond.entity.client.misc.ThrownMinosBardicheProjectileRenderer;
import io.github.Memoires.trbeyond.entity.client.misc.TrainingDummyRenderer;
import io.github.Memoires.trbeyond.entity.client.reused.MinhSlimeRenderer;
import io.github.Memoires.trbeyond.items.armor.BenimaruArmorItem;
import io.github.Memoires.trbeyond.items.armor.DiabloArmorItem;
import io.github.Memoires.trbeyond.items.armor.GabiruArmorItem;
import io.github.Memoires.trbeyond.items.armor.GeldArmorItem;
import io.github.Memoires.trbeyond.items.armor.HakuroArmorItem;
import io.github.Memoires.trbeyond.items.armor.MinosArmorItem;
import io.github.Memoires.trbeyond.items.armor.OgreArmorItem;
import io.github.Memoires.trbeyond.items.armor.SerpentArmorItem;
import io.github.Memoires.trbeyond.items.armor.ShionArmorItem;
import io.github.Memoires.trbeyond.items.armor.SoueiArmorItem;
import io.github.Memoires.trbeyond.items.client.BeyondArmorRenderer;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import io.github.Memoires.trbeyond.registry.menu.BeyondMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.EntityRenderersEvent.AddLayers;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.MOD,
   value = {Dist.CLIENT}
)
public class BeyondClient {
   @SubscribeEvent
   public static void register(FMLClientSetupEvent e) {
      registerEntityRenderers();
      registerBlockRenderers();
      MinecraftForge.EVENT_BUS.register(FontRenderer.class);
   }

   public static void registerEntityRenderers() {
      EntityRenderers.register((EntityType)BeyondEntities.DRYAD.get(), DryadRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.MINH_SLIME.get(), MinhSlimeRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.TRAINING_DUMMY.get(), TrainingDummyRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.BOSS_BLACK_SPIDER.get(), BossBlackSpiderRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.GUILD_LADY.get(), pContext -> new GuildLadyRenderer(pContext, true));
      EntityRenderers.register((EntityType)BeyondEntities.GUILD_TESTER.get(), GuildTesterRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.KUROBE.get(), KurobeRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.MJOLLMILE.get(), MjollmileRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.KAIJIN.get(), KaijinRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.KAORU.get(), KaoruRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.OGRE_LORD.get(), OgreLordRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.OGRE_SUB.get(), OgreSubRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.SCARLET_SLIME.get(), ScarletSlimeRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.KILLER_DRYAD.get(), KillerDryadRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.KING_CRAWLER.get(), KingCrawlerRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.EVIL_CENTIPEDE_BOSS.get(), EvilCentipedeBossRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.TEMPEST_SERPENT_BOSS.get(), TempestSerpentBossRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.KOBY.get(), KobyRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.SLIME_WALL.get(), SlimeWallRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.PRIMED_CHEST.get(), PrimedDungeonChestRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.SILVER_MIMIC_CHEST.get(), MimicChestRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.GOLD_MIMIC_CHEST.get(), MimicChestRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.BLUE_SLIME_DOLL.get(), BlueSlimeDollRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.GREEN_SLIME_DOLL.get(), GreenSlimeDollRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.GREEN_SLIME_DOLL_MINION.get(), GreenSlimeDollRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.RED_SLIME_DOLL.get(), RedSlimeDollRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.YELLOW_SLIME_DOLL.get(), YellowSlimeDollRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.MINOS_BARDICHE.get(), ThrownMinosBardicheProjectileRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.GLENDA_BULLET.get(), GlendaBulletRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.CHAOS_EATER.get(), ChaosEaterRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.GOZUL.get(), GozulRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.MEZUL.get(), MezulRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.LIVING_ARMOR.get(), LivingArmorRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.SKULL_NIGHT.get(), SkullNightRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.MAGE.get(), MageRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.MILIM_SLIME_SCARLET.get(), MilimSlimeScarletRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.BABY_DRAGON.get(), BabyDragonRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.FIRE_BREATH.get(), ThrownItemRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.STELLAR_SHRINE.get(), NoopRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.BRONZE_SHRINE.get(), NoopRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.SILVER_SHRINE.get(), NoopRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.GOLD_SHRINE.get(), NoopRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.MOMIJI.get(), MomijiRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.BENIMARU.get(), BenimaruRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.SHION.get(), ShionRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.GOBWE.get(), GobweRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.GRAVE_LIZARD.get(), GraveLizardRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.SLIME_DOLL_HEAD.get(), SlimeDollHeadRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.SLIME_DOLL_BOSS.get(), SlimeDollBossRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.SLIME_DOLL_MINIONS.get(), SlimeDollMinionsRenderer::new);
      EntityRenderers.register((EntityType)BeyondEntities.STELLAR_MIMIC_BOSS.get(), StellarMimicBossRenderer::new);
   }

   public static void registerBlockRenderers() {
      BlockEntityRenderers.register((BlockEntityType)BeyondBlockEntities.BRONZE_CHEST.get(), BronzeChestRenderer::new);
      BlockEntityRenderers.register((BlockEntityType)BeyondBlockEntities.SILVER_CHEST.get(), SilverChestRenderer::new);
      BlockEntityRenderers.register((BlockEntityType)BeyondBlockEntities.GOLD_CHEST.get(), GoldChestRenderer::new);
      BlockEntityRenderers.register((BlockEntityType)BeyondBlockEntities.STELLAR_CHEST.get(), StellarChestRenderer::new);
      BlockEntityRenderers.register((BlockEntityType)BeyondBlockEntities.ANIMATE_DOOR.get(), DungeonDoorRenderer::new);
      BlockEntityRenderers.register((BlockEntityType)BeyondBlockEntities.DUNGEON_GATE.get(), DungeonGateRenderer::new);
      BlockEntityRenderers.register((BlockEntityType)BeyondBlockEntities.INSIDE_GATE.get(), InsideGateRenderer::new);
      BlockEntityRenderers.register((BlockEntityType)BeyondBlockEntities.DUMBELL_RACK.get(), DumbbellRackRenderer::new);
      BlockEntityRenderers.register((BlockEntityType)BeyondBlockEntities.BENCH_PRESS.get(), BenchPressRenderer::new);
      BlockEntityRenderers.register((BlockEntityType)BeyondBlockEntities.ILLUSIONARY_BLOCK.get(), IllusionaryBlockRenderer::new);
      BlockEntityRenderers.register((BlockEntityType)BeyondBlockEntities.BOSS_SPAWNER.get(), IllusionaryBlockRenderer::new);
      BlockEntityRenderers.register((BlockEntityType)BeyondBlockEntities.SLIME_CEILING.get(), SlimeCeilingRenderer::new);
      BlockEntityRenderers.register((BlockEntityType)BeyondBlockEntities.SLIME_FLOOR.get(), SlimeFloorRenderer::new);
      BlockEntityRenderers.register((BlockEntityType)BeyondBlockEntities.SHRINE.get(), ShrineBlockRenderer::new);
   }

   @SubscribeEvent
   public static void layerDefinitions(RegisterLayerDefinitions event) {
      event.registerLayerDefinition(GlendaBulletModel.LAYER_LOCATION, GlendaBulletModel::createBodyLayer);
      event.registerLayerDefinition(BronzeChestRenderer.BRONZE, BronzeChestRenderer::createBodyLayer);
      event.registerLayerDefinition(SilverChestRenderer.SILVER, SilverChestRenderer::createBodyLayer);
      event.registerLayerDefinition(GoldChestRenderer.GOLD, GoldChestRenderer::createBodyLayer);
      event.registerLayerDefinition(DungeonDoorModel0.LAYER, DungeonDoorModel0::createBodyLayer);
      event.registerLayerDefinition(DungeonDoorModel1.LAYER, DungeonDoorModel1::createBodyLayer);
   }

   @SubscribeEvent
   public static void layerRenderer(AddLayers event) {
      GeoArmorRenderer.registerArmorRenderer(OgreArmorItem.class, BeyondArmorRenderer::new);
      GeoArmorRenderer.registerArmorRenderer(SerpentArmorItem.class, BeyondArmorRenderer::new);
      GeoArmorRenderer.registerArmorRenderer(MinosArmorItem.class, BeyondArmorRenderer::new);
      GeoArmorRenderer.registerArmorRenderer(BenimaruArmorItem.class, BeyondArmorRenderer::new);
      GeoArmorRenderer.registerArmorRenderer(ShionArmorItem.class, BeyondArmorRenderer::new);
      GeoArmorRenderer.registerArmorRenderer(GeldArmorItem.class, BeyondArmorRenderer::new);
      GeoArmorRenderer.registerArmorRenderer(DiabloArmorItem.class, BeyondArmorRenderer::new);
      GeoArmorRenderer.registerArmorRenderer(HakuroArmorItem.class, BeyondArmorRenderer::new);
      GeoArmorRenderer.registerArmorRenderer(GabiruArmorItem.class, BeyondArmorRenderer::new);
      GeoArmorRenderer.registerArmorRenderer(SoueiArmorItem.class, BeyondArmorRenderer::new);
   }

   @SubscribeEvent
   public static void RegisterScreen(FMLClientSetupEvent event) {
      event.enqueueWork(() -> {
         MenuScreens.register((MenuType)BeyondMenus.KUROBE_MENU.get(), KurobeShopScreen::new);
         MenuScreens.register((MenuType)BeyondMenus.MJOLLMILE_MENU.get(), MjollmileScreen::new);
         MenuScreens.register((MenuType)BeyondMenus.RESEARCHER_SHOP_MENU.get(), CustomResearcherEnchantmentScreen::new);
         MenuScreens.register((MenuType)BeyondMenus.KUROBE_MAIN_MENU.get(), KurobeMainMenuScreen::new);
         MenuScreens.register((MenuType)BeyondMenus.SUBSPACE_CONTAINER_MENU.get(), SubspaceContainerScreen::new);
      });
   }

   @SubscribeEvent
   public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
      event.registerAboveAll("challenge", ChallengeOverlay.CHALLENGE_OVERLAY);
   }
}
