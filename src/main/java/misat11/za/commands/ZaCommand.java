package misat11.za.commands;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import misat11.za.Main;

public class ZaCommand implements CommandExecutor {
	
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	World zaworld = Bukkit.getWorld(Main.instance.getConfig().getString("world"));
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args[0] == "gift"){
            	//TODO
            } else if (args[0] == "points"){
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
                sender.sendMessage("You have" + Integer.toString(Main.instance.getSaveConfig().getInt(player.getName()+".play.points")) + "points.");
            } else if (args[0] == "list"){
            	String players = "";
            	int playerscount = 0;
	            for(Player p : Bukkit.getOnlinePlayers())
	            {
	              if(p.getWorld().equals(zaworld))
	              {
	            	  if (players == ""){
		                  players = p.getDisplayName();
	            	  } else {
	            		  players = players + ", " + p.getDisplayName();
	            	  }
	                  playerscount = playerscount + 1;
	              }
	            }
                sender.sendMessage("In arena is " + Integer.toString(playerscount) + " players.");
                sender.sendMessage(players);
            } else if (args[0] == "join"){
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
                if (player.getWorld() != zaworld){
                    int x = Main.instance.getConfig().getInt("spawn_x");
                    int y = Main.instance.getConfig().getInt("spawn_y");
                    int z = Main.instance.getConfig().getInt("spawn_z");
                    int yaw = Main.instance.getConfig().getInt("spawn_yaw");
                    int pitch = Main.instance.getConfig().getInt("spawn_pitch");
                    Location location = new Location(zaworld, x, y, z, yaw, pitch);
                    player.teleport(location);
                    sender.sendMessage("You are teleported to arena.");
                }else{
                	sender.sendMessage("You are already in arena.");
                }

            //TODO In version 1.1.0	
            //} else if (args[0] == "shop"){
            	
            	
            //ADMIN COMMANDS
            } else if (args[0] == "setspawnloc"){
            	if (player.hasPermission("misat11.za.admin")) {
            			//TODO
            		} else {
            			sender.sendMessage("You have not any permissions!");
            		}
            } else if (args[0] == "setgiantloc"){
            	if (player.hasPermission("misat11.za.admin")) {
        			//TODO
        		} else {
        			sender.sendMessage("You have not any permissions!");
        		}
            } else if (args[0] == "enablegiantgame"){
            	if (player.hasPermission("misat11.za.admin")) {
        			//TODO
        		} else {
        			sender.sendMessage("You have not any permissions!");
        		}
            } else if (args[0] == "enablegame"){
            	if (player.hasPermission("misat11.za.admin")) {
        			//TODO
        		} else {
        			sender.sendMessage("You have not any permissions!");
        		}
            	
        	//TODO In version 1.1.0	
            //} else if (args[0] == "setzombiespawn"){
            //	if (player.hasPermission("misat11.za.admin")) {
        	//		
        	//	} else {
        	//		sender.sendMessage("You have not any permissions!");
        	//	}
            //} else if (args[0] == "delzombiespawn"){
            //	if (player.hasPermission("misat11.za.admin")) {
        	//		
        	//	} else {
        	//		sender.sendMessage("You have not any permissions!");
        	//	}

            }else{
            	
            }
        }else{
        	sender.sendMessage("It's only for players!");
        }
        
    	return true;
    }
}
