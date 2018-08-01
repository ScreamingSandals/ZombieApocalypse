package misat11.za.utils;

import org.bukkit.entity.Player;

public class Title {
	public static void send(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		p.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
	}
}
