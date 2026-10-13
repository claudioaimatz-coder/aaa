package io.github.Memoires.trbeyond.registry.enchantment;

import com.github.manasmods.tensura.enchantment.EngravingEnchantment;
import io.github.Memoires.trbeyond.enchantment.BattleInfusionEnchantment;
import io.github.Memoires.trbeyond.enchantment.DarknessInfusionEnchantment;
import io.github.Memoires.trbeyond.enchantment.EarthInfusionEnchantment;
import io.github.Memoires.trbeyond.enchantment.FireInfusionEnchantment;
import io.github.Memoires.trbeyond.enchantment.FlameInfusionEnchantment;
import io.github.Memoires.trbeyond.enchantment.LandInfusionEnchantment;
import io.github.Memoires.trbeyond.enchantment.LightInfusionEnchantment;
import io.github.Memoires.trbeyond.enchantment.SpatialInfusionEnchantment;
import io.github.Memoires.trbeyond.enchantment.WaterInfusionEnchantment;
import io.github.Memoires.trbeyond.enchantment.WindInfusionEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BeyondEnchantments {
   private static final DeferredRegister<Enchantment> registry = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, "trbeyond");
   public static final RegistryObject<EngravingEnchantment> BATTLE_INFUSION = registry.register("battle_infusion", BattleInfusionEnchantment::new);
   public static final RegistryObject<EngravingEnchantment> DARKNESS_INFUSION = registry.register("darkness_infusion", DarknessInfusionEnchantment::new);
   public static final RegistryObject<EngravingEnchantment> EARTH_INFUSION = registry.register("earth_infusion", EarthInfusionEnchantment::new);
   public static final RegistryObject<EngravingEnchantment> FIRE_INFUSION = registry.register("fire_infusion", FireInfusionEnchantment::new);
   public static final RegistryObject<EngravingEnchantment> FLAME_INFUSION = registry.register("flame_infusion", FlameInfusionEnchantment::new);
   public static final RegistryObject<EngravingEnchantment> LAND_INFUSION = registry.register("land_infusion", LandInfusionEnchantment::new);
   public static final RegistryObject<EngravingEnchantment> WATER_INFUSION = registry.register("water_infusion", WaterInfusionEnchantment::new);
   public static final RegistryObject<EngravingEnchantment> LIGHT_INFUSION = registry.register("light_infusion", LightInfusionEnchantment::new);
   public static final RegistryObject<EngravingEnchantment> WIND_INFUSION = registry.register("wind_infusion", WindInfusionEnchantment::new);
   public static final RegistryObject<EngravingEnchantment> SPATIAL_INFUSION = registry.register("spatial_infusion", SpatialInfusionEnchantment::new);

   public static void init(IEventBus modEventBus) {
      registry.register(modEventBus);
   }
}
