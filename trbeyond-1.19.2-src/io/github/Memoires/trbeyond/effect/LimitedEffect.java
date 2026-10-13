package io.github.Memoires.trbeyond.effect;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.capability.SkillStorage;
import com.github.manasmods.tensura.ability.skill.resist.ResistSkill;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.effect.template.SkillMobEffect;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import io.github.Memoires.trbeyond.capability.dungeon.DungeonCapability;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class LimitedEffect extends SkillMobEffect {
   protected static final String LIMITED = "e0ac7155-4c03-4533-b5ef-bd87e7f2ed35";

   public LimitedEffect(MobEffectCategory pCategory, int pColor) {
      super(pCategory, pColor);
      this.addAttributeModifier((Attribute)TensuraAttributeRegistry.MAX_MAGICULE.get(), "e0ac7155-4c03-4533-b5ef-bd87e7f2ed35", 1.0, Operation.ADDITION);
      this.addAttributeModifier((Attribute)TensuraAttributeRegistry.MAX_AURA.get(), "e0ac7155-4c03-4533-b5ef-bd87e7f2ed35", 1.0, Operation.ADDITION);
   }

   public void addAttributeModifiers(LivingEntity target, AttributeMap pAttributeMap, int pAmplifier) {
      if (target instanceof Player player) {
         TensuraPlayerCapability.getFrom(player)
            .ifPresent(
               cap -> {
                  double limit = DungeonCapability.getEpLimit(target);
                  if (!(limit <= 0.0)) {
                     for (Entry<Attribute, AttributeModifier> entry : this.getAttributeModifiers().entrySet()) {
                        AttributeInstance instance = pAttributeMap.getInstance(entry.getKey());
                        if (instance != null) {
                           AttributeModifier modifier = entry.getValue();
                           instance.removeModifier(modifier);
                           double value = instance.getAttribute().equals(TensuraAttributeRegistry.MAX_MAGICULE.get()) ? cap.getBaseMagicule() : cap.getBaseAura();
                           instance.addPermanentModifier(
                              new AttributeModifier(modifier.getId(), this.getDescriptionId() + " " + pAmplifier, limit / 2.0 - value, modifier.getOperation())
                           );
                        }
                     }

                     cap.setAura(Math.min(player.getAttributeValue((Attribute)TensuraAttributeRegistry.MAX_AURA.get()), cap.getAura()));
                     cap.setMagicule(Math.min(player.getAttributeValue((Attribute)TensuraAttributeRegistry.MAX_MAGICULE.get()), cap.getMagicule()));
                     TensuraPlayerCapability.sync(player);
                     TensuraEPCapability.updateEP(target);
                  }
               }
            );
      }
   }

   public void removeAttributeModifiers(LivingEntity target, AttributeMap map, int pAmplifier) {
      super.removeAttributeModifiers(target, map, pAmplifier);
      double maxMP = target.getAttributeValue((Attribute)TensuraAttributeRegistry.MAX_MAGICULE.get());
      double maxAP = target.getAttributeValue((Attribute)TensuraAttributeRegistry.MAX_AURA.get());
      if (target instanceof Player player) {
         TensuraPlayerCapability.getFrom(player).ifPresent(cap -> cap.setMagicule(Math.min(maxMP, cap.getMagicule() + maxMP)));
         TensuraPlayerCapability.getFrom(player).ifPresent(cap -> cap.setAura(Math.min(maxAP, cap.getAura() + maxAP)));
         TensuraPlayerCapability.sync(player);
      }

      TensuraEPCapability.updateEP(target);
   }

   public void applyEffectTick(LivingEntity target, int pAmplifier) {
      SkillStorage storage = SkillAPI.getSkillsFrom(target);

      for (ManasSkillInstance instance : storage.getLearnedSkills()) {
         if (instance.getSkill() instanceof ResistSkill && instance.isToggled()) {
            instance.setToggled(false);
            instance.onToggleOff(target);
            instance.markDirty();
         }
      }

      storage.syncChanges();
   }

   public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
      return pDuration % 20 == 0;
   }

   @Override
   public List<ItemStack> getCurativeItems() {
      return Collections.emptyList();
   }
}
