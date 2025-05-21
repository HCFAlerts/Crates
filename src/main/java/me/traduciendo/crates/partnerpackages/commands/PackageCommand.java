package me.traduciendo.crates.partnerpackages.commands;

import com.google.common.collect.Lists;
import me.traduciendo.crates.partnerpackages.commands.subcommands.PackageCreateCommand;
import me.traduciendo.crates.partnerpackages.commands.subcommands.PackageDeleteCommand;
import me.traduciendo.crates.partnerpackages.commands.subcommands.PackageEditCommand;
import me.traduciendo.crates.partnerpackages.commands.subcommands.PackageGiveCommand;
import me.traduciendo.crates.partnerpackages.commands.subcommands.PackageListCommand;
import me.traduciendo.crates.utils.utility.ChatUtil;
import me.traduciendo.crates.utils.utility.command.BaseCommand;
import me.traduciendo.crates.utils.utility.command.Command;
import me.traduciendo.crates.utils.utility.command.CommandArgs;
import java.util.List;

public class PackageCommand extends BaseCommand {
   public PackageCommand() {
      new PackageCreateCommand();
      new PackageDeleteCommand();
      new PackageGiveCommand();
      new PackageEditCommand();
      new PackageListCommand();
   }

   @Command(
      name = "package",
      permission = "package.admin",
      aliases = {"packages", "partnerpackage", "partnerpackages", "pp"},
      inGameOnly = false
   )
   public void onCommand(CommandArgs command) {
      List<String> message = Lists.newArrayList();
      message.add("&3&m==============================");
      message.add("&b&lPackage Help &7(1/1)");
      message.add("");
      message.add("&f<Needed> &7┃ &f[Optional]");
      message.add("");
      message.add("&a● &fCommand works");
      message.add("&6● &fCommand contain errors");
      message.add("&c● &fCommand don't works, coming soon");
      message.add("");
      message.add("&a● &f/" + command.getLabel() + " create <name>");
      message.add("&a● &f/" + command.getLabel() + " delete <name>");
      message.add("&a● &f/" + command.getLabel() + " list");
      message.add("&a● &f/" + command.getLabel() + " give <player|all> <name> <amount>");
      message.add("&6● &f/" + command.getLabel() + " edit [package_name]");
      message.add("&3&m==============================");
      message.forEach((msg) -> {
         ChatUtil.sender(command.getSender(), msg);
      });
   }
}
