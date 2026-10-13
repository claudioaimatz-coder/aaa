package io.github.Memoires.trbeyond.handlers;

import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.capability.effects.TensuraEffectsCapability;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.client.particle.TensuraParticleHelper;
import com.github.manasmods.tensura.entity.magic.TensuraProjectile;
import com.github.manasmods.tensura.network.TensuraNetwork;
import com.github.manasmods.tensura.network.play2client.RequestTotemDisplayPacket;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import io.github.Memoires.trbeyond.capability.dungeon.DungeonCapability;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.items.weapons.DiabloClawItem;
import io.github.Memoires.trbeyond.network.worldgen.DungeonSaveData;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.registry.items.BeyondItems;
import io.github.Memoires.trbeyond.registry.sounds.BeyondSoundEvents;
import java.util.List;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.PacketDistributor;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.FORGE
)
public class ItemHandler {
   @SubscribeEvent(
      priority = EventPriority.LOW
   )
   public static void onInteractEntity(EntityInteract e) {
      if (!e.isCanceled()) {
         Player player = e.getEntity();
         if (!player.level.isClientSide()) {
            if (e.getTarget() instanceof LivingEntity entity) {
               if (!(entity instanceof Player)) {
                  if (player.getMainHandItem().is((Item)BeyondItems.REVIVAL_BRACELET.get())) {
                     DungeonCapability.getFrom(entity)
                        .ifPresent(
                           cap -> {
                              if (!cap.isRevivalBracelet()) {
                                 cap.setRevivalBracelet(true);
                                 DungeonCapability.sync(entity);
                                 player.getMainHandItem().shrink(1);
                                 player.swing(InteractionHand.MAIN_HAND, true);
                                 entity.getLevel()
                                    .playSound(
                                       null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.PLAYERS, 1.0F, 1.0F
                                    );
                                 e.setCanceled(true);
                              } else if (player.isCrouching()) {
                                 cap.setRevivalBracelet(false);
                                 DungeonCapability.sync(entity);
                                 player.swing(InteractionHand.MAIN_HAND, true);
                                 SkillHelper.dropItem(entity, player.getRandom(), new ItemStack((ItemLike)BeyondItems.REVIVAL_BRACELET.get()), 10, 1.0F);
                                 entity.getLevel()
                                    .playSound(
                                       null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.PLAYERS, 1.0F, 1.0F
                                    );
                                 e.setCanceled(true);
                              }
                           }
                        );
                  }
               }
            }
         }
      }
   }

   @SubscribeEvent(
      priority = EventPriority.LOW
   )
   public static void onDeathLowPriority(LivingDeathEvent e) {
      if (!e.isCanceled()) {
         LivingEntity entity = e.getEntity();
         if (!entity.level.isClientSide()) {
            if (entity.getLevel().dimension() == BeyondDungeon.DUNGEON_LEVEL_KEY) {
               if (!e.getSource().isBypassInvul()) {
                  if (entity instanceof Player player) {
                     if (!player.isSpectator()) {
                        BlockPos deathPos = player.blockPosition();

                        for (ItemStack stack : player.getInventory().items) {
                           if (stack.is((Item)BeyondItems.AUTO_RECORDING_CRYSTAL.get())) {
                              stack.getOrCreateTag().putDouble("savedX", (double)deathPos.getX() + 0.5);
                              stack.getOrCreateTag().putDouble("savedY", (double)deathPos.getY());
                              stack.getOrCreateTag().putDouble("savedZ", (double)deathPos.getZ() + 0.5);
                              stack.getOrCreateTag().putString("savedDimension", player.level.dimension().location().toString());
                           }
                        }

                        ItemStack offhandAuto = player.getOffhandItem();
                        if (offhandAuto.is((Item)BeyondItems.AUTO_RECORDING_CRYSTAL.get())) {
                           offhandAuto.getOrCreateTag().putDouble("savedX", (double)deathPos.getX() + 0.5);
                           offhandAuto.getOrCreateTag().putDouble("savedY", (double)deathPos.getY());
                           offhandAuto.getOrCreateTag().putDouble("savedZ", (double)deathPos.getZ() + 0.5);
                           offhandAuto.getOrCreateTag().putString("savedDimension", player.level.dimension().location().toString());
                        }
                     }

                     if (entity.getY() > 113.0) {
                        revive(player, e);
                        return;
                     }

                     ItemStack offhand = player.getOffhandItem();
                     if (offhand.is((Item)BeyondItems.REVIVAL_BRACELET.get())) {
                        revive(player, e);
                        offhand.shrink(1);
                        return;
                     }

                     for (ItemStack stackx : player.getInventory().items) {
                        if (stackx.is((Item)BeyondItems.REVIVAL_BRACELET.get())) {
                           revive(player, e);
                           stackx.shrink(1);
                           break;
                        }
                     }
                  } else if (DungeonCapability.hasRevivalBracelet(entity)) {
                     revive(entity, e);
                     DungeonCapability.setRevivalBracelet(entity, false);
                  }
               }
            }
         }
      }
   }

   private static void revive(LivingEntity entity, LivingDeathEvent event) {
      entity.setHealth(entity.getMaxHealth());
      TensuraEPCapability.getFrom(entity).ifPresent(cap -> {
         double SHP = entity.getAttributeValue((Attribute)TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get());
         if (cap.getSpiritualHealth() < SHP * 0.5) {
            cap.setSpiritualHealth(SHP * 0.5);
         }
      });
      event.setCanceled(true);
      entity.invulnerableTime = 20;
      SkillHelper.removePredicateEffect(entity, effect -> true);
      float height = TensuraEffectsCapability.getHeight(entity);
      TensuraEffectsCapability.resetEverything(entity, false, false);
      if (entity instanceof Player player) {
         TensuraPlayerCapability.getFrom(player)
            .ifPresent(cap -> cap.setMagicule(Math.max(cap.getMagicule(), player.getAttributeValue((Attribute)TensuraAttributeRegistry.MAX_MAGICULE.get()) / 100.0)));
         TensuraEffectsCapability.setHeight(player, height);
         TensuraPlayerCapability.sync(player);
         TensuraEffectsCapability.sync(player);
      }

      DungeonSaveData saveData = DungeonSaveData.getOverworld(entity.getLevel());
      if (saveData != null) {
         Vec3 pos = saveData.getStartGenPos();
         entity.unRide();
         SkillHelper.moveAcrossDimensionTo(
            entity,
            pos.x() + 9.5,
            pos.y() - 23.0,
            pos.z() - 6.5,
            entity.getYRot(),
            entity.getXRot(),
            entity.getServer().m_129880_(BeyondDungeon.DUNGEON_LEVEL_KEY)
         );
      }

      if (entity instanceof ServerPlayer player) {
         TensuraNetwork.INSTANCE
            .send(PacketDistributor.PLAYER.with(() -> player), new RequestTotemDisplayPacket(((Item)BeyondItems.REVIVAL_BRACELET.get()).getDefaultInstance()));
      }

      entity.getLevel().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
      if (entity instanceof ServerPlayer player) {
         ResourceLocation advId = new ResourceLocation("trbeyond", "it_really_works");
         Advancement advancement = player.server.m_129889_().getAdvancement(advId);
         if (advancement != null) {
            AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
            if (!progress.isDone()) {
               for (String criterion : progress.getRemainingCriteria()) {
                  player.getAdvancements().award(advancement, criterion);
               }
            }
         }
      }

      TensuraParticleHelper.addServerParticlesAroundSelf(entity, ParticleTypes.TOTEM_OF_UNDYING, 1.0);
      TensuraParticleHelper.addServerParticlesAroundSelf(entity, ParticleTypes.TOTEM_OF_UNDYING, 2.0);
      TensuraParticleHelper.addServerParticlesAroundSelf(entity, ParticleTypes.FLASH, 1.0);
   }

   @SubscribeEvent
   public static void diabloDeflect(ProjectileImpactEvent event) {
      Projectile projectile = event.getProjectile();
      if (event.getRayTraceResult().getType() == Type.ENTITY) {
         EntityHitResult entityHitResult = (EntityHitResult)event.getRayTraceResult();
         if (entityHitResult.getEntity() instanceof Player player
            && player.getUseItem().getItem() instanceof DiabloClawItem diabloClawItem
            && !((List)ServerConfig.INSTANCE.diabloClawBlacklist.get()).contains(projectile.getType().builtInRegistryHolder().key().location().toString())) {
            event.setCanceled(true);
            projectile.setDeltaMovement(player.getLookAngle().normalize().scale(projectile.getDeltaMovement().length() * 1.1));
            player.startUsingItem(InteractionHand.MAIN_HAND);
            diabloClawItem.hasReflected = true;
            projectile.setOwner(player);
            if (projectile instanceof TensuraProjectile tensuraProjectile) {
               tensuraProjectile.setOwner(player);
               tensuraProjectile.setDamage(tensuraProjectile.getDamage() * 1.05F);
            }

            if (player.level.isClientSide) {
               player.level
                  .playSound(
                     player,
                     player.getX(),
                     player.getY(),
                     player.getZ(),
                     (SoundEvent)BeyondSoundEvents.diablo_claw_deflect.get(),
                     SoundSource.PLAYERS,
                     1.0F,
                     1.0F
                  );
            }
         }
      }
   }

   @SubscribeEvent
   public static void diabloCooldown(LivingEntityUseItemEvent event) {
      if (event.getEntity() instanceof Player player && event.getItem().getItem() instanceof DiabloClawItem diabloClawItem) {
         int useTime = event.getItem().getUseDuration() - event.getDuration();
         int maxTime = diabloClawItem.hasReflected ? 200 : 40;
         if (useTime >= maxTime) {
            player.stopUsingItem();
            player.getCooldowns().addCooldown(diabloClawItem, 40);
            diabloClawItem.hasReflected = false;
         }
      }
   }
}
