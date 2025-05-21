package me.traduciendo.crates.utils.utility.menu;

import me.traduciendo.crates.Crates;
import me.traduciendo.crates.utils.utility.ChatUtil;
import me.traduciendo.crates.utils.utility.item.ItemBuilder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public abstract class Menu {
   public static Map<String, Menu> currentlyOpenedMenus = new HashMap();
   private Map<Integer, Button> buttons = new HashMap();
   private boolean autoUpdate = false;
   private boolean updateAfterClick = true;
   private boolean closedByMenu = false;
   private boolean placeholder = false;
   private Button placeholderButton;
   private BukkitTask task;

   public Menu() {
      this.placeholderButton = Button.placeholder(Material.STAINED_GLASS_PANE, 7, " ");
   }

   private ItemStack createItemStack(Player player, Button button) {
      return (new ItemBuilder(button.getButtonItem(player))).build();
   }

   public void openMenu(Player player) {
      this.buttons = this.getButtons(player);
      Menu previousMenu = (Menu)currentlyOpenedMenus.get(player.getName());
      Inventory inventory = null;
      String title = ChatUtil.translate(this.getTitle(player));
      int size = this.getSize() == -1 ? this.size(this.buttons) : this.getSize();
      boolean update = false;
      if (title.length() > 32) {
         title = title.substring(0, 32);
      }

      if (player.getOpenInventory() != null) {
         if (previousMenu == null) {
            player.closeInventory();
         } else {
            int previousSize = player.getOpenInventory().getTopInventory().getSize();
            if (previousSize == size && player.getOpenInventory().getTopInventory().getTitle().equals(title)) {
               inventory = player.getOpenInventory().getTopInventory();
               update = true;
            } else {
               previousMenu.setClosedByMenu(true);
               player.closeInventory();
            }
         }
      }

      if (inventory == null) {
         inventory = Bukkit.createInventory(player, size, title);
      }

      inventory.setContents(new ItemStack[inventory.getSize()]);
      currentlyOpenedMenus.put(player.getName(), this);
      Iterator var9 = this.buttons.entrySet().iterator();

      while(var9.hasNext()) {
         Entry<Integer, Button> buttonEntry = (Entry)var9.next();
         inventory.setItem((Integer)buttonEntry.getKey(), this.createItemStack(player, (Button)buttonEntry.getValue()));
      }

      if (this.isPlaceholder()) {
         Button fillButton = this.getPlaceholderButton() == null ? this.placeholderButton : this.getPlaceholderButton();

         for(int index = 0; index < size; ++index) {
            if (this.buttons.get(index) == null) {
               this.buttons.put(index, fillButton);
               inventory.setItem(index, fillButton.getButtonItem(player));
            }
         }
      }

      if (update) {
         player.updateInventory();
      } else {
         player.openInventory(inventory);
      }

      this.setClosedByMenu(false);
      if (this.autoUpdate && this.task == null) {
         this.task = Crates.get().getServer().getScheduler().runTaskTimer(Crates.get(), () -> {
            this.openMenu(player);
         }, 0L, 20L);
      }

   }

   public int size(Map<Integer, Button> buttons) {
      int highest = 0;
      Iterator var3 = buttons.keySet().iterator();

      while(var3.hasNext()) {
         int buttonValue = (Integer)var3.next();
         if (buttonValue > highest) {
            highest = buttonValue;
         }
      }

      return (int)(Math.ceil((double)(highest + 1) / 9.0D) * 9.0D);
   }

   public int getSlot(int x, int y) {
      return 9 * y + x;
   }

   public int getSize() {
      return -1;
   }

   public Button getPlaceholderButton() {
      return null;
   }

   public abstract String getTitle(Player var1);

   public abstract Map<Integer, Button> getButtons(Player var1);

   public void onClose(Player player) {
      if (this.task != null) {
         this.task.cancel();
      }

   }

   public boolean isAutoUpdate() {
      return this.autoUpdate;
   }

   public boolean isUpdateAfterClick() {
      return this.updateAfterClick;
   }

   public boolean isClosedByMenu() {
      return this.closedByMenu;
   }

   public boolean isPlaceholder() {
      return this.placeholder;
   }

   public BukkitTask getTask() {
      return this.task;
   }

   public void setButtons(Map<Integer, Button> buttons) {
      this.buttons = buttons;
   }

   public void setAutoUpdate(boolean autoUpdate) {
      this.autoUpdate = autoUpdate;
   }

   public void setUpdateAfterClick(boolean updateAfterClick) {
      this.updateAfterClick = updateAfterClick;
   }

   public void setClosedByMenu(boolean closedByMenu) {
      this.closedByMenu = closedByMenu;
   }

   public void setPlaceholder(boolean placeholder) {
      this.placeholder = placeholder;
   }

   public void setPlaceholderButton(Button placeholderButton) {
      this.placeholderButton = placeholderButton;
   }

   public void setTask(BukkitTask task) {
      this.task = task;
   }

   public Map<Integer, Button> getButtons() {
      return this.buttons;
   }
}
