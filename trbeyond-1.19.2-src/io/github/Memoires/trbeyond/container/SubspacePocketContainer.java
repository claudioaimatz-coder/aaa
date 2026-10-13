package io.github.Memoires.trbeyond.container;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class SubspacePocketContainer extends SimpleContainer {
   private final int maxStackSize;
   private final int subordinateCapacity;
   private final NonNullList<ItemStack> subordinates;

   public SubspacePocketContainer(int size, int stackSize, int subordinateCapacity) {
      super(size);
      this.maxStackSize = stackSize;
      this.subordinateCapacity = subordinateCapacity;
      this.subordinates = NonNullList.withSize(subordinateCapacity, ItemStack.EMPTY);
   }

   public int getMaxStackSize() {
      return this.maxStackSize;
   }

   public boolean canStoreSubordinate() {
      for (int i = 0; i < this.subordinates.size(); i++) {
         if (this.subordinates.get(i) == ItemStack.EMPTY) {
            return true;
         }
      }

      return false;
   }

   public void storeSubordinate(ItemStack shadow) {
      for (int i = 0; i < this.subordinates.size(); i++) {
         if (this.subordinates.get(i) == ItemStack.EMPTY) {
            this.setSubordinateItem(i, shadow);
            return;
         }
      }
   }

   public int getSubordinateCapacity() {
      return this.subordinateCapacity;
   }

   public int getFirstSubordinateItemIndex() {
      for (int i = 0; i < this.subordinates.size(); i++) {
         if (this.subordinates.get(i) != ItemStack.EMPTY) {
            return i;
         }
      }

      return -1;
   }

   public void setSubordinateItem(int index, ItemStack stack) {
      this.subordinates.set(index, stack);
   }

   public ItemStack getSubordinateItem(int index) {
      return index >= 0 && index < this.subordinates.size() ? (ItemStack)this.subordinates.get(index) : ItemStack.EMPTY;
   }

   public void removeSubordinateItem(int index) {
      this.setSubordinateItem(index, ItemStack.EMPTY);
   }

   public void fromTag(ListTag tag) {
      for (int i = 0; i < this.getContainerSize(); i++) {
         this.setItem(i, ItemStack.EMPTY);
      }

      for (int i = 0; i < this.getSubordinateCapacity(); i++) {
         this.setSubordinateItem(i, ItemStack.EMPTY);
      }

      for (int i = 0; i < tag.size(); i++) {
         CompoundTag iTag = tag.getCompound(i);
         int k = iTag.getInt("Slot");
         boolean isSubordinate = iTag.getBoolean("s");
         if (isSubordinate && k < this.subordinates.size()) {
            ItemStack shadow = ItemStack.of(iTag);
            this.subordinates.set(k, shadow);
         }

         if (!isSubordinate && k < this.getContainerSize()) {
            ItemStack item = ItemStack.of(iTag);
            this.setItem(k, item);
         }
      }
   }

   public ListTag createTag() {
      ListTag storageNbt = new ListTag();

      for (int i = 0; i < this.getContainerSize(); i++) {
         ItemStack itemStack = this.getItem(i);
         if (!itemStack.isEmpty()) {
            CompoundTag itemTag = new CompoundTag();
            itemTag.putInt("Slot", i);
            itemTag.putBoolean("s", false);
            itemStack.save(itemTag);
            storageNbt.add(itemTag);
         }
      }

      for (int ix = 0; ix < this.subordinates.size(); ix++) {
         ItemStack subordinateItem = this.getSubordinateItem(ix);
         if (!subordinateItem.isEmpty()) {
            CompoundTag subordinateTag = new CompoundTag();
            subordinateTag.putInt("Slot", ix);
            subordinateTag.putBoolean("s", true);
            subordinateItem.save(subordinateTag);
            storageNbt.add(subordinateTag);
         }
      }

      return storageNbt;
   }
}
