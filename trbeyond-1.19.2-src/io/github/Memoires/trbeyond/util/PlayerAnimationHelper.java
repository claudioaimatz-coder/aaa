package io.github.Memoires.trbeyond.util;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

public class PlayerAnimationHelper {
   public static void playAnimationClientOnly(AbstractClientPlayer player, String animationName) {
      AnimationStack stack = PlayerAnimationAccess.getPlayerAnimLayer(player);
      KeyframeAnimation anim = PlayerAnimationRegistry.getAnimation(new ResourceLocation("trbeyond", animationName));
      if (anim != null) {
         KeyframeAnimationPlayer animation = new KeyframeAnimationPlayer(anim);
         animation.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
         animation.setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true));
         stack.addAnimLayer(0, animation);
      }
   }

   public static void stopAnimationClientOnly(AbstractClientPlayer player) {
      PlayerAnimationAccess.getPlayerAnimLayer(player).removeLayer(0);
   }
}
