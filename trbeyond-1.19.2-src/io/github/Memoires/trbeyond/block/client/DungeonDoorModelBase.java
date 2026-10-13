package io.github.Memoires.trbeyond.block.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.Memoires.trbeyond.block.DungeonUnlockerBlock;
import java.util.function.Function;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public abstract class DungeonDoorModelBase extends Model {
   protected static final String DOOR = "door";
   protected static final String RIGHT_DOOR = "right_door";
   protected static final String LEFT_DOOR = "left_door";
   protected final ModelPart root;
   protected final ModelPart door;
   protected final ModelPart right_door;
   protected final ModelPart left_door;

   public DungeonDoorModelBase(ModelPart root, Function<ResourceLocation, RenderType> renderType) {
      super(renderType);
      this.root = root;
      this.door = root.getChild("door");
      this.right_door = this.door.getChild("right_door");
      this.left_door = this.door.getChild("left_door");
   }

   public void setupAnim(BlockState state) {
      this.right_door.yRot = (float) (Math.PI / 180.0) * (float)(-(Integer)state.getValue(DungeonUnlockerBlock.ANIM_STAGE)) * 6.0F;
      this.left_door.yRot = (float) (Math.PI / 180.0) * (float)((Integer)state.getValue(DungeonUnlockerBlock.ANIM_STAGE)).intValue() * 6.0F;
   }

   public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
   }
}
