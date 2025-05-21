package me.traduciendo.crates.partnerpackages.packages.menu;

import com.google.common.collect.Maps;
import me.traduciendo.crates.Crates;
import me.traduciendo.crates.partnerpackages.packages.Package;
import me.traduciendo.crates.partnerpackages.packages.PackageManager;
import me.traduciendo.crates.partnerpackages.packages.menu.buttons.PackageButton;
import me.traduciendo.crates.utils.utility.menu.Button;
import me.traduciendo.crates.utils.utility.menu.Menu;
import java.util.Map;
import org.bukkit.entity.Player;

public class PackageMenu extends Menu {
   private final PackageManager packageManager = Crates.get().getPackageManager();

   public String getTitle(Player player) {
      return "Packages";
   }

   public int getSize() {
      return this.packageManager.getSpecialSize() * 9;
   }

   public Map<Integer, Button> getButtons(Player player) {
      Map<Integer, Button> buttons = Maps.newHashMap();

      for(int i = 0; i < this.packageManager.getPackages().size(); ++i) {
         buttons.put(i, new PackageButton((Package)this.packageManager.getPackages().get(i)));
      }

      return buttons;
   }
}
