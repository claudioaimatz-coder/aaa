package io.github.Memoires.trbeyond.core;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.skill.unique.ShadowStrikerSkill;
import com.github.manasmods.tensura.registry.skill.UniqueSkills;
import com.github.manasmods.tensura.util.damage.DamageSourceHelper;
import io.github.Memoires.trbeyond.items.weapons.NinjatoItem;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ShadowStrikerSkill.class})
public abstract class MixinShadowStrikerSkill {
   @Inject(
      method = {"onDamageEntity"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   public void onDamageEntity(ManasSkillInstance instance, LivingEntity attacker, LivingHurtEvent e, CallbackInfo ci) {
      if (((ShadowStrikerSkill)UniqueSkills.SHADOW_STRIKER.get()).isInSlot(attacker) && instance.getMode() == 2 && !instance.onCoolDown()) {
         DamageSource source = e.getSource();
         if (source.getEntity() == attacker
            && DamageSourceHelper.isPhysicalAttack(source)
            && !SkillHelper.outOfMagicule(attacker, instance)
            && this.isHoldingNinjato(attacker)) {
            LivingEntity living = e.getEntity();
            DamageSourceHelper.directSpiritualHurt(living, attacker, 1000.0F);
            instance.setCoolDown(instance.isMastered(living) ? 5 : 10);
            if (!living.isAlive()) {
               e.setCanceled(true);
            }

            ci.cancel();
         }
      }
   }

   private boolean isHoldingNinjato(LivingEntity attacker) {
      ItemStack mainHandStack = attacker.getMainHandItem();
      ItemStack offHandStack = attacker.getOffhandItem();
      return mainHandStack.getItem() instanceof NinjatoItem || offHandStack.getItem() instanceof NinjatoItem;
   }

   @Inject(
      method = {"onPressed"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   private void onUseSkill(ManasSkillInstance instance, LivingEntity entity, CallbackInfo ci) {
      if (instance.getMode() == 1 && entity.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
         ci.cancel();
      }
   }
}
