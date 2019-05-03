package misat11.za.nms.V1_14_R1;

import org.bukkit.craftbukkit.v1_14_R1.entity.CraftLivingEntity;
import net.minecraft.server.v1_14_R1.EntityCreature;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.EntityVillager;
import net.minecraft.server.v1_14_R1.GenericAttributes;
import net.minecraft.server.v1_14_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_14_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_14_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_14_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_14_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_14_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_14_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_14_R1.PathfinderGoalRandomStroll;

public class NMSUtils {
	public static void load() {

	}

	public static void makeAggressive(org.bukkit.entity.LivingEntity entity) {
		if (((CraftLivingEntity) entity).getHandle() instanceof EntityCreature) {
			EntityCreature creature = (EntityCreature) ((CraftLivingEntity) entity).getHandle();
			switch (entity.getType()) {
			case DOLPHIN:
			case OCELOT:
			case CAT:
			case WOLF:
			case SNOWMAN:
			case IRON_GOLEM:
			case LLAMA:
			case TRADER_LLAMA:
			case SPIDER:
			case CAVE_SPIDER:
			case POLAR_BEAR:
			case PANDA:
			case FOX:
				creature.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(creature, EntityHuman.class, true));
				creature.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(creature, EntityVillager.class, true));
				break;
			case COD:
			case COW:
			case DONKEY:
			case HORSE:
			case CHICKEN:
			case MUSHROOM_COW:
			case MULE:
			case PARROT:
			case PIG:
			case SALMON:
			case SHEEP:
			case SQUID:
			case TROPICAL_FISH:
			case TURTLE:
			case WANDERING_TRADER:
				creature.getAttributeMap().b(GenericAttributes.ATTACK_DAMAGE).setValue(5.0);
				creature.goalSelector.a(1, new PathfinderGoalMeleeAttack(creature, 1.0D, false));
				creature.targetSelector.a(1, new PathfinderGoalHurtByTarget(creature, new Class[0]));
				creature.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(creature, EntityHuman.class, true));
				creature.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(creature, EntityVillager.class, true));
				break;
			case RABBIT:
				creature.getAttributeInstance(GenericAttributes.ARMOR).setValue(8.0D);
				creature.goalSelector.a(4, new PathfinderGoalMeleeAttack(creature, 1.4D, true) {
					protected double a(EntityLiving entityliving) {
						return (double) (4.0F + entityliving.getWidth());
					}
				});
				creature.targetSelector.a(1, new PathfinderGoalHurtByTarget(creature, new Class[0]));
				creature.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(creature, EntityHuman.class, true));
				creature.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(creature, EntityVillager.class, true));
				break;
			case GIANT:
				creature.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(100);
		        creature.setHealth(100);
		        creature.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.3);
		        creature.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(16.0);
		        creature.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(15.0);
				//creature.width = 1;
				creature.goalSelector.a(0, new PathfinderGoalFloat(creature));
				creature.goalSelector.a(2, new PathfinderGoalMeleeAttack(creature, 1.0D, false));
				creature.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(creature, 1.0D));
				creature.goalSelector.a(7, new PathfinderGoalRandomStroll(creature, 1.0D));
				creature.goalSelector.a(8, new PathfinderGoalLookAtPlayer(creature, EntityHuman.class, 0.5F));
				creature.goalSelector.a(8, new PathfinderGoalRandomLookaround(creature));
				creature.targetSelector.a(1, new PathfinderGoalHurtByTarget(creature, new Class[0]));
				creature.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(creature, EntityHuman.class, true));
				creature.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(creature, EntityVillager.class, true));
				break;
			default:
				break;
			}
		}
	}
}
