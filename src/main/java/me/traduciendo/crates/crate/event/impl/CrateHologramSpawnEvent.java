package me.traduciendo.crates.crate.event.impl;

import org.bukkit.Location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.traduciendo.crates.crate.listeners.Crate;
import me.traduciendo.crates.crate.event.CrateEvent;

@Getter @AllArgsConstructor
public class CrateHologramSpawnEvent extends CrateEvent {

	private Crate crate;
	private Location location;
}
