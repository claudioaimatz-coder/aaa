package io.github.Memoires.trbeyond.client.screen;

import com.github.manasmods.manascore.api.client.gui.widget.ImagePredicateButton;
import com.github.manasmods.tensura.client.TensuraGUIHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.network.BeyondNetwork;
import io.github.Memoires.trbeyond.network.KurobeShopPacket;
import io.github.Memoires.trbeyond.registry.menu.BeyondMenus;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.components.Button.OnTooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.client.extensions.common.IClientItemExtensions.FontContext;
import net.minecraftforge.registries.ForgeRegistries;

public class KurobeShopScreen extends AbstractContainerScreen<KurobeShopMenu> {
   private static final HashMap<String, Object> guistate = KurobeShopMenu.guistate;
   private static final ResourceLocation TEXTURE = new ResourceLocation("trbeyond:textures/screen/kurobe_shop_screen.png");
   private final Level world;
   private final int x;
   private final int y;
   private final int z;
   private final Player entity;
   private final KurobeShopMenu menu;
   int page;
   UUID player;

   public KurobeShopScreen(KurobeShopMenu container, Inventory inventory, Component text) {
      super(container, inventory, text);
      this.world = container.world;
      this.menu = container;
      this.x = container.x;
      this.y = container.y;
      this.z = container.z;
      this.entity = container.entity;
      this.imageWidth = 176;
      this.imageHeight = 223;
      this.page = 1;
      this.player = this.entity.getUUID();
   }

   public KurobeShopScreen(KurobeShopMenu container, Inventory inventory, Component text, int page) {
      super(container, inventory, text);
      this.world = container.world;
      this.menu = container;
      this.x = container.x;
      this.y = container.y;
      this.z = container.z;
      this.entity = container.entity;
      this.imageWidth = 176;
      this.imageHeight = 223;
      this.page = page;
   }

   public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(poseStack);
      super.render(poseStack, mouseX, mouseY, partialTicks);
      this.RenderShopItems(poseStack, (this.width - this.imageWidth) / 2, (this.height - this.imageHeight) / 2);
      this.renderTooltip(poseStack, mouseX, mouseY);
   }

   public void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {
      super.renderTooltip(poseStack, mouseX, mouseY);

      for (int j = 0; j < 10; j++) {
         int weekday = Calendar.getInstance().get(7);
         int b;
         if (weekday == 1 || weekday == 4) {
            b = 1;
         } else if (weekday == 3 || weekday == 5) {
            b = 2;
         } else if (weekday == 2) {
            b = 3;
         } else if (weekday == 6) {
            b = 5;
         } else {
            b = 4;
         }

         if (KurobeShopEnum.getItem(j == 0 ? 0 : this.page, j == 0 ? b : j) != null) {
            Pair xy = this.getItemXY(j);
            int pX = this.leftPos + (Integer)xy.getFirst();
            int pY = this.topPos + (Integer)xy.getSecond();
            if (mouseX >= pX && mouseX <= pX + 16 && mouseY >= pY && mouseY <= pY + 16) {
               this.renderTooltip(
                  poseStack,
                  new ItemStack(
                     (ItemLike)ForgeRegistries.ITEMS.getValue(new ResourceLocation("trbeyond", KurobeShopEnum.getItem(j == 0 ? 0 : this.page, j == 0 ? b : j)))
                  ),
                  mouseX,
                  mouseY
               );
            }
         }
      }
   }

   protected void renderBg(PoseStack poseStack, float partialTicks, int gx, int gy) {
      RenderSystem.m_157427_(GameRenderer::getPositionTexShader);
      RenderSystem.m_157429_(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.m_157456_(0, TEXTURE);
      this.blit(poseStack, (this.width - this.imageWidth) / 2, (this.height - this.imageHeight) / 2, 0, 0, this.imageWidth, this.imageHeight);
   }

   private void RenderShopItems(PoseStack poseStack, int x, int y) {
      PoseStack stack = poseStack;
      poseStack = RenderSystem.m_157191_();
      RenderSystem.m_157182_();

      for (int j = 0; j < 10; j++) {
         ItemStack item;
         String name;
         if (j == 0) {
            if (!KurobeShopEnum.hasPage(0)) {
               continue;
            }

            int weekday = Calendar.getInstance().get(7);
            int b;
            if (weekday == 1 || weekday == 4) {
               b = 1;
            } else if (weekday == 3 || weekday == 5) {
               b = 2;
            } else if (weekday == 2) {
               b = 3;
            } else if (weekday == 6) {
               b = 5;
            } else {
               b = 4;
            }

            if (KurobeShopEnum.getItem(0, b) == null) {
               b = 1;
            }

            if (KurobeShopEnum.getItem(0, b) == null) {
               continue;
            }

            item = new ItemStack((ItemLike)ForgeRegistries.ITEMS.getValue(new ResourceLocation("trbeyond", KurobeShopEnum.getItem(0, b))));
            name = KurobeShopEnum.getItem(0, b);
         } else {
            if (KurobeShopEnum.getItem(this.page, j) == null) {
               continue;
            }

            item = new ItemStack((ItemLike)ForgeRegistries.ITEMS.getValue(new ResourceLocation("trbeyond", KurobeShopEnum.getItem(this.page, j))));
            name = KurobeShopEnum.getItem(this.page, j);
         }

         Font font = IClientItemExtensions.of(item).getFont(item, FontContext.ITEM_COUNT);
         if (font == null) {
            font = this.font;
         }

         Pair xy = this.getItemXY(j);
         int pX = x + (Integer)xy.getFirst();
         int pY = y + (Integer)xy.getSecond();
         this.itemRenderer.renderAndDecorateItem(item, pX, pY);
         this.itemRenderer.renderGuiItemDecorations(font, item, pX, pY - (this.menu.getCarried().isEmpty() ? 0 : 8), "");
         ItemStack coin = new ItemStack(
            (ItemLike)ForgeRegistries.ITEMS
               .getValue(new ResourceLocation((String)ServerConfig.INSTANCE.npcConfig.nameSpace.get(), KurobeShopEnum.getCoinType(name)))
         );
         this.itemRenderer.renderAndDecorateItem(coin, pX - 5, pY + 18);
         this.font.draw(stack, String.valueOf(KurobeShopEnum.getCost(name)), (float)(pX + 10), (float)(pY + 22), 0);
      }
   }

   private Pair getItemXY(int item) {
      switch (item) {
         case 0:
            return new Pair(153, 62);
         case 1:
            return new Pair(44, 25);
         case 2:
            return new Pair(80, 25);
         case 3:
            return new Pair(116, 25);
         case 4:
            return new Pair(44, 62);
         case 5:
            return new Pair(80, 62);
         case 6:
            return new Pair(116, 62);
         case 7:
            return new Pair(44, 99);
         case 8:
            return new Pair(80, 99);
         case 9:
            return new Pair(116, 99);
         default:
            return new Pair(0, 0);
      }
   }

   public boolean keyPressed(int key, int b, int c) {
      if (key == 256) {
         this.minecraft.player.closeContainer();
         return true;
      } else {
         return super.keyPressed(key, b, c);
      }
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
      KurobeShopMenu checkMenu = this.menu;
      ResourceLocation texture = new ResourceLocation("trbeyond", "textures/screen/shop_button.png");
      onPress = pButton -> {
         int weekday = Calendar.getInstance().get(7);
         int b;
         if (weekday == 1 || weekday == 4) {
            b = 1;
         } else if (weekday == 3 || weekday == 5) {
            b = 2;
         } else if (weekday != 2 && weekday != 6) {
            b = 4;
         } else {
            b = 3;
         }

         if (KurobeShopEnum.getItem(0, b) == null) {
            b = 1;
         }

         if (KurobeShopEnum.getItem(0, b) != null) {
            BeyondNetwork.toServer(new KurobeShopPacket(0, b));
            TensuraGUIHelper.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0F);
         }
      };
      onTooltip = (button, poseStack, i, i1) -> this.getShopTooltip(poseStack, i, i1, 1);
      ImagePredicateButton button_0 = new ImagePredicateButton(this.leftPos + 150, this.topPos + 83, 21, 9, texture, onPress, onTooltip, checkMenu::check);
      this.addRenderableWidget(button_0);
      onPress = pButton -> this.SendShopPacket(1);
      onTooltip = (button, poseStack, i, i1) -> this.getShopTooltip(poseStack, i, i1, 1);
      ImagePredicateButton button_1 = new ImagePredicateButton(this.leftPos + 41, this.topPos + 46, 21, 9, texture, onPress, onTooltip, checkMenu::check);
      this.addRenderableWidget(button_1);
      onPress = pButton -> this.SendShopPacket(2);
      onTooltip = (button, poseStack, i, i1) -> this.getShopTooltip(poseStack, i, i1, 2);
      ImagePredicateButton button_2 = new ImagePredicateButton(this.leftPos + 77, this.topPos + 46, 21, 9, texture, onPress, onTooltip, checkMenu::check);
      this.addRenderableWidget(button_2);
      onPress = pButton -> this.SendShopPacket(3);
      onTooltip = (button, poseStack, i, i1) -> this.getShopTooltip(poseStack, i, i1, 3);
      ImagePredicateButton button_3 = new ImagePredicateButton(this.leftPos + 113, this.topPos + 46, 21, 9, texture, onPress, onTooltip, checkMenu::check);
      this.addRenderableWidget(button_3);
      onPress = pButton -> this.SendShopPacket(4);
      onTooltip = (button, poseStack, i, i1) -> this.getShopTooltip(poseStack, i, i1, 4);
      ImagePredicateButton button_4 = new ImagePredicateButton(this.leftPos + 41, this.topPos + 83, 21, 9, texture, onPress, onTooltip, checkMenu::check);
      this.addRenderableWidget(button_4);
      onPress = pButton -> this.SendShopPacket(5);
      onTooltip = (button, poseStack, i, i1) -> this.getShopTooltip(poseStack, i, i1, 5);
      ImagePredicateButton button_5 = new ImagePredicateButton(this.leftPos + 77, this.topPos + 83, 21, 9, texture, onPress, onTooltip, checkMenu::check);
      this.addRenderableWidget(button_5);
      onPress = pButton -> this.SendShopPacket(6);
      onTooltip = (button, poseStack, i, i1) -> this.getShopTooltip(poseStack, i, i1, 6);
      ImagePredicateButton button_6 = new ImagePredicateButton(this.leftPos + 113, this.topPos + 83, 21, 9, texture, onPress, onTooltip, checkMenu::check);
      this.addRenderableWidget(button_6);
      onPress = pButton -> this.SendShopPacket(7);
      onTooltip = (button, poseStack, i, i1) -> this.getShopTooltip(poseStack, i, i1, 7);
      ImagePredicateButton button_7 = new ImagePredicateButton(this.leftPos + 41, this.topPos + 120, 21, 9, texture, onPress, onTooltip, checkMenu::check);
      this.addRenderableWidget(button_7);
      onPress = pButton -> this.SendShopPacket(8);
      onTooltip = (button, poseStack, i, i1) -> this.getShopTooltip(poseStack, i, i1, 8);
      ImagePredicateButton button_8 = new ImagePredicateButton(this.leftPos + 77, this.topPos + 120, 21, 9, texture, onPress, onTooltip, checkMenu::check);
      this.addRenderableWidget(button_8);
      onPress = pButton -> this.SendShopPacket(9);
      onTooltip = (button, poseStack, i, i1) -> this.getShopTooltip(poseStack, i, i1, 9);
      ImagePredicateButton button_9 = new ImagePredicateButton(this.leftPos + 113, this.topPos + 120, 21, 9, texture, onPress, onTooltip, checkMenu::check);
      this.addRenderableWidget(button_9);
      texture = new ResourceLocation("trbeyond", "textures/screen/down_arrow.png");
      onPress = pButton -> {
         TensuraGUIHelper.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0F);
         Minecraft.getInstance()
            .setScreen(
               new KurobeShopScreen(
                  (KurobeShopMenu)((MenuType)BeyondMenus.KUROBE_MENU.get()).create(1, this.minecraft.player.getInventory()),
                  this.minecraft.player.getInventory(),
                  this.title,
                  KurobeShopEnum.hasPage(this.page + 1) ? this.page + 1 : 1
               )
            );
      };
      onTooltip = (button, poseStack, i, i1) -> this.renderTooltip(poseStack, Component.literal("Previous Page"), i, i1);
      ImagePredicateButton button_down = new ImagePredicateButton(
         this.leftPos + 26, this.topPos + 112, 13, 15, texture, onPress, onTooltip, checkMenu::check
      );
      this.addRenderableWidget(button_down);
      texture = new ResourceLocation("trbeyond", "textures/screen/up_arrow.png");
      onPress = pButton -> {
         TensuraGUIHelper.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0F);
         Minecraft.getInstance()
            .setScreen(
               new KurobeShopScreen(
                  (KurobeShopMenu)((MenuType)BeyondMenus.KUROBE_MENU.get()).create(1, this.minecraft.player.getInventory()),
                  this.minecraft.player.getInventory(),
                  this.title,
                  this.page - 1 != 0 ? this.page - 1 : KurobeShopEnum.getLargestPage()
               )
            );
      };
      onTooltip = (button, poseStack, i, i1) -> this.renderTooltip(poseStack, Component.literal("Next Page"), i, i1);
      ImagePredicateButton button_up = new ImagePredicateButton(this.leftPos + 26, this.topPos + 24, 13, 15, texture, onPress, onTooltip, checkMenu::check);
      this.addRenderableWidget(button_up);
   }

   private void SendShopPacket(int slot) {
      if (KurobeShopEnum.getItem(this.page, slot) != null) {
         BeyondNetwork.toServer(new KurobeShopPacket(this.page, slot));
      }

      TensuraGUIHelper.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0F);
   }

   private void getShopTooltip(PoseStack poseStack, int i, int i1, int slot) {
      if (KurobeShopEnum.getItem(this.page, slot) != null) {
      }
   }
}
