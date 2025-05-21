package me.traduciendo.crates.partnerpackages.commands.subcommands;

import me.traduciendo.crates.Crates;
import me.traduciendo.crates.partnerpackages.packages.Package;
import me.traduciendo.crates.partnerpackages.packages.PackageManager;
import me.traduciendo.crates.utils.utility.ChatUtil;
import me.traduciendo.crates.utils.utility.JavaUtil;
import me.traduciendo.crates.utils.utility.command.BaseCommand;
import me.traduciendo.crates.utils.utility.command.Command;
import me.traduciendo.crates.utils.utility.command.CommandArgs;
import me.traduciendo.crates.utils.utility.file.FileConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PackageGiveCommand extends BaseCommand {
   private final FileConfig mainConfig = Crates.get().getMainConfig();
   private final PackageManager packageManager = Crates.get().getPackageManager();

   @Command(
      name = "package.give",
      permission = "package.admin",
      aliases = {"packages.give", "partnerpackage.give", "partnerpackages.give", "pp.give"},
      inGameOnly = false
   )
   public void onCommand(CommandArgs command) {
      CommandSender sender = command.getSender();
      String[] args = command.getArgs();
      String label = command.getLabel().replace(".give", "");
      if (args.length < 3) {
         sender.sendMessage(ChatUtil.translate("&cUsage: /" + label + " give <player|all> <name> <amount>"));
      } else if (args[0].equalsIgnoreCase("all")) {
         String packageName = ChatUtil.capitalize(args[1]);
         if (!this.packageManager.isPackageExist(packageName)) {
            ChatUtil.sender(sender, this.mainConfig.getString("PACKAGE-NOT-EXIST").replace("%PACKAGE%", packageName));
         } else {
            Integer amount = JavaUtil.tryParseInt(args[2]);
            if (amount == null) {
               ChatUtil.sender(sender, "&7[&bPackages&7] &cInvalid amount.");
            } else if (amount <= 0) {
               ChatUtil.sender(sender, "&7[&bPackages&7] &cInvalid amount.");
            } else {
               Package pack = this.packageManager.getPackageByName(packageName);
               Bukkit.getServer().getOnlinePlayers().forEach((online) -> {
                  online.getInventory().addItem(new ItemStack[]{pack.getPackageItem(amount)});
                  ChatUtil.message(online, this.mainConfig.getString("PACKAGE-GIVE-ALL").replace("%AMOUNT%", amount.toString()).replace("%PACKAGE%", pack.getDisplayName()));
               });
            }
         }
      } else {
         Player target = Bukkit.getPlayer(args[0]);
         if (target == null) {
            ChatUtil.sender(sender, "&7[&bPackages&7] &cPlayer not found.");
         } else {
            String packageName = ChatUtil.capitalize(args[1]);
            if (!this.packageManager.isPackageExist(packageName)) {
               ChatUtil.sender(sender, this.mainConfig.getString("PACKAGE-NOT-EXIST").replace("%PACKAGE%", packageName));
            } else {
               Integer amount = JavaUtil.tryParseInt(args[2]);
               if (amount == null) {
                  ChatUtil.sender(sender, "&7[&bPackages&7] &cInvalid amount.");
               } else if (amount <= 0) {
                  ChatUtil.sender(sender, "&7[&bPackages&7] &cInvalid amount.");
               } else {
                  Package pack = this.packageManager.getPackageByName(packageName);
                  target.getInventory().addItem(new ItemStack[]{pack.getPackageItem(amount)});
                  ChatUtil.sender(sender, this.mainConfig.getString("PACKAGE-RECEIVE").replace("%AMOUNT%", amount.toString()).replace("%PACKAGE%", pack.getDisplayName()).replace("%TARGET%", target.getName()));
                  ChatUtil.message(target, this.mainConfig.getString("PACKAGE-GIVE").replace("%AMOUNT%", amount.toString()).replace("%PACKAGE%", pack.getDisplayName()));
               }
            }
         }
      }
   }
}
