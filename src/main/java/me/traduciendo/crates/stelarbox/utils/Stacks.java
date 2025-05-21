package me.traduciendo.crates.stelarbox.utils;

import me.traduciendo.crates.Crates;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Stacks {
    public static ItemStack getLootboxItem(int amount) {
        ItemStack item = new ItemStack(Material.valueOf(Crates.getInstance().getConfig().getString("ICON.MATERIAL")), amount, (byte) Crates.getInstance().getConfig().getInt("ICON.MATERIAL-DATA"));
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(CC.translate(Crates.getInstance().getConfig().getString("ICON.DISPLAY-NAME")));
        meta.setLore(CC.translate(Crates.getInstance().getConfig().getStringList("ICON.LORE")));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getRandomItem() {
        ItemStack stack = new ItemStack(Material.valueOf(Crates.getInstance().getConfig().getString("CONFIG.REWARD-STACK.MATERIAL")), 1, (short)Crates.getInstance().getConfig().getInt("CONFIG.REWARD-STACK.MATERIAL-DATA"));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate(Crates.getInstance().getConfig().getString("CONFIG.REWARD-STACK.DISPLAY-NAME"))); meta.setLore(CC.translate(Crates.getInstance().getConfig().getStringList("CONFIG.REWARD-STACK.LORE")));
        stack.setItemMeta(meta);
        return stack;
    }

    public static Inventory getBoxInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, CC.translate(Crates.getInstance().getConfig().getString("CONFIG.NAMES.GUI-BOX")));
        for (int i = 0; i < inventory.getSize(); i++) {
            Random random = new Random();
            ItemStack stack = new ItemStack(Material.STAINED_GLASS_PANE, 1,
                    (Crates.getInstance().getConfig().getIntegerList("CONFIG.PANES.TYPES").get(random.nextInt(Crates.getInstance().getConfig().getIntegerList("CONFIG.PANES.TYPES").size()))).shortValue());
            ItemMeta meta = stack.getItemMeta();
            if (Crates.getInstance().getConfig().getBoolean("CONFIG.PANES.GLOW")) {
                Glow glow = new Glow(stack.getType().getId());
                meta.addEnchant(glow, 1, true);
            }
            meta.setDisplayName(CC.translate("&7"));
            stack.setItemMeta(meta);
            inventory.setItem(i, stack);
        }

        List<Integer> slots = (Crates.getInstance().getConfig().getInt("CONFIG.AMOUNT")==1)?Arrays.asList(13):Arrays.asList(12,14);
        for (Integer slot : slots) {
            inventory.setItem(slot, getRandomItem());
        }
        return inventory;
    }

    public static Inventory getLootInventory(Boolean status) {
        final String name = Crates.getInstance().getConfig().getString("CONFIG.NAMES.GUI-LOOT") + (status ? " (Editor)" : "");
        Inventory inventory = Bukkit.createInventory(null, 54, CC.translate(name));
        Crates.getInstance().getData().getConfigurationSection("ITEMS").getKeys(false).forEach((item) -> {
            int slot = Integer.parseInt(item) - 1;
            ItemStack stack = Crates.getInstance().getData().getItemStack("ITEMS." + item + ".ITEM").clone();
            inventory.setItem(slot, stack);
        });
        return inventory;
    }
}
