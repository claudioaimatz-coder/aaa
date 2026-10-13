package io.github.Memoires.trbeyond.block;

import io.github.Memoires.trbeyond.TRBeyond;
import io.github.Memoires.trbeyond.block.entity.AnimateDoorBlockEntity;
import io.github.Memoires.trbeyond.network.worldgen.DungeonSaveData;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.network.worldgen.portal.BeyondTeleporterUpdated;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DungeonUnlockerBlock extends Block implements EntityBlock {
   public static final BooleanProperty CAN_GENERATE = BooleanProperty.create("can_generate");
   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
   public static final BooleanProperty ENABLED = BooleanProperty.create("enabled");
   public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 4);
   public static final IntegerProperty FLOOR = IntegerProperty.create("floor", 0, 10);
   public static final IntegerProperty ANIM_STAGE = IntegerProperty.create("anim_stage", 0, 14);
   public static final VoxelShape SHAPE_X = box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);
   public static final VoxelShape SHAPE_Z = box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);

   public DungeonUnlockerBlock() {
      super(Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).noLootTable().isValidSpawn((state, level, pos, type) -> false));
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(CAN_GENERATE, true))
                        .setValue(FACING, Direction.NORTH))
                     .setValue(ENABLED, true))
                  .setValue(ANIM_STAGE, 0))
               .setValue(TYPE, 0))
            .setValue(FLOOR, 0)
      );
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
      if (!BeyondTeleporterUpdated.checkfloor95door(pos, level) && (Integer)state.getValue(TYPE) != 1) {
         int checkDoor = BeyondTeleporterUpdated.checkLobbyOrLastKnownLocationDoor(pos, level);
         if (checkDoor != 0 || (Integer)state.getValue(TYPE) != 0 && (Integer)state.getValue(TYPE) != 4) {
            if (!level.isClientSide()) {
               level.getServer().execute(() -> {
                  BlockPos destination = null;
                  if ((checkDoor == 1 || (Integer)state.getValue(TYPE) == 3) && player.getPersistentData().get("LastKnownLocation") != null) {
                     destination = BlockPos.of(player.getPersistentData().getLong("LastKnownLocation"));
                  } else if (checkDoor == -1 || checkDoor == 1 || (Integer)state.getValue(TYPE) == 2 || (Integer)state.getValue(TYPE) == 3) {
                     destination = DungeonSaveData.getOverworld(level).getLobbyPos();
                  }

                  if (destination != null) {
                     player.getPersistentData().putBoolean("shouldSpawn", true);
                     player.teleportTo((double)destination.getX(), (double)destination.getY(), (double)destination.getZ());
                  }
               });
            }

            return InteractionResult.SUCCESS;
         } else if (!BeyondTeleporterUpdated.checkBossRushDoor(pos, level) && (Integer)state.getValue(TYPE) != 4) {
            if (BeyondTeleporterUpdated.checkDontLetThisDoorOpen(pos)) {
               return InteractionResult.PASS;
            } else {
               ItemStack stack = player.getItemInHand(hand);
               if (level.isClientSide()) {
                  return InteractionResult.SUCCESS;
               } else {
                  if (stack.getItem() == Items.DEBUG_STICK) {
                     level.setBlockAndUpdate(pos, (BlockState)state.setValue(CAN_GENERATE, !(Boolean)state.getValue(CAN_GENERATE)));
                  } else if ((Boolean)state.getValue(CAN_GENERATE)) {
                     generateNext(state, level, pos, player, hitResult);
                  } else {
                     findGenerationBlock(state, level, pos, player, hitResult);
                  }

                  return InteractionResult.sidedSuccess(level.isClientSide());
               }
            }
         } else {
            if (!level.isClientSide()) {
               ServerPlayer serverPlayer = (ServerPlayer)player;
               EntityType<?> bossType = (EntityType<?>)BeyondEntities.EVIL_CENTIPEDE_BOSS.get();
               if (serverPlayer.getStats().getValue(Stats.ENTITY_KILLED.get(bossType)) <= 0) {
                  serverPlayer.sendSystemMessage(Component.translatable("trbeyond.message.boss_rush.no_permission", new Object[]{player.getDisplayName(), bossType.getDescription()}));
                  return InteractionResult.SUCCESS;
               }

               level.getServer().execute(() -> {
                  TRBeyond.getLogger().info("trying to teleport to rush");
                  BlockPos destination = null;
                  destination = DungeonSaveData.getOverworld(level).getBossRushPos();
                  TRBeyond.getLogger().debug("Pos: " + destination);
                  if (destination != null) {
                     player.getPersistentData().putBoolean("shouldSpawn", true);
                     player.teleportTo((double)destination.getX(), (double)destination.getY(), (double)destination.getZ());
                  }
               });
            }

            return InteractionResult.SUCCESS;
         }
      } else {
         if (!level.isClientSide()) {
            player.getPersistentData().putLong("LastKnownLocation", player.getOnPos().above().asLong());
            level.getServer().execute(() -> {
               player.getPersistentData().putBoolean("shouldSpawn", false);
               BlockPos targetPos = DungeonSaveData.getOverworld(level).getFloor95ShopPos();
               player.teleportTo((double)targetPos.getX(), (double)targetPos.getY(), (double)targetPos.getZ());
            });
         }

         return InteractionResult.SUCCESS;
      }
   }

   public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      Direction dir = (Direction)state.getValue(FACING);
      Direction.stream().filter(direction -> direction.getAxis() != dir.getAxis()).forEach(direction -> {
         BlockPos checkPos = pos.offset(direction.getNormal());
         BlockState checkState = level.getBlockState(checkPos);
         if (checkState.hasProperty(CAN_GENERATE)) {
            level.scheduleTick(checkPos, this, 1);
         }
      });
      level.setBlockAndUpdate(pos, (BlockState)state.setValue(ENABLED, false));
      if ((Boolean)state.getValue(CAN_GENERATE)) {
         int value = (Integer)state.getValue(ANIM_STAGE);
         if (value < 14) {
            level.setBlockAndUpdate(pos, (BlockState)state.setValue(ANIM_STAGE, ++value));
            level.scheduleTick(pos, this, 1);
         }
      }

      level.setBlockAndUpdate(pos, (BlockState)state.setValue(ENABLED, false));
      if ((Boolean)state.getValue(CAN_GENERATE)) {
         int value = (Integer)state.getValue(ANIM_STAGE);
         if (value < 14) {
            level.setBlockAndUpdate(pos, (BlockState)state.setValue(ANIM_STAGE, ++value));
            level.scheduleTick(pos, this, 1);
            if (value == 1) {
               this.removeBarrierBlocks(level, pos, (Direction)state.getValue(FACING));
            }
         }
      }
   }

   private void removeBarrierBlocks(ServerLevel level, BlockPos doorPos, Direction facing) {
      level.removeBlock(doorPos.above(1), false);
      level.removeBlock(doorPos.above(2), false);
      level.removeBlock(doorPos.above(3), false);
   }

   private static void generateNext(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
      BeyondDungeon.generateNextPart(state, level, pos, player, hitResult);
   }

   private static void findGenerationBlock(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
      DungeonUnlockerBlock.GenBlockFinder.findGenBlock(state, level, pos, player, hitResult)
         .ifPresent(genPos -> generateNext(level.getBlockState(genPos), level, genPos, player, hitResult));
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return (BlockState)this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
   }

   public BlockState rotate(BlockState state, Rotation rot) {
      return (BlockState)state.setValue(FACING, rot.rotate((Direction)state.getValue(FACING)));
   }

   public BlockState mirror(BlockState state, Mirror mirror) {
      return state.rotate(mirror.getRotation((Direction)state.getValue(FACING)));
   }

   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return state.getValue(ENABLED) ? (((Direction)state.getValue(FACING)).getAxis() == Axis.X ? SHAPE_X : SHAPE_Z) : Shapes.empty();
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{CAN_GENERATE, ENABLED, FACING, ANIM_STAGE, TYPE, FLOOR});
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new AnimateDoorBlockEntity(pos, state);
   }

   public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
      if (pState.getBlock() instanceof DungeonUnlockerBlock block && (Boolean)pState.getValue(CAN_GENERATE)) {
         BlockState newBlockState = (BlockState)pState.setValue(CAN_GENERATE, false);
         int floor = (Integer)pState.getValue(FLOOR);
         if (floor == 0) {
            pLevel.setBlock(pPos.below(1), newBlockState, 3);
            pLevel.setBlock(pPos.above(1), newBlockState, 3);
            pLevel.setBlock(pPos.above(2), newBlockState, 3);
            pLevel.setBlock(pPos.above(3), newBlockState, 3);
            if (pState.getValue(FACING) == Direction.EAST || pState.getValue(FACING) == Direction.WEST) {
               pLevel.setBlock(pPos.north(), newBlockState, 3);
               pLevel.setBlock(pPos.above().north(), newBlockState, 3);
               pLevel.setBlock(pPos.below().north(), newBlockState, 3);
               pLevel.setBlock(pPos.south(), newBlockState, 3);
               pLevel.setBlock(pPos.above().south(), newBlockState, 3);
               pLevel.setBlock(pPos.below().south(), newBlockState, 3);
            }

            if (pState.getValue(FACING) == Direction.NORTH || pState.getValue(FACING) == Direction.SOUTH) {
               pLevel.setBlock(pPos.east(), newBlockState, 3);
               pLevel.setBlock(pPos.above(1).east(), newBlockState, 3);
               pLevel.setBlock(pPos.above(2).east(), newBlockState, 3);
               pLevel.setBlock(pPos.below().east(), newBlockState, 3);
               pLevel.setBlock(pPos.west(), newBlockState, 3);
               pLevel.setBlock(pPos.above(1).west(), newBlockState, 3);
               pLevel.setBlock(pPos.above(2).west(), newBlockState, 3);
               pLevel.setBlock(pPos.below().west(), newBlockState, 3);
            }
         } else {
            Direction dir = (Direction)pState.getValue(FACING);

            for (int x = -2; x < 3; x++) {
               for (int y = -1; y < 5; y++) {
                  if (x != 0 || y != 0) {
                     pLevel.setBlock(pPos.relative(dir.getClockWise(), x).above(y), newBlockState, 3);
                  }
               }
            }
         }
      }

      super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.INVISIBLE;
   }

   private static class GenBlockFinder {
      private static final int MAX_LOOKUP_RADIUS = 5;

      public static Optional<BlockPos> findGenBlock(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
         Direction dir = (Direction)state.getValue(DungeonUnlockerBlock.FACING);

         for (int x = -5; x < 5; x++) {
            for (int y = -5; y < 5; y++) {
               BlockPos checkPos = pos.offset(dir.getStepZ() * x, y, dir.getStepX() * x);
               BlockState checkState = level.getBlockState(checkPos);
               if (checkState.hasProperty(DungeonUnlockerBlock.CAN_GENERATE) && (Boolean)checkState.getValue(DungeonUnlockerBlock.CAN_GENERATE)) {
                  return Optional.of(checkPos);
               }
            }
         }

         return Optional.empty();
      }
   }
}
