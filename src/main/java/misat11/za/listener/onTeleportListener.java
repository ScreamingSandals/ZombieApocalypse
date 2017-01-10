package misat11.za.listener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import misat11.za.Main;

public class onTeleportListener implements Listener{
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
    	World zaworld = Bukkit.getWorld(Main.instance.getConfig().getString("world"));
    	if (event.getFrom().getWorld() != zaworld){
    		if (event.getTo().getWorld() == zaworld){
	    		Bukkit.broadcastMessage(Main.instance.getConfig().getString("message_prefix") + " " + Main.instance.getConfig().getString("message_join").replace("%name%", event.getPlayer().getDisplayName()));
	            int x = Main.instance.getConfig().getInt("spawn_x");
	            int y = Main.instance.getConfig().getInt("spawn_y");
	            int z = Main.instance.getConfig().getInt("spawn_z");
	            int yaw = Main.instance.getConfig().getInt("spawn_yaw");
	            int pitch = Main.instance.getConfig().getInt("spawn_pitch");
	            Location location = new Location(zaworld, x, y, z, yaw, pitch);
	            event.getPlayer().teleport(location);
	            if(Main.instance.getSaveConfig().isSet(event.getPlayer().getName()+".play") == false){
	            	Main.instance.getSaveConfig().set(event.getPlayer().getName()+".play", true);
	            	Main.instance.getSaveConfig().set(event.getPlayer().getName()+".play.points", 100);
	            	Main.instance.getSaveConfig().set(event.getPlayer().getName()+".play.gift",  new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
	            	try {
	            		Main.instance.getSaveConfig().save(Main.instance.savef);
					} catch (IOException e) {
						e.printStackTrace();
					}
	            }
	    	}
    	}else{
    		if (event.getTo().getWorld() != zaworld){
	    		Bukkit.broadcastMessage(Main.instance.getConfig().getString("message_prefix") + " " + Main.instance.getConfig().getString("message_leave").replace("%name%", event.getPlayer().getDisplayName()));
    		}
	    }
	}

}
