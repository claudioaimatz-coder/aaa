package io.github.Memoires.trbeyond.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class AddItemModifier extends LootModifier {
   public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(
      () -> RecordCodecBuilder.create(
            instance -> codecStart(instance)
                  .and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item))
                  .and(Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance))
                  .and(Codec.INT.fieldOf("min").forGetter(m -> m.min))
                  .and(Codec.INT.fieldOf("max").forGetter(m -> m.max))
                  .apply(instance, AddItemModifier::new)
         )
   );
   private final Item item;
   private final float chance;
   private final int min;
   private final int max;

   public AddItemModifier(LootItemCondition[] conditionsIn, Item item, float chance, int min, int max) {
      super(conditionsIn);
      this.item = item;
      this.chance = chance;
      this.min = min;
      this.max = max;
   }

   @NotNull
   protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
      for (LootItemCondition condition : this.conditions) {
         if (!condition.test(context)) {
            return generatedLoot;
         }
      }

      if (context.getRandom().nextFloat() > this.chance) {
         return generatedLoot;
      } else {
         ItemStack stack = new ItemStack(this.item);
         stack.setCount(context.getRandom().nextInt(this.min, this.max + 1));
         generatedLoot.add(stack);
         return generatedLoot;
      }
   }

   public Codec<? extends IGlobalLootModifier> codec() {
      return CODEC.get();
   }
}
