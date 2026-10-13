package io.github.Memoires.trbeyond.util;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.capability.SkillStorage;
import com.github.manasmods.tensura.ability.skill.resist.ResistSkill;
import net.minecraft.world.entity.LivingEntity;

public class ResistHelper {
   public static boolean checkResistSkills(LivingEntity entity) {
      SkillStorage playerStorage = SkillAPI.getSkillsFrom(entity);

      for (ManasSkillInstance skill : playerStorage.getLearnedSkills()) {
         if (skill.getSkill() instanceof ResistSkill && skill.getMastery() <= 0 && skill.isToggled()) {
            return false;
         }
      }

      return true;
   }
}
