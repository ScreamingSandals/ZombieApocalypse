package misat11.za.nms.V1_13_R1;

import net.minecraft.server.v1_13_R1.EntityHuman;
import net.minecraft.server.v1_13_R1.EntitySalmon;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_13_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_13_R1.World;

public class AggressiveSalmon extends EntitySalmon {

	public AggressiveSalmon(World arg0) {
		super(arg0);

		this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1.0D, false));
		this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
	}
	
	@Override
	protected void initAttributes() {
		super.initAttributes();
		
		this.getAttributeMap().b(GenericAttributes.ATTACK_DAMAGE).setValue(5.0);
	}

}
