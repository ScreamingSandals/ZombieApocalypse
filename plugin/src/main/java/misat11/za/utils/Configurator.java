package misat11.za.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import misat11.za.Main;

public class Configurator {

	public File configf, shopconfigf, signconfigf;
	public FileConfiguration config, shopconfig, signconfig;

	public final File datafolder;
	public final Main main;

	public Configurator(Main main) {
		this.datafolder = main.getDataFolder();
		this.main = main;
	}

	public void createFiles() {

		configf = new File(datafolder, "config.yml");
		shopconfigf = new File(datafolder, "shop.yml");
		signconfigf = new File(datafolder, "sign.yml");

		if (!configf.exists()) {
			configf.getParentFile().mkdirs();
			main.saveResource("config.yml", false);
		}
		if (!shopconfigf.exists()) {
			shopconfigf.getParentFile().mkdirs();
			main.saveResource("shop.yml", false);
		}
		if (!signconfigf.exists()) {
			signconfigf.getParentFile().mkdirs();
			main.saveResource("sign.yml", false);
		}
		config = new YamlConfiguration();
		shopconfig = new YamlConfiguration();
		signconfig = new YamlConfiguration();
		try {
			config.load(configf);
			shopconfig.load(shopconfigf);
			signconfig.load(signconfigf);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
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

		AtomicBoolean modify = new AtomicBoolean(false);
		checkOrSetConfig(modify, "locale", "en");
		checkOrSetConfig(modify, "allow-crafting", false);
		checkOrSetConfig(modify, "allow-workbench-crafting", true);
		checkOrSetConfig(modify, "make-villagers-aggressive-when-phase-started", false);
		checkOrSetConfig(modify, "prevent-spawning-villager-if-previous-is-living", false);
		checkOrSetConfig(modify, "farmBlocks", new ArrayList<>());
		checkOrSetConfig(modify, "reward.default", 5);
		checkOrSetConfig(modify, "reward.GIANT", 50);
		checkOrSetConfig(modify, "vault.enable", true);
		checkOrSetConfig(modify, "vault.reward.default", 1);
		checkOrSetConfig(modify, "vault.reward.GIANT", 10);
		checkOrSetConfig(modify, "vault.reward.PLAYER", 5);
		checkOrSetConfig(modify, "items.shopback", "BARRIER");
		checkOrSetConfig(modify, "items.pageback", "ARROW");
		checkOrSetConfig(modify, "items.pageforward", "ARROW");
		checkOrSetConfig(modify, "items.shopcosmetic", "AIR");
		checkOrSetConfig(modify, "disable-drops", new ArrayList<>());
		checkOrSetConfig(modify, "giant-drops", Arrays.asList(new ItemStack(Material.IRON_INGOT, 5),
				new ItemStack(Material.GOLD_INGOT, 5), new ItemStack(Material.DIAMOND, 1)));
		checkOrSetConfig(modify, "giant-xp", 100);
		checkOrSetConfig(modify, "scoreboard.enabled", true);
		checkOrSetConfig(modify, "scoreboard.title", "§aZombieApocalypse§f - %elapsedTime%");
		checkOrSetConfig(modify, "scoreboard.content", Arrays.asList("§fMap: §2%arena%", "§fPlayers: §2%players%",
				"§fPhase: §l%phase%", "§4Leave: /za leave"));
		checkOrSetConfig(modify, "sign", Arrays.asList("§a§l[ZombieApocalypse]", "%arena%", "%status%", "%players%"));
		if (modify.get()) {
			try {
				config.save(configf);
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

	private void checkOrSetConfig(AtomicBoolean modify, String path, Object value) {
		checkOrSet(modify, this.config, path, value);
	}

	private static void checkOrSet(AtomicBoolean modify, FileConfiguration config, String path, Object value) {
		if (!config.isSet(path)) {
			config.set(path, value);
			modify.set(true);
		}
	}
}
