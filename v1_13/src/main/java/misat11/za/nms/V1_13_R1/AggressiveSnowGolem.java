package misat11.za.nms.V1_13_R1;

import net.minecraft.server.v1_13_R1.EntityHuman;
import net.minecraft.server.v1_13_R1.EntitySnowman;
import net.minecraft.server.v1_13_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_13_R1.World;

public class AggressiveSnowGolem extends EntitySnowman {

	public AggressiveSnowGolem(World world) {
		super(world);

		this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
	}

}
