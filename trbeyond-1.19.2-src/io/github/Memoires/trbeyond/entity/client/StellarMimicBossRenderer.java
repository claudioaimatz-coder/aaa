package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.StellarMimicBossEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class StellarMimicBossRenderer extends GeoEntityRenderer<StellarMimicBossEntity> {
   public StellarMimicBossRenderer(Context renderManager) {
      super(renderManager, new StellarMimicBossModel());
      this.shadowRadius = 0.7F;
   }
}
