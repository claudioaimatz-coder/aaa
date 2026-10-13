package io.github.Memoires.trbeyond.registry.effects;

import io.github.Memoires.trbeyond.effect.DodgeEffect;
import io.github.Memoires.trbeyond.effect.LimitedEffect;
import io.github.Memoires.trbeyond.effect.NoDodgeEffect;
import io.github.Memoires.trbeyond.effect.RotEffect;
import java.awt.Color;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BeyondEffects {
   public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "trbeyond");
   public static final RegistryObject<MobEffect> LIMITED = EFFECTS.register(
      "limited", () -> new LimitedEffect(MobEffectCategory.NEUTRAL, new Color(73, 4, 82).getRGB())
   );
   public static final RegistryObject<MobEffect> DODGE = EFFECTS.register(
      "dodge", () -> new DodgeEffect(MobEffectCategory.NEUTRAL, new Color(45, 40, 20).getRGB())
   );
   public static final RegistryObject<MobEffect> NO_DODGE = EFFECTS.register(
      "no_dodge", () -> new NoDodgeEffect(MobEffectCategory.NEUTRAL, new Color(45, 40, 20).getRGB())
   );
   public static final RegistryObject<MobEffect> ROT = EFFECTS.register("rot", () -> new RotEffect(MobEffectCategory.HARMFUL, new Color(150, 75, 0).getRGB()));

   public static void init(IEventBus eventBus) {
      EFFECTS.register(eventBus);
   }
}
