package misat11.za.game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import misat11.za.Main;

public class PhaseInfo {

	private int countdown;
	private List<MonsterInfo> monsters = new ArrayList<MonsterInfo>();
	private List<LivingEntity> spawnedEntities = new ArrayList<LivingEntity>();

	public PhaseInfo(int countdown) {
		this.countdown = countdown;
	}

	public void addMonster(MonsterInfo info) {
		this.monsters.add(info);
	}

	public void removeMonster(MonsterInfo info) {
		if (this.monsters.contains(info)) {
			this.monsters.remove(info);
		}
	}

	public List<MonsterInfo> getMonsters() {
		return monsters;
	}

	public int getCountdown() {
		return this.countdown;
	}

	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}

	public void phaseRun(int currentlyPhaseTime, Game game) {
		for (MonsterInfo monster : monsters) {
			if ((currentlyPhaseTime % monster.getCountdown()) == 0) {
				EntityType type = monster.getEntityType();
				Entity ent = game.getWorld().spawnEntity(getRandomLocation(game.getPos1(), game.getPos2()), type);
				if (!(ent instanceof LivingEntity)) {
					ent.remove(); // Maybe in game config is not living entity but for example boat
				}
				LivingEntity entity = (LivingEntity) ent;
				spawnedEntities.add(entity);
				Main.registerGameEntity(entity, game);
			}
		}
		if (!game.getSmallArenas().isEmpty()) {
			for (SmallArena arena : game.getSmallArenas()) {
				arena.phaseRunning(game, this, currentlyPhaseTime, spawnedEntities);
			}
		}
	}

	public void phaseEnd() {
		for (final LivingEntity entity : spawnedEntities) {
			Main.unregisterGameEntity(entity);
			entity.setFireTicks(2000);
			new BukkitRunnable() {
				
				@Override
				public void run() {
					entity.setHealth(0);
					
				}
			}.runTaskLater(Main.getInstance(), 60);
		}
		spawnedEntities.clear();
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
