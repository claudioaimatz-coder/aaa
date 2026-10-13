package io.github.Memoires.trbeyond.registry;

import io.github.Memoires.trbeyond.loot.BeyondLootModifiers;
import io.github.Memoires.trbeyond.registry.battlewill.BeyondArts;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlockEntities;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlocks;
import io.github.Memoires.trbeyond.registry.effects.BeyondAnimatedEffects;
import io.github.Memoires.trbeyond.registry.effects.BeyondEffects;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import io.github.Memoires.trbeyond.registry.items.BeyondArmorItems;
import io.github.Memoires.trbeyond.registry.items.BeyondFoodItems;
import io.github.Memoires.trbeyond.registry.items.BeyondItems;
import io.github.Memoires.trbeyond.registry.items.BeyondToolItems;
import io.github.Memoires.trbeyond.registry.menu.BeyondMenus;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import io.github.Memoires.trbeyond.registry.structures.BeyondStructures;
import io.github.Memoires.trbeyond.world.BeyondGamerules;
import net.minecraftforge.eventbus.api.IEventBus;

public class BeyondRegistry {
   public static void register(IEventBus modEventBus) {
      BeyondEnchantments.init(modEventBus);
      BeyondSoundEvents.register(modEventBus);
      BeyondItems.register(modEventBus);
      BeyondBlocks.register(modEventBus);
      BeyondBlockEntities.register(modEventBus);
      BeyondEntities.register(modEventBus);
      BeyondToolItems.init(modEventBus);
      BeyondArmorItems.init(modEventBus);
      BeyondLootModifiers.init(modEventBus);
      BeyondEffects.init(modEventBus);
      BeyondStructures.init(modEventBus);
      BeyondMenus.register(modEventBus);
      BeyondArts.register(modEventBus);
      BeyondAnimatedEffects.register(modEventBus);
      BeyondFoodItems.init(modEventBus);
      BeyondGamerules.registryGameRules();
   }
}
