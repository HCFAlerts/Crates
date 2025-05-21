package me.traduciendo.crates.crate.command.argument.completer;

import java.util.List;
import java.util.stream.Collectors;

import me.traduciendo.crates.Crates;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.traduciendo.crates.crate.listeners.Crate;

public class CratesCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if(args.length == 1) {
			return Crates.getInstance().getCrateManager().getCrates().values().stream().map(Crate::getName).collect(Collectors.toList());
		}
		
		return null;
	}

}
