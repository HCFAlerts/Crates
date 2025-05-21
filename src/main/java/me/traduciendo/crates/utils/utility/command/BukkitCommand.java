package me.traduciendo.crates.utils.utility.command;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class BukkitCommand extends org.bukkit.command.Command {
   private final Plugin ownerPlugin;
   private final CommandExecutor executor;

   protected BukkitCommand(String label, CommandExecutor executor, Plugin owner) {
      super(label);
      this.executor = executor;
      this.ownerPlugin = owner;
      this.usageMessage = "";
   }

   public boolean execute(CommandSender sender, String commandLabel, String[] args) {
      if (!this.ownerPlugin.isEnabled()) {
         return false;
      } else if (!this.testPermission(sender)) {
         return true;
      } else {
         boolean success;
         try {
            success = this.executor.onCommand(sender, this, commandLabel, args);
         } catch (Throwable var9) {
            throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + this.ownerPlugin.getDescription().getFullName(), var9);
         }

         if (!success && this.usageMessage.length() > 0) {
            String[] var5 = this.usageMessage.replace("<command>", commandLabel).split("\n");
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String line = var5[var7];
               sender.sendMessage(line);
            }
         }

         return success;
      }
   }
}
