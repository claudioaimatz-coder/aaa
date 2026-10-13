package io.github.Memoires.trbeyond.util;

import net.minecraft.ChatFormatting;

public class RainbowUtil {
   private static final ChatFormatting[] RAINBOW_COLORS = new ChatFormatting[]{
      ChatFormatting.RED,
      ChatFormatting.GOLD,
      ChatFormatting.YELLOW,
      ChatFormatting.GREEN,
      ChatFormatting.AQUA,
      ChatFormatting.BLUE,
      ChatFormatting.DARK_PURPLE
   };

   public static String getRainbowText(String text) {
      StringBuilder result = new StringBuilder();
      int colorIndex = 0;

      for (char c : text.toCharArray()) {
         result.append("§").append(RAINBOW_COLORS[colorIndex % RAINBOW_COLORS.length].getChar()).append(c);
         colorIndex++;
      }

      return result.toString();
   }
}
