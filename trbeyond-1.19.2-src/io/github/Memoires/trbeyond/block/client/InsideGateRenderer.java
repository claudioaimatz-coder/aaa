package io.github.Memoires.trbeyond.block.client;

import io.github.Memoires.trbeyond.block.entity.InsideGateBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class InsideGateRenderer extends GeoBlockRenderer<InsideGateBlockEntity> {
   public InsideGateRenderer(Context context) {
      super(context, new InsideGateBlockEntityModel());
   }
}
