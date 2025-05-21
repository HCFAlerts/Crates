package me.traduciendo.crates.utils.utility;

import org.bukkit.Bukkit;

public final class Server {
   public static final String SERVER_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);
   public static final int SERVER_VERSION_INT;

   private Server() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static {
      SERVER_VERSION_INT = Integer.parseInt(SERVER_VERSION.replace("1_", "").replaceAll("_R\\d", ""));
   }
}
