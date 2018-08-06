package misat11.za.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import misat11.za.Main;
import misat11.za.utils.I18n;

public class GameCreator {

	private Game game;
	private List<PhaseInfo> phases = new ArrayList<PhaseInfo>();

	public GameCreator(Game game) {
		this.game = game;
		PhaseInfo[] pi = game.getPhases();
		if (pi != null) {
			for (PhaseInfo p : pi) {
				phases.add(p);
			}
		}
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
					response = addPhase(Integer.parseInt(args[1]));
				} else if (args[0].equalsIgnoreCase("remove")) {
					response = removePhase(Integer.parseInt(args[1]));
				} else if (args[0].equalsIgnoreCase("insert")) {
					if (args.length >= 3) {
						response = addPhase(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
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
		} else if (action.equalsIgnoreCase("save")) {
			game.setPhases(phases.toArray(new PhaseInfo[phases.size()]));
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

	public CommandResponse addPhase(int index, int countdown) {
		PhaseInfo info = new PhaseInfo(countdown);
		phases.add(index, info);
		return CommandResponse.SUCCESS;
	}

	public CommandResponse addPhase(int countdown) {
		PhaseInfo info = new PhaseInfo(countdown);
		phases.add(info);
		return CommandResponse.SUCCESS;
	}

	public CommandResponse removePhase(int index) {
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
		MonsterInfo remove = null;
		List<MonsterInfo> mi = phases.get(index).getMonsters();
		for (MonsterInfo info : mi) {
			if (info.getEntityType() == monster) {
				remove = info;
				break;
			}
		}
		if (remove != null) {
			phases.get(index).removeMonster(remove);
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
																		"admin_command_game_saved_and_started");

		private final String msg;

		private CommandResponse(String msg) {
			this.msg = msg;
		}

		public String i18n() {
			return I18n._(msg, false);
		}
	}
}
