package io.github.Memoires.trbeyond.registry.sounds;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BeyondSoundEvents {
   private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "trbeyond");
   public static RegistryObject<SoundEvent> VORTEX_CRASH = registerSoundEvent("vortex_crash");
   public static RegistryObject<SoundEvent> HAKURO_ATTACK = registerSoundEvent("hakuro_attack");
   public static RegistryObject<SoundEvent> BLACK_FLAME_SLASH = registerSoundEvent("black_flame_slash");
   public static RegistryObject<SoundEvent> OGRE_SWORD_CANNON = registerSoundEvent("ogre_sword_cannon");
   public static RegistryObject<SoundEvent> evil_centipede_movement = registerSoundEvent("evil_centipede_movement");
   public static RegistryObject<SoundEvent> evil_centipede_floor_slam = registerSoundEvent("evil_centipede_floor_slam");
   public static RegistryObject<SoundEvent> evil_centipede_bite = registerSoundEvent("evil_centipede_bite");
   public static RegistryObject<SoundEvent> evil_centipede_grab_slam = registerSoundEvent("evil_centipede_grab_slam");
   public static RegistryObject<SoundEvent> ogre_lord_ogre_flame = registerSoundEvent("ogre_lord_ogre_flame");
   public static RegistryObject<SoundEvent> ogre_lord_meteorite_strike = registerSoundEvent("ogre_lord_meteorite_strike");
   public static RegistryObject<SoundEvent> ogre_lord_double_strike = registerSoundEvent("ogre_lord_double_strike");
   public static RegistryObject<SoundEvent> tempest_serpent_bite = registerSoundEvent("tempest_serpent_bite");
   public static RegistryObject<SoundEvent> tempest_serpent_constrict = registerSoundEvent("tempest_serpent_constrict");
   public static RegistryObject<SoundEvent> tempest_serpent_movement = registerSoundEvent("tempest_serpent_movement");
   public static RegistryObject<SoundEvent> tempest_serpent_tail_whip = registerSoundEvent("tempest_serpent_tail_whip");
   public static RegistryObject<SoundEvent> tempest_serpent_poison_cloud = registerSoundEvent("tempest_serpent_poison_cloud");
   public static RegistryObject<SoundEvent> gozul_block = registerSoundEvent("gozul_block");
   public static RegistryObject<SoundEvent> gozul_electric_horn = registerSoundEvent("gozul_electric_horn");
   public static RegistryObject<SoundEvent> gozul_jump_attack = registerSoundEvent("gozul_jump_attack");
   public static RegistryObject<SoundEvent> gozul_lightning_charge = registerSoundEvent("gozul_lightning_charge");
   public static RegistryObject<SoundEvent> gozul_regular_swing = registerSoundEvent("gozul_regular_swing");
   public static RegistryObject<SoundEvent> gozul_throw = registerSoundEvent("gozul_throw");
   public static RegistryObject<SoundEvent> GUN_SHOT = registerSoundEvent("gun_shot");
   public static RegistryObject<SoundEvent> mezul_block = registerSoundEvent("mezul_block");
   public static RegistryObject<SoundEvent> mezul_kick = registerSoundEvent("mezul_kick");
   public static RegistryObject<SoundEvent> mezul_repeat_strike = registerSoundEvent("mezul_repeat_strike");
   public static RegistryObject<SoundEvent> mezul_spin = registerSoundEvent("mezul_spin");
   public static RegistryObject<SoundEvent> mezul_spin_strike = registerSoundEvent("mezul_spin_strike");
   public static RegistryObject<SoundEvent> mezul_step_loop = registerSoundEvent("mezul_step_loop");
   public static RegistryObject<SoundEvent> mezul_strike = registerSoundEvent("mezul_strike");
   public static RegistryObject<SoundEvent> milim_slime_dash = registerSoundEvent("milim_slime_dash");
   public static RegistryObject<SoundEvent> living_armor_walk = registerSoundEvent("living_armor_walk");
   public static RegistryObject<SoundEvent> living_armor_strike = registerSoundEvent("living_armor_strike");
   public static RegistryObject<SoundEvent> living_armor_spin = registerSoundEvent("living_armor_spin");
   public static RegistryObject<SoundEvent> golden_skeleton_swing = registerSoundEvent("golden_skeleton_swing");
   public static RegistryObject<SoundEvent> ghost_mage_wind_cutter = registerSoundEvent("ghost_mage_wind_cutter");
   public static RegistryObject<SoundEvent> ghost_mage_holy_ray = registerSoundEvent("ghost_mage_holy_ray");
   public static RegistryObject<SoundEvent> diablo_claw_deflect = registerSoundEvent("diablo_claw_deflect");

   private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
      return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation("trbeyond", name)));
   }

   public static void register(IEventBus eventBus) {
      SOUNDS.register(eventBus);
   }
}
