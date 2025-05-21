package me.traduciendo.crates.partnerpackages.packages.menu.buttons.edit;

import me.traduciendo.crates.partnerpackages.packages.Package;
import me.traduciendo.crates.utils.utility.item.ItemBuilder;
import me.traduciendo.crates.utils.utility.menu.Button;
import java.beans.ConstructorProperties;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PackageDisplayButton extends Button {
   private final Package pack;

   public ItemStack getButtonItem(Player player) {
      return (new ItemBuilder(this.pack.getMaterial())).data(this.pack.getData()).name(this.pack.getDisplayName()).lore(this.pack.getLore()).build();
   }

   @ConstructorProperties({"pack"})
   public PackageDisplayButton(Package pack) {
      this.pack = pack;
   }
}
