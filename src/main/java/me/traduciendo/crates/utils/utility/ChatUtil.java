package me.traduciendo.crates.utils.utility;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ChatUtil {
   public static String LONG_LINE = "&7&m----------------------------------------";
   public static String NORMAL_LINE = "&7&m-----------------------------";
   public static String SHORT_LINE = "&7&m---------------";
   public static String BLUE;
   public static String AQUA;
   public static String YELLOW;
   public static String RED;
   public static String GRAY;
   public static String GOLD;
   public static String GREEN;
   public static String WHITE;
   public static String BLACK;
   public static String BOLD;
   public static String ITALIC;
   public static String UNDER_LINE;
   public static String STRIKE_THROUGH;
   public static String RESET;
   public static String MAGIC;
   public static String DARK_BLUE;
   public static String DARK_AQUA;
   public static String DARK_GRAY;
   public static String DARK_GREEN;
   public static String DARK_PURPLE;
   public static String DARK_RED;
   public static String PINK;

   public static String translate(String text) {
      return ChatColor.translateAlternateColorCodes('&', text);
   }

   public static List<String> translate(List<String> list) {
      return (List)list.stream().map(ChatUtil::translate).collect(Collectors.toList());
   }

   public static String strip(String text) {
      return ChatColor.stripColor(text);
   }

   public static void sender(CommandSender sender, String text) {
      sender.sendMessage(translate(text));
   }

   public static void message(Player player, String text) {
      player.sendMessage(translate(text));
   }

   public static void broadcast(String text) {
      Bukkit.broadcastMessage(translate(text));
   }

   public static void log(String text) {
      Bukkit.getConsoleSender().sendMessage(translate(text));
   }

   public static String capitalize(String str) {
      return WordUtils.capitalize(str);
   }

   public static String toReadable(Enum<?> enu) {
      return WordUtils.capitalize(enu.name().replace("_", " ").toLowerCase());
   }

   private ChatUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static {
      BLUE = ChatColor.BLUE.toString();
      AQUA = ChatColor.AQUA.toString();
      YELLOW = ChatColor.YELLOW.toString();
      RED = ChatColor.RED.toString();
      GRAY = ChatColor.GRAY.toString();
      GOLD = ChatColor.GOLD.toString();
      GREEN = ChatColor.GREEN.toString();
      WHITE = ChatColor.WHITE.toString();
      BLACK = ChatColor.BLACK.toString();
      BOLD = ChatColor.BOLD.toString();
      ITALIC = ChatColor.ITALIC.toString();
      UNDER_LINE = ChatColor.UNDERLINE.toString();
      STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString();
      RESET = ChatColor.RESET.toString();
      MAGIC = ChatColor.MAGIC.toString();
      DARK_BLUE = ChatColor.DARK_BLUE.toString();
      DARK_AQUA = ChatColor.DARK_AQUA.toString();
      DARK_GRAY = ChatColor.DARK_GRAY.toString();
      DARK_GREEN = ChatColor.DARK_GREEN.toString();
      DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
      DARK_RED = ChatColor.DARK_RED.toString();
      PINK = ChatColor.LIGHT_PURPLE.toString();
   }
}
