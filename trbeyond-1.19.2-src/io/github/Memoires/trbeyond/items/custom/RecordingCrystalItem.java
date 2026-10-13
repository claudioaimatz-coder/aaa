package io.github.Memoires.trbeyond.items.custom;

import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.network.TensuraNetwork;
import com.github.manasmods.tensura.network.play2client.RequestTotemDisplayPacket;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import com.github.manasmods.tensura.util.TensuraRarity;
import io.github.Memoires.trbeyond.capability.player.BeyondPlayerCapability;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecordingCrystalItem extends Item {
   public RecordingCrystalItem() {
      super(new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).rarity(TensuraRarity.UNIQUE).stacksTo(1));
   }

   public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
      if (stack.getTag() != null && stack.getTag().contains("savedY")) {
         String dimensionKey = stack.getTag().getString("savedDimension");
         pTooltipComponents.add(
            Component.translatable(
                  "trbeyond.tooltip.recording_crystal.saved",
                  new Object[]{stack.getTag().getDouble("savedX"), stack.getTag().getDouble("savedY"), stack.getTag().getDouble("savedZ")}
               )
               .withStyle(ChatFormatting.LIGHT_PURPLE)
         );
         pTooltipComponents.add(Component.literal("Dimension: " + dimensionKey).withStyle(ChatFormatting.GOLD));
      }

      pTooltipComponents.add(Component.translatable("trbeyond.tooltip.recording_crystal").withStyle(ChatFormatting.DARK_AQUA));
   }

   public int getUseDuration(ItemStack pStack) {
      return 10000;
   }

   public UseAnim getUseAnimation(ItemStack pStack) {
      return UseAnim.BOW;
   }

   public boolean isFoil(ItemStack stack) {
      return stack.getTag() != null && stack.getTag().contains("savedY");
   }

   @NotNull
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pHand) {
      ItemStack stack = player.getItemInHand(pHand);
      if (stack.getTag() != null && stack.getTag().contains("savedY")) {
         player.startUsingItem(pHand);
         return InteractionResultHolder.consume(stack);
      } else if (!level.dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
         player.displayClientMessage(
            Component.translatable("trbeyond.tooltip.items_dungeon_only", new Object[]{stack.getHoverName()}).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)),
            false
         );
         return InteractionResultHolder.fail(stack);
      } else {
         Vec3 pos = Vec3.atCenterOf(player.blockPosition());
         stack.getOrCreateTag().putDouble("savedX", pos.x());
         stack.getOrCreateTag().putDouble("savedY", pos.y());
         stack.getOrCreateTag().putDouble("savedZ", pos.z());
         stack.getOrCreateTag().putString("savedDimension", level.dimension().location().toString());
         level.playSound(null, pos.x(), pos.y(), pos.z(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
         TensuraParticleHelper.addServerParticlesAroundSelf(player, ParticleTypes.END_ROD, 1.0);
         return InteractionResultHolder.success(stack);
      }
   }

   public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
      if (pRemainingUseDuration % 2 == 0) {
         TensuraParticleHelper.addServerParticlesAroundSelf(pLivingEntity, ParticleTypes.END_ROD, 3.0);
      }
   }

   public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, int pTimeLeft) {
      if (!level.isClientSide()) {
         if (stack.getTag() != null) {
            if (stack.getTag().contains("savedY")) {
               int useTicks = this.getUseDuration(stack) - pTimeLeft;
               if (useTicks >= 10) {
                  BlockPos pos = new BlockPos(stack.getTag().getDouble("savedX"), stack.getTag().getDouble("savedY"), stack.getTag().getDouble("savedZ"));
                  String savedDimensionKey = stack.getTag().getString("savedDimension");
                  if (entity instanceof ServerPlayer player) {
                     MinecraftServer server = level.getServer();
                     if (server == null) {
                        return;
                     }

                     ServerLevel targetLevel = server.m_129880_(BeyondDungeon.DUNGEON_LEVEL_KEY);
                     if (targetLevel == null) {
                        player.displayClientMessage(
                           Component.literal("Teleport failed: Dungeon dimension not found.").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false
                        );
                        return;
                     }

                     BlockPos lobbyPos = new BlockPos(9, 117, 7);
                     List<LivingEntity> list = entity.getLevel()
                        .getEntitiesOfClass(
                           LivingEntity.class,
                           entity.getBoundingBox().inflate(10.0),
                           living -> living.isAlive() && (living.isAlliedTo(entity) || living.is(entity))
                        );

                     for (LivingEntity target : list) {
                        SkillHelper.moveAcrossDimensionTo(
                           target, (double)lobbyPos.getX(), (double)lobbyPos.getY(), (double)lobbyPos.getZ(), 0.0F, 0.0F, targetLevel
                        );
                     }

                     boolean isSafeLocation = isSafeLocation(targetLevel, pos);
                     boolean teleportedToLobby = false;
                     System.out
                        .println(
                           "DEBUG: Now in dungeon, checking safety for coordinates (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")"
                        );
                     System.out.println("DEBUG: Location is safe: " + isSafeLocation);
                     player.displayClientMessage(Component.literal("Teleporting to saved location...").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)), false);

                     for (LivingEntity target : list) {
                        SkillHelper.moveAcrossDimensionTo(
                           target, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 0.0F, 0.0F, targetLevel
                        );
                     }

                     if (level.dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY) || targetLevel.dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
                        BeyondPlayerCapability.getFrom(player).ifPresent(cap -> {
                           cap.setUsedTicket(true);
                           cap.setReturned(false);
                           cap.setUsedCrystal(true);
                        });
                        BeyondPlayerCapability.sync(player);
                     }

                     TensuraNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new RequestTotemDisplayPacket(this.getDefaultInstance()));
                     entity.getLevel()
                        .playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
                     TensuraParticleHelper.addServerParticlesAroundSelf(entity, ParticleTypes.END_ROD, 1.0);
                     TensuraParticleHelper.addServerParticlesAroundSelf(entity, (ParticleOptions)TensuraParticles.SOLAR_FLASH.get(), 2.0);
                     if (teleportedToLobby) {
                        if (stack.getTag() != null) {
                           stack.getTag().remove("savedX");
                           stack.getTag().remove("savedY");
                           stack.getTag().remove("savedZ");
                           stack.getTag().remove("savedDimension");
                           if (stack.getTag().isEmpty()) {
                              stack.setTag(null);
                           }
                        }

                        player.displayClientMessage(
                           Component.literal("Recording Crystal has been reset due to unsafe teleport.")
                              .setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)),
                           false
                        );
                     } else if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                     }

                     entity.getLevel()
                        .playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
                     TensuraParticleHelper.addServerParticlesAroundSelf(entity, ParticleTypes.END_ROD, 1.0);
                     TensuraParticleHelper.addServerParticlesAroundSelf(entity, (ParticleOptions)TensuraParticles.SOLAR_FLASH.get(), 2.0);
                  }
               }
            }
         }
      }
   }

   public static boolean isSafeLocation(ServerLevel level, BlockPos pos) {
      System.out.println("DEBUG: Checking safety at " + pos);
      ChunkAccess chunkAccess = level.getChunkSource().getChunkNow(pos.getX() >> 4, pos.getZ() >> 4);
      if (chunkAccess == null) {
         System.out.println("DEBUG: Chunk not loaded — attempting to load...");

         try {
            level.getChunk(pos.getX() >> 4, pos.getZ() >> 4);
            System.out.println("DEBUG: Chunk loaded successfully.");
         } catch (Exception var6) {
            System.out.println("DEBUG: Failed to load chunk: " + var6.getMessage());
            return false;
         }
      }

      BlockPos checkPos = pos;
      boolean foundSolid = false;

      for (int i = 0; i < 15; i++) {
         if (!level.getBlockState(checkPos).isAir()) {
            foundSolid = true;
            System.out
               .println("DEBUG: Found solid block at Y=" + checkPos.getY() + " (block: " + level.getBlockState(checkPos).getBlock().getName().getString() + ")");
            break;
         }

         checkPos = checkPos.below();
         if (checkPos.getY() < level.getMinBuildHeight()) {
            System.out.println("DEBUG: Reached build floor");
            break;
         }
      }

      if (!foundSolid) {
         System.out.println("DEBUG: No solid blocks found — location is in void or dungeon was reset");
         return false;
      } else {
         System.out.println("DEBUG: Location appears safe");
         return true;
      }
   }
}
