package io.github.Memoires.trbeyond.client.screen;

import com.github.manasmods.manascore.api.client.gui.widget.ImagePredicateButton;
import com.github.manasmods.tensura.ability.skill.unique.ResearcherSkill;
import com.github.manasmods.tensura.client.TensuraGUIHelper;
import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.network.TensuraNetwork;
import com.github.manasmods.tensura.network.play2server.skill.RequestResearcherEnchantmentPacket;
import com.github.manasmods.tensura.util.ITensuraScrollbar;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.Memoires.trbeyond.network.BeyondNetwork;
import io.github.Memoires.trbeyond.network.ClientConfigData;
import io.github.Memoires.trbeyond.network.RequestResearcherPaymentPacket;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.components.Button.OnTooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class CustomResearcherEnchantmentScreen extends AbstractContainerScreen<ResearcherEnchantmentShopMenu> implements ITensuraScrollbar {
   private final ResourceLocation BACKGROUND = new ResourceLocation("trbeyond", "textures/screen/researcher/researcher_enchant.png");
   private final ResourceLocation NAME_BAR = new ResourceLocation("tensura", "textures/gui/researcher/enchantment_name.png");
   private final ResourceLocation ENCHANT_BUTTON = new ResourceLocation("tensura", "textures/gui/researcher/enchant_button.png");
   private final ResourceLocation LEVEL_BOX = new ResourceLocation("tensura", "textures/gui/researcher/enchantment_level.png");
   private final ResourceLocation CHECKBOX = new ResourceLocation("tensura", "textures/gui/checkbox.png");
   private float scrollOffs;
   private int startIndex;
   private int offscreenRows;
   private static final Set<String> ENCHANT_S = new HashSet<>(
      Arrays.asList("mending", "unbreaking", "protection", "feather_falling", "efficiency", "fortune", "looting", "sharpness", "power")
   );
   private static final Set<String> ENCHANT_A = new HashSet<>(
      Arrays.asList("smite", "silk_touch", "respiration", "depth_strider", "infinity", "sweeping_edge", "quick_charge", "piercing")
   );
   private static final Set<String> ENCHANT_B = new HashSet<>(
      Arrays.asList("fire_aspect", "knockback", "flame", "multishot", "impaling", "riptide", "luck_of_the_sea", "lure", "thorns")
   );
   private static final Set<String> ENCHANT_C_TO_F = new HashSet<>(
      Arrays.asList("blast_protection", "projectile_protection", "channeling", "frost_walker", "bane_of_arthropods", "curse_of_binding", "curse_of_vanishing")
   );
   private static final Set<String> ENGRAVE_S = new HashSet<>(
      Arrays.asList(
         "elemental_boost",
         "elemental_resistance",
         "soul_eater",
         "severance",
         "energy_steal",
         "battle_infusion",
         "darkness_infusion",
         "earth_infusion",
         "fire_infusion",
         "flame_infusion",
         "land_infusion",
         "light_infusion",
         "spatial_infusion",
         "water_infusion",
         "wind_infusion",
         "holy_coat"
      )
   );
   private static final Set<String> ENGRAVE_A = new HashSet<>(
      Arrays.asList("slotting", "barrier_piercing", "magic_interference", "magic_weapon", "holy_weapon")
   );
   private static final Set<String> ENGRAVE_B = new HashSet<>(Arrays.asList("swift", "sturdy", "crushing"));
   private static final Set<String> ENGRAVE_F = new HashSet<>(Arrays.asList("breathing_support"));
   private static final Map<String, Integer> TIER_RANK = new HashMap<>();

   private int getEnchantSPriceGold() {
      return ClientConfigData.ENCHANT_S_PRICE_GOLD;
   }

   private int getEnchantAPriceSilver() {
      return ClientConfigData.ENCHANT_A_PRICE_SILVER;
   }

   private int getEnchantBPriceSilver() {
      return ClientConfigData.ENCHANT_B_PRICE_SILVER;
   }

   private int getEnchantCtoFPriceSilver() {
      return ClientConfigData.ENCHANT_C_TO_F_PRICE_SILVER;
   }

   private int getEngraveSPriceStellar() {
      return ClientConfigData.ENGRAVE_S_PRICE_STELLAR;
   }

   private int getEngraveAPriceGold() {
      return ClientConfigData.ENGRAVE_A_PRICE_GOLD;
   }

   private int getEngraveBPriceGold() {
      return ClientConfigData.ENGRAVE_B_PRICE_GOLD;
   }

   private int getEngraveFPriceGold() {
      return ClientConfigData.ENGRAVE_F_PRICE_GOLD;
   }

   public CustomResearcherEnchantmentScreen(ResearcherEnchantmentShopMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
      super(pMenu, pPlayerInventory, pTitle);
      this.imageWidth = 176;
      this.imageHeight = 227;
   }

   protected void init() {
      super.init();
      this.scrollOffs = 0.0F;
      this.startIndex = 0;
      this.offscreenRows = ResearcherSkill.getAllEnchantments(
               ((ResearcherEnchantmentShopMenu)this.menu).getPlayer(), ((ResearcherEnchantmentShopMenu)this.getMenu()).getSkill()
            )
            .size()
         - 5;
      this.initScrollbar(this.leftPos + 126, this.topPos + 67, 65, this.scrollOffs, false, false);
      int bx = this.leftPos + 145;
      int by = this.topPos + 44;
      ResourceLocation tex = this.ENCHANT_BUTTON;
      OnPress onPress = button -> {
         if (!((ResearcherEnchantmentShopMenu)this.menu).getItemInput().isEmpty()
            && (long)((ResearcherEnchantmentShopMenu)this.menu).getExperienceCost()
               <= ResearcherEnchantmentShopMenu.getTotalXp(((ResearcherEnchantmentShopMenu)this.menu).getPlayer())) {
            Map<Enchantment, Integer> selectedEnchants = ResearcherSkill.getSelectedEnchantments(
               ((ResearcherEnchantmentShopMenu)this.menu).getPlayer(), ((ResearcherEnchantmentShopMenu)this.getMenu()).getSkill()
            );
            CustomResearcherEnchantmentScreen.CostResult costResult = this.calculateCosts(selectedEnchants);
            BeyondNetwork.INSTANCE.sendToServer(new RequestResearcherPaymentPacket(costResult.stellar, costResult.gold, costResult.silver));
         }
      };
      OnTooltip onTooltip = (button, poseStack, x, y) -> this.renderTooltip(poseStack, Component.translatable("container.enchant"), x, y);
      ResearcherEnchantmentShopMenu checkMenu = (ResearcherEnchantmentShopMenu)this.menu;
      ImagePredicateButton enchantButton = new ImagePredicateButton(bx, by, 20, 18, tex, onPress, onTooltip, checkMenu::check);
      this.addRenderableWidget(enchantButton);
   }

   public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
      this.renderBackground(pPoseStack);
      super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
      this.renderTooltip(pPoseStack, pMouseX, pMouseY);
      this.renderTabIcons();
   }

   private void renderTabIcons() {
      if (this.minecraft != null) {
         this.minecraft.getItemRenderer().renderAndDecorateItem(new ItemStack(Items.ENCHANTING_TABLE), this.getGuiLeft() + 8, this.getGuiTop() + 6);
      }
   }

   public void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
      TensuraGUIHelper.renderScaledCenteredXText(
         this.font, pPoseStack, Component.translatable("tensura.researcher_menu.enchantment_tab"), 2, 31, 145, 9, Color.WHITE, false
      );
   }

   protected void renderBg(PoseStack poseStack, float partialTick, int mX, int mY) {
      RenderSystem.m_157427_(GameRenderer::getPositionTexShader);
      RenderSystem.m_157429_(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.m_157456_(0, this.BACKGROUND);
      int x = (this.width - this.imageWidth) / 2;
      int y = (this.height - this.imageHeight) / 2;
      this.blit(poseStack, x, y, 0, 0, this.imageWidth, this.imageHeight);
      this.renderScrollbar(poseStack);
      SCROLLBAR_DATA.setActive(this.offscreenRows >= 1);
      this.renderEnchantments(poseStack, mX, mY);
   }

   protected void renderTooltip(PoseStack pPoseStack, int pX, int pY) {
      if (TensuraGUIHelper.mouseOver(pX, pY, this.leftPos + 3, this.leftPos + 28, this.topPos + 1, this.topPos + 23)) {
         this.renderTooltip(pPoseStack, Component.translatable("tensura.researcher_menu.enchantment_tab"), pX, pY);
      } else if (TensuraGUIHelper.mouseOver(pX, pY, this.leftPos + 149, this.leftPos + 160, this.topPos + 91, this.topPos + 106)) {
         int costXp = ((ResearcherEnchantmentShopMenu)this.menu).getExperienceCost();
         ChatFormatting formatting = ResearcherEnchantmentShopMenu.getTotalXp(((ResearcherEnchantmentShopMenu)this.menu).getPlayer()) >= (long)costXp
            ? ChatFormatting.GREEN
            : ChatFormatting.RED;
         Map<Enchantment, Integer> selectedEnchants = ResearcherSkill.getSelectedEnchantments(
            ((ResearcherEnchantmentShopMenu)this.menu).getPlayer(), ((ResearcherEnchantmentShopMenu)this.getMenu()).getSkill()
         );
         CustomResearcherEnchantmentScreen.CostResult costResult = this.calculateCosts(selectedEnchants);
         List<Component> tooltipLines = new ArrayList<>();
         tooltipLines.add(Component.translatable("tensura.researcher_menu.enchantment_tab.xp_cost", new Object[]{costXp}).withStyle(formatting));
         if (costResult.isStellarOnly()) {
            tooltipLines.add(Component.literal("Coins Cost: " + costResult.stellar + " Stellar Coin" + (costResult.stellar != 1 ? "s" : "")));
         } else if (costResult.gold <= 0 && costResult.silver <= 0) {
            tooltipLines.add(Component.literal("Coins Cost: Free"));
         } else {
            String s = String.format("%d Gold, %d Silver", costResult.gold, costResult.silver);
            tooltipLines.add(Component.literal("Coins Cost: " + s));
         }

         this.renderTooltip(pPoseStack, tooltipLines, Optional.empty(), pX, pY);
      } else {
         int slotClicked = this.getSlotUnderMouse((double)pX, (double)pY);
         if (slotClicked >= 0 && pX <= this.leftPos + 106) {
            int id = slotClicked + this.startIndex;
            Map<Enchantment, Integer> map = ResearcherSkill.getAllEnchantments(
               ((ResearcherEnchantmentShopMenu)this.menu).getPlayer(), ((ResearcherEnchantmentShopMenu)this.getMenu()).getSkill()
            );
            if (!map.isEmpty() && id < map.entrySet().size()) {
               Enchantment enchantment = map.keySet().stream().toList().get(id);
               CustomResearcherEnchantmentScreen.UnitInfo unit = this.getUnitInfo(enchantment);
               MutableComponent base = Component.translatable(enchantment.getDescriptionId()).withStyle(this.getColor(enchantment));
               Component costComp;
               if ("STELLAR".equals(unit.currency)) {
                  costComp = Component.literal("  Cost: " + unit.unitPrice + " Stellar Coin" + (unit.unitPrice != 1 ? "s" : ""))
                     .withStyle(ChatFormatting.GOLD);
               } else if ("GOLD".equals(unit.currency)) {
                  costComp = Component.literal("  Cost: " + unit.unitPrice + " Gold").withStyle(ChatFormatting.GOLD);
               } else {
                  costComp = Component.literal("  Cost: " + unit.unitPrice + " Silver").withStyle(ChatFormatting.GOLD);
               }

               this.renderTooltip(pPoseStack, base.append(costComp), pX, pY);
               return;
            }
         }

         super.renderTooltip(pPoseStack, pX, pY);
      }
   }

   private ChatFormatting getColor(Enchantment enchantment) {
      ChatFormatting color;
      if (enchantment.isCurse()) {
         color = ChatFormatting.RED;
      } else if (enchantment instanceof EngravingEnchantment) {
         color = ChatFormatting.GOLD;
      } else {
         color = switch (enchantment.getRarity()) {
            case COMMON -> ChatFormatting.YELLOW;
            case UNCOMMON -> ChatFormatting.GREEN;
            case RARE -> ChatFormatting.AQUA;
            case VERY_RARE -> ChatFormatting.LIGHT_PURPLE;
            default -> throw new IncompatibleClassChangeError();
         };
      }

      return color;
   }

   private void renderEnchantments(PoseStack poseStack, int mX, int mY) {
      List<Enchantment> enchantments = new ArrayList<>();
      List<Integer> levels = new ArrayList<>();
      this.getAllEnchantments().forEach((key, value) -> {
         enchantments.add(key);
         levels.add(value);
      });
      int pos = 0;
      int endIndex = this.startIndex + 5;

      for (int i = this.startIndex; i < endIndex && i < enchantments.size(); i++) {
         Enchantment enchantment = enchantments.get(i);
         int level = levels.get(i);
         Component name = Component.translatable(enchantment.getDescriptionId());
         ChatFormatting color = this.getColor(enchantment);
         Map<Enchantment, Integer> map = ResearcherSkill.getSelectedEnchantments(
            ((ResearcherEnchantmentShopMenu)this.menu).getPlayer(), ((ResearcherEnchantmentShopMenu)this.getMenu()).getSkill()
         );
         boolean flag = TensuraGUIHelper.mouseOver(
            mX, mY, this.leftPos + 21, this.leftPos + 107, this.topPos + 66 + pos * 13, this.topPos + 80 + pos * 13
         );
         RenderSystem.m_157456_(0, this.NAME_BAR);
         blit(poseStack, this.leftPos + 22, this.topPos + 67 + pos * 13, 0.0F, flag ? 13.0F : 0.0F, 86, 13, 85, 26);
         drawString(
            poseStack,
            this.font,
            TensuraGUIHelper.shortenTextComponent(name.copy().withStyle(color), 14),
            this.leftPos + 27,
            this.topPos + 70 + pos * 13,
            color.getColor() == null ? 0 : color.getColor()
         );
         int offsetX = 0;
         boolean hovering = TensuraGUIHelper.mouseOver(
            mX, mY, this.leftPos + 8, this.leftPos + 22, this.topPos + 67 + pos * 13, this.topPos + 81 + pos * 13
         );
         if (hovering) {
            offsetX = 13;
         }

         int offsetY = map.containsKey(enchantment) ? 13 : 0;
         RenderSystem.m_157456_(0, this.CHECKBOX);
         blit(poseStack, this.leftPos + 8, this.topPos + 67 + pos * 13, (float)offsetX, (float)offsetY, 13, 13, 26, 26);
         hovering = TensuraGUIHelper.mouseOver(mX, mY, this.leftPos + 107, this.leftPos + 123, this.topPos + 67 + pos * 13, this.topPos + 81 + pos * 13);
         offsetY = hovering ? 13 : 0;
         RenderSystem.m_157456_(0, this.LEVEL_BOX);
         blit(poseStack, this.leftPos + 107, this.topPos + 67 + pos * 13, 0.0F, (float)offsetY, 15, 13, 15, 26);
         int selectedLevel = map.getOrDefault(enchantment, level);
         Component levelComponent = selectedLevel <= 10
            ? Component.translatable("enchantment.level." + selectedLevel)
            : Component.literal(String.valueOf(selectedLevel));
         TensuraGUIHelper.renderCenteredXText(
            this.font, poseStack, levelComponent, this.leftPos + 108, this.topPos + 70 + pos * 13, 15, Color.LIGHT_GRAY, false
         );
         pos++;
      }
   }

   private int getSlotUnderMouse(double pX, double pY) {
      for (int i = 0; i < 5; i++) {
         if (TensuraGUIHelper.mouseOver(pX, pY, this.leftPos + 8, this.leftPos + 123, this.topPos + 66 + i * 13, this.topPos + 80 + i * 13)) {
            return i;
         }
      }

      return TensuraGUIHelper.mouseOver(pX, pY, this.leftPos + 145, this.leftPos + 163, this.topPos + 44, this.topPos + 62) ? -2 : -1;
   }

   private Map<Enchantment, Integer> getAllEnchantments() {
      Map<Enchantment, Integer> enchantments = ResearcherSkill.getAllEnchantments(
         ((ResearcherEnchantmentShopMenu)this.menu).getPlayer(), ((ResearcherEnchantmentShopMenu)this.getMenu()).getSkill()
      );
      this.offscreenRows = enchantments.size() - 5;
      if (this.startIndex > enchantments.size()) {
         this.startIndex = 0;
      }

      return enchantments;
   }

   public boolean mouseClicked(double pX, double pY, int pButton) {
      this.handleScrollbarMouseClicked(pX, pY);
      int slotClicked = this.getSlotUnderMouse(pX, pY);
      if (!((ResearcherEnchantmentShopMenu)this.menu).getItemInput().isEmpty() && slotClicked >= 0) {
         int id = slotClicked + this.startIndex;
         Map<Enchantment, Integer> mapAll = ResearcherSkill.getAllEnchantments(
            ((ResearcherEnchantmentShopMenu)this.menu).getPlayer(), ((ResearcherEnchantmentShopMenu)this.getMenu()).getSkill()
         );
         if (!mapAll.isEmpty() && id < mapAll.entrySet().size()) {
            Enchantment enchantment = mapAll.keySet().stream().toList().get(id);
            if (pX > (double)(this.leftPos + 106)) {
               Map<Enchantment, Integer> map = ResearcherSkill.getSelectedEnchantments(
                  ((ResearcherEnchantmentShopMenu)this.menu).getPlayer(), ((ResearcherEnchantmentShopMenu)this.getMenu()).getSkill()
               );
               if (enchantment != null && map.containsKey(enchantment)) {
                  int level = map.get(enchantment) + 1;
                  if (level > ((ResearcherEnchantmentShopMenu)this.menu).getMaxStoredLevel(enchantment)) {
                     level = 1;
                  }

                  TensuraNetwork.INSTANCE.sendToServer(new RequestResearcherEnchantmentPacket(id, level));
                  TensuraGUIHelper.playSound(SoundEvents.LEVER_CLICK, 1.0F);
               }

               return true;
            }
         }

         TensuraNetwork.INSTANCE.sendToServer(new RequestResearcherEnchantmentPacket(id));
         TensuraGUIHelper.playSound(SoundEvents.ITEM_FRAME_ADD_ITEM, 1.0F);
         return true;
      } else {
         return super.mouseClicked(pX, pY, pButton);
      }
   }

   public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
      this.handleScrollbarMouseScrolled(pDelta, 1);
      return true;
   }

   public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
      return this.handleScrollbarMouseDragged(pMouseY, 1) ? true : super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
   }

   @Override
   public int getStartIndex() {
      return this.startIndex;
   }

   @Override
   public int getOffscreenRows() {
      return this.offscreenRows;
   }

   @Override
   public void setStartIndex(int startIndex) {
      this.startIndex = startIndex;
   }

   private CustomResearcherEnchantmentScreen.UnitInfo getUnitInfo(Enchantment ench) {
      String key = ench.getDescriptionId().toLowerCase(Locale.ROOT);
      if (key.contains(".")) {
         String[] parts = key.split("\\.");
         key = parts[parts.length - 1];
      }

      if (key.contains("tsukumogami")) {
         return new CustomResearcherEnchantmentScreen.UnitInfo("C", "SILVER", 0);
      } else {
         boolean isEngrave = ench instanceof EngravingEnchantment;
         if (isEngrave) {
            if (ENGRAVE_S.contains(key)) {
               return new CustomResearcherEnchantmentScreen.UnitInfo("S", "STELLAR", this.getEngraveSPriceStellar());
            } else if (ENGRAVE_A.contains(key)) {
               return new CustomResearcherEnchantmentScreen.UnitInfo("A", "GOLD", this.getEngraveAPriceGold());
            } else if (ENGRAVE_B.contains(key)) {
               return new CustomResearcherEnchantmentScreen.UnitInfo("B", "GOLD", this.getEngraveBPriceGold());
            } else {
               return ENGRAVE_F.contains(key)
                  ? new CustomResearcherEnchantmentScreen.UnitInfo("F", "GOLD", this.getEngraveFPriceGold())
                  : new CustomResearcherEnchantmentScreen.UnitInfo("B", "GOLD", this.getEngraveBPriceGold());
            }
         } else if (ENCHANT_S.contains(key)) {
            return new CustomResearcherEnchantmentScreen.UnitInfo("S", "GOLD", this.getEnchantSPriceGold());
         } else if (ENCHANT_A.contains(key)) {
            return new CustomResearcherEnchantmentScreen.UnitInfo("A", "SILVER", this.getEnchantAPriceSilver());
         } else if (ENCHANT_B.contains(key)) {
            return new CustomResearcherEnchantmentScreen.UnitInfo("B", "SILVER", this.getEnchantBPriceSilver());
         } else {
            return ENCHANT_C_TO_F.contains(key)
               ? new CustomResearcherEnchantmentScreen.UnitInfo("C", "SILVER", this.getEnchantCtoFPriceSilver())
               : new CustomResearcherEnchantmentScreen.UnitInfo("C", "SILVER", this.getEnchantCtoFPriceSilver());
         }
      }
   }

   private CustomResearcherEnchantmentScreen.CostResult calculateCosts(Map<Enchantment, Integer> selected) {
      CustomResearcherEnchantmentScreen.CostResult result = new CustomResearcherEnchantmentScreen.CostResult();
      if (selected != null && !selected.isEmpty()) {
         ItemStack inputItem = ((ResearcherEnchantmentShopMenu)this.menu).getItemInput().getItem(0);
         if (inputItem.isEmpty()) {
            return result;
         } else {
            Map<Enchantment, Integer> existing = EnchantmentHelper.getEnchantments(inputItem);

            class ItemInfo {
               String currency;
               int price;
            }

            List<ItemInfo> items = new ArrayList<>();

            for (Entry<Enchantment, Integer> entry : selected.entrySet()) {
               Enchantment ench = entry.getKey();
               int desiredLevel = entry.getValue();
               int currentLevel = existing.getOrDefault(ench, 0);
               if (currentLevel < desiredLevel) {
                  CustomResearcherEnchantmentScreen.UnitInfo u = this.getUnitInfo(ench);
                  ItemInfo info = new ItemInfo();
                  info.currency = u.currency;
                  info.price = u.unitPrice;
                  items.add(info);
               }
            }

            int stellarSum = items.stream().filter(i -> "STELLAR".equals(i.currency)).mapToInt(i -> i.price).sum();
            if (stellarSum > 0) {
               result.stellarOnly = true;
               result.stellar = stellarSum;
               return result;
            } else {
               int goldSum = items.stream().filter(i -> "GOLD".equals(i.currency)).mapToInt(i -> i.price).sum();
               if (goldSum > 0) {
                  result.gold = goldSum;
                  return result;
               } else {
                  int silverSum = items.stream().filter(i -> "SILVER".equals(i.currency)).mapToInt(i -> i.price).sum();
                  if (silverSum > 0) {
                     result.silver = silverSum;
                  }

                  return result;
               }
            }
         }
      } else {
         return result;
      }
   }

   static {
      TIER_RANK.put("S", 6);
      TIER_RANK.put("A", 5);
      TIER_RANK.put("B", 4);
      TIER_RANK.put("C", 3);
      TIER_RANK.put("D", 2);
      TIER_RANK.put("E", 1);
      TIER_RANK.put("F", 0);
   }

   private static class CostResult {
      public int stellar = 0;
      public int gold = 0;
      public int silver = 0;
      public boolean stellarOnly = false;

      public boolean isStellarOnly() {
         return this.stellarOnly;
      }
   }

   private static class UnitInfo {
      public final String tier;
      public final String currency;
      public final int unitPrice;

      public UnitInfo(String tier, String currency, int unitPrice) {
         this.tier = tier;
         this.currency = currency;
         this.unitPrice = unitPrice;
      }
   }
}
