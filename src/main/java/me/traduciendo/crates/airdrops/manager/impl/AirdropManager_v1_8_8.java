package me.traduciendo.crates.airdrops.manager.impl;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine;
import me.traduciendo.crates.Crates;
import me.traduciendo.crates.airdrops.rewards.Airdrop;
import me.traduciendo.crates.airdrops.listeners.AirdropListener;
import me.traduciendo.crates.airdrops.manager.AirdropManager;
import me.traduciendo.crates.airdrops.util.sound.CompatibleSound;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.block.Dropper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicReference;

public class AirdropManager_v1_8_8 implements AirdropManager {

    public AtomicReference<Integer> countdown;

    public AirdropManager_v1_8_8(Crates plugin) {
    }

    @Override
    public void place(final Player player, final Location location, Airdrop airdrop) {
        if (airdrop == null) {
            player.sendMessage(ChatColor.RED + "An error occurred: Airdrop is not configured correctly.");
            return;
        }

        final FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location.clone().add(0.0D, 20.0D, 0.0D), Material.DROPPER, (byte) 0);
        fallingBlock.setHurtEntities(false);
        fallingBlock.setDropItem(false);

        Location loc = location;
        if (airdrop.isFireworkenabled()) {
            Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();

            int fireworkPower = airdrop.getFireworkpower() > 0 ? airdrop.getFireworkpower() : 1;
            FireworkEffect.Type fireworkType = airdrop.getFireworktype() != null ? airdrop.getFireworktype() : FireworkEffect.Type.CREEPER;
            Color fireworkColor = airdrop.getFireworkcolor() != null ? airdrop.getFireworkcolor() : Color.AQUA;

            fwm.setPower(fireworkPower);
            fwm.addEffect(FireworkEffect.builder().with(fireworkType).withColor(fireworkColor).flicker(true).build());
            fw.setFireworkMeta(fwm);
        }

        (new BukkitRunnable() {
            public void run() {
                if (fallingBlock.isDead()) {
                    try {
                        final Dropper dropper = (Dropper) location.getBlock().getState();
                        if (!dropper.isPlaced()) return;

                        dropper.setMetadata("airdrop", new FixedMetadataValue(Crates.getInstance(), "airdrop"));

                        Hologram hologram = null;
                        if (airdrop.isHologramEnabled()) {
                            HolographicDisplaysAPI api = HolographicDisplaysAPI.get(Crates.getInstance());
                            Location hologramlocation = location.add(0.5D, 0.0D, 0.5D);
                            hologramlocation.setY(hologramlocation.getY() + 2.0D);
                            hologram = api.createHologram(hologramlocation);
                        }

                        CompatibleSound.ZOMBIE_WOODBREAK.play(player);
                        CompatibleSound.DIG_SNOW.play(player);
                        CompatibleSound.BURP.play(player);
                        dropper.getInventory().setContents(airdrop.getLoot());

                        final AtomicReference<Integer> countdown = new AtomicReference<>();
                        countdown.set(airdrop.getDestroytime());
                        TextHologramLine updateLine = null;

                        if (airdrop.isHologramEnabled()) {
                            TextHologramLine mainLine = hologram.getLines().insertText(0,
                                    ChatColor.translateAlternateColorCodes('&', airdrop.getMainHoloLine().replace("<name>", airdrop.getDisplayname())));
                            updateLine = hologram.getLines().appendText(
                                    ChatColor.translateAlternateColorCodes('&', airdrop.getSecondHoloLine().replace("<countdown>", String.valueOf(countdown.get()))));
                        }

                        Hologram finalHologram = hologram;
                        TextHologramLine finalUpdateLine = updateLine;
                        (new BukkitRunnable() {
                            public void run() {
                                try {
                                    if (dropper.getBlock().getType() == Material.AIR) {
                                        if (finalHologram != null) {
                                            finalHologram.delete();
                                        }
                                        cancel();
                                        return;
                                    }

                                    if (countdown.get() > 1) {
                                        countdown.set(countdown.get() - 1);

                                        if (airdrop.isHologramEnabled() && finalUpdateLine != null) {
                                            finalUpdateLine.setText(ChatColor.translateAlternateColorCodes('&',
                                                    airdrop.getSecondHoloLine().replace("<countdown>", String.valueOf(countdown.get()))));
                                        }
                                    } else if (countdown.get() == 1) {
                                        if (airdrop.isHologramEnabled() && finalHologram != null) {
                                            finalHologram.delete();
                                        }
                                        dropper.getBlock().breakNaturally();
                                        CompatibleSound.CHICKEN_EGG_POP.play(player);
                                        cancel();
                                    }
                                } catch (Throwable ex) {
                                    ex.printStackTrace();
                                    cancel();
                                }
                            }
                        }).runTaskTimer(Crates.getInstance(), 20L, 20L);

                        AirdropListener.airdropLocation.remove(location);
                        cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                        cancel();
                    }
                }
            }
        }).runTaskTimer(Crates.getInstance(), 5L, 5L);
    }
}
