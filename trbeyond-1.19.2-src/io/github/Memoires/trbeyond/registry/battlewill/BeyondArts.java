package io.github.Memoires.trbeyond.registry.battlewill;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

public class BeyondArts {
   public static final DeferredRegister<ManasSkill> ART_REGISTRY = DeferredRegister.create(SkillAPI.getSkillRegistryKey(), "trbeyond");

   public static void register(IEventBus modEventBus) {
      BeyondUtilityArts.init();
      ART_REGISTRY.register(modEventBus);
   }
}
