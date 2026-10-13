package io.github.Memoires.trbeyond.core;

import io.github.Memoires.trbeyond.registry.items.BeyondArmorItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ItemStack.class})
public abstract class MixinItemStack {
   @Inject(
      method = {"isDamageableItem()Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onIsDamageableItem(CallbackInfoReturnable<Boolean> cir) {
      ItemStack stack = (ItemStack)this;
      if (stack.is((Item)BeyondArmorItems.HAKURO_BOOTS_SPECIAL.get())
         || stack.is((Item)BeyondArmorItems.DIABLO_BOOTS_SPECIAL.get())
         || stack.is((Item)BeyondArmorItems.SHION_BOOTS_SPECIAL.get())
         || stack.is((Item)BeyondArmorItems.BENIMARU_BOOTS_SPECIAL.get())
         || stack.is((Item)BeyondArmorItems.SOUEI_BOOTS_SPECIAL.get())) {
         cir.setReturnValue(false);
      }
   }
}
