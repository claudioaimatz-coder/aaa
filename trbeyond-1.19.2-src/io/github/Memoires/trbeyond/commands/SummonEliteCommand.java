package io.github.Memoires.trbeyond.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.github.Memoires.trbeyond.capability.player.BeyondPlayerCapability;
import io.github.Memoires.trbeyond.handlers.ChallengeEventHandler;
import io.github.Memoires.trbeyond.util.ChallengeType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntitySummonArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class SummonEliteCommand {
   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.summon.failed"));
   private static final SimpleCommandExceptionType ERROR_DUPLICATE_UUID = new SimpleCommandExceptionType(Component.translatable("commands.summon.failed.uuid"));
   private static final SimpleCommandExceptionType INVALID_POSITION = new SimpleCommandExceptionType(Component.translatable("commands.summon.invalidPosition"));

   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("dungeon").requires(source -> source.hasPermission(2)))
               .then(
                  Commands.literal("summonelite")
                     .then(
                        Commands.argument("entity", EntitySummonArgument.id())
                           .suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                           .executes(
                              ctx -> summonElite(
                                    (CommandSourceStack)ctx.getSource(),
                                    EntitySummonArgument.getSummonableEntity(ctx, "entity"),
                                    ((CommandSourceStack)ctx.getSource()).getPosition(),
                                    new CompoundTag(),
                                    true
                                 )
                           )
                     )
               ))
            .then(Commands.literal("clearChallenge").executes(SummonEliteCommand::clearChallengeCommand))
      );
   }

   private static int clearChallengeCommand(CommandContext<CommandSourceStack> ctx) {
      CommandSourceStack source = (CommandSourceStack)ctx.getSource();
      ServerPlayer player = source.getPlayer();
      if (player != null) {
         BeyondPlayerCapability.getFrom(player).ifPresent(cap -> {
            ChallengeType challenge = cap.getChallengeType();
            if (challenge == null) {
               source.sendFailure(Component.literal("Player doesn't have a challenge"));
            } else if (challenge.getCategory() != "shrine") {
               ChallengeEventHandler.failChallenge(player, challenge);
               source.sendSuccess(Component.literal("Challenge removed."), false);
            } else {
               source.sendFailure(Component.literal("Not allowed on shrine challenges."));
            }
         });
      }

      return 1;
   }

   private static int summonElite(CommandSourceStack pSource, ResourceLocation pType, Vec3 pPos, CompoundTag pNbt, boolean pRandomizeProperties) throws CommandSyntaxException {
      BlockPos blockpos = new BlockPos(pPos);
      if (!Level.isInSpawnableBounds(blockpos)) {
         throw INVALID_POSITION.create();
      } else {
         CompoundTag compoundtag = pNbt.copy();
         compoundtag.putString("id", pType.toString());
         ServerLevel serverlevel = pSource.getLevel();
         Entity entity = EntityType.loadEntityRecursive(compoundtag, serverlevel, p_138828_ -> {
            p_138828_.moveTo(pPos.x, pPos.y, pPos.z, p_138828_.getYRot(), p_138828_.getXRot());
            return p_138828_;
         });
         if (entity == null) {
            throw ERROR_FAILED.create();
         } else {
            if (pRandomizeProperties && entity instanceof Mob) {
               if (!ForgeEventFactory.doSpecialSpawn(
                  (Mob)entity, pSource.getLevel(), (float)entity.getX(), (float)entity.getY(), (float)entity.getZ(), null, MobSpawnType.COMMAND
               )) {
                  ((Mob)entity)
                     .finalizeSpawn(pSource.getLevel(), pSource.getLevel().getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.COMMAND, (SpawnGroupData)null, (CompoundTag)null);
               }

               if (entity instanceof LivingEntity living) {
                  living.getPersistentData().putBoolean("spawnElite", true);
               }
            }

            if (!serverlevel.tryAddFreshEntityWithPassengers(entity)) {
               throw ERROR_DUPLICATE_UUID.create();
            } else {
               pSource.sendSuccess(Component.translatable("commands.summon.success", new Object[]{entity.getDisplayName()}), true);
               return 1;
            }
         }
      }
   }
}
