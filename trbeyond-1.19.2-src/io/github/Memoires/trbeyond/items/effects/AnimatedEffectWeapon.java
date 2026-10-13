package io.github.Memoires.trbeyond.items.effects;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import io.github.Memoires.trbeyond.network.BeyondNetwork;
import io.github.Memoires.trbeyond.network.SyncAnimationPacket;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface AnimatedEffectWeapon {
   default ModifierLayer<IAnimation> getModifierLayer(Player player) {
      try {
         return (ModifierLayer<IAnimation>)PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer)player)
            .get(new ResourceLocation("trbeyond", "animation"));
      } catch (Exception var3) {
         return null;
      }
   }

   default void playAnimationClientOnly(AbstractClientPlayer player) {
      this.playAnimationClientOnly(player, this.getAnimationName());
   }

   default void playAnimationClientOnly(AbstractClientPlayer player, String animationName) {
      AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(player);
      KeyframeAnimation animation_keyframe = PlayerAnimationRegistry.getAnimation(new ResourceLocation("trbeyond", animationName));
      KeyframeAnimationPlayer animation_player = new KeyframeAnimationPlayer(animation_keyframe);
      animation_player.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
      animation_player.setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true));
      animationStack.addAnimLayer(0, animation_player);
   }

   default void playAnimationClientSync(Player player) {
      BeyondNetwork.toServer(new SyncAnimationPacket(player.getId()));
   }

   default void playAnimationClientSync(Player player, String animationName) {
      BeyondNetwork.toServer(new SyncAnimationPacket(player.getId(), animationName));
   }

   default void playAnimationServer(Player player) {
      BeyondNetwork.toAll(new SyncAnimationPacket(player.getId()));
   }

   static void playAnimationServer(Player player, String animationName) {
      BeyondNetwork.toAll(new SyncAnimationPacket(player.getId(), animationName));
   }

   default void stopAnimationClientOnly(AbstractClientPlayer player) {
      PlayerAnimationAccess.getPlayerAnimLayer(player).removeLayer(0);
   }

   default void stopAnimationClientSync(Player player) {
      BeyondNetwork.toServer(new SyncAnimationPacket(player.getId(), false));
   }

   default void stopAnimationServer(Player player) {
      BeyondNetwork.toAll(new SyncAnimationPacket(player.getId(), false));
   }

   default boolean isAnimationActive(AbstractClientPlayer player) {
      return PlayerAnimationAccess.getPlayerAnimLayer(player).isActive();
   }

   default String getAnimationName() {
      return null;
   }

   default AnimatedEffectWeapon.Effect getEffect() {
      return null;
   }

   public static enum Effect {
      NONE("", "", "", 0),
      MINOS_TRIDENT_UNIQUE(
         "geo/effects/minos_trident_unique_effect.geo.json",
         "textures/effects/minos_trident/minos_trident_unique_effect0.png",
         "animations/effects/minos_trident_effect.animation.json",
         4
      ),
      MINOS_TRIDENT_GOD(
         "geo/effects/minos_trident_god_effect.geo.json",
         "textures/effects/minos_trident/minos_trident_god_effect0.png",
         "animations/effects/guren_effect.animation.json",
         4
      ),
      MINOS_TRIDENT_LEGEND(
         "geo/effects/minos_trident_legend_effect.geo.json",
         "textures/effects/minos_trident/minos_trident_legend_effect0.png",
         "animations/effects/minos_trident_effect.animation.json",
         4
      ),
      GUREN(
         "geo/effects/guren_effect.geo.json", "textures/effects/benimaru_blade/benimaru_blade_effect0.png", "animations/effects/guren_effect.animation.json", 7
      ),
      GORIKIMARU(
         "geo/effects/gorikimaru_effect.geo.json",
         "textures/effects/gorikimaru/shion_blade_effect0.png",
         "animations/effects/gorikimaru_effect.animation.json",
         5
      ),
      GABIRU("geo/effects/gabiru_effect.geo.json", "textures/effects/gabiru/gabiru_effect0.png", "animations/effects/gabiru_effect.animation.json", 4),
      HAKURO_SWORD(
         "geo/effects/hakuro_sword_effect.geo.json",
         "textures/effects/hakuro_sword/hakuro_sword_effect0.png",
         "animations/effects/hakuro_sword_effect.animation.json",
         1
      ),
      GELDS("geo/effects/gelds_effect.geo.json", "textures/effects/gelds/gelds_effect0.png", "animations/effects/gelds_effect.animation.json", 1);

      private final String model;
      private final String texture;
      private final String animation;
      private final int number_of_frames;

      public String getModel() {
         return this.model;
      }

      public String getTexture() {
         return this.texture;
      }

      public String getAnimation() {
         return this.animation;
      }

      public int getNumber_of_frames() {
         return this.number_of_frames;
      }

      private Effect(String model, String texture, String animation, int number_of_frames) {
         this.model = model;
         this.texture = texture;
         this.animation = animation;
         this.number_of_frames = number_of_frames;
      }
   }
}
