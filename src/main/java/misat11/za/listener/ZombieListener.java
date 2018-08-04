package misat11.za.listener;

import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import misat11.za.Main;
import misat11.za.game.Game;
import misat11.za.game.GamePlayer;
import misat11.za.game.GameStatus;

public class ZombieListener implements Listener {
	@EventHandler
	public void onCombust(final EntityCombustEvent event) {
		if (event instanceof EntityCombustByEntityEvent || event instanceof EntityCombustByBlockEvent) {
			return;
		}

		if (event.getEntity().getWorld().getEnvironment() != Environment.NORMAL) {
			return;
		}

		Game game = Main.getInGameEntity(event.getEntity());

		if (game != null) {
			if (game.getStatus() == GameStatus.RUNNING_IN_PHASE) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity().getKiller() != null) {
			Player killer = (Player) event.getEntity().getKiller();
			if (Main.isPlayerInGame(killer)) {
				GamePlayer gKiller = Main.getPlayerGameProfile(killer);
				
			}
		}
	}

}
