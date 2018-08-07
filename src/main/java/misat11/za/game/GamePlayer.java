package misat11.za.game;

import java.io.File;
import java.io.IOException;

import org.bukkit.GameMode;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import misat11.za.Main;

public class GamePlayer {

	public final Player player;
	public int lvl = 1;
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
			this.clean();
			this.game = game;
			this.game.joinPlayer(this);
		} else if (this.game != null && game != null) {
			this.game.leavePlayer(this);
			this.clean();
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
		File dir = new File(Main.getInstance().getDataFolder(), "players");
		if (!dir.exists())
			dir.mkdir();
		File file = new File(dir, saveCfgGroup + ".yml");
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
		pconfig.set("teleportAura", teleportAura);

		try {
			pconfig.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	  public void clean() {

		    PlayerInventory inv = this.player.getInventory();
		    inv.setArmorContents(new ItemStack[4]);
		    inv.setContents(new ItemStack[]{});

		    this.player.setAllowFlight(false);
		    this.player.setFlying(false);
		    this.player.setExp(0.0F);
		    this.player.setLevel(0);
		    this.player.setSneaking(false);
		    this.player.setSprinting(false);
		    this.player.setFoodLevel(20);
		    this.player.setSaturation(10);
		    this.player.setExhaustion(0);
		    this.player.setHealth(20.0D);
		    this.player.setFireTicks(0);
		    this.player.setGameMode(GameMode.ADVENTURE);

		    if (this.player.isInsideVehicle()) {
		      this.player.leaveVehicle();
		    }

		    for (PotionEffect e : this.player.getActivePotionEffects()) {
		      this.player.removePotionEffect(e.getType());
		    }

		    this.player.updateInventory();
		  }

	public void loadGamePlayerData() {
		String saveCfgGroup = player.getName().toLowerCase();
		File dir = new File(Main.getInstance().getDataFolder(), "players");
		if (!dir.exists())
			dir.mkdir();
		File file = new File(dir, saveCfgGroup + ".yml");
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
		if (pconfig.isSet("teleportAura"))
			teleportAura = pconfig.getInt("teleportAura");

		saveGamePlayerData();
	}
}