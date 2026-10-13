package io.github.Memoires.trbeyond.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.entity.custom.KillerDryadEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class KillerDryadRenderer extends GeoEntityRenderer<KillerDryadEntity> {
   public KillerDryadRenderer(Context renderManager) {
      super(renderManager, new KillerDryadModel());
      this.shadowRadius = 0.3F;
   }

   @NotNull
   public ResourceLocation getTextureLocation(@NotNull KillerDryadEntity animatable) {
      return new ResourceLocation("trbeyond", "textures/entity/killerdryad.png");
   }

   public RenderType getRenderType(
      KillerDryadEntity animatable,
      float partialTick,
      PoseStack poseStack,
      @Nullable MultiBufferSource bufferSource,
      @Nullable VertexConsumer buffer,
      int packedLight,
      ResourceLocation texture
   ) {
      return super.getRenderType(animatable, partialTick, poseStack, bufferSource, buffer, packedLight, texture);
   }
}
