package misat11.za.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import misat11.za.Main;
import misat11.za.utils.I18n;

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
		}
	}

	public void leavePlayer(GamePlayer player) {
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

	public static Game loadGame(String name, ConfigurationSection configMap) {
		Game game = new Game();
		game.name = name;
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
		Main.getInstance().getLogger().info("Arena " + name + " loaded!");
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
	}

	public void joinToGame(Player player) {
		GamePlayer gPlayer = Main.getPlayerGameProfile(player);
		gPlayer.changeGame(this);
	}

	public void leaveFromGame(Player player) {
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
		}
		countdown++;
		if (status == GameStatus.RUNNING_IN_PHASE) {
			if (countdown > phases[inPhase].getCountdown()) {
				phases[inPhase].phaseEnd();
				status = GameStatus.RUNNING_PAUSE;
				countdown = 0;
				if ((inPhase + 1) >= phases.length) {
					inPhase = 0;
				} else {
					inPhase++;
				}
				bossbar.setVisible(false);
				bossbar.removeAll();
				for (GamePlayer p : players)
					p.player.setPlayerTime(6000L, false);
			} else {
				phases[inPhase].phaseRun(countdown, this);
				bossbar.setProgress((double) countdown / (double) phases[inPhase].getCountdown());
			}
		} else if (status == GameStatus.RUNNING_PAUSE) {
			if (countdown > pauseCountdown) {
				status = GameStatus.RUNNING_IN_PHASE;
				countdown = 0;
				String title = I18n._("zombie_start_title", false);
				String subtitle = I18n._("zombie_start_subtitle", false);
				bossbar = Bukkit.createBossBar(title, BarColor.GREEN, BarStyle.SEGMENTED_20);
				for (GamePlayer p : players) {
					p.player.setPlayerTime(14000L, false);
					p.player.sendTitle(title, subtitle, 0, 20, 0);
					bossbar.addPlayer(p.player);
					if (p.teleportAura != 0) {
						p.teleportAura--;
					} else {
						p.player.teleport(spawn);
					}
					bossbar.setProgress(0);
					bossbar.setVisible(true);
				}
			} else {
				if (countdown >= (pauseCountdown - 4)) {
					String title = ChatColor.YELLOW.toString() + Integer.toString(pauseCountdown - countdown + 1);
					for (GamePlayer p : players)
						p.player.sendTitle(title, "", 0, 20, 0);
				}
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
