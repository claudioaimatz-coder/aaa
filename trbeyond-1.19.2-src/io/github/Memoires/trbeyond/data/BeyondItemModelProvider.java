package io.github.Memoires.trbeyond.data;

import io.github.Memoires.trbeyond.registry.blocks.BeyondBlocks;
import io.github.Memoires.trbeyond.registry.items.BeyondArmorItems;
import io.github.Memoires.trbeyond.registry.items.BeyondItems;
import io.github.Memoires.trbeyond.registry.items.BeyondToolItems;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BeyondItemModelProvider extends ItemModelProvider {
   public BeyondItemModelProvider(GatherDataEvent gatherDataEvent) {
      super(gatherDataEvent.getGenerator(), "trbeyond", gatherDataEvent.getExistingFileHelper());
   }

   protected void registerModels() {
      this.basicItem((Item)BeyondItems.RETURN_WHISTLE.get());
      this.basicItem((Item)BeyondItems.REVIVAL_BRACELET.get());
      this.basicItem((Item)BeyondItems.RECORDING_CRYSTAL.get());
      this.basicItem((Item)BeyondItems.AUTO_RECORDING_CRYSTAL.get());
      this.basicItem((Item)BeyondItems.ESCAPE_CRYSTAL.get());
      this.basicItem((Item)BeyondItems.DUNGEON_VOUCHER.get());
      this.basicItem((Item)BeyondArmorItems.OGRE_CHESTPLATE_RARE.get());
      this.basicItem((Item)BeyondArmorItems.OGRE_LEGGINGS_RARE.get());
      this.basicItem((Item)BeyondArmorItems.OGRE_BOOTS_RARE.get());
      this.basicItem((Item)BeyondArmorItems.OGRE_CHESTPLATE_UNIQUE.get());
      this.basicItem((Item)BeyondArmorItems.OGRE_LEGGINGS_UNIQUE.get());
      this.basicItem((Item)BeyondArmorItems.OGRE_BOOTS_UNIQUE.get());
      this.basicItem((Item)BeyondArmorItems.OGRE_CHESTPLATE_LEGEND.get());
      this.basicItem((Item)BeyondArmorItems.OGRE_LEGGINGS_LEGEND.get());
      this.basicItem((Item)BeyondArmorItems.OGRE_BOOTS_LEGEND.get());
      this.basicItem((Item)BeyondArmorItems.OGRE_CHESTPLATE_GOD.get());
      this.basicItem((Item)BeyondArmorItems.OGRE_LEGGINGS_GOD.get());
      this.basicItem((Item)BeyondArmorItems.OGRE_BOOTS_GOD.get());
      this.basicItem((Item)BeyondArmorItems.SERPENT_HELMET_UNIQUE.get());
      this.basicItem((Item)BeyondArmorItems.SERPENT_CHESTPLATE_UNIQUE.get());
      this.basicItem((Item)BeyondArmorItems.SERPENT_LEGGINGS_UNIQUE.get());
      this.basicItem((Item)BeyondArmorItems.SERPENT_BOOTS_UNIQUE.get());
      this.basicItem((Item)BeyondArmorItems.SERPENT_HELMET_LEGEND.get());
      this.basicItem((Item)BeyondArmorItems.SERPENT_CHESTPLATE_LEGEND.get());
      this.basicItem((Item)BeyondArmorItems.SERPENT_LEGGINGS_LEGEND.get());
      this.basicItem((Item)BeyondArmorItems.SERPENT_BOOTS_LEGEND.get());
      this.basicItem((Item)BeyondArmorItems.SERPENT_HELMET_GOD.get());
      this.basicItem((Item)BeyondArmorItems.SERPENT_CHESTPLATE_GOD.get());
      this.basicItem((Item)BeyondArmorItems.SERPENT_LEGGINGS_GOD.get());
      this.basicItem((Item)BeyondArmorItems.SERPENT_BOOTS_GOD.get());
      this.basicItem((Item)BeyondArmorItems.MINOS_HELMET_UNIQUE.get());
      this.basicItem((Item)BeyondArmorItems.MINOS_CHESTPLATE_UNIQUE.get());
      this.basicItem((Item)BeyondArmorItems.MINOS_LEGGINGS_UNIQUE.get());
      this.basicItem((Item)BeyondArmorItems.MINOS_BOOTS_UNIQUE.get());
      this.basicItem((Item)BeyondArmorItems.MINOS_HELMET_LEGEND.get());
      this.basicItem((Item)BeyondArmorItems.MINOS_CHESTPLATE_LEGEND.get());
      this.basicItem((Item)BeyondArmorItems.MINOS_LEGGINGS_LEGEND.get());
      this.basicItem((Item)BeyondArmorItems.MINOS_BOOTS_LEGEND.get());
      this.basicItem((Item)BeyondArmorItems.MINOS_HELMET_GOD.get());
      this.basicItem((Item)BeyondArmorItems.MINOS_CHESTPLATE_GOD.get());
      this.basicItem((Item)BeyondArmorItems.MINOS_LEGGINGS_GOD.get());
      this.basicItem((Item)BeyondArmorItems.MINOS_BOOTS_GOD.get());
      this.basicItem((Item)BeyondArmorItems.BENIMARU_CHESTPLATE_UNIQUE.get());
      this.basicItem((Item)BeyondArmorItems.BENIMARU_LEGGINGS_UNIQUE.get());
      this.basicItem((Item)BeyondArmorItems.BENIMARU_CHESTPLATE_LEGEND.get());
      this.basicItem((Item)BeyondArmorItems.BENIMARU_LEGGINGS_LEGEND.get());
      this.basicItem((Item)BeyondArmorItems.BENIMARU_CHESTPLATE_GOD.get());
      this.basicItem((Item)BeyondArmorItems.BENIMARU_LEGGINGS_GOD.get());
      this.basicItem((Item)BeyondArmorItems.BENIMARU_BOOTS_SPECIAL.get());
      this.basicItem((Item)BeyondArmorItems.SHION_CHESTPLATE_UNIQUE.get());
      this.basicItem((Item)BeyondArmorItems.SHION_LEGGINGS_UNIQUE.get());
      this.basicItem((Item)BeyondArmorItems.SHION_CHESTPLATE_LEGEND.get());
      this.basicItem((Item)BeyondArmorItems.SHION_LEGGINGS_LEGEND.get());
      this.basicItem((Item)BeyondArmorItems.SHION_CHESTPLATE_GOD.get());
      this.basicItem((Item)BeyondArmorItems.SHION_LEGGINGS_GOD.get());
      this.basicItem((Item)BeyondArmorItems.SHION_BOOTS_SPECIAL.get());
      this.basicerItem((Item)BeyondArmorItems.GELD_HELMET_UNIQUE.get());
      this.basicerItem((Item)BeyondArmorItems.GELD_CHESTPLATE_UNIQUE.get());
      this.basicerItem((Item)BeyondArmorItems.GELD_LEGGINGS_UNIQUE.get());
      this.basicerItem((Item)BeyondArmorItems.GELD_BOOTS_UNIQUE.get());
      this.basicerItem((Item)BeyondArmorItems.GELD_HELMET_LEGEND.get());
      this.basicerItem((Item)BeyondArmorItems.GELD_CHESTPLATE_LEGEND.get());
      this.basicerItem((Item)BeyondArmorItems.GELD_LEGGINGS_LEGEND.get());
      this.basicerItem((Item)BeyondArmorItems.GELD_BOOTS_LEGEND.get());
      this.basicerItem((Item)BeyondArmorItems.GELD_HELMET_GOD.get());
      this.basicerItem((Item)BeyondArmorItems.GELD_CHESTPLATE_GOD.get());
      this.basicerItem((Item)BeyondArmorItems.GELD_LEGGINGS_GOD.get());
      this.basicerItem((Item)BeyondArmorItems.GELD_BOOTS_GOD.get());
      this.basicerItem((Item)BeyondArmorItems.DIABLO_CHESTPLATE_LEGEND.get());
      this.basicerItem((Item)BeyondArmorItems.DIABLO_LEGGINGS_LEGEND.get());
      this.basicerItem((Item)BeyondArmorItems.DIABLO_CHESTPLATE_GOD.get());
      this.basicerItem((Item)BeyondArmorItems.DIABLO_LEGGINGS_GOD.get());
      this.basicerItem((Item)BeyondArmorItems.DIABLO_BOOTS_SPECIAL.get());
      this.basicerItem((Item)BeyondArmorItems.HAKURO_CHESTPLATE_UNIQUE.get());
      this.basicerItem((Item)BeyondArmorItems.HAKURO_LEGGINGS_UNIQUE.get());
      this.basicerItem((Item)BeyondArmorItems.HAKURO_CHESTPLATE_LEGEND.get());
      this.basicerItem((Item)BeyondArmorItems.HAKURO_LEGGINGS_LEGEND.get());
      this.basicerItem((Item)BeyondArmorItems.HAKURO_BOOTS_SPECIAL.get());
      this.basicerItem((Item)BeyondArmorItems.GABIRU_CHESTPLATE_UNIQUE.get());
      this.basicerItem((Item)BeyondArmorItems.GABIRU_LEGGINGS_UNIQUE.get());
      this.basicerItem((Item)BeyondArmorItems.GABIRU_CHESTPLATE_LEGEND.get());
      this.basicerItem((Item)BeyondArmorItems.GABIRU_LEGGINGS_LEGEND.get());
      this.basicerItem((Item)BeyondArmorItems.GABIRU_CHESTPLATE_GOD.get());
      this.basicerItem((Item)BeyondArmorItems.GABIRU_LEGGINGS_GOD.get());
      this.basicerItem((Item)BeyondArmorItems.SOUEI_CHESTPLATE_UNIQUE.get());
      this.basicerItem((Item)BeyondArmorItems.SOUEI_LEGGINGS_UNIQUE.get());
      this.basicerItem((Item)BeyondArmorItems.SOUEI_CHESTPLATE_LEGEND.get());
      this.basicerItem((Item)BeyondArmorItems.SOUEI_LEGGINGS_LEGEND.get());
      this.basicerItem((Item)BeyondArmorItems.SOUEI_CHESTPLATE_GOD.get());
      this.basicerItem((Item)BeyondArmorItems.SOUEI_LEGGINGS_GOD.get());
      this.basicerItem((Item)BeyondArmorItems.SOUEI_BOOTS_SPECIAL.get());
      this.handheldItem(BeyondToolItems.CENTIPEDE_DAGGER_SPECIAL);
      this.handheldItem(BeyondToolItems.CENTIPEDE_DAGGER_RARE);
      this.handheldItem(BeyondToolItems.CENTIPEDE_DAGGER_UNIQUE);
      this.handheldItem(BeyondToolItems.CENTIPEDE_DAGGER_LEGEND);
      this.handheldItem(BeyondToolItems.SPIDER_DAGGER_SPECIAL);
      this.handheldItem(BeyondToolItems.SPIDER_DAGGER_RARE);
      this.handheldItem(BeyondToolItems.SPIDER_DAGGER_UNIQUE);
      this.handheldItem(BeyondToolItems.SPIDER_DAGGER_LEGEND);
      this.vortex_spear(BeyondToolItems.VORTEX_SPEAR_UNIQUE);
      this.vortex_spear(BeyondToolItems.VORTEX_SPEAR_LEGEND);
      this.vortex_spear(BeyondToolItems.VORTEX_SPEAR_GOD);
      this.scythe(BeyondToolItems.OGRE_AXE_RARE);
      this.scythe(BeyondToolItems.OGRE_AXE_UNIQUE);
      this.scythe(BeyondToolItems.OGRE_AXE_LEGEND);
      this.scythe(BeyondToolItems.OGRE_AXE_GOD);
      this.gorikimaru(BeyondToolItems.GORIKIMARU_UNIQUE);
      this.gorikimaru(BeyondToolItems.GORIKIMARU_LEGEND);
      this.gorikimaru(BeyondToolItems.GORIKIMARU_GOD);
      this.guren(BeyondToolItems.GUREN_UNIQUE);
      this.guren(BeyondToolItems.GUREN_LEGEND);
      this.guren(BeyondToolItems.GUREN_GOD);
      this.meat_cleaver(BeyondToolItems.MEAT_CLEAVER_UNIQUE);
      this.meat_cleaver(BeyondToolItems.MEAT_CLEAVER_LEGEND);
      this.meat_cleaver(BeyondToolItems.MEAT_CLEAVER_GOD);
      this.scale_shield(BeyondToolItems.SCALE_SHIELD_UNIQUE);
      this.scale_shield(BeyondToolItems.SCALE_SHIELD_LEGEND);
      this.scale_shield(BeyondToolItems.SCALE_SHIELD_GOD);
      this.sameModelItems(BeyondToolItems.NINJATO_UNIQUE, "ninjato");
      this.sameModelItems(BeyondToolItems.NINJATO_LEGEND, "ninjato");
      this.sameModelItems(BeyondToolItems.RED_SPEAR_UNIQUE, "red_spear");
      this.sameModelItems(BeyondToolItems.RED_SPEAR_LEGEND, "red_spear");
      this.sameModelItems(BeyondToolItems.HAKURO_UNIQUE, "hakuro_sword");
      this.sameModelItems(BeyondToolItems.HAKURO_LEGEND, "hakuro_sword");
      this.shibkobo(BeyondToolItems.SHIBKOBO_UNIQUE);
      this.shibkobo(BeyondToolItems.SHIBKOBO_LEGEND);
      this.shibkobo(BeyondToolItems.SHIBKOBO_GOD);
      this.blockItem(BeyondBlocks.PURE_BLACK_BLOCK);
      this.blockItem(BeyondBlocks.BRONZE_CHEST);
      this.blockItem(BeyondBlocks.SILVER_CHEST);
      this.blockItem(BeyondBlocks.GOLD_CHEST);
      this.blockItemCopy(BeyondBlocks.BRONZE_TNT_CHEST, BeyondBlocks.BRONZE_CHEST);
      this.blockItemCopy(BeyondBlocks.SILVER_TNT_CHEST, BeyondBlocks.SILVER_CHEST);
      this.blockItemCopy(BeyondBlocks.GOLD_TNT_CHEST, BeyondBlocks.GOLD_CHEST);
      this.blockItemCopy(BeyondBlocks.BRONZE_PARALYSIS_CHEST, BeyondBlocks.BRONZE_CHEST);
      this.blockItemCopy(BeyondBlocks.SILVER_PARALYSIS_CHEST, BeyondBlocks.SILVER_CHEST);
      this.blockItemCopy(BeyondBlocks.GOLD_PARALYSIS_CHEST, BeyondBlocks.GOLD_CHEST);
      this.blockItemCopy(BeyondBlocks.SILVER_MIMIC_CHEST, BeyondBlocks.SILVER_CHEST);
      this.blockItemCopy(BeyondBlocks.GOLD_MIMIC_CHEST, BeyondBlocks.GOLD_CHEST);
      this.blockItem(BeyondBlocks.BOSS_SPIDER_EGG);
      this.trapdoorItem(BeyondBlocks.ANDESITE_TRAPDOOR);
      this.trapdoorItem(BeyondBlocks.BRICK_TRAPDOOR);
      this.wallItem(BeyondBlocks.POLISHED_DIORITE_WALL);
      this.stairItem(BeyondBlocks.SMOOTH_SANDSTONE_STAIRS);
      this.simpleItem(BeyondItems.MAGISTEEL_BULLET, "item/magisteel_bullet");
      this.simpleItem(BeyondItems.TELEPORTER, "item/missing_texture");
      this.simpleBlockItem(BeyondBlocks.INSIDE_GATE, "item/missing_texture");
      this.simpleBlockItem(BeyondBlocks.DUNGEON_GATE_TELEPORTER, "item/missing_texture");
      this.simpleBlockItem(BeyondBlocks.DUNGEON_ENTRY, "item/missing_texture");
      this.simpleBlockItem(BeyondBlocks.DUNGEON_ANCHOR, "item/missing_texture");
      this.simpleBlockItem(BeyondBlocks.ILLUSIONARY_BLOCK, "item/missing_texture");
      this.simpleBlockItem(BeyondBlocks.MOVING_TILE_BLOCK, "item/missing_texture");
      this.simpleBlockItem(BeyondBlocks.ROTATING_TILE_BLOCK, "item/missing_texture");
      this.simpleBlockItem(BeyondBlocks.SEALING_AIR, "item/missing_texture");
      this.simpleBlockItem(BeyondBlocks.STEEL_THREAD, "item/missing_texture");
      this.simpleBlockItem(BeyondBlocks.STEEL_THREAD_DIAGONAL, "item/missing_texture");
   }

   private void simpleItem(RegistryObject<? extends Item> item, String location) {
      ((ItemModelBuilder)this.withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated")))
         .texture("layer0", new ResourceLocation("trbeyond", location));
   }

   private void simpleBlockItem(RegistryObject<? extends Block> item, String location) {
      ((ItemModelBuilder)this.withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated")))
         .texture("layer0", new ResourceLocation("trbeyond", location));
   }

   public void blockItem(RegistryObject<Block> block) {
      this.withExistingParent(
         "trbeyond:" + ForgeRegistries.BLOCKS.getKey((Block)block.get()).getPath(),
         this.modLoc("block/" + ForgeRegistries.BLOCKS.getKey((Block)block.get()).getPath())
      );
   }

   public void blockItemCopy(RegistryObject<Block> block, RegistryObject<Block> toCopy) {
      this.withExistingParent(
         "trbeyond:" + ForgeRegistries.BLOCKS.getKey((Block)block.get()).getPath(),
         this.modLoc("block/" + ForgeRegistries.BLOCKS.getKey((Block)toCopy.get()).getPath())
      );
   }

   private ItemModelBuilder differentModelItems(RegistryObject<Item> item, String suffix1, String suffix2) {
      ResourceLocation resourceLocation = new ResourceLocation("trbeyond", "item/" + suffix1 + "_" + suffix2 + "_base");
      return this.handheldSingleTextureModdedItem(item, resourceLocation);
   }

   private ItemModelBuilder sameModelItems(RegistryObject<Item> item, String suffix) {
      ResourceLocation resourceLocation = new ResourceLocation("trbeyond", "item/" + suffix);
      return this.handheldSingleTextureModdedItem(item, resourceLocation);
   }

   private ItemModelBuilder scythe(RegistryObject<Item> item) {
      return this.handheldModdedItem(item, this.modLoc("ogre_axe_handheld"));
   }

   private ItemModelBuilder gorikimaru(RegistryObject<Item> item) {
      String path = "item/" + item.getId().getPath();
      ResourceLocation type = this.modLoc("item/gorikimaru");
      ItemModelBuilder modelBuilder = (ItemModelBuilder)this.withExistingParent(item.getId().getPath(), type);
      modelBuilder.override()
         .predicate(new ResourceLocation("trbeyond", "ogre_cannon"), 1.0F)
         .model((ItemModelBuilder)this.withExistingParent(item.getId().getPath() + "_cannon_effect", type + "_cannon_effect"));
      return modelBuilder;
   }

   private void guren(RegistryObject<Item> item) {
      String path = "item/" + item.getId().getPath();
      ResourceLocation type = this.modLoc("item/guren");
      ItemModelBuilder modelBuilder = (ItemModelBuilder)this.withExistingParent(item.getId().getPath(), type);
      modelBuilder.override()
         .predicate(new ResourceLocation("trbeyond", "black_flame"), 1.0F)
         .model((ItemModelBuilder)this.withExistingParent(item.getId().getPath() + "_flame_infused", type + "_flame_infused"));
   }

   private ItemModelBuilder vortex_spear(RegistryObject<Item> item) {
      return this.handheldSingleTextureModdedItem(item, new ResourceLocation("trbeyond", "item/vortex_spear"));
   }

   private ItemModelBuilder handheldItem(RegistryObject<? extends Item> item) {
      return this.handheldMinecraftItem(item, "item/handheld");
   }

   private ItemModelBuilder handheldModdedItem(RegistryObject<? extends Item> item, ResourceLocation parent) {
      return (ItemModelBuilder)((ItemModelBuilder)this.withExistingParent(item.getId().getPath(), parent))
         .texture("layer0", new ResourceLocation("trbeyond", "item/" + item.getId().getPath()));
   }

   private ItemModelBuilder handheldMinecraftItem(RegistryObject<? extends Item> item, String parent) {
      return (ItemModelBuilder)((ItemModelBuilder)this.withExistingParent(item.getId().getPath(), this.mcLoc(parent)))
         .texture("layer0", new ResourceLocation("trbeyond", "item/" + item.getId().getPath()));
   }

   private ItemModelBuilder meat_cleaver(RegistryObject<Item> item) {
      return this.handheldSingleTextureModdedItem(item, new ResourceLocation("trbeyond", "item/meat_cleaver"));
   }

   private ItemModelBuilder scale_shield(RegistryObject<Item> item) {
      return this.handheldSingleTextureModdedItem(item, new ResourceLocation("trbeyond", "item/scale_shield"));
   }

   private void shibkobo(RegistryObject<Item> item) {
      String path = "item/" + item.getId().getPath();
      ResourceLocation type = this.modLoc("item/shibkobo");
      ItemModelBuilder modelBuilder = (ItemModelBuilder)this.withExistingParent(item.getId().getPath(), type);
      modelBuilder.override()
         .predicate(new ResourceLocation("trbeyond", "attack_range"), 1.0F)
         .model((ItemModelBuilder)this.withExistingParent(item.getId().getPath() + "-1", type + "-1"));
      modelBuilder.override()
         .predicate(new ResourceLocation("trbeyond", "attack_range"), 2.0F)
         .model((ItemModelBuilder)this.withExistingParent(item.getId().getPath() + "-2", type + "-2"));
      modelBuilder.override()
         .predicate(new ResourceLocation("trbeyond", "attack_range"), 3.0F)
         .model((ItemModelBuilder)this.withExistingParent(item.getId().getPath() + "-3", type + "-3"));
      modelBuilder.override()
         .predicate(new ResourceLocation("trbeyond", "attack_range"), 4.0F)
         .model((ItemModelBuilder)this.withExistingParent(item.getId().getPath() + "-4", type + "-4"));
      modelBuilder.override()
         .predicate(new ResourceLocation("trbeyond", "attack_range"), 5.0F)
         .model((ItemModelBuilder)this.withExistingParent(item.getId().getPath() + "-5", type + "-5"));
   }

   private ItemModelBuilder handheldSingleTextureModdedItem(RegistryObject<? extends Item> item, ResourceLocation sharedModel) {
      return (ItemModelBuilder)this.withExistingParent(item.getId().getPath(), sharedModel);
   }

   private void trapdoorItem(RegistryObject<Block> block) {
      this.withExistingParent(
         "trbeyond:" + ForgeRegistries.BLOCKS.getKey((Block)block.get()).getPath(),
         this.modLoc("block/variated/trapdoor/" + ForgeRegistries.BLOCKS.getKey((Block)block.get()).getPath() + "_bottom")
      );
   }

   private void wallItem(RegistryObject<Block> block) {
      this.withExistingParent(
         "trbeyond:" + ForgeRegistries.BLOCKS.getKey((Block)block.get()).getPath(),
         this.modLoc("block/variated/wall/" + ForgeRegistries.BLOCKS.getKey((Block)block.get()).getPath() + "_inventory")
      );
   }

   private void stairItem(RegistryObject<Block> block) {
      this.withExistingParent(
         "trbeyond:" + ForgeRegistries.BLOCKS.getKey((Block)block.get()).getPath(),
         this.modLoc("block/variated/stairs/" + ForgeRegistries.BLOCKS.getKey((Block)block.get()).getPath())
      );
   }

   public ItemModelBuilder basicerItem(Item item) {
      ResourceLocation itemLocation = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));
      String cleanedPath = this.cleanItemPath(itemLocation.getPath());
      return (ItemModelBuilder)((ItemModelBuilder)this.withExistingParent(itemLocation.toString(), "item/generated"))
         .texture("layer0", new ResourceLocation(itemLocation.getNamespace(), "item/" + cleanedPath));
   }

   private String cleanItemPath(String path) {
      return path.replace("_unique", "").replace("_legend", "").replace("_god", "").replace("_special", "");
   }
}
