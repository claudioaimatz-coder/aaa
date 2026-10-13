package io.github.Memoires.trbeyond.client.screen.slot;

import com.github.manasmods.tensura.item.food.HealingPotionItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SubspaceSlot extends Slot {
   public SubspaceSlot(Container pContainer, int pSlot, int pX, int pY) {
      super(pContainer, pSlot, pX, pY);
   }

   public int getMaxStackSize(ItemStack pStack) {
      return !pStack.isStackable() && !(pStack.getItem() instanceof HealingPotionItem) ? 1 : this.container.getMaxStackSize() / 64 * super.getMaxStackSize(pStack);
   }

   public boolean mayPlace(ItemStack pStack) {
      return pStack.getItem().canFitInsideContainerItems();
   }
}
