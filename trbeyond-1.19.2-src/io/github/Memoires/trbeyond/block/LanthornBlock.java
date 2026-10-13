package io.github.Memoires.trbeyond.block;

import io.github.Memoires.trbeyond.block.entity.LanthornBlockEntity;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class LanthornBlock extends LanternBlock implements EntityBlock {
   public LanthornBlock() {
      super(Properties.of(Material.METAL).strength(-1.0F, 3600000.0F).sound(SoundType.LANTERN).noOcclusion());
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.MODEL;
   }

   public boolean canEntityDestroy(BlockState state, BlockGetter world, BlockPos pos, Entity entity) {
      return false;
   }

   public List<ItemStack> getDrops(BlockState state, Builder builder) {
      return Collections.emptyList();
   }

   public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
      return ItemStack.EMPTY;
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new LanthornBlockEntity(pos, state);
   }

   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
      return level.isClientSide ? null : createTickerHelper(type, (BlockEntityType)BeyondBlockEntities.LANTHORN_BLOCK.get(), LanthornBlockEntity::tick);
   }

   @Nullable
   protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
      BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker
   ) {
      return expectedType == givenType ? ticker : null;
   }
}
