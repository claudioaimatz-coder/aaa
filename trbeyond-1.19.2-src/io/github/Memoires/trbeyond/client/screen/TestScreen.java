package io.github.Memoires.trbeyond.client.screen;

import com.github.manasmods.tensura.entity.BarghestEntity;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.Memoires.trbeyond.network.BeyondNetwork;
import io.github.Memoires.trbeyond.network.SummonExamMobPacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class TestScreen extends Screen {
   Button button_yes;
   Button button_no;
   int x = this.width / 2;
   int y = this.height / 2;
   private static final Component TITLE = Component.literal("Stats Menu");
   private static final ResourceLocation texture = new ResourceLocation("trbeyond:textures/screen/test_gui.png");

   public TestScreen(Component text) {
      super(TITLE);
      this.width = 176;
      this.height = 166;
   }

   public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(ms);
      this.font
         .draw(ms, Component.translatable("gui.slime.test_gui.label_guild_examination"), (float)(this.width + 20), (float)(this.height + 20), -12829636);
      this.font.draw(ms, Component.translatable("gui.test_gui.start_exem"), (float)(this.width / 2 - 50), (float)(this.height / 2 - 42), -12829636);
      this.font.draw(ms, Component.translatable("gui.slime.test_gui.label_e_rank"), 7.0F, 25.0F, -12829636);
      this.font.draw(ms, Component.translatable("gui.slime.test_gui.label_barghest"), 44.0F, 25.0F, -12829636);
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
      this.button_yes = (Button)this.addRenderableWidget(
         new Button(this.width / 2 - 50, this.height / 2 - 22, 100, 20, Component.literal("Start Test"), button -> {
            BeyondNetwork.toServer(new SummonExamMobPacket((EntityType<BarghestEntity>)TensuraEntityTypes.BARGHEST.get()));
            this.minecraft.player.closeContainer();
         })
      );
      this.button_no = (Button)this.addRenderableWidget(
         new Button(this.width / 2 - 50, this.height / 2 + 2, 100, 20, Component.literal("Cancel"), button -> this.minecraft.player.closeContainer())
      );
      super.init();
   }

   public boolean isPauseScreen() {
      return false;
   }
}
