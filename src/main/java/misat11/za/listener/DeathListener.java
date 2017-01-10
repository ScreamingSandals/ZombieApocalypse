package misat11.za.listener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import misat11.za.Main;

public class DeathListener implements Listener{

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
    	World zaworld = Bukkit.getWorld(Main.instance.getConfig().getString("world"));
    	if (event.getEntity() instanceof Player){
	    	if (event.getEntity().getWorld() == zaworld){
	    		Player player = (Player)Bukkit.getPlayer(event.getEntity().getName());
	            if(Main.instance.getSaveConfig().isSet(player.getName()+".play") == false){
	            	Main.instance.getSaveConfig().set(player.getName()+".play", true);
	            	Main.instance.getSaveConfig().set(player.getName()+".play.points", 100);
	            	Main.instance.getSaveConfig().set(player.getName()+".play.gift",  new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
	            	try {
	            		Main.instance.getSaveConfig().save(Main.instance.savef);
					} catch (IOException e) {
						e.printStackTrace();
					}
	            }
	            Entity killer = event.getEntity().getKiller();
	            if (killer instanceof Player){
		            if(Main.instance.getSaveConfig().isSet(killer.getName()+".play") == false){
		            	Main.instance.getSaveConfig().set(killer.getName()+".play", true);
		            	Main.instance.getSaveConfig().set(killer.getName()+".play.points", 100);
		            	Main.instance.getSaveConfig().set(killer.getName()+".play.gift",  new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
		            	try {
		            		Main.instance.getSaveConfig().save(Main.instance.savef);
						} catch (IOException e) {
							e.printStackTrace();
						}
		            }
	            	Player killer2 = (Player)Bukkit.getPlayer(killer.getName());
	                int oldpoints = Main.instance.getSaveConfig().getInt(player.getName()+".play.points");
	                Main.instance.getSaveConfig().set(player.getName()+".play.points", oldpoints - 10);
	            	try {
	            		Main.instance.getSaveConfig().save(Main.instance.savef);
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	            	int newpoints = Main.instance.getSaveConfig().getInt(player.getName()+".play.points");
	            	player.sendMessage( Main.instance.getSaveConfig().getString("message_player_miss_points").replace("%killer%", killer2.getDisplayName()).replace("%points%", "10").replace("%newpoints%", Integer.toString(newpoints)) );
	                int oldpoints2 = Main.instance.getSaveConfig().getInt(killer2.getName()+".play.points");
	                Main.instance.getSaveConfig().set(killer2.getName()+".play.points", oldpoints2 + 10);
	            	try {
	            		Main.instance.getSaveConfig().save(Main.instance.savef);
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	            	int newpoints2 = Main.instance.getSaveConfig().getInt(killer2.getName()+".play.points");
	            	killer2.sendMessage( Main.instance.getSaveConfig().getString("message_player_get_points").replace("%points%", "10").replace("%entity%", player.getDisplayName()).replace("%newpoints%", Integer.toString(newpoints2)) );
	            }else{
	                int oldpoints = Main.instance.getSaveConfig().getInt(player.getName()+".play.points");
	                Main.instance.getSaveConfig().set(player.getName()+".play.points", oldpoints - 5);
	            	try {
	            		Main.instance.getSaveConfig().save(Main.instance.savef);
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	            	int newpoints = Main.instance.getSaveConfig().getInt(player.getName()+".play.points");
	            	player.sendMessage( Main.instance.getSaveConfig().getString("message_player_miss_points").replace("%killer%", killer.getName()).replace("%points%", "5").replace("%newpoints%", Integer.toString(newpoints)) );	
	            }
	    	}
	    }else{
	    	if (event.getEntity().getWorld() == zaworld){
	            Entity killer = event.getEntity().getKiller();
	            if (killer instanceof Player){
	            	Player killer2 = (Player)Bukkit.getPlayer(killer.getName());
		            if(Main.instance.getSaveConfig().isSet(killer2.getName()+".play") == false){
		            	Main.instance.getSaveConfig().set(killer2.getName()+".play", true);
		            	Main.instance.getSaveConfig().set(killer2.getName()+".play.points", 100);
		            	Main.instance.getSaveConfig().set(killer2.getName()+".play.gift",  new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
		            	try {
		            		Main.instance.getSaveConfig().save(Main.instance.savef);
						} catch (IOException e) {
							e.printStackTrace();
						}
		            }
		            int oldpoints = Main.instance.getSaveConfig().getInt(killer2.getName()+".play.points");
		            Main.instance.getSaveConfig().set(killer2.getName()+".play.points", oldpoints + 5);
	            	try {
	            		Main.instance.getSaveConfig().save(Main.instance.savef);
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	            	int newpoints = Main.instance.getSaveConfig().getInt(killer2.getName()+".play.points");
	            	killer2.sendMessage(Main.instance.getSaveConfig().getString("message_player_get_points").replace("%entity%", event.getEntity().toString()).replace("%points%", "5").replace("%newpoints%", Integer.toString(newpoints)) );	 
	            }
	    	}
	    }
    }
}
