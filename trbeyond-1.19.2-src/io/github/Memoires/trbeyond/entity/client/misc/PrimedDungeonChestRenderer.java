package io.github.Memoires.trbeyond.entity.client.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.Memoires.trbeyond.entity.misc.PrimedDungeonChestEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class PrimedDungeonChestRenderer extends EntityRenderer<PrimedDungeonChestEntity> {
   private final ItemRenderer itemRenderer;

   public PrimedDungeonChestRenderer(Context pContext) {
      super(pContext);
      this.shadowRadius = 0.5F;
      this.itemRenderer = pContext.getItemRenderer();
   }

   public void render(
      PrimedDungeonChestEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight
   ) {
      pMatrixStack.pushPose();
      pMatrixStack.translate(0.0, 0.5, 0.0);
      pMatrixStack.scale(2.25F, 2.25F, 2.25F);
      int i = pEntity.getFuse();
      if ((float)i - pPartialTicks + 1.0F < 10.0F) {
         float f = 1.0F - ((float)i - pPartialTicks + 1.0F) / 10.0F;
         f = Mth.clamp(f, 0.0F, 1.0F);
         f *= f;
         f *= f;
         float f1 = 1.0F + f * 0.3F;
         pMatrixStack.scale(f1, f1, f1);
      }

      Direction direction = pEntity.getSpawnDirection();
      if (direction != Direction.SOUTH && direction != Direction.EAST) {
         pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(direction.toYRot()));
      } else {
         pMatrixStack.mulPose(Vector3f.YN.rotationDegrees(direction.toYRot()));
      }

      if (direction != Direction.WEST) {
         pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
      }

      renderWhiteSolidBlock(pEntity, this.itemRenderer, pMatrixStack, pBuffer, pPackedLight, i / 5 % 2 == 0);
      pMatrixStack.popPose();
      super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
   }

   @NotNull
   public ResourceLocation getTextureLocation(PrimedDungeonChestEntity pEntity) {
      return TextureAtlas.LOCATION_BLOCKS;
   }

   public static void renderWhiteSolidBlock(
      PrimedDungeonChestEntity pEntity, ItemRenderer renderer, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, boolean pWhiteOverlay
   ) {
      int i;
      if (pWhiteOverlay) {
         i = OverlayTexture.pack(OverlayTexture.u(1.0F), 10);
      } else {
         i = OverlayTexture.NO_OVERLAY;
      }

      renderer.renderStatic(pEntity.getSourceItem(), TransformType.FIXED, pPackedLight, i, pPoseStack, pBuffer, 1);
   }
}
