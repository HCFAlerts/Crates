package me.traduciendo.crates.partnerpackages.packages.menu;

import com.google.common.collect.Maps;
import me.traduciendo.crates.partnerpackages.packages.Package;
import me.traduciendo.crates.partnerpackages.packages.menu.buttons.edit.PackageDisplayButton;
import me.traduciendo.crates.partnerpackages.packages.menu.buttons.edit.PackageIconButton;
import me.traduciendo.crates.partnerpackages.packages.menu.buttons.edit.PackageLootButton;
import me.traduciendo.crates.partnerpackages.packages.menu.buttons.edit.PackageLoreButton;
import me.traduciendo.crates.partnerpackages.packages.menu.buttons.edit.PackageNameButton;
import me.traduciendo.crates.partnerpackages.packages.menu.buttons.edit.PackagePrizeAmountButton;
import me.traduciendo.crates.utils.utility.menu.Button;
import me.traduciendo.crates.utils.utility.menu.Menu;
import me.traduciendo.crates.utils.utility.menu.buttons.BackButton;
import java.beans.ConstructorProperties;
import java.util.Map;
import org.bukkit.entity.Player;

public class PackageEditMenu extends Menu {
   private final Package pack;

   public String getTitle(Player player) {
      return this.pack.getName() + " Package";
   }

   public int getSize() {
      return 45;
   }

   public Map<Integer, Button> getButtons(Player player) {
      Map<Integer, Button> buttons = Maps.newHashMap();
      buttons.put(4, new PackageDisplayButton(this.pack));
      buttons.put(20, new PackageNameButton(this.pack));
      buttons.put(21, new PackageIconButton(this.pack));
      buttons.put(22, new PackageLoreButton(this.pack));
      buttons.put(23, new PackagePrizeAmountButton(this.pack));
      buttons.put(24, new PackageLootButton(this.pack));
      buttons.put(this.getSize() - 5, new BackButton(new PackageMenu()));
      return buttons;
   }

   @ConstructorProperties({"pack"})
   public PackageEditMenu(Package pack) {
      this.setAutoUpdate(false);
      this.setUpdateAfterClick(true);
      this.pack = pack;
   }
}
