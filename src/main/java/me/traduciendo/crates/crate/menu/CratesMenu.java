package me.traduciendo.crates.crate.menu;

import java.util.HashMap;
import java.util.Map;

import me.traduciendo.crates.Crates;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import me.traduciendo.crates.crate.listeners.Crate;
import me.traduciendo.crates.utils.util.ItemBuilder;
import me.traduciendo.crates.utils.util.menu.Button;
import me.traduciendo.crates.utils.util.menu.Menu;

public class CratesMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return "&7Crates";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<Integer, Button>();
		
		int slot = 0;
		
		for(Crate crate : Crates.getInstance().getCrateManager().getCrates().values()) {
			buttons.put(slot, new CratesButton(crate));
			slot++;
		}
		
		return buttons;
	}

	private static class CratesButton extends Button {

		private Crate crate;
		
		public CratesButton(Crate crate) {
			this.crate = crate;
		}
		
		@Override
		public ItemStack getButtonItem(Player player) {
			
			ItemBuilder itemBuilder = new ItemBuilder(Material.CHEST);
			
			itemBuilder.setName(crate.getColor() + crate.getName());
			itemBuilder.setLore(
					"&7&m--------------------------------",
					"&b&l┃ &eLeft-Click to view loot",
					"&b&l┃ &eRight-Click to edit loot",
					"&7&m--------------------------------"
					);
			
			return itemBuilder.build();
		}

		@Override
		public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
			if(clickType.isLeftClick()) {
				player.closeInventory();
				new CrateLootMenu(this.crate).openMenu(player);
			}
			
			if(player.hasPermission("crates.edit")) {
				if(clickType.isRightClick()) {
					player.closeInventory();
					new CrateEditMenu(player, this.crate);
				}
			}
		}
		
	}
}
