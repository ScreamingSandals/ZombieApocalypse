package misat11.za.listener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import misat11.za.Main;

public class DeathListener implements Listener {

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (Main.instance.getConfig().getBoolean("enabled") == true) {
			World zaworld = Bukkit.getWorld(Main.instance.getConfig().getString("world"));
			if (event.getEntity() instanceof Player) {
				if (event.getEntity().getWorld() == zaworld) {
					final Player player = (Player) Bukkit.getPlayer(event.getEntity().getName());
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
					Entity killer = event.getEntity().getKiller();
					if (killer instanceof Player) {
						if (Main.instance.getSaveConfig().isSet(killer.getName() + ".play") == false) {
							Main.instance.getSaveConfig().set(killer.getName() + ".play", true);
							Main.instance.getSaveConfig().set(killer.getName() + ".play.points", 100);
							Main.instance.getSaveConfig().set(killer.getName() + ".play.gift",
									new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
							try {
								Main.instance.getSaveConfig().save(Main.instance.savef);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						Player killer2 = (Player) Bukkit.getPlayer(killer.getName());
						int oldpoints = Main.instance.getSaveConfig().getInt(player.getName() + ".play.points");
						Main.instance.getSaveConfig().set(player.getName() + ".play.points", oldpoints - 10);
						try {
							Main.instance.getSaveConfig().save(Main.instance.savef);
						} catch (IOException e) {
							e.printStackTrace();
						}
						int newpoints = Main.instance.getSaveConfig().getInt(player.getName() + ".play.points");
						player.sendMessage(Main.instance.getConfig().getString("message_player_miss_points")
								.replace("%killer%", killer2.getDisplayName()).replace("%points%", "10")
								.replace("%newpoints%", Integer.toString(newpoints)));
						int oldpoints2 = Main.instance.getSaveConfig().getInt(killer2.getName() + ".play.points");
						Main.instance.getSaveConfig().set(killer2.getName() + ".play.points", oldpoints2 + 10);
						try {
							Main.instance.getSaveConfig().save(Main.instance.savef);
						} catch (IOException e) {
							e.printStackTrace();
						}
						int newpoints2 = Main.instance.getSaveConfig().getInt(killer2.getName() + ".play.points");
						killer2.sendMessage(Main.instance.getConfig().getString("message_player_get_points")
								.replace("%points%", "10").replace("%entity%", player.getDisplayName())
								.replace("%newpoints%", Integer.toString(newpoints2)));
						if (Main.isVault == true && oldpoints2 % 100 != 0 && newpoints2 % 100 == 0) {
							killer2.sendMessage(Main.instance.getConfig().getString("message_get_vault_money").replace("%money%", "10"));
							Main.econ.depositPlayer(killer2, 10.00);
						}
					} else {
						int oldpoints = Main.instance.getSaveConfig().getInt(player.getName() + ".play.points");
						Main.instance.getSaveConfig().set(player.getName() + ".play.points", oldpoints - 5);
						try {
							Main.instance.getSaveConfig().save(Main.instance.savef);
						} catch (IOException e) {
							e.printStackTrace();
						}
						int newpoints = Main.instance.getSaveConfig().getInt(player.getName() + ".play.points");
						player.sendMessage(Main.instance.getConfig().getString("message_player_miss_points")
								.replace("%killer%", "MONSTER").replace("%points%", "5")
								.replace("%newpoints%", Integer.toString(newpoints)));
					}
					if (Main.isSpigot == true) {
						new BukkitRunnable() {

							public void run() {
								player.spigot().respawn();
							}
						}.runTaskLater(Main.instance, 20L);
					}
				}
			} else {
				if (event.getEntity().getWorld() == zaworld) {
					if (event.getEntity().getType() == EntityType.GIANT) {
						Entity killer = event.getEntity().getKiller();
						if (killer instanceof Player) {
							Player killer2 = (Player) Bukkit.getPlayer(killer.getName());
							if (Main.instance.getSaveConfig().isSet(killer2.getName() + ".play") == false) {
								Main.instance.getSaveConfig().set(killer2.getName() + ".play", true);
								Main.instance.getSaveConfig().set(killer2.getName() + ".play.points", 100);
								Main.instance.getSaveConfig().set(killer2.getName() + ".play.gift",
										new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
								try {
									Main.instance.getSaveConfig().save(Main.instance.savef);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							int oldpoints = Main.instance.getSaveConfig().getInt(killer2.getName() + ".play.points");
							Main.instance.getSaveConfig().set(killer2.getName() + ".play.points", oldpoints + 100);
							Main.instance.getSaveConfig().set("SERVER.ARENA.phase", 0);
							Main.instance.getSaveConfig().set("SERVER.ARENA.countdown", 60);
							Main.instance.getSaveConfig().set("SERVER.ARENA.time", "day");
							try {
								Main.instance.getSaveConfig().save(Main.instance.savef);
							} catch (IOException e) {
								e.printStackTrace();
							}
							if (Main.isVault == true) {
								killer2.sendMessage(Main.instance.getConfig().getString("message_get_vault_money").replace("%money%", "50"));
								Main.econ.depositPlayer(killer2, 50.00);
							}
							for (LivingEntity f : zaworld.getLivingEntities()) {
								if (f instanceof Giant) {
									f.remove();
									continue;
								}
							}
							Bukkit.broadcastMessage(Main.instance.getConfig().getString("message_prefix") + " "
									+ Main.instance.getConfig().getString("message_giant_killed"));
						}
					} else {
						Entity killer = event.getEntity().getKiller();
						if (killer instanceof Player) {
							Player killer2 = (Player) Bukkit.getPlayer(killer.getName());
							if (Main.instance.getSaveConfig().isSet(killer2.getName() + ".play") == false) {
								Main.instance.getSaveConfig().set(killer2.getName() + ".play", true);
								Main.instance.getSaveConfig().set(killer2.getName() + ".play.points", 100);
								Main.instance.getSaveConfig().set(killer2.getName() + ".play.gift",
										new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
								try {
									Main.instance.getSaveConfig().save(Main.instance.savef);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							int oldpoints = Main.instance.getSaveConfig().getInt(killer2.getName() + ".play.points");
							Main.instance.getSaveConfig().set(killer2.getName() + ".play.points", oldpoints + 5);
							try {
								Main.instance.getSaveConfig().save(Main.instance.savef);
							} catch (IOException e) {
								e.printStackTrace();
							}
							int newpoints = Main.instance.getSaveConfig().getInt(killer2.getName() + ".play.points");
							killer2.sendMessage(Main.instance.getConfig().getString("message_player_get_points")
									.replace("%entity%", event.getEntity().toString()).replace("%points%", "5")
									.replace("%newpoints%", Integer.toString(newpoints)));
							if (Main.isVault == true && oldpoints % 100 != 0 && newpoints % 100 == 0) {
								killer2.sendMessage(Main.instance.getConfig().getString("message_get_vault_money").replace("%money%", "10"));
								Main.econ.depositPlayer(killer2, 10.00);
							}
						}
					}
				}
			}
		}
	}
}
