package misat11.za.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import misat11.za.Main;
import misat11.za.game.GamePlayer;
import misat11.za.utils.I18n;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		final Player victim = (Player) event.getEntity();
		if (Main.isPlayerInGame(victim)) {
			GamePlayer gKiller = null;
			GamePlayer gVictim = Main.getPlayerGameProfile(victim);
			Player killer = victim.getKiller();
			if (killer != null) {
				gKiller = Main.getPlayerGameProfile(killer);
			}
			if (gKiller != null) {
				int nc = gVictim.coins - 10;
				int subtract = 10;
				if (nc < 0) {
					subtract = 10 - Math.abs(nc);
				}
				gVictim.coins -= subtract;
				gKiller.coins += subtract;
				String vMessage = I18n._("player_miss_points").replace("%killer%", killer.getDisplayName())
						.replace("%coins%", Integer.toString(subtract))
						.replace("%newcoins%", Integer.toString(gVictim.coins));
				victim.sendMessage(vMessage);
				String kMessage = I18n._("player_get_points").replace("%entity%", victim.getDisplayName())
						.replace("%coins%", Integer.toString(subtract))
						.replace("%newcoins%", Integer.toString(gKiller.coins));
				killer.sendMessage(kMessage);
			} else {
				int nc = gVictim.coins - 5;
				int subtract = 5;
				if (nc < 0) {
					subtract = 5 - Math.abs(nc);
				}
				gVictim.coins -= subtract;
				String vMessage;
				if (victim.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
					Entity dKiller = ((EntityDamageByEntityEvent) victim.getLastDamageCause()).getDamager();
					vMessage = I18n._("player_miss_points")
							.replace("%killer%",
									dKiller.getCustomName() != null ? dKiller.getCustomName() : dKiller.getName())
							.replace("%coins%", Integer.toString(subtract))
							.replace("%newcoins%", Integer.toString(gVictim.coins));
				} else {
					vMessage = I18n._("player_miss_points_without_entity")
							.replace("%coins%", Integer.toString(subtract))
							.replace("%newcoins%", Integer.toString(gVictim.coins));
				}
				victim.sendMessage(vMessage);
			}
			if (Main.isSpigot()) {
				new BukkitRunnable() {
					public void run() {
						victim.spigot().respawn();
					}
				}.runTaskLater(Main.getInstance(), 20L);
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
