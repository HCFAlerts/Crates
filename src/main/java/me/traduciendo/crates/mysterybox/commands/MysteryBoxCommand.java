package me.traduciendo.crates.mysterybox.commands;

import me.traduciendo.crates.Crates;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import me.traduciendo.crates.utils.util.Builder;
import me.traduciendo.crates.utils.util.CC;

public class MysteryBoxCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
        if (!sender.hasPermission("mysterybox.admin")) {
            sender.sendMessage(CC.Color("&cYou don't have sufficient permissions"));
            return true;
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give")) {
                if (args[1].equalsIgnoreCase("all")) {
                    int amount;
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(CC.Color("&cInvalid amount."));
                        return true;
                    }

                    ItemStack box = Builder.nameItem(Material.valueOf(Crates.getInstance().getConfig().getString("OPTIONS.LOOTBOX-ITEM.MATERIAL")),
                            Crates.getInstance().getConfig().getString("OPTIONS.LOOTBOX-ITEM.DISPLAY-NAME"),
                            (short) Crates.getInstance().getConfig().getInt("OPTIONS.LOOTBOX-ITEM.ITEM-META"),
                            amount, Crates.getInstance().getConfig().getStringList("OPTIONS.LOOTBOX-ITEM.LORES"));

                    for (Player target : Bukkit.getOnlinePlayers()) {
                        if (target.getInventory().firstEmpty() == -1) {
                            target.getWorld().dropItemNaturally(target.getLocation(), box);
                            Bukkit.broadcast(CC.Color("&7[&bMysteryBox&7] &c" + target.getName() + " &fobtained a MysteryBox. His inventory was full, so the MysteryBox dropped out."), "mysterybox.admin");
                            target.sendMessage(CC.Color("&7[&bMysteryBox&7] &fYou have received &lx" + amount + "&f Mystery Box" + (amount == 1 ? "" : "es") + "&f, but your inventory was full, so it dropped outside."));
                        } else {
                            target.getInventory().addItem(box);
                            target.sendMessage(CC.Color("&7[&bMysteryBox&7] &fYou have received &lx" + amount + "&f Mystery Box" + (amount == 1 ? "" : "es") + "&f."));
                        }
                    }
                } else {
                    if (Bukkit.getPlayer(args[1]) == null) {
                        sender.sendMessage(CC.Color("&7[&bMysteryBox&7] &cPlayer not found."));
                        return true;
                    }

                    int amount;
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(CC.Color("&7[&bMysteryBox&7] &cInvalid Amount."));
                        return true;
                    }

                    Player target = Bukkit.getPlayer(args[1]);

                    ItemStack box = Builder.nameItem(Material.valueOf(Crates.getInstance().getConfig().getString("OPTIONS.LOOTBOX-ITEM.MATERIAL")),
                            Crates.getInstance().getConfig().getString("OPTIONS.LOOTBOX-ITEM.DISPLAY-NAME"),
                            (short) Crates.getInstance().getConfig().getInt("OPTIONS.LOOTBOX-ITEM.ITEM-META"),
                            amount, Crates.getInstance().getConfig().getStringList("OPTIONS.LOOTBOX-ITEM.LORES"));

                    if (target.getInventory().firstEmpty() == -1) {
                        target.getWorld().dropItemNaturally(target.getLocation(), box);
                        Bukkit.broadcast(CC.Color("&7[&bMysteryBox&7] &c" + target.getName() + " &fobtained a MysteryBox. But his inventory was full, so it dropped outside."), "mysterybox.admin");
                        target.sendMessage(CC.Color("&7[&bMysteryBox&7] &fYou have received &lx" + amount + "&f Mystery Box" + (amount == 1 ? "" : "es") + "&f. But your inventory was full, so it dropped outside."));
                    } else {
                        target.getInventory().addItem(box);
                        target.sendMessage(CC.Color("&7[&bMysteryBox&7] &fYou have received &lx" + amount + "&f Mystery Box" + (amount == 1 ? "" : "es") + "&f."));
                    }
                }
                return true;
            }
        }

        sender.sendMessage(CC.translate("&3&m=============================="));
        sender.sendMessage(CC.translate("&b&lMysteryBox Help &7(1/1)"));
        sender.sendMessage(CC.translate(""));
        sender.sendMessage(CC.translate("&f<Needed> &7┃ &f[Optional]"));
        sender.sendMessage(CC.translate(""));
        sender.sendMessage(CC.translate("&a● &fCommand works"));
        sender.sendMessage(CC.translate("&6● &fCommand contains errors"));
        sender.sendMessage(CC.translate("&c● &fCommand doesn't work, coming soon"));
        sender.sendMessage(CC.translate(""));
        sender.sendMessage(CC.translate("&a● &f/mysterybox give <player|all> <amount>"));
        sender.sendMessage(CC.translate("&c● &f/mysterybox edit [mysterybox_name]"));
        sender.sendMessage(CC.translate("&3&m=============================="));

        return false;
    }
}
