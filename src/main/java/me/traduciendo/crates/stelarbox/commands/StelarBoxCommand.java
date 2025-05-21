package me.traduciendo.crates.stelarbox.commands;

import me.traduciendo.crates.Crates;
import me.traduciendo.crates.stelarbox.utils.CC;
import me.traduciendo.crates.stelarbox.utils.Plugin;
import me.traduciendo.crates.stelarbox.utils.Stacks;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class StelarBoxCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] strings) {
        final String command = cmd.getName();
        if (!(strings.length >= 1)) {
            Arrays.asList(
              ("&3&m==============================")
            , ("&b&lStelarBox Help &7(1/1)")
            , ("")
            , ("&f<Needed> &7┃ &f[Optional]")
            , ("")
            , ("&a● &fCommand works")
            , ("&6● &fCommand contain errors")
            , ("&c● &fCommand don't works, coming soon")
            , ("")
            , ("&a● &f/stelarbox give <player|all> <amount>")
            , ("&a● &f/stelarbox loot")
            , ("&a● &f/stelarbox reload")
            , ("&3&m==============================")).forEach(m -> sender.sendMessage(CC.translate(m)));
            return true;
        } else if ("give".equalsIgnoreCase(strings[0])) {
            if (!(sender.hasPermission("stelarbox.give") || sender.hasPermission("stelarbox.admin"))) {
                sender.sendMessage(CC.translate(Crates.getInstance().getConfig().getString("MESSAGES.NO-PERMS")));
                return true;
            }
            if (strings.length != 3) {
                sender.sendMessage(CC.translate("&cUsage: /" + command + " give <player|all> <amount>"));
                return true;
            }
            final String playerarg = strings[1];
            if (!playerarg.equalsIgnoreCase("all")) {
                Player target = Bukkit.getPlayer(playerarg);
                if (target == null) {
                    sender.sendMessage(CC.translate("&7[&bStelarBox&7] &cPlayer not found."));
                    return true;
                } else if (!target.isOnline()) {
                    sender.sendMessage(CC.translate("&7[&bStelarBox&7] &c" + target.getName() + " inst online..."));
                    return true;
                }
                if (!Plugin.isInt(strings[2])) {
                    sender.sendMessage(CC.translate("&7[&bStelarBox&7] &cInvalid amount."));
                    return true;
                }
                final int amount = Integer.parseInt(strings[2]);
                if (Plugin.isFull(target)) {
                    target.getWorld().dropItem(target.getLocation(), Stacks.getLootboxItem(amount));
                    return true;
                }
                target.getInventory().addItem(Stacks.getLootboxItem(amount));
                return true;
            }
            if (!Plugin.isInt(strings[2])) {
                sender.sendMessage(CC.translate("&7[&bStelarBox&7] &cInvalid amount."));
                return true;
            }
            final int amount = Integer.parseInt(strings[2]);
            for (Player target : Bukkit.getOnlinePlayers()) {
                if (Plugin.isFull(target)) {
                    target.getWorld().dropItem(target.getLocation(), Stacks.getLootboxItem(amount));
                    return true;
                }
                target.getInventory().addItem(Stacks.getLootboxItem(amount));
            }
        } else if ("loot".equalsIgnoreCase(strings[0])) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.translate(Crates.getInstance().getConfig().getString("MESSAGES.CONSOLE")));
                return true;
            }
            ((Player) sender).openInventory(Stacks.getLootInventory((sender.hasPermission("stelarbox.loot") || sender.hasPermission("stelarbox.admin"))));
        } else if ("reload".equalsIgnoreCase(strings[0])) {
            if (!(sender.hasPermission("stelarbox.reload") || sender.hasPermission("stelarbox.admin"))) {
                sender.sendMessage(CC.translate(Crates.getInstance().getConfig().getString("MESSAGES.NO-PERMS")));
                return true;
            }
            try {
                reload();
            } catch (Error e) {
                sender.sendMessage(CC.translate("&7[&bStelarBox&7] &cFound a error in the config.yml"));
                return true;
            }
            sender.sendMessage(CC.translate("&7[&bStelarBox&7] &aReloaded successfully"));
        } else {
            sender.sendMessage(CC.translate("&7[&bStelarBox&7] &cThe subcommand " + strings[0] + " was not founded, please use /" + command));
        }
        return false;
    }

    private void reload() {
        Crates.getInstance().getMainConfig().reload();
        Crates.getInstance().getData().reload();
    }
}
