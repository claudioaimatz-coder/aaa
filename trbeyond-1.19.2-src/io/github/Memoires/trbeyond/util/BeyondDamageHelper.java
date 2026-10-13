package io.github.Memoires.trbeyond.util;

import com.github.manasmods.tensura.ability.battlewill.Battewill;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import com.github.manasmods.tensura.util.damage.TensuraDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

public class BeyondDamageHelper {
   public static boolean isBattleWill(DamageSource source) {
      if (source.getMsgId().contains("aura")) {
         return true;
      } else if (DamageSourceHelper.isPhysicalAttack(source)
         && source.getEntity() instanceof LivingEntity living
         && (living.hasEffect((MobEffect)TensuraMobEffects.AURA_SWORD.get()) || living.hasEffect((MobEffect)TensuraMobEffects.OGRE_GUILLOTINE.get()))) {
         return true;
      } else {
         if (!(source instanceof TensuraDamageSource tensuraSource)) {
            return false;
         }

         return tensuraSource.getSkill() != null && tensuraSource.getSkill().getSkill() instanceof Battewill;
      }
   }
}
