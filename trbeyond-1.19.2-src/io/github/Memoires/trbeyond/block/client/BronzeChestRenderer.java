package io.github.Memoires.trbeyond.block.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;

public class BronzeChestRenderer<T extends BlockEntity & LidBlockEntity> extends DungeonChestRenderer<T> {
   public static final ModelLayerLocation BRONZE = new ModelLayerLocation(new ResourceLocation("trbeyond", "bronze_chest"), "main");

   public BronzeChestRenderer(Context context) {
      ModelPart modelPart = context.bakeLayer(BRONZE);
      this.bottom = modelPart.getChild("All").getChild("bottom");
      this.lid = modelPart.getChild("All").getChild("lid");
      this.lock = modelPart.getChild("All").getChild("lock");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      PartDefinition All = partdefinition.addOrReplaceChild("All", CubeListBuilder.create(), PartPose.offset(-8.0F, 24.0F, 0.0F));
      PartDefinition bottom = All.addOrReplaceChild(
         "bottom",
         CubeListBuilder.create()
            .texOffs(0, 48)
            .addBox(-15.0F, -11.0F, -8.0F, 6.0F, 11.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(44, 48)
            .addBox(10.0F, -11.0F, -8.0F, 5.0F, 11.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(0, 0)
            .addBox(-8.0F, -11.0F, -8.0F, 17.0F, 11.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(66, 0)
            .addBox(-16.0F, -11.0F, -9.0F, 1.0F, 11.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(76, 75)
            .addBox(-9.0F, -11.0F, -9.0F, 1.0F, 11.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(66, 35)
            .addBox(-0.5F, -2.0F, -9.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(38, 75)
            .addBox(9.0F, -11.0F, -9.0F, 1.0F, 11.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(0, 75)
            .addBox(15.0F, -11.0F, -9.0F, 1.0F, 11.0F, 18.0F, new CubeDeformation(0.0F)),
         PartPose.offset(8.0F, 0.0F, 0.0F)
      );
      PartDefinition lid = All.addOrReplaceChild(
         "lid",
         CubeListBuilder.create()
            .texOffs(0, 27)
            .addBox(-8.0F, -5.0F, -16.0F, 17.0F, 5.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(0, 104)
            .addBox(-9.0F, -6.0F, -17.0F, 1.0F, 6.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(38, 104)
            .addBox(9.0F, -6.0F, -17.0F, 1.0F, 6.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(104, 0)
            .addBox(-16.0F, -6.0F, -17.0F, 1.0F, 6.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(76, 104)
            .addBox(15.0F, -6.0F, -17.0F, 1.0F, 6.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(86, 29)
            .addBox(-15.0F, -5.0F, -16.0F, 6.0F, 5.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(86, 50)
            .addBox(9.0F, -5.0F, -16.0F, 6.0F, 5.0F, 16.0F, new CubeDeformation(0.0F)),
         PartPose.offset(8.0F, -11.0F, 8.0F)
      );
      PartDefinition lock = All.addOrReplaceChild(
         "lock",
         CubeListBuilder.create().texOffs(66, 29).addBox(-1.5F, -2.0F, -17.0F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)),
         PartPose.offset(8.0F, -11.0F, 8.0F)
      );
      return LayerDefinition.create(meshdefinition, 256, 256);
   }

   @Override
   protected ResourceLocation getTexture() {
      return new ResourceLocation("trbeyond", "textures/block/bronze_chest.png");
   }
}
