package misat11.za.utils;

import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;

import misat11.za.Main;

public class SpawnUtils {
	public static Giant spawnGiant(Location loc) {
		if (Main.isNMS()) {
			try {
				Class<?> clazz = Class.forName("misat11.za.nms." + Main.getNMSVersion().toUpperCase() + ".NMSUtils");
				Method spawn = clazz.getMethod("spawnGiant", Location.class);
				Giant giant = (Giant) spawn.invoke(null, loc);
				if (giant != null)
					return giant;
			} catch (Exception ex) {
				Main.getInstance().getLogger().severe("Failed to invoke nms!");
				ex.printStackTrace();
			}
		}
		return (Giant) loc.getWorld().spawnEntity(loc, EntityType.GIANT);
	}
	
	public static Entity spawnAggressive(Location loc, EntityType type) {
		if (Main.isNMS()) {
			try {
				Class<?> clazz = Class.forName("misat11.za.nms." + Main.getNMSVersion().toUpperCase() + ".NMSUtils");
				Method spawn = clazz.getMethod("spawnAgressive", EntityType.class, Location.class);
				Entity entity = (Entity) spawn.invoke(null, type, loc);
				if (entity != null)
					return entity;
			} catch (Exception ex) {
				Main.getInstance().getLogger().severe("Failed to invoke nms!");
				ex.printStackTrace();
			}
		}
		return loc.getWorld().spawnEntity(loc, type);
	}
}
