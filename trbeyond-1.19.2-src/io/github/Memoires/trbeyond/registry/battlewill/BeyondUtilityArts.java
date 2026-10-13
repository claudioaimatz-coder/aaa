package io.github.Memoires.trbeyond.registry.battlewill;

import io.github.Memoires.trbeyond.ability.battlewill.utility.SkyWalkArt;
import net.minecraftforge.registries.RegistryObject;

public class BeyondUtilityArts {
   public static final RegistryObject<SkyWalkArt> SKY_WALK = BeyondArts.ART_REGISTRY.register("sky_walk", SkyWalkArt::new);

   public static void init() {
   }
}
