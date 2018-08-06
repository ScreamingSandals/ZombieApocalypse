package misat11.za.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import misat11.za.Main;

public class Configurator {

	public File configf, shopconfigf;
	public FileConfiguration config, shopconfig;
	
	public final File datafolder;
	public final Main main;
	
	public Configurator(Main main) {
		this.datafolder = main.getDataFolder();
		this.main = main;
	}


	public void createFiles() {

		configf = new File(datafolder, "config.yml");
		shopconfigf = new File(datafolder, "shop.yml");

		if (!configf.exists()) {
			configf.getParentFile().mkdirs();
			main.saveResource("config.yml", false);
		}
		if (!shopconfigf.exists()) {
			shopconfigf.getParentFile().mkdirs();
			main.saveResource("shop.yml", false);
		}
		config = new YamlConfiguration();
		shopconfig = new YamlConfiguration();
		try {
			config.load(configf);
			shopconfig.load(shopconfigf);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

		if (config.isSet("locale") == false) {
			config.set("locale", "en");
			try {
				config.save(configf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (shopconfig.isSet("enable") == false) {
			shopconfig.set("enable", true);
			try {
				shopconfig.save(shopconfigf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
