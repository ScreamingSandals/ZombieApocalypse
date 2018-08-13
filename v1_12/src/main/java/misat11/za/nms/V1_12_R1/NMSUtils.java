package misat11.za.nms.V1_12_R1;

import java.util.Random;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;

import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EnchantmentManager;
import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityVillager;
import net.minecraft.server.v1_12_R1.EnumHand;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.ItemAxe;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.MathHelper;
import net.minecraft.server.v1_12_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_12_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_12_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_12_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_12_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomStroll;

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
			case LLAMA:
			case SPIDER:
			case CAVE_SPIDER:
			case POLAR_BEAR:
				creature.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(creature, EntityHuman.class, true));
				creature.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(creature, EntityVillager.class, true));
				break;
			case COW:
			case DONKEY:
			case HORSE:
			case CHICKEN:
			case MUSHROOM_COW:
			case MULE:
			case PARROT:
			case PIG:
			case SHEEP:
			case SQUID:
				creature.getAttributeMap().b(GenericAttributes.ATTACK_DAMAGE).setValue(5.0);
				creature.goalSelector.a(0, new PathfinderGoalMeleeAttack(creature, 1.0D, false) {

				    protected void a(EntityLiving entityliving, double d0) {
				        double d1 = this.a(entityliving);

				        if (d0 <= d1 && this.c <= 0) {
				            this.c = 20;
				            this.b.a(EnumHand.MAIN_HAND);

				            float f = (float) creature.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue();
				            int i = 0;

				                f += EnchantmentManager.a(creature.getItemInMainHand(), entityliving.getMonsterType());
				                i += EnchantmentManager.b(creature);

				            boolean flag = entityliving.damageEntity(DamageSource.mobAttack(creature), f);

				            if (flag) {
				                if (i > 0) {
				                    entityliving.a(creature, i * 0.5F, MathHelper.sin(creature.yaw * 0.017453292F), (double) (-MathHelper.cos(creature.yaw * 0.017453292F)));
				                    creature.motX *= 0.6D;
				                    creature.motZ *= 0.6D;
				                }

				                int j = EnchantmentManager.getFireAspectEnchantmentLevel(creature);

				                if (j > 0) {
				                	entityliving.setOnFire(j * 4);
				                }

				                if (entityliving instanceof EntityHuman) {
				                    EntityHuman entityhuman = (EntityHuman) entityliving;
				                    ItemStack itemstack = creature.getItemInMainHand();
				                    ItemStack itemstack1 = entityhuman.isHandRaised() ? entityhuman.cJ() : ItemStack.a;

				                    if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD) {
				                        float f1 = 0.25F + (float) EnchantmentManager.getDigSpeedEnchantmentLevel(creature) * 0.05F;

				                        if (new Random().nextFloat() < f1) {
				                            entityhuman.getCooldownTracker().a(Items.SHIELD, 100);
				                            creature.world.broadcastEntityEffect(entityhuman, (byte) 30);
				                        }
				                    }
				                }

				                EnchantmentManager.a(creature, entityliving);
				                EnchantmentManager.b(entityliving, creature);
				            }
				        }

				    }
				});
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
