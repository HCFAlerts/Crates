package me.traduciendo.crates.crate.command;

import java.util.Arrays;

import me.traduciendo.crates.crate.command.argument.CrateColorArgument;
import me.traduciendo.crates.crate.command.argument.CrateCreateArgument;
import me.traduciendo.crates.crate.command.argument.CrateDeleteArgument;
import me.traduciendo.crates.crate.command.argument.CrateGiveKeyArgument;
import me.traduciendo.crates.crate.command.argument.CrateItemArgument;
import me.traduciendo.crates.crate.command.argument.CrateKeyArgument;
import me.traduciendo.crates.crate.command.argument.CrateListArgument;
import me.traduciendo.crates.crate.command.argument.CrateLootChestArgument;
import me.traduciendo.crates.crate.command.argument.CrateSaveArgument;
import me.traduciendo.crates.crate.command.argument.CrateReloadArgument;
import me.traduciendo.crates.utils.util.command.ArgumentExecutor;

public class CrateCommand extends ArgumentExecutor {

	public CrateCommand() {
		super("crate");

		Arrays.asList(
				new CrateCreateArgument(),
				new CrateDeleteArgument(),
				new CrateGiveKeyArgument(),
				new CrateItemArgument(),
				new CrateKeyArgument(),
				new CrateListArgument(),
				new CrateLootChestArgument(),
				new CrateColorArgument(),
				new CrateSaveArgument(),
				new CrateReloadArgument()
				).stream().forEach(commandArgument -> this.addArgument(commandArgument));
	}
}
