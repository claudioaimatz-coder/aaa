package io.github.Memoires.trbeyond.block;

import io.github.Memoires.trbeyond.block.entity.AnchorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class DungeonAnchor extends BaseEntityBlock implements EntityBlock {
   public static final BooleanProperty GENERATED = BooleanProperty.create("generated");

   public DungeonAnchor() {
      super(Properties.of(Material.BARRIER).noLootTable());
      this.registerDefaultState((BlockState)this.defaultBlockState().setValue(GENERATED, false));
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{GENERATED});
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new AnchorBlockEntity(pos, state);
   }
}
