package misat11.za.utils;

import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;

import misat11.za.Main;

public class SpawnUtils {
	public static Giant spawnGiant(Location loc) {
		return (Giant) spawnAggressive(loc, EntityType.GIANT);
	}
	
	public static Entity spawnAggressive(Location loc, EntityType type) {
		Entity entity = loc.getWorld().spawnEntity(loc, type);
		makeAggressive(entity);
		return entity;
		
	}
	
	public static void makeAggressive(Entity entity) {
		if (Main.isNMS() && entity instanceof LivingEntity) {
			try {
				Class<?> clazz = Class.forName("misat11.za.nms." + Main.getNMSVersion().toUpperCase() + ".NMSUtils");
				Method spawn = clazz.getMethod("makeAggressive", LivingEntity.class);
				spawn.invoke(null, (LivingEntity) entity);
			} catch (Exception ex) {
				Main.getInstance().getLogger().severe("Failed to invoke nms!");
				ex.printStackTrace();
			}
		}
	}
}
