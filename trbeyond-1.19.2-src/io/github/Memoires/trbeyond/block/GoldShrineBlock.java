package io.github.Memoires.trbeyond.block;

import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;

public class GoldShrineBlock extends ShrineBlock {
   public GoldShrineBlock() {
      super(
         Properties.of(Material.METAL).noLootTable().strength(100.0F).sound(SoundType.METAL).noOcclusion(),
         BeyondBlockEntities.SHRINE::get,
         ShrineBlock.DungeonShrineTier.GOLD
      );
   }
}
