package misat11.za.utils;

import misat11.za.Main;
import org.bukkit.entity.Player;

public class Title {
	public static void send(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		if(Main.s_version.startsWith("v1_8")){
			//TODO
		} else if (Main.s_version.startsWith("v1_9") || Main.s_version.startsWith("v1_10")){
			p.sendTitle(title, subtitle);
		} else {
			p.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
		}
	}
}
