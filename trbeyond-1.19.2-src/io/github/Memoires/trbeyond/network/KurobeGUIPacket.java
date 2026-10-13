package io.github.Memoires.trbeyond.network;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.tensura.ability.SkillUtils;
import io.github.Memoires.trbeyond.ability.battlewill.utility.TRBeyondSkills;
import io.github.Memoires.trbeyond.client.screen.KurobeShopMenu;
import io.github.Memoires.trbeyond.client.screen.ResearcherEnchantmentShopMenu;
import io.netty.buffer.Unpooled;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.NetworkEvent.Context;

public class KurobeGUIPacket {
   private final int buttonID;

   public KurobeGUIPacket(int buttonID) {
      this.buttonID = buttonID;
   }

   public static KurobeGUIPacket decode(FriendlyByteBuf buf) {
      return new KurobeGUIPacket(buf.readInt());
   }

   public static void encode(KurobeGUIPacket pkt, FriendlyByteBuf buf) {
      buf.writeInt(pkt.buttonID);
   }

   public static void handle(KurobeGUIPacket msg, Supplier<Context> ctx) {
      ctx.get()
         .enqueueWork(
            () -> {
               ServerPlayer player = ctx.get().getSender();
               if (player != null) {
                  switch (msg.buttonID) {
                     case 0:
                        SkillAPI.getSkillsFrom(player).learnSkill((ManasSkill)TRBeyondSkills.KROUBE_ENChANT_SHOP.get());
                        NetworkHooks.openScreen(
                           player,
                           new SimpleMenuProvider(
                              (pContainerId, pPlayerInventory, pPlayer) -> new ResearcherEnchantmentShopMenu(
                                    pContainerId,
                                    pPlayerInventory,
                                    ContainerLevelAccess.create(player.level, player.blockPosition()),
                                    (ManasSkill)TRBeyondSkills.KROUBE_ENChANT_SHOP.get()
                                 ),
                              Component.empty()
                           ),
                           buf -> buf.writeResourceLocation(SkillUtils.getSkillId((ManasSkill)TRBeyondSkills.KROUBE_ENChANT_SHOP.get()))
                        );
                        break;
                     case 1:
                        NetworkHooks.openScreen(
                           player,
                           new SimpleMenuProvider(
                              (windowId, inv, p) -> new KurobeShopMenu(windowId, inv, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(player.blockPosition())),
                              Component.translatable("container.trbeyond.kurobe_shop")
                           ),
                           buf -> buf.writeBlockPos(player.blockPosition())
                        );
                  }
               }
            }
         );
      ctx.get().setPacketHandled(true);
   }
}
