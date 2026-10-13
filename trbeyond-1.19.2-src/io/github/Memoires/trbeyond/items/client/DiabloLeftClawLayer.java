package io.github.Memoires.trbeyond.items.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.Memoires.trbeyond.items.weapons.DiabloClawItem;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;

public class DiabloLeftClawLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
   DiabloLeftClawModel model = new DiabloLeftClawModel(DiabloLeftClawModel.createBodyLayer().bakeRoot());
   public static final float y = 1.379F;
   public static final float x = -1.089F;
   public static float SCALE = 0.58F;
   public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("trbeyond", "textures/item/diablo_claw_left.png");

   public DiabloLeftClawLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> pRenderer) {
      super(pRenderer);
   }

   public void render(
      PoseStack pPoseStack,
      MultiBufferSource pBuffer,
      int pPackedLight,
      AbstractClientPlayer player,
      float pLimbSwing,
      float pLimbSwingAmount,
      float pPartialTick,
      float pAgeInTicks,
      float pNetHeadYaw,
      float pHeadPitch
   ) {
      if (player.getMainHandItem().getItem() instanceof DiabloClawItem) {
         this.renderArmWithItem(HumanoidArm.LEFT, pPoseStack, pBuffer, pPackedLight, player);
      } else if (player.getOffhandItem().getItem() instanceof DiabloClawItem) {
         this.renderArmWithItem(HumanoidArm.RIGHT, pPoseStack, pBuffer, pPackedLight, player);
      }
   }

   protected void renderArmWithItem(HumanoidArm pArm, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, AbstractClientPlayer player) {
      pPoseStack.pushPose();
      ((PlayerModel)this.getParentModel()).translateToHand(pArm, pPoseStack);
      pPoseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
      pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
      boolean flag = pArm == HumanoidArm.LEFT;
      pPoseStack.translate((double)((float)(flag ? -1 : 1) / 16.0F), -0.964F, 0.75399995F);
      pPoseStack.scale(SCALE, SCALE, SCALE);
      this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_LOCATION)), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      pPoseStack.popPose();
   }
}
