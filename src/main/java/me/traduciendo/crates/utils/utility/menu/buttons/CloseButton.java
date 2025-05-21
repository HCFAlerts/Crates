package me.traduciendo.crates.utils.utility.menu.buttons;

import me.traduciendo.crates.utils.utility.item.ItemBuilder;
import me.traduciendo.crates.utils.utility.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class CloseButton extends Button {
   public ItemStack getButtonItem(Player player) {
      return (new ItemBuilder(Material.INK_SACK)).data(1).name("&ChatUtillose").build();
   }

   public void clicked(Player player, int i, ClickType clickType, int hb) {
      playNeutral(player);
      player.closeInventory();
   }
}
