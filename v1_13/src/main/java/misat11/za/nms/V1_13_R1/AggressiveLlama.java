package misat11.za.nms.V1_13_R1;

import net.minecraft.server.v1_13_R1.EntityHuman;
import net.minecraft.server.v1_13_R1.EntityLlama;
import net.minecraft.server.v1_13_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_13_R1.World;

public class AggressiveLlama extends EntityLlama {

	public AggressiveLlama(World arg0) {
		super(arg0);

		this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
	}

}
