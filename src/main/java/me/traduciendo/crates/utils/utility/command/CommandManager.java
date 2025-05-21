package me.traduciendo.crates.utils.utility.command;

import me.traduciendo.crates.utils.utility.ChatUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager implements CommandExecutor {
   private final Map<String, Entry<Method, Object>> commandMap = new HashMap();
   private final JavaPlugin plugin;
   public static CommandManager instance;
   private CommandMap map;

   public static CommandManager getInstance() {
      return instance;
   }

   public CommandManager(JavaPlugin plugin) {
      instance = this;
      this.plugin = plugin;
      if (plugin.getServer().getPluginManager() instanceof SimplePluginManager) {
         SimplePluginManager manager = (SimplePluginManager)plugin.getServer().getPluginManager();

         try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            this.map = (CommandMap)field.get(manager);
         } catch (SecurityException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException var4) {
            var4.printStackTrace();
         }
      }

   }

   public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
      return this.handleCommand(sender, cmd, label, args);
   }

   public boolean handleCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
      for(int i = args.length; i >= 0; --i) {
         StringBuffer buffer = new StringBuffer();
         buffer.append(label.toLowerCase());

         for(int x = 0; x < i; ++x) {
            buffer.append("." + args[x].toLowerCase());
         }

         String cmdLabel = buffer.toString();
         if (this.commandMap.containsKey(cmdLabel)) {
            Method method = (Method)((Entry)this.commandMap.get(cmdLabel)).getKey();
            Object methodObject = ((Entry)this.commandMap.get(cmdLabel)).getValue();
            Command command = (Command)method.getAnnotation(Command.class);
            if (!command.permission().equals("") && !sender.hasPermission(command.permission())) {
               sender.sendMessage(ChatUtil.translate("&cNo permission."));
               return true;
            }

            if (command.inGameOnly() && !(sender instanceof Player)) {
               sender.sendMessage(ChatUtil.translate("&cThis command in only executable in game."));
               return true;
            }

            try {
               method.invoke(methodObject, new CommandArgs(sender, cmd, label, args, cmdLabel.split("\\.").length - 1));
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException var12) {
               var12.printStackTrace();
            }

            return true;
         }
      }

      return true;
   }

   public void registerCommands(Object obj, List<String> aliases) {
      Method[] var3 = obj.getClass().getMethods();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Method m = var3[var5];
         if (m.getAnnotation(Command.class) != null) {
            Command command = (Command)m.getAnnotation(Command.class);
            if (m.getParameterTypes().length <= 1 && m.getParameterTypes()[0] == CommandArgs.class) {
               this.registerCommand(command, command.name(), m, obj);
               String[] var8 = command.aliases();
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  String alias = var8[var10];
                  this.registerCommand(command, alias, m, obj);
               }

               if (aliases != null) {
                  Iterator var12 = aliases.iterator();

                  while(var12.hasNext()) {
                     String alias = (String)var12.next();
                     this.registerCommand(command, alias, m, obj);
                  }
               }
            } else {
               System.out.println("Unable to register command " + m.getName() + ". Unexpected method arguments");
            }
         }
      }

   }

   public void registerHelp() {
      Set<HelpTopic> help = new TreeSet(HelpTopicComparator.helpTopicComparatorInstance());
      Iterator var2 = this.commandMap.keySet().iterator();

      while(var2.hasNext()) {
         String s = (String)var2.next();
         if (!s.contains(".")) {
            org.bukkit.command.Command cmd = this.map.getCommand(s);
            HelpTopic topic = new GenericCommandHelpTopic(cmd);
            help.add(topic);
         }
      }

      IndexHelpTopic topic = new IndexHelpTopic(this.plugin.getName(), "All commands for " + this.plugin.getName(), (String)null, help, "Below is a list of all " + this.plugin.getName() + " commands:");
      Bukkit.getServer().getHelpMap().addTopic(topic);
   }

   public void unregisterCommands(Object obj) {
      Method[] var2 = obj.getClass().getMethods();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Method m = var2[var4];
         if (m.getAnnotation(Command.class) != null) {
            Command command = (Command)m.getAnnotation(Command.class);
            this.commandMap.remove(command.name().toLowerCase());
            this.commandMap.remove(this.plugin.getName() + ":" + command.name().toLowerCase());
            this.map.getCommand(command.name().toLowerCase()).unregister(this.map);
         }
      }

   }

   public void registerCommand(Command command, String label, Method m, Object obj) {
      this.commandMap.put(label.toLowerCase(), new SimpleEntry(m, obj));
      this.commandMap.put(this.plugin.getName() + ':' + label.toLowerCase(), new SimpleEntry(m, obj));
      String cmdLabel = label.replace(".", ",").split(",")[0].toLowerCase();
      if (this.map.getCommand(cmdLabel) == null) {
         org.bukkit.command.Command cmd = new BukkitCommand(cmdLabel, this, this.plugin);
         this.map.register(this.plugin.getName(), cmd);
      }

      if (!command.description().equalsIgnoreCase("") && cmdLabel.equals(label)) {
         this.map.getCommand(cmdLabel).setDescription(command.description());
      }

      if (!command.usage().equalsIgnoreCase("") && cmdLabel.equals(label)) {
         this.map.getCommand(cmdLabel).setUsage(command.usage());
      }

   }
}
