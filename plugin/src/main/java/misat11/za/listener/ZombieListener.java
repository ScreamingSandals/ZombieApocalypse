package misat11.za.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import misat11.za.Main;
import misat11.za.game.Game;
import misat11.za.game.GamePlayer;
import misat11.za.game.GameStatus;

import static misat11.lib.lang.I18n.*;

import java.util.ArrayList;
import java.util.List;

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
				int add = Main.getReward(event.getEntityType());
				if (event.getEntity() instanceof Giant) {
					event.setDroppedExp(Main.getConfigurator().config.getInt("giant-xp", 100));
					for (ItemStack stack : (List<ItemStack>) Main.getConfigurator().config.getList("giant-drops", new ArrayList<>())) {
						event.getDrops().add(stack);
					}
				}
				if (Main.getConfigurator().config.getStringList("disable-drops").contains(event.getEntityType().name())) {
					event.getDrops().clear();
				}
				gKiller.coins += add;
				gKiller.mobkills.put(gKiller.getGame().getName(), gKiller.mobkills.getOrDefault(gKiller.getGame().getName(), 0) + 1);
				String kMessage = i18n("player_get_points")
						.replace("%entity%",
								event.getEntity().getCustomName() != null ? event.getEntity().getCustomName()
										: event.getEntity().getName())
						.replace("%coins%", Integer.toString(add))
						.replace("%newcoins%", Integer.toString(gKiller.coins));
				killer.sendMessage(kMessage);
				Main.depositPlayer(killer, Main.getVaultReward(event.getEntityType()));
			}
		}
	}

	@EventHandler
	public void onEntityBlockChange(EntityChangeBlockEvent event) {
		if (event.isCancelled())
			return;
		for (String game : Main.getGameNames()) {
			if (Main.getGame(game).getStatus() != GameStatus.WAITING
					&& Main.getGame(game).getStatus() != GameStatus.DISABLED) {
				if (isInArea(event.getBlock().getLocation(), Main.getGame(game).getPos1(),
						Main.getGame(game).getPos2())) {
					event.setCancelled(true);
					return;
				}
			}
		}
	}

	private boolean isInArea(Location l, Location p1, Location p2) {
		Location min = new Location(p1.getWorld(), Math.min(p1.getX(), p2.getX()), Math.min(p1.getY(), p2.getY()),
				Math.min(p1.getZ(), p2.getZ()));
		Location max = new Location(p1.getWorld(), Math.max(p1.getX(), p2.getX()), Math.max(p1.getY(), p2.getY()),
				Math.max(p1.getZ(), p2.getZ()));
		return (min.getX() <= l.getX() && min.getY() <= l.getY() && min.getZ() <= l.getZ() && max.getX() >= l.getX()
				&& max.getY() >= l.getY() && max.getZ() >= l.getZ());
	}

}
