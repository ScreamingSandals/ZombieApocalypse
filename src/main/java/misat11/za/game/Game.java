package misat11.za.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import misat11.za.Main;
import misat11.za.utils.I18n;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {

	private String name;
	private Location pos1;
	private Location pos2;
	private Location spawn;
	private PhaseInfo[] phases;
	private int pauseCountdown;
	private List<GamePlayer> players = new ArrayList<GamePlayer>();
	private World world;

	// STATUS
	private GameStatus status = GameStatus.DISABLED;
	private int inPhase = 0;
	private int countdown = 0;
	private BukkitTask task;
	private BossBar bossbar;

	private Game() {

	}

	public String getName() {
		return name;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		if (this.world == null) {
			this.world = world;
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getPos1() {
		return pos1;
	}

	public void setPos1(Location pos1) {
		this.pos1 = pos1;
	}

	public Location getPos2() {
		return pos2;
	}

	public void setPos2(Location pos2) {
		this.pos2 = pos2;
	}

	public Location getSpawn() {
		return spawn;
	}

	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}

	public PhaseInfo[] getPhases() {
		return phases;
	}

	public void setPhases(PhaseInfo[] phases) {
		this.phases = phases;
	}

	public int getPauseCountdown() {
		return pauseCountdown;
	}

	public void setPauseCountdown(int pauseCountdown) {
		this.pauseCountdown = pauseCountdown;
	}

	public void joinPlayer(GamePlayer player) {
		if (status == GameStatus.DISABLED) {
			player.changeGame(null);
			return;
		}
		boolean isEmpty = players.isEmpty();
		if (!players.contains(player)) {
			players.add(player);
		}

		player.player.teleport(spawn);
		player.player.setPlayerTime(status == GameStatus.RUNNING_IN_PHASE ? 14000L : 6000L, false);

		String message = I18n._("join").replace("%name%", player.player.getDisplayName());
		for (GamePlayer p : players)
			p.player.sendMessage(message);

		if (isEmpty) {
			runTask();
		} else {
			bossbar.addPlayer(player.player);
		}
	}

	public void leavePlayer(GamePlayer player) {
		if (status == GameStatus.DISABLED) {
			return;
		}
		if (players.contains(player)) {
			players.remove(player);
		}

		String message = I18n._("leave").replace("%name%", player.player.getDisplayName());
		if (status == GameStatus.RUNNING_IN_PHASE) {
			bossbar.removePlayer(player.player);
		}
		for (GamePlayer p : players)
			p.player.sendMessage(message);
		if (players.isEmpty()) {
			cancelTask();
			if (status == GameStatus.RUNNING_IN_PHASE) {
				phases[inPhase].phaseEnd();
			}
			inPhase = 0;
			status = GameStatus.WAITING;
			countdown = 0;
		}
	}

	public static Game loadGame(File file) {
		if (!file.exists()) {
			return null;
		}
		FileConfiguration configMap = new YamlConfiguration();
		try {
			configMap.load(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			return null;
		}

		Game game = new Game();
		game.name = configMap.getString("name");
		game.pauseCountdown = configMap.getInt("pauseCountdown");
		game.world = Bukkit.getWorld(configMap.getString("world"));
		game.pos1 = readLocationFromString(game.world, configMap.getString("pos1"));
		game.pos2 = readLocationFromString(game.world, configMap.getString("pos2"));
		game.spawn = readLocationFromString(game.world, configMap.getString("spawn"));
		List<PhaseInfo> phasel = new ArrayList<PhaseInfo>();
		for (String phaseN : configMap.getConfigurationSection("phases").getKeys(false)) {
			ConfigurationSection phase = configMap.getConfigurationSection("phases").getConfigurationSection(phaseN);
			PhaseInfo pi = new PhaseInfo(phase.getInt("countdown"));
			for (String monsterN : phase.getConfigurationSection("monsters").getKeys(false)) {
				pi.addMonster(new MonsterInfo(phase.getInt("monsters." + monsterN), EntityType.valueOf(monsterN)));
			}
			phasel.add(pi);
		}
		game.phases = phasel.toArray(new PhaseInfo[phasel.size()]);
		game.start();
		Main.getInstance().getLogger().info("Arena " + game.name + " loaded!");
		Main.addGame(game);
		return game;
	}

	private static Location readLocationFromString(World world, String location) {
		int lpos = 0;
		double x = 0;
		double y = 0;
		double z = 0;
		float yaw = 0;
		float pitch = 0;
		for (String pos : location.split(";")) {
			lpos++;
			switch (lpos) {
			case 1:
				x = Double.parseDouble(pos);
				break;
			case 2:
				y = Double.parseDouble(pos);
				break;
			case 3:
				z = Double.parseDouble(pos);
				break;
			case 4:
				yaw = Float.parseFloat(pos);
				break;
			case 5:
				pitch = Float.parseFloat(pos);
				break;
			default:
				break;
			}
		}
		return new Location(world, x, y, z, yaw, pitch);
	}

	private static String setLocationToString(Location location) {
		return location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw() + ";"
				+ location.getPitch();
	}

	public void saveToConfig() {
		File dir = new File(Main.getInstance().getDataFolder(), "arenas");
		if (!dir.exists())
			dir.mkdir();
		File file = new File(dir, name + ".yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileConfiguration configMap = new YamlConfiguration();
		try {
			configMap.load(file);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		configMap.set("name", name);
		configMap.set("pauseCountdown", pauseCountdown);
		configMap.set("world", world.getName());
		configMap.set("pos1", setLocationToString(pos1));
		configMap.set("pos2", setLocationToString(pos2));
		configMap.set("spawn", setLocationToString(spawn));
		int lid = 0;
		for (PhaseInfo phase : phases) {
			configMap.set("phases." + lid + ".countdown", phase.getCountdown());
			for (MonsterInfo minfo : phase.getMonsters()) {
				configMap.set("phases." + lid + ".monsters." + minfo.getEntityType().name(), minfo.getCountdown());
			}
			lid++;
		}

		try {
			configMap.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Game createGame(String name) {
		Game game = new Game();
		game.name = name;

		return game;
	}

	public void start() {
		if (status == GameStatus.DISABLED) {
			status = GameStatus.WAITING;
		}
	}

	public void stop() {
		cancelTask();
		for (GamePlayer p : players)
			p.changeGame(null);
		status = GameStatus.DISABLED;
	}

	public void joinToGame(Player player) {
		if (status == GameStatus.DISABLED) {
			return;
		}
		GamePlayer gPlayer = Main.getPlayerGameProfile(player);
		gPlayer.changeGame(this);
	}

	public void leaveFromGame(Player player) {
		if (status == GameStatus.DISABLED) {
			return;
		}
		if (Main.isPlayerInGame(player)) {
			GamePlayer gPlayer = Main.getPlayerGameProfile(player);

			if (gPlayer.getGame() == this) {
				gPlayer.changeGame(null);
			}
		}
	}

	public void run() {
		if (status == GameStatus.WAITING) {
			status = GameStatus.RUNNING_PAUSE;
			String title = I18n._("zombie_pause_subtitle", false);
			bossbar = Bukkit.createBossBar(title, BarColor.RED, BarStyle.SEGMENTED_20);
			for (GamePlayer p : players)
				bossbar.addPlayer(p.player);
		}
		countdown++;
		if (status == GameStatus.RUNNING_IN_PHASE) {
			if (countdown > phases[inPhase].getCountdown()) {
				phases[inPhase].phaseEnd();
				bossbar.setProgress(0);
				bossbar.setColor(BarColor.RED);
				status = GameStatus.RUNNING_PAUSE;
				countdown = 0;
				if ((inPhase + 1) >= phases.length) {
					inPhase = 0;
				} else {
					inPhase++;
				}
				for (GamePlayer p : players) {
					String title = I18n._("zombie_pause_title", false);
					String subtitle = I18n._("zombie_pause_subtitle", false);
					p.player.sendTitle(title, subtitle, 0, 20, 0);
					p.player.setPlayerTime(6000L, false);
				}
			} else {
				phases[inPhase].phaseRun(countdown, this);
				bossbar.setProgress((double) countdown / (double) phases[inPhase].getCountdown());
			}
		} else if (status == GameStatus.RUNNING_PAUSE) {
			if (countdown > pauseCountdown) {
				status = GameStatus.RUNNING_IN_PHASE;
				countdown = 0;
				bossbar.setProgress(0);
				bossbar.setColor(BarColor.GREEN);
				for (GamePlayer p : players) {
					p.player.setPlayerTime(14000L, false);
					String title = I18n._("zombie_start_title", false);
					String subtitle = I18n._("zombie_start_subtitle", false);
					p.player.sendTitle(title, subtitle, 0, 20, 0);
					if (p.teleportAura != 0) {
						p.teleportAura--;
					} else {
						p.player.teleport(spawn);
					}
				}
			} else {
				if (countdown >= (pauseCountdown - 4)) {
					String title = ChatColor.YELLOW.toString() + Integer.toString(pauseCountdown - countdown + 1);
					for (GamePlayer p : players)
						p.player.sendTitle(title, "", 0, 20, 0);
				}
				bossbar.setProgress((double) countdown / (double) pauseCountdown);
			}
		}
	}

	public GameStatus getStatus() {
		return status;
	}

	private void runTask() {
		if (task != null) {
			if (!task.isCancelled()) {
				task.cancel();
			}
			task = null;
		}
		task = (new BukkitRunnable() {

			public void run() {
				Game.this.run();
			}

		}.runTaskTimer(Main.getInstance(), 0, 20));
	}

	private void cancelTask() {
		if (task != null) {
			if (!task.isCancelled()) {
				task.cancel();
			}
			task = null;
		}
	}

}
