package misat11.za.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import misat11.za.Main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static misat11.lib.lang.I18n.i18nonly;

public class KitSelectorInventory implements Listener {

	private Inventory inv;
	private int index = 0;
	private Map<Integer, List<ItemStack>> stacks = new HashMap<>();

	public KitSelectorInventory() {
		List<Map<String, Object>> kits = (List<Map<String, Object>>) Main.getConfigurator().config.getList("start_kits");
		int slotCount = 9;
		if (kits.size() > 9) {
			slotCount = 18;
		}
		if (kits.size() > 18) {
			slotCount = 54; // What??? There are more than 18 kits?
		}

		String teamSelectionName = i18nonly("kit_selection_inventory", "Start kit selection");
		inv = Bukkit.getServer().createInventory(null, slotCount, teamSelectionName);
		
		for (Map<String, Object> kit : kits) {
			ItemStack stack = (ItemStack) kit.get("stack");
			inv.setItem(index, stack);
			if (kit.containsKey("items")) {
				stacks.put(index, (List<ItemStack>) kit.get("items"));
			}
			index++;
		}

		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getInstance());
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!e.getInventory().equals(inv))
			return;
		e.setCancelled(true);
		if (!(e.getWhoClicked() instanceof Player)) {
			e.getWhoClicked().closeInventory();
			return; // How this happened?
		}
		Player player = (Player) e.getWhoClicked();
		if (!Main.isPlayerInGame(player)) {
			player.closeInventory();
			return;
		}
		if (e.getCurrentItem() == null) {
			return;
		}
		int slot = e.getSlot();
		if (stacks.containsKey(slot)) {
			for (ItemStack stack : stacks.get(slot)) {
				player.getInventory().addItem(stack);
			}
		}
		player.closeInventory();
	}

	public void openForPlayer(Player player) {
		player.openInventory(inv);
	}

}
