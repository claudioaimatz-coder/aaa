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

public class PlayerAnimator {
   public static void playAnimationClient(AbstractClientPlayer player, String animationName) {
      AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(player);
      KeyframeAnimation animation_keyframe = PlayerAnimationRegistry.getAnimation(new ResourceLocation("trbeyond", animationName));
      KeyframeAnimationPlayer animation_player = new KeyframeAnimationPlayer(animation_keyframe);
      animation_player.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
      animation_player.setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true));
      animationStack.addAnimLayer(0, animation_player);
   }

   public static void stopAnimationClient(AbstractClientPlayer player) {
      AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(player);
      animationStack.removeLayer(0);
   }
}
