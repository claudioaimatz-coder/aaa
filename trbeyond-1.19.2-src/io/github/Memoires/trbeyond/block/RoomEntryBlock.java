package io.github.Memoires.trbeyond.block;

import com.github.manasmods.tensura.block.SimpleBlock;
import com.github.manasmods.tensura.block.SimpleLeaves;
import com.github.manasmods.tensura.client.TensuraGUIHelper;
import com.github.manasmods.tensura.registry.items.TensuraMaterialItems;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.material.Material.Builder;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RoomEntryBlock extends SimpleBlock {
   public RoomEntryBlock() {
      super(
         new Builder(MaterialColor.NONE).build(),
         properties -> properties.isSuffocating(SimpleLeaves::never)
               .isViewBlocking((blockState, blockGetter, blockPos) -> false)
               .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false)
               .noLootTable()
               .dynamicShape()
               .noOcclusion()
               .sound(SoundType.AMETHYST)
               .strength(-1.0F, 3600000.0F)
      );
   }

   public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
      return Shapes.empty();
   }

   public RenderShape getRenderShape(BlockState pState) {
      return RenderShape.INVISIBLE;
   }

   public void animateTick(BlockState pState, Level pLevel, BlockPos pos, RandomSource pRandom) {
      TensuraGUIHelper.spawnMarkerParticle(pLevel, pState, pos, ((Block)BeyondBlocks.DUNGEON_ENTRY.get()).asItem());
   }

   public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
      Entity entity = pContext instanceof EntityCollisionContext entityCollision ? entityCollision.getEntity() : null;
      if (entity instanceof Projectile) {
         return Shapes.block();
      } else {
         if (!(entity instanceof Player player) || !(player.distanceToSqr((double)pPos.getX(), (double)pPos.getY(), (double)pPos.getZ()) <= 2.25)) {
            return Shapes.empty();
         }

         if (player.isCreative()) {
            return Shapes.empty();
         } else {
            Item entry = ((Block)BeyondBlocks.ROOM_ENTRY.get()).asItem();
            if (player.getCooldowns().isOnCooldown(entry)) {
               return Shapes.empty();
            } else if (player.getBlockZ() > 20) {
               player.getCooldowns().addCooldown(entry, 1200);
               return Shapes.empty();
            } else {
               ItemStack mainHand = player.getMainHandItem();
               if (mainHand.is((Item)TensuraMaterialItems.SILVER_COIN.get()) && mainHand.getCount() >= 1) {
                  mainHand.shrink(1);
                  player.getCooldowns().addCooldown(entry, 1200);
                  player.getLevel().playSound(player, player, SoundEvents.VILLAGER_YES, SoundSource.PLAYERS, 1.0F, 1.0F);
                  return Shapes.empty();
               } else {
                  player.displayClientMessage(Component.translatable("trbeyond.message.not_enough_coins").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                  return Shapes.block();
               }
            }
         }
      }
   }

   public boolean isCollisionShapeFullBlock(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
      return true;
   }

   public boolean canBeReplaced(BlockState pState, Fluid pFluid) {
      return false;
   }

   public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
      return false;
   }

   public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
      return true;
   }

   public boolean collisionExtendsVertically(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
      return true;
   }

   protected void spawnDestroyParticles(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState) {
   }

   public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
      return true;
   }

   public PushReaction getPistonPushReaction(BlockState pState) {
      return PushReaction.IGNORE;
   }
}
