package io.github.Memoires.trbeyond.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.Memoires.trbeyond.network.worldgen.DungeonSaveData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ClearDungeonCommand {
   public static boolean dungeonCleared = false;
   public static boolean createDungeon = false;
   public static int NUM_STRUCTURES = 10;
   public static double LOOPBACK_PROB = 0.75;
   public static double DEADEND_PROB = 0.6;

   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("cleardungeon").requires(source -> source.hasPermission(2)))
            .executes(ctx -> clearDungeon((CommandSourceStack)ctx.getSource()))
      );
      dispatcher.register(
         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("createdungeon").requires(source -> source.hasPermission(2)))
            .then(
               Commands.argument("num_structures", IntegerArgumentType.integer(1))
                  .then(
                     Commands.argument("loopback_prob", DoubleArgumentType.doubleArg(0.0, 1.0))
                        .then(
                           Commands.argument("deadend_prob", DoubleArgumentType.doubleArg(0.0, 1.0))
                              .executes(
                                 ctx -> createDungeon(
                                       (CommandSourceStack)ctx.getSource(),
                                       IntegerArgumentType.getInteger(ctx, "num_structures"),
                                       DoubleArgumentType.getDouble(ctx, "loopback_prob"),
                                       DoubleArgumentType.getDouble(ctx, "deadend_prob")
                                    )
                              )
                        )
                  )
            )
      );
   }

   private static int createDungeon(CommandSourceStack source, int numStructures, double loopbackProb, double deadendProb) {
      NUM_STRUCTURES = numStructures;
      LOOPBACK_PROB = loopbackProb;
      DEADEND_PROB = deadendProb;
      source.sendSuccess(Component.literal("Dungeon parameters set:"), false);
      source.sendSuccess(Component.literal(" - NUM_STRUCTURES: " + NUM_STRUCTURES), false);
      source.sendSuccess(Component.literal(" - LOOPBACK_PROB: " + LOOPBACK_PROB), false);
      source.sendSuccess(Component.literal(" - DEADEND_PROB: " + DEADEND_PROB), false);
      createDungeon = true;
      return 1;
   }

   private static int clearDungeon(CommandSourceStack source) {
      ResourceKey<Level> dungeonDim = ResourceKey.create(
         ResourceKey.createRegistryKey(new ResourceLocation("minecraft", "dimension")), new ResourceLocation("trbeyond", "dungeon")
      );
      ServerLevel dungeon = source.getServer().m_129880_(dungeonDim);
      if (dungeon == null) {
         source.sendFailure(Component.literal("Dungeon dimension not found."));
         return 0;
      } else {
         DungeonSaveData.getOverworld(dungeon).clearAllChestCooldown();
         DungeonSaveData.getOverworld(dungeon).clearAllShrineCooldown();
         DungeonSaveData.getOverworld(dungeon).setLoaded(false);

         for (ServerLevel serverLevel : source.getServer().m_129785_()) {
            if (serverLevel.dimension() == dungeonDim) {
               serverLevel.getEntities().getAll().forEach(entity -> {
                  if (!entity.isRemoved()) {
                     entity.discard();
                  }
               });
            }
         }

         int minY = dungeon.getMinBuildHeight();
         int maxY = dungeon.getMaxBuildHeight();
         int radius = 1024;

         for (int x = -radius; x <= radius; x++) {
            for (int y = minY; y < maxY; y++) {
               for (int z = -radius; z <= radius; z++) {
                  if (!dungeon.getBlockState(new BlockPos(x, y, z)).isAir()) {
                     dungeon.setBlockAndUpdate(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState());
                  }
               }
            }
         }

         source.sendSuccess(Component.literal("Dungeon cleared."), true);
         dungeonCleared = true;
         return 1;
      }
   }
}
