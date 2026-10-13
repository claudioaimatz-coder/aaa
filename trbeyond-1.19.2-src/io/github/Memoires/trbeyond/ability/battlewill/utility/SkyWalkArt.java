package io.github.Memoires.trbeyond.ability.battlewill.utility;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.battlewill.Battewill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class SkyWalkArt extends Battewill {
   @Override
   public double learningCost() {
      return 150.0;
   }

   @Nullable
   @Override
   public ResourceLocation getSkillIcon() {
      return new ResourceLocation("trbeyond", "textures/battlewill/sky_walk.png");
   }

   @Override
   public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) {
      return 15.0;
   }

   @Override
   public double auraCost(LivingEntity entity, ManasSkillInstance instance) {
      return 15.0;
   }

   public boolean canBeToggled(ManasSkillInstance instance, LivingEntity entity) {
      return true;
   }
}
