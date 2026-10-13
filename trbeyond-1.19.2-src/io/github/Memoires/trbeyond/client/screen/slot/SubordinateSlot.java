package io.github.Memoires.trbeyond.client.screen.slot;

import io.github.Memoires.trbeyond.container.SubspacePocketContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SubordinateSlot extends Slot {
   public final SubspacePocketContainer container;
   private final int slot;

   public SubordinateSlot(SubspacePocketContainer pContainer, int pSlot, int pX, int pY) {
      super(pContainer, pSlot, pX, pY);
      this.container = pContainer;
      this.slot = pSlot;
   }

   public ItemStack getItem() {
      return this.container.getSubordinateItem(this.slot);
   }

   public boolean allowModification(Player pPlayer) {
      return false;
   }

   public boolean mayPlace(ItemStack pStack) {
      return false;
   }

   public boolean mayPickup(Player pPlayer) {
      return false;
   }

   public void initialize(ItemStack itemStack) {
      this.container.setSubordinateItem(this.slot, itemStack);
      this.setChanged();
   }
}
