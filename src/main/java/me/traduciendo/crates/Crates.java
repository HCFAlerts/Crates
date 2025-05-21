package me.traduciendo.crates;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import lombok.Setter;
import me.traduciendo.crates.airdrops.rewards.Airdrop;
import me.traduciendo.crates.airdrops.commands.AirdropCommand;
import me.traduciendo.crates.airdrops.listeners.AirdropListener;
import me.traduciendo.crates.airdrops.manager.AirdropManager;
import me.traduciendo.crates.airdrops.manager.impl.AirdropManager_v1_8_8;
import me.traduciendo.crates.airdrops.util.file.ConfigFile;
import me.traduciendo.crates.crate.listeners.Crate;
import me.traduciendo.crates.crate.listeners.CrateHologramListener;
import me.traduciendo.crates.crate.listeners.CrateListener;
import me.traduciendo.crates.crate.managers.CrateManager;
import me.traduciendo.crates.luckyblocks.commands.LuckyBlocksCommand;
import me.traduciendo.crates.luckyblocks.listeners.OnPlace;
import me.traduciendo.crates.mysterybox.commands.MysteryBoxCommand;
import me.traduciendo.crates.mysterybox.listeners.MysteryBoxListener;
import me.traduciendo.crates.partnerpackages.commands.PackageCommand;
import me.traduciendo.crates.partnerpackages.listeners.PackageListener;
import me.traduciendo.crates.partnerpackages.packages.PackageManager;
import me.traduciendo.crates.stelarbox.commands.StelarBoxCommand;
import me.traduciendo.crates.stelarbox.listeners.StelarBoxListener;
import me.traduciendo.crates.stelarbox.utils.Glow;
import me.traduciendo.crates.stelarbox.utils.Plugin;
import me.traduciendo.crates.stelarbox.utils.files.DataFile;
import me.traduciendo.crates.utils.utility.ASAnimation;
import me.traduciendo.crates.utils.utility.ChatUtil;
import me.traduciendo.crates.utils.utility.command.CommandManager;
import me.traduciendo.crates.utils.utility.file.FileConfig;
import me.traduciendo.crates.utils.utility.menu.ButtonListener;
import me.traduciendo.crates.utils.utility.task.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.traduciendo.crates.crate.command.CrateCommand;
import me.traduciendo.crates.crate.menu.CrateEditMenu;
import me.traduciendo.crates.utils.util.config.Configuration;
import me.traduciendo.crates.utils.util.menu.MenuListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class Crates extends JavaPlugin {
	private ConfigFile airdropsFile;
	private FileConfig mainConfig;
	private FileConfig airdropsConfig;
	private FileConfig luckyblocksConfig;
	private FileConfig packagesConfig;
	private FileConfig exampleConfig;
	private PackageManager packageManager;
	private TaskManager taskManager;
	private CommandManager commandManager;
	@Getter private static Crates instance;
	private Configuration configuration;
	private CrateManager crateManager;
	private AirdropManager airdropManager;
	private ASAnimation armorstand;
	private Map<String, FileConfiguration> lbConfigs = new HashMap<>();

	@Override
	public void onEnable() {
		instance = this;
		loadFiles();
		ConfigurationSerialization.registerClass(Crate.class);
		this.mainConfig = new FileConfig(this, "config.yml");
		this.loadConfigs();
		this.loadManagers();
		this.loadListeners();
		this.loadCommands();
		ChatUtil.log("&3&m==============================");
		ChatUtil.log("&bCrates &8- &fv" + getDescription().getVersion());
		ChatUtil.log("");
		ChatUtil.log("&bPlugin Info");
		ChatUtil.log("&b&l┃ &bAuthor&f: Traduciendo");
		ChatUtil.log("&b&l┃ &bState&f: &aEnabled");
		ChatUtil.log("");
		ChatUtil.log("&bLoaded Info");
		ChatUtil.log("&b&l┃ &bAirdrops&f: &a✅");
		ChatUtil.log("&b&l┃ &bCrates&f: &a✅");
		ChatUtil.log("&b&l┃ &bLuckyBlocks&f: &a✅");
		ChatUtil.log("&b&l┃ &bMysteryBoxes&f: &a✅");
		ChatUtil.log("&b&l┃ &bPackages&f: &a✅");
		ChatUtil.log("&b&l┃ &bStelarBoxes&f: &a✅");
		ChatUtil.log("");
		ChatUtil.log("&7&oThis plugins has been made on");
		ChatUtil.log("&7&oLite Club Development (" + getDescription().getWebsite() + ")");
		ChatUtil.log("&3&m==============================");
		// Airdrops
		// TODO:
		//  EN: Fix the "No correct airdrop configuration" error and remove the posibility of get the dispenser on broken.
		//  ES: Arreglar el error de "Configuracion incorrecta del airdrop" y quitar la posibilidad de que te de el dispensador al romperlo.
		getCommand("airdrop").setExecutor(new AirdropCommand());
		getServer().getPluginManager().registerEvents(new AirdropListener(),this);
		Airdrop.loadAirdrops();
		if(getServer().getVersion().contains("1.8")){
			this.airdropManager = new AirdropManager_v1_8_8(this);
		}
		// LuckyBlocks
		// TODO:
		//  EN: Add editor menu for luckyblocks, to edit rewards and more.
		//  ES: Agregar un editor menu para los luckyblocks, para editar las recompensas y mas.
		getCommand("luckyblocks").setExecutor(new LuckyBlocksCommand(this));
		Bukkit.getPluginManager().registerEvents(new OnPlace(this), this);
		loadLuckyBlocks();
		// MysteryBoxes
		this.getCommand("mysterybox").setExecutor(new MysteryBoxCommand());
		this.getServer().getPluginManager().registerEvents(new MysteryBoxListener(), this);
		// StelarBoxes
		getData();
		Glow.registerGlow();
		Plugin.registerListeners(Collections.singletonList(new StelarBoxListener()));
		this.getCommand("stelarbox").setExecutor(new StelarBoxCommand());
		// Packages
		this.getPackageManager().loadPackage();
		// Crates
		this.configuration = new Configuration(this, "crates.yml");
		this.crateManager = new CrateManager();
		this.getCommand("crates").setExecutor(new CrateCommand());
		PluginManager manager = Bukkit.getServer().getPluginManager();
		Arrays.asList(
				new CrateListener(),
				// TODO:
				//  EN: Fix null hologram location on load holograms.
				//   Crate successfully spawn, if hologram don't spawn
				//  ES: Arreglar la ubicacion nula de los hologramas al cargar estos mismos.
				//   Crate spawnea correctamente, pero el holograma no spawnea.
				new CrateHologramListener(),
				new CrateEditMenu(),
				new MenuListener()
				).stream().forEach(listener -> manager.registerEvents(listener, this));
	}

	@Override
	public void onDisable() {
		// Crates
		this.crateManager.save();
		// Airdrops
		Airdrop.saveAll();
		// Packages, LuckyBlocks, MysteryBoxes and StelarBoxes have auto-saved method.
		ChatUtil.log("&3&m==============================");
		ChatUtil.log("&bCrates &8- &fv" + getDescription().getVersion());
		ChatUtil.log("");
		ChatUtil.log("&bPlugin Info");
		ChatUtil.log("&b&l┃ &bAuthor&f: Traduciendo");
		ChatUtil.log("&b&l┃ &bState&f: &cDisabled");
		ChatUtil.log("");
		ChatUtil.log("&bSaved Info");
		ChatUtil.log("&b&l┃ &bAirdrops&f: &a✅");
		ChatUtil.log("&b&l┃ &bCrates&f: &a✅");
		ChatUtil.log("&b&l┃ &bLuckyBlocks&f: &a✅");
		ChatUtil.log("&b&l┃ &bMysteryBoxes&f: &a✅");
		ChatUtil.log("&b&l┃ &bPackages&f: &a✅");
		ChatUtil.log("&b&l┃ &bStelarBoxes&f: &a✅");
		ChatUtil.log("");
		ChatUtil.log("&7&oThis plugins has been made on");
		ChatUtil.log("&7&oLite Club Development (" + getDescription().getWebsite() + ")");
		ChatUtil.log("&3&m==============================");
	}

	// Used for Crates
//	public boolean canCreateHolograms() {
//        return Crates.getInstance().getServer().getPluginManager().isPluginEnabled("HolographicDisplays");
//    }
	public boolean canCreateHolograms() {
		if (Crates.getInstance().getServer().getPluginManager().isPluginEnabled("HolographicDisplays")) {
			return true;
		}
		return false;
	}

    // Used for Main Config, Packages Config and LuckyBlocks Config
	private void loadConfigs() {
		this.mainConfig = new FileConfig(this, "config.yml");
		this.packagesConfig = new FileConfig(this, "packages.yml");
		this.exampleConfig = new FileConfig(this, "luckyblocks/example.yml");
	}

    // Used for Airdrops Config
	private void loadFiles(){
		try {
			this.airdropsFile = new ConfigFile(this, "airdrops.yml");
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InvalidConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	// Used for StelarBoxes Config
	public DataFile getData() {
		return DataFile.getConfig();
	}

	// Used for LuckyBlocks Folders
	public void loadLuckyBlocks() {
		File lbFolder = new File(getDataFolder(), "luckyblocks");

		if (!lbFolder.exists()) {
			lbFolder.mkdir();
		}

		for (File file : lbFolder.listFiles()) {
			if (file.isFile() && file.getName().endsWith(".yml")) {
				FileConfiguration config = YamlConfiguration.loadConfiguration(file);

				lbConfigs.put(file.getName(), config);
			}
		}
	}
	public FileConfiguration getLbConfigs(String fileName) {
		return lbConfigs.get(fileName);
	}

	// Used for Package, Task and Command Manager
	private void loadManagers() {
		this.packageManager = new PackageManager();
		this.taskManager = new TaskManager(this);
		this.commandManager = new CommandManager(this);
	}

    // Used for Package Listener, ButtonListener used for Packages Menu
	private void loadListeners() {
		new PackageListener();
		new ButtonListener(this);
	}

    // Used for Package Commands
	private void loadCommands() {
		new PackageCommand();
	}

    // Crates Class
	public static Crates get() {
		return getPlugin(Crates.class);
	}
}
