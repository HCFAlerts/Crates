package me.traduciendo.crates.partnerpackages.packages.menu.buttons.edit;

import me.traduciendo.crates.Crates;
import me.traduciendo.crates.partnerpackages.packages.Package;
import me.traduciendo.crates.partnerpackages.packages.PackageManager;
import me.traduciendo.crates.utils.utility.ChatUtil;
import me.traduciendo.crates.utils.utility.item.ItemBuilder;
import me.traduciendo.crates.utils.utility.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PackageLoreButton extends Button {
   private final Package pack;
   private final PackageManager packageManager = Crates.get().getPackageManager();

   public PackageLoreButton(Package pack) {
      this.pack = pack;
   }

   public ItemStack getButtonItem(Player player) {
      return (new ItemBuilder(Material.BOOK_AND_QUILL)).name("&aChange Description").lore("&8&m------------------------------------", "&7Change the description of the package.", "", "&eClick to edit description.", "&8&m------------------------------------").build();
   }

   public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
      this.packageManager.getEditPackageLore().add(player.getUniqueId());
      this.packageManager.getEditPackage().put(player.getUniqueId(), this.pack);
      playSuccess(player);
      ChatUtil.message(player, "&eYou're now editing lore of '&f" + this.pack.getName() + "'&e.");
      ChatUtil.message(player, "");
      ChatUtil.message(player, "&eAvailables command messages");
      ChatUtil.message(player, "&7▶ &fadd <lore>");
      ChatUtil.message(player, "&7▶ &fremove <number|all>");
      ChatUtil.message(player, "&7▶ &flist");
      ChatUtil.message(player, "");
      ChatUtil.message(player, "&eType &c'Cancel'&e in the chat to cancel the process.");
      player.closeInventory();
   }
}
