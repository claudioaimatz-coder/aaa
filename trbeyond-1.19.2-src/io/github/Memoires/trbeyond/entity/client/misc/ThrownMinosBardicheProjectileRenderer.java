package io.github.Memoires.trbeyond.entity.client.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.Memoires.trbeyond.entity.projectile.ThrownMinosBardicheProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;

public class ThrownMinosBardicheProjectileRenderer extends EntityRenderer<ThrownMinosBardicheProjectile> {
   private final ItemRenderer itemRenderer;

   public ThrownMinosBardicheProjectileRenderer(Context renderManager) {
      super(renderManager);
      this.itemRenderer = renderManager.getItemRenderer();
   }

   public ResourceLocation getTextureLocation(ThrownMinosBardicheProjectile instance) {
      return InventoryMenu.BLOCK_ATLAS;
   }

   public void render(ThrownMinosBardicheProjectile entity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
      matrixStack.pushPose();
      matrixStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(g, entity.yRotO, entity.getYRot()) - 90.0F));
      matrixStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(g, entity.xRotO, entity.getXRot()) - 45.0F));
      matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
      matrixStack.translate(0.0, -0.3, 0.0);
      matrixStack.scale(2.0F, 2.0F, 2.0F);
      if (entity.shouldSpin()) {
         float tick = (float)entity.tickCount + g;
         matrixStack.mulPose(Vector3f.ZP.rotationDegrees(tick * 30.0F));
      }

      this.itemRenderer
         .renderStatic(entity.getSourceItem(), TransformType.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrixStack, vertexConsumerProvider, entity.getId());
      matrixStack.popPose();
   }
}
