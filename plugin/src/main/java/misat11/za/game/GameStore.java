package misat11.za.game;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

public class GameStore {

	public final Location loc;
	private Villager entity;

	public GameStore(Location loc) {
		this.loc = loc;
	}

	public void spawn() {
		if (entity == null) {
			entity = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
			entity.setAI(false);
		}
	}

	public void kill() {
		if (entity != null) {
			entity.setAI(true);
			entity = null;
		}
	}

	public void forceKill() {
		if (entity != null) {
			entity.setHealth(0);
			entity = null;
		}
	}
}
