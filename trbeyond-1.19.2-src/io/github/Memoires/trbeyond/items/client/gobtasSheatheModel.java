package io.github.Memoires.trbeyond.items.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class gobtasSheatheModel<T extends Entity> extends EntityModel<T> {
   private final ModelPart group;

   public gobtasSheatheModel(ModelPart root) {
      this.group = root.getChild("group");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      PartDefinition group = partdefinition.addOrReplaceChild(
         "group",
         CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-4.5F, -21.0F, -0.25F, 4.0F, 20.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(25, 0)
            .addBox(-4.5F, -21.0F, -0.25F, 4.0F, 20.0F, 4.0F, new CubeDeformation(0.15F))
            .texOffs(44, 22)
            .addBox(-5.0F, -1.0F, -0.75F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(3.0F, 21.0F, -2.0F, -2.8362F, 0.0F, 0.0F)
      );
      return LayerDefinition.create(meshdefinition, 64, 32);
   }

   public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
   }

   public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.group.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
   }
}
