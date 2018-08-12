package misat11.za.nms.V1_9_R2;

import org.bukkit.craftbukkit.v1_9_R2.entity.CraftLivingEntity;
import net.minecraft.server.v1_9_R2.EntityCreature;
import net.minecraft.server.v1_9_R2.EntityHuman;
import net.minecraft.server.v1_9_R2.EntityLiving;
import net.minecraft.server.v1_9_R2.EntityVillager;
import net.minecraft.server.v1_9_R2.GenericAttributes;
import net.minecraft.server.v1_9_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_9_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_9_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_9_R2.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_9_R2.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_9_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_9_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_9_R2.PathfinderGoalRandomStroll;

public class NMSUtils {
	public static void load() {

	}

	public static void makeAggressive(org.bukkit.entity.LivingEntity entity) {
		if (((CraftLivingEntity) entity).getHandle() instanceof EntityCreature) {
			EntityCreature creature = (EntityCreature) ((CraftLivingEntity) entity).getHandle();
			switch (entity.getType()) {
			case OCELOT:
			case WOLF:
			case SNOWMAN:
			case IRON_GOLEM:
			case SPIDER:
			case CAVE_SPIDER:
				creature.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(creature, EntityHuman.class, true));
				creature.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(creature, EntityVillager.class, true));
				break;
			case COW:
			case HORSE:
			case CHICKEN:
			case MUSHROOM_COW:
			case PIG:
			case SHEEP:
			case SQUID:
				creature.getAttributeMap().b(GenericAttributes.ATTACK_DAMAGE).setValue(5.0);
				creature.goalSelector.a(1, new PathfinderGoalMeleeAttack(creature, 1.0D, false));
				creature.targetSelector.a(1, new PathfinderGoalHurtByTarget(creature, true, new Class[0]));
				creature.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(creature, EntityHuman.class, true));
				creature.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(creature, EntityVillager.class, true));
				break;
			case RABBIT:
				creature.getAttributeInstance(GenericAttributes.h).setValue(8.0D);
				creature.goalSelector.a(4, new PathfinderGoalMeleeAttack(creature, 1.4D, true) {
					protected double a(EntityLiving entityliving) {
						return (double) (4.0F + entityliving.width);
					}
				});
				creature.targetSelector.a(1, new PathfinderGoalHurtByTarget(creature, true, new Class[0]));
				creature.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(creature, EntityHuman.class, true));
				creature.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(creature, EntityVillager.class, true));
				break;
			case GIANT:
				creature.getAttributeInstance(GenericAttributes.maxHealth).setValue(100);
		        creature.setHealth(100);
		        creature.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.3);
		        creature.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(16.0);
		        creature.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(15.0);
				creature.width = 1;
				creature.goalSelector.a(0, new PathfinderGoalFloat(creature));
				creature.goalSelector.a(2, new PathfinderGoalMeleeAttack(creature, 1.0D, false));
				creature.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(creature, 1.0D));
				creature.goalSelector.a(7, new PathfinderGoalRandomStroll(creature, 1.0D));
				creature.goalSelector.a(8, new PathfinderGoalLookAtPlayer(creature, EntityHuman.class, 0.5F));
				creature.goalSelector.a(8, new PathfinderGoalRandomLookaround(creature));
				creature.targetSelector.a(1, new PathfinderGoalHurtByTarget(creature, true, new Class[0]));
				creature.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(creature, EntityHuman.class, true));
				creature.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(creature, EntityVillager.class, true));
				break;
			default:
				break;
			}
		}
	}
}
