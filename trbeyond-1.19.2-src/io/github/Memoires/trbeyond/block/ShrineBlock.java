package io.github.Memoires.trbeyond.block;

import com.github.manasmods.tensura.config.TensuraConfig;
import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import com.github.manasmods.tensura.entity.BarghestEntity;
import com.github.manasmods.tensura.entity.DirewolfEntity;
import com.github.manasmods.tensura.entity.GiantAntEntity;
import com.github.manasmods.tensura.entity.GiantBatEntity;
import com.github.manasmods.tensura.entity.HoundDogEntity;
import com.github.manasmods.tensura.entity.LeechLizardEntity;
import com.github.manasmods.tensura.entity.MetalSlimeEntity;
import com.github.manasmods.tensura.entity.template.HumanoidNPCEntity;
import com.github.manasmods.tensura.item.armor.SimpleArmorItem;
import com.github.manasmods.tensura.item.templates.SimpleBowItem;
import com.github.manasmods.tensura.item.templates.SimpleSwordItem;
import com.github.manasmods.tensura.item.templates.custom.TensuraSword;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import com.github.manasmods.tensura.registry.items.TensuraArmorItems;
import com.github.manasmods.tensura.registry.items.TensuraMaterialItems;
import com.github.manasmods.tensura.registry.items.TensuraToolItems;
import io.github.Memoires.trbeyond.block.entity.DungeonChestBlockEntity;
import io.github.Memoires.trbeyond.block.entity.ShrineBlockEntity;
import io.github.Memoires.trbeyond.capability.dungeon.DungeonCapability;
import io.github.Memoires.trbeyond.capability.dungeon.IDungeonCapability;
import io.github.Memoires.trbeyond.capability.player.IBeyondPlayerCapability;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.entity.custom.MinhSlimeEntity;
import io.github.Memoires.trbeyond.entity.custom.ShrineEntity;
import io.github.Memoires.trbeyond.handlers.ChallengeEventHandler;
import io.github.Memoires.trbeyond.network.worldgen.DungeonSaveData;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlocks;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import io.github.Memoires.trbeyond.util.ChallengeType;
import io.github.Memoires.trbeyond.util.TaskScheduler;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.util.random.WeightedEntry.Wrapper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.item.GeoArmorItem;

public class ShrineBlock extends BaseEntityBlock {
   public final ShrineBlock.DungeonShrineTier tier;
   private final WeightedRandomList<Wrapper<String>> challengeRandomizer;
   protected final Supplier<BlockEntityType<? extends ShrineBlockEntity>> blockEntityType;
   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
   private ShrineBlock.DungeonShrineTier currentChestChallengeTier;
   private int chestsLeft;
   private int mobsLeft;
   private int currentWave;
   private int finalWave;
   private long timer = Long.MAX_VALUE;
   private boolean dontDecrementNextFailure = false;
   private boolean challengeCausesCooldown = true;

   public ShrineBlock(Properties properties, Supplier<BlockEntityType<? extends ShrineBlockEntity>> blockEntityType, ShrineBlock.DungeonShrineTier tier) {
      super(properties);
      this.blockEntityType = blockEntityType;
      this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH));
      this.tier = tier;
      List<Wrapper<String>> challenges = new ArrayList<>();
      challenges.add(WeightedEntry.wrap("waves", 10));
      challenges.add(WeightedEntry.wrap("player_waves", 10));
      challenges.add(WeightedEntry.wrap("destroy_shrine", 10));
      challenges.add(WeightedEntry.wrap("pick_the_correct_chest", 2));
      if (tier == ShrineBlock.DungeonShrineTier.STELLAR || tier == ShrineBlock.DungeonShrineTier.GOLD) {
         challenges.add(WeightedEntry.wrap("defeat_boss", 5));
      }

      if (ShrineBlock.DungeonShrineTier.STELLAR == tier) {
         challenges.add(WeightedEntry.wrap("minh_slimes", 1));
      }

      this.challengeRandomizer = WeightedRandomList.create(challenges);
   }

   public void tick(BlockState pState, Level pLevel, BlockPos pPos) {
      BlockEntity shrineBlockEntity = pLevel.getBlockEntity(pPos);
      if (shrineBlockEntity != null) {
         if (shrineBlockEntity.getPersistentData().getString("challengeState").equals("active")) {
            String currentChallenge = shrineBlockEntity.getPersistentData().getString("currentChallenge");
            Player player = null;

            try {
               player = pLevel.getPlayerByUUID(UUID.fromString(shrineBlockEntity.getPersistentData().getString("currentPlayer")));
            } catch (IllegalArgumentException var11) {
               for (String key : shrineBlockEntity.getPersistentData().getAllKeys()) {
                  shrineBlockEntity.getPersistentData().remove(key);
               }
            }

            if (player != null) {
               switch (currentChallenge) {
                  case "pick_the_correct_chest":
                     if (this.currentChestChallengeTier == null) {
                        shrineBlockEntity.getPersistentData().putString("challengeState", "completed");
                        return;
                     }

                     if (this.timer < pLevel.getGameTime()) {
                        this.pickChestsWrongChest(pLevel, pPos);
                     }

                     if (this.chestsLeft == 0) {
                        if (this.currentChestChallengeTier.ordinal() < this.tier.ordinal()) {
                           if (player.getPersistentData().contains("challenge_time")) {
                              player.getPersistentData().putInt("challenge_time", 0);
                           }

                           this.pickChestsCleanupChests(pLevel, pPos);
                           this.pickChestsChallengeHelper(pState, pLevel, pPos, player);
                        } else {
                           this.completeChallenge(pState, pLevel, pPos, player, false, false);
                        }
                     }
                     break;
                  case "player_waves":
                     int secondsRemaining = (int)(this.timer - pLevel.getGameTime()) / 20;
                     if (this.mobsLeft == 0 && secondsRemaining >= 10) {
                        this.mobsLeft = -1;
                        if (player.getPersistentData().contains("challenge_time")) {
                           int currentPlayerChallengeTime = player.getPersistentData().getInt("challenge_time");
                           player.getPersistentData().putInt("challenge_time", currentPlayerChallengeTime + (secondsRemaining - 5));
                           player.sendSystemMessage(Component.translatable("trbeyond.challenge.wave.early_completion"));
                        }

                        this.timer -= (long)(secondsRemaining - 5) * 20L;
                     }

                     if (this.timer < pLevel.getGameTime()) {
                        this.playerWavesHelperSpawn(pState, pLevel, pPos, player);
                     }
               }
            }
         }
      }
   }

   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
      return pBlockEntityType == BeyondBlockEntities.SHRINE.get() ? ShrineBlockEntity::tick : null;
   }

   @NotNull
   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (!level.isClientSide) {
         ShrineBlockEntity shrineBlockEntity = (ShrineBlockEntity)level.getBlockEntity(pos);
         if (shrineBlockEntity == null) {
            return InteractionResult.FAIL;
         }

         Optional<IBeyondPlayerCapability> capabilityOpt = ChallengeEventHandler.getPlayerCapability(player);
         if (capabilityOpt.isPresent() && capabilityOpt.get().getChallengeType() != null) {
            player.sendSystemMessage(Component.translatable("trbeyond.message.shrine.already_possess_challenge"));
            return InteractionResult.SUCCESS;
         }

         if (shrineBlockEntity.getPersistentData().contains("challengeState")) {
            String challengeState = shrineBlockEntity.getPersistentData().getString("challengeState");
            switch (challengeState) {
               case "active":
                  BlockEntity blockEntity = level.getBlockEntity(pos);
                  if (blockEntity != null) {
                     long lastActivatedTick = blockEntity.getPersistentData().contains("lastActivatedTick")
                        ? blockEntity.getPersistentData().getLong("lastActivatedTick")
                        : 0L;
                     long currentTick = blockEntity.getLevel().getGameTime();
                     if (currentTick - lastActivatedTick > 6000L) {
                        blockEntity.getPersistentData().putString("challengeState", "failed");
                        if (blockEntity.getPersistentData().contains("player")) {
                           this.setCooldown(blockEntity.getLevel(), pos, UUID.fromString(blockEntity.getPersistentData().getString("player")));
                        }

                        player.sendSystemMessage(Component.translatable("trbeyond.message.shrine.reset"));
                        return InteractionResult.SUCCESS;
                     }
                  }

                  player.sendSystemMessage(Component.translatable("trbeyond.message.shrine.already_active"));
                  return InteractionResult.SUCCESS;
               case "failed":
                  this.activateShrine(state, level, pos, player);
                  break;
               case "completed":
                  this.activateShrine(state, level, pos, player);
            }
         } else {
            this.activateShrine(state, level, pos, player);
         }
      }

      return InteractionResult.SUCCESS;
   }

   public void activateShrine(BlockState state, Level level, BlockPos pos, Player player) {
      boolean warningIssued = false;
      BlockEntity blockEntity = level.getBlockEntity(pos);
      if (blockEntity != null) {
         if (blockEntity.getPersistentData().contains("lastActivatedTick")) {
            long previouslyActivatedTick = blockEntity.getPersistentData().getLong("lastActivatedTick");
            long currentTick = blockEntity.getLevel().getGameTime();
            if (currentTick < 80L + previouslyActivatedTick) {
               warningIssued = true;
            }
         }

         boolean playerOnCooldown = !DungeonSaveData.getOverworld(level).canUseShrine(player, pos, this.getPackedTime(), level);
         if (playerOnCooldown) {
            long cooldown;
            if (this.tier == ShrineBlock.DungeonShrineTier.STELLAR) {
               cooldown = (long)((Integer)ServerConfig.INSTANCE.dungeonConfig.stellarShrineCooldown.get()).intValue();
            } else {
               cooldown = (long)((Integer)ServerConfig.INSTANCE.dungeonConfig.shrineCooldown.get()).intValue();
            }

            long lastUsed = DungeonSaveData.getOverworld(level).getShrineCooldown(player).get(pos);
            if (!(Boolean)ServerConfig.INSTANCE.dungeonConfig.shrineCooldownPlaythroughs.get()) {
               player.sendSystemMessage(Component.translatable("trbeyond.message.shrine.cooldown", new Object[]{cooldown + lastUsed - this.getPackedTime()}));
               return;
            }

            if (!warningIssued) {
               player.sendSystemMessage(
                  Component.translatable("trbeyond.message.shrine.cooldown.reward_warning", new Object[]{cooldown + lastUsed - this.getPackedTime()})
               );
               blockEntity.getPersistentData().putLong("lastActivatedTick", blockEntity.getLevel().getGameTime());
               return;
            }
         }

         ShrineBlockEntity shrineBlockEntity = (ShrineBlockEntity)level.getBlockEntity(pos);
         if (shrineBlockEntity == null) {
            return;
         }

         shrineBlockEntity.getPersistentData().putString("challengeState", "active");
         String challengeString = "";
         Optional<Wrapper<String>> challenge = this.challengeRandomizer.getRandom(blockEntity.getLevel().getRandom());
         List<String> prohibitedCooldownChallenges = List.of("minh_slimes", "pick_the_correct_chest");

         while (playerOnCooldown && prohibitedCooldownChallenges.contains(challenge.get().getData())) {
            challenge = this.challengeRandomizer.getRandom(blockEntity.getLevel().getRandom());
         }

         if (!challenge.isPresent()) {
            return;
         }

         challengeString = (String)challenge.get().getData();
         shrineBlockEntity.getPersistentData().putString("currentPlayer", player.getStringUUID());
         shrineBlockEntity.getPersistentData().putString("currentChallenge", challengeString);
         player.getPersistentData().putString("shrineTier", this.tier.name().toLowerCase());
         player.getPersistentData().putString("shrineChallenge", challengeString);
         this.pickChestsCleanupChests(level, pos);
         level.playSound(null, pos, SoundEvents.NOTE_BLOCK_BELL, SoundSource.BLOCKS, 1.0F, 1.0F);
         if (ChallengeEventHandler.shrineChallengesDisabled(player.getLevel(), player)) {
            return;
         }

         switch (challengeString) {
            case "waves":
               this.waveSurvival(state, level, pos, player);
               break;
            case "player_waves":
               this.playerWaves(state, level, pos, player);
               break;
            case "destroy_shrine": {
               ChallengeType shrine_challenge = new ChallengeType("destroy_shrine", "shrine", pos.toString(), 1, false, "minecraft:air", 0, 60, false);
               ChallengeEventHandler.giveChallengeType(player, shrine_challenge, true);
               this.destroyShrine(state, level, pos, player);
               break;
            }
            case "defeat_boss": {
               ChallengeType shrine_challenge = new ChallengeType("defeat_boss", "shrine", pos.toString(), 1, false, "minecraft:air", 0, 180, false);
               ChallengeEventHandler.giveChallengeType(player, shrine_challenge, true);
               this.defeatBoss(state, level, pos, player);
               break;
            }
            case "minh_slimes":
               this.minhSlimeSlayerChallenge(state, level, pos, player);
               break;
            case "pick_the_correct_chest":
               this.pickChestsChallenge(state, level, pos, player);
         }
      }
   }

   private void pickChestsChallenge(BlockState state, Level level, BlockPos pos, Player player) {
      this.currentChestChallengeTier = ShrineBlock.DungeonShrineTier.STELLAR;
      this.dontDecrementNextFailure = true;
      ChallengeType shrine_challenge = new ChallengeType("pick_the_correct_chest", "shrine", pos.toString(), 1, false, "minecraft:air", 0, 60, false, 60);
      ChallengeEventHandler.giveChallengeType(player, shrine_challenge, true);
      player.sendSystemMessage(Component.translatable("trbeyond.challenge.pick_the_correct_chest.explanation"));
      this.pickChestsChallengeHelper(state, level, pos, player);
   }

   private void pickChestsChallengeHelper(BlockState state, Level level, BlockPos pos, Player player) {
      this.currentChestChallengeTier = switch (this.currentChestChallengeTier) {
         case BRONZE -> ShrineBlock.DungeonShrineTier.SILVER;
         case SILVER -> ShrineBlock.DungeonShrineTier.GOLD;
         case GOLD -> ShrineBlock.DungeonShrineTier.STELLAR;
         case STELLAR -> ShrineBlock.DungeonShrineTier.BRONZE;
      };
      this.timer = level.getGameTime() + 1200L;
      int spawnedChests = 6 - this.currentChestChallengeTier.ordinal();
      int completedChallenges = 0;
      if (DungeonCapability.getFrom(player).isPresent() && DungeonCapability.getFrom(player).resolve().isPresent()) {
         IDungeonCapability cap = (IDungeonCapability)DungeonCapability.getFrom(player).resolve().get();
         completedChallenges = cap.getChallengeCompletionCount(this.tier);
      }

      List<BlockPos> chestPositions = List.of(
         pos.relative((Direction)state.getValue(FACING), 2),
         pos.relative((Direction)state.getValue(FACING), 2).relative(((Direction)state.getValue(FACING)).getClockWise(), 3),
         pos.relative((Direction)state.getValue(FACING), 2).relative(((Direction)state.getValue(FACING)).getCounterClockWise(), 3),
         pos.relative((Direction)state.getValue(FACING), -3),
         pos.relative((Direction)state.getValue(FACING), -3).relative(((Direction)state.getValue(FACING)).getCounterClockWise(), 3),
         pos.relative((Direction)state.getValue(FACING), -3).relative(((Direction)state.getValue(FACING)).getClockWise(), 3)
      );

      Block chestBlock = switch (this.currentChestChallengeTier) {
         case BRONZE -> (Block)BeyondBlocks.BRONZE_CHEST.get();
         case SILVER -> (Block)BeyondBlocks.SILVER_CHEST.get();
         case GOLD -> (Block)BeyondBlocks.GOLD_CHEST.get();
         case STELLAR -> (Block)BeyondBlocks.STELLAR_CHEST.get();
      };

      float coinDoubleChance;
      float var25 = coinDoubleChance = switch (completedChallenges) {
         case 0 -> 0.0F;
         case 1 -> 0.05F;
         case 2 -> 0.1F;
         default -> 0.15F;
      };
      List<DungeonChestBlockEntity> chests = new ArrayList<>(spawnedChests);

      for (int i = 0; i < spawnedChests; i++) {
         BlockPos chestPos = chestPositions.get(i);
         level.setBlock(chestPos, chestBlock.defaultBlockState(), 3);
         DungeonChestBlock dun = (DungeonChestBlock)level.getBlockState(chestPos).getBlock();
         BlockPos chestPos2 = chestPos.relative(DungeonChestBlock.getConnectedDirection(level.getBlockState(chestPos)));
         level.setBlock(
            chestPos2,
            (BlockState)dun.defaultBlockState().setValue(DungeonChestBlock.TYPE, ((ChestType)level.getBlockState(chestPos).getValue(DungeonChestBlock.TYPE)).getOpposite()),
            3
         );
         level.getBlockState(chestPos).updateNeighbourShapes(level, chestPos, 3);
         if (level.getBlockEntity(chestPos) instanceof DungeonChestBlockEntity chest) {
            chest.getPersistentData().putBoolean("noLock", true);
            chest.getPersistentData().putLong("placedTime", level.getGameTime());
         }

         DungeonSaveData.getOverworld(level).addBossChest(chestPos);
         if (level.getBlockEntity(chestPos2) instanceof DungeonChestBlockEntity chest) {
            chests.add(chest);

            String lootTable = switch (this.currentChestChallengeTier) {
               case BRONZE -> "shrines/shrine_bronze_1";
               case SILVER -> "shrines/shrine_silver_1";
               case GOLD -> "shrines/shrine_gold_1";
               case STELLAR -> "shrines/shrine_stellar_1";
            };
            chest.setLootTable(new ResourceLocation("trbeyond", lootTable), level.getRandom().nextLong());
            chest.unpackLootTable(player);
            chest.setChanged();
            chest.getCapability(ForgeCapabilities.ITEM_HANDLER)
               .ifPresent(
                  h -> {
                     for (int j = 0; j < h.getSlots(); j++) {
                        ItemStack stack = h.getStackInSlot(j);
                        if (!stack.isEmpty()
                           && (
                              stack.getItem() instanceof TensuraSword
                                 || stack.getItem() instanceof GeoArmorItem
                                 || stack.getItem() instanceof SimpleArmorItem
                                 || stack.getItem() instanceof SimpleSwordItem
                                 || stack.getItem() instanceof SimpleBowItem
                           )
                           && Math.random() < 0.5) {
                           stack = h.extractItem(j, 1, false);
                           EngravingEnchantment.randomEngraving(
                              player, stack, (double)((Integer)TensuraConfig.INSTANCE.enchantmentsConfig.unique.get()).intValue()
                           );
                           h.insertItem(j, stack, false);
                        }

                        if (Math.random() < (double)coinDoubleChance
                           && (
                              stack.is((Item)TensuraMaterialItems.BRONZE_COIN.get())
                                 || stack.is((Item)TensuraMaterialItems.SILVER_COIN.get())
                                 || stack.is((Item)TensuraMaterialItems.GOLD_COIN.get())
                                 || stack.is((Item)TensuraMaterialItems.STELLAR_GOLD_COIN.get())
                           )) {
                           stack.setCount(Math.min(64, stack.getCount() * 2));
                        }
                     }
                  }
               );
         }
      }

      int trappedChest1 = getRandomInRange(0, spawnedChests - 1);
      int trappedChest2 = trappedChest1;

      while (trappedChest2 == trappedChest1) {
         trappedChest2 = getRandomInRange(0, spawnedChests - 1);
      }

      chests.get(trappedChest1).clearContent();
      chests.get(trappedChest2).clearContent();
      chests.get(trappedChest1).getPersistentData().putBoolean("incorrectChest", true);
      chests.get(trappedChest2).getPersistentData().putBoolean("incorrectChest", true);

      for (BlockEntity chest : chests) {
         chest.getPersistentData().putBoolean("challengeChest", true);
         chest.getPersistentData().putLong("shrinePos", pos.asLong());
      }

      this.chestsLeft = spawnedChests - 2;
   }

   public void pickChestsCorrectChest() {
      this.chestsLeft--;
   }

   public void pickChestsWrongChest(Level level, BlockPos pos) {
      BlockEntity shrineBlockEntity = level.getBlockEntity(pos);
      if (shrineBlockEntity != null) {
         if (shrineBlockEntity.getPersistentData().getString("challengeState").equals("active")) {
            UUID playerUUID = UUID.fromString(shrineBlockEntity.getPersistentData().getString("currentPlayer"));
            if (this.currentChestChallengeTier != this.tier) {
               this.challengeCausesCooldown = false;
            }

            this.setCooldown(level, pos, playerUUID);
            this.failChallenge(level, pos, level.getPlayerByUUID(playerUUID));
            this.pickChestsCleanupChests(level, pos);
         }
      }
   }

   private void pickChestsCleanupChests(Level level, BlockPos pos) {
      BlockState state = level.getBlockState(pos);

      for (BlockPos chestPos : List.of(
         pos.relative((Direction)state.getValue(FACING), 2),
         pos.relative((Direction)state.getValue(FACING), 2).relative(((Direction)state.getValue(FACING)).getClockWise(), 3),
         pos.relative((Direction)state.getValue(FACING), 2).relative(((Direction)state.getValue(FACING)).getCounterClockWise(), 3),
         pos.relative((Direction)state.getValue(FACING), -3),
         pos.relative((Direction)state.getValue(FACING), -3).relative(((Direction)state.getValue(FACING)).getCounterClockWise(), 3),
         pos.relative((Direction)state.getValue(FACING), -3).relative(((Direction)state.getValue(FACING)).getClockWise(), 3)
      )) {
         BlockEntity blockEntity = level.getBlockEntity(chestPos);
         if (blockEntity instanceof DungeonChestBlockEntity) {
            DungeonChestBlockEntity chest1 = (DungeonChestBlockEntity)blockEntity;
            BlockPos chestPos2 = chestPos.relative(DungeonChestBlock.getConnectedDirection(level.getBlockState(chestPos)));
            BlockEntity blockEntity2 = level.getBlockEntity(chestPos2);
            if (blockEntity2 instanceof DungeonChestBlockEntity) {
               DungeonChestBlockEntity chest2 = (DungeonChestBlockEntity)blockEntity2;
               if (chest1.getPersistentData().contains("challengeChest") || chest2.getPersistentData().contains("challengeChest")) {
                  chest1.clearContent();
                  chest2.clearContent();
               }
            }

            level.removeBlock(chestPos, false);
            level.removeBlock(chestPos2, false);
         }
      }
   }

   private void minhSlimeSlayerChallenge(BlockState state, Level level, BlockPos pos, Player player) {
      ShrineBlockEntity shrineBlockEntity = (ShrineBlockEntity)level.getBlockEntity(pos);
      if (shrineBlockEntity != null) {
         shrineBlockEntity.getPersistentData().putString("challenge", "waves");
         shrineBlockEntity.getPersistentData().putString("player", player.getUUID().toString());
         shrineBlockEntity.getPersistentData().putLong("blockPos", pos.asLong());
         int spawnTime = 60;
         int waves = 15;
         ChallengeType shrine_challenge = new ChallengeType("minh_slimes", "shrine", pos.toString(), waves, false, "minecraft:air", 0, 60, false);
         ChallengeEventHandler.giveChallengeType(player, shrine_challenge, true);

         for (int j = 1; j < waves; j++) {
            int waveCount = j;
            TaskScheduler.ScheduledTask task = new TaskScheduler.ScheduledTask(spawnTime * j, () -> {
               if (shrineBlockEntity.getPersistentData().contains("challengeState")) {
                  String challengeState = shrineBlockEntity.getPersistentData().getString("challengeState");
                  if (challengeState.equals("failed") || challengeState.equals("completed")) {
                     return;
                  }
               }

               double dx = (double)pos.getX() + 0.5 - player.getX();
               double dy = (double)pos.getY() + 0.5 - player.getY();
               double dz = (double)pos.getZ() + 0.5 - player.getZ();
               double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
               if (distance > 25.0) {
                  this.failChallenge(level, pos, player);
               } else {
                  shrineBlockEntity.getPersistentData().putInt("wave", waveCount);
                  ChallengeEventHandler.getPlayerCapability(player).ifPresent(capability -> {
                     if (capability.getChallengeType() != null) {
                        capability.getChallengeType().setCountDone(waveCount);
                        MinhSlimeEntity challengeMob = new MinhSlimeEntity((EntityType<? extends MetalSlimeEntity>)BeyondEntities.MINH_SLIME.get(), level);
                        challengeMob.setSize(level.random.nextIntBetweenInclusive(1, 3), false);
                        int xOffset = getRandomInRange(-6, 6);
                        int zOffset = getRandomInRange(-6, 6);
                        BlockPos mobPos = pos.offset(xOffset, 0, zOffset);
                        challengeMob.setPos((double)mobPos.getX() + 0.5, (double)mobPos.getY(), (double)mobPos.getZ() + 0.5);
                        challengeMob.setYRot(((Direction)state.getValue(FACING)).toYRot());
                        challengeMob.getPersistentData().putString("challengeMob", "Minh slime challenge");
                        level.addFreshEntity(challengeMob);
                        challengeMob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 1200, 1, false, false));
                        challengeMob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 2, false, false));
                     }
                  });
               }
            });
            TaskScheduler.scheduleTask(task);
         }

         TaskScheduler.scheduleTask(new TaskScheduler.ScheduledTask(1180, () -> {
            level.getEntitiesOfClass(MinhSlimeEntity.class, new AABB(pos).inflate(20.0)).forEach(mob -> {
               if (mob.getPersistentData().contains("challengeMob") && mob.getPersistentData().getString("challengeMob").equals("Minh slime challenge")) {
                  mob.discard();
               }
            });
            ChallengeEventHandler.getPlayerCapability(player).ifPresent(capability -> {
               if (capability.getChallengeType() != null) {
                  capability.getChallengeType().setCountDone(waves);
               }
            });
            this.completeChallenge(state, level, pos, player, false, true);
         }));
      }
   }

   public static int getRandomInRange(int min, int max) {
      return (int)(Math.random() * (double)(max - min + 1)) + min;
   }

   public void destroyShrine(BlockState state, Level level, BlockPos pos, Player player) {
      ShrineBlockEntity shrineBlockEntity = (ShrineBlockEntity)level.getBlockEntity(pos);
      if (shrineBlockEntity != null) {
         shrineBlockEntity.getPersistentData().putString("challenge", "destroy_shrine");
         shrineBlockEntity.getPersistentData().putString("player", player.getUUID().toString());
         shrineBlockEntity.getPersistentData().putLong("blockPos", pos.asLong());
         ShrineEntity shrineEntity = new ShrineEntity(
            (EntityType<? extends ShrineEntity>)BeyondEntities.BRONZE_SHRINE.get(), level, ShrineBlock.DungeonShrineTier.BRONZE
         );

         shrineEntity = switch (this.tier) {
            case SILVER -> new ShrineEntity((EntityType<? extends ShrineEntity>)BeyondEntities.SILVER_SHRINE.get(), level, ShrineBlock.DungeonShrineTier.SILVER);
            case GOLD -> new ShrineEntity((EntityType<? extends ShrineEntity>)BeyondEntities.GOLD_SHRINE.get(), level, ShrineBlock.DungeonShrineTier.GOLD);
            case STELLAR -> new ShrineEntity(
            (EntityType<? extends ShrineEntity>)BeyondEntities.STELLAR_SHRINE.get(), level, ShrineBlock.DungeonShrineTier.STELLAR
         );
         };
         shrineEntity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         shrineEntity.setYRot(((Direction)state.getValue(FACING)).toYRot());
         level.addFreshEntity(shrineEntity);
         shrineEntity.getPersistentData().putString("challengeMob", "Destroy the Shrine");
         shrineEntity.getPersistentData().putString("player", player.getUUID().toString());
         shrineEntity.getPersistentData().putLong("blockPos", pos.asLong());
         TaskScheduler.ScheduledTask task = new TaskScheduler.ScheduledTask(
            1200,
            () -> {
               if (shrineBlockEntity.getPersistentData().contains("challengeState")) {
                  AABB shrineArea = new AABB(pos).inflate(45.0);
                  boolean shrineAlive = level.getEntitiesOfClass(
                        LivingEntity.class,
                        shrineArea,
                        entity -> entity.getPersistentData().contains("challengeMob")
                              && entity.getPersistentData().getString("challengeMob").equals("Destroy the Shrine")
                     )
                     .stream()
                     .anyMatch(entity -> !entity.isDeadOrDying());
                  if (shrineAlive && shrineBlockEntity.getPersistentData().getString("challengeState").equals("active")) {
                     this.failChallenge(level, pos, player);
                  }
               }
            }
         );
         TaskScheduler.scheduleTask(task);
      }
   }

   public void defeatBoss(BlockState state, Level level, BlockPos pos, Player player) {
      ShrineBlockEntity shrineBlockEntity = (ShrineBlockEntity)level.getBlockEntity(pos);
      if (shrineBlockEntity != null) {
         shrineBlockEntity.getPersistentData().putString("challenge", "defeat_boss");
         shrineBlockEntity.getPersistentData().putString("player", player.getUUID().toString());
         shrineBlockEntity.getPersistentData().putLong("blockPos", pos.asLong());
         EntityType<? extends LivingEntity> bossType;
         if (this.tier == ShrineBlock.DungeonShrineTier.STELLAR) {
            List<EntityType<? extends LivingEntity>> stellarBosses = List.of(
               (EntityType<? extends LivingEntity>)TensuraEntityTypes.SUPERMASSIVE_SLIME.get(),
               (EntityType<? extends LivingEntity>)BeyondEntities.SLIME_DOLL_BOSS.get(),
               (EntityType<? extends LivingEntity>)BeyondEntities.STELLAR_MIMIC_BOSS.get()
            );
            bossType = stellarBosses.get(level.getRandom().nextInt(stellarBosses.size()));
         } else {
            bossType = (EntityType<? extends LivingEntity>)TensuraEntityTypes.SUPERMASSIVE_SLIME.get();
         }

         LivingEntity bossMob = (LivingEntity)bossType.create(level);
         bossMob.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         bossMob.setYRot(((Direction)state.getValue(FACING)).toYRot());
         bossMob.getPersistentData().putString("challengeMob", "Defeat the Boss");
         bossMob.getPersistentData().putLong("blockPos", pos.asLong());
         bossMob.getPersistentData().putString("player", player.getUUID().toString());
         bossMob.getPersistentData().putBoolean("challengeElite", true);
         level.addFreshEntity(bossMob);
         if (this.tier == ShrineBlock.DungeonShrineTier.STELLAR) {
            player.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.ANTI_SKILL.get(), 3600, 0, false, false));
            player.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.SILENCE.get(), 3600, 0, false, false));
            player.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.MAGIC_INTERFERENCE.get(), 3600, 0, false, false));
         }

         TaskScheduler.ScheduledTask task = new TaskScheduler.ScheduledTask(
            1200,
            () -> {
               if (shrineBlockEntity.getPersistentData().contains("challengeState")) {
                  String challengeState = shrineBlockEntity.getPersistentData().getString("challengeState");
                  if (challengeState.equals("failed") || challengeState.equals("completed")) {
                     return;
                  }

                  AABB bossArea = new AABB(pos).inflate(45.0);
                  boolean bossAlive = level.getEntitiesOfClass(
                        LivingEntity.class,
                        bossArea,
                        entity -> entity.getPersistentData().contains("challengeMob")
                              && entity.getPersistentData().getString("challengeMob").equals("Defeat the Boss")
                     )
                     .stream()
                     .anyMatch(entity -> !entity.isDeadOrDying());
                  if (!bossAlive && shrineBlockEntity.getPersistentData().getString("challengeState").equals("active")) {
                     this.completeChallenge(state, level, pos, player, true, true);
                  } else {
                     this.failChallenge(level, pos, player);
                     shrineBlockEntity.getPersistentData().putString("challengeState", "failed");
                  }
               }
            }
         );
         TaskScheduler.scheduleTask(task);
      }
   }

   public void playerWaves(BlockState state, Level level, BlockPos pos, Player player) {
      ShrineBlockEntity shrineBlockEntity = (ShrineBlockEntity)level.getBlockEntity(pos);
      if (shrineBlockEntity != null) {
         shrineBlockEntity.getPersistentData().putString("challenge", "player_waves");
         shrineBlockEntity.getPersistentData().putString("player", player.getUUID().toString());
         shrineBlockEntity.getPersistentData().putLong("blockPos", pos.asLong());

         this.finalWave = switch (this.tier) {
            case SILVER -> 3;
            case GOLD, STELLAR -> 5;
            default -> 1;
         };
         this.timer = level.getGameTime() - 20L;
         ChallengeType shrine_challenge = new ChallengeType(
            "player_waves", "shrine", pos.toString(), this.finalWave, false, "minecraft:air", 0, this.finalWave * 60 + 1, false, 60
         );
         ChallengeEventHandler.giveChallengeType(player, shrine_challenge, true);
         this.playerWavesHelperSpawn(state, level, pos, player);
      }
   }

   private void playerWavesHelperSpawn(BlockState state, Level level, BlockPos pos, Player player) {
      this.timer += 1200L;
      boolean failedClear = false;

      for (Entity entity : level.getEntities(null, new AABB(pos).inflate(20.0))) {
         if (entity.getPersistentData().getString("challengeMob").equals("Survive the Waves")) {
            entity.discard();
            failedClear = true;
         }
      }

      if (failedClear) {
         this.failChallenge(level, pos, player);
      } else {
         if (this.currentWave < this.finalWave) {
            record EffectInfo(MobEffect effect, int amplification) {
            }

            WeightedRandomList<Wrapper<EffectInfo>> randomEffects = WeightedRandomList.create(
               new Wrapper[]{
                  WeightedEntry.wrap(new EffectInfo(MobEffects.REGENERATION, this.tier.ordinal()), 5),
                  WeightedEntry.wrap(new EffectInfo(MobEffects.DAMAGE_RESISTANCE, this.tier.ordinal()), 5),
                  WeightedEntry.wrap(new EffectInfo(MobEffects.MOVEMENT_SPEED, this.tier.ordinal()), 5),
                  WeightedEntry.wrap(new EffectInfo(MobEffects.DAMAGE_BOOST, this.tier.ordinal()), 5),
                  WeightedEntry.wrap(new EffectInfo((MobEffect)TensuraMobEffects.BEAST_TRANSFORMATION.get(), 0), 1)
               }
            );
            WeightedRandomList<Wrapper<EffectInfo>> randomPenalties = WeightedRandomList.create(
               new Wrapper[]{
                  WeightedEntry.wrap(new EffectInfo((MobEffect)TensuraMobEffects.FRAGILITY.get(), 0), 1),
                  WeightedEntry.wrap(new EffectInfo((MobEffect)TensuraMobEffects.CORROSION.get(), 0), 1),
                  WeightedEntry.wrap(new EffectInfo(MobEffects.WEAKNESS, 2), 1),
                  WeightedEntry.wrap(new EffectInfo(MobEffects.MOVEMENT_SLOWDOWN, 2), 1),
                  WeightedEntry.wrap(new EffectInfo(MobEffects.POISON, 2), 1)
               }
            );
            ChallengeEventHandler.getPlayerCapability(player).ifPresent(capability -> {
               if (capability.getChallengeType() != null) {
                  capability.getChallengeType().setCountDone(this.currentWave);
               }
            });
            int mobCount = getRandomInRange(4, 6) + this.tier.ordinal() * 2;
            this.mobsLeft = mobCount;
            WeightedRandomList<Wrapper<EntityType<? extends Mob>>> waveTheme = (WeightedRandomList<Wrapper<EntityType<? extends Mob>>>)((Wrapper)ShrineBlock.WaveThemeProvider.themesProvider(
                     this.tier
                  )
                  .getRandom(level.getRandom())
                  .get())
               .getData();

            for (int k = 0; k < mobCount; k++) {
               Mob challengeMob = (Mob)((EntityType)((Wrapper)waveTheme.getRandom(level.random).get()).getData()).create(level);
               int xOffset = getRandomInRange(-5, 5);
               int zOffset = getRandomInRange(-5, 5);
               BlockPos mobPos = pos.offset(xOffset, 0, zOffset);
               challengeMob.setPos((double)mobPos.getX() + 0.5, (double)mobPos.getY(), (double)mobPos.getZ() + 0.5);
               challengeMob.setYRot(((Direction)state.getValue(FACING)).toYRot());
               int challengeCount = player.getPersistentData().getInt("challenges_completed");
               if (DungeonCapability.getFrom(player).isPresent()) {
                  IDungeonCapability cap = (IDungeonCapability)DungeonCapability.getFrom(player).resolve().get();
                  challengeCount = cap.getChallengeCompletionCount(this.tier);
               }

               if (level.getRandom().nextDouble() < getEliteChance(challengeCount)) {
                  challengeMob.getPersistentData().putBoolean("challengeElite", true);
               }

               challengeMob.getPersistentData().putString("challengeMob", "Survive the Waves");
               challengeMob.getPersistentData().putLong("shrinePos", pos.asLong());
               if (challengeMob instanceof HumanoidNPCEntity humanoidMob) {
                  switch (this.tier) {
                     case BRONZE:
                     default:
                        break;
                     case SILVER:
                        if (humanoidMob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof SwordItem) {
                           humanoidMob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
                        } else if (humanoidMob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof BowItem) {
                           EnchantmentHelper.enchantItem(level.getRandom(), humanoidMob.getItemBySlot(EquipmentSlot.MAINHAND), 10, true);
                        }

                        humanoidMob.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike)TensuraArmorItems.MONSTER_LEATHER_C_HELMET.get()));
                        humanoidMob.setItemSlot(EquipmentSlot.CHEST, new ItemStack((ItemLike)TensuraArmorItems.MONSTER_LEATHER_C_CHESTPLATE.get()));
                        humanoidMob.setItemSlot(EquipmentSlot.LEGS, new ItemStack((ItemLike)TensuraArmorItems.MONSTER_LEATHER_C_LEGGINGS.get()));
                        humanoidMob.setItemSlot(EquipmentSlot.FEET, new ItemStack((ItemLike)TensuraArmorItems.MONSTER_LEATHER_C_BOOTS.get()));
                        break;
                     case GOLD:
                        if (humanoidMob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof SwordItem) {
                           humanoidMob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)TensuraToolItems.HIGH_MAGISTEEL_SWORD.get()));
                        } else if (humanoidMob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof BowItem) {
                           humanoidMob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)TensuraToolItems.SPIDER_BOW.get()));
                        }

                        humanoidMob.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike)TensuraArmorItems.HIGH_MAGISTEEL_HELMET.get()));
                        humanoidMob.setItemSlot(EquipmentSlot.CHEST, new ItemStack((ItemLike)TensuraArmorItems.HIGH_MAGISTEEL_CHESTPLATE.get()));
                        humanoidMob.setItemSlot(EquipmentSlot.LEGS, new ItemStack((ItemLike)TensuraArmorItems.HIGH_MAGISTEEL_LEGGINGS.get()));
                        humanoidMob.setItemSlot(EquipmentSlot.FEET, new ItemStack((ItemLike)TensuraArmorItems.HIGH_MAGISTEEL_BOOTS.get()));
                        break;
                     case STELLAR:
                        if (humanoidMob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof SwordItem) {
                           humanoidMob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)TensuraToolItems.ADAMANTITE_SWORD.get()));
                        } else if (humanoidMob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof BowItem) {
                           humanoidMob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)TensuraToolItems.SPIDER_BOW.get()));
                           EnchantmentHelper.enchantItem(level.getRandom(), humanoidMob.getItemBySlot(EquipmentSlot.MAINHAND), 30, true);
                        }

                        humanoidMob.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike)TensuraArmorItems.MITHRIL_HELMET.get()));
                        humanoidMob.setItemSlot(EquipmentSlot.CHEST, new ItemStack((ItemLike)TensuraArmorItems.MITHRIL_CHESTPLATE.get()));
                        humanoidMob.setItemSlot(EquipmentSlot.LEGS, new ItemStack((ItemLike)TensuraArmorItems.MITHRIL_LEGGINGS.get()));
                        humanoidMob.setItemSlot(EquipmentSlot.FEET, new ItemStack((ItemLike)TensuraArmorItems.MITHRIL_BOOTS.get()));
                  }
               }

               level.addFreshEntity(challengeMob);
               challengeMob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0, false, false));
               challengeMob.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.PRESENCE_SENSE.get(), 1200, 9, false, false));
               if (this.tier == ShrineBlock.DungeonShrineTier.GOLD || this.tier == ShrineBlock.DungeonShrineTier.STELLAR) {
                  EffectInfo randomEffect = (EffectInfo)((Wrapper)randomEffects.getRandom(level.getRandom()).get()).getData();
                  challengeMob.addEffect(new MobEffectInstance(randomEffect.effect, 1200, randomEffect.amplification));
                  int absorptionAmp = this.tier == ShrineBlock.DungeonShrineTier.STELLAR ? 4 : 14;
                  challengeMob.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, absorptionAmp));
               }

               challengeMob.setTarget(player);
            }

            if (this.tier == ShrineBlock.DungeonShrineTier.STELLAR) {
               EffectInfo randomPenalty = (EffectInfo)((Wrapper)randomPenalties.getRandom(level.getRandom()).get()).getData();
               player.addEffect(new MobEffectInstance(randomPenalty.effect, 300, randomPenalty.amplification));
            }

            this.currentWave++;
         } else {
            if (player.getLevel() != level) {
               this.failChallenge(level, pos, player);
               return;
            }

            if (player.distanceToSqr((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) > Math.pow(15.0, 2.0)) {
               this.failChallenge(level, pos, player);
               return;
            }

            this.completeChallenge(state, level, pos, player, true, true);
         }
      }
   }

   public void mobKilled() {
      this.mobsLeft--;
   }

   private static double getEliteChance(int challengeCount) {
      double eliteChance = 0.0;
      if (challengeCount >= 20) {
         eliteChance = 0.95;
      } else if (challengeCount >= 10) {
         eliteChance = 0.5;
      } else if (challengeCount >= 6) {
         eliteChance = 0.25;
      } else if (challengeCount >= 5) {
         eliteChance = 0.15;
      } else if (challengeCount >= 3) {
         eliteChance = 0.1;
      } else if (challengeCount >= 2) {
         eliteChance = 0.05;
      }

      return eliteChance;
   }

   public void waveSurvival(BlockState state, Level level, BlockPos pos, Player player) {
      ShrineBlockEntity shrineBlockEntity = (ShrineBlockEntity)level.getBlockEntity(pos);
      if (shrineBlockEntity != null) {
         shrineBlockEntity.getPersistentData().putString("challenge", "waves");
         shrineBlockEntity.getPersistentData().putString("player", player.getUUID().toString());
         shrineBlockEntity.getPersistentData().putLong("blockPos", pos.asLong());
         ShrineEntity shrineEntity = new ShrineEntity(
            (EntityType<? extends ShrineEntity>)BeyondEntities.BRONZE_SHRINE.get(), level, ShrineBlock.DungeonShrineTier.BRONZE
         );

         shrineEntity = switch (this.tier) {
            case SILVER -> new ShrineEntity((EntityType<? extends ShrineEntity>)BeyondEntities.SILVER_SHRINE.get(), level, ShrineBlock.DungeonShrineTier.SILVER);
            case GOLD -> new ShrineEntity((EntityType<? extends ShrineEntity>)BeyondEntities.GOLD_SHRINE.get(), level, ShrineBlock.DungeonShrineTier.GOLD);
            case STELLAR -> new ShrineEntity(
            (EntityType<? extends ShrineEntity>)BeyondEntities.STELLAR_SHRINE.get(), level, ShrineBlock.DungeonShrineTier.STELLAR
         );
         };
         shrineEntity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         shrineEntity.setYRot(((Direction)state.getValue(FACING)).toYRot());
         level.addFreshEntity(shrineEntity);
         shrineEntity.getPersistentData().putString("challenge", "Protect the Shrine");
         shrineEntity.getPersistentData().putString("challengeMob", "Protect the Shrine");
         shrineEntity.getPersistentData().putString("player", player.getUUID().toString());
         shrineEntity.getPersistentData().putLong("blockPos", pos.asLong());
         int spawnTime = 200;
         if (this.tier == ShrineBlock.DungeonShrineTier.BRONZE) {
            spawnTime = 400;
         } else if (this.tier == ShrineBlock.DungeonShrineTier.SILVER) {
            spawnTime = 300;
         } else if (this.tier == ShrineBlock.DungeonShrineTier.STELLAR) {
            spawnTime = 100;
         }

         int waves = 3600 / spawnTime;
         ChallengeType shrine_challenge = new ChallengeType("waves", "shrine", pos.toString(), waves, false, "minecraft:air", 0, 180, false, spawnTime / 20);
         ChallengeEventHandler.giveChallengeType(player, shrine_challenge, true);

         for (int j = 1; j < waves + 1; j++) {
            int waveCount = j;
            TaskScheduler.ScheduledTask task = new TaskScheduler.ScheduledTask(
               spawnTime * j,
               () -> {
                  if (shrineBlockEntity.getPersistentData().contains("challengeState")) {
                     String challengeState = shrineBlockEntity.getPersistentData().getString("challengeState");
                     if (challengeState.equals("failed") || challengeState.equals("completed")) {
                        return;
                     }
                  }

                  double dx = (double)pos.getX() + 0.5 - player.getX();
                  double dy = (double)pos.getY() + 0.5 - player.getY();
                  double dz = (double)pos.getZ() + 0.5 - player.getZ();
                  double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                  if (distance > 25.0) {
                     this.failChallenge(level, pos, player);
                  } else {
                     shrineBlockEntity.getPersistentData().putInt("wave", waveCount);
                     ChallengeEventHandler.getPlayerCapability(player)
                        .ifPresent(
                           capability -> {
                              if (capability.getChallengeType() != null) {
                                 capability.getChallengeType().setCountDone(waveCount);
                                 if (waveCount < waves) {
                                    int mobCount = 1;
                                    if (this.tier == ShrineBlock.DungeonShrineTier.BRONZE) {
                                       mobCount = getRandomInRange(1, 3);
                                    } else if (this.tier == ShrineBlock.DungeonShrineTier.SILVER) {
                                       mobCount = getRandomInRange(3, 4);
                                    } else if (this.tier == ShrineBlock.DungeonShrineTier.GOLD) {
                                       mobCount = getRandomInRange(4, 5);
                                    } else if (this.tier == ShrineBlock.DungeonShrineTier.STELLAR) {
                                       mobCount = getRandomInRange(5, 6);
                                    }

                                    for (int i = 0; i < mobCount; i++) {
                                       LivingEntity challengeMob = (LivingEntity)(switch (getRandomInRange(0, 6)
                                          + (this.tier == ShrineBlock.DungeonShrineTier.STELLAR ? getRandomInRange(1, 4) : 0)) {
                                          case 0, 1, 2 -> new LeechLizardEntity(
                                          (EntityType<? extends LeechLizardEntity>)TensuraEntityTypes.LEECH_LIZARD.get(), level
                                       );
                                          case 3, 4 -> new HoundDogEntity((EntityType<? extends HoundDogEntity>)TensuraEntityTypes.HOUND_DOG.get(), level);
                                          case 5, 6 -> new BarghestEntity((EntityType<? extends BarghestEntity>)TensuraEntityTypes.BARGHEST.get(), level);
                                          case 7, 8 -> new DirewolfEntity((EntityType<? extends DirewolfEntity>)TensuraEntityTypes.DIREWOLF.get(), level);
                                          case 9 -> new GiantAntEntity((EntityType<? extends GiantAntEntity>)TensuraEntityTypes.GIANT_ANT.get(), level);
                                          default -> new GiantBatEntity((EntityType<? extends TamableAnimal>)TensuraEntityTypes.GIANT_BAT.get(), level);
                                       });
                                       int xOffset = getRandomInRange(-6, 6);
                                       int zOffset = getRandomInRange(-6, 6);
                                       BlockPos mobPos = pos.offset(xOffset, 0, zOffset);
                                       challengeMob.setPos((double)mobPos.getX() + 0.5, (double)mobPos.getY(), (double)mobPos.getZ() + 0.5);
                                       challengeMob.setYRot(((Direction)state.getValue(FACING)).toYRot());
                                       challengeMob.getPersistentData().putString("challengeMob", "Protect the Shrine");
                                       int challengeCount = 0;
                                       if (DungeonCapability.getFrom(player).isPresent()) {
                                          IDungeonCapability cap = (IDungeonCapability)DungeonCapability.getFrom(player).resolve().get();
                                          challengeCount = cap.getChallengeCompletionCount(this.tier);
                                       }

                                       if (level.getRandom().nextDouble() < getEliteChance(challengeCount)) {
                                          challengeMob.getPersistentData().putBoolean("challengeElite", true);
                                       }

                                       level.addFreshEntity(challengeMob);
                                       if (this.tier == ShrineBlock.DungeonShrineTier.GOLD) {
                                          if (Math.random() < 0.05) {
                                             challengeMob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, getRandomInRange(1, 2), false, false));
                                          }

                                          if (Math.random() < 0.1) {
                                             challengeMob.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 6000, 0, false, false));
                                             challengeMob.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.PRESENCE_CONCEALMENT.get(), 6000, 3));
                                          }

                                          if (Math.random() < 0.25) {
                                             challengeMob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, getRandomInRange(1, 3), false, false));
                                          }
                                       } else if (this.tier == ShrineBlock.DungeonShrineTier.STELLAR) {
                                          if (Math.random() < 0.1) {
                                             challengeMob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, getRandomInRange(1, 3), false, false));
                                          }

                                          if (Math.random() < 0.2) {
                                             challengeMob.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 6000, 0, false, false));
                                             challengeMob.addEffect(
                                                new MobEffectInstance((MobEffect)TensuraMobEffects.PRESENCE_CONCEALMENT.get(), 6000, 3, false, false)
                                             );
                                          }

                                          if (Math.random() < 0.5) {
                                             challengeMob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, getRandomInRange(1, 4), false, false));
                                          }
                                       }

                                       LivingEntity challengeMobTarget = level.getNearestEntity(
                                          level.getEntitiesOfClass(ShrineEntity.class, new AABB(mobPos).inflate(10.0)),
                                          TargetingConditions.forCombat(),
                                          challengeMob,
                                          (double)mobPos.getX() + 0.5,
                                          (double)mobPos.getY(),
                                          (double)mobPos.getZ() + 0.5
                                       );
                                       if (challengeMobTarget != null) {
                                          level.getEntitiesOfClass(Mob.class, new AABB(pos).inflate(10.0))
                                             .forEach(
                                                mob -> {
                                                   if (mob.getPersistentData().contains("challengeMob")
                                                      && mob.getPersistentData().getString("challengeMob").equals("Protect the Shrine")) {
                                                      mob.setTarget(challengeMobTarget);
                                                   }
                                                }
                                             );
                                       }
                                    }
                                 } else {
                                    if (shrineBlockEntity.getPersistentData().contains("challengeState")) {
                                       String challengeState = shrineBlockEntity.getPersistentData().getString("challengeState");
                                       if (challengeState.equals("failed")) {
                                          return;
                                       }
                                    }

                                    this.completeChallenge(state, level, pos, player, true, true);
                                 }
                              }
                           }
                        );
                  }
               }
            );
            TaskScheduler.scheduleTask(task);
         }
      }
   }

   public long getPackedTime() {
      long time = System.currentTimeMillis() / 1000L;
      return time / 60L;
   }

   public void setCooldown(Level level, BlockPos pos, UUID player) {
      if (!this.challengeCausesCooldown) {
         this.challengeCausesCooldown = true;
      } else {
         DungeonSaveData.getOverworld(level).setShrineCooldown(player, pos, this.getPackedTime());
      }
   }

   public void failChallenge(Level level, BlockPos pos, Player player) {
      ShrineBlockEntity shrineBlockEntity = (ShrineBlockEntity)level.getBlockEntity(pos);
      if (shrineBlockEntity != null) {
         boolean cooldownRun = !DungeonSaveData.getOverworld(level).canUseShrine(player, pos, this.getPackedTime(), level);
         if (!cooldownRun) {
            this.setCooldown(level, pos, player.getUUID());
         }

         shrineBlockEntity.getPersistentData().putString("challengeState", "failed");
         player.getPersistentData().remove("shrineTier");
         level.getEntitiesOfClass(Mob.class, new AABB(pos).inflate(32.0)).forEach(mob -> {
            if (mob.getPersistentData().contains("challengeMob")) {
               mob.discard();
            }
         });
         player.sendSystemMessage(Component.translatable("trbeyond.message.shrine.challenge_failed"));
         ChallengeEventHandler.getPlayerCapability(player).ifPresent(capability -> {
            if (capability.getChallengeType() != null) {
               player.getPersistentData().putInt("challenge_time", capability.getChallengeType().getTimeLimit());
            }
         });
         if (!this.dontDecrementNextFailure && !cooldownRun) {
            DungeonCapability.getFrom(player).ifPresent(cap -> {
               int penalty = (Integer)ServerConfig.INSTANCE.dungeonConfig.shrineFailPenalty.get();
               cap.setChallengeCompletionCount(this.tier, Math.max(cap.getChallengeCompletionCount(this.tier) - penalty, 0));
            });
         } else {
            this.dontDecrementNextFailure = false;
         }
      }
   }

   public void completeChallenge(BlockState state, Level level, BlockPos pos, Player player, boolean incrementCompletions, boolean spawnChests) {
      ShrineBlockEntity shrineBlockEntity = (ShrineBlockEntity)level.getBlockEntity(pos);
      this.dontDecrementNextFailure = false;
      boolean cooldownRun = !DungeonSaveData.getOverworld(level).canUseShrine(player, pos, this.getPackedTime(), level);
      if (shrineBlockEntity != null) {
         shrineBlockEntity.getPersistentData().putString("challengeState", "completed");
         player.getPersistentData().remove("shrineTier");
         level.getEntitiesOfClass(Mob.class, new AABB(pos).inflate(32.0)).forEach(mob -> {
            if (mob.getPersistentData().contains("challengeMob")) {
               mob.discard();
            }
         });
         ChallengeEventHandler.getPlayerCapability(player).ifPresent(capability -> {
            if (capability.getChallengeType() != null) {
               capability.getChallengeType().setCountDone(capability.getChallengeType().getCount());
            }
         });
         int completedChallenges = 0;
         if (DungeonCapability.getFrom(player).isPresent()) {
            IDungeonCapability cap = (IDungeonCapability)DungeonCapability.getFrom(player).resolve().get();
            completedChallenges = cap.getChallengeCompletionCount(this.tier);
            if (incrementCompletions && !cooldownRun) {
               cap.setChallengeCompletionCount(this.tier, completedChallenges + 1);
            }
         }

         List<BlockPos> chestPositions = List.of(
            pos.relative((Direction)state.getValue(FACING), 2),
            pos.relative((Direction)state.getValue(FACING), 2).relative(((Direction)state.getValue(FACING)).getClockWise(), 3),
            pos.relative((Direction)state.getValue(FACING), 2).relative(((Direction)state.getValue(FACING)).getCounterClockWise(), 3)
         );
         player.sendSystemMessage(Component.translatable("trbeyond.message.shrine.challenge_completed"));
         if (spawnChests && !cooldownRun) {
            this.setCooldown(level, pos, player.getUUID());
            player.sendSystemMessage(Component.translatable("trbeyond.message.shrine.challenge_completed.chests"));

            Block chestBlock = switch (this.tier) {
               case BRONZE -> (Block)BeyondBlocks.BRONZE_CHEST.get();
               case SILVER -> (Block)BeyondBlocks.SILVER_CHEST.get();
               case GOLD -> (Block)BeyondBlocks.GOLD_CHEST.get();
               case STELLAR -> (Block)BeyondBlocks.STELLAR_CHEST.get();
            };
            int chestCount = completedChallenges >= 20 ? 3 : (completedChallenges >= 10 ? 2 : 1);

            float coinDoubleChance;
            float var25 = coinDoubleChance = switch (completedChallenges) {
               case 0 -> 0.0F;
               case 1 -> 0.05F;
               case 2 -> 0.1F;
               default -> 0.15F;
            };

            for (int i = 0; i < chestCount; i++) {
               BlockPos chestPos = chestPositions.get(i);
               level.setBlock(chestPos, chestBlock.defaultBlockState(), 3);
               DungeonChestBlock dun = (DungeonChestBlock)level.getBlockState(chestPos).getBlock();
               BlockPos chestPos2 = chestPos.relative(DungeonChestBlock.getConnectedDirection(level.getBlockState(chestPos)));
               level.setBlock(
                  chestPos2,
                  (BlockState)dun.defaultBlockState().setValue(DungeonChestBlock.TYPE, ((ChestType)level.getBlockState(chestPos).getValue(DungeonChestBlock.TYPE)).getOpposite()),
                  3
               );
               level.getBlockState(chestPos).updateNeighbourShapes(level, chestPos, 3);
               if (level.getBlockEntity(chestPos) instanceof DungeonChestBlockEntity chest) {
                  chest.getPersistentData().putBoolean("noLock", true);
                  chest.getPersistentData().putLong("placedTime", level.getGameTime());
               }

               DungeonSaveData.getOverworld(level).addBossChest(chestPos);

               for (int p = 1; p < 2; p++) {
                  BlockEntity lootTable = level.getBlockEntity(chestPos2);
                  if (lootTable instanceof DungeonChestBlockEntity) {
                     DungeonChestBlockEntity chest = (DungeonChestBlockEntity)lootTable;

                     String lootTablex = switch (this.tier) {
                        case BRONZE -> "shrines/shrine_bronze_1";
                        case SILVER -> "shrines/shrine_silver_1";
                        case GOLD -> "shrines/shrine_gold_1";
                        case STELLAR -> "shrines/shrine_stellar_1";
                     };
                     chest.setLootTable(new ResourceLocation("trbeyond", lootTablex), level.getRandom().nextLong());
                     chest.unpackLootTable(player);
                     chest.setChanged();
                     if (p == 1) {
                        chest.getCapability(ForgeCapabilities.ITEM_HANDLER)
                           .ifPresent(
                              h -> {
                                 for (int j = 0; j < h.getSlots(); j++) {
                                    ItemStack stack = h.getStackInSlot(j);
                                    if (!stack.isEmpty()
                                       && (
                                          stack.getItem() instanceof TensuraSword
                                             || stack.getItem() instanceof GeoArmorItem
                                             || stack.getItem() instanceof SimpleArmorItem
                                             || stack.getItem() instanceof SimpleSwordItem
                                             || stack.getItem() instanceof SimpleBowItem
                                       )
                                       && Math.random() < 0.5) {
                                       stack = h.extractItem(j, 1, false);
                                       EngravingEnchantment.randomEngraving(
                                          player, stack, (double)((Integer)TensuraConfig.INSTANCE.enchantmentsConfig.unique.get()).intValue()
                                       );
                                       h.insertItem(j, stack, false);
                                    }

                                    if (Math.random() < (double)coinDoubleChance
                                       && (
                                          stack.is((Item)TensuraMaterialItems.BRONZE_COIN.get())
                                             || stack.is((Item)TensuraMaterialItems.SILVER_COIN.get())
                                             || stack.is((Item)TensuraMaterialItems.GOLD_COIN.get())
                                             || stack.is((Item)TensuraMaterialItems.STELLAR_GOLD_COIN.get())
                                       )) {
                                       stack.setCount(Math.min(64, stack.getCount() * 2));
                                    }
                                 }
                              }
                           );
                     }
                  }
               }
            }
         }
      }
   }

   public BlockState getStateForPlacement(BlockPlaceContext pContext) {
      Direction direction = pContext.getHorizontalDirection().getOpposite();
      return (BlockState)this.defaultBlockState().setValue(FACING, direction);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> blockBlockStateBuilder) {
      blockBlockStateBuilder.add(new Property[]{FACING});
   }

   public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
      return (BlockState)state.setValue(FACING, direction.rotate((Direction)state.getValue(FACING)));
   }

   @NotNull
   public BlockState mirror(BlockState blockState, LevelAccessor level, BlockPos pos, Mirror mirror) {
      return blockState.rotate(level, pos, mirror.getRotation((Direction)blockState.getValue(FACING)));
   }

   @NotNull
   public RenderShape getRenderShape() {
      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   @Nullable
   public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
      return new ShrineBlockEntity(pPos, pState);
   }

   public ShrineBlock.DungeonShrineTier getTier() {
      return this.tier;
   }

   public static enum DungeonShrineTier {
      BRONZE,
      SILVER,
      GOLD,
      STELLAR;
   }

   public static class WaveThemeProvider {
      public static final WeightedRandomList<Wrapper<WeightedRandomList<Wrapper<EntityType<? extends Mob>>>>> bronzeWaveList;
      public static final WeightedRandomList<Wrapper<WeightedRandomList<Wrapper<EntityType<? extends Mob>>>>> silverWaveList;
      public static final WeightedRandomList<Wrapper<WeightedRandomList<Wrapper<EntityType<? extends Mob>>>>> goldWaveList;
      public static final WeightedRandomList<Wrapper<WeightedRandomList<Wrapper<EntityType<? extends Mob>>>>> stellarWaveList;

      public static WeightedRandomList<Wrapper<WeightedRandomList<Wrapper<EntityType<? extends Mob>>>>> themesProvider(ShrineBlock.DungeonShrineTier tier) {
         return switch (tier) {
            case BRONZE -> bronzeWaveList;
            case SILVER -> silverWaveList;
            case GOLD -> goldWaveList;
            case STELLAR -> stellarWaveList;
            default -> null;
         };
      }

      static {
         WeightedRandomList<Wrapper<EntityType<? extends Mob>>> allianceTheme = WeightedRandomList.create(
            new Wrapper[]{
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.GOBLIN.get(), 1),
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.ORC.get(), 1),
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.LIZARDMAN.get(), 1),
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.DIREWOLF.get(), 1)
            }
         );
         WeightedRandomList<Wrapper<EntityType<? extends Mob>>> bearTheme = WeightedRandomList.create(
            new Wrapper[]{
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.GIANT_BEAR.get(), 1),
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.HORNED_BEAR.get(), 1)
            }
         );
         WeightedRandomList<Wrapper<EntityType<? extends Mob>>> reptileTheme = WeightedRandomList.create(
            new Wrapper[]{
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.HOVER_LIZARD.get(), 3),
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.ARMOURSAURUS.get(), 1),
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.LEECH_LIZARD.get(), 3)
            }
         );
         WeightedRandomList<Wrapper<EntityType<? extends Mob>>> slimeTheme = WeightedRandomList.create(
            new Wrapper[]{
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.SLIME.get(), 10),
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.METAL_SLIME.get(), 1),
               WeightedEntry.wrap((EntityType)BeyondEntities.BLUE_SLIME_DOLL.get(), 1),
               WeightedEntry.wrap((EntityType)BeyondEntities.GREEN_SLIME_DOLL.get(), 1),
               WeightedEntry.wrap((EntityType)BeyondEntities.RED_SLIME_DOLL.get(), 1),
               WeightedEntry.wrap((EntityType)BeyondEntities.YELLOW_SLIME_DOLL.get(), 1)
            }
         );
         WeightedRandomList<Wrapper<EntityType<? extends Mob>>> basicSlimeTheme = WeightedRandomList.create(
            new Wrapper[]{WeightedEntry.wrap((EntityType)TensuraEntityTypes.SLIME.get(), 1)}
         );
         WeightedRandomList<Wrapper<EntityType<? extends Mob>>> spiritTheme = WeightedRandomList.create(
            new Wrapper[]{
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.AQUA_FROG.get(), 1),
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.BEAST_GNOME.get(), 1),
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.FEATHERED_SERPENT.get(), 1),
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.SALAMANDER.get(), 1),
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.WINGED_CAT.get(), 1)
            }
         );
         WeightedRandomList<Wrapper<EntityType<? extends Mob>>> wolfTheme = WeightedRandomList.create(
            new Wrapper[]{
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.BARGHEST.get(), 1),
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.DIREWOLF.get(), 1),
               WeightedEntry.wrap((EntityType)TensuraEntityTypes.HOUND_DOG.get(), 1)
            }
         );
         WeightedRandomList<Wrapper<EntityType<? extends Mob>>> vanillaTheme = WeightedRandomList.create(
            new Wrapper[]{
               WeightedEntry.wrap(EntityType.ZOMBIE, 1),
               WeightedEntry.wrap(EntityType.SKELETON, 1),
               WeightedEntry.wrap(EntityType.HUSK, 1),
               WeightedEntry.wrap(EntityType.STRAY, 1),
               WeightedEntry.wrap(EntityType.SPIDER, 1),
               WeightedEntry.wrap(EntityType.CAVE_SPIDER, 1)
            }
         );
         WeightedRandomList<Wrapper<EntityType<? extends Mob>>> pillagerTheme = WeightedRandomList.create(
            new Wrapper[]{
               WeightedEntry.wrap(EntityType.PILLAGER, 10),
               WeightedEntry.wrap(EntityType.VINDICATOR, 5),
               WeightedEntry.wrap(EntityType.WITCH, 3),
               WeightedEntry.wrap(EntityType.VEX, 3),
               WeightedEntry.wrap(EntityType.RAVAGER, 1),
               WeightedEntry.wrap(EntityType.ILLUSIONER, 1),
               WeightedEntry.wrap(EntityType.EVOKER, 1)
            }
         );
         WeightedRandomList<Wrapper<EntityType<? extends Mob>>> netherTheme = WeightedRandomList.create(
            new Wrapper[]{
               WeightedEntry.wrap(EntityType.WITHER_SKELETON, 1),
               WeightedEntry.wrap(EntityType.BLAZE, 1),
               WeightedEntry.wrap(EntityType.MAGMA_CUBE, 1)
            }
         );
         WeightedRandomList<Wrapper<EntityType<? extends Mob>>> endTheme = WeightedRandomList.create(
            new Wrapper[]{WeightedEntry.wrap(EntityType.ENDERMAN, 5), WeightedEntry.wrap(EntityType.SHULKER, 1)}
         );
         WeightedRandomList<Wrapper<EntityType<? extends Mob>>> golemTheme = WeightedRandomList.create(
            new Wrapper[]{WeightedEntry.wrap(EntityType.IRON_GOLEM, 1)}
         );
         bronzeWaveList = WeightedRandomList.create(
            new Wrapper[]{
               WeightedEntry.wrap(allianceTheme, 1),
               WeightedEntry.wrap(vanillaTheme, 1),
               WeightedEntry.wrap(netherTheme, 1),
               WeightedEntry.wrap(reptileTheme, 1),
               WeightedEntry.wrap(basicSlimeTheme, 1),
               WeightedEntry.wrap(wolfTheme, 1)
            }
         );
         silverWaveList = WeightedRandomList.create(
            new Wrapper[]{
               WeightedEntry.wrap(allianceTheme, 1),
               WeightedEntry.wrap(golemTheme, 1),
               WeightedEntry.wrap(pillagerTheme, 1),
               WeightedEntry.wrap(endTheme, 1),
               WeightedEntry.wrap(reptileTheme, 1),
               WeightedEntry.wrap(wolfTheme, 1)
            }
         );
         goldWaveList = WeightedRandomList.create(
            new Wrapper[]{
               WeightedEntry.wrap(allianceTheme, 1),
               WeightedEntry.wrap(reptileTheme, 1),
               WeightedEntry.wrap(slimeTheme, 1),
               WeightedEntry.wrap(wolfTheme, 1),
               WeightedEntry.wrap(bearTheme, 1)
            }
         );
         stellarWaveList = WeightedRandomList.create(
            new Wrapper[]{
               WeightedEntry.wrap(allianceTheme, 3),
               WeightedEntry.wrap(reptileTheme, 3),
               WeightedEntry.wrap(slimeTheme, 3),
               WeightedEntry.wrap(wolfTheme, 3),
               WeightedEntry.wrap(bearTheme, 3),
               WeightedEntry.wrap(spiritTheme, 1)
            }
         );
      }
   }
}
