package io.github.Memoires.trbeyond.block.entity;

import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GoldChestBlockEntity extends DungeonChestBlockEntity {
   public GoldChestBlockEntity(BlockPos blockPos, BlockState blockState) {
      super((BlockEntityType<?>)BeyondBlockEntities.GOLD_CHEST.get(), blockPos, blockState);
   }

   @Override
   protected Component getDefaultName() {
      return Component.translatable("block.trbeyond.gold_chest");
   }
}
