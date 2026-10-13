package io.github.Memoires.trbeyond.registry.items;

import com.github.manasmods.tensura.item.custom.ShadowStorageItem;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.items.TeleportItem;
import io.github.Memoires.trbeyond.items.custom.AutoRecordingCrystalItem;
import io.github.Memoires.trbeyond.items.custom.DungeonDominatorsItem;
import io.github.Memoires.trbeyond.items.custom.DungeonMapItem;
import io.github.Memoires.trbeyond.items.custom.EscapeCrystalItem;
import io.github.Memoires.trbeyond.items.custom.FireBreathItem;
import io.github.Memoires.trbeyond.items.custom.RecordingCrystalItem;
import io.github.Memoires.trbeyond.items.custom.ReturnWhistleItem;
import io.github.Memoires.trbeyond.items.custom.RevivalBraceletItem;
import io.github.Memoires.trbeyond.items.custom.SubspacePocketItem;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import java.awt.Color;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BeyondItems {
   public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "trbeyond");
   public static final RegistryObject<Item> REVIVAL_BRACELET = ITEMS.register("revival_bracelet", RevivalBraceletItem::new);
   public static final RegistryObject<Item> RETURN_WHISTLE = ITEMS.register("return_whistle", ReturnWhistleItem::new);
   public static final RegistryObject<Item> RECORDING_CRYSTAL = ITEMS.register("recording_crystal", RecordingCrystalItem::new);
   public static final RegistryObject<Item> AUTO_RECORDING_CRYSTAL = ITEMS.register("auto_recording_crystal", AutoRecordingCrystalItem::new);
   public static final RegistryObject<Item> ESCAPE_CRYSTAL = ITEMS.register("escape_crystal", EscapeCrystalItem::new);
   public static final RegistryObject<Item> DRYAD_SPAWN_EGG = ITEMS.register(
      "dryad_spawn_egg",
      () -> new ForgeSpawnEggItem(
            BeyondEntities.DRYAD, 629145, 10066329, new Properties().rarity(Rarity.RARE).stacksTo(1).tab(BeyondItemGroup.BEYOND_ITEMS)
         )
   );
   public static final RegistryObject<Item> MINH_SLIME_SPAWN_EGG = ITEMS.register(
      "minh_slime_spawn_egg",
      () -> new ForgeSpawnEggItem(
            BeyondEntities.MINH_SLIME, 16777215, 15843890, new Properties().rarity(Rarity.RARE).stacksTo(1).tab(BeyondItemGroup.BEYOND_ITEMS)
         )
   );
   public static final RegistryObject<Item> MAGISTEEL_BULLET = ITEMS.register(
      "magisteel_bullet", () -> new Item(new Properties().tab(BeyondItemGroup.BEYOND_ITEMS))
   );
   public static final RegistryObject<Item> ID_CARD = ITEMS.register(
      "id_card", () -> new Item(new Properties().rarity(Rarity.UNCOMMON).stacksTo(1).tab(BeyondItemGroup.BEYOND_ITEMS))
   );
   public static final RegistryObject<Item> E_RANK_CARD = ITEMS.register(
      "e_rank_card", () -> new Item(new Properties().rarity(Rarity.UNCOMMON).stacksTo(1).tab(BeyondItemGroup.BEYOND_ITEMS))
   );
   public static final RegistryObject<Item> DUNGEON_MAP = ITEMS.register("dungeon_map", DungeonMapItem::new);
   public static final RegistryObject<Item> DUNGEON_VOUCHER = ITEMS.register(
      "dungeon_voucher", () -> new Item(new Properties().rarity(Rarity.COMMON).stacksTo(64).tab(BeyondItemGroup.BEYOND_ITEMS))
   );
   public static final RegistryObject<TeleportItem> TELEPORTER = ITEMS.register(
      "teleporter", () -> new TeleportItem(new Properties().stacksTo(1).tab(BeyondItemGroup.BEYOND_ITEMS))
   );
   public static final RegistryObject<Item> BOSS_SPIDER_SPAWN_EGG = ITEMS.register(
      "boss_black_spider_spawn_egg",
      () -> new ForgeSpawnEggItem(
            BeyondEntities.BOSS_BLACK_SPIDER,
            new Color(31, 27, 41).getRGB(),
            new Color(135, 49, 49).getRGB(),
            new Properties().rarity(Rarity.EPIC).tab(BeyondItemGroup.BEYOND_ITEMS)
         )
   );
   public static final RegistryObject<Item> OGRE_LORD_SPAWN_EGG = ITEMS.register(
      "ogre_lord_spawn_egg",
      () -> new ForgeSpawnEggItem(BeyondEntities.OGRE_LORD, 16777215, 0, new Properties().rarity(Rarity.EPIC).tab(BeyondItemGroup.BEYOND_ITEMS))
   );
   public static final RegistryObject<Item> SLIME_WALL_SPAWN_EGG = ITEMS.register(
      "slime_wall_spawn_egg",
      () -> new ForgeSpawnEggItem(
            BeyondEntities.SLIME_WALL,
            new Color(134, 32, 33).getRGB(),
            new Color(113, 25, 26).getRGB(),
            new Properties().rarity(Rarity.EPIC).tab(BeyondItemGroup.BEYOND_ITEMS)
         )
   );
   public static final RegistryObject<Item> SILVER_MIMIC_SPAWN_EGG = ITEMS.register(
      "silver_mimic_chest_spawn_egg",
      () -> new ForgeSpawnEggItem(
            BeyondEntities.SILVER_MIMIC_CHEST,
            new Color(211, 224, 224).getRGB(),
            new Color(73, 68, 73).getRGB(),
            new Properties().rarity(Rarity.EPIC).tab(BeyondItemGroup.BEYOND_ITEMS)
         )
   );
   public static final RegistryObject<Item> GOLD_MIMIC_SPAWN_EGG = ITEMS.register(
      "gold_mimic_chest_spawn_egg",
      () -> new ForgeSpawnEggItem(
            BeyondEntities.GOLD_MIMIC_CHEST,
            new Color(223, 211, 62).getRGB(),
            new Color(200, 30, 29).getRGB(),
            new Properties().rarity(Rarity.EPIC).tab(BeyondItemGroup.BEYOND_ITEMS)
         )
   );
   public static final RegistryObject<Item> BLUE_SLIME_DOLL_SPAWN_EGG = ITEMS.register(
      "blue_slime_doll_spawn_egg",
      () -> new ForgeSpawnEggItem(
            BeyondEntities.BLUE_SLIME_DOLL,
            new Color(69, 144, 199).getRGB(),
            new Color(49, 64, 175).getRGB(),
            new Properties().rarity(Rarity.EPIC).tab(BeyondItemGroup.BEYOND_ITEMS)
         )
   );
   public static final RegistryObject<Item> GREEN_SLIME_DOLL_SPAWN_EGG = ITEMS.register(
      "green_slime_doll_spawn_egg",
      () -> new ForgeSpawnEggItem(
            BeyondEntities.GREEN_SLIME_DOLL,
            new Color(164, 218, 74).getRGB(),
            new Color(61, 157, 43).getRGB(),
            new Properties().rarity(Rarity.EPIC).tab(BeyondItemGroup.BEYOND_ITEMS)
         )
   );
   public static final RegistryObject<Item> RED_SLIME_DOLL_SPAWN_EGG = ITEMS.register(
      "red_slime_doll_spawn_egg",
      () -> new ForgeSpawnEggItem(
            BeyondEntities.RED_SLIME_DOLL,
            new Color(203, 16, 30).getRGB(),
            new Color(220, 64, 7).getRGB(),
            new Properties().rarity(Rarity.EPIC).tab(BeyondItemGroup.BEYOND_ITEMS)
         )
   );
   public static final RegistryObject<Item> YELLOW_SLIME_DOLL_SPAWN_EGG = ITEMS.register(
      "yellow_slime_doll_spawn_egg",
      () -> new ForgeSpawnEggItem(
            BeyondEntities.YELLOW_SLIME_DOLL,
            new Color(241, 237, 19).getRGB(),
            new Color(238, 207, 21).getRGB(),
            new Properties().rarity(Rarity.EPIC).tab(BeyondItemGroup.BEYOND_ITEMS)
         )
   );
   public static final RegistryObject<Item> GOZUL_SPAWN_EGG = ITEMS.register(
      "gozul_spawn_egg", () -> new ForgeSpawnEggItem(BeyondEntities.GOZUL, -1, -1, new Properties().tab(CreativeModeTab.TAB_MISC))
   );
   public static final RegistryObject<Item> MEZUL_SPAWN_EGG = ITEMS.register(
      "mezul_spawn_egg", () -> new ForgeSpawnEggItem(BeyondEntities.MEZUL, -1, -1, new Properties().tab(CreativeModeTab.TAB_MISC))
   );
   public static final RegistryObject<Item> DUNGEON_DOMINATORS = ITEMS.register("dungeon_dominators", () -> new DungeonDominatorsItem());
   public static final RegistryObject<Item> FIRE_BREATH = ITEMS.register("fire_breath", () -> new FireBreathItem());
   public static final RegistryObject<Item> SPATIAL_POUCH = ITEMS.register("spatial_pouch", () -> new SubspacePocketItem(9, 128, 1));
   public static final RegistryObject<Item> SPATIAL_BAG = ITEMS.register("spatial_bag", () -> new SubspacePocketItem(18, 192, 3));
   public static final RegistryObject<Item> SPATIAL_BACKPACK = ITEMS.register("spatial_backpack", () -> new SubspacePocketItem(27, 256, 5));
   public static final RegistryObject<Item> STORED_SUBORDINATE = ITEMS.register("stored_subordinate", () -> new ShadowStorageItem(new Properties().stacksTo(1)));

   public static void register(IEventBus eventBus) {
      ITEMS.register(eventBus);
   }
}
