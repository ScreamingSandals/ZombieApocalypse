package misat11.za.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import misat11.za.Main;

public class VillagerListener implements Listener {

	@EventHandler
	public void onVillagerInteract(PlayerInteractEntityEvent event) {
		if (Main.isPlayerInGame(event.getPlayer())) {
			if (event.getRightClicked().getType() == EntityType.VILLAGER) {
				event.setCancelled(true);
				Main.openStore(event.getPlayer());
			}
		}
	}
}
