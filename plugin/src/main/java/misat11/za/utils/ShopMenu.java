package misat11.za.utils;

import static misat11.lib.lang.I18n.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import misat11.lib.sgui.ItemData;
import misat11.lib.sgui.ItemInfo;
import misat11.lib.sgui.SimpleGuiFormat;
import misat11.lib.sgui.StaticGuiCreator;
import misat11.lib.sgui.StaticInventoryListener;
import misat11.lib.sgui.events.GenerateItemEvent;
import misat11.lib.sgui.events.PostActionEvent;
import misat11.lib.sgui.events.PreActionEvent;
import misat11.za.Main;
import misat11.za.game.GamePlayer;

public class ShopMenu implements Listener {
	private ItemStack backItem, pageBackItem, pageForwardItem, cosmeticItem;
	private SimpleGuiFormat format;
	private StaticGuiCreator creator;
	private StaticInventoryListener listener;

	public ShopMenu() {
		List<Map<String, Object>> data = (List<Map<String, Object>>) Main.getConfigurator().shopconfig.getList("data");

		backItem = Main.getConfigurator().readDefinedItem("shopback", "BARRIER");
		ItemMeta backItemMeta = backItem.getItemMeta();
		backItemMeta.setDisplayName(i18n("shop_back", false));
		backItem.setItemMeta(backItemMeta);

		pageBackItem = Main.getConfigurator().readDefinedItem("pageback", "ARROW");
		ItemMeta pageBackItemMeta = backItem.getItemMeta();
		pageBackItemMeta.setDisplayName(i18n("page_back", false));
		pageBackItem.setItemMeta(pageBackItemMeta);

		pageForwardItem = Main.getConfigurator().readDefinedItem("pageforward", "ARROW");
		ItemMeta pageForwardItemMeta = backItem.getItemMeta();
		pageForwardItemMeta.setDisplayName(i18n("page_forward", false));
		pageForwardItem.setItemMeta(pageForwardItemMeta);

		cosmeticItem = Main.getConfigurator().readDefinedItem("shopcosmetic", "AIR");

		format = new SimpleGuiFormat(data);
		format.generateData();

		creator = new StaticGuiCreator("[ZA] Shop", format, backItem, pageBackItem, pageForwardItem, cosmeticItem);

		listener = new StaticInventoryListener(creator);
		Bukkit.getServer().getPluginManager().registerEvents(listener, Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getInstance());

		creator.generate();
	}

	@EventHandler
	public void onGeneratingItem(GenerateItemEvent event) {
		if (event.getFormat() != format) {
			return;
		}

		ItemInfo item = event.getInfo();
		ItemData data = item.getData();
		Map<String, Object> originalItemData = data.getData();
		if (originalItemData.containsKey("price")) {
			ItemStack stack = event.getStack();
			ItemMeta stackMeta = stack.getItemMeta();
			List<String> lore = new ArrayList<String>();
			if (stackMeta.hasLore()) {
				lore = stackMeta.getLore();
			}
			int price = (int) originalItemData.get("price");

			boolean buy = true;
			if (originalItemData.containsKey("price-type")) {
				if ("sell".equalsIgnoreCase((String) originalItemData.get("price-type"))) {
					buy = false;
				}
			}

			lore.add(i18n("coins", false));
			lore.add(Integer.toString(price));
			lore.add(i18n("amount", false));
			lore.add(Integer.toString(stack.getAmount()));
			lore.add(i18n(buy ? "buy" : "sell", false));
			stackMeta.setLore(lore);
			stack.setItemMeta(stackMeta);
			event.setStack(stack);
		}

	}

	@EventHandler
	public void onPreAction(PreActionEvent event) {
		if (event.getFormat() != format || event.isCancelled()) {
			return;
		}

		if (!Main.isPlayerInGame(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPostAction(PostActionEvent event) {
		if (event.getFormat() != format || event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();
		GamePlayer gPlayer = Main.getPlayerGameProfile(event.getPlayer());

		ItemInfo item = event.getItem();
		ItemData data = item.getData();
		Map<String, Object> originalItemData = data.getData();
		if (originalItemData.containsKey("price") && originalItemData.containsKey("price-type")) {
			int price = (int) originalItemData.get("price");
			String priceType = ((String) originalItemData.get("price-type")).toLowerCase();
			ItemStack newItem = ((ItemStack) originalItemData.get("stack")).clone();
			if ("tpaura".equalsIgnoreCase(priceType)) {
				if (gPlayer.coins >= price) {
					gPlayer.teleportAura += newItem.getAmount();
					gPlayer.coins -= price;
					player.sendMessage(i18n("buy_succes").replace("%item%", i18n("teleport_aura", false))
							.replace("%yourcoins%", Integer.toString(gPlayer.coins)));
				} else {
					player.sendMessage(i18n("buy_no_coins").replace("%item%", i18n("teleport_aura", false))
							.replace("%yourcoins%", Integer.toString(gPlayer.coins)));
				}
			} else if ("sell".equalsIgnoreCase(priceType)) {
				if (player.getInventory().containsAtLeast(newItem, price)) {
					player.getInventory().removeItem(newItem);
					gPlayer.coins += price;
					player.sendMessage(i18n("sell_success").replace("%item%", getNameOrCustomNameOfItem(newItem))
							.replace("%yourcoins%", Integer.toString(gPlayer.coins)));
				} else {

					player.sendMessage(i18n("sell_no_items").replace("%item%", getNameOrCustomNameOfItem(newItem))
							.replace("%yourcoins%", Integer.toString(gPlayer.coins)));
				}
			} else {
				if (gPlayer.coins >= price) {
					gPlayer.player.getInventory().addItem(newItem);
					gPlayer.coins -= price;
					player.sendMessage(i18n("buy_succes").replace("%item%", getNameOrCustomNameOfItem(newItem))
							.replace("%yourcoins%", Integer.toString(gPlayer.coins)));
				} else {
					player.sendMessage(i18n("buy_no_coins").replace("%item%", getNameOrCustomNameOfItem(newItem))
							.replace("%yourcoins%", Integer.toString(gPlayer.coins)));
				}
			}
		}

	}

	public static String getNameOrCustomNameOfItem(ItemStack stack) {
		if (stack.hasItemMeta()) {
			ItemMeta meta = stack.getItemMeta();
			if (meta.hasDisplayName()) {
				return meta.getDisplayName();
			}
			if (meta.hasLocalizedName()) {
				return meta.getLocalizedName();
			}
		}
		String normalName = stack.getType().name().replace("_", " ").toLowerCase();
		return normalName.substring(0, 1).toUpperCase() + normalName.substring(1);
	}

	public void show(Player p) {
		p.openInventory(creator.getInventories(null).get(0));
	}
}
