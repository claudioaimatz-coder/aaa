package io.github.Memoires.trbeyond.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DungeonWeightedList<T> {
   private final List<DungeonWeightedList.WeightedEntry<T>> entries = new ArrayList<>();
   private final DungeonWeightedList.WeightSummary totalWeight = new DungeonWeightedList.WeightSummary();
   private boolean dirty = false;

   private DungeonWeightedList() {
   }

   @NotNull
   public static <T> DungeonWeightedList<T> newList() {
      return new DungeonWeightedList<>();
   }

   public DungeonWeightedList<T> add(Supplier<Float> weight, @NotNull List<T> values) {
      return this.add(weight, values, () -> true);
   }

   public DungeonWeightedList<T> add(Supplier<Float> weight, @NotNull List<T> values, DungeonWeightedList.WeightedEntryCondition condition) {
      this.entries.add(new DungeonWeightedList.WeightedEntry<>(weight, values, condition));
      this.dirty = true;
      return this;
   }

   @Nullable
   public T getRandom(RandomSource random, boolean testValue) {
      if (this.entries.isEmpty()) {
         return null;
      } else {
         float chance = random.nextFloat();
         float acc = 0.0F;

         for (DungeonWeightedList.WeightedEntry<T> entry : this.entries.stream().filter(entryx -> entryx.condition().test() == testValue).toList()) {
            acc += entry.weight().get();
            if (chance <= acc) {
               return entry.getRandom(random);
            }
         }

         return null;
      }
   }

   private void updateWeights() {
      if (this.dirty) {
         for (DungeonWeightedList.WeightedEntry<T> entry : this.entries) {
            if (entry.condition.test()) {
               this.totalWeight.increaseEnabledWeight(entry.weight().get());
            } else {
               this.totalWeight.increaseDisableWeight(entry.weight().get());
            }
         }

         this.dirty = false;
      }
   }

   private void normalize() {
      this.updateWeights();
      if (!this.entries.isEmpty()) {
         for (int q = 0; q < this.entries.size(); q++) {
            DungeonWeightedList.WeightedEntry<T> e = this.entries.get(q);
            float weight = this.totalWeight.getWeight(e.condition().test());
            float testWeight = weight > 0.0F ? e.weight().get() / weight : 0.0F;
            this.entries.set(q, new DungeonWeightedList.WeightedEntry<>(() -> testWeight, e.values(), e.condition()));
         }

         this.totalWeight.setDisableWeight(1.0F);
         this.totalWeight.setEnabledWeight(1.0F);
      }
   }

   public DungeonWeightedList<T> end() {
      this.normalize();
      return this;
   }

   private static class WeightSummary {
      private float enabledWeight = 0.0F;
      private float disableWeight = 0.0F;

      public void increaseEnabledWeight(float value) {
         this.enabledWeight += value;
      }

      public void increaseDisableWeight(float value) {
         this.disableWeight += value;
      }

      public void setEnabledWeight(float enabledWeight) {
         this.enabledWeight = enabledWeight;
      }

      public void setDisableWeight(float disableWeight) {
         this.disableWeight = disableWeight;
      }

      public float getEnabledWeight() {
         return this.enabledWeight;
      }

      public float getDisableWeight() {
         return this.disableWeight;
      }

      public float getWeight(boolean value) {
         return value ? this.enabledWeight : this.disableWeight;
      }
   }

   public static record WeightedEntry<T>(Supplier<Float> weight, List<T> values, DungeonWeightedList.WeightedEntryCondition condition) {
      @Nullable
      public T getRandom(@NotNull RandomSource randomSource) {
         return this.values.get(randomSource.nextInt(this.values().size()));
      }
   }

   @FunctionalInterface
   public interface WeightedEntryCondition extends BooleanSupplier {
      @Override
      boolean getAsBoolean();

      default boolean test() {
         return this.getAsBoolean();
      }
   }
}
