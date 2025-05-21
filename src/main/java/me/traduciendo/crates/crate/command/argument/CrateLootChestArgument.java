package me.traduciendo.crates.crate.command.argument;

import java.util.List;

import me.traduciendo.crates.Crates;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.traduciendo.crates.crate.listeners.Crate;
import me.traduciendo.crates.crate.command.argument.completer.CratesCompleter;
import me.traduciendo.crates.utils.util.ChatUtil;
import me.traduciendo.crates.utils.util.command.CommandArgument;

public class CrateLootChestArgument extends CommandArgument {

	public CrateLootChestArgument() {
		super("crate", "Get the crate.", "crate.command.givecrate");
	}

	@Override
	public String getUsage(String label) {
		return "/" + label + ' ' + this.getName() + " <crate_name>";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0) {
			ChatUtil.sendMessage(sender,  "&cIncorrect usage, please use: " + this.getUsage(label));
			return true;
		}
		
		Crate crate = Crates.getInstance().getCrateManager().getCrate(args[0]);
		
		if(crate == null) {
			ChatUtil.sendMessage(sender, "&cCrate &r" + args[0] + "&c not found.");
			return true;
		}
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			
			ChatUtil.sendMessage(sender, "&7[&bCrates&7] &aGived crate &a" + crate.getName() + "&a.");
			Crates.getInstance().getCrateManager().giveCrate(player, crate);
		} else {
			ChatUtil.sendMessage(sender, "&cThis command in only executable in game.");
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return new CratesCompleter().onTabComplete(sender, command, label, args);
	}
}
