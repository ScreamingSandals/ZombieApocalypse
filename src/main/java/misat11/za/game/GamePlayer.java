package misat11.za.game;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class GamePlayer {

	public final Player player;
	public int health;
	public int lvl;
	public int xp;
	public ItemStack[] items;

	private StoredInventory oldinventory = new StoredInventory();

	public GamePlayer(Player player) {
		this.player = player;
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

		for (PotionEffect e : player.getActivePotionEffects()) {
			player.removePotionEffect(e.getType());
		}

		player.addPotionEffects(oldinventory.effects);

		player.setPlayerListName(oldinventory.listName);
		player.setDisplayName(oldinventory.displayName);

		player.setGameMode(oldinventory.mode);

		if (oldinventory.mode == GameMode.CREATIVE) {
			player.setAllowFlight(true);
		}
		player.updateInventory();
	}
}
