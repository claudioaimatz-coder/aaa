package io.github.Memoires.trbeyond.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderGuiOverlayEvent.Post;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FontRenderer {
   private static String textToRender = null;
   private static Vec3 targetPosition = null;
   private static int renderDuration = 0;
   private static int ticksRemaining = 0;

   public static void renderText(String textToRender, Vec3 targetPosition, int renderDuration) {
      FontRenderer.textToRender = textToRender;
      FontRenderer.targetPosition = targetPosition;
      FontRenderer.renderDuration = renderDuration;
      ticksRemaining = renderDuration;
   }

   @SubscribeEvent
   public static void onRenderGuiOverlay(Post event) {
      if (event.getOverlay() == VanillaGuiOverlay.HOTBAR.type()) {
         if (textToRender != null && targetPosition != null && ticksRemaining > 0) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.level != null) {
               PoseStack poseStack = event.getPoseStack();
               EntityRenderDispatcher renderDispatcher = mc.getEntityRenderDispatcher();
               Vec3 cameraPos = renderDispatcher.camera.getPosition();
               Vec3 relativePos = targetPosition.subtract(cameraPos);
               Vec3 cameraSpacePos = transformToCameraSpace(relativePos, mc.gameRenderer);
               float[] screenCoords = projectToScreen(cameraSpacePos, poseStack);
               float screenX = screenCoords[0];
               float screenY = screenCoords[1];
               if (cameraSpacePos.z <= 0.0) {
                  ticksRemaining--;
               } else {
                  Font font = mc.font;
                  font.draw(poseStack, textToRender, screenX, screenY, 16777215);
                  ticksRemaining--;
                  if (ticksRemaining <= 0) {
                     textToRender = null;
                     targetPosition = null;
                  }
               }
            }
         }
      }
   }

   private static Vec3 transformToCameraSpace(Vec3 relativePos, GameRenderer camera) {
      Quaternion rotation = camera.getMainCamera().rotation();
      Matrix4f rotationMatrix = new Matrix4f(rotation);
      Vector4f vec = new Vector4f((float)relativePos.x, (float)relativePos.y, (float)relativePos.z, 1.0F);
      vec.transform(rotationMatrix);
      return new Vec3((double)vec.x(), (double)vec.y(), (double)vec.z());
   }

   private static float[] projectToScreen(Vec3 cameraSpacePos, PoseStack poseStack) {
      Minecraft mc = Minecraft.getInstance();
      int screenWidth = mc.getWindow().getGuiScaledWidth();
      int screenHeight = mc.getWindow().getGuiScaledHeight();
      float fov = ((Integer)mc.options.fov().get()).floatValue();
      float fovRad = (float)Math.toRadians((double)fov);
      float tanHalfFov = (float)Math.tan((double)(fovRad / 2.0F));
      float aspectRatio = (float)mc.getWindow().getWidth() / (float)mc.getWindow().getHeight();
      float ndcX = (float)(cameraSpacePos.x / (cameraSpacePos.z * (double)tanHalfFov * (double)aspectRatio));
      float ndcY = (float)(cameraSpacePos.y / (cameraSpacePos.z * (double)tanHalfFov));
      float screenX = (ndcX + 1.0F) * 0.5F * (float)screenWidth;
      float screenY = (1.0F - ndcY) * 0.5F * (float)screenHeight;
      return new float[]{screenX, screenY};
   }
}
