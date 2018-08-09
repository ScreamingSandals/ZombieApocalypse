package misat11.za.game;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import misat11.za.Main;

public class SmallArena {

	public String name;
	public Location pos1;
	public Location pos2;
	public HashMap<PhaseInfo, List<MonsterInfo>> monsters = new HashMap<PhaseInfo, List<MonsterInfo>>();
	
	public SmallArena(String name, Location pos1, Location pos2) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.name = name;
	}
	
	public void phaseRunning(Game game, PhaseInfo info, int currentlyPhaseTime, List<LivingEntity> spawnedEntities) {
		if (monsters.containsKey(info)) {
			for (MonsterInfo monster : monsters.get(info)) {
				if ((currentlyPhaseTime % monster.getCountdown()) == 0) {
					EntityType type = monster.getEntityType();
					Entity ent = game.getWorld().spawnEntity(getRandomLocation(pos1, pos2), type);
					if (!(ent instanceof LivingEntity)) {
						ent.remove(); // Maybe in game config is not living entity but for example boat
					}
					LivingEntity entity = (LivingEntity) ent;
					spawnedEntities.add(entity);
					Main.registerGameEntity(entity, game);
				}
			}
		} else {
			for (MonsterInfo monster : info.getMonsters()) {
				if ((currentlyPhaseTime % monster.getCountdown()) == 0) {
					EntityType type = monster.getEntityType();
					Entity ent = game.getWorld().spawnEntity(getRandomLocation(pos1, pos2), type);
					if (!(ent instanceof LivingEntity)) {
						ent.remove(); // Maybe in game config is not living entity but for example boat
					}
					LivingEntity entity = (LivingEntity) ent;
					spawnedEntities.add(entity);
					Main.registerGameEntity(entity, game);
				}
			}
		}
	}

	private Location getRandomLocation(Location a, Location b) {
		return new Location(a.getWorld(), rd(Math.min(a.getX(), b.getX()), Math.max(a.getX(), b.getX())),
				rd(Math.min(a.getY(), b.getY()), Math.max(a.getY(), b.getY())),
				rd(Math.min(a.getZ(), b.getZ()), Math.max(a.getZ(), b.getZ())));
	}

	private double rd(double a, double b) {
		return a + ThreadLocalRandom.current().nextDouble(Math.abs(b - a + 1));
	}
}
