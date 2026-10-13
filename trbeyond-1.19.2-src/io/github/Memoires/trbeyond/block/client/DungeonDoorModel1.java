package io.github.Memoires.trbeyond.block.client;

import io.github.Memoires.trbeyond.Database;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class DungeonDoorModel1 extends DungeonDoorModelBase {
   public static final ModelLayerLocation LAYER = new ModelLayerLocation(Database.rl("forest_dungeon_door"), "main");
   private static final String RIGHT_HANDLE = "right_handle";
   private static final String LEFT_HANDLE = "left_handle";
   private final ModelPart right_handle = this.right_door.getChild("right_handle");
   private final ModelPart left_handle = this.left_door.getChild("left_handle");

   public DungeonDoorModel1(ModelPart root) {
      super(root, RenderType::entitySolid);
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      PartDefinition door = partdefinition.addOrReplaceChild("door", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -1.0F));
      PartDefinition right_door = door.addOrReplaceChild(
         "right_door",
         CubeListBuilder.create()
            .texOffs(236, 68)
            .addBox(-15.0F, -30.0F, -8.0F, 4.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(236, 88)
            .addBox(-15.0F, -62.0F, -8.0F, 4.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(236, 108)
            .addBox(-26.0F, -62.0F, -8.0F, 4.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(48, 0)
            .addBox(-8.0F, -96.0F, -8.0F, 8.0F, 96.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(176, 48)
            .addBox(-16.0F, -88.0F, -4.0F, 8.0F, 88.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(36, 112)
            .addBox(-24.0F, -88.0F, -5.0F, 8.0F, 88.0F, 10.0F, new CubeDeformation(0.0F))
            .texOffs(72, 208)
            .addBox(-32.0F, -88.0F, -3.0F, 8.0F, 88.0F, 6.0F, new CubeDeformation(0.0F))
            .texOffs(176, 144)
            .addBox(-40.0F, -88.0F, -4.0F, 8.0F, 88.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(144, 24)
            .addBox(-48.0F, -96.0F, -8.0F, 40.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(96, 104)
            .addBox(-48.0F, -88.0F, -8.0F, 8.0F, 88.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(140, 240)
            .addBox(-26.0F, -30.0F, -8.0F, 4.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(180, 242)
            .addBox(-37.0F, -30.0F, -8.0F, 4.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(220, 242)
            .addBox(-37.0F, -62.0F, -8.0F, 4.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(208, 182)
            .addBox(-40.0F, -32.0F, -6.0F, 32.0F, 8.0F, 12.0F, new CubeDeformation(0.0F))
            .texOffs(208, 202)
            .addBox(-40.0F, -64.0F, -6.0F, 32.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)),
         PartPose.offset(48.0F, 0.0F, 0.0F)
      );
      PartDefinition left_handle = right_door.addOrReplaceChild(
         "right_handle",
         CubeListBuilder.create()
            .texOffs(72, 114)
            .addBox(-3.0F, 0.0F, -1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(76, 116)
            .addBox(1.0F, 1.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(80, 116)
            .addBox(-3.0F, 1.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(82, 114)
            .addBox(-3.0F, 7.0F, -1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(-43.0F, -41.0F, -8.0F, -0.1739F, -0.0151F, -0.0859F)
      );
      PartDefinition left_door = door.addOrReplaceChild(
         "left_door",
         CubeListBuilder.create()
            .texOffs(100, 208)
            .addBox(11.0F, -30.0F, -8.0F, 4.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(236, 48)
            .addBox(11.0F, -62.0F, -8.0F, 4.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(100, 228)
            .addBox(22.0F, -62.0F, -8.0F, 4.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(0, 0)
            .addBox(0.0F, -96.0F, -8.0F, 8.0F, 96.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(144, 48)
            .addBox(8.0F, -88.0F, -4.0F, 8.0F, 88.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(0, 112)
            .addBox(16.0F, -88.0F, -5.0F, 8.0F, 88.0F, 10.0F, new CubeDeformation(0.0F))
            .texOffs(208, 48)
            .addBox(24.0F, -88.0F, -3.0F, 8.0F, 88.0F, 6.0F, new CubeDeformation(0.0F))
            .texOffs(144, 144)
            .addBox(32.0F, -88.0F, -4.0F, 8.0F, 88.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(144, 0)
            .addBox(8.0F, -96.0F, -8.0F, 40.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(96, 0)
            .addBox(40.0F, -88.0F, -8.0F, 8.0F, 88.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(208, 222)
            .addBox(22.0F, -30.0F, -8.0F, 4.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(0, 210)
            .addBox(33.0F, -30.0F, -8.0F, 4.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(0, 230)
            .addBox(33.0F, -62.0F, -8.0F, 4.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(208, 162)
            .addBox(8.0F, -32.0F, -6.0F, 32.0F, 8.0F, 12.0F, new CubeDeformation(0.0F))
            .texOffs(208, 142)
            .addBox(8.0F, -64.0F, -6.0F, 32.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)),
         PartPose.offset(-48.0F, 0.0F, 0.0F)
      );
      PartDefinition right_handle = left_door.addOrReplaceChild(
         "left_handle",
         CubeListBuilder.create()
            .texOffs(72, 112)
            .addBox(-1.0F, 0.0F, -1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(92, 112)
            .addBox(-2.0F, 1.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(72, 116)
            .addBox(2.0F, 1.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(82, 112)
            .addBox(-1.0F, 7.0F, -1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(43.0F, -41.0F, -8.0F, -0.1739F, 0.0151F, 0.0859F)
      );
      return LayerDefinition.create(meshdefinition, 512, 512);
   }
}
