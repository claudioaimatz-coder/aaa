package io.github.Memoires.trbeyond.util;

import java.util.PriorityQueue;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber
public class TaskScheduler {
   private static final PriorityQueue<TaskScheduler.ScheduledTask> TASK_QUEUE = new PriorityQueue<>();
   private static long CurrentTick = -1L;

   public static void scheduleTask(TaskScheduler.ScheduledTask task) {
      TASK_QUEUE.add(task);
   }

   @SubscribeEvent
   public static void onServerTick(ServerTickEvent event) {
      if (event.phase == Phase.END) {
         if (CurrentTick > 0L) {
            CurrentTick++;
         } else {
            CurrentTick = (long)event.getServer().m_129921_();
         }

         while (!TASK_QUEUE.isEmpty() && TASK_QUEUE.peek().runTicks <= CurrentTick) {
            TaskScheduler.ScheduledTask task = TASK_QUEUE.poll();
            task.run();
         }
      }
   }

   public static void scheduleRepeatingTask(final int ticks, final TaskScheduler.RepeatingTask task) {
      scheduleTask(new TaskScheduler.ScheduledTask(ticks, new Runnable() {
         @Override
         public void run() {
            task.run();
            TaskScheduler.scheduleRepeatingTask(ticks, task);
         }
      }));
   }

   public interface RepeatingTask extends Runnable {
   }

   public static class ScheduledTask implements Comparable<TaskScheduler.ScheduledTask> {
      public long runTicks;
      public Runnable task;

      public ScheduledTask(int ticks, Runnable task) {
         this.runTicks = (long)ticks + TaskScheduler.CurrentTick;
         this.task = task;
      }

      public int compareTo(@NotNull TaskScheduler.ScheduledTask o) {
         return Long.compare(this.runTicks, o.runTicks);
      }

      public void run() {
         this.task.run();
      }
   }
}
