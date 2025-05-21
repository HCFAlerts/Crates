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

public class CrateKeyArgument extends CommandArgument {

	public CrateKeyArgument() {
		super("key", "Set crate key.", "crate.command.key", new String[] { "setkey" });
		this.isPlayerOnly = true;
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

		Player player = (Player) sender;
		if(player.getItemInHand() == null) {
			ChatUtil.sendMessage(sender, "&cItem in hand cannot be null.");
			return true;
		}

		crate.setItemKey(player.getItemInHand());

		ChatUtil.sendMessage(sender, "&7[&bCrates&7] &aYou have seted key type to the &l" + args[0] + "&a crate.");
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return new CratesCompleter().onTabComplete(sender, command, label, args);
	}
}
