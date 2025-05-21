package me.traduciendo.crates.crate.command.argument;

import me.traduciendo.crates.Crates;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.traduciendo.crates.utils.util.ChatUtil;
import me.traduciendo.crates.utils.util.command.CommandArgument;

public class CrateSaveArgument extends CommandArgument {

	public CrateSaveArgument() {
		super("save", "Save all crates to yml.", "crate.command.save");
	}

	@Override
	public String getUsage(String label) {
		return "/" + label + ' ' + this.getName();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		ChatUtil.sendMessage(sender, "&7[&bCrates&7] &aAll crates has been saved to the yml storage.");
		Crates.getInstance().getCrateManager().save();
		return false;
	}

}
