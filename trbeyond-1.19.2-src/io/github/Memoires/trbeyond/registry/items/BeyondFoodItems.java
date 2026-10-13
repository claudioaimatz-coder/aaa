package io.github.Memoires.trbeyond.registry.items;

import com.github.manasmods.manascore.api.data.gen.annotation.GenerateItemModels;
import io.github.Memoires.trbeyond.items.food.BrandyBottleFoodItem;
import io.github.Memoires.trbeyond.items.food.BrandyGlassFoodItem;
import io.github.Memoires.trbeyond.items.food.BulkMeatSkewerFoodItem;
import io.github.Memoires.trbeyond.items.food.HoneyFoodItem;
import io.github.Memoires.trbeyond.items.food.MeatFoodItem;
import io.github.Memoires.trbeyond.items.food.MegalodonSashimiFoodItem;
import io.github.Memoires.trbeyond.items.food.PorkRamenFoodItem;
import io.github.Memoires.trbeyond.items.food.ShunaSoupItem;
import io.github.Memoires.trbeyond.items.food.SushiPlateFoodItem;
import io.github.Memoires.trbeyond.items.food.TamahoyakiFoodItem;
import io.github.Memoires.trbeyond.items.food.TeaFoodItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@GenerateItemModels
public class BeyondFoodItems {
   private static final DeferredRegister<Item> registry = DeferredRegister.create(ForgeRegistries.ITEMS, "trbeyond");
   public static final RegistryObject<Item> TEA = registry.register("tea", TeaFoodItem::new);
   public static final RegistryObject<Item> BULK_MEAT_SKEWER = registry.register("bulk_meat_skewer", BulkMeatSkewerFoodItem::new);
   public static final RegistryObject<Item> MEAT = registry.register("meat", MeatFoodItem::new);
   public static final RegistryObject<Item> MEGALODON_SASHIMI = registry.register("megalodon_sashimi", MegalodonSashimiFoodItem::new);
   public static final RegistryObject<Item> PORK_RAMEN = registry.register("pork_ramen", PorkRamenFoodItem::new);
   public static final RegistryObject<Item> SHUNA_SOUP = registry.register("shuna_soup", ShunaSoupItem::new);
   public static final RegistryObject<Item> SUSHI_PLATE = registry.register("sushi_plate", SushiPlateFoodItem::new);
   public static final RegistryObject<Item> TAMAHOYAKI = registry.register("tamahoyaki", TamahoyakiFoodItem::new);
   public static final RegistryObject<Item> BRANDY_BOTTLE = registry.register("brandy_bottle", BrandyBottleFoodItem::new);
   public static final RegistryObject<Item> BRANDY_GLASS = registry.register("brandy_glass", BrandyGlassFoodItem::new);
   public static final RegistryObject<Item> HONEY = registry.register("honey", HoneyFoodItem::new);

   public static void init(IEventBus modEventBus) {
      registry.register(modEventBus);
   }
}
