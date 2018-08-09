package misat11.za.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundGen {

	public static void play(String sound, Player player) {
		try {
			Sound finalSound = Sound.valueOf(sound);

			if (finalSound != null) {
				player.playSound(player.getLocation(), finalSound, 1, 1);
			}
		} catch (Exception ex) {

		}
	}

}