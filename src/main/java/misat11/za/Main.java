package misat11.za;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import misat11.za.commands.ZaCommand;
import misat11.za.game.Game;
import net.milkbowl.vault.economy.Economy;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Main extends JavaPlugin {

	public File configf, arenasf, playersf, shopconfigf;
	public FileConfiguration config, arenaconfig, playersconfig, shopconfig;
	public static Main instance;
	public static String version;
	public static boolean isSpigot, snapshot, isVault;
	public static Economy econ = null;
	public static HashMap<String, Game> games = new HashMap<String, Game>();

	public void onEnable() {
		instance = this;
		version = "2.0.0-pre";
		snapshot = true;

		isSpigot = getIsSpigot();

		if (!getServer().getPluginManager().isPluginEnabled("Vault")) {
			isVault = false;
		} else {
			setupEconomy();
			isVault = true;
		}

		createFiles();

		this.getCommand("za").setExecutor(new ZaCommand());

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

		
	}

	public void createFiles() {

		configf = new File(getDataFolder(), "config.yml");
		arenasf = new File(getDataFolder(), "arenas.yml");
		playersf = new File(getDataFolder(), "players.yml");
		shopconfigf = new File(getDataFolder(), "shop.yml");

		if (!configf.exists()) {
			configf.getParentFile().mkdirs();
			saveResource("config.yml", false);
		}
		if (!arenasf.exists()) {
			arenasf.getParentFile().mkdirs();
			saveResource("arenas.yml", false);
		}
		if (!playersf.exists()) {
			playersf.getParentFile().mkdirs();
			saveResource("players.yml", false);
		}
		if (!shopconfigf.exists()) {
			shopconfigf.getParentFile().mkdirs();
			saveResource("shop.yml", false);
		}
		config = new YamlConfiguration();
		arenaconfig = new YamlConfiguration();
		playersconfig = new YamlConfiguration();
		shopconfig = new YamlConfiguration();
		try {
			config.load(configf);
			arenaconfig.load(arenasf);
			playersconfig.load(playersf);
			shopconfig.load(shopconfigf);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

		if (this.getConfig().isSet("enabled") == false) {
			this.getConfig().set("enabled", false);
		}
		if (this.getConfig().isSet("help_spectate") == false) {
			this.getConfig().set("help_spectate", "Change from player to spectator and from spectator to player.");
		}
		if (this.getConfig().isSet("help_list") == false) {
			this.getConfig().set("help_list", "List of players in Arena");
		}
		if (this.getConfig().isSet("help_join") == false) {
			this.getConfig().set("help_join", "Join to Arena");
		}
		if (this.getConfig().isSet("help_shop") == false) {
			this.getConfig().set("help_shop", "Open shop menu");
		}
		if (this.getConfig().isSet("help_points") == false) {
			this.getConfig().set("help_points", "Display your points");
		}
		if (this.getConfig().isSet("help_tpaura") == false) {
			this.getConfig().set("help_tpaura", "Display count of your AntiTeleportAura");
		}
		if (this.getConfig().isSet("help_gift") == false) {
			this.getConfig().set("help_gift", "Get daily gift to you");
		}
		if (this.getConfig().isSet("help_phaseinfo") == false) {
			this.getConfig().set("help_phaseinfo",
					"Display actual phase, countdown to end of phase and day after phase");
		}
		if (this.getConfig().isSet("help_arena") == false) {
			this.getConfig().set("help_arena", "Admin Command: Settings of location where i'm putting zombies :)");
		}
		if (this.getConfig().isSet("help_setspawnloc") == false) {
			this.getConfig().set("help_setspawnloc", "Admin Command: set spawn location");
		}
		if (this.getConfig().isSet("help_setgiantloc") == false) {
			this.getConfig().set("help_setgiantloc", "Admin Command: set giant location");
		}
		if (this.getConfig().isSet("help_enablegame") == false) {
			this.getConfig().set("help_enablegame", "Admin Command: enable/disable game");
		}
		if (this.getConfig().isSet("help_enablegiantgame") == false) {
			this.getConfig().set("help_enablegiantgame", "Admin Command: enable/disable giant spawn");
		}
		if (this.getConfig().isSet("world") == false) {
			this.getConfig().set("world", "zaworld");
		}
		if (this.getConfig().isSet("spawn_x") == false) {
			this.getConfig().set("spawn_x", 0);
		}
		if (this.getConfig().isSet("spawn_y") == false) {
			this.getConfig().set("spawn_y", 60);
		}
		if (this.getConfig().isSet("spawn_z") == false) {
			this.getConfig().set("spawn_z", 0);
		}
		if (this.getConfig().isSet("spawn_yaw") == false) {
			this.getConfig().set("spawn_yaw", 0);
		}
		if (this.getConfig().isSet("spawn_pitch") == false) {
			this.getConfig().set("spawn_pitch", 0);
		}
		if (this.getConfig().isSet("teleport_on_start_end") == false) {
			this.getConfig().set("teleport_on_start_end", true);
		}
		if (this.getConfig().isSet("spawn_giant") == false) {
			this.getConfig().set("spawn_giant", true);
		}
		if (this.getConfig().isSet("giant_x") == false) {
			this.getConfig().set("giant_x", 0);
		}
		if (this.getConfig().isSet("giant_y") == false) {
			this.getConfig().set("giant_y", 60);
		}
		if (this.getConfig().isSet("giant_z") == false) {
			this.getConfig().set("giant_z", 0);
		}
		if (this.getConfig().isSet("giant_yaw") == false) {
			this.getConfig().set("giant_yaw", 0);
		}
		if (this.getConfig().isSet("giant_pitch") == false) {
			this.getConfig().set("giant_pitch", 0);
		}
		if (this.getConfig().isSet("zombies_spawn_countdown") == false) {
			this.getConfig().set("zombies_spawn_countdown", 10);
		}
		if (this.getConfig().isSet("message_prefix") == false) {
			this.getConfig().set("message_prefix", "[ZA]");
		}
		if (this.getConfig().isSet("message_phase_night") == false) {
			this.getConfig().set("message_phase_night",
					"It's night of phase %phase%. Remaining until the end: %countdown%");
		}
		if (this.getConfig().isSet("message_phase_day") == false) {
			this.getConfig().set("message_phase_day",
					"It's day after phase %phase%. Remaining until the end: %countdown%");
		}
		if (this.getConfig().isSet("message_have_points") == false) {
			this.getConfig().set("message_have_points", "You have %points% points.");
		}
		if (this.getConfig().isSet("message_gift_got") == false) {
			this.getConfig().set("message_gift_got", "You got your Daily gift: %gift%");
		}
		if (this.getConfig().isSet("message_gift_already_have") == false) {
			this.getConfig().set("message_gift_already_have", "You already have Daily gift. Try it tomorrow.");
		}
		if (this.getConfig().isSet("message_join") == false) {
			this.getConfig().set("message_join", "%name% is join to game.");
		}
		if (this.getConfig().isSet("message_leave") == false) {
			this.getConfig().set("message_leave", "%name% is leave the game.");
		}
		if (this.getConfig().isSet("message_change_player") == false) {
			this.getConfig().set("message_change_player", "%name% is changed to player.");
		}
		if (this.getConfig().isSet("message_change_spectator") == false) {
			this.getConfig().set("message_change_spectator", "%name% is changed to spectator.");
		}
		if (this.getConfig().isSet("message_giant_spawned") == false) {
			this.getConfig().set("message_giant_spawned", "Giant spawned. Apocalypse will end after kill giant.");
		}
		if (this.getConfig().isSet("message_giant_killed") == false) {
			this.getConfig().set("message_giant_killed", "Giant killed. Apocalypse is restarting now.");
		}
		if (this.getConfig().isSet("message_phase_start") == false) {
			this.getConfig().set("message_phase_start", "Zombie apocalypse started. Phase: %number%");
		}
		if (this.getConfig().isSet("message_death_player") == false) {
			this.getConfig().set("message_death_player", "%player% killed by %killer%.");
		}
		if (this.getConfig().isSet("message_player_get_points") == false) {
			this.getConfig().set("message_player_get_points",
					"You killed %entity% and got %points% points. Your points: %newpoints%");
		}
		if (this.getConfig().isSet("message_player_miss_points") == false) {
			this.getConfig().set("message_player_miss_points",
					"You killed by %killer% and miss %points% points. Your points: %newpoints%");
		}
		if (this.getConfig().isSet("message_get_vault_money") == false) {
			this.getConfig().set("message_get_vault_money",
					"For your benefit in game you will get $%money% into your account Vault Economy.");
		}
		if (this.getConfig().isSet("message_starting") == false) {
			this.getConfig().set("message_starting", "Zombie Apocalypse starting in %time%");
		}
		if (this.getConfig().isSet("message_minutes") == false) {
			this.getConfig().set("message_minutes", "minutes");
		}
		if (this.getConfig().isSet("message_minute") == false) {
			this.getConfig().set("message_minute", "minute");
		}
		if (this.getConfig().isSet("message_seconds") == false) {
			this.getConfig().set("message_seconds", "seconds");
		}
		if (this.getConfig().isSet("message_second") == false) {
			this.getConfig().set("message_second", "second");
		}
		if (this.getConfig().isSet("message_arena_teleport") == false) {
			this.getConfig().set("message_arena_teleport", "You are teleported to arena.");
		}
		if (this.getConfig().isSet("message_arena_already_in") == false) {
			this.getConfig().set("message_arena_already_in", "You are already in arena.");
		}
		if (this.getConfig().isSet("message_players_in_arena") == false) {
			this.getConfig().set("message_players_in_arena", "In arena is %count% players.");
		}
		if (this.getConfig().isSet("message_buy_succes") == false) {
			this.getConfig().set("message_buy_succes",
					"Successfully purchased %item%. We've got points detected: %yourpoints%");
		}
		if (this.getConfig().isSet("message_buy_no_points") == false) {
			this.getConfig().set("message_buy_no_points",
					"You can not get a %item% because you have a few points. Your points are: %yourpoints%");
		}
		if (this.getConfig().isSet("message_have_tpaura") == false) {
			this.getConfig().set("message_have_tpaura", "Count of your AntiTeleportAura is %tpaura%.");
		}
		if (this.getConfig().isSet("message_dont_have_tpaura") == false) {
			this.getConfig().set("message_dont_have_tpaura",
					"You don't have AntiTeleportAura. You can buy by /za shop");
		}
		try {
			getConfig().save(configf);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (this.shopconfig.isSet("enable") == false) {
			this.shopconfig.set("enable", true);
		}
		try {
			shopconfig.save(shopconfigf);
		} catch (IOException e) {
			e.printStackTrace();
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
