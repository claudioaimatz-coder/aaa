package io.github.Memoires.trbeyond.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class SubspaceContainerScreen extends AbstractContainerScreen<SubspaceContainerMenu> {
   private static final ResourceLocation POUCH = new ResourceLocation("trbeyond", "textures/screen/spatial_storage/spatial_pouch.png");
   private static final ResourceLocation BAG = new ResourceLocation("trbeyond", "textures/screen/spatial_storage/spatial_bag.png");
   private static final ResourceLocation BACKPACK = new ResourceLocation("trbeyond", "textures/screen/spatial_storage/spatial_backpack.png");
   private final ResourceLocation BackGround;
   private final Player player;

   public SubspaceContainerScreen(SubspaceContainerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
      super(pMenu, pPlayerInventory, pTitle);
      this.player = pPlayerInventory.player;
      switch (pMenu.getContainerSize()) {
         case 9:
            this.imageWidth = 200;
            this.imageHeight = 241;
            this.BackGround = POUCH;
            break;
         case 18:
            this.imageWidth = 200;
            this.imageHeight = 249;
            this.BackGround = BAG;
            break;
         case 27:
            this.imageWidth = 200;
            this.imageHeight = 253;
            this.BackGround = BACKPACK;
            break;
         default:
            this.BackGround = null;
      }
   }

   protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
      RenderSystem.m_157427_(GameRenderer::getPositionTexShader);
      RenderSystem.m_157429_(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.m_157456_(0, this.BackGround);
      blit(pPoseStack, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
   }

   protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
   }

   public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
      this.renderBackground(poseStack);
      super.render(poseStack, mouseX, mouseY, partialTick);
      this.renderTooltip(poseStack, mouseX, mouseY);
   }
}
