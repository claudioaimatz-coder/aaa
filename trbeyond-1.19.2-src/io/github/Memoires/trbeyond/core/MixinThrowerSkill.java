package io.github.Memoires.trbeyond.core;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.skill.unique.ThrowerSkill;
import com.github.manasmods.tensura.registry.skill.ExtraSkills;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import io.github.Memoires.trbeyond.entity.projectile.ThrownMinosBardicheProjectile;
import io.github.Memoires.trbeyond.items.weapons.MinosBardicheItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ThrowerSkill.class})
public abstract class MixinThrowerSkill {
   @Inject(
      method = {"getProjectile"},
      at = {@At("RETURN")},
      cancellable = true,
      remap = false
   )
   private static void getProjectile(Level level, LivingEntity entity, ItemStack stack, ManasSkillInstance instance, CallbackInfoReturnable<Projectile> cir) {
      if (stack.getItem() instanceof MinosBardicheItem) {
         float baseDamage = DamageSourceHelper.getMainWeaponDamage(entity, null) + (float)(instance.isMastered(entity) ? 100 : 50);
         int multiplier = 1;
         if (SkillUtils.isSkillToggled(entity, (ManasSkill)ExtraSkills.GRAVITY_DOMINATION.get())) {
            multiplier = 3;
         } else if (SkillUtils.isSkillToggled(entity, (ManasSkill)ExtraSkills.GRAVITY_MANIPULATION.get())) {
            multiplier = 2;
         }

         baseDamage *= (float)multiplier;
         ThrownMinosBardicheProjectile spear = new ThrownMinosBardicheProjectile(level, entity, stack, true);
         spear.setBaseDamage((double)baseDamage);
         spear.setLoyaltyLevel(4);
         cir.setReturnValue(spear);
         cir.cancel();
      }
   }
}
