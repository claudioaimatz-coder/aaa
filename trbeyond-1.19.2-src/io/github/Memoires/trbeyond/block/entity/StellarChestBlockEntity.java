package io.github.Memoires.trbeyond.block.entity;

import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class StellarChestBlockEntity extends DungeonChestBlockEntity implements IAnimatable {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

   public StellarChestBlockEntity(BlockPos blockPos, BlockState blockState) {
      super((BlockEntityType<?>)BeyondBlockEntities.STELLAR_CHEST.get(), blockPos, blockState);
   }

   @Override
   protected Component getDefaultName() {
      return Component.translatable("block.trbeyond.stellar_chest");
   }

   public void registerControllers(AnimationData data) {
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }
}
