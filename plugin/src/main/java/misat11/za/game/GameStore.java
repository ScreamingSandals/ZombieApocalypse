package misat11.za.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import misat11.za.Main;
import misat11.za.utils.SpawnUtils;

public class GameStore {

	public final Location loc;
	private Villager entity;
	private List<Villager> oldVillagers = new ArrayList<Villager>();

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
			oldVillagers.add(entity);
			if (Main.getConfigurator().config.getBoolean("make-villagers-aggressive-when-phase-started", false)) {
				SpawnUtils.makeAggressive(entity);
			}
			entity = null;
		}
	}

	public void forceKill() {
		if (entity != null) {
			entity.setHealth(0);
			entity = null;
		}
		for (Villager vill : oldVillagers) {
			vill.setHealth(0);
		}
		oldVillagers.clear();
	}
}
