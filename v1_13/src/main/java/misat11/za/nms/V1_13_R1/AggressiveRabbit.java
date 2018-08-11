package misat11.za.nms.V1_13_R1;

import net.minecraft.server.v1_13_R1.EntityHuman;
import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.EntityRabbit;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_13_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_13_R1.World;

public class AggressiveRabbit extends EntityRabbit {

	public AggressiveRabbit(World world) {
		super(world);

        this.goalSelector.a(4, new PathfinderGoalRabbitMeleeAttack(this));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
	}
	
	@Override
	protected void initAttributes() {
		super.initAttributes();
		
        this.getAttributeInstance(GenericAttributes.h).setValue(8.0D);
	}
	
    static class PathfinderGoalRabbitMeleeAttack extends PathfinderGoalMeleeAttack {

        public PathfinderGoalRabbitMeleeAttack(EntityRabbit entityrabbit) {
            super(entityrabbit, 1.4D, true);
        }

        protected double a(EntityLiving entityliving) {
            return (double) (4.0F + entityliving.width);
        }
    }

}
