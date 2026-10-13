package io.github.Memoires.trbeyond.data;

import com.github.manasmods.manascore.api.data.gen.LanguageProvider;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlocks;
import io.github.Memoires.trbeyond.registry.effects.BeyondEffects;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import io.github.Memoires.trbeyond.registry.items.BeyondArmorItems;
import io.github.Memoires.trbeyond.registry.items.BeyondFoodItems;
import io.github.Memoires.trbeyond.registry.items.BeyondItems;
import io.github.Memoires.trbeyond.registry.items.BeyondToolItems;
import io.github.Memoires.trbeyond.util.BeyondDamageSources;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.RegistryObject;

public class BeyondLanguageProvider extends LanguageProvider {
   public BeyondLanguageProvider(GatherDataEvent gatherDataEvent) {
      super(gatherDataEvent, "trbeyond");
   }

   protected void generate() {
      this.add("itemGroup.trbeyond.main", "T:R Beyond");
      this.entities();
      this.items();
      this.armors();
      this.blocks();
      this.enchantments();
      this.message();
      this.tooltip();
      this.toolItems();
      this.deathMessages();
      this.addSubtitle();
      this.battlewills();
      this.mobEffects();
      this.foodItems();
      this.challenge();
      this.gamerules();
      this.gui();
      this.add((MobEffect)BeyondEffects.LIMITED.get(), "Limited");
      this.add(
         "trbeyond.join_message",
         "We appreciate you downloading Tensura: Dungeon! Our goal is to get to 100 floors. If you'd like to help us reach that goal or need to report any bugs join our discord: "
      );
   }

   private void entities() {
      this.addEntity(BeyondEntities.BOSS_BLACK_SPIDER, "Black Spider Boss");
      this.addEntity(BeyondEntities.GOZUL, "Gozul");
      this.addEntity(BeyondEntities.MEZUL, "Mezul");
      this.addEntity(BeyondEntities.EVIL_CENTIPEDE_BOSS, "Evil Centipede Boss");
      this.addEntity(BeyondEntities.TEMPEST_SERPENT_BOSS, "Tempest Serpent Boss");
      this.addEntity(BeyondEntities.KILLER_DRYAD, "Killer Dryad");
      this.addEntity(BeyondEntities.DRYAD, "Dryad");
      this.addEntity(BeyondEntities.KUROBE, "Kurobe");
      this.addEntity(BeyondEntities.MJOLLMILE, "Mjollmile");
      this.addEntity(BeyondEntities.KOBY, "Koby");
      this.addEntity(BeyondEntities.KAIJIN, "Kaijin");
      this.addEntity(BeyondEntities.GUILD_LADY, "Guild Lady");
      this.addEntity(BeyondEntities.GUILD_TESTER, "Guild Tester");
      this.addEntity(BeyondEntities.OGRE_LORD, "Ogre Lord");
      this.addEntity(BeyondEntities.OGRE_SUB, "Ogre Subordinate");
      this.addEntity(BeyondEntities.SLIME_WALL, "Slime Wall");
      this.addEntity(BeyondEntities.PRIMED_CHEST, "Primed Dungeon Chest");
      this.addEntity(BeyondEntities.SILVER_MIMIC_CHEST, "Silver Mimic");
      this.addEntity(BeyondEntities.GOLD_MIMIC_CHEST, "Gold Mimic");
      this.addEntity(BeyondEntities.BLUE_SLIME_DOLL, "Blue Slime Doll");
      this.addEntity(BeyondEntities.GREEN_SLIME_DOLL, "Green Slime Doll");
      this.addEntity(BeyondEntities.GREEN_SLIME_DOLL_MINION, "Green Slime Doll Minion");
      this.addEntity(BeyondEntities.RED_SLIME_DOLL, "Red Slime Doll");
      this.addEntity(BeyondEntities.YELLOW_SLIME_DOLL, "Yellow Slime Doll");
      this.addEntity(BeyondEntities.MINOS_BARDICHE, "Minos Bardiche");
      this.addEntity(BeyondEntities.KAORU, "Kaoru");
      this.addEntity(BeyondEntities.MINH_SLIME, "Minh Slime");
      this.addEntity(BeyondEntities.GRAVE_LIZARD, "Grave Lizard");
      this.addEntity(BeyondEntities.KING_CRAWLER, "King Crawler");
      this.addEntity(BeyondEntities.SLIME_DOLL_BOSS, "Slime Doll Fusion");
      this.addEntity(BeyondEntities.STELLAR_MIMIC_BOSS, "Stellar Mimic");
      this.addEntity(BeyondEntities.STELLAR_SHRINE, "Stellar Shrine");
      this.addEntity(BeyondEntities.GOLD_SHRINE, "Gold Shrine");
      this.addEntity(BeyondEntities.SILVER_SHRINE, "Silver Shrine");
      this.addEntity(BeyondEntities.MOMIJI, "Momiji");
      this.addEntity(BeyondEntities.SHION, "Shion");
      this.addEntity(BeyondEntities.BENIMARU, "Benimaru");
   }

   private void items() {
      this.add((Item)BeyondItems.RETURN_WHISTLE.get(), "Return Whistle");
      this.add((Item)BeyondItems.REVIVAL_BRACELET.get(), "Revival Bracelet");
      this.add((Item)BeyondItems.RECORDING_CRYSTAL.get(), "Recording Crystal");
      this.add((Item)BeyondItems.AUTO_RECORDING_CRYSTAL.get(), "Auto Recording Crystal");
      this.add((Item)BeyondItems.ESCAPE_CRYSTAL.get(), "Escape Crystal");
      this.add((Item)BeyondItems.DRYAD_SPAWN_EGG.get(), "Dryad Spawn Egg");
      this.add((Item)BeyondItems.DUNGEON_MAP.get(), "Dungeon Map");
      this.add((Item)BeyondItems.MINH_SLIME_SPAWN_EGG.get(), "Minh Slime Spawn Egg");
      this.add((Item)BeyondItems.TELEPORTER.get(), "Dungeon Teleporter");
      this.add((Item)BeyondItems.SPATIAL_POUCH.get(), "Spatial Pouch");
      this.add((Item)BeyondItems.SPATIAL_BAG.get(), "Spatial Bag");
      this.add((Item)BeyondItems.SPATIAL_BACKPACK.get(), "Spatial Backpack");
      this.add((Item)BeyondItems.MAGISTEEL_BULLET.get(), "Magisteel Bullet");
      this.add((Item)BeyondItems.ID_CARD.get(), "ID Card");
      this.add((Item)BeyondItems.E_RANK_CARD.get(), "E Rank Card");
      this.add((Item)BeyondItems.BOSS_SPIDER_SPAWN_EGG.get(), "Boss Black Spider Spawn Egg");
      this.add((Item)BeyondItems.GOZUL_SPAWN_EGG.get(), "Gozul Spawn Egg");
      this.add((Item)BeyondItems.MEZUL_SPAWN_EGG.get(), "Mezul Spawn Egg");
      this.add((Item)BeyondItems.OGRE_LORD_SPAWN_EGG.get(), "Ogre Lord Spawn Egg");
      this.add((Item)BeyondItems.DUNGEON_VOUCHER.get(), "Dungeon Voucher");
      this.add((Item)BeyondItems.SLIME_WALL_SPAWN_EGG.get(), "Slime Wall Spawn Egg");
      this.add((Item)BeyondItems.SILVER_MIMIC_SPAWN_EGG.get(), "Silver Mimic Spawn Egg");
      this.add((Item)BeyondItems.GOLD_MIMIC_SPAWN_EGG.get(), "Gold Mimic Spawn Egg");
      this.add((Item)BeyondItems.BLUE_SLIME_DOLL_SPAWN_EGG.get(), "Blue Slime Doll Spawn Egg");
      this.add((Item)BeyondItems.GREEN_SLIME_DOLL_SPAWN_EGG.get(), "Green Slime Doll Spawn Egg");
      this.add((Item)BeyondItems.RED_SLIME_DOLL_SPAWN_EGG.get(), "Red Slime Doll Spawn Egg");
      this.add((Item)BeyondItems.YELLOW_SLIME_DOLL_SPAWN_EGG.get(), "Yellow Slime Doll Spawn Egg");
   }

   private void toolItems() {
      this.add((Item)BeyondToolItems.CENTIPEDE_DAGGER_SPECIAL.get(), "Centipede Dagger [Special]");
      this.add((Item)BeyondToolItems.CENTIPEDE_DAGGER_RARE.get(), "Rare Centipede Dagger [Rare]");
      this.add((Item)BeyondToolItems.CENTIPEDE_DAGGER_UNIQUE.get(), "Unique Centipede Dagger [Unique]");
      this.add((Item)BeyondToolItems.CENTIPEDE_DAGGER_LEGEND.get(), "Legend Centipede Dagger [Legend]");
      this.add((Item)BeyondToolItems.SPIDER_DAGGER_SPECIAL.get(), "Spider Dagger [Special]");
      this.add((Item)BeyondToolItems.SPIDER_DAGGER_RARE.get(), "Spider Dagger [Rare]");
      this.add((Item)BeyondToolItems.SPIDER_DAGGER_UNIQUE.get(), "Spider Dagger [Unique]");
      this.add((Item)BeyondToolItems.SPIDER_DAGGER_LEGEND.get(), "Spider Dagger [Legend]");
      this.add((Item)BeyondToolItems.OGRE_AXE_RARE.get(), "Ogre Axe [Rare]");
      this.add((Item)BeyondToolItems.OGRE_AXE_UNIQUE.get(), "Ogre Axe [Unique]");
      this.add((Item)BeyondToolItems.OGRE_AXE_LEGEND.get(), "Ogre Axe [Legend]");
      this.add((Item)BeyondToolItems.OGRE_AXE_GOD.get(), "Ogre Axe [God]");
      this.add((Item)BeyondToolItems.GORIKIMARU_UNIQUE.get(), "Revised Gorikimaru");
      this.add((Item)BeyondToolItems.GORIKIMARU_LEGEND.get(), "True Gorikimaru");
      this.add((Item)BeyondToolItems.GORIKIMARU_GOD.get(), "Divine Gorikimaru");
      this.add((Item)BeyondToolItems.GUREN_UNIQUE.get(), "Guren [Unique]");
      this.add((Item)BeyondToolItems.GUREN_LEGEND.get(), "Guren [Legend]");
      this.add((Item)BeyondToolItems.GUREN_GOD.get(), "Guren [God]");
      this.add((Item)BeyondToolItems.SHIBKOBO_UNIQUE.get(), "Shibkobo [Unique]");
      this.add((Item)BeyondToolItems.SHIBKOBO_LEGEND.get(), "Shibkobo [Legend]");
      this.add((Item)BeyondToolItems.SHIBKOBO_GOD.get(), "Shibkobo [God]");
      this.add((Item)BeyondToolItems.RED_SPEAR_UNIQUE.get(), "Red Spear [Unique]");
      this.add((Item)BeyondToolItems.RED_SPEAR_LEGEND.get(), "Red Spear [Legend]");
      this.add((Item)BeyondToolItems.NINJATO_UNIQUE.get(), "Ninjato [Unique]");
      this.add((Item)BeyondToolItems.NINJATO_LEGEND.get(), "Ninjato [Legend]");
      this.add((Item)BeyondToolItems.HAKURO_UNIQUE.get(), "Hakuro's Katana [Unique]");
      this.add((Item)BeyondToolItems.HAKURO_LEGEND.get(), "Hakuro's Katana [Legend]");
      this.add((Item)BeyondToolItems.MINOS_BARDICHE_UNIQUE.get(), "Minos Bardiche [Unique]");
      this.add((Item)BeyondToolItems.MINOS_BARDICHE_LEGEND.get(), "Minos Bardiche [Legend]");
      this.add((Item)BeyondToolItems.MINOS_BARDICHE_GOD.get(), "Minos Bardiche [God]");
      this.add((Item)BeyondToolItems.MINOS_TRIDENT_UNIQUE.get(), "Minos Trident [Unique]");
      this.add((Item)BeyondToolItems.MINOS_TRIDENT_LEGEND.get(), "Minos Trident [Legend]");
      this.add((Item)BeyondToolItems.MINOS_TRIDENT_GOD.get(), "Minos Trident [God]");
      this.add((Item)BeyondToolItems.MEAT_CLEAVER_UNIQUE.get(), "Meat Cleaver [Unique]");
      this.add((Item)BeyondToolItems.MEAT_CLEAVER_LEGEND.get(), "Meat Cleaver [Legend]");
      this.add((Item)BeyondToolItems.MEAT_CLEAVER_GOD.get(), "Meat Cleaver [God]");
      this.add((Item)BeyondToolItems.SCALE_SHIELD_UNIQUE.get(), "Scale Shield [Unique]");
      this.add((Item)BeyondToolItems.SCALE_SHIELD_LEGEND.get(), "Scale Shield [Legend]");
      this.add((Item)BeyondToolItems.SCALE_SHIELD_GOD.get(), "Scale Shield [God]");
      this.add((Item)BeyondToolItems.VORTEX_SPEAR_UNIQUE.get(), "Vortex Spear [Unique]");
      this.add((Item)BeyondToolItems.VORTEX_SPEAR_LEGEND.get(), "Vortex Spear [Legend]");
      this.add((Item)BeyondToolItems.VORTEX_SPEAR_GOD.get(), "Vortex Spear [God]");
      this.add((Item)BeyondToolItems.DIABLO_CLAW_LEGEND.get(), "Diablo's Claw [Legend]");
      this.add((Item)BeyondToolItems.DIABLO_CLAW_GOD.get(), "Diablo's Claw [God]");
      this.add((Item)BeyondToolItems.GLENDA_PISTOL.get(), "Glenda Pistol");
   }

   private void foodItems() {
      this.add((Item)BeyondFoodItems.TEA.get(), "Tea");
      this.add((Item)BeyondFoodItems.TAMAHOYAKI.get(), "Tamahoyaki");
      this.add((Item)BeyondFoodItems.SUSHI_PLATE.get(), "Sushi Plate");
      this.add((Item)BeyondFoodItems.SHUNA_SOUP.get(), "Shuna's Soup");
      this.add((Item)BeyondFoodItems.MEAT.get(), "Meat");
      this.add((Item)BeyondFoodItems.PORK_RAMEN.get(), "Pork Ramen");
      this.add((Item)BeyondFoodItems.MEGALODON_SASHIMI.get(), "Megalodon Sashimi");
      this.add((Item)BeyondFoodItems.BULK_MEAT_SKEWER.get(), "Bulk Meat Skewer");
      this.add((Item)BeyondFoodItems.BRANDY_BOTTLE.get(), "Brandy Bottle");
      this.add((Item)BeyondFoodItems.BRANDY_GLASS.get(), "Brandy Glass");
      this.add((Item)BeyondFoodItems.HONEY.get(), "Honey");
   }

   private void armors() {
      this.add((Item)BeyondArmorItems.OGRE_CHESTPLATE_RARE.get(), "Ogre Chestplate [Rare]");
      this.add((Item)BeyondArmorItems.OGRE_LEGGINGS_RARE.get(), "Ogre Leggings [Rare]");
      this.add((Item)BeyondArmorItems.OGRE_BOOTS_RARE.get(), "Ogre Boots [Rare]");
      this.add((Item)BeyondArmorItems.OGRE_CHESTPLATE_UNIQUE.get(), "Ogre Chestplate [Unique]");
      this.add((Item)BeyondArmorItems.OGRE_LEGGINGS_UNIQUE.get(), "Ogre Leggings [Unique]");
      this.add((Item)BeyondArmorItems.OGRE_BOOTS_UNIQUE.get(), "Ogre Boots [Unique]");
      this.add((Item)BeyondArmorItems.OGRE_CHESTPLATE_LEGEND.get(), "Ogre Chestplate [Legend]");
      this.add((Item)BeyondArmorItems.OGRE_LEGGINGS_LEGEND.get(), "Ogre Leggings [Legend]");
      this.add((Item)BeyondArmorItems.OGRE_BOOTS_LEGEND.get(), "Ogre Boots [Legend]");
      this.add((Item)BeyondArmorItems.OGRE_CHESTPLATE_GOD.get(), "Ogre Chestplate [God]");
      this.add((Item)BeyondArmorItems.OGRE_LEGGINGS_GOD.get(), "Ogre Leggings [God]");
      this.add((Item)BeyondArmorItems.OGRE_BOOTS_GOD.get(), "Ogre Boots [God]");
      this.add((Item)BeyondArmorItems.SERPENT_HELMET_UNIQUE.get(), "Serpent Helmet [Unique]");
      this.add((Item)BeyondArmorItems.SERPENT_CHESTPLATE_UNIQUE.get(), "Serpent Chestplate [Unique]");
      this.add((Item)BeyondArmorItems.SERPENT_LEGGINGS_UNIQUE.get(), "Serpent Leggings [Unique]");
      this.add((Item)BeyondArmorItems.SERPENT_BOOTS_UNIQUE.get(), "Serpent Boots [Unique]");
      this.add((Item)BeyondArmorItems.SERPENT_HELMET_LEGEND.get(), "Serpent Helmet [Legend]");
      this.add((Item)BeyondArmorItems.SERPENT_CHESTPLATE_LEGEND.get(), "Serpent Chestplate [Legend]");
      this.add((Item)BeyondArmorItems.SERPENT_LEGGINGS_LEGEND.get(), "Serpent Leggings [Legend]");
      this.add((Item)BeyondArmorItems.SERPENT_BOOTS_LEGEND.get(), "Serpent Boots [Legend]");
      this.add((Item)BeyondArmorItems.SERPENT_HELMET_GOD.get(), "Serpent Helmet [God]");
      this.add((Item)BeyondArmorItems.SERPENT_CHESTPLATE_GOD.get(), "Serpent Chestplate [God]");
      this.add((Item)BeyondArmorItems.SERPENT_LEGGINGS_GOD.get(), "Serpent Leggings [God]");
      this.add((Item)BeyondArmorItems.SERPENT_BOOTS_GOD.get(), "Serpent Boots [God]");
      this.add((Item)BeyondArmorItems.MINOS_HELMET_UNIQUE.get(), "Minos Helmet [Unique]");
      this.add((Item)BeyondArmorItems.MINOS_CHESTPLATE_UNIQUE.get(), "Minos Chestplate [Unique]");
      this.add((Item)BeyondArmorItems.MINOS_LEGGINGS_UNIQUE.get(), "Minos Leggings [Unique]");
      this.add((Item)BeyondArmorItems.MINOS_BOOTS_UNIQUE.get(), "Minos Boots [Unique]");
      this.add((Item)BeyondArmorItems.MINOS_HELMET_LEGEND.get(), "Minos Helmet [Legend]");
      this.add((Item)BeyondArmorItems.MINOS_CHESTPLATE_LEGEND.get(), "Minos Chestplate [Legend]");
      this.add((Item)BeyondArmorItems.MINOS_LEGGINGS_LEGEND.get(), "Minos Leggings [Legend]");
      this.add((Item)BeyondArmorItems.MINOS_BOOTS_LEGEND.get(), "Minos Boots [Legend]");
      this.add((Item)BeyondArmorItems.MINOS_HELMET_GOD.get(), "Minos Helmet [God]");
      this.add((Item)BeyondArmorItems.MINOS_CHESTPLATE_GOD.get(), "Minos Chestplate [God]");
      this.add((Item)BeyondArmorItems.MINOS_LEGGINGS_GOD.get(), "Minos Leggings [God]");
      this.add((Item)BeyondArmorItems.MINOS_BOOTS_GOD.get(), "Minos Boots [God]");
      this.add((Item)BeyondArmorItems.GELD_HELMET_UNIQUE.get(), "Geld Helmet [Unique]");
      this.add((Item)BeyondArmorItems.GELD_CHESTPLATE_UNIQUE.get(), "Geld Chestplate [Unique]");
      this.add((Item)BeyondArmorItems.GELD_LEGGINGS_UNIQUE.get(), "Geld Leggings [Unique]");
      this.add((Item)BeyondArmorItems.GELD_BOOTS_UNIQUE.get(), "Geld Boots [Unique]");
      this.add((Item)BeyondArmorItems.GELD_HELMET_LEGEND.get(), "Geld Helmet [Legend]");
      this.add((Item)BeyondArmorItems.GELD_CHESTPLATE_LEGEND.get(), "Geld Chestplate [Legend]");
      this.add((Item)BeyondArmorItems.GELD_LEGGINGS_LEGEND.get(), "Geld Leggings [Legend]");
      this.add((Item)BeyondArmorItems.GELD_BOOTS_LEGEND.get(), "Geld Boots [Legend]");
      this.add((Item)BeyondArmorItems.GELD_HELMET_GOD.get(), "Geld Helmet [God]");
      this.add((Item)BeyondArmorItems.GELD_CHESTPLATE_GOD.get(), "Geld Chestplate [God]");
      this.add((Item)BeyondArmorItems.GELD_LEGGINGS_GOD.get(), "Geld Leggings [God]");
      this.add((Item)BeyondArmorItems.GELD_BOOTS_GOD.get(), "Geld Boots [God]");
      this.add((Item)BeyondArmorItems.DIABLO_CHESTPLATE_LEGEND.get(), "Diablo's Suit [Legend]");
      this.add((Item)BeyondArmorItems.DIABLO_LEGGINGS_LEGEND.get(), "Diablo's Pants [Legend]");
      this.add((Item)BeyondArmorItems.DIABLO_CHESTPLATE_GOD.get(), "Diablo's Suit  [God]");
      this.add((Item)BeyondArmorItems.DIABLO_LEGGINGS_GOD.get(), "Diablo's Pants [God]");
      this.add((Item)BeyondArmorItems.DIABLO_BOOTS_SPECIAL.get(), "Diablo's Shoes [Special]");
      this.add((Item)BeyondArmorItems.HAKURO_CHESTPLATE_UNIQUE.get(), "Hakuro's Chestplate [Unique]");
      this.add((Item)BeyondArmorItems.HAKURO_LEGGINGS_UNIQUE.get(), "Hakuro's Pants [Unique]");
      this.add((Item)BeyondArmorItems.HAKURO_CHESTPLATE_LEGEND.get(), "Hakuro's Chestplate [Legend]");
      this.add((Item)BeyondArmorItems.HAKURO_LEGGINGS_LEGEND.get(), "Hakuro's Pants [Legend]");
      this.add((Item)BeyondArmorItems.HAKURO_BOOTS_SPECIAL.get(), "Hakuro's Sandals [Special]");
      this.add((Item)BeyondArmorItems.GABIRU_CHESTPLATE_UNIQUE.get(), "Gabiru Chestplate [Unique]");
      this.add((Item)BeyondArmorItems.GABIRU_LEGGINGS_UNIQUE.get(), "Gabiru Leggings [Unique]");
      this.add((Item)BeyondArmorItems.GABIRU_CHESTPLATE_LEGEND.get(), "Gabiru Chestplate [Legend]");
      this.add((Item)BeyondArmorItems.GABIRU_LEGGINGS_LEGEND.get(), "Gabiru Leggings [Legend]");
      this.add((Item)BeyondArmorItems.GABIRU_CHESTPLATE_GOD.get(), "Gabiru Chestplate [God]");
      this.add((Item)BeyondArmorItems.GABIRU_LEGGINGS_GOD.get(), "Gabiru Leggings [God]");
      this.add((Item)BeyondArmorItems.BENIMARU_CHESTPLATE_UNIQUE.get(), "Benimaru's Chestplate [Unique]");
      this.add((Item)BeyondArmorItems.BENIMARU_LEGGINGS_UNIQUE.get(), "Benimaru's Leggings [Unique]");
      this.add((Item)BeyondArmorItems.BENIMARU_CHESTPLATE_LEGEND.get(), "Benimaru's Chestplate [Legend]");
      this.add((Item)BeyondArmorItems.BENIMARU_LEGGINGS_LEGEND.get(), "Benimaru's Leggings [Legend]");
      this.add((Item)BeyondArmorItems.BENIMARU_CHESTPLATE_GOD.get(), "Benimaru's Chestplate [God]");
      this.add((Item)BeyondArmorItems.BENIMARU_LEGGINGS_GOD.get(), "Benimaru's Leggings [God]");
      this.add((Item)BeyondArmorItems.BENIMARU_BOOTS_SPECIAL.get(), "Benimaru's Sandals [Special]");
      this.add((Item)BeyondArmorItems.SOUEI_CHESTPLATE_UNIQUE.get(), "Souei's Chestplate [Unique]");
      this.add((Item)BeyondArmorItems.SOUEI_LEGGINGS_UNIQUE.get(), "Souei's Leggings [Unique]");
      this.add((Item)BeyondArmorItems.SOUEI_CHESTPLATE_LEGEND.get(), "Souei's Chestplate [Legend]");
      this.add((Item)BeyondArmorItems.SOUEI_LEGGINGS_LEGEND.get(), "Souei's Leggings [Legend]");
      this.add((Item)BeyondArmorItems.SOUEI_CHESTPLATE_GOD.get(), "Souei's Chestplate [God]");
      this.add((Item)BeyondArmorItems.SOUEI_LEGGINGS_GOD.get(), "Souei's Leggings [God]");
      this.add((Item)BeyondArmorItems.SOUEI_BOOTS_SPECIAL.get(), "Souei's Sandals [Special]");
      this.add((Item)BeyondArmorItems.SHION_CHESTPLATE_UNIQUE.get(), "Shion's Chestplate [Unique]");
      this.add((Item)BeyondArmorItems.SHION_LEGGINGS_UNIQUE.get(), "Shion's Leggings [Unique]");
      this.add((Item)BeyondArmorItems.SHION_CHESTPLATE_LEGEND.get(), "Shion's Chestplate [Legend]");
      this.add((Item)BeyondArmorItems.SHION_LEGGINGS_LEGEND.get(), "Shion's Leggings [Legend]");
      this.add((Item)BeyondArmorItems.SHION_CHESTPLATE_GOD.get(), "Shion's Chestplate [God]");
      this.add((Item)BeyondArmorItems.SHION_LEGGINGS_GOD.get(), "Shion's Leggings [God]");
      this.add((Item)BeyondArmorItems.SHION_BOOTS_SPECIAL.get(), "Shion's Boots [Special]");
   }

   private void blocks() {
      this.addBlock(BeyondBlocks.DUNGEON_ANCHOR, "Dungeon Anchor");
      this.addBlock(BeyondBlocks.DUNGEON_UNLOCKER_BLOCK, "Dungeon Unlocker");
      this.add((Block)BeyondBlocks.PURE_BLACK_BLOCK.get(), "Vanta Block");
      this.add((Block)BeyondBlocks.BRONZE_CHEST.get(), "Bronze Chest");
      this.add((Block)BeyondBlocks.SILVER_CHEST.get(), "Silver Chest");
      this.add((Block)BeyondBlocks.GOLD_CHEST.get(), "Gold Chest");
      this.add((Block)BeyondBlocks.STELLAR_CHEST.get(), "Stellar Chest");
      this.add((Block)BeyondBlocks.BRONZE_TNT_CHEST.get(), "Bronze Tnt Chest");
      this.add((Block)BeyondBlocks.SILVER_TNT_CHEST.get(), "Silver Tnt Chest");
      this.add((Block)BeyondBlocks.GOLD_TNT_CHEST.get(), "Gold Tnt Chest");
      this.add((Block)BeyondBlocks.BRONZE_PARALYSIS_CHEST.get(), "Bronze Paralysis Chest");
      this.add((Block)BeyondBlocks.SILVER_PARALYSIS_CHEST.get(), "Silver Paralysis Chest");
      this.add((Block)BeyondBlocks.GOLD_PARALYSIS_CHEST.get(), "Gold Paralysis Chest");
      this.add((Block)BeyondBlocks.SILVER_MIMIC_CHEST.get(), "Silver Mimic Chest");
      this.add((Block)BeyondBlocks.GOLD_MIMIC_CHEST.get(), "Gold Mimic Chest");
      this.add((Block)BeyondBlocks.DUNGEON_GATE.get(), "Dungeon Gate");
      this.add((Block)BeyondBlocks.INSIDE_GATE.get(), "Inside Gate");
      this.add((Block)BeyondBlocks.DUNGEON_GATE_TELEPORTER.get(), "Dungeon Gate Teleporter");
      this.add((Block)BeyondBlocks.DUNGEON_ENTRY.get(), "Dungeon Entry");
      this.add((Block)BeyondBlocks.BRICK_TRAPDOOR.get(), "Brick Trapdoor");
      this.add((Block)BeyondBlocks.ANDESITE_TRAPDOOR.get(), "Andesite Trapdoor");
      this.add((Block)BeyondBlocks.POLISHED_DIORITE_WALL.get(), "Polished Diorite Wall");
      this.add((Block)BeyondBlocks.ILLUSIONARY_BLOCK.get(), "Illusionary Block");
      this.add((Block)BeyondBlocks.MOVING_TILE_BLOCK.get(), "Moving Tile Block");
      this.add((Block)BeyondBlocks.ROTATING_TILE_BLOCK.get(), "Rotating Tile Block");
      this.add((Block)BeyondBlocks.SEALING_AIR.get(), "Sealing Air");
      this.addBlock(BeyondBlocks.SLIME_CEILING, "Slime Ceiling");
      this.addBlock(BeyondBlocks.SLIME_FLOOR, "Slime Floor");
      this.addBlock(BeyondBlocks.STEEL_THREAD, "Steel Thread");
      this.addBlock(BeyondBlocks.STEEL_THREAD_DIAGONAL, "Diagonal Steel Thread");
      this.addBlock(BeyondBlocks.BOSS_SPIDER_EGG, "Boss Spider Egg");
      this.addBlock(BeyondBlocks.DUMBELL_RACK, "Dumbbell Rack");
      this.addBlock(BeyondBlocks.BENCH_PRESS, "Bench Press");
      this.add((Block)BeyondBlocks.ROOM_ENTRY.get(), "Room Entry");
      this.addBlock(BeyondBlocks.STELLAR_SHRINE, "Stellar Shrine");
      this.addBlock(BeyondBlocks.GOLD_SHRINE, "Gold Shrine");
      this.addBlock(BeyondBlocks.SILVER_SHRINE, "Silver Shrine");
      this.addBlock(BeyondBlocks.BRONZE_SHRINE, "Bronze Shrine");
   }

   private void enchantments() {
      this.addEnchantment(
         (Enchantment)BeyondEnchantments.BATTLE_INFUSION.get(),
         "Battle Infusion",
         "Increases the damage of physical related abilities and your resistance against another elemental type."
      );
      this.addEnchantment(
         (Enchantment)BeyondEnchantments.DARKNESS_INFUSION.get(),
         "Darkness Infusion",
         "Increases the damage of darkness related abilities and your resistance against another elemental type."
      );
      this.addEnchantment(
         (Enchantment)BeyondEnchantments.EARTH_INFUSION.get(),
         "Earth Infusion",
         "Increases the damage of earth related abilities and your resistance against another elemental type."
      );
      this.addEnchantment(
         (Enchantment)BeyondEnchantments.FIRE_INFUSION.get(),
         "Fire Infusion",
         "Increases the damage of fire related abilities and your resistance against another elemental type."
      );
      this.addEnchantment(
         (Enchantment)BeyondEnchantments.FLAME_INFUSION.get(),
         "Flame Infusion",
         "Increases the damage of flame related abilities and your resistance against another elemental type."
      );
      this.addEnchantment(
         (Enchantment)BeyondEnchantments.LAND_INFUSION.get(),
         "Land Infusion",
         "Increases the damage of land related abilities and your resistance against another elemental type."
      );
      this.addEnchantment(
         (Enchantment)BeyondEnchantments.WATER_INFUSION.get(),
         "Water Infusion",
         "Increases the damage of water related abilities and your resistance against another elemental type."
      );
      this.addEnchantment(
         (Enchantment)BeyondEnchantments.LIGHT_INFUSION.get(),
         "Light Infusion",
         "Increases the damage of light related abilities and your resistance against another elemental type."
      );
      this.addEnchantment(
         (Enchantment)BeyondEnchantments.WIND_INFUSION.get(),
         "Wind Infusion",
         "Increases the damage of wind related abilities and your resistance against another elemental type."
      );
      this.addEnchantment(
         (Enchantment)BeyondEnchantments.SPATIAL_INFUSION.get(),
         "Spatial Infusion",
         "Increases the damage of spatial related abilities and your resistance against another elemental type."
      );
   }

   private void mobEffects() {
      this.add((MobEffect)BeyondEffects.DODGE.get(), "Certain Dodge");
      this.add((MobEffect)BeyondEffects.NO_DODGE.get(), "Dodge Prevention");
      this.add((MobEffect)BeyondEffects.ROT.get(), "Rot");
   }

   private void battlewills() {
      this.add("trbeyond.skill.sky_walk", "Sky Walk");
      this.add(
         "trbeyond.skill.sky_walk.description",
         "Harness the power of the wind to create invisible footholds in the air, allowing the user to unleash ground-only attacks while airborne."
      );
   }

   private void message() {
      this.add("trbeyond.message.not_enough_coins", "Dungeon entry requires 3 Silver Coins.");
      this.add("trbeyond.message.rule_violation.confirmed", "Rule Violation Confirmed, please carry on adventurer.");
      this.add("trbeyond.message.boss_rule_violation.confirmed", "You have reached the maximum limit of kills for this boss today. Leave or Die");
      this.add("trbeyond.message.rule_violation.one_minute", "1 minute remaining!");
      this.add("trbeyond.message.rule_violation.seconds", "%s seconds remaining!");
      this.add("trbeyond.message.rule_violation.eliminated", "You have been Eliminated!");
      this.add("trbeyond.message.dungeon_ejection", "You are about to be ejected from the Dungeon!");
      this.add("trbeyond.message.rule_violation.prohibited", "This action is strictly prohibited here.");
      this.add("trbeyond.message.dryad.voucher", "It's your first time right? Here, take these, it's on us!");
      this.add(
         "trbeyond.message.dryad.boss_rush",
         "You should try going through the door on the left after beating the boss on the 20th floor.\nMake sure you're prepared!"
      );
      this.add("trbeyond.message.ogre_cannon_cooldown.on_cooldown", "The attack is on cooldown, wait %1$s more seconds.");
      this.add("trbeyond.message.generation", "Dungeon Generating, Please wait...");
      this.add("trbeyond.message.dungeon_full", "Dungeon is full, please try again later.");
      this.add("trbeyond.message.dungeon_enter_cooldown", "You have entered the Dungeon too recently, please wait.");
      this.add("trbeyond.message.shrine.cooldown", "You must wait %s minutes before you can use this shrine again.");
      this.add(
         "trbeyond.message.shrine.cooldown.reward_warning",
         "You need to wait %s minutes to get reward chests from this shrine. Right click again to take on the challenge without rewards."
      );
      this.add("trbeyond.message.shrine.challenge_completed", "Challenge completed!");
      this.add("trbeyond.message.shrine.challenge_completed.chests", "Chest(s) have spawned next to the shrine.");
      this.add("trbeyond.message.shrine.already_active", "The shrine is already active, try again in a few minutes.");
      this.add("trbeyond.message.shrine.already_possess_challenge", "You already have an active challenge.");
      this.add("trbeyond.message.shrine.challenge_failed", "Challenge failed. You can reattempt the challenge another time.");
      this.add("trbeyond.message.shrine.reset", "The shrine has been reset. Right-click again to active it.");
      this.add("trbeyond.message.player_stats", "%s needs to wait %s seconds until they can enter the dungeon.");
      this.add("trbeyond.message.player_stats.no_cooldown", "%s has no cooldown.");
      this.add("trbeyond.message.player_stats.challenges.header", "%s has completed:");
      this.add("trbeyond.message.player_stats.challenges.count", "%s %s Challenges");
      this.add("trbeyond.message.player_stats.challenges.set_completions", "Set %s challenge completion count to %s");
      this.add("trbeyond.message.player_stats.deny", "You don't have permission to view this player's stats");
      this.add("trbeyond.message.dungeon_player_count", "There are currently %s player(s) in the dungeon.");
      this.add("trbeyond.message.dungeon_not_loaded", "The dungeon is not loaded.");
      this.add("trbeyond.message.floor_display.lobby", "Lobby");
      this.add("trbeyond.message.floor_display", "Floor %s");
      this.add("trbeyond.message.boss_rush.no_permission", "%s needs to defeat %s before they are allowed to teleport to the boss rush section.");
   }

   private void tooltip() {
      this.add("trbeyond.tooltip.items_dungeon_only", "This item is only usable in the Dungeon.");
      this.add("trbeyond.tooltip.return_whistle", "Return the user and allies in 10-block radius to the entrance of the Dungeon.");
      this.add("trbeyond.tooltip.revival_bracelet", "Prevent the user from death and teleport them to the entrance of the Dungeon unharmed.");
      this.add(
         "trbeyond.tooltip.dungeon_map", "This map has been showing up at cartographers everywhere. Using it reveals the location of the legendary Dungeon."
      );
      this.add("trbeyond.tooltip.recording_crystal", "Save a location within the Dungeon to teleport the user and allies in 10-block radius to when used.");
      this.add("trbeyond.tooltip.recording_crystal.saved", "Saved Location: [%s, %s, %s]");
      this.add(
         "trbeyond.tooltip.auto_recording_crystal",
         "Automatically saves your death location in the Dungeon; use it later to return there with allies in a 10-block radius."
      );
      this.add("trbeyond.tooltip.auto_recording_crystal.saved", "Saved (on death): [%s, %s, %s]");
      this.add("trbeyond.tooltip.auto_recording_crystal.no_saved", "No death location saved yet.");
      this.add("trbeyond.tooltip.escape_crystal", "Instantly teleports the user and nearby allies to the entrance of the dungeon. Has infinite uses.");
   }

   private void deathMessages() {
      this.addDeathMessage(BeyondDamageSources.PARALYSIS, "%1$s was paralyzed to death", "%1$s was paralyzed to death by %2$s");
      this.addDeathMessage(new DamageSource("Physical"), "%1$s died", "%1$s was killed by %2$s");
   }

   private void addSubtitle() {
      this.add("sounds.trbeyond.vortex_crash", "vortex crash");
      this.add("sounds.trbeyond.hakuro_attack", "Thunder Heaven and Earth Slash");
      this.add("sounds.trbeyond.black_flame_slash", "Black Flame Slash");
      this.add("sounds.trbeyond.ogre_sword_cannon", "Ogre Sword Cannon");
      this.add("sounds.trbeyond.gun_shot", "Gun shot");
   }

   protected void addEntity(RegistryObject<? extends EntityType<?>> entity, String name) {
      this.add(String.format("entity.%s.%s", entity.getId().getNamespace(), entity.getId().getPath().replace('/', '.')), name);
   }

   protected void addDeathMessage(DamageSource damageSource, String message, String sourceMessage) {
      this.add("death.attack." + damageSource.getMsgId(), message);
      this.add("death.attack." + damageSource.getMsgId() + ".player", message + " whilst fighting %2$s");
      this.add("death.attack." + damageSource.getMsgId() + ".item", message + " whilst fighting %2$s wielding %3$s");
      this.add("death.attack." + damageSource.getMsgId() + ".source", sourceMessage);
      this.add("death.attack." + damageSource.getMsgId() + ".source.item", sourceMessage + " using %3$");
   }

   protected void addEnchantment(Enchantment enchantment, String name, String description) {
      this.add(enchantment, name);
      this.add(enchantment.getDescriptionId() + ".desc", description);
   }

   private void challenge() {
      this.add("trbeyond.challenge.give_challenge", "You have been given a new challenge: %s");
      this.add("trbeyond.challenge.fail_challenge", "You have failed the challenge: %s");
      this.add("trbeyond.challenge.complete_challenge", "You have completed the challenge: %s");
      this.add("trbeyond.challenge.locate_majin.hint", "Find %s before they die");
      this.add("trbeyond.challenge.reward", "Reward: %s x%d");
      this.add("trbeyond.challenge.locate_majin", "Locate Majin");
      this.add("trbeyond.challenge.locate_majin_name", "Locate %s");
      this.add("trbeyond.challenge.protect_or_kill_majin", "Protect or Kill Majin");
      this.add("trbeyond.challenge.protect_or_kill_majin_name", "Protect or Kill %s");
      this.add("trbeyond.challenge.target_entity", "Elite Target: %s");
      this.add("trbeyond.challenge.kill_slimes", "Kill Slimes");
      this.add("trbeyond.challenge.kill_hound_dog", "Kill Hound Dogs");
      this.add("trbeyond.challenge.kill_leech_lizard", "Kill Leech Lizards");
      this.add("trbeyond.challenge.kill_floor_boss", "Kill Floor 10 Boss");
      this.add("trbeyond.challenge.kill_elite", "Kill Elite Monster");
      this.add("trbeyond.challenge.kill_minh_slime", "Kill Minh Slime");
      this.add("trbeyond.challenge.dont_take_damage", "Don't Take Damage");
      this.add("trbeyond.challenge.waves", "Protect the Shrine");
      this.add("trbeyond.challenge.player_waves", "Survive the waves");
      this.add("trbeyond.challenge.wave.early_completion", "Wave completed early, advancing.");
      this.add("trbeyond.challenge.destroy_shrine", "Destroy the Shrine");
      this.add("trbeyond.challenge.defeat_boss", "Defeat the Boss");
      this.add("trbeyond.challenge.kill_projectile", "Kill Mobs with Projectiles");
      this.add("trbeyond.challenge.kill_stunned", "Kill Mobs while Paralyzed");
      this.add("trbeyond.challenge.kill_no_weapon", "Kill Mobs without any Weapons");
      this.add("trbeyond.challenge.kill_floor_boss_no_heal", "Kill Floor Boss without Regeneration Skills");
      this.add("trbeyond.challenge.kill_floor_boss_no_resist", "Kill Floor Boss without Resistance Skills");
      this.add("trbeyond.challenge.kill_floor_boss_no_heal_no_resist", "Kill Floor Boss without Regeneration and Resistance Skills");
      this.add("trbeyond.challenge.kill_otherworlders", "Kill Otherworlders");
      this.add("trbeyond.challenge.kill_otherworlders.start", "Help Ramiris get rid of a group of otherworlders!");
      this.add("trbeyond.challenge.kill_dominators", "Kill Dungeon Dominators");
      this.add("trbeyond.challenge.kill_dominators.start", "Defeat the nearby dungeon dominators!");
      this.add("trbeyond.challenge.one_shot_a_hundred", "One shot 100 mobs");
      this.add("trbeyond.challenge.tame_kill_boss", "Have Your Tame Kill the Floor Boss");
      this.add("trbeyond.challenge.shrine_disabled", "Shrine Challenges are disabled.");
      this.add("trbeyond.challenge.minh_slimes", "Kill the Minh Slimes!");
      this.add("trbeyond.challenge.pick_the_correct_chest", "Pick the correct chests!");
      this.add(
         "trbeyond.challenge.pick_the_correct_chest.explanation",
         "In each waves, two chests are trapped, if you pick all the correct chests, the challenge ends if the chests reached the same tier as the shrine\nIf you pick the wrong chest, all the correct chests you've picked will drop their contents and the challenge ends."
      );
   }

   private void gamerules() {
      this.add("gamerule.nightmareMode", "Nightmare Mode");
      this.add("gamerule.nightmareMode.description", "Enables Nightmare Mode, which increases the difficulty of the Dungeon.");
      this.add("gamerule.lootExplosionChance", "Loot Explosion Chance, out of 1000");
      this.add("gamerule.lootExplosionChance.description", "Adjusts the chance for chests to trigger a loot explosion when opened");
      this.add("gamerule.dungeonFloorSize", "Dungeon Floor Size");
      this.add("gamerule.dungeonFloorSize.description", "Number of dungeon pieces per floor");
      this.add("gamerule.ambientMobSpawnRate", "Ambient Mob Spawn Rate");
      this.add("gamerule.ambientMobSpawnRate.description", "Reduces the ambient mob spawn rate around players in the Dungeon");
      this.add("gamerule.uniqueMonsterChance", "Unique Monster Chance, out of 1000");
      this.add("gamerule.uniqueMonsterChance.description", "Set the chance for mobs to spawn as Unique Monsters");
      this.add("gamerule.disablechallenges", "Disable Challenges");
      this.add("gamerule.disablechallenges.description", "If enabled, challenges will not be given to players in the Dungeon");
      this.add("gamerule.disableShrines", "Disable Shrine Challenges");
      this.add("gamerule.disableShrines.description", "If enabled, shrine challenges will not be given to players in the Dungeon");
      this.add("gamerule.mobsSpawnWithDungeon", "Mob Spawn with Dungeon");
      this.add(
         "gamerule.mobsSpawnWithDungeon.description", "If enabled, mobs will spawn in the Dungeon as it generates, rather than waiting for players to enter"
      );
      this.add("gamerule.UniqueMonsterOverworld", "Unique Monster Overworld");
      this.add("gamerule.UniqueMonsterOverworld.description", "If enabled, Unique Monsters can spawn in the Overworld, not just in the Dungeon");
      this.add("gamerule.UniqueMonsterParticles", "Unique Monster Particles");
      this.add(
         "gamerule.UniqueMonsterParticles.description",
         "If enabled, Unique Monsters will have special particles when they spawn, making them easier to identify"
      );
      this.add("gamerule.bossEPOneTimeDrop", "Boss One Time EP Drop");
      this.add("gamerule.bossEPOneTimeDrop.description", "If enabled, bosses will only drop EP the first they are killed by the player.");
   }

   private void gui() {
      this.add("gui.trbeyond.kurobe_main_menu.button_engrave_enchanting", "Engrave Enchanting");
      this.add("gui.trbeyond.kurobe_main_menu.button_open_shop_gui", "Open Shop Menu");
   }
}
