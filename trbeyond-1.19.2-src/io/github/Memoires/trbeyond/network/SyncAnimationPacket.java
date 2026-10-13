package io.github.Memoires.trbeyond.network;

import io.github.Memoires.trbeyond.items.effects.AnimatedEffectWeapon;
import io.github.Memoires.trbeyond.util.PlayerAnimationHelper;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent.Context;

public class SyncAnimationPacket {
   int playerId;
   String animationName;
   boolean playAnimation;

   public SyncAnimationPacket(int playerId, String animationName) {
      this.playerId = playerId;
      this.animationName = animationName;
      this.playAnimation = true;
   }

   public SyncAnimationPacket(int playerId, boolean playAnimation) {
      this.playerId = playerId;
      this.animationName = "default";
      this.playAnimation = playAnimation;
   }

   public SyncAnimationPacket(int playerId, String animationName, boolean playAnimation) {
      this.playerId = playerId;
      this.animationName = animationName;
      this.playAnimation = playAnimation;
   }

   public SyncAnimationPacket(int playerId) {
      this.playerId = playerId;
      this.animationName = "default";
      this.playAnimation = true;
   }

   public SyncAnimationPacket(FriendlyByteBuf buf) {
      this.playerId = buf.readInt();
      this.animationName = buf.readUtf();
      this.playAnimation = buf.readBoolean();
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeInt(this.playerId);
      buf.writeUtf(this.animationName);
      buf.writeBoolean(this.playAnimation);
   }

   public void handle(Supplier<Context> ctx) {
      ctx.get().enqueueWork(() -> {
         if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            AbstractClientPlayer player = (AbstractClientPlayer)Minecraft.getInstance().level.getEntity(this.playerId);
            if (player != null) {
               if (player.getMainHandItem().getItem() instanceof AnimatedEffectWeapon weapon) {
                  if (this.playAnimation) {
                     if (this.animationName.equals("default")) {
                        weapon.playAnimationClientOnly(player);
                     } else {
                        weapon.playAnimationClientOnly(player, this.animationName);
                     }
                  } else {
                     weapon.stopAnimationClientOnly(player);
                  }
               } else {
                  PlayerAnimationHelper.playAnimationClientOnly(player, this.animationName);
               }
            }
         } else {
            BeyondNetwork.toAll(new SyncAnimationPacket(this.playerId, this.animationName, this.playAnimation));
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
