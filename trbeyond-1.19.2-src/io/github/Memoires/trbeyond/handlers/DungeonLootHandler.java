package io.github.Memoires.trbeyond.handlers;

import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import com.github.manasmods.tensura.registry.items.TensuraArmorItems;
import com.github.manasmods.tensura.registry.items.TensuraConsumableItems;
import com.github.manasmods.tensura.registry.items.TensuraMaterialItems;
import com.github.manasmods.tensura.registry.items.TensuraMobDropItems;
import com.github.manasmods.tensura.registry.items.TensuraToolItems;
import io.github.Memoires.trbeyond.block.DungeonChestBlock;
import io.github.Memoires.trbeyond.block.entity.DungeonChestBlockEntity;
import io.github.Memoires.trbeyond.capability.dungeon.DungeonCapability;
import io.github.Memoires.trbeyond.capability.dungeon.IDungeonCapability;
import io.github.Memoires.trbeyond.capability.entities.EliteMobCapability;
import io.github.Memoires.trbeyond.capability.entities.IEliteMobCapability;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.data.BeyondTags;
import io.github.Memoires.trbeyond.entity.custom.BossBlackSpiderEntity;
import io.github.Memoires.trbeyond.network.worldgen.DungeonSaveData;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlocks;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import io.github.Memoires.trbeyond.registry.items.BeyondToolItems;
import io.github.Memoires.trbeyond.world.BeyondGamerules;
import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.FORGE
)
public class DungeonLootHandler {
   @SubscribeEvent
   public static void onLivingDrops(LivingDropsEvent event) {
      LivingEntity entity = event.getEntity();
      if (!(entity instanceof Player)) {
         if (entity.getLevel().dimension() == BeyondDungeon.DUNGEON_LEVEL_KEY) {
            event.setCanceled(true);
         }
      }
   }

   @SubscribeEvent(
      priority = EventPriority.LOWEST
   )
   public static void onDeathLowPriority(LivingDeathEvent e) {
      if (!e.isCanceled()) {
         LivingEntity entity = e.getEntity();
         Level level = entity.getLevel();
         if (!level.isClientSide()) {
            if (level.dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY) && !(entity instanceof Player)) {
               if (entity.getType().is(BeyondTags.DUNGEON_BOSS) && e.getSource().getEntity() instanceof LivingEntity attacker) {
                  if (SkillHelper.getSubordinateOwner(attacker) instanceof Player player) {
                     attacker = player;
                  }

                  if (attacker instanceof Player player) {
                     DungeonSaveData saveData = DungeonSaveData.getOverworld(level);
                     if (saveData != null && !saveData.isOnBossCooldown(entity, player)) {
                        saveData.setBossCooldown(entity, player, 72000);
                        BlockPos chestPos = entity.blockPosition();
                        BlockPos chestPos2 = entity.blockPosition().below(1);

                        for (int i = 0; i < 10; i++) {
                           chestPos = entity.blockPosition().below(i);
                           if (!level.getBlockState(chestPos).isAir()
                              || !level.getBlockState(chestPos.relative(Direction.NORTH)).isAir()
                              || !level.getBlockState(chestPos.relative(Direction.EAST)).isAir()
                              || !level.getBlockState(chestPos.relative(Direction.SOUTH)).isAir()
                              || !level.getBlockState(chestPos.relative(Direction.WEST)).isAir()) {
                              chestPos = chestPos.above();
                              break;
                           }
                        }

                        if (level.getBlockState(chestPos).isAir()) {
                           level.setBlock(chestPos, ((Block)BeyondBlocks.GOLD_CHEST.get()).defaultBlockState(), 3);
                           DungeonChestBlock dun = (DungeonChestBlock)level.getBlockState(chestPos).getBlock();
                           chestPos2 = chestPos.relative(DungeonChestBlock.getConnectedDirection(level.getBlockState(chestPos)));
                           level.setBlock(
                              chestPos2,
                              (BlockState)dun.defaultBlockState()
                                 .setValue(DungeonChestBlock.TYPE, ((ChestType)level.getBlockState(chestPos).getValue(DungeonChestBlock.TYPE)).getOpposite()),
                              3
                           );
                           level.getBlockState(chestPos).updateNeighbourShapes(level, chestPos, 3);
                           if (level.getBlockEntity(chestPos) instanceof DungeonChestBlockEntity chest) {
                              chest.getPersistentData().putLong("placedTime", level.getGameTime());
                           }

                           DungeonSaveData.getOverworld(level).addBossChest(chestPos);
                        }

                        for (int ix = 0; ix < 2; ix++) {
                           BlockEntity var27 = level.getBlockEntity(ix == 0 ? chestPos : chestPos2);
                           if (var27 instanceof DungeonChestBlockEntity) {
                              DungeonChestBlockEntity chest = (DungeonChestBlockEntity)var27;
                              chest.setLootTable(
                                 ResourceLocation.of((String)ServerConfig.INSTANCE.dungeonConfig.bossChestLoot.get(), ':'),
                                 level.getRandom().nextLong()
                              );
                              chest.unpackLootTable(player);
                              chest.setChanged();
                              if (ix == 0) {
                                 chest.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
                                    if (level.getRandom().nextFloat() < 0.05F) {
                                       ItemStack loot = new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get());
                                       if (entity instanceof BossBlackSpiderEntity) {
                                          loot = new ItemStack((ItemLike)BeyondToolItems.SPIDER_DAGGER_SPECIAL.get());
                                       }

                                       for (int j = 0; j < h.getSlots(); j++) {
                                          if (h.getStackInSlot(j).isEmpty()) {
                                             h.insertItem(j, loot, false);
                                             break;
                                          }
                                       }
                                    }
                                 });
                              }
                           }
                        }
                     }
                  }
               }

               float chance = (float)entity.level.getGameRules().getInt(BeyondGamerules.LOOT_EXPLOSION_CHANCE) / 1000.0F;
               if (entity.getPersistentData().contains("challengeMob")
                  && e.getSource().getEntity() instanceof Player playerx
                  && DungeonCapability.getFrom(playerx).isPresent()) {
                  chance += (float)((double)((IDungeonCapability)DungeonCapability.getFrom(playerx).resolve().get()).getTotalChallengeCompletionCount() * 0.001);
               }

               boolean loot = entity.getRandom().nextFloat() < chance;
               EntityType<?> entityType = entity.getType();
               if (entityType.is(BeyondTags.LOOT_T1)) {
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get()), 0.01F, 1, 3, loot);
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraConsumableItems.LOW_POTION.get()), 5.0E-4F, 1, 1, loot);
               } else if (entityType.is(BeyondTags.LOOT_T2)) {
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get()), 0.02F, 1, 3, loot);
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraConsumableItems.LOW_POTION.get()), 0.01F, 1, 1, loot);
               } else if (entityType.is(BeyondTags.LOOT_T3)) {
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get()), 0.03F, 1, 3, loot);
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get()), 0.001F, 1, 1, loot);
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraConsumableItems.LOW_POTION.get()), 0.01F, 1, 1, false);
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraConsumableItems.HIGH_POTION.get()), 0.005F, 1, 1, loot);
                  spawnLoot(entity, DungeonLootHandler.LootTables.T3_GEAR, 0.01F, 1, 1, loot);
                  spawnLoot(entity, DungeonLootHandler.LootTables.ELEMENTAL_CORES, 0.005F, 1, 1, false);
               } else if (entityType.is(BeyondTags.LOOT_T4)) {
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get()), 0.02F, 1, 3, loot);
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get()), 0.01F, 1, 1, loot);
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraConsumableItems.HIGH_POTION.get()), 0.005F, 1, 1, loot);
               } else if (entityType.is(BeyondTags.LOOT_T5)) {
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get()), 0.04F, 5, 10, loot);
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get()), 0.005F, 1, 3, loot);
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraConsumableItems.HIGH_POTION.get()), 0.02F, 1, 1, loot);
                  spawnLoot(entity, DungeonLootHandler.LootTables.T5_GEAR, 0.01F, 1, 1, loot);
                  spawnLoot(entity, DungeonLootHandler.LootTables.ELEMENTAL_CORES, 0.01F, 1, 1, false);
               } else if (entityType.is(BeyondTags.LOOT_T6)) {
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get()), 0.04F, 5, 10, loot);
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get()), 0.005F, 1, 10, loot);
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraConsumableItems.HIGH_POTION.get()), 0.02F, 1, 1, loot);
                  spawnLoot(entity, DungeonLootHandler.LootTables.T6_GEAR, 0.05F, 1, 1, loot);
                  spawnLoot(entity, DungeonLootHandler.LootTables.ELEMENTAL_CORES, 0.05F, 1, 1, loot);
               } else if (entityType.is(BeyondTags.LOOT_T7)) {
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get()), 1.0F, 1, 10, loot);
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraConsumableItems.FULL_POTION.get()), 0.5F, 1, 1, loot);
                  spawnLoot(entity, DungeonLootHandler.LootTables.T7_GEAR, 0.1F, 1, 1, loot);
                  spawnLoot(entity, DungeonLootHandler.LootTables.ELEMENTAL_CORES, 0.1F, 1, 1, loot);
               } else if (entityType == BeyondEntities.BOSS_BLACK_SPIDER.get()) {
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get()), 0.5F, 1, 2, loot);
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraConsumableItems.HIGH_POTION.get()), 0.2F, 1, 1, loot);
                  spawnLoot(entity, DungeonLootHandler.LootTables.T7_GEAR, 0.1F, 1, 1, false);
                  spawnLoot(entity, new ItemStack((ItemLike)BeyondToolItems.SPIDER_DAGGER_RARE.get()), 0.15F, 1, 1, loot);
               }

               if (entityType == TensuraEntityTypes.BLADE_TIGER.get()) {
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraToolItems.BLADE_TIGER_SCYTHE.get()), 0.05F, 1, 1, loot);
               } else if (entityType.is(BeyondTags.SLIME_DOLLS)) {
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraMobDropItems.SLIME_CORE.get()), 0.01F, 1, 1, loot);
               } else if (entityType == BeyondEntities.SLIME_DOLL_BOSS.get()) {
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraMobDropItems.SLIME_CORE.get()), 0.05F, 1, 1, loot);
               } else if (entityType == BeyondEntities.STELLAR_MIMIC_BOSS.get()) {
                  spawnLoot(entity, new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get()), 0.05F, 1, 1, loot);
               }

               if (EliteMobCapability.getFrom(entity).map(IEliteMobCapability::isElite).orElse(false)) {
                  MinecraftServer server = level.getServer();
                  ServerLevel serverLevel;
                  if (server != null && (serverLevel = server.m_129880_(level.dimension())) != null) {
                     LootTable lootTable;
                     if (entity.getPersistentData().contains("")) {
                        lootTable = level.getServer().m_129898_().get(new ResourceLocation("trbeyond", "elites/shrine_elite"));
                     } else {
                        lootTable = level.getServer().m_129898_().get(new ResourceLocation("trbeyond", "elites/dungeon_elite"));
                     }

                     for (ItemStack item : lootTable.getRandomItems(new Builder(serverLevel).create(LootContextParamSets.EMPTY))) {
                        entity.spawnAtLocation(item);
                     }
                  }
               }

               if (loot) {
                  lootExplosion(entity);
               }
            }
         }
      }
   }

   private static void spawnLoot(LivingEntity entity, Object itemOrList, float chance, int min, int max, boolean loot) {
      Random random = new Random();
      if (loot) {
         chance = 1.0F;
         if (itemOrList instanceof ItemStack itemStack) {
            if (itemStack.getItem() != TensuraMaterialItems.BRONZE_COIN.get()
               && !itemStack.is((Item)TensuraMaterialItems.SILVER_COIN.get())
               && !itemStack.is((Item)TensuraMaterialItems.GOLD_COIN.get())) {
               min++;
               max += 2;
            } else {
               min *= 10;
               max *= 10;
            }
         } else if (itemOrList instanceof List) {
            min++;
            max += 2;
         }
      }

      if (EliteMobCapability.getFrom(entity).map(IEliteMobCapability::isElite).orElse(false)) {
         chance = Math.min(chance * 10.0F, 1.0F);
      }

      if (!(random.nextFloat() > chance)) {
         int amount = getRandomInRange(min, max);
         if (itemOrList instanceof ItemStack item) {
            if (item.getItem().getMaxStackSize() > 1) {
               while (amount > 0) {
                  item.setCount(Math.min(amount, item.getMaxStackSize()));
                  amount -= item.getCount();
                  entity.spawnAtLocation(item);
               }
            } else {
               for (int i = 0; i < amount; i++) {
                  entity.spawnAtLocation(item);
               }
            }
         } else if (itemOrList instanceof List<?> list && !list.isEmpty()) {
            Object chosenItem = list.get(random.nextInt(list.size()));
            if (chosenItem instanceof ItemStack itemx) {
               if (itemx.getItem().getMaxStackSize() > 1) {
                  while (amount > 0) {
                     itemx.setCount(Math.min(amount, itemx.getMaxStackSize()));
                     amount -= itemx.getCount();
                     entity.spawnAtLocation(itemx);
                  }
               } else {
                  for (int i = 0; i < amount; i++) {
                     entity.spawnAtLocation(itemx);
                  }
               }
            } else if (chosenItem instanceof List) {
               spawnLoot(entity, chosenItem, 1.0F, amount, amount, false);
            }
         }
      }
   }

   private static void lootExplosion(LivingEntity entity) {
      entity.level.playSound(null, entity.blockPosition(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.HOSTILE, 1.0F, 1.0F);
      entity.level.playSound(null, entity.blockPosition(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.HOSTILE, 1.0F, 1.0F);
      if (entity.getLevel() instanceof ServerLevel serverLevel) {
         ItemStack firework = new ItemStack(Items.FIREWORK_ROCKET);
         CompoundTag tag = new CompoundTag();
         CompoundTag explosion = new CompoundTag();
         int[] colors = new int[]{16711680, 16744192, 16776960, 65280, 255, 4915330, 9109759};
         explosion.putIntArray("Colors", colors);
         explosion.putBoolean("Flicker", true);
         explosion.putBoolean("Trail", true);
         explosion.putByte("Type", (byte)0);
         ListTag explosions = new ListTag();
         explosions.add(explosion);
         CompoundTag fireworksTag = new CompoundTag();
         fireworksTag.put("Explosions", explosions);
         fireworksTag.putByte("Flight", (byte)1);
         tag.put("Fireworks", fireworksTag);
         firework.setTag(tag);
         FireworkRocketEntity fireworkEntity = new FireworkRocketEntity(serverLevel, firework, entity.getX(), entity.getY(), entity.getZ(), true);
         serverLevel.addFreshEntity(fireworkEntity);
      }

      entity.discard();
   }

   private static ItemStack getCoinDropFor(LivingEntity entity) {
      if (entity.getType().is(BeyondTags.DUNGEON_BOSS)) {
         return new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get());
      } else {
         return !entity.getType().is(BeyondTags.LOOT_T3)
               && !entity.getType().is(BeyondTags.LOOT_T4)
               && !entity.getType().is(BeyondTags.LOOT_T5)
               && !entity.getType().is(BeyondTags.LOOT_T6)
               && !entity.getType().is(BeyondTags.LOOT_T7)
            ? new ItemStack((ItemLike)TensuraMaterialItems.BRONZE_COIN.get())
            : new ItemStack((ItemLike)TensuraMaterialItems.SILVER_COIN.get());
      }
   }

   public static int getRandomInRange(int min, int max) {
      return (int)(Math.random() * (double)(max - min + 1)) + min;
   }

   private static class LootTables {
      private static final List<ItemStack> ANT_ARMOR = List.of(
         new ItemStack((ItemLike)TensuraArmorItems.ANT_CARAPACE_CHESTPLATE.get()),
         new ItemStack((ItemLike)TensuraArmorItems.ANT_CARAPACE_BOOTS.get()),
         new ItemStack((ItemLike)TensuraArmorItems.ANT_CARAPACE_HELMET.get()),
         new ItemStack((ItemLike)TensuraArmorItems.ANT_CARAPACE_LEGGINGS.get())
      );
      private static final List<ItemStack> KNIGHT_SPIDER_ARMOR = List.of(
         new ItemStack((ItemLike)TensuraArmorItems.KNIGHT_SPIDER_CARAPACE_BOOTS.get()),
         new ItemStack((ItemLike)TensuraArmorItems.KNIGHT_SPIDER_CARAPACE_CHESTPLATE.get()),
         new ItemStack((ItemLike)TensuraArmorItems.KNIGHT_SPIDER_CARAPACE_LEGGINGS.get()),
         new ItemStack((ItemLike)TensuraArmorItems.KNIGHT_SPIDER_CARAPACE_HELMET.get())
      );
      private static final List<ItemStack> ARMOURSAURUS_ARMOR = List.of(
         new ItemStack((ItemLike)TensuraArmorItems.ARMOURSAURUS_SCALEMAIL_BOOTS.get()),
         new ItemStack((ItemLike)TensuraArmorItems.ARMOURSAURUS_SCALEMAIL_CHESTPLATE.get()),
         new ItemStack((ItemLike)TensuraArmorItems.ARMOURSAURUS_SCALEMAIL_HELMET.get()),
         new ItemStack((ItemLike)TensuraArmorItems.ARMOURSAURUS_SCALEMAIL_LEGGINGS.get())
      );
      private static final List<ItemStack> SERPENT_ARMOR = List.of(
         new ItemStack((ItemLike)TensuraArmorItems.SERPENT_SCALEMAIL_BOOTS.get()),
         new ItemStack((ItemLike)TensuraArmorItems.SERPENT_SCALEMAIL_CHESTPLATE.get()),
         new ItemStack((ItemLike)TensuraArmorItems.SERPENT_SCALEMAIL_HELMET.get()),
         new ItemStack((ItemLike)TensuraArmorItems.SERPENT_SCALEMAIL_LEGGINGS.get())
      );
      private static final List<ItemStack> LOW_MAGISTEEL_WEAPONS = List.of(
         new ItemStack((ItemLike)TensuraToolItems.LOW_MAGISTEEL_AXE.get()),
         new ItemStack((ItemLike)TensuraToolItems.LOW_MAGISTEEL_SWORD.get()),
         new ItemStack((ItemLike)TensuraToolItems.LOW_MAGISTEEL_SHORT_SWORD.get()),
         new ItemStack((ItemLike)TensuraToolItems.LOW_MAGISTEEL_LONG_SWORD.get()),
         new ItemStack((ItemLike)TensuraToolItems.LOW_MAGISTEEL_GREAT_SWORD.get()),
         new ItemStack((ItemLike)TensuraToolItems.LOW_MAGISTEEL_KATANA.get()),
         new ItemStack((ItemLike)TensuraToolItems.LOW_MAGISTEEL_KODACHI.get()),
         new ItemStack((ItemLike)TensuraToolItems.LOW_MAGISTEEL_TACHI.get()),
         new ItemStack((ItemLike)TensuraToolItems.LOW_MAGISTEEL_ODACHI.get()),
         new ItemStack((ItemLike)TensuraToolItems.LOW_MAGISTEEL_SPEAR.get()),
         new ItemStack((ItemLike)TensuraToolItems.LOW_MAGISTEEL_SCYTHE.get())
      );
      private static final List<ItemStack> LOW_MAGISTEEL_ARMOR = List.of(
         new ItemStack((ItemLike)TensuraArmorItems.LOW_MAGISTEEL_BOOTS.get()),
         new ItemStack((ItemLike)TensuraArmorItems.LOW_MAGISTEEL_CHESTPLATE.get()),
         new ItemStack((ItemLike)TensuraArmorItems.LOW_MAGISTEEL_HELMET.get()),
         new ItemStack((ItemLike)TensuraArmorItems.LOW_MAGISTEEL_LEGGINGS.get())
      );
      private static final List<ItemStack> ELEMENTAL_CORES = List.of(
         new ItemStack((ItemLike)TensuraMaterialItems.ELEMENT_CORE_FIRE.get()),
         new ItemStack((ItemLike)TensuraMaterialItems.ELEMENT_CORE_EARTH.get()),
         new ItemStack((ItemLike)TensuraMaterialItems.ELEMENT_CORE_EMPTY.get()),
         new ItemStack((ItemLike)TensuraMaterialItems.ELEMENT_CORE_SPACE.get()),
         new ItemStack((ItemLike)TensuraMaterialItems.ELEMENT_CORE_WATER.get()),
         new ItemStack((ItemLike)TensuraMaterialItems.ELEMENT_CORE_WIND.get())
      );
      private static final List<Object> T3_GEAR = List.of(
         new ItemStack((ItemLike)TensuraToolItems.SPIDER_BOW.get()),
         new ItemStack((ItemLike)TensuraToolItems.ANT_CROSSBOW.get()),
         new ItemStack((ItemLike)TensuraToolItems.GOBLIN_CLUB.get()),
         ANT_ARMOR,
         KNIGHT_SPIDER_ARMOR
      );
      private static final List<Object> T5_GEAR = List.of(
         new ItemStack((ItemLike)TensuraToolItems.SPIDER_BOW.get()),
         new ItemStack((ItemLike)TensuraToolItems.ANT_CROSSBOW.get()),
         new ItemStack((ItemLike)TensuraToolItems.GOBLIN_CLUB.get()),
         ANT_ARMOR,
         KNIGHT_SPIDER_ARMOR,
         LOW_MAGISTEEL_ARMOR,
         LOW_MAGISTEEL_WEAPONS
      );
      private static final List<Object> T6_GEAR = List.of(
         new ItemStack((ItemLike)TensuraToolItems.KANABO.get()),
         new ItemStack((ItemLike)TensuraToolItems.BEAST_HORN_SPEAR.get()),
         new ItemStack((ItemLike)TensuraToolItems.UNICORN_HORN_SPEAR.get()),
         new ItemStack((ItemLike)TensuraToolItems.ICE_BLADE.get()),
         new ItemStack((ItemLike)TensuraToolItems.VORTEX_SPEAR.get()),
         new ItemStack((ItemLike)TensuraToolItems.TEMPEST_SCALE_KNIFE.get()),
         new ItemStack((ItemLike)TensuraToolItems.TEMPEST_SCALE_SWORD.get()),
         new ItemStack((ItemLike)TensuraToolItems.TEMPEST_SCALE_SHIELD.get()),
         ARMOURSAURUS_ARMOR,
         SERPENT_ARMOR,
         LOW_MAGISTEEL_ARMOR,
         LOW_MAGISTEEL_WEAPONS
      );
      private static final List<Object> T7_GEAR = List.of(
         new ItemStack((ItemLike)TensuraToolItems.KANABO.get()),
         new ItemStack((ItemLike)TensuraToolItems.BEAST_HORN_SPEAR.get()),
         new ItemStack((ItemLike)TensuraToolItems.UNICORN_HORN_SPEAR.get()),
         new ItemStack((ItemLike)TensuraToolItems.ICE_BLADE.get()),
         new ItemStack((ItemLike)TensuraToolItems.VORTEX_SPEAR.get()),
         new ItemStack((ItemLike)TensuraToolItems.TEMPEST_SCALE_KNIFE.get()),
         new ItemStack((ItemLike)TensuraToolItems.TEMPEST_SCALE_SWORD.get()),
         new ItemStack((ItemLike)TensuraToolItems.TEMPEST_SCALE_SHIELD.get()),
         ARMOURSAURUS_ARMOR,
         SERPENT_ARMOR
      );
   }
}
