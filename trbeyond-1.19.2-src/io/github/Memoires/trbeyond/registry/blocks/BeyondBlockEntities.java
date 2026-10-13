package io.github.Memoires.trbeyond.registry.blocks;

import io.github.Memoires.trbeyond.block.entity.AnchorBlockEntity;
import io.github.Memoires.trbeyond.block.entity.AnimateDoorBlockEntity;
import io.github.Memoires.trbeyond.block.entity.BenchPressBlockEntity;
import io.github.Memoires.trbeyond.block.entity.BronzeChestBlockEntity;
import io.github.Memoires.trbeyond.block.entity.DumbbellRackBlockEntity;
import io.github.Memoires.trbeyond.block.entity.DungeonGateBlockEntity;
import io.github.Memoires.trbeyond.block.entity.GoldChestBlockEntity;
import io.github.Memoires.trbeyond.block.entity.IllusionaryBlockEntity;
import io.github.Memoires.trbeyond.block.entity.InsideGateBlockEntity;
import io.github.Memoires.trbeyond.block.entity.LanthornBlockEntity;
import io.github.Memoires.trbeyond.block.entity.RespawnBossBlockEntity;
import io.github.Memoires.trbeyond.block.entity.ShrineBlockEntity;
import io.github.Memoires.trbeyond.block.entity.SilverChestBlockEntity;
import io.github.Memoires.trbeyond.block.entity.SlimeCeilingBlockEntity;
import io.github.Memoires.trbeyond.block.entity.SlimeFloorBlockEntity;
import io.github.Memoires.trbeyond.block.entity.StellarChestBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BeyondBlockEntities {
   public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "trbeyond");
   public static final RegistryObject<BlockEntityType<BronzeChestBlockEntity>> BRONZE_CHEST = BLOCK_ENTITIES.register(
      "bronze_chest",
      () -> Builder.of(
               BronzeChestBlockEntity::new,
               new Block[]{
                  (Block)BeyondBlocks.BRONZE_CHEST.get(), (Block)BeyondBlocks.BRONZE_TNT_CHEST.get(), (Block)BeyondBlocks.BRONZE_PARALYSIS_CHEST.get()
               }
            )
            .build(null)
   );
   public static final RegistryObject<BlockEntityType<SilverChestBlockEntity>> SILVER_CHEST = BLOCK_ENTITIES.register(
      "silver_chest",
      () -> Builder.of(
               SilverChestBlockEntity::new,
               new Block[]{
                  (Block)BeyondBlocks.SILVER_CHEST.get(),
                  (Block)BeyondBlocks.SILVER_TNT_CHEST.get(),
                  (Block)BeyondBlocks.SILVER_PARALYSIS_CHEST.get(),
                  (Block)BeyondBlocks.SILVER_MIMIC_CHEST.get()
               }
            )
            .build(null)
   );
   public static final RegistryObject<BlockEntityType<GoldChestBlockEntity>> GOLD_CHEST = BLOCK_ENTITIES.register(
      "gold_chest",
      () -> Builder.of(
               GoldChestBlockEntity::new,
               new Block[]{
                  (Block)BeyondBlocks.GOLD_CHEST.get(),
                  (Block)BeyondBlocks.GOLD_TNT_CHEST.get(),
                  (Block)BeyondBlocks.GOLD_PARALYSIS_CHEST.get(),
                  (Block)BeyondBlocks.GOLD_MIMIC_CHEST.get()
               }
            )
            .build(null)
   );
   public static final RegistryObject<BlockEntityType<StellarChestBlockEntity>> STELLAR_CHEST = BLOCK_ENTITIES.register(
      "stellar_chest",
      () -> Builder.of(
               StellarChestBlockEntity::new,
               new Block[]{
                  (Block)BeyondBlocks.STELLAR_CHEST.get(), (Block)BeyondBlocks.STELLAR_TNT_CHEST.get(), (Block)BeyondBlocks.STELLAR_PARALYSIS_CHEST.get()
               }
            )
            .build(null)
   );
   public static final RegistryObject<BlockEntityType<DungeonGateBlockEntity>> DUNGEON_GATE = BLOCK_ENTITIES.register(
      "dungeon_gate", () -> Builder.of(DungeonGateBlockEntity::new, new Block[]{(Block)BeyondBlocks.DUNGEON_GATE.get()}).build(null)
   );
   public static final RegistryObject<BlockEntityType<InsideGateBlockEntity>> INSIDE_GATE = BLOCK_ENTITIES.register(
      "inside_gate", () -> Builder.of(InsideGateBlockEntity::new, new Block[]{(Block)BeyondBlocks.INSIDE_GATE.get()}).build(null)
   );
   public static final RegistryObject<BlockEntityType<AnchorBlockEntity>> DUNGEON_ANCHOR = BLOCK_ENTITIES.register(
      "dungeon_anchor", () -> Builder.of(AnchorBlockEntity::new, new Block[]{(Block)BeyondBlocks.DUNGEON_ANCHOR.get()}).build(null)
   );
   public static final RegistryObject<BlockEntityType<AnimateDoorBlockEntity>> ANIMATE_DOOR = BLOCK_ENTITIES.register(
      "animate_door", () -> Builder.of(AnimateDoorBlockEntity::new, new Block[]{(Block)BeyondBlocks.DUNGEON_UNLOCKER_BLOCK.get()}).build(null)
   );
   public static final RegistryObject<BlockEntityType<SlimeCeilingBlockEntity>> SLIME_CEILING = BLOCK_ENTITIES.register(
      "slime_ceiling_entity", () -> Builder.of(SlimeCeilingBlockEntity::new, new Block[]{(Block)BeyondBlocks.SLIME_CEILING.get()}).build(null)
   );
   public static final RegistryObject<BlockEntityType<SlimeFloorBlockEntity>> SLIME_FLOOR = BLOCK_ENTITIES.register(
      "slime_floor_entity", () -> Builder.of(SlimeFloorBlockEntity::new, new Block[]{(Block)BeyondBlocks.SLIME_FLOOR.get()}).build(null)
   );
   public static final RegistryObject<BlockEntityType<IllusionaryBlockEntity>> ILLUSIONARY_BLOCK = BLOCK_ENTITIES.register(
      "illusionary_block_entity",
      () -> Builder.of(
               IllusionaryBlockEntity::new,
               new Block[]{
                  (Block)BeyondBlocks.ILLUSIONARY_BLOCK.get(), (Block)BeyondBlocks.MOVING_TILE_BLOCK.get(), (Block)BeyondBlocks.ROTATING_TILE_BLOCK.get()
               }
            )
            .build(null)
   );
   public static final RegistryObject<BlockEntityType<LanthornBlockEntity>> LANTHORN_BLOCK = BLOCK_ENTITIES.register(
      "lanthorn_block", () -> Builder.of(LanthornBlockEntity::new, new Block[]{(Block)BeyondBlocks.LANTHORN_BLOCK.get()}).build(null)
   );
   public static final RegistryObject<BlockEntityType<DumbbellRackBlockEntity>> DUMBELL_RACK = BLOCK_ENTITIES.register(
      "dumbbell_rack", () -> Builder.of(DumbbellRackBlockEntity::new, new Block[]{(Block)BeyondBlocks.DUMBELL_RACK.get()}).build(null)
   );
   public static final RegistryObject<BlockEntityType<BenchPressBlockEntity>> BENCH_PRESS = BLOCK_ENTITIES.register(
      "bench_press", () -> Builder.of(BenchPressBlockEntity::new, new Block[]{(Block)BeyondBlocks.BENCH_PRESS.get()}).build(null)
   );
   public static final RegistryObject<BlockEntityType<ShrineBlockEntity>> SHRINE = BLOCK_ENTITIES.register(
      "shrine",
      () -> Builder.of(
               ShrineBlockEntity::new,
               new Block[]{
                  (Block)BeyondBlocks.BRONZE_SHRINE.get(),
                  (Block)BeyondBlocks.SILVER_SHRINE.get(),
                  (Block)BeyondBlocks.GOLD_SHRINE.get(),
                  (Block)BeyondBlocks.STELLAR_SHRINE.get()
               }
            )
            .build(null)
   );
   public static final RegistryObject<BlockEntityType<RespawnBossBlockEntity>> BOSS_SPAWNER = BLOCK_ENTITIES.register(
      "boss_respawner", () -> Builder.of(RespawnBossBlockEntity::new, new Block[]{(Block)BeyondBlocks.BOSS_SPAWNER.get()}).build(null)
   );

   public static void register(IEventBus eventBus) {
      BLOCK_ENTITIES.register(eventBus);
   }
}
