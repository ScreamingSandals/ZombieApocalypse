package misat11.za;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import misat11.za.commands.ZaCommand;
import misat11.za.listener.DeathListener;
import misat11.za.listener.JoinListener;
import misat11.za.listener.LeaveListener;
import misat11.za.listener.RespawnListener;
import misat11.za.listener.onTeleportListener;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin
{

    public File configf, savef;
    public FileConfiguration config, save;
    public static Main instance;
	
	public void onEnable() {
		instance = this;
		
		createFiles();
		
		if (this.getConfig().isSet("world") == false){
			this.getConfig().set("world", "zaworld");
		}
		if (this.getConfig().isSet("spawn_x") == false){
			this.getConfig().set("spawn_x", 0);
		}
		if (this.getConfig().isSet("spawn_y") == false){
			this.getConfig().set("spawn_y", 60);
		}
		if (this.getConfig().isSet("spawn_z") == false){
			this.getConfig().set("spawn_z", 0);
		}
		if (this.getConfig().isSet("spawn_yaw") == false){
			this.getConfig().set("spawn_yaw", 0);
		}
		if (this.getConfig().isSet("spawn_pitch") == false){
			this.getConfig().set("spawn_pitch", 0);
		}
		if (this.getConfig().isSet("spawn_giant") == false){
			this.getConfig().set("spawn_giant", true);
		}
		if (this.getConfig().isSet("giant_x") == false){
			this.getConfig().set("giant_x", 0);
		}
		if (this.getConfig().isSet("giant_y") == false){
			this.getConfig().set("giant_y", 60);
		}
		if (this.getConfig().isSet("giant_z") == false){
			this.getConfig().set("giant_z", 0);
		}
		if (this.getConfig().isSet("giant_yaw") == false){
			this.getConfig().set("giant_yaw", 0);
		}
		if (this.getConfig().isSet("giant_pitch") == false){
			this.getConfig().set("giant_pitch", 0);
		}
		if (this.getConfig().isSet("message_prefix") == false){
			this.getConfig().set("message_prefix", "[ZA]");
		}
		if (this.getConfig().isSet("message_join") == false){
			this.getConfig().set("message_join", "%name% is join to game.");
		}
		if (this.getConfig().isSet("message_leave") == false){
			this.getConfig().set("message_leave", "%name% is leave the game.");
		}
		if (this.getConfig().isSet("message_change_player") == false){
			this.getConfig().set("message_change_player", "%name% is changed to player.");
		}
		if (this.getConfig().isSet("message_change_spectator") == false){
			this.getConfig().set("message_change_spectator", "%name% is changed to spectator.");
		}
		if (this.getConfig().isSet("message_giant_spawned") == false){
			this.getConfig().set("message_giant_spawned", "Giant spawned. Apocalypse will end after kill giant.");
		}
		if (this.getConfig().isSet("message_giant_killed") == false){
			this.getConfig().set("message_giant_killed", "Giant killed. Apocalypse is restarting now.");
		}
		if (this.getConfig().isSet("message_phase_start") == false){
			this.getConfig().set("message_phase_start", "Zombie apocalypse started. Phase: %number%");
		}
		if (this.getConfig().isSet("message_death_player") == false){
			this.getConfig().set("message_death_player", "%player% killed by %killer%.");
		}
		if (this.getConfig().isSet("message_player_get_points") == false){
			this.getConfig().set("message_player_get_points", "You killed %entity% and got %points% points. Your points: %newpoints%");
		}
		if (this.getConfig().isSet("message_player_miss_points") == false){
			this.getConfig().set("message_player_miss_points", "You killed by %killer% and miss %points% points. Your points: %newpoints%");
		}
		if (this.getConfig().isSet("message_starting") == false){
			this.getConfig().set("message_starting", "Zombie Apocalypse starting in %time%");
		}
		if (this.getConfig().isSet("message_minutes") == false){
			this.getConfig().set("message_minutes", "minutes");
		}
		if (this.getConfig().isSet("message_minute") == false){
			this.getConfig().set("message_minute", "minute");
		}
		if (this.getConfig().isSet("message_seconds") == false){
			this.getConfig().set("message_seconds", "seconds");
		}
		if (this.getConfig().isSet("message_second") == false){
			this.getConfig().set("message_second", "second");
		}
    	try {
    		this.getConfig().save(savef);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new LeaveListener(), this);
		Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
		Bukkit.getPluginManager().registerEvents(new onTeleportListener(), this);
		Bukkit.getPluginManager().registerEvents(new RespawnListener(), this);
		
		this.getCommand("za").setExecutor(new ZaCommand());
		
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
            	World zaworld = Bukkit.getWorld(getConfig().getString("world"));
                if(getSaveConfig().isSet("SERVER.ARENA") == false){
                	getSaveConfig().set("SERVER.ARENA.phase", 1);
                	getSaveConfig().set("SERVER.ARENA.time", "day");
                	getSaveConfig().set("SERVER.ARENA.countdown", 60);
                	try {
                		getSaveConfig().save(savef);
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
                }
            	if (getSaveConfig().getString("SERVER.ARENA.time") == "day"){
            			getSaveConfig().set("SERVER.ARENA.countdown", getSaveConfig().getInt("SERVER.ARENA.countdown")-1);
            			zaworld.setTime(0);
    	        		if (getSaveConfig().getInt("SERVER.ARENA.countdown") == 0){
    	        			if(getConfig().getBoolean("spawn_giant") == true){
    	        				if(getSaveConfig().getInt("SERVER.ARENA.phase") != 5){
    	        					getSaveConfig().set("SERVER.ARENA.phase", getSaveConfig().getInt("SERVER.ARENA.phase")+1);
    	        				}
    	        				if(getSaveConfig().getInt("SERVER.ARENA.phase") == 5){
    	    	    	            Bukkit.broadcastMessage(getConfig().getString("message_prefix") + " " + getConfig().getString("message_giant_spawned"));
    	    	                	int x = Main.instance.getConfig().getInt("giant_x");
    	    	    	            int y = Main.instance.getConfig().getInt("giant_y");
    	    	    	            int z = Main.instance.getConfig().getInt("giant_z");
    	    	    	            int yaw = Main.instance.getConfig().getInt("giant_yaw");
    	    	    	            int pitch = Main.instance.getConfig().getInt("giant_pitch");
    	    	    	            Location location = new Location(zaworld, x, y, z, yaw, pitch);
    	    	    	            Bukkit.getWorld("world").spawnEntity(location, EntityType.GIANT);
    	        				}
    	        			}
    	                	getSaveConfig().set("SERVER.ARENA.time", "night");
    	                	getSaveConfig().set("SERVER.ARENA.countdown", 300);
    	                	int x = Main.instance.getConfig().getInt("spawn_x");
    	    	            int y = Main.instance.getConfig().getInt("spawn_y");
    	    	            int z = Main.instance.getConfig().getInt("spawn_z");
    	    	            int yaw = Main.instance.getConfig().getInt("spawn_yaw");
    	    	            int pitch = Main.instance.getConfig().getInt("spawn_pitch");
    	    	            Location location = new Location(zaworld, x, y, z, yaw, pitch);
    	    	            for(Player p : Bukkit.getOnlinePlayers())
    	    	            {
    	    	              if(p.getWorld().equals(zaworld))
    	    	              {
    	    	                  p.teleport(location);
    	    	              }
    	    	            }
    	    	            Bukkit.broadcastMessage(getConfig().getString("message_prefix") + " " + getConfig().getString("message_phase_start").replace("%number%", Integer.toString(getSaveConfig().getInt("SERVER.ARENA.phase"))));
    	        		}
            		}else{
            			getSaveConfig().set("SERVER.ARENA.countdown", getSaveConfig().getInt("SERVER.ARENA.countdown")-1);
            			zaworld.setTime(20000);
    	        		if (getSaveConfig().getInt("SERVER.ARENA.countdown") == 0 && getSaveConfig().getInt("SERVER.ARENA.phase") < 5){
    	                	getSaveConfig().set("SERVER.ARENA.time", "day");
    	                	getSaveConfig().set("SERVER.ARENA.countdown", 60);
    	                	int x = Main.instance.getConfig().getInt("spawn_x");
    	    	            int y = Main.instance.getConfig().getInt("spawn_y");
    	    	            int z = Main.instance.getConfig().getInt("spawn_z");
    	    	            int yaw = Main.instance.getConfig().getInt("spawn_yaw");
    	    	            int pitch = Main.instance.getConfig().getInt("spawn_pitch");
    	    	            Location location = new Location(zaworld, x, y, z, yaw, pitch);
    	    	            for(Player p : Bukkit.getOnlinePlayers())
    	    	            {
    	    	              if(p.getWorld().equals(zaworld))
    	    	              {
    	    	                  p.teleport(location);
    	    	              }
    	    	            }
    	    	            Bukkit.broadcastMessage(getConfig().getString("message_prefix") + " " + getConfig().getString("message_starting").replace("%time%", "1 "+getConfig().getString("message_minute")));
    	        		}
            		}
	            	try {
	            		getSaveConfig().save(savef);
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
                }
        }, 0L, 20L);
	}
	
    public FileConfiguration getSaveConfig() {
        return this.save;
    }

    private void createFiles() {

        savef = new File(getDataFolder(), "save.yml");
        configf = new File(getDataFolder(), "config.yml");

        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        if (!savef.exists()) {
            savef.getParentFile().mkdirs();
            saveResource("save.yml", false);
         }

        config = new YamlConfiguration();
        save = new YamlConfiguration();
        try {
            config.load(configf);
			save.load(savef);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
    }
}
