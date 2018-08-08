package misat11.za.listener;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import misat11.za.Main;
import misat11.za.game.Game;
import misat11.za.game.GamePlayer;
import misat11.za.game.GameStatus;
import misat11.za.utils.I18n;

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
		if (event.getEntity() instanceof Player)
			return;

		if (event.getEntity().getKiller() != null) {
			Player killer = (Player) event.getEntity().getKiller();
			if (Main.isPlayerInGame(killer)) {
				GamePlayer gKiller = Main.getPlayerGameProfile(killer);
				int add = 5;
				if (event.getEntity() instanceof Giant) {
					add = 50;
					event.setDroppedExp(100);
					event.getDrops().add(new ItemStack(Material.IRON_INGOT, 5));
					event.getDrops().add(new ItemStack(Material.GOLD_INGOT, 5));
					event.getDrops().add(new ItemStack(Material.DIAMOND, 1));
				}
				gKiller.coins += add;
				String kMessage = I18n._("player_get_points")
						.replace("%entity%",
								event.getEntity().getCustomName() != null ? event.getEntity().getCustomName()
										: event.getEntity().getName())
						.replace("%coins%", Integer.toString(add)).replace("%newcoins%", Integer.toString(gKiller.coins));
				killer.sendMessage(kMessage);
				Main.depositPlayer(killer, 1);
			}
		}
	}

}
