package me.traduciendo.crates.utils.utility;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class PlayerUtil {
   public static int getPing(Player player) {
      try {
         String a = Bukkit.getServer().getClass().getPackage().getName().substring(23);
         Class<?> b = Class.forName("org.bukkit.craftbukkit." + a + ".entity.CraftPlayer");
         Object c = b.getMethod("getHandle").invoke(player);
         return (Integer)c.getClass().getDeclaredField("ping").get(c);
      } catch (Exception var4) {
         return 0;
      }
   }

   public static boolean isInventoryFull(Player player) {
      return player.getInventory().firstEmpty() < 0;
   }

   public static void decrement(Player player, ItemStack itemStack, boolean sound, boolean cursor) {
      if (sound) {
         player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1.0F, 1.0F);
      }

      if (itemStack.getAmount() <= 1) {
         if (cursor) {
            player.setItemOnCursor((ItemStack)null);
         } else {
            player.setItemInHand((ItemStack)null);
         }
      } else {
         itemStack.setAmount(itemStack.getAmount() - 1);
      }

      player.updateInventory();
   }

   private PlayerUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
