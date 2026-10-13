package io.github.Memoires.trbeyond.handlers;

import com.github.manasmods.tensura.event.EnergyDrainEvent;
import com.github.manasmods.tensura.event.PossessionEvent;
import com.github.manasmods.tensura.event.SpiritualHurtEvent;
import io.github.Memoires.trbeyond.entity.custom.DryadEntity;
import io.github.Memoires.trbeyond.entity.custom.GozulEntity;
import io.github.Memoires.trbeyond.entity.custom.GuildLadyEntity;
import io.github.Memoires.trbeyond.entity.custom.GuildTesterEntity;
import io.github.Memoires.trbeyond.entity.custom.KaijinEntity;
import io.github.Memoires.trbeyond.entity.custom.KaoruEntity;
import io.github.Memoires.trbeyond.entity.custom.KillerDryadEntity;
import io.github.Memoires.trbeyond.entity.custom.KobyEntity;
import io.github.Memoires.trbeyond.entity.custom.KurobeEntity;
import io.github.Memoires.trbeyond.entity.custom.MezulEntity;
import io.github.Memoires.trbeyond.entity.custom.ShrineEntity;
import io.github.Memoires.trbeyond.entity.custom.TrainingDummyEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.FORGE
)
public class NPCHandler {
   @SubscribeEvent
   public static void onSpiritualDamage(SpiritualHurtEvent event) {
      if (event.getEntity() instanceof GuildLadyEntity
         || event.getEntity() instanceof DryadEntity
         || event.getEntity() instanceof KurobeEntity
         || event.getEntity() instanceof KaijinEntity
         || event.getEntity() instanceof GuildTesterEntity
         || event.getEntity() instanceof TrainingDummyEntity
         || event.getEntity() instanceof KillerDryadEntity
         || event.getEntity() instanceof KobyEntity
         || event.getEntity() instanceof KaoruEntity
         || event.getEntity() instanceof ShrineEntity) {
         event.setCanceled(true);
      }

      if ((event.getEntity() instanceof GozulEntity || event.getEntity() instanceof MezulEntity)
         && event.getEntity() instanceof Mob mob
         && mob.getTarget() == null) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onPossession(PossessionEvent event) {
      if (event.getEntity() instanceof GuildLadyEntity
         || event.getEntity() instanceof DryadEntity
         || event.getEntity() instanceof KurobeEntity
         || event.getEntity() instanceof KaijinEntity
         || event.getEntity() instanceof GuildTesterEntity
         || event.getEntity() instanceof TrainingDummyEntity
         || event.getEntity() instanceof KillerDryadEntity
         || event.getEntity() instanceof KobyEntity
         || event.getEntity() instanceof KaoruEntity
         || event.getEntity() instanceof ShrineEntity) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onAttack(LivingAttackEvent event) {
      if (event.getEntity() instanceof GuildLadyEntity
         || event.getEntity() instanceof DryadEntity
         || event.getEntity() instanceof KurobeEntity
         || event.getEntity() instanceof KaijinEntity
         || event.getEntity() instanceof GuildTesterEntity
         || event.getEntity() instanceof TrainingDummyEntity
         || event.getEntity() instanceof KillerDryadEntity
         || event.getEntity() instanceof KobyEntity
         || event.getEntity() instanceof KaoruEntity) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onDamage(LivingDamageEvent event) {
      if (!event.getSource().isBypassInvul()
         && (
            event.getEntity() instanceof GuildLadyEntity
               || event.getEntity() instanceof DryadEntity
               || event.getEntity() instanceof KurobeEntity
               || event.getEntity() instanceof KaijinEntity
               || event.getEntity() instanceof GuildTesterEntity
               || event.getEntity() instanceof TrainingDummyEntity
               || event.getEntity() instanceof KillerDryadEntity
               || event.getEntity() instanceof KobyEntity
               || event.getEntity() instanceof KaoruEntity
         )) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onEnergySteal(EnergyDrainEvent event) {
      if (event.getEntity() instanceof GuildLadyEntity
         || event.getEntity() instanceof DryadEntity
         || event.getEntity() instanceof KurobeEntity
         || event.getEntity() instanceof KaijinEntity
         || event.getEntity() instanceof GuildTesterEntity
         || event.getEntity() instanceof TrainingDummyEntity
         || event.getEntity() instanceof KillerDryadEntity
         || event.getEntity() instanceof KobyEntity
         || event.getEntity() instanceof KaoruEntity
         || event.getEntity() instanceof ShrineEntity) {
         event.setCanceled(true);
      }

      if ((event.getEntity() instanceof GozulEntity || event.getEntity() instanceof MezulEntity)
         && event.getEntity() instanceof Mob mob
         && mob.getTarget() == null) {
         event.setCanceled(true);
      }
   }
}
