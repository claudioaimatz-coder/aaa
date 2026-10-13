package io.github.Memoires.trbeyond.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.entity.misc.MimicChestEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MimicChestRenderer extends GeoEntityRenderer<MimicChestEntity> {
   public MimicChestRenderer(Context renderManager) {
      super(renderManager, new MimicChestModel());
      this.shadowRadius = 0.3F;
   }

   public ResourceLocation getTextureLocation(MimicChestEntity animatable) {
      return animatable.isGolden()
         ? new ResourceLocation("trbeyond", "textures/entity/gold_mimic.png")
         : new ResourceLocation("trbeyond", "textures/entity/silver_mimic.png");
   }

   public RenderType getRenderType(
      MimicChestEntity animatable,
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
