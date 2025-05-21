package me.traduciendo.crates.utils.utility.menu.pagination;

import me.traduciendo.crates.utils.utility.item.ItemBuilder;
import me.traduciendo.crates.utils.utility.menu.Button;
import java.beans.ConstructorProperties;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PageButton extends Button {
   private final int mod;
   private final PaginatedMenu menu;

   public ItemStack getButtonItem(Player player) {
      ItemBuilder itemBuilder = new ItemBuilder(Material.CARPET);
      if (this.mod > 0) {
         itemBuilder.data(13);
      } else {
         itemBuilder.data(14);
      }

      if (this.hasNext(player)) {
         itemBuilder.name(this.mod > 0 ? "&aNext page" : "&cPrevious page");
      } else {
         itemBuilder.name("&7" + (this.mod > 0 ? "Last page" : "First page"));
      }

      return itemBuilder.build();
   }

   public void clicked(Player player, int i, ClickType clickType, int hb) {
      if (this.hasNext(player)) {
         this.menu.modPage(player, this.mod);
         Button.playNeutral(player);
      } else {
         Button.playFail(player);
      }

   }

   private boolean hasNext(Player player) {
      int pg = this.menu.getPage() + this.mod;
      return pg > 0 && this.menu.getPages(player) >= pg;
   }

   @ConstructorProperties({"mod", "menu"})
   public PageButton(int mod, PaginatedMenu menu) {
      this.mod = mod;
      this.menu = menu;
   }
}
