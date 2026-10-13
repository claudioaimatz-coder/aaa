package io.github.Memoires.trbeyond.block;

import com.github.manasmods.tensura.ability.SkillHelper;
import io.github.Memoires.trbeyond.capability.dungeon.DungeonCapability;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.network.worldgen.DungeonSaveData;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class DungeonGateTeleporterBlock extends Block {
   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
   private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
   private static final VoxelShape COLLIDE = Block.box(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

   public DungeonGateTeleporterBlock(Properties props) {
      super(props);
   }

   @NotNull
   public VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
      return COLLIDE;
   }

   @NotNull
   public VoxelShape getVisualShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
      return SHAPE;
   }

   @NotNull
   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.INVISIBLE;
   }

   public BlockState getStateForPlacement(BlockPlaceContext pContext) {
      return (BlockState)this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
   }

   @NotNull
   public BlockState rotate(BlockState pState, Rotation pRotation) {
      return (BlockState)pState.setValue(FACING, pRotation.rotate((Direction)pState.getValue(FACING)));
   }

   @NotNull
   public BlockState mirror(BlockState pState, Mirror pMirror) {
      return pState.rotate(pMirror.getRotation((Direction)pState.getValue(FACING)));
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING});
   }

   public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
      if (entity instanceof ServerPlayer player && level instanceof ServerLevel sLevel) {
         if (!level.isClientSide && entity.isAlive()) {
            if (level.dimension() == BeyondDungeon.DUNGEON_LEVEL_KEY) {
               level.getServer()
                  .execute(
                     () -> DungeonCapability.getFrom(player)
                           .ifPresent(
                              cap -> {
                                 BlockPos overworldCoords = cap.getOverworldCoords();
                                 SkillHelper.moveAcrossDimensionTo(
                                    player,
                                    (double)overworldCoords.getX(),
                                    (double)overworldCoords.getY(),
                                    (double)overworldCoords.getZ(),
                                    player.getYRot(),
                                    player.getXRot(),
                                    sLevel.getServer().m_129783_()
                                 );
                              }
                           )
                  );
            } else {
               BlockPos savePos = pos.offset(2, 0, 2);
               if (state.getValue(FACING) == Direction.NORTH) {
                  savePos = pos.offset(0, 0, -2);
               } else if (state.getValue(FACING) == Direction.SOUTH) {
                  savePos = pos.offset(0, 0, 2);
               } else if (state.getValue(FACING) == Direction.EAST) {
                  savePos = pos.offset(2, 0, 0);
               } else if (state.getValue(FACING) == Direction.WEST) {
                  savePos = pos.offset(-2, 0, 0);
               }

               ServerLevel dungeonLevel = level.getServer().m_129880_(BeyondDungeon.DUNGEON_LEVEL_KEY);
               if (dungeonLevel != null
                  && dungeonLevel.players().size() >= (Integer)ServerConfig.INSTANCE.dungeonConfig.playerLimit.get()
                  && (Integer)ServerConfig.INSTANCE.dungeonConfig.playerLimit.get() != 0) {
                  player.displayClientMessage(Component.translatable("trbeyond.message.dungeon_full"), true);
                  player.teleportTo((double)savePos.getX() + 0.5, (double)savePos.getY(), (double)savePos.getZ() + 0.5);
                  return;
               }

               if ((Long)ServerConfig.INSTANCE.dungeonConfig.enterCooldown.get() > 0L) {
                  Long cooldown = (Long)ServerConfig.INSTANCE.dungeonConfig.enterCooldown.get();
                  if (player.getPersistentData().getLong("dungeonEnterCooldown") + cooldown >= System.currentTimeMillis() / 1000L) {
                     player.displayClientMessage(Component.translatable("trbeyond.message.dungeon_enter_cooldown", new Object[]{cooldown}), true);
                     player.teleportTo((double)savePos.getX() + 0.5, (double)savePos.getY(), (double)savePos.getZ() + 0.5);
                     return;
                  }
               }

               DungeonCapability.setOverworldCoords(player, savePos);
               DungeonCapability.sync(player);
               DungeonSaveData.getOverworld(sLevel).addTpToDungeonPlayer(player.getUUID());
            }

            return;
         }

         return;
      }

      if (entity instanceof LivingEntity livingEntity && level instanceof ServerLevel sLevel && livingEntity instanceof TamableAnimal tame) {
         if (tame.getOwner() == null) {
            return;
         }

         if (!level.isClientSide && entity.isAlive()) {
            if (level.dimension() == BeyondDungeon.DUNGEON_LEVEL_KEY) {
               level.getServer()
                  .execute(
                     () -> DungeonCapability.getFrom(tame.getOwner())
                           .ifPresent(
                              cap -> {
                                 BlockPos overworldCoords = cap.getOverworldCoords();
                                 tame.getPersistentData().putLong("dungeonEnterCooldown", System.currentTimeMillis() / 1000L);
                                 SkillHelper.moveAcrossDimensionTo(
                                    tame,
                                    (double)overworldCoords.getX(),
                                    (double)overworldCoords.getY(),
                                    (double)overworldCoords.getZ(),
                                    tame.getYRot(),
                                    tame.getXRot(),
                                    sLevel.getServer().m_129783_()
                                 );
                              }
                           )
                  );
            } else {
               ServerLevel dungeonLevelx = tame.getServer().m_129880_(BeyondDungeon.DUNGEON_LEVEL_KEY);
               if (dungeonLevelx != null) {
                  SkillHelper.moveAcrossDimensionTo(tame, 8.0, 116.0, -7.0, tame.getYRot(), tame.getXRot(), dungeonLevelx);
               }
            }
         }
      }
   }
}
