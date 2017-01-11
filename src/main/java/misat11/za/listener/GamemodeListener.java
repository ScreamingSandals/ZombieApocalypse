package misat11.za.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import misat11.za.Main;

public class GamemodeListener implements Listener {

	@EventHandler
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
		World zaworld = Bukkit.getWorld(Main.instance.getConfig().getString("world"));
		if (event.getPlayer().getWorld() == zaworld) {
			if (event.getPlayer().hasPermission("misat11.za.admin")) {
				// Nothing to add now :)
			} else {

				if (event.getNewGameMode() == GameMode.SPECTATOR) {
					// Nothing to add now :)
				} else if (event.getNewGameMode() == GameMode.SURVIVAL) {
					event.getPlayer().setGameMode(GameMode.ADVENTURE);
				} else if (event.getNewGameMode() == GameMode.ADVENTURE) {
					// Nothing to add now :)
				} else if (event.getNewGameMode() == GameMode.CREATIVE) {
					event.getPlayer().setGameMode(GameMode.ADVENTURE);
				}
			}
		}
	}
}
