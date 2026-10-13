package io.github.Memoires.trbeyond.entity.client.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.Memoires.trbeyond.entity.projectile.ChaosEaterProjectile;
import io.github.Memoires.trbeyond.registry.effects.BeyondAnimatedEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ChaosEaterRenderer extends EntityRenderer<ChaosEaterProjectile> {
   private static final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
   private static final ItemStack chaos_eater = new ItemStack((ItemLike)BeyondAnimatedEffects.CHAOS_EATER.get());

   public ChaosEaterRenderer(Context pContext) {
      super(pContext);
   }

   public void render(ChaosEaterProjectile pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
      pMatrixStack.pushPose();
      pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
      pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot())));
      itemRenderer.renderStatic(chaos_eater, TransformType.GROUND, pPackedLight, pPackedLight, pMatrixStack, pBuffer, pPackedLight);
      pMatrixStack.popPose();
      super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
   }

   public ResourceLocation getTextureLocation(ChaosEaterProjectile pEntity) {
      return null;
   }
}
