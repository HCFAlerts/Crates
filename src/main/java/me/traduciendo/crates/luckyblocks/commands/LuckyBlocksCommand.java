package me.traduciendo.crates.luckyblocks.commands;

import me.traduciendo.crates.Crates;
import me.traduciendo.crates.airdrops.util.chat.CC;
import me.traduciendo.crates.luckyblocks.commands.subcommands.GiveCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LuckyBlocksCommand implements CommandExecutor {

    private final GiveCommand giveCommand;

    public LuckyBlocksCommand(Crates plugin) {
        this.giveCommand = new GiveCommand(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(CC.translate("&3&m=============================="));
            sender.sendMessage(CC.translate("&b&lLuckyBlocks Help &7(1/1)"));
            sender.sendMessage(CC.translate(""));
            sender.sendMessage(CC.translate("&f<Needed> &7┃ &f[Optional]"));
            sender.sendMessage(CC.translate(""));
            sender.sendMessage(CC.translate("&a● &fCommand works"));
            sender.sendMessage(CC.translate("&6● &fCommand contain errors"));
            sender.sendMessage(CC.translate("&c● &fCommand don't works, coming soon"));
            sender.sendMessage(CC.translate(""));
            sender.sendMessage(CC.translate("&a● &f/luckyblocks give <player> <name> <amount>"));
            sender.sendMessage(CC.translate("&c● &f/luckyblocks edit [luckyblock_name]"));
            sender.sendMessage(CC.translate("&3&m=============================="));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command is only for players.");
            return true;
        }

        Player player = (Player) sender;

        switch (args[0].toLowerCase()) {
            case "give":
                giveCommand.execute(player, args);
                break;
            default:
                sender.sendMessage("§c/luckyblocks give <player> <type> <amount>");
                break;
        }
        return true;
    }
}
