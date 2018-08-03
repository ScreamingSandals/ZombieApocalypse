package misat11.za.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import misat11.za.Main;
import misat11.za.game.GamePlayer;
import misat11.za.utils.I18n;

public class ZaCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("coins")) {
					GamePlayer gPlayer = Main.getInstance().getPlayerGameProfile(player);
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
				}
			}
		}
		return true;
	}
}
