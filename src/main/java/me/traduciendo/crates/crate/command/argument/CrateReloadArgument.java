package me.traduciendo.crates.crate.command.argument;

import me.traduciendo.crates.Crates;
import me.traduciendo.crates.utils.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.traduciendo.crates.utils.util.command.CommandArgument;

public class CrateReloadArgument extends CommandArgument {

    public CrateReloadArgument() {
        super("reload", "Reload the main config yml.", "crate.command.reload");
    }

    @Override
    public String getUsage(String label) {
        return "/" + label + ' ' + this.getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("crate.command.reload")) {
            ChatUtil.sendMessage(sender, "&cYou don't have permission to execute this command.");
            return false;
        }

        Crates.getInstance().getMainConfig().reload();
        ChatUtil.sendMessage(sender, "&7[&bCrates&7] &aThe configuration has been successfully reloaded.");

        return true;
    }
}

