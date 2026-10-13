package io.github.Memoires.trbeyond.entity.client.misc;

import io.github.Memoires.trbeyond.entity.custom.TrainingDummyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class TrainingDummyRenderer extends GeoEntityRenderer<TrainingDummyEntity> {
   public TrainingDummyRenderer(Context renderManager) {
      super(renderManager, new TrainingDummyModel());
      this.shadowRadius = 0.3F;
   }
}
