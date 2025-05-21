package me.traduciendo.crates.airdrops.commands;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.traduciendo.crates.airdrops.rewards.Airdrop;
import me.traduciendo.crates.airdrops.rewards.Reward;
import me.traduciendo.crates.airdrops.listeners.AirdropListener;
import me.traduciendo.crates.airdrops.menus.MainEditMenu;
import me.traduciendo.crates.airdrops.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class AirdropCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("airdrop")) {
            return false;
        }

        if (!commandSender.hasPermission("airdrop.admin")) {
            commandSender.sendMessage(CC.RED + "You don't have permission to execute this command.");
            return false;
        }

        if (args.length == 0) {
            sendUsage(commandSender);
            return false;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 2) {
                sendUsage(commandSender);
                return false;
            }
            if (Airdrop.getAirdropMap().get(args[1]) != null) {
                commandSender.sendMessage(CC.RED + "This airdrop already exists");
                return false;
            }
            List<Reward> rewards = Lists.newArrayList();
            rewards.add(Reward.exampleReward());
            Airdrop airdrop = new Airdrop(
                    args[1], args[1], Material.DISPENSER, rewards,
                    Lists.newArrayList(CC.translate("&7Right click to place and get rewards")),
                    30, 1, Color.AQUA, FireworkEffect.Type.STAR,
                    true, true, "<name>", "Destroying in <countdown> seconds"
            );
            commandSender.sendMessage(CC.translate("&aCreated a new airdrop named " + airdrop.getName()));
            airdrop.save();
            return true;
        }

        if (args[0].equalsIgnoreCase("edit")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(CC.RED + "This command can only be used by players.");
                return false;
            }
            Player player = (Player) commandSender;
            new MainEditMenu().openMenu(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("delete")) {
            if (args.length < 2) {
                sendUsage(commandSender);
                return false;
            }
            if (Airdrop.getByName(args[1]) == null) {
                commandSender.sendMessage(CC.RED + "This airdrop does not exist");
                return false;
            }
            Airdrop.getByName(args[1]).delete();
            commandSender.sendMessage(CC.translate("&aSuccessfully deleted airdrop"));
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 4) {
                sendUsage(commandSender);
                return false;
            }
            if (Airdrop.getAirdropMap().get(args[1]) == null) {
                commandSender.sendMessage(CC.RED + "This airdrop does not exist");
                return false;
            }
            String targetName = args[2];
            int amount;
            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                commandSender.sendMessage(CC.RED + "Please input an integer for the amount");
                return false;
            }

            Airdrop airdrop = Airdrop.getByName(args[1]);
            ItemStack item = airdrop.toItemStack();
            item.setAmount(amount);

            if (targetName.equalsIgnoreCase("all")) {
                for (Player target : Bukkit.getOnlinePlayers()) {
                    target.getInventory().addItem(item.clone());
                }
                commandSender.sendMessage(CC.GREEN + "Gave all players x" + amount + " " + airdrop.getName() + " Airdrops");
            } else {
                Player target = Bukkit.getPlayer(targetName);
                if (target == null) {
                    commandSender.sendMessage(CC.RED + "This player is not online");
                    return false;
                }
                target.getInventory().addItem(item);
                commandSender.sendMessage(CC.GREEN + "Gave " + target.getName() + " x" + amount + " " + airdrop.getName() + " Airdrops");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("addreward")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(CC.RED + "This command can only be used by players.");
                return false;
            }
            Player player = (Player) commandSender;
            if (args.length < 3) {
                sendUsage(player);
                return false;
            }
            Airdrop airdrop = Airdrop.getByName(args[1]);
            if (airdrop == null) {
                player.sendMessage(CC.RED + "This airdrop does not exist");
                return false;
            }
            int chance;
            try {
                chance = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                player.sendMessage(CC.RED + "Please input an integer for the chances");
                return false;
            }
            if (chance > 100) {
                player.sendMessage(CC.RED + "Please input a number below 100");
                return false;
            }
            if (airdrop.getRewards().size() == 45) {
                player.sendMessage(CC.RED + airdrop.getName() + " already has the maximum possible rewards");
                return false;
            }
            ItemStack item = player.getItemInHand();
            boolean loreEnabled = item.getItemMeta().hasLore();
            List<String> lore = loreEnabled ? item.getItemMeta().getLore() : Lists.newArrayList();
            Map<Enchantment, Integer> enchantments = item.getEnchantments();
            String displayname = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : null;

            Reward reward = new Reward(displayname, item.getType(), item.getAmount(), item.getDurability(), chance, loreEnabled, lore, !enchantments.isEmpty(), enchantments);
            airdrop.getRewards().add(reward);
            airdrop.setRewards(airdrop.getRewards());
            player.sendMessage(CC.translate("&aSuccessfully added a reward to " + airdrop.getName()));
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            sendAirdropList(commandSender);
            return true;
        }

        return false;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(CC.translate("&3&m=============================="));
        sender.sendMessage(CC.translate("&b&lAirdrop Help &7(1/1)"));
        sender.sendMessage(CC.translate(""));
        sender.sendMessage(CC.translate("&f<Needed> &7┃ &f[Optional]"));
        sender.sendMessage(CC.translate(""));
        sender.sendMessage(CC.translate("&a● &fCommand works"));
        sender.sendMessage(CC.translate("&6● &fCommand contain errors"));
        sender.sendMessage(CC.translate("&c● &fCommand don't works, coming soon"));
        sender.sendMessage(CC.translate(""));
        sender.sendMessage(CC.translate("&a● &f/airdrop create <name>"));
        sender.sendMessage(CC.translate("&a● &f/airdrop delete <name>"));
        sender.sendMessage(CC.translate("&a● &f/airdrop list"));
        sender.sendMessage(CC.translate("&a● &f/airdrop give <name> <player|all> <amount>"));
        sender.sendMessage(CC.translate("&a● &f/airdrop addreward <airdrop_name> <chance>"));
        sender.sendMessage(CC.translate("&6● &f/airdrop edit [airdrop_name]"));
        sender.sendMessage(CC.translate("&3&m=============================="));
    }

    private void sendAirdropList(CommandSender sender) {
        sender.sendMessage(CC.translate("&3&m=============================="));
        sender.sendMessage(CC.translate("&b&lAirdrop List"));
        sender.sendMessage(" ");
        for (Map.Entry<String, Airdrop> entry : Airdrop.getAirdropMap().entrySet()) {
            Airdrop airdrop = entry.getValue();
            sender.sendMessage(CC.translate(airdrop.getName() + " &8- &r" + airdrop.getDisplayname()));
        }
        sender.sendMessage(" ");
        sender.sendMessage(CC.translate("&3&m=============================="));
    }
}
