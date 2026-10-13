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
import net.minecraft.nbt.CompoundTag;
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
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AutoRecordingCrystalItem extends Item {
   public AutoRecordingCrystalItem() {
      super(new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).rarity(TensuraRarity.UNIQUE).stacksTo(1));
   }

   public void appendHoverText(@NotNull ItemStack stack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
      if (stack.getTag() != null && stack.getTag().contains("savedY")) {
         CompoundTag tag = stack.getTag();
         String dimensionKey = tag.getString("savedDimension");
         pTooltipComponents.add(
            Component.translatable(
                  "trbeyond.tooltip.auto_recording_crystal.saved", new Object[]{tag.getDouble("savedX"), tag.getDouble("savedY"), tag.getDouble("savedZ")}
               )
               .withStyle(ChatFormatting.LIGHT_PURPLE)
         );
         pTooltipComponents.add(Component.literal("Dimension: " + dimensionKey).withStyle(ChatFormatting.GOLD));
      }

      pTooltipComponents.add(Component.translatable("trbeyond.tooltip.auto_recording_crystal").withStyle(ChatFormatting.DARK_AQUA));
   }

   public int getUseDuration(@NotNull ItemStack pStack) {
      return 10000;
   }

   @NotNull
   public UseAnim getUseAnimation(@NotNull ItemStack pStack) {
      return UseAnim.BOW;
   }

   public boolean isFoil(@NotNull ItemStack stack) {
      CompoundTag tag = stack.getTag();
      return tag != null && tag.contains("savedY");
   }

   @NotNull
   public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand pHand) {
      ItemStack stack = player.getItemInHand(pHand);
      CompoundTag tag = stack.getTag();
      if (tag != null && tag.contains("savedY")) {
         player.startUsingItem(pHand);
         return InteractionResultHolder.consume(stack);
      } else {
         player.displayClientMessage(Component.translatable("trbeyond.tooltip.auto_recording_crystal.no_saved").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
         return InteractionResultHolder.fail(stack);
      }
   }

   public void onUseTick(@NotNull Level pLevel, @NotNull LivingEntity pLivingEntity, @NotNull ItemStack pStack, int pRemainingUseDuration) {
      if (pRemainingUseDuration % 2 == 0) {
         TensuraParticleHelper.addServerParticlesAroundSelf(pLivingEntity, ParticleTypes.END_ROD, 3.0);
      }
   }

   public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, int pTimeLeft) {
      if (!level.isClientSide()) {
         CompoundTag tag = stack.getTag();
         if (tag != null) {
            if (tag.contains("savedY")) {
               int useTicks = this.getUseDuration(stack) - pTimeLeft;
               if (useTicks >= 10) {
                  BlockPos pos = new BlockPos(tag.getDouble("savedX"), tag.getDouble("savedY"), tag.getDouble("savedZ"));
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

                     boolean isSafeLocation = RecordingCrystalItem.isSafeLocation(targetLevel, pos);
                     boolean teleportedToLobby = false;
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
                        CompoundTag tag2 = stack.getTag();
                        if (tag2 != null) {
                           tag2.remove("savedX");
                           tag2.remove("savedY");
                           tag2.remove("savedZ");
                           tag2.remove("savedDimension");
                           if (tag2.isEmpty()) {
                              stack.setTag(null);
                           }
                        }

                        player.displayClientMessage(
                           Component.literal("Auto Recording Crystal has been reset due to unsafe teleport.")
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
}
