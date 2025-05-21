package me.traduciendo.crates.stelarbox.listeners;

import me.traduciendo.crates.Crates;
import me.traduciendo.crates.stelarbox.utils.CC;
import me.traduciendo.crates.stelarbox.utils.Plugin;
import me.traduciendo.crates.stelarbox.utils.Stacks;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class StelarBoxListener implements Listener {
    @EventHandler
    public void onOpen(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (e.getItem() != null && e.getItem().isSimilar(Stacks.getLootboxItem(1))) {
                e.setCancelled(true);
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf(Crates.getInstance().getConfig().getString("CONFIG.SOUNDS.OPEN")), 1.0F, 1.0F);
                for (int i = 0; i < Crates.getInstance().getConfig().getInt("CONFIG.PARTICLES.AMOUNT"); i++) {
                    if (Crates.getInstance().getConfig().getString("CONFIG.PARTICLES.OPEN").equalsIgnoreCase("None")) {
                        return;
                    }
                    e.getPlayer().playEffect(e.getPlayer().getLocation(), Effect.valueOf(Crates.getInstance().getConfig().getString("CONFIG.PARTICLES.OPEN")), (short)0);
                }
                if (e.getItem().getAmount() > 1) {
                    e.getItem().setAmount(e.getItem().getAmount()-1);
                } else {
                    e.getPlayer().setItemInHand(null);
                }
                if (e.getPlayer().isSneaking() && Crates.getInstance().getConfig().getBoolean("CONFIG.FAST-OPEN")) {
                    int repeats = (Crates.getInstance().getConfig().getInt("CONFIG.AMOUNT")==1)?1:2;
                    for (int i = 0; i < repeats; i++) {
                        ItemStack stack = Plugin.getRandomItem().clone();
                        String reward = (stack.getItemMeta().getDisplayName() != null) ? stack.getItemMeta().getDisplayName() : stack.getType().name();
                        e.getPlayer().sendMessage(CC.translate(Crates.getInstance().getConfig().getString("MESSAGES.RECEIVE").replace("%amount%", String.valueOf(stack.getAmount())).replace("%reward%", reward)));
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf(Crates.getInstance().getConfig().getString("CONFIG.SOUNDS.OPEN")), 1.0F, 1.0F);
                        if (!Plugin.isFull(e.getPlayer())) {
                            e.getPlayer().getInventory().addItem(stack);
                        } else {
                            e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), stack);
                        }
                    }
                    return;
                }
                e.getPlayer().openInventory(Stacks.getBoxInventory());
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null || e.getInventory() == null || e.getWhoClicked() == null
        || e.getCurrentItem() == null || (!(e.getWhoClicked() instanceof Player))) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(Stacks.getBoxInventory().getTitle())
        && e.getClickedInventory().equals(e.getInventory())) {
            e.setCancelled(true);
            if (e.getCurrentItem().isSimilar(Stacks.getRandomItem())) {
                for (int i = 0; i < Crates.getInstance().getConfig().getInt("CONFIG.PARTICLES.AMOUNT"); i++) {
                    if (Crates.getInstance().getConfig().getString("CONFIG.PARTICLES.REWARDS").equalsIgnoreCase("None")) {
                        return;
                    }
                    p.playEffect(p.getLocation(), Effect.valueOf(Crates.getInstance().getConfig().getString("CONFIG.PARTICLES.REWARDS")), (short)0);
                }
                ItemStack stack = Plugin.getRandomItem().clone();
                String reward = (stack.getItemMeta().getDisplayName() != null) ? stack.getItemMeta().getDisplayName() : stack.getType().name();
                p.sendMessage(CC.translate(Crates.getInstance().getConfig().getString("MESSAGES.RECEIVE").replace("%amount%", String.valueOf(stack.getAmount())).replace("%reward%", reward)));
                e.getInventory().setItem(e.getRawSlot(), stack);
                p.playSound(p.getLocation(), Sound.valueOf(Crates.getInstance().getConfig().getString("CONFIG.SOUNDS.REWARDS")), 1.0F, 1.0F);
                if (!Plugin.isFull(p)) {
                    p.getInventory().addItem(stack);
                    return;
                }
                p.getWorld().dropItem(p.getLocation(), stack);
            }
            p.updateInventory();
        }
        else if (e.getView().getTitle().equals(Stacks.getLootInventory(false).getTitle())) {
            e.setCancelled(true);
            p.updateInventory();
        }
    }

    @EventHandler
    public void onEdit(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) {
            return;
        }
        if (e.getView().getTitle().equals(Stacks.getLootInventory(true).getTitle())) {
            ItemStack[] content = e.getInventory().getContents();
            byte ib = 0;
            for (ItemStack stacks : content) {
                ++ib;
                if (stacks != null && !stacks.getType().equals(Material.AIR)) {
                    if (Crates.getInstance().getData().get("ITEMS." + ib) != null) {
                        ItemStack stack = Crates.getInstance().getData().getItemStack("ITEMS." + ib + ".ITEM").clone();
                        if (stack.equals(stacks)) continue;
                    }
                    Crates.getInstance().getData().set("ITEMS." + ib + ".ITEM", stacks);
                    Crates.getInstance().getData().saveAll();
                } else {
                    Crates.getInstance().getData().set("ITEMS." + ib, null);
                    Crates.getInstance().getData().saveAll();
                }
            }
            ((Player) e.getPlayer()).sendMessage(CC.translate("&7[&bStelarBox&7] &aYou successfully edited the loot."));
        }
        else if (e.getView().getTitle().equals(Stacks.getBoxInventory().getTitle())) {
            Player p = (Player) e.getPlayer();
            for (int i = 0; i < e.getInventory().getSize(); i++) {
                if (e.getInventory().getItem(i).isSimilar(Stacks.getRandomItem())) {
                    ItemStack stack = Plugin.getRandomItem().clone();
                    String reward = (stack.getItemMeta().getDisplayName() != null) ? stack.getItemMeta().getDisplayName() : stack.getType().name();
                    p.sendMessage(CC.translate(Crates.getInstance().getConfig().getString("MESSAGES.RECEIVE").replace("%amount%", String.valueOf(stack.getAmount())).replace("%reward%", reward)));
                    if (!Plugin.isFull(p)) {
                        p.getInventory().addItem(stack);
                        p.updateInventory();
                    } else {
                        p.getWorld().dropItem(p.getLocation(), stack);
                    }
                }
            }
        }
    }
}
