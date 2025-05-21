package me.traduciendo.crates.partnerpackages.commands.subcommands;

import me.traduciendo.crates.Crates;
import me.traduciendo.crates.partnerpackages.packages.PackageManager;
import me.traduciendo.crates.utils.utility.ChatUtil;
import me.traduciendo.crates.utils.utility.command.BaseCommand;
import me.traduciendo.crates.utils.utility.command.Command;
import me.traduciendo.crates.utils.utility.command.CommandArgs;
import me.traduciendo.crates.utils.utility.file.FileConfig;
import org.bukkit.command.CommandSender;

public class PackageDeleteCommand extends BaseCommand {
   private final FileConfig mainConfig = Crates.get().getMainConfig();
   private final PackageManager packageManager = Crates.get().getPackageManager();

   @Command(
      name = "package.delete",
      permission = "package.admin",
      aliases = {"packages.delete", "partnerpackage.delete", "partnerpackages.delete", "pp.delete"},
      inGameOnly = false
   )
   public void onCommand(CommandArgs command) {
      CommandSender sender = command.getSender();
      String[] args = command.getArgs();
      String label = command.getLabel().replace(".delete", "");
      if (args.length < 1) {
         ChatUtil.sender(sender, "&cUsage: /" + label + " delete <name>");
      } else {
         String packageName = ChatUtil.capitalize(args[0]);
         if (!this.packageManager.isPackageExist(packageName)) {
            ChatUtil.sender(sender, this.mainConfig.getString("PACKAGE-NOT-EXIST").replace("%PACKAGE%", packageName));
         } else {
            this.packageManager.deletePackage(packageName);
            ChatUtil.sender(sender, this.mainConfig.getString("PACKAGE-DELETE").replace("%PACKAGE%", packageName));
         }
      }
   }
}
