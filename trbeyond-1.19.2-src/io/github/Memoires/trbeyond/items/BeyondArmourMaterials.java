package io.github.Memoires.trbeyond.items;

import com.github.manasmods.tensura.registry.items.TensuraMaterialItems;
import com.github.manasmods.tensura.registry.items.TensuraMobDropItems;
import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public enum BeyondArmourMaterials implements ArmorMaterial {
   OGRE_RARE(
      "ogre_armor_rare",
      33,
      new int[]{2, 5, 6, 2},
      18,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      1.0F,
      0.2F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMobDropItems.MONSTER_LEATHER_C.get()})
   ),
   OGRE_UNIQUE(
      "ogre_armor_unique",
      35,
      new int[]{3, 6, 8, 3},
      20,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      2.0F,
      0.3F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMobDropItems.MONSTER_LEATHER_B.get()})
   ),
   OGRE_LEGEND(
      "ogre_armor_legend",
      36,
      new int[]{4, 7, 9, 4},
      25,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      4.0F,
      0.4F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMobDropItems.MONSTER_LEATHER_A.get()})
   ),
   OGRE_GOD(
      "ogre_armor_god",
      75,
      new int[]{9, 12, 14, 9},
      45,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      7.5F,
      0.8F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMobDropItems.MONSTER_LEATHER_SPECIAL_A.get()})
   ),
   SERPENT_UNIQUE(
      "serpent_armor_unique",
      40,
      new int[]{7, 11, 13, 7},
      20,
      SoundEvents.ARMOR_EQUIP_DIAMOND,
      4.0F,
      0.3F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMobDropItems.SERPENT_SCALE.get()})
   ),
   SERPENT_LEGEND(
      "serpent_armor_legend",
      50,
      new int[]{8, 12, 14, 8},
      25,
      SoundEvents.ARMOR_EQUIP_DIAMOND,
      5.0F,
      0.5F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMobDropItems.SERPENT_SCALE.get()})
   ),
   SERPENT_GOD(
      "serpent_armor_god",
      60,
      new int[]{10, 13, 15, 10},
      30,
      SoundEvents.ARMOR_EQUIP_DIAMOND,
      8.0F,
      0.8F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMobDropItems.SERPENT_SCALE.get()})
   ),
   MINOS_UNIQUE(
      "minos_armor_unique",
      40,
      new int[]{5, 9, 11, 6},
      20,
      SoundEvents.ARMOR_EQUIP_DIAMOND,
      5.0F,
      0.3F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.MITHRIL_INGOT.get()})
   ),
   MINOS_LEGEND(
      "minos_armor_legend",
      50,
      new int[]{8, 12, 14, 8},
      30,
      SoundEvents.ARMOR_EQUIP_DIAMOND,
      6.0F,
      0.5F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.MITHRIL_INGOT.get()})
   ),
   MINOS_GOD(
      "minos_armor_god",
      60,
      new int[]{11, 15, 17, 11},
      40,
      SoundEvents.ARMOR_EQUIP_DIAMOND,
      8.0F,
      1.0F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.MITHRIL_INGOT.get()})
   ),
   BENIMARU_UNIQUE(
      "benimaru_armor_unique",
      40,
      new int[]{0, 8, 10, 0},
      20,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      4.0F,
      0.3F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   BENIMARU_LEGEND(
      "benimaru_armor_legend",
      50,
      new int[]{0, 10, 12, 0},
      30,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      6.0F,
      0.6F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   BENIMARU_GOD(
      "benimaru_armor_god",
      60,
      new int[]{0, 13, 15, 0},
      40,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      8.0F,
      0.8F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   BENIMARU_SPECIAL(
      "benimaru_armor_special",
      60,
      new int[]{0, 13, 15, 0},
      40,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      8.0F,
      0.8F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   SHION_UNIQUE(
      "shion_armor_unique",
      40,
      new int[]{0, 10, 12, 0},
      20,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      7.0F,
      0.6F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   SHION_LEGEND(
      "shion_armor_legend",
      50,
      new int[]{0, 12, 14, 0},
      30,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      8.0F,
      0.8F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   SHION_GOD(
      "shion_armor_god",
      60,
      new int[]{0, 14, 16, 0},
      40,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      10.0F,
      1.0F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   SHION_SPECIAL(
      "shion_armor_special",
      60,
      new int[]{0, 13, 15, 0},
      40,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      8.0F,
      0.8F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   GELD_UNIQUE(
      "geld_armor_unique",
      60,
      new int[]{9, 11, 13, 9},
      40,
      SoundEvents.ARMOR_EQUIP_IRON,
      7.0F,
      0.6F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.PURE_MAGISTEEL_INGOT.get()})
   ),
   GELD_LEGEND(
      "geld_armor_god",
      70,
      new int[]{10, 13, 15, 10},
      45,
      SoundEvents.ARMOR_EQUIP_IRON,
      8.0F,
      0.8F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.ADAMANTITE_INGOT.get()})
   ),
   GELD_GOD(
      "geld_armor_god",
      80,
      new int[]{12, 15, 17, 12},
      50,
      SoundEvents.ARMOR_EQUIP_IRON,
      10.0F,
      1.0F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.ADAMANTITE_INGOT.get()})
   ),
   DIABLO_LEGEND(
      "diablo_armor_legend",
      50,
      new int[]{0, 10, 12, 0},
      30,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      6.0F,
      0.6F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   DIABLO_GOD(
      "diablo_armor_god",
      60,
      new int[]{0, 13, 15, 0},
      40,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      8.0F,
      0.8F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   DIABLO_SPECIAL(
      "diablo_armor_special",
      Integer.MAX_VALUE,
      new int[]{0, 0, 0, 0},
      40,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      0.0F,
      0.0F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   HAKURO_UNIQUE(
      "hakuro_armor_unique",
      50,
      new int[]{0, 7, 9, 0},
      30,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      2.0F,
      0.3F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   HAKURO_LEGEND(
      "hakuro_armor_legend",
      60,
      new int[]{0, 8, 10, 0},
      40,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      4.0F,
      0.4F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   HAKURO_SPECIAL(
      "hakuro_armor_special",
      Integer.MAX_VALUE,
      new int[]{0, 0, 0, 0},
      40,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      0.0F,
      0.0F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   GABIRU_UNIQUE(
      "gabiru_armor_unique",
      50,
      new int[]{0, 7, 9, 0},
      40,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      4.0F,
      0.3F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   GABIRU_LEGEND(
      "gabiru_armor_legend",
      60,
      new int[]{0, 9, 11, 0},
      40,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      6.0F,
      0.6F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   GABIRU_GOD(
      "gabiru_armor_god",
      70,
      new int[]{0, 12, 14, 0},
      40,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      8.0F,
      0.8F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   SOUEI_UNIQUE(
      "souei_armor_unique",
      50,
      new int[]{0, 7, 9, 0},
      40,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      4.0F,
      0.3F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   SOUEI_LEGEND(
      "souei_armor_legend",
      60,
      new int[]{0, 9, 11, 0},
      40,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      6.0F,
      0.6F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   SOUEI_GOD(
      "souei_armor_god",
      70,
      new int[]{0, 12, 14, 0},
      40,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      8.0F,
      0.8F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   ),
   SOUEI_SPECIAL(
      "souei_armor_special",
      Integer.MAX_VALUE,
      new int[]{0, 0, 0, 0},
      40,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      0.0F,
      0.0F,
      () -> Ingredient.of(new ItemLike[]{(ItemLike)TensuraMaterialItems.HIHIIROKANE_INGOT.get()})
   );

   private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
   private final String name;
   private final int durabilityMultiplier;
   private final int[] slotProtections;
   private final int enchantmentValue;
   private final SoundEvent sound;
   private final float toughness;
   private final float knockbackResistance;
   private final LazyLoadedValue<Ingredient> repairIngredient;

   private BeyondArmourMaterials(
      String pName,
      int pDurabilityMultiplier,
      int[] pSlotProtections,
      int pEnchantmentValue,
      SoundEvent pSound,
      float pToughness,
      float pKnockbackResistance,
      Supplier<Ingredient> pRepairIngredient
   ) {
      this.name = pName;
      this.durabilityMultiplier = pDurabilityMultiplier;
      this.slotProtections = pSlotProtections;
      this.enchantmentValue = pEnchantmentValue;
      this.sound = pSound;
      this.toughness = pToughness;
      this.knockbackResistance = pKnockbackResistance;
      this.repairIngredient = new LazyLoadedValue(pRepairIngredient);
   }

   public int getDurabilityForSlot(EquipmentSlot pSlot) {
      return HEALTH_PER_SLOT[pSlot.getIndex()] * this.durabilityMultiplier;
   }

   public int getDefenseForSlot(EquipmentSlot pSlot) {
      return this.slotProtections[pSlot.getIndex()];
   }

   @NotNull
   public SoundEvent getEquipSound() {
      return this.sound;
   }

   @NotNull
   public Ingredient getRepairIngredient() {
      return (Ingredient)this.repairIngredient.get();
   }

   @NotNull
   public String getName() {
      return this.name;
   }

   public int getEnchantmentValue() {
      return this.enchantmentValue;
   }

   public float getToughness() {
      return this.toughness;
   }

   public float getKnockbackResistance() {
      return this.knockbackResistance;
   }
}
