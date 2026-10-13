package io.github.Memoires.trbeyond.block.client;

import io.github.Memoires.trbeyond.block.entity.DungeonGateBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class DungeonGateRenderer extends GeoBlockRenderer<DungeonGateBlockEntity> {
   public DungeonGateRenderer(Context context) {
      super(context, new DungeonGateBlockEntityModel());
   }
}
