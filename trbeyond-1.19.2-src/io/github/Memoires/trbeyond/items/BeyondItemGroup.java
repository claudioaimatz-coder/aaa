package io.github.Memoires.trbeyond.items;

import io.github.Memoires.trbeyond.registry.items.BeyondItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class BeyondItemGroup extends CreativeModeTab {
   public static final BeyondItemGroup BEYOND_ITEMS = new BeyondItemGroup("trbeyond.main");

   public BeyondItemGroup(String label) {
      super(label);
   }

   @NotNull
   public ItemStack makeIcon() {
      return new ItemStack((ItemLike)BeyondItems.REVIVAL_BRACELET.get());
   }
}
