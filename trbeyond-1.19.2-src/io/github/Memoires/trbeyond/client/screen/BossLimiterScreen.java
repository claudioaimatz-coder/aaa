package io.github.Memoires.trbeyond.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.Memoires.trbeyond.entity.custom.ILimiter;
import io.github.Memoires.trbeyond.network.BeyondNetwork;
import io.github.Memoires.trbeyond.network.RequestLimiterPacket;
import java.util.UUID;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class BossLimiterScreen extends Screen {
   Button button_one;
   Button button_two;
   Button button_three;
   Button button_four;
   int x = this.width / 2;
   int y = this.height / 2;
   private static final Component TITLE = Component.literal("Limiter");
   private static final ResourceLocation texture = new ResourceLocation("textures/screen/test_gui.png");
   private final UUID bossUUID;

   public BossLimiterScreen(UUID uuid) {
      super(TITLE);
      this.bossUUID = uuid;
      this.width = 176;
      this.height = 166;
   }

   public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(ms);
      super.render(ms, mouseX, mouseY, partialTicks);
   }

   public boolean keyPressed(int key, int b, int c) {
      if (key == 256) {
         this.minecraft.player.closeContainer();
         return true;
      } else {
         return super.keyPressed(key, b, c);
      }
   }

   public void onClose() {
      super.onClose();
   }

   public void init() {
      this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
      this.button_one = (Button)this.addRenderableWidget(
         new Button(this.width / 2 - 50, this.height / 2 - 46, 120, 20, Component.literal("No Passive or Toggles"), button -> {
            BeyondNetwork.INSTANCE.sendToServer(new RequestLimiterPacket(this.bossUUID, ILimiter.Limitation.NO_TOGGLE));
            this.minecraft.player.closeContainer();
         })
      );
      this.button_two = (Button)this.addRenderableWidget(
         new Button(this.width / 2 - 50, this.height / 2 - 22, 120, 20, Component.literal("No Resistances"), button -> {
            BeyondNetwork.INSTANCE.sendToServer(new RequestLimiterPacket(this.bossUUID, ILimiter.Limitation.NO_RESISTANCE));
            this.minecraft.player.closeContainer();
         })
      );
      this.button_three = (Button)this.addRenderableWidget(
         new Button(this.width / 2 - 50, this.height / 2 + 2, 120, 20, Component.literal("No Skills"), button -> {
            BeyondNetwork.INSTANCE.sendToServer(new RequestLimiterPacket(this.bossUUID, ILimiter.Limitation.NO_SKILL));
            this.minecraft.player.closeContainer();
         })
      );
      this.button_four = (Button)this.addRenderableWidget(
         new Button(this.width / 2 - 50, this.height / 2 + 26, 120, 20, Component.literal("Fair Fight"), button -> {
            BeyondNetwork.INSTANCE.sendToServer(new RequestLimiterPacket(this.bossUUID, ILimiter.Limitation.LOWER_SELF));
            this.minecraft.player.closeContainer();
         })
      );
      super.init();
   }

   public boolean isPauseScreen() {
      return false;
   }
}
