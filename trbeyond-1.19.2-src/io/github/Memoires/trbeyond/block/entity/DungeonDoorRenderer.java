package io.github.Memoires.trbeyond.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.Memoires.trbeyond.Database;
import io.github.Memoires.trbeyond.block.DungeonUnlockerBlock;
import io.github.Memoires.trbeyond.block.client.DungeonDoorModel0;
import io.github.Memoires.trbeyond.block.client.DungeonDoorModel1;
import io.github.Memoires.trbeyond.block.client.DungeonDoorModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class DungeonDoorRenderer implements BlockEntityRenderer<AnimateDoorBlockEntity> {
   private static final ResourceLocation TEXTURE0 = Database.rl("textures/models/dungeon_doors.png");
   private static final ResourceLocation TEXTURE1 = Database.rl("textures/models/forest_dungeon_doors.png");
   private final DungeonDoorModel0 model0;
   private final DungeonDoorModel1 model1;

   public DungeonDoorRenderer(@NotNull Context ctx) {
      this.model0 = new DungeonDoorModel0(ctx.bakeLayer(DungeonDoorModel0.LAYER));
      this.model1 = new DungeonDoorModel1(ctx.bakeLayer(DungeonDoorModel1.LAYER));
   }

   public void render(
      AnimateDoorBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay
   ) {
      if (blockEntity != null) {
         BlockState state = blockEntity.getBlockState();
         Direction dir = (Direction)state.getValue(DungeonUnlockerBlock.FACING);
         int floor = (Integer)state.getValue(DungeonUnlockerBlock.FLOOR);
         if ((Boolean)state.getValue(DungeonUnlockerBlock.CAN_GENERATE)) {
            poseStack.pushPose();
            ResourceLocation texture = this.getTexture(floor);
            DungeonDoorModelBase model = this.getModel(floor);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(dir.toYRot()));
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            poseStack.translate(
               (double)(-0.5F * (float)(dir.getAxis() == Axis.Z ? dir.getStepZ() : dir.getStepX())), -0.5, (double)(0.5F * (float)dir.getStepZ())
            );
            int fullLight = 15728880;
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entitySolid(texture));
            model.setupAnim(state);
            model.renderToBuffer(poseStack, vertexConsumer, fullLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
         }
      }
   }

   private ResourceLocation getTexture(int floor) {
      return floor == 0 ? TEXTURE0 : TEXTURE1;
   }

   private DungeonDoorModelBase getModel(int floor) {
      return (DungeonDoorModelBase)(floor == 0 ? this.model0 : this.model1);
   }
}
