package me.traduciendo.crates.crate.command.argument;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.traduciendo.crates.crate.menu.CratesMenu;
import me.traduciendo.crates.utils.util.ChatUtil;
import me.traduciendo.crates.utils.util.command.CommandArgument;

public class CrateListArgument extends CommandArgument {

	public CrateListArgument() {
		super("list", "Open a menu with all crates.", "crate.command.list", new String[]{"edit"});
	}

	@Override
	public String getUsage(String label) {
		return "/" + label + ' ' + this.getName();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			new CratesMenu().openMenu(player);
		} else {
			ChatUtil.sendMessage(sender, "&cThis command in only executable in game.");
		}
		return false;
	}

}
