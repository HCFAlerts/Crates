package me.traduciendo.crates.crate.listeners;

import me.traduciendo.crates.Crates;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.traduciendo.crates.crate.event.impl.CrateHologramDespawnEvent;
import me.traduciendo.crates.crate.event.impl.CrateHologramSpawnEvent;
import me.traduciendo.crates.utils.util.ChatUtil;

import java.util.List;

public class CrateHologramListener implements Listener {

	private Hologram getHologram(Location location) {
		for (Hologram hologram : HologramsAPI.getHolograms(Crates.getInstance())) {
			if (hologram.getLocation().equals(location)) {
				return hologram;
			}
		}
		return null;
	}

	@EventHandler
	public void onHologramSpawn(CrateHologramSpawnEvent event) {
		Hologram hologram = HologramsAPI.createHologram(Crates.getInstance(), event.getLocation());
		hologram.getVisibilityManager().setVisibleByDefault(true);

		hologram.appendItemLine(event.getCrate().getItemHologram());
		List<String> hologramLines = Crates.getInstance().getMainConfig().getStringList("CRATE-HOLOGRAM-LINES");
		String crateName = event.getCrate().getName();
		String crateColor = event.getCrate().getColor().toString();

		for (String line : hologramLines) {
			hologram.appendTextLine(ChatUtil.translate(line
					.replace("{crate}", crateName)
					.replace("{crate_color}", crateColor)));
		}
	}

	@EventHandler
	public void onHologramDespawn(CrateHologramDespawnEvent event) {
		Hologram hologram = getHologram(event.getLocation());
		if (hologram != null) {
			hologram.delete();
		}
	}
}
