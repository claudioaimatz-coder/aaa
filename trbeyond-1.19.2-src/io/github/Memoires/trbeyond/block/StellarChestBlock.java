package io.github.Memoires.trbeyond.block;

import io.github.Memoires.trbeyond.block.entity.StellarChestBlockEntity;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;

public class StellarChestBlock extends DungeonChestBlock {
   public StellarChestBlock(DungeonChestBlock.DungeonChestType type) {
      super(Properties.of(Material.HEAVY_METAL).noLootTable().strength(100.0F).sound(SoundType.METAL), BeyondBlockEntities.STELLAR_CHEST::get, type);
   }

   public StellarChestBlock() {
      this(DungeonChestBlock.DungeonChestType.DEFAULT);
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
      return new StellarChestBlockEntity(blockPos, blockState);
   }
}
