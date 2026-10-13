package io.github.Memoires.trbeyond.data;

import com.github.manasmods.manascore.api.data.gen.CustomDataProvider;
import com.github.manasmods.tensura.data.pack.GearEPCount;
import com.google.gson.JsonElement;
import io.github.Memoires.trbeyond.registry.items.BeyondArmorItems;
import io.github.Memoires.trbeyond.registry.items.BeyondToolItems;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.data.event.GatherDataEvent;

public class BeyondGearEPProvider extends CustomDataProvider {
   public BeyondGearEPProvider(GatherDataEvent gatherDataEvent) {
      super("gear/ep", gatherDataEvent.getGenerator());
   }

   public String getName() {
      return "Beyond Gear EP";
   }

   protected void run(BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
      ogre(biConsumer);
      serpent(biConsumer);
      minos(biConsumer);
      benimaru(biConsumer);
      shion(biConsumer);
      daggers(biConsumer);
      spears(biConsumer);
      geld(biConsumer);
      diablo(biConsumer);
      cleavers(biConsumer);
      shields(biConsumer);
      shibkobo(biConsumer);
      gabiru(biConsumer);
      red_spear(biConsumer);
   }

   protected static void red_spear(BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
      GearEPCount.of(BeyondToolItems.RED_SPEAR_UNIQUE.getId(), 52000, 225000, 0.035, BeyondToolItems.RED_SPEAR_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.RED_SPEAR_LEGEND.getId(), 225000, 225000, 0.04).buildJson(biConsumer);
   }

   protected static void ogre(BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
      GearEPCount.of(BeyondToolItems.OGRE_AXE_RARE.getId(), 18000, 52000, 0.03, BeyondToolItems.OGRE_AXE_UNIQUE.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.OGRE_CHESTPLATE_RARE.getId(), 18000, 52000, 0.03, BeyondArmorItems.OGRE_CHESTPLATE_UNIQUE.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.OGRE_LEGGINGS_RARE.getId(), 18000, 52000, 0.03, BeyondArmorItems.OGRE_LEGGINGS_UNIQUE.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.OGRE_BOOTS_RARE.getId(), 18000, 52000, 0.03, BeyondArmorItems.OGRE_BOOTS_UNIQUE.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.OGRE_AXE_UNIQUE.getId(), 52000, 225000, 0.035, BeyondToolItems.OGRE_AXE_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.OGRE_CHESTPLATE_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.OGRE_CHESTPLATE_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.OGRE_LEGGINGS_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.OGRE_LEGGINGS_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.OGRE_BOOTS_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.OGRE_BOOTS_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.OGRE_AXE_LEGEND.getId(), 225000, 1000000, 0.04, BeyondToolItems.OGRE_AXE_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.OGRE_CHESTPLATE_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.OGRE_CHESTPLATE_GOD.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.OGRE_LEGGINGS_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.OGRE_LEGGINGS_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.OGRE_BOOTS_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.OGRE_BOOTS_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.OGRE_AXE_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.OGRE_CHESTPLATE_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.OGRE_LEGGINGS_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.OGRE_BOOTS_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.GORIKIMARU_UNIQUE.getId(), 52000, 225000, 0.035, BeyondToolItems.GORIKIMARU_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.GORIKIMARU_LEGEND.getId(), 225000, 1000000, 0.04, BeyondToolItems.GORIKIMARU_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.GORIKIMARU_GOD.getId(), 1000000, 1080000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.GUREN_UNIQUE.getId(), 52000, 225000, 0.035, BeyondToolItems.GUREN_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.GUREN_LEGEND.getId(), 225000, 1000000, 0.04, BeyondToolItems.GUREN_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.GUREN_GOD.getId(), 1000000, 1140000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.NINJATO_UNIQUE.getId(), 52000, 225000, 0.035, BeyondToolItems.NINJATO_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.NINJATO_LEGEND.getId(), 225000, 750000, 0.04).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.HAKURO_UNIQUE.getId(), 52000, 225000, 0.035, BeyondToolItems.HAKURO_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.HAKURO_LEGEND.getId(), 225000, 500000, 0.04).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.HAKURO_CHESTPLATE_UNIQUE.getId(), 225000, 1000000, 0.04, BeyondArmorItems.HAKURO_CHESTPLATE_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.HAKURO_LEGGINGS_UNIQUE.getId(), 225000, 1000000, 0.04, BeyondArmorItems.HAKURO_LEGGINGS_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.HAKURO_CHESTPLATE_LEGEND.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.HAKURO_LEGGINGS_LEGEND.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SOUEI_CHESTPLATE_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.SOUEI_CHESTPLATE_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SOUEI_LEGGINGS_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.SOUEI_LEGGINGS_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SOUEI_CHESTPLATE_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.SOUEI_CHESTPLATE_GOD.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SOUEI_LEGGINGS_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.SOUEI_LEGGINGS_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SOUEI_CHESTPLATE_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SOUEI_LEGGINGS_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
   }

   protected static void serpent(BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
      GearEPCount.of(BeyondArmorItems.SERPENT_HELMET_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.SERPENT_HELMET_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SERPENT_CHESTPLATE_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.SERPENT_CHESTPLATE_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SERPENT_LEGGINGS_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.SERPENT_LEGGINGS_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SERPENT_BOOTS_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.SERPENT_BOOTS_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SERPENT_HELMET_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.SERPENT_HELMET_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SERPENT_CHESTPLATE_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.SERPENT_CHESTPLATE_GOD.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SERPENT_LEGGINGS_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.SERPENT_LEGGINGS_GOD.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SERPENT_BOOTS_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.SERPENT_BOOTS_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SERPENT_HELMET_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SERPENT_CHESTPLATE_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SERPENT_LEGGINGS_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SERPENT_BOOTS_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
   }

   protected static void minos(BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
      GearEPCount.of(BeyondArmorItems.MINOS_HELMET_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.MINOS_HELMET_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.MINOS_CHESTPLATE_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.MINOS_CHESTPLATE_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.MINOS_LEGGINGS_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.MINOS_LEGGINGS_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.MINOS_BOOTS_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.MINOS_BOOTS_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.MINOS_HELMET_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.MINOS_HELMET_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.MINOS_CHESTPLATE_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.MINOS_CHESTPLATE_GOD.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.MINOS_LEGGINGS_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.MINOS_LEGGINGS_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.MINOS_BOOTS_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.MINOS_BOOTS_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.MINOS_HELMET_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.MINOS_CHESTPLATE_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.MINOS_LEGGINGS_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.MINOS_BOOTS_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.MINOS_BARDICHE_UNIQUE.getId(), 52000, 225000, 0.035, BeyondToolItems.MINOS_BARDICHE_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.MINOS_BARDICHE_LEGEND.getId(), 225000, 1000000, 0.04, BeyondToolItems.MINOS_BARDICHE_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.MINOS_BARDICHE_GOD.getId(), 1000000, 1140000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.MINOS_TRIDENT_UNIQUE.getId(), 52000, 225000, 0.035, BeyondToolItems.MINOS_TRIDENT_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.MINOS_TRIDENT_LEGEND.getId(), 225000, 1000000, 0.04, BeyondToolItems.MINOS_TRIDENT_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.MINOS_TRIDENT_GOD.getId(), 1000000, 1140000, 0.05).buildJson(biConsumer);
   }

   protected static void benimaru(BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
      GearEPCount.of(BeyondArmorItems.BENIMARU_CHESTPLATE_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.BENIMARU_CHESTPLATE_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.BENIMARU_LEGGINGS_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.BENIMARU_LEGGINGS_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.BENIMARU_CHESTPLATE_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.BENIMARU_CHESTPLATE_GOD.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.BENIMARU_LEGGINGS_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.BENIMARU_LEGGINGS_GOD.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.BENIMARU_CHESTPLATE_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.BENIMARU_LEGGINGS_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
   }

   protected static void shion(BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
      GearEPCount.of(BeyondArmorItems.SHION_CHESTPLATE_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.SHION_CHESTPLATE_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SHION_LEGGINGS_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.SHION_LEGGINGS_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SHION_CHESTPLATE_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.SHION_CHESTPLATE_GOD.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SHION_LEGGINGS_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.SHION_LEGGINGS_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SHION_CHESTPLATE_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.SHION_LEGGINGS_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
   }

   protected static void daggers(BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
      GearEPCount.of(BeyondToolItems.CENTIPEDE_DAGGER_SPECIAL.getId(), 6000, 18000, 0.025, BeyondToolItems.CENTIPEDE_DAGGER_RARE.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.CENTIPEDE_DAGGER_RARE.getId(), 18000, 52000, 0.03, BeyondToolItems.CENTIPEDE_DAGGER_UNIQUE.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.CENTIPEDE_DAGGER_UNIQUE.getId(), 52000, 225000, 0.035, BeyondToolItems.CENTIPEDE_DAGGER_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.SPIDER_DAGGER_SPECIAL.getId(), 6000, 18000, 0.025, BeyondToolItems.SPIDER_DAGGER_RARE.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.SPIDER_DAGGER_RARE.getId(), 18000, 52000, 0.03, BeyondToolItems.SPIDER_DAGGER_UNIQUE.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.SPIDER_DAGGER_UNIQUE.getId(), 52000, 225000, 0.035, BeyondToolItems.SPIDER_DAGGER_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.CENTIPEDE_DAGGER_LEGEND.getId(), 225000, 500000, 0.04).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.SPIDER_DAGGER_LEGEND.getId(), 225000, 500000, 0.04).buildJson(biConsumer);
   }

   protected static void spears(BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
      GearEPCount.of(BeyondToolItems.VORTEX_SPEAR_UNIQUE.getId(), 6000, 52000, 0.025, BeyondToolItems.VORTEX_SPEAR_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.VORTEX_SPEAR_LEGEND.getId(), 52000, 1000000, 0.025, BeyondToolItems.VORTEX_SPEAR_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.VORTEX_SPEAR_GOD.getId(), 1000000, 1000000, 0.025).buildJson(biConsumer);
   }

   protected static void geld(BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
      GearEPCount.of(BeyondArmorItems.GELD_HELMET_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.GELD_HELMET_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.GELD_CHESTPLATE_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.GELD_CHESTPLATE_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.GELD_LEGGINGS_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.GELD_LEGGINGS_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.GELD_BOOTS_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.GELD_BOOTS_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.GELD_HELMET_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.GELD_HELMET_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.GELD_CHESTPLATE_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.GELD_CHESTPLATE_GOD.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.GELD_LEGGINGS_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.GELD_LEGGINGS_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.GELD_BOOTS_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.GELD_BOOTS_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.GELD_HELMET_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.GELD_CHESTPLATE_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.GELD_LEGGINGS_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.GELD_BOOTS_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
   }

   protected static void diablo(BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
      GearEPCount.of(BeyondArmorItems.DIABLO_CHESTPLATE_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.DIABLO_CHESTPLATE_GOD.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.DIABLO_LEGGINGS_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.DIABLO_LEGGINGS_GOD.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.DIABLO_CHESTPLATE_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.DIABLO_LEGGINGS_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.DIABLO_CLAW_LEGEND.getId(), 225000, 1000000, 0.12, BeyondToolItems.DIABLO_CLAW_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.DIABLO_CLAW_GOD.getId(), 1000000, 1140000, 0.15).buildJson(biConsumer);
   }

   protected static void cleavers(BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
      GearEPCount.of(BeyondToolItems.MEAT_CLEAVER_UNIQUE.getId(), 52000, 225000, 0.105, BeyondToolItems.MEAT_CLEAVER_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.MEAT_CLEAVER_LEGEND.getId(), 225000, 1000000, 0.12, BeyondToolItems.MEAT_CLEAVER_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.MEAT_CLEAVER_GOD.getId(), 1000000, 1000000, 0.15).buildJson(biConsumer);
   }

   protected static void shields(BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
      GearEPCount.of(BeyondToolItems.SCALE_SHIELD_UNIQUE.getId(), 52000, 225000, 0.105, BeyondToolItems.SCALE_SHIELD_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.SCALE_SHIELD_LEGEND.getId(), 225000, 1000000, 0.12, BeyondToolItems.SCALE_SHIELD_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.SCALE_SHIELD_GOD.getId(), 1000000, 1000000, 0.15).buildJson(biConsumer);
   }

   protected static void shibkobo(BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
      GearEPCount.of(BeyondToolItems.SHIBKOBO_UNIQUE.getId(), 52000, 225000, 0.035, BeyondToolItems.SHIBKOBO_LEGEND.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.SHIBKOBO_LEGEND.getId(), 225000, 1000000, 0.04, BeyondToolItems.SHIBKOBO_GOD.getId()).buildJson(biConsumer);
      GearEPCount.of(BeyondToolItems.SHIBKOBO_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
   }

   protected static void gabiru(BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
      GearEPCount.of(BeyondArmorItems.GABIRU_CHESTPLATE_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.GABIRU_CHESTPLATE_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.GABIRU_LEGGINGS_UNIQUE.getId(), 52000, 225000, 0.035, BeyondArmorItems.GABIRU_LEGGINGS_LEGEND.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.GABIRU_CHESTPLATE_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.GABIRU_CHESTPLATE_GOD.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.GABIRU_LEGGINGS_LEGEND.getId(), 225000, 1000000, 0.04, BeyondArmorItems.GABIRU_LEGGINGS_GOD.getId())
         .buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.GABIRU_CHESTPLATE_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
      GearEPCount.of(BeyondArmorItems.GABIRU_LEGGINGS_GOD.getId(), 1000000, 1000000, 0.05).buildJson(biConsumer);
   }
}
