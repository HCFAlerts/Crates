package me.traduciendo.crates.crate.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import me.traduciendo.crates.crate.listeners.Crate;
import me.traduciendo.crates.utils.util.BukkitUtil;
import me.traduciendo.crates.utils.util.ChatUtil;
import me.traduciendo.crates.utils.util.ItemBuilder;
import me.traduciendo.crates.utils.util.menu.Button;
import me.traduciendo.crates.utils.util.menu.Menu;

public class CrateColorMenu extends Menu {

	private Crate crate;
	
	public CrateColorMenu(Crate crate) {
		this.crate = crate;
	}
	
	private List<ChatColor> getColors() {
		List<ChatColor> colors = new ArrayList<ChatColor>();
		
		for(ChatColor color : ChatColor.values()) {
			if(!color.equals(ChatColor.UNDERLINE)
					&& !color.equals(ChatColor.STRIKETHROUGH)
					&& !color.equals(ChatColor.BOLD)
					&& !color.equals(ChatColor.RESET)
					&& !color.equals(ChatColor.MAGIC)
					&& !color.equals(ChatColor.ITALIC)) {
				colors.add(color);
			}
		}
		
		return colors;
	}
	
	@Override
	public String getTitle(Player player) {
		return crate.getColor() + crate.getName() +  " &7Colors (" + getColors().size() + ")";
	}
	
	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<Integer, Button>();
		
		int i = 0;
		for(ChatColor color : getColors()) {
			buttons.put(i, new ColorButton(crate, color));
			i++;
		}
		
		return buttons;
	}
	
	private static class ColorButton extends Button {

		private Crate crate;
		private ChatColor color;
		
		public ColorButton(Crate crate, ChatColor color) {
			this.crate = crate;
			this.color = color;
		}
		
		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.WOOL)
					.setName(color + WordUtils.capitalizeFully(color.name().replace("_", " ")))
					.setData(BukkitUtil.convertChatColorToWoolData(color))
					.setLore(
					  "&7&m--------------------------------"
					, "&b&l┃ &aClick to select this color."
					, "&7&m--------------------------------")
					.build();
		}
		
		@Override
		public void clicked(Player player, ClickType clickType) {
			if(clickType.isLeftClick()) {
				crate.setColor(color);
				player.closeInventory();
				
				ChatUtil.sendMessage(player, "&7[&bCrates&7] &aUpdated &r" + crate.getName() + "&a color to &r" + color + WordUtils.capitalizeFully(color.name().replace("_", " ")));
			}
			
		}		
	}
}
