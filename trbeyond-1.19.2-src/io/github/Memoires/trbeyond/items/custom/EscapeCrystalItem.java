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
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

public class EscapeCrystalItem extends Item {
   public EscapeCrystalItem() {
      super(new Properties().tab(BeyondItemGroup.BEYOND_ITEMS).rarity(TensuraRarity.UNIQUE).stacksTo(1));
   }

   public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
      pTooltipComponents.add(Component.translatable("trbeyond.tooltip.escape_crystal").withStyle(ChatFormatting.DARK_AQUA));
   }

   public int getUseDuration(ItemStack pStack) {
      return 10000;
   }

   public UseAnim getUseAnimation(ItemStack pStack) {
      return UseAnim.BOW;
   }

   @NotNull
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pHand) {
      ItemStack stack = player.getItemInHand(pHand);
      TensuraParticleHelper.addServerParticlesAroundSelf(player, ParticleTypes.END_ROD, 1.0);
      player.startUsingItem(pHand);
      return InteractionResultHolder.consume(stack);
   }

   public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
      if (pRemainingUseDuration % 2 == 0) {
         TensuraParticleHelper.addServerParticlesAroundSelf(pLivingEntity, ParticleTypes.END_ROD, 3.0);
      }
   }

   public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, int pTimeLeft) {
      if (!level.isClientSide()) {
         if (entity instanceof ServerPlayer player) {
            MinecraftServer server = level.getServer();
            if (server == null) {
               return;
            }

            ServerLevel lab = server.m_129880_(BeyondDungeon.DUNGEON_LEVEL_KEY);
            if (lab == null) {
               return;
            }

            Vec3 pos = new Vec3(9.5, 117.0, -6.5);
            TensuraNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new RequestTotemDisplayPacket(this.getDefaultInstance()));
            entity.getLevel().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
            TensuraParticleHelper.addServerParticlesAroundSelf(entity, ParticleTypes.END_ROD, 1.0);
            TensuraParticleHelper.addServerParticlesAroundSelf(entity, (ParticleOptions)TensuraParticles.SOLAR_FLASH.get(), 2.0);

            for (LivingEntity target : entity.getLevel()
               .getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(10.0), living -> living.isAlive() && (living.isAlliedTo(entity) || living.is(entity)))) {
               target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0, false, false, false));
               SkillHelper.moveAcrossDimensionTo(target, pos.x, pos.y, pos.z, 0.0F, 0.0F, lab);
            }

            entity.getLevel().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            TensuraParticleHelper.addServerParticlesAroundSelf(entity, ParticleTypes.END_ROD, 1.0);
            TensuraParticleHelper.addServerParticlesAroundSelf(entity, (ParticleOptions)TensuraParticles.SOLAR_FLASH.get(), 2.0);
         }
      }
   }
}
