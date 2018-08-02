package misat11.za.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import misat11.za.Main;

public class Configurator {

	public File configf, arenasf, shopconfigf;
	public FileConfiguration config, arenaconfig, shopconfig;
	
	public final File datafolder;
	public final Main main;
	
	public Configurator(Main main) {
		this.datafolder = main.getDataFolder();
		this.main = main;
	}


	public void createFiles() {

		configf = new File(datafolder, "config.yml");
		arenasf = new File(datafolder, "arenas.yml");
		shopconfigf = new File(datafolder, "shop.yml");

		if (!configf.exists()) {
			configf.getParentFile().mkdirs();
			main.saveResource("config.yml", false);
		}
		if (!arenasf.exists()) {
			arenasf.getParentFile().mkdirs();
			main.saveResource("arenas.yml", false);
		}
		if (!shopconfigf.exists()) {
			shopconfigf.getParentFile().mkdirs();
			main.saveResource("shop.yml", false);
		}
		config = new YamlConfiguration();
		arenaconfig = new YamlConfiguration();
		shopconfig = new YamlConfiguration();
		try {
			config.load(configf);
			arenaconfig.load(arenasf);
			shopconfig.load(shopconfigf);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

		if (config.isSet("help_spectate") == false) {
			config.set("help_spectate", "Change from player to spectator and from spectator to player.");
		}
		if (config.isSet("help_list") == false) {
			config.set("help_list", "List of players in Arena");
		}
		if (config.isSet("help_join") == false) {
			config.set("help_join", "Join to Arena");
		}
		if (config.isSet("help_shop") == false) {
			config.set("help_shop", "Open shop menu");
		}
		if (config.isSet("help_points") == false) {
			config.set("help_points", "Display your points");
		}
		if (config.isSet("help_tpaura") == false) {
			config.set("help_tpaura", "Display count of your AntiTeleportAura");
		}
		if (config.isSet("help_gift") == false) {
			config.set("help_gift", "Get daily gift to you");
		}
		if (config.isSet("help_phaseinfo") == false) {
			config.set("help_phaseinfo",
					"Display actual phase, countdown to end of phase and day after phase");
		}
		if (config.isSet("help_arena") == false) {
			config.set("help_arena", "Admin Command: Settings of location where i'm putting zombies :)");
		}
		if (config.isSet("help_setspawnloc") == false) {
			config.set("help_setspawnloc", "Admin Command: set spawn location");
		}
		if (config.isSet("help_setgiantloc") == false) {
			config.set("help_setgiantloc", "Admin Command: set giant location");
		}
		if (config.isSet("help_enablegame") == false) {
			config.set("help_enablegame", "Admin Command: enable/disable game");
		}
		if (config.isSet("help_enablegiantgame") == false) {
			config.set("help_enablegiantgame", "Admin Command: enable/disable giant spawn");
		}
		if (config.isSet("message_prefix") == false) {
			config.set("message_prefix", "[ZA]");
		}
		if (config.isSet("message_phase_night") == false) {
			config.set("message_phase_night",
					"It's night of phase %phase%. Remaining until the end: %countdown%");
		}
		if (config.isSet("message_phase_day") == false) {
			config.set("message_phase_day",
					"It's day after phase %phase%. Remaining until the end: %countdown%");
		}
		if (config.isSet("message_have_points") == false) {
			config.set("message_have_points", "You have %points% points.");
		}
		if (config.isSet("message_gift_got") == false) {
			config.set("message_gift_got", "You got your Daily gift: %gift%");
		}
		if (config.isSet("message_gift_already_have") == false) {
			config.set("message_gift_already_have", "You already have Daily gift. Try it tomorrow.");
		}
		if (config.isSet("message_join") == false) {
			config.set("message_join", "%name% is join to game.");
		}
		if (config.isSet("message_leave") == false) {
			config.set("message_leave", "%name% is leave the game.");
		}
		if (config.isSet("message_change_player") == false) {
			config.set("message_change_player", "%name% is changed to player.");
		}
		if (config.isSet("message_change_spectator") == false) {
			config.set("message_change_spectator", "%name% is changed to spectator.");
		}
		if (config.isSet("message_giant_spawned") == false) {
			config.set("message_giant_spawned", "Giant spawned. Apocalypse will end after kill giant.");
		}
		if (config.isSet("message_giant_killed") == false) {
			config.set("message_giant_killed", "Giant killed. Apocalypse is restarting now.");
		}
		if (config.isSet("message_phase_start") == false) {
			config.set("message_phase_start", "Zombie apocalypse started. Phase: %number%");
		}
		if (config.isSet("message_death_player") == false) {
			config.set("message_death_player", "%player% killed by %killer%.");
		}
		if (config.isSet("message_player_get_points") == false) {
			config.set("message_player_get_points",
					"You killed %entity% and got %points% points. Your points: %newpoints%");
		}
		if (config.isSet("message_player_miss_points") == false) {
			config.set("message_player_miss_points",
					"You killed by %killer% and miss %points% points. Your points: %newpoints%");
		}
		if (config.isSet("message_get_vault_money") == false) {
			config.set("message_get_vault_money",
					"For your benefit in game you will get $%money% into your account Vault Economy.");
		}
		if (config.isSet("message_starting") == false) {
			config.set("message_starting", "Zombie Apocalypse starting in %time%");
		}
		if (config.isSet("message_minutes") == false) {
			config.set("message_minutes", "minutes");
		}
		if (config.isSet("message_minute") == false) {
			config.set("message_minute", "minute");
		}
		if (config.isSet("message_seconds") == false) {
			config.set("message_seconds", "seconds");
		}
		if (config.isSet("message_second") == false) {
			config.set("message_second", "second");
		}
		if (config.isSet("message_arena_teleport") == false) {
			config.set("message_arena_teleport", "You are teleported to arena.");
		}
		if (config.isSet("message_arena_already_in") == false) {
			config.set("message_arena_already_in", "You are already in arena.");
		}
		if (config.isSet("message_players_in_arena") == false) {
			config.set("message_players_in_arena", "In arena is %count% players.");
		}
		if (config.isSet("message_buy_succes") == false) {
			config.set("message_buy_succes",
					"Successfully purchased %item%. We've got points detected: %yourpoints%");
		}
		if (config.isSet("message_buy_no_points") == false) {
			config.set("message_buy_no_points",
					"You can not get a %item% because you have a few points. Your points are: %yourpoints%");
		}
		if (config.isSet("message_have_tpaura") == false) {
			config.set("message_have_tpaura", "Count of your AntiTeleportAura is %tpaura%.");
		}
		if (config.isSet("message_dont_have_tpaura") == false) {
			config.set("message_dont_have_tpaura",
					"You don't have AntiTeleportAura. You can buy by /za shop");
		}
		try {
			config.save(configf);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (shopconfig.isSet("enable") == false) {
			shopconfig.set("enable", true);
		}
		try {
			shopconfig.save(shopconfigf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
