package misat11.za.game;

import org.bukkit.entity.EntityType;

public class MonsterInfo {
	
	private int spawnCountdown;
	private EntityType entityType;
	
	public MonsterInfo(int spawnCountdown, EntityType entityType) {
		this.spawnCountdown = spawnCountdown;
		this.entityType = entityType;
	}
	
	public void setCountdown(int spawnCountdown) {
		this.spawnCountdown = spawnCountdown;
	}
	
	public int getCountdown() {
		return this.spawnCountdown;
	}
	
	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}
	
	public EntityType getEntityType() {
		return this.entityType;
	}
	
}
