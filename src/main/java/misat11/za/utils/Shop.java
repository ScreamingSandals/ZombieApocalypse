package misat11.za.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import misat11.za.Main;

public class Shop {
	public static ItemStack getItem(String material, int damage) {
		ItemStack item = new ItemStack(Material.getMaterial(material), 1, (short) damage);
		return item;
	}

	public static boolean buyItem(String pl, String item) {
		Player p = Bukkit.getPlayer(pl);
		if (Main.instance.getShopConfig().isSet("shop-items." + item)) {
			if (Main.instance.getSaveConfig().getInt(p.getName()
					+ ".play.points") > (Main.instance.getShopConfig().getInt("shop-items." + item + ".points") - 1)) {
				if (Main.instance.getShopConfig().getString("shop-items." + item + ".type").equals("tpaura")) {
					if (Main.instance.getSaveConfig().isSet(p.getName() + ".play.tpaura") == false) {
						Main.instance.getSaveConfig().set(p.getName() + ".play.tpaura", 5);
					} else {
						Main.instance.getSaveConfig().set(p.getName() + ".play.tpaura",
								Main.instance.getSaveConfig().getInt(p.getName() + ".play.tpaura") + 5);
					}
					Main.instance.getSaveConfig().set(p.getName() + ".play.points",
							Main.instance.getSaveConfig().getInt(p.getName() + ".play.points")
									- Main.instance.getShopConfig().getInt("shop-items." + item + ".points"));
					return true;
				} else {
					p.getInventory()
							.addItem(getItem(Main.instance.getShopConfig().getString("shop-items." + item + ".item"),
									Main.instance.getShopConfig().getInt("shop-items." + item + ".item-damage")));
					Main.instance.getSaveConfig().set(p.getName() + ".play.points",
							Main.instance.getSaveConfig().getInt(p.getName() + ".play.points")
									- Main.instance.getShopConfig().getInt("shop-items." + item + ".points"));
					return true;
				}
			}
		}
		return false;
	}
}
