package io.github.Memoires.trbeyond.registry.structures;

import com.mojang.serialization.Codec;
import io.github.Memoires.trbeyond.worldgen.colosseum.Colosseum;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BeyondStructures {
   public static final DeferredRegister<StructureType<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, "trbeyond");
   public static final RegistryObject<StructureType<Colosseum>> COLOSSEUM = DEFERRED_REGISTRY_STRUCTURE.register(
      "colosseum", () -> explicitStructureTypeTyping(Colosseum.CODEC)
   );

   public static void init(IEventBus bus) {
      DEFERRED_REGISTRY_STRUCTURE.register(bus);
   }

   private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> structureCodec) {
      return () -> structureCodec;
   }
}
