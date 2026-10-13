package io.github.Memoires.trbeyond.ability.battlewill.utility;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.skill.SkillRegistry;
import io.github.Memoires.trbeyond.ability.KurobeData;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TRBeyondSkills {
   public static final DeferredRegister<ManasSkill> SKILLS = DeferredRegister.create(SkillRegistry.REGISTRY_KEY, "trbeyond");
   public static final RegistryObject<ManasSkill> SKY_WALKING_ART = SKILLS.register("sky_walking_art", SkyWalkingArt::new);
   public static final RegistryObject<ManasSkill> KROUBE_ENChANT_SHOP = SKILLS.register("kroube_enchant_shop", KurobeData::new);

   public static void register(IEventBus eventBus) {
      SKILLS.register(eventBus);
   }
}
