package io.github.Memoires.trbeyond.effect;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.capability.effects.TensuraEffectsCapability;
import com.github.manasmods.tensura.effect.template.TensuraMobEffect;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.skill.ResistanceSkills;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSources;
import io.github.Memoires.trbeyond.registry.effects.BeyondEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RotEffect extends TensuraMobEffect {
   public RotEffect(MobEffectCategory pCategory, int pColor) {
      super(pCategory, pColor);
   }

   public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
      if (pLivingEntity.getHealth() > 0.0F) {
         int durabilityCorrosion = 15 * (pAmplifier + 1);
         float damage = 25.0F * (float)(pAmplifier + 1);
         Player source = TensuraEffectsCapability.getEffectSource(pLivingEntity, this);
         if (source == null) {
            pLivingEntity.hurt(TensuraDamageSources.CORROSION, damage);
            if (SkillUtils.hasSkill(pLivingEntity, (ManasSkill)ResistanceSkills.SPIRITUAL_ATTACK_RESISTANCE.get())) {
               ManasSkillInstance instance = (ManasSkillInstance)SkillAPI.getSkillsFrom(pLivingEntity)
                  .getSkill((ManasSkill)ResistanceSkills.SPIRITUAL_ATTACK_RESISTANCE.get())
                  .get();
               if (!instance.isToggled()) {
                  DamageSourceHelper.directSpiritualHurt(pLivingEntity, null, damage, 0.0F);
               }
            }
         } else {
            pLivingEntity.hurt(TensuraDamageSources.corrosion(source), damage);
            if (SkillUtils.hasSkill(pLivingEntity, (ManasSkill)ResistanceSkills.SPIRITUAL_ATTACK_RESISTANCE.get())) {
               ManasSkillInstance instance = (ManasSkillInstance)SkillAPI.getSkillsFrom(pLivingEntity)
                  .getSkill((ManasSkill)ResistanceSkills.SPIRITUAL_ATTACK_RESISTANCE.get())
                  .get();
               if (!instance.isToggled()) {
                  DamageSourceHelper.directSpiritualHurt(pLivingEntity, null, damage, 0.0F);
               }
            }
         }

         for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack slotStack = pLivingEntity.getItemBySlot(slot);
            slotStack.hurtAndBreak(durabilityCorrosion, pLivingEntity, player -> player.broadcastBreakEvent(slot));
         }
      }

      MobEffectInstance effectInstance = pLivingEntity.getEffect((MobEffect)BeyondEffects.ROT.get());
      if (effectInstance != null) {
         int duration = effectInstance.getDuration();
         if (duration % 160 == 0) {
            MobEffectInstance insanityEffect = pLivingEntity.getEffect((MobEffect)TensuraMobEffects.INSANITY.get());
            int insanityLevel = insanityEffect != null ? insanityEffect.getAmplifier() + 1 : 0;
            if (SkillUtils.hasSkill(pLivingEntity, (ManasSkill)ResistanceSkills.SPIRITUAL_ATTACK_RESISTANCE.get())) {
               ManasSkillInstance instance = (ManasSkillInstance)SkillAPI.getSkillsFrom(pLivingEntity)
                  .getSkill((ManasSkill)ResistanceSkills.SPIRITUAL_ATTACK_RESISTANCE.get())
                  .get();
               if (!instance.isToggled()) {
                  pLivingEntity.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.INSANITY.get(), duration, insanityLevel, false, false, true));
               }
            } else {
               pLivingEntity.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.INSANITY.get(), duration, insanityLevel - 1, false, false, true));
            }
         }
      }
   }

   public boolean isDurationEffectTick(int duration, int amplifier) {
      return duration % 20 == 0;
   }
}
