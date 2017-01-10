package misat11.za.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import misat11.za.Main;

public class RespawnListener implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
    	World zaworld = Bukkit.getWorld(Main.instance.getConfig().getString("world"));
    	if (event.getPlayer().getWorld() == zaworld){
    		int x = Main.instance.getConfig().getInt("spawn_x");
    		int y = Main.instance.getConfig().getInt("spawn_y");
    		int z = Main.instance.getConfig().getInt("spawn_z");
    		int yaw = Main.instance.getConfig().getInt("spawn_yaw");
    		int pitch = Main.instance.getConfig().getInt("spawn_pitch");
    		Location location = new Location(zaworld, x, y, z, yaw, pitch);
    		event.getPlayer().teleport(location);
    	}
    }
}
