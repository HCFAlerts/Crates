package me.traduciendo.crates.partnerpackages.packages.menu;

import com.google.common.collect.Maps;
import me.traduciendo.crates.Crates;
import me.traduciendo.crates.partnerpackages.packages.Package;
import me.traduciendo.crates.utils.utility.menu.Button;
import me.traduciendo.crates.utils.utility.menu.Menu;
import java.beans.ConstructorProperties;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PackageLootMenu extends Menu {
   private final Package pack;

   public String getTitle(Player player) {
      return this.pack.getName() + " Loot";
   }

   public int getSize() {
      return 54;
   }

   public Map<Integer, Button> getButtons(Player player) {
      Map<Integer, Button> buttons = Maps.newHashMap();
      ItemStack[] items = (ItemStack[])this.pack.getItems().clone();

      for(int i = 0; i < this.pack.getItems().length; ++i) {
         if (items[i] != null) {
            buttons.put(i, new PackageLootMenu.PackageItemLootButton(items[i]));
         }
      }

      return buttons;
   }

   @ConstructorProperties({"pack"})
   public PackageLootMenu(Package pack) {
      this.setAutoUpdate(false);
      this.setUpdateAfterClick(false);
      this.pack = pack;
   }

   private static class PackageItemLootButton extends Button {
      private final ItemStack item;

      public ItemStack getButtonItem(Player player) {
         return this.item;
      }

      public boolean shouldCancel(Player player, int slot, ClickType clickType) {
         return !Crates.get().getPackageManager().getEditPackageLoot().contains(player.getUniqueId());
      }

      public boolean shouldShift(Player player, int slot, ClickType clickType) {
         return !Crates.get().getPackageManager().getEditPackageLoot().contains(player.getUniqueId());
      }

      @ConstructorProperties({"item"})
      public PackageItemLootButton(ItemStack item) {
         this.item = item;
      }
   }
}
