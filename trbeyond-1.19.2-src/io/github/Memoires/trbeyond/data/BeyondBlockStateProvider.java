package io.github.Memoires.trbeyond.data;

import io.github.Memoires.trbeyond.block.DungeonUnlockerBlock;
import io.github.Memoires.trbeyond.registry.blocks.BeyondBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class BeyondBlockStateProvider extends BlockStateProvider {
   public BeyondBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
      super(gen, "trbeyond", exFileHelper);
   }

   protected void registerStatesAndModels() {
      this.registerUnlockerBlock();
   }

   private void registerUnlockerBlock() {
      RegistryObject<DungeonUnlockerBlock> block = BeyondBlocks.DUNGEON_UNLOCKER_BLOCK;
      ResourceLocation texture = this.mcLoc(this.blockPrefix("nether_portal"));
      ModelFile model = ((BlockModelBuilder)((BlockModelBuilder)((BlockModelBuilder)((BlockModelBuilder)((BlockModelBuilder)this.models()
                        .withExistingParent(this.blockPrefix(this.name((Block)block.get())), this.mcLoc(this.blockPrefix("block"))))
                     .renderType("solid"))
                  .texture("portal", texture))
               .texture("particle", texture))
            .ao(false))
         .element()
         .from(0.0F, 0.0F, 6.0F)
         .to(16.0F, 16.0F, 10.0F)
         .face(Direction.NORTH)
         .texture("#portal")
         .end()
         .face(Direction.SOUTH)
         .texture("#portal")
         .end()
         .end();
      this.getVariantBuilder((Block)block.get()).forAllStates(state -> {
         Direction dir = (Direction)state.getValue(DungeonUnlockerBlock.FACING);
         return ConfiguredModel.builder().modelFile(model).rotationY(90 * dir.get2DDataValue()).build();
      });
      ((ItemModelBuilder)this.itemModels().getBuilder(this.itemPrefix(this.name((Block)block.get())))).parent(model);
   }

   @NotNull
   private String itemPrefix(String str) {
      return "item/" + str;
   }

   @NotNull
   private String blockPrefix(String str) {
      return "block/" + str;
   }

   @NotNull
   private String name(Block block) {
      return ForgeRegistries.BLOCKS.getKey(block).getPath();
   }

   @NotNull
   public String getName() {
      return "TR Beyong BlockStates";
   }
}
