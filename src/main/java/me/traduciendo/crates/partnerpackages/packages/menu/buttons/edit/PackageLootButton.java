package me.traduciendo.crates.partnerpackages.packages.menu.buttons.edit;

import me.traduciendo.crates.Crates;
import me.traduciendo.crates.partnerpackages.packages.Package;
import me.traduciendo.crates.partnerpackages.packages.PackageManager;
import me.traduciendo.crates.partnerpackages.packages.menu.PackageLootMenu;
import me.traduciendo.crates.utils.utility.item.ItemBuilder;
import me.traduciendo.crates.utils.utility.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PackageLootButton extends Button {
   private final Package pack;
   private final PackageManager packageManager = Crates.get().getPackageManager();

   public PackageLootButton(Package pack) {
      this.pack = pack;
   }

   public ItemStack getButtonItem(Player player) {
      return (new ItemBuilder(Material.CHEST)).name("&aChange Loot").lore("&8&m-----------------------------", "&7Change the loot of the package.", "", "&eLeft-Click to edit loot.", "&eRight-Click to preview loot.", "&8&m------------------------------").build();
   }

   public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
      (new PackageLootMenu(this.pack)).openMenu(player);
      if (clickType.isLeftClick()) {
         this.packageManager.getEditPackage().put(player.getUniqueId(), this.pack);
         this.packageManager.getEditPackageLoot().add(player.getUniqueId());
      }

   }
}
