package io.github.Memoires.trbeyond.core;

import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.Memoires.trbeyond.enchantment.InfusionEnchantment;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.registry.enchantment.BeyondEnchantments;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class})
public class MixinLivingEntity {
   @Shadow
   public boolean addEffect(MobEffectInstance pEffectInstance, @Nullable Entity pEntity) {
      return false;
   }

   @Inject(
      method = {"addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = true
   )
   private void addEffect(MobEffectInstance instance, @org.jetbrains.annotations.Nullable Entity source, CallbackInfoReturnable<Boolean> cir) {
      LivingEntity target = (LivingEntity)this;
      if (target.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
         if (instance.getEffect().equals(TensuraMobEffects.MIND_CONTROL.get())) {
            if (source instanceof Player player) {
               if (player.isCreative()) {
                  return;
               }

               player.displayClientMessage(Component.translatable("trbeyond.message.rule_violation.prohibited").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
            }

            cir.setReturnValue(false);
         }
      }
   }

   @ModifyArg(
      method = {"travel"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V",
         ordinal = 1
      )
   )
   public float travel(float speed) {
      LivingEntity entity = (LivingEntity)this;
      float multiplier = InfusionEnchantment.hasFullSetEffect(entity, (Enchantment)BeyondEnchantments.FIRE_INFUSION.get()) ? 5.0F : 1.0F;
      if (InfusionEnchantment.hasFullSetEffect(entity, (Enchantment)BeyondEnchantments.FLAME_INFUSION.get())) {
         multiplier *= 2.0F;
      }

      return speed * multiplier;
   }
}
