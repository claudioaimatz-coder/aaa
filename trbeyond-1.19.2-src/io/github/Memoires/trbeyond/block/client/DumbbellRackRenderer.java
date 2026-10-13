package io.github.Memoires.trbeyond.block.client;

import io.github.Memoires.trbeyond.block.entity.DumbbellRackBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class DumbbellRackRenderer extends GeoBlockRenderer<DumbbellRackBlockEntity> {
   public DumbbellRackRenderer(Context context) {
      super(context, new DumbbellRackBlockEntityModel());
   }
}
