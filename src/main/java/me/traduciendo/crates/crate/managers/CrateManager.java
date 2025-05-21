package me.traduciendo.crates.crate.managers;

import java.util.*;

import lombok.Getter;
import me.traduciendo.crates.crate.listeners.Crate;
import me.traduciendo.crates.Crates;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.traduciendo.crates.crate.event.impl.CrateHologramSpawnEvent;
import me.traduciendo.crates.utils.util.ChatUtil;
import me.traduciendo.crates.utils.util.InventoryUtil;
import me.traduciendo.crates.utils.util.ItemBuilder;
import me.traduciendo.crates.utils.util.config.Config;
import me.traduciendo.crates.utils.util.serializer.impl.LocationSerializer;

public class CrateManager {

	private Map<String, Crate> crates = new HashMap<String, Crate>();
	private Config config;

	public static String META = "§1§2§3";

	public CrateManager() {
		this.reload();

		if (Crates.getInstance().canCreateHolograms()) {
			for (Crate crate : this.crates.values()) {
				for (String serializedLocation : crate.getCrateLocations()) {
					Location location = new LocationSerializer().deserialize(serializedLocation);
					if (location != null) {
						location.add(0, 3, 0);
						new CrateHologramSpawnEvent(crate, location).call();
					} else {
						System.out.println("Error: location is null for crate " + crate.getName());
					}

//		if (Crates.getInstance().canCreateHolograms()) {
//			for (Crate crate : this.crates.values()) {
//				for (String serializedLocation : crate.getCrateLocations()) {
//					Location location = new LocationSerializer().deserialize(serializedLocation).clone().add(0, 3, 0);
//					new CrateHologramSpawnEvent(crate, location).call();
				}
			}
		}
	}

//    public Crate getCrate(String name) {
//		return crates.get(name.toLowerCase());
//	}
//
//	public Crate getCrate(Location location) {
//		for (Crate crate : crates.values()) {
//			for (String serializedLocation : crate.getCrateLocations()) {
//				if (serializedLocation.equals(new LocationSerializer().serialize(location))) {
//					return crate;
//				}
//			}
//		}
//		return null;
//	}

	public Map<String, Crate> getCrates() {
		return crates;
	}

	public Crate getCrate(String name) {

		if (crates.keySet().contains(name.toLowerCase())) {
			return crates.get(name.toLowerCase());
		}

		return null;
	}

	public Crate getCrate(Location location) {

		for (String key : crates.keySet()) {
			Crate crate = crates.get(key);

			for (String serializedLocation : crate.getCrateLocations()) {
				if (serializedLocation.equals(new LocationSerializer().serialize(location))) {
					return crate;
				}
			}
		}

		return null;
	}

	public Crate getCrateFromKey(ItemStack item) {
		if (item == null || item.getType() == Material.AIR || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
			return null;
		}

		String itemName = ChatColor.stripColor(item.getItemMeta().getDisplayName());

		for (Crate crate : crates.values()) {
			if (crate.getKeyName() != null && itemName.equalsIgnoreCase(crate.getKeyName())) {
				return crate;
			}
		}

		return null;
	}

	public void create(String name) {
		crates.put(name.toLowerCase(), new Crate(name));
	}

	public void delete(Crate crate) {
		if (crate != null) {
			crates.remove(crate.getName().toLowerCase());
		}
	}

	public void giveCrate(Player player, Crate crate) {
		ItemBuilder builder = new ItemBuilder(crate.getType().getMaterial());
		builder.setName(crate.getColor().toString() + crate.getName() + "&7 Crate" + META);
		player.getInventory().addItem(builder.build());
	}

	public void giveKey(Player player, Crate crate, int amount) {
		ItemBuilder builder = new ItemBuilder(crate.getItemKey());
		builder.setName(crate.getColor().toString() + crate.getName() + "&7 Key" + META);
		builder.setLore(Crates.getInstance().getMainConfig().getStringList("CRATE-KEY-LORE"));
		builder.setAmount(amount);

		ItemStack item = builder.build();
		if (InventoryUtil.isFull(player)) {
			player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
		} else {
			player.getInventory().addItem(item);
		}
		ChatUtil.sendMessage(player, "&7[&bCrates&7] &aYou've received &lx" + amount + "&a " + crate.getColor().toString() + crate.getName() + "&a keys.");
	}

	public boolean isKey(Crate crate, ItemStack item) {
		if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
			String displayName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
			String name = displayName.toLowerCase().replace(" key", "").replace(CrateManager.META, "");
			Crate crate2 = getCrate(name);
			return crate2 != null && crate.getName().equalsIgnoreCase(crate2.getName());
		}
		return false;
	}

	public void reload() {
		this.config = new Config(Crates.getInstance(), "crates");

		Object object = config.get("CRATES");
		if (object instanceof MemorySection) {
			MemorySection section = (MemorySection) object;
			Collection<String> keys = section.getKeys(false);
			for (String id : keys) {
				Object crateObject = config.get(section.getCurrentPath() + '.' + id);
				if (crateObject instanceof Crate) {
					crates.put(id.toLowerCase(), (Crate) crateObject);
				} else {
					System.out.println("Warning: Failed to load crate with id " + id);
				}
			}
		} else {
			System.out.println("Warning: 'CRATES' section not found or invalid in crates.yml");
		}
	}

//	public void save() {
//		Map<String, Crate> saveMap = new LinkedHashMap<>(crates);
//		config.set("CRATES", saveMap);
//		config.save();
//	}

	public void save() {
		Set<Map.Entry<String, Crate>> entrySet = crates.entrySet();
		Map<String, Crate> saveMap = new LinkedHashMap<String, Crate>(entrySet.size());
		for (Map.Entry<String, Crate> entry : entrySet) {
			saveMap.put(entry.getKey().toString(), entry.getValue());
		}

		config.set("CRATES", saveMap);
		config.save();
	}
}
