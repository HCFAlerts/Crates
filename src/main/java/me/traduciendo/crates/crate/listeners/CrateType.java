package me.traduciendo.crates.crate.listeners;

import org.bukkit.Material;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum CrateType {

	CHEST(Material.CHEST),
	ENDERCHEST(Material.ENDER_CHEST),
	ENCHANTMENT_TABBLE(Material.ENCHANTMENT_TABLE),
	BEACON(Material.BEACON),
	WORKBENCH(Material.WORKBENCH),
	REDSTONE_BLOCK(Material.REDSTONE_BLOCK);

	private Material material;
}
