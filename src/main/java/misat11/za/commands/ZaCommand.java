package misat11.za.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import misat11.za.Main;
import misat11.za.game.Game;
import misat11.za.game.GameCreator;
import misat11.za.game.GamePlayer;
import misat11.za.utils.I18n;

public class ZaCommand implements CommandExecutor {

	public HashMap<String, GameCreator> gc = new HashMap<String, GameCreator>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				sendHelp(player);
			} else if (args.length > 0) {
				if (args[0].equalsIgnoreCase("help")) {
					sendHelp(player);
				} else if (args[0].equalsIgnoreCase("coins")) {
					GamePlayer gPlayer = Main.getPlayerGameProfile(player);
					sender.sendMessage(I18n._("have_coins").replace("%coins%", Integer.toString(gPlayer.coins)));
				} else if (args[0].equalsIgnoreCase("join")) {
					if (args.length > 1) {
						String arenaN = args[1];
						if (Main.isGameExists(arenaN)) {
							Main.getGame(arenaN).joinToGame(player);
						} else {
							player.sendMessage(I18n._("no_arena_found"));
						}
					} else {
						player.sendMessage(I18n._("usage_za_join"));
					}
				} else if (args[0].equalsIgnoreCase("leave")) {
					if (Main.isPlayerInGame(player)) {
						Main.getPlayerGameProfile(player).changeGame(null);
					} else {
						player.sendMessage(I18n._("you_arent_in_game"));
					}
				} else if (args[0].equalsIgnoreCase("list")) {
					player.sendMessage(I18n._("list_header"));
					Main.sendGameListInfo(player);
				} else if (args[0].equalsIgnoreCase("admin")) {
					if (player.hasPermission("misat11.za.admin")) {
						if (args.length >= 3) {
							String arN = args[1];
							if (args[2].equalsIgnoreCase("add")) {
								if (Main.isGameExists(arN)) {
									player.sendMessage(I18n._("allready_exists"));
								} else if (gc.containsKey(arN)) {
									player.sendMessage(I18n._("allready_working_on_it"));
								} else {
									GameCreator creator = new GameCreator(Game.createGame(arN));
									gc.put(arN, creator);
									player.sendMessage(I18n._("arena_added"));
								}
							} else if (args[2].equalsIgnoreCase("remove")) {
								if (Main.isGameExists(arN)) {
									if (!gc.containsKey(arN)) {
										player.sendMessage(I18n._("arena_must_be_in_edit_mode"));
									} else {
										gc.remove(arN);
										new File(Main.getInstance().getDataFolder(), "arenas/" + arN + ".yml").delete();
										Main.removeGame(Main.getGame(arN));
										player.sendMessage(I18n._("arena_removed"));
									}
								} else if (gc.containsKey(arN)) {
									gc.remove(arN);
									player.sendMessage(I18n._("arena_removed"));
								} else {
									player.sendMessage(I18n._("no_arena_found"));
								}
							} else if (args[2].equalsIgnoreCase("edit")) {
								if (Main.isGameExists(arN)) {
									Game game = Main.getGame(arN);
									game.stop();
									gc.put(arN, new GameCreator(game));
									player.sendMessage(I18n._("arena_switched_to_edit"));
								} else {
									player.sendMessage(I18n._("no_arena_found"));
								}
							} else {
								if (gc.containsKey(arN)) {
									List<String> nargs = new ArrayList<String>();
									int lid = 0;
									for (String arg : args) {
										if (lid >= 3) {
											nargs.add(arg);
										}
										lid++;
									}
									gc.get(arN).cmd(player, args[2], nargs.toArray(new String[nargs.size()]));
									if (args[2].equalsIgnoreCase("save")) {
										gc.remove(arN);
									}
								} else {
									player.sendMessage(I18n._("arena_not_in_edit"));
								}
							}
						} else {
							player.sendMessage(I18n._("usage_za_admin"));
						}
					} else {
						player.sendMessage(I18n._("no_permissions"));
					}
				} else {
					player.sendMessage(I18n._("unknown_command"));
				}
			}
		} else {
			sender.sendMessage("Za commands cannot be executed from console!");
		}
		return true;
	}

	public void sendHelp(Player player) {
		player.sendMessage(I18n._("help_title", false));
		player.sendMessage(I18n._("help_za_join", false));
		player.sendMessage(I18n._("help_za_leave", false));
		player.sendMessage(I18n._("help_za_list", false));
		player.sendMessage(I18n._("help_za_coins", false));
		if (player.hasPermission("misat11.za.admin")) {
			player.sendMessage(I18n._("help_za_admin_add", false));
			player.sendMessage(I18n._("help_za_admin_spawn", false));
			player.sendMessage(I18n._("help_za_admin_pos1", false));
			player.sendMessage(I18n._("help_za_admin_pos2", false));
			player.sendMessage(I18n._("help_za_admin_pausecountdown", false));
			player.sendMessage(I18n._("help_za_admin_phase_add", false));
			player.sendMessage(I18n._("help_za_admin_phase_remove", false));
			player.sendMessage(I18n._("help_za_admin_phase_insert", false));
			player.sendMessage(I18n._("help_za_admin_phase_set", false));
			player.sendMessage(I18n._("help_za_admin_monster_add", false));
			player.sendMessage(I18n._("help_za_admin_monster_remove", false));
			player.sendMessage(I18n._("help_za_admin_small_add", false));
			player.sendMessage(I18n._("help_za_admin_small_remove", false));
			player.sendMessage(I18n._("help_za_admin_small_pos1", false));
			player.sendMessage(I18n._("help_za_admin_small_pos2", false));
			player.sendMessage(I18n._("help_za_admin_store_add", false));
			player.sendMessage(I18n._("help_za_admin_store_remove", false));
			player.sendMessage(I18n._("help_za_admin_save", false));
			player.sendMessage(I18n._("help_za_admin_remove", false));
			player.sendMessage(I18n._("help_za_admin_edit", false));
		}
	}
}
