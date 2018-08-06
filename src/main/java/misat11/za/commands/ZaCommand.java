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
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("coins")) {
					GamePlayer gPlayer = Main.getPlayerGameProfile(player);
					sender.sendMessage(I18n._("have_points").replace("%points%", Integer.toString(gPlayer.coins)));
				} else if (args[0].equalsIgnoreCase("join")) {
					if (args.length > 1) {
						String arenaN = args[1];
						if (Main.isGameExists(arenaN)) {
							Main.getGame(arenaN).joinToGame(player);
						}
					}
				} else if (args[0].equalsIgnoreCase("leave")) {
					if (Main.isPlayerInGame(player)) {
						Main.getPlayerGameProfile(player).changeGame(null);
					}
				} else if (args[0].equalsIgnoreCase("admin") && sender.hasPermission("misat11.za.admin")) {
					if (args.length >= 3) {
						String arN = args[1];
						if (args[2].equalsIgnoreCase("add")) {
							if (Main.isGameExists(arN)) {

							} else if (gc.containsKey(arN)) {

							} else {
								GameCreator creator = new GameCreator(Game.createGame(arN));
								gc.put(arN, creator);
							}
						} else if (args[2].equalsIgnoreCase("remove")) {
							if (Main.isGameExists(arN)) {
								if (gc.containsKey(arN)) {
									gc.remove(arN);
								}
								new File(Main.getInstance().getDataFolder(), "arenas/" + arN + ".yml").delete();
								Main.removeGame(Main.getGame(arN));
							} else if (gc.containsKey(arN)) {
								gc.remove(arN);
							}
						} else if (args[2].equalsIgnoreCase("edit")) {
							if (Main.isGameExists(arN)) {
								Game game = Main.getGame(arN);
								game.stop();
								gc.put(arN, new GameCreator(game));
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
							}
						}
					}
				} else {
					// TODO help
				}
			}
		}
		return true;
	}
}
