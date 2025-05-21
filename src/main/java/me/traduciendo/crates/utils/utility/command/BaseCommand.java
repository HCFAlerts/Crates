package me.traduciendo.crates.utils.utility.command;

import java.util.List;

public abstract class BaseCommand {
   public BaseCommand() {
      CommandManager.getInstance().registerCommands(this, (List)null);
   }

   public abstract void onCommand(CommandArgs var1);
}
