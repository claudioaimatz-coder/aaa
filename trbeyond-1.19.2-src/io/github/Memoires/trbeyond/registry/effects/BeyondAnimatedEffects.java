package io.github.Memoires.trbeyond.registry.effects;

import io.github.Memoires.trbeyond.items.effects.EffectItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BeyondAnimatedEffects {
   public static final DeferredRegister<Item> registry = DeferredRegister.create(ForgeRegistries.ITEMS, "trbeyond");
   public static final RegistryObject<Item> MINOS_TRIDENT_EFFECT = registry.register("minos_trident_effect", EffectItem.MinosTrident::new);
   public static final RegistryObject<Item> GUREN_EFFECT = registry.register("guren_effect", EffectItem.Guren::new);
   public static final RegistryObject<Item> GORIKIMARU_EFFECT = registry.register("gorikimaru_effect", EffectItem.Gorikimaru::new);
   public static final RegistryObject<Item> GABIRU_EFFECT = registry.register("gabiru_effect", EffectItem.Gabiru::new);
   public static final RegistryObject<Item> HARUKO_SWORD_EFFECT = registry.register("haruko_sword_effect", EffectItem.Haruko::new);
   public static final RegistryObject<Item> GELDS_EFFECT = registry.register("gelds_effect", EffectItem.Gelds::new);
   public static final RegistryObject<Item> CHAOS_EATER = registry.register("chaos_eater", () -> new Item(new Properties()));

   public static void register(IEventBus eventBus) {
      registry.register(eventBus);
   }
}
