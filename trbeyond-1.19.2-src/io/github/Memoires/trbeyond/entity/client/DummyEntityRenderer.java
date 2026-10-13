package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.client.misc.TrainingDummyModel;
import io.github.Memoires.trbeyond.entity.custom.TrainingDummyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class DummyEntityRenderer extends GeoEntityRenderer<TrainingDummyEntity> {
   public DummyEntityRenderer(Context context) {
      super(context, new TrainingDummyModel());
   }
}
