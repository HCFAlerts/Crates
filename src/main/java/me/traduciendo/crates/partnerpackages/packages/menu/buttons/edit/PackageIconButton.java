package me.traduciendo.crates.partnerpackages.packages.menu.buttons.edit;

import me.traduciendo.crates.Crates;
import me.traduciendo.crates.partnerpackages.packages.Package;
import me.traduciendo.crates.partnerpackages.packages.PackageManager;
import me.traduciendo.crates.utils.utility.item.ItemBuilder;
import me.traduciendo.crates.utils.utility.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PackageIconButton extends Button {
   private final Package pack;
   private final PackageManager packageManager = Crates.get().getPackageManager();

   public PackageIconButton(Package pack) {
      this.pack = pack;
   }

   public ItemStack getButtonItem(Player player) {
      return (new ItemBuilder(Material.ITEM_FRAME)).name("&aChange Icon").lore("&8&m-----------------------------", "&7Change the icon of the package.", "", "&eDrag an item to set as the icon.", "&8&m------------------------------").build();
   }

   public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
      ItemStack packageItem = (new ItemBuilder(this.pack.getMaterial())).data(this.pack.getData()).build();
      if (player.getItemOnCursor() != null && !player.getItemOnCursor().getType().equals(Material.AIR) && !player.getItemOnCursor().isSimilar(packageItem)) {
         this.packageManager.setMaterial(this.pack, player.getItemOnCursor().getType());
         this.packageManager.setData(this.pack, player.getItemOnCursor().getDurability());
         playSuccess(player);
      }
   }
}
