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

public class PackagePrizeAmountButton extends Button {
   private final Package pack;
   private final PackageManager packageManager = Crates.get().getPackageManager();

   public PackagePrizeAmountButton(Package pack) {
      this.pack = pack;
   }

   public ItemStack getButtonItem(Player player) {
      return (new ItemBuilder(Material.DIAMOND)).name("&aChange Prize Amount").lore("&8&m-------------------------------", "&7Change the prize amount of the package.", "", "&7Prize: &r" + this.pack.getPrizeAmount(), "", "&eRight-Click to increase amount.", "&eMiddle-Click to reset amount.", "&eLeft-Click to decrease amount.", "&8&m-------------------------------").build();
   }

   public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
      if (clickType.isRightClick()) {
         this.packageManager.setPrizeAmount(this.pack, this.pack.getPrizeAmount() + 1);
         playSuccess(player);
      }

      if (clickType.isCreativeAction()) {
         this.packageManager.setPrizeAmount(this.pack, 1);
         playNeutral(player);
      }

      if (clickType.isLeftClick()) {
         if (this.pack.getPrizeAmount() <= 1) {
            playFail(player);
            return;
         }

         this.packageManager.setPrizeAmount(this.pack, this.pack.getPrizeAmount() - 1);
         playSuccess(player);
      }

   }
}
