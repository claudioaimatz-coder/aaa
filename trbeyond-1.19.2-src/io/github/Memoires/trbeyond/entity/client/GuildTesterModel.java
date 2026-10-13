package io.github.Memoires.trbeyond.entity.client;

import io.github.Memoires.trbeyond.entity.custom.GuildTesterEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GuildTesterModel extends AnimatedGeoModel<GuildTesterEntity> {
   public ResourceLocation getModelResource(GuildTesterEntity entity) {
      return new ResourceLocation("trbeyond", "geo/guild_tester.geo.json");
   }

   public ResourceLocation getTextureResource(GuildTesterEntity entity) {
      return new ResourceLocation("trbeyond", "textures/entity/guild_tester.png");
   }

   public ResourceLocation getAnimationResource(GuildTesterEntity entity) {
      return new ResourceLocation("trbeyond", "animations/guild_tester.animation.json");
   }
}
