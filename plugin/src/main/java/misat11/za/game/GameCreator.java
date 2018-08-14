package misat11.za.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import misat11.za.Main;
import misat11.za.utils.I18n;

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
		CommandResponse response = null;
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
			Main.addGame(game);
			response = CommandResponse.SAVED_AND_STARTED;
		}

		if (response == null) {
			response = CommandResponse.UNKNOWN_COMMAND;
		}
		player.sendMessage(response.i18n());
	}

	public CommandResponse setBossGame(Location loc) {
		if (game.getWorld() != loc.getWorld()) {
			return CommandResponse.MUST_BE_IN_SAME_WORLD;
		}
		game.setBoss(loc);
		return CommandResponse.SUCCESS;
	}

	public CommandResponse resetBossGame() {
		game.setBoss(null);
		return CommandResponse.SUCCESS;
	}

	public CommandResponse addStore(Location loc) {
		if (game.getWorld() != loc.getWorld()) {
			return CommandResponse.MUST_BE_IN_SAME_WORLD;
		}
		String location = loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
		if (villagerstores.contains(location)) {
			return CommandResponse.STORE_EXISTS;
		}
		villagerstores.add(location);
		return CommandResponse.SUCCESS;
	}

	public CommandResponse removeStore(Location loc) {
		String location = loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
		if (!villagerstores.contains(location)) {
			return CommandResponse.STORE_NOT_EXISTS;
		}
		villagerstores.remove(location);
		return CommandResponse.SUCCESS;
	}

	public CommandResponse addSmallArena(String name) {
		if (smallarenas.containsKey(name)) {
			return CommandResponse.SMALL_ALREADY_EXISTS;
		}
		smallarenas.put(name, new SmallArena(name, null, null));
		return CommandResponse.SUCCESS;
	}

	public CommandResponse removeSmallArena(String name) {
		if (!smallarenas.containsKey(name)) {
			return CommandResponse.SMALL_NOT_EXISTS;
		}
		smallarenas.remove(name);
		return CommandResponse.SUCCESS;
	}

	public CommandResponse pos1SmallArena(String name, Location loc) {
		if (!smallarenas.containsKey(name)) {
			return CommandResponse.SMALL_NOT_EXISTS;
		}
		if (game.getWorld() != loc.getWorld()) {
			return CommandResponse.MUST_BE_IN_SAME_WORLD;
		}
		smallarenas.get(name).pos1 = loc;
		return CommandResponse.SUCCESS;
	}

	public CommandResponse pos2SmallArena(String name, Location loc) {
		if (!smallarenas.containsKey(name)) {
			return CommandResponse.SMALL_NOT_EXISTS;
		}
		if (game.getWorld() != loc.getWorld()) {
			return CommandResponse.MUST_BE_IN_SAME_WORLD;
		}
		smallarenas.get(name).pos2 = loc;
		return CommandResponse.SUCCESS;
	}

	public CommandResponse setSpawn(Location loc) {
		if (game.getPos1() == null || game.getPos2() == null) {
			return CommandResponse.SET_POS1_POS2_FIRST;
		}
		if (game.getWorld() != loc.getWorld()) {
			return CommandResponse.MUST_BE_IN_SAME_WORLD;
		}
		if (!isInArea(loc, game.getPos1(), game.getPos2())) {
			return CommandResponse.SPAWN_MUST_BE_IN_MAIN_AREA;
		}
		game.setSpawn(loc);
		return CommandResponse.SUCCESS;
	}

	public CommandResponse setPos1(Location loc) {
		if (game.getWorld() == null) {
			game.setWorld(loc.getWorld());
		}
		if (game.getWorld() != loc.getWorld()) {
			return CommandResponse.MUST_BE_IN_SAME_WORLD;
		}
		if (game.getPos2() != null) {
			if (Math.abs(game.getPos2().getBlockY() - loc.getBlockY()) <= 5) {
				return CommandResponse.POS1_POS2_DIFFERENCE_MUST_BE_HIGHER;
			}
		}
		game.setPos1(loc);
		return CommandResponse.SUCCESS;
	}

	public CommandResponse setPos2(Location loc) {
		if (game.getWorld() == null) {
			game.setWorld(loc.getWorld());
		}
		if (game.getWorld() != loc.getWorld()) {
			return CommandResponse.MUST_BE_IN_SAME_WORLD;
		}
		if (game.getPos1() != null) {
			if (Math.abs(game.getPos1().getBlockY() - loc.getBlockY()) <= 5) {
				return CommandResponse.POS1_POS2_DIFFERENCE_MUST_BE_HIGHER;
			}
		}
		game.setPos2(loc);
		return CommandResponse.SUCCESS;
	}

	public CommandResponse setPauseCountdown(int countdown) {
		if (countdown >= 10 && countdown <= 600) {
			game.setPauseCountdown(countdown);
			return CommandResponse.SUCCESS;
		}
		return CommandResponse.INVALID_COUNTDOWN;
	}

	public CommandResponse addPhase(Player player, int index, int countdown) {
		PhaseInfo info = new PhaseInfo(countdown);
		phases.add(index, info);
		player.sendMessage(I18n._("admin_phase_added_number", false).replace("%number%", Integer.toString(index)));
		return CommandResponse.SUCCESS;
	}

	public CommandResponse addPhase(Player player, int countdown) {
		PhaseInfo info = new PhaseInfo(countdown);
		phases.add(info);
		player.sendMessage(
				I18n._("admin_phase_added_number", false).replace("%number%", Integer.toString(phases.size() - 1)));
		return CommandResponse.SUCCESS;
	}

	public CommandResponse removePhase(int index) {
		if (index < 0 || index >= phases.size()) {
			return CommandResponse.PHASE_NOT_EXISTS;
		}
		if (phases.get(index) == null) {
			return CommandResponse.PHASE_NOT_EXISTS;
		}
		phases.remove(index);
		return CommandResponse.SUCCESS;
	}

	public CommandResponse editPhaseCountdown(int index, int countdown) {
		if (index < 0 || index >= phases.size()) {
			return CommandResponse.PHASE_NOT_EXISTS;
		}
		if (phases.get(index) == null) {
			return CommandResponse.PHASE_NOT_EXISTS;
		}
		phases.get(index).setCountdown(countdown);
		return CommandResponse.SUCCESS;
	}

	public CommandResponse addMonster(int index, EntityType monster, int spawnCountdown) {
		if (index < 0 || index >= phases.size()) {
			return CommandResponse.PHASE_NOT_EXISTS;
		}
		if (phases.get(index) == null) {
			return CommandResponse.PHASE_NOT_EXISTS;
		}
		phases.get(index).addMonster(new MonsterInfo(spawnCountdown, monster));
		return CommandResponse.SUCCESS;
	}

	public CommandResponse removeMonster(int index, EntityType monster) {
		if (index < 0 || index >= phases.size()) {
			return CommandResponse.PHASE_NOT_EXISTS;
		}
		if (phases.get(index) == null) {
			return CommandResponse.PHASE_NOT_EXISTS;
		}
		List<MonsterInfo> mi = new ArrayList<MonsterInfo>((phases.get(index).getMonsters()));
		for (MonsterInfo info : mi) {
			if (info.getEntityType() == monster) {
				phases.get(index).removeMonster(info);
				break;
			}
		}
		return CommandResponse.SUCCESS;

	}

	public CommandResponse addSmallMonster(String small, int index, EntityType monster, int spawnCountdown) {
		if (!smallarenas.containsKey(small)) {
			return CommandResponse.SMALL_NOT_EXISTS;
		}
		if (index < 0 || index >= phases.size()) {
			return CommandResponse.PHASE_NOT_EXISTS;
		}
		if (phases.get(index) == null) {
			return CommandResponse.PHASE_NOT_EXISTS;
		}
		PhaseInfo phase = phases.get(index);
		SmallArena smallarena = smallarenas.get(small);
		if (!smallarena.monsters.containsKey(phase)) {
			smallarena.monsters.put(phase, new ArrayList<MonsterInfo>());
		}
		List<MonsterInfo> list = smallarena.monsters.get(phase);
		list.add(new MonsterInfo(spawnCountdown, monster));
		return CommandResponse.SUCCESS;
	}

	public CommandResponse removeSmallMonster(String small, int index, EntityType monster) {
		if (!smallarenas.containsKey(small)) {
			return CommandResponse.SMALL_NOT_EXISTS;
		}
		if (index < 0 || index >= phases.size()) {
			return CommandResponse.PHASE_NOT_EXISTS;
		}
		if (phases.get(index) == null) {
			return CommandResponse.PHASE_NOT_EXISTS;
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
		return CommandResponse.SUCCESS;

	}

	public boolean isInArea(Location l, Location p1, Location p2) {
		Location min = new Location(p1.getWorld(), Math.min(p1.getX(), p2.getX()), Math.min(p1.getY(), p2.getY()),
				Math.min(p1.getZ(), p2.getZ()));
		Location max = new Location(p1.getWorld(), Math.max(p1.getX(), p2.getX()), Math.max(p1.getY(), p2.getY()),
				Math.max(p1.getZ(), p2.getZ()));
		return (min.getX() <= l.getX() && min.getY() <= l.getY() && min.getZ() <= l.getZ() && max.getX() >= l.getX()
				&& max.getY() >= l.getY() && max.getZ() >= l.getZ());
	}

	enum CommandResponse {
		SUCCESS("admin_command_success"), MUST_BE_IN_SAME_WORLD(
				"admin_command_must_be_in_same_world"), INVALID_COUNTDOWN(
						"admin_command_invalid_countdown"), SPAWN_MUST_BE_IN_MAIN_AREA(
								"admin_command_spawn_must_be_in_area"), SET_POS1_POS2_FIRST(
										"admin_command_set_pos1_pos2_first"), POS1_POS2_DIFFERENCE_MUST_BE_HIGHER(
												"admin_command_pos1_pos2_difference_must_be_higher"), PHASE_NOT_EXISTS(
														"admin_command_phase_not_exists"), UNKNOWN_COMMAND(
																"unknown_command"), SAVED_AND_STARTED(
																		"admin_command_game_saved_and_started"), SMALL_ALREADY_EXISTS(
																				"admin_command_small_already_exists"), SMALL_NOT_EXISTS(
																						"admin_command_small_not_exists"), STORE_EXISTS(
																								"admin_command_store_already_exists"), STORE_NOT_EXISTS(
																										"admin_command_store_not_exists");

		private final String msg;

		private CommandResponse(String msg) {
			this.msg = msg;
		}

		public String i18n() {
			return I18n._(msg, false);
		}
	}
}
