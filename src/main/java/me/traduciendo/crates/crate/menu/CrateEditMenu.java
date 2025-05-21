package me.traduciendo.crates.crate.menu;

import me.traduciendo.crates.Crates;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.traduciendo.crates.crate.listeners.Crate;
import me.traduciendo.crates.utils.util.ChatUtil;
import net.md_5.bungee.api.ChatColor;

public class CrateEditMenu implements Listener {
	
	public CrateEditMenu() { }
	
	public CrateEditMenu(Player target, Crate crate) {
		
		Inventory inventory = Bukkit.createInventory(null, 54, ChatUtil.translate("&7Editing " + crate.getColor() + crate.getName()));
		
		for(Integer pos : crate.getLoot().keySet()) {
			inventory.setItem(pos, crate.getLoot().get(pos));
		}
		
		target.openInventory(inventory);
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		Inventory inventory = event.getInventory();
		
		if(event.getPlayer().hasPermission("crates.edit")) {
			if(inventory.getTitle().contains(ChatUtil.translate("&7Editing "))) {
				String crateName = ChatColor.stripColor(inventory.getTitle().replace(ChatUtil.translate("&7Editing "), ""));
				Crate crate = Crates.getInstance().getCrateManager().getCrate(crateName);
				if(crate.getLoot() != null) {
					if(!crate.getLoot().isEmpty()) {
						crate.getLoot().clear();
					}
				}
				for(int i = 0; i < inventory.getSize(); i++) {
					ItemStack itemStack = inventory.getItem(i);
					if(itemStack != null && itemStack.getType() != Material.AIR) {
												
						crate.getLoot().put(i, itemStack);
					}
				}
				
				Player player = (Player) event.getPlayer();
				ChatUtil.sendMessage(player, "&7[&bCrates&7] &aSuccessfully Edited &l" + crate.getColor() + crate.getName() + "&a Crate");
			}
		}
	}
}
