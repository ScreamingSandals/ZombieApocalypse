package misat11.za;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import misat11.za.commands.ZaCommand;
import misat11.za.game.Game;
import misat11.za.game.GamePlayer;
import misat11.za.game.GameStatus;
import misat11.za.listener.PlayerListener;
import misat11.za.listener.SignListener;
import misat11.za.listener.VillagerListener;
import misat11.za.listener.ZombieListener;
import misat11.za.utils.Configurator;
import misat11.za.utils.I18n;
import misat11.za.utils.Menu;
import net.milkbowl.vault.economy.Economy;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin {
	private static Main instance;
	private String version, nmsVersion;
	private boolean isSpigot, snapshot, isVault, isNMS;
	private Economy econ = null;
	private HashMap<String, Game> games = new HashMap<String, Game>();
	private HashMap<Player, GamePlayer> playersInGame = new HashMap<Player, GamePlayer>();
	private HashMap<Entity, Game> entitiesInGame = new HashMap<Entity, Game>();
	private Configurator configurator;
	private Menu menu;

	public static Main getInstance() {
		return instance;
	}

	public static Configurator getConfigurator() {
		return instance.configurator;
	}

	public static String getVersion() {
		return instance.version;
	}

	public static boolean isSnapshot() {
		return instance.snapshot;
	}

	public static boolean isVault() {
		return instance.isVault;
	}

	public static boolean isSpigot() {
		return instance.isSpigot;
	}

	public static boolean isNMS() {
		return instance.isNMS;
	}

	public static String getNMSVersion() {
		return isNMS() ? instance.nmsVersion : null;
	}

	public static void depositPlayer(Player player, double coins) {
		if (isVault()) {
			Main.instance.econ.depositPlayer(player, coins);
		}
	}

	public static Game getGame(String string) {
		return instance.games.get(string);
	}

	public static boolean isGameExists(String string) {
		return instance.games.containsKey(string);
	}

	public static void addGame(Game game) {
		instance.games.put(game.getName(), game);
	}

	public static void removeGame(Game game) {
		instance.games.remove(game.getName());
	}

	public static Game getInGameEntity(Entity entity) {
		return instance.entitiesInGame.containsKey(entity) ? instance.entitiesInGame.get(entity) : null;
	}

	public static void registerGameEntity(Entity entity, Game game) {
		instance.entitiesInGame.put(entity, game);
	}

	public static void unregisterGameEntity(Entity entity) {
		if (instance.entitiesInGame.containsKey(entity))
			instance.entitiesInGame.remove(entity);
	}

	public static boolean isPlayerInGame(Player player) {
		if (instance.playersInGame.containsKey(player))
			if (instance.playersInGame.get(player).isInGame())
				return true;
		return false;
	}

	public static GamePlayer getPlayerGameProfile(Player player) {
		if (instance.playersInGame.containsKey(player))
			return instance.playersInGame.get(player);
		GamePlayer gPlayer = new GamePlayer(player);
		instance.playersInGame.put(player, gPlayer);
		return gPlayer;
	}

	public static void unloadPlayerGameProfile(Player player) {
		if (instance.playersInGame.containsKey(player)) {
			instance.playersInGame.get(player).changeGame(null);
			instance.playersInGame.remove(player);
		}
	}

	public static boolean isPlayerGameProfileRegistered(Player player) {
		return instance.playersInGame.containsKey(player);
	}

	public static void sendGameListInfo(Player player) {
		for (Game game : instance.games.values()) {
			player.sendMessage((game.getStatus() == GameStatus.DISABLED ? "§c" : "§a") + game.getName() + "§f "
					+ game.countPlayers());
		}
	}

	public static void openStore(Player player) {
		instance.menu.show(player);
	}
	
	public static List<String> getGameNames() {
		List<String> list = new ArrayList<String>();
		for(Game game : instance.games.values()) {
			list.add(game.getName());
		}
		return list;
	}

	public void onEnable() {
		instance = this;
		version = this.getDescription().getVersion();
		snapshot = version.toLowerCase().contains("pre");

		try {
			Package spigotPackage = Package.getPackage("org.spigotmc");
			isSpigot = (spigotPackage != null);
		} catch (Exception e) {
			isSpigot = false;
		}

		try {
			Class.forName("org.bukkit.craftbukkit.Main");
			isNMS = true;
		} catch (ClassNotFoundException e) {
			isNMS = false;
		}

		if (isNMS) {
			String packName = Bukkit.getServer().getClass().getPackage().getName();
			nmsVersion = packName.substring(packName.lastIndexOf('.') + 1);
			try {
				Class<?> clazz = Class.forName("misat11.za.nms." + nmsVersion.toUpperCase() + ".NMSUtils");
				Method load = clazz.getMethod("load");
				load.invoke(null);
				getLogger().info("Loaded nms library for version: " + nmsVersion);
			} catch (Exception ex) {
				isNMS = false;
				getLogger().severe("Failed to find nms library for this server version! Disabling NMS library!");
			}
		} else {
			getLogger().warning("You aren't using server with NMS support! Some features maybe not working.");
		}

		if (!getServer().getPluginManager().isPluginEnabled("Vault")) {
			isVault = false;
		} else {
			setupEconomy();
			isVault = true;
		}

		configurator = new Configurator(this);

		configurator.createFiles();

		I18n.load();

		ZaCommand cmd = new ZaCommand();
		getCommand("za").setExecutor(cmd);
		getCommand("za").setTabCompleter(cmd);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new ZombieListener(), this);
		getServer().getPluginManager().registerEvents(new VillagerListener(), this);
		getServer().getPluginManager().registerEvents(new SignListener(), this);
		menu = new Menu(this);

		Bukkit.getLogger().info("********************");
		Bukkit.getLogger().info("* ZombieApocalypse *");
		Bukkit.getLogger().info("*    by Misat11    *");
		Bukkit.getLogger().info("*                  *");
		if (version.length() == 10) {
			Bukkit.getLogger().info("*                  *");
			Bukkit.getLogger().info("*    V" + version + "   *");
		} else {
			Bukkit.getLogger().info("*      V" + version + "      *");
		}
		Bukkit.getLogger().info("*                  *");
		if (snapshot == true) {
			Bukkit.getLogger().info("* SNAPSHOT VERSION *");
		} else {
			Bukkit.getLogger().info("*  STABLE VERSION  *");
		}
		Bukkit.getLogger().info("*                  *");

		if (isVault == true) {
			Bukkit.getLogger().info("*                  *");
			Bukkit.getLogger().info("*   Vault hooked   *");
			Bukkit.getLogger().info("*                  *");
		}

		if (isSpigot == false) {
			Bukkit.getLogger().info("*                  *");
			Bukkit.getLogger().info("*     WARNING:     *");
			Bukkit.getLogger().info("* You aren't using *");
			Bukkit.getLogger().info("*      Spigot      *");
			Bukkit.getLogger().info("*                  *");
			Bukkit.getLogger().info("* Please download! *");
			Bukkit.getLogger().info("*   spigotmc.org   *");
		}

		Bukkit.getLogger().info("*                  *");
		Bukkit.getLogger().info("********************");

		File folder = new File(getDataFolder().toString(), "arenas");
		if (folder.exists()) {
			File[] listOfFiles = folder.listFiles();
			if (listOfFiles.length > 0) {
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						Game.loadGame(listOfFiles[i]);
					}
				}
			}
		}

	}

	public void onDisable() {
		for (Game game : games.values()) {
			game.stop();
		}
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}

		econ = rsp.getProvider();
		return econ != null;
	}
}
