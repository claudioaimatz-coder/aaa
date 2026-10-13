package io.github.Memoires.trbeyond.handlers;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerLevel;

public class TaskHandler {
   private static final List<TaskHandler.DelayedTask> tasks = new ArrayList<>();

   public static void queueTask(ServerLevel level, int delayTicks, Runnable task) {
      tasks.add(new TaskHandler.DelayedTask(level, delayTicks, task));
   }

   public static void tick(ServerLevel level) {
      tasks.removeIf(t -> {
         if (t.level != level) {
            return false;
         } else {
            t.delay--;
            if (t.delay <= 0) {
               t.task.run();
               return true;
            } else {
               return false;
            }
         }
      });
   }

   private static class DelayedTask {
      final ServerLevel level;
      int delay;
      final Runnable task;

      DelayedTask(ServerLevel level, int delay, Runnable task) {
         this.level = level;
         this.delay = delay;
         this.task = task;
      }
   }
}
