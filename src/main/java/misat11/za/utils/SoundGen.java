
package misat11.za.utils;

import org.bukkit.Sound;

public class SoundGen {

  public static Sound get(String name) {
    Sound finalSound = null;

    try {
        finalSound = Sound.valueOf(name);
    } catch (Exception ex) {
    	
    }

    return finalSound;
  }

}