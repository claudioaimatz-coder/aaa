package io.github.Memoires.trbeyond.registry.items;

import com.github.manasmods.manascore.api.data.gen.annotation.GenerateItemModels;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.items.weapons.CentipedeDaggerItem;
import io.github.Memoires.trbeyond.items.weapons.DiabloClawItem;
import io.github.Memoires.trbeyond.items.weapons.GlendaPistolItem;
import io.github.Memoires.trbeyond.items.weapons.GobtaKodachiItem;
import io.github.Memoires.trbeyond.items.weapons.GorikimaruItem;
import io.github.Memoires.trbeyond.items.weapons.GurenItem;
import io.github.Memoires.trbeyond.items.weapons.HakuroItem;
import io.github.Memoires.trbeyond.items.weapons.MeatCleaverItem;
import io.github.Memoires.trbeyond.items.weapons.MinosBardicheItem;
import io.github.Memoires.trbeyond.items.weapons.MinosTridentItem;
import io.github.Memoires.trbeyond.items.weapons.NinjatoItem;
import io.github.Memoires.trbeyond.items.weapons.OgreAxeItem;
import io.github.Memoires.trbeyond.items.weapons.RedSpearItem;
import io.github.Memoires.trbeyond.items.weapons.ShibkoboItem;
import io.github.Memoires.trbeyond.items.weapons.SpiderDaggerItem;
import io.github.Memoires.trbeyond.items.weapons.VortexSpearItem;
import io.github.Memoires.trbeyond.items.weapons.scaleShieldItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@GenerateItemModels
public class BeyondToolItems {
   private static final DeferredRegister<Item> registry = DeferredRegister.create(ForgeRegistries.ITEMS, "trbeyond");
   public static final RegistryObject<Item> CENTIPEDE_DAGGER_SPECIAL = registry.register(
      "centipede_dagger_special", () -> new CentipedeDaggerItem(CentipedeDaggerItem.Grade.SPECIAL)
   );
   public static final RegistryObject<Item> CENTIPEDE_DAGGER_RARE = registry.register(
      "centipede_dagger_rare", () -> new CentipedeDaggerItem(CentipedeDaggerItem.Grade.RARE)
   );
   public static final RegistryObject<Item> CENTIPEDE_DAGGER_UNIQUE = registry.register(
      "centipede_dagger_unique", () -> new CentipedeDaggerItem(CentipedeDaggerItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> CENTIPEDE_DAGGER_LEGEND = registry.register(
      "centipede_dagger_legend", () -> new CentipedeDaggerItem(CentipedeDaggerItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> SPIDER_DAGGER_SPECIAL = registry.register(
      "spider_dagger_special", () -> new SpiderDaggerItem(SpiderDaggerItem.Grade.SPECIAL)
   );
   public static final RegistryObject<Item> SPIDER_DAGGER_RARE = registry.register(
      "spider_dagger_rare", () -> new SpiderDaggerItem(SpiderDaggerItem.Grade.RARE)
   );
   public static final RegistryObject<Item> SPIDER_DAGGER_UNIQUE = registry.register(
      "spider_dagger_unique", () -> new SpiderDaggerItem(SpiderDaggerItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> SPIDER_DAGGER_LEGEND = registry.register(
      "spider_dagger_legend", () -> new SpiderDaggerItem(SpiderDaggerItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> OGRE_AXE_RARE = registry.register("ogre_axe_rare", () -> new OgreAxeItem(OgreAxeItem.Grade.RARE));
   public static final RegistryObject<Item> OGRE_AXE_UNIQUE = registry.register("ogre_axe_unique", () -> new OgreAxeItem(OgreAxeItem.Grade.UNIQUE));
   public static final RegistryObject<Item> OGRE_AXE_LEGEND = registry.register("ogre_axe_legend", () -> new OgreAxeItem(OgreAxeItem.Grade.LEGEND));
   public static final RegistryObject<Item> OGRE_AXE_GOD = registry.register("ogre_axe_god", () -> new OgreAxeItem(OgreAxeItem.Grade.GOD));
   public static final RegistryObject<Item> GORIKIMARU_UNIQUE = registry.register("gorikimaru_unique", () -> new GorikimaruItem(GorikimaruItem.Grade.UNIQUE));
   public static final RegistryObject<Item> GORIKIMARU_LEGEND = registry.register("gorikimaru_legend", () -> new GorikimaruItem(GorikimaruItem.Grade.LEGEND));
   public static final RegistryObject<Item> GORIKIMARU_GOD = registry.register("gorikimaru_god", () -> new GorikimaruItem(GorikimaruItem.Grade.GOD));
   public static final RegistryObject<Item> SHIBKOBO_UNIQUE = registry.register("shibkobo_unique", () -> new ShibkoboItem(ShibkoboItem.Grade.UNIQUE));
   public static final RegistryObject<Item> SHIBKOBO_LEGEND = registry.register("shibkobo_legend", () -> new ShibkoboItem(ShibkoboItem.Grade.LEGENDARY));
   public static final RegistryObject<Item> SHIBKOBO_GOD = registry.register("shibkobo_god", () -> new ShibkoboItem(ShibkoboItem.Grade.GOD));
   public static final RegistryObject<Item> GUREN_UNIQUE = registry.register("guren_unique", () -> new GurenItem(GurenItem.Grade.UNIQUE));
   public static final RegistryObject<Item> GUREN_LEGEND = registry.register("guren_legend", () -> new GurenItem(GurenItem.Grade.LEGEND));
   public static final RegistryObject<Item> GUREN_GOD = registry.register("guren_god", () -> new GurenItem(GurenItem.Grade.GOD));
   public static final RegistryObject<Item> VORTEX_SPEAR_UNIQUE = registry.register(
      "vortex_spear_unique", () -> new VortexSpearItem(VortexSpearItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> VORTEX_SPEAR_LEGEND = registry.register(
      "vortex_spear_legend", () -> new VortexSpearItem(VortexSpearItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> VORTEX_SPEAR_GOD = registry.register("vortex_spear_god", () -> new VortexSpearItem(VortexSpearItem.Grade.GOD));
   public static final RegistryObject<Item> NINJATO_UNIQUE = registry.register("ninjato_unique", () -> new NinjatoItem(NinjatoItem.Grade.UNIQUE));
   public static final RegistryObject<Item> NINJATO_LEGEND = registry.register("ninjato_legend", () -> new NinjatoItem(NinjatoItem.Grade.LEGEND));
   public static final RegistryObject<Item> HAKURO_UNIQUE = registry.register("hakuro_unique", () -> new HakuroItem(HakuroItem.Grade.UNIQUE));
   public static final RegistryObject<Item> HAKURO_LEGEND = registry.register("hakuro_legend", () -> new HakuroItem(HakuroItem.Grade.LEGEND));
   public static final RegistryObject<Item> MINOS_BARDICHE_UNIQUE = registry.register(
      "minos_bardiche_unique_base", () -> new MinosBardicheItem(MinosBardicheItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> MINOS_BARDICHE_LEGEND = registry.register(
      "minos_bardiche_legend_base", () -> new MinosBardicheItem(MinosBardicheItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> MINOS_BARDICHE_GOD = registry.register(
      "minos_bardiche_god_base", () -> new MinosBardicheItem(MinosBardicheItem.Grade.GOD)
   );
   public static final RegistryObject<Item> MINOS_TRIDENT_UNIQUE = registry.register(
      "minos_trident_unique_base", () -> new MinosTridentItem(MinosTridentItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> MINOS_TRIDENT_LEGEND = registry.register(
      "minos_trident_legend_base", () -> new MinosTridentItem(MinosTridentItem.Grade.LEGEND)
   );
   public static final RegistryObject<Item> MINOS_TRIDENT_GOD = registry.register(
      "minos_trident_god_base", () -> new MinosTridentItem(MinosTridentItem.Grade.GOD)
   );
   public static final RegistryObject<Item> MEAT_CLEAVER_UNIQUE = registry.register(
      "meat_cleaver_unique", () -> new MeatCleaverItem(MeatCleaverItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> MEAT_CLEAVER_LEGEND = registry.register(
      "meat_cleaver_legend", () -> new MeatCleaverItem(MeatCleaverItem.Grade.LEGENDARY)
   );
   public static final RegistryObject<Item> MEAT_CLEAVER_GOD = registry.register("meat_cleaver_god", () -> new MeatCleaverItem(MeatCleaverItem.Grade.GOD));
   public static final RegistryObject<Item> SCALE_SHIELD_UNIQUE = registry.register(
      "scale_shield_unique", () -> new scaleShieldItem(scaleShieldItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> SCALE_SHIELD_LEGEND = registry.register(
      "scale_shield_legend", () -> new scaleShieldItem(scaleShieldItem.Grade.LEGENDARY)
   );
   public static final RegistryObject<Item> SCALE_SHIELD_GOD = registry.register("scale_shield_god", () -> new scaleShieldItem(scaleShieldItem.Grade.GOD));
   public static final RegistryObject<Item> DIABLO_CLAW_LEGEND = registry.register("diablo_claw_legend", () -> new DiabloClawItem(DiabloClawItem.Grade.LEGEND));
   public static final RegistryObject<Item> DIABLO_CLAW_GOD = registry.register("diablo_claw_god", () -> new DiabloClawItem(DiabloClawItem.Grade.GOD));
   public static final RegistryObject<Item> RED_SPEAR_UNIQUE = registry.register("red_spear_unique", () -> new RedSpearItem(RedSpearItem.Grade.UNIQUE));
   public static final RegistryObject<Item> RED_SPEAR_LEGEND = registry.register("red_spear_legend", () -> new RedSpearItem(RedSpearItem.Grade.LEGENDARY));
   public static final RegistryObject<Item> GOBTA_KODACHI_RARE = registry.register(
      "gobta_kodachi_rare", () -> new GobtaKodachiItem(GobtaKodachiItem.Grade.RARE)
   );
   public static final RegistryObject<Item> GOBTA_KODACHI_UNIQUE = registry.register(
      "gobta_kodachi_unique", () -> new GobtaKodachiItem(GobtaKodachiItem.Grade.UNIQUE)
   );
   public static final RegistryObject<Item> GLENDA_PISTOL = registry.register(
      "glenda_pistol", () -> new GlendaPistolItem(new Properties().durability(128).rarity(Rarity.RARE).tab(BeyondItemGroup.BEYOND_ITEMS))
   );

   public static void init(IEventBus modEventBus) {
      registry.register(modEventBus);
   }
}
