package me.traduciendo.crates.utils.utility.menu.buttons;

import me.traduciendo.crates.utils.utility.item.ItemBuilder;
import me.traduciendo.crates.utils.utility.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class AirButton extends Button {
   public ItemStack getButtonItem(Player player) {
      return (new ItemBuilder(Material.AIR)).build();
   }

   public boolean shouldCancel(Player player, int slot, ClickType clickType) {
      return true;
   }
}
