package io.github.Memoires.trbeyond.handlers;

import com.github.manasmods.manascore.api.skills.event.SkillActivationEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.entity.BlackSpiderEntity;
import com.github.manasmods.tensura.entity.magic.projectile.AuraSlashProjectile;
import com.github.manasmods.tensura.event.SpiritualHurtEvent;
import com.github.manasmods.tensura.registry.battlewill.ProjectileArts;
import com.github.manasmods.tensura.util.damage.TensuraDamageSource;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.Memoires.trbeyond.TRBeyond;
import io.github.Memoires.trbeyond.block.ShrineBlock;
import io.github.Memoires.trbeyond.capability.dungeon.DungeonCapability;
import io.github.Memoires.trbeyond.capability.player.BeyondPlayerCapability;
import io.github.Memoires.trbeyond.entity.custom.BossBlackSpiderEntity;
import io.github.Memoires.trbeyond.entity.custom.ShrineEntity;
import io.github.Memoires.trbeyond.items.custom.SubspacePocketItem;
import io.github.Memoires.trbeyond.items.weapons.GorikimaruItem;
import io.github.Memoires.trbeyond.items.weapons.HakuroItem;
import io.github.Memoires.trbeyond.items.weapons.NinjatoItem;
import io.github.Memoires.trbeyond.items.weapons.scaleShieldItem;
import io.github.Memoires.trbeyond.network.worldgen.DungeonSaveData;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.network.worldgen.portal.BeyondTeleporterUpdated;
import io.github.Memoires.trbeyond.registry.effects.BeyondEffects;
import io.github.Memoires.trbeyond.registry.items.BeyondItems;
import io.github.Memoires.trbeyond.util.BeyondDamageHelper;
import io.github.Memoires.trbeyond.util.BeyondRarity;
import io.github.Memoires.trbeyond.util.BeyondSpawnUtils;
import io.github.Memoires.trbeyond.util.ChallengeType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent.Expired;
import net.minecraftforge.event.entity.player.AdvancementEvent.AdvancementEarnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(
   modid = "trbeyond"
)
public class ServerEventHandler {
   @SubscribeEvent
   public static void addCustomTrades(VillagerTradesEvent event) {
      if (event.getType() == VillagerProfession.CARTOGRAPHER) {
         Int2ObjectMap<List<ItemListing>> trades = event.getTrades();
         ItemStack stack = new ItemStack((ItemLike)BeyondItems.DUNGEON_MAP.get(), 1);
         int villagerLevel = 1;
         ((List)trades.get(villagerLevel)).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, 2), stack, 10, 0, 0.0F));
      }
   }

   @SubscribeEvent
   public static void onEffectExpire(Expired event) {
      MobEffectInstance effectInstance = event.getEffectInstance();
      if (effectInstance != null) {
         if (effectInstance.getEffect().equals(BeyondEffects.LIMITED.get())) {
            DungeonCapability.setEpLimit(event.getEntity(), 0.0);
         }
      }
   }

   @SubscribeEvent
   public static void BlockSpiritualDamage(SpiritualHurtEvent event) {
      LivingEntity target = event.getEntity();
      ItemStack stack = target.getOffhandItem();
      if (target instanceof Player player
         && stack.getItem() instanceof scaleShieldItem scaleShieldItem
         && scaleShieldItem.getRarity(stack) == BeyondRarity.GOD
         && player.isBlocking()) {
         stack.setDamageValue(stack.getDamageValue() + 1);
         event.setAmount(0.0F);
      }
   }

   @SubscribeEvent
   public static void advancementGain(AdvancementEarnEvent event) {
      Player player = event.getEntity();
      Advancement advancement = event.getAdvancement();
      if (advancement.getId().equals(new ResourceLocation("trbeyond", "e_rank_card")) && player instanceof ServerPlayer serverPlayer) {
         int p = serverPlayer.getInventory().findSlotMatchingItem(new ItemStack((ItemLike)BeyondItems.ID_CARD.get()));
         serverPlayer.getInventory().setItem(p, ((Item)BeyondItems.E_RANK_CARD.get()).getDefaultInstance());
      }
   }

   @SubscribeEvent
   public static void increasedDamageHakuroSword(LivingHurtEvent e) {
      DamageSource source = e.getSource();
      if (source.getEntity() instanceof LivingEntity entity) {
         ItemStack stackOffHand = entity.getOffhandItem();
         ItemStack stackMainHand = entity.getMainHandItem();
         if (BeyondDamageHelper.isBattleWill(source)) {
            if (stackOffHand.getItem() instanceof HakuroItem) {
               e.setAmount(e.getAmount() * 2.0F);
            }

            if (stackMainHand.getItem() instanceof HakuroItem) {
               e.setAmount(e.getAmount() * 2.0F);
            }
         }
      }
   }

   @SubscribeEvent
   public static void changeTargets(LivingChangeTargetEvent event) {
      if (event.getEntity() instanceof BlackSpiderEntity && event.getOriginalTarget() instanceof BossBlackSpiderEntity) {
         event.setCanceled(true);
      }

      if (event.getNewTarget() != null
         && event.getEntity().getPersistentData().contains("challengeMob")
         && !(event.getNewTarget() instanceof ShrineEntity)
         && event.getNewTarget().getPersistentData().contains("challengeMob")
         && event.getEntity().getPersistentData().getString("challengeMob").equals(event.getNewTarget().getPersistentData().getString("challengeMob"))) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onLivingAttack(LivingAttackEvent event) {
      if (event.getSource().getEntity() != null
         && !(event.getEntity() instanceof ShrineEntity)
         && event.getSource().getEntity().getPersistentData().contains("challengeMob")
         && event.getEntity().getPersistentData().contains("challengeMob")
         && event.getSource().getEntity().getPersistentData().getString("challengeMob").equals(event.getEntity().getPersistentData().getString("challengeMob"))) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void lockAgreCannon(SkillActivationEvent event) {
      if (event.getSkillInstance().getSkill().equals(ProjectileArts.OGRE_SWORD_CANNON.get())
         && event.getEntity().getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof GorikimaruItem) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void bypassArmor(LivingHurtEvent event) {
      DamageSource source = event.getSource();
      Entity entity = source.getEntity();
      Entity directEntity = source.getDirectEntity();
      if (entity instanceof LivingEntity living
         && directEntity instanceof AuraSlashProjectile
         && living.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof GorikimaruItem) {
         source = new TensuraDamageSource(source).setIgnoreResistance(1.0F).bypassArmor();
         event.getEntity().hurt(source, event.getAmount());
      }
   }

   @SubscribeEvent
   public static void OnEquipmentChange(LivingEquipmentChangeEvent event) {
      if (event.getTo().getItem() instanceof NinjatoItem item && event.getEntity() != null) {
         item.setUser(event.getEntity());
      }
   }

   @SubscribeEvent(
      priority = EventPriority.LOWEST
   )
   public static void onPlayerTick(PlayerTickEvent event) {
      if (event.phase != Phase.START && event.player instanceof ServerPlayer player) {
         if (player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            if (!player.getPersistentData().contains("floor")) {
               player.getPersistentData().putInt("floor", -1);
            }

            int currentFloor = BeyondSpawnUtils.GetFloorFromBlockPos(player.getOnPos());
            if (player.getPersistentData().getInt("floor") != currentFloor) {
               player.getPersistentData().putInt("floor", currentFloor);
               if (currentFloor == 1) {
                  showTitle(player, Component.translatable("trbeyond.message.floor_display.lobby"));
               } else {
                  showTitle(player, Component.translatable("trbeyond.message.floor_display", new Object[]{currentFloor}));
               }
            }

            if (player.getY() >= 116.0 && player.getY() < 130.0) {
               if (player.isCreative() || player.isSpectator()) {
                  return;
               }

               BeyondPlayerCapability.getFrom(player).ifPresent(cap -> {
                  if (!cap.isReturned() && !cap.isUsedTicket()) {
                     cap.setUsedTicket(false);
                     cap.setReturned(true);
                  }
               });
               BeyondPlayerCapability.sync(player);
            }

            if (player.getY() < 110.0) {
               tpToLobby(player);
            }
         }
      }
   }

   private static void showTitle(ServerPlayer player, Component pTitle) {
      showTitle(player, pTitle, ClientboundSetTitleTextPacket::new);
      setTimes(player, 10, 25, 10);
   }

   private static void showTitle(ServerPlayer player, Component pTitle, Function<Component, Packet<?>> packet) {
      try {
         player.connection.send(packet.apply(ComponentUtils.updateForEntity(null, pTitle, player, 0)));
      } catch (CommandSyntaxException var4) {
         TRBeyond.getLogger().error("Error showing title to player {}: {}", player.getName().getString(), var4.getMessage());
      }
   }

   private static void setTimes(ServerPlayer player, int pFade, int pStay, int pFadeOut) {
      ClientboundSetTitlesAnimationPacket clientboundsettitlesanimationpacket = new ClientboundSetTitlesAnimationPacket(pFade, pStay, pFadeOut);
      player.connection.send(clientboundsettitlesanimationpacket);
   }

   @SubscribeEvent
   public static void onDeath(LivingDeathEvent event) {
      if (event.getEntity() instanceof Player player) {
         BeyondPlayerCapability.getFrom(player).ifPresent(cap -> {
            cap.setReturned(true);
            ChallengeType currentChallenge = cap.getChallengeType();
            cap.setUsedTicket(false);
            if (currentChallenge != null && currentChallenge.getCategory() == "shrine") {
               String shrineCoords = currentChallenge.getObjective();
               String[] coordStrings = shrineCoords.trim().split("[^\\d-]+");
               if (coordStrings.length == 3) {
                  BlockPos shrinePos = new BlockPos(Integer.parseInt(coordStrings[0]), Integer.parseInt(coordStrings[1]), Integer.parseInt(coordStrings[2]));
                  BlockState shrineState = player.getLevel().getBlockState(shrinePos);
                  if (shrineState.getBlock() instanceof ShrineBlock shrineBlock) {
                     shrineBlock.failChallenge(player.getLevel(), shrinePos, player);
                  }
               }

               ChallengeEventHandler.failChallenge(player, currentChallenge);
            }
         });
         BeyondPlayerCapability.sync(player);
         if (player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
            player.getPersistentData().putLong("dungeonEnterCooldown", System.currentTimeMillis() / 1000L);
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerChangeDimension(PlayerChangedDimensionEvent e) {
      BeyondPlayerCapability.getFrom(e.getEntity())
         .ifPresent(
            cap -> {
               cap.setReturned(true);
               cap.setUsedTicket(false);
               ChallengeType currentChallenge = cap.getChallengeType();
               if (currentChallenge != null && currentChallenge.getCategory() == "shrine") {
                  String shrineCoords = currentChallenge.getObjective();
                  String[] coordStrings = shrineCoords.trim().split("[^\\d-]+");
                  if (coordStrings.length == 3) {
                     BlockPos shrinePos = new BlockPos(Integer.parseInt(coordStrings[0]), Integer.parseInt(coordStrings[1]), Integer.parseInt(coordStrings[2]));
                     BlockState shrineState = e.getEntity().getLevel().getBlockState(shrinePos);
                     if (shrineState.getBlock() instanceof ShrineBlock shrineBlock) {
                        shrineBlock.failChallenge(
                           Objects.requireNonNull(Objects.requireNonNull(e.getEntity().getLevel().getServer()).m_129880_(e.getFrom())), shrinePos, e.getEntity()
                        );
                     }
                  }

                  ChallengeEventHandler.failChallenge(e.getEntity(), currentChallenge);
               }
            }
         );
      BeyondPlayerCapability.sync(e.getEntity());
      if (e.getFrom().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
         e.getEntity().getPersistentData().putLong("dungeonEnterCooldown", System.currentTimeMillis() / 1000L);
      }
   }

   @SubscribeEvent
   public static void onClone(Clone e) {
      if (e.isWasDeath()) {
         CompoundTag originalData = e.getOriginal().getPersistentData();
         if (originalData.contains("dungeonEnterCooldown")) {
            e.getEntity().getPersistentData().putLong("dungeonEnterCooldown", originalData.getLong("dungeonEnterCooldown"));
         }

         DungeonCapability.getFrom(e.getOriginal())
            .ifPresent(oldCap -> DungeonCapability.getFrom(e.getEntity()).ifPresent(newCap -> newCap.deserializeNBT((CompoundTag)oldCap.serializeNBT())));
      }
   }

   public static void tpToLobby(Player player) {
      if (!player.isCreative() && !player.isSpectator()) {
         BeyondPlayerCapability.getFrom(player)
            .ifPresent(
               cap -> {
                  if (cap.isUsedTicket()) {
                     cap.setReturned(false);
                  }

                  if (!cap.isUsedTicket() && cap.isReturned()) {
                     if (!(player instanceof ServerPlayer serverPlayer)) {
                        return;
                     }

                     serverPlayer.unRide();
                     MinecraftServer server = serverPlayer.getServer();
                     if (server != null) {
                        ServerLevel dungeonLevel = server.m_129880_(BeyondDungeon.DUNGEON_LEVEL_KEY);
                        if (dungeonLevel != null) {
                           double lobbyX = 9.5;
                           double lobbyY = 116.0;
                           double lobbyZ = 9.5;
                           BlockPos lobbyPos = new BlockPos(9, 116, 9);
                           ChunkPos chunkPos = new ChunkPos(lobbyPos);
                           dungeonLevel.getChunkSource().addRegionTicket(TicketType.PORTAL, chunkPos, 3, lobbyPos);
                           dungeonLevel.getChunkSource().getChunk(chunkPos.x, chunkPos.z, ChunkStatus.FULL, true);
                           server.execute(
                              () -> {
                                 try {
                                    serverPlayer.teleportTo(dungeonLevel, lobbyX, lobbyY, lobbyZ, 0.0F, 0.0F);
                                    serverPlayer.setPos(lobbyX, lobbyY, lobbyZ);
                                    serverPlayer.setDeltaMovement(Vec3.ZERO);
                                    serverPlayer.hasImpulse = false;
                                    server.execute(() -> {
                                       serverPlayer.connection.teleport(lobbyX, lobbyY, lobbyZ, 0.0F, 0.0F);
                                       serverPlayer.connection.resetPosition();
                                       serverPlayer.absMoveTo(lobbyX, lobbyY, lobbyZ, 0.0F, 0.0F);
                                       server.execute(() -> BeyondPlayerCapability.sync(serverPlayer));
                                    });
                                 } catch (Exception var12x) {
                                    System.out.println("teleportTo failed, using command fallback: " + var12x.getMessage());
                                    String dimensionName = dungeonLevel.dimension().location().toString();
                                    String command = String.format(
                                       "execute in %s run tp %s %.1f %.1f %.1f", dimensionName, serverPlayer.getGameProfile().getName(), lobbyX, lobbyY, lobbyZ
                                    );
                                    server.m_129892_().performPrefixedCommand(server.m_129893_(), command);
                                    server.execute(() -> {
                                       serverPlayer.setPos(lobbyX, lobbyY, lobbyZ);
                                       serverPlayer.setDeltaMovement(Vec3.ZERO);
                                       BeyondPlayerCapability.sync(serverPlayer);
                                    });
                                 }
                              }
                           );
                        }
                     }
                  }
               }
            );
      }
   }

   @SubscribeEvent
   public static void playerDungeonTeleport(ServerTickEvent event) {
      if (event.phase != Phase.START && event.getServer().m_129783_().getGameTime() % 20L == 0L) {
         if (!DungeonSaveData.getOverworld(event.getServer().m_129783_()).getTpToDungeonPlayers().isEmpty()) {
            List<Player> tpToDungeonPlayers = new ArrayList<>();

            for (UUID uuid : DungeonSaveData.getOverworld(event.getServer().m_129783_()).getTpToDungeonPlayers()) {
               tpToDungeonPlayers.add(event.getServer().getPlayerList().getPlayer(uuid));
            }

            if (!tpToDungeonPlayers.isEmpty()
               && !BeyondTeleporterUpdated.generateDungeon(event.getServer().m_129783_(), event.getServer().m_129880_(BeyondDungeon.DUNGEON_LEVEL_KEY))) {
               for (Player player : tpToDungeonPlayers) {
                  if (player != null) {
                     DungeonSaveData.getOverworld(event.getServer().m_129783_()).removeTpToDungeonPlayer(player.getUUID());
                     DungeonCapability.sync(player);
                     ServerLevel serverLevel = event.getServer().m_129880_(BeyondDungeon.DUNGEON_LEVEL_KEY);
                     if (serverLevel != null) {
                        SkillHelper.moveAcrossDimensionTo(player, 9.5, 118.0, -23.5, 0.0F, 0.0F, serverLevel);
                     } else {
                        TRBeyond.getLogger().warn("Dungeon level is null, cannot teleport player: {}", player.getName().getString());
                     }
                  }
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerInteract(RightClickBlock event) {
      if (event.getLevel().dimension() == BeyondDungeon.DUNGEON_LEVEL_KEY
         && event.getItemStack().isEmpty()
         && event.getLevel()
            .getBlockState(event.getPos())
            .getBlock()
            .isBed(event.getLevel().getBlockState(event.getPos()), event.getLevel(), event.getPos(), event.getEntity())) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onEntityInteractSpecific(EntityInteractSpecific event) {
      if (event.getEntity().getMainHandItem().getItem() instanceof SubspacePocketItem) {
         event.setCanceled(true);
         event.setCancellationResult(InteractionResult.PASS);
      }
   }

   @SubscribeEvent
   public static void onEntityInteract(EntityInteract event) {
      ItemStack stack = event.getEntity().getMainHandItem();
      if (stack.getItem() instanceof SubspacePocketItem subspacePocketItem
         && event.getEntity().isCrouching()
         && event.getTarget() instanceof LivingEntity livingEntity) {
         event.setCanceled(true);
         event.setCancellationResult(subspacePocketItem.interactLivingEntity(stack, event.getEntity(), livingEntity, InteractionHand.MAIN_HAND));
      }
   }
}
