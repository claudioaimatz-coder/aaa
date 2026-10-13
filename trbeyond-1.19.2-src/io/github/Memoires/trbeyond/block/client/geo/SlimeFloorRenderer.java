package io.github.Memoires.trbeyond.block.client.geo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.block.entity.SlimeFloorBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class SlimeFloorRenderer extends GeoBlockRenderer<SlimeFloorBlockEntity> {
   public SlimeFloorRenderer(Context rendererDispatcherIn) {
      super(rendererDispatcherIn, new SlimeFloorModel());
   }

   public RenderType getRenderType(
      SlimeFloorBlockEntity animatable,
      float partialTick,
      PoseStack poseStack,
      MultiBufferSource bufferSource,
      VertexConsumer buffer,
      int packedLight,
      ResourceLocation texture
   ) {
      return RenderType.entityTranslucent(this.getTextureLocation(animatable));
   }
}
