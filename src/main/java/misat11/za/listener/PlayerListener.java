package misat11.za.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import misat11.za.Main;
import misat11.za.game.GamePlayer;

public class PlayerListener implements Listener {

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			Player victim = (Player) event.getEntity();
			if (Main.isPlayerInGame(victim)) {

			}
		}
		Player killer = event.getEntity().getKiller();
		if (killer != null) {
			if (Main.isPlayerInGame(killer)) {

			}
		}
	}

	@EventHandler
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
		if (event.isCancelled())
			return;
		if (Main.isPlayerInGame(event.getPlayer())) {
			if (event.getNewGameMode() != GameMode.ADVENTURE) {
				event.getPlayer().setGameMode(GameMode.ADVENTURE);
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (Main.isPlayerGameProfileRegistered(event.getPlayer())) {
			GamePlayer gPlayer = Main.getPlayerGameProfile(event.getPlayer());
			if (gPlayer.isInGame())
				gPlayer.changeGame(null);
			Main.unloadPlayerGameProfile(event.getPlayer());
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if (Main.isPlayerInGame(event.getPlayer()))
			event.setRespawnLocation(Main.getPlayerGameProfile(event.getPlayer()).getGame().getSpawn());
	}

	@EventHandler
	public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
		if (Main.isPlayerInGame(event.getPlayer()))
			Main.getPlayerGameProfile(event.getPlayer()).changeGame(null);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;
		if (Main.isPlayerInGame(event.getPlayer()))
			event.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;
		if (Main.isPlayerInGame(event.getPlayer()))
			event.setCancelled(true);
	}
}
