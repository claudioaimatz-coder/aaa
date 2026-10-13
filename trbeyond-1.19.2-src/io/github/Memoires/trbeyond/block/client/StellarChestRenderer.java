package io.github.Memoires.trbeyond.block.client;

import io.github.Memoires.trbeyond.block.entity.StellarChestBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class StellarChestRenderer extends GeoBlockRenderer<StellarChestBlockEntity> {
   public StellarChestRenderer(Context context) {
      super(context, new StellarChestBlockEntityModel());
   }
}
