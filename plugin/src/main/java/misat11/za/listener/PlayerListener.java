package misat11.za.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import misat11.za.Main;
import misat11.za.game.GamePlayer;

import static misat11.lib.lang.I18n.*;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		final Player victim = (Player) event.getEntity();
		if (Main.isPlayerInGame(victim)) {
			GamePlayer gKiller = null;
			final GamePlayer gVictim = Main.getPlayerGameProfile(victim);
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
				String vMessage = i18n("player_miss_points").replace("%killer%", killer.getDisplayName())
						.replace("%coins%", Integer.toString(subtract))
						.replace("%newcoins%", Integer.toString(gVictim.coins));
				victim.sendMessage(vMessage);
				String kMessage = i18n("player_get_points").replace("%entity%", victim.getDisplayName())
						.replace("%coins%", Integer.toString(subtract))
						.replace("%newcoins%", Integer.toString(gKiller.coins));
				killer.sendMessage(kMessage);
				Main.depositPlayer(killer, Main.getVaultReward(EntityType.PLAYER));
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
					vMessage = i18n("player_miss_points")
							.replace("%killer%",
									dKiller.getCustomName() != null ? dKiller.getCustomName() : dKiller.getName())
							.replace("%coins%", Integer.toString(subtract))
							.replace("%newcoins%", Integer.toString(gVictim.coins));
				} else {
					vMessage = i18n("player_miss_points_without_entity")
							.replace("%coins%", Integer.toString(subtract))
							.replace("%newcoins%", Integer.toString(gVictim.coins));
				}
				victim.sendMessage(vMessage);
			}
			event.setKeepInventory(true);
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
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (Main.isPlayerGameProfileRegistered(event.getPlayer())) {
			GamePlayer gPlayer = Main.getPlayerGameProfile(event.getPlayer());
			if (gPlayer.isInGame())
				gPlayer.changeGame(null);
			Main.unloadPlayerGameProfile(event.getPlayer());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if (Main.isPlayerInGame(event.getPlayer()))
			event.setRespawnLocation(Main.getPlayerGameProfile(event.getPlayer()).getGame().getSpawn());
	}

	@EventHandler
	public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
		if (Main.isPlayerInGame(event.getPlayer()))
			if (Main.getPlayerGameProfile(event.getPlayer()).getGame().getWorld() != event.getPlayer().getWorld())
				Main.getPlayerGameProfile(event.getPlayer()).changeGame(null);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;
		if (Main.isPlayerInGame(event.getPlayer())) {
			if (Main.isFarmBlock(event.getBlock().getType())) {
				return;
			}
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;
		if (Main.isPlayerInGame(event.getPlayer())) {
			if (Main.isFarmBlock(event.getBlock().getType())) {
				return;
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onCommandExecuted(PlayerCommandPreprocessEvent event) {
		if (event.isCancelled())
			return;
		if (Main.isPlayerInGame(event.getPlayer())) {
			if (!event.getMessage().split(" ")[0].equals("/za")) {
				event.setCancelled(true);
			}
		}
	}


	@EventHandler
	public void onCraft(CraftItemEvent event) {
		if (event.isCancelled() || !(event.getWhoClicked() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getWhoClicked();
		if (Main.isPlayerInGame(player)) {
			if (event.getInventory().getType() == InventoryType.WORKBENCH) {
				if (!Main.getConfigurator().config.getBoolean("allow-workbench-crafting", true)) {
					event.setCancelled(true);
					return;
				}
			} else {
				if (!Main.getConfigurator().config.getBoolean("allow-crafting", true)) {
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onPVP(EntityDamageByEntityEvent event) {
		if (event.isCancelled() || Main.getConfigurator().config.getBoolean("allow-pvp", true)) {
			return;
		}
		
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (Main.isPlayerInGame(player)) {
				if (event.getDamager() instanceof Player) {
					event.setCancelled(true);
				} else if (event.getDamager() instanceof Projectile) {
					Projectile projectile = (Projectile) event.getDamager();
					if (projectile.getShooter() instanceof Player) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

}
