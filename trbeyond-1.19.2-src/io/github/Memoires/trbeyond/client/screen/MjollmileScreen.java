package io.github.Memoires.trbeyond.client.screen;

import com.github.manasmods.manascore.api.client.gui.widget.ImagePredicateButton;
import com.github.manasmods.tensura.client.TensuraGUIHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.Memoires.trbeyond.network.BeyondNetwork;
import io.github.Memoires.trbeyond.network.MjollmileCoinPacket;
import java.util.HashMap;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.components.Button.OnTooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class MjollmileScreen extends AbstractContainerScreen<MjollmileMenu> {
   private static final HashMap<String, Object> guistate = MjollmileMenu.guistate;
   private static final ResourceLocation TEXTURE = new ResourceLocation("trbeyond:textures/screen/mjollmile_screen.png");
   private final Level world;
   private final int x;
   private final int y;
   private final int z;
   private final Player entity;
   private final MjollmileMenu menu;

   public MjollmileScreen(MjollmileMenu container, Inventory inventory, Component text) {
      super(container, inventory, text);
      this.world = container.world;
      this.menu = container;
      this.x = container.x;
      this.y = container.y;
      this.z = container.z;
      this.entity = container.entity;
      this.imageWidth = 176;
      this.imageHeight = 223;
   }

   public boolean keyPressed(int key, int b, int c) {
      if (key == 256) {
         this.minecraft.player.closeContainer();
         return true;
      } else {
         return super.keyPressed(key, b, c);
      }
   }

   public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(poseStack);
      super.render(poseStack, mouseX, mouseY, partialTicks);
      this.renderCoins(poseStack);
      this.renderTooltip(poseStack, mouseX, mouseY);
   }

   public void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {
      super.renderTooltip(poseStack, mouseX, mouseY);

      for (int i = 0; i < 4; i++) {
         Item coin = switch (i) {
            case 0 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "bronze_coin"));
            case 1 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "silver_coin"));
            case 2 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "gold_coin"));
            case 3 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "stellar_gold_coin"));
            default -> null;
         };
         if (coin != null) {
            ItemStack stack = new ItemStack(coin);
            int x = this.leftPos + 44 * i + 14;
            int y = this.topPos + 33;
            if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
               this.renderTooltip(poseStack, stack, mouseX, mouseY);
            }
         }
      }
   }

   private void renderCoins(PoseStack poseStack) {
      poseStack = RenderSystem.m_157191_();
      RenderSystem.m_157182_();

      for (int i = 0; i < 4; i++) {
         int x = this.leftPos + 44 * i + 14;
         int y = this.topPos + 33;

         Item coin = switch (i) {
            case 0 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "bronze_coin"));
            case 1 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "silver_coin"));
            case 2 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "gold_coin"));
            case 3 -> (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation("tensura", "stellar_gold_coin"));
            default -> null;
         };
         if (coin != null) {
            ItemStack stack = new ItemStack(coin);
            this.itemRenderer.renderAndDecorateItem(stack, x, y);
            this.itemRenderer.renderGuiItemDecorations(this.font, stack, x, y - (this.menu.getCarried().isEmpty() ? 0 : 8), "");
         }
      }
   }

   protected void renderBg(PoseStack poseStack, float partialTicks, int gx, int gy) {
      RenderSystem.m_157427_(GameRenderer::getPositionTexShader);
      RenderSystem.m_157429_(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.m_157456_(0, TEXTURE);
      this.blit(poseStack, (this.width - this.imageWidth) / 2, (this.height - this.imageHeight) / 2, 0, 0, this.imageWidth, this.imageHeight);
   }

   protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
   }

   public void init() {
      super.init();
      new ResourceLocation("tensura", "textures/blank_texture.png");
      OnPress onPress = pButton -> {
      };
      OnTooltip onTooltip = (button, poseStack, i, i1) -> {
      };
      MjollmileMenu checkMenu = this.menu;
      ResourceLocation texture = new ResourceLocation("trbeyond", "textures/screen/left_arrow.png");
      onPress = pButton -> this.SendCoinPacket(1);
      ImagePredicateButton silverToBronze = new ImagePredicateButton(
         this.leftPos + 33, this.topPos + 35, 10, 13, texture, onPress, onTooltip, checkMenu::check
      );
      this.addRenderableWidget(silverToBronze);
      onPress = pButton -> this.SendCoinPacket(2);
      ImagePredicateButton goldToSilver = new ImagePredicateButton(
         this.leftPos + 77, this.topPos + 35, 10, 13, texture, onPress, onTooltip, checkMenu::check
      );
      this.addRenderableWidget(goldToSilver);
      onPress = pButton -> this.SendCoinPacket(3);
      ImagePredicateButton stellarToGold = new ImagePredicateButton(
         this.leftPos + 121, this.topPos + 35, 10, 13, texture, onPress, onTooltip, checkMenu::check
      );
      this.addRenderableWidget(stellarToGold);
      texture = new ResourceLocation("trbeyond", "textures/screen/right_arrow.png");
      onPress = pButton -> this.SendCoinPacket(4);
      ImagePredicateButton bronzeToSilver = new ImagePredicateButton(
         this.leftPos + 45, this.topPos + 35, 10, 13, texture, onPress, onTooltip, checkMenu::check
      );
      this.addRenderableWidget(bronzeToSilver);
      onPress = pButton -> this.SendCoinPacket(5);
      ImagePredicateButton silverToGold = new ImagePredicateButton(
         this.leftPos + 89, this.topPos + 35, 10, 13, texture, onPress, onTooltip, checkMenu::check
      );
      this.addRenderableWidget(silverToGold);
      onPress = pButton -> this.SendCoinPacket(6);
      ImagePredicateButton goldToStellar = new ImagePredicateButton(
         this.leftPos + 133, this.topPos + 35, 10, 13, texture, onPress, onTooltip, checkMenu::check
      );
      this.addRenderableWidget(goldToStellar);
   }

   private void SendCoinPacket(int type) {
      BeyondNetwork.toServer(new MjollmileCoinPacket(type));
      TensuraGUIHelper.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0F);
   }
}
