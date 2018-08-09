package misat11.za.nms.V1_13_R1;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.minecraft.server.v1_13_R1.*;

public class GameGiant extends EntityGiantZombie
{
	public GameGiant(Location loc) {
		this(((CraftWorld)loc.getWorld()).getHandle());
        this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        this.world.addEntity(this, SpawnReason.CUSTOM);
	}
	
    public GameGiant(World world)
    {
        super(world);

        width = 1;

        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalStomp(this, 1.0D, false));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 0.5F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
    }

    @Override
    protected void initAttributes()
    {
        super.initAttributes();

        double health = 100.0;

        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(health);
        setHealth((float) health);

        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.3);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(16.0);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(15.0);
    }

    @Override
    public boolean c(NBTTagCompound nbtTagCompound)
    {
        boolean success = super.c(nbtTagCompound);
        if (!success && !this.dead)
        {
            nbtTagCompound.setString("id", "minecraft:zombieapocalypse_giant");
            this.save(nbtTagCompound);
            return true;
        }
        else
        {
            return success;
        }
    }

    @Override
    public boolean d(NBTTagCompound nbtTagCompound)
    {
        boolean success = super.d(nbtTagCompound);
        if (!success && !this.dead && !this.isPassenger())
        {
            nbtTagCompound.setString("id", "minecraft:zombieapocalypse_giant");
            this.save(nbtTagCompound);
            return true;
        }
        else
        {
            return success;
        }
    }
}