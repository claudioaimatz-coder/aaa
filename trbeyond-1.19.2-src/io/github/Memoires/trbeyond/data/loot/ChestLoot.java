package io.github.Memoires.trbeyond.data.loot;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ChestLoot implements Consumer<BiConsumer<ResourceLocation, Builder>> {
   public void accept(BiConsumer<ResourceLocation, Builder> builder) {
      builder.accept(
         new ResourceLocation("trbeyond", "chests/test"),
         LootTable.lootTable()
            .withPool(
               LootPool.lootPool()
                  .setRolls(ConstantValue.exactly(1.0F))
                  .add(LootItem.lootTableItem(Blocks.LODESTONE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
            )
            .withPool(
               LootPool.lootPool()
                  .setRolls(UniformGenerator.between(1.0F, 2.0F))
                  .add(
                     LootItem.lootTableItem(Items.CROSSBOW)
                        .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.1F, 0.5F)))
                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment())
                  )
                  .add(LootItem.lootTableItem(Items.SPECTRAL_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(10.0F, 28.0F))))
                  .add(LootItem.lootTableItem(Blocks.GILDED_BLACKSTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 12.0F))))
                  .add(LootItem.lootTableItem(Blocks.CRYING_OBSIDIAN).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F))))
                  .add(LootItem.lootTableItem(Blocks.GOLD_BLOCK).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
                  .add(LootItem.lootTableItem(Items.GOLD_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                  .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                  .add(LootItem.lootTableItem(Items.GOLDEN_SWORD).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
                  .add(
                     LootItem.lootTableItem(Items.GOLDEN_CHESTPLATE)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment())
                  )
                  .add(
                     LootItem.lootTableItem(Items.GOLDEN_HELMET)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment())
                  )
                  .add(
                     LootItem.lootTableItem(Items.GOLDEN_LEGGINGS)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment())
                  )
                  .add(
                     LootItem.lootTableItem(Items.GOLDEN_BOOTS)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment())
                  )
                  .add(
                     LootItem.lootTableItem(Items.GOLDEN_AXE)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment())
                  )
            )
            .withPool(
               LootPool.lootPool()
                  .setRolls(UniformGenerator.between(2.0F, 4.0F))
                  .add(LootItem.lootTableItem(Items.STRING).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 6.0F))))
                  .add(LootItem.lootTableItem(Items.LEATHER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                  .add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 17.0F))))
                  .add(LootItem.lootTableItem(Items.IRON_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
                  .add(LootItem.lootTableItem(Items.GOLD_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
            )
      );
   }
}
