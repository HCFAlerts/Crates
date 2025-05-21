package me.traduciendo.crates.luckyblocks.commands.subcommands;

import me.traduciendo.crates.Crates;
import me.traduciendo.crates.utils.utility.LuckyBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand {

    private final Crates plugin;

    public GiveCommand(Crates plugin) {
        this.plugin = plugin;
    }

    public void execute(Player sender, String[] args) {
        if (!sender.hasPermission("luckyblocks.admin")) {
            sender.sendMessage("§cYou don't have permissions.");
            return;
        }

        if (args.length < 4) {
            sender.sendMessage("§cUsage: /luckyblocks give <player> <type> <amount>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        String type = args[2];
        int quantity;

        try {
            quantity = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid amount.");
            return;
        }

        FileConfiguration config = plugin.getLbConfigs(type + ".yml");

        if (config == null) {
            sender.sendMessage("§cInvalid LuckyBlock: " + type);
            return;
        }

        String nbtValue = type + "-luckyblock";

        ItemStack luckyBlockItem = LuckyBuilder.create(config, nbtValue, quantity);

        if (luckyBlockItem == null) {
            sender.sendMessage("§cInvalid LuckyBlock: " + type);
            return;
        }

        luckyBlockItem.setAmount(1);

        target.getInventory().addItem(luckyBlockItem);

        sender.sendMessage("§aGived x" + quantity + " luckyblock(s) type " + type + " for " + target.getName() + ".");
        target.sendMessage("§aReceived x" + quantity + " luckyblock(s) type " + type + ".");
    }
}
