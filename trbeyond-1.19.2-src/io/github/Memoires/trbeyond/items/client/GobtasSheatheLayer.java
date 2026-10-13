package io.github.Memoires.trbeyond.items.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.Memoires.trbeyond.items.weapons.GobtaKodachiItem;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class GobtasSheatheLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
   private final gobtasSheatheModel model = new gobtasSheatheModel(gobtasSheatheModel.createBodyLayer().bakeRoot());
   public static float SCALE = 0.58F;
   public static final ResourceLocation TEXTURE = new ResourceLocation("trbeyond", "textures/item/gobtas_sheathe.png");

   public GobtasSheatheLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
      super(parent);
   }

   public void render(
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      AbstractClientPlayer player,
      float limbSwing,
      float limbSwingAmount,
      float partialTicks,
      float ageInTicks,
      float netHeadYaw,
      float headPitch
   ) {
      if (!player.isSpectator() && !player.isInvisible()) {
         ItemStack main = player.getMainHandItem();
         ItemStack off = player.getOffhandItem();
         boolean mainHas = main.getItem() instanceof GobtaKodachiItem;
         boolean offHas = off.getItem() instanceof GobtaKodachiItem;
         if (mainHas || offHas) {
            HumanoidArm holdingArm;
            if (mainHas) {
               holdingArm = player.getMainArm();
            } else {
               holdingArm = player.getMainArm() == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
            }

            HumanoidArm renderArm = holdingArm == HumanoidArm.LEFT ? HumanoidArm.RIGHT : HumanoidArm.LEFT;
            this.renderSheath(renderArm, poseStack, buffer, packedLight, player);
         }
      }
   }

   private void renderSheath(HumanoidArm arm, PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player) {
      poseStack.pushPose();
      ((PlayerModel)this.getParentModel()).translateToHand(arm, poseStack);
      poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
      poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
      boolean left = arm == HumanoidArm.LEFT;
      float OFFSET_X = (float)(left ? -1 : 1) / 16.0F;
      double OFFSET_Y = -0.964;
      double OFFSET_Z = 0.754;
      poseStack.translate((double)OFFSET_X, -0.964, 0.754);
      poseStack.scale(SCALE, SCALE, SCALE);
      this.model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      poseStack.popPose();
   }
}
