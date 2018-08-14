package misat11.za.nms.V1_9_R2;

import java.util.Random;

import org.bukkit.event.entity.EntityCombustByEntityEvent;

import net.minecraft.server.v1_9_R2.BlockPosition;
import net.minecraft.server.v1_9_R2.DamageSource;
import net.minecraft.server.v1_9_R2.EnchantmentManager;
import net.minecraft.server.v1_9_R2.EntityCreature;
import net.minecraft.server.v1_9_R2.EntityHuman;
import net.minecraft.server.v1_9_R2.EntityLiving;
import net.minecraft.server.v1_9_R2.EnumHand;
import net.minecraft.server.v1_9_R2.GenericAttributes;
import net.minecraft.server.v1_9_R2.ItemAxe;
import net.minecraft.server.v1_9_R2.ItemStack;
import net.minecraft.server.v1_9_R2.Items;
import net.minecraft.server.v1_9_R2.MathHelper;
import net.minecraft.server.v1_9_R2.PathEntity;
import net.minecraft.server.v1_9_R2.PathfinderGoal;

public class PahtfinderGoalAnimalAttack extends PathfinderGoal {

    protected EntityCreature b;
    int c;
    double d;
    boolean e;
    PathEntity f;
    private int h;
    private double i;
    private double j;
    private double k;
    protected final int g = 20;

    public PahtfinderGoalAnimalAttack(EntityCreature entitycreature, double d0, boolean flag) {
        this.b = entitycreature;
        this.d = d0;
        this.e = flag;
        this.a(3);
    }

    public boolean a() {
        EntityLiving entityliving = this.b.getGoalTarget();

        if (entityliving == null) {
            return false;
        } else if (!entityliving.isAlive()) {
            return false;
        } else {
            this.f = this.b.getNavigation().a(entityliving);
            return this.f != null;
        }
    }

    public boolean b() {
        EntityLiving entityliving = this.b.getGoalTarget();

        return entityliving == null ? false : (!entityliving.isAlive() ? false : (!this.e ? !this.b.getNavigation().n() : (!this.b.f(new BlockPosition(entityliving)) ? false : !(entityliving instanceof EntityHuman) || !((EntityHuman) entityliving).isSpectator() && !((EntityHuman) entityliving).l_())));
    }

    public void c() {
        this.b.getNavigation().a(this.f, this.d);
        this.h = 0;
    }

    public void d() {
        EntityLiving entityliving = this.b.getGoalTarget();

        if (entityliving instanceof EntityHuman && (((EntityHuman) entityliving).isSpectator() || ((EntityHuman) entityliving).l_())) {
            this.b.setGoalTarget((EntityLiving) null);
        }

        this.b.getNavigation().o();
    }

    public void e() {
        EntityLiving entityliving = this.b.getGoalTarget();

        this.b.getControllerLook().a(entityliving, 30.0F, 30.0F);
        double d0 = this.b.e(entityliving.locX, entityliving.getBoundingBox().b, entityliving.locZ);
        double d1 = this.a(entityliving);

        --this.h;
        if ((this.e || this.b.getEntitySenses().a(entityliving)) && this.h <= 0 && (this.i == 0.0D && this.j == 0.0D && this.k == 0.0D || entityliving.e(this.i, this.j, this.k) >= 1.0D || this.b.getRandom().nextFloat() < 0.05F)) {
            this.i = entityliving.locX;
            this.j = entityliving.getBoundingBox().b;
            this.k = entityliving.locZ;
            this.h = 4 + this.b.getRandom().nextInt(7);
            if (d0 > 1024.0D) {
                this.h += 10;
            } else if (d0 > 256.0D) {
                this.h += 5;
            }

            if (!this.b.getNavigation().a(entityliving, this.d)) {
                this.h += 15;
            }
        }

        this.c = Math.max(this.c - 1, 0);
        if (d0 <= d1 && this.c <= 0) {
            this.c = 20;
            this.b.a(EnumHand.MAIN_HAND);
            this.damageEntity(this.b, entityliving);
        }

    }

    protected double a(EntityLiving entityliving) {
        return (double) (this.b.width * 2.0F * this.b.width * 2.0F + entityliving.width);
    }
    
    public void damageEntity(EntityCreature creature, EntityLiving entityliving) {
		float f = (float) creature.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue();
		int i = 0;

		f += EnchantmentManager.a(creature.getItemInMainHand(), (entityliving).getMonsterType());
		i += EnchantmentManager.a(creature);

		boolean flag = entityliving.damageEntity(DamageSource.mobAttack(creature), f);

		if (flag) {
			if (i > 0) {
				entityliving.a(creature, i * 0.5F, MathHelper.sin(creature.yaw * 0.017453292F),
						(-MathHelper.cos(creature.yaw * 0.017453292F)));
				creature.motX *= 0.6D;
				creature.motZ *= 0.6D;
			}

			int j = EnchantmentManager.getFireAspectEnchantmentLevel(creature);

			if (j > 0) {
				EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(
						creature.getBukkitEntity(), entityliving.getBukkitEntity(), j * 4);
				org.bukkit.Bukkit.getPluginManager().callEvent(combustEvent);

				if (!combustEvent.isCancelled()) {
					entityliving.setOnFire(combustEvent.getDuration());
				}
			}

			if (entityliving instanceof EntityHuman) {
				EntityHuman entityhuman = (EntityHuman) entityliving;
				ItemStack itemstack = creature.getItemInMainHand();
				ItemStack itemstack1 = entityhuman.ct() ? entityhuman.cw() : null;

				if (itemstack != null && itemstack1 != null && itemstack.getItem() instanceof ItemAxe
						&& itemstack1.getItem() == Items.SHIELD) {
					float f1 = 0.25F
							+ (float) EnchantmentManager.getDigSpeedEnchantmentLevel(creature) * 0.05F;

					if (new Random().nextFloat() < f1) {
						entityhuman.db().a(Items.SHIELD, 100);
						creature.world.broadcastEntityEffect(entityhuman, (byte) 30);
					}
				}
			}

			EnchantmentManager.a(creature, entityliving);
			EnchantmentManager.b(entityliving, creature);
		}
    }
}
