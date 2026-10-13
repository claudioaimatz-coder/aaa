package io.github.Memoires.trbeyond.handlers;

import io.github.Memoires.trbeyond.block.LanthornBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.FORGE
)
public class LanthornPlacementHandler {
   @SubscribeEvent
   public static void onBlockPlace(EntityPlaceEvent event) {
      BlockState placedState = event.getPlacedBlock();
      if (placedState.getBlock() instanceof LanthornBlock) {
         BlockPos pos = event.getPos();
         Level world = (Level)event.getLevel();

         for (BlockPos checkPos : BlockPos.betweenClosed(pos.offset(-2, 0, -2), pos.offset(2, 0, 2))) {
            if (!checkPos.equals(pos)) {
               BlockState nearbyState = world.getBlockState(checkPos);
               if (nearbyState.getBlock() instanceof LanthornBlock) {
                  if (event.getEntity() instanceof Player player) {
                     player.displayClientMessage(Component.translatable("You cannot place another lantern in this area."), true);
                  }

                  event.setCanceled(true);
                  return;
               }
            }
         }
      }
   }
}
