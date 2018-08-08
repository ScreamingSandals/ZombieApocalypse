package misat11.za.nms.V1_13_R1;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Giant;

import com.mojang.datafixers.types.Type;

import net.minecraft.server.v1_13_R1.DataConverterRegistry;
import net.minecraft.server.v1_13_R1.DataConverterTypes;
import net.minecraft.server.v1_13_R1.EntityTypes;

public class NMSUtils {
	public static void load() {
		Map<Object, Type<?>> types = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(15190)
				.findChoiceType(DataConverterTypes.n).types();
		types.put("minecraft:zombieapocalypse_giant", types.get("minecraft:giant"));

		EntityTypes.a("zombieapocalypse_giant", EntityTypes.a.a(GameGiant.class, GameGiant::new));
	}

	public static Giant spawnGiant(Location loc) {
		GameGiant giant = new GameGiant(loc);
		return (Giant) giant.getBukkitEntity();
	}
}
