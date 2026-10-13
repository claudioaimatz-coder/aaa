package io.github.Memoires.trbeyond.handlers;

import io.github.Memoires.trbeyond.entity.custom.BabyDragonEntity;
import io.github.Memoires.trbeyond.entity.custom.BenimaruEntity;
import io.github.Memoires.trbeyond.entity.custom.BossBlackSpiderEntity;
import io.github.Memoires.trbeyond.entity.custom.DryadEntity;
import io.github.Memoires.trbeyond.entity.custom.EvilCentipedeBossEntity;
import io.github.Memoires.trbeyond.entity.custom.GobweEntity;
import io.github.Memoires.trbeyond.entity.custom.GozulEntity;
import io.github.Memoires.trbeyond.entity.custom.GraveLizard;
import io.github.Memoires.trbeyond.entity.custom.GuildLadyEntity;
import io.github.Memoires.trbeyond.entity.custom.GuildTesterEntity;
import io.github.Memoires.trbeyond.entity.custom.KaijinEntity;
import io.github.Memoires.trbeyond.entity.custom.KaoruEntity;
import io.github.Memoires.trbeyond.entity.custom.KillerDryadEntity;
import io.github.Memoires.trbeyond.entity.custom.KingCrawlerEntity;
import io.github.Memoires.trbeyond.entity.custom.KobyEntity;
import io.github.Memoires.trbeyond.entity.custom.KurobeEntity;
import io.github.Memoires.trbeyond.entity.custom.LivingArmorEntity;
import io.github.Memoires.trbeyond.entity.custom.MageEntity;
import io.github.Memoires.trbeyond.entity.custom.MezulEntity;
import io.github.Memoires.trbeyond.entity.custom.MilimSlimeScarletEntity;
import io.github.Memoires.trbeyond.entity.custom.MinhSlimeEntity;
import io.github.Memoires.trbeyond.entity.custom.MjollmileEntity;
import io.github.Memoires.trbeyond.entity.custom.MomijiEntity;
import io.github.Memoires.trbeyond.entity.custom.OgreLordEntity;
import io.github.Memoires.trbeyond.entity.custom.OgreSubEntity;
import io.github.Memoires.trbeyond.entity.custom.ScarletSlimeEntity;
import io.github.Memoires.trbeyond.entity.custom.ShionEntity;
import io.github.Memoires.trbeyond.entity.custom.ShrineEntity;
import io.github.Memoires.trbeyond.entity.custom.SkullNightEntity;
import io.github.Memoires.trbeyond.entity.custom.SlimeDollBossEntity;
import io.github.Memoires.trbeyond.entity.custom.SlimeDollMinions;
import io.github.Memoires.trbeyond.entity.custom.SlimeWallEntity;
import io.github.Memoires.trbeyond.entity.custom.StellarMimicBossEntity;
import io.github.Memoires.trbeyond.entity.custom.TempestSerpentBossEntity;
import io.github.Memoires.trbeyond.entity.custom.TrainingDummyEntity;
import io.github.Memoires.trbeyond.entity.custom.slime.BlueSlimeDollEntity;
import io.github.Memoires.trbeyond.entity.custom.slime.GreenSlimeDollEntity;
import io.github.Memoires.trbeyond.entity.custom.slime.RedSlimeDollEntity;
import io.github.Memoires.trbeyond.entity.custom.slime.YellowSlimeDollEntity;
import io.github.Memoires.trbeyond.entity.misc.MimicChestEntity;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.MOD
)
public class BeyondEntityHanders {
   @SubscribeEvent
   public static void registerAttributes(EntityAttributeCreationEvent event) {
      event.put((EntityType)BeyondEntities.DRYAD.get(), DryadEntity.setAttributes());
      event.put((EntityType)BeyondEntities.MINH_SLIME.get(), MinhSlimeEntity.setAttributes());
      event.put((EntityType)BeyondEntities.TRAINING_DUMMY.get(), TrainingDummyEntity.setAttributes());
      event.put((EntityType)BeyondEntities.BOSS_BLACK_SPIDER.get(), BossBlackSpiderEntity.setAttributes());
      event.put((EntityType)BeyondEntities.GUILD_LADY.get(), GuildLadyEntity.setAttributes());
      event.put((EntityType)BeyondEntities.GUILD_TESTER.get(), GuildTesterEntity.setAttributes());
      event.put((EntityType)BeyondEntities.KUROBE.get(), KurobeEntity.setAttributes());
      event.put((EntityType)BeyondEntities.MJOLLMILE.get(), MjollmileEntity.setAttributes());
      event.put((EntityType)BeyondEntities.KAIJIN.get(), KaijinEntity.setAttributes());
      event.put((EntityType)BeyondEntities.KAORU.get(), KaoruEntity.setAttributes());
      event.put((EntityType)BeyondEntities.OGRE_LORD.get(), OgreLordEntity.setAttributes());
      event.put((EntityType)BeyondEntities.OGRE_SUB.get(), OgreSubEntity.setAttributes());
      event.put((EntityType)BeyondEntities.KILLER_DRYAD.get(), KillerDryadEntity.setAttributes());
      event.put((EntityType)BeyondEntities.KING_CRAWLER.get(), KingCrawlerEntity.setAttributes());
      event.put((EntityType)BeyondEntities.EVIL_CENTIPEDE_BOSS.get(), EvilCentipedeBossEntity.setAttributes());
      event.put((EntityType)BeyondEntities.TEMPEST_SERPENT_BOSS.get(), TempestSerpentBossEntity.setAttributes());
      event.put((EntityType)BeyondEntities.KOBY.get(), KobyEntity.setAttributes());
      event.put((EntityType)BeyondEntities.SLIME_WALL.get(), SlimeWallEntity.setAttributes());
      event.put((EntityType)BeyondEntities.SILVER_MIMIC_CHEST.get(), MimicChestEntity.setAttributes());
      event.put((EntityType)BeyondEntities.GOLD_MIMIC_CHEST.get(), MimicChestEntity.setAttributes());
      event.put((EntityType)BeyondEntities.BLUE_SLIME_DOLL.get(), BlueSlimeDollEntity.setAttributes());
      event.put((EntityType)BeyondEntities.GREEN_SLIME_DOLL.get(), GreenSlimeDollEntity.setAttributes());
      event.put((EntityType)BeyondEntities.GREEN_SLIME_DOLL_MINION.get(), GreenSlimeDollEntity.setMinionAttributes());
      event.put((EntityType)BeyondEntities.RED_SLIME_DOLL.get(), RedSlimeDollEntity.setAttributes());
      event.put((EntityType)BeyondEntities.YELLOW_SLIME_DOLL.get(), YellowSlimeDollEntity.setAttributes());
      event.put((EntityType)BeyondEntities.SCARLET_SLIME.get(), ScarletSlimeEntity.setAttributes());
      event.put((EntityType)BeyondEntities.GOZUL.get(), GozulEntity.createAttributes().build());
      event.put((EntityType)BeyondEntities.MEZUL.get(), MezulEntity.createAttributes().build());
      event.put((EntityType)BeyondEntities.LIVING_ARMOR.get(), LivingArmorEntity.createAttributes().build());
      event.put((EntityType)BeyondEntities.SKULL_NIGHT.get(), SkullNightEntity.createAttributes().build());
      event.put((EntityType)BeyondEntities.MAGE.get(), MageEntity.createAttributes().build());
      event.put((EntityType)BeyondEntities.MILIM_SLIME_SCARLET.get(), MilimSlimeScarletEntity.createAttributes().build());
      event.put((EntityType)BeyondEntities.BABY_DRAGON.get(), BabyDragonEntity.createAttributes().build());
      event.put((EntityType)BeyondEntities.STELLAR_SHRINE.get(), ShrineEntity.setAttributes());
      event.put((EntityType)BeyondEntities.BRONZE_SHRINE.get(), ShrineEntity.setAttributes());
      event.put((EntityType)BeyondEntities.SILVER_SHRINE.get(), ShrineEntity.setAttributes());
      event.put((EntityType)BeyondEntities.GOLD_SHRINE.get(), ShrineEntity.setAttributes());
      event.put((EntityType)BeyondEntities.MOMIJI.get(), MomijiEntity.setAttributes());
      event.put((EntityType)BeyondEntities.BENIMARU.get(), BenimaruEntity.setAttributes());
      event.put((EntityType)BeyondEntities.SHION.get(), ShionEntity.setAttributes());
      event.put((EntityType)BeyondEntities.GRAVE_LIZARD.get(), GraveLizard.createAttributes().build());
      event.put((EntityType)BeyondEntities.GOBWE.get(), GobweEntity.setAttributes());
      event.put((EntityType)BeyondEntities.STELLAR_MIMIC_BOSS.get(), StellarMimicBossEntity.createAttributes().build());
      event.put((EntityType)BeyondEntities.SLIME_DOLL_BOSS.get(), SlimeDollBossEntity.createAttributes().build());
      event.put((EntityType)BeyondEntities.SLIME_DOLL_MINIONS.get(), SlimeDollMinions.createAttributes().build());
   }
}
