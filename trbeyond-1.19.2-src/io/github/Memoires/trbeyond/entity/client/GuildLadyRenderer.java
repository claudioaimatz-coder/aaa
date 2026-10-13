package io.github.Memoires.trbeyond.entity.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.AbstractVillager;
import org.jetbrains.annotations.NotNull;

public class GuildLadyRenderer<T extends AbstractVillager> extends HumanoidMobRenderer<T, HumanoidModel<T>> {
   public GuildLadyRenderer(Context pContext, boolean slim) {
      super(pContext, new HumanoidModel(pContext.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER)), 0.5F);
      this.addLayer(
         new HumanoidArmorLayer(
            this,
            new HumanoidModel(pContext.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR)),
            new HumanoidModel(pContext.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR))
         )
      );
   }

   @NotNull
   public ResourceLocation getTextureLocation(T entity) {
      return new ResourceLocation("trbeyond", "textures/entity/guild_lady.png");
   }
}
