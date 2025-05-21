package me.traduciendo.crates.utils.utility.menu.buttons;

import me.traduciendo.crates.utils.utility.item.ItemBuilder;
import me.traduciendo.crates.utils.utility.menu.Button;
import me.traduciendo.crates.utils.utility.menu.pagination.PaginatedMenu;
import java.beans.ConstructorProperties;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PageInfoButton extends Button {
   private final PaginatedMenu paginatedMenu;

   public ItemStack getButtonItem(Player player) {
      return (new ItemBuilder(Material.NETHER_STAR)).name("&ePage Info").lore("&e" + this.paginatedMenu.getPage() + "&7/&a" + this.paginatedMenu.getPages(player)).build();
   }

   public boolean shouldCancel(Player player, int slot, ClickType clickType) {
      return true;
   }

   @ConstructorProperties({"paginatedMenu"})
   public PageInfoButton(PaginatedMenu paginatedMenu) {
      this.paginatedMenu = paginatedMenu;
   }
}
