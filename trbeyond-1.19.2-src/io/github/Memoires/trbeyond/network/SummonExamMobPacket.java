package io.github.Memoires.trbeyond.network;

import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.entity.BarghestEntity;
import com.github.manasmods.tensura.entity.HoundDogEntity;
import com.github.manasmods.tensura.network.TensuraNetwork;
import com.github.manasmods.tensura.network.play2client.RequestFxSpawningPacket;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent.Context;

public class SummonExamMobPacket {
   private EntityType<BarghestEntity> ent;

   public SummonExamMobPacket(EntityType<BarghestEntity> ent) {
      this.ent = ent;
   }

   public SummonExamMobPacket(FriendlyByteBuf buf) {
   }

   public void toBytes(FriendlyByteBuf buf) {
   }

   public void handle(Supplier<Context> ctx) {
      ctx.get()
         .enqueueWork(
            () -> {
               ServerPlayer player = ctx.get().getSender();
               LivingEntity tester = player;
               Level level = player.level;

               for (int i = 0; i < 25; i++) {
                  level.playSound((Player)null, tester.getX(), tester.getY(), tester.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.5F, 1.0F);
                  TensuraNetwork.INSTANCE
                     .send(
                        PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> tester),
                        new RequestFxSpawningPacket(
                           new ResourceLocation("tensura:dark_circle"),
                           new BlockPos(tester.getX() + 3.0, tester.getY(), tester.getZ() + 3.0),
                           0.0,
                           0.0,
                           0.0,
                           0,
                           true
                        )
                     );
               }

               EntityType<HoundDogEntity> testMob = (EntityType<HoundDogEntity>)TensuraEntityTypes.HOUND_DOG.get();
               HoundDogEntity mob = new HoundDogEntity(testMob, level);
               mob.moveTo(tester.getX() + 3.0, tester.getY(), tester.getZ() + 3.0, tester.getYHeadRot(), 0.0F);
               level.addFreshEntity(mob);
               mob.addTag("exam_mob_e");
               mob.playSound(SoundEvents.EVOKER_CAST_SPELL, 3.0F, 1.0F);
               TensuraParticleHelper.addServerParticlesAroundSelf(mob, ParticleTypes.FLASH, 2.0);
               TensuraParticleHelper.addServerParticlesAroundSelf(mob, ParticleTypes.FLASH, 3.0);
            }
         );
      ctx.get().setPacketHandled(true);
   }
}
