package io.github.Memoires.trbeyond.block.client.geo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.block.entity.SlimeCeilingBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class SlimeCeilingRenderer extends GeoBlockRenderer<SlimeCeilingBlockEntity> {
   public SlimeCeilingRenderer(Context rendererDispatcherIn) {
      super(rendererDispatcherIn, new SlimeCeilingModel());
   }

   public boolean shouldRenderOffScreen(SlimeCeilingBlockEntity pBlockEntity) {
      return true;
   }

   public boolean shouldRender(SlimeCeilingBlockEntity tileEntity, Vec3 pCameraPos) {
      return true;
   }

   public RenderType getRenderType(
      SlimeCeilingBlockEntity animatable,
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
