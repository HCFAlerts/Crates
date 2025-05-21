package me.traduciendo.crates.partnerpackages.commands.subcommands;

import me.traduciendo.crates.partnerpackages.packages.menu.PackageMenu;
import me.traduciendo.crates.utils.utility.command.BaseCommand;
import me.traduciendo.crates.utils.utility.command.Command;
import me.traduciendo.crates.utils.utility.command.CommandArgs;

public class PackageEditCommand extends BaseCommand {
   @Command(
      name = "package.edit",
      permission = "package.admin",
      aliases = {"packages.edit", "partnerpackage.edit", "partnerpackages.edit", "pp.edit"}
   )
   public void onCommand(CommandArgs command) {
      (new PackageMenu()).openMenu(command.getPlayer());
   }
}
