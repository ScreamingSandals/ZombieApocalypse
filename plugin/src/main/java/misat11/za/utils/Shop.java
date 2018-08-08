package misat11.za.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import misat11.za.Main;
import misat11.za.game.GamePlayer;

public class Shop {
	public static ItemStack getItem(String material, int damage) {
		ItemStack item = new ItemStack(Material.getMaterial(material), 1, (short) damage);
		return item;
	}

	public static boolean buyItem(GamePlayer gPlayer, String item) {
		if (Main.getConfigurator().shopconfig.isSet("shop-items." + item)) {
			if (gPlayer.coins >= Main.getConfigurator().shopconfig.getInt("shop-items." + item + ".points")) {
				if (Main.getConfigurator().shopconfig.getString("shop-items." + item + ".type").equals("tpaura")) {
					gPlayer.teleportAura += 5;
					gPlayer.coins -= Main.getConfigurator().shopconfig.getInt("shop-items." + item + ".points");
					return true;
				} else {
					gPlayer.player.getInventory()
							.addItem(getItem(Main.getConfigurator().shopconfig.getString("shop-items." + item + ".item"),
									Main.getConfigurator().shopconfig.getInt("shop-items." + item + ".item-damage")));
					gPlayer.coins -= Main.getConfigurator().shopconfig.getInt("shop-items." + item + ".points");
					return true;
				}
			}
		}
		return false;
	}
}