package me.traduciendo.crates.crate.command.argument;

import me.traduciendo.crates.Crates;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.traduciendo.crates.crate.listeners.Crate;
import me.traduciendo.crates.utils.util.ChatUtil;
import me.traduciendo.crates.utils.util.command.CommandArgument;

public class CrateCreateArgument extends CommandArgument {

	public CrateCreateArgument() {
		super("create", "Create a new crate.", "crate.command.create");
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
		
		if(crate != null) {
			ChatUtil.sendMessage(sender, "&cCrate already exists.");
			return true;
		}
		
		Crates.getInstance().getCrateManager().create(args[0]);
		ChatUtil.sendMessage(sender, "&7[&bCrates&7] &aYou have created the &l" + args[0] + "&a crate.");
		return false;
	}
}
