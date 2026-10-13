package io.github.Memoires.trbeyond.entity.client.reused;

import com.github.manasmods.tensura.entity.SlimeEntity;
import com.github.manasmods.tensura.entity.client.SlimeRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class MinhSlimeRenderer extends SlimeRenderer {
   public MinhSlimeRenderer(Context renderManager) {
      super(renderManager);
   }

   @Override
   public ResourceLocation getTextureLocation(SlimeEntity slime) {
      return new ResourceLocation("tensura", "textures/entity/slime/slime_metal_golden.png");
   }
}
