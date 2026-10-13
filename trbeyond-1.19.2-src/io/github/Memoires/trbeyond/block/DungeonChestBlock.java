package io.github.Memoires.trbeyond.block;

import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import io.github.Memoires.trbeyond.block.entity.DungeonChestBlockEntity;
import io.github.Memoires.trbeyond.block.entity.GoldChestBlockEntity;
import io.github.Memoires.trbeyond.block.entity.SilverChestBlockEntity;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.entity.misc.MimicChestEntity;
import io.github.Memoires.trbeyond.entity.misc.PrimedDungeonChestEntity;
import io.github.Memoires.trbeyond.network.worldgen.DungeonSaveData;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import io.github.Memoires.trbeyond.util.BeyondDamageSources;
import io.github.Memoires.trbeyond.util.TaskScheduler;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner.BlockType;
import net.minecraft.world.level.block.DoubleBlockCombiner.Combiner;
import net.minecraft.world.level.block.DoubleBlockCombiner.NeighborCombineResult;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class DungeonChestBlock extends BaseEntityBlock implements SimpleWaterloggedBlock, Fallable {
   public static final EnumProperty<ChestType> TYPE = BlockStateProperties.CHEST_TYPE;
   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   protected final Supplier<BlockEntityType<? extends DungeonChestBlockEntity>> blockEntityType;
   private final DungeonChestBlock.DungeonChestType type;

   public DungeonChestBlock(
      Properties properties, Supplier<BlockEntityType<? extends DungeonChestBlockEntity>> blockEntityType, DungeonChestBlock.DungeonChestType type
   ) {
      super(properties);
      this.blockEntityType = blockEntityType;
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(TYPE, ChestType.LEFT))
            .setValue(WATERLOGGED, Boolean.FALSE)
      );
      this.type = type;
   }

   public static BlockType getBlockType(BlockState blockState) {
      ChestType chesttype = (ChestType)blockState.getValue(TYPE);
      return chesttype == ChestType.RIGHT ? BlockType.FIRST : BlockType.SECOND;
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   @Deprecated
   public BlockState updateShape(
      BlockState state, Direction direction, BlockState facingState, LevelAccessor levelAccessor, BlockPos currentPos, BlockPos facingPos
   ) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         levelAccessor.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
      }

      if (facingState.is(this) && direction.getAxis().isHorizontal()) {
         ChestType chesttype = (ChestType)facingState.getValue(TYPE);
         if (state.getValue(TYPE) == ChestType.SINGLE
            && chesttype != ChestType.SINGLE
            && state.getValue(FACING) == facingState.getValue(FACING)
            && getConnectedDirection(facingState) == direction.getOpposite()) {
            return (BlockState)state.setValue(TYPE, chesttype.getOpposite());
         }
      } else if (getConnectedDirection(state) == direction) {
         return (BlockState)state.setValue(TYPE, ChestType.SINGLE);
      }

      return super.updateShape(state, direction, facingState, levelAccessor, currentPos, facingPos);
   }

   @Deprecated
   public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
      return switch (getConnectedDirection(blockState)) {
         case SOUTH -> Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 16.0);
         case WEST -> Block.box(0.0, 0.0, 1.0, 15.0, 14.0, 15.0);
         case EAST -> Block.box(1.0, 0.0, 1.0, 16.0, 14.0, 15.0);
         default -> Block.box(1.0, 0.0, 0.0, 15.0, 14.0, 15.0);
      };
   }

   public static Direction getConnectedDirection(BlockState state) {
      Direction direction = (Direction)state.getValue(FACING);
      return state.getValue(TYPE) == ChestType.LEFT ? direction.getClockWise() : direction.getCounterClockWise();
   }

   public BlockState getStateForPlacement(BlockPlaceContext pContext) {
      Level level = pContext.getLevel();
      BlockPos blockpos = pContext.getClickedPos();
      Direction direction = pContext.getHorizontalDirection().getOpposite();
      BlockPos other = blockpos.relative(direction.getClockWise());
      return level.getWorldBorder().isWithinBounds(other) && level.getBlockState(other).canBeReplaced(pContext)
         ? (BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, direction)).setValue(TYPE, ChestType.LEFT))
            .setValue(WATERLOGGED, pContext.getLevel().getFluidState(blockpos).getType() == Fluids.WATER)
         : null;
   }

   public FluidState getFluidState(BlockState blockState) {
      return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
   }

   public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
      if (pLevel.getBlockEntity(pPos) instanceof DungeonChestBlockEntity entity) {
         entity.wasPlaced(livingEntity, itemStack);
         if (itemStack.hasCustomHoverName()) {
            entity.setCustomName(itemStack.getHoverName());
         }
      }

      if (!pLevel.isClientSide()) {
         BlockPos blockpos = pPos.relative(getConnectedDirection(pState));
         pLevel.setBlock(
            blockpos,
            (BlockState)((BlockState)pState.setValue(TYPE, ((ChestType)pState.getValue(TYPE)).getOpposite()))
               .setValue(WATERLOGGED, pLevel.getFluidState(blockpos).is(Fluids.WATER)),
            3
         );
         pLevel.blockUpdated(pPos, Blocks.AIR);
         pState.updateNeighbourShapes(pLevel, pPos, 3);
      }
   }

   public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean isMoving) {
      if (!blockState.is(newState.getBlock())) {
         BlockEntity blockentity = level.getBlockEntity(blockPos);
         if (blockentity instanceof Container) {
            Containers.dropContents(level, blockPos, (Container)blockentity);
            level.updateNeighbourForOutputSignal(blockPos, this);
         }

         super.onRemove(blockState, level, blockPos, newState, isMoving);
      }
   }

   public void onLand(Level pLevel, BlockPos pPos, BlockState pState, BlockState pReplaceableState, FallingBlockEntity pFallingBlock) {
      if (!pLevel.isClientSide()) {
         BlockPos blockpos = pPos.relative(getConnectedDirection(pState));
         pLevel.setBlock(
            blockpos,
            (BlockState)((BlockState)pState.setValue(TYPE, ((ChestType)pState.getValue(TYPE)).getOpposite()))
               .setValue(WATERLOGGED, pLevel.getFluidState(blockpos).is(Fluids.WATER)),
            3
         );
         pLevel.blockUpdated(pPos, Blocks.AIR);
         pState.updateNeighbourShapes(pLevel, pPos, 3);
      }
   }

   public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
      super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
      if (!pLevel.isClientSide()) {
         BlockPos blockpos = pPos.relative(getConnectedDirection(pState));
         pLevel.destroyBlock(blockpos, false, pPlayer);
      }
   }

   public InteractionResult use(
      BlockState blockState, Level pLevel, BlockPos pPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult
   ) {
      if (pLevel.isClientSide) {
         return InteractionResult.SUCCESS;
      } else {
         switch (this.getType()) {
            case DEFAULT: {
               long packedTime = System.currentTimeMillis() / 1000L / 60L;
               MenuProvider menuProvider = this.getMenuProvider(blockState, pLevel, pPos);
               if (menuProvider == null || !(pLevel.getBlockEntity(pPos) instanceof DungeonChestBlockEntity chest)) {
                  break;
               }

               BlockEntity blockEntity = pLevel.getBlockEntity(pPos.relative(getConnectedDirection(blockState)));
               BlockEntity challengeChest = (BlockEntity)(chest.getPersistentData().contains("challengeChest")
                  ? chest
                  : (blockEntity != null && blockEntity.getPersistentData().contains("challengeChest") ? blockEntity : null));
               BlockPos shrinePos;
               if (challengeChest != null
                  && challengeChest.getPersistentData().contains("shrinePos")
                  && pLevel.getBlockState(shrinePos = BlockPos.of(challengeChest.getPersistentData().getLong("shrinePos"))).getBlock() instanceof ShrineBlock shrine
                  )
                {
                  challengeChest.getPersistentData().remove("challengeChest");
                  if (challengeChest.getPersistentData().contains("incorrectChest")) {
                     shrine.pickChestsWrongChest(pLevel, shrinePos);
                  } else {
                     shrine.pickChestsCorrectChest();
                  }
               }

               if (blockEntity == null) {
                  return InteractionResult.CONSUME;
               }

               boolean noLock = chest.getPersistentData().contains("noLock") || blockEntity.getPersistentData().contains("noLock");
               if ((Boolean)ServerConfig.INSTANCE.dungeonConfig.chestLock.get()
                  && (
                     !DungeonSaveData.getOverworld(pLevel).canOpenChest(player, pPos, packedTime)
                        || !DungeonSaveData.getOverworld(pLevel).canOpenChest(player, pPos.relative(getConnectedDirection(blockState)), packedTime) && !noLock
                  )) {
                  return InteractionResult.CONSUME;
               }

               if (!noLock) {
                  DungeonSaveData.getOverworld(pLevel).setChestCooldown(player, pPos, packedTime);
                  CompoundTag tag = chest.getPersistentData();
                  int openedTime;
                  if (tag.contains("openedTime")) {
                     openedTime = tag.getInt("openedTime");
                  } else {
                     openedTime = -(Integer)ServerConfig.INSTANCE.dungeonConfig.chestLootTime.get();
                  }

                  if (packedTime >= (long)(openedTime + (Integer)ServerConfig.INSTANCE.dungeonConfig.chestLootTime.get())) {
                     tag.putInt("openedTime", (int)packedTime);
                     ResourceLocation lootTable;
                     if (chest instanceof GoldChestBlockEntity) {
                        lootTable = ResourceLocation.of((String)ServerConfig.INSTANCE.dungeonConfig.goldChestLoot.get(), ':');
                     } else if (chest instanceof SilverChestBlockEntity) {
                        lootTable = ResourceLocation.of((String)ServerConfig.INSTANCE.dungeonConfig.silverChestLoot.get(), ':');
                     } else {
                        lootTable = ResourceLocation.of((String)ServerConfig.INSTANCE.dungeonConfig.bronzeChestLoot.get(), ':');
                     }

                     if ((Boolean)ServerConfig.INSTANCE.dungeonConfig.perPlayerLoot.get()) {
                        chest.playersWithGeneratedLoot.clear();
                     }

                     for (int i = 0; i < chest.getContainerSize(); i++) {
                        chest.setItem(i, ItemStack.EMPTY);
                        if (pLevel.getBlockEntity(pPos.relative(getConnectedDirection(blockState))) instanceof DungeonChestBlockEntity connected) {
                           connected.setItem(i, ItemStack.EMPTY);
                        }
                     }

                     if ((Boolean)ServerConfig.INSTANCE.dungeonConfig.perPlayerLoot.get()) {
                        chest.setLootTable(lootTable);
                     } else {
                        chest.setLootTable(lootTable, pLevel.getRandom().nextLong());
                     }

                     chest.unpackLootTable(player);
                  }

                  if (pLevel.getBlockEntity(pPos.relative(getConnectedDirection(blockState))) instanceof DungeonChestBlockEntity connected) {
                     CompoundTag otherTag = connected.getPersistentData();
                     otherTag.putInt("openedTime", (int)packedTime);
                  }
               }

               player.openMenu(menuProvider);
               player.awardStat(this.getOpenChestStat());
               BlockState state = pLevel.getBlockState(pPos);
               ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(state.getBlock());
               if (blockId != null && blockId.equals(new ResourceLocation("trbeyond", "gold_chest")) && player instanceof ServerPlayer serverPlayer) {
                  MinecraftServer server = serverPlayer.getServer();
                  if (server != null) {
                     ResourceLocation advId = new ResourceLocation("trbeyond", "loot");
                     Advancement advancement = server.m_129889_().getAdvancement(advId);
                     if (advancement != null) {
                        AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);

                        for (String criterion : progress.getRemainingCriteria()) {
                           serverPlayer.getAdvancements().award(advancement, criterion);
                        }
                     }
                  }
               }
               break;
            }
            case TNT: {
               PrimedDungeonChestEntity chest = new PrimedDungeonChestEntity(
                  pLevel, (double)pPos.getX() + 0.5, (double)pPos.getY(), (double)pPos.getZ() + 0.5
               );
               chest.setSourceItem(this.asItem().getDefaultInstance());
               chest.setSpawnDirection((Direction)blockState.getValue(FACING));
               pLevel.addFreshEntity(chest);
               TensuraParticleHelper.addServerParticlesAroundSelf(chest, ParticleTypes.LARGE_SMOKE);
               TensuraParticleHelper.addServerParticlesAroundSelf(chest, ParticleTypes.SMOKE);
               pLevel.playSound(null, chest.getX(), chest.getY(), chest.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
               BlockPos relative = pPos.relative(getConnectedDirection(blockState));
               pLevel.removeBlock(pPos, false);
               pLevel.removeBlock(pPos.relative(getConnectedDirection(blockState)), false);
               TaskScheduler.scheduleTask(
                  new TaskScheduler.ScheduledTask(
                     6000,
                     () -> {
                        pLevel.setBlock(pPos, blockState, 3);
                        pLevel.setBlock(
                           relative,
                           (BlockState)((BlockState)blockState.setValue(TYPE, ((ChestType)blockState.getValue(TYPE)).getOpposite()))
                              .setValue(WATERLOGGED, pLevel.getFluidState(relative).is(Fluids.WATER)),
                           3
                        );
                     }
                  )
               );
               break;
            }
            case PARALYSIS: {
               BlockPos relative = pPos.relative(getConnectedDirection(blockState));
               Vec3 center = new Vec3(
                  (double)(relative.getX() + pPos.getX()) / 2.0 + 0.5,
                  (double)pPos.getY() + 1.25,
                  (double)(relative.getZ() + pPos.getZ()) / 2.0 + 0.5
               );
               AABB box = new AABB(center.add(-0.5, -0.5, -0.5), center.add(0.5, 0.5, 0.5));
               List<LivingEntity> list = pLevel.getEntitiesOfClass(LivingEntity.class, box.inflate(9.0));
               if (!list.isEmpty()) {
                  for (LivingEntity target : list) {
                     if (!(target instanceof Player pPlayer) || !pPlayer.getAbilities().invulnerable) {
                        target.hurt(BeyondDamageSources.PARALYSIS, 4.0F);
                        target.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.PARALYSIS.get(), 200, 4));
                        TensuraParticleHelper.addServerParticlesAroundSelf(target, (ParticleOptions)TensuraParticles.YELLOW_LIGHTNING_SPARK.get(), 2.0);
                     }
                  }
               }

               TensuraParticleHelper.addServerParticlesAroundPos(pLevel.getRandom(), pLevel, center, (ParticleOptions)TensuraParticles.SOLAR_FLASH.get(), 1.0);
               TensuraParticleHelper.spawnServerParticles(
                  player.level,
                  (ParticleOptions)TensuraParticles.YELLOW_LIGHTNING_SPARK.get(),
                  center.x(),
                  center.y(),
                  center.z(),
                  60,
                  0.08,
                  0.08,
                  0.08,
                  0.5,
                  true
               );
               player.getLevel().playSound(null, center.x(), center.y(), center.z(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 3.0F, 1.0F);
               break;
            }
            case MIMIC:
               spawnMimic(pLevel, pPos, player, blockState);

               for (Entity entity : pLevel.getEntities(player, player.getBoundingBox().inflate(10.0))) {
                  if (entity instanceof MimicChestEntity mimic) {
                     mimic.setTarget(player);
                  }
               }

               for (int x = -10; x < 10; x++) {
                  for (int y = -10; y < 10; y++) {
                     for (int z = -10; z < 10; z++) {
                        Block blockEntity = pLevel.getBlockState(pPos.offset(x, y, z)).getBlock();
                        if (blockEntity instanceof DungeonChestBlock) {
                           DungeonChestBlock chestx = (DungeonChestBlock)blockEntity;
                           if (chestx.getType() == DungeonChestBlock.DungeonChestType.MIMIC) {
                              spawnMimic(pLevel, pPos.offset(x, y, z), player, pLevel.getBlockState(pPos.offset(x, y, z)));
                           }
                        }
                     }
                  }
               }
         }

         return InteractionResult.CONSUME;
      }
   }

   public static void spawnMimic(Level level, BlockPos blockPos, Player player, BlockState blockState) {
      EntityType<MimicChestEntity> entityType;
      if (level.getBlockEntity(blockPos) instanceof GoldChestBlockEntity) {
         entityType = (EntityType<MimicChestEntity>)BeyondEntities.GOLD_MIMIC_CHEST.get();
      } else {
         entityType = (EntityType<MimicChestEntity>)BeyondEntities.SILVER_MIMIC_CHEST.get();
      }

      MimicChestEntity mimic = new MimicChestEntity(entityType, level, true);
      if (blockState.getValue(FACING) == Direction.NORTH) {
         mimic.setPos((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ() + 0.5);
         mimic.rotate(Rotation.CLOCKWISE_180);
      } else if (blockState.getValue(FACING) == Direction.SOUTH) {
         mimic.setPos((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ() + 0.5);
      } else if (blockState.getValue(FACING) == Direction.EAST) {
         mimic.setPos((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ());
         mimic.rotate(Rotation.COUNTERCLOCKWISE_90);
      } else if (blockState.getValue(FACING) == Direction.WEST) {
         mimic.setPos((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ());
         mimic.rotate(Rotation.CLOCKWISE_90);
      }

      mimic.getPersistentData().putBoolean("respawn", false);
      level.addFreshEntity(mimic);
      mimic.setTarget(player);
      TaskScheduler.scheduleTask(
         new TaskScheduler.ScheduledTask(
            6000,
            () -> {
               level.setBlock(blockPos, blockState, 3);
               level.setBlock(
                  blockPos.relative(getConnectedDirection(blockState)),
                  (BlockState)((BlockState)blockState.setValue(TYPE, ((ChestType)blockState.getValue(TYPE)).getOpposite()))
                     .setValue(WATERLOGGED, level.getFluidState(blockPos.relative(getConnectedDirection(blockState))).is(Fluids.WATER)),
                  3
               );
            }
         )
      );
      level.removeBlock(blockPos.relative(getConnectedDirection(blockState)), false);
      level.removeBlock(blockPos, false);
   }

   protected Stat<ResourceLocation> getOpenChestStat() {
      return Stats.CUSTOM.get(Stats.OPEN_CHEST);
   }

   public BlockEntityType<? extends DungeonChestBlockEntity> blockEntityType() {
      return this.blockEntityType.get();
   }

   @Nullable
   public static Container getContainer(DungeonChestBlock chestBlock, BlockState blockState, Level level, BlockPos blockPos, boolean ignoreBlockedChest) {
      return (Container)((Optional)chestBlock.combine(blockState, level, blockPos, ignoreBlockedChest).apply(chestCombiner())).orElse(null);
   }

   public NeighborCombineResult<? extends DungeonChestBlockEntity> combine(BlockState blockState, Level level, BlockPos blockPos, boolean ignoreBlockedChest) {
      BiPredicate<LevelAccessor, BlockPos> biPredicate;
      if (ignoreBlockedChest) {
         biPredicate = (levelAccessor, blockPos1) -> false;
      } else {
         biPredicate = DungeonChestBlock::isChestBlockedAt;
      }

      return DoubleBlockCombiner.combineWithNeigbour(
         this.blockEntityType.get(),
         DungeonChestBlock::getBlockType,
         DungeonChestBlock::getConnectedDirection,
         FACING,
         blockState,
         level,
         blockPos,
         biPredicate
      );
   }

   @Nullable
   public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
      return (MenuProvider)((Optional)this.combine(blockState, level, blockPos, false).apply(menuCombiner())).orElse(null);
   }

   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
      return createTickerHelper(blockEntityType, this.blockEntityType(), DungeonChestBlockEntity::tick);
   }

   public static boolean isChestBlockedAt(LevelAccessor levelAccessor, BlockPos blockPos) {
      return isBlockedChestByBlock(levelAccessor, blockPos);
   }

   private static boolean isBlockedChestByBlock(BlockGetter blockGetter, BlockPos blockPos) {
      BlockPos above = blockPos.above();
      return blockGetter.getBlockState(above).isRedstoneConductor(blockGetter, above);
   }

   public boolean hasAnalogOutputSignal(BlockState blockState) {
      return true;
   }

   public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
      return AbstractContainerMenu.getRedstoneSignalFromContainer(getContainer(this, blockState, level, blockPos, false));
   }

   public BlockState rotate(BlockState blockState, Rotation rotation) {
      return (BlockState)blockState.setValue(FACING, rotation.rotate((Direction)blockState.getValue(FACING)));
   }

   public BlockState mirror(BlockState blockState, Mirror mirror) {
      return blockState.rotate(mirror.getRotation((Direction)blockState.getValue(FACING)));
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> blockBlockStateBuilder) {
      blockBlockStateBuilder.add(new Property[]{FACING, WATERLOGGED, TYPE});
   }

   public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
      return false;
   }

   public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
      if (serverLevel.getBlockEntity(blockPos) instanceof DungeonChestBlockEntity entity) {
         entity.recheckOpen();
      }
   }

   public static Combiner<DungeonChestBlockEntity, Float2FloatFunction> opennessCombiner(final LidBlockEntity lidBlockEntity) {
      return new Combiner<DungeonChestBlockEntity, Float2FloatFunction>() {
         public Float2FloatFunction acceptDouble(DungeonChestBlockEntity blockEntityOne, DungeonChestBlockEntity blockEntityTwo) {
            return lidBlockEntityxx -> Math.max(blockEntityOne.getOpenNess(lidBlockEntityxx), blockEntityTwo.getOpenNess(lidBlockEntityxx));
         }

         public Float2FloatFunction acceptSingle(DungeonChestBlockEntity blockEntity) {
            return blockEntity::getOpenNess;
         }

         public Float2FloatFunction acceptNone() {
            return lidBlockEntity::getOpenNess;
         }
      };
   }

   public static Combiner<DungeonChestBlockEntity, Optional<Container>> chestCombiner() {
      return new Combiner<DungeonChestBlockEntity, Optional<Container>>() {
         public Optional<Container> acceptDouble(DungeonChestBlockEntity entity, DungeonChestBlockEntity entity2) {
            return Optional.of(new CompoundContainer(entity, entity2));
         }

         public Optional<Container> acceptSingle(DungeonChestBlockEntity entity) {
            return Optional.of(entity);
         }

         public Optional<Container> acceptNone() {
            return Optional.empty();
         }
      };
   }

   public static Combiner<DungeonChestBlockEntity, Optional<MenuProvider>> menuCombiner() {
      return new Combiner<DungeonChestBlockEntity, Optional<MenuProvider>>() {
         public Optional<MenuProvider> acceptDouble(final DungeonChestBlockEntity entity, final DungeonChestBlockEntity entity2) {
            final Container container = new CompoundContainer(entity, entity2);
            return Optional.of(new MenuProvider() {
               @Nullable
               public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                  if (entity.canOpen(player) && entity2.canOpen(player)) {
                     entity.unpackLootTable(inventory.player);
                     entity2.unpackLootTable(inventory.player);
                     return ChestMenu.sixRows(i, inventory, container);
                  } else {
                     return null;
                  }
               }

               public Component getDisplayName() {
                  if (entity.hasCustomName()) {
                     return entity.getDisplayName();
                  } else {
                     return entity2.hasCustomName() ? entity2.getDisplayName() : entity.getName();
                  }
               }
            });
         }

         public Optional<MenuProvider> acceptSingle(DungeonChestBlockEntity entity) {
            return Optional.of(entity);
         }

         public Optional<MenuProvider> acceptNone() {
            return Optional.empty();
         }
      };
   }

   public DungeonChestBlock.DungeonChestType getType() {
      return this.type;
   }

   public static enum DungeonChestType {
      DEFAULT,
      TNT,
      PARALYSIS,
      MIMIC;
   }
}
