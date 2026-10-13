package io.github.Memoires.trbeyond.handlers;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.entity.magic.projectile.AuraSlashProjectile;
import com.github.manasmods.tensura.registry.battlewill.ProjectileArts;
import io.github.Memoires.trbeyond.capability.player.BeyondPlayerCapability;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OgreSwordCannonHandler {
   public static boolean isSwing = false;

   public static void useSkillServerSide(ServerPlayer player) {
      ManasSkillInstance instance = (ManasSkillInstance)SkillAPI.getSkillsFrom(player).getSkill((ManasSkill)ProjectileArts.OGRE_SWORD_CANNON.get()).get();
      ItemStack stack = player.getMainHandItem();
      if (!stack.isEmpty()) {
         int power = instance.isMastered(player) ? 10 : 5;
         double cost = 200.0 * (double)power;
         if (!SkillHelper.outOfAura(player, 0.0)) {
            player.level.playSound(null, player.blockPosition(), (SoundEvent)BeyondSoundEvents.OGRE_SWORD_CANNON.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            OgreSwordCannonHandler.OgreSwordCannonTickHandler handler = new OgreSwordCannonHandler.OgreSwordCannonTickHandler(
               player, stack, power, instance, cost
            );
            MinecraftForge.EVENT_BUS.register(handler);
         }
      }
   }

   public static void setSwingState(Player player, boolean isSwinging) {
      player.getPersistentData().putBoolean("ogre_sword_swinging", isSwinging);
   }

   public static boolean isSwinging(Player player) {
      return player.getPersistentData().getBoolean("ogre_sword_swinging");
   }

   public static class OgreSwordCannonTickHandler {
      private final ServerPlayer player;
      private final ItemStack stack;
      private final int power;
      private final ManasSkillInstance instance;
      private final double cost;
      private int ticks = 0;

      public OgreSwordCannonTickHandler(ServerPlayer player, ItemStack stack, int power, ManasSkillInstance instance, double cost) {
         this.player = player;
         this.stack = stack;
         this.power = power;
         this.instance = instance;
         this.cost = cost;
      }

      @SubscribeEvent
      public void onServerTick(ServerTickEvent event) {
         if (event.phase == Phase.END) {
            this.ticks++;
            if (this.ticks == 35) {
               BeyondPlayerCapability.getFrom(this.player).ifPresent(cap -> cap.setAction(true));
               BeyondPlayerCapability.sync(this.player);
            }

            if (this.ticks == 45 && !this.player.level.isClientSide) {
               AuraSlashProjectile slash = new AuraSlashProjectile(this.player.level, this.player);
               slash.setSpeed(2.5F);
               slash.setDamage((float)this.player.getAttributeValue(Attributes.ATTACK_DAMAGE) * (1.5F + 0.5F * (float)this.power));
               slash.setSize((float)this.power);
               slash.setLife(20 * this.power);
               slash.setApCost(this.cost * (double)this.power);
               slash.setSkill(this.instance);
               slash.setSpeed(1.5F);
               slash.setNoGravity(true);
               slash.shootFromRot(this.player.getLookAngle().multiply(1.0, 0.0, 1.0));
               slash.setPos(this.player.position().add(0.0, (double)(this.player.getBbHeight() / 2.0F - slash.getBbHeight() / 2.0F), 0.0));
               this.player.level.addFreshEntity(slash);
               this.player
                  .level
                  .playSound(null, this.player.getX(), this.player.getY(), this.player.getZ(), SoundEvents.WARDEN_ATTACK_IMPACT, SoundSource.PLAYERS, 1.0F, 1.0F);
            }

            if (this.ticks == 70) {
               BeyondPlayerCapability.getFrom(this.player).ifPresent(cap -> cap.setAction(false));
               BeyondPlayerCapability.sync(this.player);
               this.stack.getOrCreateTag().putInt("int", 0);
               MinecraftForge.EVENT_BUS.unregister(this);
            }
         }
      }
   }
}
