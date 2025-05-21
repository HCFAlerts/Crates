package me.traduciendo.crates.utils.utility.menu.pagination;

import me.traduciendo.crates.utils.utility.ChatUtil;
import me.traduciendo.crates.utils.utility.item.ItemBuilder;
import me.traduciendo.crates.utils.utility.menu.Button;
import java.beans.ConstructorProperties;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class JumpToPageButton extends Button {
   private final int page;
   private final PaginatedMenu menu;
   private final boolean current;

   public ItemStack getButtonItem(Player player) {
      ItemBuilder itemBuilder = new ItemBuilder(this.current ? Material.ENCHANTED_BOOK : Material.BOOK, this.page);
      itemBuilder.name(ChatUtil.translate("&cPage " + this.page));
      if (this.current) {
         itemBuilder.lore("", ChatUtil.translate("&aCurrent page"));
      }

      return itemBuilder.build();
   }

   public void clicked(Player player, int i, ClickType clickType, int hb) {
      this.menu.modPage(player, this.page - this.menu.getPage());
      Button.playNeutral(player);
   }

   @ConstructorProperties({"page", "menu", "current"})
   public JumpToPageButton(int page, PaginatedMenu menu, boolean current) {
      this.page = page;
      this.menu = menu;
      this.current = current;
   }
}
