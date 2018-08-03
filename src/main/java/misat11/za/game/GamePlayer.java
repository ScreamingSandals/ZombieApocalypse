package misat11.za.game;

import java.io.File;
import java.io.IOException;

import org.bukkit.GameMode;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import misat11.za.Main;

public class GamePlayer {

	public final Player player;
	public int lvl = 1;
	public int xp = 0;
	public int coins = 0;
	public int teleportAura = 0;
	private Game game = null;

	private StoredInventory oldinventory = new StoredInventory();

	public GamePlayer(Player player) {
		this.player = player;
		loadGamePlayerData();
	}

	public void changeGame(Game game) {
		if (this.game != null && game == null) {
			this.game.leavePlayer(this);
			this.game = null;
			this.restoreInv();
		} else if (this.game == null && game != null) {
			this.storeInv();
			this.game = game;
			this.game.joinPlayer(this);
		} else if (this.game != null && game != null) {
			this.game.leavePlayer(this);
			this.game = game;
			this.game.joinPlayer(this);
		}
		saveGamePlayerData();
	}

	public Game getGame() {
		return game;
	}

	public boolean isInGame() {
		return game != null;
	}

	public void storeInv() {
		oldinventory.inventory = player.getInventory().getContents();
		oldinventory.armor = player.getInventory().getArmorContents();
		oldinventory.xp = Float.valueOf(player.getExp());
		oldinventory.effects = player.getActivePotionEffects();
		oldinventory.mode = player.getGameMode();
		oldinventory.left = player.getLocation();
		oldinventory.level = player.getLevel();
		oldinventory.listName = player.getPlayerListName();
		oldinventory.displayName = player.getDisplayName();
		oldinventory.foodLevel = player.getFoodLevel();
	}

	public void restoreInv() {
		player.getInventory().setContents(oldinventory.inventory);
		player.getInventory().setArmorContents(oldinventory.armor);

		player.addPotionEffects(oldinventory.effects);
		player.setLevel(oldinventory.level);
		player.setExp(oldinventory.xp);
		player.setFoodLevel(oldinventory.foodLevel);

		for (PotionEffect e : player.getActivePotionEffects())
			player.removePotionEffect(e.getType());

		player.addPotionEffects(oldinventory.effects);

		player.setPlayerListName(oldinventory.listName);
		player.setDisplayName(oldinventory.displayName);

		player.setGameMode(oldinventory.mode);

		if (oldinventory.mode == GameMode.CREATIVE)
			player.setAllowFlight(true);

		player.updateInventory();
		player.teleport(oldinventory.left);
		player.resetPlayerTime();
	}

	public void saveGamePlayerData() {
		String saveCfgGroup = player.getName().toLowerCase();
		File file = new File(Main.getInstance().getDataFolder().toString() + "/players/" + saveCfgGroup + ".yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileConfiguration pconfig = new YamlConfiguration();
		pconfig.set("coins", coins);
		pconfig.set("lvl", lvl);
		pconfig.set("xp", xp);
		pconfig.set("teleportAura", teleportAura);

		try {
			pconfig.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadGamePlayerData() {
		String saveCfgGroup = player.getName().toLowerCase();
		File file = new File(Main.getInstance().getDataFolder().toString() + "/players/" + saveCfgGroup + ".yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileConfiguration pconfig = new YamlConfiguration();
		try {
			pconfig.load(file);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		if (pconfig.isSet("coins"))
			coins = pconfig.getInt("coins");
		if (pconfig.isSet("lvl"))
			lvl = pconfig.getInt("lvl");
		if (pconfig.isSet("xp"))
			xp = pconfig.getInt("xp");
		if (pconfig.isSet("teleportAura"))
			teleportAura = pconfig.getInt("teleportAura");

		saveGamePlayerData();
	}
}
