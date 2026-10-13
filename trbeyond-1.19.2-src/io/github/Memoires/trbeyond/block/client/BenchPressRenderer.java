package io.github.Memoires.trbeyond.block.client;

import io.github.Memoires.trbeyond.block.entity.BenchPressBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class BenchPressRenderer extends GeoBlockRenderer<BenchPressBlockEntity> {
   public BenchPressRenderer(Context context) {
      super(context, new BenchPressBlockEntityModel());
   }
}
