package me.traduciendo.crates.partnerpackages.listeners;

import me.traduciendo.crates.Crates;
import me.traduciendo.crates.partnerpackages.packages.Package;
import me.traduciendo.crates.partnerpackages.packages.PackageManager;
import me.traduciendo.crates.partnerpackages.packages.menu.PackageEditMenu;
import me.traduciendo.crates.utils.utility.ChatUtil;
import me.traduciendo.crates.utils.utility.JavaUtil;
import me.traduciendo.crates.utils.utility.file.FileConfig;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PackageListener implements Listener {
   private final Crates plugin = Crates.get();
   private final PackageManager packageManager = Crates.get().getPackageManager();
   private final FileConfig mainConfig = Crates.get().getMainConfig();

   public PackageListener() {
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
   }

   @EventHandler
   private void onInteract(PlayerInteractEvent event) {
      if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
         if (event.getItem() == null || event.getItem().getType().equals(Material.AIR)) {
            return;
         }

         this.packageManager.getPackages().forEach((pack) -> {
            if (event.getItem().isSimilar(pack.getPackageItem(event.getItem().getAmount()))) {
               event.setCancelled(true);
               this.packageManager.getLoot(event.getPlayer(), pack);
            }

         });
      }

   }

   @EventHandler
   private void onPackageName(AsyncPlayerChatEvent event) {
      Player player = event.getPlayer();
      if (this.packageManager.getEditPackageName().contains(player.getUniqueId())) {
         event.setCancelled(true);
         Package pack = (Package)this.packageManager.getEditPackage().get(player.getUniqueId());
         String name = pack.getDisplayName();
         String message = event.getMessage();
         if (message.equalsIgnoreCase("cancel")) {
            (new PackageEditMenu(pack)).openMenu(player);
            this.packageManager.getEditPackage().remove(player.getUniqueId());
            this.packageManager.getEditPackageName().remove(player.getUniqueId());
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
            ChatUtil.message(player, "&cPackage name process has been cancelled.");
            return;
         }

         this.packageManager.setDisplayName(pack, message);
         this.packageManager.getEditPackage().remove(player.getUniqueId());
         this.packageManager.getEditPackageName().remove(player.getUniqueId());
         (new PackageEditMenu(pack)).openMenu(player);
         player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1.0F, 1.0F);
         ChatUtil.message(player, "&ePackage name has been changed from '&r" + name + "&e' to '&r" + message + "&e'.");
      }

   }

   @EventHandler
   private void onPackageLore(AsyncPlayerChatEvent event) {
      Player player = event.getPlayer();
      if (this.packageManager.getEditPackageLore().contains(player.getUniqueId())) {
         event.setCancelled(true);
         String[] args = event.getMessage().split(" ");
         Package pack;
         List lore;
         if (args[0].equalsIgnoreCase("add")) {
            if (args.length < 2) {
               ChatUtil.message(player, "&cPlease enter a lore.");
               return;
            }

            pack = (Package)this.packageManager.getEditPackage().get(player.getUniqueId());
            lore = pack.getNormalLore();
            String addLore = StringUtils.join(args, ' ', 1, args.length);
            lore.add(ChatUtil.translate(addLore));
            this.packageManager.setLore(pack, lore);
            ChatUtil.message(player, "&eSuccessfully added '&f" + addLore + "&e' to lore.");
         } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length < 2) {
               ChatUtil.message(player, "&cPlease enter a number or type 'all' to remove all lore.");
               return;
            }

            pack = (Package)this.packageManager.getEditPackage().get(player.getUniqueId());
            lore = pack.getNormalLore();
            if (args[1].equalsIgnoreCase("all")) {
               if (lore.isEmpty()) {
                  ChatUtil.message(player, "&cThis ability not have lore.");
                  return;
               }

               lore.clear();
               this.packageManager.setLore(pack, lore);
               ChatUtil.message(player, "&eSuccessfully remove all lore.");
               return;
            }

            int index = JavaUtil.tryParseInt(args[1]);
            if (lore.size() <= index || index < 0) {
               ChatUtil.message(player, "&cLore number '" + index + "' not found.");
               return;
            }

            lore.remove(index);
            this.packageManager.setLore(pack, lore);
            ChatUtil.message(player, "&eSuccessfully removed lore with number &a" + index + "&e.");
         } else if (args[0].equalsIgnoreCase("list")) {
            pack = (Package)this.packageManager.getEditPackage().get(player.getUniqueId());
            lore = pack.getNormalLore();
            ChatUtil.message(player, ChatUtil.NORMAL_LINE);
            ChatUtil.message(player, "&b&lLore List");
            ChatUtil.message(player, "");
            if (lore.isEmpty()) {
               ChatUtil.message(player, "&cThis ability not have lore.");
            } else {
               AtomicInteger count = new AtomicInteger();
               lore.forEach((lines) -> {
                  ChatUtil.message(player, "&7[&b" + count + "&7] &r" + lines);
                  count.getAndIncrement();
               });
            }

            ChatUtil.message(player, ChatUtil.NORMAL_LINE);
         } else if (args[0].equalsIgnoreCase("cancel")) {
            pack = (Package)this.packageManager.getEditPackage().get(player.getUniqueId());
            (new PackageEditMenu(pack)).openMenu(player);
            this.packageManager.getEditPackage().remove(player.getUniqueId());
            this.packageManager.getEditPackageLoot().remove(player.getUniqueId());
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
            ChatUtil.message(player, "&cPackage lore process has been cancelled.");
         } else {
            ChatUtil.message(player, "&cPlease type a correct command message.");
         }
      }

   }

   @EventHandler
   private void onCloseInventory(InventoryCloseEvent event) {
      Player player = (Player)event.getPlayer();
      if (this.packageManager.getEditPackageLoot().contains(player.getUniqueId())) {
         ItemStack[] contents = event.getInventory().getContents();
         Package pack = (Package)this.packageManager.getEditPackage().get(player.getUniqueId());
         this.packageManager.setItems(pack, contents);
         this.packageManager.getEditPackage().remove(player.getUniqueId());
         this.packageManager.getEditPackageLoot().remove(player.getUniqueId());
         ChatUtil.message(player, this.mainConfig.getString("PACKAGE-EDIT-LOOT").replace("%PACKAGE%", pack.getName()));
         this.plugin.getTaskManager().runLaterAsync(() -> {
            (new PackageEditMenu(pack)).openMenu(player);
         }, 1L);
      }

   }

   public Crates getPlugin() {
      return this.plugin;
   }

   public PackageManager getPackageManager() {
      return this.packageManager;
   }

   public FileConfig getMainConfig() {
      return this.mainConfig;
   }
}
