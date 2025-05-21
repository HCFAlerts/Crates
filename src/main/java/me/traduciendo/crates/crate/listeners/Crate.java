package me.traduciendo.crates.crate.listeners;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Getter;
import lombok.Setter;
import me.traduciendo.crates.utils.util.ChatUtil;
import me.traduciendo.crates.utils.util.GenericUtil;
import me.traduciendo.crates.utils.util.InventoryUtil;
import me.traduciendo.crates.utils.util.ItemBuilder;
import me.traduciendo.crates.utils.util.serializer.impl.LocationSerializer;

@Getter
public class Crate implements ConfigurationSerializable {

	private String name;
	private UUID id;
	@Setter private ChatColor color;
	@Setter private CrateType type;
	@Setter private ItemStack itemKey;
	@Setter private ItemStack itemHologram;
	private List<String> crateLocations;
	private Map<Integer, ItemStack> loot;
	
	public Crate(String name) {
		this.name = name;
		this.id = UUID.randomUUID();
		this.color = ChatColor.WHITE;
		this.type = CrateType.CHEST;
		this.itemKey = new ItemStack(Material.TRIPWIRE_HOOK);
		this.itemHologram = new ItemStack(Material.PAPER);
		
		this.crateLocations = Lists.newArrayList();
		this.loot = Maps.newHashMap();
		
		this.loot.put(0, new ItemBuilder(Material.WATER_BUCKET)
				.setName("&bLite Club Development")
				.setLore("&7dsc.gg/liteclubdevelopment")
				.build());
	}
	
	public Crate(Map<String, Object> map) {
		this.name = (String) map.get("NAME");
		this.id = UUID.fromString((String) map.get("ID"));
		this.color = ChatColor.valueOf((String) map.get("COLOR"));
		this.type = CrateType.valueOf((String) map.get("TYPE"));
		this.itemKey = (ItemStack) map.get("ITEM-KEY");
		this.itemHologram = (ItemStack) map.get("ITEM-HOLOGRAM");
		
		this.crateLocations = GenericUtil.createList(map.get("CRATE-LOCATIONS"), String.class);
		
		this.loot = Maps.newHashMap();
		
		for(Map.Entry<Integer, ItemStack> lootEntry : GenericUtil.castMap(map.get("LOOT"), Integer.class, ItemStack.class).entrySet()) {
			this.loot.put(lootEntry.getKey(), lootEntry.getValue());
		}
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = Maps.newHashMap();
		
		map.put("NAME", name);
		map.put("ID", id.toString());
		map.put("COLOR", color.name());
		map.put("TYPE", type.name());
		map.put("CRATE-LOCATIONS", crateLocations);
		map.put("LOOT", loot);
		map.put("ITEM-KEY", itemKey);
		map.put("ITEM-HOLOGRAM", itemHologram);
		
		return map;
	}

	public boolean giveLoot(Player player) {
		if (InventoryUtil.isFull(player)) {
			ChatUtil.sendMessage(player, "&7[&bCrates&7] &cYour inventory is full.");
			return false;
		}
		
		List<ItemStack> results = Lists.newArrayList();
		
		for (ItemStack item : this.loot.values()) {
			if (item.getType().equals(Material.STAINED_GLASS_PANE)) {
				continue;
			}
			
			results.add(item);
		}
		
		Random random = new Random();
		
		int select = random.nextInt(results.size());
		
		ItemStack item = results.get(select);
		
		player.getInventory().addItem(item);
		return true;
	}
	
	public void addLoot(Integer position, ItemStack item) {
		loot.put(position, item);
	}
	
	public List<Location> getLocations() {
		List<Location> locations = Lists.newArrayList();
		
		for (String crateLocation : this.crateLocations) {
			LocationSerializer locationSerializer = new LocationSerializer();
			Location location = locationSerializer.deserialize(crateLocation);
			
			if (location != null) {
				locations.add(location);
			}
		}
		
		return locations;
	}

	public String getKeyName() {
		if (itemKey != null && itemKey.hasItemMeta() && itemKey.getItemMeta().hasDisplayName()) {
			return ChatColor.stripColor(itemKey.getItemMeta().getDisplayName());
		}
		return null;
	}

}
