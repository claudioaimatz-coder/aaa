package io.github.Memoires.trbeyond.client.gui;

import io.github.Memoires.trbeyond.handlers.ChallengeEventHandler;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ChallengeOverlay {
   public static final IGuiOverlay CHALLENGE_OVERLAY = (gui, poseStack, partialTick, width, height) -> {
      Player player = gui.getMinecraft().player;
      if (player != null && player.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
         ChallengeEventHandler.getPlayerCapability(player)
            .ifPresent(
               capability -> {
                  if (capability.getChallengeType() != null) {
                     int y = 10;
                     int timeLimit = capability.getChallengeType().getTimeLimit();
                     int timeLeft = 0;
                     if (timeLimit > 0) {
                        timeLeft = timeLimit - capability.getChallengeType().getTime();
                        if (timeLeft < 0) {
                           timeLeft = 0;
                        }
                     }

                     String challengeName = capability.getChallengeType().getName().contains("elite")
                        ? Component.translatable("trbeyond.challenge.kill_elite").getString()
                        : Component.translatable("trbeyond.challenge." + capability.getChallengeType().getName()).getString();
                     String countText = String.format("Count: %d/%d", capability.getChallengeType().getCountDone(), capability.getChallengeType().getCount());
                     String timeText = "Time left: %d seconds";
                     if (capability.getChallengeType().getWaveDelay() != -1) {
                        int waveDelay = capability.getChallengeType().getWaveDelay();
                        countText = String.format("Wave: %d/%d", capability.getChallengeType().getCountDone(), capability.getChallengeType().getCount());
                        if (capability.getChallengeType().getName().contains("wave")) {
                           timeText = "Next wave in: %d seconds";
                           timeLeft = ((timeLeft - timeLimit) % waveDelay + waveDelay) % waveDelay;
                        }
                     }

                     String itemKey = capability.getChallengeType().getReward().replace(":", ".");
                     String rewardText = String.format(
                        "Reward: %s x%d", Component.translatable("item." + itemKey).getString(), capability.getChallengeType().getRewardAmount()
                     );
                     if (capability.getChallengeType().getCategory().contains("shrine")) {
                        rewardText = "";
                     }

                     if (capability.getChallengeType().getName().equals("kill_elite")) {
                        countText = Component.translatable(
                              "trbeyond.challenge.target_entity",
                              new Object[]{
                                 Component.translatable(
                                    String.format(
                                       "entity.%s.%s",
                                       capability.getChallengeType().getObjective().substring(0, capability.getChallengeType().getObjective().indexOf(":")),
                                       capability.getChallengeType().getObjective().substring(capability.getChallengeType().getObjective().indexOf(":") + 1)
                                    )
                                 )
                              }
                           )
                           .getString();
                     }

                     int x = width - gui.getFont().width(challengeName) - 10;
                     x = Math.min(x, width - gui.getFont().width(countText) - 10);
                     x = Math.min(x, width - gui.getFont().width(rewardText) - 10);
                     if (capability.getChallengeType().getTimeLimit() > 0) {
                        x = Math.min(x, width - gui.getFont().width(String.format(timeText, capability.getChallengeType().getTimeLimit())) - 10);
                     }

                     gui.getFont().draw(poseStack, challengeName, (float)x, (float)y, 4031942);
                     gui.getFont().draw(poseStack, countText, (float)x, (float)(y + 10), 4031942);
                     if (rewardText != null && !rewardText.isEmpty()) {
                        gui.getFont().draw(poseStack, rewardText, (float)x, (float)(y + 20), 4031942);
                     } else {
                        y -= 10;
                     }

                     if (timeLimit > 0) {
                        gui.getFont().draw(poseStack, String.format(timeText, timeLeft), (float)x, (float)(y + 30), 4031942);
                     }
                  }
               }
            );
      }
   };
}
