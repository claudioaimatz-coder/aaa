package io.github.Memoires.trbeyond.client.screen;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.skill.unique.ResearcherSkill;
import com.github.manasmods.tensura.menu.ResearcherEnchantmentMenu;
import io.github.Memoires.trbeyond.ability.battlewill.utility.TRBeyondSkills;
import io.github.Memoires.trbeyond.registry.menu.BeyondMenus;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class ResearcherEnchantmentShopMenu extends ResearcherEnchantmentMenu {
   public MenuType<?> getType() {
      return (MenuType<?>)BeyondMenus.RESEARCHER_SHOP_MENU.get();
   }

   public ResearcherEnchantmentShopMenu(int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
      this(pContainerId, inventory, ContainerLevelAccess.NULL, (ManasSkill)SkillAPI.getSkillRegistry().getValue(buf.readResourceLocation()));
   }

   public ResearcherEnchantmentShopMenu(int pContainerId, Inventory inventory, ContainerLevelAccess access, @Nullable final ManasSkill skill) {
      super(pContainerId, inventory, access, skill);
      int bookSlotIndex = -1;

      for (int i = 0; i < this.slots.size(); i++) {
         Slot s = (Slot)this.slots.get(i);
         if (s.container == this.getBookInput()) {
            bookSlotIndex = i;
            break;
         }
      }

      if (bookSlotIndex != -1) {
         Slot oldSlot = (Slot)this.slots.get(bookSlotIndex);
         Slot customBookSlot = new Slot(this.getBookInput(), 0, 64, 44) {
            public int getMaxStackSize() {
               return 1;
            }

            public void set(ItemStack pStack) {
               super.set(pStack);
               boolean book = pStack.is(Items.ENCHANTED_BOOK) || skill != null && skill.equals(TRBeyondSkills.KROUBE_ENChANT_SHOP.get());
               if (book && !ResearcherEnchantmentShopMenu.this.getPlayer().level.isClientSide()) {
                  if (ResearcherSkill.addEnchantments(ResearcherEnchantmentShopMenu.this.getPlayer(), EnchantmentHelper.getEnchantments(pStack), skill)) {
                     if (pStack.is(Items.ENCHANTED_BOOK)) {
                        ResearcherEnchantmentShopMenu.this.getBookInput().clearContent();
                        ResearcherEnchantmentShopMenu.this.getPlayer().playNotifySound(SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 1.0F, 1.0F);
                     } else {
                        ItemStack copy = pStack.copy();
                        EnchantmentHelper.setEnchantments(Map.of(), copy);
                        ResearcherEnchantmentShopMenu.this.getBookInput().setItem(0, copy);
                        ResearcherEnchantmentShopMenu.this.getPlayer().playNotifySound(SoundEvents.GRINDSTONE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                     }
                  }

                  ResearcherEnchantmentShopMenu.this.slotsChanged(this.container);
               }
            }

            public boolean mayPlace(ItemStack pStack) {
               return super.mayPlace(pStack) && (pStack.is(Items.ENCHANTED_BOOK) || skill != null && skill.equals(TRBeyondSkills.KROUBE_ENChANT_SHOP.get()));
            }
         };
         customBookSlot.index = oldSlot.index;
         this.slots.set(bookSlotIndex, customBookSlot);
      }
   }

   @Override
   public void handleFinalizeEnchantment() {
      ItemStack stack = this.getItemInput().getItem(0).copy();
      if (stack.isEmpty()) {
         ResearcherSkill.addSelectedEnchantments(this.getPlayer(), Map.of(), true, this.getSkill());
      } else {
         int cost = this.getExperienceCost();
         if (getTotalXp(this.getPlayer()) >= (long)cost) {
            if (cost > 0) {
               this.getPlayer().giveExperiencePoints(-cost);
            }

            this.mayPickup = true;
            Map<Enchantment, Integer> selected = ResearcherSkill.getSelectedEnchantments(this.getPlayer(), this.getSkill());
            EnchantmentHelper.setEnchantments(selected, stack);
            this.getItemInput().clearContent();
            this.getItemOutput().setItem(0, stack);
            this.broadcastFullState();
            ResearcherSkill.addSelectedEnchantments(this.getPlayer(), Map.of(), true, this.getSkill());
         }
      }
   }
}
