package misat11.za.commands;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import misat11.za.Main;

public class ZaCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		World zaworld = Bukkit.getWorld(Main.instance.getConfig().getString("world"));
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("gift")) {
					if (player.getWorld() == zaworld) {
						String time = new SimpleDateFormat("yyyyMMdd")
								.format(Calendar.getInstance().getTime());
						if (Main.instance.getSaveConfig()
								.getString(player.getName() + ".play.gift") == time) {
							sender.sendMessage(Main.instance.getConfig().getString("message_gift_already_have"));
						} else {
							double d = Math.random();
							if (d < 0.1) {
								ItemStack item = new ItemStack(Material.BREAD, 2);
								player.getInventory().addItem(item);
								sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
										.replace("%gift%", "Bread"));
							} else if (d < 0.1) {
								ItemStack item = new ItemStack(Material.STONE_SWORD);
								player.getInventory().addItem(item);
								sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
										.replace("%gift%", "Stone sword"));
							} else if (d < 0.1) {
								ItemStack item = new ItemStack(Material.APPLE, 5);
								player.getInventory().addItem(item);
								sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
										.replace("%gift%", "Apple"));
							} else if (d < 0.1) {
								ItemStack item = new ItemStack(Material.WOOD_SWORD);
								player.getInventory().addItem(item);
								sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
										.replace("%gift%", "Wood sword"));
							} else if (d < 0.1) {
								ItemStack item = new ItemStack(Material.BOW);
								player.getInventory().addItem(item);
								sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
										.replace("%gift%", "Bow"));
							} else if (d < 0.1) {
								ItemStack item = new ItemStack(Material.ARROW, 10);
								player.getInventory().addItem(item);
								sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
										.replace("%gift%", "Arrow"));
							} else if (d < 0.1) {
								ItemStack item = new ItemStack(Material.GOLDEN_APPLE);
								player.getInventory().addItem(item);
								sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
										.replace("%gift%", "Golden Apple"));
							} else if (d < 0.1) {
								ItemStack item = new ItemStack(Material.INK_SACK, 10, (short) 4);
								player.getInventory().addItem(item);
								sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
										.replace("%gift%", "Lapis Lazuli"));
							} else if (d < 0.1) {
								ItemStack item = new ItemStack(Material.APPLE, 3);
								player.getInventory().addItem(item);
								sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
										.replace("%gift%", "Apple"));
							} else {
								ItemStack item = new ItemStack(Material.CARROT_ITEM, 2);
								player.getInventory().addItem(item);
								sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
										.replace("%gift%", "Carrot"));
							}
							Main.instance.getSaveConfig().set(player.getName() + ".play.gift",
									new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
						}
					}
				} else if (args[0].equalsIgnoreCase("points")) {
					if (Main.instance.getSaveConfig().isSet(player.getName() + ".play") == false) {
						Main.instance.getSaveConfig().set(player.getName() + ".play", true);
						Main.instance.getSaveConfig().set(player.getName() + ".play.points", 100);
						Main.instance.getSaveConfig().set(player.getName() + ".play.gift",
								new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
						try {
							Main.instance.getSaveConfig().save(Main.instance.savef);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					sender.sendMessage("You have"
							+ Integer.toString(Main.instance.getSaveConfig().getInt(player.getName() + ".play.points"))
							+ "points.");
				} else if (args[0].equalsIgnoreCase("list")) {
					String players = "";
					int playerscount = 0;
					for (Player p : Bukkit.getOnlinePlayers()) {
						if (p.getWorld().equals(zaworld)) {
							if (players == "") {
								players = p.getDisplayName();
							} else {
								players = players + ", " + p.getDisplayName();
							}
							playerscount = playerscount + 1;
						}
					}
					sender.sendMessage(Main.instance.getConfig().getString("message_players_in_arena")
							.replace("%count%", Integer.toString(playerscount)));
					sender.sendMessage(players);
				} else if (args[0].equalsIgnoreCase("join")) {
					if (Main.instance.getSaveConfig().isSet(player.getName() + ".play") == false) {
						Main.instance.getSaveConfig().set(player.getName() + ".play", true);
						Main.instance.getSaveConfig().set(player.getName() + ".play.points", 100);
						Main.instance.getSaveConfig().set(player.getName() + ".play.gift",
								new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
						try {
							Main.instance.getSaveConfig().save(Main.instance.savef);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (player.getWorld() != zaworld) {
						int x = Main.instance.getConfig().getInt("spawn_x");
						int y = Main.instance.getConfig().getInt("spawn_y");
						int z = Main.instance.getConfig().getInt("spawn_z");
						int yaw = Main.instance.getConfig().getInt("spawn_yaw");
						int pitch = Main.instance.getConfig().getInt("spawn_pitch");
						Location location = new Location(zaworld, x, y, z, yaw, pitch);
						player.teleport(location);
						sender.sendMessage(Main.instance.getConfig().getString("message_arena_teleport"));
					} else {
						sender.sendMessage(Main.instance.getConfig().getString("message_arena_already_in"));
					}
				} else if (args[0].equalsIgnoreCase("spectate")) {

					if (player.getWorld() == zaworld) {
						if (player.getGameMode() == GameMode.SPECTATOR) {
							int x = Main.instance.getConfig().getInt("spawn_x");
							int y = Main.instance.getConfig().getInt("spawn_y");
							int z = Main.instance.getConfig().getInt("spawn_z");
							int yaw = Main.instance.getConfig().getInt("spawn_yaw");
							int pitch = Main.instance.getConfig().getInt("spawn_pitch");
							Location location = new Location(zaworld, x, y, z, yaw, pitch);
							player.teleport(location);
							player.setGameMode(GameMode.ADVENTURE);
							Bukkit.broadcastMessage(Main.instance.getConfig().getString("message_change_player")
									.replace("%name%", player.getDisplayName()));
						} else {
							int x = Main.instance.getConfig().getInt("spawn_x");
							int y = Main.instance.getConfig().getInt("spawn_y");
							int z = Main.instance.getConfig().getInt("spawn_z");
							int yaw = Main.instance.getConfig().getInt("spawn_yaw");
							int pitch = Main.instance.getConfig().getInt("spawn_pitch");
							Location location = new Location(zaworld, x, y, z, yaw, pitch);
							player.teleport(location);
							player.setGameMode(GameMode.SPECTATOR);
							Bukkit.broadcastMessage(Main.instance.getConfig().getString("message_change_spectator")
									.replace("%name%", player.getDisplayName()));
						}
					}
					// TODO In version 1.1.0
					// } else if (args[0].equalsIgnoreCase("shop")){

					// ADMIN COMMANDS
				} else if (args[0].equalsIgnoreCase("setspawnloc")) {
					if (player.hasPermission("misat11.za.admin")) {
						Location playerloc = player.getLocation();
						Main.instance.getConfig().set("spawn_x", playerloc.getBlockX());
						Main.instance.getConfig().set("spawn_y", playerloc.getBlockY());
						Main.instance.getConfig().set("spawn_z", playerloc.getBlockZ());
						Main.instance.getConfig().set("spawn_yaw", (int) playerloc.getYaw());
						Main.instance.getConfig().set("spawn_pitch", (int) playerloc.getPitch());
						try {
							Main.instance.getConfig().save(Main.instance.configf);
						} catch (IOException e) {
							e.printStackTrace();
						}
						sender.sendMessage("Spawn of Arena set.");
					} else {
						sender.sendMessage("You have not any permissions!");
					}
				} else if (args[0].equalsIgnoreCase("setgiantloc")) {
					if (player.hasPermission("misat11.za.admin")) {
						Location playerloc = player.getLocation();
						Main.instance.getConfig().set("giant_x", playerloc.getBlockX());
						Main.instance.getConfig().set("giant_y", playerloc.getBlockY());
						Main.instance.getConfig().set("giant_z", playerloc.getBlockZ());
						Main.instance.getConfig().set("giant_yaw", (int) playerloc.getYaw());
						Main.instance.getConfig().set("giant_pitch", (int) playerloc.getPitch());
						try {
							Main.instance.getConfig().save(Main.instance.configf);
						} catch (IOException e) {
							e.printStackTrace();
						}
						sender.sendMessage("Spawn of Giant set.");
					} else {
						sender.sendMessage("You have not any permissions!");
					}
				} else if (args[0].equalsIgnoreCase("enablegame")) {
					if (player.hasPermission("misat11.za.admin")) {
						if (Main.instance.getConfig().getBoolean("enabled") == false) {
							Main.instance.getConfig().set("enabled", true);
							sender.sendMessage("Arena is enabled");
						} else {
							Main.instance.getConfig().set("enabled", false);
							sender.sendMessage("Arena is disabled");
						}
						try {
							Main.instance.getConfig().save(Main.instance.configf);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						sender.sendMessage("You have not any permissions!");
					}
				} else if (args[0].equalsIgnoreCase("enablegiantgame")) {
					if (player.hasPermission("misat11.za.admin")) {
						if (Main.instance.getConfig().getBoolean("spawn_giant") == false) {
							Main.instance.getConfig().set("spawn_giant", true);
							sender.sendMessage("Giant spawn is enabled. ");
						} else {
							Main.instance.getConfig().set("spawn_giant", false);
							sender.sendMessage("Giant spawn is disabled");
						}
						try {
							Main.instance.getConfig().save(Main.instance.configf);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						sender.sendMessage("You have not any permissions!");
					}

				} else {
					sender.sendMessage("ZombieApocalypse V" + Main.version + " by Misat11");
					sender.sendMessage("/za spectate - " + Main.instance.getConfig().getString("help_spectate"));
					sender.sendMessage("/za gift - " + Main.instance.getConfig().getString("help_gift"));
					sender.sendMessage("/za points - " + Main.instance.getConfig().getString("help_points"));
					sender.sendMessage("/za join - " + Main.instance.getConfig().getString("help_join"));
					sender.sendMessage("/za list - " + Main.instance.getConfig().getString("help_list"));
					sender.sendMessage("/za setspawnloc - " + Main.instance.getConfig().getString("help_setspawnloc"));
					sender.sendMessage("/za setgiantloc - " + Main.instance.getConfig().getString("help_setgiantloc"));
					sender.sendMessage("/za enablegame - " + Main.instance.getConfig().getString("help_enablegame"));
					sender.sendMessage(
							"/za enablegiantgame - " + Main.instance.getConfig().getString("help_enablegiantgame"));
				}
			} else {
				sender.sendMessage("ZombieApocalypse V" + Main.version + " by Misat11");
				sender.sendMessage("/za spectate - " + Main.instance.getConfig().getString("help_spectate"));
				sender.sendMessage("/za gift - " + Main.instance.getConfig().getString("help_gift"));
				sender.sendMessage("/za points - " + Main.instance.getConfig().getString("help_points"));
				sender.sendMessage("/za join - " + Main.instance.getConfig().getString("help_join"));
				sender.sendMessage("/za list - " + Main.instance.getConfig().getString("help_list"));
				sender.sendMessage("/za setspawnloc - " + Main.instance.getConfig().getString("help_setspawnloc"));
				sender.sendMessage("/za setgiantloc - " + Main.instance.getConfig().getString("help_setgiantloc"));
				sender.sendMessage("/za enablegame - " + Main.instance.getConfig().getString("help_enablegame"));
				sender.sendMessage(
						"/za enablegiantgame - " + Main.instance.getConfig().getString("help_enablegiantgame"));
			}
		} else {
			sender.sendMessage("It's only for players!");
		}

		return true;
	}
}
