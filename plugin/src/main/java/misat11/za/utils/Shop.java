package misat11.za.utils;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import misat11.za.Main;
import misat11.za.game.GamePlayer;

public class Shop {
	public static ItemStack getItem(String material, int amount, int damage) {
		return new ItemStack(Material.getMaterial(material), amount, (short) damage);
	}

	public static boolean buyItem(GamePlayer gPlayer, String item) {
		if (Main.getConfigurator().shopconfig.isSet("shop-items." + item)) {
			ConfigurationSection shopItem = Main.getConfigurator().shopconfig
					.getConfigurationSection("shop-items." + item);
			if (gPlayer.coins >= shopItem.getInt("points")) {
				if (shopItem.getString("type").equals("tpaura")) {
					gPlayer.teleportAura += shopItem.isSet("amount") ? shopItem.getInt("amount") : 5;
					gPlayer.coins -= shopItem.getInt("points");
					return true;
				} else if (shopItem.getString("type").equals("sell")) {
					ItemStack stack = getItem(shopItem.getString("item"), shopItem.isSet("amount") ? shopItem.getInt("amount") : 1, shopItem.getInt("item-damage"));
					if (gPlayer.player.getInventory().containsAtLeast(stack, shopItem.isSet("amount") ? shopItem.getInt("amount") : 1)) {
						gPlayer.player.getInventory().removeItem(stack);
						gPlayer.coins += shopItem.getInt("points");
						return true;
					}
				} else {
					ItemStack stack = getItem(shopItem.getString("item"),
							shopItem.isSet("amount") ? shopItem.getInt("amount") : 1, shopItem.getInt("item-damage"));
					gPlayer.player.getInventory().addItem(stack);
					gPlayer.coins -= shopItem.getInt("points");
					return true;
				}
			}
		}
		return false;
	}
}