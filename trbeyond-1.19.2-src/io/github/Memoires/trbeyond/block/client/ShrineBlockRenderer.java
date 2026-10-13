package io.github.Memoires.trbeyond.block.client;

import io.github.Memoires.trbeyond.block.entity.ShrineBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class ShrineBlockRenderer extends GeoBlockRenderer<ShrineBlockEntity> {
   public ShrineBlockRenderer(Context context) {
      super(context, new ShrineBlockEntityModel());
   }
}
