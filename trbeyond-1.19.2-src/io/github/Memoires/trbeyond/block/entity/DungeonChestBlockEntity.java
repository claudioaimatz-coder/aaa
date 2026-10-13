package io.github.Memoires.trbeyond.block.entity;

import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.network.worldgen.DungeonSaveData;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class DungeonChestBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity {
   public NonNullList<ItemStack> items;
   private final ContainerOpenersCounter openersCounter;
   private final ChestLidController chestLidController;
   private long placedTime = -1L;
   public final Set<UUID> playersWithGeneratedLoot = new HashSet<>();
   private ResourceLocation lootTable;

   public DungeonChestBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
      super(blockEntityType, blockPos, blockState);
      this.items = NonNullList.withSize(27, ItemStack.EMPTY);
      this.openersCounter = this.createOpenersCounter();
      this.chestLidController = new ChestLidController();
   }

   private ContainerOpenersCounter createOpenersCounter() {
      return new ContainerOpenersCounter() {
         protected void onOpen(Level level, BlockPos pos, BlockState blockState) {
            DungeonChestBlockEntity.playSound(level, pos, blockState, SoundEvents.CHEST_OPEN);
         }

         protected void onClose(Level level, BlockPos pos, BlockState blockState) {
            DungeonChestBlockEntity.playSound(level, pos, blockState, SoundEvents.CHEST_CLOSE);
         }

         protected void openerCountChanged(Level level, BlockPos pos, BlockState blockState, int previousCount, int newCount) {
            DungeonChestBlockEntity.this.signalOpenCount(level, pos, blockState, previousCount, newCount);
         }

         protected boolean isOwnContainer(Player player) {
            if (!(player.containerMenu instanceof ChestMenu)) {
               return false;
            } else {
               Container container = ((ChestMenu)player.containerMenu).getContainer();
               if (container == DungeonChestBlockEntity.this) {
                  return true;
               } else {
                  if (container instanceof CompoundContainer cc && cc.contains(DungeonChestBlockEntity.this)) {
                     return true;
                  }

                  return false;
               }
            }
         }
      };
   }

   public void load(CompoundTag tag) {
      this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
      if (!this.tryLoadLootTable(tag)) {
         ContainerHelper.loadAllItems(tag, this.items);
      }

      if (tag.contains("PlayersWithLoot", 9)) {
         for (Tag playerTag : tag.getList("PlayersWithLoot", 11)) {
            this.playersWithGeneratedLoot.add(NbtUtils.loadUUID(playerTag));
         }
      }

      if (tag.contains("placedTime")) {
         this.placedTime = tag.getLong("placedTime");
      }

      super.load(tag);
   }

   protected void saveAdditional(CompoundTag tag) {
      if (!this.trySaveLootTable(tag)) {
         ContainerHelper.saveAllItems(tag, this.items);
      }

      ListTag playerList = new ListTag();

      for (UUID playerId : this.playersWithGeneratedLoot) {
         playerList.add(NbtUtils.createUUID(playerId));
      }

      tag.put("PlayersWithLoot", playerList);
      tag.putLong("placedTime", this.placedTime);
      super.saveAdditional(tag);
   }

   public void startOpen(Player player) {
      if (!this.remove && !player.isSpectator()) {
         this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
         if ((Boolean)ServerConfig.INSTANCE.dungeonConfig.perPlayerLoot.get()
            && !this.level.isClientSide
            && !this.playersWithGeneratedLoot.contains(player.getUUID())) {
            this.generateLoot(player);
            this.playersWithGeneratedLoot.add(player.getUUID());
            this.setChanged();
         }
      }
   }

   public void stopOpen(Player player) {
      if (!this.remove && !player.isSpectator()) {
         this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
      }
   }

   private void generateLoot(Player player) {
      if (this.lootTable != null) {
         this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
         Builder builder = new Builder((ServerLevel)this.level)
            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition))
            .withParameter(LootContextParams.THIS_ENTITY, player)
            .withLuck(player.getLuck());
         this.level.getServer().m_129898_().get(this.lootTable).fill(this, builder.create(LootContextParamSets.CHEST));
      }
   }

   public void setLootTable(ResourceLocation lootTable) {
      this.lootTable = lootTable;
      this.setChanged();
   }

   public static void tick(Level level, BlockPos pos, BlockState state, DungeonChestBlockEntity blockEntity) {
      blockEntity.chestLidController.tickLid();
      if (level.getGameTime() % 600L == 0L) {
         if (!level.isClientSide && DungeonSaveData.getOverworld(level).getBossChests().contains(pos)) {
            if (blockEntity.placedTime < 0L) {
               blockEntity.placedTime = level.getGameTime();
            } else if (level.getGameTime() >= blockEntity.placedTime + 6000L) {
               level.removeBlock(pos, false);
               DungeonSaveData.getOverworld(level).removeBossChest(pos);
            }
         }
      }
   }

   public float getOpenNess(float partialTicks) {
      return this.chestLidController.getOpenness(partialTicks);
   }

   public int getContainerSize() {
      return 27;
   }

   public void wasPlaced(@Nullable LivingEntity livingEntity, ItemStack stack) {
   }

   protected NonNullList<ItemStack> getItems() {
      return this.items;
   }

   protected void setItems(NonNullList<ItemStack> itemsIn) {
      this.items = itemsIn;
   }

   protected Component getDefaultName() {
      return Component.translatable("container.chest");
   }

   protected AbstractContainerMenu createMenu(int id, Inventory player) {
      return ChestMenu.threeRows(id, player, this);
   }

   public void recheckOpen() {
      if (!this.remove) {
         this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
      }
   }

   static void playSound(Level level, BlockPos pos, BlockState state, SoundEvent sound) {
      double x = (double)pos.getX() + 0.5;
      double y = (double)pos.getY() + 0.5;
      double z = (double)pos.getZ() + 0.5;
      level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
   }

   protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int prevOpenCount, int openCount) {
      Block block = state.getBlock();
      level.blockEvent(pos, block, 1, openCount);
   }

   public boolean triggerEvent(int id, int param) {
      if (id == 1) {
         this.chestLidController.shouldBeOpen(param > 0);
         return true;
      } else {
         return super.triggerEvent(id, param);
      }
   }
}
