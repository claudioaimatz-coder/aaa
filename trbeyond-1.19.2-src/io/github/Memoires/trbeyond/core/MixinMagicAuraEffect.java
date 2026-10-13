package io.github.Memoires.trbeyond.core;

import com.github.manasmods.tensura.effect.skill.MagicAuraEffect;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MagicAuraEffect.class})
public class MixinMagicAuraEffect {
   @Inject(
      method = {"onPlayerAttack"},
      at = {@At("HEAD")},
      remap = false,
      cancellable = true
   )
   private void onPlayerAttack(Player attacker, Entity entity, CallbackInfo ci) {
      if (attacker.getItemBySlot(EquipmentSlot.MAINHAND).getEnchantmentLevel((Enchantment)BeyondEnchantments.BATTLE_INFUSION.get()) > 0) {
         ci.cancel();
      }
   }
}
