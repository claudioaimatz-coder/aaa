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

public class DungeonDoorModel0 extends DungeonDoorModelBase {
   public static final ModelLayerLocation LAYER = new ModelLayerLocation(Database.rl("dungeon_door"), "main");

   public DungeonDoorModel0(ModelPart root) {
      super(root, RenderType::entitySolid);
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      PartDefinition door = partdefinition.addOrReplaceChild("door", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -1.0F));
      PartDefinition right_door = door.addOrReplaceChild(
         "right_door",
         CubeListBuilder.create()
            .texOffs(48, 0)
            .addBox(8.0F, -68.0F, -2.0F, 8.0F, 68.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(0, 0)
            .addBox(20.0F, -80.0F, -4.0F, 4.0F, 80.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(32, 88)
            .addBox(4.0F, -60.0F, -2.0F, 4.0F, 60.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(72, 0)
            .addBox(0.0F, -64.0F, -4.0F, 4.0F, 64.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(96, 64)
            .addBox(4.0F, -51.0F, -3.0F, 16.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
            .texOffs(96, 74)
            .addBox(4.0F, -22.0F, -3.0F, 16.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
            .texOffs(136, 0)
            .addBox(16.0F, -21.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(136, 10)
            .addBox(11.0F, -21.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(136, 20)
            .addBox(6.0F, -21.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(136, 30)
            .addBox(6.0F, -50.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(136, 40)
            .addBox(11.0F, -50.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(136, 50)
            .addBox(16.0F, -50.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(120, 104)
            .addBox(4.0F, -64.0F, -4.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(96, 104)
            .addBox(8.0F, -72.0F, -4.0F, 4.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(120, 116)
            .addBox(12.0F, -72.0F, -4.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(112, 0)
            .addBox(16.0F, -80.0F, -4.0F, 4.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(0, 88)
            .addBox(16.0F, -68.0F, -2.0F, 4.0F, 68.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(-24.0F, 0.0F, 1.0F)
      );
      PartDefinition left_door = door.addOrReplaceChild(
         "left_door",
         CubeListBuilder.create()
            .texOffs(48, 72)
            .addBox(-16.0F, -68.0F, -2.0F, 8.0F, 68.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(24, 0)
            .addBox(-24.0F, -80.0F, -4.0F, 4.0F, 80.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(96, 0)
            .addBox(-8.0F, -60.0F, -2.0F, 4.0F, 60.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(72, 72)
            .addBox(-4.0F, -64.0F, -4.0F, 4.0F, 64.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(96, 84)
            .addBox(-20.0F, -51.0F, -3.0F, 16.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
            .texOffs(96, 94)
            .addBox(-20.0F, -22.0F, -3.0F, 16.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
            .texOffs(96, 136)
            .addBox(-18.0F, -21.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(140, 60)
            .addBox(-13.0F, -21.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(140, 70)
            .addBox(-8.0F, -21.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(140, 80)
            .addBox(-8.0F, -50.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(140, 90)
            .addBox(-13.0F, -50.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(116, 140)
            .addBox(-18.0F, -50.0F, -4.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(96, 124)
            .addBox(-8.0F, -64.0F, -4.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(112, 20)
            .addBox(-12.0F, -72.0F, -4.0F, 4.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(120, 128)
            .addBox(-16.0F, -72.0F, -4.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(112, 40)
            .addBox(-20.0F, -80.0F, -4.0F, 4.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(16, 88)
            .addBox(-20.0F, -68.0F, -2.0F, 4.0F, 68.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(24.0F, 0.0F, 1.0F)
      );
      return LayerDefinition.create(meshdefinition, 256, 256);
   }
}
