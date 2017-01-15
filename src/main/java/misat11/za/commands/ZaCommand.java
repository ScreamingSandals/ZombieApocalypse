package misat11.za.commands;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
					if (Main.instance.getConfig().getBoolean("enabled") == true) {
						if (Main.instance.getConfig().getBoolean("enabled") == true) {
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
						}
						if (player.getWorld() == zaworld) {
							String time = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
							if (Main.instance.getSaveConfig().getString(player.getName() + ".play.gift").equals(time)) {
								sender.sendMessage(Main.instance.getConfig().getString("message_gift_already_have"));
							} else {
								double d = Math.random();
								if (d < 0.1) {
									ItemStack item = new ItemStack(Material.BREAD, 2);
									player.getInventory().addItem(item);
									sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
											.replace("%gift%", "Bread"));
								} else if (d < 0.2) {
									ItemStack item = new ItemStack(Material.STONE_SWORD);
									player.getInventory().addItem(item);
									sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
											.replace("%gift%", "Stone sword"));
								} else if (d < 0.3) {
									ItemStack item = new ItemStack(Material.APPLE, 5);
									player.getInventory().addItem(item);
									sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
											.replace("%gift%", "Apple"));
								} else if (d < 0.4) {
									ItemStack item = new ItemStack(Material.WOOD_SWORD);
									player.getInventory().addItem(item);
									sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
											.replace("%gift%", "Wood sword"));
								} else if (d < 0.5) {
									ItemStack item = new ItemStack(Material.BOW);
									player.getInventory().addItem(item);
									sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
											.replace("%gift%", "Bow"));
								} else if (d < 0.6) {
									ItemStack item = new ItemStack(Material.ARROW, 10);
									player.getInventory().addItem(item);
									sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
											.replace("%gift%", "Arrow"));
								} else if (d < 0.7) {
									ItemStack item = new ItemStack(Material.GOLDEN_APPLE);
									player.getInventory().addItem(item);
									sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
											.replace("%gift%", "Golden Apple"));
								} else if (d < 0.8) {
									ItemStack item = new ItemStack(Material.INK_SACK, 10, (short) 4);
									player.getInventory().addItem(item);
									sender.sendMessage(Main.instance.getConfig().getString("message_gift_got")
											.replace("%gift%", "Lapis Lazuli"));
								} else if (d < 0.9) {
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
					sender.sendMessage(Main.instance.getConfig().getString("message_have_points").replace("%points%",
							Integer.toString(Main.instance.getSaveConfig().getInt(player.getName() + ".play.points"))));
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
					if (Main.instance.getConfig().getBoolean("enabled") == true) {
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
					}
				} else if (args[0].equalsIgnoreCase("spectate")) {
					if (Main.instance.getConfig().getBoolean("enabled") == true) {
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
					}
				} else if (args[0].equalsIgnoreCase("phaseinfo")) {
					if (Main.instance.getConfig().getBoolean("enabled") == true) {
						if (Main.instance.getSaveConfig().getString("SERVER.ARENA.time") == "day") {
							sender.sendMessage(Main.instance.getConfig().getString("message_phase_day")
									.replace("%phase%",
											Integer.toString(
													Main.instance.getSaveConfig().getInt("SERVER.ARENA.phase")))
									.replace("%countdown%", Integer
											.toString(Main.instance.getSaveConfig().getInt("SERVER.ARENA.countdown"))));
						} else {
							sender.sendMessage(Main.instance.getConfig().getString("message_phase_night")
									.replace("%phase%",
											Integer.toString(
													Main.instance.getSaveConfig().getInt("SERVER.ARENA.phase")))
									.replace("%countdown%", Integer
											.toString(Main.instance.getSaveConfig().getInt("SERVER.ARENA.countdown"))));
						}
					}
					// TODO In version 1.1.0
					// } else if (args[0].equalsIgnoreCase("shop")){

					// ADMIN COMMANDS
				} else if (args[0].equalsIgnoreCase("setspawnloc")) {
					if (player.hasPermission("misat11.za.admin")) {
						Location playerloc = player.getLocation();
						Main.instance.getConfig().set("world", playerloc.getWorld().getName());
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
						sender.sendMessage(
								"Spawn of Arena set. World of arena set to: " + playerloc.getWorld().getName());
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
				} else if (args[0].equalsIgnoreCase("arena")) {
					if (player.hasPermission("misat11.za.admin")) {
						if (player.getWorld() == zaworld) {
							if (args.length > 1) {
								if (args[1].equalsIgnoreCase("pos1")) {
									if (args.length == 3) {
										if (Main.instance.getConfig()
												.isSet("arena_settings." + args[2].toString()) == false) {
											Main.instance.getConfig()
													.set("arena_settings." + args[2].toString() + ".pos1_x", 0);
											Main.instance.getConfig()
													.set("arena_settings." + args[2].toString() + ".pos1_z", 0);
											Main.instance.getConfig()
													.set("arena_settings." + args[2].toString() + ".pos2_x", 0);
											Main.instance.getConfig()
													.set("arena_settings." + args[2].toString() + ".pos2_z", 0);
											Main.instance.getConfig()
													.set("arena_settings." + args[2].toString() + ".countdown", 10);
										}

										Location ploc = player.getLocation();
										Main.instance.getConfig().set(
												"arena_settings." + args[2].toString() + ".pos1_x", ploc.getBlockX());
										Main.instance.getConfig().set(
												"arena_settings." + args[2].toString() + ".pos1_z", ploc.getBlockZ());

										sender.sendMessage("Arena pos 1 set.");
									} else {
										sender.sendMessage("Too many arguments.");
									}
								} else if (args[1].equalsIgnoreCase("pos2")) {
									if (args.length == 3) {
										if (Main.instance.getConfig()
												.isSet("arena_settings." + args[2].toString()) == false) {
											Main.instance.getConfig()
													.set("arena_settings." + args[2].toString() + ".pos1_x", 0);
											Main.instance.getConfig()
													.set("arena_settings." + args[2].toString() + ".pos1_z", 0);
											Main.instance.getConfig()
													.set("arena_settings." + args[2].toString() + ".pos2_x", 0);
											Main.instance.getConfig()
													.set("arena_settings." + args[2].toString() + ".pos2_z", 0);
											Main.instance.getConfig()
													.set("arena_settings." + args[2].toString() + ".countdown", 10);
										}

										Location ploc = player.getLocation();
										Main.instance.getConfig().set(
												"arena_settings." + args[2].toString() + ".pos2_x", ploc.getBlockX());
										Main.instance.getConfig().set(
												"arena_settings." + args[2].toString() + ".pos2_z", ploc.getBlockZ());

										sender.sendMessage("Arena pos 2 set.");
									} else {
										sender.sendMessage("Too many arguments.");
									}
								} else if (args[1].equalsIgnoreCase("countdown")) {
									if (args.length == 4) {
										if (Main.instance.getConfig()
												.isSet("arena_settings." + args[2].toString()) == false) {
											Main.instance.getConfig()
													.set("arena_settings." + args[2].toString() + ".pos1_x", 0);
											Main.instance.getConfig()
													.set("arena_settings." + args[2].toString() + ".pos1_z", 0);
											Main.instance.getConfig()
													.set("arena_settings." + args[2].toString() + ".pos2_x", 0);
											Main.instance.getConfig()
													.set("arena_settings." + args[2].toString() + ".pos2_z", 0);
											Main.instance.getConfig()
													.set("arena_settings." + args[2].toString() + ".countdown", 10);
										}

										Main.instance.getConfig().set(
												"arena_settings." + args[2].toString() + ".countdown",
												Integer.parseInt(args[3]));

										sender.sendMessage("Countdown set.");
									} else {
										sender.sendMessage("Too many arguments.");
									}
								} else if (args[1].equalsIgnoreCase("delete")) {
									if (args.length == 3) {
										if (Main.instance.getConfig()
												.isSet("arena_settings." + args[2].toString()) == true) {
											Main.instance.getConfig().set("arena_settings." + args[2].toString(), null);
										}

										sender.sendMessage("Customize arena removed.");
									} else {
										sender.sendMessage("Too many arguments.");
									}
								} else if (args[1].equalsIgnoreCase("list")) {
									String list = "";
									Set<String> arena_settings = Main.instance.getConfig()
											.getConfigurationSection("arena_settings").getKeys(false);
									for (String arena_setting : arena_settings) {
										if (list.equals("")) {
											list = arena_setting;
										} else {
											list = list + ", " + arena_setting;
										}
									}
									sender.sendMessage("List of zombie arena spawns:");
									sender.sendMessage(list);
								} else {
									sender.sendMessage("Too many arguments.");
								}
							} else {
								sender.sendMessage("Too many arguments.");
							}
							try {
								Main.instance.getConfig().save(Main.instance.configf);
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							sender.sendMessage("You aren't in world where is set /za setspawnloc");
						}
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
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (player.hasPermission("misat11.za.admin")) {
						Main.instance.createFiles();
						sender.sendMessage("Config reloaded.");
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
					sender.sendMessage("/za phaseinfo - " + Main.instance.getConfig().getString("help_phaseinfo"));
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
					sender.sendMessage("/za arena <pos1|pos2|countdown|delete> - "
							+ Main.instance.getConfig().getString("help_arena"));
				}
			} else {
				sender.sendMessage("ZombieApocalypse V" + Main.version + " by Misat11");
				sender.sendMessage("/za phaseinfo - " + Main.instance.getConfig().getString("help_phaseinfo"));
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
				sender.sendMessage("/za arena <pos1|pos2|countdown|delete> - "
						+ Main.instance.getConfig().getString("help_arena"));
			}
		} else {
			sender.sendMessage("It's only for players!");
		}

		return true;
	}
}
