package io.github.Memoires.trbeyond.items.effects;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.Memoires.trbeyond.capability.player.BeyondPlayerCapability;
import io.github.Memoires.trbeyond.items.client.DiabloLeftClawLayer;
import io.github.Memoires.trbeyond.items.client.GobtasSheatheLayer;
import io.github.Memoires.trbeyond.items.weapons.DiabloClawItem;
import io.github.Memoires.trbeyond.items.weapons.GurenItem;
import io.github.Memoires.trbeyond.items.weapons.MeatCleaverItem;
import io.github.Memoires.trbeyond.network.BeyondNetwork;
import io.github.Memoires.trbeyond.network.ChaosEaterPacket;
import io.github.Memoires.trbeyond.network.RequestTeleportPacket;
import io.github.Memoires.trbeyond.util.PlayerAnimator;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent.AddLayers;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import software.bernie.geckolib3.core.event.CustomInstructionKeyframeEvent;

@EventBusSubscriber(
   modid = "trbeyond",
   value = {Dist.CLIENT},
   bus = Bus.FORGE
)
public class EffectHandler {
   private static float last_y_rot;
   public static boolean lockMovement = true;
   public static boolean lockPOV = false;

   @SubscribeEvent
   public static void renderEffect(RenderPlayerEvent event) {
      Player player = event.getEntity();
      ItemStack held_item = player.getMainHandItem();
      EffectRenderer renderer = new EffectRenderer(new EffectModel(AnimatedEffectWeapon.Effect.NONE));
      if (held_item.getItem() instanceof AnimatedEffectWeapon weapon && !player.isUsingItem() && weapon.isAnimationActive((AbstractClientPlayer)player)) {
         set_effect_model(weapon, renderer);
         render_effect(weapon.getEffect(), renderer, player, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
      }
   }

   @SubscribeEvent
   public static void disableInputWhilstUsingWeapon(InputEvent event) {
      Minecraft mc = Minecraft.getInstance();
      Options options = mc.options;
      if (mc.player != null && mc.player.isAlive() && mc.screen == null) {
         AtomicBoolean isAction = new AtomicBoolean(false);
         if (mc.player != null) {
            BeyondPlayerCapability.getFrom(mc.player).ifPresent(cap -> isAction.set(cap.getAction()));
         }

         try {
            if (mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof AnimatedEffectWeapon weapon
               && weapon.isAnimationActive(mc.player)
               && (mc.player.isOnGround() || isAction.get())) {
               event.setCanceled(true);
            }
         } catch (Exception var6) {
         }
      }
   }

   @SubscribeEvent
   public static void lock_player_movement(ClientTickEvent event) {
      Minecraft mc = Minecraft.getInstance();
      Options options = mc.options;
      AtomicBoolean isAction = new AtomicBoolean(false);
      if (mc.player != null) {
         BeyondPlayerCapability.getFrom(mc.player).ifPresent(cap -> isAction.set(cap.getAction()));
      }

      try {
         if (animationIsActive(mc.player) && (mc.player.isOnGround() || isAction.get())) {
            if (lockMovement) {
               options.keyDown.setDown(false);
               options.keyUp.setDown(false);
               Minecraft.getInstance().options.mouseWheelSensitivity().set(0.0);
               options.keyLeft.setDown(false);
               options.keyRight.setDown(false);
               options.keyUse.setDown(false);
               options.keyJump.setDown(false);
               options.keyShift.setDown(false);
               options.keyDrop.setDown(false);
               options.keyAttack.setDown(false);
            }

            if (lockPOV) {
               mc.player.setYRot(last_y_rot);
            }
         } else {
            lockPOV = false;
         }
      } catch (Exception var5) {
      }
   }

   @SubscribeEvent
   public static void saveLastRot(ClientTickEvent event) {
      Player player = Minecraft.getInstance().player;
      if (player != null && !lockPOV) {
         last_y_rot = Minecraft.getInstance().player.getYRot();
      }
   }

   @SubscribeEvent
   public static void animateDiabloClaw(PlayerTickEvent event) {
      Player player = event.player;
      if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof DiabloClawItem && player instanceof AbstractClientPlayer abstractClientPlayer) {
         if (player.getUseItem().getItem() instanceof DiabloClawItem) {
            PlayerAnimator.playAnimationClient(abstractClientPlayer, "diablo_block");
         } else {
            PlayerAnimator.stopAnimationClient(abstractClientPlayer);
         }
      }
   }

   private static boolean animationIsActive(Player player) {
      if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof AnimatedEffectWeapon weapon
         && weapon.isAnimationActive((AbstractClientPlayer)player)) {
         return true;
      }

      return false;
   }

   private static void set_effect_model(AnimatedEffectWeapon weapon, EffectRenderer renderer) {
      renderer.setModel(new EffectModel(weapon.getEffect()));
      renderer.setItem(weapon.getEffect());
   }

   public static void handleInstructionKeyframe(CustomInstructionKeyframeEvent event) {
      AbstractClientPlayer player = Minecraft.getInstance().player;
      if (event.instructions.equals("offsetPlayer")) {
         if (player.getMainHandItem().getItem() instanceof GurenItem guren && guren.isAnimationActive(player)) {
            BeyondNetwork.toServer(new RequestTeleportPacket());
            lockPOV = true;
         }
      } else if (event.instructions.equals("lockPov")
         && player.getMainHandItem().getItem() instanceof AnimatedEffectWeapon weapon
         && weapon.isAnimationActive(player)) {
         lockPOV = true;
      } else if (event.instructions.equals("chaosEater;")
         && player.getMainHandItem().getItem() instanceof MeatCleaverItem cleaver
         && cleaver.isAnimationActive(player)) {
         BeyondNetwork.toServer(new ChaosEaterPacket());
      }
   }

   private static void render_effect(
      AnimatedEffectWeapon.Effect effect, EffectRenderer renderer, Player player, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight
   ) {
      if (effect == AnimatedEffectWeapon.Effect.GUREN) {
         pPoseStack.pushPose();
         pPoseStack.translate(0.0, -0.5, 0.0);
         pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - player.getYRot()));
         renderer.renderByItem(renderer.getItemStack(), TransformType.HEAD, pPoseStack, pBuffer, pPackedLight, pPackedLight);
         pPoseStack.popPose();
      } else if (effect == AnimatedEffectWeapon.Effect.MINOS_TRIDENT_LEGEND
         || effect == AnimatedEffectWeapon.Effect.MINOS_TRIDENT_UNIQUE
         || effect == AnimatedEffectWeapon.Effect.MINOS_TRIDENT_GOD) {
         pPoseStack.pushPose();
         pPoseStack.translate(0.0, -1.0, 0.0);
         pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - player.getYRot()));
         renderer.renderByItem(renderer.getItemStack(), TransformType.HEAD, pPoseStack, pBuffer, pPackedLight, pPackedLight);
         pPoseStack.popPose();
      } else if (effect == AnimatedEffectWeapon.Effect.GORIKIMARU) {
         pPoseStack.pushPose();
         pPoseStack.translate(0.0, -0.5, 1.0);
         pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - player.getYRot()));
         renderer.renderByItem(renderer.getItemStack(), TransformType.HEAD, pPoseStack, pBuffer, pPackedLight, pPackedLight);
         pPoseStack.popPose();
      } else if (effect == AnimatedEffectWeapon.Effect.GABIRU) {
         pPoseStack.pushPose();
         pPoseStack.translate(0.0, -0.5, 0.0);
         pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - player.getYRot()));
         renderer.renderByItem(renderer.getItemStack(), TransformType.HEAD, pPoseStack, pBuffer, pPackedLight, pPackedLight);
         pPoseStack.popPose();
      } else if (effect == AnimatedEffectWeapon.Effect.HAKURO_SWORD) {
         pPoseStack.pushPose();
         pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - player.getYRot()));
         renderer.renderByItem(renderer.getItemStack(), TransformType.HEAD, pPoseStack, pBuffer, pPackedLight, pPackedLight);
         pPoseStack.popPose();
      } else if (effect == AnimatedEffectWeapon.Effect.GELDS) {
         pPoseStack.pushPose();
         pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - player.getYRot()));
         renderer.renderByItem(renderer.getItemStack(), TransformType.HEAD, pPoseStack, pBuffer, pPackedLight, pPackedLight);
         pPoseStack.popPose();
      }
   }

   @EventBusSubscriber(
      modid = "trbeyond",
      value = {Dist.CLIENT},
      bus = Bus.MOD
   )
   public static class ModEvents {
      @SubscribeEvent
      public static void registerCustomRenderLayers(AddLayers event) {
         for (String skinName : event.getSkins()) {
            LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> playerRenderer = event.getSkin(skinName);
            if (playerRenderer != null) {
               playerRenderer.addLayer(new DiabloLeftClawLayer(playerRenderer));
               playerRenderer.addLayer(new GobtasSheatheLayer(playerRenderer));
            }
         }
      }
   }
}
