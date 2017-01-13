package misat11.za;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import misat11.za.commands.ZaCommand;
import misat11.za.listener.DeathListener;
import misat11.za.listener.GamemodeListener;
import misat11.za.listener.JoinListener;
import misat11.za.listener.LeaveListener;
import misat11.za.listener.RespawnListener;
import misat11.za.listener.onTeleportListener;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

public class Main extends JavaPlugin {

	public File configf, savef;
	public FileConfiguration config, save;
	public static Main instance;
	public static String version;
	public static boolean isSpigot, snapshot;

	public void onEnable() {
		instance = this;
		version = "1.0.10";
		snapshot = false;

		isSpigot = getIsSpigot();

		Bukkit.getLogger().info("********************");
		Bukkit.getLogger().info("* ZombieApocalypse *");
		Bukkit.getLogger().info("*    by Misat11    *");
		Bukkit.getLogger().info("*                  *");
		Bukkit.getLogger().info("*      V" + version + "      *");
		Bukkit.getLogger().info("*                  *");
		if (snapshot == true){
			Bukkit.getLogger().info("* SNAPSHOT VERSION *");
		}else{
			Bukkit.getLogger().info("*  STABLE VERSION  *");
		}
		Bukkit.getLogger().info("*                  *");

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

		createFiles();

		Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new LeaveListener(), this);
		Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
		Bukkit.getPluginManager().registerEvents(new onTeleportListener(), this);
		Bukkit.getPluginManager().registerEvents(new RespawnListener(), this);
		Bukkit.getPluginManager().registerEvents(new GamemodeListener(), this);

		this.getCommand("za").setExecutor(new ZaCommand());

		BukkitScheduler scheduler = getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				if (getConfig().getBoolean("enabled") == true) {
					World zaworld = Bukkit.getWorld(getConfig().getString("world"));
					if (zaworld.getPlayers().size() > 0) {
						if (getSaveConfig().isSet("SERVER.ARENA") == false) {
							getSaveConfig().set("SERVER.ARENA.phase", 0);
							getSaveConfig().set("SERVER.ARENA.time", "day");
							getSaveConfig().set("SERVER.ARENA.countdown", 60);
							try {
								getSaveConfig().save(savef);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (getSaveConfig().getString("SERVER.ARENA.time") == "day") {
							getSaveConfig().set("SERVER.ARENA.countdown",
									getSaveConfig().getInt("SERVER.ARENA.countdown") - 1);
							zaworld.setTime(0);
							if (getSaveConfig().getInt("SERVER.ARENA.countdown") < 1) {
								if (getConfig().getBoolean("spawn_giant") == true) {
									if (getSaveConfig().getInt("SERVER.ARENA.phase") != 5) {
										getSaveConfig().set("SERVER.ARENA.phase",
												getSaveConfig().getInt("SERVER.ARENA.phase") + 1);
									}
									if (getSaveConfig().getInt("SERVER.ARENA.phase") == 5) {
										Bukkit.broadcastMessage(getConfig().getString("message_prefix") + " "
												+ getConfig().getString("message_giant_spawned"));
										int x = Main.instance.getConfig().getInt("giant_x");
										int y = Main.instance.getConfig().getInt("giant_y");
										int z = Main.instance.getConfig().getInt("giant_z");
										int yaw = Main.instance.getConfig().getInt("giant_yaw");
										int pitch = Main.instance.getConfig().getInt("giant_pitch");
										Location location = new Location(zaworld, x, y, z, yaw, pitch);
										Bukkit.getWorld(getConfig().getString("world")).spawnEntity(location,
												EntityType.GIANT);
										getSaveConfig().set("SERVER.ARENA.time", "night");
										getSaveConfig().set("SERVER.ARENA.countdown", 1800);
									} else {
										getSaveConfig().set("SERVER.ARENA.time", "night");
										getSaveConfig().set("SERVER.ARENA.countdown", 300);
									}
								} else {
									getSaveConfig().set("SERVER.ARENA.time", "night");
									getSaveConfig().set("SERVER.ARENA.countdown", 300);
								}
								int x = Main.instance.getConfig().getInt("spawn_x");
								int y = Main.instance.getConfig().getInt("spawn_y");
								int z = Main.instance.getConfig().getInt("spawn_z");
								int yaw = Main.instance.getConfig().getInt("spawn_yaw");
								int pitch = Main.instance.getConfig().getInt("spawn_pitch");
								Location location = new Location(zaworld, x, y, z, yaw, pitch);
								for (Player p : Bukkit.getOnlinePlayers()) {
									if (p.getWorld().equals(zaworld)) {
										if (p.getGameMode() != GameMode.CREATIVE){
											p.teleport(location);
										}
									}
								}
								Bukkit.broadcastMessage(getConfig().getString("message_prefix") + " "
										+ getConfig().getString("message_phase_start").replace("%number%",
												Integer.toString(getSaveConfig().getInt("SERVER.ARENA.phase"))));
							}
						} else {
							getSaveConfig().set("SERVER.ARENA.countdown",
									getSaveConfig().getInt("SERVER.ARENA.countdown") - 1);
							zaworld.setTime(20000);
							if (getConfig().isSet("arena_settings")) {
								Set<String> arena_settings = getConfig().getConfigurationSection("arena_settings")
										.getKeys(false);
								for (String arena_setting : arena_settings) {
									if (getSaveConfig().getInt("SERVER.ARENA.countdown") % getConfig()
											.getInt("arena_settings." + arena_setting + ".countdown") < 1) {
										int zombie_x = (int) (Math.random() * (Math.abs(getConfig()
												.getInt("arena_settings." + arena_setting + ".pos1_x")
												- getConfig().getInt("arena_settings." + arena_setting + ".pos2_x"))))
												+ Math.min(
														(getConfig()
																.getInt("arena_settings." + arena_setting + ".pos1_x")),
														getConfig()
																.getInt("arena_settings." + arena_setting + ".pos2_x"));
										int zombie_z = (int) (Math.random() * (Math.abs(getConfig()
												.getInt("arena_settings." + arena_setting + ".pos1_z")
												- getConfig().getInt("arena_settings." + arena_setting + ".pos2_z"))))
												+ Math.min(
														getConfig()
																.getInt("arena_settings." + arena_setting + ".pos1_z"),
														getConfig()
																.getInt("arena_settings." + arena_setting + ".pos2_z"));
										int zombie_y = (int) zaworld.getHighestBlockYAt(zombie_x, zombie_z);
										Location zombie_location = new Location(zaworld, zombie_x, zombie_y, zombie_z);
										Bukkit.getWorld(getConfig().getString("world")).spawnEntity(zombie_location,
												EntityType.ZOMBIE);
									}
								}
							}
							if (getSaveConfig().getInt("SERVER.ARENA.phase") > 4
									&& getConfig().getBoolean("spawn_giant") == true) {
								int total = 0;
								for (LivingEntity f : zaworld.getLivingEntities()) {
									if (f instanceof Giant) {
										total++;
										continue;
									}
								}
								if (total < 1) {
									int x = Main.instance.getConfig().getInt("giant_x");
									int y = Main.instance.getConfig().getInt("giant_y");
									int z = Main.instance.getConfig().getInt("giant_z");
									int yaw = Main.instance.getConfig().getInt("giant_yaw");
									int pitch = Main.instance.getConfig().getInt("giant_pitch");
									Location location = new Location(zaworld, x, y, z, yaw, pitch);
									Bukkit.getWorld(getConfig().getString("world")).spawnEntity(location,
											EntityType.GIANT);
								}
							}
							if (getSaveConfig().getInt("SERVER.ARENA.countdown") < 1) {
								getSaveConfig().set("SERVER.ARENA.time", "day");
								getSaveConfig().set("SERVER.ARENA.countdown", 60);
								int x = Main.instance.getConfig().getInt("spawn_x");
								int y = Main.instance.getConfig().getInt("spawn_y");
								int z = Main.instance.getConfig().getInt("spawn_z");
								int yaw = Main.instance.getConfig().getInt("spawn_yaw");
								int pitch = Main.instance.getConfig().getInt("spawn_pitch");
								Location location = new Location(zaworld, x, y, z, yaw, pitch);
								for (Player p : Bukkit.getOnlinePlayers()) {
									if (p.getWorld().equals(zaworld)) {
										if (p.getGameMode() != GameMode.CREATIVE){
											p.teleport(location);
										}
									}
								}
								for (LivingEntity f : zaworld.getLivingEntities()) {
									if (f instanceof Zombie) {
										f.remove();
										continue;
									}
								}
								Bukkit.broadcastMessage(getConfig().getString("message_prefix") + " "
										+ getConfig().getString("message_starting").replace("%time%",
												"1 " + getConfig().getString("message_minute")));
								if (getConfig().getBoolean("spawn_giant") == true
										&& getSaveConfig().getInt("SERVER.ARENA.phase") == 5) {
									getSaveConfig().set("SERVER.ARENA.phase", 0);
									getSaveConfig().set("SERVER.ARENA.time", "day");
									getSaveConfig().set("SERVER.ARENA.countdown", 60);
									for (LivingEntity f : zaworld.getLivingEntities()) {
										if (f instanceof Giant) {
											f.remove();
											continue;
										}
									}
								}
							}
						}
						try {
							getSaveConfig().save(savef);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}, 0L, 20L);
	}

	public FileConfiguration getSaveConfig() {
		return this.save;
	}

	public void createFiles() {

		savef = new File(getDataFolder(), "save.yml");
		configf = new File(getDataFolder(), "config.yml");

		if (!configf.exists()) {
			configf.getParentFile().mkdirs();
			saveResource("config.yml", false);
		}
		if (!savef.exists()) {
			savef.getParentFile().mkdirs();
			saveResource("save.yml", false);
		}

		config = new YamlConfiguration();
		save = new YamlConfiguration();
		try {
			config.load(configf);
			save.load(savef);
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
		if (this.getConfig().isSet("help_points") == false) {
			this.getConfig().set("help_points", "Display your points");
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
		try {
			getConfig().save(configf);
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
}
