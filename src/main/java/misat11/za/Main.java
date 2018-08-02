package misat11.za;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import misat11.za.commands.ZaCommand;
import misat11.za.game.Game;
import misat11.za.game.GamePlayer;
import misat11.za.listener.PlayerListener;
import misat11.za.utils.Configurator;
import net.milkbowl.vault.economy.Economy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Main extends JavaPlugin {
	public static Main instance;
	public static String version;
	public static boolean isSpigot, snapshot, isVault;
	public static Economy econ = null;
	public static HashMap<String, Game> games = new HashMap<String, Game>();
	public static HashMap<Player, GamePlayer> playersInGame = new HashMap<Player, GamePlayer>();
	public static Configurator configurator;

	public void onEnable() {
		instance = this;
		version = this.getDescription().getVersion();
		snapshot = version.toLowerCase().contains("pre");

		isSpigot = getIsSpigot();

		if (!getServer().getPluginManager().isPluginEnabled("Vault")) {
			isVault = false;
		} else {
			setupEconomy();
			isVault = true;
		}
		
		configurator = new Configurator(this);

		configurator.createFiles();

		getCommand("za").setExecutor(new ZaCommand());
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);

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

		if (configurator.arenaconfig.isSet("arenas")) {
			Set<String> arenas = configurator.arenaconfig.getConfigurationSection("arenas").getKeys(false);
			for (String arena : arenas) {
				games.put(arena, Game.loadGame(arena, configurator.arenaconfig.getConfigurationSection("arenas." + arena)));
			}
		}

	}

	private boolean getIsSpigot() {
		try {
			Package spigotPackage = Package.getPackage("org.spigotmc");
			return (spigotPackage != null);
		} catch (Exception e) {
			return false;
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
