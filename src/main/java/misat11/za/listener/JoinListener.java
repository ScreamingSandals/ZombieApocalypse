package misat11.za.listener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import misat11.za.Main;

public class JoinListener implements Listener{
	
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
    	if (event.getPlayer().getWorld().getName() == Main.instance.getConfig().getString("world")){
    		Bukkit.broadcastMessage(Main.instance.getConfig().getString("message_prefix") + Main.instance.getConfig().getString("message_join").replace("%name%", event.getPlayer().getDisplayName()));
            Location location = null;
            location.setWorld(Bukkit.getServer().getWorld(Main.instance.getConfig().getString("world")));
            location.setX(Main.instance.getConfig().getInt("spawn_x"));
            location.setY(Main.instance.getConfig().getInt("spawn_y"));
            location.setZ(Main.instance.getConfig().getInt("spawn_z"));
            location.setYaw(Main.instance.getConfig().getInt("spawn_yaw"));
            location.setPitch(Main.instance.getConfig().getInt("spawn_pitch"));
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
    }

}
