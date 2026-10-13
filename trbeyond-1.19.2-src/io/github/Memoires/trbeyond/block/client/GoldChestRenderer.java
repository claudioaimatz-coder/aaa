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

public class GoldChestRenderer<T extends BlockEntity & LidBlockEntity> extends DungeonChestRenderer<T> {
   public static final ModelLayerLocation GOLD = new ModelLayerLocation(new ResourceLocation("trbeyond", "gold_chest"), "main");

   public GoldChestRenderer(Context context) {
      ModelPart modelPart = context.bakeLayer(GOLD);
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
            .texOffs(56, 56)
            .addBox(-14.0F, -11.0F, -7.0F, 6.0F, 9.0F, 14.0F, new CubeDeformation(0.0F))
            .texOffs(0, 79)
            .addBox(8.0F, -11.0F, -7.0F, 6.0F, 9.0F, 14.0F, new CubeDeformation(0.0F))
            .texOffs(0, 56)
            .addBox(-7.0F, -11.0F, -7.0F, 14.0F, 9.0F, 14.0F, new CubeDeformation(0.0F))
            .texOffs(154, 63)
            .addBox(-16.0F, -13.0F, -9.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(92, 27)
            .addBox(-9.0F, -1.0F, -9.0F, 3.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(40, 79)
            .addBox(-2.0F, -1.0F, -9.0F, 4.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(96, 46)
            .addBox(6.0F, -1.0F, -9.0F, 3.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(154, 78)
            .addBox(14.0F, -13.0F, -9.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(32, 157)
            .addBox(14.0F, -13.0F, 7.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(40, 157)
            .addBox(-16.0F, -13.0F, 7.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(92, 0)
            .addBox(-15.0F, -11.0F, -8.0F, 1.0F, 11.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(40, 98)
            .addBox(-8.0F, -11.0F, -8.0F, 1.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(138, 36)
            .addBox(-10.0F, -9.0F, -8.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(138, 142)
            .addBox(-6.0F, -8.0F, -8.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(102, 143)
            .addBox(5.0F, -8.0F, -8.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(113, 107)
            .addBox(-5.0F, -10.0F, -8.0F, 1.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(0, 127)
            .addBox(4.0F, -10.0F, -8.0F, 1.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(34, 140)
            .addBox(9.0F, -9.0F, -8.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(68, 142)
            .addBox(10.0F, -10.0F, -8.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(138, 125)
            .addBox(-11.0F, -10.0F, -8.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(118, 65)
            .addBox(2.0F, -11.0F, -8.0F, 2.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(75, 107)
            .addBox(-4.0F, -11.0F, -8.0F, 2.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(34, 123)
            .addBox(-13.0F, -11.0F, -8.0F, 2.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(118, 82)
            .addBox(11.0F, -11.0F, -8.0F, 2.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(70, 124)
            .addBox(-9.0F, -8.0F, -8.0F, 1.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(126, 0)
            .addBox(-7.0F, -7.0F, -8.0F, 1.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(134, 18)
            .addBox(6.0F, -7.0F, -8.0F, 1.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(104, 125)
            .addBox(8.0F, -8.0F, -8.0F, 1.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(88, 38)
            .addBox(-2.0F, -5.0F, -8.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(96, 76)
            .addBox(-1.0F, -6.0F, -8.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(0, 38)
            .addBox(-14.0F, -2.0F, -8.0F, 28.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(88, 42)
            .addBox(1.0F, -5.0F, -8.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(74, 103)
            .addBox(4.0F, -4.0F, -8.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(78, 103)
            .addBox(-5.0F, -4.0F, -8.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(0, 102)
            .addBox(7.0F, -11.0F, -8.0F, 1.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(84, 79)
            .addBox(14.0F, -11.0F, -8.0F, 1.0F, 11.0F, 16.0F, new CubeDeformation(0.0F)),
         PartPose.offset(8.0F, 0.0F, 0.0F)
      );
      PartDefinition lid = All.addOrReplaceChild(
         "lid",
         CubeListBuilder.create()
            .texOffs(0, 21)
            .addBox(-7.0F, -4.0F, -16.0F, 30.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(0, 145)
            .addBox(22.0F, -3.0F, -15.0F, 2.0F, 3.0F, 14.0F, new CubeDeformation(0.0F))
            .texOffs(106, 67)
            .addBox(5.0F, -9.0F, -16.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(106, 69)
            .addBox(10.0F, -9.0F, -16.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(138, 53)
            .addBox(1.0F, -6.0F, -9.0F, 14.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(88, 51)
            .addBox(21.0F, -5.0F, -2.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(96, 71)
            .addBox(-7.0F, -5.0F, -2.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(34, 106)
            .addBox(10.0F, -7.0F, -16.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(104, 75)
            .addBox(6.0F, -8.0F, -16.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(74, 98)
            .addBox(-7.0F, -5.0F, -16.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(106, 65)
            .addBox(5.0F, -7.0F, -16.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(88, 46)
            .addBox(21.0F, -5.0F, -16.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(104, 71)
            .addBox(9.0F, -8.0F, -16.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(148, 99)
            .addBox(-8.0F, -3.0F, -15.0F, 2.0F, 3.0F, 14.0F, new CubeDeformation(0.0F))
            .texOffs(34, 102)
            .addBox(7.0F, -7.0F, -16.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(0, 0)
            .addBox(-6.0F, -3.0F, -17.0F, 28.0F, 3.0F, 18.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, -11.0F, 8.0F)
      );
      PartDefinition cube_r1 = lid.addOrReplaceChild(
         "cube_r1",
         CubeListBuilder.create().texOffs(138, 57).addBox(-7.0F, 0.0F, -1.0F, 10.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(20.0F, -5.0F, -3.0F, 0.0F, -0.7854F, 0.0F)
      );
      PartDefinition cube_r2 = lid.addOrReplaceChild(
         "cube_r2",
         CubeListBuilder.create().texOffs(138, 60).addBox(-3.0F, 0.0F, -1.0F, 10.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(-4.0F, -5.0F, -3.0F, 0.0F, 0.7854F, 0.0F)
      );
      PartDefinition cube_r3 = lid.addOrReplaceChild(
         "cube_r3",
         CubeListBuilder.create().texOffs(118, 99).addBox(-3.0F, 0.0F, -1.0F, 10.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(-4.0F, -5.0F, -13.0F, 0.0F, -0.7854F, 0.0F)
      );
      PartDefinition cube_r4 = lid.addOrReplaceChild(
         "cube_r4",
         CubeListBuilder.create().texOffs(118, 102).addBox(-7.0F, 0.0F, -1.0F, 10.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(20.0F, -5.0F, -13.0F, 0.0F, 0.7854F, 0.0F)
      );
      PartDefinition lock = All.addOrReplaceChild(
         "lock",
         CubeListBuilder.create().texOffs(96, 65).addBox(6.0F, -2.0F, -18.0F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, -11.0F, 8.0F)
      );
      return LayerDefinition.create(meshdefinition, 256, 256);
   }

   @Override
   protected ResourceLocation getTexture() {
      return new ResourceLocation("trbeyond", "textures/block/gold_chest.png");
   }
}
