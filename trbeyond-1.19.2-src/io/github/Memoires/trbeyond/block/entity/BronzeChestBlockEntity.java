package io.github.Memoires.trbeyond.block.entity;

import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BronzeChestBlockEntity extends DungeonChestBlockEntity {
   public BronzeChestBlockEntity(BlockPos blockPos, BlockState blockState) {
      super((BlockEntityType<?>)BeyondBlockEntities.BRONZE_CHEST.get(), blockPos, blockState);
   }

   @Override
   protected Component getDefaultName() {
      return Component.translatable("block.trbeyond.bronze_chest");
   }
}
