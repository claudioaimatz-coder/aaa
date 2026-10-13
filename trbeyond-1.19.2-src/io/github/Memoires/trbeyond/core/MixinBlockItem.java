package io.github.Memoires.trbeyond.core;

import io.github.Memoires.trbeyond.data.BeyondTags;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BlockItem.class})
public class MixinBlockItem {
   @Inject(
      method = {"place(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/InteractionResult;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void place(BlockPlaceContext pContext, CallbackInfoReturnable<InteractionResult> cir) {
      if (pContext.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
         Player player = pContext.getPlayer();
         if (player != null && player.isCreative()) {
            return;
         }

         BlockItem blockItem = (BlockItem)this;
         if (blockItem.getBlock().builtInRegistryHolder().is(BeyondTags.DUNGEON_PLACEABLE)) {
            return;
         }

         if (player != null) {
            player.displayClientMessage(Component.translatable("trbeyond.message.rule_violation.prohibited").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
         }

         cir.setReturnValue(InteractionResult.FAIL);
         cir.cancel();
      }
   }
}
