package io.github.Memoires.trbeyond.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.github.Memoires.trbeyond.block.ShrineBlock;
import io.github.Memoires.trbeyond.capability.dungeon.DungeonCapability;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.command.EnumArgument;

public class DungeonStatsCommand {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("dungeonStats")
               .then(
                  ((LiteralArgumentBuilder)Commands.literal("labyrinth").requires(source -> source.hasPermission(2)))
                     .executes(
                        ctx -> {
                           CommandSourceStack source = (CommandSourceStack)ctx.getSource();
                           ServerLevel labyrinthLevel = source.getServer().m_129880_(BeyondDungeon.DUNGEON_LEVEL_KEY);
                           if (labyrinthLevel != null) {
                              List<ServerPlayer> players = labyrinthLevel.players();
                              String playerNames = players.stream().map(p -> p.getName().getString()).collect(Collectors.joining(", "));
                              Component playerCountComponent = Component.literal(String.valueOf(players.size()))
                                 .withStyle(
                                    style -> style.withHoverEvent(
                                             new HoverEvent(Action.SHOW_TEXT, Component.literal(playerNames.isEmpty() ? "Nobody" : playerNames))
                                          )
                                          .withColor(ChatFormatting.YELLOW)
                                 );
                              source.sendSuccess(Component.translatable("trbeyond.message.dungeon_player_count", new Object[]{playerCountComponent}), false);
                           } else {
                              source.sendFailure(Component.translatable("trbeyond.message.dungeon_not_loaded"));
                           }

                           return 1;
                        }
                     )
               ))
            .then(
               Commands.literal("playerStats")
                  .then(
                     ((RequiredArgumentBuilder)Commands.argument("player", EntityArgument.player())
                           .then(
                              ((LiteralArgumentBuilder)Commands.literal("setChallengeCount").requires(source -> source.hasPermission(2)))
                                 .then(
                                    Commands.argument("tier", EnumArgument.enumArgument(ShrineBlock.DungeonShrineTier.class))
                                       .then(
                                          Commands.argument("count", IntegerArgumentType.integer())
                                             .executes(
                                                ctx -> {
                                                   CommandSourceStack source = (CommandSourceStack)ctx.getSource();
                                                   ServerPlayer targetPlayer = EntityArgument.getPlayer(ctx, "player");
                                                   ShrineBlock.DungeonShrineTier tier = (ShrineBlock.DungeonShrineTier)ctx.getArgument(
                                                      "tier", ShrineBlock.DungeonShrineTier.class
                                                   );
                                                   Integer completions = IntegerArgumentType.getInteger(ctx, "count");
                                                   DungeonCapability.getFrom(targetPlayer)
                                                      .ifPresent(
                                                         cap -> {
                                                            cap.setChallengeCompletionCount(tier, completions);
                                                            source.sendSuccess(
                                                               Component.translatable(
                                                                  "trbeyond.message.player_stats.challenges.set_completions",
                                                                  new Object[]{tier.name(), completions}
                                                               ),
                                                               true
                                                            );
                                                         }
                                                      );
                                                   return 1;
                                                }
                                             )
                                       )
                                 )
                           ))
                        .executes(
                           ctx -> {
                              CommandSourceStack source;
                              ServerPlayer targetPlayer;
                              boolean var10000;
                              label30: {
                                 source = (CommandSourceStack)ctx.getSource();
                                 targetPlayer = EntityArgument.getPlayer(ctx, "player");
                                 if (source.getEntity() instanceof ServerPlayer sourcePlayer && sourcePlayer.getUUID().equals(targetPlayer.getUUID())) {
                                    var10000 = true;
                                    break label30;
                                 }

                                 var10000 = false;
                              }

                              boolean isSelf = var10000;
                              boolean isOP = source.hasPermission(2);
                              if (!isSelf && !isOP) {
                                 source.sendFailure(Component.translatable("trbeyond.message.player_stats.deny"));
                              } else {
                                 long cooldownDuration = (Long)ServerConfig.INSTANCE.dungeonConfig.enterCooldown.get();
                                 long remainingCooldown = -1L;
                                 if (targetPlayer.getPersistentData().contains("dungeonEnterCooldown")) {
                                    long entryCooldown = targetPlayer.getPersistentData().getLong("dungeonEnterCooldown");
                                    long currentTime = System.currentTimeMillis() / 1000L;
                                    remainingCooldown = entryCooldown + cooldownDuration - currentTime;
                                 }

                                 if (remainingCooldown > 0L) {
                                    source.sendSuccess(
                                       Component.translatable("trbeyond.message.player_stats", new Object[]{targetPlayer.getName(), remainingCooldown}), false
                                    );
                                 } else {
                                    source.sendSuccess(
                                       Component.translatable("trbeyond.message.player_stats.no_cooldown", new Object[]{targetPlayer.getName()}), false
                                    );
                                 }

                                 DungeonCapability.getFrom(targetPlayer)
                                    .ifPresent(
                                       cap -> {
                                          source.sendSuccess(
                                             Component.translatable("trbeyond.message.player_stats.challenges.header", new Object[]{targetPlayer.getName()}),
                                             false
                                          );

                                          for (ShrineBlock.DungeonShrineTier tier : Arrays.stream(ShrineBlock.DungeonShrineTier.values()).toList()) {
                                             source.sendSuccess(
                                                Component.translatable(
                                                   "trbeyond.message.player_stats.challenges.count",
                                                   new Object[]{cap.getChallengeCompletionCount(tier), tier.name()}
                                                ),
                                                false
                                             );
                                          }
                                       }
                                    );
                              }

                              return 1;
                           }
                        )
                  )
            )
      );
   }
}
