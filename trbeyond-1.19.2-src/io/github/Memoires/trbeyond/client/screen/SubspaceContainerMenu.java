package io.github.Memoires.trbeyond.client.screen;

import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.item.food.HealingPotionItem;
import io.github.Memoires.trbeyond.TRBeyond;
import io.github.Memoires.trbeyond.client.screen.slot.SubordinateSlot;
import io.github.Memoires.trbeyond.client.screen.slot.SubspaceSlot;
import io.github.Memoires.trbeyond.container.SubspacePocketContainer;
import io.github.Memoires.trbeyond.items.custom.SubspacePocketItem;
import io.github.Memoires.trbeyond.registry.menu.BeyondMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

public class SubspaceContainerMenu extends AbstractContainerMenu {
   private final SubspacePocketContainer container;
   private final ItemStack storageItem;

   public static SubspaceContainerMenu fromNetwork(int id, Inventory inv, FriendlyByteBuf extraData) {
      return new SubspaceContainerMenu(id, inv, inv.player, extraData.readItem());
   }

   public SubspaceContainerMenu(int pContainerId, Inventory inv, Player player, final ItemStack itemStack) {
      super((MenuType)BeyondMenus.SUBSPACE_CONTAINER_MENU.get(), pContainerId);
      if (itemStack.getItem() instanceof SubspacePocketItem subspacePocketItem) {
         this.container = new SubspacePocketContainer(
            subspacePocketItem.storageCapacity, subspacePocketItem.maxStackSize, subspacePocketItem.subordinateCapacity
         ) {
            public void setChanged() {
               itemStack.getOrCreateTag().put("SpatialStorage", this.createTag());
            }
         };
         this.container.fromTag(itemStack.getOrCreateTag().getList("SpatialStorage", 10));
      } else {
         this.container = new SubspacePocketContainer(9, 64, 9);
      }

      this.storageItem = itemStack;
      TRBeyond.getLogger().info(itemStack.toString());
      checkContainerSize(this.container, this.container.getContainerSize());
      this.container.startOpen(player);
      this.addPlayerInventory(inv);
      this.addPlayerHotbar(inv);
      this.addSubordinateSlots();
      this.addSpatialSlots();
   }

   private void addPlayerInventory(Inventory inventory) {
      Vec2 offset = this.getInventoryOffset();

      for (int row = 0; row < 3; row++) {
         for (int slot = 0; slot < 9; slot++) {
            this.addSlot(new Slot(inventory, slot + row * 9 + 9, slot * 18 + (int)offset.x, row * 18 + (int)offset.y));
         }
      }
   }

   private void addPlayerHotbar(Inventory inventory) {
      Vec2 offset = this.getInventoryOffset();

      for (int slot = 0; slot < 9; slot++) {
         if (slot == inventory.selected && inventory.getItem(slot).getItem() instanceof SubspacePocketItem) {
            this.addSlot(new Slot(inventory, slot, slot * 18 + (int)offset.x, 59 + (int)offset.y) {
               public boolean allowModification(Player pPlayer) {
                  return false;
               }

               public boolean mayPlace(ItemStack pStack) {
                  return false;
               }

               public boolean mayPickup(Player pPlayer) {
                  return false;
               }
            });
         } else {
            this.addSlot(new Slot(inventory, slot, slot * 18 + (int)offset.x, 59 + (int)offset.y));
         }
      }
   }

   private void addSubordinateSlots() {
      Vec2 offset = this.getSpatialSlotsOffset();

      for (int slot = 0; slot < this.container.getSubordinateCapacity(); slot++) {
         this.addSlot(new SubordinateSlot(this.container, slot, slot * 18 + (int)offset.x + 92, (int)offset.y + 38));
      }
   }

   private void addSpatialSlots() {
      Vec2 offset = this.getSpatialSlotsOffset();

      for (int row = 0; row < this.getContainerSize() / 9; row++) {
         for (int slot = 0; slot < 9; slot++) {
            this.addSlot(new SubspaceSlot(this.container, slot + row * 9, slot * 18 + (int)offset.x + 20, row * 18 + (int)offset.y + 74));
         }
      }
   }

   public int getContainerSize() {
      return this.container.getContainerSize();
   }

   private Vec2 getInventoryOffset() {
      return switch (this.getContainerSize()) {
         case 9 -> new Vec2(20.0F, 158.0F);
         case 18 -> new Vec2(20.0F, 166.0F);
         case 27 -> new Vec2(20.0F, 170.0F);
         default -> new Vec2(0.0F, 0.0F);
      };
   }

   private Vec2 getSpatialSlotsOffset() {
      return switch (this.getContainerSize()) {
         case 9 -> new Vec2(0.0F, 0.0F);
         case 18 -> new Vec2(0.0F, 9.0F);
         case 27 -> new Vec2(0.0F, 9.0F);
         default -> new Vec2(0.0F, 0.0F);
      };
   }

   public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
      ItemStack itemstack = ItemStack.EMPTY;
      Slot slot = (Slot)this.slots.get(pIndex);
      if (slot != null && slot.hasItem()) {
         ItemStack itemstack1 = slot.getItem();
         itemstack = itemstack1.copy();
         if (pIndex < 36) {
            if (!this.moveItemStackTo(itemstack1, 36, this.slots.size(), true)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
            return ItemStack.EMPTY;
         }

         if (itemstack1.isEmpty()) {
            slot.set(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }
      }

      return itemstack;
   }

   protected boolean moveItemStackTo(ItemStack pStack, int pStartIndex, int pEndIndex, boolean pReverseDirection) {
      boolean flag = false;
      int i = pStartIndex;
      if (pReverseDirection) {
         i = pEndIndex - 1;
      }

      if (pStack.isStackable() || pStartIndex == 36 && pStack.getItem() instanceof HealingPotionItem) {
         while (!pStack.isEmpty() && (pReverseDirection ? i >= pStartIndex : i < pEndIndex)) {
            Slot slot = (Slot)this.slots.get(i);
            ItemStack itemstack = slot.getItem();
            if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(pStack, itemstack)) {
               int j = itemstack.getCount() + pStack.getCount();
               int maxSize = slot.getMaxStackSize(pStack);
               if (j <= maxSize) {
                  pStack.setCount(0);
                  itemstack.setCount(j);
                  slot.setChanged();
                  flag = true;
               } else if (itemstack.getCount() < maxSize) {
                  pStack.shrink(maxSize - itemstack.getCount());
                  itemstack.setCount(maxSize);
                  slot.setChanged();
                  flag = true;
               }
            }

            if (pReverseDirection) {
               i--;
            } else {
               i++;
            }
         }
      }

      if (!pStack.isEmpty()) {
         if (pReverseDirection) {
            i = pEndIndex - 1;
         } else {
            i = pStartIndex;
         }

         while (pReverseDirection ? i >= pStartIndex : i < pEndIndex) {
            Slot slot1 = (Slot)this.slots.get(i);
            ItemStack itemstack1 = slot1.getItem();
            if (itemstack1.isEmpty() && slot1.mayPlace(pStack)) {
               if (pStack.getCount() > slot1.getMaxStackSize(pStack)) {
                  slot1.set(pStack.split(slot1.getMaxStackSize(pStack)));
               } else {
                  slot1.set(pStack.split(pStack.getCount()));
               }

               slot1.setChanged();
               flag = true;
               break;
            }

            if (pReverseDirection) {
               i--;
            } else {
               i++;
            }
         }
      }

      return flag;
   }

   public void doClick(int pSlotId, int pButton, ClickType pClickType, Player pPlayer) {
      if (pClickType == ClickType.SWAP) {
         Inventory inventory = pPlayer.getInventory();
         Slot hoveredSlot = (Slot)this.slots.get(pSlotId);
         ItemStack hotbarStack = inventory.getItem(pButton);
         ItemStack hoveredStack = hoveredSlot.getItem();
         if (!hotbarStack.isEmpty() || !hoveredStack.isEmpty()) {
            if (hotbarStack.isEmpty()) {
               if (hoveredSlot.mayPickup(pPlayer)) {
                  if (hoveredStack.getCount() > hoveredStack.getMaxStackSize()) {
                     inventory.setItem(pButton, hoveredStack.split(hoveredStack.getMaxStackSize()));
                  } else {
                     inventory.setItem(pButton, hoveredStack);
                     hoveredSlot.set(ItemStack.EMPTY);
                  }

                  hoveredSlot.onTake(pPlayer, hoveredStack);
               }
            } else if (hoveredStack.isEmpty()) {
               if (hoveredSlot.mayPlace(hotbarStack)) {
                  int l1 = hoveredSlot.getMaxStackSize(hotbarStack);
                  if (hotbarStack.getCount() > l1) {
                     hoveredSlot.set(hotbarStack.split(l1));
                  } else {
                     inventory.setItem(pButton, ItemStack.EMPTY);
                     hoveredSlot.set(hotbarStack);
                  }
               }
            } else if (hoveredSlot.mayPickup(pPlayer) && hoveredSlot.mayPlace(hotbarStack)) {
               int hoveredMaxStackSize = hoveredSlot.getMaxStackSize(hotbarStack);
               if (hotbarStack.getCount() > hoveredMaxStackSize) {
                  hoveredSlot.set(hotbarStack.split(hoveredMaxStackSize));
                  hoveredSlot.onTake(pPlayer, hoveredStack);
                  if (!inventory.add(hoveredStack)) {
                     pPlayer.drop(hoveredStack, true);
                  }
               } else {
                  int hoveredStackCount = hoveredStack.getCount();
                  int hoveredBaseMaxStackSize = hoveredStack.getMaxStackSize();
                  if (hoveredStackCount > hoveredBaseMaxStackSize) {
                     ItemStack extraStack = hoveredStack.split(hoveredStackCount - hoveredBaseMaxStackSize);
                     if (!inventory.add(extraStack)) {
                        pPlayer.drop(hoveredStack, true);
                     }
                  }

                  inventory.setItem(pButton, hoveredStack);
                  hoveredSlot.set(hotbarStack);
                  hoveredSlot.onTake(pPlayer, hoveredStack);
               }
            }
         }
      } else {
         super.doClick(pSlotId, pButton, pClickType, pPlayer);
      }
   }

   public boolean stillValid(@NotNull Player pPlayer) {
      return !(this.storageItem.getItem() instanceof SubspacePocketItem)
         ? false
         : pPlayer.getMainHandItem() == this.storageItem || pPlayer.getOffhandItem() == this.storageItem;
   }

   public void removed(@NotNull Player player) {
      this.storageItem.getOrCreateTag().put("SpatialStorage", this.container.createTag());
      ItemStack stack = player.containerMenu.getCarried();
      if (!stack.isEmpty()) {
         this.moveItemStackTo(stack, 0, 36, false);
         if (stack.getCount() > 0) {
            SkillHelper.dropItem(player, player.getRandom(), stack, 10, 0.5F);
         }
      }
   }

   public boolean isThisContainerItem(@NotNull ItemStack item) {
      return this.storageItem == item;
   }
}
