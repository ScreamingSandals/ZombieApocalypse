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
			if (Main.playersInGame.containsKey(victim)) {
				GamePlayer gPlayer = Main.playersInGame.get(victim);
				if (gPlayer.isInGame()) {
					
				}
			}
		}
		Player killer = event.getEntity().getKiller();
		if (killer != null) {
			if (Main.playersInGame.containsKey(killer)) {
				GamePlayer gPlayer = Main.playersInGame.get(killer);
				if (gPlayer.isInGame()) {
					
				}
			}
		}
	}

	@EventHandler
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
		if (event.isCancelled()) return;
		if (Main.playersInGame.containsKey(event.getPlayer())) {
			GamePlayer gPlayer = Main.playersInGame.get(event.getPlayer());
			if (gPlayer.isInGame()) {
				if (event.getNewGameMode() != GameMode.ADVENTURE) {
					event.getPlayer().setGameMode(GameMode.ADVENTURE);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (Main.playersInGame.containsKey(event.getPlayer())) {
			GamePlayer gPlayer = Main.playersInGame.get(event.getPlayer());
			if (gPlayer.isInGame()) {
				gPlayer.changeGame(null);
			}
			Main.playersInGame.remove(event.getPlayer());
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if (Main.playersInGame.containsKey(event.getPlayer())) {
			GamePlayer gPlayer = Main.playersInGame.get(event.getPlayer());
			if (gPlayer.isInGame()) {
				event.setRespawnLocation(gPlayer.getGame().getSpawn());
			}
		}
	}

	@EventHandler
	public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
		if (Main.playersInGame.containsKey(event.getPlayer())) {
			GamePlayer gPlayer = Main.playersInGame.get(event.getPlayer());
			if (gPlayer.isInGame()) {
				gPlayer.changeGame(null);
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled()) return;
		if (Main.playersInGame.containsKey(event.getPlayer())) {
			GamePlayer gPlayer = Main.playersInGame.get(event.getPlayer());
			if (gPlayer.isInGame()) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled()) return;
		if (Main.playersInGame.containsKey(event.getPlayer())) {
			GamePlayer gPlayer = Main.playersInGame.get(event.getPlayer());
			if (gPlayer.isInGame()) {
				event.setCancelled(true);
			}
		}
	}
}
