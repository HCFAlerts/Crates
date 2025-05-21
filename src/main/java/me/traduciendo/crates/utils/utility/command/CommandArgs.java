package me.traduciendo.crates.utils.utility.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandArgs {
   private final CommandSender sender;
   private final org.bukkit.command.Command command;
   private final String label;
   private final String[] args;

   protected CommandArgs(CommandSender sender, org.bukkit.command.Command command, String label, String[] args, int subCommand) {
      String[] modArgs = new String[args.length - subCommand];
      if (args.length - subCommand >= 0) {
         System.arraycopy(args, 0 + subCommand, modArgs, 0, args.length - subCommand);
      }

      StringBuffer buffer = new StringBuffer();
      buffer.append(label);

      for(int x = 0; x < subCommand; ++x) {
         buffer.append("." + args[x]);
      }

      String cmdLabel = buffer.toString();
      this.sender = sender;
      this.command = command;
      this.label = cmdLabel;
      this.args = modArgs;
   }

   public String getArgs(int index) {
      return this.args[index];
   }

   public int length() {
      return this.args.length;
   }

   public boolean isPlayer() {
      return this.sender instanceof Player;
   }

   public Player getPlayer() {
      return this.sender instanceof Player ? (Player)this.sender : null;
   }

   public CommandSender getSender() {
      return this.sender;
   }

   public org.bukkit.command.Command getCommand() {
      return this.command;
   }

   public String getLabel() {
      return this.label;
   }

   public String[] getArgs() {
      return this.args;
   }
}
