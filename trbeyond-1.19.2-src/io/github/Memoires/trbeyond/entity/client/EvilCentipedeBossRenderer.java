package io.github.Memoires.trbeyond.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.entity.custom.EvilCentipedeBossEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class EvilCentipedeBossRenderer extends GeoEntityRenderer<EvilCentipedeBossEntity> {
   public EvilCentipedeBossRenderer(Context renderManager) {
      super(renderManager, new EvilCentipedeBossModel());
      this.shadowRadius = 0.8F;
   }

   @NotNull
   public ResourceLocation getTextureLocation(@NotNull EvilCentipedeBossEntity boss) {
      return new ResourceLocation("trbeyond", "textures/entity/evil_centipede_boss.png");
   }

   public RenderType getRenderType(
      EvilCentipedeBossEntity animatable,
      float partialTick,
      PoseStack stack,
      @Nullable MultiBufferSource renderTypeBuffer,
      @Nullable VertexConsumer vertexBuilder,
      int packedLightIn,
      ResourceLocation texture
   ) {
      return super.getRenderType(animatable, partialTick, stack, renderTypeBuffer, vertexBuilder, packedLightIn, texture);
   }
}
