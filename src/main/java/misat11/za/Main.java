package misat11.za;

import org.bukkit.plugin.java.JavaPlugin;

import misat11.za.listener.DeathListener;
import misat11.za.listener.JoinListener;
import misat11.za.listener.onTeleportListener;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Main extends JavaPlugin
{

    public File configf, savef;
    public FileConfiguration config, save;
    public static Main instance;
	
	public void onEnable() {
		instance = this;
		
		createFiles();
		
		if (this.getConfig().isSet("world")){
			this.getConfig().set("world", "zaworld");
		}
		if (this.getConfig().isSet("spawn_x")){
			this.getConfig().set("spawn_x", 0);
		}
		if (this.getConfig().isSet("spawn_y")){
			this.getConfig().set("spawn_y", 60);
		}
		if (this.getConfig().isSet("spawn_z")){
			this.getConfig().set("spawn_z", 0);
		}
		if (this.getConfig().isSet("spawn_yaw")){
			this.getConfig().set("spawn_yaw", 0);
		}
		if (this.getConfig().isSet("spawn_pitch")){
			this.getConfig().set("spawn_pitch", 0);
		}
		if (this.getConfig().isSet("spawn_giant")){
			this.getConfig().set("spawn_giant", true);
		}
		if (this.getConfig().isSet("giant_x")){
			this.getConfig().set("giant_x", 0);
		}
		if (this.getConfig().isSet("giant_y")){
			this.getConfig().set("giant_y", 60);
		}
		if (this.getConfig().isSet("giant_z")){
			this.getConfig().set("giant_z", 0);
		}
		if (this.getConfig().isSet("giant_yaw")){
			this.getConfig().set("giant_yaw", 0);
		}
		if (this.getConfig().isSet("giant_pitch")){
			this.getConfig().set("giant_pitch", 0);
		}
		if (this.getConfig().isSet("message_prefix")){
			this.getConfig().set("message_prefix", "[ZA]");
		}
		if (this.getConfig().isSet("message_join")){
			this.getConfig().set("message_join", "%name% is join to game.");
		}
		if (this.getConfig().isSet("message_leave")){
			this.getConfig().set("message_leave", "%name% is leave the game.");
		}
		if (this.getConfig().isSet("message_change_player")){
			this.getConfig().set("message_change_player", "%name% is changed to player.");
		}
		if (this.getConfig().isSet("message_change_spectator")){
			this.getConfig().set("message_change_spectator", "%name% is changed to spectator.");
		}
		if (this.getConfig().isSet("message_giant_spawned")){
			this.getConfig().set("message_giant_spawned", "Giant spawned. Apocalypse will end after kill giant.");
		}
		if (this.getConfig().isSet("message_giant_killed")){
			this.getConfig().set("message_giant_killed", "Giant killed. Apocalypse is restarting now.");
		}
		if (this.getConfig().isSet("message_phase_start")){
			this.getConfig().set("message_phase_start", "Zombie apocalypse started. Phase: %number%");
		}
		if (this.getConfig().isSet("message_death_player")){
			this.getConfig().set("message_death_player", "%player% killed by %killer%.");
		}
		if (this.getConfig().isSet("message_player_get_points")){
			this.getConfig().set("message_player_get_points", "You killed %entity% and got %points% points. Your points: %newpoints%");
		}
		if (this.getConfig().isSet("message_player_miss_points")){
			this.getConfig().set("message_player_miss_points", "You killed by %killer% and miss %points% points. Your points: %newpoints%");
		}
		if (this.getConfig().isSet("message_starting")){
			this.getConfig().set("message_starting", "Zombie Apocalypse starting in %time%");
		}
		if (this.getConfig().isSet("message_minutes")){
			this.getConfig().set("message_minutes", "minutes");
		}
		if (this.getConfig().isSet("message_minute")){
			this.getConfig().set("message_minute", "minute");
		}
		if (this.getConfig().isSet("message_seconds")){
			this.getConfig().set("message_seconds", "seconds");
		}
		if (this.getConfig().isSet("message_second")){
			this.getConfig().set("message_second", "second");
		}
        
		Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
		Bukkit.getPluginManager().registerEvents(new onTeleportListener(), this);
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
