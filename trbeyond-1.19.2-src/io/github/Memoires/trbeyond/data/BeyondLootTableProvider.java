package io.github.Memoires.trbeyond.data;

import com.mojang.datafixers.util.Pair;
import io.github.Memoires.trbeyond.data.loot.ChestLoot;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

public class BeyondLootTableProvider extends LootTableProvider {
   public BeyondLootTableProvider(DataGenerator pGenerator) {
      super(pGenerator);
   }

   protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> getTables() {
      return List.of(Pair.of(ChestLoot::new, LootContextParamSets.BLOCK));
   }

   protected void validate(Map<ResourceLocation, LootTable> map, @NotNull ValidationContext validationtracker) {
      map.forEach((location, lootTable) -> LootTables.validate(validationtracker, location, lootTable));
   }
}
