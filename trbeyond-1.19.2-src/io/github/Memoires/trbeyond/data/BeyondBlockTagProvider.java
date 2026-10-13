package io.github.Memoires.trbeyond.data;

import com.github.manasmods.manascore.api.data.gen.BlockTagProvider;
import com.github.manasmods.tensura.data.TensuraTags.Blocks;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.data.event.GatherDataEvent;

public class BeyondBlockTagProvider extends BlockTagProvider {
   public BeyondBlockTagProvider(GatherDataEvent gatherDataEvent) {
      super(gatherDataEvent, "tensura");
   }

   protected void generate() {
      this.tag(Blocks.TRAP_BLOCKS)
         .add(
            new Block[]{
               (Block)BeyondBlocks.ANDESITE_TRAPDOOR.get(),
               (Block)BeyondBlocks.BRICK_TRAPDOOR.get(),
               (Block)BeyondBlocks.SLIME_CEILING.get(),
               (Block)BeyondBlocks.ROTATING_TILE_BLOCK.get(),
               (Block)BeyondBlocks.MOVING_TILE_BLOCK.get(),
               (Block)BeyondBlocks.ILLUSIONARY_BLOCK.get(),
               (Block)BeyondBlocks.SLIME_FLOOR.get(),
               (Block)BeyondBlocks.SEALING_AIR.get(),
               (Block)BeyondBlocks.STEEL_THREAD.get(),
               (Block)BeyondBlocks.STEEL_THREAD_DIAGONAL.get(),
               (Block)BeyondBlocks.BOSS_SPIDER_EGG.get(),
               (Block)BeyondBlocks.BRONZE_TNT_CHEST.get(),
               (Block)BeyondBlocks.SILVER_TNT_CHEST.get(),
               (Block)BeyondBlocks.GOLD_TNT_CHEST.get(),
               (Block)BeyondBlocks.BRONZE_PARALYSIS_CHEST.get(),
               (Block)BeyondBlocks.SILVER_PARALYSIS_CHEST.get(),
               (Block)BeyondBlocks.GOLD_PARALYSIS_CHEST.get()
            }
         );
      this.tag(Blocks.TREASURE_BLOCKS)
         .add(new Block[]{(Block)BeyondBlocks.BRONZE_CHEST.get(), (Block)BeyondBlocks.SILVER_CHEST.get(), (Block)BeyondBlocks.GOLD_CHEST.get()});
      this.tag(Blocks.WEB_BLOCKS).add((Block)BeyondBlocks.BOSS_SPIDER_EGG.get());
      this.tag(BeyondTags.DUNGEON_PLACEABLE).add((Block)BeyondBlocks.BOSS_SPIDER_EGG.get());
      this.tag(Blocks.SKILL_UNBREAKABLE)
         .add(
            new Block[]{
               (Block)BeyondBlocks.DUNGEON_GATE_TELEPORTER.get(),
               (Block)BeyondBlocks.ILLUSIONARY_BLOCK.get(),
               (Block)BeyondBlocks.BENCH_PRESS.get(),
               (Block)BeyondBlocks.BRONZE_CHEST.get(),
               (Block)BeyondBlocks.BRONZE_PARALYSIS_CHEST.get(),
               (Block)BeyondBlocks.BRONZE_TNT_CHEST.get(),
               (Block)BeyondBlocks.BRONZE_SHRINE.get(),
               (Block)BeyondBlocks.DUMBELL_RACK.get(),
               (Block)BeyondBlocks.DUNGEON_ANCHOR.get(),
               (Block)BeyondBlocks.DUNGEON_ENTRY.get(),
               (Block)BeyondBlocks.DUNGEON_GATE.get(),
               (Block)BeyondBlocks.GOLD_CHEST.get(),
               (Block)BeyondBlocks.GOLD_TNT_CHEST.get(),
               (Block)BeyondBlocks.GOLD_PARALYSIS_CHEST.get(),
               (Block)BeyondBlocks.GOLD_MIMIC_CHEST.get(),
               (Block)BeyondBlocks.SILVER_CHEST.get(),
               (Block)BeyondBlocks.SILVER_PARALYSIS_CHEST.get(),
               (Block)BeyondBlocks.SILVER_TNT_CHEST.get(),
               (Block)BeyondBlocks.SEALING_AIR.get(),
               (Block)BeyondBlocks.SILVER_SHRINE.get(),
               (Block)BeyondBlocks.GOLD_SHRINE.get(),
               (Block)BeyondBlocks.SLIME_CEILING.get(),
               (Block)BeyondBlocks.SLIME_FLOOR.get(),
               (Block)BeyondBlocks.DUNGEON_UNLOCKER_BLOCK.get(),
               (Block)BeyondBlocks.INSIDE_GATE.get(),
               (Block)BeyondBlocks.LANTHORN_BLOCK.get(),
               (Block)BeyondBlocks.MOVING_TILE_BLOCK.get(),
               (Block)BeyondBlocks.ROOM_ENTRY.get(),
               (Block)BeyondBlocks.ROTATING_TILE_BLOCK.get(),
               (Block)BeyondBlocks.STELLAR_TNT_CHEST.get(),
               (Block)BeyondBlocks.STELLAR_PARALYSIS_CHEST.get(),
               (Block)BeyondBlocks.STELLAR_SHRINE.get(),
               (Block)BeyondBlocks.STELLAR_CHEST.get(),
               (Block)BeyondBlocks.STEEL_THREAD.get()
            }
         );
      this.tag(Blocks.SKILL_UNOBTAINABLE)
         .add(
            new Block[]{
               (Block)BeyondBlocks.DUNGEON_GATE_TELEPORTER.get(),
               (Block)BeyondBlocks.ILLUSIONARY_BLOCK.get(),
               (Block)BeyondBlocks.BENCH_PRESS.get(),
               (Block)BeyondBlocks.BRONZE_CHEST.get(),
               (Block)BeyondBlocks.BRONZE_PARALYSIS_CHEST.get(),
               (Block)BeyondBlocks.BRONZE_TNT_CHEST.get(),
               (Block)BeyondBlocks.BRONZE_SHRINE.get(),
               (Block)BeyondBlocks.DUMBELL_RACK.get(),
               (Block)BeyondBlocks.DUNGEON_ANCHOR.get(),
               (Block)BeyondBlocks.DUNGEON_ENTRY.get(),
               (Block)BeyondBlocks.DUNGEON_GATE.get(),
               (Block)BeyondBlocks.GOLD_CHEST.get(),
               (Block)BeyondBlocks.GOLD_TNT_CHEST.get(),
               (Block)BeyondBlocks.GOLD_PARALYSIS_CHEST.get(),
               (Block)BeyondBlocks.GOLD_MIMIC_CHEST.get(),
               (Block)BeyondBlocks.SILVER_CHEST.get(),
               (Block)BeyondBlocks.SILVER_PARALYSIS_CHEST.get(),
               (Block)BeyondBlocks.SILVER_TNT_CHEST.get(),
               (Block)BeyondBlocks.SEALING_AIR.get(),
               (Block)BeyondBlocks.SILVER_SHRINE.get(),
               (Block)BeyondBlocks.GOLD_SHRINE.get(),
               (Block)BeyondBlocks.SLIME_CEILING.get(),
               (Block)BeyondBlocks.SLIME_FLOOR.get(),
               (Block)BeyondBlocks.DUNGEON_UNLOCKER_BLOCK.get(),
               (Block)BeyondBlocks.INSIDE_GATE.get(),
               (Block)BeyondBlocks.LANTHORN_BLOCK.get(),
               (Block)BeyondBlocks.MOVING_TILE_BLOCK.get(),
               (Block)BeyondBlocks.ROOM_ENTRY.get(),
               (Block)BeyondBlocks.ROTATING_TILE_BLOCK.get(),
               (Block)BeyondBlocks.STELLAR_TNT_CHEST.get(),
               (Block)BeyondBlocks.STELLAR_PARALYSIS_CHEST.get(),
               (Block)BeyondBlocks.STELLAR_SHRINE.get(),
               (Block)BeyondBlocks.STELLAR_CHEST.get(),
               (Block)BeyondBlocks.STEEL_THREAD.get()
            }
         );
   }
}
