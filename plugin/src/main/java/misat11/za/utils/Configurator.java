package misat11.za.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
		
		if (shopconfig.contains("shop-items")) {
			Set<String> s = shopconfig.getConfigurationSection("shop-items").getKeys(false);
			
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			
			for (String i : s) {
				ConfigurationSection shopItem = Main.getConfigurator().shopconfig
						.getConfigurationSection("shop-items." + i);
				
				try {
					Material material = Material.valueOf(shopItem.getString("item"));
					int amount = shopItem.getInt("amount", 5);
					int price = shopItem.getInt("points");
					String shopItemType = shopItem.getString("type", "give");
					int damage = shopItem.getInt("item-damage");
					ItemStack stack = new ItemStack(material, amount, (short) damage);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("stack", stack);
					map.put("price", price);
					map.put("price-type", shopItemType);
					list.add(map);
				} catch (Throwable t) {
					
				}
			}
			
			shopconfig.set("data", list);
			shopconfig.set("shop-items", null);
			
			try {
				shopconfig.save(shopconfigf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ItemStack readDefinedItem(String item, String def) {
		ItemStack material = new ItemStack(Material.valueOf(def));
		
		if (config.isSet("items." + item)) {
			Object obj = config.get("items." + item);
			if (obj instanceof ItemStack) {
				material = (ItemStack) obj;
			} else {
				material.setType(Material.valueOf((String) obj));
			}
		}
		
		return material;
	}
}
