package me.traduciendo.crates.partnerpackages.packages;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import me.traduciendo.crates.Crates;
import me.traduciendo.crates.utils.utility.ChatUtil;
import me.traduciendo.crates.utils.utility.PlayerUtil;
import me.traduciendo.crates.utils.utility.file.FileConfig;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class PackageManager {
   private final FileConfig mainConfig = Crates.get().getMainConfig();
   private final FileConfig packagesConfig = Crates.get().getPackagesConfig();
   private final List<Package> packages = Lists.newArrayList();
   private final Map<UUID, Package> editPackage = Maps.newHashMap();
   private final Set<UUID> editPackageName = Sets.newHashSet();
   private final Set<UUID> editPackageLore = Sets.newHashSet();
   private final Set<UUID> editPackageLoot = Sets.newHashSet();

   public void createPackage(String name) {
      this.packages.add(new Package(name, "&b&l" + name + " Package", Arrays.asList("", "&7Rewards: ", "%REWARDS%", "", "&7Right-Click to open " + name + " Package"), 1, Material.ENDER_CHEST, 0, new ItemStack[0]));
      this.packagesConfig.getConfiguration().set("PACKAGES." + name + ".DISPLAYNAME", "&b&l" + name + " Package");
      this.packagesConfig.getConfiguration().set("PACKAGES." + name + ".LORE", Arrays.asList("", "&7Rewards: ", "%REWARDS%", "", "&7Right-Click to open " + name + " Package"));
      this.packagesConfig.getConfiguration().set("PACKAGES." + name + ".PRIZE-AMOUNT", 1);
      this.packagesConfig.getConfiguration().set("PACKAGES." + name + ".ITEM.MATERIAL", Material.ENDER_CHEST.name());
      this.packagesConfig.getConfiguration().set("PACKAGES." + name + ".ITEM.DATA", 0);
      this.packagesConfig.getConfiguration().set("PACKAGES." + name + ".ITEMS", new ItemStack[0]);
      this.packagesConfig.save();
   }

   public void deletePackage(String name) {
      this.packages.remove(this.getPackageByName(name));
      this.packagesConfig.getConfiguration().set("PACKAGES." + name, (Object)null);
      this.packagesConfig.save();
   }

   public void loadPackage() {
      this.getPackages().clear();
      ConfigurationSection section = this.packagesConfig.getConfiguration().getConfigurationSection("PACKAGES");
      section.getKeys(false).forEach((key) -> {
         String displayName = this.packagesConfig.getString("PACKAGES." + key + ".DISPLAYNAME");
         List<String> lore = this.packagesConfig.getStringList("PACKAGES." + key + ".LORE");
         int prizeAmount = this.packagesConfig.getInt("PACKAGES." + key + ".PRIZE-AMOUNT");
         Material material = Material.valueOf(this.packagesConfig.getString("PACKAGES." + key + ".ITEM.MATERIAL"));
         int data = this.packagesConfig.getInt("PACKAGES." + key + ".ITEM.DATA");
         ItemStack[] items = (ItemStack[])((List)this.packagesConfig.getConfiguration().get("PACKAGES." + key + ".ITEMS")).toArray(new ItemStack[0]);
         this.packages.add(new Package(key, displayName, lore, prizeAmount, material, data, items));
      });
   }

   public void getLoot(Player player, Package packageManager) {
      List<ItemStack> items = Lists.newArrayList();
      ItemStack[] var4 = (ItemStack[])packageManager.getItems().clone();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ItemStack itemStack = var4[var6];
         if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
            items.add(itemStack);
         }
      }

      if (items.isEmpty()) {
         ChatUtil.message(player, this.mainConfig.getString("PACKAGE-EMPTY"));
      } else if (PlayerUtil.isInventoryFull(player)) {
         ChatUtil.message(player, this.mainConfig.getString("PACKAGE-INVENTORY-FULL"));
      } else {
         PlayerUtil.decrement(player, player.getItemInHand(), false, false);

         for(int i = 0; i < packageManager.getPrizeAmount(); ++i) {
            ItemStack item = ((ItemStack)items.get(ThreadLocalRandom.current().nextInt(items.size()))).clone();
            if (item.getItemMeta().hasDisplayName()) {
               item.getItemMeta().getDisplayName();
            } else {
               ChatUtil.toReadable(item.getType());
            }

            int amount = item.getAmount();
            player.getInventory().addItem(new ItemStack[]{item});
         }

         player.updateInventory();
      }
   }

   public Package getPackageByName(String name) {
      return (Package)this.packages.stream().filter((packages) -> {
         return packages.getName().equalsIgnoreCase(name);
      }).findFirst().orElse((Package) null);
   }

   public boolean isPackageExist(String name) {
      return this.packages.contains(this.getPackageByName(name));
   }

   public void setDisplayName(Package packageManager, String displayname) {
      packageManager.setDisplayName(displayname);
      this.packagesConfig.getConfiguration().set("PACKAGES." + packageManager.getName() + ".DISPLAYNAME", packageManager.getDisplayName());
      this.packagesConfig.save();
   }

   public void setLore(Package packageManager, List<String> lore) {
      packageManager.setLore(lore);
      this.packagesConfig.getConfiguration().set("PACKAGES." + packageManager.getName() + ".LORE", packageManager.getLore());
      this.packagesConfig.save();
   }

   public void setPrizeAmount(Package packageManager, int amount) {
      packageManager.setPrizeAmount(amount);
      this.packagesConfig.getConfiguration().set("PACKAGES." + packageManager.getName() + ".PRIZE-AMOUNT", packageManager.getPrizeAmount());
      this.packagesConfig.save();
   }

   public void setMaterial(Package packageManager, Material material) {
      packageManager.setMaterial(material);
      this.packagesConfig.getConfiguration().set("PACKAGES." + packageManager.getName() + ".ITEM.MATERIAL", packageManager.getMaterial().name());
      this.packagesConfig.save();
   }

   public void setData(Package packageManager, int data) {
      packageManager.setData(data);
      this.packagesConfig.getConfiguration().set("PACKAGES." + packageManager.getName() + ".ITEM.DATA", packageManager.getData());
      this.packagesConfig.save();
   }

   public void setItems(Package packageManager, ItemStack[] items) {
      packageManager.setItems(items);
      this.packagesConfig.getConfiguration().set("PACKAGES." + packageManager.getName() + ".ITEMS", packageManager.getItems());
      this.packagesConfig.save();
   }

   public int getSpecialSize() {
      if (this.packages.size() <= 9) {
         return 1;
      } else if (this.packages.size() <= 18) {
         return 2;
      } else if (this.packages.size() <= 27) {
         return 3;
      } else if (this.packages.size() <= 36) {
         return 4;
      } else if (this.packages.size() <= 45) {
         return 5;
      } else {
         return this.packages.size() <= 54 ? 6 : 0;
      }
   }

}
