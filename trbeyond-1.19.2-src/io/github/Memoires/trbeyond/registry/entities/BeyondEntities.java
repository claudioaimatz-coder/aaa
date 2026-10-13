package io.github.Memoires.trbeyond.registry.entities;

import io.github.Memoires.trbeyond.block.ShrineBlock;
import io.github.Memoires.trbeyond.entity.custom.BabyDragonEntity;
import io.github.Memoires.trbeyond.entity.custom.BenimaruEntity;
import io.github.Memoires.trbeyond.entity.custom.BossBlackSpiderEntity;
import io.github.Memoires.trbeyond.entity.custom.DryadEntity;
import io.github.Memoires.trbeyond.entity.custom.EvilCentipedeBossEntity;
import io.github.Memoires.trbeyond.entity.custom.FireBreathEntity;
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
import io.github.Memoires.trbeyond.entity.custom.SlimeDollHeadEntity;
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
import io.github.Memoires.trbeyond.entity.misc.PrimedDungeonChestEntity;
import io.github.Memoires.trbeyond.entity.projectile.ChaosEaterProjectile;
import io.github.Memoires.trbeyond.entity.projectile.GlendaBulletProjectile;
import io.github.Memoires.trbeyond.entity.projectile.ThrownMinosBardicheProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BeyondEntities {
   public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "trbeyond");
   public static final RegistryObject<EntityType<DryadEntity>> DRYAD = ENTITY_TYPES.register(
      "dryad",
      () -> Builder.of(DryadEntity::new, MobCategory.CREATURE).sized(0.6F, 1.8F).build(new ResourceLocation("trbeyond", "dryad").toString())
   );
   public static final RegistryObject<EntityType<MinhSlimeEntity>> MINH_SLIME = ENTITY_TYPES.register(
      "minh_slime", () -> Builder.of(MinhSlimeEntity::new, MobCategory.CREATURE).build(new ResourceLocation("trbeyond", "minh_slime").toString())
   );
   public static final RegistryObject<EntityType<TrainingDummyEntity>> TRAINING_DUMMY = ENTITY_TYPES.register(
      "training_dummy",
      () -> Builder.of(TrainingDummyEntity::new, MobCategory.MISC).build(new ResourceLocation("trbeyond", "training_dummy").toString())
   );
   public static final RegistryObject<EntityType<GuildLadyEntity>> GUILD_LADY = ENTITY_TYPES.register(
      "guild_lady", () -> Builder.of(GuildLadyEntity::new, MobCategory.CREATURE).build(new ResourceLocation("trbeyond", "guild_lady").toString())
   );
   public static final RegistryObject<EntityType<BossBlackSpiderEntity>> BOSS_BLACK_SPIDER = ENTITY_TYPES.register(
      "boss_black_spider",
      () -> Builder.of(BossBlackSpiderEntity::new, MobCategory.MONSTER)
            .sized(3.0F, 2.5F)
            .build(new ResourceLocation("trbeyond", "boss_black_spider").toString())
   );
   public static final RegistryObject<EntityType<EvilCentipedeBossEntity>> EVIL_CENTIPEDE_BOSS = ENTITY_TYPES.register(
      "evil_centipede_boss",
      () -> Builder.of(EvilCentipedeBossEntity::new, MobCategory.MONSTER)
            .sized(2.0F, 1.5F)
            .build(new ResourceLocation("trbeyond", "evil_centipede_boss").toString())
   );
   public static final RegistryObject<EntityType<TempestSerpentBossEntity>> TEMPEST_SERPENT_BOSS = ENTITY_TYPES.register(
      "tempest_serpent_boss",
      () -> Builder.of(TempestSerpentBossEntity::new, MobCategory.MONSTER)
            .sized(2.5F, 3.0F)
            .build(new ResourceLocation("trbeyond", "tempest_serpent_boss").toString())
   );
   public static final RegistryObject<EntityType<GuildTesterEntity>> GUILD_TESTER = ENTITY_TYPES.register(
      "guild_tester",
      () -> Builder.of(GuildTesterEntity::new, MobCategory.CREATURE).build(new ResourceLocation("trbeyond", "guild_tester").toString())
   );
   public static final RegistryObject<EntityType<KurobeEntity>> KUROBE = ENTITY_TYPES.register(
      "kurobe", () -> Builder.of(KurobeEntity::new, MobCategory.CREATURE).build(new ResourceLocation("trbeyond", "kurobe").toString())
   );
   public static final RegistryObject<EntityType<MjollmileEntity>> MJOLLMILE = ENTITY_TYPES.register(
      "mjollmile", () -> Builder.of(MjollmileEntity::new, MobCategory.CREATURE).build(new ResourceLocation("trbeyond", "mjollmile").toString())
   );
   public static final RegistryObject<EntityType<KaijinEntity>> KAIJIN = ENTITY_TYPES.register(
      "kaijin", () -> Builder.of(KaijinEntity::new, MobCategory.CREATURE).build(new ResourceLocation("trbeyond", "kaijin").toString())
   );
   public static final RegistryObject<EntityType<KaoruEntity>> KAORU = ENTITY_TYPES.register(
      "kaoru", () -> Builder.of(KaoruEntity::new, MobCategory.CREATURE).build(new ResourceLocation("trbeyond", "kaoru").toString())
   );
   public static final RegistryObject<EntityType<OgreLordEntity>> OGRE_LORD = ENTITY_TYPES.register(
      "ogreboss",
      () -> Builder.of(OgreLordEntity::new, MobCategory.MONSTER).sized(1.5F, 3.0F).build(new ResourceLocation("trbeyond", "ogrelord").toString())
   );
   public static final RegistryObject<EntityType<KingCrawlerEntity>> KING_CRAWLER = ENTITY_TYPES.register(
      "king_crawler",
      () -> Builder.of(KingCrawlerEntity::new, MobCategory.MONSTER)
            .sized(1.6F, 3.0F)
            .build(new ResourceLocation("trbeyond", "kingcrawler").toString())
   );
   public static final RegistryObject<EntityType<OgreSubEntity>> OGRE_SUB = ENTITY_TYPES.register(
      "ogre_subordinate",
      () -> Builder.of(OgreSubEntity::new, MobCategory.MONSTER)
            .sized(1.0F, 2.0F)
            .build(new ResourceLocation("trbeyond", "ogre_subordinate").toString())
   );
   public static final RegistryObject<EntityType<KillerDryadEntity>> KILLER_DRYAD = ENTITY_TYPES.register(
      "killer_dryad",
      () -> Builder.of(KillerDryadEntity::new, MobCategory.MONSTER)
            .sized(1.0F, 1.8F)
            .build(new ResourceLocation("trbeyond", "killerdryad").toString())
   );
   public static final RegistryObject<EntityType<KobyEntity>> KOBY = ENTITY_TYPES.register(
      "koby", () -> Builder.of(KobyEntity::new, MobCategory.CREATURE).build(new ResourceLocation("trbeyond", "koby").toString())
   );
   public static final RegistryObject<EntityType<ScarletSlimeEntity>> SCARLET_SLIME = ENTITY_TYPES.register(
      "scarlet_slime",
      () -> Builder.of(ScarletSlimeEntity::new, MobCategory.MONSTER)
            .sized(1.0F, 0.7F)
            .build(new ResourceLocation("trbeyond", "scarlet_slime").toString())
   );
   public static final RegistryObject<EntityType<SlimeWallEntity>> SLIME_WALL = ENTITY_TYPES.register(
      "slime_wall",
      () -> Builder.of(SlimeWallEntity::new, MobCategory.MONSTER)
            .fireImmune()
            .sized(2.7F, 2.9F)
            .build(new ResourceLocation("trbeyond", "slime_wall").toString())
   );
   public static final RegistryObject<EntityType<PrimedDungeonChestEntity>> PRIMED_CHEST = ENTITY_TYPES.register(
      "primed_dungeon_chest",
      () -> Builder.of(PrimedDungeonChestEntity::new, MobCategory.MISC)
            .fireImmune()
            .sized(1.0F, 1.0F)
            .clientTrackingRange(10)
            .updateInterval(10)
            .build(new ResourceLocation("trbeyond", "primed_dungeon_chest").toString())
   );
   public static final RegistryObject<EntityType<MimicChestEntity>> SILVER_MIMIC_CHEST = ENTITY_TYPES.register(
      "silver_mimic_chest",
      () -> Builder.of((type, level) -> new MimicChestEntity(type, level, false), MobCategory.MISC)
            .fireImmune()
            .sized(1.0F, 1.0F)
            .clientTrackingRange(10)
            .updateInterval(10)
            .build(new ResourceLocation("trbeyond", "silver_mimic_chest").toString())
   );
   public static final RegistryObject<EntityType<MimicChestEntity>> GOLD_MIMIC_CHEST = ENTITY_TYPES.register(
      "gold_mimic_chest",
      () -> Builder.of((type, level) -> new MimicChestEntity(type, level, true), MobCategory.MISC)
            .fireImmune()
            .sized(1.0F, 1.0F)
            .clientTrackingRange(10)
            .updateInterval(10)
            .build(new ResourceLocation("trbeyond", "gold_mimic_chest").toString())
   );
   public static final RegistryObject<EntityType<BlueSlimeDollEntity>> BLUE_SLIME_DOLL = ENTITY_TYPES.register(
      "blue_slime_doll",
      () -> Builder.of(BlueSlimeDollEntity::new, MobCategory.MONSTER)
            .sized(0.8F, 2.0F)
            .build(new ResourceLocation("trbeyond", "blue_slime_doll").toString())
   );
   public static final RegistryObject<EntityType<GreenSlimeDollEntity>> GREEN_SLIME_DOLL = ENTITY_TYPES.register(
      "green_slime_doll",
      () -> Builder.of(GreenSlimeDollEntity::new, MobCategory.MONSTER)
            .sized(0.8F, 2.0F)
            .build(new ResourceLocation("trbeyond", "green_slime_doll").toString())
   );
   public static final RegistryObject<EntityType<GreenSlimeDollEntity>> GREEN_SLIME_DOLL_MINION = ENTITY_TYPES.register(
      "green_slime_doll_minion",
      () -> Builder.of(GreenSlimeDollEntity::new, MobCategory.MONSTER)
            .sized(0.8F, 2.0F)
            .build(new ResourceLocation("trbeyond", "green_slime_doll_minion").toString())
   );
   public static final RegistryObject<EntityType<RedSlimeDollEntity>> RED_SLIME_DOLL = ENTITY_TYPES.register(
      "red_slime_doll",
      () -> Builder.of(RedSlimeDollEntity::new, MobCategory.MONSTER)
            .sized(0.8F, 2.0F)
            .build(new ResourceLocation("trbeyond", "red_slime_doll").toString())
   );
   public static final RegistryObject<EntityType<YellowSlimeDollEntity>> YELLOW_SLIME_DOLL = ENTITY_TYPES.register(
      "yellow_slime_doll",
      () -> Builder.of(YellowSlimeDollEntity::new, MobCategory.MONSTER)
            .sized(0.8F, 2.0F)
            .build(new ResourceLocation("trbeyond", "yellow_slime_doll").toString())
   );
   public static final RegistryObject<EntityType<GlendaBulletProjectile>> GLENDA_BULLET = ENTITY_TYPES.register(
      "glenda_bullet",
      () -> Builder.of(GlendaBulletProjectile::new, MobCategory.MISC)
            .sized(0.75F, 0.75F)
            .build(new ResourceLocation("trbeyond", "glenda_bullet").toString())
   );
   public static final RegistryObject<EntityType<ThrownMinosBardicheProjectile>> MINOS_BARDICHE = ENTITY_TYPES.register(
      "minos_bardiche",
      () -> Builder.of(ThrownMinosBardicheProjectile::new, MobCategory.MISC)
            .sized(0.75F, 0.75F)
            .build(new ResourceLocation("trbeyond", "minos_bardiche").toString())
   );
   public static final RegistryObject<EntityType<GozulEntity>> GOZUL = ENTITY_TYPES.register(
      "gozul",
      () -> Builder.of(GozulEntity::new, MobCategory.MONSTER)
            .sized(1.1F, 3.5F)
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .setCustomClientFactory(GozulEntity::new)
            .build(new ResourceLocation("trbeyond", "gozul").toString())
   );
   public static final RegistryObject<EntityType<MezulEntity>> MEZUL = ENTITY_TYPES.register(
      "mezul",
      () -> Builder.of(MezulEntity::new, MobCategory.MONSTER)
            .sized(1.2F, 3.0F)
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .setCustomClientFactory(MezulEntity::new)
            .build(new ResourceLocation("trbeyond", "mezul").toString())
   );
   public static final RegistryObject<EntityType<LivingArmorEntity>> LIVING_ARMOR = ENTITY_TYPES.register(
      "living_armor",
      () -> Builder.of(LivingArmorEntity::new, MobCategory.MONSTER)
            .sized(1.0F, 2.0F)
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .setCustomClientFactory(LivingArmorEntity::new)
            .build(new ResourceLocation("trbeyond", "living_armor").toString())
   );
   public static final RegistryObject<EntityType<SkullNightEntity>> SKULL_NIGHT = ENTITY_TYPES.register(
      "skull_night",
      () -> Builder.of(SkullNightEntity::new, MobCategory.MONSTER)
            .sized(0.8F, 2.0F)
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .setCustomClientFactory(SkullNightEntity::new)
            .build(new ResourceLocation("trbeyond", "skull_night").toString())
   );
   public static final RegistryObject<EntityType<MageEntity>> MAGE = ENTITY_TYPES.register(
      "mage",
      () -> Builder.of(MageEntity::new, MobCategory.MONSTER)
            .sized(1.0F, 2.0F)
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .setCustomClientFactory(MageEntity::new)
            .build(new ResourceLocation("trbeyond", "mage").toString())
   );
   public static final RegistryObject<EntityType<MilimSlimeScarletEntity>> MILIM_SLIME_SCARLET = ENTITY_TYPES.register(
      "milim_slime_scarlet",
      () -> Builder.of(MilimSlimeScarletEntity::new, MobCategory.MONSTER)
            .sized(0.6F, 0.8F)
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .setCustomClientFactory(MilimSlimeScarletEntity::new)
            .build(new ResourceLocation("trbeyond", "milim_slime_scarlet").toString())
   );
   public static final RegistryObject<EntityType<BabyDragonEntity>> BABY_DRAGON = ENTITY_TYPES.register(
      "baby_dragon",
      () -> Builder.of(BabyDragonEntity::new, MobCategory.MONSTER)
            .sized(1.0F, 2.0F)
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .setCustomClientFactory(BabyDragonEntity::new)
            .fireImmune()
            .build(new ResourceLocation("trbeyond", "baby_dragon").toString())
   );
   public static final RegistryObject<EntityType<FireBreathEntity>> FIRE_BREATH = ENTITY_TYPES.register(
      "projectile_fire_breath",
      () -> Builder.of(FireBreathEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .setCustomClientFactory(FireBreathEntity::new)
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(64)
            .setUpdateInterval(1)
            .build(new ResourceLocation("trbeyond", "projectile_fire_breath").toString())
   );
   public static final RegistryObject<EntityType<ChaosEaterProjectile>> CHAOS_EATER = ENTITY_TYPES.register(
      "chaos_eater",
      () -> Builder.of(ChaosEaterProjectile::new, MobCategory.MISC)
            .sized(0.75F, 0.75F)
            .build(new ResourceLocation("trbeyond", "chaos_eater").toString())
   );
   public static final RegistryObject<EntityType<ShrineEntity>> STELLAR_SHRINE = ENTITY_TYPES.register(
      "stellar_shrine", () -> Builder.of(new EntityFactory<ShrineEntity>() {
            public ShrineEntity create(EntityType<ShrineEntity> type, Level level) {
               return new ShrineEntity(type, level, ShrineBlock.DungeonShrineTier.STELLAR);
            }
         }, MobCategory.CREATURE).build(new ResourceLocation("trbeyond", "stellar_shrine").toString())
   );
   public static final RegistryObject<EntityType<ShrineEntity>> BRONZE_SHRINE = ENTITY_TYPES.register(
      "bronze_shrine", () -> Builder.of(new EntityFactory<ShrineEntity>() {
            public ShrineEntity create(EntityType<ShrineEntity> type, Level level) {
               return new ShrineEntity(type, level, ShrineBlock.DungeonShrineTier.BRONZE);
            }
         }, MobCategory.CREATURE).build(new ResourceLocation("trbeyond", "bronze_shrine").toString())
   );
   public static final RegistryObject<EntityType<ShrineEntity>> SILVER_SHRINE = ENTITY_TYPES.register(
      "silver_shrine", () -> Builder.of(new EntityFactory<ShrineEntity>() {
            public ShrineEntity create(EntityType<ShrineEntity> type, Level level) {
               return new ShrineEntity(type, level, ShrineBlock.DungeonShrineTier.SILVER);
            }
         }, MobCategory.CREATURE).build(new ResourceLocation("trbeyond", "silver_shrine").toString())
   );
   public static final RegistryObject<EntityType<ShrineEntity>> GOLD_SHRINE = ENTITY_TYPES.register(
      "gold_shrine", () -> Builder.of(new EntityFactory<ShrineEntity>() {
            public ShrineEntity create(EntityType<ShrineEntity> type, Level level) {
               return new ShrineEntity(type, level, ShrineBlock.DungeonShrineTier.GOLD);
            }
         }, MobCategory.CREATURE).build(new ResourceLocation("trbeyond", "gold_shrine").toString())
   );
   public static final RegistryObject<EntityType<MomijiEntity>> MOMIJI = ENTITY_TYPES.register(
      "momiji",
      () -> Builder.of(MomijiEntity::new, MobCategory.CREATURE).sized(0.7F, 2.0F).build(new ResourceLocation("trbeyond", "momiji").toString())
   );
   public static final RegistryObject<EntityType<BenimaruEntity>> BENIMARU = ENTITY_TYPES.register(
      "benimaru",
      () -> Builder.of(BenimaruEntity::new, MobCategory.CREATURE).sized(0.7F, 2.0F).build(new ResourceLocation("trbeyond", "benimaru").toString())
   );
   public static final RegistryObject<EntityType<ShionEntity>> SHION = ENTITY_TYPES.register(
      "shion",
      () -> Builder.of(ShionEntity::new, MobCategory.CREATURE).sized(0.7F, 2.0F).build(new ResourceLocation("trbeyond", "shion").toString())
   );
   public static final RegistryObject<EntityType<GobweEntity>> GOBWE = ENTITY_TYPES.register(
      "gobwe",
      () -> Builder.of(GobweEntity::new, MobCategory.CREATURE).sized(0.7F, 1.0F).build(new ResourceLocation("trbeyond", "gobwe").toString())
   );
   public static final RegistryObject<EntityType<GraveLizard>> GRAVE_LIZARD = ENTITY_TYPES.register(
      "grave_lizard", () -> Builder.of(GraveLizard::new, MobCategory.MONSTER).sized(3.0F, 4.0F).build("grave_lizard")
   );
   public static final RegistryObject<EntityType<StellarMimicBossEntity>> STELLAR_MIMIC_BOSS = ENTITY_TYPES.register(
      "stellar_mimic_boss", () -> Builder.of(StellarMimicBossEntity::new, MobCategory.MONSTER).sized(2.0F, 1.5F).build("stellar_mimic_boss")
   );
   public static final RegistryObject<EntityType<SlimeDollBossEntity>> SLIME_DOLL_BOSS = ENTITY_TYPES.register(
      "slime_doll_fusion", () -> Builder.of(SlimeDollBossEntity::new, MobCategory.MONSTER).sized(2.0F, 3.0F).build("slime_doll_fusion")
   );
   public static final RegistryObject<EntityType<SlimeDollMinions>> SLIME_DOLL_MINIONS = ENTITY_TYPES.register(
      "slime_doll_minions", () -> Builder.of(SlimeDollMinions::new, MobCategory.MONSTER).sized(0.8F, 1.6F).build("slime_doll_minions")
   );
   public static final RegistryObject<EntityType<SlimeDollHeadEntity>> SLIME_DOLL_HEAD = ENTITY_TYPES.register(
      "slime_doll_head",
      () -> Builder.of(SlimeDollHeadEntity::new, MobCategory.MISC).sized(1.0F, 1.0F).build(new ResourceLocation("trbeyond", "gobwe").toString())
   );

   public static void register(IEventBus eventBus) {
      ENTITY_TYPES.register(eventBus);
   }
}
