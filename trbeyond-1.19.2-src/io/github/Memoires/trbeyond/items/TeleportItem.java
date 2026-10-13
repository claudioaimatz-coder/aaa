package io.github.Memoires.trbeyond.items;

import com.github.manasmods.tensura.ability.SkillHelper;
import io.github.Memoires.trbeyond.TRBeyond;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public class TeleportItem extends Item {
   public TeleportItem(Properties props) {
      super(props);
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (!player.isAlive()) {
         return super.use(level, player, hand);
      } else if (level.isClientSide()) {
         return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
      } else if (!player.isPassenger() && !player.isVehicle() && player.canChangeDimensions()) {
         ResourceKey<Level> destination = getDestination(player);
         ServerLevel serverWorld = player.getCommandSenderWorld().getServer().m_129880_(destination);
         TRBeyond.getLogger().warn("Destination level is null: {}", serverWorld == null);
         if (serverWorld == null) {
            return super.use(level, player, hand);
         } else {
            SkillHelper.moveAcrossDimensionTo(player, 9.5, 117.0, -6.5, player.getYRot(), player.getXRot(), serverWorld);
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
         }
      } else {
         return super.use(level, player, hand);
      }
   }

   private static ResourceKey<Level> getDestination(Player player) {
      return !player.getCommandSenderWorld().dimension().location().equals(BeyondDungeon.DUNGEON_LEVEL_KEY.location()) ? BeyondDungeon.DUNGEON_LEVEL_KEY : Level.OVERWORLD;
   }
}
