package io.github.Memoires.trbeyond.worldgen.colosseum;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.registry.structures.BeyondStructures;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationStub;
import net.minecraft.world.level.levelgen.structure.Structure.StructureSettings;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.jetbrains.annotations.NotNull;

public class Colosseum extends Structure {
   public static final MapCodec<Colosseum> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
               settingsCodec(instance),
               StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
               ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
               Codec.intRange(0, 50).fieldOf("size").forGetter(structure -> structure.size),
               HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
               Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
               Codec.intRange(1, 512).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter)
            )
            .apply(instance, Colosseum::new)
   );
   public static final Codec<Colosseum> CODEC = MAP_CODEC.codec();
   private final Holder<StructureTemplatePool> startPool;
   private final Optional<ResourceLocation> startJigsawName;
   private final int size;
   private final HeightProvider startHeight;
   private final Optional<Types> projectStartToHeightmap;
   private final int maxDistanceFromCenter;

   public Colosseum(
      StructureSettings config,
      Holder<StructureTemplatePool> startPool,
      Optional<ResourceLocation> startJigsawName,
      int size,
      HeightProvider startHeight,
      Optional<Types> projectStartToHeightmap,
      int maxDistanceFromCenter
   ) {
      super(config);
      this.startPool = startPool;
      this.startJigsawName = startJigsawName;
      this.size = size;
      this.startHeight = startHeight;
      this.projectStartToHeightmap = projectStartToHeightmap;
      this.maxDistanceFromCenter = maxDistanceFromCenter;
   }

   private boolean extraSpawningChecks(@NotNull GenerationContext context) {
      if (!(Boolean)ServerConfig.INSTANCE.dungeonConfig.generateColosseum.get()) {
         return false;
      } else {
         Rotation rotation = Rotation.getRandom(context.random());
         BlockPos blockpos = this.getLowestYIn5by5BoxOffset7Blocks(context, rotation);
         int height = blockpos.getY();
         return height < 150 && height > 60;
      }
   }

   @NotNull
   public Optional<GenerationStub> findGenerationPoint(@NotNull GenerationContext context) {
      if (!this.extraSpawningChecks(context)) {
         return Optional.empty();
      } else {
         int startY = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
         ChunkPos chunkPos = context.chunkPos();
         BlockPos blockPos = new BlockPos(chunkPos.getMinBlockX(), startY, chunkPos.getMinBlockZ());
         return JigsawPlacement.addPieces(
            context, this.startPool, this.startJigsawName, this.size, blockPos, false, this.projectStartToHeightmap, this.maxDistanceFromCenter
         );
      }
   }

   public StructureType<?> type() {
      return (StructureType<?>)BeyondStructures.COLOSSEUM.get();
   }
}
