package misat11.za.nms.V1_13_R1;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.mojang.datafixers.types.Type;

import net.minecraft.server.v1_13_R1.DataConverterRegistry;
import net.minecraft.server.v1_13_R1.DataConverterTypes;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.World;

public class NMSUtils {
	public static void load() {
		Map<Object, Type<?>> types = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(15190)
				.findChoiceType(DataConverterTypes.n).types();

		// Boss
		types.put("minecraft:zombieapocalypse_giant", types.get("minecraft:giant"));

		EntityTypes.a("zombieapocalypse_giant", EntityTypes.a.a(GameGiant.class, GameGiant::new));

		// Passive or neutral to aggressive
		types.put("minecraft:zombieapocalypse_aggressive_polar_bear", types.get("minecraft:polar_bear"));
		EntityTypes.a("zombieapocalypse_aggressive_polar_bear",
				EntityTypes.a.a(AggressiveBear.class, AggressiveBear::new));

		types.put("minecraft:zombieapocalypse_aggressive_cod", types.get("minecraft:cod"));
		EntityTypes.a("zombieapocalypse_aggressive_cod", EntityTypes.a.a(AggressiveCod.class, AggressiveCod::new));

		types.put("minecraft:zombieapocalypse_aggressive_cow", types.get("minecraft:cow"));
		EntityTypes.a("zombieapocalypse_aggressive_cow", EntityTypes.a.a(AggressiveCow.class, AggressiveCow::new));

		types.put("minecraft:zombieapocalypse_aggressive_dolphin", types.get("minecraft:dolphin"));
		EntityTypes.a("zombieapocalypse_aggressive_dolphin",
				EntityTypes.a.a(AggressiveDolphin.class, AggressiveDolphin::new));

		types.put("minecraft:zombieapocalypse_aggressive_donkey", types.get("minecraft:donkey"));
		EntityTypes.a("zombieapocalypse_aggressive_donkey",
				EntityTypes.a.a(AggressiveDonkey.class, AggressiveDonkey::new));

		types.put("minecraft:zombieapocalypse_aggressive_horse", types.get("minecraft:horse"));
		EntityTypes.a("zombieapocalypse_aggressive_horse",
				EntityTypes.a.a(AggressiveHorse.class, AggressiveHorse::new));

		types.put("minecraft:zombieapocalypse_aggressive_chicken", types.get("minecraft:chicken"));
		EntityTypes.a("zombieapocalypse_aggressive_chicken",
				EntityTypes.a.a(AggressiveChicken.class, AggressiveChicken::new));

		types.put("minecraft:zombieapocalypse_aggressive_iron_golem", types.get("minecraft:iron_golem"));
		EntityTypes.a("zombieapocalypse_aggressive_iron_golem",
				EntityTypes.a.a(AggressiveIronGolem.class, AggressiveIronGolem::new));

		types.put("minecraft:zombieapocalypse_aggressive_llama", types.get("minecraft:llama"));
		EntityTypes.a("zombieapocalypse_aggressive_llama",
				EntityTypes.a.a(AggressiveLlama.class, AggressiveLlama::new));

		types.put("minecraft:zombieapocalypse_aggressive_mooshroom", types.get("minecraft:mooshroom"));
		EntityTypes.a("zombieapocalypse_aggressive_mooshroom",
				EntityTypes.a.a(AggressiveMooshroom.class, AggressiveMooshroom::new));

		types.put("minecraft:zombieapocalypse_aggressive_mule", types.get("minecraft:mule"));
		EntityTypes.a("zombieapocalypse_aggressive_mule", EntityTypes.a.a(AggressiveMule.class, AggressiveMule::new));

		types.put("minecraft:zombieapocalypse_aggressive_ocelot", types.get("minecraft:ocelot"));
		EntityTypes.a("zombieapocalypse_aggressive_ocelot",
				EntityTypes.a.a(AggressiveOcelot.class, AggressiveOcelot::new));

		types.put("minecraft:zombieapocalypse_aggressive_parrot", types.get("minecraft:parrot"));
		EntityTypes.a("zombieapocalypse_aggressive_parrot",
				EntityTypes.a.a(AggressiveParrot.class, AggressiveParrot::new));

		types.put("minecraft:zombieapocalypse_aggressive_pig", types.get("minecraft:pig"));
		EntityTypes.a("zombieapocalypse_aggressive_pig", EntityTypes.a.a(AggressivePig.class, AggressivePig::new));

		types.put("minecraft:zombieapocalypse_aggressive_rabbit", types.get("minecraft:rabbit"));
		EntityTypes.a("zombieapocalypse_aggressive_rabbit",
				EntityTypes.a.a(AggressiveRabbit.class, AggressiveRabbit::new));

		types.put("minecraft:zombieapocalypse_aggressive_salmon", types.get("minecraft:salmon"));
		EntityTypes.a("zombieapocalypse_aggressive_salmon",
				EntityTypes.a.a(AggressiveSalmon.class, AggressiveSalmon::new));

		types.put("minecraft:zombieapocalypse_aggressive_sheep", types.get("minecraft:sheep"));
		EntityTypes.a("zombieapocalypse_aggressive_sheep",
				EntityTypes.a.a(AggressiveSheep.class, AggressiveSheep::new));

		types.put("minecraft:zombieapocalypse_aggressive_snow_golem", types.get("minecraft:snow_golem"));
		EntityTypes.a("zombieapocalypse_aggressive_snow_golem",
				EntityTypes.a.a(AggressiveSnowGolem.class, AggressiveSnowGolem::new));

		types.put("minecraft:zombieapocalypse_aggressive_squid", types.get("minecraft:squid"));
		EntityTypes.a("zombieapocalypse_aggressive_squid",
				EntityTypes.a.a(AggressiveSquid.class, AggressiveSquid::new));

		types.put("minecraft:zombieapocalypse_aggressive_tropical_fish", types.get("minecraft:tropical_fish"));
		EntityTypes.a("zombieapocalypse_aggressive_tropical_fish",
				EntityTypes.a.a(AggressiveTropicalFish.class, AggressiveTropicalFish::new));

		types.put("minecraft:zombieapocalypse_aggressive_turtle", types.get("minecraft:turtle"));
		EntityTypes.a("zombieapocalypse_aggressive_turtle",
				EntityTypes.a.a(AggressiveTurtle.class, AggressiveTurtle::new));

		types.put("minecraft:zombieapocalypse_aggressive_wolf", types.get("minecraft:wolf"));
		EntityTypes.a("zombieapocalypse_aggressive_wolf", EntityTypes.a.a(AggressiveWolf.class, AggressiveWolf::new));
	}

	public static Giant spawnGiant(Location loc) {
		GameGiant giant = new GameGiant(loc);
		return (Giant) giant.getBukkitEntity();
	}

	public static org.bukkit.entity.Entity spawnAgressive(EntityType type, Location loc) {
		World world = ((CraftWorld) loc.getWorld()).getHandle();
		Entity entity = null;
		switch (type) {
		case POLAR_BEAR:
			entity = new AggressiveBear(world);
			break;
		case COD:
			entity = new AggressiveCod(world);
			break;
		case COW:
			entity = new AggressiveCow(world);
			break;
		case DOLPHIN:
			entity = new AggressiveDolphin(world);
			break;
		case DONKEY:
			entity = new AggressiveDonkey(world);
			break;
		case HORSE:
			entity = new AggressiveHorse(world);
			break;
		case CHICKEN:
			entity = new AggressiveChicken(world);
			break;
		case IRON_GOLEM:
			entity = new AggressiveIronGolem(world);
			break;
		case LLAMA:
			entity = new AggressiveLlama(world);
			break;
		case MUSHROOM_COW:
			entity = new AggressiveMooshroom(world);
			break;
		case MULE:
			entity = new AggressiveMule(world);
			break;
		case OCELOT:
			entity = new AggressiveOcelot(world);
			break;
		case PARROT:
			entity = new AggressiveParrot(world);
			break;
		case PIG:
			entity = new AggressivePig(world);
			break;
		case RABBIT:
			entity = new AggressiveRabbit(world);
			break;
		case SALMON:
			entity = new AggressiveSalmon(world);
			break;
		case SHEEP:
			entity = new AggressiveSheep(world);
			break;
		case SNOWMAN:
			entity = new AggressiveSnowGolem(world);
			break;
		case SQUID:
			entity = new AggressiveSquid(world);
			break;
		case TROPICAL_FISH:
			entity = new AggressiveTropicalFish(world);
			break;
		case TURTLE:
			entity = new AggressiveTurtle(world);
			break;
		case WOLF:
			entity = new AggressiveWolf(world);
			break;
		default:
			break;
		}
		
		if (entity != null) {
			entity.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
			entity.world.addEntity(entity, SpawnReason.CUSTOM);
		}

		return entity != null ? entity.getBukkitEntity() : null;
	}
}
