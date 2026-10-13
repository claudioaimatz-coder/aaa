package io.github.Memoires.trbeyond.handlers.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggingIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.FORGE,
   value = {Dist.CLIENT}
)
public class ClientJoinEventHandler {
   @SubscribeEvent
   public static void onPlayerJoin(LoggingIn event) {
      Minecraft mc = Minecraft.getInstance();
      GameProfile playerProfile = mc.getUser().getGameProfile();
      Component base = Component.translatable("trbeyond.join_message", new Object[]{playerProfile.getName()});
      Component discordLink = Component.literal("[Discord Link]")
         .withStyle(
            Style.EMPTY.withColor(ChatFormatting.DARK_AQUA).withUnderlined(true).withClickEvent(new ClickEvent(Action.OPEN_URL, "https://discord.gg/XJKhmVrRsV"))
         );
      event.getPlayer().displayClientMessage(base.copy().append(discordLink), false);
   }
}
