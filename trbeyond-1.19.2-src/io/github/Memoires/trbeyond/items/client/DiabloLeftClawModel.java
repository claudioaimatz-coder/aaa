package io.github.Memoires.trbeyond.items.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class DiabloLeftClawModel extends Model {
   private final ModelPart wrist;
   public final ModelPart claw;
   private final ModelPart group3;
   private final ModelPart group2;
   private final ModelPart group5;
   private final ModelPart group6;
   private final ModelPart bone;
   private final ModelPart group4;

   public DiabloLeftClawModel(ModelPart root) {
      super(RenderType::entitySolid);
      this.wrist = root.getChild("wrist");
      this.claw = root.getChild("claw");
      this.group3 = this.claw.getChild("group3");
      this.group2 = this.claw.getChild("group2");
      this.group5 = this.claw.getChild("group5");
      this.group6 = this.claw.getChild("group6");
      this.bone = this.claw.getChild("bone");
      this.group4 = this.bone.getChild("group4");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      PartDefinition wrist = partdefinition.addOrReplaceChild("wrist", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 24.0F, -7.25F, -0.3927F, 0.0F, 0.0F));
      PartDefinition cube_r1 = wrist.addOrReplaceChild(
         "cube_r1",
         CubeListBuilder.create().texOffs(43, 13).addBox(-3.0F, -2.0F, -0.5F, 6.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.0F, 0.75F, -2.0F, 0.3927F, 0.0F, 0.0F)
      );
      PartDefinition cube_r2 = wrist.addOrReplaceChild(
         "cube_r2",
         CubeListBuilder.create().texOffs(0, 57).addBox(-4.0F, -3.0F, 0.5F, 7.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.5F, 0.5F, 0.0F, 0.3927F, 0.0F, 0.0F)
      );
      PartDefinition cube_r3 = wrist.addOrReplaceChild(
         "cube_r3",
         CubeListBuilder.create().texOffs(44, 1).addBox(-3.0F, -2.0F, -0.5F, 6.0F, 5.0F, 3.0F, new CubeDeformation(0.2F)),
         PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F)
      );
      PartDefinition claw = partdefinition.addOrReplaceChild(
         "claw",
         CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-3.5F, -3.0F, 0.0F, 7.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
            .texOffs(0, 2)
            .addBox(-3.5F, 0.4F, -0.3F, 7.0F, 2.0F, 9.0F, new CubeDeformation(-0.1F))
            .texOffs(6, 5)
            .addBox(-3.0F, -3.2398F, 0.1109F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(6, 5)
            .addBox(-3.0F, -3.2398F, 3.9461F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(0, 13)
            .addBox(-4.0F, -2.0F, 0.25F, 8.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(-1.0F, 24.0F, -6.0F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition cube_r4 = claw.addOrReplaceChild(
         "cube_r4",
         CubeListBuilder.create()
            .texOffs(3, 48)
            .addBox(1.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
            .texOffs(3, 48)
            .addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
            .texOffs(3, 48)
            .addBox(-2.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.0F, -2.9F, 5.2124F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition cube_r5 = claw.addOrReplaceChild(
         "cube_r5",
         CubeListBuilder.create()
            .texOffs(3, 43)
            .addBox(1.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
            .texOffs(3, 43)
            .addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
            .texOffs(3, 43)
            .addBox(-2.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.0F, -2.9F, 2.5F, 0.3927F, 0.0F, 0.0F)
      );
      PartDefinition group3 = claw.addOrReplaceChild(
         "group3",
         CubeListBuilder.create().texOffs(0, 28).addBox(-1.0F, -0.2622F, -6.272F, 2.0F, 2.0F, 5.0F, new CubeDeformation(-0.1F)),
         PartPose.offset(2.5F, -2.9693F, 13.8677F)
      );
      PartDefinition cube_r6 = group3.addOrReplaceChild(
         "cube_r6",
         CubeListBuilder.create().texOffs(0, 35).mirror().addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
         PartPose.offsetAndRotation(-0.2F, -0.0122F, -6.272F, 0.0F, 0.3927F, 0.0F)
      );
      PartDefinition cube_r7 = group3.addOrReplaceChild(
         "cube_r7",
         CubeListBuilder.create().texOffs(30, 17).addBox(-0.5959F, -1.0954F, -0.1976F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.1F)),
         PartPose.offsetAndRotation(0.0086F, 3.5456F, 3.9253F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition cube_r8 = group3.addOrReplaceChild(
         "cube_r8",
         CubeListBuilder.create().texOffs(32, 0).addBox(-0.5959F, -1.1131F, -3.0044F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.1F)),
         PartPose.offsetAndRotation(0.0F, 3.6235F, 4.0893F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition cube_r9 = group3.addOrReplaceChild(
         "cube_r9",
         CubeListBuilder.create().texOffs(24, 35).addBox(4.125F, -1.2137F, 1.6815F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.3F)),
         PartPose.offsetAndRotation(-4.625F, 1.5316F, -1.5367F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition cube_r10 = group3.addOrReplaceChild(
         "cube_r10",
         CubeListBuilder.create().texOffs(6, 35).addBox(4.125F, -1.2137F, 1.6815F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.1F)),
         PartPose.offsetAndRotation(-4.625F, 1.593F, -1.4592F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition cube_r11 = group3.addOrReplaceChild(
         "cube_r11",
         CubeListBuilder.create().texOffs(30, 13).addBox(3.125F, -2.2137F, 1.6815F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)),
         PartPose.offsetAndRotation(-4.125F, 1.2148F, -3.769F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition group2 = claw.addOrReplaceChild(
         "group2",
         CubeListBuilder.create()
            .texOffs(0, 28)
            .mirror()
            .addBox(-1.0F, -0.2622F, -6.272F, 2.0F, 2.0F, 5.0F, new CubeDeformation(-0.1F))
            .mirror(false),
         PartPose.offset(-3.1F, -2.8693F, 13.8677F)
      );
      PartDefinition cube_r12 = group2.addOrReplaceChild(
         "cube_r12",
         CubeListBuilder.create().texOffs(0, 35).addBox(-2.6326F, -2.2531F, -6.4099F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.1F, 1.7409F, -0.4578F, 0.0F, -0.3927F, 0.0F)
      );
      PartDefinition cube_r13 = group2.addOrReplaceChild(
         "cube_r13",
         CubeListBuilder.create()
            .texOffs(30, 17)
            .mirror()
            .addBox(-0.5127F, -1.1054F, 4.5425F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.1F))
            .mirror(false)
            .texOffs(32, 0)
            .mirror()
            .addBox(-0.5041F, -1.1139F, 1.9171F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.1F))
            .mirror(false)
            .texOffs(24, 35)
            .mirror()
            .addBox(-0.6F, -0.9942F, 0.6046F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.3F))
            .mirror(false)
            .texOffs(6, 35)
            .mirror()
            .addBox(-0.6F, -0.9672F, 0.6997F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.1F))
            .mirror(false)
            .texOffs(30, 13)
            .mirror()
            .addBox(-1.1F, -1.4327F, -1.579F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F))
            .mirror(false),
         PartPose.offsetAndRotation(0.1F, 1.7409F, -0.4578F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition group5 = claw.addOrReplaceChild(
         "group5",
         CubeListBuilder.create()
            .texOffs(0, 28)
            .mirror()
            .addBox(-1.0575F, -0.1379F, -6.1884F, 2.0F, 2.0F, 5.0F, new CubeDeformation(-0.1F))
            .mirror(false),
         PartPose.offset(-1.3208F, -2.9769F, 13.7524F)
      );
      PartDefinition cube_r14 = group5.addOrReplaceChild(
         "cube_r14",
         CubeListBuilder.create().texOffs(0, 35).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.1425F, 0.1121F, -6.1884F, 0.0F, -0.3927F, 0.0F)
      );
      PartDefinition cube_r15 = group5.addOrReplaceChild(
         "cube_r15",
         CubeListBuilder.create()
            .texOffs(30, 17)
            .mirror()
            .addBox(-0.4041F, -1.0954F, -0.1976F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.1F))
            .mirror(false),
         PartPose.offsetAndRotation(-0.0661F, 3.6699F, 4.0089F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition cube_r16 = group5.addOrReplaceChild(
         "cube_r16",
         CubeListBuilder.create()
            .texOffs(32, 0)
            .mirror()
            .addBox(-0.4041F, -1.1131F, -3.0044F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.1F))
            .mirror(false),
         PartPose.offsetAndRotation(-0.0575F, 3.7477F, 4.1729F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition cube_r17 = group5.addOrReplaceChild(
         "cube_r17",
         CubeListBuilder.create()
            .texOffs(24, 35)
            .mirror()
            .addBox(-5.125F, -1.2137F, 1.6815F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.3F))
            .mirror(false),
         PartPose.offsetAndRotation(4.5675F, 1.6558F, -1.4531F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition cube_r18 = group5.addOrReplaceChild(
         "cube_r18",
         CubeListBuilder.create()
            .texOffs(6, 35)
            .mirror()
            .addBox(-5.125F, -1.2137F, 1.6815F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.1F))
            .mirror(false),
         PartPose.offsetAndRotation(4.5675F, 1.7172F, -1.3756F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition cube_r19 = group5.addOrReplaceChild(
         "cube_r19",
         CubeListBuilder.create()
            .texOffs(30, 13)
            .mirror()
            .addBox(-5.125F, -2.2137F, 1.6815F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F))
            .mirror(false),
         PartPose.offsetAndRotation(4.0675F, 1.339F, -3.6854F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition group6 = claw.addOrReplaceChild(
         "group6",
         CubeListBuilder.create().texOffs(0, 28).addBox(-0.9425F, -0.1379F, -6.1884F, 2.0F, 2.0F, 5.0F, new CubeDeformation(-0.1F)),
         PartPose.offset(0.5708F, -2.9769F, 13.7524F)
      );
      PartDefinition cube_r20 = group6.addOrReplaceChild(
         "cube_r20",
         CubeListBuilder.create().texOffs(0, 35).mirror().addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
         PartPose.offsetAndRotation(-0.1425F, 0.1121F, -6.1884F, 0.0F, 0.3927F, 0.0F)
      );
      PartDefinition cube_r21 = group6.addOrReplaceChild(
         "cube_r21",
         CubeListBuilder.create().texOffs(30, 17).addBox(-0.5959F, -1.0954F, -0.1976F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.1F)),
         PartPose.offsetAndRotation(0.0661F, 3.6699F, 4.0089F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition cube_r22 = group6.addOrReplaceChild(
         "cube_r22",
         CubeListBuilder.create().texOffs(32, 0).addBox(-0.5959F, -1.1131F, -3.0044F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.1F)),
         PartPose.offsetAndRotation(0.0575F, 3.7477F, 4.1729F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition cube_r23 = group6.addOrReplaceChild(
         "cube_r23",
         CubeListBuilder.create().texOffs(24, 35).addBox(4.125F, -1.2137F, 1.6815F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.3F)),
         PartPose.offsetAndRotation(-4.5675F, 1.6558F, -1.4531F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition cube_r24 = group6.addOrReplaceChild(
         "cube_r24",
         CubeListBuilder.create().texOffs(6, 35).addBox(4.125F, -1.2137F, 1.6815F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.1F)),
         PartPose.offsetAndRotation(-4.5675F, 1.7172F, -1.3756F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition cube_r25 = group6.addOrReplaceChild(
         "cube_r25",
         CubeListBuilder.create().texOffs(30, 13).addBox(3.125F, -2.2137F, 1.6815F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)),
         PartPose.offsetAndRotation(-4.0675F, 1.339F, -3.6854F, -0.3927F, 0.0F, 0.0F)
      );
      PartDefinition bone = claw.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(4.6F, -0.8693F, 8.8677F));
      PartDefinition cube_r26 = bone.addOrReplaceChild(
         "cube_r26",
         CubeListBuilder.create().texOffs(28, 28).addBox(-0.5F, -1.5F, -2.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(-0.6F, -0.4189F, -5.774F, 0.3927F, 0.0F, 0.0F)
      );
      PartDefinition group4 = bone.addOrReplaceChild(
         "group4",
         CubeListBuilder.create()
            .texOffs(14, 28)
            .addBox(-1.0F, -2.0122F, -6.272F, 2.0F, 2.0F, 5.0F, new CubeDeformation(-0.1F))
            .texOffs(18, 35)
            .addBox(-0.5F, -2.5122F, -7.272F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      PartDefinition cube_r27 = group4.addOrReplaceChild(
         "cube_r27",
         CubeListBuilder.create().texOffs(32, 8).addBox(-0.5F, -2.7997F, 2.9382F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.1F)),
         PartPose.offsetAndRotation(0.0F, -0.5F, -1.5076F, -0.7854F, 0.0F, 0.0F)
      );
      PartDefinition cube_r28 = group4.addOrReplaceChild(
         "cube_r28",
         CubeListBuilder.create()
            .texOffs(28, 35)
            .addBox(-0.5F, -0.858F, 1.7188F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.3F))
            .texOffs(12, 35)
            .addBox(-0.5F, -0.858F, 1.7188F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.1F))
            .texOffs(32, 4)
            .addBox(-1.0F, -1.3809F, -0.4212F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)),
         PartPose.offsetAndRotation(0.0F, -0.5F, -1.5076F, -0.3927F, 0.0F, 0.0F)
      );
      return LayerDefinition.create(meshdefinition, 64, 64);
   }

   public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.wrist.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
      this.claw.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
   }
}
