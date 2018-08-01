package misat11.za.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Game {
	
	private String name;
	private Location pos1;
	private Location pos2;
	private Location spawn;
	private int spawnProtect;
	private HashMap<Integer, PhaseInfo> phases = new HashMap<Integer, PhaseInfo>();
	private int pauseCountdown;
	private HashMap<Player, GamePlayer> players = new HashMap<Player, GamePlayer>();
	
	private Game() {
		
	}

	public String getName() {
		return name;
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

	public HashMap<Integer, PhaseInfo> getPhases() {
		return phases;
	}

	public void setPhases(HashMap<Integer, PhaseInfo> phases) {
		this.phases = phases;
	}

	public int getPauseCountdown() {
		return pauseCountdown;
	}

	public void setPauseCountdown(int pauseCountdown) {
		this.pauseCountdown = pauseCountdown;
	}
	
	public static Game loadGameFromFile(String filename) {
		Game game = new Game();
		
		return game;
	}
	
	public static Game createGame(String name) {
		Game game = new Game();
		game.name = name;
		
		return game;
	}
	
	public void start() {
		
	}
	
}
