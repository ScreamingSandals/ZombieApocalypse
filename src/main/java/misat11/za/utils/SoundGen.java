
package misat11.za.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class SoundGen {

  public static Sound get(String v18, String v19) {
    Sound finalSound = null;

    try {
      if (Bukkit.getVersion().contains("1.8")) {
        finalSound = Sound.valueOf(v18);
      } else {
        finalSound = Sound.valueOf(v19);
      }
    } catch (Exception ex) {
    	
    }

    return finalSound;
  }

}