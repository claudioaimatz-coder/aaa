package io.github.Memoires.trbeyond.block.entity;

import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class BenchPressBlockEntity extends BlockEntity implements IAnimatable {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

   public BenchPressBlockEntity(BlockPos pos, BlockState state) {
      super((BlockEntityType)BeyondBlockEntities.BENCH_PRESS.get(), pos, state);
   }

   public void registerControllers(AnimationData data) {
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }
}
