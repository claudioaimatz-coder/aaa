package io.github.Memoires.trbeyond.block.entity;

import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class IllusionaryBlockEntity extends BlockEntity {
   public IllusionaryBlockEntity(BlockPos pos, BlockState state) {
      super((BlockEntityType)BeyondBlockEntities.ILLUSIONARY_BLOCK.get(), pos, state);
   }

   protected void saveAdditional(CompoundTag pTag) {
      super.saveAdditional(pTag);
   }

   public void load(CompoundTag pTag) {
      super.load(pTag);
   }

   public ClientboundBlockEntityDataPacket getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   public CompoundTag getUpdateTag() {
      CompoundTag tag = super.getUpdateTag();
      this.saveAdditional(tag);
      return tag;
   }
}
