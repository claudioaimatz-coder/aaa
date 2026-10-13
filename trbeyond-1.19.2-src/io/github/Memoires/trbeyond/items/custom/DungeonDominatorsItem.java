package io.github.Memoires.trbeyond.items.custom;

import io.github.Memoires.trbeyond.procedures.DungeonDominatorsRightclickedOnBlockProcedure;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.UseOnContext;

public class DungeonDominatorsItem extends Item {
   public DungeonDominatorsItem() {
      super(new Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(64).rarity(Rarity.RARE));
   }

   public InteractionResult useOn(UseOnContext context) {
      super.useOn(context);
      DungeonDominatorsRightclickedOnBlockProcedure.execute(
         context.getLevel(), (double)context.getClickedPos().getX(), (double)context.getClickedPos().getY(), (double)context.getClickedPos().getZ()
      );
      return InteractionResult.SUCCESS;
   }
}
