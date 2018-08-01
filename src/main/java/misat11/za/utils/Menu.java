package misat11.za.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import misat11.za.Main;
import misat11.za.utils.Shop;

public class Menu implements Listener {

	private Inventory inv;
	private HashMap<String, String> ids = new HashMap<String, String>();

	public Menu(Plugin p) {
		inv = Bukkit.getServer().createInventory(null, 54, "[ZA] Shop");
		int lastpos = 0;

		Set<String> s = Main.instance.getShopConfig().getConfigurationSection("shop-items").getKeys(false);

		for (String i : s) {
			inv.setItem(lastpos,
					createItem(Main.instance.getShopConfig().getString("shop-items." + i + ".item"),
							Main.instance.getShopConfig().getInt("shop-items." + i + ".item-damage"),
							Main.instance.getShopConfig().getString("shop-items." + i + ".name"),
							Integer.toString(Main.instance.getShopConfig().getInt("shop-items." + i + ".points"))));
			addIds(Integer.toString(lastpos), i);
			lastpos = lastpos + 1;
		}

		Bukkit.getServer().getPluginManager().registerEvents(this, p);
	}

	private ItemStack createItem(String material, int damage, String name, String points) {
		ItemStack i = new ItemStack(Material.getMaterial(material), 1, (short) damage);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(name);
		im.setLore(Arrays.asList("Points:", points));
		i.setItemMeta(im);
		return i;
	}

	public void addIds(String key, String value) {
		this.ids.put(key, value);
	}

	public String getIds(String key) {
		return this.ids.get(key);
	}

	public void show(Player p) {
		p.openInventory(inv);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!e.getInventory().getName().equalsIgnoreCase(inv.getName()))
			return;
		if (e.getCurrentItem().getItemMeta() == null)
			return;
		e.setCancelled(true);
		String i = getIds(Integer.toString(e.getSlot()));
		boolean buy = Shop.buyItem(e.getWhoClicked().getName(), i);
		if (buy == true) {
			e.getWhoClicked().closeInventory();
			e.getWhoClicked().sendMessage(Main.instance.getConfig().getString("message_buy_succes")
					.replace("%item%", Main.instance.getShopConfig().getString("shop-items." + i + ".name"))
					.replace("%yourpoints%", Integer.toString(
							Main.instance.getSaveConfig().getInt(e.getWhoClicked().getName() + ".play.points"))));
		} else {
			e.getWhoClicked().closeInventory();
			e.getWhoClicked().sendMessage(Main.instance.getConfig().getString("message_buy_no_points")
					.replace("%item%", Main.instance.getShopConfig().getString("shop-items." + i + ".name"))
					.replace("%yourpoints%", Integer.toString(
							Main.instance.getSaveConfig().getInt(e.getWhoClicked().getName() + ".play.points"))));

		}
	}
}