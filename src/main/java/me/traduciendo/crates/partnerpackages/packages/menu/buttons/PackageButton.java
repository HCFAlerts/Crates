package me.traduciendo.crates.partnerpackages.packages.menu.buttons;

import me.traduciendo.crates.partnerpackages.packages.Package;
import me.traduciendo.crates.partnerpackages.packages.menu.PackageEditMenu;
import me.traduciendo.crates.utils.utility.item.ItemBuilder;
import me.traduciendo.crates.utils.utility.menu.Button;
import java.beans.ConstructorProperties;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PackageButton extends Button {
   private final Package pack;

   public ItemStack getButtonItem(Player player) {
      return (new ItemBuilder(this.pack.getMaterial())).data(this.pack.getData()).name("&b" + this.pack.getName()).lore("&7&m--------------------------------", "&b&l┃ &eRight-Click to edit this package", "&7&m--------------------------------").build();
   }

   public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
      (new PackageEditMenu(this.pack)).openMenu(player);
      playSuccess(player);
   }

   @ConstructorProperties({"pack"})
   public PackageButton(Package pack) {
      this.pack = pack;
   }
}
