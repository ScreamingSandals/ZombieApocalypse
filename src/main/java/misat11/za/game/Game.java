package misat11.za.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import misat11.za.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Game {

	private String name;
	private Location pos1;
	private Location pos2;
	private Location spawn;
	private int spawnProtect;
	private TreeMap<Integer, PhaseInfo> phases = new TreeMap<Integer, PhaseInfo>();
	private int pauseCountdown;
	private List<GamePlayer> players = new ArrayList<GamePlayer>();
	private int zombieSpawnInterval;
	private World world;
	private int phasesLoaded = 0;

	// STATUS
	private GameStatus status;
	private int inPhase = 0;
	private int countdown = 0;

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

	public int getSpawnProtect() {
		return spawnProtect;
	}

	public void setSpawnProtect(int spawnProtect) {
		this.spawnProtect = spawnProtect;
	}

	public TreeMap<Integer, PhaseInfo> getPhases() {
		return phases;
	}

	public void setPhases(TreeMap<Integer, PhaseInfo> phases) {
		this.phases = phases;
	}

	public int getPauseCountdown() {
		return pauseCountdown;
	}

	public void setPauseCountdown(int pauseCountdown) {
		this.pauseCountdown = pauseCountdown;
	}

	public int getZombieSpawnInterval() {
		return zombieSpawnInterval;
	}

	public void setZombieSpawnInterval(int zombieSpawnInterval) {
		this.zombieSpawnInterval = zombieSpawnInterval;
	}

	public void joinPlayer(GamePlayer player) {
		if (!players.contains(player)) {
			players.add(player);
		}
	}

	public void leavePlayer(GamePlayer player) {
		if (players.contains(player)) {
			players.remove(player);
		}
	}

	public static Game loadGame(String name, ConfigurationSection configMap) {
		Game game = new Game();
		game.name = name;
		game.pauseCountdown = configMap.getInt("pauseCountdown");
		game.spawnProtect = configMap.getInt("spawnProtect");
		game.zombieSpawnInterval = configMap.getInt("zombieSpawnInterval");
		game.world = Bukkit.getWorld(configMap.getString("world"));
		game.pos1 = readLocationFromString(game.world, configMap.getString("pos1"));
		game.pos2 = readLocationFromString(game.world, configMap.getString("pos2"));
		game.spawn = readLocationFromString(game.world, configMap.getString("spawn"));
		for (String phaseN : configMap.getConfigurationSection("phases").getKeys(false)) {
			ConfigurationSection phase = configMap.getConfigurationSection("phases").getConfigurationSection(phaseN);
			PhaseInfo pi = new PhaseInfo(phase.getInt("countdown"));
			for (String monsterN : phase.getConfigurationSection("monsters").getKeys(false)) {
				pi.addMonster(new MonsterInfo(phase.getInt("monsters." + monsterN), EntityType.valueOf(monsterN)));
			}
			game.phasesLoaded++;
			game.phases.put(game.phasesLoaded, pi);
		}
		game.status = GameStatus.WAITING;
		Main.instance.getLogger().info("Arena " + name + " loaded!");
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

	}

}
