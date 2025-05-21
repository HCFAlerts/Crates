package me.traduciendo.crates.utils.utility.task;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskManager {
   private final JavaPlugin plugin;

   public TaskManager(JavaPlugin plugin) {
      this.plugin = plugin;
   }

   public void run(Runnable runnable) {
      this.plugin.getServer().getScheduler().runTask(this.plugin, runnable);
   }

   public void runTimer(Runnable runnable, long delay, long timer) {
      this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, runnable, delay, timer);
   }

   public void runTimer(BukkitRunnable runnable, long delay, long timer) {
      runnable.runTaskTimer(this.plugin, delay, timer);
   }

   public void runTimerAsync(Runnable runnable, long delay, long timer) {
      this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, runnable, delay, timer);
   }

   public void runTimerAsync(BukkitRunnable runnable, long delay, long timer) {
      runnable.runTaskTimerAsynchronously(this.plugin, delay, timer);
   }

   public void runLater(Runnable runnable, long delay) {
      this.plugin.getServer().getScheduler().runTaskLater(this.plugin, runnable, delay);
   }

   public void runLaterAsync(Runnable runnable, long delay) {
      try {
         this.plugin.getServer().getScheduler().runTaskLaterAsynchronously(this.plugin, runnable, delay);
      } catch (IllegalStateException var5) {
         this.plugin.getServer().getScheduler().runTaskLater(this.plugin, runnable, delay);
      } catch (IllegalArgumentException var6) {
         var6.printStackTrace();
      }

   }

   public void runTaskTimerAsynchronously(Runnable runnable, int delay) {
      try {
         this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, runnable, 20L * (long)delay, 20L * (long)delay);
      } catch (IllegalStateException var4) {
         this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, runnable, 20L * (long)delay, 20L * (long)delay);
      } catch (IllegalArgumentException var5) {
         var5.printStackTrace();
      }

   }

   public void runAsync(Runnable runnable) {
      try {
         this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, runnable);
      } catch (IllegalStateException var3) {
         this.plugin.getServer().getScheduler().runTask(this.plugin, runnable);
      } catch (IllegalArgumentException var4) {
         var4.printStackTrace();
      }

   }
}
