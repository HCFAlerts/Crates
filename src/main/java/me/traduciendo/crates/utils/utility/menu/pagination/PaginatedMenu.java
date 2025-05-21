package me.traduciendo.crates.utils.utility.menu.pagination;

import me.traduciendo.crates.utils.utility.menu.Button;
import me.traduciendo.crates.utils.utility.menu.Menu;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.entity.Player;

public abstract class PaginatedMenu extends Menu {
   private int page = 1;

   public PaginatedMenu() {
      this.setUpdateAfterClick(false);
   }

   public String getTitle(Player player) {
      return this.getPrePaginatedTitle(player) + " - " + this.page + "/" + this.getPages(player);
   }

   public final void modPage(Player player, int mod) {
      this.page += mod;
      this.getButtons().clear();
      this.openMenu(player);
   }

   public final int getPages(Player player) {
      int buttonAmount = this.getAllPagesButtons(player).size();
      return buttonAmount == 0 ? 1 : (int)Math.ceil((double)buttonAmount / (double)this.getMaxItemsPerPage(player));
   }

   public final Map<Integer, Button> getButtons(Player player) {
      int minIndex = (int)((double)(this.page - 1) * (double)this.getMaxItemsPerPage(player));
      int maxIndex = (int)((double)this.page * (double)this.getMaxItemsPerPage(player));
      HashMap<Integer, Button> buttons = new HashMap();
      Iterator var5 = this.getAllPagesButtons(player).entrySet().iterator();

      while(var5.hasNext()) {
         Entry<Integer, Button> entry = (Entry)var5.next();
         int ind = (Integer)entry.getKey();
         if (ind >= minIndex && ind < maxIndex) {
            ind -= (int)((double)this.getMaxItemsPerPage(player) * (double)(this.page - 1));
            buttons.put(ind, entry.getValue());
         }
      }

      Map<Integer, Button> global = this.getGlobalButtons(player);
      if (global != null) {
         Iterator var9 = global.entrySet().iterator();

         while(var9.hasNext()) {
            Entry<Integer, Button> gent = (Entry)var9.next();
            buttons.put(gent.getKey(), gent.getValue());
         }
      }

      return buttons;
   }

   public int getMaxItemsPerPage(Player player) {
      return 18;
   }

   public Map<Integer, Button> getGlobalButtons(Player player) {
      return null;
   }

   public abstract String getPrePaginatedTitle(Player var1);

   public abstract Map<Integer, Button> getAllPagesButtons(Player var1);

   public int getPage() {
      return this.page;
   }
}
