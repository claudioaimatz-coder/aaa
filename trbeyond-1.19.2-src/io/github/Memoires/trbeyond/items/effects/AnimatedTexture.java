package io.github.Memoires.trbeyond.items.effects;

import net.minecraft.resources.ResourceLocation;

public class AnimatedTexture {
   private String texture_path;
   private final String MOD_ID;
   private final int num_frames;
   private int cursor = 0;
   private final int ticks_per_frame;
   private int current_tick;

   public AnimatedTexture(String MOD_ID, String path, int number_of_frames, int ticks_per_frame) {
      this.texture_path = path;
      this.MOD_ID = MOD_ID;
      this.num_frames = number_of_frames;
      this.ticks_per_frame = ticks_per_frame;
      this.current_tick = ticks_per_frame;
   }

   public void tick() {
      this.current_tick = this.ticks_per_frame;
      this.cursor = ++this.cursor % this.num_frames;
      this.texture_path = this.texture_path.replaceAll("(\\d+)\\.png", this.cursor + ".png");
   }

   public ResourceLocation getTexture() {
      return new ResourceLocation(this.MOD_ID, this.texture_path);
   }
}
