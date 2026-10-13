package io.github.Memoires.trbeyond.registry.blocks;

import io.github.Memoires.trbeyond.block.BenchPressBlock;
import io.github.Memoires.trbeyond.block.BossSpiderEggBlock;
import io.github.Memoires.trbeyond.block.BronzeChestBlock;
import io.github.Memoires.trbeyond.block.BronzeShrineBlock;
import io.github.Memoires.trbeyond.block.DumbbellRackBlock;
import io.github.Memoires.trbeyond.block.DungeonAnchor;
import io.github.Memoires.trbeyond.block.DungeonChestBlock;
import io.github.Memoires.trbeyond.block.DungeonEntryBlock;
import io.github.Memoires.trbeyond.block.DungeonGateBlock;
import io.github.Memoires.trbeyond.block.DungeonGateTeleporterBlock;
import io.github.Memoires.trbeyond.block.DungeonUnlockerBlock;
import io.github.Memoires.trbeyond.block.GoldChestBlock;
import io.github.Memoires.trbeyond.block.GoldShrineBlock;
import io.github.Memoires.trbeyond.block.IllusionaryBlock;
import io.github.Memoires.trbeyond.block.InsideGateBlock;
import io.github.Memoires.trbeyond.block.LanthornBlock;
import io.github.Memoires.trbeyond.block.MovingTileBlock;
import io.github.Memoires.trbeyond.block.RespawnBossBlock;
import io.github.Memoires.trbeyond.block.RoomEntryBlock;
import io.github.Memoires.trbeyond.block.RotatingTileBlock;
import io.github.Memoires.trbeyond.block.SealingAirBlock;
import io.github.Memoires.trbeyond.block.SilverChestBlock;
import io.github.Memoires.trbeyond.block.SilverShrineBlock;
import io.github.Memoires.trbeyond.block.SlimeCeilingBlock;
import io.github.Memoires.trbeyond.block.SlimeFloorBlock;
import io.github.Memoires.trbeyond.block.StellarChestBlock;
import io.github.Memoires.trbeyond.block.StellarShrineBlock;
import io.github.Memoires.trbeyond.block.ThreadBlock;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.registry.items.BeyondItems;
import java.util.function.Supplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class BeyondBlocks {
   public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "trbeyond");
   public static final RegistryObject<Block> PURE_BLACK_BLOCK = registerBlock(
      "pure_black_block", () -> new Block(Properties.of(Material.GLASS).strength(-1.0F, 3600000.0F)), BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> POLISHED_DIORITE_WALL = registerBlock(
      "polished_diorite_wall", () -> new WallBlock(Properties.of(Material.STONE).strength(5.0F)), BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> ANDESITE_TRAPDOOR = registerBlock(
      "andesite_trapdoor", () -> new TrapDoorBlock(Properties.of(Material.STONE).strength(3.0F).noOcclusion()), BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> BRICK_TRAPDOOR = registerBlock(
      "brick_trapdoor", () -> new TrapDoorBlock(Properties.of(Material.STONE).strength(3.0F).noOcclusion()), BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> DUNGEON_GATE = registerBlock(
      "dungeon_gate", () -> new DungeonGateBlock(Properties.of(Material.METAL).strength(6.0F).requiresCorrectToolForDrops().noOcclusion()), BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> DUNGEON_GATE_TELEPORTER = registerBlock(
      "dungeon_gate_teleporter",
      () -> new DungeonGateTeleporterBlock(Properties.of(Material.METAL).strength(6.0F).requiresCorrectToolForDrops().noOcclusion()),
      BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> INSIDE_GATE = registerBlock(
      "inside_gate", () -> new InsideGateBlock(Properties.of(Material.METAL).strength(6.0F).requiresCorrectToolForDrops().noOcclusion()), BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> DUNGEON_ENTRY = registerBlock("dungeon_entry", DungeonEntryBlock::new, BeyondItemGroup.BEYOND_ITEMS);
   public static final RegistryObject<Block> ROOM_ENTRY = registerBlock("room_entry", RoomEntryBlock::new, BeyondItemGroup.BEYOND_ITEMS);
   public static final RegistryObject<Block> BRONZE_CHEST = registerBlock("bronze_chest", BronzeChestBlock::new, BeyondItemGroup.BEYOND_ITEMS);
   public static final RegistryObject<Block> SILVER_CHEST = registerBlock("silver_chest", SilverChestBlock::new, BeyondItemGroup.BEYOND_ITEMS);
   public static final RegistryObject<Block> GOLD_CHEST = registerBlock("gold_chest", GoldChestBlock::new, BeyondItemGroup.BEYOND_ITEMS);
   public static final RegistryObject<Block> BRONZE_TNT_CHEST = registerBlock(
      "bronze_tnt_chest", () -> new BronzeChestBlock(DungeonChestBlock.DungeonChestType.TNT), BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> SILVER_TNT_CHEST = registerBlock(
      "silver_tnt_chest", () -> new SilverChestBlock(DungeonChestBlock.DungeonChestType.TNT), BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> GOLD_TNT_CHEST = registerBlock(
      "gold_tnt_chest", () -> new GoldChestBlock(DungeonChestBlock.DungeonChestType.TNT), BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> BRONZE_PARALYSIS_CHEST = registerBlock(
      "bronze_paralysis_chest", () -> new BronzeChestBlock(DungeonChestBlock.DungeonChestType.PARALYSIS), BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> SILVER_PARALYSIS_CHEST = registerBlock(
      "silver_paralysis_chest", () -> new SilverChestBlock(DungeonChestBlock.DungeonChestType.PARALYSIS), BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> GOLD_PARALYSIS_CHEST = registerBlock(
      "gold_paralysis_chest", () -> new GoldChestBlock(DungeonChestBlock.DungeonChestType.PARALYSIS), BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> SILVER_MIMIC_CHEST = registerBlock(
      "silver_mimic_chest", () -> new SilverChestBlock(DungeonChestBlock.DungeonChestType.MIMIC), BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> GOLD_MIMIC_CHEST = registerBlock(
      "gold_mimic_chest", () -> new GoldChestBlock(DungeonChestBlock.DungeonChestType.MIMIC), BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> STELLAR_CHEST = registerBlock("stellar_chest", StellarChestBlock::new, BeyondItemGroup.BEYOND_ITEMS);
   public static final RegistryObject<Block> STELLAR_TNT_CHEST = registerBlock(
      "stellar_tnt_chest", () -> new StellarChestBlock(DungeonChestBlock.DungeonChestType.TNT), BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> STELLAR_PARALYSIS_CHEST = registerBlock(
      "stellar_paralysis_chest", () -> new StellarChestBlock(DungeonChestBlock.DungeonChestType.PARALYSIS), BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> SMOOTH_SANDSTONE_STAIRS = registerBlock(
      "smooth_sandstone_stairs",
      () -> new StairBlock(Blocks.SMOOTH_SANDSTONE.defaultBlockState(), Properties.of(Material.STONE).strength(2.0F, 6.0F)),
      BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<IllusionaryBlock> ILLUSIONARY_BLOCK = registerBlock(
      "illusionary_block", IllusionaryBlock::new, BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<MovingTileBlock> MOVING_TILE_BLOCK = registerBlock(
      "moving_tile_block", MovingTileBlock::new, BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<RotatingTileBlock> ROTATING_TILE_BLOCK = registerBlock(
      "rotating_tile_block", RotatingTileBlock::new, BeyondItemGroup.BEYOND_ITEMS
   );
   public static final RegistryObject<Block> SEALING_AIR = registerBlock("sealing_air", SealingAirBlock::new, BeyondItemGroup.BEYOND_ITEMS);
   public static final RegistryObject<SlimeCeilingBlock> SLIME_CEILING = registerBlock("slime_ceiling", SlimeCeilingBlock::new, BeyondItemGroup.BEYOND_ITEMS);
   public static final RegistryObject<SlimeFloorBlock> SLIME_FLOOR = registerBlock("slime_floor", SlimeFloorBlock::new, BeyondItemGroup.BEYOND_ITEMS);
   public static final RegistryObject<Block> STEEL_THREAD = registerBlock("steel_thread", ThreadBlock::new, BeyondItemGroup.BEYOND_ITEMS);
   public static final RegistryObject<Block> STEEL_THREAD_DIAGONAL = registerBlock("steel_thread_diagonal", ThreadBlock::new, BeyondItemGroup.BEYOND_ITEMS);
   public static final RegistryObject<Block> BOSS_SPIDER_EGG = registerBlock("boss_spider_egg", BossSpiderEggBlock::new, BeyondItemGroup.BEYOND_ITEMS);
   public static final RegistryObject<Block> LANTHORN_BLOCK = registerBlock("lanthorn_block", LanthornBlock::new, BeyondItemGroup.BEYOND_ITEMS);
   public static final RegistryObject<Block> DUMBELL_RACK = registerBlock(
      "dumbbell_rack",
      () -> new DumbbellRackBlock(Material.METAL, properties -> properties.strength(3.0F, 1200.0F).sound(SoundType.ANVIL).noOcclusion().requiresCorrectToolForDrops())
   );
   public static final RegistryObject<Block> BENCH_PRESS = registerBlock(
      "bench_press",
      () -> new BenchPressBlock(Material.METAL, properties -> properties.strength(3.0F, 1200.0F).sound(SoundType.ANVIL).noOcclusion().requiresCorrectToolForDrops())
   );
   public static final RegistryObject<Block> BRONZE_SHRINE = registerBlock("bronze_shrine", BronzeShrineBlock::new, BeyondItemGroup.BEYOND_ITEMS);
   public static final RegistryObject<Block> SILVER_SHRINE = registerBlock("silver_shrine", SilverShrineBlock::new, BeyondItemGroup.BEYOND_ITEMS);
   public static final RegistryObject<Block> GOLD_SHRINE = registerBlock("gold_shrine", GoldShrineBlock::new, BeyondItemGroup.BEYOND_ITEMS);
   public static final RegistryObject<Block> STELLAR_SHRINE = registerBlock("stellar_shrine", StellarShrineBlock::new, BeyondItemGroup.BEYOND_ITEMS);
   public static final RegistryObject<Block> BOSS_SPAWNER = BLOCKS.register(
      "boss_respawner", () -> new RespawnBossBlock(Properties.of(Material.METAL).strength(3.5F).requiresCorrectToolForDrops())
   );
   public static final RegistryObject<DungeonAnchor> DUNGEON_ANCHOR = registerBlock("dungeon_anchor", DungeonAnchor::new);
   public static final RegistryObject<DungeonUnlockerBlock> DUNGEON_UNLOCKER_BLOCK = registerBlock("dungeon_unlocker", DungeonUnlockerBlock::new);

   private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, @Nullable CreativeModeTab tab) {
      RegistryObject<T> toReturn = BLOCKS.register(name, block);
      registerBlockItem(name, toReturn, tab);
      return toReturn;
   }

   private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, Supplier<T> block, @Nullable CreativeModeTab tab) {
      return tab == null
         ? BeyondItems.ITEMS.register(name, () -> new BlockItem(block.get(), new net.minecraft.world.item.Item.Properties()))
         : BeyondItems.ITEMS.register(name, () -> new BlockItem(block.get(), new net.minecraft.world.item.Item.Properties().tab(tab)));
   }

   private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
      RegistryObject<T> obj = BLOCKS.register(name, block);
      BeyondItems.ITEMS
         .register(name, () -> new BlockItem((Block)obj.get(), new net.minecraft.world.item.Item.Properties().tab(BeyondItemGroup.BEYOND_ITEMS)));
      return obj;
   }

   public static void register(IEventBus eventBus) {
      BLOCKS.register(eventBus);
   }
}
