package io.github.Memoires.trbeyond.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.Memoires.trbeyond.entity.projectile.GlendaBulletProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class GlendaBulletRenderer extends EntityRenderer<GlendaBulletProjectile> {
   private ResourceLocation Texture = new ResourceLocation("trbeyond", "textures/entity/bullet.png");
   private final GlendaBulletModel model;

   public GlendaBulletRenderer(Context pContext) {
      super(pContext);
      this.model = new GlendaBulletModel(pContext.bakeLayer(GlendaBulletModel.LAYER_LOCATION));
   }

   @NotNull
   public ResourceLocation getTextureLocation(@NotNull GlendaBulletProjectile pEntity) {
      return this.Texture;
   }

   public void render(
      GlendaBulletProjectile entity, float pEntityYaw, float pPartialTicks, PoseStack poseStack, @NotNull MultiBufferSource buffer, int pPackedLight
   ) {
      poseStack.pushPose();
      double dx = entity.getDeltaMovement().x();
      double dy = entity.getDeltaMovement().y();
      double dz = entity.getDeltaMovement().z();
      if (dx == 0.0 && dy == 0.0 && dz == 0.0) {
         dx = 1.0E-4;
      }

      float yaw = (float)Math.toDegrees(Math.atan2(dz, dx));
      float pitch = (float)Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)));
      poseStack.mulPose(Vector3f.YP.rotationDegrees(-yaw - 90.0F));
      poseStack.mulPose(Vector3f.XP.rotationDegrees(pitch));
      poseStack.translate(0.0, -1.25, 0.0);
      VertexConsumer vertexConsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
      this.model.renderToBuffer(poseStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      poseStack.popPose();
      super.render(entity, pEntityYaw, pPartialTicks, poseStack, buffer, pPackedLight);
   }

   public static int getOverlayCoords(GlendaBulletProjectile pLivingEntity, float pU) {
      return OverlayTexture.pack(OverlayTexture.u(pU), OverlayTexture.v(false));
   }
}
