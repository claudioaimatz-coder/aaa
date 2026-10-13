package io.github.Memoires.trbeyond.handlers;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import com.github.manasmods.tensura.registry.skill.ResistanceSkills;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import java.util.List;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.FORGE
)
public class DungeonDominatorsHandler {
   @SubscribeEvent
   public static void DrinkPotion(LivingTickEvent event) {
      if (event.getEntity().getPersistentData().getInt("drink_times") < 4) {
         List<EntityType<?>> Dominators = List.of(
            (EntityType<?>)BeyondEntities.LIVING_ARMOR.get(),
            (EntityType<?>)BeyondEntities.MILIM_SLIME_SCARLET.get(),
            (EntityType<?>)BeyondEntities.BABY_DRAGON.get(),
            (EntityType<?>)BeyondEntities.MAGE.get(),
            (EntityType<?>)BeyondEntities.SKULL_NIGHT.get()
         );
         if (Dominators.contains(event.getEntity().getType()) && event.getEntity().getHealth() <= 15.0F && event.getEntity().isAlive()) {
            event.getEntity().heal((float)((double)(event.getEntity().getMaxHealth() * 0.33F) * 1.0));
            event.getEntity().getPersistentData().putInt("drink_times", event.getEntity().getPersistentData().getInt("drink_times") + 1);
            if (event.getEntity() instanceof Player) {
               Player player = (Player)event.getEntity();
               double maxMP = player.getAttributeValue((Attribute)TensuraAttributeRegistry.MAX_MAGICULE.get());
               TensuraPlayerCapability.getFrom(player).ifPresent(cap -> cap.setMagicule(Math.min(maxMP, cap.getMagicule() + maxMP * 0.05F * 1.0)));
               TensuraPlayerCapability.sync(player);
            }
         }
      }
   }

   @SubscribeEvent
   public static void GrantNullification(LivingSpawnEvent event) {
      List<EntityType<?>> Dominators = List.of(
         (EntityType<?>)BeyondEntities.LIVING_ARMOR.get(),
         (EntityType<?>)BeyondEntities.MILIM_SLIME_SCARLET.get(),
         (EntityType<?>)BeyondEntities.BABY_DRAGON.get(),
         (EntityType<?>)BeyondEntities.MAGE.get(),
         (EntityType<?>)BeyondEntities.SKULL_NIGHT.get()
      );
      if (Dominators.contains(event.getEntity().getType())) {
         LivingEntity entity = event.getEntity();
         if (entity.getType().equals(BeyondEntities.MAGE.get())) {
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.PHYSICAL_ATTACK_NULLIFICATION.get());
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.MAGIC_RESISTANCE.get());
         } else if (entity.getType().equals(BeyondEntities.SKULL_NIGHT.get())) {
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.PHYSICAL_ATTACK_RESISTANCE.get());
         } else if (entity.getType().equals(BeyondEntities.MILIM_SLIME_SCARLET.get())) {
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.PHYSICAL_ATTACK_RESISTANCE.get());
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.FLAME_ATTACK_RESISTANCE.get());
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.HEAT_RESISTANCE.get());
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.WATER_ATTACK_RESISTANCE.get());
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.EARTH_ATTACK_RESISTANCE.get());
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.ELECTRICITY_RESISTANCE.get());
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.COLD_RESISTANCE.get());
         } else if (entity.getType().equals(BeyondEntities.BABY_DRAGON.get())) {
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.MAGIC_RESISTANCE.get());
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.EARTH_ATTACK_NULLIFICATION.get());
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.FLAME_ATTACK_RESISTANCE.get());
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.HEAT_RESISTANCE.get());
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.WATER_ATTACK_RESISTANCE.get());
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.EARTH_ATTACK_RESISTANCE.get());
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.ELECTRICITY_RESISTANCE.get());
            SkillUtils.learnSkill(entity, (ManasSkill)ResistanceSkills.COLD_RESISTANCE.get());
         }
      }
   }
}
