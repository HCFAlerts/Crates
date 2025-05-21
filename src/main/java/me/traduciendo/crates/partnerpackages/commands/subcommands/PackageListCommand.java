package me.traduciendo.crates.partnerpackages.commands.subcommands;

import me.traduciendo.crates.Crates;
import me.traduciendo.crates.partnerpackages.packages.Package;
import me.traduciendo.crates.partnerpackages.packages.PackageManager;
import me.traduciendo.crates.utils.utility.ChatUtil;
import me.traduciendo.crates.utils.utility.command.BaseCommand;
import me.traduciendo.crates.utils.utility.command.Command;
import me.traduciendo.crates.utils.utility.command.CommandArgs;
import java.util.Iterator;

public class PackageListCommand extends BaseCommand {
   private final PackageManager packageManager = Crates.get().getPackageManager();

   @Command(
      name = "package.list",
      permission = "package.admin",
      aliases = {"packages.list", "partnerpackage.list", "partnerpackages.list", "pp.list"},
      inGameOnly = false
   )
   public void onCommand(CommandArgs command) {
      ChatUtil.sender(command.getSender(), ChatUtil.NORMAL_LINE);
      ChatUtil.sender(command.getSender(), "&b&lPackages &7(&f" + this.packageManager.getPackages().size() + "&7)");
      ChatUtil.sender(command.getSender(), "");
      Iterator var2 = this.packageManager.getPackages().iterator();

      while(var2.hasNext()) {
         Package pack = (Package)var2.next();
         ChatUtil.sender(command.getSender(), "&7▶ &f" + pack.getName() + " Package");
      }

      ChatUtil.sender(command.getSender(), ChatUtil.NORMAL_LINE);
   }
}
