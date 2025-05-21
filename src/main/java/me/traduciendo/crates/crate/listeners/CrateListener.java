package me.traduciendo.crates.crate.listeners;

import me.traduciendo.crates.Crates;
import me.traduciendo.crates.crate.managers.CrateManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.traduciendo.crates.crate.event.impl.CrateHologramDespawnEvent;
import me.traduciendo.crates.crate.event.impl.CrateHologramSpawnEvent;
import me.traduciendo.crates.crate.menu.CrateEditMenu;
import me.traduciendo.crates.crate.menu.CrateLootMenu;
import me.traduciendo.crates.utils.util.ChatUtil;
import me.traduciendo.crates.utils.util.LocationUtil;
import me.traduciendo.crates.utils.util.serializer.impl.LocationSerializer;

public class CrateListener implements Listener {

	private LocationSerializer locationSerializer = new LocationSerializer();

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();

		if (player.getItemInHand() != null) {
			ItemStack hand = player.getItemInHand();

			if (hand.hasItemMeta() && hand.getItemMeta().hasDisplayName()) {
				if (hand.getItemMeta().getDisplayName().contains(CrateManager.META)) {
					if (!player.getGameMode().equals(GameMode.CREATIVE)) {
						event.setCancelled(true);
						return;
					}

					String displayName = ChatColor.stripColor(hand.getItemMeta().getDisplayName());
					String name = displayName.toLowerCase().replace(" crate", "").replace(CrateManager.META, "");

					Crate crate = Crates.getInstance().getCrateManager().getCrate(name);

					if (crate != null) {
						String serializedLocation = locationSerializer.serialize(event.getBlockPlaced().getLocation().clone().add(0.5, 0, 0.5));

						if (Crates.getInstance().canCreateHolograms()) {
							double hologramHeight = Crates.getInstance().getMainConfig().getDouble("CRATE-HOLOGRAM-HEIGHT");
							new CrateHologramSpawnEvent(crate, event.getBlockPlaced().getLocation().clone().add(0.5, hologramHeight, 0.5)).call();
						}

						crate.getCrateLocations().add(serializedLocation);

						ChatUtil.sendMessage(player, "&7[&bCrates&7] &aYou have placed the &l" + crate.getName() + "&a at &l" + LocationUtil.getFormatted(event.getBlockPlaced().getLocation(), true) + "&a location.");
					}
				}
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Location location = event.getBlock().getLocation().clone().add(0.5, 0, 0.5);
		Crate crate = Crates.getInstance().getCrateManager().getCrate(location);

		if (crate != null) {
			if (player.isSneaking() && player.getGameMode().equals(GameMode.CREATIVE)) {
				String serializedLocation = locationSerializer.serialize(location);

				if (Crates.getInstance().canCreateHolograms()) {
					double hologramHeight = Crates.getInstance().getMainConfig().getDouble("CRATE-HOLOGRAM-HEIGHT");
					new CrateHologramDespawnEvent(crate, location.clone().add(0, hologramHeight, 0)).call();
				}

				crate.getCrateLocations().remove(serializedLocation);
				ChatUtil.sendMessage(player, "&7[&bCrates&7] &aYou have removed the &l" + crate.getName() + "&a from &l" + LocationUtil.getFormatted(event.getBlock().getLocation(), true) + "&a location.");
				return;
			}

			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();

		if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();

			Location blockLocation = block.getLocation();
			ItemStack hand = player.getItemInHand();

			blockLocation.setX(blockLocation.getBlockX());
			blockLocation.setZ(blockLocation.getBlockZ());

			Crate crate = Crates.getInstance().getCrateManager().getCrate(blockLocation.add(0.5, 0, 0.5));

			if (crate != null) {
				if (action == Action.RIGHT_CLICK_BLOCK) {
					if (player.isSneaking()) {
						if (player.getGameMode().equals(GameMode.CREATIVE) && player.hasPermission("crates.edit")) {
							new CrateEditMenu(player, crate);
						}
					} else {
						if (Crates.getInstance().getCrateManager().isKey(crate, hand)) {
							if (crate.giveLoot(player)) {
								if (hand.getAmount() == 1) {
									player.setItemInHand(new ItemStack(Material.AIR, 1));
								} else {
									hand.setAmount(hand.getAmount() - 1);
								}

								player.updateInventory();
								player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.5F, 0.5F);
							}
						} else {
							ChatUtil.sendMessage(player, "&7[&bCrates&7] &cYou need " + crate.getColor() + crate.getName() + "&c key.");
						}
					}

					event.setCancelled(true);
				}

				if (action == Action.LEFT_CLICK_BLOCK) {
					if (player.isSneaking()) {
						if (!player.getGameMode().equals(GameMode.CREATIVE)) {
							event.setCancelled(true);
						}
					} else {
						if (!crate.getLoot().values().isEmpty()) {
							new CrateLootMenu(crate).openMenu(player);
						}
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onAirInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		ItemStack hand = player.getItemInHand();

		if (!Crates.getInstance().getMainConfig().getBoolean("OPEN-CRATE-KEY-ANYWHERE")) {
			return;
		}

		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			if (hand != null && hand.getType() != Material.AIR) {
				Crate crate = Crates.getInstance().getCrateManager().getCrateFromKey(hand);

				if (crate != null) {
					if (crate.giveLoot(player)) {
						if (hand.getAmount() == 1) {
							player.setItemInHand(new ItemStack(Material.AIR));
						} else {
							hand.setAmount(hand.getAmount() - 1);
						}

						player.updateInventory();
						player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.5F, 0.5F);
					}
					event.setCancelled(true);
				}
			}
		}
	}
}

