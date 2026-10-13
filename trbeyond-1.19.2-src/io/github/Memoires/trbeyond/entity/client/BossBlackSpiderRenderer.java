package io.github.Memoires.trbeyond.entity.client;

import com.github.manasmods.tensura.config.client.TensuraClientConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.entity.custom.BossBlackSpiderEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BossBlackSpiderRenderer extends GeoEntityRenderer<BossBlackSpiderEntity> {
   public BossBlackSpiderRenderer(Context renderManager) {
      super(renderManager, new BossBlackSpiderModel());
      this.shadowRadius = 0.7F;
   }

   public ResourceLocation getTextureLocation(BossBlackSpiderEntity entity) {
      return getSpiderTexture(entity);
   }

   public static ResourceLocation getSpiderTexture(BossBlackSpiderEntity instance) {
      if ((Boolean)TensuraClientConfig.INSTANCE.displayConfig.arachnophobia.get()) {
         return new ResourceLocation("trbeyond", "textures/entity/boss_black_spider_safe.png");
      } else {
         return instance.isStriped()
            ? new ResourceLocation("trbeyond", "textures/entity/boss_black_spider_striped.png")
            : new ResourceLocation("trbeyond", "textures/entity/boss_black_spider.png");
      }
   }

   public RenderType getRenderType(
      BossBlackSpiderEntity animatable,
      float partialTicks,
      PoseStack stack,
      @Nullable MultiBufferSource renderTypeBuffer,
      @Nullable VertexConsumer vertexBuilder,
      int packedLightIn,
      ResourceLocation textureLocation
   ) {
      float size = animatable.isBaby() ? 0.375F : 0.75F;
      stack.scale(size, size, size);
      return RenderType.entityTranslucent(this.getTextureLocation(animatable));
   }
}
