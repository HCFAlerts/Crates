package me.traduciendo.crates.airdrops.manager;

import me.traduciendo.crates.airdrops.rewards.Airdrop;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface AirdropManager {

     void place(final Player player, final Location location,Airdrop airdrop);



}
