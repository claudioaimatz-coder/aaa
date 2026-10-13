package io.github.Memoires.trbeyond.registry.items;

import com.github.manasmods.manascore.api.data.gen.annotation.GenerateItemModels;
import io.github.Memoires.trbeyond.items.armor.BenimaruArmorItem;
import io.github.Memoires.trbeyond.items.armor.DiabloArmorItem;
import io.github.Memoires.trbeyond.items.armor.GabiruArmorItem;
import io.github.Memoires.trbeyond.items.armor.GeldArmorItem;
import io.github.Memoires.trbeyond.items.armor.HakuroArmorItem;
import io.github.Memoires.trbeyond.items.armor.MinosArmorItem;
import io.github.Memoires.trbeyond.items.armor.OgreArmorItem;
import io.github.Memoires.trbeyond.items.armor.SerpentArmorItem;
import io.github.Memoires.trbeyond.items.armor.ShionArmorItem;
import io.github.Memoires.trbeyond.items.armor.SoueiArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@GenerateItemModels
public class BeyondArmorItems {
   private static final DeferredRegister<Item> registry = DeferredRegister.create(ForgeRegistries.ITEMS, "trbeyond");
   public static final RegistryObject<Item> OGRE_CHESTPLATE_RARE = registry.register(
      "ogre_chestplate_rare", () -> new OgreArmorItem(EquipmentSlot.CHEST, OgreArmorItem.Grade.RARE)
   );
   public static final RegistryObject<Item> OGRE_LEGGINGS_RARE = registry.register(
      "ogre_leggings_rare", () -> new OgreArmorItem(EquipmentSlot.LEGS, OgreArmorItem.Grade.RARE)
   );
   public static final RegistryObject<Item> OGRE_BOOTS_RARE = registry.register(
      "ogre_boots_rare", () -> new OgreArmorItem(EquipmentSlot.FEET, OgreArmorItem.Grade.RARE)
   );
   public static final RegistryObject<Item> OGRE_CHESTPLATE_UNIQUE = registry.register(
      "ogre_chestplate_unique", () -> new OgreArmorItem(EquipmentSlot.CHEST, OgreArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> OGRE_LEGGINGS_UNIQUE = registry.register(
      "ogre_leggings_unique", () -> new OgreArmorItem(EquipmentSlot.LEGS, OgreArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> OGRE_BOOTS_UNIQUE = registry.register(
      "ogre_boots_unique", () -> new OgreArmorItem(EquipmentSlot.FEET, OgreArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> OGRE_CHESTPLATE_LEGEND = registry.register(
      "ogre_chestplate_legend", () -> new OgreArmorItem(EquipmentSlot.CHEST, OgreArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> OGRE_LEGGINGS_LEGEND = registry.register(
      "ogre_leggings_legend", () -> new OgreArmorItem(EquipmentSlot.LEGS, OgreArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> OGRE_BOOTS_LEGEND = registry.register(
      "ogre_boots_legend", () -> new OgreArmorItem(EquipmentSlot.FEET, OgreArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> OGRE_CHESTPLATE_GOD = registry.register(
      "ogre_chestplate_god", () -> new OgreArmorItem(EquipmentSlot.CHEST, OgreArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> OGRE_LEGGINGS_GOD = registry.register(
      "ogre_leggings_god", () -> new OgreArmorItem(EquipmentSlot.LEGS, OgreArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> OGRE_BOOTS_GOD = registry.register(
      "ogre_boots_god", () -> new OgreArmorItem(EquipmentSlot.FEET, OgreArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> SERPENT_HELMET_UNIQUE = registry.register(
      "serpent_helmet_unique", () -> new SerpentArmorItem(EquipmentSlot.HEAD, SerpentArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> SERPENT_CHESTPLATE_UNIQUE = registry.register(
      "serpent_chestplate_unique", () -> new SerpentArmorItem(EquipmentSlot.CHEST, SerpentArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> SERPENT_LEGGINGS_UNIQUE = registry.register(
      "serpent_leggings_unique", () -> new SerpentArmorItem(EquipmentSlot.LEGS, SerpentArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> SERPENT_BOOTS_UNIQUE = registry.register(
      "serpent_boots_unique", () -> new SerpentArmorItem(EquipmentSlot.FEET, SerpentArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> SERPENT_HELMET_LEGEND = registry.register(
      "serpent_helmet_legend", () -> new SerpentArmorItem(EquipmentSlot.HEAD, SerpentArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> SERPENT_CHESTPLATE_LEGEND = registry.register(
      "serpent_chestplate_legend", () -> new SerpentArmorItem(EquipmentSlot.CHEST, SerpentArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> SERPENT_LEGGINGS_LEGEND = registry.register(
      "serpent_leggings_legend", () -> new SerpentArmorItem(EquipmentSlot.LEGS, SerpentArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> SERPENT_BOOTS_LEGEND = registry.register(
      "serpent_boots_legend", () -> new SerpentArmorItem(EquipmentSlot.FEET, SerpentArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> SERPENT_HELMET_GOD = registry.register(
      "serpent_helmet_god", () -> new SerpentArmorItem(EquipmentSlot.HEAD, SerpentArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> SERPENT_CHESTPLATE_GOD = registry.register(
      "serpent_chestplate_god", () -> new SerpentArmorItem(EquipmentSlot.CHEST, SerpentArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> SERPENT_LEGGINGS_GOD = registry.register(
      "serpent_leggings_god", () -> new SerpentArmorItem(EquipmentSlot.LEGS, SerpentArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> SERPENT_BOOTS_GOD = registry.register(
      "serpent_boots_god", () -> new SerpentArmorItem(EquipmentSlot.FEET, SerpentArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> MINOS_HELMET_UNIQUE = registry.register(
      "minos_helmet_unique", () -> new MinosArmorItem(EquipmentSlot.HEAD, MinosArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> MINOS_CHESTPLATE_UNIQUE = registry.register(
      "minos_chestplate_unique", () -> new MinosArmorItem(EquipmentSlot.CHEST, MinosArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> MINOS_LEGGINGS_UNIQUE = registry.register(
      "minos_leggings_unique", () -> new MinosArmorItem(EquipmentSlot.LEGS, MinosArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> MINOS_BOOTS_UNIQUE = registry.register(
      "minos_boots_unique", () -> new MinosArmorItem(EquipmentSlot.FEET, MinosArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> MINOS_HELMET_LEGEND = registry.register(
      "minos_helmet_legend", () -> new MinosArmorItem(EquipmentSlot.HEAD, MinosArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> MINOS_CHESTPLATE_LEGEND = registry.register(
      "minos_chestplate_legend", () -> new MinosArmorItem(EquipmentSlot.CHEST, MinosArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> MINOS_LEGGINGS_LEGEND = registry.register(
      "minos_leggings_legend", () -> new MinosArmorItem(EquipmentSlot.LEGS, MinosArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> MINOS_BOOTS_LEGEND = registry.register(
      "minos_boots_legend", () -> new MinosArmorItem(EquipmentSlot.FEET, MinosArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> MINOS_HELMET_GOD = registry.register(
      "minos_helmet_god", () -> new MinosArmorItem(EquipmentSlot.HEAD, MinosArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> MINOS_CHESTPLATE_GOD = registry.register(
      "minos_chestplate_god", () -> new MinosArmorItem(EquipmentSlot.CHEST, MinosArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> MINOS_LEGGINGS_GOD = registry.register(
      "minos_leggings_god", () -> new MinosArmorItem(EquipmentSlot.LEGS, MinosArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> MINOS_BOOTS_GOD = registry.register(
      "minos_boots_god", () -> new MinosArmorItem(EquipmentSlot.FEET, MinosArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> BENIMARU_CHESTPLATE_UNIQUE = registry.register(
      "benimaru_chestplate_unique", () -> new BenimaruArmorItem(EquipmentSlot.CHEST, BenimaruArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> BENIMARU_LEGGINGS_UNIQUE = registry.register(
      "benimaru_leggings_unique", () -> new BenimaruArmorItem(EquipmentSlot.LEGS, BenimaruArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> BENIMARU_CHESTPLATE_LEGEND = registry.register(
      "benimaru_chestplate_legend", () -> new BenimaruArmorItem(EquipmentSlot.CHEST, BenimaruArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> BENIMARU_LEGGINGS_LEGEND = registry.register(
      "benimaru_leggings_legend", () -> new BenimaruArmorItem(EquipmentSlot.LEGS, BenimaruArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> BENIMARU_CHESTPLATE_GOD = registry.register(
      "benimaru_chestplate_god", () -> new BenimaruArmorItem(EquipmentSlot.CHEST, BenimaruArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> BENIMARU_LEGGINGS_GOD = registry.register(
      "benimaru_leggings_god", () -> new BenimaruArmorItem(EquipmentSlot.LEGS, BenimaruArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> BENIMARU_BOOTS_SPECIAL = registry.register(
      "benimaru_boots_special", () -> new BenimaruArmorItem(EquipmentSlot.FEET, BenimaruArmorItem.Grade.SPECIAL)
   );
   public static final RegistryObject<Item> SHION_CHESTPLATE_UNIQUE = registry.register(
      "shion_chestplate_unique", () -> new ShionArmorItem(EquipmentSlot.CHEST, ShionArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> SHION_LEGGINGS_UNIQUE = registry.register(
      "shion_leggings_unique", () -> new ShionArmorItem(EquipmentSlot.LEGS, ShionArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> SHION_CHESTPLATE_LEGEND = registry.register(
      "shion_chestplate_legend", () -> new ShionArmorItem(EquipmentSlot.CHEST, ShionArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> SHION_LEGGINGS_LEGEND = registry.register(
      "shion_leggings_legend", () -> new ShionArmorItem(EquipmentSlot.LEGS, ShionArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> SHION_CHESTPLATE_GOD = registry.register(
      "shion_chestplate_god", () -> new ShionArmorItem(EquipmentSlot.CHEST, ShionArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> SHION_LEGGINGS_GOD = registry.register(
      "shion_leggings_god", () -> new ShionArmorItem(EquipmentSlot.LEGS, ShionArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> SHION_BOOTS_SPECIAL = registry.register(
      "shion_boots_special", () -> new ShionArmorItem(EquipmentSlot.FEET, ShionArmorItem.Grade.SPECIAL)
   );
   public static final RegistryObject<Item> GELD_HELMET_UNIQUE = registry.register(
      "geld_helmet_unique", () -> new GeldArmorItem(EquipmentSlot.HEAD, GeldArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> GELD_CHESTPLATE_UNIQUE = registry.register(
      "geld_chestplate_unique", () -> new GeldArmorItem(EquipmentSlot.CHEST, GeldArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> GELD_LEGGINGS_UNIQUE = registry.register(
      "geld_leggings_unique", () -> new GeldArmorItem(EquipmentSlot.LEGS, GeldArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> GELD_BOOTS_UNIQUE = registry.register(
      "geld_boots_unique", () -> new GeldArmorItem(EquipmentSlot.FEET, GeldArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> GELD_HELMET_LEGEND = registry.register(
      "geld_helmet_legend", () -> new GeldArmorItem(EquipmentSlot.HEAD, GeldArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> GELD_CHESTPLATE_LEGEND = registry.register(
      "geld_chestplate_legend", () -> new GeldArmorItem(EquipmentSlot.CHEST, GeldArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> GELD_LEGGINGS_LEGEND = registry.register(
      "geld_leggings_legend", () -> new GeldArmorItem(EquipmentSlot.LEGS, GeldArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> GELD_BOOTS_LEGEND = registry.register(
      "geld_boots_legend", () -> new GeldArmorItem(EquipmentSlot.FEET, GeldArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> GELD_HELMET_GOD = registry.register(
      "geld_helmet_god", () -> new GeldArmorItem(EquipmentSlot.HEAD, GeldArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> GELD_CHESTPLATE_GOD = registry.register(
      "geld_chestplate_god", () -> new GeldArmorItem(EquipmentSlot.CHEST, GeldArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> GELD_LEGGINGS_GOD = registry.register(
      "geld_leggings_god", () -> new GeldArmorItem(EquipmentSlot.LEGS, GeldArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> GELD_BOOTS_GOD = registry.register(
      "geld_boots_god", () -> new GeldArmorItem(EquipmentSlot.FEET, GeldArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> DIABLO_CHESTPLATE_LEGEND = registry.register(
      "diablo_chestplate_legend", () -> new DiabloArmorItem(EquipmentSlot.CHEST, DiabloArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> DIABLO_LEGGINGS_LEGEND = registry.register(
      "diablo_leggings_legend", () -> new DiabloArmorItem(EquipmentSlot.LEGS, DiabloArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> DIABLO_CHESTPLATE_GOD = registry.register(
      "diablo_chestplate_god", () -> new DiabloArmorItem(EquipmentSlot.CHEST, DiabloArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> DIABLO_LEGGINGS_GOD = registry.register(
      "diablo_leggings_god", () -> new DiabloArmorItem(EquipmentSlot.LEGS, DiabloArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> DIABLO_BOOTS_SPECIAL = registry.register(
      "diablo_boots_special", () -> new DiabloArmorItem(EquipmentSlot.FEET, DiabloArmorItem.Grade.SPECIAL)
   );
   public static final RegistryObject<Item> HAKURO_CHESTPLATE_UNIQUE = registry.register(
      "hakuro_chestplate_unique", () -> new HakuroArmorItem(EquipmentSlot.CHEST, HakuroArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> HAKURO_LEGGINGS_UNIQUE = registry.register(
      "hakuro_leggings_unique", () -> new HakuroArmorItem(EquipmentSlot.LEGS, HakuroArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> HAKURO_BOOTS_SPECIAL = registry.register(
      "hakuro_boots_special", () -> new HakuroArmorItem(EquipmentSlot.FEET, HakuroArmorItem.Grade.SPECIAL)
   );
   public static final RegistryObject<Item> HAKURO_CHESTPLATE_LEGEND = registry.register(
      "hakuro_chestplate_legend", () -> new HakuroArmorItem(EquipmentSlot.CHEST, HakuroArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> HAKURO_LEGGINGS_LEGEND = registry.register(
      "hakuro_leggings_legend", () -> new HakuroArmorItem(EquipmentSlot.LEGS, HakuroArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> GABIRU_CHESTPLATE_UNIQUE = registry.register(
      "gabiru_chestplate_unique", () -> new GabiruArmorItem(EquipmentSlot.CHEST, GabiruArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> GABIRU_LEGGINGS_UNIQUE = registry.register(
      "gabiru_leggings_unique", () -> new GabiruArmorItem(EquipmentSlot.LEGS, GabiruArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> GABIRU_CHESTPLATE_LEGEND = registry.register(
      "gabiru_chestplate_legend", () -> new GabiruArmorItem(EquipmentSlot.CHEST, GabiruArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> GABIRU_LEGGINGS_LEGEND = registry.register(
      "gabiru_leggings_legend", () -> new GabiruArmorItem(EquipmentSlot.LEGS, GabiruArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> GABIRU_CHESTPLATE_GOD = registry.register(
      "gabiru_chestplate_god", () -> new GabiruArmorItem(EquipmentSlot.CHEST, GabiruArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> GABIRU_LEGGINGS_GOD = registry.register(
      "gabiru_leggings_god", () -> new GabiruArmorItem(EquipmentSlot.LEGS, GabiruArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> SOUEI_CHESTPLATE_UNIQUE = registry.register(
      "souei_chestplate_unique", () -> new SoueiArmorItem(EquipmentSlot.CHEST, SoueiArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> SOUEI_LEGGINGS_UNIQUE = registry.register(
      "souei_leggings_unique", () -> new SoueiArmorItem(EquipmentSlot.LEGS, SoueiArmorItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> SOUEI_BOOTS_SPECIAL = registry.register(
      "souei_boots_special", () -> new SoueiArmorItem(EquipmentSlot.FEET, SoueiArmorItem.Grade.SPECIAL)
   );
   public static final RegistryObject<Item> SOUEI_CHESTPLATE_LEGEND = registry.register(
      "souei_chestplate_legend", () -> new SoueiArmorItem(EquipmentSlot.CHEST, SoueiArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> SOUEI_LEGGINGS_LEGEND = registry.register(
      "souei_leggings_legend", () -> new SoueiArmorItem(EquipmentSlot.LEGS, SoueiArmorItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> SOUEI_CHESTPLATE_GOD = registry.register(
      "souei_chestplate_god", () -> new SoueiArmorItem(EquipmentSlot.CHEST, SoueiArmorItem.Grade.GOD)
   );
   public static final RegistryObject<Item> SOUEI_LEGGINGS_GOD = registry.register(
      "souei_leggings_god", () -> new SoueiArmorItem(EquipmentSlot.LEGS, SoueiArmorItem.Grade.GOD)
   );

   public static void init(IEventBus modEventBus) {
      registry.register(modEventBus);
   }
}
