package misat11.za.listener;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import misat11.za.Main;

public class LeaveListener implements Listener{
	
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
    	World zaworld = Bukkit.getWorld(Main.instance.getConfig().getString("world"));
    	if (event.getPlayer().getWorld() == zaworld){
    		Bukkit.broadcastMessage(Main.instance.getConfig().getString("message_prefix") + " " + Main.instance.getConfig().getString("message_leave").replace("%name%", event.getPlayer().getDisplayName()));
    	}
    }

}
