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

public class SilverChestRenderer<T extends BlockEntity & LidBlockEntity> extends DungeonChestRenderer<T> {
   public static final ModelLayerLocation SILVER = new ModelLayerLocation(new ResourceLocation("trbeyond", "silver_chest"), "main");

   public SilverChestRenderer(Context context) {
      ModelPart modelPart = context.bakeLayer(SILVER);
      this.bottom = modelPart.getChild("All").getChild("bottom");
      this.lid = modelPart.getChild("All").getChild("lid");
      this.lock = modelPart.getChild("All").getChild("lock");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      PartDefinition All = partdefinition.addOrReplaceChild("All", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
      PartDefinition bottom = All.addOrReplaceChild(
         "bottom",
         CubeListBuilder.create()
            .texOffs(60, 40)
            .addBox(-15.0F, -11.0F, -8.0F, 8.0F, 11.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(0, 65)
            .addBox(7.0F, -11.0F, -8.0F, 8.0F, 11.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(0, 0)
            .addBox(-6.0F, -11.0F, -8.0F, 12.0F, 11.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(0, 92)
            .addBox(-16.0F, -11.0F, 8.0F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(194, 19)
            .addBox(-16.0F, -11.0F, -8.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(194, 57)
            .addBox(-16.0F, -1.0F, -8.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(22, 92)
            .addBox(-16.0F, -11.0F, -9.0F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(100, 67)
            .addBox(-15.0F, -11.0F, -9.0F, 8.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(100, 86)
            .addBox(7.0F, -11.0F, -9.0F, 8.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(0, 46)
            .addBox(-6.0F, -11.0F, -9.0F, 12.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(56, 0)
            .addBox(-6.0F, -1.0F, -9.0F, 12.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(0, 105)
            .addBox(7.0F, -1.0F, -9.0F, 8.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(52, 105)
            .addBox(-15.0F, -1.0F, -9.0F, 8.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(116, 0)
            .addBox(-7.0F, -11.0F, -9.0F, 1.0F, 11.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(0, 153)
            .addBox(-6.0F, -8.0F, -9.0F, 1.0F, 3.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(156, 156)
            .addBox(5.0F, -8.0F, -9.0F, 1.0F, 3.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(80, 166)
            .addBox(-5.0F, -9.0F, -9.0F, 1.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(0, 174)
            .addBox(4.0F, -9.0F, -9.0F, 1.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(118, 166)
            .addBox(-4.0F, -10.0F, -9.0F, 1.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(38, 169)
            .addBox(3.0F, -10.0F, -9.0F, 1.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(38, 92)
            .addBox(-1.0F, -4.0F, -9.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(48, 65)
            .addBox(1.0F, -2.0F, -9.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(54, 65)
            .addBox(-3.0F, -2.0F, -9.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(44, 92)
            .addBox(3.0F, -3.0F, -9.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(44, 95)
            .addBox(-4.0F, -3.0F, -9.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(0, 124)
            .addBox(6.0F, -11.0F, -9.0F, 1.0F, 11.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(4, 92)
            .addBox(15.0F, -11.0F, -9.0F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(156, 177)
            .addBox(15.0F, -11.0F, -8.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(38, 188)
            .addBox(15.0F, -1.0F, -8.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(18, 92)
            .addBox(15.0F, -11.0F, 8.0F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)),
         PartPose.offset(8.0F, 0.0F, 0.0F)
      );
      PartDefinition lid = All.addOrReplaceChild(
         "lid",
         CubeListBuilder.create()
            .texOffs(60, 19)
            .addBox(2.0F, -5.0F, -16.0F, 12.0F, 5.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(110, 185)
            .addBox(23.0F, -6.0F, -16.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(76, 185)
            .addBox(23.0F, -1.0F, -16.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(38, 124)
            .addBox(1.0F, -6.0F, -17.0F, 1.0F, 6.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(114, 126)
            .addBox(7.0F, -6.0F, -17.0F, 2.0F, 3.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(114, 147)
            .addBox(4.0F, -5.0F, -17.0F, 3.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(154, 0)
            .addBox(-4.0F, -5.0F, -17.0F, 3.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(38, 150)
            .addBox(9.0F, -5.0F, -17.0F, 3.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(152, 80)
            .addBox(17.0F, -5.0F, -17.0F, 3.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(154, 19)
            .addBox(2.0F, -4.0F, -17.0F, 2.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(156, 137)
            .addBox(-1.0F, -4.0F, -17.0F, 2.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(154, 118)
            .addBox(12.0F, -4.0F, -17.0F, 2.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(152, 61)
            .addBox(20.0F, -4.0F, -17.0F, 3.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(152, 99)
            .addBox(-7.0F, -4.0F, -17.0F, 3.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(156, 38)
            .addBox(15.0F, -4.0F, -17.0F, 2.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(76, 126)
            .addBox(14.0F, -6.0F, -17.0F, 1.0F, 6.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(26, 92)
            .addBox(-8.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(190, 177)
            .addBox(-8.0F, -6.0F, -16.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(0, 193)
            .addBox(-8.0F, -1.0F, -16.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(34, 92)
            .addBox(-8.0F, -6.0F, -17.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(48, 67)
            .addBox(-7.0F, -1.0F, -17.0F, 8.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(48, 86)
            .addBox(15.0F, -1.0F, -17.0F, 8.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(0, 27)
            .addBox(2.0F, -1.0F, -17.0F, 12.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(56, 19)
            .addBox(23.0F, -6.0F, -17.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(30, 92)
            .addBox(23.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(104, 105)
            .addBox(-7.0F, -5.0F, -16.0F, 8.0F, 5.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(108, 40)
            .addBox(15.0F, -5.0F, -16.0F, 8.0F, 5.0F, 16.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, -11.0F, 8.0F)
      );
      PartDefinition lock = All.addOrReplaceChild(
         "lock",
         CubeListBuilder.create().texOffs(8, 92).addBox(6.0F, -2.0F, -18.0F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, -11.0F, 8.0F)
      );
      return LayerDefinition.create(meshdefinition, 256, 256);
   }

   @Override
   protected ResourceLocation getTexture() {
      return new ResourceLocation("trbeyond", "textures/block/silver_chest.png");
   }
}
