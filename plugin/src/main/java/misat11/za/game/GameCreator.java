package misat11.za.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import misat11.za.Main;

import static misat11.lib.lang.I18n.*;

public class GameCreator {

	private Game game;
	private List<PhaseInfo> phases = new ArrayList<PhaseInfo>();
	private HashMap<String, SmallArena> smallarenas = new HashMap<String, SmallArena>();
	private List<String> villagerstores = new ArrayList<String>();

	public GameCreator(Game game) {
		this.game = game;
		PhaseInfo[] pi = game.getPhases();
		if (pi != null) {
			for (PhaseInfo p : pi) {
				phases.add(p);
			}
		}
		List<SmallArena> sa = game.getSmallArenas();
		if (!sa.isEmpty()) {
			for (SmallArena small : sa) {
				smallarenas.put(small.name, small);
			}
		}
		List<GameStore> gs = game.getGameStores();
		if (!gs.isEmpty()) {
			for (GameStore store : gs) {
				villagerstores.add(store.loc.getBlockX() + ";" + store.loc.getBlockY() + ";" + store.loc.getBlockZ());
			}
		}
	}

	public List<String> getSmallArenas() {
		List<String> list = new ArrayList<String>();
		for (String str : smallarenas.keySet()) {
			list.add(str);
		}
		return list;
	}

	public List<String> getPhaseIndexes() {
		List<String> list = new ArrayList<String>();
		int size = phases.size();
		for (int i = 0; i < size; i++) {
			list.add(Integer.toString(i));
		}
		return list;
	}

	public void cmd(Player player, String action, String[] args) {
		String response = null;
		if (action.equalsIgnoreCase("spawn")) {
			response = setSpawn(player.getLocation());
		} else if (action.equalsIgnoreCase("pos1")) {
			response = setPos1(player.getLocation());
		} else if (action.equalsIgnoreCase("pos2")) {
			response = setPos2(player.getLocation());
		} else if (action.equalsIgnoreCase("pausecountdown")) {
			if (args.length >= 1) {
				response = setPauseCountdown(Integer.parseInt(args[0]));
			}
		} else if (action.equalsIgnoreCase("phase")) {
			if (args.length >= 2) {
				if (args[0].equalsIgnoreCase("add")) {
					response = addPhase(player, Integer.parseInt(args[1]));
				} else if (args[0].equalsIgnoreCase("remove")) {
					response = removePhase(Integer.parseInt(args[1]));
				} else if (args[0].equalsIgnoreCase("insert")) {
					if (args.length >= 3) {
						response = addPhase(player, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
					}
				} else if (args[0].equalsIgnoreCase("set")) {
					if (args.length >= 3) {
						response = editPhaseCountdown(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
					}
				}
			}
		} else if (action.equalsIgnoreCase("monster")) {
			if (args.length >= 3) {
				if (args[0].equalsIgnoreCase("add")) {
					if (args.length >= 4) {
						response = addMonster(Integer.parseInt(args[1]), EntityType.valueOf(args[2]),
								Integer.parseInt(args[3]));
					}
				} else if (args[0].equalsIgnoreCase("remove")) {
					response = removeMonster(Integer.parseInt(args[1]), EntityType.valueOf(args[2]));
				}
			}
		} else if (action.equalsIgnoreCase("small")) {
			if (args.length >= 2) {
				if (args[0].equalsIgnoreCase("add")) {
					response = addSmallArena(args[1]);
				} else if (args[0].equalsIgnoreCase("remove")) {
					response = removeSmallArena(args[1]);
				} else if (args[0].equalsIgnoreCase("pos1")) {
					response = pos1SmallArena(args[1], player.getLocation());
				} else if (args[0].equalsIgnoreCase("pos2")) {
					response = pos2SmallArena(args[1], player.getLocation());
				} else if (args[0].equalsIgnoreCase("monsteradd")) {
					if (args.length >= 5) {
						response = addSmallMonster(args[1], Integer.parseInt(args[2]), EntityType.valueOf(args[3]),
								Integer.parseInt(args[4]));
					}
				} else if (args[0].equalsIgnoreCase("monsterremove")) {
					if (args.length >= 4) {
						response = removeSmallMonster(args[1], Integer.parseInt(args[2]), EntityType.valueOf(args[3]));
					}
				}
			}
		} else if (action.equalsIgnoreCase("store"))

		{
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("add")) {
					response = addStore(player.getLocation());
				} else if (args[0].equalsIgnoreCase("remove")) {
					response = removeStore(player.getLocation());
				}
			}
		} else if (action.equalsIgnoreCase("bossgame")) {
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("set")) {
					response = setBossGame(player.getLocation());
				} else if (args[0].equalsIgnoreCase("reset")) {
					response = resetBossGame();
				}
			}
		} else if (action.equalsIgnoreCase("save")) {
			game.setPhases(phases.toArray(new PhaseInfo[phases.size()]));
			List<SmallArena> arenalist = new ArrayList<SmallArena>();
			for (SmallArena arr : smallarenas.values()) {
				arenalist.add(arr);
			}
			game.setSmallArenas(arenalist);
			List<GameStore> gamestores = new ArrayList<GameStore>();
			for (String vloc : villagerstores) {
				gamestores.add(new GameStore(Game.readLocationFromString(game.getWorld(), vloc)));
			}
			game.setGameStores(gamestores);
			game.saveToConfig();
			game.start();
			game.updateSigns();
			Main.addGame(game);
			response = i18n("admin_command_game_saved_and_started");
		}

		if (response == null) {
			response = i18n("unknown_command");
		}
		player.sendMessage(response);
	}

	public String setBossGame(Location loc) {
		if (game.getWorld() != loc.getWorld()) {
			return i18n("admin_command_must_be_in_same_world");
		}
		game.setBoss(loc);
		return i18n("admin_command_success");
	}

	public String resetBossGame() {
		game.setBoss(null);
		return i18n("admin_command_success");
	}

	public String addStore(Location loc) {
		if (game.getWorld() != loc.getWorld()) {
			return i18n("admin_command_must_be_in_same_world");
		}
		String location = loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
		if (villagerstores.contains(location)) {
			return i18n("admin_command_store_already_exists");
		}
		villagerstores.add(location);
		return i18n("admin_command_success");
	}

	public String removeStore(Location loc) {
		String location = loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
		if (!villagerstores.contains(location)) {
			return i18n("admin_command_store_not_exists");
		}
		villagerstores.remove(location);
		return i18n("admin_command_success");
	}

	public String addSmallArena(String name) {
		if (smallarenas.containsKey(name)) {
			return i18n("admin_command_small_already_exists");
		}
		smallarenas.put(name, new SmallArena(name, null, null));
		return i18n("admin_command_success");
	}

	public String removeSmallArena(String name) {
		if (!smallarenas.containsKey(name)) {
			return i18n("admin_command_small_not_exists");
		}
		smallarenas.remove(name);
		return i18n("admin_command_success");
	}

	public String pos1SmallArena(String name, Location loc) {
		if (!smallarenas.containsKey(name)) {
			return i18n("admin_command_small_not_exists");
		}
		if (game.getWorld() != loc.getWorld()) {
			return i18n("admin_command_must_be_in_same_world");
		}
		smallarenas.get(name).pos1 = loc;
		return i18n("admin_command_success");
	}

	public String pos2SmallArena(String name, Location loc) {
		if (!smallarenas.containsKey(name)) {
			return i18n("admin_command_small_not_exists");
		}
		if (game.getWorld() != loc.getWorld()) {
			return i18n("admin_command_must_be_in_same_world");
		}
		smallarenas.get(name).pos2 = loc;
		return i18n("admin_command_success");
	}

	public String setSpawn(Location loc) {
		if (game.getPos1() == null || game.getPos2() == null) {
			return i18n("admin_command_set_pos1_pos2_first");
		}
		if (game.getWorld() != loc.getWorld()) {
			return i18n("admin_command_must_be_in_same_world");
		}
		if (!isInArea(loc, game.getPos1(), game.getPos2())) {
			return i18n("admin_command_spawn_must_be_in_area");
		}
		game.setSpawn(loc);
		return i18n("admin_command_success");
	}

	public String setPos1(Location loc) {
		if (game.getWorld() == null) {
			game.setWorld(loc.getWorld());
		}
		if (game.getWorld() != loc.getWorld()) {
			return i18n("admin_command_must_be_in_same_world");
		}
		if (game.getPos2() != null) {
			if (Math.abs(game.getPos2().getBlockY() - loc.getBlockY()) <= 5) {
				return i18n("admin_command_pos1_pos2_difference_must_be_higher");
			}
		}
		game.setPos1(loc);
		return i18n("admin_command_success");
	}

	public String setPos2(Location loc) {
		if (game.getWorld() == null) {
			game.setWorld(loc.getWorld());
		}
		if (game.getWorld() != loc.getWorld()) {
			return i18n("admin_command_must_be_in_same_world");
		}
		if (game.getPos1() != null) {
			if (Math.abs(game.getPos1().getBlockY() - loc.getBlockY()) <= 5) {
				return i18n("admin_command_pos1_pos2_difference_must_be_higher");
			}
		}
		game.setPos2(loc);
		return i18n("admin_command_success");
	}

	public String setPauseCountdown(int countdown) {
		if (countdown >= 10 && countdown <= 600) {
			game.setPauseCountdown(countdown);
			return i18n("admin_command_success");
		}
		return i18n("admin_command_invalid_countdown");
	}

	public String addPhase(Player player, int index, int countdown) {
		PhaseInfo info = new PhaseInfo(countdown);
		phases.add(index, info);
		player.sendMessage(i18n("admin_phase_added_number", false).replace("%number%", Integer.toString(index)));
		return i18n("admin_command_success");
	}

	public String addPhase(Player player, int countdown) {
		PhaseInfo info = new PhaseInfo(countdown);
		phases.add(info);
		player.sendMessage(
				i18n("admin_phase_added_number", false).replace("%number%", Integer.toString(phases.size() - 1)));
		return i18n("admin_command_success");
	}

	public String removePhase(int index) {
		if (index < 0 || index >= phases.size()) {
			return i18n("admin_command_phase_not_exists");
		}
		if (phases.get(index) == null) {
			return i18n("admin_command_phase_not_exists");
		}
		phases.remove(index);
		return i18n("admin_command_success");
	}

	public String editPhaseCountdown(int index, int countdown) {
		if (index < 0 || index >= phases.size()) {
			return i18n("admin_command_phase_not_exists");
		}
		if (phases.get(index) == null) {
			return i18n("admin_command_phase_not_exists");
		}
		phases.get(index).setCountdown(countdown);
		return i18n("admin_command_success");
	}

	public String addMonster(int index, EntityType monster, int spawnCountdown) {
		if (index < 0 || index >= phases.size()) {
			return i18n("admin_command_phase_not_exists");
		}
		if (phases.get(index) == null) {
			return i18n("admin_command_phase_not_exists");
		}
		phases.get(index).addMonster(new MonsterInfo(spawnCountdown, monster));
		return i18n("admin_command_success");
	}

	public String removeMonster(int index, EntityType monster) {
		if (index < 0 || index >= phases.size()) {
			return i18n("admin_command_phase_not_exists");
		}
		if (phases.get(index) == null) {
			return i18n("admin_command_phase_not_exists");
		}
		List<MonsterInfo> mi = new ArrayList<MonsterInfo>((phases.get(index).getMonsters()));
		for (MonsterInfo info : mi) {
			if (info.getEntityType() == monster) {
				phases.get(index).removeMonster(info);
				break;
			}
		}
		return i18n("admin_command_success");

	}

	public String addSmallMonster(String small, int index, EntityType monster, int spawnCountdown) {
		if (!smallarenas.containsKey(small)) {
			return i18n("admin_command_small_not_exists");
		}
		if (index < 0 || index >= phases.size()) {
			return i18n("admin_command_phase_not_exists");
		}
		if (phases.get(index) == null) {
			return i18n("admin_command_phase_not_exists");
		}
		PhaseInfo phase = phases.get(index);
		SmallArena smallarena = smallarenas.get(small);
		if (!smallarena.monsters.containsKey(phase)) {
			smallarena.monsters.put(phase, new ArrayList<MonsterInfo>());
		}
		List<MonsterInfo> list = smallarena.monsters.get(phase);
		list.add(new MonsterInfo(spawnCountdown, monster));
		return i18n("admin_command_success");
	}

	public String removeSmallMonster(String small, int index, EntityType monster) {
		if (!smallarenas.containsKey(small)) {
			return i18n("admin_command_small_not_exists");
		}
		if (index < 0 || index >= phases.size()) {
			return i18n("admin_command_phase_not_exists");
		}
		if (phases.get(index) == null) {
			return i18n("admin_command_phase_not_exists");
		}
		PhaseInfo phase = phases.get(index);
		SmallArena smallarena = smallarenas.get(small);
		if (!smallarena.monsters.containsKey(phase)) {
			smallarena.monsters.put(phase, new ArrayList<MonsterInfo>());
		}
		List<MonsterInfo> mi = smallarena.monsters.get(phase);
		for (MonsterInfo info : new ArrayList<MonsterInfo>(mi)) {
			if (info.getEntityType() == monster) {
				mi.remove(info);
				break;
			}
		}
		return i18n("admin_command_success");

	}

	public boolean isInArea(Location l, Location p1, Location p2) {
		Location min = new Location(p1.getWorld(), Math.min(p1.getX(), p2.getX()), Math.min(p1.getY(), p2.getY()),
				Math.min(p1.getZ(), p2.getZ()));
		Location max = new Location(p1.getWorld(), Math.max(p1.getX(), p2.getX()), Math.max(p1.getY(), p2.getY()),
				Math.max(p1.getZ(), p2.getZ()));
		return (min.getX() <= l.getX() && min.getY() <= l.getY() && min.getZ() <= l.getZ() && max.getX() >= l.getX()
				&& max.getY() >= l.getY() && max.getZ() >= l.getZ());
	}
}
