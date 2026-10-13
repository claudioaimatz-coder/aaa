package io.github.Memoires.trbeyond.registry.menu;

import io.github.Memoires.trbeyond.client.screen.KurobeMainMenu;
import io.github.Memoires.trbeyond.client.screen.KurobeShopMenu;
import io.github.Memoires.trbeyond.client.screen.MjollmileMenu;
import io.github.Memoires.trbeyond.client.screen.ResearcherEnchantmentShopMenu;
import io.github.Memoires.trbeyond.client.screen.SubspaceContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BeyondMenus {
   public static final DeferredRegister<MenuType<?>> MENU_REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, "trbeyond");
   public static final RegistryObject<MenuType<KurobeShopMenu>> KUROBE_MENU = MENU_REGISTRY.register("kurobe", () -> IForgeMenuType.create(KurobeShopMenu::new));
   public static final RegistryObject<MenuType<MjollmileMenu>> MJOLLMILE_MENU = MENU_REGISTRY.register(
      "mjollmile", () -> IForgeMenuType.create(MjollmileMenu::new)
   );
   public static final RegistryObject<MenuType<ResearcherEnchantmentShopMenu>> RESEARCHER_SHOP_MENU = MENU_REGISTRY.register(
      "custom_researcher_menu", () -> IForgeMenuType.create(ResearcherEnchantmentShopMenu::new)
   );
   public static final RegistryObject<MenuType<KurobeMainMenu>> KUROBE_MAIN_MENU = MENU_REGISTRY.register(
      "kurobe_main_menu", () -> IForgeMenuType.create(KurobeMainMenu::new)
   );
   public static final RegistryObject<MenuType<SubspaceContainerMenu>> SUBSPACE_CONTAINER_MENU = MENU_REGISTRY.register(
      "subspace_container", () -> IForgeMenuType.create(SubspaceContainerMenu::fromNetwork)
   );

   public static void register(IEventBus bus) {
      MENU_REGISTRY.register(bus);
   }
}
