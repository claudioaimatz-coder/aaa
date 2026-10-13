package io.github.Memoires.trbeyond.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.Memoires.trbeyond.network.BeyondNetwork;
import io.github.Memoires.trbeyond.network.KurobeGUIPacket;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class KurobeMainMenuScreen extends AbstractContainerScreen<KurobeMainMenu> {
   private static final HashMap<String, Object> guistate = new HashMap<>();
   private final Level world;
   private final int x;
   private final int y;
   private final int z;
   private final Player entity;
   Button button_engrave_enchanting;
   Button button_open_shop_gui;
   private static final ResourceLocation texture = new ResourceLocation("trbeyond:textures/screen/researcher/mode.png");

   public KurobeMainMenuScreen(KurobeMainMenu container, Inventory inventory, Component text) {
      super(container, inventory, text);
      this.world = container.world;
      this.x = container.x;
      this.y = container.y;
      this.z = container.z;
      this.entity = container.entity;
      this.imageWidth = 162;
      this.imageHeight = 116;
   }

   public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(ms);
      super.render(ms, mouseX, mouseY, partialTicks);
      this.renderTooltip(ms, mouseX, mouseY);
   }

   protected void renderBg(PoseStack ms, float partialTicks, int gx, int gy) {
      RenderSystem.m_157429_(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.m_69478_();
      RenderSystem.m_69453_();
      RenderSystem.m_157456_(0, texture);
      blit(ms, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
      RenderSystem.m_69461_();
   }

   public boolean keyPressed(int key, int b, int c) {
      if (key == 256) {
         this.minecraft.player.closeContainer();
         return true;
      } else {
         return super.keyPressed(key, b, c);
      }
   }

   public void containerTick() {
      super.containerTick();
   }

   protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
   }

   public void onClose() {
      super.onClose();
      Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
   }

   public void init() {
      super.init();
      this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
      this.button_engrave_enchanting = new Button(
         this.leftPos + 20,
         this.topPos + 63,
         119,
         20,
         Component.translatable("gui.trbeyond.kurobe_main_menu.button_engrave_enchanting"),
         e -> BeyondNetwork.INSTANCE.sendToServer(new KurobeGUIPacket(0))
      );
      guistate.put("button:button_engrave_enchanting", this.button_engrave_enchanting);
      this.addRenderableWidget(this.button_engrave_enchanting);
      this.button_open_shop_gui = new Button(
         this.leftPos + 34,
         this.topPos + 26,
         92,
         20,
         Component.translatable("gui.trbeyond.kurobe_main_menu.button_open_shop_gui"),
         e -> BeyondNetwork.INSTANCE.sendToServer(new KurobeGUIPacket(1))
      );
      guistate.put("button:button_open_shop_gui", this.button_open_shop_gui);
      this.addRenderableWidget(this.button_open_shop_gui);
   }
}
