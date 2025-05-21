package me.traduciendo.crates.utils.utility.menu.buttons;

import me.traduciendo.crates.utils.utility.item.ItemBuilder;
import me.traduciendo.crates.utils.utility.menu.Button;
import me.traduciendo.crates.utils.utility.menu.Menu;
import java.beans.ConstructorProperties;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BackButton extends Button {
   private final Menu back;

   public ItemStack getButtonItem(Player player) {
      return (new ItemBuilder(Material.REDSTONE)).name("&cBack").lore("&cClick here to return", "&cto the previous menu.").build();
   }

   public void clicked(Player player, int i, ClickType clickType, int hb) {
      Button.playNeutral(player);
      this.back.openMenu(player);
   }

   @ConstructorProperties({"back"})
   public BackButton(Menu back) {
      this.back = back;
   }
}
