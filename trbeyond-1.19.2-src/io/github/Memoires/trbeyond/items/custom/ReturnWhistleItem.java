package io.github.Memoires.trbeyond.items.custom;

import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.network.TensuraNetwork;
import com.github.manasmods.tensura.network.play2client.RequestTotemDisplayPacket;
import com.github.manasmods.tensura.registry.particle.TensuraParticles;
import com.github.manasmods.tensura.util.TensuraRarity;
import io.github.Memoires.trbeyond.items.BeyondItemGroup;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReturnWhistleItem extends Item {
   public ReturnWhistleItem() {
      super(new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).rarity(TensuraRarity.UNIQUE).stacksTo(1));
   }

   public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
      pTooltipComponents.add(Component.translatable("trbeyond.tooltip.items_dungeon_only").withStyle(ChatFormatting.RED));
      pTooltipComponents.add(Component.translatable("trbeyond.tooltip.return_whistle").withStyle(ChatFormatting.DARK_AQUA));
   }

   public int getUseDuration(ItemStack pStack) {
      return 10000;
   }

   public UseAnim getUseAnimation(ItemStack pStack) {
      return UseAnim.TOOT_HORN;
   }

   @NotNull
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pHand) {
      ItemStack stack = player.getItemInHand(pHand);
      player.startUsingItem(pHand);
      return InteractionResultHolder.consume(stack);
   }

   public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
      TensuraParticleHelper.addServerParticlesAroundSelf(pLivingEntity, ParticleTypes.REVERSE_PORTAL, 3.0);
   }

   public void releaseUsing(@NotNull ItemStack pStack, @NotNull Level level, @NotNull LivingEntity entity, int pTimeLeft) {
      if (!level.isClientSide()) {
         int useTicks = this.getUseDuration(pStack) - pTimeLeft;
         if (useTicks >= 10) {
            MinecraftServer server = level.getServer();
            if (entity instanceof ServerPlayer player && server != null) {
               ServerLevel dungeon = server.m_129880_(BeyondDungeon.DUNGEON_LEVEL_KEY);
               if (!level.dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
                  player.displayClientMessage(
                     Component.translatable("trbeyond.tooltip.items_dungeon_only", new Object[]{pStack.getHoverName()})
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)),
                     false
                  );
                  return;
               }

               if (dungeon == null) {
                  return;
               }

               TensuraNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new RequestTotemDisplayPacket(this.getDefaultInstance()));
               entity.getLevel().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 2.0F, 1.0F);
               TensuraParticleHelper.addServerParticlesAroundSelf(entity, ParticleTypes.FLASH, 1.0);
               TensuraParticleHelper.addServerParticlesAroundSelf(entity, (ParticleOptions)TensuraParticles.SOLAR_FLASH.get(), 2.0);
               if (!player.getAbilities().instabuild) {
                  pStack.shrink(1);
               }

               new Vec3(0.0, 121.0, 0.0);

               for (LivingEntity target : entity.getLevel()
                  .getEntitiesOfClass(
                     LivingEntity.class, entity.getBoundingBox().inflate(10.0), living -> living.isAlive() && (living.isAlliedTo(entity) || living.is(entity))
                  )) {
                  SkillHelper.moveAcrossDimensionTo(target, 8.0, 116.0, -7.0, 0.0F, 0.0F, dungeon);
               }

               ResourceLocation advId = new ResourceLocation("trbeyond", "scaredy_cat");
               Advancement advancement = server.m_129889_().getAdvancement(advId);
               if (advancement != null) {
                  AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);

                  for (String criterion : progress.getRemainingCriteria()) {
                     player.getAdvancements().award(advancement, criterion);
                  }
               }

               entity.getLevel().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 2.0F, 1.0F);
               TensuraParticleHelper.addServerParticlesAroundSelf(entity, ParticleTypes.FLASH, 1.0);
               TensuraParticleHelper.addServerParticlesAroundSelf(entity, (ParticleOptions)TensuraParticles.SOLAR_FLASH.get(), 2.0);
            }
         }
      }
   }
}
