package io.github.Memoires.trbeyond.util;

import java.util.HashMap;
import java.util.Map;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class TimerUtils {
   private static final Map<String, TimerUtils.Timer> timers = new HashMap<>();

   public static TimerUtils.Timer getOrCreateTimer(String timerName) {
      return timers.computeIfAbsent(timerName, k -> new TimerUtils.Timer(timerName));
   }

   @SubscribeEvent
   public static void onTick(ServerTickEvent event) {
      if (event.phase == Phase.START) {
         for (TimerUtils.Timer timer : timers.values()) {
            timer.update();
         }
      }
   }

   public static class Timer {
      private int ticksElapsed = 0;
      private int intervalTicks = 60;
      public boolean active = false;
      private Runnable action;

      public Timer(String name) {
      }

      public void start(int intervalTicks, Runnable action) {
         this.intervalTicks = intervalTicks;
         this.ticksElapsed = 0;
         this.active = true;
         this.action = action;
      }

      public void reset() {
         this.ticksElapsed = 0;
      }

      public void stop() {
         this.active = false;
      }

      public void update() {
         if (this.active) {
            this.ticksElapsed++;
            if (this.ticksElapsed >= this.intervalTicks) {
               if (this.action != null) {
                  this.action.run();
               }

               this.reset();
            }
         }
      }

      public int getTicks() {
         return this.ticksElapsed;
      }

      public boolean isActive() {
         return this.active;
      }
   }
}
